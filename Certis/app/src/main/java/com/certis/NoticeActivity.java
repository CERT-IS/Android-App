package com.certis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.certis.notice.Notice;
import com.certis.notice.NoticeListAdapter;
import com.certis.sqlite.DBHelper;
import com.certis.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeActivity extends NavigationDrawerActivity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    public static Context mContext;

    private TextView toolbar_title;

    private ProgressDialog pDialog;
    private List<Notice> noticeList = new ArrayList<>();
    private ListView lv;
    private NoticeListAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notice);
        getLayoutInflater().inflate(R.layout.activity_notice, main_frame);
        mContext = this;

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.sub_notice));

        dbHelper = new DBHelper(this);

        lv = (ListView) findViewById(R.id.lv);
        adapter = new NoticeListAdapter(this, noticeList);
        lv.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        /*// changing action bar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));*/

        // Creating volley request obj
        JsonArrayRequest noticeReq = new JsonArrayRequest(getString(R.string.certis_test_url),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Notice notice = new Notice();
                                notice.setTitle(obj.getString("title"));
                                notice.setThumbnailUrl(obj.getString("image"));
                                notice.setUid(obj.getString("uid"));
                                notice.setCreated_at(obj.getString("created_at"));
                                notice.setId(obj.getInt("id"));

                                // adding notice to notices array
                                noticeList.add(notice);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Token " + "token=" + dbHelper.dbSelect());
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(noticeReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}