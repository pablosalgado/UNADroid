package co.edu.unadvirtual.computacion.movil.iam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

/**
 * Captura los datos básicos del usuario y los envia al servidor.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);

        buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(
                        editTextEmail.getText().toString(),
                        editTextPassword.getText().toString(),
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString()
                );
            }
        });

    }

    /**
     * Método para hacer el registro del usuario
     *
     * @param email     Correo electrónico del usuario.
     * @param password  Contraseña
     * @param firstName Nombres
     * @param lastName  Apellidos
     */
    private void registerUser(String email, String password, String firstName, String lastName) {
        // Los datos del usuario para ser enviadas al servidor en formato JSON
        JSONObject params = new JSONObject();

        try {
            params.put("email", email);
            params.put("password", password);
            params.put("firstName", firstName);
            params.put("lastName", lastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://unadroid.tk/api/register",
                params,
                new SuccessListener(),
                new ErrorListener()
        );

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
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
                            RegistrationActivity.this,
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
            } catch (JSONException e){
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
                    RegistrationActivity.this.getApplicationContext(),
                    error.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
