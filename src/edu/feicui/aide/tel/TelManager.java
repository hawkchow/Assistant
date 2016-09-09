package edu.feicui.aide.tel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import edu.feicui.aide.bean.ContactsInfo;
import edu.feicui.aide.bean.TelClass;
import edu.feicui.aide.bean.TelTableClass;

/**
 * �ṩ�����ݿ����ݵĻ�ȡ����
 * 
 */
public class TelManager {
	/**
	 * ������
	 */
	Context mContext;
	/**
	 * ��ǰ��ʵ��
	 */
	static TelManager sInstance;
	/**
	 * ��Assets��Դ�������
	 */
	AssetManager mAssetManager;
	/**
	 * �ڲ��洢��
	 */
	File dbFile;
	/**
	 * ���ݽ�����,�ɶ������ṩ�߽��в���
	 */
	ContentResolver mContentResolver;

	private TelManager(Context context) {
		mContext = context;
		mAssetManager = context.getAssets();
		mContentResolver = context.getContentResolver();
		// ��Ӧ�ö�Ӧ���ڲ��洢File
		dbFile = new File(context.getFilesDir(), "commonnum.db");
		copyDBFile();
	}

	public static TelManager getInstance(Context context) {
		if (null == sInstance) {
			synchronized (TelManager.class) {
				if (null == sInstance) {
					context = context.getApplicationContext();
					sInstance = new TelManager(context);
				}
			}
		}
		return sInstance;
	}

	/**
	 * ��Assets/db/commonnum.db�ļ��е����ݸ��Ƶ�dbfile��<br/>
	 * dbfileΪ��Ӧ�ö�Ӧ���ڲ��洢file
	 * 
	 */
	public void copyDBFile() {
		InputStream in = null;
		OutputStream out = null;
		try {
			// �����������ڶ�ȡDB����
			in = mAssetManager.open("db/commonnum.db");
			// �����������д����
			out = new FileOutputStream(dbFile);
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ȡclasslist���е�����,����TelMgrActivity
	 * 
	 * @return
	 */
	public List<TelClass> getClassList() {
		List<TelClass> list = new ArrayList<>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
		String sql = "select * from classlist";
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			String name = c.getString(c.getColumnIndex("name"));
			int idx = c.getInt(c.getColumnIndex("idx"));
			TelClass t = new TelClass(name, idx);
			list.add(t);
		}
		c.close();
		db.close();
		return list;
	}

	/**
	 * ��ȡ�����е����ݣ���Ӧ������Ŀ��<br/>
	 * ���ṩ��Ӧ����Ϊ����
	 * 
	 * @return
	 */
	public List<TelTableClass> getTableList(String tableName) {
		List<TelTableClass> list = new ArrayList<>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
		String sql = "select * from " + tableName;
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			int _id = c.getInt(c.getColumnIndex("_id"));
			String number = c.getString(c.getColumnIndex("number"));
			String name = c.getString(c.getColumnIndex("name"));
			TelTableClass t = new TelTableClass(_id, number, name);
			list.add(t);
		}
		c.close();
		db.close();
		return list;
	}

	/**
	 * ��ȡͨѶ��¼��Ϣ
	 * 
	 * @return
	 */
	public List<ContactsInfo> getContactsInfo() {
		List<ContactsInfo> list = new ArrayList<>();
		Uri uri = CallLog.Calls.CONTENT_URI;
		Cursor cursor = mContentResolver.query(uri, null, null, null, null);

		int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
		int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
		int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				// ͨѶʱ���
				long time = cursor.getLong(dateIndex);
				Date date = new Date(time);
				String timeStr = dateFormat.format(date);
				// ͨѶ����
				int type = cursor.getInt(typeIndex);
				// �绰����
				String number = cursor.getString(numberIndex);
				// ͨѶʱ��
				long duration = cursor.getLong(durationIndex);
				long sec = duration % 60;// ��
				long min = duration / 60 % 60;// ��
				long hour = duration / 60 / 60 % 24;// ʱ
				String durationStr = String.format("ʱ����%1$02dʱ%2$02d��%3$02d��",
						hour, min, sec);
				// ͨѶ��
				String name = "İ����";
				Cursor cur = mContentResolver.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.NUMBER
								+ " = ?", new String[] { number }, null);
				if (cur.moveToFirst()) {
					name = cur
							.getString(cur
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				}

				// ����Ϣ��װ��ContactsInfo����
				ContactsInfo contact = new ContactsInfo(number, name,
						durationStr, timeStr, type);
				list.add(contact);
				// Log.e("tag", contact.toString());
				cur.close();
			}
		}
		cursor.close();
		return list;
	}
}
