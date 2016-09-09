package edu.feicui.aide.util;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

/**
 * ��������
 *
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
	/**
	 * ������
	 */
	private Context mContext;
	/**
	 * CrashHandler����
	 */
	private static CrashHandler mInstance;
	/**
	 * ϵͳ����
	 */
	private Thread.UncaughtExceptionHandler mExceptionHandler;
	/**
	 * ʱ���ʽ
	 */
	private SimpleDateFormat mFormat;

	private CrashHandler() {
		mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		mFormat = new SimpleDateFormat("yyyy��MM��dd��  HHʱmm��ss��");
	}

	/**
	 * ���һ��CrashHandler�ĵ�������
	 */
	public static CrashHandler getInstance() {
		if (null == mInstance) {
			synchronized (CrashHandler.class) {
				if (null == mInstance) {
					mInstance = new CrashHandler();
				}
			}
		}
		return mInstance;
	}

	public void init(Context context) {
		mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * �������߳��������δ������쳣����ֹʱ�����ø÷���
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			// ���쳣��Ϣд��SDcard��
			String name = getCurrentTime() + ".txt";
			File file = new File(FileUtil.FILE_LOG, name);
			// ����Ŀ���ļ�

			PrintWriter err = new PrintWriter(file);
			do {
				ex.printStackTrace(err);
			} while (ex.getCause() != null);
			err.flush();
			err.close();
			// ����ϵͳԭ�д���ʽ
			if (null != mExceptionHandler) {
				mExceptionHandler.uncaughtException(thread, ex);
			}
		} catch (Exception e) {
			// ��������
		}
	}

	/**
	 * ��ȡϵͳ��ǰʱ��
	 * 
	 * @return
	 */
	public String getCurrentTime() {
		Date date = new Date();
		String time = mFormat.format(date);
		return time;

	}
}
