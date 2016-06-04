package cn.com.cmy.www.waterdotviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class MagicCircle extends View {

    private Path mPath;
    private Paint mFillCirclePaint,movePaint;

    /**
     * View的宽度
     **/
    private int width;
    /**
     * View的高度，这里View应该是正方形，所以宽高是一样的
     **/
    private int height;
    /**
     * View的中心坐标x
     **/
    private int centerX;
    /**
     * View的中心坐标y
     **/
    private int centerY;

    private float moveLength;
    private float startLength;
    private float mInterpolatedTime;
    private float stretchDistance;
    private float moveDistance;
    private float cDistance;
    private float radius;
    private float c;
    private float blackMagic = 0.551915024494f;
    private VPoint p2, p4;
    private HPoint p1, p3;

    private boolean isArrowRight;
    private float p1XMove;

    public MagicCircle(Context context) {
        this(context, null, 0);
    }

    public MagicCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFillCirclePaint = new Paint();
        mFillCirclePaint.setColor(Color.GRAY);
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setStrokeWidth(1);
        mFillCirclePaint.setAntiAlias(true);
        movePaint = new Paint();
        movePaint.setColor(0xFFfe626d);
        movePaint.setStyle(Paint.Style.FILL);
        movePaint.setStrokeWidth(1);
        movePaint.setAntiAlias(true);
        mPath = new Path();
        p2 = new VPoint();
        p4 = new VPoint();

        p1 = new HPoint();
        p3 = new HPoint();

        initRadius(30);
    }

    private void initRadius(int rad) {
        radius = rad;
        c = radius * blackMagic;
        stretchDistance = radius;
        moveDistance = radius * (3 / 5f);
        cDistance = c * 0.45f;
        moveLength = 4 * radius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth=0,mHeight=0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            Log.e("xxx", "EXACTLY");
            mWidth = specSize;
        } else
        {
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                int desire =  getPaddingLeft() + getPaddingRight() + (2*4+2+2)*(int)radius;
                mWidth = desire;
                Log.e("xxx", "AT_MOST");
            }
        }

        /***
         * 设置高度
         */

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            mHeight = specSize;
        } else
        {
            int desire = getPaddingTop() + getPaddingBottom() + 2*(int)radius;
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                mHeight = Math.min(desire, specSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        canvas.translate(radius, radius);

        canvas.save();
        model0(radius);

        mPath.moveTo(p1.x, p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);

        model0(4 * radius+radius);

        mPath.moveTo(p1.x, p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);

        model0(8 * radius+radius);

        mPath.moveTo(p1.x, p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
        canvas.drawPath(mPath, mFillCirclePaint);
        canvas.restore();
        canvas.save();
        mPath.reset();
        if (mInterpolatedTime >= 0 && mInterpolatedTime <= 0.2) {
            model1(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.2 && mInterpolatedTime <= 0.5) {
            model2(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.5 && mInterpolatedTime <= 0.8) {
            model3(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.8 && mInterpolatedTime <= 0.9) {
            model4(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.9 && mInterpolatedTime <= 1) {
            model5(mInterpolatedTime);
        }

        float offset = moveLength * (mInterpolatedTime - 0.2f);
        if (mInterpolatedTime - 0.2f < 0) {
            offset = 0;
        }
        Log.v("leng", offset + "");
        offset = offset + startLength;
        p1.adjustAllX(offset);
        p2.adjustAllX(offset);
        p3.adjustAllX(offset);
        p4.adjustAllX(offset);


        mPath.moveTo(p1.x, p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
        canvas.drawPath(mPath, movePaint);
        canvas.restore();
    }

    private void model0() {
        p1.setY(radius);
        p3.setY(-radius);
        p3.x = p1.x = 0;
        p3.left.x = p1.left.x = -c;
        p3.right.x = p1.right.x = c;

        p2.setX(radius);
        p4.setX(-radius);
        p2.y = p4.y = 0;
        p2.top.y = p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = c;
    }

    private void model0(float x) {
        p1.setY(radius);
        p3.setY(-radius);
        p3.x = p1.x = x;
        p3.left.x = p1.left.x = x - c;
        p3.right.x = p1.right.x = x + c;

        p2.setX(x + radius);
        p4.setX(x + -radius);
        p2.y = p4.y = 0;
        p2.top.y = p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = +c;
    }

    private void model1(float time) {//0~0.2
        model0(radius);
        if (isArrowRight) {
            p2.setX(p2.x + stretchDistance * time * 5);
        } else {
            p4.setX(p4.x + stretchDistance * time * 5);
        }
    }

    private void model2(float time) {//0.2~0.5
        model1(0.2f);
        time = (time - 0.2f) * (10f / 3);
        p1.adjustAllX(stretchDistance / 2 * time);
        p1XMove = stretchDistance / 2 * time;
        Log.e("move" , p1XMove+"");
        p3.adjustAllX(stretchDistance / 2 * time);
        p2.adjustY(cDistance * time);
        p4.adjustY(cDistance * time);
    }

    private void model3(float time) {//0.5~0.8
        model2(0.5f);
        time = (time - 0.5f) * (10f / 3);
        p1.adjustAllX(stretchDistance / 2 * time);
        p1XMove = stretchDistance / 2 * time;
        Log.e("move" , p1XMove+"");
        p3.adjustAllX(stretchDistance / 2 * time);
        p2.adjustY(-cDistance * time);
        p4.adjustY(-cDistance * time);

        if(isArrowRight){
            p4.adjustAllX(stretchDistance / 2 * time);
        }else{
            p2.adjustAllX(stretchDistance / 2 * time);
        }


    }

    private void model4(float time) {//0.8~0.9
        model3(0.8f);
        time = (time - 0.8f) * 10;
        if(isArrowRight){
            p4.adjustAllX(stretchDistance / 2 * time);
        }else{
            p2.adjustAllX(stretchDistance / 2 * time);
        }
    }

    private void model5(float time) {
        model4(0.9f);
        time = time - 0.9f;
        if(isArrowRight){
            p4.adjustAllX((float) (Math.sin(Math.PI * time * 10f) * (2 / 10f * radius)));
        }else{
            p2.adjustAllX((float) (Math.sin(Math.PI * time * 10f) * (2 / 10f * radius)));
        }
    }

    class VPoint {
        public float x;
        public float y;
        public PointF top = new PointF();
        public PointF bottom = new PointF();

        public void setX(float x) {
            this.x = x;
            top.x = x;
            bottom.x = x;
        }

        public void adjustY(float offset) {
            top.y -= offset;
            bottom.y += offset;
        }

        public void adjustAllX(float offset) {
            this.x += offset;
            top.x += offset;
            bottom.x += offset;
        }
    }

    class HPoint {
        public float x;
        public float y;
        public PointF left = new PointF();
        public PointF right = new PointF();

        public void setY(float y) {
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void adjustAllX(float offset) {
            this.x += offset;
            left.x += offset;
            right.x += offset;
        }
    }

    private class MoveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            //super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }

    private void initPosition(int last, int now) {
        startLength = last * 4 * radius;
        if (now < last) {
            isArrowRight = false;
            stretchDistance = stretchDistance > 0 ? -stretchDistance : stretchDistance;
        } else {
            isArrowRight = true;
            stretchDistance = stretchDistance < 0 ? -stretchDistance : stretchDistance;
        }
        moveLength = ((now - last) * (float)(4 * radius)-stretchDistance)/(float)0.8;


    }

    public void startAnimation(int lastPosition, int nowPosition) {
        initPosition(lastPosition, nowPosition);
        mPath.reset();
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(1000);
        move.setInterpolator(new AccelerateDecelerateInterpolator());
        startAnimation(move);
    }

    public void setRadius(int radius){
        initRadius(radius);
    }

    public void SetBackgroundCircleColor(@ColorInt int color){
        mFillCirclePaint.setColor(color);
    }
    public void setMoveCircleColor(@ColorInt int color){
        movePaint.setColor(color);
    }
}
