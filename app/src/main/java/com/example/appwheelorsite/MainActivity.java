package com.example.appwheelorsite;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.onesignal.OneSignal;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {
    private static final String ONESIGNAL_APP_ID = ""; // KeyPush
    private static final String URL = "https://ru.imgbb.com/"; // Link
    public ValueCallback<Uri[]> uploadMessage;

    // save image from gallery and load image in webView
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (uploadMessage == null) {
                    return;
                }
                Intent data = result.getData();
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(Activity.RESULT_OK, data));
                uploadMessage = null;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OneSignal.promptForPushNotifications();

        WebView webView;
        Button openGameButton;
        webView = findViewById(R.id.webView);
        openGameButton = findViewById(R.id.open_Game_Activity);

        if (!URL.equals("")) {
            openGameButton.setVisibility(View.GONE);
            webView.getSettings().setJavaScriptEnabled(true); //? i don't know what this need now
            webView.loadUrl(URL);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient()
            {
                public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
                {
                    if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(null);
                    }
                    uploadMessage = filePathCallback;

                    Intent intent;
                    intent = fileChooserParams.createIntent();
                    try
                    {
                         startForResult.launch(intent);


                    } catch (ActivityNotFoundException e)
                    {
                        uploadMessage = null;
                        return false;
                    }
                    return true;
                }

            });
        }
        else {
            openGameButton = findViewById(R.id.open_Game_Activity);
            openGameButton.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }
    }

    public void openGameActivity(View view){
        Intent intent = new Intent(this, ActivityGame.class);
        startActivity(intent);
    }

}