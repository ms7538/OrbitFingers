package poloapps.orbitfingers;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;



public class OF_Main_Container_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);


        android.support.v7.app.ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle("ORBITFINGERS");


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        SharedPreferences.Editor editor = mSettings.edit();


        editor.putString( "max_h", String.valueOf(displayMetrics.heightPixels));
        editor.putString( "max_w", String.valueOf(displayMetrics.widthPixels));
        editor.apply();

        View view = new OF_Main_Activity(getApplicationContext());
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final   SharedPreferences mSettings      = this.getSharedPreferences("Settings", 0);
        final   SharedPreferences.Editor editor  = mSettings.edit();
        String Sound;
        switch (item.getItemId()) {
            case R.id.action_settings:
                String Sound_on  = this.getString(R.string.on);
                String Sound_off = this.getString(R.string.off);
                final boolean OnOff    = mSettings.getBoolean("sound",true);
                if(OnOff) Sound = Sound_off;
                else Sound = Sound_on;
                // User chose the "Settings" item, show the app settings UI...
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        OF_Main_Container_Activity.this);
                builder.setMessage("Sound")
                        .setPositiveButton(Sound, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(OnOff) editor.putBoolean("sound",false);
                                else editor.putBoolean("sound",true);
                                editor.apply();
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}