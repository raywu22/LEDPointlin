package com.projx.ledpointlin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Brandon on 1/27/2016.
 */
public class PixelGridView extends View{
    private Paint mPaint;

    private int mNumHeightPixels, mNumWidthPixels;//dimensions of LED grid
    private int mSquareWidth;
    private PixelSquare mPixelArray[][];
    private PixelChangedListener mListener;


    /**
     * Listener that gets called when a input event changing pixels on this view is finished
     * Right now is fired on every tap b/c that is the only touch events that are logged
     * TODO: Track multiple changes of pixels using slide input gestures
     */
    public interface PixelChangedListener
    {
        public abstract void onPixelsChanged();
    }

    private class PixelSquare {
        private int mX, mY;
        private int mSquareWidth;
        private int mColor;
        private int mBaseColor;
        private boolean mToggled;


        public PixelSquare(int color, int squareWidth, int x, int y){
            mX = x;
            mY = y;
            mSquareWidth = squareWidth;
            mColor  = color;
            mBaseColor = Color.BLUE;
            mToggled = false;
        }

        public void setSquareWidth(int squareWidth)
        {
            mSquareWidth = squareWidth;
        }

        public void setColor(int color) {
            mColor = color;
        }

        public void draw(Canvas canvas, Paint paint)
        {
            if(mToggled)
            {
                paint.setColor(mColor);
            }
            else {
                paint.setColor(mBaseColor);
            }

            Log.i("tag", "DREW PIXEL SQUARE - toggled = " + mToggled);


            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(mX, mY, mX + mSquareWidth, mY + mSquareWidth, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            canvas.drawRect(mX, mY, mX+mSquareWidth, mY+mSquareWidth, paint);
            Log.i("tag","Drew rect at :"+mX+","+mY+" and corner at "+mX+mSquareWidth+","+mY+mSquareWidth);
        }

        public void toggleColor()
        {
            mToggled = !mToggled;
            Log.i("tag","toggled!");
        }

        public void setXY(int x, int y)
        {
            mX = x;
            mY = y;
        }

        public int getDisplayColor() {
            if (mToggled) {
                return mColor;
            } else {
                return mBaseColor;
            }

        }

        public void setBaseColor(int color)
        {
            mBaseColor = color;
        }

    }


    public PixelGridView(Context context, int heightPixels, int widthPixels){
        super(context);
        mNumHeightPixels = heightPixels;
        mNumWidthPixels = widthPixels;
        mSquareWidth = -1;
        init();
    }


    public void setPixelChangedListener(PixelChangedListener listener){
        mListener = listener;
    }

    public void setBaseColors(int [][] state)
    {
        for(int row = 0 ; row < mNumHeightPixels ; row++)
        {
            for(int col = 0 ; col < mNumWidthPixels ; col++)
            {
                mPixelArray[row][col].setBaseColor(state[row][col]);
            }
        }

        this.invalidate();
    }

    public void setBaseColor(int row, int col, int color)
    {
        mPixelArray[row][col].setBaseColor(color);
    }

    public void setActiveColors(int [][] state)
    {
        for(int row = 0 ; row < mNumHeightPixels ; row++)
        {
            for(int col = 0 ; col < mNumWidthPixels ; col++)
            {
                mPixelArray[row][col].setColor(state[row][col]);
            }
        }

        this.invalidate();
    }



    /**
     * Get the grid state of the grid view
     * @return returns a 2D array of arrays - each value within array is the color of that pixel in grid
     */
    public int[][] getGridState(){
        int[][] state = new int[mNumHeightPixels][mNumWidthPixels];

        for(int row = 0 ; row < mNumHeightPixels ; row++)
        {
            for(int col = 0 ; col < mNumWidthPixels ; col++)
            {
                state[row][col] =  mPixelArray[row][col].getDisplayColor();
            }
        }

        return state;

    }


    private void init(){
        mPaint = new Paint();
        instantiateGridPixels();
    }



    private void instantiateGridPixels()
    {
        //instantiate grid array
        mPixelArray = new PixelSquare[mNumHeightPixels][mNumWidthPixels];

        for(int i = 0 ; i<mNumHeightPixels ; i++){
            for(int j = 0 ; j < mNumWidthPixels ; j++)
            {
                PixelSquare curr = new PixelSquare(Color.RED, i*mSquareWidth, j*mSquareWidth, mSquareWidth);
                mPixelArray[i][j] = curr;
            }
        }
    }

    private void updateGridPixels()
    {
        for(int i = 0 ; i<mNumHeightPixels ; i++){
            for(int j = 0 ; j < mNumWidthPixels ; j++)
            {
                mPixelArray[i][j].setXY(j*mSquareWidth,i*mSquareWidth);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = event.getX();
                float y = event.getY();

                //TODO: test this
                int row = (int) (y / mSquareWidth);
                int col = (int) (x / mSquareWidth);

                //edge case
                if (row == mNumHeightPixels)
                {
                    row -= 1;
                }
                if(col == mNumWidthPixels)
                {
                    col -= 1;
                }

                Log.i("tag","Row = "+row +"/Col = "+col );

                if(row >= mNumHeightPixels || col > mNumWidthPixels)
                {
                    Log.i("tag","Logged tap outside of grid bounds!");
                }
                {
                    mPixelArray[row][col].toggleColor();
                }
                //TODO:
                //toggle pixel view at (row, col) in 2d collection of views
                //invalidate this view to redraw it

                
                this.invalidate();
                Log.i("tag", "Entered ActionDown");
                if(mListener != null)
                {
                    mListener.onPixelsChanged();
                }
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h , int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);

        //maintain square shape
        //so find the side length so that the longest side of the grid fits snugly wihtin the parent
        int fromWidth = w / mNumWidthPixels;
        int fromHeight = h / mNumHeightPixels;

        mSquareWidth = Math.min(fromWidth, fromHeight);

        //update square widths of pixel views
        for(PixelSquare[] row : mPixelArray)
        {
            for(PixelSquare pixel : row)
            {
                pixel.setSquareWidth(mSquareWidth);
            }
        }

        //recalculate grid pixel locations based on new sizes
        updateGridPixels();

        Log.i("tag", "OnSizechanged: msquarewidth = "+mSquareWidth+" /width="+w+"/height = "+h);
        return;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        //draw all pixel squares
        for(PixelSquare[] row : mPixelArray)
        {
            for(PixelSquare pixel : row)
            {
                pixel.draw(canvas,mPaint);
            }
        }

        canvas.drawRect(200,200,50,50,mPaint);

        Log.i("tag","Drew pixelsquare");
    }


}
