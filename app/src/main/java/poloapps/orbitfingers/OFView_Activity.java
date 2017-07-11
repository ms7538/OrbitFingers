package poloapps.orbitfingers;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import java.util.Formatter;
import android.graphics.Typeface;
import android.widget.Toast;

//V3.6j Sound Effects in progress
public class OFView_Activity extends View {

    SharedPreferences prefs = super.getContext().getSharedPreferences("Settings", 0);

    String  max_height     = prefs.getString("max_h", "1");
    String  max_width      = prefs.getString("max_w", "1");
    Integer LS             = prefs.getInt("ls", 1);
    Integer Peak_Score     = prefs.getInt("peakscore", 0);
    Integer SMPs_Remaining = prefs.getInt("set_peak_min", 1);


    private int L1C     = ContextCompat.getColor(getContext(), (R.color.l1col));
    private int L2C     = ContextCompat.getColor(getContext(), (R.color.l2col));
    private int L3C     = ContextCompat.getColor(getContext(), (R.color.l3col));
    private int L4C     = ContextCompat.getColor(getContext(), (R.color.l4col));
    private int L5C     = ContextCompat.getColor(getContext(), (R.color.l5col));
    private int RED     = ContextCompat.getColor(getContext(), (R.color.red));
    private int GREEN   = ContextCompat.getColor(getContext(), (R.color.green2));
    private int Fade1   = ContextCompat.getColor(getContext(), (R.color.fade1));
    private int Fade2   = ContextCompat.getColor(getContext(), (R.color.fade2));
    private int Fade3   = ContextCompat.getColor(getContext(), (R.color.fade3));
    private int Fade4   = ContextCompat.getColor(getContext(), (R.color.fade4));
    private int Fade5   = ContextCompat.getColor(getContext(), (R.color.fade5));
    private int YELLOW  = ContextCompat.getColor(getContext(), (R.color.bright_yellow));

    String Peak_Score_Value = Integer.toString(Peak_Score);
    String next_level_value = "100";
    String next_text        =  getContext().getString(R.string.level_2);


    double Max_Height       = Double.parseDouble(max_height); // max y (height)
    double Max_Width        = Double.parseDouble(max_width); //max x (width)

    int Min_Ind_X           = (int) (.478  * Max_Width);  // Min Score Indication X Start
    int Level_Ind_X         = (int) (.4325 * Max_Width);  // Level Number Text Indication X Start
    int Next_Ind_X          = (int) (.87   * Max_Width);   // Next Level Text Indication X Start
    int SMP_Ind_X           = (int) (.47   * Max_Width);   // Next Level Text Indication X Start
    final int Score_X_0_9   = (int) (.495  * Max_Width);
    int Next_Value_X        = (int) (.915  * Max_Width);
    int Peak_Value_X        = 0;
    int Score_X_Adjusted    = Score_X_0_9;
    int SMP_X_Adjusted      = Score_X_0_9;

    int Min_Ind_Y           = (int) (.23  * Max_Height); // Min Score Indication Y Start
    int Upper_Text_Y        = (int) (.05  * Max_Height); // Peak, Level, Next Indication Y Start
    int Peak_Next_Values_Y  = (int) (.10  * Max_Height); // Peak, Next Values Y Start
    int Score_Value_Y       = (int) (.15  * Max_Height); // Peak, Next Values Y Start
    int SMP_Ind_Y           = (int) (.68  * Max_Height);
    int SMP_Value_Y         = (int) (.75  * Max_Height);
    int Text_Length         = (int) (.03  * Max_Width);


    private ScaleGestureDetector detector;
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing

    private float Static_Radius   = (float) (.035  * Max_Height); // Center ball size
    private float Dynamic_Radius  = (float) (.0125 * Max_Height); // outer ball size
    private float Common_Center_Y = (float) (.2    * Max_Width);
    private float Right_Center_X  = (float) (.75   * Max_Width);
    private float Left_Center_X   = (float) (.25   * Max_Width);

    private float B1yL      = 0;
    private float B2y       = 0;
    private float MBxL      = Left_Center_X;
    private float B1XL      = Left_Center_X;
    private float B2XL      = Left_Center_X;
    private float B1y       = Common_Center_Y;
    private float YAL       = Common_Center_Y;
    private float B2yL      = B2y;

    private float B2X       = Right_Center_X;  // Ball's center (x,y)
    private float B1X       = Right_Center_X;  // Ball's center (x,y)
    private float MBx       = Right_Center_X;  // Right center (x,y)
    // touch events and print thumbnails
    private int  L_TE_X_S   = (int) (.21 * Max_Width);   // Left Touch Event X Start Location
    private int  L_TE_X_E   = (int) (.3  * Max_Width);  // Left Touch Event X End Location
    private int  R_TE_X_S   = (int) (.71 * Max_Width);   // Right Touch Event X Start Location
    private int  R_TE_X_E   = (int) (.80 * Max_Width);  // Right Touch Event X End Location
    private int  C_TE_Y_S   = (int) (.65 * Max_Height); // Common Touch Event Y Start Location
    private int  C_TE_Y_E   = (int) (.99 * Max_Height); // Common Touch Event Y End Location

    private int  CX         = Math.round(Right_Center_X);
    private int  CXL        = Math.round(Left_Center_X);
    private int  CY         = Math.round(YAL);

    private double B1dist   = .125 * Max_Height; // orbit 1 distance
    private double B2dist   = .25  * Max_Height; // orbit 2 distance

    private float FB1       = (float) B1dist;
    private float FB2       = (float) B2dist;
    private float FB1_x     = (float) B1dist - 1;
    private float FB2_x     = (float) B2dist - 1;
    private float FB1_xx    = (float) B1dist - 2;
    private float FB2_xx    = (float) B2dist - 2;


    private String Level_Color  = "#" + Integer.toHexString(L1C);
    private String L2col        = "#" + Integer.toHexString(L2C);
    private String L3col        = "#" + Integer.toHexString(L3C);
    private String L4col        = "#" + Integer.toHexString(L4C);
    private String L5col        = "#" + Integer.toHexString(L5C);
    private String Green1       = "#" + Integer.toHexString(GREEN);
    private String Red1         = "#" + Integer.toHexString(RED);
    private String F1_color     = "#" + Integer.toHexString(Fade1);
    private String F2_color     = "#" + Integer.toHexString(Fade2);
    private String F3_color     = "#" + Integer.toHexString(Fade3);
    private String F4_color     = "#" + Integer.toHexString(Fade4);
    private String F5_color     = "#" + Integer.toHexString(Fade5);
    private String Yellow       = "#" + Integer.toHexString(YELLOW);

    private String Right_Color  = Level_Color;
    private String Left_Color   = Level_Color;
    private String ScCo         = Level_Color;
    private String score_color  = ScCo;
    private String next_color   = L2col;
    private double RotSpeed     = 1.15;
    private double Right_RS     = RotSpeed;
    private double theta        = 0;
    private double Right_RS2    = RotSpeed;
    private double theta2       = 0;
    private int    Mch          = 0;
    private int    Multi        = 9;

    private double ThtAbs1, ThtAbs2;
    private double LThtAbs1, LThtAbs2;

    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter     = new Formatter(statusMsg);  // Formatting the statusMs

    private double Left_Rotation_Speed   = RotSpeed;
    private double Left_theta            = 180;
    private double Left_RS2              = RotSpeed;
    private double Left_theta2           = 270;
    private int    LMch                  = 0;

    private int  c2,c3,c4,c5;

    private double AA_min   = .973;
    private double AA_max   = 1.027;
    private int ScorePen    = 1;
    private int ScoreMin    =  prefs.getInt("min_score", 0);


    SharedPreferences mSettings = getContext().getSharedPreferences("Settings", 0);
    SharedPreferences.Editor editor = mSettings.edit();

    private int score   = mSettings.getInt("current_score",ScoreMin);

    private int levl    = mSettings.getInt("levl",1);

    Bitmap left_Finger_Print = BitmapFactory.decodeResource(
            getResources(),
            R.drawable.thmb1);

    Bitmap right_Finger_Print = BitmapFactory.decodeResource(  // inverse print image
            getResources(),
            R.drawable.thmb1);


    MediaPlayer Hit_Sound  = MediaPlayer.create(this.getContext(), R.raw.b3);
    MediaPlayer Miss_Sound = MediaPlayer.create(this.getContext(), R.raw.b1);
    MediaPlayer SMP_Sound  = MediaPlayer.create(this.getContext(), R.raw.b2);

    public OFView_Activity(Context context) {
        super(context);

        detector   = new ScaleGestureDetector(getContext(), new ScaleListener());
        ballBounds = new RectF();
        paint      = new Paint();
        // Set the font face and size of drawing text

        if ( Peak_Score < 1000 ) {
            if     (Peak_Score < 10)  Peak_Value_X = (int) (.025 * Max_Width);
            else if(Peak_Score < 100) Peak_Value_X = (int) (.018  * Max_Width);
            else Peak_Value_X = (int) (.005  * Max_Width);

        }
        else Peak_Value_X = 0;
        int c1 = 0;
        while (c1 == 0) {
            Toast.makeText(getContext(), "Click during alignment to gain points ",
                    Toast.LENGTH_LONG).show();
            c1++;
        }
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
        ThtAbs1 = 0.0;
        ThtAbs2 = 0.0;
        LThtAbs1 = 0.0;
        LThtAbs2 = 0.0;
        c2 = 0;
        c3 = 0;
        c4 = 0;
        c5 = 0;
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

        draw_ball(canvas, ballBounds, MBxL, Static_Radius, Common_Center_Y, paint, Left_Color);
        draw_ball(canvas, ballBounds, B1XL, Dynamic_Radius, B1yL, paint, Left_Color);
        draw_ball(canvas, ballBounds, B2XL, Dynamic_Radius, B2yL, paint, Left_Color);
        draw_ball(canvas, ballBounds, MBx,  Static_Radius,  Common_Center_Y, paint, Right_Color);
        draw_ball(canvas, ballBounds, B1X,  Dynamic_Radius, B1y, paint, Right_Color);
        draw_ball(canvas, ballBounds, B2X,  Dynamic_Radius, B2y, paint, Right_Color);

        if( score == Peak_Score && score != ScoreMin) score_color = Green1;
        else if (score == ScoreMin) score_color = Red1;
        else score_color = ScCo;
        if( score > 9 ) {
            int lsf = (int) (Math.log10(score) + 1); // lsf = length_score_factor
            Score_X_Adjusted = (int)( (1 - (.0125 * lsf)) * Score_X_0_9);
        }
        else Score_X_Adjusted = Score_X_0_9;
        String SMP_Color = Green1;
        if(SMPs_Remaining > 9){
            int lsf2 = (int) (Math.log10(SMPs_Remaining) + 1); // lsf = length_score_factor
            SMP_X_Adjusted = (int)( (1 - (.0125 * lsf2)) * Score_X_0_9);
        }
        if( SMPs_Remaining == 0 ) SMP_Color = Red1;
        else if ( SMPs_Remaining < 3 ) SMP_Color = F1_color;
        else if ( SMPs_Remaining < 6 ) SMP_Color = Yellow;

        if ( Peak_Score < 1000 ) {
            if (Peak_Score  < 10)       Peak_Value_X = (int) (.025  * Max_Width);
            else if( Peak_Score < 100 ) Peak_Value_X = (int) (.018  * Max_Width);
            else Peak_Value_X = (int) (.0075  * Max_Width);
        }
        else Peak_Value_X = 0;
        canvas_text(canvas,Integer.toString(score), Score_X_Adjusted,
                Score_Value_Y, Text_Length, score_color);
        canvas_text(canvas, (getContext().getString(R.string.level_string) + Integer.toString(LS)), Level_Ind_X,
                Upper_Text_Y, Text_Length, Level_Color);
        String Min_Text =  getContext().getString(R.string.empty);
        if (score == ScoreMin){
           Min_Text =  getContext().getString(R.string.min);
       }
        canvas_text(canvas,Integer.toString(SMPs_Remaining),SMP_X_Adjusted,SMP_Value_Y,Text_Length,
                SMP_Color);
        canvas_text(canvas,getContext().getString(R.string.SMPs), SMP_Ind_X , SMP_Ind_Y,Text_Length, SMP_Color);
        canvas_text(canvas,getContext().getString(R.string.peak_text), 0, Upper_Text_Y, Text_Length, Green1);
        canvas_text(canvas,Min_Text, Min_Ind_X, Min_Ind_Y,Text_Length,Red1);
        canvas_text(canvas,Peak_Score_Value, Peak_Value_X, Peak_Next_Values_Y,Text_Length, Green1);

        if(LS == 5){
            next_level_value = " " + Integer.toString(Multi);
            if     (Multi  > 7)  next_color = F1_color;
            else if(Multi  > 5)  next_color = F2_color;
            else if(Multi  > 3)  next_color = F3_color;
            else if(Multi == 3)  next_color = F4_color;
            else if(Multi == 2)  next_color = F5_color;
            else if(Multi == 1)  next_color = Green1;
        }
        canvas_text(canvas, next_level_value,Next_Value_X, Peak_Next_Values_Y,
                Text_Length, next_color);
        canvas_text(canvas, next_text, Next_Ind_X, Upper_Text_Y, Text_Length, next_color);
        canvas.drawBitmap(right_Finger_Print, R_TE_X_S, C_TE_Y_S, null);
        canvas.drawBitmap(left_Finger_Print, L_TE_X_S, C_TE_Y_S, null);

        update();

        // Delay
        try {
            Thread.sleep(25);
        } catch (InterruptedException ignored) {
        }
        canvas.restore();
        invalidate();  // Force a re-draw
    }
    private static void orbit(Canvas canvas, Paint paint, String blue1, int CX, int CY, float FB1){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(blue1));
        canvas.drawCircle(CX, CY, FB1, paint);
    }
    private static void draw_ball(Canvas canvas, RectF ballBounds, float MBx, float MB_size,
                                  float MBy, Paint paint, String MB_c){
        paint.setStyle(Paint.Style.FILL);
        ballBounds.set(MBx - MB_size, MBy - MB_size, MBx + MB_size, MBy + MB_size);
        paint.setColor(Color.parseColor(MB_c));
        canvas.drawOval(ballBounds, paint);//must be done for each
    }
    private void canvas_text(Canvas canvas, String str, int x, int y, int t_size, String color){
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(t_size);
        formatter.format(str);
        paint.setColor(Color.parseColor(color));
        canvas.drawText(statusMsg.toString(), x, y, paint);
        statusMsg.delete(0, statusMsg.length());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean OnOff  = mSettings.getBoolean("sound",true);

        switch (event.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_DOWN:

                if(event.getX() >= R_TE_X_S && event.getX() <= R_TE_X_E && event.getY() >= C_TE_Y_S
                        && event.getY() < C_TE_Y_E){
                    right_Finger_Print = BitmapFactory.decodeResource(getResources(),
                            R.drawable.thmb2);
                    if (Mch == 10 || Mch == 5) {
                        Right_Color = Green1;
                        score_color = Green1;
                        if (Mch == 10) score += 10;
                        else score += 10;
                        if(OnOff) Hit_Sound.start();
                        Multi = Multi - 1;
                        editor.putInt("current_score", score);
                        Peak_Score_Check();
                        editor.commit();
                    } else {
                        Right_Color = Red1;
                        if (score >= (ScoreMin + ScorePen)) {
                            score -= ScorePen;
                            editor.putInt("current_score", score);
                            Multi = 9;
                            editor.commit();
                            if(OnOff) Miss_Sound.start();
                            score_color = Red1;
                        }
                    }
                }
                else if(event.getX() >= L_TE_X_S && event.getX() <= L_TE_X_E && event.getY() >=
                        C_TE_Y_S && event.getY() < C_TE_Y_E){

                    left_Finger_Print = BitmapFactory.decodeResource(getResources(),R.drawable.
                            thmb2);
                    if(LMch == 10 || LMch == 5) {
                        Left_Color = Green1;
                        score_color = Green1;
                        if (LMch == 10) score += 10;
                        else score += 10;
                        if(OnOff) Hit_Sound.start();
                        Multi = Multi - 1;
                        editor.putInt("current_score", score);
                        Peak_Score_Check();
                        editor.commit();
                    }else{
                        Left_Color = Red1;
                        if (score >= (ScoreMin + ScorePen)){
                            score -= ScorePen;
                            editor.putInt("current_score",score);
                            Multi = 9;
                            if(OnOff) Miss_Sound.start();
                            editor.commit();
                            score_color = Red1;
                       }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                right_Finger_Print = BitmapFactory.decodeResource(getResources(),R.drawable.thmb1);
                left_Finger_Print  = BitmapFactory.decodeResource(getResources(),R.drawable.thmb1);
                break;
        }
        detector.onTouchEvent(event);
        return true;
    }

    private void update() {
        final boolean OnOff  = mSettings.getBoolean("sound",true);

        int level2_min = getContext().getResources().getInteger(R.integer.L1_target_score);
        int level3_min = getContext().getResources().getInteger(R.integer.L2_target_score);
        int level4_min = getContext().getResources().getInteger(R.integer.L3_target_score);
        int level5_min = getContext().getResources().getInteger(R.integer.L4_target_score);
        int level2_pen = getContext().getResources().getInteger(R.integer.L2_penalty);
        int level3_pen = getContext().getResources().getInteger(R.integer.L3_penalty);
        int level4_pen = getContext().getResources().getInteger(R.integer.L4_penalty);
        int level5_pen = getContext().getResources().getInteger(R.integer.L5_penalty);

        score_color = ScCo;
        if (score <  ScoreMin) {
            score = ScoreMin;
        }
        if ( score >= level2_min && score < level3_min ){
            ScCo = L2col;
            if ( score   < ScoreMin + level2_pen ) ScorePen = score - ScoreMin;
            else ScorePen = level2_pen;

            if ( c2 == 0 ) {
                c2++;
                if ( levl < 2 ) {
                    editor.putInt("levl", 2);
                    editor.putInt("min_score", level2_min);
                    SMPs_Remaining = SMPs_Remaining + 1;
                    if(OnOff) SMP_Sound.start();
                    editor.putInt("set_peak_min",SMPs_Remaining);
                }

                editor.putInt("scorelevel",level2_min);
                editor.commit();
                next_color = L3col;
                LS               = 2;
                next_text        = getContext().getString(R.string.level_3);
                next_level_value = Integer.toString(level3_min);
                Level_Color      = L2col;
                if (ScoreMin  < level2_min) ScoreMin  = level2_min;
                AA_min           = .975;
                AA_max           = 1.025;
            }
        }
        else if ( score  >= level3_min && score < level4_min ) {
            ScCo = L3col;
            if ( score   < ScoreMin + level3_pen ) ScorePen = score - ScoreMin;
            else ScorePen = level3_pen;
            if ( c3 == 0 ) {
               c3++;
               if (levl < 3) {
                   editor.putInt("levl", 3);
                   editor.putInt("min_score",level3_min);
                   SMPs_Remaining = SMPs_Remaining + 2;
                   if(OnOff) SMP_Sound.start();
                   editor.putInt("set_peak_min",SMPs_Remaining);
               }
                LS               = 3;
                next_text        = getContext().getString(R.string.level_4);
                next_color       = L4col;
                next_level_value = Integer.toString(level4_min);
                Level_Color      = L3col;
                editor.putInt("scorelevel",level3_min);
                editor.commit();
                if (ScoreMin  < level3_min)ScoreMin = level3_min;
                AA_min           = .98;
                AA_max           = 1.02;
            }
        }else if ( score >= level4_min && score < level5_min){
            ScCo = L4col;
            if ( score   < ScoreMin + level4_pen ) ScorePen = score - ScoreMin;
            else ScorePen = level4_pen;

            if ( c4==0 ) {
               c4++;
               if (levl < 4) {
                   editor.putInt("levl", 4);
                   editor.putInt("min_score",level4_min);
                   SMPs_Remaining = SMPs_Remaining + 2;
                   if(OnOff) SMP_Sound.start();
                   editor.putInt("set_peak_min",SMPs_Remaining);
               }
                editor.putInt("scorelevel",level4_min);
                editor.commit();
                next_text        = getContext().getString(R.string.level_5);
                next_color       = L5col;
                next_level_value = Integer.toString(level5_min);
                Level_Color      = L4col;
                if (ScoreMin  < level4_min) ScoreMin  = level4_min;
                AA_min          = .982;
                AA_max          = 1.018;
                LS              = 4;

           }
        }else if ( score >= level5_min ){
            ScCo = L5col;
            if ( score   < ScoreMin + level5_pen ) ScorePen = score - ScoreMin;
            else ScorePen = level5_pen;

            if( c5 == 0) {
                c5++;
                if (levl < 5) {
                    editor.putInt("levl", 5);
                    editor.putInt("min_score",level5_min);
                    SMPs_Remaining  = SMPs_Remaining + 5;
                    if(OnOff) SMP_Sound.start();
                    editor.putInt("set_peak_min",SMPs_Remaining);
                   }
                editor.putInt("scorelevel",level5_min);
                editor.commit();
                next_text          = getContext().getString(R.string.multi);
                next_color         = L5col;
                Integer Multi = prefs.getInt("multi", 9);
                next_level_value   = " " + Integer.toString(Multi);
                Level_Color        = L5col;
                if (ScoreMin  < level5_min) ScoreMin = level5_min;
                AA_min            = .985;
                AA_max            = 1.015;
                LS                = 5;
            }
        }
        if (Multi == 0){
            SMPs_Remaining = SMPs_Remaining  + 1;
            if(OnOff) SMP_Sound.start();
            Multi = 9;
            editor.putInt("set_peak_min",SMPs_Remaining);
            editor.apply();
        }
        ScoreRSpeed();
        ThetaCalc();
        double Ex  = B1dist * Math.cos(Math.toRadians(theta));
        double Ey  = B1dist * Math.sin(Math.toRadians(theta));
        double E2x = B2dist * Math.cos(Math.toRadians(theta2));
        double E2y = B2dist * Math.sin(Math.toRadians(theta2));
        B1X = CX + (float) Ex;
        B1y = CY + (float) Ey;
        B2X = CX + (float) E2x;
        B2y = CY + (float) E2y;
        Ex  = B1dist * Math.cos(Math.toRadians(Left_theta));
        Ey  = B1dist * Math.sin(Math.toRadians(Left_theta));
        E2x = B2dist * Math.cos(Math.toRadians(Left_theta2));
        E2y = B2dist * Math.sin(Math.toRadians(Left_theta2));
        B1XL = CXL + (float) Ex;
        B1yL = CY  + (float) Ey;
        B2XL = CXL + (float) E2x;
        B2yL = CY  + (float) E2y;

        if (!(Right_Color.equals( Level_Color)) || !(Left_Color.equals(Level_Color))){
            Sleep(80);
            Right_Color = Level_Color;
            Left_Color  = Level_Color;
        }
    }
   private void ScoreRSpeed(){
       Right_RS            = RotSpeed;
       Right_RS2           = RotSpeed;
       Left_Rotation_Speed = RotSpeed;
       Left_RS2            = RotSpeed;

       if      (score >= 50   && score < 100)  RotSpeed = 1.3;
       else if (score >= 100  && score < 175)  RotSpeed = 1.5;
       else if (score >= 175  && score < 300)  RotSpeed = 1.65;
       else if (score >= 300  && score < 400)  RotSpeed = 1.75;
       else if (score >= 400  && score < 600)  RotSpeed = 1.85;
       else if (score >= 600  && score < 800)  RotSpeed = 1.9;
       else if (score >= 800  && score < 1000) RotSpeed = 1.95;
       else if (score >= 1000 && score < 1100) RotSpeed = 2.05;
       else if (score >= 1100 && score < 1300) RotSpeed = 2.2;
       else if (score >= 1300 && score < 1600) RotSpeed = 2.4;
       else if (score >= 1600)                 RotSpeed = 2.5;
   }
    private void ThetaCalc(){
        theta += Right_RS;
        if(theta > 360 || theta < -360) theta = 0;
        Left_theta += Left_Rotation_Speed;
        if(Left_theta > 360 || Left_theta < -360) Left_theta = 0;
        theta2 -= Right_RS2;
        if(theta2 > 360 || theta2 < -360) theta2 = 0;
        Left_theta2 -= Left_RS2;
        if(Left_theta2 > 360 || Left_theta2 < -360) Left_theta2 = 0;
        if(theta < 0) ThtAbs1 = theta + 360;
        else ThtAbs1 = theta;
        if(Left_theta < 0) LThtAbs1 = Left_theta + 360;
        else LThtAbs1 = Left_theta;
        if(theta2 < 0) ThtAbs2 = theta2 + 360;
        else ThtAbs2 = theta2;
        if(Left_theta2 < 0)LThtAbs2    = Left_theta2 + 360;
        else LThtAbs2 = Left_theta2;


        if(ThtAbs2 > AA_min * ThtAbs1 && ThtAbs2 < AA_max * ThtAbs1)Mch  = 10;
        else if(ThtAbs2 - ThtAbs1 > AA_min * 180 && ThtAbs2 - ThtAbs1 < AA_max * 180)Mch  = 5;
        else if(ThtAbs1 - ThtAbs2 > AA_min * 180 && ThtAbs1 - ThtAbs2 < AA_max * 180)Mch  = 5;
        else Mch  = 0;
        if(LThtAbs2 > AA_min * LThtAbs1 && LThtAbs2 < AA_max * LThtAbs1) LMch = 10;
        else if(LThtAbs2 - LThtAbs1 > AA_min * 180 && LThtAbs2 - LThtAbs1 < AA_max * 180) LMch = 5;
        else if(LThtAbs1 - LThtAbs2 > AA_min * 180 && LThtAbs1 - LThtAbs2 < AA_max * 180) LMch = 5;
        else LMch = 0;
        score_color = ScCo;
   }
   private void Peak_Score_Check(){
       if (score > Peak_Score) {
           Peak_Score = score;
           Peak_Score_Value = Integer.toString(Peak_Score);
           editor.putInt("peakscore", Peak_Score);
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