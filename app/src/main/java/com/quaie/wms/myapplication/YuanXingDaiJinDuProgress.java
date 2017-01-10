package com.quaie.wms.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by yue on 2017/1/10.
 * 　　　　　　　  ┏┓　 ┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　     ┃
 * 　　　　　　　┃　　　━　    ┃ ++ + + +
 * 　　　　　　 ████━████     ┃++  ++
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃  +  +
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 */

public class YuanXingDaiJinDuProgress extends ProgressBar {

    private static final int DEFAULT_TEXT_SIEZ = 10;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_TEXT_OFFSET = 10;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;
    private static final int DEFAULT_HEIGHT_REACH = 2;
    private static final int DEFAULT_COLOR_UNREACH = 0xFFD3D6DA;
    private static final int DEFAULT_COLOR_REACH = 0xFFFC00D1;

    private int mTextSize = sp2px(DEFAULT_TEXT_SIEZ);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextOffSet = dp2px(DEFAULT_TEXT_OFFSET);
    private int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);
    private int mUnReachHeight = dp2px(DEFAULT_HEIGHT_UNREACH);
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mUnReachColor = DEFAULT_COLOR_UNREACH;
    //画圆的半径
    private int mRadius = dp2px(30);

    //进度条的宽度
    private int mMaxPainWidth;

    private Paint mPaint = new Paint();

    public YuanXingDaiJinDuProgress(Context context) {
        this(context, null);
    }

    public YuanXingDaiJinDuProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YuanXingDaiJinDuProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(attrs);

    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.YuanXingDaiJinDuProgress);

        mTextSize = (int) ta.getDimension(R.styleable.YuanXingDaiJinDuProgress_Rprogress_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.YuanXingDaiJinDuProgress_Rprogress_text_color, mTextColor);
        mTextOffSet = (int) ta.getDimension(R.styleable.YuanXingDaiJinDuProgress_Rprogress_text_offset, mTextOffSet);
        mReachColor = ta.getColor(R.styleable.YuanXingDaiJinDuProgress_Rprogress_reach_color, mReachColor);
        mUnReachColor = ta.getColor(R.styleable.YuanXingDaiJinDuProgress_Rprogress_unreach_color, mUnReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.YuanXingDaiJinDuProgress_Rprogress_reach_height, mReachHeight);
        mUnReachHeight = (int) ta.getDimension(R.styleable.YuanXingDaiJinDuProgress_Rprogress_unreach_height, mUnReachHeight);
        mRadius = (int) ta.getDimension(R.styleable.YuanXingDaiJinDuProgress_Rprogress_radius, mRadius);

        ta.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //进度条的宽度选最大的外圆
        //mMaxPainWidth = Math.max(mReachHeight, mUnReachHeight);
        mMaxPainWidth = mReachHeight;

        int expect = mRadius * 2 + mMaxPainWidth + getPaddingLeft() + getPaddingRight();

        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);
        int readWidth = Math.min(width, height);

        mRadius = (readWidth - getPaddingLeft() - getPaddingRight() - mMaxPainWidth) / 2;

        setMeasuredDimension(readWidth, readWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        canvas.save();

        //把控件放到指定的位置
        canvas.translate(getPaddingLeft() + mMaxPainWidth / 2, getPaddingTop() + mMaxPainWidth / 2);

        mPaint.setStyle(Paint.Style.STROKE);

        //绘制unreachbar
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        //画圆，前两个参数是圆心坐标，第三个参数是半径
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        //绘制reachbar
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        //计算弧度
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), sweepAngle, sweepAngle, false, mPaint);

        //绘制进度
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight / 2, mPaint);

        canvas.restore();
    }

    //单位转化
    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal,
                getResources().getDisplayMetrics());
    }

    //单位转化
    private int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal,
                getResources().getDisplayMetrics());
    }
}
