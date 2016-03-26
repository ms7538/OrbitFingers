package poloapps.orbitfingers;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import java.util.Formatter;
import android.graphics.Typeface;
import android.widget.Toast;

public class SOFview3 extends View {
    SharedPreferences prefs = super.getContext().getSharedPreferences("Settings", 0);
    String DensScale= prefs.getString("scale", "1");
    Integer LS= prefs.getInt("ls", 1);
    String LSt="LEVEL:"+ Integer.toString(LS);
    float scalefactor = Float.parseFloat(DensScale);
    private ScaleGestureDetector detector;
    private float MBsze = scalefactor*30; // Center ball size
    private float Bsize = scalefactor*10; // outer ball size
    private float XAL = scalefactor*950;
    private float XALL=scalefactor*350;
    private float YAL=scalefactor*210;
    private float MBx = XAL;  // Right center (x,y)
    private float MBxL = scalefactor*350;//Left Center
    private float MBy = scalefactor*210;
    private float B1X = XAL;  // Ball's center (x,y)
    private float B1y = scalefactor*210;
    private float  B2X = XAL;  // Ball's center (x,y)
    private float B2y = scalefactor*410;
    private float B1XL = scalefactor*350;  // Ball's center (x,y)
    private float B1yL = scalefactor*310;
    private float B2XL = scalefactor*350;  // Ball's center (x,y)
    private float B2yL = scalefactor*410;
    private float rectLbeginX=scalefactor*320;//left click rectangle x start
    private float rectLendX=scalefactor*425;//left click rectangle x end
    private float rectbeginY=scalefactor*450;//both click rectangle y start
    private float rectendY=scalefactor*550;//both click rectangle y end
    private float rectRbeginX=scalefactor*920;//right click rectangle x start
    private float rectRendX=scalefactor*1025;//right click rectangle x end
    private float txtYandlength=scalefactor*35;// text y start and x lentght (both 35)
    private float txtXscr=scalefactor*575;// text "score" x start
    private float txtactscore=scalefactor*705;/// acutal score start x
    private int  TYL = Math.round(txtYandlength);
    private int  TXS = Math.round(txtXscr);
    private int  TAS = Math.round(txtactscore);
    private int  RLBX = Math.round(rectLbeginX);
    private int  RLEX = Math.round(rectLendX);
    private int  RRBX = Math.round(rectRbeginX);
    private int  RREX = Math.round(rectRendX);
    private int  RBY = Math.round(rectbeginY);
    private int  REY = Math.round(rectendY);
    private int CX = Math.round(XAL);
    private int CXL=Math.round(XALL);
    private int CY = Math.round(YAL);
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private double B1dist = scalefactor*100;
    private double B2dist = scalefactor*200;
    private int score = 0;
    private float flrdB1 = (float) B1dist;
    private float flrdB2 = (float) B2dist;
    private float flrdB1x = (float) B1dist - 1;
    private float flrdB2x = (float) B2dist - 1;
    private float flrdB1xx = (float) B1dist - 2;
    private float flrdB2xx = (float) B2dist - 2;
    private String Blue1="#663333";
    private String Currcol = Blue1;
    private String CurrcolL = Blue1;
    private String Green1 = "#00ff00";
    private String Red1 = "#ff0000";
    private String currscorecol = Red1;
    private double thcns = 1;
    private double theta = 0;
    private double thcns2 = 1;
    private double theta2 = 0;
    private int Mch = 0;
    private double ThtAbs1 = 0.0, ThtAbs2 = 0.0;
    private double LThtAbs1 = 0.0, LThtAbs2 = 0.0;
    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter = new Formatter(statusMsg);  // Formatting the statusMsg
    private double Lthcns = 1;
    private double Ltheta = 180;
    private double Lthcns2 = 1;
    private double Ltheta2 = 270;
    private int LMch = 0, dsbc=0;
    private int c1 = 0, c2 = 0;
    SharedPreferences mSettings = getContext().getSharedPreferences("Settings", 0);
    SharedPreferences.Editor editor = mSettings.edit();
    Bitmap myBitmap = BitmapFactory.decodeResource(
            getResources(),
            R.drawable.thmb1);
    public SOFview3(Context context) {
        super(context);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        ballBounds = new RectF();
        paint = new Paint();
        // Set the font face and size of drawing text
        if (c1==0) {
            Toast.makeText(getContext(), "Click during alligment to gain 10 points, 5 points penalty during missalignment" +
                            "   Level 4 Unlocks at 200 points",
                    Toast.LENGTH_LONG).show();
            c1++;
        }
        // To enable keypad on this View
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        orbit(canvas, paint, Currcol, CX, CY, flrdB1);
        orbit(canvas, paint, Currcol, CX, CY, flrdB2);
        orbit(canvas, paint, Currcol, CX, CY, flrdB1x);
        orbit(canvas, paint, Currcol, CX, CY, flrdB2x);
        orbit(canvas, paint, Currcol, CX, CY, flrdB1xx);
        orbit(canvas, paint, Currcol, CX, CY, flrdB2xx);
        orbit(canvas, paint, CurrcolL, CXL, CY, flrdB1);
        orbit(canvas, paint, CurrcolL, CXL, CY, flrdB2);
        orbit(canvas, paint, CurrcolL, CXL, CY, flrdB1x);
        orbit(canvas, paint, CurrcolL, CXL, CY, flrdB2x);
        orbit(canvas, paint, CurrcolL, CXL, CY, flrdB1xx);
        orbit(canvas, paint, CurrcolL, CXL, CY, flrdB2xx);
        drawball(canvas, ballBounds, MBxL, MBsze, MBy, paint, CurrcolL);
        drawball(canvas, ballBounds, B1XL, Bsize, B1yL, paint, CurrcolL);
        drawball(canvas, ballBounds, B2XL, Bsize, B2yL, paint, CurrcolL);
        drawball(canvas, ballBounds, MBx, MBsze, MBy, paint, Currcol);
        drawball(canvas, ballBounds, B1X, Bsize, B1y, paint, Currcol);
        drawball(canvas, ballBounds, B2X, Bsize, B2y, paint, Currcol);
        if (score < 0) {
            score = 0;
        }
        txtcnvs(canvas, Integer.toString(score), TAS, TYL, TYL, currscorecol);
        txtcnvs(canvas, "SCORE: ", TXS, TYL, TYL, Blue1);
        txtcnvs(canvas, LSt, 0, TYL, TYL, Blue1);
        canvas.drawBitmap(myBitmap, RRBX, RBY, null);
        canvas.drawBitmap(myBitmap, RLBX, RBY, null);
        update();
        // Delay
        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
        }
        canvas.restore();
        invalidate();  // Force a re-draw
    }
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
                if((event.getX() >= RRBX && event.getX() <= RREX && event.getY()>= RBY && event.getY() < REY) && (Mch==10 || Mch==5)) {
                    Currcol=Green1;
                    if(Mch==10){
                        score +=10;
                    }else score +=10;
                }else if ((event.getX() >= RRBX  && event.getX() <=  RREX && event.getY()>= RBY && event.getY() <REY) &&!(Mch==10 || Mch==5)){
                    Currcol=Red1;
                    score -=5;
                }
                if((event.getX() >= RLBX  && event.getX() <=  RLEX && event.getY()>= RBY && event.getY() <REY) && (LMch==10 || LMch==5)) {
                    CurrcolL=Green1;
                    if(LMch==10){
                        score +=10;

                    }else score +=10;
                }else if ((event.getX() >= RLBX  && event.getX() <=  RLEX && event.getY()>= RBY && event.getY() <REY) &&!(LMch==10 || LMch==5)){
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

        if (score>=200){
            currscorecol = Green1;
            editor.putInt("levl", 4);
            editor.commit();
            if(c2==0) {
                Toast.makeText(getContext(), " Level 4 Unlocked",
                        Toast.LENGTH_SHORT).show();
                c2++;
            }
        }
        if ( score < 20){
            thcns=1.9;
            thcns2=1.9;
            Lthcns=1.9;
            Lthcns2=1.9;
        }
        if ( score >= 20 && score <=40)
        {
            thcns=1.95;
            thcns2=1.95;
            Lthcns=1.95;
            Lthcns2=1.95;
        }
        if ( score > 40 && score <=60)
        {
            thcns=2;
            thcns2=2;
            Lthcns=2;
            Lthcns2=2;
        }
        if ( score > 60 && score <=80)
        {
            thcns=2.05;
            thcns2=2.05;
            Lthcns=2.05;
            Lthcns2=2.05;
        }
        if ( score > 80 && score <=100)
        {
            thcns=2.1;
            thcns2=2.1;
            Lthcns=2.1;
            Lthcns2=2.1;
        }
        if ( score > 100 && score <=125)
        {
            thcns=2.15;
            thcns2=2.15;
            Lthcns=2.15;
            Lthcns2=2.15;
        }
        if ( score > 125 && score <=160)
        {
            thcns=1.85;
            thcns2=1.85;
            Lthcns=1.85;
            Lthcns2=1.85;
        }
        if ( score > 160)
        {
            thcns=1.9;
            thcns2=1.9;
            Lthcns=1.9;
            Lthcns2=1.9;
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
        if (ThtAbs2 > .983 * ThtAbs1 && ThtAbs2 < 1.017 * ThtAbs1) {
            Mch = 10;
        }else if(ThtAbs2 -ThtAbs1> .983*180 && ThtAbs2 -ThtAbs1< 1.017*180){
            Mch=5;
        }else if(ThtAbs1 -ThtAbs2> .983*180 && ThtAbs1 -ThtAbs2< 1.017*180){
            Mch=5;
        } else {
            Mch = 0;
        }
        if (LThtAbs2 > .983 * LThtAbs1 && LThtAbs2 < 1.017 * LThtAbs1) {
            LMch = 10;
        }else if(LThtAbs2 -LThtAbs1> .983*180 && LThtAbs2 -LThtAbs1< 1.017*180){
            LMch=5;
        }else if(LThtAbs1 -LThtAbs2> .983*180 && LThtAbs1 -LThtAbs2< 1.017*180){
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
        B1XL = CXL + (float) Ex;
        B1yL = CY + (float) Ey;
        B2XL = CXL + (float) E2x;
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
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return true;
        }
    }
}