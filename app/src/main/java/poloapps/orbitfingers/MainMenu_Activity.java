package poloapps.orbitfingers;


import android.content.Context;
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

public class MainMenu_Activity extends AppCompatActivity {
    //InterstitialAd mInterstitialAd;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main); //sets activity_main xml file
         final SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
         final SharedPreferences.Editor editor = mSettings.edit();
         Integer min_score_value = mSettings.getInt("min_score",0);
         final Integer peak_score_value = mSettings.getInt("peakscore", 0);

 /*        Log.i("M123A", Integer.toString(current_level));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6002737231550640/9358444811");
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                Intent myIntent = new Intent(MainMenu_Activity.this, OF_Activity.class);
                MainMenu_Activity.this.startActivity(myIntent);
            }
        });*/


         Button how_to_button = (Button) findViewById(R.id.how_to_btn);
         how_to_button.setBackgroundColor(ContextCompat.getColor(this, (R.color.orange)));

         final Button play_btn = (Button) findViewById(R.id.play_btn);

         final Button Set_Peak_Min_btn = (Button) findViewById(R.id.set_peak_min);

        if (min_score_value < peak_score_value){

            Set_Peak_Min_btn.setBackgroundColor(ContextCompat.getColor(this, (R.color.navy_blue)));
            Set_Peak_Min_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Set_Peak_Min_btn.setBackgroundColor(ContextCompat.
                            getColor(getApplicationContext(), (R.color.green)));

                    editor.putInt("min_score",peak_score_value);
                    editor.commit();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });


        }
        else{
            Set_Peak_Min_btn.setBackgroundColor(ContextCompat.getColor(this, (R.color.dark_gray)));
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
                 play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                         (R.color.dark_gray)));
                 launch_level();
             }
         });


         AdView mAdView = (AdView) findViewById(R.id.adView);
         AdRequest adRequest = new AdRequest.Builder().build();
         mAdView.loadAd(adRequest);

         Button reset_button = (Button) findViewById(R.id.reset1);
         reset_button.setBackgroundColor(ContextCompat.getColor(this, (R.color.red)));
         reset_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {
                 Set_Peak_Min_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                         (R.color.dark_gray)));
                 editor.putInt("levl", 1);
                 editor.putInt("peakscore", 0);
                 editor.putInt("min_score",0);
                 editor.putInt("current_score",0);
                 editor.commit();
                 SetText_TColors();
                 ((TextView) findViewById(R.id.peak_score_value)).setText(Integer.toString(0));
             }
         });

         ((TextView) findViewById(R.id.peak_score_value)).setText
                                                            (Integer.toString(peak_score_value));


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

        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();
        Integer Min_Score_Value = mSettings.getInt("min_score",0);
        Integer current_level= mSettings.getInt("levl", 1);
        Button how_to_button = (Button) findViewById(R.id.how_to_btn);
        how_to_button.setBackgroundColor(ContextCompat.getColor(this,(R.color.orange)));
        final Button play_btn = (Button) findViewById(R.id.play_btn);
        TextView current_level_text = (TextView) findViewById(R.id.current_level_text_view);
        TextView level_current = (TextView) findViewById(R.id.current_level);
        TextView level_current_ui = (TextView) findViewById(R.id.current_level_ui);
        TextView min_score_text = (TextView) findViewById(R.id.min_score_text_view);
        TextView min_score_value = (TextView) findViewById(R.id.current_min_score);
        TextView min_score_ui = (TextView) findViewById(R.id.min_score_ui);
        TextView target_score_text = (TextView) findViewById(R.id.target_score_text_view);
        TextView target_score_ui = (TextView) findViewById(R.id.target_score_ui);
        TextView target_score = (TextView) findViewById(R.id.current_target_score);
        TextView penalty_text = (TextView) findViewById(R.id.penalty_text_view);
        penalty_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.red)));
        TextView penalty_text_value = (TextView) findViewById(R.id.current_penalty);
        penalty_text_value.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.red)));
        level_current.setText(Integer.toString(current_level));
        min_score_value.setText(Integer.toString(Min_Score_Value));

        switch (current_level){
            case 1:
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l1col)));

                play_btn.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                            (R.color.light_gray)));

                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l1col)));

                target_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l1col)));

                level_current_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l1col)));

                target_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                min_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l1col)));

                min_score_value.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l1col)));

                level_current.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l1col)));
                target_score.setText(Integer.toString(L1_Target_Score));
                target_score_text.setText(R.string.target_score_tv);
                target_score.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                penalty_text_value.setText(Integer.toString(L1_Penalty));
                break;
            case 2:
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                target_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                level_current.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                level_current_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                target_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                min_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                min_score_value.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l2col)));

                target_score.setText(Integer.toString(L2_Target_Score));
                target_score.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                penalty_text_value.setText(Integer.toString(L2_Penalty));
                break;
            case 3:
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                play_btn.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                            (R.color.dark_gray)));

                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                target_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                level_current.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                min_score_value.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                level_current_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                target_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                min_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l3col)));

                target_score.setText(Integer.toString(L3_Target_Score));

                target_score.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                penalty_text_value.setText(Integer.toString(L3_Penalty));
                break;
            case 4:
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                play_btn.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                            (R.color.dark_gray)));
                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                target_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                level_current.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                min_score_value.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                level_current_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                target_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                min_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l4col)));

                target_score.setText(Integer.toString(L4_Target_Score));

                target_score.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                penalty_text_value.setText(Integer.toString(L4_Penalty));
                break;
            case 5:
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                play_btn.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                            (R.color.dark_gray)));

                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                level_current.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                min_score_value.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                level_current_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                target_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                            (R.color.dark_gray)));

                min_score_ui.setTextColor(ContextCompat.getColor(getApplicationContext(),
                                                                                (R.color.l5col)));

                target_score_text.setText(R.string.empty);
                target_score.setText(R.string.empty);
                penalty_text_value.setText(Integer.toString(L5_Penalty));
                break;
        }
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void launch_level(){
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
        Intent myIntent = new Intent(MainMenu_Activity.this, OF_Activity.class);
        MainMenu_Activity.this.startActivity(myIntent);
    //    }
    }
//    private void requestNewInterstitial() {
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//
//        mInterstitialAd.loadAd(adRequest);
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}