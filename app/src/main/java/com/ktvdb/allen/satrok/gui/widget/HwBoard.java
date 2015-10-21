package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;

import com.wwengine.hw.WWHandWrite;

import java.io.InputStream;

/**
 * Created by Allen on 15/8/31.
 */
public class HwBoard extends AbsoluteLayout
{
    private float mx;
    private float my;
    private Path  mPath;
    private Paint mPaint;
    private Paint mPaintText;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private static char[]  mResult1;
    private static short[] mTracks;
    private static int     mCount;
    private static Context mContext;
    private static int     mFontSize;

    public HwBoard(Context context)
    {
        this(context, null);
    }

    public HwBoard(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public HwBoard(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setBackgroundResource(android.R.color.background_dark);

        {
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFFFF0000);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(6);
        }

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(0xFFFF0000);
        mPaintText.setTextSize(20);
        mPaintText.setTextAlign(Paint.Align.LEFT);

        byte[] hwData = readData(context.getAssets(), "hwdata.bin");
        if (hwData == null)
        {
            return;
        }

        WWHandWrite.apkBinding(context);

        if (WWHandWrite.hwInit(hwData, 0) != 0)
        {
            return;
        }

        mResult1 = new char[256];
        mTracks = new short[1024];
        mCount = 0;
    }

    private static byte[] readData(AssetManager am, String name)
    {
        try
        {

            InputStream is = am.open(name);
            if (is == null)
            {
                return null;
            }

            int length = is.available();
            if (length <= 0)
            {
                return null;
            }

            byte[] buf = new byte[length];

            if (is.read(buf, 0, length) == -1)
            {
                return null;
            }

            is.close();

            return buf;

        }
        catch (Exception ex)
        {
            return null;
        }
    }

    private void touch_start(float x, float y)
    {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

        mTracks[mCount++] = (short) x;
        mTracks[mCount++] = (short) y;
    }

    private void touch_move(float x, float y)
    {
        {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
            {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }
        mTracks[mCount++] = (short) x;
        mTracks[mCount++] = (short) y;
    }

    private void touch_up()
    {
        mPath.lineTo(mX, mY);

        mTracks[mCount++] = -1;
        mTracks[mCount++] = 0;
        recognize();
    }

    private void recognize()
    {
        short[] mTracksTemp;
        int     countTemp = mCount;

        mTracksTemp = mTracks.clone();
        mTracksTemp[countTemp++] = -1;
        mTracksTemp[countTemp++] = -1;

        WWHandWrite.hwRecognize(mTracksTemp, mResult1, 10, 0xFFFF);
    }

    public void reset_recognize()
    {
        mCount = 0;
        mResult1 = new char[256];
        {
            mPath.reset();
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);

        //canvas.drawText(mResult.toString(), 10, 100, mPaintText);
        canvas.drawText(mResult1, 0, 10, 5, 20 + mFontSize / 2, mPaintText);
    }
}
