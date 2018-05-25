package co.edu.unadvirtual.computacion.movil.videos;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.edu.unadvirtual.computacion.movil.AppSingleton;
import co.edu.unadvirtual.computacion.movil.R;

public class VideosActivity extends ListActivity {
    private static final String TAG = "VideosActivity";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        listView = findViewById(android.R.id.list);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                AppSingleton.UNADROID_SERVER_ENDPOINT + "/videos",
                null,
                response -> loadVideos(response),
                error -> {

                }
        );

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);

    }

    private void loadVideos(JSONArray response) {
        ArrayAdapter<Video> a = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                Video.fromJSON(response)
        );
        listView.setAdapter(a);
    }
}