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

public class MainMenu_Activity extends AppCompatActivity {
    //InterstitialAd mInterstitialAd;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main); //sets activity_main xml file
         SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
         final SharedPreferences.Editor editor = mSettings.edit();


         Integer PS = mSettings.getInt("peakscore", 0);

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
         SetTextCols();
         how_to_button.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {

             }
         });


         play_btn.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {
                 play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), (R.color.dkgry)));
                 luanch_level();
             }
         });


         AdView mAdView = (AdView) findViewById(R.id.adView);
         AdRequest adRequest = new AdRequest.Builder().build();
         mAdView.loadAd(adRequest);

         Button rbutton = (Button) findViewById(R.id.reset1);
         rbutton.setBackgroundColor(ContextCompat.getColor(this, (R.color.dkgry)));
         rbutton.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {
                 int PS = 0;
                 editor.putInt("levl", 1);
                 editor.putInt("peakscore", PS);
                 editor.commit();
                 SetTextCols();
                 ((TextView) findViewById(R.id.peaksc)).setText(Integer.toString(PS));
             }
         });

         ((TextView) findViewById(R.id.peaksc)).setText(Integer.toString(PS));


     }
    private void SetTextCols(){
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();
        Integer current_level= mSettings.getInt("levl", 1);
        Button how_to_button = (Button) findViewById(R.id.how_to_btn);
        how_to_button.setBackgroundColor(ContextCompat.getColor(this,(R.color.orange)));
        final Button play_btn = (Button) findViewById(R.id.play_btn);
        TextView current_level_text = (TextView) findViewById(R.id.current_level_text_view);
        TextView min_score_text = (TextView) findViewById(R.id.min_score_text_view);
        TextView target_score_text = (TextView) findViewById(R.id.target_score_text_view);
        TextView penalty_text = (TextView) findViewById(R.id.penalty_text_view);
        penalty_text.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),(R.color.red)));

        switch (current_level){
            case 1:
                editor.putInt("ls", 1);
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),(R.color.blue1)));
                play_btn.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l5col)));//l5col = white
                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.blue1)));
                target_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l2col)));
                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.blue1)));
                current_level_text.setText(getString(R.string.current_level_tv));
                target_score_text.setText(getString(R.string.target_score_tv));
                min_score_text.setText(getString(R.string.min_score_tv));
                penalty_text.setText(getString(R.string.penalty_tv));

                break;
            case 2:
                editor.putInt("ls", 2);
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),(R.color.l2col)));
                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l2col)));
                current_level_text.setText(getString(R.string.current_level2_tv));
                target_score_text.setText(getString(R.string.target2_score_tv));
                min_score_text.setText(getString(R.string.min2_score_tv));
                penalty_text.setText(getString(R.string.penalty2_tv));
                target_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l3col)));
                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l2col)));
                break;
            case 3:
                editor.putInt("ls", 3);
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),(R.color.l3col)));
                play_btn.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.dkgry)));
                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l3col)));
                current_level_text.setText(getString(R.string.current_level3_tv));
                target_score_text.setText(getString(R.string.target3_score_tv));
                min_score_text.setText(getString(R.string.min3_score_tv));
                penalty_text.setText(getString(R.string.penalty3_tv));
                target_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l4col)));
                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l3col)));
                break;
            case 4:
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),(R.color.l4col)));
                play_btn.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.dkgry)));
                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l4col)));
                current_level_text.setText(getString(R.string.current_level4_tv));
                target_score_text.setText(getString(R.string.target4_score_tv));
                min_score_text.setText(getString(R.string.min4_score_tv));
                penalty_text.setText(getString(R.string.penalty4_tv));
                target_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l5col)));
                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l4col)));
                editor.putInt("ls", 4);
                break;
            case 5:
                play_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),(R.color.l5col)));
                play_btn.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.dkgry)));
                current_level_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l5col)));
                current_level_text.setText(getString(R.string.current_level5_tv));
                target_score_text.setText(getString(R.string.empty));
                min_score_text.setText(getString(R.string.min5_score_tv));
                penalty_text.setText(getString(R.string.penalty5_tv));
                min_score_text.setTextColor(ContextCompat.getColor(getApplicationContext(),(R.color.l5col)));
                editor.putInt("ls", 4);
                editor.putInt("ls", 5);
                break;
        }
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void luanch_level(){
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