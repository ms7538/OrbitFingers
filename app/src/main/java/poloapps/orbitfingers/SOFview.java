package poloapps.orbitfingers;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import java.util.Formatter;
import android.graphics.Typeface;
import android.widget.Toast;
import android.util.Log;

public class SOFview extends View {

   private ScaleGestureDetector detector; 
    private float MBsze = 20; // Ball's radius
    private float Bsize = 6; // 2 Ball's  radius
    private float MBx = 900;  // Ball's center (x,y)
    private float MBy = 300;
    private float B1X = 900;  // Ball's center (x,y)
    private float B1y= 480;
    private float B2X = 900;  // Ball's center (x,y)
    private float B2y= 500;
    private int CX=900;
    private int CY=300;
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private double B1dist=150;
    private double B2dist=250;

    private float flrdB1 = (float) B1dist;
    private float flrdB2 = (float) B2dist;
    private String MBclr="#ffea7d";
    private String B2color="#017ed5";
    private String B1color="#017ed5";
    private  double thcns=.1;
    private double theta=180;
    private  double thcns2=.1;
    private double theta2=180;
    // Status message to show Ball's (x,y) position and speed.
    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter = new Formatter(statusMsg);  // Formatting the statusMsg
    // Constructor


    public SOFview(Context context) {
        super(context);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        ballBounds = new RectF();
        paint = new Paint();
        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(16);
        // To enable keypad on this View
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);



    }

    // Called back to draw the view. Also called by invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        //String unitType = getContext().getString(R.string.pref_units_key);
        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.parseColor(B1color));
        canvas.drawCircle(CX, CY, flrdB1, paint);
        paint.setColor(Color.parseColor(B1color));
        canvas.drawCircle(CX, CY, flrdB2, paint);
        // Draw Planets
        paint.setStyle(Paint.Style.FILL);
        drawball(canvas, ballBounds, MBx, MBsze, MBy, paint, MBclr);
        drawball(canvas, ballBounds, B1X, Bsize, B1y, paint, B1color);
        drawball(canvas, ballBounds, B2X, Bsize, B2y, paint, B2color);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(12);
       String str="POS1";
       int y=460;
        txtcnvs(canvas, str, CX, y);
        update();

        // Delay
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) { }

        canvas.restore();
        invalidate();  // Force a re-draw
        // update();
    }

    private static void drawball(Canvas canvas, RectF ballBounds, float MBx, float MBsze, float MBy, Paint paint, String MBclr) {
        ballBounds.set(MBx - MBsze, MBy - MBsze, MBx + MBsze, MBy + MBsze);
        paint.setColor(Color.parseColor(MBclr));
        canvas.drawOval(ballBounds, paint);//must be done for each
    }

    private void txtcnvs(Canvas canvas, String str, int x, int y) {
        formatter.format(str);
        paint.setColor(Color.parseColor(B1color));
        canvas.drawText(statusMsg.toString(), x, y, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                if(event.getX() >= 870 && event.getX() <= 920 && event.getY() >= 250 && event.getY() < 350) {

                    Toast.makeText(getContext(), "push detected",
                            Toast.LENGTH_SHORT).show();
                }
              break;

            case MotionEvent.ACTION_MOVE:
//

                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                break;

            case MotionEvent.ACTION_UP:


                break;

            case MotionEvent.ACTION_POINTER_UP:

                break;
        }

        detector.onTouchEvent(event);



        return true;
    }



    private void update() {

        thcns=.1;
        theta+=thcns*8;
        Log.i("theta", Double.toString(theta));
        if(theta>360|| theta < -360){theta=0;}
        theta2+=thcns*10;
        Log.i("theta2", Double.toString(theta2));
        if(theta2>360|| theta2 < -360){theta2=0;}

       // if(theta2==theta){ Toast.makeText(getContext(), "theta equals",
          //      Toast.LENGTH_SHORT).show();}
        double Ex=B1dist* Math.cos(Math.toRadians(theta));
        double Ey=B1dist* Math.sin(Math.toRadians(theta));
        double E2x=B2dist* Math.cos(Math.toRadians(theta2));
        double E2y=B2dist* Math.sin(Math.toRadians(theta2));
        B1X =CX+ (float) Ex;
        B1y =CY+(float) Ey;
        B2X =CX+ (float) E2x;
        B2y =CY+(float) E2y;

       }

       // Called back when the view is first created or its size changes.
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
//            scaleFactor *= detector.getScaleFactor();
//            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
//            invalidate();
            return true;
        }
    }

    void update3() {}

}
