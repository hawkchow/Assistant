package edu.feicui.aide.tel;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import edu.feicui.aide.bean.TelPerson;
import edu.feicui.aide.main.BaseActivity;

/**
 * ͨѶ¼������Ϣ
 * 
 */
public class ContactsContractActivity extends BaseActivity {
	/**
	 * ����
	 */
	TextView mTxtTitle;
	/**
	 * ���ϻ���
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
	 * ͨѶ¼����Դ
	 */
	List<TelPerson> mListPerson;
	/**
	 * ������
	 */
	ContactsContractUtil mContactsContractUtil;
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
		mContactsContractUtil = new ContactsContractUtil(this);
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
		// ���ñ���
		mTxtTitle = (TextView) findViewById(R.id.txt_telcontacts_title);
		mTxtTitle.setText(getResources().getString(R.string.txt_tel_addrlist));
		// ListView
		mLst = (ListView) findViewById(R.id.lst_telcontacts);
		// ���ò��Ź���
		mLst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String number = mListPerson.get(position).mobileNumber;
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
				mListPerson = mContactsContractUtil.getContactsInfo();
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
			return null != mListPerson ? mListPerson.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null != mListPerson ? mListPerson.get(position) : null;
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
						R.layout.item_contacts_contract, parent, false);
				holder.name = (TextView) convertView
						.findViewById(R.id.txt_contactscontract_name);
				holder.email = (TextView) convertView
						.findViewById(R.id.txt_contactscontract_email);
				holder.homeNumber = (TextView) convertView
						.findViewById(R.id.txt_contactscontract_homenum);
				holder.workNumber = (TextView) convertView
						.findViewById(R.id.txt_contactscontract_worknum);
				holder.mobileNumber = (TextView) convertView
						.findViewById(R.id.txt_contactscontract_mobilenum);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(mListPerson.get(position).name);
			String emailstr = String.format("�����ʼ�:%1$s",
					mListPerson.get(position).email);
			holder.email.setText(emailstr);

			String homeNumberStr = String.format("��ͥ�绰��%1$s",
					mListPerson.get(position).homeNumber);
			holder.homeNumber.setText(homeNumberStr);

			String mobileNumberStr = String.format("�ƶ��绰��%1$s",
					mListPerson.get(position).mobileNumber);
			holder.mobileNumber.setText(mobileNumberStr);

			String workNumberStr = String.format("�����绰��%1$s",
					mListPerson.get(position).workNumber);
			holder.workNumber.setText(workNumberStr);
			return convertView;
		}

		class ViewHolder {
			TextView name;
			TextView mobileNumber;
			TextView homeNumber;
			TextView workNumber;
			TextView email;
		}
	}
}
