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

//V3.0 Created
public class OFView_Activity extends View {

    SharedPreferences prefs = super.getContext().getSharedPreferences("Settings", 0); //

    String DensScale = prefs.getString("scale", "1");

    Integer LS  = prefs.getInt("ls", 1);
    Integer PS  = prefs.getInt("peakscore", 0);


    private int L1C         = ContextCompat.getColor(getContext(), (R.color.l1col));
    private int L2C         = ContextCompat.getColor(getContext(), (R.color.l2col));
    private int L3C         = ContextCompat.getColor(getContext(), (R.color.l3col));
    private int L4C         = ContextCompat.getColor(getContext(), (R.color.l4col));
    private int L5C         = ContextCompat.getColor(getContext(), (R.color.l5col));
    private int RED         = ContextCompat.getColor(getContext(), (R.color.red));
    private int GREEN       = ContextCompat.getColor(getContext(), (R.color.green2));

    String LSd  = Integer.toString(PS);

    String LUP  = "100";
    String tLUP = "NEXT 2@";

    float scale_factor = Float.parseFloat(DensScale);

    private ScaleGestureDetector detector;
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing

    private float MB_size   = scale_factor *30; // Center ball size
    private float B_size    = scale_factor *10; // outer ball size
    private float XAL       = scale_factor *950;
    private float XA_LL     = scale_factor *350;
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
    private int   RL_BX     = Math.round(scale_factor *320);
    private int   RL_EX     = Math.round(scale_factor *425);
    private int   RR_BX     = Math.round(scale_factor *920);
    private int   RR_EX     = Math.round(scale_factor *1025);
    private int   RBY       = Math.round(scale_factor *450);
    private int   REY       = Math.round(scale_factor *550);
    private int   CX        = Math.round(XAL);
    private int   CXL       = Math.round(XA_LL);
    private int   CY        = Math.round(YAL);

    private double B1dist   = scale_factor *100;
    private double B2dist   = scale_factor *200;
    private float FB1       = (float) B1dist;
    private float FB2       = (float) B2dist;
    private float FB1_x     = (float) B1dist - 1;
    private float FB2_x     = (float) B2dist - 1;
    private float FB1_xx    = (float) B1dist - 2;
    private float FB2_xx    = (float) B2dist - 2;


    private String L1col        = "#"+ Integer.toHexString(L1C);
    private String L2col        = "#"+ Integer.toHexString(L2C);
    private String L3col        = "#"+ Integer.toHexString(L3C);
    private String L4col        = "#"+ Integer.toHexString(L4C);
    private String L5col        = "#"+ Integer.toHexString(L5C);
    private String Right_Color  = L1col;
    private String Left_Color   = L1col;
    private String Green1       = "#"+ Integer.toHexString(GREEN);
    private String Red1         = "#"+ Integer.toHexString(RED);
    private String ScCo         = L5col;
    private String score_color  = ScCo;
    private String next_color   = L2col;
    private double RotSpeed     = 1.15;
    private double Right_RS     = RotSpeed;
    private double theta        = 0;
    private double Right_RS2    = RotSpeed;
    private double theta2       = 0;
    private int Mch             = 0;

    private double ThtAbs1  = 0.0, ThtAbs2  = 0.0;
    private double LThtAbs1 = 0.0, LThtAbs2 = 0.0;

    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter     = new Formatter(statusMsg);  // Formatting the statusMs

    private double Left_Rotation_Speed   = RotSpeed;
    private double Left_theta            = 180;
    private double Left_RS2              = RotSpeed;
    private double Left_theta2           = 270;
    private int    LMch                  = 0;

    private int c1 = 0, c2 = 0,c3=0,c4=0,c5=0;

    private double AA_min   = .973;
    private double AA_max   = 1.027;
    private int ScorePen    = 1;
    private int ScoreMin    =  prefs.getInt("min_score", 0);

    SharedPreferences mSettings = getContext().getSharedPreferences("Settings", 0);
    SharedPreferences.Editor editor = mSettings.edit();

    private int score   = ScoreMin;
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

        orbit(canvas, paint, Right_Color, CX, CY, FB1);
        orbit(canvas, paint, Right_Color, CX, CY, FB2);
        orbit(canvas, paint, Right_Color, CX, CY, FB1_x);
        orbit(canvas, paint, Right_Color, CX, CY, FB2_x);
        orbit(canvas, paint, Right_Color, CX, CY, FB1_xx);
        orbit(canvas, paint, Right_Color, CX, CY, FB2_xx);
        orbit(canvas, paint, Left_Color, CXL, CY, FB1);
        orbit(canvas, paint, Left_Color, CXL, CY, FB2);
        orbit(canvas, paint, Left_Color, CXL, CY, FB1_x);
        orbit(canvas, paint, Left_Color, CXL, CY, FB2_x);
        orbit(canvas, paint, Left_Color, CXL, CY, FB1_xx);
        orbit(canvas, paint, Left_Color, CXL, CY, FB2_xx);
        draw_ball(canvas, ballBounds, MBxL, MB_size, MBy, paint, Left_Color);
        draw_ball(canvas, ballBounds, B1XL, B_size, B1yL, paint, Left_Color);
        draw_ball(canvas, ballBounds, B2XL, B_size, B2yL, paint, Left_Color);
        draw_ball(canvas, ballBounds, MBx, MB_size, MBy, paint, Right_Color);
        draw_ball(canvas, ballBounds, B1X, B_size, B1y, paint, Right_Color);
        draw_ball(canvas, ballBounds, B2X, B_size, B2y, paint, Right_Color);

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

        canvas_text(canvas,Integer.toString(score), TXS2, TYL4, TYL, score_color);
        canvas_text(canvas, ("LEVEL:"+Integer.toString(LS)), TXS3, TYL, TYL, L1col);
        canvas_text(canvas, "PEAK ", 0, TYL2, TYL, Green1);
        canvas_text(canvas, LSd, 0, TYL, TYL, Green1);
        canvas_text(canvas, LUP, TXS2, TYL5, TYL, next_color);
        canvas_text(canvas, tLUP, TXS, TYL3, TYL, next_color);
        canvas.drawBitmap(right_Finger_Print, RR_BX,RBY , null);
        canvas.drawBitmap(left_Finger_Print, RL_BX,RBY , null);

        update();

        // Delay
        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
        }
        canvas.restore();
        invalidate();  // Force a re-draw
    }
    private static void orbit(Canvas canvas, Paint paint, String blue1, int CX, int CY, float FB1) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(blue1));
        canvas.drawCircle(CX, CY, FB1, paint);
    }
    private static void draw_ball(Canvas canvas, RectF ballBounds, float MBx, float MB_size, float MBy, Paint paint, String MB_c) {
        paint.setStyle(Paint.Style.FILL);
        ballBounds.set(MBx - MB_size, MBy - MB_size, MBx + MB_size, MBy + MB_size);
        paint.setColor(Color.parseColor(MB_c));
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
                if(event.getX() >= RR_BX && event.getX() <= RR_EX && event.getY()>= RBY && event.getY() < REY) {
                right_Finger_Print = BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.thmb2);
                }
                if(event.getX() >= RL_BX && event.getX() <= RL_EX && event.getY()>= RBY && event.getY() <REY){
                            left_Finger_Print = BitmapFactory.decodeResource(
                            getResources(),
                            R.drawable.thmb2);

                }
                if((event.getX() >= RR_BX && event.getX() <= RR_EX && event.getY()>= RBY && event.getY() < REY) && (Mch==10 || Mch==5)) {
                    Right_Color =Green1;
                    if(Mch==10){
                        score +=100;
                        score_color =Green1;
                        Peak_Score_Check();
                    }else {
                        score += 100;
                        Peak_Score_Check();
                    }
                }else if ((event.getX() >= RR_BX && event.getX() <= RR_EX && event.getY()>= RBY && event.getY() <REY) &&!(Mch==10 || Mch==5)){
                    Right_Color =Red1;
                    if (score>=(ScoreMin+ScorePen)){
                        score -= ScorePen;
                        score_color =Red1;
                    }
                }
                if((event.getX() >= RL_BX && event.getX() <= RL_EX && event.getY()>= RBY && event.getY() <REY) && (LMch==10 || LMch==5)) {
                    Left_Color =Green1;
                    if(LMch==10){
                        score +=10;
                        score_color =Green1;
                        Peak_Score_Check();
                    }else{
                        score += 10;
                        Peak_Score_Check();
                    }
                }else if ((event.getX() >= RL_BX && event.getX() <= RL_EX && event.getY()>= RBY && event.getY() <REY) &&!(LMch==10 || LMch==5)){
                    Left_Color =Red1;
                    if (score>=(ScoreMin+ScorePen)){
                        score -= ScorePen;
                        score_color =Red1;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if(event.getX() >= RR_BX && event.getX() <= RR_EX && event.getY()>= RBY && event.getY() < REY) {
                            right_Finger_Print = BitmapFactory.decodeResource(
                            getResources(),
                            R.drawable.thmb1);
                }
                if(event.getX() >= RL_BX && event.getX() <= RL_EX && event.getY()>= RBY && event.getY() <REY) {
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
        int level2_min = 100;
        int level3_min = 300;
        int level4_min = 600;
        int level5_min = 1000;
        int level2_pen = 5;
        int level3_pen = 10;
        int level4_pen = 15;
        int level5_pen = 20;

        score_color = ScCo;
        if (score <= ScoreMin) {
            score = ScoreMin;
        }
        if ( score >= level2_min && score < level3_min ){

            if ( score   > level2_min + level2_pen ){
                ScorePen = level2_pen;
            }else  {
                ScorePen = 0;
            }
            if ( c2 == 0 ) {
                c2++;
                if ( levl < 2 ) {
                    editor.putInt("levl", 2);
                    editor.putInt("min_score", level2_min);
                }
                editor.putInt("scorelevel",level2_min);
                editor.commit();
                next_color = L3col;
                LS         = 2;
                tLUP       = getContext().getString(R.string.level_3);
                LUP        = Integer.toString(level3_min);
                L1col      = L2col;
                ScoreMin   = level2_min;
                AA_min     = .975;
                AA_max     = 1.025;

            }

        }
        else if ( score  >= level3_min && score < level4_min ) {

            if ( score   > level3_min + level3_pen ){
                ScorePen = level3_pen;
            }else  {
                ScorePen = 0;
            }

            if (c3==0) {
               c3++;
               if (levl < 3) {
                   editor.putInt("levl", 3);
                   editor.putInt("min_score",level3_min);
               }
                LS         = 3;
                editor.putInt("scorelevel",level3_min);
                editor.commit();
                tLUP       = getContext().getString(R.string.level_4);
                next_color = L4col;
                LUP        = Integer.toString(level4_min);
                L1col      = L3col;
                ScoreMin   = level3_min;
                AA_min     = .98;
                AA_max     = 1.02;
            }
        }else if ( score >= level4_min && score < level5_min){

            if ( score   > level4_min + level4_pen ){
                ScorePen = level4_pen;
            }else  {
                ScorePen = 0;
            }
            if ( c4==0 ) {
               c4++;
               if (levl < 4) {
                   editor.putInt("levl", 4);
                   editor.putInt("min_score",level4_min);
               }
                editor.putInt("scorelevel",level4_min);
                editor.commit();
                tLUP        = getContext().getString(R.string.level_5);
                next_color  = L5col;
                LUP         = Integer.toString(level5_min);
                L1col     = L4col;
                ScoreMin  = level4_min;
                AA_min    = .982;
                AA_max    = 1.018;
                LS        = 4;

           }
        }else if ( score >= level5_min ){
            if ( score > level5_min + level5_pen ){
                ScorePen   = level5_pen;
            }else ScorePen = 0;

            if( c5==0) {
                c5++;
                if (levl < 5) {
                    editor.putInt("levl", 5);
                    editor.putInt("min_score",level5_min);
                   }
                editor.putInt("scorelevel",level5_min);
                editor.commit();
                tLUP       = getContext().getString(R.string.empty) ;
                next_color = L5col;
                LUP        = getContext().getString(R.string.empty);
                L1col      = L5col;
                ScoreMin   = level5_min;
                AA_min     = .985;
                AA_max     = 1.015;
                LS         = 5;
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
        Ex = B1dist * Math.cos(Math.toRadians(Left_theta));
        Ey = B1dist * Math.sin(Math.toRadians(Left_theta));
        E2x = B2dist * Math.cos(Math.toRadians(Left_theta2));
        E2y = B2dist * Math.sin(Math.toRadians(Left_theta2));
        B1XL = CXL + (float) Ex;
        B1yL = CY + (float) Ey;
        B2XL = CXL + (float) E2x;
        B2yL = CY + (float) E2y;

        if ((Right_Color != L1col) || Left_Color != L1col) {
            Sleep(80);
            Right_Color = L1col;
            Left_Color = L1col;
        }
    }
   private void ScoreRSpeed(){
       Right_RS = RotSpeed;
       Right_RS2 = RotSpeed;
       Left_Rotation_Speed = RotSpeed;
       Left_RS2 = RotSpeed;

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
       theta += Right_RS;
       if (theta > 360 || theta < -360) {
           theta = 0;
       }
       Left_theta += Left_Rotation_Speed;
       if (Left_theta > 360 || Left_theta < -360) {
           Left_theta = 0;
       }
       theta2 -= Right_RS2;
       if (theta2 > 360 || theta2 < -360) {
           theta2 = 0;
       }
       Left_theta2 -= Left_RS2;
       if (Left_theta2 > 360 || Left_theta2 < -360) {
           Left_theta2 = 0;
       }
       if(theta<0){
           ThtAbs1= theta+360;
       }else{
           ThtAbs1= theta;
       }
       if(Left_theta <0){
           LThtAbs1= Left_theta +360;
       }else{
           LThtAbs1= Left_theta;
       }
       if(theta2<0){
           ThtAbs2= theta2+360;
       }else{
           ThtAbs2= theta2;
       }
       if(Left_theta2 <0){
           LThtAbs2= Left_theta2 +360;
       }else{
           LThtAbs2= Left_theta2;
       }

       if (ThtAbs2 > AA_min * ThtAbs1 && ThtAbs2 < AA_max * ThtAbs1) {
           Mch = 10;
       }else if(ThtAbs2 -ThtAbs1> AA_min *180 && ThtAbs2 -ThtAbs1< AA_max *180){
           Mch=5;
       }else if(ThtAbs1 -ThtAbs2> AA_min *180 && ThtAbs1 -ThtAbs2< AA_max *180){
           Mch=5;
       } else {
           Mch = 0;
       }
       if (LThtAbs2 > AA_min * LThtAbs1 && LThtAbs2 < AA_max * LThtAbs1) {

           LMch = 10;
       }else if(LThtAbs2 -LThtAbs1> AA_min *180 && LThtAbs2 -LThtAbs1< AA_max *180){

           LMch=5;

       }else if(LThtAbs1 -LThtAbs2> AA_min *180 && LThtAbs1 -LThtAbs2< AA_max *180){
           LMch=5;

       } else {
           LMch = 0;
       }
        score_color = ScCo;
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