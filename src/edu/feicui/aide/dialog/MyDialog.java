package edu.feicui.aide.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.feicui.aide.R;

public class MyDialog extends Dialog implements
		android.view.View.OnClickListener {
	Button mBtnYes;
	Button mBtnNo;
	OnMyDialogListener mDialogListener;

	public MyDialog(Context context) {
		this(context, 0);
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_my);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		// ���ȷ�ϰ�ť
		mBtnYes = (Button) findViewById(R.id.btn_yes);
		mBtnYes.setOnClickListener(this);
		// ���ȡ����ť
		mBtnNo = (Button) findViewById(R.id.btn_no);
		mBtnNo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_yes:// ����Ի���ȷ�ϰ�ťʱ�Ĳ���
			mDialogListener.onClickYesListener();
			break;
		case R.id.btn_no:// ����Ի���ȡ����ťʱ�Ĳ���
			mDialogListener.onClickNoListener();
			break;
		}
	}

	/**
	 * �Զ���Ի���Ļص��ӿ�
	 *
	 */
	public interface OnMyDialogListener {
		/**
		 * �÷������û����ȷ��ʱִ��
		 */
		void onClickYesListener();

		/**
		 * �÷������û����ȡ��ʱִ��
		 */
		void onClickNoListener();
	}

	/**
	 * �÷�������ʵ�����Ի������
	 */
	public void setOnMyDiaLogListener(OnMyDialogListener mDialogListener) {
		this.mDialogListener = mDialogListener;
	}
}
