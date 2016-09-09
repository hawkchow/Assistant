package edu.feicui.aide.check;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.DisplayMetrics;
import edu.feicui.aide.util.FileUtil;

public class CheckUtil {
	static CheckUtil mCheckInfo;
	/**
	 * ������
	 */
	Context mContext;

	private CheckUtil(Context context) {
		mContext = context;
	}

	public static CheckUtil getInstance(Context context) {
		if (null == mCheckInfo) {
			synchronized (CheckUtil.class) {
				if (null == mCheckInfo) {
					context = context.getApplicationContext();
					mCheckInfo = new CheckUtil(context);
				}
			}
		}
		return mCheckInfo;
	}

	/**
	 * ��ȡCPU��Ϣ
	 * 
	 * @return
	 */
	public String getCpuInfo() {
		// ��ȡCPU��Ϣ��·��Ϊ"/proc/cpuinfo"
		String path = "/proc/cpuinfo";
		FileUtil fileUtil = new FileUtil();
		// ��ȡ����CPU��Ϣ
		String cpuinfo = fileUtil.readFile(path);
		// Log.e("tag", cpuinfo);
		return cpuinfo;
	}

	/**
	 * ��ȡCPU����
	 * 
	 * @return
	 */
	public String getCpuName() {
		String str = getCpuInfo();
		int start = str.indexOf(" ");
		int end = str.indexOf("\n");
		String cpuName = str.substring(start, end).trim();

		return cpuName;
	}

	/**
	 * �÷�����ȡcpu����
	 * 
	 * @return
	 */
	public int getCpuNum() {
		String pathname = "/sys/devices/system/cpu";
		File file = new File(pathname);
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				boolean b = Pattern.matches("cpu[0-9]", filename);
				return b;
			}
		};
		File[] arrs = file.listFiles(filter);
		return arrs.length;
	}

	/**
	 * ��ȡ�ֻ���Ļ�ֱ���
	 * 
	 * @return
	 */
	public String getPhoneMetrics() {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;
		String str = String
				.format("�ֻ��ֱ���:%1$d*%2$d", widthPixels, heightPixels);
		return str;
	}

	/**
	 * ��ȡ������ֱ���
	 * 
	 * @return
	 */
	public String getCameraMetrics() {
		Camera camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		camera.release();
		List<Size> list = parameters.getSupportedPreviewSizes();
		// ����ֱ�������±�Ϊ0;
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			int height = list.get(i).height;
			int width = list.get(i).width;
			// Log.e("tag", width + "*" + height);
			if (list.get(index).width * list.get(index).height < width * height) {
				index = i;
			}
		}
		// ��ȡ���ֱ���
		String str = String.format("����ֱ��ʣ�%1$d*%2$d", list.get(index).width,
				list.get(index).height);
		return str;
	}

	/**
	 * ��ȡ�����汾
	 * 
	 * @return
	 */
	public String getBasebandVersion() {
		String version = Build.getRadioVersion();
		return null != version ? version : "unknow";
	}

	/**
	 * �ж��Ƿ�Root
	 * 
	 * @return ���ǡ� ��ʾ��Root; ���� ��ʾδRoot
	 */
	public String isRoot() {
		String[] sus = { "/system/bin/su", "/system/sbin/su", "/system/xbin/su" };
		boolean isRoot = false;
		for (String str : sus) {
			File file = new File(str);
			if (file.exists()) {
				isRoot = true;
				break;
			}
		}
		return isRoot ? "��" : "��";
	}
}
