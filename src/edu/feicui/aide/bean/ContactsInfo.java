package edu.feicui.aide.bean;

/**
 * �������ڷ�װ����ͨѶ��¼
 * 
 */
public class ContactsInfo {
	public String number;
	public String name;
	public String duration;
	public String time;
	public int type;

	public ContactsInfo(String number, String name, String duration,
			String time, int type) {
		super();
		this.number = number;
		this.name = name;
		this.duration = duration;
		this.time = time;
		this.type = type;
	}

	@Override
	public String toString() {
		return "ContactsInfo [number=" + number + ", name=" + name
				+ ", duration=" + duration + ", time=" + time + ", type="
				+ type + "]";
	}

}
