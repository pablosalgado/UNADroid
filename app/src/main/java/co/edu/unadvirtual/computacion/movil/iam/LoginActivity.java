package co.edu.unadvirtual.computacion.movil.iam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
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
 * Captura las credenciales del usuario y las valida contra el servidor
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText editTextPassword;
    private EditText editTextEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this::loginClicked);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this::registerClicked);
    }

    private void loginClicked(View view) {
        progressBar.setVisibility(View.VISIBLE);
        loginUser(
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString()
        );
    }

    private void registerClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método para llevar a cabo la autenticación del usuario.
     *
     * @param email El nombre de usuario.
     * @param password La contraseña del usuario.
     */
    private void loginUser(final String email, final String password) {
        // Las credenciales del usuario para ser enviadas al servidor en formato JSON
        JSONObject params = new JSONObject();

        try {
            params.put("email", email);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Se hace una petición POST al servidor con las credenciales de acceso del usuario.
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppSingleton.UNADROID_SERVER_ENDPOINT + "/login",
                params,
                this::loginSuccessful,
                this::loginError
        );

        // Enviar la petición a la cola. Los resultados enviados por el servidor serán procesados
        // por los Listener asociados en el momento que la respuesta arrive al dispositivo.
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
    }

    /**
     * Procesa la respuesta del servidor. Si las credenciales que el usuario ha ingresado permiten
     * autenticar con éxito, se carga la aplicación
     *
     * @param response
     */
    private void loginSuccessful(JSONObject response) {
        try {
            User user = User.fromJSON(response);

            Session.putUserEmail(getApplicationContext(), user.getEmail());

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Toast.makeText(
                    getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG
            ).show();
        } finally {
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    /**
     * Si ocurre algún error, se despliega el mensaje del mismo.
     *
     * @param volleyError
     */
    private void loginError(VolleyError volleyError) {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(
                LoginActivity.this.getApplicationContext(),
                volleyError.getMessage(),
                Toast.LENGTH_LONG
        ).show();
    }

}
