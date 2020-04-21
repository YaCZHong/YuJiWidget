package com.czh.yujiwidget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.Nullable;

import com.czh.yujiwidget.R;

import java.util.ArrayList;

public class Line extends View {

    private static final String TAG = "Line";

    private int mWidth;
    private int mHeight;

    private int mCoordinatesLineWidth_default = 4;
    private int mCoordinatesLineColor_default = Color.BLACK;
    private int mCoordinatesTextSize_default = 16;
    private int mCoordinatesTextColor_default = Color.BLACK;
    private int mLineWidth_default = 10;
    private int mLineColor_default = Color.BLACK;
    private int mLineTextSize_default = 16;
    private int mLineTextColor_default = Color.BLACK;
    private int maxCircleRadius_default = 5;
    private int minCircleRadius_default = 3;
    private int maxCircleColor_default = Color.BLACK;
    private int minCircleColor_default = Color.BLACK;
    private int mVerticalLineColor_default = Color.BLACK;
    private int mVerticalLineWidth_default = 2;
    private int mBackgroundColor_default = Color.TRANSPARENT;

    private int mCoordinatesLineWidth;
    private int mCoordinatesLineColor;
    private int mCoordinatesTextSize;
    private int mCoordinatesTextColor;
    private int mLineWidth;
    private int mLineColor;
    private int mLineTextSize;
    private int mLineTextColor;

    private int maxCircleRadius;
    private int minCircleRadius;
    private int maxCircleColor;
    private int minCircleColor;
    private int mVerticalLineColor;
    private int mVerticalLineWidth;
    private int mBackgroundColor;

    private Paint mCoordinatesLinePaint;
    private Paint mCoordinatesTextPaint;
    private Paint mLinePaint;
    private Paint mLineTextPaint;
    private Paint maxCirclePaint;
    private Paint minCirclePaint;
    private Paint mVerticalLinePaint;

    private Path mVerticalLinePath;

    private ArrayList<String> xValues = new ArrayList<>();//横坐标刻度值
    private ArrayList<Float> values = new ArrayList<>();//曲线图各点的值

    private float XScale;
    private float YScale;

    private float XSection;

    private float YSection;
    private float max_dv;

//    private AnimationSet animationSet;
//    private Animation translate;
//    private Animation alpha;

    public Line(Context context) {
        this(context, null);
    }

    public Line(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Line(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Line, defStyleAttr, 0);

        int count = array.getIndexCount();

        for (int index = 0; index < count; index++) {

            int attr = array.getIndex(index);

            switch (attr) {
                case R.styleable.Line_coordinatesLineWidth:
                    mCoordinatesLineWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mCoordinatesLineWidth_default, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Line_coordinatesLineColor:
                    mCoordinatesLineColor = array.getColor(attr, mCoordinatesLineColor_default);
                    break;
                case R.styleable.Line_coordinatesTextColor:
                    mCoordinatesTextColor = array.getColor(attr, mCoordinatesTextColor_default);
                    break;
                case R.styleable.Line_coordinatesTextSize:
                    mCoordinatesTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mCoordinatesTextSize_default, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Line_lineColor:
                    mLineColor = array.getColor(attr, mLineColor_default);
                    break;
                case R.styleable.Line_lineWidth:
                    mLineWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLineWidth_default, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Line_lineTextSize:
                    mLineTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mLineTextSize_default, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Line_lineTextColor:
                    mLineTextColor = array.getColor(attr, mLineTextColor_default);
                    break;
                case R.styleable.Line_maxCircleRadius:
                    maxCircleRadius = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, maxCircleRadius_default, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Line_maxCircleColor:
                    maxCircleColor = array.getColor(attr, maxCircleColor_default);
                    break;
                case R.styleable.Line_minCircleRadius:
                    minCircleRadius = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minCircleRadius_default, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Line_minCircleColor:
                    minCircleColor = array.getColor(attr, minCircleColor_default);
                    break;
                case R.styleable.Line_backgroundColor:
                    mBackgroundColor = array.getColor(attr, mBackgroundColor_default);
                    break;
                case R.styleable.Line_verticalLineWidth:
                    mVerticalLineWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mVerticalLineWidth_default, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.Line_verticalLineColor:
                    mVerticalLineColor = array.getColor(attr, mVerticalLineColor_default);
                    break;
                default:
                    break;
            }
        }
        array.recycle();
        init();
    }

    private void init() {

        xValues.add("01");
        xValues.add("02");
        xValues.add("03");
        xValues.add("04");
        xValues.add("05");
        xValues.add("06");
        xValues.add("07");
        xValues.add("08");

        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);
        values.add(0f);

        //setLayerType(LAYER_TYPE_SOFTWARE, null); //如果需要阴影，则需要关闭硬件加速

        mCoordinatesLinePaint = new Paint();
        mCoordinatesLinePaint.setAntiAlias(true);
        mCoordinatesLinePaint.setColor(mCoordinatesLineColor);
        mCoordinatesLinePaint.setStrokeWidth(mCoordinatesLineWidth);
        mCoordinatesLinePaint.setStyle(Paint.Style.STROKE);

        mCoordinatesTextPaint = new Paint();
        mCoordinatesTextPaint.setAntiAlias(true);
        mCoordinatesTextPaint.setColor(mCoordinatesTextColor);
        mCoordinatesTextPaint.setTextSize(mCoordinatesTextSize);
        mCoordinatesTextPaint.setTextAlign(Paint.Align.CENTER);// 设置文本的对齐方式
        mCoordinatesTextPaint.setFakeBoldText(true);
        mCoordinatesTextPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
//        mLinePaint.setShadowLayer(dp2px(3), dp2px(0), dp2px(2), Color.parseColor("#88000000"));

        mLineTextPaint = new Paint();
        mLineTextPaint.setAntiAlias(true);
        mLineTextPaint.setColor(mLineTextColor);
        mLineTextPaint.setTextSize(mLineTextSize);
        mLineTextPaint.setTextAlign(Paint.Align.CENTER);// 设置文本的对齐方式
        mLineTextPaint.setFakeBoldText(true);// 设置粗字体
        mLineTextPaint.setStyle(Paint.Style.FILL);
//        mLineTextPaint.setShadowLayer(dp2px(3), dp2px(0), dp2px(2), Color.parseColor("#44000000"));

        minCirclePaint = new Paint();
        minCirclePaint.setAntiAlias(true);
        minCirclePaint.setColor(minCircleColor);
        minCirclePaint.setStyle(Paint.Style.FILL);

        maxCirclePaint = new Paint();
        maxCirclePaint.setAntiAlias(true);
        maxCirclePaint.setColor(maxCircleColor);
        maxCirclePaint.setStyle(Paint.Style.FILL);

        mVerticalLinePaint = new Paint();
        mVerticalLinePaint.setAntiAlias(true);
        mVerticalLinePaint.setColor(mVerticalLineColor);
        mVerticalLinePaint.setStrokeWidth(mVerticalLineWidth);
        mVerticalLinePaint.setStyle(Paint.Style.STROKE);
        mVerticalLinePaint.setPathEffect(new DashPathEffect(new float[]{16, 8}, 1));

        mVerticalLinePath = new Path(); // 垂线是间断性，需要用drawPath

//        // 组合动画
//        animationSet = new AnimationSet(false);
//
//        // 平移动画
//        translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0,
//                TranslateAnimation.RELATIVE_TO_SELF, 0,
//                TranslateAnimation.RELATIVE_TO_SELF, 0.05f
//                , TranslateAnimation.RELATIVE_TO_SELF, 0);
//        translate.setDuration(600);
//
//        // 透明度动画
//        alpha = new AlphaAnimation(0, 1);
//        alpha.setDuration(600);
//
//        animationSet.addAnimation(translate);
//        animationSet.addAnimation(alpha);
//        animationSet.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.EXACTLY) {
            mWidth = widthSpecSize;
        } else {
            mWidth = (int) dp2px(360f);
        }

        if (heightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = heightSpecSize;
        } else {
            mHeight = (int) dp2px(240f);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        prepare();
        canvas.drawColor(mBackgroundColor);
        drawCoordinates(canvas);
        drawCoordinatesXValues(canvas);
        drawLine(canvas);
    }

    private void prepare() {

        // 折线图X轴的总区间，减去48是为了给首尾垂线的左右两边留点空白
        XSection = (mWidth - getPaddingEnd() - getPaddingStart() - dp2px(48));

        // X轴单位区间的大小
        XScale = XSection / (xValues.size() - 1);

        // 折线图Y轴的区间，只取3/6，留下1/6给X轴的刻度值,1/6给最顶端y值空间显示
        YSection = (mHeight - getPaddingBottom() - getPaddingTop()) / 6 * 3;

        // 待描绘的点的最大差值
        max_dv = getMax(values) - getMin(values);

        // 简陋的自适应调整
        if (max_dv >= 5) {
            // 这里将折线图的区间进行 max_dv 等分，YScale表示max_dv等分后每一个小区间的大小
            YScale = YSection / max_dv;
        } else {
            // 把折线空间5等分，坐标值差为1对应一个YScale的高度，坐标值差为 K 对应一个 k * YScale 的高度这样，就能控制坐标点都在坐标系内
            YScale = YSection / 5;
        }
    }

    /**
     * 绘制坐标轴
     *
     * @param canvas
     */
    private void drawCoordinates(Canvas canvas) {
        canvas.drawLine(getPaddingStart(),
                getPaddingTop() + YSection + YSection / 3 * 2,
                mWidth - getPaddingEnd(),
                getPaddingTop() + YSection + YSection / 3 * 2,
                mCoordinatesLinePaint);
    }

    /**
     * 绘制X轴上的刻度
     *
     * @param canvas
     */
    private void drawCoordinatesXValues(Canvas canvas) {
        for (int i = 0; i < xValues.size(); i++) {
            canvas.drawText(xValues.get(i),
                    getPaddingStart() + dp2px(48) / 2 + (i * XScale),
                    getPaddingTop() + YSection + YSection / 3 * 2 + dp2px(20), // getPaddingTop() + YSection + YSection / 3 * 2 :横坐标轴位置，+ dp2px(16) :下移让刻度与横坐标轴有间隔，提升美观度
                    mCoordinatesTextPaint);
        }
    }

    /**
     * 绘制折线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {

        float valueScale;
        float nextValueScale;

        for (int i = 0; i < values.size(); i++) {

            // 获取到该坐标点的缩放倍数
            valueScale = (values.get(i) - getMin(values));

            // 绘制折线
            if (i < values.size() - 1) { // 如果不是最后一个坐标点

                // 获取下一个坐标点的缩放倍数
                nextValueScale = values.get(i + 1) - getMin(values);

                // 绘制线条
                canvas.drawLine(getPaddingStart() + dp2px(48) / 2 + (XScale * i),
                        getPaddingTop() + YSection + YSection / 3 - (YScale * valueScale),
                        getPaddingStart() + dp2px(48) / 2 + (XScale * (i + 1)),
                        getPaddingTop() + YSection + YSection / 3 - (YScale * nextValueScale),
                        mLinePaint);
            }

            // 绘制坐标点标签
            canvas.drawText(String.valueOf(values.get(i)).replace(".0", "°"),
                    getPaddingStart() + dp2px(48) / 2 + (XScale * i),
                    getPaddingTop() + YSection + YSection / 3 - (YScale * valueScale) - dp2px(12),
                    mLineTextPaint);

            // 绘制垂直线
            mVerticalLinePath.reset();
            mVerticalLinePath.moveTo(getPaddingStart() + dp2px(48) / 2 + XScale * i, getPaddingTop() + YSection + YSection / 3 * 2);
            mVerticalLinePath.lineTo(getPaddingStart() + dp2px(48) / 2 + XScale * i, getPaddingTop() + YSection + YSection / 3 * 2 - (YScale * valueScale) - YSection / 3);
            canvas.drawPath(mVerticalLinePath, mVerticalLinePaint);

            // 绘制两个小圆点
            canvas.drawCircle(getPaddingStart() + dp2px(48) / 2 + (XScale * i),
                    getPaddingTop() + YSection + YSection / 3 - (YScale * valueScale), maxCircleRadius,
                    maxCirclePaint);
            canvas.drawCircle(getPaddingStart() + dp2px(48) / 2 + (XScale * i),
                    getPaddingTop() + YSection + YSection / 3 - (YScale * valueScale), minCircleRadius,
                    minCirclePaint);
        }
    }

    private float getMax(ArrayList<Float> data) {
        float max = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            if (max < data.get(i)) {
                max = data.get(i);
            }
        }
        return max;
    }

    private float getMin(ArrayList<Float> data) {
        float min = data.get(0);
        for (int i = 1; i < data.size(); i++) {
            if (min > data.get(i)) {
                min = data.get(i);
            }
        }
        return min;
    }

    public void setxValues(ArrayList<String> xValues) {
        this.xValues = xValues;
    }

    public void setValues(ArrayList<Float> values) {
        this.values = values;
    }

    public void setChange() {
//        this.startAnimation(animationSet);
        this.invalidate();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    private float dp2px(float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
