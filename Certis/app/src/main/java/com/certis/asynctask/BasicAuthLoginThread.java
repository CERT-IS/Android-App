package com.certis.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.certis.LoginActivity;
import com.certis.MainActivity;
import com.certis.sqlite.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by GreenUser on 2015-11-10.
 */
public class BasicAuthLoginThread extends AsyncTask<Integer, Void, Integer> {
    private Context mContext;
    private DBHelper dbHelper;

    private String uId = LoginActivity.uId;
    private String uPw = LoginActivity.uPw;
    private String localToken;

    private ProgressDialog dialog;

    public BasicAuthLoginThread(Context context) {
        mContext = context;
        dbHelper = new DBHelper(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(LoginActivity.mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("로그인 중입니다.");
        dialog.setMessage("잠시만 기다려주세요...");
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferReader = null;
        StringBuilder strBuilder = null; // string과 비슷하고 다양한 문자열 추가 및 변경이 가능.

        try {
            url = new URL(((MainActivity) MainActivity.mContext).certis_url[0]);

            conn = (HttpURLConnection) url.openConnection();

            String authString = uId + ":" + uPw;

            byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.NO_WRAP);
            String authStringEnc = new String(authEncBytes);

            conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            int responseCode = conn.getResponseCode(); // 서버의 응답코드를 받음.

            conn.connect(); // 서버 연결 시도

            if(responseCode == HttpURLConnection.HTTP_OK) { // 응답코드가 '200' 이면 아래 코드 실행 후 엔티티 본문 리턴
                inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                bufferReader = new BufferedReader(inputStreamReader);
                strBuilder = new StringBuilder();
                String str;

                while ((str = bufferReader.readLine()) != null) {
                    strBuilder.append(str);
                }

                String json = strBuilder.toString();
                JSONObject jsonObject = new JSONObject(json);

                String token = jsonObject.getString("authentication_token");

                    /* localToken은 처음 asynctask 동작 시 안에서는 동작하지만 onCreate 안에서는
                    선언 시 null을 반환하고 2번째부터 Asynctask 동작시
                    onCreate 안에서 선언 시 동작한다. */
                localToken = token;

                bufferReader.close();
                inputStreamReader.close();
            }
            conn.disconnect();

            return responseCode;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Integer res) {
        super.onPostExecute(res);

        if (res == 200) {
            if (dbHelper.dbSelect() != null) {
                dbHelper.dbUpdate(localToken);
            } else {
                dbHelper.dbInsert(localToken);
            }

            new TokenLoginThread(LoginActivity.mContext).execute();
        } else if (res == 401) {
            Toast.makeText(LoginActivity.mContext, "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            Log.v("BasicAuthLoginThread", String.valueOf(res));
        } else {
            Log.v("BasicAuthLoginThread", String.valueOf(res));
        }

        dialog.dismiss();
    }
}
