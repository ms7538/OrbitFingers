package poloapps.orbitfingers;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import java.util.Formatter;
import android.graphics.Typeface;
import android.widget.Toast;

import static poloapps.orbitfingers.R.color.l1col;

//V3.0 Created
public class OFView_Activity extends View {

    SharedPreferences prefs = super.getContext().getSharedPreferences("Settings", 0); //

    String DensScale = prefs.getString("scale", "1");

    Integer LS  = prefs.getInt("ls", 1);
    Integer PS  = prefs.getInt("peakscore", 0);



    String LSd  = Integer.toString(PS);

    String LUP  = "100";
    String tLUP = "NEXT 2@";

    float scale_factor = Float.parseFloat(DensScale);

    private ScaleGestureDetector detector;
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing

    private float MBsze     = scale_factor *30; // Center ball size
    private float Bsize     = scale_factor *10; // outer ball size
    private float XAL       = scale_factor *950;
    private float XALL      = scale_factor *350;
    private float YAL       = scale_factor *210;
    private float MBx       = XAL;  // Right center (x,y)
    private float MBxL      = scale_factor *350;//Left Center
    private float MBy       = scale_factor *210;
    private float B1X       = XAL;  // Ball's center (x,y)
    private float B1y       = scale_factor *210;
    private float B2X       = XAL;  // Ball's center (x,y)
    private float B2y       = scale_factor *410;
    private float B1XL      = scale_factor *350;  // Ball's center (x,y)
    private float B1yL      = scale_factor *310;
    private float B2XL      = scale_factor *350;  // Ball's center (x,y)
    private float B2yL      = scale_factor *410;
    private int   TYL       = Math.round(scale_factor *35);// text y and length
    private int   TYL2      = Math.round(scale_factor *70);//peaktext
    private int   TYL4      = Math.round(scale_factor *225);
    private int   TYL3      = Math.round(scale_factor *484);
    private int   TYL5      = Math.round(scale_factor *530);
    private int   TXS2      = Math.round(scale_factor *630);
    private int   TXS       = Math.round(scale_factor *580);//"SCORE:" start x
    private int   TXS3      = Math.round(scale_factor *575);//"SCORE:" start x
    private int   TAS       = Math.round(scale_factor *600);
    private int   RLBX      = Math.round(scale_factor *320);
    private int   RLEX      = Math.round(scale_factor *425);
    private int   RRBX      = Math.round(scale_factor *920);
    private int   RREX      = Math.round(scale_factor *1025);
    private int   RBY       = Math.round(scale_factor *450);
    private int   REY       = Math.round(scale_factor *550);
    private int   CX        = Math.round(XAL);
    private int   CXL       = Math.round(XALL);
    private int   CY        = Math.round(YAL);

    private double B1dist   = scale_factor *100;
    private double B2dist   = scale_factor *200;
    private float flrdB1    = (float) B1dist;
    private float flrdB2    = (float) B2dist;
    private float flrdB1x   = (float) B1dist - 1;
    private float flrdB2x   = (float) B2dist - 1;
    private float flrdB1xx  = (float) B1dist - 2;
    private float flrdB2xx  = (float) B2dist - 2;

    private String L1col    = "#017ed5";
    private String L2col    = "#E800FF";
    private String L3col    = "#11dbec";
    private String L4col    = "#ffc63d";
    private String L5col    = "#f2f2f2";
    private String Currcol  = L1col;
    private String CurrcolL = L1col;
    private String Green1   = "#00ff00";
    private String Red1     = "#ff0000";
    private String ScCo     = "#f2f2f2";
    private String currscorecol = ScCo;
    private String levlupcol    = L2col;
    private double RotSpeed     = 1.15;
    private double thcns        = RotSpeed;
    private double theta        = 0;
    private double thcns2       = RotSpeed;
    private double theta2       = 0;
    private int Mch             = 0;

    private double ThtAbs1  = 0.0, ThtAbs2  = 0.0;
    private double LThtAbs1 = 0.0, LThtAbs2 = 0.0;

    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter     = new Formatter(statusMsg);  // Formatting the statusMs

    private double Lthcns  = RotSpeed;
    private double Ltheta  = 180;
    private double Lthcns2 = RotSpeed;
    private double Ltheta2 = 270;
    private int    LMch    = 0;

    private int c1 = 0, c2 = 0,c3=0,c4=0,c5=0;

    private double AAmin = .973;
    private double AAmax = 1.027;
    private int ScorePen = 1;
    private int ScoreMin = 0;

    SharedPreferences mSettings = getContext().getSharedPreferences("Settings", 0);
    SharedPreferences.Editor editor = mSettings.edit();

    private int score   = mSettings.getInt("LSS", 0);
    private int levl    = mSettings.getInt("levl",1);

    Bitmap left_Finger_Print = BitmapFactory.decodeResource(
            getResources(),
            R.drawable.thmb1);

    Bitmap right_Finger_Print = BitmapFactory.decodeResource(  // inverse print image
            getResources(),
            R.drawable.thmb1);

    public OFView_Activity(Context context) {
        super(context);

        detector   = new ScaleGestureDetector(getContext(), new ScaleListener());
        ballBounds = new RectF();
        paint      = new Paint();
        // Set the font face and size of drawing text

        if (c1 == 0) {
            Toast.makeText(getContext(), "Click during alignment to gain points ", Toast.LENGTH_LONG).show();
            c1++;
        }


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
        draw_ball(canvas, ballBounds, MBxL, MBsze, MBy, paint, CurrcolL);
        draw_ball(canvas, ballBounds, B1XL, Bsize, B1yL, paint, CurrcolL);
        draw_ball(canvas, ballBounds, B2XL, Bsize, B2yL, paint, CurrcolL);
        draw_ball(canvas, ballBounds, MBx, MBsze, MBy, paint, Currcol);
        draw_ball(canvas, ballBounds, B1X, Bsize, B1y, paint, Currcol);
        draw_ball(canvas, ballBounds, B2X, Bsize, B2y, paint, Currcol);

        switch (LS){
            case 1:
                TAS=625;
                break;
            case 2:
                TAS=615;
                break;
            case 3:
                TAS=615;
                break;
            case 4:
                TAS=615;
                break;
            case 5:
                TAS=600;
                break;

        }

        canvas_text(canvas,Integer.toString(score), TXS2, TYL4, TYL, currscorecol);
        canvas_text(canvas, ("LEVEL:"+Integer.toString(LS)), TXS3, TYL, TYL, L1col);
        canvas_text(canvas, "PEAK ", 0, TYL2, TYL, Green1);
        canvas_text(canvas, LSd, 0, TYL, TYL, Green1);
        canvas_text(canvas, LUP, TXS2, TYL5, TYL, levlupcol);
        canvas_text(canvas, tLUP, TXS, TYL3, TYL, levlupcol);
        canvas.drawBitmap(right_Finger_Print, RRBX,RBY , null);
        canvas.drawBitmap(left_Finger_Print, RLBX,RBY , null);

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
    private static void draw_ball(Canvas canvas, RectF ballBounds, float MBx, float MBsze, float MBy, Paint paint, String MBclr) {
        paint.setStyle(Paint.Style.FILL);
        ballBounds.set(MBx - MBsze, MBy - MBsze, MBx + MBsze, MBy + MBsze);
        paint.setColor(Color.parseColor(MBclr));
        canvas.drawOval(ballBounds, paint);//must be done for each
    }
    private void canvas_text(Canvas canvas, String str, int x, int y, int tsize, String color) {
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(tsize);
        formatter.format(str);
        paint.setColor(Color.parseColor(color));
        canvas.drawText(statusMsg.toString(), x, y, paint);
        statusMsg.delete(0, statusMsg.length());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                if(event.getX() >= RRBX && event.getX() <= RREX && event.getY()>= RBY && event.getY() < REY) {
                right_Finger_Print = BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.thmb2);
                }
                if(event.getX() >= RLBX  && event.getX() <=  RLEX && event.getY()>= RBY && event.getY() <REY){
                            left_Finger_Print = BitmapFactory.decodeResource(
                            getResources(),
                            R.drawable.thmb2);

                }
                if((event.getX() >= RRBX && event.getX() <= RREX && event.getY()>= RBY && event.getY() < REY) && (Mch==10 || Mch==5)) {
                    Currcol=Green1;
                    if(Mch==10){
                        score +=100;
                        currscorecol=Green1;
                        Peak_Score_Check();
                    }else {
                        score += 100;
                        Peak_Score_Check();
                    }
                }else if ((event.getX() >= RRBX  && event.getX() <=  RREX && event.getY()>= RBY && event.getY() <REY) &&!(Mch==10 || Mch==5)){
                    Currcol=Red1;
                    if (score>=(ScoreMin+ScorePen)){
                        score -= ScorePen;
                        currscorecol=Red1;
                    }
                }
                if((event.getX() >= RLBX  && event.getX() <=  RLEX && event.getY()>= RBY && event.getY() <REY) && (LMch==10 || LMch==5)) {
                    CurrcolL=Green1;
                    if(LMch==10){
                        score +=100;
                        currscorecol=Green1;
                        Peak_Score_Check();
                    }else{
                        score += 100;
                        Peak_Score_Check();
                    }
                }else if ((event.getX() >= RLBX  && event.getX() <=  RLEX && event.getY()>= RBY && event.getY() <REY) &&!(LMch==10 || LMch==5)){
                    CurrcolL=Red1;
                    if (score>=(ScoreMin+ScorePen)){
                        score -= ScorePen;
                        currscorecol=Red1;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if(event.getX() >= RRBX && event.getX() <= RREX && event.getY()>= RBY && event.getY() < REY) {
                            right_Finger_Print = BitmapFactory.decodeResource(
                            getResources(),
                            R.drawable.thmb1);
                }
                if(event.getX() >= RLBX  && event.getX() <=  RLEX && event.getY()>= RBY && event.getY() <REY) {
                            left_Finger_Print = BitmapFactory.decodeResource(
                            getResources(),
                            R.drawable.thmb1);
                }
                break;
        }

        detector.onTouchEvent(event);
        return true;
    }

    private void update() {

        currscorecol=ScCo;
        if (score <= ScoreMin) {
            score = ScoreMin;
        }
        if ((score>=100&& score<300) && c2==0){
            c2++;
            if (levl<2) {
                editor.putInt("levl", 2);
            }

            levlupcol   = L3col;
            LS          = 2;
            tLUP        = "NEXT 3@";
            LUP         = "300";
            editor.putInt("scorelevel",100);
            editor.commit();
            L1col = L2col;
            ScoreMin    = 100;
            AAmin       =.975;
            AAmax       =1.025;
            ScorePen    = 5;


        }
        else if (score  >= 300 && score < 600) {
            if (score   != 305){
                ScorePen = 10;
            }else  {
                ScorePen = 5;
            }
            if (c3==0) {
               c3++;
               if (levl < 3) {
                   editor.putInt("levl", 3);
               }
                LS=3;
                tLUP="NEXT 4@";
                levlupcol=L4col;
                LUP="600";
               editor.putInt("scorelevel", 300);
               editor.commit();
               L1col = L3col;
               ScoreMin = 300;
               AAmin = .98;
               AAmax = 1.02;
               ScorePen = 10;
           }
        }else if (score>=600&& score<1000){
           if (score!=605 && score!=610){
               ScorePen = 15;
           }else if (score==605) {
               ScorePen = 5;
           }else  ScorePen = 10;
            if( c4==0) {
               c4++;
               if (levl < 4) {
                   editor.putInt("levl", 4);
               }
                tLUP="NEXT 5@";
                levlupcol=L5col;
                LUP="1000";
                editor.putInt("scorelevel", 600);
                editor.commit();
                L1col = L4col;
                ScoreMin = 600;
                AAmin = .982;
                AAmax = 1.018;
                LS=4;

           }
        }else if (score>=1000){
            if (score!=1010 && score!=1005){
                ScorePen = 20;
            }else ScorePen = 5;
            if( c5==0) {
                c5++;
                if (levl < 5) {
                    editor.putInt("levl", 5);
                   }
                tLUP="";
                levlupcol = L5col;
                LUP="";
                editor.putInt("scorelevel", 1000);
                editor.commit();
                L1col = L5col;
                ScoreMin = 1000;
                AAmin = .985;
                AAmax = 1.015;
                LS=5;
            }
        }

        ScoreRSpeed();
        ThetaCalc();
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

        if ((Currcol != L1col) || CurrcolL != L1col) {
            Sleep(80);
            Currcol  = L1col;
            CurrcolL = L1col;
        }
    }
   private void ScoreRSpeed(){
       thcns    = RotSpeed;
       thcns2   = RotSpeed;
       Lthcns   = RotSpeed;
       Lthcns2  = RotSpeed;

       if (score >= 50 && score <100) {
           RotSpeed = 1.3;
       }else if (score >= 100 && score <175) {

           RotSpeed=1.5;

       }else if (score >= 175 && score <300) {

           RotSpeed =  1.65;

       }else if (score >= 300 && score <400) {

           RotSpeed = 1.75;

       }else if (score >= 400 && score <600) {

           RotSpeed = 1.85;

       }else if (score >= 600 && score <800) {

           RotSpeed = 1.9;

       }else if (score >= 800 && score <1000) {

           RotSpeed = 1.95;

       }else if (score>=1000&& score <1100){

           RotSpeed=2.05;

       }else if (score>=1100&& score <1300) {

           RotSpeed = 2.2;

       }else if (score>=1300&& score <1600){

           RotSpeed=2.4;

       }else if (score>=1600){

           RotSpeed=2.5;
       }
   }
    private void ThetaCalc(){
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

       if (ThtAbs2 > AAmin * ThtAbs1 && ThtAbs2 < AAmax * ThtAbs1) {
           Mch = 10;
       }else if(ThtAbs2 -ThtAbs1> AAmin*180 && ThtAbs2 -ThtAbs1< AAmax*180){
           Mch=5;
       }else if(ThtAbs1 -ThtAbs2> AAmin*180 && ThtAbs1 -ThtAbs2< AAmax*180){
           Mch=5;
       } else {
           Mch = 0;
       }
       if (LThtAbs2 > AAmin * LThtAbs1 && LThtAbs2 < AAmax * LThtAbs1) {

           LMch = 10;
       }else if(LThtAbs2 -LThtAbs1> AAmin*180 && LThtAbs2 -LThtAbs1< AAmax*180){

           LMch=5;

       }else if(LThtAbs1 -LThtAbs2> AAmin*180 && LThtAbs1 -LThtAbs2< AAmax*180){
           LMch=5;

       } else {
           LMch = 0;
       }
        currscorecol=ScCo;
   }


   private void Peak_Score_Check(){
       if (score > PS) {
           PS   = score;
           LSd  = Integer.toString(PS);
           editor.putInt("peakscore",PS);
           editor.commit();
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