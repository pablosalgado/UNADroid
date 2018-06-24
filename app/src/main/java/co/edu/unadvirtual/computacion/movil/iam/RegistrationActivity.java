package co.edu.unadvirtual.computacion.movil.iam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONObject;

import co.edu.unadvirtual.computacion.movil.AppSingleton;
import co.edu.unadvirtual.computacion.movil.MainActivity;
import co.edu.unadvirtual.computacion.movil.R;
import co.edu.unadvirtual.computacion.movil.common.Session;
import co.edu.unadvirtual.computacion.movil.common.Utilities;
import co.edu.unadvirtual.computacion.movil.domain.User;

/**
 * Captura los datos básicos del usuario y los envia al servidor.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private AwesomeValidation awesomeValidation;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        progressBar = findViewById(R.id.progressBar);

        // Validaciones del formulaio de registro
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.validate_invalid_email);
        awesomeValidation.addValidation(this, R.id.editTextFirstName, Utilities.namesRegex(), R.string.validate_firstname_required);
        awesomeValidation.addValidation(this, R.id.editTextLastName, Utilities.namesRegex(), R.string.validate_lastname_required);
        awesomeValidation.addValidation(this, R.id.editTextPassword, Utilities.passwordRegex(), R.string.validate_password_policy);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this::sendClicked);
    }

    private void sendClicked(View view) {
        registerUser(
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString(),
                editTextFirstName.getText().toString(),
                editTextLastName.getText().toString()
        );
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
        if (!awesomeValidation.validate()) {
            return;
        }

        try {
            progressBar.setVisibility(View.VISIBLE);

            // Los datos del usuario para ser enviadas al servidor en formato JSON
            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("password", password);
            params.put("firstName", firstName);
            params.put("lastName", lastName);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/register",
                    params,
                    this::registrationSuccess,
                    this::registrationError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } catch (Exception e) {
            progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.iam_registration_error,
                    Toast.LENGTH_LONG
            ).show();

            Log.e(
                    TAG,
                    e.getMessage(),
                    e
            );
        }

    }

    /**
     * Procesa la respuesta del servidor si el usuario se ha podido registrar exitosamente.
     *
     * @param response Contiene el usuario recién creado.
     */
    private void registrationSuccess(JSONObject response) {
        try {
            boolean error = !response.isNull("error");

            if (error) {
                String errorMsg = response.getString("error_msg");
                Toast.makeText(
                        getApplicationContext(),
                        errorMsg,
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            User user = User.fromJSON(response);

            Intent intent = new Intent(this, MainActivity.class);
            Session.putUserEmail(getApplicationContext(), user.getEmail());
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.iam_registration_error,
                    Toast.LENGTH_LONG
            ).show();

            Log.e(
                    TAG,
                    e.getMessage(),
                    e
            );
        } finally {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Procesa las situaciones de error durante la comunicación con el servidor, mostrando el mensaje
     * de error.
     *
     * @param volleyError El error que se ha generado.
     */
    private void registrationError(VolleyError volleyError) {
        progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(
                getApplicationContext(),
                R.string.iam_registration_error,
                Toast.LENGTH_LONG
        ).show();

        Log.e(
                TAG,
                volleyError.getMessage(),
                volleyError
        );
    }

}
