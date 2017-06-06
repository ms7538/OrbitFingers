package poloapps.orbitfingers;


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

import static java.security.AccessController.getContext;

//V3.6c created
public class MainMenu_Activity extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main); //sets activity_main xml file
         final   SharedPreferences mSettings      = this.getSharedPreferences("Settings", 0);
         final   SharedPreferences.Editor editor  = mSettings.edit();
         Integer min_score_value                  = mSettings.getInt         ("min_score",0);
         final   Integer set_peak_min_remaining   = mSettings.getInt         ("set_peak_min",2);
         final   Integer peak_score_value         = mSettings.getInt         ("peakscore", 0);
         final   Boolean logged_in                = mSettings.getBoolean     ("Signed_In", false);
         final   String username                  = mSettings.getString      ("current_user","");

         final Integer Navy_Blue = ContextCompat.getColor(getApplicationContext(),
                                                                               (R.color.navy_blue));
         Integer Orange      = ContextCompat.getColor(getApplicationContext(),(R.color.orange));
         Integer Green       = ContextCompat.getColor(getApplicationContext(),(R.color.green));
         Integer Red         = ContextCompat.getColor(getApplicationContext(),(R.color.red));

         final Integer Dark_Gray   = ContextCompat.getColor(getApplicationContext(),
                                                                               (R.color.dark_gray));
         final Integer Light_Gray   = ContextCompat.getColor(getApplicationContext(),
                                                                              (R.color.light_gray));

         Button how_to_button              = (Button) findViewById(R.id.how_to_btn);
         final Button play_btn             = (Button) findViewById(R.id.play_btn);
         final Button Set_Peak_Min_btn     = (Button) findViewById(R.id.set_peak_min_button);

         how_to_button.setBackgroundColor(Orange);

         if (min_score_value < peak_score_value && set_peak_min_remaining > 0){

            Set_Peak_Min_btn.setBackgroundColor(Green);
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
        else{
            Set_Peak_Min_btn.setBackgroundColor(Dark_Gray);
        }

         SetText_TColors();

         how_to_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {

             }
         });


         play_btn.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {
                 play_btn.setBackgroundColor(Dark_Gray);
                 launch_level();
             }
         });


         AdView mAdView = (AdView) findViewById(R.id.adView);
         AdRequest adRequest = new AdRequest.Builder().build();
         mAdView.loadAd(adRequest);

         final Button sign_in_button = (Button) findViewById(R.id.login_reg);

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
                 Intent myIntent = new Intent(MainMenu_Activity.this, LoginActivity.class);

                 if(logged_in) {
                     myIntent = new Intent(MainMenu_Activity.this, UserAreaActivity.class);
                 }
                 MainMenu_Activity.this.startActivity(myIntent);
             }
         });

         final Button reset_button = (Button) findViewById(R.id.reset1);
         if (peak_score_value > 0 || logged_in) {
             reset_button.setBackgroundColor(Red);
             reset_button.setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View arg0) {
                     reset_button.setBackgroundColor(Navy_Blue);
                     Set_Peak_Min_btn.setBackgroundColor(Dark_Gray);
                     editor.putInt    ("levl", 1);
                     editor.putInt    ("peakscore", 0);
                     editor.putInt    ("min_score", 0);
                     editor.putInt    ("current_score", 0);
                     editor.putInt    ("set_peak_min", 2);
                     editor.putBoolean("Signed_In", false);
                     editor.commit();
                     SetText_TColors();
                     ((TextView) findViewById(R.id.peak_score_value)).setText
                                                                (String.format(Locale.US,"%d",0));
                     Intent intent = getIntent();
                     finish();
                     startActivity(intent);
                 }
             });
         }
         else   reset_button.setBackgroundColor(Dark_Gray);

         ((TextView) findViewById(R.id.peak_score_value)).setText
                                                   (String.format(Locale.US,"%d",peak_score_value));


     }
    private void SetText_TColors(){   // set text and color fields

        final int L1_Target_Score = 100;
        final int L2_Target_Score = 300;
        final int L3_Target_Score = 600;
        final int L4_Target_Score = 1000;

        final int L1_Penalty = 1;
        final int L2_Penalty = 5;
        final int L3_Penalty = 10;
        final int L4_Penalty = 15;
        final int L5_Penalty = 20;

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
        Integer Orange      = ContextCompat.getColor(getApplicationContext(),(R.color.orange));

        Integer Min_Score_Value = mSettings.getInt("min_score",0);
        Integer set_peak_min_remaining = mSettings.getInt("set_peak_min",2);
        Integer current_level          = mSettings.getInt("levl", 1);
        Button how_to_button           = (Button) findViewById(R.id.how_to_btn);
        final Button play_btn          = (Button) findViewById(R.id.play_btn);
        Button reset_button            = (Button) findViewById(R.id.reset1);
        Button Set_Peak_Min_btn        = (Button) findViewById(R.id.set_peak_min_button);
        TextView current_level_text    = (TextView) findViewById(R.id.current_level_text_view);
        TextView level_current         = (TextView) findViewById(R.id.current_level);
        TextView level_current_ui      = (TextView) findViewById(R.id.current_level_ui);
        TextView min_score_text        = (TextView) findViewById(R.id.min_score_text_view);
        TextView min_score_value       = (TextView) findViewById(R.id.current_min_score);
        TextView min_score_ui          = (TextView) findViewById(R.id.min_score_ui);
        TextView target_score_text     = (TextView) findViewById(R.id.target_score_text_view);
        TextView target_score_ui       = (TextView) findViewById(R.id.target_score_ui);
        TextView target_score          = (TextView) findViewById(R.id.current_target_score);
        TextView penalty_text          = (TextView) findViewById(R.id.penalty_text_view);
        TextView SMPs_remaining_text   = (TextView) findViewById(R.id.smp_text_view);
        TextView SMPs_remaining_value  = (TextView) findViewById(R.id.remaining_smp_value);
        TextView SMPs_ui               = (TextView) findViewById(R.id.smp_ui);
        TextView penalty_text_value    = (TextView) findViewById(R.id.current_penalty);
        penalty_text.setTextColor(Red);
        how_to_button.setBackgroundColor(Orange);
        reset_button.setBackgroundColor(Red);
        penalty_text_value.setTextColor(Red);
        SMPs_remaining_value.setText(String.format(Locale.US,"%d",set_peak_min_remaining));
        level_current.setText(String.format(Locale.US,"%d",current_level));
        min_score_value.setText(String.format(Locale.US,"%d",Min_Score_Value));

        if( set_peak_min_remaining > 0 ){
            Set_Peak_Min_btn.setVisibility(View.VISIBLE);
            SMPs_remaining_text.setTextColor(Green2);
            SMPs_remaining_value.setTextColor(Green2);
            SMPs_ui.setTextColor(Green2);

        }
        else{
            Set_Peak_Min_btn.setVisibility(View.INVISIBLE);
            SMPs_remaining_text.setTextColor(Red);
            SMPs_remaining_value.setTextColor(Red);
            SMPs_ui.setTextColor(Red);
        }


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
                target_score.setText(String.format(Locale.US,"%d",L1_Target_Score));
                target_score_text.setText(R.string.target_score_tv);
                target_score.setTextColor(L2_Color);
                penalty_text_value.setText(String.format(Locale.US,"%d",L1_Penalty));
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
                target_score.setText(String.format(Locale.US,"%d",L2_Target_Score));
                target_score.setTextColor(L3_Color);
                penalty_text_value.setText(String.format(Locale.US,"%d",L2_Penalty));
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
                target_score.setText(String.format(Locale.US,"%d",L3_Target_Score));
                target_score.setTextColor(L4_Color);
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
                target_score.setText(String.format(Locale.US,"%d",L4_Target_Score));
                target_score.setTextColor(L5_Color);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void launch_level(){
        Intent myIntent = new Intent(MainMenu_Activity.this, OF_Activity.class);
        MainMenu_Activity.this.startActivity(myIntent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}