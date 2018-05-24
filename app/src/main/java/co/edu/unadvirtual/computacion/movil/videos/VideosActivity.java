package co.edu.unadvirtual.computacion.movil.videos;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.edu.unadvirtual.computacion.movil.AppSingleton;
import co.edu.unadvirtual.computacion.movil.R;
import co.edu.unadvirtual.computacion.movil.iam.RegistrationActivity;

public class VideosActivity extends ListActivity {
    private static final String TAG = "VideosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                AppSingleton.UNADROID_SERVER_ENDPOINT + "/videos",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loadVideos(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);

    }

    private void loadVideos(JSONArray response) {
        List<JSONObject> list = new ArrayList<>();

        try {
            for(int i = 0; i < response.length(); i++) {
                JSONObject o = response.getJSONObject(0);
                list.add(o);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<JSONObject> a = new ArrayAdapter<JSONObject>(this, R.layout.activity_videos, list);
        setListAdapter(a);
    }
}