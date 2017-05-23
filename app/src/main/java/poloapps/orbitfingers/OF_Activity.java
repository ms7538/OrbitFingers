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


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        SharedPreferences.Editor editor = mSettings.edit();

        /*editor.putString( "scale", String.valueOf(this
                .getResources().getDisplayMetrics().density/2));*/

        editor.putString( "max_h", String.valueOf(displayMetrics.heightPixels));
        editor.putString( "max_w", String.valueOf(displayMetrics.widthPixels));
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