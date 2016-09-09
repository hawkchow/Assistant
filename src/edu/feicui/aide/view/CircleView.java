package edu.feicui.aide.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {
	/**
	 * �������ڵľ���
	 */
	RectF mRectF;
	/**
	 * ���εĻ���
	 */
	Paint mPaint;
	/**
	 * ����ɨ���ĽǶ�
	 */
	private float mSweepAngle;
	/**
	 * ���ƵĿ���
	 */
	private boolean mIsRunning;
	/**
	 * ���ƻ��ε�״̬
	 */
	private int mState;

	public CircleView(Context context) {
		this(context, null);
	}

	public CircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		mRectF = new RectF(0, 0, width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawArc(mRectF, -90f, mSweepAngle, true, mPaint);
	}

	/**
	 * ���ƶ�̬�仯�Ļ���
	 * 
	 * @param sweepAngle
	 *            �������Ϊ��ʼ�ĽǶ�
	 */
	public void drawCircle(final float sweepAngle) {
		if (mIsRunning) {
			return;
		}
		mSweepAngle = sweepAngle;
		// �����У��ı俪��mIsRunning
		mIsRunning = true;
		mState = 0;
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				switch (mState) {
				case 0:// ����״̬
					mSweepAngle -= 10;
					postInvalidate();// ֪ͨˢ��
					if (mSweepAngle <= 0) {
						mSweepAngle = 0;
						// ��״̬Ϊǰ��״̬
						mState = 1;
					}
					break;
				case 1:// ǰ��״̬
					mSweepAngle += 10;
					postInvalidate();// ֪ͨˢ��
					if (mSweepAngle >= sweepAngle) {
						// �˳�����
						mSweepAngle = sweepAngle;
						timer.cancel();
						mIsRunning = false;
					}
					break;
				}
			}

		};
		// �������
		timer.schedule(task, 30, 30);
	}
}
