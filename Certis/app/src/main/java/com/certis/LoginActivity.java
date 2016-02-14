package com.certis;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.certis.login.BasicAuthLogin;
import com.certis.login.User;
import com.certis.volley.AppController;

public class LoginActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private EditText edtUserID;
    private EditText edtUserPW;

    private Button btnSignIn;

    private TextView tvForgetPW;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        edtUserID = (EditText) findViewById(R.id.edtUserID);
        edtUserPW = (EditText) findViewById(R.id.edtUserPW);

        tvForgetPW = (TextView)findViewById(R.id.tvForgetPW);
        tvSignUp = (TextView)findViewById(R.id.tvSignUp);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = edtUserID.getText().toString();
                String userPW = edtUserPW.getText().toString();

                if (TextUtils.isEmpty(userID) && TextUtils.isEmpty(userPW)) {
                    Toast.makeText(getApplicationContext(), "아이디와 패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userID) && !TextUtils.isEmpty(userPW)) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(userID) && TextUtils.isEmpty(userPW)) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 네트워크가 연결되어 있을 때 -> HttpURLConnection
                    if (networkInfo != null && networkInfo.isConnected()) {
                        User user = new User().setUserID(userID).setUserPW(userPW);

                        BasicAuthLogin basicAuthLogin =
                                new BasicAuthLogin(Request.Method.GET, getString(R.string.certis_api_url), user, LoginActivity.this, LoginActivity.this, LoginActivity.this);
                        AppController.getInstance().addToRequestQueue(basicAuthLogin);
                    } else {
                        Toast.makeText(getApplicationContext(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //인덱스 액티비티로 이동
        tvForgetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected()) {
                    activityMove(IndexActivity.class);
                } else {
                    Toast.makeText(getApplicationContext(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 회원가입 액티비티로 이동
        tvSignUp .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected()) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getApplicationContext(), "에러입니다. : " + String.valueOf(volleyError.networkResponse.statusCode), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object response) {
        Toast.makeText(getApplicationContext(), "로그인 성공했습니다.", Toast.LENGTH_SHORT).show();

        activityMove(IndexActivity.class);
    }

    public void activityMove(Class activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
