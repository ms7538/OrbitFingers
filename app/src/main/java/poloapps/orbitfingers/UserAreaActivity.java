package poloapps.orbitfingers;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static java.security.AccessController.getContext;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle(R.string.load_save);
        final SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();

        Boolean logged_in = mSettings.getBoolean("Signed_In", false);

        if (!logged_in){
            Intent intent   = getIntent();
            String name     = intent.getStringExtra("name");
            String username = intent.getStringExtra("username");
            int peak        = intent.getIntExtra("peak", -1);
            int min         = intent.getIntExtra("min", -1);
            int smp         = intent.getIntExtra("smp", -1);

            editor.putString ("current_user",username);
            editor.putBoolean("Signed_In", true);
            editor.putInt    ("peak_server",peak);
            editor.putInt    ("min_server",min);
            editor.putInt    ("smp_server",smp);
            editor.apply();
        }
        final Integer min_score_device          = mSettings.getInt("min_score",0);
        final Integer smp_device                = mSettings.getInt("set_peak_min",2);
        final Integer peak_score_device   = mSettings.getInt("peakscore", 0);

        Integer min_score_server    = mSettings.getInt("min_server",0);
        Integer smp_server          = mSettings.getInt("smp_server",0);
        Integer peak_score_server   = mSettings.getInt("peak_server", 0);

        TextView tv_Device_Peak_value  = (TextView) findViewById(R.id.tv_Peak_Device_value);
        TextView tv_Device_Min_value   = (TextView) findViewById(R.id.tv_Min_Device_value);
        TextView tv_Device_SMP_value   = (TextView) findViewById(R.id.tv_Device_SMP);

        TextView tv_Server_Peak_value  = (TextView) findViewById(R.id.tv_Server_Peak);
        TextView tv_Server_Min_value   = (TextView) findViewById(R.id.tv_Server_Min);
        TextView tv_Server_SMP_value   = (TextView) findViewById(R.id.tv_Server_SMP);

        Button Device_Set_Button       = (Button)   findViewById((R.id.device_set_button));
        Button Server_Set_Button       = (Button)   findViewById((R.id.server_set_button));

        final String username = mSettings.getString("current_user","");


        tv_Device_Peak_value.setText(String.format(Locale.US,"%d",peak_score_device));
        tv_Device_Min_value.setText (String.format(Locale.US,"%d",min_score_device));
        tv_Device_SMP_value.setText (String.format(Locale.US,"%d",smp_device));

        tv_Server_Peak_value.setText(String.format(Locale.US,"%d",peak_score_server));
        tv_Server_Min_value.setText (String.format(Locale.US,"%d",min_score_server));
        tv_Server_SMP_value.setText (String.format(Locale.US,"%d",smp_server));


        Integer Navy_Blue    = ContextCompat.getColor(getApplicationContext(),(R.color.navy_blue));
        Integer Dark_Gray    = ContextCompat.getColor(getApplicationContext(),(R.color.dark_gray));

        Integer Device_Set_Color = Dark_Gray;
        Integer Server_Set_Color = Dark_Gray;

        if ( peak_score_device < peak_score_server){

            Device_Set_Color = Navy_Blue;
            Device_Set_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    int min_score_db = mSettings.getInt("min_server", 0);
                    editor.putInt("peakscore"   ,mSettings.getInt("peak_server",0));
                    editor.putInt("min_score"   ,min_score_db  );
                    editor.putInt("set_peak_min",mSettings.getInt("smp_server", 0));
                    editor.apply();

                    if ( min_score_db >= getApplicationContext()
                            .getResources().getInteger(R.integer.L4_target_score)){
                        editor.putInt("levl",5);
                    }
                    else if (min_score_db >= getApplicationContext()
                            .getResources().getInteger(R.integer.L3_target_score)){
                        editor.putInt("levl",4);
                    }
                    else if (min_score_db >= getApplicationContext()
                            .getResources().getInteger(R.integer.L2_target_score)){
                        editor.putInt("levl",3);
                    }
                    else if (min_score_db >= getApplicationContext()
                            .getResources().getInteger(R.integer.L1_target_score)){
                        editor.putInt("levl",2);
                    }
                    else
                        editor.putInt("levl",1);

                    editor.apply();
                    //Restart Current Activity
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

        }
        else if (peak_score_device > peak_score_server){

            Server_Set_Color = Navy_Blue;
            Server_Set_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {

                                    editor.putInt("peak_server",jsonResponse.getInt("peak"));
                                    editor.putInt("min_server" ,jsonResponse.getInt("min"));
                                    editor.putInt("smp_server" ,jsonResponse.getInt("smp"));
                                    editor.apply();

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);

                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            UserAreaActivity.this);
                                    builder.setMessage("Update Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    UpdateRequest updateRequest = new UpdateRequest( username,peak_score_device,
                                                    min_score_device,smp_device,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
                    queue.add(updateRequest);

                }
            });
        }

        Device_Set_Button.setBackgroundColor(Device_Set_Color);
        Server_Set_Button.setBackgroundColor(Server_Set_Color);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenu_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        super.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}