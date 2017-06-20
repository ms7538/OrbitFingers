package poloapps.orbitfingers;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class TopTenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        final TextView tv_Top_Peak_value      = (TextView) findViewById(R.id.tv_top_1);
        final TextView tv_Top2_Peak_value      = (TextView) findViewById(R.id.tv_top_2);
        final TextView tv_Top_Username        = (TextView) findViewById(R.id.tv_top_username);
        final TextView tv_Top2_Username        = (TextView) findViewById(R.id.tv_top2_username);

        final SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);


        final String username             = mSettings.getString("current_user","");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("tt_success");
                    if (success) {

                        int top_peak       = jsonResponse.getInt("top1_peak");
                       
                        String top_username = jsonResponse.getString("top1_username");
                        int top2_peak       = jsonResponse.getInt("top2_peak");
                        String top2_username = jsonResponse.getString("top2_username");
                        tv_Top_Peak_value.setText(String.format(Locale.US,"%d",top_peak));
                        tv_Top_Username.setText(top_username);
                        tv_Top2_Peak_value.setText(String.format(Locale.US,"%d",top2_peak));
                        tv_Top2_Username.setText(top2_username);
                    } else {
                        Toast.makeText(getBaseContext(), "Failed To Get TT",
                                Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                TopTenActivity.this);
                        builder.setMessage("Get TT Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(),"JSON EXCEPTION",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };


        TopTenRequest topTenRequest = new TopTenRequest(username,responseListener);
        RequestQueue queue = Volley.newRequestQueue(TopTenActivity.this);
        queue.add(topTenRequest);

    }
}
