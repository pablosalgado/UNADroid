package co.edu.unadvirtual.computacion.movil;

import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StartEvaluationActivity extends AppCompatActivity {

    private int eval_id = 0;
    private String eval_desc= "Evaluacion unidad 0";
    TextView eval_title;
    PdfRenderer pdfRenderer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_evaluation);
        eval_id = getIntent().getExtras().getInt("ACTIVITY_PARAMS_QUIZ_ID",0);
        eval_desc = getIntent().getExtras().getString("ACTIVITY_PARAMS_QUIZ_NAME");
        eval_title = findViewById(R.id.start_eval_title);
        eval_title.setText(eval_desc);
    }

    public void startEvaluation(View view){
        Intent intent = new Intent(StartEvaluationActivity.this, EvaluationActivity.class);
        intent.putExtra("ACTIVITY_PARAMS_QUIZ_ID",eval_id);
        intent.putExtra("ACTIVITY_PARAMS_QUIZ_NAME",eval_desc);
        startActivity(intent);
    }

}
