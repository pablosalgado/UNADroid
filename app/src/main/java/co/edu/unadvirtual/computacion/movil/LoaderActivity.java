package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        loaderProcess();
    }

    public void loaderProcess(){
        AsyncTask<String,String,String> at = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "ok";
            }

            @Override
            protected void onPostExecute(String s) {
                if ("ok".equals(s)){
                    Intent intent = new Intent(LoaderActivity.this,Main2Activity.class);
                    startActivity(intent);
                }
            }
        };
        at.execute();
    }
}
