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



public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle("Login To OrbitFingers");

        final EditText etUsername             = findViewById(R.id.etUsername);
        final EditText etPassword             = findViewById(R.id.etPassword);
        final TextView tvRegisterLink         = findViewById(R.id.tvRegisterLink);
        final Button bLogin                   = findViewById(R.id.bLogin);
        final SharedPreferences mSettings     = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(R.string.tou_actual)
                        .setPositiveButton(R.string.agree,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent registerIntent = new Intent(LoginActivity.this,
                                                RegisterActivity.class);
                                        LoginActivity.this.startActivity(registerIntent);
                                    }
                                })
                        .setNegativeButton(R.string.not_agree,null)
                        .create()
                        .show();


            }
        });
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm =
                        (ConnectivityManager)getApplicationContext()
                                .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = null;
                if (cm != null) {
                    activeNetwork = cm.getActiveNetworkInfo();
                }
                boolean Connect = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if(Connect) {
                    final String username = etUsername.getText().toString();
                    final String password = etPassword.getText().toString();
                    // Response received from the server
                    Response.Listener<String> r_Listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    String name = jsonResponse.getString("name");
                                    int peak_score_value = jsonResponse.getInt("peak");
                                    int min_score_value = jsonResponse.getInt("min");
                                    int set_min_peak_rem = jsonResponse.getInt("smp");

                                    Intent intent = new Intent(LoginActivity.this,
                                            User_Activity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("username", username);
                                    intent.putExtra("peak", peak_score_value);
                                    intent.putExtra("min", min_score_value);
                                    intent.putExtra("smp", set_min_peak_rem);

                                    editor.putString("current_user", username);
                                    editor.apply();
                                    LoginActivity.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            LoginActivity.this);
                                    builder.setMessage(R.string.login_fail)
                                            .setNegativeButton(R.string.retry, null)
                                            .create()
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(username, password, r_Listener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.no_ic)
                            .setNegativeButton(R.string.back, null)
                            .create()
                            .show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        super.startActivity(intent);
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
                        LoginActivity.this);
                builder.setMessage(R.string.privacy_policy_actual)
                        .setPositiveButton(this.getString(R.string.back), new DialogInterface.OnClickListener() {
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
                        LoginActivity.this);
                builder2.setMessage(R.string.tou_actual)
                        .setPositiveButton(this.getString(R.string.back), new DialogInterface.OnClickListener() {
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