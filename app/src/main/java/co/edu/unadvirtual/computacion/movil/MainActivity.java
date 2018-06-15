package co.edu.unadvirtual.computacion.movil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.edu.unadvirtual.computacion.movil.common.ListTopicsActivity;
import co.edu.unadvirtual.computacion.movil.common.Utilities;
import co.edu.unadvirtual.computacion.movil.common.Session;
import co.edu.unadvirtual.computacion.movil.domain.Unit;
import co.edu.unadvirtual.computacion.movil.iam.EditProfileActivity;
import co.edu.unadvirtual.computacion.movil.iam.LoginActivity;
import co.edu.unadvirtual.computacion.movil.unadroid.EvaluationListActivity;
import co.edu.unadvirtual.computacion.movil.videos.VideosActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressBar = findViewById(R.id.progressBarUnits);
        callUnitList();
    }

    private void signOut() {
        Session.logOut(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

//        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//        homeIntent.addCategory(Intent.CATEGORY_HOME);
//        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(homeIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_unit_1) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_videos) {
            Intent intent = new Intent(MainActivity.this, VideosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_quiz) {
            Intent intent = new Intent(MainActivity.this, EvaluationListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_campus) {
            Intent intent = new Intent(this, WebCampusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            signOut();
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void callUnitList() {
        if (Utilities.connectionExist(getApplicationContext())) {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/units",
                    null,
                    this::requestOk,
                    this::requestError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } else {
            Intent intent = new Intent(MainActivity.this, ConnectionErrorActivity.class);
            intent.putExtra("CONN_INTENT_CLASS", MainActivity.this.getClass().getName());
            startActivity(intent);
        }
    }

    /**
     * Realiza una llamada al servidor para listar las unidades disponibles
     *
     * @param response el resultado de la consulta.
     */
    private void requestOk(JSONArray response) {
        try {

            progressBar.setVisibility(View.GONE);
            if (!response.isNull(0)) {
                drawUnits(Unit.fromJSON(response));
            } else {
                Toast.makeText(getApplicationContext(), "No hay Unidades disponibles", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawUnits(List<Unit> units) {
        /*se agrgan las unidades de la base de datos*/
        LinearLayout units_container = findViewById(R.id.units_container);
        LayoutParams cardParams;
        FrameLayout.LayoutParams relativeLayoutParams;
        RelativeLayout.LayoutParams imageParams;
        RelativeLayout.LayoutParams mainTextParams;
        RelativeLayout.LayoutParams subTextParams;
        CardView card;
        TextView mainText;
        TextView subText;
        RelativeLayout relativeLayout;
        ImageView imageView;
        int iconId = 0;

        for (Unit unit : units) {
            cardParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            relativeLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mainTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            subTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout = new RelativeLayout(this);
            cardParams.setMargins(25, 25, 25, 25);

            card = new CardView(getApplicationContext());
            card.setLayoutParams(cardParams);
            card.setPadding(10, 10, 10, 10);
            card.setId(unit.getId() + 6000);
            card.setCardBackgroundColor(Color.WHITE);
            card.setRadius(7);
            card.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, ListTopicsActivity.class);
                intent.putExtra("MAINACTIVITY_PARAMS_UNIT_ID",unit.getId());
                startActivity(intent);
            });

            relativeLayout.setLayoutParams(relativeLayoutParams);
            //relativeLayout.setPadding(getDpUnit(16), getDpUnit(16), getDpUnit(16), getDpUnit(16));
            imageView = new ImageView(this);
            imageView.setId(unit.getId() + 7000);

            try{
                iconId = getResources().getIdentifier(unit.getIconName(), "drawable", getPackageName());
            }catch(Exception ie){

            }finally {
                if(iconId==0){
                    iconId = getResources().getIdentifier("unit_def_icon", "drawable", getPackageName());
                }
            }
            imageView.setImageResource(iconId);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            imageParams.setMargins(getDpUnit(16), getDpUnit(16), getDpUnit(16), 0);
            imageView.setLayoutParams(imageParams);
            imageView.getLayoutParams().width = getDpUnit(50);
            imageView.getLayoutParams().height = getDpUnit(50);
            relativeLayout.addView(imageView, 0);

            mainText = new TextView(this);
            mainText.setId(unit.getId() + 8000);
            mainTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            mainTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
            mainTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            mainTextParams.setMargins(5, 25, 0, 0);
            mainText.setLayoutParams(mainTextParams);
            mainText.setText(unit.getName());
            mainText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            mainText.setTextColor(getResources().getColor(R.color.colorPrimary));
            relativeLayout.addView(mainText, 1);

            subText = new TextView(this);
            subText.setId(unit.getId() + 9000);
            subTextParams.addRule(RelativeLayout.BELOW, mainText.getId());
            subTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
            subTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            subTextParams.setMargins(5,5,0, 48);
            subText.setLayoutParams(subTextParams);
            subText.setText(unit.getDescription());
            subText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            subText.setTextColor(getResources().getColor(R.color.colorAccent));
            relativeLayout.addView(subText, 2);

            card.addView(relativeLayout);
            // Finally, add the CardView in root layout
            units_container.addView(card);
        }
    }

    private void requestError(VolleyError volleyError) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this.getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    private int getDpUnit(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }
}
