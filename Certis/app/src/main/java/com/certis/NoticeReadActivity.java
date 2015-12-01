package com.certis;

import android.os.Bundle;
import android.widget.TextView;

public class NoticeReadActivity extends NavigationDrawerActivity {
    private TextView tvTitle;
    private TextView tvEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notice_read);
        getLayoutInflater().inflate(R.layout.activity_notice_read, main_frame);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvEntity = (TextView) findViewById(R.id.tvEntity);

        String title = getIntent().getStringExtra("noticeTitle");

        tvTitle.setText(title);

    }
}
