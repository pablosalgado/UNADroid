package co.edu.unadvirtual.computacion.movil.common;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.List;

import co.edu.unadvirtual.computacion.movil.AppSingleton;
import co.edu.unadvirtual.computacion.movil.ConnectionErrorActivity;
import co.edu.unadvirtual.computacion.movil.MainActivity;
import co.edu.unadvirtual.computacion.movil.R;
import co.edu.unadvirtual.computacion.movil.domain.Topic;
import co.edu.unadvirtual.computacion.movil.domain.Unit;


public class ListTopicsActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_topics);
        //Intent intent = getIntent();
        //Bundle data = intent.getExtras();
        //int unit_id = data.getInt("MAINACTIVITY_PARAMS_UNIT_ID");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.addDrawerListener(toggle);
        //toggle.syncState();

       // NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        progressBar = findViewById(R.id.progressBarTopics);
        callTopicsList();
    }



    private void callTopicsList() {
        if (Utilities.connectionExist(getApplicationContext())) {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/unit/"+1+"/topics",
                    null,
                    this::requestOk,
                    this::requestError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } else {
            //Intent intent = new Intent(MainActivity.this, ConnectionErrorActivity.class);
            //intent.putExtra("CONN_INTENT_CLASS", MainActivity.this.getClass().getName());
            //startActivity(intent);
        }
    }

    private void requestOk(JSONArray response) {
        try {

            progressBar.setVisibility(View.GONE);
            if (!response.isNull(0)) {
                drawTopics(Topic.fromJSON(response));
            } else {
                Toast.makeText(getApplicationContext(), "No Topics enabled", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestError(VolleyError volleyError) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this.getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }



    private void drawTopics(List<Topic> topics) {

        /*Lista de CardViews para las unidades de la Documentacion*/
        /*CardView cv_unidad_1 = findViewById(R.id.cv_unidad_1);
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
        });*/

    }
}
