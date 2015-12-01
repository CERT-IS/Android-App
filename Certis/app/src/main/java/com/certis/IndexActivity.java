package com.certis;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.certis.sqlite.DBHelper;

import java.util.HashMap;
import java.util.Map;

public class IndexActivity extends NavigationDrawerActivity {
    private TextView tv;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        getLayoutInflater().inflate(R.layout.activity_index, main_frame);

        tv = (TextView) findViewById(R.id.tv);
        dbHelper = new DBHelper(this);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest request= new StringRequest(StringRequest.Method.GET, getString(R.string.certis_test_url),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                tv.setText(Html.fromHtml(s));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        tv.setText(volleyError.getMessage());
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

                requestQueue.add(request);
    }
}