package poloapps.orbitfingers;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
public class SimpOF extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        Integer LS= mSettings.getInt("ls", 1);
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