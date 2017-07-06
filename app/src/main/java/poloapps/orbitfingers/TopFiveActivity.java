package poloapps.orbitfingers;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TableRow;
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

public class TopFiveActivity extends AppCompatActivity {

    final Handler handler = new Handler();
    Timer timer2 = new Timer();
    TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    try {
                        get_Ranking();
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
        setContentView(R.layout.activity_top_five);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle(R.string.top_five);
        set_NT5_Rank();
        timer2.schedule(task2, 0 , 10000);  // interval of 10 sec
    }

    private void get_Ranking(){

        final TextView tv_Top_Peak_value       = (TextView) findViewById(R.id.tv_top_1);
        final TextView tv_Top2_Peak_value      = (TextView) findViewById(R.id.tv_top_2);
        final TextView tv_Top3_Peak_value      = (TextView) findViewById(R.id.tv_top_3);
        final TextView tv_Top4_Peak_value      = (TextView) findViewById(R.id.tv_top_4);
        final TextView tv_Top5_Peak_value      = (TextView) findViewById(R.id.tv_top_5);
        final TextView tv_Top_Username         = (TextView) findViewById(R.id.tv_top_username);
        final TextView tv_Top2_Username        = (TextView) findViewById(R.id.tv_top2_username);
        final TextView tv_Top3_Username        = (TextView) findViewById(R.id.tv_top3_username);
        final TextView tv_Top4_Username        = (TextView) findViewById(R.id.tv_top4_username);
        final TextView tv_Top5_Username        = (TextView) findViewById(R.id.tv_top5_username);
        final TextView tv_Top_MSG              = (TextView) findViewById(R.id.tv_top_message);
        final TextView tv_T2_MSG               = (TextView) findViewById(R.id.tv_t2_message);
        final TextView tv_T3_MSG               = (TextView) findViewById(R.id.tv_t3_message);
        final TextView tv_T4_MSG               = (TextView) findViewById(R.id.tv_t4_message);
        final TextView tv_T5_MSG               = (TextView) findViewById(R.id.tv_t5_message);
        final SharedPreferences mSettings      = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor  = mSettings.edit();
        final String username                  = mSettings.getString("current_user","");
        int peak_score_server          = mSettings.getInt("peak_server", 0);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("tt_success");
                    if (success) {
                        int UserT5           = 0;
                        int top_peak         = jsonResponse.getInt   ("top1_peak");
                        String top_username  = jsonResponse.getString("top1_username");
                        String top_message   = jsonResponse.getString("top1_message");
                        int top2_peak        = jsonResponse.getInt   ("top2_peak");
                        String top2_username = jsonResponse.getString("top2_username");
                        String top2_message  = jsonResponse.getString("top2_message");
                        int top3_peak        = jsonResponse.getInt   ("top3_peak");
                        String top3_username = jsonResponse.getString("top3_username");
                        String top3_message  = jsonResponse.getString("top3_message");
                        int top4_peak        = jsonResponse.getInt   ("top4_peak");
                        String top4_username = jsonResponse.getString("top4_username");
                        String top4_message  = jsonResponse.getString("top4_message");
                        int top5_peak        = jsonResponse.getInt   ("top5_peak");
                        String top5_username = jsonResponse.getString("top5_username");
                        String top5_message  = jsonResponse.getString("top5_message");
                        editor.putBoolean("users_equal_peaks",jsonResponse.getBoolean
                                                                                 ("equal_peaks"));
                        editor.putInt("users_higher_peaks",jsonResponse.getInt
                                                                                 ("higher_peaks")+1);

                        editor.putInt("peak_server",jsonResponse.getInt("user_peak"));
                        editor.apply();
                        tv_Top_Peak_value.setText (String.format(Locale.US,"%d",top_peak));
                        tv_Top2_Peak_value.setText(String.format(Locale.US,"%d",top2_peak));
                        tv_Top3_Peak_value.setText(String.format(Locale.US,"%d",top3_peak));
                        tv_Top4_Peak_value.setText(String.format(Locale.US,"%d",top4_peak));
                        tv_Top5_Peak_value.setText(String.format(Locale.US,"%d",top5_peak));
                        tv_Top_Username.setText (top_username);
                        tv_Top2_Username.setText(top2_username);
                        tv_Top3_Username.setText(top3_username);
                        tv_Top4_Username.setText(top4_username);
                        tv_Top5_Username.setText(top5_username);
                        tv_Top_MSG.setText(top_message);
                        tv_T2_MSG.setText(top2_message);
                        tv_T3_MSG.setText(top3_message);
                        tv_T4_MSG.setText(top4_message);
                        tv_T5_MSG.setText(top5_message);
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
                        set_Highlight(UserT5);
                        editor.putInt("ut5", UserT5);
                        editor.apply();
                    } else {
                        Toast.makeText(getBaseContext(), "Failed To Get TT",
                                Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                TopFiveActivity.this);
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
        TopFiveRequest topFiveRequest = new TopFiveRequest(username,peak_score_server,
                                                                                responseListener);
        RequestQueue queue = Volley.newRequestQueue(TopFiveActivity.this);
        queue.add(topFiveRequest);
    }

    private void set_Highlight(int pos){

        final Integer Dark   = ContextCompat.getColor(getApplicationContext(),(R.color.dark_gray));
        final Integer Marker = ContextCompat.getColor(getApplicationContext(),(R.color.maroon));
        Integer trT1         = Dark;
        Integer trT2         = Dark;
        Integer trT3         = Dark;
        Integer trT4         = Dark;
        Integer trT5         = Dark;
        final TableRow tr_Top1  = (TableRow) findViewById(R.id.tr_top1);
        final TableRow tr_Top2  = (TableRow) findViewById(R.id.tr_top2);
        final TableRow tr_Top3  = (TableRow) findViewById(R.id.tr_top3);
        final TableRow tr_Top4  = (TableRow) findViewById(R.id.tr_top4);
        final TableRow tr_Top5  = (TableRow) findViewById(R.id.tr_top5);
        final TableRow tr_NT5   = (TableRow) findViewById(R.id.tr_nonT5);
        boolean NT5_Visible     = false;
        switch (pos){
            case 1:
                trT1 = Marker;
                break;
            case 2:
                trT2 = Marker;
                break;
            case 3:
                trT3 = Marker;
                break;
            case 4:
                trT4 = Marker;
                break;
            case 5:
                trT5 = Marker;
                break;
            case 0:
                NT5_Visible = true;
                final SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
                final String username             = mSettings.getString("current_user","");
                int peak_score_server             = mSettings.getInt("peak_server", 0);

                final TextView tv_N5_name = (TextView) findViewById(R.id.tv_nt5_username);
                final TextView tv_N5_peak = (TextView) findViewById(R.id.tv_NT5_Peak);
                set_NT5_Rank();
                tv_N5_peak.setText(String.format(Locale.US,"%d",peak_score_server));
                tv_N5_name.setText(username);
                AdView mAdView = (AdView) findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                break;
        }
        if(NT5_Visible){
            tr_NT5.setVisibility(View.VISIBLE);
        }
        else tr_NT5.setVisibility(View.INVISIBLE);

        tr_Top1.setBackgroundColor(trT1);
        tr_Top2.setBackgroundColor(trT2);
        tr_Top3.setBackgroundColor(trT3);
        tr_Top4.setBackgroundColor(trT4);
        tr_Top5.setBackgroundColor(trT5);
    }
    private  void set_NT5_Rank(){

        SharedPreferences mSettings   = this.getSharedPreferences("Settings", 0);
        int NT5_Ranking               = mSettings.getInt("ut5", 0);

        if (NT5_Ranking == 0) {

            final TextView tv_N5_rank  = (TextView) findViewById(R.id.tv_NT5_Rank);
            int Peaks_Above            = mSettings.getInt("users_higher_peaks", 0);
            boolean Tied               = mSettings.getBoolean("users_equal_peaks", false);
            String Ranking_String      = Integer.toString(Peaks_Above);
            if (Tied) {
                Ranking_String = Ranking_String + this.getString(R.string.T);
            }
            tv_N5_rank.setText(Ranking_String);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, User_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        task2.cancel();
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
        task2.cancel();
    }
    @Override
    public void onResume() {
        super.onResume();
        task2.run();
    }
}
