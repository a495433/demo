package com.example.mm.diyprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by mm on 16-9-10m  test
 */
public class HorizontalProgressBar extends ProgressBar {
    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACH = 0xFF0000FF;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;//dp
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH = 2;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp

    private int mTextSize = Sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mUnReachColor = DEFAULT_COLOR_UNREACH;
    private int mUnReachHeight = Dp2px(DEFAULT_HEIGHT_UNREACH);
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mReachHeight = Dp2px(DEFAULT_HEIGHT_REACH);
    private int mTextOffset = Dp2px(DEFAULT_TEXT_OFFSET);
    private Paint mPaint = new Paint();
    private int mRealWidth;


    public HorizontalProgressBar(Context context) {
        this(context,null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ObtainStyledAttrs(attrs);
    }
    //获取自定义属性
    private void ObtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.HorizontalProgressBar);
        mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_text_size,mTextSize);
        mTextColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_text_color,mTextColor);
        mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_textoffset,mTextOffset);
        mUnReachColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_unreach_color,mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_unreach_height,mUnReachHeight);
        mReachColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_reach_color,mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_reach_height,mReachHeight);
        mPaint.setTextSize(mTextSize);
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthval = MeasureSpec.getSize(widthMeasureSpec);

        int height = MeasureHeight(heightMeasureSpec);
        setMeasuredDimension(widthval,height);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int MeasureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mReachHeight,mUnReachHeight),Math.abs(textHeight));
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(textHeight,size);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);
        boolean noNeedUnRech = false;
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        float radio = getProgress() * 1.0f / getMax();
        float useWidth = mRealWidth - mTextOffset * 2 - textWidth;
        float progressX = radio * useWidth;
        if (getProgress() == getMax()) {
            progressX = useWidth + mTextOffset;
            noNeedUnRech = true;
        }
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        canvas.drawLine(0,0,progressX,0,mPaint);
        int y = (int) ((-(mPaint.descent() + mPaint.ascent())) / 2);
        mPaint.setColor(mTextColor);
        canvas.drawText(text,progressX + mTextOffset,y,mPaint);
        if (noNeedUnRech) {
            canvas.restore();
            return;
        }
        float start = progressX + mTextOffset * 2 + textWidth;
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawLine(start,0,mRealWidth,0,mPaint);
        canvas.restore();
    }

    private int Dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
    }
    private int Sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,getResources().getDisplayMetrics());
    }
}
