package com.certis;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class JSONParse {

    String urls;
    InputStream IS = null;

    public JSONParse() {

    }

    public InputStream JSONParseExecute(String str, List<NameValuePair> pairs) {

        if (str.equals("INSERT")) {
            urls = "http://certis.woobi.co.kr/insert.php";
        } else if (str.equals("SELECT")) {
            urls = "http://certis.woobi.co.kr/select.php";
        }else if(str.equals("MODIFY")) {
            urls = "http://certis.woobi.co.kr/modify.php";
        } else if(str.equals("DELETE")) {
            urls = "http://certis.woobi.co.kr/delete.php";
        }

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urls);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            IS = entity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e("ClientProtocol", "Log_tag");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Log_tag", "IOException");
            e.printStackTrace();
        }

        return IS;
    }
}
