package poloapps.orbitfingers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        final SharedPreferences.Editor editor = mSettings.edit();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");


        int peak = intent.getIntExtra("peak", -1);
        int min = intent.getIntExtra("min", -1);
        int smp = intent.getIntExtra("smp", -1);

        Boolean logged_in = mSettings.getBoolean("Signed_In", false);
        if (!logged_in){
            editor.putString("current_user",username);
            editor.putBoolean("Signed_In", true);
            editor.apply();
        }

        TextView tvWelcomeMsg   = (TextView) findViewById(R.id.tvWelcomeMsg);
        EditText etUsername     = (EditText) findViewById(R.id.etUsername);
        EditText etPeak_display = (EditText) findViewById(R.id.etPeak);
        EditText etMin_display  = (EditText) findViewById(R.id.etMin);
        EditText etSMP_display  = (EditText) findViewById(R.id.etSMP);
        // Display user details
        String message = name + " welcome to your user area";
        tvWelcomeMsg.setText(message);
        etUsername.setText(username);
        etPeak_display.setText(String.format(Locale.US,"%d",peak));
        etMin_display.setText(String.format(Locale.US,"%d",min));
        etSMP_display.setText(String.format(Locale.US,"%d",smp));
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
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}