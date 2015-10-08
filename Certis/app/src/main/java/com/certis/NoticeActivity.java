package com.certis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoticeActivity extends NavigationDrawerActivity {
    private ListView lv;
    private ArrayAdapter adapter;
    private ArrayList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notice);
        getLayoutInflater().inflate(R.layout.activity_notice, main_frame);

        lv = (ListView) findViewById(R.id.lv);

        list = new ArrayList();

        for (int i = 0; i < 30; i++) {
            list.add(i);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
    }

    public void bottomMenuOnClick(View view) {
        switch (view.getId()) {
            case R.id.bottomMenuWriting :
                Intent intent = new Intent(getApplication(), EditActivity.class);
                startActivity(intent);
                break;

            case R.id.bottomMenuSearchWriting :
                Toast.makeText(getApplication(), "글찾기 클릭", Toast.LENGTH_LONG).show();
                break;

            default :
//                Toast.makeText(getApplication(), "하단 레이아웃 클릭", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
