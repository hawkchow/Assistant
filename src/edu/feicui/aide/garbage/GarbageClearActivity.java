package edu.feicui.aide.garbage;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.feicui.aide.R;
import edu.feicui.aide.garbage.CacheUtil.OnFlushListener;
import edu.feicui.aide.main.BaseActivity;

public class GarbageClearActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * ���������
	 */
	LayoutInflater mInflater;
	/**
	 * ���ϻ���
	 */
	ImageView mImgBack;
	/**
	 * ���ϲ���ʾ���ļ���С�Ŀؼ�
	 */
	TextView mTxtNum;
	/**
	 * ����չ��ListView
	 */
	ExpandableListView mElst;
	/**
	 * �ײ�ȫѡ��ѡ��
	 */
	CheckBox mChbAll;
	/**
	 * �ײ�����ť
	 */
	TextView mTxtClear;
	/**
	 * ���湤����
	 */
	CacheUtil mCacheUtil;
	/**
	 * ����Դ
	 */
	List<List<ChildInfo>> mGroup;
	/**
	 * �ֻ�����������С
	 */
	long phoneCacheSize;
	/**
	 * �ֻ��ڴ�������С
	 */
	long phoneStorageSize;
	/**
	 * SD������������С
	 */
	long SDcardCacheSize;
	/**
	 * SD���ڴ�������С
	 */
	long SDcardStorageSize;
	/**
	 * �����ļ�������С
	 */
	long fileCacheSize;
	/**
	 * ���������ļ���С
	 */
	long allSize;
	/**
	 * ���group����Դ
	 */
	String[] groupType = { "�ֻ���������", "�ֻ��ڴ�����", "SD����������", "SD���ڴ�����", "�����ļ�����" };
	/**
	 * ������
	 */
	MyAdapter mAdapter;
	/**
	 * ���ڼ�������
	 */
	boolean isLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_garbage);
	}

	void init() {
		mInflater = getLayoutInflater();
		mCacheUtil = new CacheUtil(this);
		mGroup = new ArrayList<>();
		mAdapter = new MyAdapter();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mImgBack = (ImageView) findViewById(R.id.img_garbage_back);
		mImgBack.setOnClickListener(this);
		mTxtNum = (TextView) findViewById(R.id.txt_garbage_num);

		mChbAll = (CheckBox) findViewById(R.id.chb_all_garbage);
		// �ײ�ȫѡ��ı��¼�
		mChbAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// ��������childs
				for (int i = 0; i < mGroup.size(); i++) {
					for (int j = 0; j < mGroup.get(i).size(); j++) {
						// �ı�����Դ
						mGroup.get(i).get(j).flag = isChecked;
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		});

		mTxtClear = (TextView) findViewById(R.id.txt_clear_garbage);
		mTxtClear.setOnClickListener(this);// �ײ�����ť����¼�

		mElst = (ExpandableListView) findViewById(R.id.elst_garbage);
		mElst.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				ChildViewHolder childHolder = (ChildViewHolder) v.getTag();
				childHolder.flag.toggle();
				// ����������Դ
				mGroup.get(groupPosition).get(childPosition).flag = childHolder.flag
						.isChecked();
				// �жϵײ�ȫѡ��״̬
				boolean b = true;
				for (int i = 0; i < mGroup.size(); i++) {
					for (int j = 0; j < mGroup.get(i).size(); j++) {
						if (mGroup.get(i).get(j).flag == false) {
							b = false;
						}
					}
				}
				mChbAll.setChecked(b);
				return true;
			}
		});
		setData();
		mElst.setAdapter(mAdapter);

		mCacheUtil.setOnFlushListener(new OnFlushListener() {

			@Override
			public void flush() {
				mHandler.sendEmptyMessageDelayed(1, 200);
			}
		});
	}

	private void setData() {
		new Thread() {
			public void run() {
				isLoading = true;// ���ڼ�������
				mGroup.add(mCacheUtil.getChild("phoneCache"));// ��ȡ�ֻ�������������
				mGroup.add(mCacheUtil.getChild("phoneStorage"));// ��ȡ�ֻ��ڴ���������
				mGroup.add(mCacheUtil.getChild("SDcardCache"));// ��ȡSD��������������
				mGroup.add(mCacheUtil.getChild("SDcardStorage"));// ��ȡSD���ڴ���������
				mGroup.add(mCacheUtil.getChild("fileCache"));// �����ļ���������
				isLoading = false;// �������
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						String allSize = Formatter.formatFileSize(
								GarbageClearActivity.this, mCacheUtil.allLen);
						// �����ܴ�С
						mTxtNum.setText(allSize);
						mAdapter.notifyDataSetChanged();
					}
				});
			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.img_garbage_back:// ���ϻ���
			finish();
			break;
		case R.id.txt_clear_garbage:// �ײ�����ť
			clearAllSelected();
			break;
		default:
			break;
		}
	}

	private void clearAllSelected() {
		// ��������childs
		for (int i = 0; i < mGroup.size(); i++) {
			for (int j = 0; j < mGroup.get(i).size(); j++) {
				// ���child�е�CheckBox״̬Ϊtrue����ɾ��
				if (mGroup.get(i).get(j).flag == true) {
					mGroup.get(i).get(j).file.delete();
					mGroup.get(i).remove(j);
				}
			}
		}
		// ����
		clearToZero();
		// ɾ���ļ���ˢ�¸������С��ʾֵ
		setTypeSize();
		mAdapter.notifyDataSetChanged();
		// �����ܴ�С
		String text = Formatter.formatFileSize(GarbageClearActivity.this,
				allSize);
		mTxtNum.setText(text);
		// ɾ����ɺ��жϵײ�ȫѡ��״̬
		boolean b = true;
		if (allSize == 0) {
			b = false;
		} else {
			for (int i = 0; i < mGroup.size(); i++) {
				for (int j = 0; j < mGroup.get(i).size(); j++) {
					if (mGroup.get(i).get(j).flag == false) {
						b = false;
					}
				}
			}
		}
		mChbAll.setChecked(b);

	}

	/**
	 * ����ɾ���������ļ��Ĵ�С ���ܴ�С
	 */
	private void setTypeSize() {
		for (int i = 0; i < mGroup.get(0).size(); i++) {
			phoneCacheSize += mGroup.get(0).get(i).size;
		}
		for (int i = 0; i < mGroup.get(1).size(); i++) {
			phoneStorageSize += mGroup.get(1).get(i).size;
		}
		for (int i = 0; i < mGroup.get(2).size(); i++) {
			SDcardCacheSize += mGroup.get(2).get(i).size;
		}
		for (int i = 0; i < mGroup.get(3).size(); i++) {
			SDcardStorageSize += mGroup.get(3).get(i).size;
		}
		for (int i = 0; i < mGroup.get(4).size(); i++) {
			fileCacheSize += mGroup.get(4).get(i).size;
		}
		allSize = phoneCacheSize + phoneStorageSize + SDcardCacheSize
				+ SDcardStorageSize + fileCacheSize;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		String allSize = Formatter.formatFileSize(this, mCacheUtil.allLen);
		// �����ܴ�С
		mTxtNum.setText(allSize);
		phoneCacheSize = mCacheUtil.phoneCacheLen;
		phoneStorageSize = mCacheUtil.phoneStorageLen;
		SDcardCacheSize = mCacheUtil.SDcardCacheLen;
		SDcardStorageSize = mCacheUtil.SDcardStorageLen;
		fileCacheSize = mCacheUtil.fileCacheLen;
		// ˢ��������
		mAdapter.notifyDataSetChanged();
	}

	class MyAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return null != mGroup ? mGroup.size() : 0;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return mGroup.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mGroup.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return mGroup.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			GroupViewHolder groupHolder = null;
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.item_group_garbage,
						parent, false);
				groupHolder = new GroupViewHolder();
				groupHolder.type = (TextView) convertView
						.findViewById(R.id.txt_type_group_garbage);
				groupHolder.groupSize = (TextView) convertView
						.findViewById(R.id.txt_size_group_garbage);
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupViewHolder) convertView.getTag();
			}
			groupHolder.type.setText(groupType[groupPosition]);
			String text = null;
			switch (groupPosition) {
			case 0:
				text = Formatter.formatFileSize(GarbageClearActivity.this,
						phoneCacheSize);
				break;
			case 1:
				text = Formatter.formatFileSize(GarbageClearActivity.this,
						phoneStorageSize);
				break;
			case 2:
				text = Formatter.formatFileSize(GarbageClearActivity.this,
						SDcardCacheSize);
				break;
			case 3:
				text = Formatter.formatFileSize(GarbageClearActivity.this,
						SDcardStorageSize);
				break;
			case 4:
				text = Formatter.formatFileSize(GarbageClearActivity.this,
						fileCacheSize);
				break;
			}
			groupHolder.groupSize.setText(text);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder childHolder = null;
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.item_childs_garbage,
						parent, false);
				childHolder = new ChildViewHolder();
				childHolder.flag = (CheckBox) convertView
						.findViewById(R.id.chb_child_garbage);
				childHolder.icon = (ImageView) convertView
						.findViewById(R.id.img_child_garbage);
				childHolder.name = (TextView) convertView
						.findViewById(R.id.txt_name_child_garbage);
				childHolder.size = (TextView) convertView
						.findViewById(R.id.txt_size_child_garbage);
				convertView.setTag(childHolder);
			} else {
				childHolder = (ChildViewHolder) convertView.getTag();
			}
			childHolder.flag.setChecked(mGroup.get(groupPosition).get(
					childPosition).flag);
			childHolder.icon.setImageDrawable(mGroup.get(groupPosition).get(
					childPosition).icon);
			childHolder.name.setText(mGroup.get(groupPosition).get(
					childPosition).name);
			String size = Formatter.formatFileSize(GarbageClearActivity.this,
					mGroup.get(groupPosition).get(childPosition).size);
			childHolder.size.setText(size);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			if (isLoading) {
				return false;
			}
			return true;
		}

	}

	class GroupViewHolder {
		TextView type;
		TextView groupSize;
		ProgressBar Pgb;
	}

	class ChildViewHolder {
		CheckBox flag;
		ImageView icon;
		TextView name;
		TextView size;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mGroup.clear();
		clearToZero();
	}

	void clearToZero() {
		phoneCacheSize = 0;
		phoneStorageSize = 0;
		SDcardCacheSize = 0;
		SDcardStorageSize = 0;
		fileCacheSize = 0;
		allSize = 0;
	}
}
