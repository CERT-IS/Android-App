/**
 * MainActivity.java를 작성하기 앞서 이 앱은 네트워크를 이용하므로 AndroidManifest.xml에 다음과 같은 코드를 추가한다. !!!!!!
 *
 * 네트워크 연결을 위한 INTERNET Permission과 네트워크 연결 상태를 확인하기 위한 ACCESS_NETWORK_STATE Permission을 설정
 *     <uses-permission android:name="android.permission.INTERNET"/>
 *     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 */

package com.example.greenuser.basicauthtest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private EditText edtUrl;
    private Button btnConn;
    private TextView tvRes;

    private EditText edtUrl2;
    private Button btnConn2;
    private TextView tvRes2;

    private LinearLayout layout;

    private static final String uID = "sagalim";
    private static final String uPW = "11112222";

    private String localToken; // basicAuth를 이용한 토큰값을 저장


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUrl = (EditText) findViewById(R.id.edtUrl);
        btnConn = (Button) findViewById(R.id.btnConn);
        tvRes = (TextView) findViewById(R.id.tvRes);

        edtUrl2 = (EditText) findViewById(R.id.edtUrl2);
        btnConn2 = (Button) findViewById(R.id.btnConn2);
        tvRes2 = (TextView) findViewById(R.id.tvRes2);

        layout = (LinearLayout) findViewById(R.id.layout);

        btnConn.setOnClickListener(new View.OnClickListener() {
            /**
             * 네트워크 연결상태 확인
             * ConnectivityManager : 네트워크 연결 상태를 얻기 위한 클래스
             * 1) 네트워크 연결상태 모니터링
             * 2) 네트워크 연결상태가 변경되었을때 intent를 broadcast
             * 3) 현재 네트워크 연결을 잃었을 때 다른 네트워크로 fail over
             * 4) 사용가능한 네트워크 상태를 조회하는 API 제공
             * NetworkInfo : 인자로 주어진 네트워크 인터페이스(WiFi, mobile 등)의 연결상태를 얻기 위한 클래스
             */
            @Override
            public void onClick(View v) {
                // URL을 입력하지 않았을때
                if (!edtUrl.getText().equals("")) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    // 네트워크가 연결되어 있을 때 -> HttpURLConnection
                    if (networkInfo != null && networkInfo.isConnected()) {
                        btnConn.setEnabled(false);
                        new AsyncTaskClass().execute(edtUrl.getText().toString());
                        layout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "URL을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnConn2.setOnClickListener(new View.OnClickListener() {
            /**
             * 네트워크 연결상태 확인
             * ConnectivityManager : 네트워크 연결 상태를 얻기 위한 클래스
             * 1) 네트워크 연결상태 모니터링
             * 2) 네트워크 연결상태가 변경되었을때 intent를 broadcast
             * 3) 현재 네트워크 연결을 잃었을 때 다른 네트워크로 fail over
             * 4) 사용가능한 네트워크 상태를 조회하는 API 제공
             * NetworkInfo : 인자로 주어진 네트워크 인터페이스(WiFi, mobile 등)의 연결상태를 얻기 위한 클래스
             */
            @Override
            public void onClick(View v) {
                // URL을 입력하지 않았을때
                if (!edtUrl2.getText().equals("")) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    // 네트워크가 연결되어 있을 때 -> HttpURLConnection
                    if (networkInfo != null && networkInfo.isConnected()) {
                        btnConn2.setEnabled(false);
                        new AsyncTaskClass2().execute(edtUrl2.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "URL을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class AsyncTaskClass extends AsyncTask<String, Void, String> {

        /**
         * 기본 접근 인증(Basic Authentication)은 아래와 같은 절차로 진행
         * 1단계 : URL 객체 생성 -->> URL url = new URL(urlString);
         * 2단계 : 사용자 아이디와 패스워드를 사용자로부터 얻음.
         * 3단계 : 사용자 아이디와 패스워드로 다음과 같은 문장을 만듬(':' 구분자를 첨부) -->> String userPassword = the Username + ":" + thePassword;
         * 4단계 : 문자열 인코딩(BASE64 클래스 이용) -->> String encondig = Base64.encode(userPassword.getBytes("UTF-8"));
         * 5단계 : URL로부터 URLConnection 클래스의 인스턴스 생성 -->> URLConnection con = url.openConnection();
         * 6단계 : 요청 프로퍼티 생성 -->> con.setRequestProperty("Authorization", "Basic " + encoding);
         */
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            HttpURLConnection conn = null;
//            HttpResponse response = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferReader = null;
            StringBuilder strBuilder = null; // string과 비슷하고 다양한 문자열 추가 및 변경이 가능.

            try {
                url = new URL (params[0]);

                conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("Accept-Language", "ko");

                String authString = uID + ":" + uPW;

                byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.NO_WRAP);
                String authStringEnc = new String(authEncBytes);

                conn.setRequestProperty("Authorization", "Basic " + authStringEnc);

//                conn.setRequestProperty("User-Agent", "test");
//                conn.setRequestProperty("Name", "name");
//                conn.setRequestMethod("POST");
                conn.setRequestMethod("GET");

//                String gMethod = conn.getRequestMethod();
//                String gPath = url.getPath();
//                String gProtocol = url.getProtocol();
//                String gUrl = url.getHost();
//                String gDate = conn.getHeaderField("Date");
//                String gContent = conn.getHeaderField("Content-Type");
//                String gConnection= conn.getHeaderField("Connection");
//
//                String sgUrl = String.valueOf(gUrl);
//
//                String res = (gMethod + " " + gPath + " " + gProtocol + "\n" + gUrl + "\n" + gDate + "\n" + gContent + "\n" + gConnection);

//                Map<String, List<String>> map = conn.getHeaderFields();
//
//                for (Map.Entry<String, List<String>> entry : map.entrySet()){
//                    Log.v("Key : ", entry.getKey());
//                }

                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);

                int status = conn.getResponseCode(); // 서버의 응답코드를 받음.
                String strStatus = String.valueOf(status);

                conn.connect(); // 서버 연결 시도

                if(status == HttpURLConnection.HTTP_OK){ // 응답코드가 '200' 이면 아래 코드 실행 후 엔티티 본문 리턴
                    inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    bufferReader = new BufferedReader(inputStreamReader);
                    strBuilder = new StringBuilder();
                    String str;

                    while((str = bufferReader.readLine()) != null){
                        strBuilder.append(str);
                    }

                    inputStreamReader.close();

                    String json = strBuilder.toString();
                    JSONObject jsonObject = new JSONObject(json);

                    String token = jsonObject.getString("authentication_token");

                    localToken = token;

//                    return strBuilder.toString() + "\n\n" + token; // url 과 token 값 리턴

                    return token;
                } else {
                    return String.valueOf(status); // 응답코드가 '200' 이 아니라면 해당 응답코드를 리턴
                }

//                return strStatus; // onPostExecute(String result) 메서드의 파라미터 result 값이 된다.
//                return res;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e){
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // TextView에 출력
            if (result != null) {
                tvRes.setText(result);
            } else {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
            }
            btnConn.setEnabled(true);
        }
    }

    class AsyncTaskClass2 extends AsyncTask<String, Void, String> {

        /**
         * 기본 접근 인증(Basic Authentication)은 아래와 같은 절차로 진행
         * 1단계 : URL 객체 생성 -->> URL url = new URL(urlString);
         * 2단계 : 사용자 아이디와 패스워드를 사용자로부터 얻음.
         * 3단계 : 사용자 아이디와 패스워드로 다음과 같은 문장을 만듬(':' 구분자를 첨부) -->> String userPassword = the Username + ":" + thePassword;
         * 4단계 : 문자열 인코딩(BASE64 클래스 이용) -->> String encondig = Base64.encode(userPassword.getBytes("UTF-8"));
         * 5단계 : URL로부터 URLConnection 클래스의 인스턴스 생성 -->> URLConnection con = url.openConnection();
         * 6단계 : 요청 프로퍼티 생성 -->> con.setRequestProperty("Authorization", "Basic " + encoding);
         */
        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            HttpURLConnection conn = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferReader = null;
            StringBuilder strBuilder = null; // string과 비슷하고 다양한 문자열 추가 및 변경이 가능.

            try {
                url = new URL (params[0]);

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // 저장한 토큰 값으로 서버에 요청!!
                conn.setRequestProperty("Authorization", "Token " + "token=" + localToken);

                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);

                int status = conn.getResponseCode(); // 서버의 응답코드를 받음.
                String strStatus = String.valueOf(status);

                conn.connect(); // 서버 연결 시도

                if(status == HttpURLConnection.HTTP_OK){ // 응답코드가 '200' 이면 아래 코드 실행 후 엔티티 본문 리턴
                    return strStatus;
                } else {
                    return strStatus; // 응답코드가 '200' 이 아니라면 해당 응답코드를 리턴
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // TextView에 출력
            if (result != null) {
                tvRes2.setText(result);
            } else {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
            }
            btnConn2.setEnabled(true);
        }
    }
}
