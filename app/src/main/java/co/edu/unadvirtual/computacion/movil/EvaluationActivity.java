package co.edu.unadvirtual.computacion.movil;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import co.edu.unadvirtual.computacion.movil.common.Utilities;
import co.edu.unadvirtual.computacion.movil.domain.Question;
import co.edu.unadvirtual.computacion.movil.domain.QuestionAnswers;
import co.edu.unadvirtual.computacion.movil.domain.Topic;

public class EvaluationActivity extends AppCompatActivity {

    private static final String TAG = "EvaluationActivity";
    private ProgressBar progressBar;
    private int eval_id = 0;
    private int current_question_number = 0;
    private int current_question_id = 0;
    private int correct_answers = 0;
    private int correct_answer_id = 0;
    private int lives_left = 3;
    private String eval_desc = "Evaluacion unidad 0";
    TextView eval_title;
    TextView question_count;
    TextView question;
    ImageView heart;
    Button answer_action;
    List<Question> question_list;
    List<QuestionAnswers> answer_list;
    LinearLayout quest_container;
    LinearLayout answer_container;
    CardView question_container;
    RadioGroup rbg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        question_container = findViewById(R.id.questions_container);
        quest_container = findViewById(R.id.quest_container);
        answer_container = findViewById(R.id.answer_container);
        eval_title = findViewById(R.id.start_eval_title);
        question_count = findViewById(R.id.question_count);
        question = findViewById(R.id.question);
        progressBar = findViewById(R.id.progressBarQuest);
        answer_action = findViewById(R.id.answer_action);

        eval_id = getIntent().getExtras().getInt("ACTIVITY_PARAMS_QUIZ_ID", 0);
        eval_desc = getIntent().getExtras().getString("ACTIVITY_PARAMS_QUIZ_NAME");
        eval_title.setText(eval_desc);
        answer_action.setEnabled(false);
        callQuestionList();
    }

    public void doAnimation() {
        Animation anim = AnimationUtils.loadAnimation(EvaluationActivity.this, R.anim.righttoleft);
        question_container.startAnimation(anim);
    }

    public void doHeartAnimation() {
        Animation anim = AnimationUtils.loadAnimation(EvaluationActivity.this, R.anim.bounce);
        if (lives_left == 0) {
            heart = findViewById(R.id.heart_3);
        } else if (lives_left == 1) {
            heart = findViewById(R.id.heart_2);
        } else if (lives_left == 2) {
            heart = findViewById(R.id.heart_1);
        }
        heart.startAnimation(anim);
        heart.setImageResource(R.drawable.wrong_android);
    }

    private void drawQuestion() {
        answer_container.removeAllViews();
        Question q = question_list.get(current_question_number - 1);
        current_question_id = q.getId();
        question.setText(current_question_number + ") " + q.getQuestion());
        progressBar.setVisibility(View.VISIBLE);
        question_count.setText("Pregunta: "+current_question_number+" de " + question_list.size());
        if(current_question_number == question_list.size()){
            answer_action.setText("Finalizar");
        }
        doAnimation();
        callAnswerList();
    }

    private void drawAnwers() {
        answer_action.setEnabled(true);
        if(answer_list.size() > 0) {

            switch (question_list.get(current_question_number - 1).getQuestionTypeId()){
                case 1:

                    rbg = new RadioGroup(this);
                    rbg.setId(100+current_question_id);
                    RadioButton rb;
                    for (QuestionAnswers answer : answer_list) {
                        rb = new RadioButton(this);
                        rb.setId(answer.getId());
                        rb.setText(answer.getAnswer());
                        rb.setTypeface(Typeface.MONOSPACE);
                        if(answer.getCorrect()==1){
                            correct_answer_id = answer.getId();
                        }
                        rbg.addView(rb);
                    }
                    answer_container.addView(rbg);
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Tipo de pregunta no programada", Toast.LENGTH_LONG).show();
                    break;

            }

        }
    }

    public void evaluateAnswer(View view){
        answer_action.setEnabled(false);
        switch (question_list.get(current_question_number - 1).getQuestionTypeId()){
            case 1:
                rbg = findViewById(100+current_question_id);

                int radioButtonID = rbg.getCheckedRadioButtonId();


              if(radioButtonID != -1){
                  if(correct_answer_id==radioButtonID) {
                      correct_answers++;
                      Toast.makeText(getApplicationContext(), "Correcto!", Toast.LENGTH_LONG).show();
                  }else{
                      lives_left--;
                      doHeartAnimation();
                  }
              }else {
                  Toast.makeText(getApplicationContext(), "Debe seleccionar una opción!", Toast.LENGTH_LONG).show();
              }
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                Toast.makeText(getApplicationContext(), "Tipo de pregunta no programada", Toast.LENGTH_LONG).show();
                break;

        }

        if(current_question_number == question_list.size()){
            answer_action.setEnabled(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Aviso");
            builder.setMessage("Ha finalizado el examen: Tu puntaje es: "+correct_answers+"/"+question_list.size()+".");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(EvaluationActivity.this, EvaluationListActivity.class);
                    startActivity(intent);
                }
            });
            builder.create();
            builder.show();
        }else if(lives_left==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Aviso");
            builder.setMessage("Te has quedado sin oportunidades! Tu puntaje es: "+correct_answers+"/"+question_list.size()+".");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(EvaluationActivity.this, EvaluationListActivity.class);
                    startActivity(intent);
                }
            });
            builder.create();
            builder.show();
        }else{
            current_question_number++;
            drawQuestion();
        }

    }

    private void callQuestionList() {
        if (Utilities.connectionExist(getApplicationContext())) {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/question/evaluation_id/" + eval_id,
                    null,
                    this::requestOk,
                    this::requestError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } else {
            Intent intent = new Intent(EvaluationActivity.this, ConnectionErrorActivity.class);
            intent.putExtra("CONN_INTENT_CLASS", EvaluationListActivity.class.getClass().getName());
            startActivity(intent);
        }
    }

    private void requestOk(JSONArray response) {

        try {
            progressBar.setVisibility(View.GONE);
            if (!response.isNull(0)) {
                question_list = Question.fromJSON(response);
                current_question_number = 1;
                drawQuestion();
            } else {
                question_list = new ArrayList<>();
                Toast.makeText(getApplicationContext(), "No hay Preguntas disponibles", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestError(VolleyError volleyError) {
        progressBar.setVisibility(View.GONE);
        question_list = new ArrayList<>();
        Toast.makeText(this.getApplicationContext(), "No se puede procesar su solicitud en este momento!", Toast.LENGTH_LONG).show();
    }

    private void callAnswerList() {
        if (Utilities.connectionExist(getApplicationContext())) {
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/answer/question_id/" + current_question_id,
                    null,
                    this::answersRequestOk,
                    this::answersRequestError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } else {
            Intent intent = new Intent(EvaluationActivity.this, ConnectionErrorActivity.class);
            intent.putExtra("CONN_INTENT_CLASS", EvaluationListActivity.class.getClass().getName());
            startActivity(intent);
        }
    }

    private void answersRequestOk(JSONArray response) {

        try {
            progressBar.setVisibility(View.GONE);
            if (!response.isNull(0)) {
                answer_list = QuestionAnswers.fromJSON(response);
                drawAnwers();
            } else {
                answer_list = new ArrayList<>();
                Toast.makeText(getApplicationContext(), "No hay respuestas disponibles", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    private void answersRequestError(VolleyError volleyError) {
        progressBar.setVisibility(View.GONE);
        answer_list = new ArrayList<>();
        Toast.makeText(this.getApplicationContext(), "No se puede procesar su solicitud en este momento!", Toast.LENGTH_LONG).show();
    }

}

