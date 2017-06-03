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




        Boolean logged_in = mSettings.getBoolean("Signed_In", false);

        if (!logged_in){

            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String username = intent.getStringExtra("username");
            int peak = intent.getIntExtra("peak", -1);
            int min = intent.getIntExtra("min", -1);
            int smp = intent.getIntExtra("smp", -1);
            editor.putString("current_user",username);
            editor.putBoolean("Signed_In", true);
            editor.putInt("peak_server",peak);
            editor.putInt("min_server",min);
            editor.putInt("smp_server",smp);
            editor.apply();
        }
        Integer min_score_device    = mSettings.getInt("min_score",0);
        Integer smp_device          = mSettings.getInt("set_peak_min",2);
        Integer peak_score_device   = mSettings.getInt("peakscore", 0);
        Integer min_score_server    = mSettings.getInt("min_server",0);
        Integer smp_server          = mSettings.getInt("smp_server",0);
        Integer peak_score_server   = mSettings.getInt("peak_server", 0);

        TextView tv_Device_Peak_value  = (TextView) findViewById(R.id.tv_Peak_Device_value);
        TextView tv_Device_Min_value   = (TextView) findViewById(R.id.tv_Min_Device_value);
        TextView tv_Device_SMP_value   = (TextView) findViewById(R.id.tv_Device_SMP);

        TextView tv_Server_Peak_value  = (TextView) findViewById(R.id.tv_Server_Peak);
        TextView tv_Server_Min_value   = (TextView) findViewById(R.id.tv_Server_Min);
        TextView tv_Server_SMP_value   = (TextView) findViewById(R.id.tv_Server_SMP);

        tv_Device_Peak_value.setText(String.format(Locale.US,"%d",peak_score_device));
        tv_Device_Min_value.setText(String.format(Locale.US,"%d",min_score_device));
        tv_Device_SMP_value.setText(String.format(Locale.US,"%d",smp_device));
        tv_Server_Peak_value.setText(String.format(Locale.US,"%d",peak_score_server));
        tv_Server_Min_value.setText(String.format(Locale.US,"%d",min_score_server));
        tv_Server_SMP_value.setText(String.format(Locale.US,"%d",smp_server));

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