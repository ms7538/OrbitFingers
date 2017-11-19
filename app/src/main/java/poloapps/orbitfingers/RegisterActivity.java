package poloapps.orbitfingers;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle("Register");
        final SharedPreferences mSettings     = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();

        final EditText etName             = findViewById(R.id.etName);
        final EditText etUsername         = findViewById(R.id.etUsername);
        final EditText etPassword         = findViewById(R.id.etPassword);
        final EditText etPassword2        = findViewById(R.id.etPassword2);
        final Button   bRegister          = findViewById(R.id.bRegister);
        final TextView tvPeak_Value       = findViewById(R.id.tv_peak_value);
        final TextView tvMin_Value        = findViewById(R.id.tv_min_score_value);
        final TextView tvSMPs_Value       = findViewById(R.id.tv_smp_rem);


        tvPeak_Value.setText(String.format(Locale.US,"%d",mSettings.getInt("peakscore",    0)));
        tvMin_Value.setText (String.format(Locale.US,"%d",mSettings.getInt("min_score",    0)));
        tvSMPs_Value.setText(String.format(Locale.US,"%d",mSettings.getInt("set_peak_min", 1)));

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)getApplicationContext()
                                .getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean Connect = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if(Connect) {
                    final String name      = etName.getText().toString();
                    final String username  = etUsername.getText().toString();
                    final int peak         = mSettings.getInt("peakscore",   0);
                    final int min          = mSettings.getInt("min_score",   0);
                    final int smp          = mSettings.getInt("set_peak_min",1);
                    final String password  = etPassword.getText().toString();
                    final String password2 = etPassword2.getText().toString();
                    if( password.equals(password2) ) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        editor.putString("user_id", username);
                                        editor.apply();

                                        Intent intent = new Intent(RegisterActivity.this,
                                                LoginActivity.class);
                                        RegisterActivity.this.startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                RegisterActivity.this);
                                        builder.setMessage(R.string.reg_fail).setCancelable(false)
                                                .setNegativeButton(R.string.retry, null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        RegisterRequest registerRequest = new RegisterRequest(name, username, peak,
                                min, smp, password, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                        queue.add(registerRequest);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity
                                .this);
                        builder.setMessage(R.string.mismatch)
                                .setNegativeButton(R.string.back, null)
                                .create()
                                .show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage(R.string.no_ic)
                            .setNegativeButton(R.string.back, null)
                            .create()
                            .show();
                }

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_privacy:
                // User chose the "Settings" item, show the app settings UI...
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        RegisterActivity.this);
                builder.setMessage(R.string.privacy_policy_actual)
                        .setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;

            case R.id.action_tou:
                // User chose the "Settings" item, show the app settings UI...
                AlertDialog.Builder builder2 = new AlertDialog.Builder(
                        RegisterActivity.this);
                builder2.setMessage(R.string.tou_actual)
                        .setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}