package co.edu.unadvirtual.computacion.movil.videos;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import co.edu.unadvirtual.computacion.movil.AppSingleton;
import co.edu.unadvirtual.computacion.movil.R;
import co.edu.unadvirtual.computacion.movil.domain.Video;

public class VideosActivity extends AppCompatActivity {
    private static final String TAG = "VideosActivity";
    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        listView = findViewById(R.id.videosListView);
        progressBar = findViewById(R.id.videosProgressBar);

        // Se construye la ruta al endpoint de UNADroid server para obtener la lista de videos:
        // https://unadroid.tk/api/videos
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                AppSingleton.UNADROID_SERVER_ENDPOINT + "/videos",
                null,
                this::successLoadingVideos,
                this::errorLoadingVideos
        );

        // Se envia la petición a la cola
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);

        // Se muestra la barra de progreso mientras la petición recibe una respuesta del servidor
        progressBar.setVisibility(View.VISIBLE);

        listView.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Obtener el video seleccionado
        Video video = (Video) adapterView.getItemAtPosition(i);

        try {
            // Se intenta cargar el video en la app de YouTube, si está instalada
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getUrl()));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Si la aplicación no existe, se carga directamente en el navegador
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + video.getUrl()));
            startActivity(intent);
        }
    }

    private void successLoadingVideos(JSONArray jsonArray) {
        try {
            // Se carga la lista de videos obtenida del servidor a la interface
            VideosActivity.this.listView.setAdapter(
                    new VideoListViewAdapter(Video.fromJSON(jsonArray), this)
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void errorLoadingVideos(VolleyError volleyError) {
        progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(
                this.getApplicationContext(),
                volleyError.getMessage(),
                Toast.LENGTH_LONG
        ).show();

    }
}