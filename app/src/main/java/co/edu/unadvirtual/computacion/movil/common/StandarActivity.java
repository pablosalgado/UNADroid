package co.edu.unadvirtual.computacion.movil.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import co.edu.unadvirtual.computacion.movil.MainActivity;
import co.edu.unadvirtual.computacion.movil.R;
import co.edu.unadvirtual.computacion.movil.ViewResourceActivity;
import co.edu.unadvirtual.computacion.movil.videos.VideosActivity;

public class StandarActivity extends AppCompatActivity {

    private int unit_id = 0;
    private int topic_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standar);

        unit_id = getIntent().getIntExtra("unit_id", 0);
        topic_id = getIntent().getIntExtra("topic_id", 0);
        Toast.makeText(getApplicationContext(), "Unit: " + unit_id + " Topic: " + topic_id, Toast.LENGTH_LONG).show();
    }

    public void callResource(View view) {
        Random r = new Random();
        unit_id = (r.nextInt(100 - 1) + 1);
        topic_id = (r.nextInt(100 - 1) + 1);
        Intent intent = new Intent(StandarActivity.this, ViewResourceActivity.class);
        intent.putExtra("unit_id", unit_id);
        intent.putExtra("topic_id", topic_id);
        startActivity(intent);
    }
}
