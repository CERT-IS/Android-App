package com.certis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class LinkActivity extends NavigationDrawerActivity {

    ImageButton linkWriteBtn;
    /** Called when the activity is first created. */
    private ArrayList<Custom_List_Data> Array_Data;
    private Custom_List_Data data;
    private Custom_List_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_link);
        getLayoutInflater().inflate(R.layout.activity_link, main_frame);

        // 링크 글쓰기로 이동
        linkWriteBtn = (ImageButton)findViewById(R.id.LinkWriteBtn);

        linkWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LinkActivity.this, LinkWriteActivity.class);
                startActivity(intent);
            }
        });


        ////////////////////////////////////////////////////////////
        Array_Data = new ArrayList<Custom_List_Data>();

        data = new Custom_List_Data(R.drawable.naver, "네이버",
                "http://www.naver.com","지식인에게 물어보세요");
        Array_Data.add(data);

        data = new Custom_List_Data(R.drawable.daum, "다음",
                "http://www.daum.net", "옛날이름은 한메일");
        Array_Data.add(data);

        data = new Custom_List_Data(R.drawable.google, "구글",
                "http://www.google.com", "구글링이 짱");
        Array_Data.add(data);

        ListView custom_list = (ListView) findViewById(R.id.ListView);

        adapter = new Custom_List_Adapter(this, android.R.layout.simple_list_item_1, Array_Data);
        custom_list.setAdapter(adapter);

    }
}
