package edu.feicui.aide.bean;

/**
 * ͨѶ��ȫJavaBean
 *
 */
public class TelClass {
	public String name;
	public int idx;

	public TelClass(String name, int idx) {
		super();
		this.name = name;
		this.idx = idx;
	}

	@Override
	public String toString() {
		return "TelClass [name=" + name + ", idx=" + idx + "]";
	}

}
