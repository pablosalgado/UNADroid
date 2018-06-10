package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import co.edu.unadvirtual.computacion.movil.common.Utilities;
import co.edu.unadvirtual.computacion.movil.iam.LoginActivity;

public class ConnectionErrorActivity extends AppCompatActivity {


    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_error);
        progressBar = findViewById(R.id.progressBarCon);
    }

    public void checkConnection(View view) {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent;
                        if (Utilities.connectionExist(getApplicationContext())) {
                            /*
                            * Se verifica si se ha suministrado una activity a la cual redirigir.
                            * Se debe proporcionar la ruta completa por ejemplo co.edu.unadvirtual.computacion.movil.iam.LoginActivity
                            * Si no se suministra una actividad se toma LoginActivity.class por defecto
                            * */
                            String dynamicClass = getIntent().getStringExtra("CONN_INTENT_CLASS");
                            Class<?> activityClass;
                            if (dynamicClass != null) {
                                try {
                                    activityClass = Class.forName(dynamicClass);
                                } catch (ClassNotFoundException cne) {
                                    cne.printStackTrace();
                                    activityClass = LoginActivity.class;
                                }
                            } else {
                                activityClass = LoginActivity.class;
                            }

                            /*Se lanza la intent a la activity suministrada  o  a la activity por defecto ( LoginActivity.class)*/
                            intent = new Intent(ConnectionErrorActivity.this, activityClass);
                            ConnectionErrorActivity.this.startActivity(intent);
                            ConnectionErrorActivity.this.finish();

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ConnectionErrorActivity.this.getApplicationContext(), "Sin acceso a internet!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                2000
        );
    }
}
