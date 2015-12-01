package com.certis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.certis.link.CardAdapter;
import com.certis.link.Config;
import com.certis.link.ItemClickSupport;
import com.certis.link.SiteLink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LinkActivity extends NavigationDrawerActivity {
    private TextView toolbar_title;

    //Creating a List of superheroes
    private List<SiteLink> listSiteMap;
    private HashMap<Integer, String> siteUrl;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    ImageButton siteWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_link, main_frame);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.sub_link_board));

        // Write Schedule
        siteWrite = (ImageButton) findViewById(R.id.siteWrite);
        siteWrite.bringToFront();

        siteWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LinkActivity.this, LinkWriteActivity.class);
                startActivity(intent);
            }
        });

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing list
        listSiteMap = new ArrayList<>();
        siteUrl = new HashMap<>();

        //Calling method to get data
        getData();

        //Item Click Event
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent mintent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl.get(position)));
                startActivity(mintent);
            }
        });
    }

    //This method will get data from the web api
    private void getData(){
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();

                        //calling method to parse json array
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseData(JSONArray array){
        for (int i = 0; i < array.length(); i++) {
            SiteLink itemObj = new SiteLink();
            JSONObject json = null;

            try {
                json = array.getJSONObject(i);
                itemObj.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                itemObj.setTitle(json.getString(Config.TAG_TITLE));
                itemObj.setUrls(json.getString(Config.TAG_URLS));
                //url list
                siteUrl.put(i, json.getString(Config.TAG_URLS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listSiteMap.add(itemObj);
        }

        //Finally initializing our adapter
        adapter = new CardAdapter(listSiteMap, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}

