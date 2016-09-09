package edu.feicui.aide.check;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.aide.R;
import edu.feicui.aide.main.BaseActivity;
import edu.feicui.aide.util.MemoryUtil;

/**
 * �ֻ����
 * 
 */
public class CheckActivity extends BaseActivity {
	/**
	 * ���Ͻǻ��˰�ť
	 */
	ImageView mImgBack;

	/**
	 * ��ʾ��綯���ؼ�
	 */
	ImageView mImgLevel;
	/**
	 * ��ʾ�������ı�
	 */
	TextView mTxtLevel;
	/**
	 * ��ʾ���״̬
	 */
	TextView mTxtStatus;
	/**
	 * �豸����
	 */
	TextView mTxtBrand;
	/**
	 * ϵͳ�汾
	 */
	TextView mTxtVersion;
	/**
	 * ȫ�������ڴ�
	 */
	TextView mTxtTotalRAM;
	/**
	 * �����ڴ�
	 */
	TextView mTxtAvailRAM;
	/**
	 * cpu����
	 */
	TextView mTxtCpuname;
	/**
	 * cpu����
	 */
	TextView mTxtCpuNum;
	/**
	 * �ֻ��ֱ���
	 */
	TextView mTxtPhoneMetrics;
	/**
	 * ����ֱ���
	 */
	TextView mTxtCameraMetrics;
	/**
	 * �����汾
	 */
	TextView mTxtBaseband;
	/**
	 * �Ƿ�root
	 */
	TextView mTxtIsroot;
	/**
	 * CheckInfo�����
	 */
	CheckUtil mInfo;
	/**
	 * ��ع㲥������
	 */
	BatteryReceiver mReceiver;
	/**
	 * ��ȡ�ڴ������
	 */
	MemoryUtil mMem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_check);
	}

	void init() {
		mMem = MemoryUtil.getInstance(this);
		mInfo = CheckUtil.getInstance(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ���õ�ع㲥ע�᷽��
		registerBattery();
	}

	/**
	 * �÷�������ע���ع㲥����̬ע�ᣩ
	 */
	public void registerBattery() {
		mReceiver = new BatteryReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		// ���Ͻǻ��˼�
		mImgBack = (ImageView) findViewById(R.id.img_check_back);
		mImgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mImgLevel = (ImageView) findViewById(R.id.img_check_level);
		mTxtLevel = (TextView) findViewById(R.id.txt_check_level);

		mTxtStatus = (TextView) findViewById(R.id.txt_check_status);

		mTxtBrand = (TextView) findViewById(R.id.txt_check_brand);
		mTxtBrand
				.setText(String.format("�豸���ƣ�%1$S", Build.BRAND.toUpperCase()));

		mTxtVersion = (TextView) findViewById(R.id.txt_check_version);
		mTxtVersion.setText(String.format("ϵͳ�汾��%1$S", Build.VERSION.RELEASE));

		mTxtTotalRAM = (TextView) findViewById(R.id.txt_check_total);
		String total = Formatter.formatFileSize(this, mMem.getTotalMem());
		String totalRAM = String.format("ȫ�������ڴ棺%1$s", total);
		mTxtTotalRAM.setText(totalRAM);

		mTxtAvailRAM = (TextView) findViewById(R.id.txt_check_avail);
		String avail = Formatter.formatFileSize(this, mMem.getAvailMem());
		String availRAM = String.format("ʣ�������ڴ棺%1$s", avail);
		mTxtAvailRAM.setText(availRAM);

		mTxtCpuname = (TextView) findViewById(R.id.txt_check_cpuname);
		String cpuname = String.format("cpu���ƣ�%1$s", mInfo.getCpuName());
		mTxtCpuname.setText(cpuname);

		mTxtCpuNum = (TextView) findViewById(R.id.txt_check_cpunum);
		String cpuNum = String.format("cpu������%1$d", mInfo.getCpuNum());
		mTxtCpuNum.setText(cpuNum);

		mTxtPhoneMetrics = (TextView) findViewById(R.id.txt_check_phone);
		mTxtPhoneMetrics.setText(mInfo.getPhoneMetrics());

		mTxtCameraMetrics = (TextView) findViewById(R.id.txt_check_camera);
		mTxtCameraMetrics.setText(mInfo.getCameraMetrics());

		mTxtBaseband = (TextView) findViewById(R.id.txt_check_baseband);
		String baseband = String
				.format("�����汾��%1$s", mInfo.getBasebandVersion());
		mTxtBaseband.setText(baseband);

		mTxtIsroot = (TextView) findViewById(R.id.txt_check_root);
		String isRoot = String.format("�Ƿ�ROOT��%1$s", mInfo.isRoot());
		mTxtIsroot.setText(isRoot);
	}

	/**
	 * ���ڲ����������յ�ع㲥
	 * 
	 */
	class BatteryReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ���״̬
			String statusKey = BatteryManager.EXTRA_STATUS;
			int value = intent.getIntExtra(statusKey, 0);
			switch (value) {
			case BatteryManager.BATTERY_STATUS_UNKNOWN:// δ֪
				mTxtStatus.setText("δ֪״̬");
				break;
			case BatteryManager.BATTERY_STATUS_CHARGING:// �����
				mTxtStatus.setText("�����");
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:// �ŵ���
				mTxtStatus.setText("�ŵ���");
				break;
			case BatteryManager.BATTERY_STATUS_FULL:// ����
				mTxtStatus.setText("����");
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:// δ���
				mTxtStatus.setText("δ���");
				break;
			}
			// ��ȡ������
			String maxLevel = BatteryManager.EXTRA_SCALE;
			int max = intent.getIntExtra(maxLevel, 0);
			// ��ȡ��ǰ����
			String levelKey = BatteryManager.EXTRA_LEVEL;
			int level = intent.getIntExtra(levelKey, 0);
			int ratio = (int) ((float) level / max * 100);
			String str = String.format("%1$d %%", ratio);
			mTxtLevel.setText(str);
			mImgLevel.setImageLevel(ratio);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �رյ�ع㲥
		unregisterReceiver(mReceiver);
	}
}
