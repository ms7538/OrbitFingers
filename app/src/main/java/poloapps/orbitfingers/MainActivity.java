package poloapps.orbitfingers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
    SharedPreferences.Editor editor = mSettings.edit();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor.putInt("LVL", 0); editor.commit();
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
        button2.setBackgroundColor(getResources().getColor(R.color.dkgry));

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
