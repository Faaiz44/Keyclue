package com.keyclue.android;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.hp.keyclue.R;

public class MainActivity extends AppCompatActivity {

    android.webkit.WebView wv;
    // When back pressed go back


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        wv = (android.webkit.WebView) findViewById(R.id.webview);
        //Enable JavaScript
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setFocusable(true);
        wv.setFocusableInTouchMode(true);
        //Set Render Priority to High
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //Load URL
        wv.loadUrl("http://keyclue.co.kr");
        wv.setWebViewClient(new WebViewClient());

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        BottomNavigationViewHelper helper = new BottomNavigationViewHelper();
        helper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_account:
//                                Uri uri = Uri.parse("http://keyclue.co.kr/myshop/index.html"); // missing 'http://' will cause crashed
//                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                startActivity(intent);
                                wv.loadUrl("http://keyclue.co.kr/myshop/index.html");

                                break;
                            case R.id.action_backfilled:

                                break;
                            case R.id.action_forwardfilled:

                                break;
                            case R.id.action_shoppingcart:
//                                Uri uricart = Uri.parse("http://keyclue.co.kr/order/basket.html"); // missing 'http://' will cause crashed
//                                Intent intentcart = new Intent(Intent.ACTION_VIEW, uricart);
//                                startActivity(intentcart);
                                wv.loadUrl("http://keyclue.co.kr/order/basket.html");
                                break;
                        }
                        return false;
                    }
                });
    }

    public void onBackPressed(){
        if (wv.canGoBack()) {
            wv.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
