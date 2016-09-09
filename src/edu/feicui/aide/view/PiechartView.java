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

/**
 * �������Բ�α�ͼ
 *
 */
public class PiechartView extends View {
	/**
	 * ����
	 */
	Paint mPaint;
	/**
	 * ����(����Բ����Ĳ���)
	 */
	RectF mRectF;
	/**
	 * �ֻ����ÿռ�ռ�ȵĽǶȴ�С
	 */
	float mPhoneAngle;
	/**
	 * �ⲿ�洢�ռ�ռ�ȵĽǶȴ�С
	 */
	float mSDAngle;

	public PiechartView(Context context) {
		this(context, null);
	}

	public PiechartView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PiechartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint = new Paint();
		// ���ÿ����
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		mRectF = new RectF(0, 0, width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ���ƻ�ɫ����ͼ
		mPaint.setColor(Color.YELLOW);
		canvas.drawArc(mRectF, -90f, 360f, true, mPaint);
		// ������ɫ�ֻ����ÿռ�
		mPaint.setColor(Color.BLUE);
		canvas.drawArc(mRectF, -90f, mPhoneAngle, true, mPaint);
		// ������ɫ���ô洢�ռ�
		mPaint.setColor(Color.GREEN);
		canvas.drawArc(mRectF, -90f + mPhoneAngle, mSDAngle, true, mPaint);
	}

	/**
	 * ���ƶ�̬����ͼ
	 * 
	 * @param phoneRatio
	 *            �ֻ����ÿռ�ռ��
	 * @param SDRatio
	 *            ���ô洢�ռ�ռ��
	 */
	public void drawPiechart(float phoneRatio, float SDRatio) {
		final float phoneAngle = phoneRatio * 360;
		final float SDAngle = SDRatio * 360;
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				mPhoneAngle += 10;
				mSDAngle += 10;
				// ֪ͨˢ��
				postInvalidate();
				if (mPhoneAngle >= phoneAngle) {
					mPhoneAngle = phoneAngle;
				}
				if (mSDAngle >= SDAngle) {
					mSDAngle = SDAngle;
				}
				// �˳�����
				if (mPhoneAngle >= phoneAngle && mSDAngle >= SDAngle) {
					timer.cancel();
				}
			}

		};
		// 50�����ʼ���ƣ�ÿ�λ��Ƽ��50����
		timer.schedule(task, 50, 50);
	}
}
