package com.zz.temperaturemonitor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zz.temperaturemonitor.utils.DisplayUtil;

public class TempCircleView extends View {

    private Paint linePaint;
    private Paint circlePaint;
    private Paint textPaint;//刻度值
    private Paint centerTextPaint;//中间的温度值
    private Paint indicatorPaint;//指示器
    private RectF mRectF;

    private int defaultValue;
    int mCenter = 0;// 圆的半径
    int mRadius = 0;
    private SweepGradient mSweepGradient,mSweepGradient2;
    private int scanDegree = 0;//最高温度和最低温度扫描角度
    private int currentScanDegree=0;//当前温度扫过的角度
    private boolean isCanMove;
    private GetDegreeInterface mGetDegreeInterface;
    int minDegrees = 0;//最低温度
    int maxDegree=0;//最高温度
    int currentDegree=0;//当前温度

    public boolean flag=false;
    int screenWidth,screenHeight;
    
    private float scaleX, scaleY;
    
    public TempCircleView(Context context) {
        super(context);
        initPaint();
    }

    public TempCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenWidth= DisplayUtil.getScreenWidth(context);
        screenHeight=DisplayUtil.getScreenHeight(context);
        scaleX = screenHeight / 540;
        scaleY = screenHeight / 960;
        Log.e("My----->", "2  "+screenWidth+"  "+screenHeight);
        initPaint();
    }

    private void initPaint() {
        linePaint = new Paint();
        linePaint.setColor(Color.CYAN);
        linePaint.setStyle(Style.FILL);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1.0f);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(30 * scaleY);

        centerTextPaint = new Paint();
        centerTextPaint.setColor(Color.BLUE);
        centerTextPaint.setTextAlign(Paint.Align.CENTER);
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setTextSize(50 * scaleY);

        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeCap(Cap.ROUND);//实现末端圆弧
        circlePaint.setStrokeWidth(60.0f * scaleY);

        // 着色的共有270度，这里设置了12个颜色均分360度s
        int[] colors = { 0xFFD52B2B, 0xFFf70101, 0xFFFFFFFF, 0xFFFFFFFF,
                0xFF6AE2FD, 0xFF8CD0E5, 0xFFA3CBCB, 0xFFD1C299, 0xFFE5BD7D,
                0xFFAA5555, 0xFFBB4444, 0xFFC43C3C };

        mCenter = (int) ((screenWidth - 100) / 2);
        mRadius = (int) ((screenWidth - 300) / 2 / scaleX * scaleY) ;
        // 渐变色
        mSweepGradient = new SweepGradient(mCenter, mCenter, colors, null);
        // 构建圆的外切矩形
        mRectF = new RectF(mCenter - mRadius + 25, mCenter - mRadius - 30, mCenter
                + mRadius + 25, mCenter + mRadius - 30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO 自动生成的方法存根
        super.onDraw(canvas);
        circlePaint.setShader(null);

        canvas.drawArc(mRectF, 135, 270, false, circlePaint);

        Log.e("scan--1-->", scanDegree+"");
        //重新赋值了，所以手指滑动没有显示
        scanDegree=(getMaxDegree()-getMinDegree())*3;
        // 设置画笔渐变色
        circlePaint.setShader(mSweepGradient);
        canvas.drawArc(mRectF, 135+(getMinDegree()-10)*9, (float) scanDegree, false, circlePaint);

        currentScanDegree=(getCurrentDegree()-10)*3;
        canvas.drawText((currentScanDegree/9+10)+"℃", mCenter + 25, mCenter - 20, centerTextPaint);

        // x代表文字的x轴距离圆心x轴的距离 因为刚好是45度，所以文字x轴值和y值相等
        int x = 0;
        // 三角形的斜边
        int c = mRadius + 60 / 2 + 20;// 20代表这个字距离圆外边的距离
        // 因为是每45度写一次文字，故根据到圆心的位置，利用三角形的公式，可以算出文字的坐标值
        x = (int) Math.sqrt((c * c / 2));

        //		if (getMinDegree() == 0) {
        //			minDegrees = 10;
        //		}
        canvas.drawText("10", mCenter - x + 25 * scaleY, mCenter + x - 20 * scaleY, textPaint);
        canvas.drawText("15", mCenter - c + 25 * scaleY, mCenter - 20 * scaleY,
                textPaint);
        canvas.drawText("20", mCenter - x + 25 * scaleY, mCenter - x - 20 * scaleY,
                textPaint);
        canvas.drawText("25", mCenter + 25 * scaleY, mCenter - c - 20 * scaleY,
                textPaint);
        canvas.drawText( "30", mCenter + x + 25 * scaleY, mCenter - x - 20 * scaleY,
                textPaint);
        canvas.drawText( "35", mCenter + c + 25 * scaleY, mCenter - 20 * scaleY,
                textPaint);
        canvas.drawText( "40", mCenter + x + 25 * scaleY, mCenter + x - 25 * scaleY,
                textPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO 自动生成的方法存根
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isCanMove = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float StartX = event.getX();
                float StartY = event.getY();
                // 判断当前手指距离圆心的距离 如果大于mCenter代表在圆心的右侧
                if (x > mCenter) {
                    x = x - mCenter;
                } else {
                    x = mCenter - x;
                }
                if (y > mCenter) {
                    y = y - mCenter;
                } else {
                    y = mCenter - y;
                }
                // 判断当前手指是否在圆环上的（30+10多加了10个像素）
                if ((mRadius + 40) < Math.sqrt(x * x + y * y)
                        || Math.sqrt(x * x + y * y) < (mRadius - 40)) {
                    Log.e("cmos---->", "终止滑动");
                    isCanMove = false;
                    return false;
                }
                float cosValue = x / (float) Math.sqrt(x * x + y * y);
                // 根据cosValue求角度值
                double acos = Math.acos(cosValue);// 弧度值
                acos = Math.toDegrees(acos);// 角度值

                if (StartX > mCenter && StartY < mCenter) {
                    acos = 360 - acos;// 第一象限
                    Log.e("象限---->", "第一象限");
                } else if (StartX < mCenter && StartY < mCenter) {
                    acos = 180 + acos;// 第二象限
                    Log.e("象限---->", "第二象限");
                } else if (StartX < mCenter && StartY > mCenter) {
                    acos = 180 - acos;// 第三象限
                    Log.e("象限---->", "第三象限");
                } else {
                    // acos=acos;
                    Log.e("象限---->", "第四象限");
                }
                Log.e("旋转的角度---->", acos + "");
                scanDegree = (int) acos;
                if (scanDegree >= 135 && scanDegree < 360) {
                    scanDegree = scanDegree - 135;
                    int actualDegree = (int) (scanDegree / 9);
                    if (mGetDegreeInterface != null) {
                        mGetDegreeInterface.getActualDegree(actualDegree
                                + minDegrees);
                    }
                } else if (scanDegree <= 45) {
                    scanDegree = (int) (180 + 45 + acos);
                    int actualDegree = (int) (scanDegree / 9);
                    if (mGetDegreeInterface != null) {
                        mGetDegreeInterface.getActualDegree(actualDegree
                                + minDegrees);
                    }
                } else {
                    return false;
                }
                postInvalidate();
                return true;
        }
        return true;
    }

    /**
     * 设置最低温度
     *
     * @param degree
     */
    public void setMinDegree(int degree) {
        this.minDegrees = degree;
    }

    public int getMinDegree() {
        return minDegrees;
    }

    /**
     * 获取当前温度值接口
     *
     */
    public interface GetDegreeInterface {
        void getActualDegree(int degree);
    }

    public void setGetDegreeInterface(GetDegreeInterface arg) {
        this.mGetDegreeInterface = arg;
    }

    /**
     * 设置最高温度值
     */
    public void setMaxDegree(int degree){
        this.maxDegree=degree;
    }

    public int getMaxDegree() {
        return maxDegree;
    }

    /**
     * 设置当前温度
     * @param currentDegree
     */
    public void setCurrentDegree(int currentDegree) {
        this.currentDegree = currentDegree;
    }

    public int getCurrentDegree() {
        return currentDegree;
    }

    // 因为自定义的空间的高度设置的是wrap_content,所以我们必须要重写onMeasure方法去测量高度，否则布局界面看不到
    // 其他控件(被覆盖)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        // 默认宽高;
        defaultValue=screenWidth;

        switch (mode) {
            case MeasureSpec.AT_MOST:
                // 最大值模式 当控件的layout_Width或layout_height属性指定为wrap_content时
                Log.e("cmos---->", "size " + size + " screenWidth " + screenWidth);
                size = Math.min(defaultValue, size);
                break;
            case MeasureSpec.EXACTLY:
                // 精确值模式
                // 当控件的android:layout_width=”100dp”或android:layout_height=”match_parent”时

                break;
            default:
                size = defaultValue;
                break;
        }
        defaultValue = size;
        return size;
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {
            case MeasureSpec.AT_MOST:
                // 最大值模式 当控件的layout_Width或layout_height属性指定为wrap_content时
                Log.e("cmos---->", "size " + size + " screenHeight " + screenHeight);
                size = Math.min(screenHeight/2, size);
                break;
            case MeasureSpec.EXACTLY:
                // 精确值模式
                // 当控件的android:layout_width=”100dp”或android:layout_height=”match_parent”时

                break;
            default:
                size = defaultValue;
                break;
        }
        return size;
    }

}
