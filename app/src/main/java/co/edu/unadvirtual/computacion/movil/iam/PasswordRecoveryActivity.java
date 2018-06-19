package co.edu.unadvirtual.computacion.movil.iam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
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
import co.edu.unadvirtual.computacion.movil.R;

public class PasswordRecoveryActivity extends AppCompatActivity {
    private static final String TAG = "PassRecoveryActivity";
    private EditText editTextEmail;
    private ProgressBar progressBar;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        editTextEmail = findViewById(R.id.editTextEmail);
        progressBar = findViewById(R.id.progressBar);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.validate_invalid_email);

        findViewById(R.id.buttonPasswordRecovery).setOnClickListener(this::sendClicked);
    }

    private void sendClicked(View view) {
        if (!awesomeValidation.validate()) {
            return;
        }

        recoverPassword(editTextEmail.getText().toString());
    }

    private void recoverPassword(String email) {
        try {
            progressBar.setVisibility(View.VISIBLE);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    AppSingleton.UNADROID_SERVER_ENDPOINT + "/recover/" + email,
                    null,
                    this::recoverSuccessful,
                    this::recoverError
            );

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request, TAG);
        } catch (Exception e) {
            progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.iam_recovery_error,
                    Toast.LENGTH_LONG
            ).show();

            Log.e(
                    TAG,
                    e.getMessage(),
                    e
            );
        }
    }

    private void recoverSuccessful(JSONObject jsonObject) {
        try {
            boolean error = !jsonObject.isNull("error");

            if (error) {
                String errorMsg = jsonObject.getString("error_msg");
                Toast.makeText(
                        getApplicationContext(),
                        errorMsg,
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            Toast.makeText(
                    getApplicationContext(),
                    R.string.iam_recover_message,
                    Toast.LENGTH_LONG
            ).show();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.iam_login_error,
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

    private void recoverError(VolleyError volleyError) {
        progressBar.setVisibility(View.INVISIBLE);

        Toast.makeText(
                getApplicationContext(),
                R.string.iam_login_error,
                Toast.LENGTH_LONG
        ).show();

        Log.e(
                TAG,
                volleyError.getMessage(),
                volleyError
        );
    }
}
