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

public class EditPasswordActivity extends AppCompatActivity {

    private static final String TAG = "EditPasswordActivity";
    private EditText editTextPasswordAEP1;
    private EditText editTextPasswordAEP2;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        editTextPasswordAEP1 = findViewById(R.id.editTextPasswordAEP1);
        editTextPasswordAEP2 = findViewById(R.id.editTextPasswordAEP2);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.editTextPasswordAEP1, Utilities.passwordRegex(), R.string.validate_password_policy);
        awesomeValidation.addValidation(this, R.id.editTextPasswordAEP2, R.id.editTextPasswordAEP1, R.string.validate_password_repeat_required);

        Button buttonSend = findViewById(R.id.buttonSendAEP);
        buttonSend.setOnClickListener(v -> editPassword(
                editTextPasswordAEP1.getText().toString()
        ));
    }

    private void editPassword(String password) {
        if (!awesomeValidation.validate()) {
            return;
        }

        String email = Session.getUserEmail(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put("password", password);
            params.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                AppSingleton.UNADROID_SERVER_ENDPOINT + "/user/setPassword",
                params,
                new EditPasswordActivity.SuccessListener(),
                new EditPasswordActivity.ErrorListener()
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
                            EditPasswordActivity.this,
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
                    EditPasswordActivity.this.getApplicationContext(),
                    error.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }


}
