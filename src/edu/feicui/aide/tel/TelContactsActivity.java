package edu.feicui.aide.tel;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import edu.feicui.aide.R;
import edu.feicui.aide.bean.ContactsInfo;
import edu.feicui.aide.main.BaseActivity;

/**
 * ͨ����¼������Ϣ
 * 
 */
public class TelContactsActivity extends BaseActivity {
	/**
	 * �м����
	 */
	TextView mTxtTitle;
	/**
	 * ���Ϸ��ذ�ť
	 */
	ImageView mImgBack;
	/**
	 * ListView
	 */
	ListView mLst;
	/**
	 * ���������
	 */
	LayoutInflater mLayoutInflater;
	/**
	 * ͨ����¼����Դ
	 */
	List<ContactsInfo> mListContacts;
	/**
	 * TelManager�����
	 */
	TelManager mManager;
	/**
	 * ���������
	 */
	ProgressDialog mDialog;
	/**
	 * ������
	 */
	TelContactsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_tel_contacts);
	}

	void init() {
		mLayoutInflater = getLayoutInflater();
		mManager = TelManager.getInstance(this);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mImgBack = (ImageView) findViewById(R.id.img_telcontacts_back);
		mImgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTxtTitle = (TextView) findViewById(R.id.txt_telcontacts_title);
		mTxtTitle.setText(getResources().getString(R.string.txt_tel_contacts));
		// ListView
		mLst = (ListView) findViewById(R.id.lst_telcontacts);
		// ���ò��Ź���
		mLst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String number = mListContacts.get(position).number;
				String uri = "tel:" + number;
				intent.setData(Uri.parse(uri));
				startActivity(intent);
			}
		});
		// ��������Դ
		setData();
		// ������
		mAdapter = new TelContactsAdapter();
		// ����������
		mLst.setAdapter(mAdapter);
	}

	/**
	 * ���߳��м�������Դ,���ڻ������Դǰ��ʾ������
	 */
	public void setData() {
		// �������������
		mDialog = ProgressDialog
				.show(this,
						this.getResources().getString(
								R.string.dialog_progress_title),
						this.getResources().getString(
								R.string.dialog_progress_message));
		// ����ListView
		mLst.setVisibility(View.INVISIBLE);
		new Thread() {
			public void run() {
				mListContacts = mManager.getContactsInfo();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mAdapter.notifyDataSetChanged();
						mDialog.cancel();
						mLst.setVisibility(View.VISIBLE);
					}
				});
			};
		}.start();
	}

	class TelContactsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return null != mListContacts ? mListContacts.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null != mListContacts ? mListContacts.get(position) : null;
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
				convertView = mLayoutInflater.inflate(
						R.layout.item_telcontacts, parent, false);
				holder.img = (ImageView) convertView
						.findViewById(R.id.img_telcontacts_type);
				holder.txtName = (TextView) convertView
						.findViewById(R.id.txt_telcontacts_name);
				holder.txtNumber = (TextView) convertView
						.findViewById(R.id.txt_telcontacts_number);
				holder.txtTime = (TextView) convertView
						.findViewById(R.id.txt_telcontacts_time);
				holder.txtDuration = (TextView) convertView
						.findViewById(R.id.txt_telcontacts_duration);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// ����type
			int type = mListContacts.get(position).type;
			switch (type) {
			case CallLog.Calls.INCOMING_TYPE:// ����
				holder.img.setImageResource(R.drawable.call_incoming);
				break;
			case CallLog.Calls.OUTGOING_TYPE:// ����
				holder.img.setImageResource(R.drawable.call_outgoing);
				break;
			case CallLog.Calls.MISSED_TYPE:// δ��
				holder.img.setImageResource(R.drawable.call_missed);
				break;
			default:
				break;
			}
			// ��������
			holder.txtName.setText(mListContacts.get(position).name);
			// ���õ绰����
			holder.txtNumber.setText(mListContacts.get(position).number);
			// ����ͨ��ʱ��
			holder.txtDuration.setText(mListContacts.get(position).duration);
			// ����ͨ��ʱ��
			holder.txtTime.setText(mListContacts.get(position).time);
			return convertView;
		}
	}

	class ViewHolder {
		ImageView img;
		TextView txtName;
		TextView txtNumber;
		TextView txtTime;
		TextView txtDuration;
	}
}
