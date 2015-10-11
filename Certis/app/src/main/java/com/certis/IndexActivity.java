package com.certis;

import android.os.Bundle;

public class IndexActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        getLayoutInflater().inflate(R.layout.activity_index, main_frame);
    }
}