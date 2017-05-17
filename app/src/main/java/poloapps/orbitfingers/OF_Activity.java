package poloapps.orbitfingers;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static java.security.AccessController.getContext;

public class OF_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);


        android.support.v7.app.ActionBar bar = getSupportActionBar();

        bar.setTitle("ORBITFINGERS");

        float density = this.getResources().getDisplayMetrics().density;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Log.i("ob1 screen height", String.valueOf(height) );
        Log.i("ob1 screen width", String.valueOf(width) );
        Log.i("ob1 float density", String.valueOf(density) );
        double scale_multiplier = density /2;
/*
        if( density > 2.0 ){

            scale_multiplier = density / 2;

        }
        else if ( density){

        }*/
        String format_SM_string = String.valueOf(scale_multiplier);

       // Log.i("formatted density",format_SM_string);
        SharedPreferences.Editor editor = mSettings.edit();

        editor.putString( "scale", format_SM_string );

        editor.commit();

        View view = new OFView_Activity(getApplicationContext());
        setContentView(view);
        view.setBackgroundColor(Color.parseColor("#090404"));
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
        return;
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