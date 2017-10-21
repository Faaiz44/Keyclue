package com.keyclue.android;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.hp.keyclue.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

public class MainActivity extends AppCompatActivity {

    android.webkit.WebView wv;
    // When back pressed go back
    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    // [START declare_analytics]
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String TAG = "MainActivity";
    private Trace mTrace;
    private String STARTUP_TRACE_NAME = "startup_trace";
    private String REQUESTS_COUNTER_NAME = "requests sent";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            // Begin tracing app startup tasks.
            mTrace = FirebasePerformance.getInstance().newTrace(STARTUP_TRACE_NAME);
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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
            // Increment the counter of number of requests sent in the trace.
            Log.d(TAG, "Incrementing number of requests counter in trace");
            mTrace.incrementCounter(REQUESTS_COUNTER_NAME);
            mFirebaseAnalytics.setCurrentScreen(this, "Key Clue Main", null /* class override */);
            mFirebaseAnalytics.setUserProperty("favorite_food", "Key Clue Main Page");
        }
        catch(Exception ex){
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
            FirebaseCrash.report(ex);
        }
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
                                try {
                                    wv.loadUrl("http://keyclue.co.kr/myshop/index.html");
                                }
                                catch(Exception ex){
                                    FirebaseCrash.logcat(Log.ERROR, TAG, "General Exception caught");
                                    FirebaseCrash.report(ex);
                                }
                                Bundle params = new Bundle();
                                params.putString("account", "Account Button Pressed");
                                mFirebaseAnalytics.logEvent("accountbutton", params);
                                break;
                            case R.id.action_backfilled:
                                Bundle backparams = new Bundle();
                                backparams.putString("back", "Back Button Pressed");
                                mFirebaseAnalytics.logEvent("backbutton", backparams);
                                try {
                                    onBackPressed();
                                }
                                catch(Exception ex){
                                    FirebaseCrash.logcat(Log.ERROR, TAG, "General Exception caught");
                                    FirebaseCrash.report(ex);
                                }
                                break;
                            case R.id.action_forwardfilled:
                                Bundle forwardparams = new Bundle();
                                forwardparams.putString("forward", "Forward Button Pressed");
                                mFirebaseAnalytics.logEvent("forwardbutton", forwardparams);
                                try {
                                    onForwardPressed();
                                }
                                catch(Exception ex){
                                FirebaseCrash.logcat(Log.ERROR, TAG, "General Exception caught");
                                FirebaseCrash.report(ex);
                            }
                                break;
                            case R.id.action_shoppingcart:
                                Bundle shcartparams = new Bundle();
                                shcartparams.putString("shcart", "Account Button Pressed");
                                mFirebaseAnalytics.logEvent("shcartbutton", shcartparams);
//                                Uri uricart = Uri.parse("http://keyclue.co.kr/order/basket.html"); // missing 'http://' will cause crashed
//                                Intent intentcart = new Intent(Intent.ACTION_VIEW, uricart);
//                                startActivity(intentcart);
                                try {
                                    wv.loadUrl("http://keyclue.co.kr/order/basket.html");
                                }
                                catch(Exception ex){
                                FirebaseCrash.logcat(Log.ERROR, TAG, "General Exception caught");
                                FirebaseCrash.report(ex);
                            }
                                break;
                        }
                        return false;
                    }
                });
        Log.d(TAG, "Stopping trace");
        mTrace.stop();
    }

    public void onBackPressed(){
        if (wv.canGoBack()) {
            wv.goBack();
        }else {
            super.onBackPressed();
        }
    }

    public void onForwardPressed(){
        if(wv.canGoForward()) {
            wv.goForward();
        }else{
            super.finish();
        }
    }
}
