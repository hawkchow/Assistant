package edu.feicui.aide.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;

/**
 * ����������ȡRAM��Ϣ
 * 
 */
public class MemoryUtil {
	/**
	 * ��ȡ�ڴ��С�����
	 */
	static MemoryUtil memUtil;
	/**
	 * ������
	 */
	Context mContext;
	/**
	 * �APP����
	 */
	ActivityManager mActivityManager;

	private MemoryUtil(Context context) {
		this.mContext = context;
		mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	public static MemoryUtil getInstance(Context context) {
		if (memUtil == null) {
			synchronized (MemoryUtil.class) {
				if (memUtil == null) {
					context = context.getApplicationContext();
					memUtil = new MemoryUtil(context);
				}
			}
		}
		return memUtil;
	}

	/**
	 * @return ��ȡ�����ڴ��С(long)<br/>
	 *         ��λΪB
	 */
	public long getAvailMem() {
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		mActivityManager.getMemoryInfo(memoryInfo);
		// ������ݵ�λĬ��Ϊbyte;
		long availMem = memoryInfo.availMem;
		// Log.e("�����ڴ�=", availMem + "");
		return availMem;
	}

	/**
	 * @return ��ȡ���ڴ��С(long)<br/>
	 *         ��λΪB
	 */
	public long getTotalMem() {
		BufferedReader bufferedReader = null;
		long totalMem = 0;
		try {
			bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"));
			// ��ȡ��һ�е�����
			String str1 = bufferedReader.readLine();
			// ��ȡ��һ�γ��ֿո���±�
			int start = str1.indexOf(' ');
			// ��ȡ��һ�γ����ַ�k���±�
			int end = str1.indexOf('k');
			// ��ȡ�ƶ��±�֮�������
			String str2 = str1.substring(start, end);
			// ȥ��ǰ��ո�
			String str3 = str2.trim();
			// ��number��λΪKB;
			long number1 = Long.parseLong(str3);
			// ת����λΪB
			totalMem = number1 * 1024;
			// Log.e("���ڴ�=", totalMem + " ");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return totalMem;
	}

	/**
	 * ��ȡ�����ڴ��С��long��<br/>
	 * ��λΪB
	 * 
	 * @return
	 */
	public long getConsumeMem() {
		return getTotalMem() - getAvailMem();
	}

	/**
	 * @return �������ڴ��ı�����ʾ���ַ���
	 */
	public String getTextMem() {
		long consumeLong = getTotalMem() - getAvailMem();
		String total = Formatter.formatFileSize(mContext, getTotalMem());
		String consume = Formatter.formatFileSize(mContext, consumeLong);
		return String.format("�����ڴ棺 %1$S/%2$S", consume, total);
	}

	/**
	 * 
	 * @return ��ȡ�����ڽ���������ʾ��ֵ
	 */
	public int getProgress() {
		int progress = (int) ((double) (getTotalMem() - getAvailMem())
				/ getTotalMem() * 100);
		return progress;
	}
}
