package com.certis.asynctask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.certis.IndexActivity;
import com.certis.LoginActivity;
import com.certis.MainActivity;
import com.certis.sqlite.DBHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by GreenUser on 2015-11-07.
 */
public class TokenLoginThread extends AsyncTask<Integer, Void, Integer> {
    private Intent intent;
    private Context mContext;
    private DBHelper dbHelper;

    public TokenLoginThread(Context context) {
        mContext = context;
        dbHelper = new DBHelper(mContext);
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        URL url;
        HttpURLConnection conn;

        try {
            url = new URL(((MainActivity) MainActivity.mContext).certis_url[1]);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 저장한 토큰 값으로 서버에 요청!!
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Token " + "token=" + dbHelper.dbSelect());
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            int responseCode = conn.getResponseCode();

            conn.connect();

            conn.disconnect();

            return responseCode;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onPostExecute(Integer res) {
        super.onPostExecute(res);

        if (res == 200) {
            intent = new Intent(MainActivity.mContext, IndexActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            (MainActivity.mContext).startActivity(intent);
            Toast.makeText(MainActivity.mContext, "로그인 성공", Toast.LENGTH_SHORT).show();
        } else if (res == 401) {
            Log.v("TokenLoginThread", "토큰 인증 오류(401)");
            intent = new Intent(MainActivity.mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            (MainActivity.mContext).startActivity(intent);
        } else {
            Log.v("TokenLoginThread", String.valueOf(res));
            intent = new Intent(MainActivity.mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            (MainActivity.mContext).startActivity(intent);
        }
    }
}
