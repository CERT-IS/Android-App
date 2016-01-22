package com.certis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.certis.notice.Notice;
import com.certis.notice.NoticeListAdapter;
import com.certis.scroll.EndlessScrollListener;
import com.certis.sqlite.DBHelper;
import com.certis.volley.AppController;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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
    private ProgressBar progressBar;
    private NoticeListAdapter adapter;
    private DBHelper dbHelper;

    private FloatingActionButton fab_writing;
    private FloatingActionButton fab_search_property;

    private int total_page;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notice);
        getLayoutInflater().inflate(R.layout.activity_notice, main_frame);
        mContext = this;

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.sub_notice));

        final FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.fam);
        fam.setClosedOnTouchOutside(true);

        fab_writing = (FloatingActionButton) findViewById(R.id.fab_writing);
        fab_search_property = (FloatingActionButton) findViewById(R.id.fab_search_property);

        fab_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
                startActivity(intent);
            }
        });

        fab_search_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "글찾기 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        dbHelper = new DBHelper(this);

        handler = new Handler();

        final View footer = getLayoutInflater().inflate(R.layout.progress_bar_footer, null);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        lv = (ListView) findViewById(R.id.lv);
        adapter = new NoticeListAdapter(this, noticeList);
        lv.setAdapter(adapter);
        lv.addFooterView(footer);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

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

                                if (i == 0) {
                                    total_page = obj.getInt("total_page");
                                } else {
                                    Notice notice = new Notice();
                                    notice.setTitle(obj.getString("title"));
                                    notice.setThumbnailUrl(obj.getString("image"));
                                    notice.setUid(obj.getString("uid"));
                                    notice.setCreated_at(obj.getString("created_at"));
                                    notice.setId(obj.getInt("id"));

                                    // adding notice to notices array
                                    noticeList.add(notice);
                                }
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

        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                handler.postDelayed(loadMoreDataRequest(page), 3000);

                if (page == total_page) {
                    lv.removeFooterView(footer);
                }
                return true;
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // view.findViewById()에서 view(클릭한 view)를 생략하면 값이 잘못나온다.
                TextView noticeListId = (TextView) view.findViewById(R.id.noticeListId);

                Intent intent = new Intent(getApplicationContext(), NoticeListReadingActivity.class);
                intent.putExtra("offset", noticeListId.getText().toString());
                startActivity(intent);
            }
        });
    }

    // Append more data into the adapter
    public Runnable loadMoreDataRequest(final int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        Runnable runnable = new Runnable() {
            @Override
            public synchronized void run() {
                // Creating volley request obj
                JsonArrayRequest noticeReq = new JsonArrayRequest(getString(R.string.certis_test_add_request_url) + offset,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                hidePDialog();

                                // 첫 번째 요청(인덱스(0))은 페이지 관련 데이터를 응답 받아서 제외
                                for (int i = 1; i < response.length(); i++) {
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
        };

        return runnable;
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