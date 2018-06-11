package co.edu.unadvirtual.computacion.movil.iam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import co.edu.unadvirtual.computacion.movil.AppSingleton;
import co.edu.unadvirtual.computacion.movil.MainActivity;
import co.edu.unadvirtual.computacion.movil.R;
import co.edu.unadvirtual.computacion.movil.common.Session;
import co.edu.unadvirtual.computacion.movil.domain.User;

/**
 * Permite editar los datos básicos del usuario y los envia al servidor.
 */
public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getUser();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(v -> editUser(
                editTextEmail.getText().toString(),
                editTextFirstName.getText().toString(),
                editTextLastName.getText().toString()
        ));
    }

    /**
     * Método para hacer la edición del usuario
     *
     * @param email     Correo electrónico del usuario.
     * @param firstName Nombres
     * @param lastName  Apellidos
     */
    private void editUser(String email, String firstName, String lastName) {
        // Los datos del usuario para ser enviadas al servidor en formato JSON
        JSONObject params = new JSONObject();

        try {
            params.put("email", email);
            params.put("firstName", firstName);
            params.put("lastName", lastName);
            params.put("id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                AppSingleton.UNADROID_SERVER_ENDPOINT + "/user",
                params,
                new EditProfileActivity.SuccessListener(),
                new EditProfileActivity.ErrorListener()
        );

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
    }

    public void getUser() {
        JSONObject params = new JSONObject();

        String email = Session.getUserEmail(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                AppSingleton.UNADROID_SERVER_ENDPOINT + "/user/" + email,
                params,
                this::successGetUser,
                this::errorGetUser
        );

        // Se envia la petición a la cola
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
    }

    private void successGetUser(JSONObject response) {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);

        try {
            User user = User.fromJSON(response);

            user_id = user.getId();
            editTextEmail.setText(user.getEmail());
            editTextFirstName.setText(user.getFirstName());
            editTextLastName.setText(user.getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void errorGetUser(VolleyError volleyError) {
        //progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(
                this.getApplicationContext(),
                volleyError.getMessage(),
                Toast.LENGTH_LONG
        ).show();

    }

    /**
     * Procesa la respuesta del servidor.
     */
    private class SuccessListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                boolean error = !response.isNull("error");

                if (!error) {
                    Intent intent = new Intent(
                            EditProfileActivity.this,
                            MainActivity.class);
                    intent.putExtra("email", response.getString("email"));
                    startActivity(intent);
                    finish();

                } else {
                    String errorMsg = response.getString("error_msg");
                    Toast.makeText(
                            getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG
                    ).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Si ocurre algún error, se despliega el mensaje del mismo.
     */
    private class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(
                    EditProfileActivity.this.getApplicationContext(),
                    error.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
