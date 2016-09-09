package edu.feicui.aide.soft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.aide.R;
import edu.feicui.aide.main.BaseActivity;
import edu.feicui.aide.util.SpaceSizeUtil;
import edu.feicui.aide.view.PiechartView;

/**
 * SoftManagerʵ�ֵڶ�����ť����
 * 
 */
public class SoftActivity extends BaseActivity implements OnClickListener {
	/**
	 * ���������ڲ��ռ䣩
	 */
	ProgressBar mPgbInternal;
	/**
	 * �ڲ��洢�ı�
	 */
	TextView mTxtInternal;
	/**
	 * ���������ⲿ�ռ䣩
	 */
	ProgressBar mPgbStorage;
	/**
	 * �ⲿ�洢�ı�
	 */
	TextView mTxtStorage;
	/**
	 * �������
	 */
	TextView mTxtAllSoft;
	/**
	 * ϵͳ���
	 */
	TextView mTxtSystemSoft;
	/**
	 * �û����
	 */
	TextView mTxtUser;

	/**
	 * ��ͼIntent
	 */
	private Intent mIntent;
	/**
	 * ��ȡROM��SDcard��С�Ķ���
	 */
	SpaceSizeUtil mSpaceSizeUtil;
	/**
	 * �Զ���View Բ�α�ͼ
	 */
	PiechartView mPiechar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_soft);
	}

	private void init() {
		mIntent = new Intent(this, SecondSoftActivity.class);
		mSpaceSizeUtil = SpaceSizeUtil.getInstace(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mPiechar = (PiechartView) findViewById(R.id.soft_circle_view);
		// �ֻ�ROM��SDcard�ܴ�С
		long total = mSpaceSizeUtil.getRomTotalSize()
				+ mSpaceSizeUtil.getSDTotalSize();
		// ROM�����ô�С
		long romConsume = mSpaceSizeUtil.getRomTotalSize()
				- mSpaceSizeUtil.getRomAvailableSize();
		// SDcard�����ô�С
		long sdConsume = mSpaceSizeUtil.getSDTotalSize()
				- mSpaceSizeUtil.getSDAvailableSize();
		float phoneRatio = (float) romConsume / total;
		float SDRatio = (float) sdConsume / total;
		mPiechar.drawPiechart(phoneRatio, SDRatio);

		// �ֻ������ڴ������
		mPgbInternal = (ProgressBar) findViewById(R.id.pgb_soft_internal);
		// ���������ڴ��������ʼֵ
		mPgbInternal.setProgress(mSpaceSizeUtil.getInternalInt());
		// �ֻ�SDcard������
		mPgbStorage = (ProgressBar) findViewById(R.id.pgb_soft_storage);
		// ��������SDcard��������ʼֵ
		mPgbStorage.setProgress(mSpaceSizeUtil.getStorageInt());
		// ����ROM�ı�����
		mTxtInternal = (TextView) findViewById(R.id.txt_internal_mem);
		// ����ͬ�ڴ�ֵת��ΪString����

		String strInternal = String.format("%1$S/%2$S",
				mSpaceSizeUtil.getRomConsumeSizeString(),
				mSpaceSizeUtil.getRomTotalSizeString());
		mTxtInternal.setText(strInternal);
		// ����SDcrd�ı�����
		mTxtStorage = (TextView) findViewById(R.id.txt_storage_mem);

		String strStorage = String.format("%1$S/%2$S",
				mSpaceSizeUtil.getSDConsumeSizeString(),
				mSpaceSizeUtil.getSDTotalSizeString());
		mTxtStorage.setText(strStorage);
		// �����������¼�
		mTxtAllSoft = (TextView) findViewById(R.id.txt_soft_all);
		mTxtAllSoft.setOnClickListener(this);
		// ϵͳ�������¼�
		mTxtSystemSoft = (TextView) findViewById(R.id.txt_soft_system);
		mTxtSystemSoft.setOnClickListener(this);
		mTxtUser = (TextView) findViewById(R.id.txt_soft_user);
		mTxtUser.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		String value = null;
		switch (id) {
		case R.id.txt_soft_all:// ���������ť
			value = this.getResources().getString(R.string.txt_soft_all);
			mIntent.putExtra("title", value);
			mIntent.putExtra("key", 0);
			break;
		case R.id.txt_soft_system:// ϵͳ�����ť
			value = this.getResources().getString(R.string.txt_soft_system);
			mIntent.putExtra("title", value);
			mIntent.putExtra("key", 1);
			break;
		case R.id.txt_soft_user:// �û����
			value = this.getResources().getString(R.string.txt_soft_user);
			mIntent.putExtra("title", value);
			mIntent.putExtra("key", 2);
			break;
		}
		startActivity(mIntent);
	}
}
