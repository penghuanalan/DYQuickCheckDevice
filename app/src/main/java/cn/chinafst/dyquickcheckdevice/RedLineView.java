package cn.chinafst.dyquickcheckdevice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class RedLineView extends ImageView {
    private int width,height;

    public RedLineView(Context context) {
        super(context);


    }

    public RedLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAlpha(250);
        // 下面是取景框的8条线
        // xy的算法是：把屏幕横着(逆时针旋转90度的屏幕)，从左到右是x轴，从上到下是y轴
      /*  canvas.drawLine(200, 150, 300, 150, paint);
        canvas.drawLine(200, 150, 200, 200, paint);

        canvas.drawLine(200, height-150, 300, height-150, paint);
        canvas.drawLine(200, height-150, 200, height-200, paint);

        canvas.drawLine(width-355, 150, width-455, 150, paint);
        canvas.drawLine(width-355, 150, width-355, 200, paint);

        canvas.drawLine(width-355, height-150, width-455, height-150, paint);
        canvas.drawLine(width-355, height-150, width-355, height-200, paint);*/
        canvas.drawLine(width/2-50, height/2-300, width/2+50, height/2-300, paint);
        canvas.drawLine(width/2-100, height/2-500, width/2-100, height/2+300, paint);
        canvas.drawLine(width/2+100, height/2-500, width/2+100, height/2+300, paint);
        paint.setTextSize(50); //以px为单位
        canvas.drawText("C",width/2+50,height/2-300,paint);

        super.onDraw(canvas);

    }
}
