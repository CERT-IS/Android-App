package com.certis.login;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.certis.sqlite.DBHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 클라이언트가 Token 인증을 위한 volley 요청
 * Implementing a Custom Request
 */
public class TokenLogin extends Request<Integer> {
    private Context context;
    private DBHelper dbHelper;

    Response.Listener listener;
    private Map<String, String> headers = new HashMap<>();

    public TokenLogin(int method, String url, Response.Listener listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, errorListener);

        this.listener = listener;
        this.context = context;

        dbHelper = new DBHelper(this.context);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Token" + " " + "token=" + dbHelper.dbSelect());
        return headers;
    }

    /**
     * Subclasses must implement this to parse the raw network response and return an appropriate response type.
     * This method will be called from a worker thread. The response will not be delivered if you return null.
     *
     * @param networkResponse Response from the network
     * @return The parsed response, or null in the case of an error
     */
    @Override
    protected Response<Integer> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(networkResponse.statusCode, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * Subclasses must implement this to perform delivery of the parsed response to their listeners.
     * The given response is guaranteed to be non-null; responses that fail to parse are not delivered.
     *
     * @param response The parsed response returned by parseNetworkResponse(NetworkResponse)
     */
    @Override
    protected void deliverResponse(Integer response) {
//        Intent intent = new Intent(MainActivity.mContext, IndexActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        (MainActivity.mContext).startActivity(intent);

        listener.onResponse(response);
    }

//    @Override
//    public void deliverError(VolleyError error) {
//        Log.v("TokenLoginError", String.valueOf(error.networkResponse.statusCode));
//    }
}
