package com.certis;

import android.os.Bundle;

public class EditActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit);
        getLayoutInflater().inflate(R.layout.activity_edit, main_frame);
    }
}
