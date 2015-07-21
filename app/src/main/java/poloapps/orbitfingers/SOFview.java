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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;

import android.graphics.Typeface;
import android.widget.Toast;
import android.util.Log;

public class SOFview extends View {

    private static int NONE = 0;
    private static int DRAG = 1;
    private static int ZOOM = 2;
    private int mode;
    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 10f;
    private float scaleFactor = 1f;
    private ScaleGestureDetector detector;
    private float startX = 0f;
    private float startY = 0f;
    private boolean dragged;
    private float translateX = 0f;
    private float translateY = 0f;
    private float previousTranslateX = 0f;
    private float previousTranslateY = 0f;
    private float displayWidth;
    private float displayHeight;
    private float  mLastGestureX;
    private float mLastGestureY;

    private float sunRadius = 8; // Ball's radius
    private float earthRadius = 6; // 2 Ball's  radius
    private float sunX = 650;  // Ball's center (x,y)
    private float earthX = 650;  // Ball's center (x,y)
    private float sunY = 300;
    private float earthY= 480;

    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private double radearth=150;

    private float flrdearth = (float) radearth;

    private String Suncolor="#ffea7d";
    private String Suncolor2="#fffffe";
    private String Earthcolor="#017ed5";

    private String Earthorbclr="#017ed5";

    private  double thcns=.001;
    Calendar c = Calendar.getInstance();
    int dyr = c.get(Calendar.DAY_OF_YEAR);
    int yr = c.get(Calendar.YEAR)-1;
    int dte= c.get(Calendar.DATE);

    //int oyr=2014; int oday=132;
    double dayspass= (yr * 365.25)+dyr-735745.5;
    private double earthdegrees=(dayspass*0.98563);

    private double theta=130-earthdegrees;// earth at 139 on may 12 2015

    //String dayOfyear = Integer.toString(dyr);

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
        if (detector.isInProgress()){

            canvas.scale(scaleFactor, scaleFactor,this.getPivotX(), this.getPivotY());
        }
        else{

            canvas.scale(scaleFactor, scaleFactor, this.getX(), this.getY());
        }
        //If translateX times -1 is lesser than zero, let's set it to zero. This takes care of the left bound

        displayWidth = this.getWidth();
        displayHeight = this.getHeight();

        if((translateX * -1) < 0) {
            translateX = 0;
        }
        //This is where we take care of the right bound. We compare translateX times -1 to (scaleFactor - 1) * displayWidth.
        //If translateX is greater than that value, then we know that we've gone over the bound. So we set the value of
        //translateX to (1 - scaleFactor) times the display width. Notice that the terms are interchanged; it's the same
        //as doing -1 * (scaleFactor - 1) * displayWidth
        else if((translateX * -1) > (scaleFactor - 1) * displayWidth) {
            translateX = (1 - scaleFactor) * displayWidth;
        }
        if(translateY * -1 < 0) {
            translateY = 0;
        }
        //We do the exact same thing for the bottom bound, except in this case we use the height of the display
        else if((translateY * -1) > (scaleFactor - 1) * displayHeight) {
            translateY = (1 - scaleFactor) * displayHeight;
        }
        //We need to divide by the scale factor here, otherwise we end up with excessive panning based on our zoom level
        //because the translation amount also gets scaled according to how much we've zoomed into the canvas.
        canvas.translate(translateX / scaleFactor, translateY / scaleFactor);
        //String unitType = getContext().getString(R.string.pref_units_key);
        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.parseColor(Earthorbclr));
        canvas.drawCircle(650, 300, flrdearth, paint);

        // Draw Planets
        paint.setStyle(Paint.Style.FILL);
        ballBounds.set(sunX - sunRadius, sunY - sunRadius, sunX + sunRadius, sunY + sunRadius);
        paint.setColor(Color.parseColor(Suncolor));
        canvas.drawOval(ballBounds, paint);//must be done for each
        ballBounds.set(earthX - earthRadius, earthY - earthRadius, earthX + earthRadius, earthY + earthRadius);
        paint.setColor(Color.parseColor(Earthcolor));

        canvas.drawOval(ballBounds, paint);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(12);
         formatter.format("EARTH");
        paint.setColor(Color.parseColor(Earthorbclr));
        canvas.drawText(statusMsg.toString(), 629, 460, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer
        update();

        // Delay
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) { }

        canvas.restore();
        invalidate();  // Force a re-draw
        // update();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

                //We assign the current X and Y coordinate of the finger to startX and startY minus the previously translated
                //amount for each coordinates This works even when we are translating the first time because the initial
                //values for these two variables is zero.
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;

                if (translateX == 0 && translateY == 0){
                    if(event.getX() >= 600 && event.getX() <= 700 && event.getY() >= 250 && event.getY() < 350) {
                        update2();

                    }}
                break;

            case MotionEvent.ACTION_MOVE:
                if (detector.isInProgress()){
                    mLastGestureX = detector.getFocusX();
                    mLastGestureY =detector.getFocusY();
                }

                translateX = event.getX() - startX;
                translateY = event.getY() - startY;

                //We cannot use startX and startY directly because we have adjusted their values using the previous translation values.
                //This is why we need to add those values to startX and startY so that we can get the actual coordinates of the finger.
                double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) +
                                Math.pow(event.getY() - (startY + previousTranslateY), 2)
                );

                if(distance > 0) {
                    dragged = true;
                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;

            case MotionEvent.ACTION_UP:
                mode = NONE;
                dragged = false;

                //All fingers went up, so let's save the value of translateX and translateY into previousTranslateX and
                //previousTranslate
                previousTranslateX = translateX;
                previousTranslateY = translateY;


                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = DRAG;

                //This is not strictly necessary; we save the value of translateX and translateY into previousTranslateX
                //and previousTranslateY when the second finger goes up
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;
        }

        detector.onTouchEvent(event);

        //We redraw the canvas only in the following cases:
        //
        // o The mode is ZOOM
        //        OR
        // o The mode is DRAG and the scale factor is not equal to 1 (meaning we have zoomed) and dragged is
        //   set to true (meaning the finger has actually moved)
        if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
            invalidate();
        }

        return true;
    }


    // Detect collision and update the position of the ball.
    private void update() {
        // Get new (x,y) position
        SharedPreferences mSettings = getContext().getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        String spd= mSettings.getString("id", "missing");
        thcns=.1;


        String Orbs=mSettings.getString("orbits","?");
        if (Orbs=="on"){
            Earthorbclr="#017ed6";

        }else if (Orbs=="off") {
            Earthorbclr="#090404";

        }else if (Orbs=="?") {
            Earthorbclr="#017ed6";
           }

        theta-=thcns*1;
        double Ex=radearth* Math.cos(Math.toRadians(theta));
        double Ey=radearth* Math.sin(Math.toRadians(theta));


        earthX =650+ (float) Ex;
        earthY =300+(float) Ey;

    }

    private void update2() {

          }

    // Called back when the view is first created or its size changes.
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            invalidate();
            return true;
        }
    }

    void update3() {


    }


}
