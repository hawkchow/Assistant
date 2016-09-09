package edu.feicui.aide.filemgr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import edu.feicui.aide.bean.FileInfo;
import edu.feicui.aide.bean.SecondFileInfo;

/**
 * �����ṩ��ȡ�ļ�������������Դ�ķ���
 * 
 */
public class FileMgrUtil {

	/**
	 * �ڶ���������Դ(ȫ���������޺�׺�ļ�)
	 */
	List<SecondFileInfo> secAllFile;// �����ļ��������޺�׺���ļ���
	/**
	 * �ڶ���������Դ���ĵ���
	 */
	List<SecondFileInfo> secTxtFile;// �ĵ�����Դ
	/**
	 * �ڶ���������Դ����Ƶ��
	 */
	List<SecondFileInfo> secAudioFile;// ��Ƶ
	/**
	 * �ڶ���������Դ����Ƶ��
	 */
	List<SecondFileInfo> secVideoFile;// ��Ƶ
	/**
	 * �ڶ���������Դ��ͼƬ��
	 */
	List<SecondFileInfo> secImgFile;// ͼƬ
	/**
	 * �ڶ���������Դ��ѹ������
	 */
	List<SecondFileInfo> secZipFile;// ѹ����
	/**
	 * �ڶ���������Դ����װ����
	 */
	List<SecondFileInfo> secApkFile;// ��װ��
	/**
	 * �ڶ���������Դ���޺�׺�ļ���
	 */
	List<SecondFileInfo> secSpacilFile;// �޺�׺�����ļ�
	/**
	 * ��һ��������Դ(��װ�������ļ���Ϣ)
	 */
	List<FileInfo> mList;
	/**
	 * FileInfo�ı�ʾȫ���Ķ���
	 */
	FileInfo allFile;// �����ļ�
	/**
	 * FileInfo���ʾ�ĵ��Ķ���
	 */
	FileInfo txtFile;// �ĵ�
	/**
	 * FileInfo���ʾ�ĵ��Ķ���
	 */
	FileInfo audioFile;// ��Ƶ
	/**
	 * FileInfo���ʾ��Ƶ�Ķ���
	 */
	FileInfo videoFile;// ��Ƶ
	/**
	 * FileInfo���ʾͼƬ�Ķ���
	 */
	FileInfo imageFile;// ͼƬ
	/**
	 * FileInfo���ʾѹ�����Ķ���
	 */
	FileInfo zipFile;// ѹ����
	/**
	 * FileInfo���ʾ��װ���Ķ���
	 */
	FileInfo apkFile;// ��װ��
	/**
	 * �����ļ���С
	 */
	long allLen;
	/**
	 * �ĵ���С
	 */
	long txtLen;
	/**
	 * ��Ƶ��С
	 */
	long audioLen;
	/**
	 * ��Ƶ��С
	 */
	long videoLen;
	/**
	 * ͼƬ��С
	 */
	long imageLen;
	/**
	 * ѹ������С
	 */
	long zipLen;
	/**
	 * ��װ����С
	 */
	long apkLen;
	/**
	 * �ص��ӿڶ���
	 */
	onFlushListener mFlushListener;
	private static FileMgrUtil mFileMgrUtil;

	private FileMgrUtil() {
		init();
	}

	/**
	 * @return ��ȡFileMgrUtil��һ������
	 */
	public static FileMgrUtil getInstance() {
		if (null == mFileMgrUtil) {
			synchronized (FileMgrUtil.class) {
				if (null == mFileMgrUtil) {
					mFileMgrUtil = new FileMgrUtil();
				}
			}
		}
		return mFileMgrUtil;
	}

	/**
	 * ��ʼ������Դ�������ø�����Դ����
	 */
	void init() {
		mList = new ArrayList<>();
		allFile = new FileInfo();
		allFile.setFileType("ȫ��");
		mList.add(allFile);

		txtFile = new FileInfo();
		txtFile.setFileType("�ĵ�");
		mList.add(txtFile);

		audioFile = new FileInfo();
		audioFile.setFileType("��Ƶ");
		mList.add(audioFile);

		videoFile = new FileInfo();
		videoFile.setFileType("��Ƶ");
		mList.add(videoFile);

		imageFile = new FileInfo();
		imageFile.setFileType("ͼƬ");
		mList.add(imageFile);

		zipFile = new FileInfo();
		zipFile.setFileType("ѹ����");
		mList.add(zipFile);

		apkFile = new FileInfo();
		apkFile.setFileType("��װ��");
		mList.add(apkFile);

		// ��ת�������Դ��ʼ��
		secAllFile = new ArrayList<>();
		secTxtFile = new ArrayList<>();
		secAudioFile = new ArrayList<>();
		secVideoFile = new ArrayList<>();
		secImgFile = new ArrayList<>();
		secZipFile = new ArrayList<>();
		secApkFile = new ArrayList<>();
		secSpacilFile = new ArrayList<>();
	}

	/**
	 * ����FileInfo����Դ��Ϣ
	 * 
	 * @return
	 */
	public void SetFileInfo() {
		File file = getSDPath();
		listFile(file, true);
	}

	/**
	 * ��ȡSDcard·��
	 * 
	 * @return
	 */
	public File getSDPath() {
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
	 * ����ָ���ļ�Ŀ¼<br/>
	 * ��Ӧ��Ϣ��װ��javabean��<br/>
	 * �ݹ�һ�ε���һ��ˢ�½��淽��
	 * 
	 * @param file
	 * @return
	 */
	public void listFile(File file, boolean isFinish) {
		// �ж�file�Ƿ���Ч
		if (!file.exists() || !file.canRead() || file.length() == 0) {
			return;
		}
		if (file.isFile()) {// ������ļ�
			allLen += file.length();
			allFile.setFileLen(allLen);
			// �ļ���
			String fileName = file.getName();
			// û�к�׺���ļ����Ᵽ��
			if (fileName.indexOf(".") == -1) {
				long time = file.lastModified();
				long size = file.length();
				SecondFileInfo info1 = new SecondFileInfo(false, "δ֪",
						fileName, time, size, file);
				secSpacilFile.add(info1);
				return;
			}
			// ��װ������Ŀ��Ϣ(�к�׺)
			setFile(file);
			// ˢ�½���(ʹ�ûص�)
			mFlushListener.onFlush();
			return;
		} else {// ������ļ���
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				listFile(files[i], false);
			}
		}
		// ��ȡ��Ϻ��˳��ݹ�
		if (isFinish) {
			// �������ļ��м����޺�׺���ļ�
			secAllFile.addAll(secSpacilFile);
			// ����FileInfo��״̬���Ե�ֵΪtrue
			setState();
			return;
		}
	}

	/**
	 * �жϲ����ļ������Ͳ���ȡ���С
	 * 
	 * @param file
	 *            ��ǰ�ļ�
	 */
	public void setFile(File file) {
		// �ļ���
		String fileName = file.getName();
		String[] strs = fileName.split("\\.");
		// ��ȡ�ļ���׺(��Ϊicon)
		String suffix = strs[1];
		// �ļ�����޸�ʱ��
		long time = file.lastModified();
		// �ļ���С
		long size = file.length();
		SecondFileInfo info = new SecondFileInfo(false, suffix, fileName, time,
				size, file);
		secAllFile.add(info);
		// ÿ�ж�һ�κ��������С����ˢ���Ի�ö�̬Ч��
		if (isTextFile(suffix)) {// �ĵ�
			txtLen += file.length();
			txtFile.setFileLen(txtLen);
			secTxtFile.add(info);
		} else if (isAudioFile(suffix)) {// ��Ƶ
			audioLen += file.length();
			audioFile.setFileLen(audioLen);
			secAudioFile.add(info);
		} else if (isImageFile(suffix)) {// ͼƬ
			imageLen += file.length();
			imageFile.setFileLen(imageLen);
			secImgFile.add(info);
		} else if (isProgramFile(suffix)) {// ��װ��
			apkLen += file.length();
			apkFile.setFileLen(apkLen);
			secApkFile.add(info);
		} else if (isVideoFile(suffix)) {// ��Ƶ
			videoLen += file.length();
			videoFile.setFileLen(videoLen);
			secVideoFile.add(info);
		} else if (isZipFile(suffix)) {// ѹ����
			zipLen += file.length();
			zipFile.setFileLen(zipLen);
			secZipFile.add(info);
		}
	}

	/**
	 * �ļ�ȫ����ȡ��Ϻ���������Դ��״̬Ϊtrue;
	 */
	public void setState() {
		allFile.setFinish(true);
		txtFile.setFinish(true);
		apkFile.setFinish(true);
		videoFile.setFinish(true);
		zipFile.setFinish(true);
		imageFile.setFinish(true);
		audioFile.setFinish(true);
	}

	/**
	 * �����ļ�����׺�жϸ��ļ��Ƿ�Ϊ�ĵ����ļ�
	 * 
	 * @param suffix
	 *            �ļ����ĺ�׺
	 * 
	 * @return ������ĵ����ļ�����true�����򷵻�false
	 */
	public boolean isTextFile(String suffix) {
		String[] str = { "txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
				"pdf", "c", "h", "cpp", "hpp", "java", "js", "html", "xml",
				"xhtml", "css" };
		for (String string : str) {
			if (suffix.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ļ�����׺�жϸ��ļ��Ƿ�Ϊ��Ƶ���ļ�
	 * 
	 * @param suffix
	 *            �ļ����ĺ�׺
	 * 
	 * @return �������Ƶ���ļ�����true�����򷵻�false
	 */
	public boolean isVideoFile(String suffix) {
		String[] str = { "avi", "mp4", "rm", "rmvb", "3gp", "flash" };
		for (String string : str) {
			if (suffix.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ļ�����׺�жϸ��ļ��Ƿ�Ϊ��Ƶ���ļ�
	 * 
	 * @param suffix
	 *            �ļ����ĺ�׺
	 * 
	 * @return �������Ƶ���ļ�����true�����򷵻�false
	 */
	public boolean isAudioFile(String suffix) {
		String[] str = { "mp3", "wav", "wma", };
		for (String string : str) {
			if (suffix.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ļ�����׺�жϸ��ļ��Ƿ�Ϊͼ�����ļ�
	 * 
	 * @param suffix
	 *            �ļ����ĺ�׺
	 * 
	 * @return �����ͼ�����ļ�����true�����򷵻�false
	 */
	public boolean isImageFile(String suffix) {
		String[] str = { "bmp", "jpg", "gif", "png", "jpeg", "ico", "tiff",
				"xcf" };
		for (String string : str) {
			if (suffix.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ļ�����׺�жϸ��ļ��Ƿ�Ϊѹ���ļ�
	 * 
	 * @param suffix
	 *            �ļ����ĺ�׺
	 * 
	 * @return �����ѹ���ļ�����true�����򷵻�false
	 */
	public boolean isZipFile(String suffix) {
		String[] str = { "zip", "rar", "gz", "tar" };
		for (String string : str) {
			if (suffix.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ļ�����׺�жϸ��ļ��Ƿ�Ϊ������ļ�
	 * 
	 * @param suffix
	 *            �ļ����ĺ�׺
	 * 
	 * @return ����ǳ�����ļ�����true�����򷵻�false
	 */
	public boolean isProgramFile(String suffix) {
		String[] str = { "apk" };
		for (String string : str) {
			if (suffix.equals(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �������ʵ�ָ÷�����ʵ���¼��ļ���
	 */
	public void setOnFlushListener(onFlushListener flushListener) {
		mFlushListener = flushListener;
	}

	/**
	 * �ص��ӿ�<br/>
	 * onFlush() ��������ʵ�־������
	 * 
	 */
	public interface onFlushListener {
		void onFlush();
	}
}
