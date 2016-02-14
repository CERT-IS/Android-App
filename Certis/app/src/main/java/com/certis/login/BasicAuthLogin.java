package com.certis.login;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.certis.MainActivity;
import com.certis.sqlite.DBHelper;
import com.certis.volley.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 클라이언트가 BasicAuthorization 인증을 위한 volley 요청
 * Implementing a Custom Request
 */
public class BasicAuthLogin extends Request<String> {
    private Context context;
    private DBHelper dbHelper;

    Response.Listener listener;
    Response.ErrorListener errorListener;
    private Map<String, String> headers = new HashMap<>();

    private User user;

    private String token;

    /**
     *
     * @param method 요청 메서드
     * @param url 요청 url
     * @param user set 내용들을 이 클래스에서 get 메서드를 사용할 수 있게 한다.
     * @param listener 해당 액티비티에 implement 한다.
     * @param errorListener 해당 액티비티에 implement 한다.
     * @param context 해당 액티비티에 context가 필요하다.
     */
    public BasicAuthLogin(int method, String url, User user, Response.Listener listener, Response.ErrorListener errorListener, Context context) {
        super(method, url, errorListener);

        this.user = user;
        this.listener = listener;
        this.context = context;

        dbHelper = new DBHelper(this.context);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError{
        StringBuilder auth = new StringBuilder();
        auth.append(user.getUserID()).append(':').append(user.getUserPW());

        byte[] authEncBytes = Base64.encode(auth.toString().getBytes(), Base64.NO_WRAP);
        String authStrEnc = new String(authEncBytes);

        headers.put("Authorization", "Basic" + " " + authStrEnc);
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
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(new String(networkResponse.data), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            Log.v("BasicAuthException", "기본인증 에러입니다.");
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
    protected void deliverResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            token = jsonObject.getString("authentication_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (dbHelper.dbSelect() != null) {
            dbHelper.dbUpdate(token);
        } else {
            dbHelper.dbInsert(token);
        }

        String url = ((MainActivity) MainActivity.mContext).certis_url[1];

        TokenLogin tokenLogin = new TokenLogin(Method.GET, url, listener, errorListener, this.context);
        AppController.getInstance().addToRequestQueue(tokenLogin);

        // 아래 코드로 해당 액티비티에 Override onResponse 메서드로 보낸다.
//        listener.onResponse(token);
    }

//    @Override
//    public void deliverError(VolleyError error) {
//        super.deliverError(error);
//        Log.v("BasicAuth", String.valueOf(error.networkResponse.statusCode));
//    }
}