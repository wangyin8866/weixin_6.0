package com.wy.weinxin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wy on 2016/8/1.
 */
public class ChangeColorWithText extends View {
    private static final String TAG = "ChangeColorWithText";
    private Bitmap mIcon;
    private int mColor;
    private String mText;
    private int mTextSize;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;

    private float mAlpha;

    private Rect mIconRect;
    private Rect mTextBound;
    private Paint mTextPaint;

    public ChangeColorWithText(Context context) {
        this(context, null);
    }

    public ChangeColorWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorWithText);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorWithText_wyman_icon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
                    mIcon = drawable.getBitmap();
                    break;
                case R.styleable.ChangeColorWithText_wyman_color:
                    mColor = a.getColor(attr, 0xFF45C01A);
                    break;
                case R.styleable.ChangeColorWithText_wyman_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.ChangeColorWithText_wyman_text_size:
                    mTextSize = a.getDimensionPixelSize(attr, 12);
                    break;
            }
        }
        a.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);

        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom() - getPaddingTop() - mTextBound.height());
        int left = (getMeasuredWidth() - iconWidth) / 2;
        int top = (getMeasuredHeight() - mTextBound.height() - iconWidth) / 2;
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIcon, null, mIconRect, null);
        int alpha = (int) Math.ceil(255 * mAlpha);
        //内存去准备mBitmap，setAlpha,纯色，xfermode,图标
        setupTargetBitmap(alpha);
        //1.绘制原文本。2.绘制变色的文本
        drawSourceText(canvas,alpha);
        drawTargetText(canvas,alpha);

        //绘制处理过后的mBitmap
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 绘制变色的文本
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        Log.e(TAG, "drawTargetText: "+alpha );
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y = mIconRect.bottom + mTextBound.height()/2;
        canvas.drawText(mText,x,y,mTextPaint);
    }

    /**
     * 绘制原文本
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255-alpha);
        Log.e(TAG, "drawSourceText: "+alpha );
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y = mIconRect.bottom + mTextBound.height()/2;
        canvas.drawText(mText,x,y,mTextPaint);
    }

    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect, mPaint);//绘制纯色
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(mIcon, null, mIconRect, mPaint);//得到处理过的mBitmap
    }
    public  void setIconAlpha(float alpha){
        this.mAlpha = alpha;
        invalidateView();

    }

    /**
     * 重绘（是否是UI线程）
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    /**
     *防止系统回收activity内存（长期至于后台的activity重启），导致高亮tab与显示的fragment不符
     */
    private static  final String INSTANCE_STATUS="instance_status";
    private static  final String STATUS_ALPHA="status_alpha";
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS,super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA,mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof  Bundle) {
            Bundle bundle= (Bundle) state;
            mAlpha = bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
