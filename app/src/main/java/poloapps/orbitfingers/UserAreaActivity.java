package poloapps.orbitfingers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        int peak = intent.getIntExtra("peak", -1);
        int min = intent.getIntExtra("min", -1);
        TextView tvWelcomeMsg   = (TextView) findViewById(R.id.tvWelcomeMsg);
        EditText etUsername     = (EditText) findViewById(R.id.etUsername);
        EditText etPeak_display = (EditText) findViewById(R.id.etPeak);
        EditText etMin_display = (EditText) findViewById(R.id.etMin);
        // Display user details
        String message = name + " welcome to your user area";
        tvWelcomeMsg.setText(message);
        etUsername.setText(username);
        etPeak_display.setText(String.format(Locale.US,"%d",peak));
        etMin_display.setText(String.format(Locale.US,"%d",min));
    }
}