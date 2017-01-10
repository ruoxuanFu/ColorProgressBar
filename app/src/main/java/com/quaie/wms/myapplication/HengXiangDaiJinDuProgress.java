package com.quaie.wms.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
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

public class HengXiangDaiJinDuProgress extends ProgressBar {

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

    private Paint mPaint = new Paint();

    //控件当前宽度减去padding的宽度剩下的真正宽度
    private int mRealWidth;

    public HengXiangDaiJinDuProgress(Context context) {
        this(context, null);
    }

    public HengXiangDaiJinDuProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HengXiangDaiJinDuProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(attrs);
    }


    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HengXiangDaiJinDuProgress);

        mTextSize = (int) ta.getDimension(R.styleable.HengXiangDaiJinDuProgress_progress_text_size, mTextSize);
        mTextColor = ta.getColor(R.styleable.HengXiangDaiJinDuProgress_progress_text_color, mTextColor);
        mTextOffSet = (int) ta.getDimension(R.styleable.HengXiangDaiJinDuProgress_progress_text_offset, mTextOffSet);
        mReachColor = ta.getColor(R.styleable.HengXiangDaiJinDuProgress_progress_reach_color, mReachColor);
        mUnReachColor = ta.getColor(R.styleable.HengXiangDaiJinDuProgress_progress_unreach_color, mUnReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.HengXiangDaiJinDuProgress_progress_reach_height, mReachHeight);
        mUnReachHeight = (int) ta.getDimension(R.styleable.HengXiangDaiJinDuProgress_progress_unreach_height, mUnReachHeight);

        ta.recycle();

        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);

        int height = measuerHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedUnReach = false;

        //设置文本
        String text = getProgress() + "%";
        //得到文本宽度
        int textWidth = (int) mPaint.measureText(text);

        //计算进度
        float radio = getProgress() * 1.0f / getMax();

        //计算进度百分比
        float progressX = radio * mRealWidth;

        //真正的绘制区域：radio * 真正的总长度 - 字体前面的textoffset的二分之一
        float endX = progressX - mTextOffSet / 2;
        //当文字到最后面的时候，reachbar就不再不需要继续向右绘制，否则会穿过字体
        float endx = mRealWidth - mTextOffSet / 2 - textWidth;

        //当进度+字体宽度 = 真正的宽度时，就不在绘制后面的unreach的部分
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnReach = true;
        }

        //绘制reachbar
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, Math.min(endX, endx), 0, mPaint);
        }

        //绘制文本
        mPaint.setColor(mTextColor);
        int textY = (int) -((mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX, textY, mPaint);

        //绘制unreachbar
        if (!noNeedUnReach) {
            float unreachX = progressX + mTextOffSet / 2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(Math.min(unreachX, mRealWidth), 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();
    }

    /**
     * 测量控件的高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measuerHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            //获取字体的高度
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            //控件的高度是上下边距加上（mReachHeight，mUnReachHeight，textHeight）三者中的最大高度
            result = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));

            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }

        return result;
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
