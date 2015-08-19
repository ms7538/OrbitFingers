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
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        String lvl= mSettings.getString("level", "");
        Log.i("M123A",lvl);
        Button button = (Button) findViewById(R.id.simple);
        button.setBackgroundColor(getResources().getColor(R.color.green));
        button.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, SimpOF.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        Button button2 = (Button) findViewById(R.id.simple2);
        Button button3 = (Button) findViewById(R.id.simple3);
        Button button4 = (Button) findViewById(R.id.simple4);
        Button button5 = (Button) findViewById(R.id.simple5);
        Button button6 = (Button) findViewById(R.id.simple6);


        if (lvl=="one"){
            button2.setBackgroundColor(getResources().getColor(R.color.green));
        }else if (lvl=="0"){
            button2.setBackgroundColor(getResources().getColor(R.color.red));
            button3.setBackgroundColor(getResources().getColor(R.color.red));
            button4.setBackgroundColor(getResources().getColor(R.color.red));
            button5.setBackgroundColor(getResources().getColor(R.color.red));
            button6.setBackgroundColor(getResources().getColor(R.color.red));

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
