package com.big_whiteweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.big_whiteweather.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ViewPagerActivity extends Activity {

	private ViewPager mViewPager;

	private int[] mImgIds = new int[] { R.drawable.guide_image1,
			R.drawable.guide_image2, R.drawable.guide_image3 };

	private List<ImageView> mImages = new ArrayList<ImageView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_page);

		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				 if(arg0==mImages.size()-1)
				 { Intent intent = new Intent(getApplicationContext() , ChooseAreaActivity.class);
				   startActivity(intent); 
				 }
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mViewPager.setAdapter(new PagerAdapter() {
        
			@SuppressWarnings("deprecation")
			@Override
			public Object instantiateItem(ViewGroup container, int position) {

				ImageView imageView = new ImageView(ViewPagerActivity.this);
				imageView.setImageResource(mImgIds[position]);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				container.addView(imageView);
				
				mImages.add(imageView);
				return imageView;
			}

			@SuppressWarnings("deprecation")
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(mImages.get(position));
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mImgIds.length;
			}
		});
	}

}
