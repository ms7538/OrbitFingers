package poloapps.orbitfingers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

public class SOFview2 extends View {
    private ScaleGestureDetector detector;
    private float MBsze = 30; // Ball's radius
    private float Bsize = 10; // 2 Ball's  radius
    private float XAL=950;
    private float MBx = XAL;  // Ball's center (x,y)
    private float MBxL=XAL-600;
    private float MBy = 210;
    private float B1X = XAL;  // Ball's center (x,y)
    private float B1y= 210;
    private float B2X = XAL;  // Ball's center (x,y)
    private float B2y= 410;
    private float B1XL = XAL-600;  // Ball's center (x,y)
    private float B1yL= 310;
    private float B2XL = XAL-600;  // Ball's center (x,y)
    private float B2yL= 410;
    private int CX=Math.round(XAL);
    private int CY=210;
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private double B1dist=100;
    private double B2dist=200;
    private int score=0;
    private float flrdB1 = (float) B1dist;
    private float flrdB2 = (float) B2dist;
    private float flrdB1x = (float) B1dist-1;
    private float flrdB2x = (float) B2dist-1;
    private float flrdB1xx = (float) B1dist-2;
    private float flrdB2xx = (float) B2dist-2;
    private String MBclr="#ffea7d";
    private String Blue1="#800080";
    private String Currcol= Blue1;
    private String CurrcolL= Blue1;
    private String Green1="#00ff00";
    private String Red1="#ff0000";
    private String currscorecol= Red1;
    private  double thcns=1.3;
    private double theta=0;
    private  double thcns2=1.3;
    private double theta2=0;
    private int Mch=0;
    private double ThtAbs1=0.0, ThtAbs2=0.0;
    private double LThtAbs1=0.0, LThtAbs2=0.0;
    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter = new Formatter(statusMsg);  // Formatting the statusMsg
    // Constructor
    private  double Lthcns=1.3;
    private double Ltheta=180;
    private  double Lthcns2=1.3;
    private double Ltheta2=270;
    private int LMch=0,updC=0;
    private int c1=0,c2=0;
    SharedPreferences mSettings = getContext().getSharedPreferences("Settings", 0);
    SharedPreferences.Editor editor = mSettings.edit();


    public SOFview2(Context context) {
        super(context);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        ballBounds = new RectF();
        paint = new Paint();
        // Set the font face and size of drawing text
        if (c1==0) {
            Toast.makeText(getContext(), "Push center while circles are alligned to gain 10 points, loose 5 points when pushing while circles are not alligned" +
                            "   Level 3 Unlocks at 150 points",
                    Toast.LENGTH_LONG).show();
            c1++;
        }
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

        orbit(canvas, paint, Currcol, CX, CY, flrdB1);
        orbit(canvas, paint, Currcol, CX, CY, flrdB2);
        orbit(canvas, paint, Currcol, CX, CY, flrdB1x);
        orbit(canvas, paint, Currcol, CX, CY, flrdB2x);
        orbit(canvas, paint, Currcol, CX, CY, flrdB1xx);
        orbit(canvas, paint, Currcol, CX, CY, flrdB2xx);
        orbit(canvas, paint, CurrcolL, CX-600, CY, flrdB1);
        orbit(canvas, paint, CurrcolL, CX-600, CY, flrdB2);
        orbit(canvas, paint, CurrcolL, CX-600, CY, flrdB1x);
        orbit(canvas, paint, CurrcolL, CX-600, CY, flrdB2x);
        orbit(canvas, paint, CurrcolL, CX - 600, CY, flrdB1xx);
        orbit(canvas, paint, CurrcolL, CX - 600, CY, flrdB2xx);
        // Draw Planets
        drawball(canvas, ballBounds, MBxL, MBsze, MBy, paint, CurrcolL);
        drawball(canvas, ballBounds, B1XL, Bsize, B1yL, paint, CurrcolL);
        drawball(canvas, ballBounds, B2XL, Bsize, B2yL, paint, CurrcolL);

        drawball(canvas, ballBounds, MBx, MBsze, MBy, paint, Currcol);
        drawball(canvas, ballBounds, B1X, Bsize, B1y, paint, Currcol);
        drawball(canvas, ballBounds, B2X, Bsize, B2y, paint, Currcol);

        int y=460;
        //txtcnvs(canvas, Double.toString(ThtAbs1), CX, y,12);
        //txtcnvs(canvas, Double.toString(ThtAbs2), CX, y+60,12);
        if(score<0){
            score=0;
        }
        txtcnvs(canvas, Integer.toString(score), 705, 35,30,currscorecol);
        txtcnvs(canvas, "SCORE: ", 575, 35, 30, Blue1);
        txtcnvs(canvas, "LEVEL 2", 0, 35, 30, Blue1);


        if(score>=150){
            currscorecol=Green1;
        }else  currscorecol=Red1;

        update();

        // Delay
        try {
            Thread.sleep(25);
        } catch (InterruptedException e) { }

        canvas.restore();
        invalidate();  // Force a re-draw
        // update();
    }
    //test
    private static void orbit(Canvas canvas, Paint paint, String blue1, int CX, int CY, float flrdB1) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(blue1));
        canvas.drawCircle(CX, CY, flrdB1, paint);
    }

    private static void drawball(Canvas canvas, RectF ballBounds, float MBx, float MBsze, float MBy, Paint paint, String MBclr) {
        paint.setStyle(Paint.Style.FILL);
        ballBounds.set(MBx - MBsze, MBy - MBsze, MBx + MBsze, MBy + MBsze);
        paint.setColor(Color.parseColor(MBclr));
        canvas.drawOval(ballBounds, paint);//must be done for each
    }

    private void txtcnvs(Canvas canvas, String str, int x, int y, int tsize, String color) {
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(tsize);
        formatter.format(str);
        paint.setColor(Color.parseColor(color));
        canvas.drawText(statusMsg.toString(), x, y, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                if((event.getX() >= CX-30 && event.getX() <= CX+30 && event.getY() >= 160 && event.getY() < 260) && (Mch==10 || Mch==5)) {
                    Currcol=Green1;
                    if(Mch==10){
                        score +=10;

                    }else score +=10;
                }else if ((event.getX() >= CX-30 && event.getX() <= CX+30 && event.getY() >= 160 && event.getY() < 260) &&!(Mch==10 || Mch==5)){
                    Currcol=Red1;
                    score -=5;
                }
                if((event.getX() >= CX-630 && event.getX() <= CX-570 && event.getY() >= 160 && event.getY() < 260) && (LMch==10 || LMch==5)) {
                    CurrcolL=Green1;
                    if(LMch==10){
                        score +=10;

                    }else score +=10;
                }else if ((event.getX() >= CX-630 && event.getX() <= CX-570 && event.getY() >= 160 && event.getY() < 260) &&!(LMch==10 || LMch==5)){
                    CurrcolL=Red1;
                    score -=5;
                }
                break;
            case MotionEvent.ACTION_MOVE:
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
        //String lvl= mSettings.getString("level", "0");
        if (score>=150){  /// ensure 100
            editor.putInt("levl", 3);
            editor.commit();
            if(c2==0) {
                Toast.makeText(getContext(), " Level 3 Unlocked",
                        Toast.LENGTH_SHORT).show();
                c2++;
            }
            // Intent intent = new Intent(getContext(), MainActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //super.getContext().startActivity(intent);

        }

        //updC++;
        //



        if ( score >= 20 && score <=40)
        {
            thcns=1.45;
            thcns2=1.45;
            Lthcns=1.45;
            Lthcns2=1.45;
        }
        if ( score > 40 && score <=60)
        {
            thcns=1.5;
            thcns2=1.5;
            Lthcns=1.5;
            Lthcns2=1.5;
                    }

         if ( score > 60 && score <=80)
        {
            thcns=1.55;
            thcns2=1.55;
            Lthcns=1.55;
            Lthcns2=1.55;
        }

        if ( score > 80 && score <=100)
        {
            thcns=1.65;
            thcns2=1.65;
            Lthcns=1.65;
            Lthcns2=1.65;
        }

        if ( score > 100 && score <=125)
        {
            thcns=1.75;
            thcns2=1.75;
            Lthcns=1.75;
            Lthcns2=1.75;
        }
        if ( score > 125)
        {
            thcns=1.85;
            thcns2=1.85;
            Lthcns=1.85;
            Lthcns2=1.85;
        }
        theta += thcns;
        if (theta > 360 || theta < -360) {
            theta = 0;
        }
        Ltheta += Lthcns;
        if (Ltheta > 360 || Ltheta < -360) {
            Ltheta = 0;
        }

        theta2 -= thcns2;
        if (theta2 > 360 || theta2 < -360) {
            theta2 = 0;
        }
        Ltheta2 -= Lthcns2;
        if (Ltheta2 > 360 || Ltheta2 < -360) {
            Ltheta2 = 0;
        }
        if(theta<0){
            ThtAbs1= theta+360;
        }else{
            ThtAbs1= theta;
        }
        // ThtAbs2= Math.abs(theta2);
        if(Ltheta<0){
            LThtAbs1= Ltheta+360;
        }else{
            LThtAbs1= Ltheta;
        }
        if(theta2<0){
            ThtAbs2= theta2+360;
        }else{
            ThtAbs2= theta2;
        }
        if(Ltheta2<0){
            LThtAbs2= Ltheta2+360;
        }else{
            LThtAbs2= Ltheta2;
        }

        if (ThtAbs2 > .982 * ThtAbs1 && ThtAbs2 < 1.018 * ThtAbs1) {
            Mch = 10;
        }else if(ThtAbs2 -ThtAbs1> .982*180 && ThtAbs2 -ThtAbs1< 1.018*180){
            Mch=5;
        }else if(ThtAbs1 -ThtAbs2> .982*180 && ThtAbs1 -ThtAbs2< 1.018*180){
            Mch=5;
        } else {
            Mch = 0;
        }
        if (LThtAbs2 > .982 * LThtAbs1 && LThtAbs2 < 1.018 * LThtAbs1) {
            LMch = 10;
        }else if(LThtAbs2 -LThtAbs1> .982*180 && LThtAbs2 -LThtAbs1< 1.018*180){
            LMch=5;
        }else if(LThtAbs1 -LThtAbs2> .982*180 && LThtAbs1 -LThtAbs2< 1.018*180){
            LMch=5;
        } else {
            LMch = 0;
        }

        double Ex = B1dist * Math.cos(Math.toRadians(theta));
        double Ey = B1dist * Math.sin(Math.toRadians(theta));
        double E2x = B2dist * Math.cos(Math.toRadians(theta2));
        double E2y = B2dist * Math.sin(Math.toRadians(theta2));
        B1X = CX + (float) Ex;
        B1y = CY + (float) Ey;
        B2X = CX + (float) E2x;
        B2y = CY + (float) E2y;

        Ex = B1dist * Math.cos(Math.toRadians(Ltheta));
        Ey = B1dist * Math.sin(Math.toRadians(Ltheta));
        E2x = B2dist * Math.cos(Math.toRadians(Ltheta2));
        E2y = B2dist * Math.sin(Math.toRadians(Ltheta2));
        B1XL = CX-600 + (float) Ex;
        B1yL = CY + (float) Ey;
        B2XL = CX-600 + (float) E2x;
        B2yL = CY + (float) E2y;

        if ((Currcol != Blue1) ||CurrcolL != Blue1) {
            Sleep(80);
            Currcol = Blue1;
            CurrcolL=Blue1;
        }
    }

    private static void Sleep(int CX) {
        try {
            Thread.sleep(CX);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
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

}
