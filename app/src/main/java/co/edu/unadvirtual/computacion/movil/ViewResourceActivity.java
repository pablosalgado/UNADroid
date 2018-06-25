package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.barteksc.pdfviewer.PDFView;
import java.io.InputStream;
import java.net.URL;

public class ViewResourceActivity extends AppCompatActivity {
    private PDFView pdfView;
    private int unit_id = 0;
    private int topic_id = 0;
    private int resource_type = 0;
    private String resource_url = "https://www.unad.edu.co/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resource);

        unit_id = getIntent().getIntExtra("ACTIVITY_PARAMS_UNIT_ID",0);
        topic_id = getIntent().getIntExtra("ACTIVITY_PARAMS_TOPIC_ID",0);
        resource_url = getIntent().getStringExtra("ACTIVITY_PARAMS_URL");
        resource_type = getIntent().getIntExtra("ACTIVITY_PARAMS_RESOURCE_TYPE",3);
        WebView webView = findViewById(R.id.resource_webview);
        if(resource_type ==1){
            resource_url = "https://www.youtube.com/embed/" + resource_url + "?autoplay=1&vq=small";
        }else if(resource_type ==2){
            pdfView = findViewById(R.id.pdfView);
            pdfView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            renderPdf(resource_url);

        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(resource_url);
        /*webView.setWebChromeClient(new WebChromeClient(){
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
*/
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void renderPdf(String url){
        try {
            Thread thread = new Thread(() -> {
                try  {
                    InputStream input = new URL(url).openStream();
                    pdfView.fromStream(input).load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void callStander(View view){
        Intent intent = new Intent(ViewResourceActivity.this, ListResourcesActivity.class);
        intent.putExtra("ACTIVITY_PARAMS_UNIT_ID",unit_id);
        intent.putExtra("ACTIVITY_PARAMS_TOPIC_ID",topic_id);
        startActivity(intent);
    }
}
