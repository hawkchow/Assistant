package edu.feicui.aide.filemgr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import edu.feicui.aide.R;
import edu.feicui.aide.bean.SecondFileInfo;
import edu.feicui.aide.main.BaseActivity;

public class SecondFileMgrActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * ListView
	 */
	ListView mLst;
	/**
	 * ���������
	 */
	LayoutInflater mInflater;
	SimpleDateFormat mDateFormat;
	/**
	 * ������
	 */
	SecondFileAdapter mAdapter;
	/**
	 * ���ϱ���
	 */
	TextView mTxtTitle;
	/**
	 * ���ϻ���
	 */
	ImageView mImgBack;
	/**
	 * �����ť
	 */
	TextView mTxtClear;
	/**
	 * �ļ�����
	 */
	TextView mTxtNum;
	/**
	 * ռ�ÿռ�
	 */
	TextView mTxtSize;
	/**
	 * �ļ������������
	 */
	FileMgrUtil mFileMgrUtil;
	/**
	 * ��ͼ
	 */
	Intent mIntent;
	/**
	 * ɾ���ļ����ܴ�С
	 */
	long mLenDeleted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_secondfilemgr);
	}

	void init() {
		mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mAdapter = new SecondFileAdapter();
		mInflater = getLayoutInflater();
		mFileMgrUtil = FileMgrUtil.getInstance();
		mIntent = getIntent();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mTxtTitle = (TextView) findViewById(R.id.txt_second_file_title);
		mImgBack = (ImageView) findViewById(R.id.img_second_file_back);
		mImgBack.setOnClickListener(this);
		mTxtNum = (TextView) findViewById(R.id.txt_second_file_num);
		mTxtSize = (TextView) findViewById(R.id.txt_second_file_size);
		mTxtClear = (TextView) findViewById(R.id.txt_second_file_clear);
		mTxtClear.setOnClickListener(this);
		mLst = (ListView) findViewById(R.id.lst_second_file);
		setData();
		mLst.setAdapter(mAdapter);
		mLst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ǰ����Ŀ����Դ
				List<SecondFileInfo> list = mAdapter.getList();
				String suffix = list.get(position).suffix;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				// �ļ�·��Uri
				Uri uri = Uri.fromFile(list.get(position).file);
				// ͨ����׺�ж��ļ����ͣ�ʹ�ò�ͬ��ʽ��ͼ��
				if (mFileMgrUtil.isTextFile(suffix)) {// �ļ�
					intent.setDataAndType(uri, "text/*");
				} else if (mFileMgrUtil.isImageFile(suffix)) {// ͼƬ
					intent.setDataAndType(uri, "image/*");
				} else if (mFileMgrUtil.isAudioFile(suffix)) {// ��Ƶ
					intent.setDataAndType(uri, "audio/*");
				} else if (mFileMgrUtil.isVideoFile(suffix)) {// ��Ƶ
					intent.setDataAndType(uri, "video/*");
				} else if (mFileMgrUtil.isZipFile(suffix)
						|| mFileMgrUtil.isProgramFile(suffix)) {// ѹ�����Ͱ�װ��
					intent.setDataAndType(uri, "application/*");
				} else {// ����
					Toast.makeText(SecondFileMgrActivity.this, "׳ʿ�����������֣�",
							Toast.LENGTH_SHORT).show();
					return;
				}
				startActivity(intent);
			}
		});
	}

	/**
	 * ����Intent����������Ϣ�����ز�ͬ����Դ�ͱ���
	 */
	public void setData() {
		String type = mIntent.getStringExtra("type");

		switch (type) {
		case "ȫ��":// ��ȡȫ������Դ��SecondFileInfo��
			mAdapter.setList(mFileMgrUtil.secAllFile);
			mTxtTitle.setText("ȫ��");
			setTextContent(mFileMgrUtil.secAllFile, mFileMgrUtil.allLen);
			break;
		case "�ĵ�":
			mAdapter.setList(mFileMgrUtil.secTxtFile);
			mTxtTitle.setText("�ĵ�");
			setTextContent(mFileMgrUtil.secTxtFile, mFileMgrUtil.txtLen);
			break;
		case "��Ƶ":
			mAdapter.setList(mFileMgrUtil.secAudioFile);
			mTxtTitle.setText("��Ƶ");
			setTextContent(mFileMgrUtil.secAudioFile, mFileMgrUtil.audioLen);
			break;
		case "��Ƶ":
			mAdapter.setList(mFileMgrUtil.secVideoFile);
			mTxtTitle.setText("��Ƶ");
			setTextContent(mFileMgrUtil.secVideoFile, mFileMgrUtil.videoLen);
			break;
		case "ͼƬ":
			mAdapter.setList(mFileMgrUtil.secImgFile);
			mTxtTitle.setText("ͼƬ");
			setTextContent(mFileMgrUtil.secImgFile, mFileMgrUtil.imageLen);
			break;
		case "ѹ����":
			mAdapter.setList(mFileMgrUtil.secZipFile);
			mTxtTitle.setText("ѹ����");
			setTextContent(mFileMgrUtil.secZipFile, mFileMgrUtil.zipLen);
			break;
		case "��װ��":
			mAdapter.setList(mFileMgrUtil.secApkFile);
			mTxtTitle.setText("��װ��");
			setTextContent(mFileMgrUtil.secApkFile, mFileMgrUtil.apkLen);
			break;
		default:
			break;
		}
	}

	/**
	 * ����ҳ�����ļ��������ļ�ռ�ÿռ��С
	 * 
	 * @param list
	 *            Ŀ�꼯��
	 * @param length
	 *            ĳ�������ļ���С
	 */
	public void setTextContent(List<SecondFileInfo> list, long length) {
		String numStr = String.format("%1$d��", list.size());
		mTxtNum.setText(numStr);
		String lengthStr = Formatter.formatFileSize(SecondFileMgrActivity.this,
				length);
		mTxtSize.setText(lengthStr);
	}

	class SecondFileAdapter extends BaseAdapter {
		List<SecondFileInfo> list;

		public List<SecondFileInfo> getList() {
			return list;
		}

		public void setList(List<SecondFileInfo> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return null != list ? list.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null != list ? list.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			// ������ǰ�±�
			final int id = position;
			if (null == convertView) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_second_file,
						parent, false);
				holder.chb = (CheckBox) convertView
						.findViewById(R.id.chb_second_file_item);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.img_second_file_item);
				holder.name = (TextView) convertView
						.findViewById(R.id.txt_second_file_item_name);
				holder.time = (TextView) convertView
						.findViewById(R.id.txt_second_file_item_time);
				holder.size = (TextView) convertView
						.findViewById(R.id.txt_second_file_item_size);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// ��������Ŀ��CheckBox����¼�
			holder.chb
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// ����״̬���浽��Ӧ����Դ
							list.get(id).state = isChecked;
						}
					});
			// ���õ�ǰChecbox��״̬
			holder.chb.setChecked(list.get(position).state);
			// ����֪��׺������£��Բ�ͬ��׺���ò�ͬicon
			String suffix = list.get(position).suffix;
			String iconName = "icon_" + suffix;
			// ��ȡ����ͼƬ��Դ��ID
			int ImgID = SecondFileMgrActivity.this.getResources()
					.getIdentifier(iconName, "drawable",
							SecondFileMgrActivity.this.getPackageName());
			// ����icon
			if (mFileMgrUtil.isAudioFile(suffix)) {// ��Ƶ
				holder.icon.setImageResource(R.drawable.icon_audio);
			} else if (mFileMgrUtil.isVideoFile(suffix)) {// ��Ƶ
				holder.icon.setImageResource(R.drawable.icon_video);
			} else if (mFileMgrUtil.isZipFile(suffix)) {// ѹ����
				holder.icon.setImageResource(R.drawable.icon_rar);
			} else if (mFileMgrUtil.isProgramFile(suffix)) {// ��װ��
				holder.icon.setImageResource(R.drawable.icon_app);
			} else if (0 != ImgID) {// �ú�׺ƥ���icon����
				holder.icon.setImageResource(ImgID);
			} else if (mFileMgrUtil.isImageFile(suffix)) {// �ļ�����ΪͼƬʱ���⴦��
				Glide.with(SecondFileMgrActivity.this)
						.load(list.get(position).file).into(holder.icon);
			} else {// ����
				holder.icon.setImageResource(R.drawable.icon_file);
			}

			holder.name.setText(list.get(position).name);

			Date date = new Date(list.get(position).time);
			String time = mDateFormat.format(date);
			holder.time.setText(time);

			String size = Formatter.formatFileSize(SecondFileMgrActivity.this,
					list.get(position).size);
			holder.size.setText(size);
			return convertView;
		}
	}

	class ViewHolder {
		CheckBox chb;
		ImageView icon;
		TextView name;
		TextView time;
		TextView size;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.img_second_file_back:// ���ϻ��˰�ť
			onBackPressed();
			break;
		case R.id.txt_second_file_clear:// �ײ�ж�ذ�ť
			clearAllSelected();
			break;
		default:
			break;
		}
	}

	/**
	 * ɾ����ѡ���ļ�
	 */
	public void clearAllSelected() {
		// ��ǰ����Դ���ļ��ܴ�С
		long length = 0;
		// �洢��ֵ�ı���ÿ�ν�������0
		mLenDeleted = 0;
		// ��ǰListView����Դ
		List<SecondFileInfo> list = mAdapter.getList();
		// ɾ����ѡ�ļ�
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).state == true) {
				mLenDeleted += list.get(i).size;
				list.get(i).file.delete();
				list.remove(i);
			}
		}
		// ˢ��������
		mAdapter.notifyDataSetChanged();
		// �����ϲ��ļ���Ŀ��ռ�ÿռ�
		for (int i = 0; i < list.size(); i++) {
			length += list.get(i).size;
		}
		setTextContent(list, length);
	}

	/*
	 * ���ط��ؼ�
	 */
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		mIntent.putExtra("lenDeleted", mLenDeleted);
		setResult(RESULT_OK, mIntent);
		finish();
	}

}