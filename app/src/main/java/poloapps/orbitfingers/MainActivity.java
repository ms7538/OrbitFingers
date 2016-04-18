package poloapps.orbitfingers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends ActionBarActivity {
    InterstitialAd mInterstitialAd;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();
        Integer lv= mSettings.getInt("levl", 1);
         Integer PS= mSettings.getInt("peakscore", 0);
        //Log.i("M123A", Integer.toString(lv));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6002737231550640/9358444811");
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                Intent myIntent = new Intent(MainActivity.this, SimpOF.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        Button button = (Button) findViewById(R.id.simple);
        button.setBackgroundColor(getResources().getColor(R.color.green));
        Button button2 = (Button) findViewById(R.id.simple2);
        Button button3 = (Button) findViewById(R.id.simple3);
        Button button4 = (Button) findViewById(R.id.simple4);
        Button button5 = (Button) findViewById(R.id.simple5);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                editor.putInt("ls", 1);
                editor.commit();
                luanchlevel();
            }
        });
        if (lv >=2) {
            button2.setBackgroundColor(getResources().getColor(R.color.green));
            button2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    editor.putInt("ls", 2);
                    editor.commit();
                    luanchlevel();
                }
            });
        }
        if (lv >=3) {

            button3.setBackgroundColor(getResources().getColor(R.color.green));
            button3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    editor.putInt("ls", 3);
                    editor.commit();
                    luanchlevel();
                }
            });
        }
        if (lv >=4) {
            button4.setBackgroundColor(getResources().getColor(R.color.green));
            button4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    editor.putInt("ls", 4);
                    editor.commit();
                    luanchlevel();
                }
            });
        }
        if (lv >=5) {
            button5.setBackgroundColor(getResources().getColor(R.color.green));
            button5.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    editor.putInt("ls", 5);
                    editor.commit();
                    luanchlevel();
                }
            });
        }
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
         Button rbutton = (Button) findViewById(R.id.reset1);
         rbutton.setBackgroundColor(getResources().getColor(R.color.dkgry));
         rbutton.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View arg0) {
                int PS=0;
                 editor.putInt("peakscore",PS);
                 editor.commit();
                 ((TextView) findViewById(R.id.peaksc)).setText("                                    " + Integer.toString(PS));
             }
         });
         if (PS<10) {
             ((TextView) findViewById(R.id.peaksc)).setText("                                    " + Integer.toString(PS));
         }else if (PS<100){
             ((TextView) findViewById(R.id.peaksc)).setText("                                   " + Integer.toString(PS));
         }else if (PS<1000){
             ((TextView) findViewById(R.id.peaksc)).setText("                                  " + Integer.toString(PS));
         }else if (PS<10000) {
             ((TextView) findViewById(R.id.peaksc)).setText("                                 " + Integer.toString(PS));
         }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void luanchlevel(){
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
        Intent myIntent = new Intent(MainActivity.this, SimpOF.class);
        MainActivity.this.startActivity(myIntent);
       // }
    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}