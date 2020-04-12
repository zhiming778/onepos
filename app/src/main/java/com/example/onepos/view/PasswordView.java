package com.example.onepos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.onepos.R;

public class PasswordView extends View {
    private int width;
    private int height;
    private boolean layoutFinished;
    private int interval;
    private final int RADIUS;
    private final int NUM_OF_DIGITS;
    private int digits;
    private Paint paintPW, paintDivider;

    public PasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        width = 0;
        height = 0;
        layoutFinished = false;
        NUM_OF_DIGITS = 4;
        RADIUS = 20;
        digits = 0;
        int color = context.getColor(R.color.colorText);
        paintPW = new Paint();
        paintPW.setStyle(Paint.Style.FILL);
        paintPW.setColor(color);
        paintPW.setAntiAlias(true);
        paintDivider = new Paint();
        paintDivider.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDividers(canvas);
        if (layoutFinished)
            drawPassword(canvas);
    }

    private void drawDividers(Canvas canvas) {
        for (int i = 0; i < NUM_OF_DIGITS - 1; i++) {
            canvas.drawLine(interval*(i+1), 0, interval*(i+1), height, paintDivider);
        }
        canvas.drawLine(0, height-1, width, height-1, paintPW);
    }
    private void drawPassword(Canvas canvas) {
        for (int i = 0; i < digits; i++) {
            canvas.drawCircle(interval/2+interval*i, height/2, RADIUS, paintPW);
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        layoutFinished = true;
        interval = width / NUM_OF_DIGITS;

    }

    public void setDigits(int digits) {
        this.digits = digits;
        invalidate();
    }
}
