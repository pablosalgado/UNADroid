package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import co.edu.unadvirtual.computacion.movil.common.StandarActivity;

public class ViewResourceActivity extends AppCompatActivity {

    private int unit_id = 0;
    private int topic_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resource);

        unit_id = getIntent().getIntExtra("unit_id",0);
        topic_id = getIntent().getIntExtra("topic_id",0);

        Toast.makeText(getApplicationContext(), "Unit: "+unit_id+" Topic: "+topic_id, Toast.LENGTH_LONG).show();
        WebView webView = findViewById(R.id.resource_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.loadUrl(getString(R.string.campus_url));

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                getWindow().setTitle(title); //Set Activity tile to page title.
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    public void callStander(View view){
        Intent intent = new Intent(ViewResourceActivity.this, StandarActivity.class);
        intent.putExtra("unit_id",unit_id);
        intent.putExtra("topic_id",topic_id);
        startActivity(intent);
    }
}
