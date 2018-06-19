package co.edu.unadvirtual.computacion.movil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ViewResourceActivity extends AppCompatActivity {

    private int unit_id = 0;
    private int topic_id = 0;
    private int resource_type = 0;
    private String resource_url = "https://www.unad.edu.co/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resource);

        unit_id = getIntent().getIntExtra("ACTIVITY_PARAMS_UNIT_ID", 0);
        topic_id = getIntent().getIntExtra("ACTIVITY_PARAMS_TOPIC_ID", 0);
        resource_url = getIntent().getStringExtra("ACTIVITY_PARAMS_URL");
        resource_type = getIntent().getIntExtra("ACTIVITY_PARAMS_RESOURCE_TYPE", 3);

        if (resource_type == 1) {
            playVideo(resource_url);
        } else {
            //Toast.makeText(getApplicationContext(), resource_url, Toast.LENGTH_LONG).show();
            WebView webView = findViewById(R.id.resource_webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setGeolocationEnabled(true);
            webView.loadUrl(resource_url);
            WebSettings webSettings = webView.getSettings();
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient() {
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
    }

    private void playVideo(String resource_url) {
        try {
            // Se intenta cargar el video en la app de YouTube, si está instalada
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + resource_url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Si la aplicación no existe, se carga directamente en el navegador
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + resource_url));
            startActivity(intent);
        }
    }

    public void callStander(View view) {
        Intent intent = new Intent(ViewResourceActivity.this, ListResourcesActivity.class);
        intent.putExtra("ACTIVITY_PARAMS_UNIT_ID", unit_id);
        intent.putExtra("ACTIVITY_PARAMS_TOPIC_ID", topic_id);
        startActivity(intent);
    }
}
