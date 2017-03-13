package com.budgetload.materialdesign.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.budgetload.materialdesign.R;

import static com.budgetload.materialdesign.Constant.Constant.DRAGONPAY;
import static com.budgetload.materialdesign.R.id.webview;

public class MyWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);
        WebView myWebView = (WebView) findViewById(webview);

        myWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        Bundle bundle = getIntent().getExtras();
        myWebView.loadUrl(DRAGONPAY + Uri.parse(bundle.getString("MyURl")));

        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Toast.makeText(getBaseContext(), url, Toast.LENGTH_SHORT).show();
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });
    }


}
