/**
 * MainActivity.java를 작성하기 앞서 이 앱은 네트워크를 이용하므로 AndroidManifest.xml에 다음과 같은 코드를 추가한다. !!!!!!
 *
 * 네트워크 연결을 위한 INTERNET Permission과 네트워크 연결 상태를 확인하기 위한 ACCESS_NETWORK_STATE Permission을 설정
 *     <uses-permission android:name="android.permission.INTERNET"/>
 *     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 */

package com.certis;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.certis.login.TokenLogin;
import com.certis.sqlite.DBHelper;
import com.certis.volley.AppController;

public class MainActivity extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    public static Context mContext;
    public String certis_url [];

    private Handler handler;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();

        dbHelper = new DBHelper(getApplication());

        mContext = this;

        certis_url = new String[]{getString(R.string.certis_api_url), getString(R.string.certis_test_url)};

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dbHelper.dbSelect() != null) {
                    /**
                     * 네트워크 연결상태 확인
                     * ConnectivityManager : 네트워크 연결 상태를 얻기 위한 클래스
                     * 1) 네트워크 연결상태 모니터링
                     * 2) 네트워크 연결상태가 변경되었을때 intent를 broadcast
                     * 3) 현재 네트워크 연결을 잃었을 때 다른 네트워크로 fail over
                     * 4) 사용가능한 네트워크 상태를 조회하는 API 제공
                     * NetworkInfo : 인자로 주어진 네트워크 인터페이스(WiFi, mobile 등)의 연결상태를 얻기 위한 클래스
                     */
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    // 네트워크가 연결되어 있을 때 -> HttpURLConnection
                    if (networkInfo != null && networkInfo.isConnected()) {
                        TokenLogin tokenLogin =
                                new TokenLogin(Request.Method.GET, certis_url[1], MainActivity.this, MainActivity.this, MainActivity.this);
                        AppController.getInstance().addToRequestQueue(tokenLogin);
                    } else {
                        Toast.makeText(getApplication(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();

                        activityMove(LoginActivity.class);
                    }
                } else {
                    Log.v("MainActivity", "토큰을 찾을 수 없습니다.");

                    activityMove(LoginActivity.class);
                }
            }
        }, 3000);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getApplicationContext(), "에러입니다. : " + String.valueOf(volleyError.networkResponse.statusCode), Toast.LENGTH_SHORT).show();

        activityMove(LoginActivity.class);
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
