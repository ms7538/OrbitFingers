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
import java.util.GregorianCalendar;

import android.graphics.Typeface;
import android.widget.Toast;
import android.util.Log;

public class SOFview extends View {

   private ScaleGestureDetector detector; 
    private float MBrad = 20; // Ball's radius
    private float B1rad = 6; // 2 Ball's  radius
    private float MBx = 950;  // Ball's center (x,y)
    private float B1X = 950;  // Ball's center (x,y)
    private float MBy = 300;
    private float B1y= 480;
    private int CX=950;
    private int CY=300;
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private double B1dist=150;

    private float flrdB1 = (float) B1dist;

    private String MBclr="#ffea7d";
    private String MBclr2="#fffffe";
    private String B1color="#017ed5";

    private String B1orbclr="#017ed5";

    private  double thcns=.001;
    private double theta=130;
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

        paint.setColor(Color.parseColor(B1orbclr));
        canvas.drawCircle(CX, CY, flrdB1, paint);

        // Draw Planets
        paint.setStyle(Paint.Style.FILL);
        ballBounds.set(MBx - MBrad, MBy - MBrad, MBx + MBrad, MBy + MBrad);
        paint.setColor(Color.parseColor(MBclr));
        canvas.drawOval(ballBounds, paint);//must be done for each
        ballBounds.set(B1X - B1rad, B1y - B1rad, B1X + B1rad, B1y + B1rad);
        paint.setColor(Color.parseColor(B1color));

        canvas.drawOval(ballBounds, paint);
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

    private void txtcnvs(Canvas canvas, String str, int x, int y) {
        formatter.format(str);
        paint.setColor(Color.parseColor(B1orbclr));
        canvas.drawText(statusMsg.toString(), x, y, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

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
        B1orbclr="#017ed6";


        theta-=thcns*1;
        double Ex=B1dist* Math.cos(Math.toRadians(theta));
        double Ey=B1dist* Math.sin(Math.toRadians(theta));
        B1X =CX+ (float) Ex;
        B1y =CY+(float) Ey;

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
