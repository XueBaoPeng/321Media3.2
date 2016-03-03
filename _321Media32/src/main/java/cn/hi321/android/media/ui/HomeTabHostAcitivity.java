package cn.hi321.android.media.ui;
 
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import cn.hi321.android.media.local.PlayListsActivity;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.SystemScreenInfo;

import com.android.china.R;

/**
 * @author 杨光福老师
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class HomeTabHostAcitivity extends TabActivity {

	private AnimationTabHost mTabHost;
	private TabWidget mTabWidget; 
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private ImageView[] views; 
 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		System.out.println("HomeTabHostAcitivity--------Oncreate");
		ActivityHolder.getInstance().addActivity(this);
		SystemScreenInfo.getSystemInfo(HomeTabHostAcitivity.this); 
		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
		mTabHost = (AnimationTabHost) findViewById(android.R.id.tabhost);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				initBottomMenu();
			}
		}, 300);
		init();
	}
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("HomeTabHostAcitivity--------onStart");
//		getImageId(flag, true);
	}
	



	private int getImageId(int index, boolean isSelect){
		int result = -1;
		switch (index) {
		case 0: 
			result = isSelect ? R.drawable.ic_tab_home_press : R.drawable.ic_tab_home;
			break;
		case 1:
			result = isSelect ? R.drawable.ic_tab_channel_press : R.drawable.ic_tab_channel;
			break;
		case 2:
			result = isSelect ? R.drawable.ic_tab_search_press : R.drawable.ic_tab_search;
			break;
		case 3:
			result = isSelect ? R.drawable.ic_tab_my_press : R.drawable.ic_tab_my;
			break;
		}
		return result;
	}

	private void initBottomMenu() {
		int viewCount = mTabWidget.getChildCount();
		views = new ImageView[viewCount];
		for (int i = 0; i < views.length; i++) {
			View v = (LinearLayout) mTabWidget.getChildAt(i);
			views[i] = (ImageView) v.findViewById(R.id.main_activity_tab_image);
		}
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				int tabID = Integer.valueOf(tabId);
				views[currIndex].setImageResource(getImageId(currIndex, false));
				views[tabID].setImageResource(getImageId(tabID, true));
				onPageSelected(tabID);
			}
		});
	}

	private void init() { 
		setIndicator("", 0, new Intent(this, MainActivity.class), R.drawable.ic_tab_home_press);
		setIndicator("", 1, new Intent(this, ChannelActivity.class), R.drawable.ic_tab_channel);
		setIndicator("", 2, new Intent(this, PlayListsActivity.class), R.drawable.ic_tab_search);
		setIndicator("", 3, new Intent(this, AboutMeActivity.class), R.drawable.ic_tab_my);
		mTabHost.setOpenAnimation(true); 
	}

	private void setIndicator(String ss, int tabId, Intent intent, int image_id) {

		View localView = LayoutInflater.from(this.mTabHost.getContext()).inflate(R.layout.tab_widget_view, null);
		((ImageView) localView.findViewById(R.id.main_activity_tab_image)).setImageResource(image_id);
//		((TextView) localView.findViewById(R.id.main_activity_tab_text)).setText(ss);
		String str = String.valueOf(tabId);
		TabHost.TabSpec localTabSpec = mTabHost.newTabSpec(str).setIndicator(localView).setContent(intent);
		 
		mTabHost.addTab(localTabSpec);
	} 

	public void onPageSelected(int arg0) {

		int one = offset * 2 + bmpW;
		Animation animation = null;
		animation = new TranslateAnimation(one * currIndex, one * arg0 , 0, 0);
		currIndex = arg0;
		animation.setFillAfter(true);
		animation.setDuration(300); 
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			Utils.dialog(HomeTabHostAcitivity.this, "退出提示框");
			return true;
		}
		return false;
	}
	 	
	   
	protected void onDestroy() {
		super.onDestroy();
		ActivityHolder.getInstance().removeActivity(this);
	}
	
	
	 

}