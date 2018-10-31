package com.kakao.kakaonavi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class KakaoNaviWebViewActivity extends Activity {
    private final int REQUEST_CODE_FINE_LOCATION = 1022;
    public class KakaoNaviWebViewClient extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && KakaoNaviProtocol.NAVI_MARKET_URL.equals(url)) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                finish();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            if (uri != null && KakaoNaviProtocol.NAVI_MARKET_URL.equals(request.getUrl().toString())) {
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                finish();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    public class KakaoNaviWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_navi_web_view);
        if (isLocationEnabled(this)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE_LOCATION);
                } else {
                    showRationaleAlertDialog();
                }
            } else {
                loadKakaoNaviWebView();
            }
        } else {
            showRationaleAlertDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadKakaoNaviWebView();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadKakaoNaviWebView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(KakaoNaviProtocol.PROPERTY_WEB_URL);
        if (url == null) {
            finish();
        }
        WebView webView = (WebView) findViewById(R.id.navi_web_view);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new KakaoNaviWebViewClient());
        webView.setWebChromeClient(new KakaoNaviWebChromeClient());
    }

    private void showRationaleAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.permission_error_title))
                .setMessage(getResources().getString(R.string.permission_error_message))
                .setNeutralButton(getResources().getString(R.string.permission_error_okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.show();
    }

    @SuppressWarnings("deprecation")
    private boolean isLocationEnabled(final Context context) {
        int locationMode;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}
