package edu.feicui.aide.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.os.Environment;

/**
 * �й��ļ��Ĳ���
 * 
 */
public class FileUtil {
	/**
	 * sdcard Ŀ¼
	 */
	public final static File FILE_SDCARD;
	/**
	 * Ӧ�ø�Ŀ¼
	 */
	public final static File FILE_APPROOT;
	/**
	 * Ӧ����־Ŀ¼
	 */
	public final static File FILE_LOG;
	static {
		FILE_SDCARD = getSDPath();
		FILE_APPROOT = new File(FILE_SDCARD, "aide");
		FILE_LOG = new File(FILE_APPROOT, "Log");
		try {
			if (!FILE_LOG.exists()) {
				FILE_LOG.mkdirs();
			}
		} catch (Exception e) {
			// ��������
		}
	}

	/**
	 * ��ȡSDcard·��
	 * 
	 * @return
	 */
	public static File getSDPath() {
		File sdDir = null;
		// �ж�sdcard�Ƿ����
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		return sdDir;
	}

	/**
	 * ��ȡָ��·�����ļ�
	 * 
	 * @param filename
	 *            file·��
	 * @return
	 */
	public String readFile(String filename) {
		StringBuffer sb = new StringBuffer();
		String str = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			while ((str = reader.readLine()) != null) {
				sb.append(str);
				sb.append("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
