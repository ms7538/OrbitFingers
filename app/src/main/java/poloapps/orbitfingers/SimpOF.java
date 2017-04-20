package poloapps.orbitfingers;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;

public class SimpOF extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        Integer LS = mSettings.getInt("ls", 1);


        double scalemultiplier = 1.0;

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        bar.setTitle("ORBITFINGERS");

        float density = this.getResources().getDisplayMetrics().density;

        scalemultiplier = density / 2;
        String plain = String.format( "%.1f", scalemultiplier );
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString( "scale", plain );
        editor.commit();



        switch (LS){
            case 1:
                editor.putInt("LSS", 0);
                editor.commit();
                break;
            case 2:
                editor.putInt("LSS", 100);
                editor.commit();
                break;
            case 3:
                editor.putInt("LSS", 300);
                editor.commit();
                break;
            case 4:
                editor.putInt("LSS", 600);
                editor.commit();
                break;
            case 5:
                editor.putInt("LSS", 1000);
                editor.commit();
                break;

        }
        View view = new SOFview(getApplicationContext());
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
        Intent intent = new Intent(this, MainActivity.class);
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