package bw.projxconcept;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * This view represents one pixel on the LED game board, will be toggled on any touch input
 */
public class InputSquare extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private boolean mIsOn;
    private int mColor;//color to show when toggled on
    private int mBaseColor;//default color - the color to show when toggled off
    private int mViewWidth;
    private int mViewHeight;

    private Paint mPaint;

    public InputSquare(Context context) {
        super(context);
        mBaseColor = Color.WHITE;
        mColor = Color.BLACK;
        init();
    }

    public InputSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputSquare(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onSizeChanged(int w, int h , int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        mViewWidth = w;
        mViewHeight = h;
        return;
    }

    public boolean isOn(){
        return mIsOn;
    }

    public void setOn(boolean isOn){
        mIsOn = isOn;
    }

    public int getColor(){
        return mColor;
    }

    public void setColor(int newColor)
    {
//        if(newColor == null)
//            throw new NullPointerException("newColor cannot be null!");
        mColor = newColor;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mIsOn)
        {
            mPaint.setColor(mColor);
        }
        else
        {
            mPaint.setColor(mBaseColor);
        }

        //draw fill
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0, mViewWidth, mViewHeight, mPaint);

        //draw outline
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,mViewWidth,mViewHeight,mPaint);


    }

    public void toggle(){
        mIsOn = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                toggle();
                this.invalidate();
                Log.i("tag","Entered ActionDown");
                return true;
            }
            case MotionEvent.ACTION_CANCEL:case MotionEvent.ACTION_OUTSIDE: {
                toggle();
                this.invalidate();
                Log.i("tag", "Entered Hover_Enter");
                return true;
            }
            default: {
                Log.i("tag", "Entered something else: Action # = "+event.getAction());
                return false;
            }
        }
    }

}
