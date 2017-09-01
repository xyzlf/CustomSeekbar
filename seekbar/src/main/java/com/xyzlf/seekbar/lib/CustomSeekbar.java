package com.xyzlf.seekbar.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by zhanglifeng on 2017/8/10.
 * Pop tip seekbar
 */
public class CustomSeekbar extends SeekBar {

    private int mWidth;
    private int mHeight;

    private int redColor;
    private int grayColor;
    private int whiteColor;

    /**
     * 利率.9图绘制相关
     **/
    private NinePatch mNinePatch;//画9图的
    private Bitmap mBitmap;//bitmap，引入9图
    private int mBitmapMinWidth;
    private int mBitmapHeight;
    private Rect mBitmapRect; // 目标区域
    private int mSpace;
    private int mThumWidth;
    private Paint mBitmapPaint;

    /**
     * padding设置
     **/
    private int mLeftPadding, mTopPadding, mRightPadding, mBottomPadding;

    /**
     * 绘制虚线
     **/
    private Paint mLinePaint;

    /**
     * 文本绘制相关
     **/
    private Paint mTextPanit;
    private Rect mTextRect;

    private String transferMoney;
    private String minTransferMoney;
    private String maxTransferMoney;
    private String minProfit;
    private String maxProfit;

    private String floatProfit; //浮动利率

    private float density;

    public CustomSeekbar(Context context) {
        this(context, null);
    }

    public CustomSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;

        /** 初始化.9图片 **/
        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.seekbar_progress_tip);
        mNinePatch = new NinePatch(mBitmap, mBitmap.getNinePatchChunk(), null);
        mBitmapMinWidth = (int) (density * 45);
        mBitmapHeight = (int) (density * 25);
        mBitmapRect = new Rect();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mSpace = (int) (density * 6); // 图片与进度条间距
        mThumWidth = (int) (density * 15);

        /** 初始化Padding **/
        mLeftPadding = (int) (15 * density);
        mRightPadding = (int) (15 * density);
        mTopPadding = (int) (95 * density);
        mBottomPadding = (int) (40 * density);

        /** 初始化虚线 **/
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(Color.parseColor("#eeeeee"));

        /** 初始化文案paint **/
        mTextPanit = new Paint();
        mTextPanit.setAntiAlias(true);
        mTextPanit.setStyle(Paint.Style.FILL);
        mTextPanit.setTextAlign(Paint.Align.LEFT);

        mTextRect = new Rect();

        /** 初始化颜色值 **/
        redColor = Color.parseColor("#ed4e39");
        grayColor = Color.parseColor("#999999");
        whiteColor = Color.parseColor("#ffffff");
    }

    public void setLable(String transferMoney, String minTransferMoney, String maxTransferMoney, String minProfit, String maxProfit, String floatProfit) {
        this.transferMoney = transferMoney;
        this.minTransferMoney = minTransferMoney;
        this.maxTransferMoney = maxTransferMoney;
        this.minProfit = minProfit;
        this.maxProfit = maxProfit;
        this.floatProfit = floatProfit;
        postInvalidate();
    }

    public void setTransferMoney(String transferMoney) {
        this.transferMoney = transferMoney;
    }

    public void setFloatProfit(String floatProfit) {
        this.floatProfit = floatProfit;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setPadding(mLeftPadding, mTopPadding, mRightPadding, mBottomPadding);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /** 绘制转让金额 **/
        if (!TextUtils.isEmpty(transferMoney)) {
            mTextPanit.setTextSize(56);
            mTextPanit.setColor(redColor);
            mTextPanit.getTextBounds(transferMoney, 0, transferMoney.length(), mTextRect);
            int leftMargin = mWidth / 2 - mTextRect.width() / 2;
            int topMargin = (int) (density * 20 + mTextRect.height());
            canvas.drawText(transferMoney, leftMargin, topMargin, mTextPanit);

            /** 绘制虚线 **/
            int startX = leftMargin - 20, startY = topMargin + 20, endX = leftMargin + mTextRect.width() + 40, endY = startY;
//          canvas.drawLine(startX, startY, endX, endY, mTextPanit);
            DashPathEffect mLinePathEffect = new DashPathEffect(new float[]{10, 6}, 0);
            mLinePaint.setPathEffect(mLinePathEffect);
            Path mLinePath = new Path();
            mLinePath.moveTo(startX, startY);
            mLinePath.lineTo(endX, endY);
            canvas.drawPath(mLinePath, mLinePaint);
        }

        /** 绘制文案 **/
        if (!TextUtils.isEmpty(minProfit)) {
            mTextPanit.setTextSize(36);
            mTextPanit.setColor(grayColor);
            canvas.drawText(minProfit, mLeftPadding, mHeight / 2 + density * 15, mTextPanit);
        }

        if (!TextUtils.isEmpty(maxProfit)) {
            mTextPanit.getTextBounds(maxProfit, 0, maxProfit.length(), mTextRect);
            canvas.drawText(maxProfit, mWidth - mRightPadding - mTextRect.width(), mHeight / 2 + density * 15, mTextPanit);
        }

        if (!TextUtils.isEmpty(minTransferMoney)) {
            canvas.drawText(minTransferMoney, mLeftPadding, mHeight - density * 25, mTextPanit);
        }

        if (!TextUtils.isEmpty(maxTransferMoney)) {
            mTextPanit.getTextBounds(maxTransferMoney, 0, maxTransferMoney.length(), mTextRect);
            canvas.drawText(maxTransferMoney, mWidth - mRightPadding - mTextRect.width(), mHeight - density * 25, mTextPanit);
        }

        /** 绘制浮动利率背景图 **/
        if (!TextUtils.isEmpty(floatProfit)) {
            Rect progressBounds = getProgressDrawable().getBounds(); //进度条的区域
            int positionX = progressBounds.width() * getProgress() / getMax() + mThumWidth / 2 - mBitmapMinWidth / 2 + (int)density * 6;
            int positionY = mTopPadding - mBitmapHeight - mSpace;
            mBitmapRect.set(positionX, positionY, positionX + mBitmapMinWidth, positionY + mBitmapHeight);
            mNinePatch.draw(canvas, mBitmapRect, mBitmapPaint);

            mTextPanit.setTextSize(36);
            mTextPanit.setColor(whiteColor);
            mTextPanit.getTextBounds(floatProfit, 0, floatProfit.length(), mTextRect);
            canvas.drawText(floatProfit, positionX + mBitmapMinWidth / 2 - mTextRect.width() / 2, positionY + mBitmapHeight / 2 + density * 2, mTextPanit);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

}
