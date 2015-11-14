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
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.certis.asynctask.TokenLoginThread;
import com.certis.sqlite.DBHelper;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    public String certis_url [];

    private DBHelper dbHelper;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // 스플래시 액티비티를 먼저 실행한다.
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));

        mContext = this;

        dbHelper = new DBHelper(getApplication());

        certis_url = new String[]{getString(R.string.certis_api_url), getString(R.string.certis_test_url)};

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
                new TokenLoginThread(getApplicationContext()).execute();
            } else {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplication(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
