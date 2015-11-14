package com.certis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.certis.sqlite.DBHelper;

public class SplashActivity extends AppCompatActivity {
    private Handler handler;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();

        dbHelper = new DBHelper(getApplicationContext());

        /**
         * handler.postDelayed(r, d) : d 시간 후에 Runnable 메서드를 수행한다.
         */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dbHelper.dbSelect() == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Log.v("SplashActivity", "Token을 찾을 수 없습니다.");
                }

                finish();
            }
        }, 3000);
    }
}
