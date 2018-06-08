package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import co.edu.unadvirtual.computacion.movil.iam.LoginActivity;

public class LoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                       //Intent intent = new Intent(LoaderActivity.this, LoginActivity.class);
                        Intent intent = new Intent(LoaderActivity.this, MainActivity.class);
                        LoaderActivity.this.startActivity(intent);
                        LoaderActivity.this.finish();
                    }
                },
                3000
        );
    }
}
