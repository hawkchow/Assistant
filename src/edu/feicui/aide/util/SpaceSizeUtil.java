package edu.feicui.aide.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * ��ȡROM �� SDcard ��Ϣ
 * 
 */
public class SpaceSizeUtil {
	Context mContext;
	static SpaceSizeUtil mSpaceSizeUtil;

	private SpaceSizeUtil(Context context) {
		mContext = context;
	}

	public static SpaceSizeUtil getInstace(Context context) {
		if (null == mSpaceSizeUtil) {
			synchronized (SpaceSizeUtil.class) {
				if (null == mSpaceSizeUtil) {
					context = context.getApplicationContext();
					mSpaceSizeUtil = new SpaceSizeUtil(context);
				}
			}
		}
		return mSpaceSizeUtil;
	}

	/**
	 * ���SD���ܴ�С(long)
	 * 
	 * @return
	 */
	public long getSDTotalSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long totalBlocks = stat.getBlockCountLong();
		long sdTotal = blockSize * totalBlocks;
		return sdTotal;
	}

	/**
	 * ���SD���ܴ�С��String��
	 * 
	 * @return
	 */
	public String getSDTotalSizeString() {
		long sdTotal = getSDTotalSize();
		String total = Formatter.formatFileSize(mContext, sdTotal);
		return total;
	}

	/**
	 * ���sd��ʣ�������������ô�С(long)
	 * 
	 * @return
	 */
	public long getSDAvailableSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getAvailableBlocksLong();
		long sdAvail = blockSize * availableBlocks;
		return sdAvail;

	}

	/**
	 * ��ȡSDcard�����ڴ�ֵ(String)
	 * 
	 * @return
	 */
	public String getSDConsumeSizeString() {
		long avail = getSDAvailableSize();
		long total = getSDTotalSize();
		long consume = total - avail;
		String str = Formatter.formatFileSize(mContext, consume);
		return str;
	}

	/**
	 * ��û����ڴ��ܴ�С(long)
	 * 
	 * @return
	 */
	public long getRomTotalSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long totalBlocks = stat.getBlockCountLong();
		long romTotal = blockSize * totalBlocks;
		return romTotal;
	}

	/**
	 * ���ROM�ܴ�С��String��
	 * 
	 * @return
	 */
	public String getRomTotalSizeString() {
		long romTotal = getRomTotalSize();
		String total = Formatter.formatFileSize(mContext, romTotal);
		return total;
	}

	/**
	 * ��û�������ڴ�(long)
	 * 
	 * @return
	 */
	public long getRomAvailableSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getAvailableBlocksLong();
		long romAvail = blockSize * availableBlocks;
		return romAvail;
	}

	/**
	 * ��ȡ�ֻ�ROM�����ڴ�(String)
	 * 
	 * @return
	 */
	public String getRomConsumeSizeString() {
		long avail = getRomAvailableSize();
		long total = getRomTotalSize();
		long consume = total - avail;
		String str = Formatter.formatFileSize(mContext, consume);
		return str;
	}

	/**
	 * ����ֻ�ROM�ڽ������е�ֵ
	 * 
	 * @return
	 */
	public int getInternalInt() {
		long avail = getRomAvailableSize();
		long total = getRomTotalSize();
		long consume = total - avail;
		int num = (int) ((double) consume / total * 100);
		return num;
	}

	/**
	 * ���SDcard�ڽ���������ʾ��ֵ
	 * 
	 * @return
	 */
	public int getStorageInt() {
		long avail = getSDAvailableSize();
		long total = getSDTotalSize();
		long consume = total - avail;
		int num = (int) ((double) consume / total * 100);
		return num;
	}
}
