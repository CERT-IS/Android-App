package com.certis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 스플래시 액티비티를 먼저 실행한다.
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        /**
         * 네비게이션 드로어 액티비티로 이동한다.
         */
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                startActivity(intent);
            }
        });
    }
}
