package edu.feicui.aide.accelerate;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import edu.feicui.aide.R;
import edu.feicui.aide.dialog.MyDialog;
import edu.feicui.aide.dialog.MyDialog.OnMyDialogListener;
import edu.feicui.aide.main.BaseActivity;
import edu.feicui.aide.util.MemoryUtil;
import edu.feicui.aide.util.ProcessUtil;

/**
 * ����Ϊʵ���ֻ����ٹ���
 * 
 */
public class AccelerateActivity extends BaseActivity implements
		OnItemClickListener, View.OnClickListener {

	/**
	 * ���Ͻǻ��˰�ť
	 */
	ImageView mImgBack;
	/**
	 * �ֻ�Ʒ��
	 */
	TextView mTxtBrand;
	/**
	 * �ֻ���Ʒ���� �� �汾��Ϣ
	 */
	TextView mTxtProduct;
	/**
	 * Item ListView
	 */
	ListView mListView;
	/**
	 * �ײ�ȫѡͼ��
	 */
	ImageView mImgAll;
	/**
	 * ���������
	 */
	LayoutInflater mInflater;
	/**
	 * ϵͳ��������Դ
	 */
	List<ProcessInfo> mListSystem;
	/**
	 * �û���������Դ
	 */
	List<ProcessInfo> mListUser;

	/**
	 * �ײ��л��û����̺�ϵͳ���̰�ť
	 */
	ToggleButton mTgl;
	/**
	 * �Զ���������
	 */
	MyAdapter mAdapter;
	/**
	 * �ϲ�������
	 */
	ProgressBar mPgb;
	/**
	 * ��ʾ�ڴ��ı�
	 */
	TextView mTxtMem;
	/**
	 * �ϲ�һ������ť
	 */
	Button mBtn;
	/**
	 * ����APP����
	 */
	ProcessUtil mProcessUtil;
	/**
	 * �Զ���Ի������
	 */
	private MyDialog mMyDialog;
	/**
	 * ��ȡ�ڴ���Ϣ����
	 */
	private MemoryUtil mMemUtil;
	boolean mFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_accelerate);
	}

	/**
	 * ��ʼ���Զ��������������������������APP���̶���
	 */
	void init() {
		mAdapter = new MyAdapter();
		mInflater = getLayoutInflater();
		mProcessUtil = ProcessUtil.getInstance(AccelerateActivity.this);
		mMyDialog = new MyDialog(this, R.style.Dialog_FS);
		mMemUtil = MemoryUtil.getInstance(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		// �����ֻ�Ʒ��
		mTxtBrand = (TextView) findViewById(R.id.txt_accelerate_brand);
		mTxtBrand.setText(Build.BRAND);
		// �����ֻ���Ʒ����ϵͳ�汾��
		mTxtProduct = (TextView) findViewById(R.id.txt_accelerate_product);
		String product = String.format("Huawei %1$S Android%2$S",
				Build.PRODUCT, Build.VERSION.RELEASE);
		mTxtProduct.setText(product);
		// ������
		mPgb = (ProgressBar) findViewById(R.id.pgb_accelerate_up);
		// ��ʾ���ڴ������ı�
		mTxtMem = (TextView) findViewById(R.id.txt_accelerate_mem);

		mImgBack = (ImageView) findViewById(R.id.img_accelerate_back);
		mImgBack.setOnClickListener(this);
		mBtn = (Button) findViewById(R.id.btn_accelerate_clear);
		mBtn.setOnClickListener(this);

		// ʵ����ListView
		mListView = (ListView) findViewById(R.id.lst_accelerate_mid);
		// ��������Դ������ʾ��ʼϵͳ����
		initData();
		// ��������Ŀ�ĵ���¼�
		mListView.setOnItemClickListener(this);

		mImgAll = (ImageView) findViewById(R.id.img_accelerate_bottom);
		// ���õײ�ȫѡͼ�����¼�
		mImgAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFlag = !mFlag;
				List<ProcessInfo> list = mAdapter.getData();
				for (int i = 0; i < list.size(); i++) {
					list.get(i).flag = mFlag;
				}
				// �ı�ײ�ȫѡͼ��ͼƬ
				setImgAll(mFlag);
				mAdapter.notifyDataSetChanged();
			}
		});

		mTgl = (ToggleButton) findViewById(R.id.tgl_acceletate_bottom);
		mTgl.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// �ж�ToogleButton��ť��״̬���Ӷ����ز�ͬ����Դ
				boolean flag = true;
				if (isChecked) {
					mAdapter.setData(mListSystem);
					for (int i = 0; i < mListSystem.size(); i++) {
						if (mListSystem.get(i).flag == false) {
							flag = false;
						}
					}
				} else {
					mAdapter.setData(mListUser);
					for (int i = 0; i < mListUser.size(); i++) {
						if (mListUser.get(i).flag == false) {
							flag = false;
						}
					}
				}
				// ����flag������ò�ͬͼƬ
				setImgAll(flag);
				// ˢ��������
				mAdapter.notifyDataSetChanged();
			}
		});

	}

	/**
	 * �ڼ�������Դʱ��ʾ�������Ի���<br/>
	 * 
	 * ������ɺ���ʾListView
	 */
	public void initData() {
		// ��������֮ǰ��ʾ������������ListView
		final ProgressDialog dialog = ProgressDialog.show(this, this
				.getResources().getString(R.string.dialog_progress_title), this
				.getResources().getString(R.string.dialog_progress_message));
		mListView.setVisibility(View.INVISIBLE);
		new Thread() {

			@Override
			public void run() {// ���߳��м�������Դ��ֹANR�쳣

				// ϵͳ��������Դ
				mListSystem = mProcessUtil.getSystemRunningAppInfo();
				// �û���������Դ
				mListUser = mProcessUtil.getUserRunningAppInfo();

				// �÷������������߳���ֱ�Ӳ���UI
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						// ����ϵͳ���ݸ��������е�list
						mAdapter.setData(mListUser);
						// ����������
						mListView.setAdapter(mAdapter);
						dialog.cancel();
						// ���ı��������������ڴ�����
						mTxtMem.setText(mMemUtil.getTextMem());
						// ˢ�½�������ʾ
						mPgb.setProgress(mMemUtil.getProgress());
						// ��ʾlistview
						mListView.setVisibility(View.VISIBLE);

					}
				});
			}
		}.start();

	}

	/**
	 * �Զ���������
	 * 
	 */
	class MyAdapter extends BaseAdapter {
		List<ProcessInfo> list;

		public void setData(List<ProcessInfo> list) {
			this.list = list;
		}

		public List<ProcessInfo> getData() {
			return list;
		}

		@Override
		public int getCount() {
			return null != list ? list.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_accelerate,
						parent, false);
				holder = new ViewHolder();
				holder.chb = (CheckBox) convertView
						.findViewById(R.id.chb_accelerate_item);
				holder.img = (ImageView) convertView
						.findViewById(R.id.img_accelerate_item);
				holder.txtUp = (TextView) convertView
						.findViewById(R.id.txt1_accelerate_item);
				holder.txtDown = (TextView) convertView
						.findViewById(R.id.txt2_accelerate_item);
				holder.txtRight = (TextView) convertView
						.findViewById(R.id.txt_accelerate_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// ����ͬ�ؼ�������Ӧ����
			holder.chb.setChecked(list.get(position).flag);
			holder.img.setImageDrawable(list.get(position).img);
			holder.txtUp.setText(list.get(position).label);
			holder.txtDown.setText(list.get(position).memory);
			holder.txtRight.setText(list.get(position).textRight);
			return convertView;
		}
	}

	static class ViewHolder {
		CheckBox chb;
		ImageView img;
		TextView txtUp;
		TextView txtDown;
		TextView txtRight;
	}

	/*
	 * ����Ŀ�ĵ���¼�
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.chb.toggle();

		// ��������Ӧ����Դ
		boolean flag = holder.chb.isChecked();
		List<ProcessInfo> list = mAdapter.getData();
		list.get(position).flag = flag;
		// ������ǰ����Դ������flagAll״̬���ò�ͬͼƬ
		boolean flagAll = true;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).flag == false) {
				flagAll = false;
			}
		}
		setImgAll(flagAll);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_accelerate_clear:// һ������ť
			// �����������ϵͳ������Ҫ���ж����ж�
			if (mTgl.isChecked()) {
				showDialog();
				mMyDialog.show();
			} else {
				clearAllSelected();
			}
			break;
		case R.id.img_accelerate_back:// ���Ͻǻ��˰�ť
			finish();
		default:
			break;
		}
	}

	/**
	 * ��ʾ�Ի���,�����û�ѡ����������ز���<br/>
	 * MyDialog�лص������ľ���ʵ��
	 */
	public void showDialog() {
		mMyDialog.setOnMyDiaLogListener(new OnMyDialogListener() {

			@Override
			public void onClickYesListener() {
				clearAllSelected();
				mMyDialog.cancel();
			}

			@Override
			public void onClickNoListener() {

				mMyDialog.cancel();

			}
		});
	}

	/**
	 * ɾ��ѡ�е�����Ŀ
	 */
	public void clearAllSelected() {
		List<ProcessInfo> list = mAdapter.getData();
		// Log.e("tag", "ɾ��ǰ�Ľ�������="
		// + mProcessUtil.mActivityManager.getRunningAppProcesses().size());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).flag == true) {
				// Log.e("tag", list.get(i).packageName);
				mProcessUtil.mActivityManager.killBackgroundProcesses(list
						.get(i).packageName);
				// ������Դ���Ƴ���ɾ������Ŀ
				list.remove(i);
			}
		}

		// Log.e("tag", "ɾ����Ľ�������="
		// + mProcessUtil.mActivityManager.getRunningAppProcesses().size());
		// ˢ��������
		mAdapter.notifyDataSetChanged();
		// ˢ�½���������Ӧ�ı���������
		mTxtMem.setText(mMemUtil.getTextMem());
		mPgb.setProgress(mMemUtil.getProgress());

		// ɾ����ɺ�ı�ײ���ѡ���״̬
		if (list.size() == 0) {
			mFlag = false;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).flag == false) {
					mFlag = false;
				}
			}
		}
		setImgAll(mFlag);
	}

	public void setImgAll(boolean flag) {
		if (flag) {
			mImgAll.setImageDrawable(this.getResources().getDrawable(
					R.drawable.check_rect_correct_checked));
		} else {
			mImgAll.setImageDrawable(this.getResources().getDrawable(
					R.drawable.check_rect_correct_default));
		}
	}
}
