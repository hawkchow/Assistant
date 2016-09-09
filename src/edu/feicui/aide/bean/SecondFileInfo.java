package edu.feicui.aide.bean;

import java.io.File;

public class SecondFileInfo {
	/**
	 * Chb��״̬
	 */
	public boolean state;
	/**
	 * ��׺
	 */
	public String suffix;
	/**
	 * �ļ���
	 */
	public String name;
	/**
	 * ����޸�ʱ��
	 */
	public long time;
	/**
	 * �ļ���С
	 */
	public long size;
	/**
	 * ���ļ�����
	 */
	public File file;

	public SecondFileInfo() {

	}

	public SecondFileInfo(boolean state, String suffix, String name, long time,
			long size, File file) {
		super();
		this.state = state;
		this.suffix = suffix;
		this.name = name;
		this.time = time;
		this.size = size;
		this.file = file;
	}

	@Override
	public String toString() {
		return "SecondFileInfo [state=" + state + ", suffix=" + suffix
				+ ", name=" + name + ", time=" + time + ", size=" + size
				+ ", file=" + file + "]";
	}

}
