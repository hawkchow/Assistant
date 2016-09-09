package edu.feicui.aide.main;

import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.feicui.aide.R;
import edu.feicui.aide.accelerate.AccelerateActivity;
import edu.feicui.aide.accelerate.ProcessInfo;
import edu.feicui.aide.check.CheckActivity;
import edu.feicui.aide.filemgr.FileMgrActivity;
import edu.feicui.aide.garbage.GarbageClearActivity;
import edu.feicui.aide.soft.SoftActivity;
import edu.feicui.aide.tel.TelMgrActivity;
import edu.feicui.aide.util.MemoryUtil;
import edu.feicui.aide.util.ProcessUtil;
import edu.feicui.aide.view.CircleView;

public class MainActivity extends BaseActivity implements OnClickListener {
	/**
	 * ���ֱ�Сʱ�ı�ʶwhat
	 */
	private final int MESSAGE_DOWN = 1;
	/**
	 * ���ֱ��ʱ�ı�ʶwhat
	 */
	private final int MESSAGE_UP = 2;
	/**
	 * ����ǰ�����ڴ����
	 */
	private float mPrevious;
	/**
	 * ����������ڴ����
	 */
	private float mLater;
	/**
	 * Բ���м���ʾ�����ڴ��С�ı仯ֵ
	 */
	private int mConsumeNum;
	/**
	 * �Զ���CircleView<br/>
	 * ������ɫ����
	 */
	CircleView mCircleView;
	/**
	 * �ɵ����Բ�ΰ�ť
	 */
	ImageButton mIgbCircle;
	/**
	 * ��ʾ�ڴ�������ı���
	 */
	TextView mTxtPercent;
	/**
	 * �ֻ�����������ʾ
	 */
	TextView mTxtState;
	/**
	 * �ֻ�����
	 */
	TextView mTxtAccelerate;
	/**
	 * �������
	 */
	TextView mTxtSoftManage;
	/**
	 * �ֻ����
	 */
	TextView mTxtCheck;
	/**
	 * ͨѶ��ȫ
	 */
	TextView mTxtTel;
	/**
	 * �ļ�����
	 */
	TextView mTxtFileManage;
	/**
	 * ��������
	 */
	TextView mTxtClear;
	/**
	 * ���Ͻǲ˵�
	 */
	ImageButton mIgbMenu;
	/**
	 * ��ȡ�ڴ������
	 */
	MemoryUtil mMemUtil;
	private ProcessUtil mProcessUtil;
	/**
	 * �ı������ݸ��Ŀ��أ���ֹ�������Ӱ��
	 */
	private boolean mFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		float ratio = (float) mMemUtil.getConsumeMem() / mMemUtil.getTotalMem();
		mCircleView.drawCircle(ratio * 360);
		mTxtPercent.setText((int) (ratio * 100) + "");
	}

	private void init() {
		mMemUtil = MemoryUtil.getInstance(this);
		mProcessUtil = ProcessUtil.getInstance(this);
		mPrevious = (float) mMemUtil.getConsumeMem() / mMemUtil.getTotalMem();
		mConsumeNum = (int) (mPrevious * 100);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mCircleView = (CircleView) findViewById(R.id.circle_mid);
		// ����ʼ�ڴ���������Ϊ��ʼֵ
		mCircleView.drawCircle(mPrevious * 360);

		mTxtPercent = (TextView) findViewById(R.id.txt_mid_percent);
		// ���ó�ʼ��ʾֵ
		mTxtPercent.setText("" + (int) (mPrevious * 100));

		mTxtState = (TextView) findViewById(R.id.txt_mid_accelerate);

		mIgbCircle = (ImageButton) findViewById(R.id.igb_mid);
		mIgbCircle.setOnClickListener(this);
		// �ֻ�����
		mTxtAccelerate = (TextView) findViewById(R.id.txt_bottom_accelerate);
		mTxtAccelerate.setOnClickListener(this);
		// �������
		mTxtSoftManage = (TextView) findViewById(R.id.txt_bottom_softmanage);
		mTxtSoftManage.setOnClickListener(this);

		mTxtCheck = (TextView) findViewById(R.id.txt_bottom_check);
		mTxtCheck.setOnClickListener(this);

		mTxtTel = (TextView) findViewById(R.id.txt_bottom_tel);
		mTxtTel.setOnClickListener(this);

		mTxtFileManage = (TextView) findViewById(R.id.txt_bottom_filemanage);
		mTxtFileManage.setOnClickListener(this);

		mTxtClear = (TextView) findViewById(R.id.txt_bottom_clear);
		mTxtClear.setOnClickListener(this);

		mIgbMenu = (ImageButton) findViewById(R.id.imb_top_right);
		mIgbMenu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.igb_mid:// ������ƶ�̬Բ�����ı��ı����ֵ
			clearOneKey();
			// ������ɺ������ڴ����
			mLater = (float) mMemUtil.getConsumeMem() / mMemUtil.getTotalMem();
			mCircleView.drawCircle(mLater * 360);
			// ���ñ仯���ı�������
			mHandler.sendEmptyMessage(MESSAGE_DOWN);
			break;
		case R.id.txt_bottom_accelerate:// �ֻ�����
			gotoActivity(this, AccelerateActivity.class);
			break;
		case R.id.txt_bottom_softmanage:// �������
			gotoActivity(this, SoftActivity.class);
			break;
		case R.id.txt_bottom_check:// �ֻ����
			gotoActivity(this, CheckActivity.class);
			break;
		case R.id.txt_bottom_tel:// ͨѶ��ȫ
			gotoActivity(this, TelMgrActivity.class);
			break;
		case R.id.txt_bottom_filemanage:// �ļ�����
			gotoActivity(this, FileMgrActivity.class);
			break;
		case R.id.txt_bottom_clear:// ��������
			gotoActivity(this, GarbageClearActivity.class);
			break;
		}
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		int what = msg.what;
		switch (what) {
		case MESSAGE_DOWN:// ��ֵ��Сʱִ��
			mTxtState.setText(R.string.buttom_accelerating);
			if (mConsumeNum >= 0) {
				mTxtPercent.setText("" + mConsumeNum);
				mConsumeNum -= 2;
				mHandler.sendEmptyMessageDelayed(MESSAGE_DOWN, 15);
			} else {
				mConsumeNum = 0;
				mTxtPercent.setText("" + mConsumeNum);
				mHandler.sendEmptyMessageDelayed(MESSAGE_UP, 15);
			}

			break;
		case MESSAGE_UP:// ��ֵ���ʱִ��
			if (mConsumeNum < (int) (mLater * 100)) {
				mTxtPercent.setText("" + mConsumeNum);
				mConsumeNum += 2;
				mHandler.sendEmptyMessageDelayed(MESSAGE_UP, 15);
			} else {
				mTxtPercent.setText("" + (int) (mLater * 100));
				mTxtState.setText(R.string.buttom_accelerate);
			}
			break;

		}

	}

	/**
	 * ���������������е��û����̣�������⣩
	 */
	public void clearOneKey() {
		List<ProcessInfo> listUser = mProcessUtil.getUserRunningAppInfo();
		for (int i = 0; i < listUser.size(); i++) {
			// �ų�����
			if (listUser.get(i).packageName == this.getPackageName()) {
				continue;
			}
			mProcessUtil.mActivityManager.killBackgroundProcesses(listUser
					.get(i).packageName);
			listUser.remove(i);
		}
	}
}
