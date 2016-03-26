package poloapps.orbitfingers;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SimpOF extends ActionBarActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        Integer LS= mSettings.getInt("ls", 1);
        //View view = new SOFview(getApplicationContext());
        String scalemultiplier="1";
        float density = this.getResources().getDisplayMetrics().density;
        if (density == 4.0) {
            scalemultiplier= "2.5";
        }
        if (density == 3.0) {
            scalemultiplier="2.0";
        }
        if (density == 2.0) {
            scalemultiplier="1";
        }
        if (density == 1.5) {
            scalemultiplier=".6";
        }
        if (density == 1.0) {
            scalemultiplier=".5";
        }

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("scale", scalemultiplier);
        editor.commit();
        switch (LS){
            case 1:
                View view = new SOFview(getApplicationContext());
                setContentView(view);
                view.setBackgroundColor(Color.parseColor("#090404"));
                break;
            case 2:
                View view2 = new SOFview2(getApplicationContext());
                setContentView(view2);
                view2.setBackgroundColor(Color.parseColor("#090404"));
                break;
            case 3:
                View view3 = new SOFview3(getApplicationContext());
                setContentView(view3);
                view3.setBackgroundColor(Color.parseColor("#090404"));
                break;
            case 4:
                View view4 = new SOFview4(getApplicationContext());
                setContentView(view4);
                view4.setBackgroundColor(Color.parseColor("#090404"));
                break;
            case 5:
                View view5 = new SOFview5(getApplicationContext());
                setContentView(view5);
                view5.setBackgroundColor(Color.parseColor("#090404"));
                break;

        }

        //

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}