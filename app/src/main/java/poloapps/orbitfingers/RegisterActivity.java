package poloapps.orbitfingers;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button   bRegister  = (Button) findViewById(R.id.bRegister);
        final TextView tvPeak_Value = (TextView) findViewById(R.id.tv_peak_value);
        final TextView tvMin_Value = (TextView) findViewById(R.id.tv_min_score_value);

        tvPeak_Value.setText(String.format(Locale.US,"%d",mSettings.getInt("peakscore", 0)));

        tvMin_Value.setText(String.format(Locale.US,"%d",mSettings.getInt("min_score", 0)));


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name     = etName.getText().toString();
                final String username = etUsername.getText().toString();
                final int peak        = mSettings.getInt("peakscore", 0);
                final int min         = mSettings.getInt("min_score", 0);
                final int smp         = mSettings.getInt("set_peak_min", 0);
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(RegisterActivity.this,
                                                                              LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                                                            RegisterActivity.this);
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name, username, peak, min,
                                                                   smp, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);


            }
        });
    }
}