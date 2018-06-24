package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.edu.unadvirtual.computacion.movil.common.Utilities;
import co.edu.unadvirtual.computacion.movil.domain.Question;
import co.edu.unadvirtual.computacion.movil.domain.QuestionAnswers;

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
    MediaPlayer mp_correct;
    MediaPlayer mp_wrong;


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
        mp_correct  = MediaPlayer.create(getApplicationContext(), R.raw.correct);
        mp_wrong  = MediaPlayer.create(getApplicationContext(), R.raw.wrong);

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

    public void playSoundClip(boolean correct) {
        if (correct) {
            mp_correct.start();
        } else {
            mp_wrong.start();
        }
    }

    private void drawQuestion() {
        answer_container.removeAllViews();
        Question q = question_list.get(current_question_number - 1);
        current_question_id = q.getId();
        question.setText(current_question_number + ") " + q.getQuestion());
        progressBar.setVisibility(View.VISIBLE);
        question_count.setText("Pregunta: " + current_question_number + " de " + question_list.size());
        if (current_question_number == question_list.size()) {
            answer_action.setText("Finalizar");
        }
        doAnimation();
        callAnswerList();
    }

    private void drawAnwers() {
        answer_action.setEnabled(true);
        if (answer_list.size() > 0) {

            switch (question_list.get(current_question_number - 1).getQuestionTypeId()) {
                case 1:

                    rbg = new RadioGroup(this);
                    rbg.setId(100 + current_question_id);
                    RadioButton rb;
                    for (QuestionAnswers answer : answer_list) {
                        rb = new RadioButton(this);
                        rb.setId(answer.getId());
                        rb.setText(answer.getAnswer());
                        rb.setTypeface(Typeface.MONOSPACE);
                        if (answer.getCorrect() == 1) {
                            correct_answer_id = answer.getId();
                        }
                        rbg.addView(rb);
                    }
                    answer_container.addView(rbg);
                    break;
                case 2:
                    CheckBox cb;
                    for (QuestionAnswers answer : answer_list) {
                        cb = new CheckBox(this);
                        cb.setId(answer.getId());
                        cb.setText(answer.getAnswer());
                        cb.setTypeface(Typeface.MONOSPACE);
                        if (answer.getCorrect() == 1) {
                            correct_answer_id = answer.getId();
                        }
                        answer_container.addView(cb);
                    }
                    break;
                case 3:
                    EditText et = new EditText(this);
                    QuestionAnswers answer = answer_list.get(0);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    et.setId(answer.getId());
                    answer_container.addView(et);
                    et.requestFocus();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Tipo de pregunta no programada", Toast.LENGTH_LONG).show();
                    break;

            }

        }
    }

    public void evaluateAnswer(View view) {
        answer_action.setEnabled(false);
        boolean selected_answer = false;

        switch (question_list.get(current_question_number - 1).getQuestionTypeId()) {
            case 1:
                rbg = findViewById(100 + current_question_id);

                int radioButtonID = rbg.getCheckedRadioButtonId();

                if (radioButtonID != -1) {
                    selected_answer = true;
                    if (correct_answer_id == radioButtonID) {
                        correct_answers++;
                        playSoundClip(true);
                    } else {
                        lives_left--;
                        playSoundClip(false);
                        doHeartAnimation();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe seleccionar una opción!", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                CheckBox cb;
                boolean correct = true;
                int selected = 0;
                for (QuestionAnswers qa : answer_list) {
                    cb = findViewById(qa.getId());
                    if (cb.isChecked()) {
                        selected++;
                        if (qa.getCorrect() != 1) {
                            correct = false;
                        }
                    } else {
                        if (qa.getCorrect() == 1) {
                            correct = false;
                        }
                    }
                }

                if (selected > 0) {
                    selected_answer = true;
                    if (correct) {
                        correct_answers++;
                        playSoundClip(true);
                    } else {
                        lives_left--;
                        playSoundClip(false);
                        doHeartAnimation();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe seleccionar una opción!", Toast.LENGTH_LONG).show();
                    selected_answer = false;
                }
                break;
            case 3:
                answer_action.setEnabled(false);
                QuestionAnswers answer = answer_list.get(0);
                EditText et = findViewById(answer.getId());
                if (et.getText().toString() != null && !"".equals(et.getText().toString().trim())) {
                    if (et.getText().toString().trim().equalsIgnoreCase(answer.getAnswer())) {
                        correct_answers++;
                        playSoundClip(true);
                    } else {
                        lives_left--;
                        playSoundClip(false);
                        doHeartAnimation();
                    }
                    selected_answer = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Debe ingresar una respuesta!", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                Toast.makeText(getApplicationContext(), "Tipo de pregunta no programada", Toast.LENGTH_LONG).show();
                break;

        }
        if (selected_answer) {
            if (current_question_number == question_list.size() || lives_left == 0) {

                String main_msg = "Ha finalizado el examen: Tu puntaje es: " + correct_answers + "/" + question_list.size() + ".";
                String result_msg;
                int percent = 0;
                try {
                    percent = ((correct_answers / question_list.size()) * 100);
                } catch (Exception ne) {

                }
                if (percent == 100) {
                    result_msg = "\n\nExcelente puntuación! ;)";
                } else if (percent > 70) {
                    result_msg = "\n\nBuen resultado. Pero puedes mejorar!";
                } else {
                    result_msg = "\n\nTerecomendamos leer la documetacion para obtener mejores resultados. (v_v)";
                }
                if (lives_left == 0) {
                    main_msg = "Te has quedado sin oportunidades! Tu puntaje es: " + correct_answers + "/" + question_list.size() + ".";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Aviso");
                builder.setMessage(main_msg + result_msg);
                builder.setPositiveButton("Aceptar", (dialog, id) -> {
                    Intent intent = new Intent(EvaluationActivity.this, EvaluationListActivity.class);
                    startActivity(intent);
                });
                builder.create();
                builder.show();
            } else {
                answer_action.setEnabled(true);
                current_question_number++;
                drawQuestion();
            }
        } else {
            answer_action.setEnabled(true);
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
                Collections.shuffle(question_list);
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

