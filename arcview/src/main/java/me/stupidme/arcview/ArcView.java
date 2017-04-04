package me.stupidme.arcview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {

    private static final String TAG = "ArcView";

    private Paint mOuterCirclePaint;

    private Paint mInnerCirclePaint;

    private Paint mArcPaint;

    private Paint mTextPaint;

    private Paint mRectFPaint;

    private float mInnerCircleRadius;

    private int mInnerCircleColor;

    private String mCircleText;

    private int mCircleTextColor;

    private float mCircleTextSize;

    private int mOuterCircleColor;

    private float mOuterCircleRadius;

    private int mArcColor;

    private int mCircleX;

    private int mCircleY;

    private RectF mOuterCircleRectF;

    private RectF mInnerCircleRectF;

    private RectF mArcRectF;

    private float mArcAngleStart;

    private float mArcAngleSweep;


    public ArcView(Context context) {
        super(context);
        init(null, 0);

    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ArcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcView, defStyle, 0);
        mInnerCircleRadius = (int) a.getDimension(R.styleable.ArcView_innerCircleRadius, ArchViewUtil.dip2px(getContext(), 50));
        mInnerCircleColor = a.getColor(R.styleable.ArcView_innerCircleColor, Color.parseColor("#303F9F"));
        mCircleText = a.getString(R.styleable.ArcView_circleText);
        mCircleTextColor = a.getColor(R.styleable.ArcView_circleTextColor, Color.WHITE);
        mCircleTextSize = a.getDimension(R.styleable.ArcView_circleTextSize, ArchViewUtil.sp2px(getContext(), 18));
        mOuterCircleRadius = (int) a.getDimension(R.styleable.ArcView_outerCircleRadius, ArchViewUtil.dip2px(getContext(), 100));
        mOuterCircleColor = a.getColor(R.styleable.ArcView_outerCircleColor, Color.parseColor("#3F51B5"));
        mArcColor = a.getColor(R.styleable.ArcView_arcColor, Color.parseColor("#FF4081"));
        mArcAngleStart = a.getFloat(R.styleable.ArcView_arcAngleStart, -90);
        mArcAngleSweep = a.getFloat(R.styleable.ArcView_arcAngleSweep, 90);
        a.recycle();

        mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterCirclePaint.setColor(mOuterCircleColor);
        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint.setColor(mInnerCircleColor);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(mArcColor);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mCircleTextSize);
        mTextPaint.setColor(mCircleTextColor);

        mOuterCircleRectF = new RectF();
        mInnerCircleRectF = new RectF();
        mArcRectF = new RectF();

        mRectFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectFPaint.setStyle(Paint.Style.STROKE);
        mRectFPaint.setStrokeWidth(5);
        mRectFPaint.setColor(Color.BLACK);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mOuterCircleRadius * 2, (int) mOuterCircleRadius * 2);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) mOuterCircleRadius * 2, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, (int) mOuterCircleRadius * 2);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCircleX = getWidth() / 2;
        mCircleY = getHeight() / 2;

        drawOuterCircle(canvas);
        drawArc(canvas);
        drawCircle(canvas);
        drawText(canvas);
        canvas.restore();
    }

    private void drawOuterCircle(Canvas canvas) {

        mOuterCirclePaint.setColor(mOuterCircleColor);
        canvas.drawCircle(mCircleX, mCircleY, mOuterCircleRadius, mOuterCirclePaint);

        mOuterCircleRectF.left = mCircleX - mOuterCircleRadius;
        mOuterCircleRectF.top = mCircleY - mOuterCircleRadius;
        mOuterCircleRectF.right = mCircleX + mOuterCircleRadius;
        mOuterCircleRectF.bottom = mCircleY + mOuterCircleRadius;

    }

    private void drawCircle(Canvas canvas) {
        mInnerCirclePaint.setColor(mInnerCircleColor);
        canvas.drawCircle(mCircleX, mCircleY, mInnerCircleRadius, mInnerCirclePaint);

        mInnerCircleRectF.left = mCircleX - mInnerCircleRadius;
        mInnerCircleRectF.top = mCircleY - mInnerCircleRadius;
        mInnerCircleRectF.right = mCircleX + mInnerCircleRadius;
        mInnerCircleRectF.bottom = mCircleY + mInnerCircleRadius;

    }

    private void drawArc(final Canvas canvas) {
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mOuterCircleRadius - mInnerCircleRadius);

        mArcRectF.left = (mInnerCircleRectF.left + mOuterCircleRectF.left) / 2;
        mArcRectF.top = (mInnerCircleRectF.top + mOuterCircleRectF.top) / 2;
        mArcRectF.right = (mInnerCircleRectF.right + mOuterCircleRectF.right) / 2;
        mArcRectF.bottom = (mInnerCircleRectF.bottom + mOuterCircleRectF.bottom) / 2;

        canvas.drawArc(mArcRectF.left, mArcRectF.top, mArcRectF.right, mArcRectF.bottom,
                mArcAngleStart, mArcAngleSweep, false, mArcPaint);

    }

    private void drawText(Canvas canvas) {
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(ArchViewUtil.dip2px(getContext(), 18));
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mCircleText, 0, mCircleText.length(), bounds);
        canvas.drawText(mCircleText, (mInnerCircleRectF.centerX() - mTextPaint.measureText(mCircleText) / 2),
                (mInnerCircleRectF.centerY() + bounds.height() / 2), mTextPaint);
    }

    private void drawRectF(Canvas canvas) {
        canvas.drawRect(mArcRectF.left, mArcRectF.top, mArcRectF.right, mArcRectF.bottom, mRectFPaint);
        canvas.drawRect(mInnerCircleRectF.left, mInnerCircleRectF.top, mInnerCircleRectF.right, mInnerCircleRectF.bottom, mRectFPaint);
        canvas.drawRect(mOuterCircleRectF.left, mOuterCircleRectF.top, mOuterCircleRectF.right, mOuterCircleRectF.bottom, mRectFPaint);
    }

    public void setCircleTextSize(float dp) {
        mCircleTextSize = ArchViewUtil.dip2px(getContext(), dp);
    }

    public void setArcAngleStart(float start) {
        mArcAngleStart = start;
        invalidate();
    }

    public void setArcAngleSweep(float sweep) {
        mArcAngleSweep = sweep;
        invalidate();
    }

    public void setCircleText(String text) {
        mCircleText = text;
        invalidate();
    }

    public void setCircleTextColor(int color) {
        mCircleTextColor = color;
    }

    public void setInnerCircleColor(int color) {
        mInnerCircleColor = color;
    }

    public void setInnerCircleRadius(float radius) {
        mInnerCircleRadius = radius;
    }

    public void setOuterCircleColor(int color) {
        mOuterCircleColor = color;
    }

    public void setOuterCircleRadius(float radius) {
        mOuterCircleRadius = radius;
    }

    public void setArcColor(int color) {
        mArcColor = color;
    }
}
