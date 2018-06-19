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
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import co.edu.unadvirtual.computacion.movil.AppSingleton;
import co.edu.unadvirtual.computacion.movil.MainActivity;
import co.edu.unadvirtual.computacion.movil.R;
import co.edu.unadvirtual.computacion.movil.common.Session;
import co.edu.unadvirtual.computacion.movil.common.Utilities;
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
    private ProgressBar progressBar;

    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        progressBar = findViewById(R.id.progressBar);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.validate_invalid_email);
        awesomeValidation.addValidation(this, R.id.editTextFirstName, Utilities.namesRegex(), R.string.validate_firstname_required);
        awesomeValidation.addValidation(this, R.id.editTextLastName, Utilities.namesRegex(), R.string.validate_lastname_required);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(v -> editUser(
                editTextEmail.getText().toString(),
                editTextFirstName.getText().toString(),
                editTextLastName.getText().toString()
        ));
    }

    @Override
    protected void onStart() {
        super.onStart();

        getUser();
    }

    private void getUser() {
        try {
            progressBar.setVisibility(View.VISIBLE);

            String email = Session.getUserEmail(getApplicationContext());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/user/" + email,
                    null,
                    this::successGetUser,
                    this::errorGetUser
            );

            // Se envia la petición a la cola
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } catch (Exception e) {
            progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.iam_profile_error,
                    Toast.LENGTH_LONG
            ).show();

            Log.e(
                    TAG,
                    e.getMessage(),
                    e
            );

        }
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
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    private void errorGetUser(VolleyError volleyError) {
        progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(
                this.getApplicationContext(),
                volleyError.getMessage(),
                Toast.LENGTH_LONG
        ).show();
    }

    /**
     * Método para hacer la edición del usuario
     *
     * @param email     Correo electrónico del usuario.
     * @param firstName Nombres
     * @param lastName  Apellidos
     */
    private void editUser(String email, String firstName, String lastName) {
        if(!awesomeValidation.validate()) {
            return;
        }

        try {
            progressBar.setVisibility(View.VISIBLE);

            // Los datos del usuario para ser enviadas al servidor en formato JSON
            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("firstName", firstName);
            params.put("lastName", lastName);
            params.put("id", user_id);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/user",
                    params,
                    this::updateSuccess,
                    this::updateError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } catch (Exception e) {
            progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.iam_profile_error,
                    Toast.LENGTH_LONG
            ).show();

            Log.e(
                    TAG,
                    e.getMessage(),
                    e
            );
        }
    }

    private void updateSuccess(JSONObject jsonObject) {
        try {
            boolean error = !jsonObject.isNull("error");

            if (!error) {
                Intent intent = new Intent(
                        EditProfileActivity.this,
                        MainActivity.class);
                intent.putExtra("email", jsonObject.getString("email"));
                startActivity(intent);
                finish();

            } else {
                String errorMsg = jsonObject.getString("error_msg");
                Toast.makeText(
                        getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG
                ).show();
            }
        } catch (JSONException e) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.iam_profile_error,
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

    private void updateError(VolleyError volleyError) {
        progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(
                getApplicationContext(),
                volleyError.getMessage(),
                Toast.LENGTH_LONG
        ).show();

        Log.e(
                TAG,
                volleyError.getMessage(),
                volleyError
        );
    }

}
