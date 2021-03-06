package poloapps.orbitfingers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v4.content.ContextCompat;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

//V3.6c created
public class Home_Activity extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_home); //sets activity_home xml file
         final   SharedPreferences mSettings      = this.getSharedPreferences("Settings", 0);
         final   SharedPreferences.Editor editor  = mSettings.edit();
         Integer min_score_value                  = mSettings.getInt         ("min_score",0);
         final   Integer set_peak_min_remaining   = mSettings.getInt         ("set_peak_min",1);
         final   Integer peak_score_value         = mSettings.getInt         ("peakscore", 0);
         final   Boolean logged_in                = mSettings.getBoolean     ("Signed_In", false);
         final   String username                  = mSettings.getString      ("current_user","");

         final Integer Navy_Blue = ContextCompat.getColor(getApplicationContext(),
                 (R.color.navy_blue));
         Integer Orange          = ContextCompat.getColor(getApplicationContext(),(R.color.orange));
         Integer Green           = ContextCompat.getColor(getApplicationContext(),(R.color.green));
         Integer Fade1           = ContextCompat.getColor(getApplicationContext(),(R.color.fade1));
         Integer Yellow          = ContextCompat.getColor(getApplicationContext(),
                 (R.color.bright_yellow));
         final Integer Dark_Gray  = ContextCompat.getColor(getApplicationContext(),
                 (R.color.dark_gray));
         final Integer Light_Gray = ContextCompat.getColor(getApplicationContext(),
                 (R.color.light_gray));
         Integer SMP_Button_Color;

         TextView how_to_link          = findViewById(R.id.how_to_link);
         final Button play_btn         = findViewById(R.id.play_btn);
         final Button Set_Peak_Min_btn = findViewById(R.id.set_peak_min_button);

        how_to_link.setTextColor(Orange);

         if (min_score_value < peak_score_value && set_peak_min_remaining > 0){

             if (  set_peak_min_remaining < 3 )      SMP_Button_Color = Fade1;
             else if (  set_peak_min_remaining < 6 ) SMP_Button_Color = Yellow;
             else                                    SMP_Button_Color = Green;

             Set_Peak_Min_btn.setBackgroundColor(SMP_Button_Color);
             Set_Peak_Min_btn.setTextColor(Dark_Gray);
             Set_Peak_Min_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Set_Peak_Min_btn.setBackgroundColor(Navy_Blue);
                    Integer SMPs_Remaining =  set_peak_min_remaining - 1;
                    editor.putInt("set_peak_min",SMPs_Remaining);
                    editor.putInt("min_score",peak_score_value);
                    editor.apply();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
         }
         else Set_Peak_Min_btn.setBackgroundColor(Dark_Gray);
         if (min_score_value.equals(peak_score_value) && set_peak_min_remaining > 0){
             Set_Peak_Min_btn.setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View arg0) {

                     AlertDialog.Builder builder = new AlertDialog.Builder(
                             Home_Activity.this);
                     builder.setMessage(R.string.smp_equal)
                             .setNegativeButton("Back", null)
                             .create()
                             .show();
                 }
             });
         }
         SetText_TColors();
         how_to_link.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {
                 AlertDialog.Builder builder = new AlertDialog.Builder(
                         Home_Activity.this);
                 builder.setMessage(R.string.alert_1)
                         .setNegativeButton(R.string.back, null)
                         .create()
                         .show();

             }
         });
         play_btn.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {
                 play_btn.setBackgroundColor(Dark_Gray);
                 launch_level();
             }
         });

         AdView mAdView = findViewById(R.id.adView);
         AdRequest adRequest = new AdRequest.Builder().build();
         mAdView.loadAd(adRequest);

         final Button sign_in_button = findViewById(R.id.login_reg);

         if(!logged_in) {
             sign_in_button.setText(this.getString(R.string.login_register));
             sign_in_button.setBackgroundColor(Light_Gray);
             sign_in_button.setTextColor(Navy_Blue);
         }
         else{
             sign_in_button.setText(username);
             sign_in_button.setBackgroundColor(Navy_Blue);
             sign_in_button.setTextColor(Light_Gray);
         }
         sign_in_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {
                 sign_in_button.setBackgroundColor(Dark_Gray);
                 sign_in_button.setTextColor(Light_Gray);
                 Intent myIntent = new Intent(Home_Activity.this, LoginActivity.class);

                 if(logged_in) {
                     myIntent = new Intent(Home_Activity.this, User_Activity.class);
                 }
                 Home_Activity.this.startActivity(myIntent);
             }
         });


         ((TextView) findViewById(R.id.peak_score_value)).setText
                 (String.format(Locale.US,"%d",peak_score_value));


     }
    private void SetText_TColors(){   // set text and color fields

        final int L1_Target_Score = getApplicationContext()
                .getResources().getInteger(R.integer.L1_target_score);
        final int L2_Target_Score = getApplicationContext()
                .getResources().getInteger(R.integer.L2_target_score);
        final int L3_Target_Score = getApplicationContext()
                .getResources().getInteger(R.integer.L3_target_score);
        final int L4_Target_Score = getApplicationContext()
                .getResources().getInteger(R.integer.L4_target_score);

        final int L1_Penalty = getApplicationContext()
                .getResources().getInteger(R.integer.L1_penalty);
        final int L2_Penalty = getApplicationContext()
                .getResources().getInteger(R.integer.L2_penalty);
        final int L3_Penalty = getApplicationContext()
                .getResources().getInteger(R.integer.L3_penalty);
        final int L4_Penalty = getApplicationContext()
                .getResources().getInteger(R.integer.L4_penalty);
        final int L5_Penalty = getApplicationContext()
                .getResources().getInteger(R.integer.L5_penalty);

        SharedPreferences mSettings           = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();

        Integer L1_Color    = ContextCompat.getColor(getApplicationContext(),(R.color.l1col));
        Integer L2_Color    = ContextCompat.getColor(getApplicationContext(),(R.color.l2col));
        Integer L3_Color    = ContextCompat.getColor(getApplicationContext(),(R.color.l3col));
        Integer L4_Color    = ContextCompat.getColor(getApplicationContext(),(R.color.l4col));
        Integer L5_Color    = ContextCompat.getColor(getApplicationContext(),(R.color.l5col));
        Integer Red         = ContextCompat.getColor(getApplicationContext(),(R.color.red));
        Integer Green2      = ContextCompat.getColor(getApplicationContext(),(R.color.green2));
        Integer Light_Gray  = ContextCompat.getColor(getApplicationContext(),(R.color.light_gray));
        Integer Dark_Gray   = ContextCompat.getColor(getApplicationContext(),(R.color.dark_gray));
        //Integer Orange      = ContextCompat.getColor(getApplicationContext(),(R.color.orange));
        Integer Dk_Orange   = ContextCompat.getColor(getApplicationContext(),(R.color.fade1));
        Integer Yellow      = ContextCompat.getColor(getApplicationContext(),
                (R.color.bright_yellow));
        Integer SMP_Color       = Red;


        Integer Min_Score_Value        = mSettings.getInt("min_score",0);
        Integer set_peak_min_remaining = mSettings.getInt("set_peak_min",1);
        Integer current_level          = mSettings.getInt("levl", 1);

      //  Button how_to_button           = (Button)   findViewById(R.id.how_to_btn);
        final Button play_btn          =   findViewById(R.id.play_btn);

        Button Set_Peak_Min_btn        = findViewById(R.id.set_peak_min_button);
        TextView current_level_text    = findViewById(R.id.current_level_text_view);
        TextView level_current         = findViewById(R.id.current_level);
        TextView level_current_ui      = findViewById(R.id.current_level_ui);
        TextView min_score_text        = findViewById(R.id.min_score_text_view);
        TextView min_score_value       = findViewById(R.id.current_min_score);
        TextView min_score_ui          = findViewById(R.id.min_score_ui);
        TextView target_score_text     = findViewById(R.id.target_score_text_view);
        TextView target_score_ui       = findViewById(R.id.target_score_ui);
        TextView target_score          = findViewById(R.id.current_target_score);
        TextView penalty_text          = findViewById(R.id.penalty_text_view);
        TextView SMPs_remaining_text   = findViewById(R.id.smp_text_view);
        TextView SMPs_remaining_value  = findViewById(R.id.remaining_smp_value);
        TextView SMPs_ui               = findViewById(R.id.smp_ui);
        TextView penalty_text_value    = findViewById(R.id.current_penalty);

        penalty_text.setTextColor(Red);
        //how_to_button.setBackgroundColor(Orange);

        penalty_text_value.setTextColor(Red);
        SMPs_remaining_value.setText(String.format(Locale.US,"%d",set_peak_min_remaining));
        level_current.setText(String.format(Locale.US,"%d",current_level));
        min_score_value.setText(String.format(Locale.US,"%d",Min_Score_Value));

        if( set_peak_min_remaining > 0 ){
            Set_Peak_Min_btn.setVisibility(View.VISIBLE);
            if(set_peak_min_remaining < 3){
                SMP_Color = Dk_Orange;
            }else if(set_peak_min_remaining < 6) {
                SMP_Color = Yellow;
            }else SMP_Color = Green2;
        }

        SMPs_remaining_text.setTextColor(SMP_Color);
        SMPs_remaining_value.setTextColor(SMP_Color);
        SMPs_ui.setTextColor(SMP_Color);

        switch (current_level){
            case 1:
                play_btn.setBackgroundColor(L1_Color);
                play_btn.setTextColor(Light_Gray);
                current_level_text.setTextColor(L1_Color);
                target_score_text.setTextColor(L2_Color);
                min_score_text.setTextColor(L1_Color);
                level_current_ui.setTextColor(L1_Color);
                target_score_ui.setTextColor(L2_Color);
                min_score_ui.setTextColor(L1_Color);
                min_score_value.setTextColor(L1_Color);
                level_current.setTextColor(L1_Color);
                target_score_text.setText(R.string.target_score_tv);
                target_score.setTextColor(L2_Color);
                penalty_text_value.setText(String.format(Locale.US,"%d",L1_Penalty));
                target_score.setText(String.format(Locale.US,"%d",L1_Target_Score));
                break;

            case 2:
                play_btn.setBackgroundColor(L2_Color);
                current_level_text.setTextColor(L2_Color);
                target_score_text.setTextColor(L3_Color);
                min_score_text.setTextColor(L2_Color);
                level_current.setTextColor(L2_Color);
                level_current_ui.setTextColor(L2_Color);
                target_score_ui.setTextColor(L3_Color);
                min_score_ui.setTextColor(L2_Color);
                min_score_value.setTextColor(L2_Color);
                target_score.setTextColor(L3_Color);
                penalty_text_value.setText(String.format(Locale.US,"%d",L2_Penalty));
                target_score.setText(String.format(Locale.US,"%d",L2_Target_Score));
                break;

            case 3:
                play_btn.setBackgroundColor(L3_Color);
                play_btn.setTextColor(Dark_Gray);
                current_level_text.setTextColor(L3_Color);
                target_score_text.setTextColor(L4_Color);
                min_score_text.setTextColor(L3_Color);
                level_current.setTextColor(L3_Color);
                min_score_value.setTextColor(L3_Color);
                level_current_ui.setTextColor(L3_Color);
                target_score_ui.setTextColor(L4_Color);
                min_score_ui.setTextColor(L3_Color);
                target_score.setTextColor(L4_Color);
                target_score.setText(String.format(Locale.US,"%d",L3_Target_Score));
                penalty_text_value.setText(String.format(Locale.US,"%d",L3_Penalty));
                break;

            case 4:
                play_btn.setBackgroundColor(L4_Color);
                play_btn.setTextColor(Dark_Gray);
                current_level_text.setTextColor(L4_Color);
                target_score_text.setTextColor(L5_Color);
                min_score_text.setTextColor(L4_Color);
                level_current.setTextColor(L4_Color);
                min_score_value.setTextColor(L4_Color);
                level_current_ui.setTextColor(L4_Color);
                target_score_ui.setTextColor(L5_Color);
                min_score_ui.setTextColor(L4_Color);
                target_score.setTextColor(L5_Color);
                target_score.setText(String.format(Locale.US,"%d",L4_Target_Score));
                penalty_text_value.setText(String.format(Locale.US,"%d",L4_Penalty));
                break;

            case 5:
                play_btn.setBackgroundColor(L5_Color);
                play_btn.setTextColor(Dark_Gray);
                current_level_text.setTextColor(L5_Color);
                level_current.setTextColor(L5_Color);
                min_score_text.setTextColor(L5_Color);
                min_score_value.setTextColor(L5_Color);
                level_current_ui.setTextColor(L5_Color);
                target_score_ui.setTextColor(Dark_Gray);
                min_score_ui.setTextColor(L5_Color);
                target_score_text.setText(R.string.empty);
                target_score.setText(R.string.empty);
                penalty_text_value.setText(String.format(Locale.US,"%d",L5_Penalty));
                break;
        }
        editor.apply();
    }

    private void launch_level(){
        Intent myIntent = new Intent(Home_Activity.this, OF_Main_Container_Activity.class);
        Home_Activity.this.startActivity(myIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final   SharedPreferences mSettings      = this.getSharedPreferences("Settings", 0);
        final   SharedPreferences.Editor editor  = mSettings.edit();
        switch (item.getItemId()) {
            case R.id.action_privacy:
                // User chose the "Settings" item, show the app settings UI...
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        Home_Activity.this);
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
                        Home_Activity.this);
                builder2.setMessage(R.string.tou_actual)
                        .setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;
            case R.id.action_reset:
                 builder = new AlertDialog.Builder(
                        Home_Activity.this);
                builder.setMessage(R.string.alert_2)
                        .setPositiveButton(R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        final Integer Dark_Gray  = ContextCompat.getColor(getApplicationContext(),
                                                (R.color.dark_gray));
                                        final Button Set_Peak_Min_btn = findViewById(R.id.set_peak_min_button);
                                        Set_Peak_Min_btn.setBackgroundColor(Dark_Gray);
                                        editor.putInt    ("levl",          1);
                                        editor.putInt    ("peakscore",     0);
                                        editor.putInt    ("min_score",     0);
                                        editor.putInt    ("current_score", 0);
                                        editor.putInt    ("set_peak_min",  1);
                                        editor.putBoolean("Signed_In", false);
                                        editor.putString ("current_user","" );
                                        editor.putString ("rank_message","" );
                                        editor.putInt    ("peak_server",   0);
                                        editor.putInt    ("min_server",    0);
                                        editor.putInt    ("smp_server",    1);
                                        editor.apply();
                                        SetText_TColors();
                                        ((TextView) findViewById(R.id.peak_score_value)).setText
                                                (String.format(Locale.US,"%d",0));
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(R.string.no,null)
                        .create()
                        .show();
                return true;
            case R.id.action_about:
                // User chose the "Settings" item, show the app settings UI...
                AlertDialog.Builder builder3 = new AlertDialog.Builder(
                        Home_Activity.this);
                @SuppressLint("InflateParams") View mView3 =
                        getLayoutInflater().inflate(R.layout.about_dialog,null);
                builder3.setView(mView3);
                final AlertDialog dialog3 =  builder3.create();
                Button Back_btn                = mView3.findViewById(R.id.back_btn);
                Back_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog3.cancel();
                    }
                });
                dialog3.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}