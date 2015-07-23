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
   private float XAL=950;
    private float MBx = XAL;  // Ball's center (x,y)
    private float MBy = 300;
    private float B1X = XAL;  // Ball's center (x,y)
    private float B1y= 480;
    private float B2X = XAL;  // Ball's center (x,y)
    private float B2y= 500;
    private int CX=Math.round(XAL);
    private int CY=300;
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private double B1dist=100;
    private double B2dist=200;

    private float flrdB1 = (float) B1dist;
    private float flrdB2 = (float) B2dist;
    private String MBclr="#ffea7d";
    private String Blue1="#017ed5";
    private String Currcol= Blue1;
    private String Green1="#00ff00";
    private String Red1="#ff0000";
    private  double thcns=.5;
    private double theta=180;
    private  double thcns2=.99;
    private double theta2=280;
    private int Mch=0;
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

        orbit(canvas, paint, Currcol, CX, CY, flrdB1);
        orbit(canvas, paint, Currcol, CX, CY, flrdB2);
        // Draw Planets
        paint.setStyle(Paint.Style.FILL);
        drawball(canvas, ballBounds, MBx, MBsze, MBy, paint, MBclr);
        drawball(canvas, ballBounds, B1X, Bsize, B1y, paint, Blue1);
        drawball(canvas, ballBounds, B2X, Bsize, B2y, paint, Blue1);
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

    private static void orbit(Canvas canvas, Paint paint, String blue1, int CX, int CY, float flrdB1) {
        paint.setColor(Color.parseColor(blue1));
        canvas.drawCircle(CX, CY, flrdB1, paint);
    }

    private static void drawball(Canvas canvas, RectF ballBounds, float MBx, float MBsze, float MBy, Paint paint, String MBclr) {
        ballBounds.set(MBx - MBsze, MBy - MBsze, MBx + MBsze, MBy + MBsze);
        paint.setColor(Color.parseColor(MBclr));
        canvas.drawOval(ballBounds, paint);//must be done for each
    }

    private void txtcnvs(Canvas canvas, String str, int x, int y) {
        formatter.format(str);
        paint.setColor(Color.parseColor(Blue1));
        canvas.drawText(statusMsg.toString(), x, y, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                if((event.getX() >= CX-30 && event.getX() <= CX+30 && event.getY() >= 250 && event.getY() < 350) && Mch==10) {
                    Currcol=Green1;
                    Log.i("theta", Double.toString(Mch));
                }else if ((event.getX() >= CX-30 && event.getX() <= CX+30 && event.getY() >= 250 && event.getY() < 350) && Mch!=10){
                    Currcol=Red1;
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

        theta += thcns * 8;
        if (theta > 360 || theta < -360) {
            theta = 0;
        }
        theta2 += thcns2;
        if (theta2 > 360 || theta2 < -360) {
            theta2 = 0;
        }
        if (theta2 > .95 * theta && theta2 < 1.05 * theta) {
            Mch = 10;
        } else {
            Mch = 0;
        }
        double Ex = B1dist * Math.cos(Math.toRadians(theta));
        double Ey = B1dist * Math.sin(Math.toRadians(theta));
        double E2x = B2dist * Math.cos(Math.toRadians(theta2));
        double E2y = B2dist * Math.sin(Math.toRadians(theta2));
        B1X = CX + (float) Ex;
        B1y = CY + (float) Ey;
        B2X = CX + (float) E2x;
        B2y = CY + (float) E2y;

        if (Currcol != Blue1) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            Currcol = Blue1;
        }
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
