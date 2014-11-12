package net.pooksoft.nrclicker.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class VerticalTextView extends TextView {
    final boolean topDown;

    public VerticalTextView(Context context, AttributeSet attrs){
        super(context, attrs);

        topDown = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b){
        return super.setFrame(l, t, l+(b-t), t+(r-l));
    }

    @Override
    public void draw(Canvas canvas){
        if(topDown){
            canvas.translate(getHeight(), 0);
            canvas.rotate(90);
        }else {
            canvas.translate(0, getWidth());
            canvas.rotate(-90);
        }
        canvas.clipRect(0, 0, getWidth(), getHeight(), android.graphics.Region.Op.REPLACE);
        super.draw(canvas);
    }
}