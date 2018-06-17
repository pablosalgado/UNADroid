package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.List;

import co.edu.unadvirtual.computacion.movil.common.Utilities;
import co.edu.unadvirtual.computacion.movil.domain.Resource;

public class ListResourcesActivity extends AppCompatActivity {
    private int unit_id = 0;
    private int topic_id = 0;
    private static final String TAG = "ListResourcesActivity";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resources);

        unit_id = getIntent().getIntExtra("ACTIVITY_PARAMS_UNIT_ID", 0);
        topic_id = getIntent().getIntExtra("ACTIVITY_PARAMS_TOPIC_ID", 0);
        progressBar = findViewById(R.id.progressBarRes);
        callResourceList();
      //  Toast.makeText(getApplicationContext(), "Unit: " + unit_id + " Topic: " + topic_id, Toast.LENGTH_LONG).show();
    }

    public void callTopics(View view) {
        Intent intent = new Intent(ListResourcesActivity.this, ListTopicsActivity.class);
        intent.putExtra("ACTIVITY_PARAMS_UNIT_ID", unit_id);
        intent.putExtra("ACTIVITY_PARAMS_TOPIC_ID", topic_id);
        startActivity(intent);
    }

    private void callResourceList() {
        if (Utilities.connectionExist(getApplicationContext())) {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/resource/unit/" + unit_id + "/topic_id/" + topic_id + "'",
                    null,
                    this::requestOk,
                    this::requestError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } else {
            Intent intent = new Intent(ListResourcesActivity.this, ConnectionErrorActivity.class);
            intent.putExtra("CONN_INTENT_CLASS", ListResourcesActivity.this.getClass().getName());
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
                drawResources(Resource.fromJSON(response));
            } else {
                Toast.makeText(getApplicationContext(), "No hay Unidades disponibles", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawResources(List<Resource> resources) {
        /*se agrgan los recursos de la base de datos*/
        LinearLayout units_container = findViewById(R.id.resources_container);
        LinearLayout.LayoutParams cardParams;
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

        for (Resource res : resources) {
            cardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            relativeLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mainTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            subTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout = new RelativeLayout(this);
            cardParams.setMargins(25, 25, 25, 25);

            card = new CardView(getApplicationContext());
            card.setLayoutParams(cardParams);
            card.setPadding(10, 10, 10, 10);
            card.setId(res.getId() + 6000);
            card.setCardBackgroundColor(Color.WHITE);
            card.setRadius(7);
            card.setOnClickListener(view -> {
                Intent intent = new Intent(ListResourcesActivity.this, ViewResourceActivity.class);
                intent.putExtra("ACTIVITY_PARAMS_UNIT_ID", unit_id);
                intent.putExtra("ACTIVITY_PARAMS_TOPIC_ID", topic_id);
                intent.putExtra("ACTIVITY_PARAMS_RESOURCE_TYPE", res.getResourceTypeId());
                intent.putExtra("ACTIVITY_PARAMS_URL", res.getUrl());
                startActivity(intent);
            });

            relativeLayout.setLayoutParams(relativeLayoutParams);
            //relativeLayout.setPadding(getDpUnit(16), getDpUnit(16), getDpUnit(16), getDpUnit(16));
            imageView = new ImageView(this);
            imageView.setId(res.getId() + 7000);

            try {
                iconId = getResources().getIdentifier(res.getIconName(), "drawable", getPackageName());
            } catch (Exception ie) {

            } finally {
                if (iconId == 0) {
                    iconId = getResources().getIdentifier("doc", "drawable", getPackageName());
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
            mainText.setId(res.getId() + 8000);
            mainTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            mainTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
            mainTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            mainTextParams.setMargins(5, 25, 0, 0);
            mainText.setLayoutParams(mainTextParams);
            mainText.setText(res.getName());
            mainText.setTextSize(20);
            mainText.setTextColor(getResources().getColor(R.color.colorPrimary));
            relativeLayout.addView(mainText, 1);

            subText = new TextView(this);
            subText.setId(res.getId() + 9000);
            subTextParams.addRule(RelativeLayout.BELOW, mainText.getId());
            subTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
            subTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            subTextParams.setMargins(5, 5, 0, 48);
            subText.setLayoutParams(subTextParams);
            subText.setText(res.getDescription());
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
