package co.edu.unadvirtual.computacion.movil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import co.edu.unadvirtual.computacion.movil.domain.Unit;
import co.edu.unadvirtual.computacion.movil.iam.EditProfileActivity;
import co.edu.unadvirtual.computacion.movil.videos.VideosActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardView cv_unidad_1 = findViewById(R.id.cv_unidad_1);

        cv_unidad_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Ir a al contenido de la unidad 1", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, ListTopicsActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        callUnitList();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        } else if (id == R.id.nav_campus) {
            Intent intent = new Intent(this, WebCampusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void meet(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void callLoader(View view) {
        Intent intent = new Intent(this, LoaderActivity.class);
        startActivity(intent);

    }


    private void callUnitList() {

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                AppSingleton.UNADROID_SERVER_ENDPOINT + "/units",
                null,
                this::requestOk,
                this::requestError
        );

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
    }

    /**
     * Procesa la respuesta del servidor si el usuario se ha podido registrar exitosamente.
     *
     * @param response Contiene el usuario recién creado.
     */
    private void requestOk(JSONArray response) {
        try {
            drawUnits(Unit.fromJSON(response));

//            if (!response.isNull(0)) {
//
//                drawUnits(response);
//
//            } else {
//                Toast.makeText(getApplicationContext(), "No Units enabled", Toast.LENGTH_LONG).show();
//            }
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
                startActivity(intent);
            });

            relativeLayout.setLayoutParams(relativeLayoutParams);
            //relativeLayout.setPadding(getDpUnit(16), getDpUnit(16), getDpUnit(16), getDpUnit(16));
            imageView = new ImageView(this);
            imageView.setId(unit.getId() + 7000);
            imageView.setImageResource(getResources().getIdentifier(unit.getIconName(), "drawable", getPackageName()));
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
            mainTextParams.setMargins(15, 25, 0, 0);
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
            subTextParams.setMargins(getDpUnit(1), getDpUnit(1), getDpUnit(1), 25);
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
        Log.d("Arsensys - >", volleyError.getMessage());
        Toast.makeText(this.getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    @SuppressLint("ResourceType")
    private void drawUnits(JSONArray response) {
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
        JSONObject jsonobject;
        String unitName;
        String unitDescr;
        String unitIcon;
        RelativeLayout relativeLayout;
        ImageView imageView;
        try {


            for (int i = 0; i < response.length(); i++) {

                cardParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                relativeLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mainTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                subTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                relativeLayout = new RelativeLayout(this);
                jsonobject = response.getJSONObject(i);
                unitName = jsonobject.getString("name");
                unitDescr = jsonobject.getString("description");
                unitIcon = jsonobject.getString("iconName");
                card = new CardView(getApplicationContext());

                cardParams.setMargins(25, 25, 25, 25);
                card.setLayoutParams(cardParams);
                card.setPadding(10, 10, 10, 10);
                card.setId(i + 6000);
                card.setCardBackgroundColor(Color.WHITE);
                card.setRadius(7);
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ListTopicsActivity.class);
                        startActivity(intent);
                    }
                });

                relativeLayout.setLayoutParams(relativeLayoutParams);
                //relativeLayout.setPadding(getDpUnit(16), getDpUnit(16), getDpUnit(16), getDpUnit(16));
                imageView = new ImageView(this);
                imageView.setId(i + 7000);
                imageView.setImageResource(getResources().getIdentifier(unitIcon, "drawable", getPackageName()));
                imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                imageParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                imageParams.setMargins(getDpUnit(16), getDpUnit(16), getDpUnit(16), 0);
                imageView.setLayoutParams(imageParams);
                imageView.getLayoutParams().width = getDpUnit(50);
                imageView.getLayoutParams().height = getDpUnit(50);
                relativeLayout.addView(imageView, 0);

                mainText = new TextView(this);
                mainText.setId(i + 8000);
                mainTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                mainTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
                mainTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
                mainTextParams.setMargins(15, 25, 0, 0);
                mainText.setLayoutParams(mainTextParams);
                mainText.setText(unitName);
                mainText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                mainText.setTextColor(getResources().getColor(R.color.colorPrimary));
                relativeLayout.addView(mainText, 1);

                subText = new TextView(this);
                subText.setId(i + 9000);
                subTextParams.addRule(RelativeLayout.BELOW, mainText.getId());
                subTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
                subTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
                subTextParams.setMargins(getDpUnit(1), getDpUnit(1), getDpUnit(1), 25);
                subText.setLayoutParams(subTextParams);
                subText.setText(unitDescr);
                subText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                subText.setTextColor(getResources().getColor(R.color.colorAccent));
                relativeLayout.addView(subText, 2);

                card.addView(relativeLayout);
                // Finally, add the CardView in root layout
                units_container.addView(card);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getDpUnit(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }
}
