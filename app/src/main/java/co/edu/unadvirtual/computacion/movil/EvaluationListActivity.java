package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import co.edu.unadvirtual.computacion.movil.common.StandarActivity;
import co.edu.unadvirtual.computacion.movil.common.Utilities;
import co.edu.unadvirtual.computacion.movil.domain.Evaluation;

public class EvaluationListActivity extends AppCompatActivity {

    private static final String TAG = "EvaluationListActivity";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_list);
        progressBar = findViewById(R.id.progressBarEvals);
        callEvaluationList();
    }


    /**
     * Realiza una llamada al servidor para listar las unidades disponibles
     * Llama al metodo requestOk en caso de que la solicitud no tenga errores,
     * llama a requestError de lo contrario
     */
    private void callEvaluationList() {
        if (Utilities.connectionExist(getApplicationContext())) {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/evaluations",
                    null,
                    this::requestOk,
                    this::requestError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } else {
            Intent intent = new Intent(EvaluationListActivity.this, ConnectionErrorActivity.class);
            intent.putExtra("CONN_INTENT_CLASS", EvaluationListActivity.this.getClass().getName());
            startActivity(intent);
        }
    }

    /**
     * Se ejecuta en caso de que callEvaluationList() se ejecute de menera satisfactoria
     *
     * @param response el resultado de la consulta con los datos retornados.
     */
    private void requestOk(JSONArray response) {
        try {

            progressBar.setVisibility(View.GONE);
            if (!response.isNull(0)) {
                drawUnits(Evaluation.fromJSON(response));
            } else {
                Toast.makeText(getApplicationContext(), "No hay Unidades disponibles", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Sin acceso al servidor de datos.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Se ejecuta en caso de que callEvaluationList() se ejecute con errores
     *
     * @param volleyError el resultado de la consulta con los datos retornados.
     */
    private void requestError(VolleyError volleyError) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this.getApplicationContext(), "No es posible cargar las unidades.", Toast.LENGTH_LONG).show();
    }

    private void drawUnits(List<Evaluation> evals) {
        /*se agrgan las unidades de la base de datos*/
        if(evals.size() > 0) {
            LinearLayout units_container = findViewById(R.id.evals_container);
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

            for (Evaluation eval : evals) {
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
                card.setId(eval.getId() + 6000);
                card.setCardBackgroundColor(Color.WHITE);
                card.setRadius(7);
                card.setOnClickListener(view -> {
                    Intent intent = new Intent(EvaluationListActivity.this, StartEvaluationActivity.class);
                    intent.putExtra("ACTIVITY_PARAMS_QUIZ_ID", eval.getId());
                    intent.putExtra("ACTIVITY_PARAMS_QUIZ_NAME", eval.getName());
                    startActivity(intent);
                });

                relativeLayout.setLayoutParams(relativeLayoutParams);
                //relativeLayout.setPadding(getDpUnit(16), getDpUnit(16), getDpUnit(16), getDpUnit(16));
                imageView = new ImageView(this);
                imageView.setId(eval.getId() + 7000);
                iconId = getResources().getIdentifier("quiz_icon", "drawable", getPackageName());

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
                mainText.setId(eval.getId() + 8000);
                mainTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                mainTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
                mainTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
                mainTextParams.setMargins(5, 25, 0, 0);
                mainText.setLayoutParams(mainTextParams);
                mainText.setText(eval.getName());
                mainText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
                mainText.setTextColor(getResources().getColor(R.color.colorPrimary));
                relativeLayout.addView(mainText, 1);

                subText = new TextView(this);
                subText.setId(eval.getId() + 9000);
                subTextParams.addRule(RelativeLayout.BELOW, mainText.getId());
                subTextParams.addRule(RelativeLayout.END_OF, imageView.getId());
                subTextParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
                subTextParams.setMargins(5, 5, 0, 48);
                subText.setLayoutParams(subTextParams);
                subText.setText(eval.getDescription());
                subText.setTypeface(subText.getTypeface(), Typeface.BOLD);
                subText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                subText.setTextColor(getResources().getColor(R.color.colorAccent));
                relativeLayout.addView(subText, 2);

                card.addView(relativeLayout);
                // Finally, add the CardView in root layout
                units_container.addView(card);
            }
        }else{
            Toast.makeText(getApplicationContext(), "No hay Unidades disponibles", Toast.LENGTH_LONG).show();
        }
    }


    private int getDpUnit(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }

}
