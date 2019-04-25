package com.example.zhggj;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;



public class MainActivity extends AppCompatActivity {

    private String URL = "http://zh.qx121.com/ADAPP/";
    private FrameLayout mFrameLayout;
    private MyWebChromeClient mMyWebChromeClient;
    private WebView mWebView;

    private void initWebView() {
        WebSettings localWebSettings = this.mWebView.getSettings();
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        localWebSettings.setPluginState(WebSettings.PluginState.ON);
        localWebSettings.setAllowFileAccess(true);
        localWebSettings.setLoadWithOverviewMode(true);
        localWebSettings.setUseWideViewPort(true);
        localWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        localWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mMyWebChromeClient = new MyWebChromeClient();
        mWebView.setWebChromeClient(this.mMyWebChromeClient);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString) {
                super.onPageFinished(paramAnonymousWebView, paramAnonymousString);
            }

            public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString) {
                paramAnonymousWebView.loadUrl(paramAnonymousString);
                return true;
            }
        });
    }

    private boolean openApp(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return false;
        }
        try {
            if (paramString.endsWith("pdf")) {
                return false;
            }
            if ((!paramString.startsWith("http")) || (!paramString.startsWith("https")) || (!paramString.startsWith("ftp"))) {
                Uri uri = Uri.parse(paramString);
                String str1 = uri.getHost();
                String str2 = uri.getScheme();
                if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)) && (!str2.equals("http")) && (!str2.equals("https"))) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
        }
        return false;
    }

    public void onBackPressed() {
        if (this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

//    public void onConfigurationChanged(Configuration paramConfiguration) {
//        super.onConfigurationChanged(paramConfiguration);
//        switch (paramConfiguration.orientation) {
//            default:
//                return;
//            case 2:
//                getWindow().clearFlags(2048);
//                getWindow().addFlags(1024);
//                return;
//        }
//        getWindow().clearFlags(1024);
//        getWindow().addFlags(2048);
//    }

    protected void onCreate(Bundle paramBundle) {
        requestWindowFeature(1);
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        getIntent().getData();
        mFrameLayout = findViewById(R.id.fragment);
        mWebView = ((WebView) findViewById(R.id.webview));
        mWebView.getSettings().setJavaScriptEnabled(true);
        initWebView();
        mWebView.setWebViewClient(new CommentWebViewClient());
        mWebView.loadUrl(this.URL);
    }

    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    public class CommentWebViewClient
            extends WebViewClient {
        public CommentWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            return openApp(paramString);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;

        private MyWebChromeClient() {
        }

        public void onHideCustomView() {
            mWebView.setVisibility(View.VISIBLE);
            if (this.mCustomView == null) {
                return;
            }
            this.mCustomView.setVisibility(View.GONE);
            mFrameLayout.removeView(this.mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            super.onShowCustomView(paramView, paramCustomViewCallback);
            if (this.mCustomView != null) {
                paramCustomViewCallback.onCustomViewHidden();
                return;
            }
            this.mCustomView = paramView;
            mFrameLayout.addView(this.mCustomView);
            this.mCustomViewCallback = paramCustomViewCallback;
            mWebView.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}