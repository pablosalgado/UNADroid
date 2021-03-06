package co.edu.unadvirtual.computacion.movil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
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
import co.edu.unadvirtual.computacion.movil.domain.Topic;


public class ListTopicsActivity extends AppCompatActivity  {

    private static final String TAG = "ListTopicsActivity";
    private ProgressBar progressBar;
    private TextView  titleUnit;
    private int unit_id;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_topics);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        unit_id = data.getInt("ACTIVITY_PARAMS_UNIT_ID");
        progressBar = findViewById(R.id.progressBarTopics);
        titleUnit = findViewById(R.id.titleUnit);
        titleUnit.setText("Unidad "+unit_id);
        callTopicsList();
    }

    public void callUnits(View view){
        Intent intent = new Intent(ListTopicsActivity.this, MainActivity.class);
        startActivity(intent);
    }


    private void callTopicsList() {
        if (Utilities.connectionExist(getApplicationContext())) {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/unit/"+unit_id+"/topics",
                    null,
                    this::requestOk,
                    this::requestError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } else {
            Intent intent = new Intent(ListTopicsActivity.this, ConnectionErrorActivity.class);
            intent.putExtra("CONN_INTENT_CLASS", ListTopicsActivity.this.getClass().getName());
            startActivity(intent);
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
        /*se agregan los temas de la unidad de la base de datos*/
        LinearLayout topics_container = findViewById(R.id.topics_container);
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

        for (Topic topic : topics) {

            cardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            relativeLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mainTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout = new RelativeLayout(this);
            cardParams.setMargins(25, 5, 25, 5);

            card = new CardView(getApplicationContext());
            card.setLayoutParams(cardParams);
            card.setPadding(10, 20, 10, 20);
            card.setId(topic.getId() + 6000);
            card.setCardBackgroundColor(Color.WHITE);
            card.setRadius(7);

            card.setOnClickListener(view -> {
                Intent intent = new Intent( ListTopicsActivity.this, ListResourcesActivity.class);
                intent.putExtra("ACTIVITY_PARAMS_UNIT_ID",topic.getUnitId());
                intent.putExtra("ACTIVITY_PARAMS_TOPIC_ID",topic.getId());
                startActivity(intent);
            });

            relativeLayout.setLayoutParams(relativeLayoutParams);

            imageView = new ImageView(this);
            imageView.setId(topic.getId() + 7000);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            imageParams.setMargins(5, 20, 0, 20);
            imageView.setLayoutParams(imageParams);
            imageView.getLayoutParams().width = getDpTopic(62);
            imageView.getLayoutParams().height = getDpTopic(50);
            imageView.setImageResource(getResources().getIdentifier("ic_folder", "drawable", getPackageName()));
            relativeLayout.addView(imageView, 0);


            mainText = new TextView(this);
            mainText.setId(topic.getId() + 8000);
            mainTextParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            mainTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
            mainTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
            mainTextParams.addRule(RelativeLayout.CENTER_VERTICAL, imageView.getId());
            mainTextParams.setMargins(5, 0, 0, 0);
            mainText.setLayoutParams(mainTextParams);
            mainText.setGravity(Gravity.CENTER_VERTICAL);
            mainText.setText(topic.getName());
            mainText.setTextSize(18);
            mainText.setTextColor(getResources().getColor(R.color.colorPrimary));
            relativeLayout.addView(mainText, 1);


            card.addView(relativeLayout);
            // Finally, add the CardView in root layout
            topics_container.addView(card);
        }
    }

    private int getDpTopic(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }

}
