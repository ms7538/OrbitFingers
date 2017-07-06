package poloapps.orbitfingers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Timer;
import java.util.TimerTask;

//v3.6h Top 5 Display Complete
public class User_Activity extends AppCompatActivity {

    final Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    try {
                        if( check_Connection()) check_Ranking();
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
        if( check_Connection()) check_Ranking();
        final SharedPreferences mSettings     = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();
        final EditText etRMessage             = (EditText) findViewById(R.id.et_ranking_message);
        timer.schedule(task, 0 , 10000);  // interval of 10 sec

        Boolean logged_in                     = mSettings.getBoolean("Signed_In", false);

        if (!logged_in){
            Intent intent   = getIntent();
            String rank_msg = intent.getStringExtra("name");
            String username = intent.getStringExtra("username");
            int peak        = intent.getIntExtra   ("peak", -1);
            int min         = intent.getIntExtra   ("min",  -1);
            int smp         = intent.getIntExtra   ("smp",  -1);
            editor.putString ("name",        rank_msg);
            editor.putString ("rank_message",rank_msg);
            editor.putString ("current_user",username);
            editor.putBoolean("Signed_In",   true);
            editor.putInt    ("peak_server", peak);
            editor.putInt    ("min_server",  min);
            editor.putInt    ("smp_server",  smp);
            editor.apply();
        }
        else{
            Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success        = jsonResponse.getBoolean("success");
                        if (success) {
                            int peak_check     = jsonResponse.getInt    ("peak");
                            int DB_peak_stored = mSettings.getInt       ("peak_server", 0);
                            int min_check      = jsonResponse.getInt    ("min");
                            int DB_min_stored  = mSettings.getInt       ("min_server",  0);
                            int smp_check      = jsonResponse.getInt    ("smp");
                            int DB_smp_stored  = mSettings.getInt       ("smp_server",  0);

                            String RMsg        = jsonResponse.getString("rank_msg");
                            editor.putString("rank_message", RMsg);
                            editor.apply();
                            etRMessage.setText(RMsg);

                            if (peak_check != DB_peak_stored || min_check != DB_min_stored ||
                                                                    smp_check != DB_smp_stored) {
                                editor.putInt("peak_server", peak_check);
                                editor.putInt("min_server", min_check);
                                editor.putInt("smp_server", smp_check);
                                editor.apply();
                                Activity_restart();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Failed To Get Database Values",
                                    Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    User_Activity.this);
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
            RequestQueue queue          = Volley.newRequestQueue(User_Activity.this);
            queue.add(valuesRequest);
        }

        final String rank_msg          = mSettings.getString("rank_message","");
        final String username          = mSettings.getString("current_user","");
        final int min_score_device     = mSettings.getInt   ("min_score",   0);
        final int smp_device           = mSettings.getInt   ("set_peak_min",1);
        final int peak_score_device    = mSettings.getInt   ("peakscore",   0);
        int min_score_server           = mSettings.getInt   ("min_server",  0);
        int smp_server                 = mSettings.getInt   ("smp_server",  0);
        int peak_score_server          = mSettings.getInt   ("peak_server", 0);
        final TextView tv_Top5_Link    = (TextView) findViewById(R.id.tv_Top_Five_Link);
        final TextView tv_SRM_Link     = (TextView) findViewById(R.id.tv_rank_msg_link);
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
        final TextView tv_Tied         = (TextView) findViewById(R.id.tv_Tie_Indication);
        Button Device_Set_Button       = (Button)   findViewById((R.id.device_set_button));
        Button Server_Set_Button       = (Button)   findViewById((R.id.server_set_button));
        etRMessage.setText(rank_msg);
        tv_Tied.setVisibility(View.INVISIBLE);
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

        if (peak_score_device < peak_score_server){
            Peak_Text_Color             = Navy_Blue;
            Device_Peak_Color           = Navy_Blue;
            Server_Peak_Color           = Green;

            if( min_score_device > min_score_server ) {
                Min_Text_Color          = Red;
                Server_Min_Color        = Red;
            }
            else  Min_Text_Color        = Navy_Blue;

            Device_Min_Color = Navy_Blue;

            if( smp_device > smp_server ) {
                SMP_Text_Color          = Red;
                Server_SMP_Color        = Red;
            }
            else SMP_Text_Color         = Navy_Blue;

            Device_SMP_Color            = Navy_Blue;
            Device_SET_Button_Color     = Navy_Blue;
            Device_BG_Color             = Navy_Blue;

            Device_Set_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    int min_score_db = mSettings.getInt("min_server", 0);
                    editor.putInt("peakscore"   ,mSettings.getInt("peak_server",0));
                    editor.putInt("min_score"   ,min_score_db  );
                    editor.putInt("set_peak_min",mSettings.getInt("smp_server", 0));
                    editor.apply();

                    if      (min_score_db >= getApplicationContext()
                            .getResources().getInteger(R.integer.L4_target_score)) editor.putInt("levl",5);
                    else if (min_score_db  >= getApplicationContext()
                            .getResources().getInteger(R.integer.L3_target_score)) editor.putInt("levl",4);
                    else if (min_score_db  >= getApplicationContext()
                            .getResources().getInteger(R.integer.L2_target_score)) editor.putInt("levl",3);
                    else if (min_score_db  >= getApplicationContext()
                            .getResources().getInteger(R.integer.L1_target_score)) editor.putInt("levl",2);
                    else                           editor.putInt("levl",1);
                    editor.apply();
                    Activity_restart();
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

                    if(check_Connection()) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        editor.putInt("peak_server", jsonResponse.getInt("peak"));
                                        editor.putInt("min_server", jsonResponse.getInt("min"));
                                        editor.putInt("smp_server", jsonResponse.getInt("smp"));
                                        editor.apply();
                                        Activity_restart();
                                    } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                                User_Activity.this);
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

                        UpdateRequest updateRequest = new UpdateRequest(username, peak_score_device,
                                min_score_device, smp_device, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(User_Activity.this);
                        queue.add(updateRequest);
                    } else no_IC_alert();

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
        tv_Device_Min_value.setTextColor (Device_Min_Color);
        tv_Device_SMP_value.setTextColor (Device_SMP_Color);
        tv_Peak_Text.setTextColor        (Peak_Text_Color);
        tv_Min_Text.setTextColor         (Min_Text_Color);
        tv_SMP_Text.setTextColor         (SMP_Text_Color);
        tv_Server_Peak_value.setTextColor(Server_Peak_Color);
        tv_Server_Min_value.setTextColor (Server_Min_Color);
        tv_Server_SMP_value.setTextColor (Server_SMP_Color);
        tv_Device_text.setBackgroundColor(Device_BG_Color);
        tv_Server_text.setBackgroundColor(Server_BG_Color);

        Device_Set_Button.setBackgroundColor(Device_SET_Button_Color);
        Server_Set_Button.setBackgroundColor(Server_SET_Button_Color);

        AdView mAdView      = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        tv_Top5_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( check_Connection()) {
                      Intent T5Intent = new Intent(User_Activity.this, TopFiveActivity.class);
                      User_Activity.this.startActivity(T5Intent);
                }else no_IC_alert();
            }
        });

        tv_SRM_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( check_Connection()) {

                    String rank_msg = etRMessage.getText().toString();
                    editor.putString("rank_message", rank_msg);
                    editor.apply();
                    String RMessage = etRMessage.getText().toString();
                    final Response.Listener<String> responseListener4 =
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("msg_success");
                                        if (success) {
                                            String MSG = jsonResponse.getString("message");
                                            editor.putString("rank_message", MSG);
                                            editor.apply();
                                            Toast.makeText(getBaseContext(),"Ranking Message Set To"
                                                                                        + " " + MSG,
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getBaseContext(), "Failed To set MSGs",
                                                    Toast.LENGTH_LONG).show();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                                    User_Activity.this);
                                            builder.setMessage("Set Message Failed")
                                                    .setNegativeButton("Retry", null)
                                                    .create()
                                                    .show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(getBaseContext(), "JSON EXCEPTION",
                                                Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            };

                    MessageRequest messageRequest = new MessageRequest(username, RMessage,
                            responseListener4);
                    RequestQueue queue = Volley.newRequestQueue(User_Activity.this);
                    queue.add(messageRequest);
                } else no_IC_alert();
            }
        });

    }

    private void Activity_restart(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void no_IC_alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(User_Activity.this);
        builder.setMessage("No Internet Connection")
                .setNegativeButton("Back", null)
                .create()
                .show();
    }

    private boolean check_Connection(){
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ( activeNetwork != null && activeNetwork.isConnectedOrConnecting() );
    }

    private void check_Ranking(){
        /////Ranking
        final SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        final TextView tv_Rank_value      = (TextView) findViewById(R.id.tv_Ranking_Value);
        final TextView tv_Tied_indication = (TextView) findViewById(R.id.tv_Tie_Indication);
        final TextView tv_Top5_Link       = (TextView) findViewById(R.id.tv_Top_Five_Link);
        final TextView tv_RM_Link         = (TextView) findViewById(R.id.tv_rank_msg_link);
        final EditText etRMessage         = (EditText) findViewById(R.id.et_ranking_message);

        final SharedPreferences.Editor editor = mSettings.edit();
        final int peak_score_server           = mSettings.getInt("peak_server",0);
        final int min_score_server            = mSettings.getInt("min_server", 0);
        final int smp_server                  = mSettings.getInt("smp_server", 2);
        final String username                 = mSettings.getString("current_user","");
        final String rank_msg                 = mSettings.getString("rank_message","");
        final Integer Red       = ContextCompat.getColor(getApplicationContext(),(R.color.red));
        final Integer Green     = ContextCompat.getColor(getApplicationContext(),(R.color.green2));

        final Response.Listener<String> responseListener3 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("rank_success");
                    if (success) {
                        if( mSettings.getInt("ut5", 0) == 0 ) {
                            int number_peaks_above = jsonResponse.getInt("higher_peaks") + 1;
                            tv_Rank_value.setText(String.format(Locale.US, "%d",
                                                                              number_peaks_above));
                            boolean equal_peaks = jsonResponse.getBoolean("equal_peaks");
                            if (equal_peaks) {
                                tv_Tied_indication.setVisibility(View.VISIBLE);
                            }else{
                                tv_Tied_indication.setVisibility(View.INVISIBLE);
                            }

                            editor.putInt    ("users_higher_peaks", number_peaks_above);
                            editor.putBoolean("users_equal_peaks",  equal_peaks);
                            editor.apply();
                        }

                        } else {
                        Toast.makeText(getBaseContext(), "Failed To get Ranking",
                                Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                User_Activity.this);
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
        RequestQueue queue = Volley.newRequestQueue(User_Activity.this);
        queue.add(rankingRequest);


        Response.Listener<String> responseT5Listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse  = new JSONObject(response);
                    boolean success          = jsonResponse.getBoolean("tt_success");
                    if (success) {
                        int UserT5           = 0;
                        String top_username  = jsonResponse.getString("top1_username");
                        String top2_username = jsonResponse.getString("top2_username");
                        String top3_username = jsonResponse.getString("top3_username");
                        String top4_username = jsonResponse.getString("top4_username");
                        String top5_username = jsonResponse.getString("top5_username");
                        Integer user_Peak    = jsonResponse.getInt   ("user_peak");
                        Integer user_Min     = jsonResponse.getInt   ("user_min");
                        Integer user_SMP     = jsonResponse.getInt   ("user_smp");
                        String  user_RMsg    = jsonResponse.getString("user_msg");
                        Boolean restart      = false;

                        if(user_Peak != peak_score_server){
                            editor.putInt("peak_server",user_Peak);
                            restart = true;

                        }
                        if(user_Min != min_score_server){
                            editor.putInt("min_server",user_Min);
                            restart = true;
                        }
                        if(user_SMP != smp_server){
                            editor.putInt("smp_server",user_SMP);
                            restart = true;
                        }
                        if(!(user_RMsg.equals(rank_msg))){
                            editor.putString("rank_message",rank_msg);
                            restart = true;
                        }
                        if (username.equals(top_username)){
                            UserT5 = 1;
                        }
                        else if (username.equals(top2_username)){
                            UserT5 = 2;
                        }
                        else if (username.equals(top3_username)){
                            UserT5 = 3;
                        }
                        else if (username.equals(top4_username)){
                            UserT5 = 4;
                        }
                        else if (username.equals(top5_username)){
                            UserT5 = 5;
                        }

                        editor.putInt("ut5", UserT5);
                        editor.apply();

                        if( UserT5 != 0 ) {
                            tv_Top5_Link.setTextColor(Green);
                            tv_Rank_value.setText(String.format(Locale.US,"%d",UserT5));
                            tv_Rank_value.setTextColor(Green);
                            tv_Tied_indication.setVisibility(View.INVISIBLE);
                            etRMessage.setVisibility(View.VISIBLE);
                            tv_RM_Link.setVisibility(View.VISIBLE);

                        }
                        else{
                            tv_Top5_Link.setTextColor(Red);
                            tv_Rank_value.setTextColor(Red);
                            etRMessage.setVisibility(View.INVISIBLE);
                            tv_RM_Link.setVisibility(View.INVISIBLE);
                        }

                        if(restart){
                            editor.apply();
                            Activity_restart();
                        }

                    } else {
                        Toast.makeText(getBaseContext(), "Failed To Get TT",
                                Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                User_Activity.this);
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


        TopFiveRequest topFiveRequest = new TopFiveRequest(username,100000,responseT5Listener);
        queue.add(topFiveRequest);
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