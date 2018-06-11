package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import co.edu.unadvirtual.computacion.movil.common.Session;
import co.edu.unadvirtual.computacion.movil.common.Utilities;
import co.edu.unadvirtual.computacion.movil.iam.LoginActivity;

public class LoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        new Handler().postDelayed(() -> {
            Intent intent;

            Boolean session = Session.isLoggedIn(getApplicationContext());
            Boolean connection = Utilities.connectionExist(getApplicationContext());

            if (connection && !session) {
                intent = new Intent(LoaderActivity.this, LoginActivity.class);
            } else if (connection && session) {
                intent = new Intent(LoaderActivity.this, MainActivity.class);
            } else {
                intent = new Intent(LoaderActivity.this, ConnectionErrorActivity.class);
            }

            LoaderActivity.this.startActivity(intent);
            LoaderActivity.this.finish();

        }, 2000);
    }
}
