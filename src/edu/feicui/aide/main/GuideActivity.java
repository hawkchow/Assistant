package edu.feicui.aide.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import edu.feicui.aide.R;

public class GuideActivity extends BaseActivity implements OnPageChangeListener {
	final static String SPREFERENCES_GUIDE = "preferences";
	/**
	 * ViewPager ���򻬶�
	 */
	ViewPager mVpgGuide;

	/**
	 * ����Դ
	 */
	ImageView[] mArrs;

	ImageView mImg1;
	ImageView mImg2;
	ImageView mImg3;
	TextView mTxtJump;
	/**
	 * ��������ʾ��ͼ��
	 */
	ImageView[] mImgView;
	/**
	 * SharedPreferences
	 */
	SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hasGuide();
		init();
		setContentView(R.layout.activity_guide);
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		// ViewPager
		mVpgGuide = (ViewPager) findViewById(R.id.vp_test);
		MyAdapter adapter = new MyAdapter();
		mVpgGuide.setAdapter(adapter);
		mVpgGuide.setOnPageChangeListener(this);
		mVpgGuide.setCurrentItem(0);
		// ������ť�ؼ�ʵ����
		mImg1 = (ImageView) findViewById(R.id.img1_guide);
		mImgView[0] = mImg1;
		mImg2 = (ImageView) findViewById(R.id.img2_guide);
		mImgView[1] = mImg2;
		mImg3 = (ImageView) findViewById(R.id.img3_guide);
		mImgView[2] = mImg3;
		setImgLight(0);
		// TextView
		mTxtJump = (TextView) findViewById(R.id.txt_jump);
		mTxtJump.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoActivity(GuideActivity.this, SplashActivity1.class);
				finish();
			}
		});
	}

	/**
	 * ��ʼ������
	 */

	void init() {
		mArrs = new ImageView[3];
		mArrs[0] = new ImageView(this);
		mArrs[0].setBackgroundResource(R.drawable.adware_style_applist);

		mArrs[1] = new ImageView(this);
		mArrs[1].setBackgroundResource(R.drawable.adware_style_banner);

		mArrs[2] = new ImageView(this);
		mArrs[2].setBackgroundResource(R.drawable.adware_style_creditswall);

		mImgView = new ImageView[3];
	}

	/**
	 * �Ƿ���Ҫ����ҳ�ķ���
	 */
	private void hasGuide() {
		mPreferences = getSharedPreferences(SPREFERENCES_GUIDE,
				Context.MODE_PRIVATE);
		if (isFirst()) {
			// д������
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putBoolean("isFirst", false);
			editor.commit();
		} else {
			gotoActivity(this, SplashActivity1.class);
			finish();
		}
	}

	/**
	 * ��SharedPreferences��ȡ����
	 * 
	 * @return ���õ�һ�ν���ʱ ����true
	 */
	public boolean isFirst() {
		boolean judge = mPreferences.getBoolean("isFirst", true);
		return judge;
	}

	/**
	 * ��������ʾ��ͼ���Ƿ����
	 */
	private void setImgLight(int position) {
		for (int i = 0; i < mImgView.length; i++) {
			if (position == i) {
				mImgView[i]
						.setBackgroundResource(R.drawable.adware_style_selected);
			} else {
				mImgView[i]
						.setBackgroundResource(R.drawable.adware_style_default);
			}
		}
	}

	/**
	 * PagerAdapter������
	 * 
	 */
	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mArrs.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView v = mArrs[position];
			container.addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			ImageView v = mArrs[position];
			container.removeView(v);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		setImgLight(position);
		if (position == 2) {
			mTxtJump.setVisibility(View.VISIBLE);
		} else {
			mTxtJump.setVisibility(View.GONE);
		}
	}
}
