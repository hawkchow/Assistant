package edu.feicui.aide.tel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import edu.feicui.aide.bean.TelPerson;

/**
 * �ṩ��ȡͨѶ¼���ݵķ���
 * 
 */
public class ContactsContractUtil {
	Context mContext;
	ContentResolver mContentResolver;

	public ContactsContractUtil(Context context) {
		mContext = context;
		mContentResolver = context.getContentResolver();
	}

	/**
	 * ��ȡͨѶ¼��Ϣ(Contacts�ܱ�)
	 * 
	 * @return
	 */
	public List<TelPerson> getContactsInfo() {
		List<TelPerson> list = new ArrayList<>();
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		Cursor cursor = mContentResolver.query(uri, new String[] {
				ContactsContract.Contacts._ID,// ����_ID
				ContactsContract.Contacts.DISPLAY_NAME,// ����
				ContactsContract.Contacts.HAS_PHONE_NUMBER // �Ƿ��е绰
				}, null, null, null);
		// �±�
		int _idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		int displayNameIndex = cursor
				.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		int hasPhoneNumberIndex = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		if (null != cursor) {
			while (cursor.moveToNext()) {
				long _id = cursor.getLong(_idIndex);
				String name = cursor.getString(displayNameIndex);
				int hasPhoneNumber = cursor.getInt(hasPhoneNumberIndex);
				TelPerson p = new TelPerson();
				p._id = String.valueOf(_id);
				p.name = name;
				p.hasPhoneNumber = hasPhoneNumber;
				// ��ȡָ����ϵ�˵绰����
				if (hasPhoneNumber > 0) {
					setPhoneNumber(p, p._id);
				}
				if (null == p.homeNumber) {
					p.homeNumber = "δ֪";
				}
				if (null == p.workNumber) {
					p.workNumber = "δ֪";
				}
				if (null == p.mobileNumber) {
					p.mobileNumber = "δ֪";
				}
				// ��ȡָ����ϵ�˵����ʼ�
				setEmailadr(p, p._id);
				if (null == p.email) {
					p.email = "δ֪";
				}
				list.add(p);
				// Log.e("tag", p.toString());
			}
			cursor.close();
		}
		return list;
	}

	/**
	 * ��ȡָ����ϵ�˵ĵ绰����,������
	 * 
	 * @param p
	 *            ָ��Ŀ����ϵ��
	 * @param _id
	 *            Ŀ����ϵ�˵�ID
	 */
	public void setPhoneNumber(TelPerson p, String _id) {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		Cursor cursor = mContentResolver.query(uri, new String[] {
				ContactsContract.CommonDataKinds.Phone.NUMBER,// �绰����
				ContactsContract.CommonDataKinds.Phone.TYPE },// �绰����
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",// ɸѡ����
				new String[] { _id },// �����Ĳ���
				null);
		// �±�
		int numberIndex = cursor
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		int typeIndex = cursor
				.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
		if (null != cursor) {
			while (cursor.moveToNext()) {
				String number = cursor.getString(numberIndex);
				int type = cursor.getInt(typeIndex);
				switch (type) {
				case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:// ��ͥ�绰
					p.homeNumber = number;
					break;
				case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:// �����绰
					p.workNumber = number;
					break;
				case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:// �����绰
					p.mobileNumber = number;
					break;
				default:
					break;
				}
			}
			cursor.close();
		}
	}

	/**
	 * ��ȡָ����ϵ�˵�email
	 * 
	 * @param p
	 * @param _id
	 */
	public void setEmailadr(TelPerson p, String _id) {
		Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		Cursor cursor = mContentResolver
				.query(uri,
						new String[] { ContactsContract.CommonDataKinds.Email.ADDRESS, },// �����ʼ���ַ
						ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " =?",// ɸѡ����
						new String[] { _id },// ��������
						null);
		int emailadrIndex = cursor
				.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
		if (null != cursor) {
			while (cursor.moveToNext()) {
				String emailadr = cursor.getString(emailadrIndex);
				p.email = emailadr;
			}
			cursor.close();
		}
	}
}
