package com.ktvdb.allen.satrok.gui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.model.Advertisement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Allen on 15/8/22.
 */
public class ScrollTextView extends SurfaceView implements SurfaceHolder.Callback
{

    private final SurfaceHolder mSurfaceHolder;
    private       Thread        mScrollThread;
    private       boolean       isScroll;

    private String currentText;
    private List<Advertisement> mTextList = new ArrayList<>();

    private int     textColor;
    private int     textSize;
    private String  mNextText;
    private int     currentScrollX;
    private int     mScrollSpeed;
    private boolean isAdd;

    public ScrollTextView(Context context)
    {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT); // 顶层绘制SurfaceView设成透明
        this.setZOrderOnTop(true);
        @SuppressLint("Recycle")
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                                                               R.styleable.ScrollTextView);
        textColor = typedArray.getColor(R.styleable.ScrollTextView_stColor, Color.WHITE);
        textSize = typedArray.getInteger(R.styleable.ScrollTextView_stTextSize, 22);
        mScrollSpeed = typedArray.getInteger(R.styleable.ScrollTextView_stScrollSpeed, 5);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mScrollThread = new Thread(new ScrollRunnable());
        isScroll = true;
        mScrollThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }

    class ScrollRunnable implements Runnable
    {
        private Paint mPaint = null;
        private float textWidth;
        private ListIterator<Advertisement> listIterator = null;
        final   PaintFlagsDrawFilter        drawFilter   = new PaintFlagsDrawFilter(0,
                                                                                    Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        @Override
        public void run()
        {
            Canvas canvas = null;
            //整个空间的宽度
            int mWidth = ScrollTextView.this.getWidth();
            while (isScroll)
            {
                try
                {
                    if (mTextList.size() > 0)
                    {
                        if (mPaint == null)
                        {
                            createPanint();
                            // 得到文本的长度。
                            textWidth = mPaint.measureText(currentText);
                        }
                        // 滚动速度
                        currentScrollX -= mScrollSpeed;
                        canvas = mSurfaceHolder.lockCanvas(new Rect(0,
                                                                    0,
                                                                    ScrollTextView.this.getWidth(),
                                                                    ScrollTextView.this.getHeight()));
                        if (canvas != null)
                        {
                            // 退出时holder.lockCanvas（）方法可能返回空，未免报空指针异常
                            // 清除画布方法一
                            canvas.setDrawFilter(drawFilter);
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                            canvas.drawText(currentText, 0, currentText.length(),
                                            currentScrollX, mPaint.getTextSize(), mPaint);
                            mSurfaceHolder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
                        }

                        if ((currentScrollX + textWidth + getWidth()) <= mWidth)
                        {
                            currentText = mNextText;
                            textWidth = mPaint.measureText(currentText);// 得到文本的长度。
                            findNext();
                        }

                        if (isAdd)
                        {
                            currentScrollX = ScrollTextView.this.getWidth();
                            isAdd = false;
                        }
                        Thread.sleep(50);
                    }
                }
                catch (Exception e)
                {
                    LogUtils.e("ScrollSurfaceView：绘制失败...\r\n" + e);
                }

            }
        }

        private void createPanint()
        {
            mPaint = new Paint();
            listIterator = mTextList.listIterator();
            if (listIterator.hasNext())
            {
                currentText = listIterator.next().getContent();
                findNext();
            }
            mPaint.setTextSize(textSize);
            mPaint.setAntiAlias(true);
            mPaint.setColor(textColor);
        }

        void findNext()
        {
            if (listIterator.hasNext())
            {
                mNextText = listIterator.next().getContent();
            }
            else
            { // 文本集合播完了就再播第一项。
                isAdd = true;
                while (listIterator.hasPrevious())
                {
                    listIterator.previous();
                }
                mNextText = listIterator.next().getContent();
            }
        }
    }

    public void addScrollText(Advertisement advertisement)
    {
        mTextList.add(advertisement);
    }

    public void addScrollText(Collection<Advertisement> advertisements)
    {
        mTextList.addAll(advertisements);
    }

}
