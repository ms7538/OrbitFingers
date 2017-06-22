package poloapps.orbitfingers;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.Timer;
import java.util.TimerTask;

//v3.6f Top 10 Display in progress
public class UserAreaActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    try {
                        check_Ranking();
                    } catch (Exception e) {
                        // error, do something
                    }
                }
            });
        }
    };

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
            //String name     = intent.getStringExtra("name");
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
        else{
            Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            int peak_check     = jsonResponse.getInt("peak");
                            int DB_peak_stored = mSettings.getInt("peak_server", 0);
                            int min_check     = jsonResponse.getInt("min");
                            int DB_min_stored = mSettings.getInt("min_server", 0);
                            int smp_check     = jsonResponse.getInt("smp");
                            int DB_smp_stored = mSettings.getInt("smp_server", 0);

                            if (peak_check != DB_peak_stored || min_check != DB_min_stored ||
                                                                    smp_check != DB_smp_stored) {

                                editor.putInt("peak_server", peak_check);
                                editor.putInt("min_server", min_check);
                                editor.putInt("smp_server", smp_check);
                                editor.apply();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Failed To Get Database Values",
                                    Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    UserAreaActivity.this);
                            builder.setMessage("Get Server Values Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };



            ValuesRequest valuesRequest = new ValuesRequest(mSettings.getString("current_user",""),
                                                                                responseListener2);
            RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
            queue.add(valuesRequest);
        }


        final String username               = mSettings.getString("current_user","");
        final int min_score_device          = mSettings.getInt("min_score",0);
        final int smp_device                = mSettings.getInt("set_peak_min",2);
        final int peak_score_device         = mSettings.getInt("peakscore", 0);

        int min_score_server                = mSettings.getInt("min_server",0);
        int smp_server                      = mSettings.getInt("smp_server",0);
        int peak_score_server               = mSettings.getInt("peak_server", 0);

        //check_Ranking();
        final TextView tv_TT10_Link    = (TextView) findViewById(R.id.tv_Top_Ten_Link);
        TextView tv_Username_Display   = (TextView) findViewById(R.id.tvUsername);
        TextView tv_Device_text        = (TextView) findViewById(R.id.tv_Device);
        TextView tv_Server_text        = (TextView) findViewById(R.id.tv_Server);
        TextView tv_Device_Peak_value  = (TextView) findViewById(R.id.tv_Peak_Device_value);
        TextView tv_Device_Min_value   = (TextView) findViewById(R.id.tv_Min_Device_value);
        TextView tv_Device_SMP_value   = (TextView) findViewById(R.id.tv_Device_SMP);
        TextView tv_Server_Peak_value  = (TextView) findViewById(R.id.tv_Server_Peak);
        TextView tv_Server_Min_value   = (TextView) findViewById(R.id.tv_Server_Min);
        TextView tv_Server_SMP_value   = (TextView) findViewById(R.id.tv_Server_SMP);
        final TextView tv_Rank_value   = (TextView) findViewById(R.id.tv_Ranking_Value);
        TextView tv_Peak_Text          = (TextView) findViewById((R.id.tvPeak));
        TextView tv_Min_Text           = (TextView) findViewById((R.id.tvMin));
        TextView tv_SMP_Text           = (TextView) findViewById((R.id.tvSMP));
        final TextView tv_Tied_indication = (TextView) findViewById(R.id.tv_Tie_Indication);
        tv_Tied_indication.setVisibility(View.INVISIBLE);
        Button Device_Set_Button       = (Button)   findViewById((R.id.device_set_button));
        Button Server_Set_Button       = (Button)   findViewById((R.id.server_set_button));

        tv_Username_Display.setText(username);
        tv_Device_Peak_value.setText(String.format(Locale.US,"%d",peak_score_device));
        tv_Device_Min_value.setText (String.format(Locale.US,"%d",min_score_device));
        tv_Device_SMP_value.setText (String.format(Locale.US,"%d",smp_device));
        tv_Server_Peak_value.setText(String.format(Locale.US,"%d",peak_score_server));
        tv_Server_Min_value.setText (String.format(Locale.US,"%d",min_score_server));
        tv_Server_SMP_value.setText (String.format(Locale.US,"%d",smp_server));
        tv_Rank_value.setText(R.string.not_available);



        Integer Navy_Blue = ContextCompat.getColor(getApplicationContext(),(R.color.navy_blue));
        Integer Dark_Gray = ContextCompat.getColor(getApplicationContext(),(R.color.dark_gray));
        Integer Green     = ContextCompat.getColor(getApplicationContext(),(R.color.green2));
        Integer Red       = ContextCompat.getColor(getApplicationContext(),(R.color.red));
        Integer White     = ContextCompat.getColor(getApplicationContext(),(R.color.white));
        Integer Yellow    = ContextCompat.getColor(getApplicationContext(),(R.color.bright_yellow));

        Integer Device_BG_Color         = Dark_Gray;
        Integer Server_BG_Color         = Dark_Gray;
        Integer Device_Peak_Color       = Green;
        Integer Device_Min_Color        = Green;
        Integer Device_SMP_Color        = Green;
        Integer Server_Peak_Color       = Green;
        Integer Server_Min_Color        = Green;
        Integer Server_SMP_Color        = Green;
        Integer Device_SET_Button_Color = Dark_Gray;
        Integer Server_SET_Button_Color = Dark_Gray;
        Integer Peak_Text_Color         = White;
        Integer Min_Text_Color          = White;
        Integer SMP_Text_Color          = White;




        if ( peak_score_device < peak_score_server){

            Peak_Text_Color          = Navy_Blue;
            Device_Peak_Color        = Navy_Blue;
            Server_Peak_Color        = Green;

            if( min_score_device > min_score_server ) {

                Min_Text_Color       = Red;
                Server_Min_Color     = Red;

            }
            else
                Min_Text_Color       = Navy_Blue;

            Device_Min_Color = Navy_Blue;

            if( smp_device > smp_server ) {
                SMP_Text_Color       = Red;
                Server_SMP_Color     = Red;
            }
            else
                SMP_Text_Color       = Navy_Blue;

            Device_SMP_Color         = Navy_Blue;
            Device_SET_Button_Color  = Navy_Blue;
            Device_BG_Color          = Navy_Blue;

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

            Peak_Text_Color          = Navy_Blue;
            Server_Peak_Color        = Navy_Blue;
            Server_Min_Color         = Navy_Blue;
            Server_SMP_Color         = Navy_Blue;

            if ( min_score_device < min_score_server) {
                Min_Text_Color       = Red;
                Device_Min_Color     = Red;
            }
            else
                Min_Text_Color       = Navy_Blue;

            if ( smp_device < smp_server) {

                SMP_Text_Color       = Red;
                Device_SMP_Color     = Red;
            }
            else
                SMP_Text_Color       = Navy_Blue;

            Server_SET_Button_Color  = Navy_Blue;
            Server_BG_Color          = Navy_Blue;

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
        else if (min_score_device != min_score_server){

            Min_Text_Color       = White;
            Device_Min_Color     = Yellow;
            Server_Min_Color     = Yellow;

            if (smp_device != smp_server){

                SMP_Text_Color   = White;
                Device_SMP_Color = Yellow;
                Server_SMP_Color = Yellow;

            }

        }
        else if (smp_device != smp_server){

            SMP_Text_Color       = White;
            Device_SMP_Color     = Yellow;
            Server_SMP_Color     = Yellow;

        }


        tv_Device_Peak_value.setTextColor(Device_Peak_Color);
        tv_Device_Min_value.setTextColor(Device_Min_Color);
        tv_Device_SMP_value.setTextColor(Device_SMP_Color);

        tv_Peak_Text.setTextColor(Peak_Text_Color);
        tv_Min_Text.setTextColor(Min_Text_Color);
        tv_SMP_Text.setTextColor(SMP_Text_Color);

        tv_Server_Peak_value.setTextColor(Server_Peak_Color);
        tv_Server_Min_value.setTextColor(Server_Min_Color);
        tv_Server_SMP_value.setTextColor(Server_SMP_Color);

        tv_Device_text.setBackgroundColor(Device_BG_Color);
        tv_Server_text.setBackgroundColor(Server_BG_Color);

        Device_Set_Button.setBackgroundColor(Device_SET_Button_Color);
        Server_Set_Button.setBackgroundColor(Server_SET_Button_Color);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        tv_TT10_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TT10Intent = new Intent(UserAreaActivity.this, TopFiveActivity.class);
                UserAreaActivity.this.startActivity(TT10Intent);
            }
        });

        timer.schedule(task, 0 , 10000);  // interval of 10 sec

    }

    private void check_Ranking(){
        /////Ranking
        final TextView tv_Rank_value      = (TextView) findViewById(R.id.tv_Ranking_Value);
        final TextView tv_Tied_indication = (TextView) findViewById(R.id.tv_Tie_Indication);
        final SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);

        int peak_score_server             = mSettings.getInt("peak_server", 0);
        final String username             = mSettings.getString("current_user","");

        final Integer Red       = ContextCompat.getColor(getApplicationContext(),(R.color.red));

        Response.Listener<String> responseListener3 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("rank_success");
                    if (success) {
                        int number_peaks_above   = jsonResponse.getInt("higher_peaks") + 1;
                        tv_Rank_value.setText(String.format(Locale.US,"%d",number_peaks_above));
                        if ( number_peaks_above > 9 ){

                            tv_Rank_value.setTextColor(Red);
                        }
                         boolean equal_peaks = jsonResponse.getBoolean("equal_peaks");
                        if (equal_peaks){
                            tv_Tied_indication.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Failed To get Ranking",
                                Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                UserAreaActivity.this);
                        builder.setMessage("Get Ranking Failed")
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


        RankingRequest rankingRequest = new RankingRequest(username,peak_score_server,
                                                                                responseListener3);
        RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
        queue.add(rankingRequest);
        /////////Ranking Finished
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        task.cancel();
        super.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        super.onPause();
        task.cancel();
    }
    @Override
    public void onResume() {
        super.onResume();
        task.run();
    }

}