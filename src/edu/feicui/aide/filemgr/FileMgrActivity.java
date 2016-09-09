package edu.feicui.aide.filemgr;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.aide.R;
import edu.feicui.aide.bean.FileInfo;
import edu.feicui.aide.filemgr.FileMgrUtil.onFlushListener;
import edu.feicui.aide.main.BaseActivity;

/**
 * �ļ������һ����
 * 
 */
public class FileMgrActivity extends BaseActivity {
	/**
	 * ���ϻ���
	 */
	ImageView mImgBack;
	/**
	 * �����ļ���С�ľ�������
	 */
	TextView mTxtNum;
	/**
	 * ListView
	 */
	ListView mLst;
	/**
	 * ��һ��������Դ
	 */
	List<FileInfo> mList;
	/**
	 * ���������
	 */
	LayoutInflater mInflater;
	/**
	 * �ļ����������
	 */
	FileMgrUtil mFileMgrUtil;
	/**
	 * ����������
	 */
	FileMgrAdapter mAdapter;
	/**
	 * �жϼ��������Ƿ����<br/>
	 * true��ʾ���ڼ���<br/>
	 * false��ʾ�������
	 */
	boolean isLoad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_fielmgr);
	}

	/**
	 * ��ʼ���������
	 */
	void init() {
		mFileMgrUtil = FileMgrUtil.getInstance();
		mInflater = getLayoutInflater();
		mAdapter = new FileMgrAdapter();
	}

	/**
	 * ��������
	 */
	public void load() {
		new Thread() {
			public void run() {
				isLoad = true;
				// ��ȡ����Դ
				mList = mFileMgrUtil.mList;
				// ��������Դ
				mFileMgrUtil.SetFileInfo();
				isLoad = false;
			};
		}.start();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mImgBack = (ImageView) findViewById(R.id.img_file_back);
		mImgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTxtNum = (TextView) findViewById(R.id.txt_file_num);
		// ListView
		mLst = (ListView) findViewById(R.id.lst_file_mgr);
		// ��������
		load();
		// ����������
		mLst.setAdapter(mAdapter);
		// ListView ����Ŀ����¼�
		mLst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����δ��ȡ��ɣ�������ת
				if (isLoad) {
					return;
				}
				// ʹ�ô�����ֵ��Intent
				Intent intent = new Intent(FileMgrActivity.this,
						SecondFileMgrActivity.class);
				String fileType = mList.get(position).fileType;
				// ���ļ����ʹ��ݹ�ȥ,���ڷֱ浱ǰ���������Ŀ
				intent.putExtra("type", fileType);
				startActivityForResult(intent, position);
			}
		});
		mFileMgrUtil.setOnFlushListener(new onFlushListener() {
			// �ص�����ʵ��
			@Override
			public void onFlush() {
				mHandler.sendEmptyMessageDelayed(1, 300);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// �ڶ���������ɾ���ļ��Ĵ�С
		long lenDeleted = data.getLongExtra("lenDeleted", 0);
		// �ı����ļ����ļ���С
		mFileMgrUtil.allLen -= lenDeleted;
		// �ı����Ӧ�ļ��Ĵ�С����FileMgrUtil�еĳ�Ա������
		switch (requestCode) {
		case 1:
			mFileMgrUtil.txtLen -= lenDeleted;
			break;
		case 2:
			mFileMgrUtil.audioLen -= lenDeleted;
			break;
		case 3:
			mFileMgrUtil.videoLen -= lenDeleted;
			break;
		case 4:
			mFileMgrUtil.imageLen -= lenDeleted;
			break;
		case 5:
			mFileMgrUtil.zipLen -= lenDeleted;
			break;
		case 6:
			mFileMgrUtil.apkLen -= lenDeleted;
			break;
		default:
			break;
		}
		// �ı���Ӧ����Ŀ���ļ���С
		mList.get(requestCode).fileLen -= lenDeleted;
		// Log.e("����Ŀ��ʣ���ļ���С:", mList.get(requestCode).fileLen + "");
		// Log.e("ɾ���ļ��Ĵ�С:", lenDeleted + "");
		// Log.e("�������ļ���ʣ���ļ���С��", mFileMgrUtil.allLen + "");
		// �ı�"ȫ��"����Ŀ�д�Сֵ
		mList.get(0).setFileLen(mFileMgrUtil.allLen);
		// ˢ��������
		mAdapter.notifyDataSetChanged();
		// ˢ���ϲ���ʾ���ļ���С����
		String lenStr = Formatter.formatFileSize(FileMgrActivity.this,
				mFileMgrUtil.allLen);
		mTxtNum.setText(lenStr);
	}

	class FileMgrAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return null != mList ? mList.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null != mList ? mList.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (null == convertView) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_fielmgr, parent,
						false);
				holder.type = (TextView) convertView
						.findViewById(R.id.txt_fileitem_type);
				holder.size = (TextView) convertView
						.findViewById(R.id.txt_fileitem_size);
				holder.pgb = (ProgressBar) convertView
						.findViewById(R.id.pgb_file_item);
				holder.img = (ImageView) convertView
						.findViewById(R.id.img_file_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.type.setText(mList.get(position).fileType);
			// ��ʾ����Ŀ���ļ���С��ֵ
			long fileLen = mList.get(position).fileLen;
			String fileLenStr = Formatter.formatFileSize(FileMgrActivity.this,
					fileLen);
			holder.size.setText(fileLenStr);

			// flagΪtrueʱ��ʾimg,����pgb;
			if (mList.get(position).isFinish) {
				holder.pgb.setVisibility(View.INVISIBLE);
				holder.img.setVisibility(View.VISIBLE);
			} else {
				holder.pgb.setVisibility(View.VISIBLE);
				holder.img.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}
	}

	class ViewHolder {
		TextView type;
		TextView size;
		ProgressBar pgb;
		ImageView img;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		// ��ȡ��ǰȫ���ļ��Ĵ�С
		long fileAllLen = mFileMgrUtil.allLen;
		String fileAllLenStr = Formatter.formatFileSize(FileMgrActivity.this,
				fileAllLen);
		// ����ȫ���ļ���С
		mTxtNum.setText(fileAllLenStr);
		// ˢ��������
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearToZero();
	}

	/**
	 * �÷���������0
	 */
	public void clearToZero() {
		// ��С��0(��һ����)
		mFileMgrUtil.allLen = 0;
		mFileMgrUtil.txtLen = 0;
		mFileMgrUtil.audioLen = 0;
		mFileMgrUtil.videoLen = 0;
		mFileMgrUtil.apkLen = 0;
		mFileMgrUtil.zipLen = 0;
		mFileMgrUtil.imageLen = 0;
		// �Ƴ����󣨵ڶ����棩
		mFileMgrUtil.secAllFile.clear();
		mFileMgrUtil.secApkFile.clear();
		mFileMgrUtil.secAudioFile.clear();
		mFileMgrUtil.secImgFile.clear();
		mFileMgrUtil.secSpacilFile.clear();
		mFileMgrUtil.secTxtFile.clear();
		mFileMgrUtil.secVideoFile.clear();
		mFileMgrUtil.secZipFile.clear();
	}
}
