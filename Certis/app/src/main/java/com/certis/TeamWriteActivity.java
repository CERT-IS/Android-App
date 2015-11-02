package com.certis;

import android.os.Bundle;

public class TeamWriteActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_link_write);
        getLayoutInflater().inflate(R.layout.activity_teamproject_write, main_frame);
    }
}
