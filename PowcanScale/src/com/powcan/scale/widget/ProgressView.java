package com.powcan.scale.widget;

import com.powcan.scale.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author naiyu(http://snailws.com)
 * @version 1.0
 */
public class ProgressView extends View {

	// 画实心圆的画笔
	private Paint mCirclePaint;
	// 画圆环的画笔
	private Paint mRingPaint;
	// 画字体的画笔
	private Paint mTextPaint;
	// 圆形颜色
	private int mCircleColor;
	// 圆环颜色
	private int mRingColor;
	// 半径
	private float mRadius;
	// 圆环半径
	private float mRingRadius;
	// 圆环宽度
	private float mStrokeWidth;
	// 圆心x坐标
	private int mXCenter;
	// 圆心y坐标
	private int mYCenter;
	// 字的长度
	private float mTxtWidth;
	// 字的高度
	private float mTxtHeight;
	// 总进度
	private float mTotalProgress;
	// 当前进度
	private float mProgress;
	
	// 中间的文字
	private String content;

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ProgressView, 0, 0);
		mRadius = typeArray.getDimension(R.styleable.ProgressView_radius, 80);
		mStrokeWidth = typeArray.getDimension(R.styleable.ProgressView_strokeWidth, 10);
		mCircleColor = typeArray.getColor(R.styleable.ProgressView_circleColor, 0xFFFFFFFF);
		mRingColor = typeArray.getColor(R.styleable.ProgressView_ringColor, 0xFFFFFFFF);
		
		mRingRadius = mRadius + mStrokeWidth / 2;
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Paint.Style.FILL);
		
		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setARGB(255, 255, 255, 255);
		mTextPaint.setTextSize(mRadius / 2);
		
		FontMetrics fm = mTextPaint.getFontMetrics();
		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
		
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;
		
		canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
		
		if (mProgress > 0 ) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
			canvas.drawArc(oval, 90, ( mProgress / mTotalProgress ) * 360, false, mRingPaint); 
		}
		mTxtWidth = mTextPaint.measureText( content, 0, content.length() );
		canvas.drawText( content, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
	}
	
	public void setTotalProgress( float totalProgress )
	{
		mTotalProgress = totalProgress;
	}
	
	public void setData( String content, float progress ) 
	{
		this.content = content;
		mProgress = progress;
		postInvalidate();
	}
}
