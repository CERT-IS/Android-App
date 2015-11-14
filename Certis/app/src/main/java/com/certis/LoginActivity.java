package com.certis;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.certis.asynctask.BasicAuthLoginThread;
import com.certis.sqlite.DBHelper;

public class LoginActivity extends AppCompatActivity {
    public static Context mContext;
    public static String uId;
    public static String uPw;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private EditText edtLoginEmail;
    private EditText edtLoginPw;

    private Button btnSignIn;

    private TextView signUpText;
    private TextView toMain;

    private String certis_url [];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        edtLoginEmail = (EditText) findViewById(R.id.LoginEmail);
        edtLoginPw = (EditText) findViewById(R.id.LoginPw);

        btnSignIn = (Button) findViewById(R.id.SignInBtn);

        certis_url = new String[]{getString(R.string.certis_api_url), getString(R.string.certis_test_url)};

        dbHelper = new DBHelper(getApplicationContext());

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempId = edtLoginEmail.getText().toString();
                String tempPw = edtLoginPw.getText().toString();

                Log.v("LoginActivity tempId", tempId);
                Log.v("LoginActivity tempPw", tempPw);

                if (tempId.equals("") && tempPw.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디와 패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (tempId.equals("") && !tempPw.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (!tempId.equals("") && tempPw.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    // 네트워크가 연결되어 있을 때 -> HttpURLConnection
                    if (networkInfo != null && networkInfo.isConnected()) {
                        uId = tempId;
                        uPw = tempPw;

                        new BasicAuthLoginThread(getApplicationContext()).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //임시 버튼 메인으로 이동
        toMain = (TextView)findViewById(R.id.ForgetPW);
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                startActivity(intent);
            }
        });

        // 회원가입으로 이동
        signUpText = (TextView)findViewById(R.id.SignUpText);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
