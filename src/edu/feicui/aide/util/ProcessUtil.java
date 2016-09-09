package edu.feicui.aide.util;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.text.format.Formatter;
import edu.feicui.aide.R;
import edu.feicui.aide.accelerate.ProcessInfo;
import edu.feicui.aide.soft.SoftInfo;

/**
 * �������������
 * 
 */
public class ProcessUtil {
	/**
	 * ������
	 */
	private PackageManager mManager;
	/**
	 * ������
	 */
	Context mContext;
	private static ProcessUtil mProgressUtil;

	/**
	 * �APP����
	 */
	public ActivityManager mActivityManager;

	private ProcessUtil(Context context) {
		this.mContext = context;
		mManager = context.getPackageManager();
		mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	public static ProcessUtil getInstance(Context context) {
		if (mProgressUtil == null) {
			synchronized (ProcessUtil.class) {
				if (mProgressUtil == null) {
					context = context.getApplicationContext();
					mProgressUtil = new ProcessUtil(context);
				}
			}
		}
		return mProgressUtil;
	}

	/**
	 * ��ȡ�������Ϣ��ص�����Դ
	 * 
	 * @param value
	 *            0 ��Ӧ���������Ϣ<br/>
	 *            1��Ӧϵͳ�����Ϣ<br/>
	 *            2��Ӧ�û������Ϣ
	 * @return
	 */
	public List<SoftInfo> getSoftInfo(int value) {
		List<SoftInfo> list = new ArrayList<>();
		List<PackageInfo> data = new ArrayList<PackageInfo>();
		switch (value) {
		case 0:// �����Ѱ�װ����Դ
			data = mManager.getInstalledPackages(0);
			break;
		case 1:// ϵͳ�������Դ
			data = getSystemAPP();
			break;
		case 2:// �û��������Դ
			data = getUserAPP();
			break;
		}
		for (int i = 0; i < data.size(); i++) {
			Drawable icon = data.get(i).applicationInfo.loadIcon(mManager);
			String label = data.get(i).applicationInfo.loadLabel(mManager)
					.toString();
			String packageName = data.get(i).packageName;
			String versionName = data.get(i).versionName;
			SoftInfo softInfo = new SoftInfo(false, icon, label, packageName,
					versionName);
			list.add(softInfo);
		}
		// Log.e("all=", list.size() + "");
		return list;
	}

	/**
	 * @return ���������û��Ѱ�װAPP(PackageInfo)
	 */
	public List<PackageInfo> getUserAPP() {
		List<PackageInfo> all = mManager.getInstalledPackages(0);
		// �ü������ڴ洢�û�APP
		List<PackageInfo> list = new ArrayList<>();
		for (int i = 0; i < all.size(); i++) {
			PackageInfo info = all.get(i);
			ApplicationInfo applicationInfo = info.applicationInfo;
			if (!((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)) {
				list.add(info);
			}
		}
		// Log.e("all=", list.size() + "");
		return list;
	}

	/**
	 * @return ��������ϵͳ��APP(PackageInfo)
	 */
	public List<PackageInfo> getSystemAPP() {
		List<PackageInfo> all = mManager.getInstalledPackages(0);
		// �ü������ڴ洢�û�APP
		List<PackageInfo> list = new ArrayList<>();
		for (int i = 0; i < all.size(); i++) {
			PackageInfo info = all.get(i);
			ApplicationInfo applicationInfo = info.applicationInfo;
			if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				list.add(info);
			}
		}
		return list;
	}

	/**
	 * �����е�ϵͳ����Դ <br/>
	 * 
	 * @return�����е�ϵͳAPP
	 */
	public List<ProcessInfo> getSystemRunningAppInfo() {
		List<ProcessInfo> list = new ArrayList<>();
		List<PackageInfo> allSystem = getSystemAPP();
		List<RunningAppProcessInfo> appPrecessInfos = mActivityManager
				.getRunningAppProcesses();
		for (int i = 0; i < appPrecessInfos.size(); i++) {
			// �������е�Ӧ�ý�����
			RunningAppProcessInfo appProcessinfo = appPrecessInfos.get(i);
			String processName1 = appProcessinfo.processName;
			for (int j = 0; j < allSystem.size(); j++) {
				// �Ѱ�װ��ϵͳ���������
				PackageInfo packageInfo = allSystem.get(j);
				String processName2 = packageInfo.applicationInfo.processName;
				if (processName1.equals(processName2)) {
					// ͼƬ
					Drawable img = packageInfo.applicationInfo
							.loadIcon(mManager);
					// Ӧ����
					String label = packageInfo.applicationInfo.loadLabel(
							mManager).toString();
					// ��ȡ����ռ�õ��ڴ�
					int[] pids = new int[] { appProcessinfo.pid };
					Debug.MemoryInfo[] memoryInfo = mActivityManager
							.getProcessMemoryInfo(pids);
					int memory1 = memoryInfo[0].dalvikPrivateDirty;
					// �ı䵥λ kb ת��ΪMb
					String memory2 = Formatter
							.formatFileSize(mContext, memory1);
					String memory = String.format("�ڴ棺%1$S", memory2);
					ProcessInfo info = new ProcessInfo(false, img, label,
							memory, "ϵͳ����");
					// Log.e("tag", info.toString());
					// ���APP������д������Դ
					info.packageName = packageInfo.packageName;
					list.add(info);
				}
			}
		}
		return list;

	}

	/**
	 * �����е�APP�û�����Դ <br/>
	 * 
	 * @return ��ȡ�������е��û�APP
	 */
	public List<ProcessInfo> getUserRunningAppInfo() {
		List<ProcessInfo> list = new ArrayList<>();
		List<PackageInfo> allUser = getUserAPP();
		List<RunningAppProcessInfo> appPrecessInfos = mActivityManager
				.getRunningAppProcesses();

		for (int i = 0; i < appPrecessInfos.size(); i++) {
			// �������е�Ӧ�ý�����
			RunningAppProcessInfo appProcessinfo = appPrecessInfos.get(i);
			String processName1 = appProcessinfo.processName;
			for (int j = 0; j < allUser.size(); j++) {
				// �Ѱ�װ���û����������
				PackageInfo packageInfo = allUser.get(j);
				String processName2 = packageInfo.applicationInfo.processName;
				if (processName1.equals(processName2)) {
					// ͼƬ
					Drawable img = packageInfo.applicationInfo
							.loadIcon(mManager);
					// Ӧ����
					String label = packageInfo.applicationInfo.loadLabel(
							mManager).toString();
					// ��ȡ�ý���ռ�õ��ڴ�
					int[] pids = new int[] { appProcessinfo.pid };
					Debug.MemoryInfo[] memoryInfo = mActivityManager
							.getProcessMemoryInfo(pids);
					int memory1 = memoryInfo[0].dalvikPrivateDirty;
					// �ı䵥λ kb ת��ΪMb
					String memory2 = Formatter
							.formatFileSize(mContext, memory1);
					// ʹ����Դresourcesȥ��Ӳ����
					String memory = String.format(mContext.getResources()
							.getString(R.string.processinfo_memory), memory2);
					ProcessInfo info = new ProcessInfo(false, img, label,
							memory, "�û�����");
					// Log.e("tag", info.toString());

					// ���APP������д������Դ
					info.packageName = packageInfo.packageName;
					list.add(info);
				}
			}
		}
		return list;
	}
}
