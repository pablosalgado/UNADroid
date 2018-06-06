package co.edu.unadvirtual.computacion.movil.common;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import co.edu.unadvirtual.computacion.movil.R;


public class ListTopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_topics);

        /*Lista de CardViews para las unidades de la Documentacion*/
        CardView cv_unidad_1 = findViewById(R.id.cv_unidad_1);
        CardView cv_unidad_2 = findViewById(R.id.cv_unidad_2);
        CardView cv_unidad_3 = findViewById(R.id.cv_unidad_3);

        cv_unidad_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ir a al contenido de la unidad 1", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        cv_unidad_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ir a al contenido de la unidad 2", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        cv_unidad_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ir a al contenido de la unidad 3", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }
}
