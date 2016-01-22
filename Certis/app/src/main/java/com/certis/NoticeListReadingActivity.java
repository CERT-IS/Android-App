package com.certis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.certis.sqlite.DBHelper;

import java.util.HashMap;
import java.util.Map;

public class NoticeListReadingActivity extends NavigationDrawerActivity {
    // Log tag
    private static final String TAG = NoticeListReadingActivity.class.getSimpleName();

    private DBHelper dbHelper;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notice_list_reading);
        getLayoutInflater().inflate(R.layout.activity_notice_list_reading, main_frame);

        String defaultUrl = (getString(R.string.certis_test_url));
        String offset = getIntent().getStringExtra("offset");

        dbHelper = new DBHelper(this);

        webView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JavaScriptObject(this), "Android");

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl(defaultUrl + "/" + offset, getHeaders());
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
    }

    private class WebChromeClient extends android.webkit.WebChromeClient {
//        @Override
//        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//            new AlertDialog.Builder(NoticeListReadingActivity.this)
//                    .setMessage(message)
//                    .setPositiveButton("확인", new AlertDialog.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            result.confirm();
//                        }
//                    })
//                    .setCancelable(false).create().show();
//
//            return true;
//        }
//
//        @Override
//        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//            new AlertDialog.Builder(NoticeListReadingActivity.this)
//                    .setTitle(message)
//                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            result.confirm();
//                        }
//                    })
//                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            result.cancel();
//                        }
//                    })
//                    .setCancelable(false).create().show();
//            return true;
//        }
//
//        @Override
//        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
//            return true;
//        }
    }

    private Map<String, String> getHeaders() {
        Map<String, String> params = new HashMap<>();
        params.put("Authorization", "Token " + "token=" + dbHelper.dbSelect());
        return params;
    }

    public class JavaScriptObject {
        Context context;

        JavaScriptObject(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void startNoticeList() {
            Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
