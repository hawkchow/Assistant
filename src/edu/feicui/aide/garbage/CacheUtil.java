package edu.feicui.aide.garbage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Environment;

/**
 * �����ṩ��ȡ�����������ļ��ķ���
 * 
 */
public class CacheUtil {
	/**
	 * ������
	 */
	Context mContext;
	private PackageManager mManager;
	/**
	 * ָ�������ļ���С
	 */
	long mLen;
	/**
	 * �ֻ�����������С
	 */
	long phoneCacheLen;
	/**
	 * �ֻ��ڴ�������С
	 */
	long phoneStorageLen;
	/**
	 * SD������������С
	 */
	long SDcardCacheLen;
	/**
	 * SD���ڴ�������С
	 */
	long SDcardStorageLen;
	/**
	 * �����ļ�������С
	 */
	long fileCacheLen;
	/**
	 * ���������ļ���С
	 */
	long allLen;
	OnFlushListener mFlushListener;

	public CacheUtil(Context context) {
		mContext = context;
	}

	/**
	 * ��ȡ��Ӧ���͵�����Դ
	 * 
	 * @param type
	 *            �������ͷֱ��Ӧ
	 * @return
	 */
	public List<ChildInfo> getChild(String type) {
		List<ChildInfo> list = new ArrayList<>();
		mLen = 0;
		mManager = mContext.getPackageManager();
		// ��ȡ�����Ѱ�װ��Ӧ��
		List<PackageInfo> packages = mManager.getInstalledPackages(0);
		for (PackageInfo packageInfo : packages) {
			// ��ȡ����
			String packageName = packageInfo.packageName;

			// ��ȡÿ��Ӧ�õ�������
			Context context = getPackageContext(packageName);
			if (null != context) {
				File file = null;
				// �ֻ���������
				switch (type) {
				case "phoneCache":// �ֻ���������
					file = context.getCacheDir();
					break;
				case "phoneStorage":// �ֻ��ڴ�����
					file = context.getFilesDir();
					break;
				case "SDcardCache":// SD����������
					file = context.getExternalCacheDir();
					break;
				case "SDcardStorage":// SD���ڴ�����
					file = context.getExternalFilesDir(null);
					break;
				case "fileCache":// �����ļ�����
					file = Environment.getDownloadCacheDirectory();
					break;
				default:
					break;
				}
				listFile(file, packageInfo, true, list, type);
			}
		}
		return list;
	}

	/**
	 * �ݹ�����������ļ�
	 * 
	 * @param file
	 * @param icon
	 * @param isFinish
	 * @param list
	 */
	private void listFile(File file, PackageInfo packageInfo, boolean isFinish,
			List<ChildInfo> list, String type) {
		if (null == file || !file.canRead() || file.length() == 0) {
			return;
		}
		if (file.isFile()) {
			// ��ȡ��Ӧ�õ�icon
			Drawable icon = packageInfo.applicationInfo.loadIcon(mManager);
			// ��ȡ��Ӧ�õ�label
			String name = packageInfo.applicationInfo.loadLabel(mManager)
					.toString();
			long size = file.length();
			mLen += size;
			switch (type) {
			case "phoneCache":// �ֻ���������
				phoneCacheLen = mLen;
				break;
			case "phoneStorage":// �ֻ��ڴ�����
				phoneStorageLen = mLen;
				break;
			case "SDcardCache":// SD����������
				SDcardCacheLen = mLen;
				break;
			case "SDcardStorage":// SD���ڴ�����
				SDcardStorageLen = mLen;
				break;
			case "fileCache":// �����ļ�����
				fileCacheLen = mLen;
				break;
			}
			allLen += size;
			ChildInfo info = new ChildInfo(file, false, icon, name, size);
			list.add(info);
			// �ص�����ˢ�¸���Сֵ
			mFlushListener.flush();
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				listFile(files[i], packageInfo, false, list, type);
			}
		}
		// �������
		if (isFinish) {
			return;
		}
	}

	/**
	 * �ص��ӿ�
	 * 
	 */
	public interface OnFlushListener {
		void flush();
	}

	/**
	 * ����ͨ���÷���ʵ�ֻص�
	 */
	public void setOnFlushListener(OnFlushListener flushListener) {
		mFlushListener = flushListener;
	}

	/**
	 * @param packageName
	 *            ����
	 * @return ��Ӧ������������
	 */
	private Context getPackageContext(String packageName) {
		Context packageContext = null;
		try {
			packageContext = mContext.createPackageContext(packageName,
					Context.CONTEXT_IGNORE_SECURITY
							| Context.CONTEXT_INCLUDE_CODE);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return packageContext;
	}
}
