package com.certis;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.certis.sqlite.DBHelper;

import java.util.HashMap;
import java.util.Map;

public class NoticeListWritingActivity extends NavigationDrawerActivity {
    // Log tag
    private static final String TAG = NoticeListWritingActivity.class.getSimpleName();

    private DBHelper dbHelper;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notice_write);
        getLayoutInflater().inflate(R.layout.activity_webview, main_frame);

        dbHelper = new DBHelper(this);

        webView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setSaveFormData(false);

        webView.loadUrl("http://www.cert-is.ga/notices/new", getHeaders());

        webView.setWebViewClient(new WebViewClient());
    }

    private Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", "Token " + "token=" + dbHelper.dbSelect());
        return params;
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url, getHeaders());
            Log.v("URL", url + " " + getHeaders().get("Authorization"));
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.v("onPageStarted", url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.v("onLoadResource", url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.v("onPageFinished", url);
        }
    }
}
