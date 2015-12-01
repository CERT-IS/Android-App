package com.certis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class NoticeWriteActivity extends AppCompatActivity {
    private EditText edtEntity;
    private LinearLayout llview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);

        edtEntity = (EditText) findViewById(R.id.edtEntity);
        llview = (LinearLayout) findViewById(R.id.llview);

        edtEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                llview.setVisibility(View.VISIBLE);
            }
        });
    }
}
