package cn.hi321.android.media.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TabHost;
/**
 * @author 杨光福老师
 * tab切换
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class AnimationTabHost extends TabHost {

	private boolean isOpenAnimation;
	private int mTabCount;

	public AnimationTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		isOpenAnimation = false;
	}

	public void setOpenAnimation(boolean isOpenAnimation) {
		this.isOpenAnimation = isOpenAnimation;
	}

	public boolean setTabAnimation(int[] animationResIDs) {
		return false;
	}

	public int getTabCount() {
		return mTabCount;
	}

	public void addTab(TabSpec tabSpec) {
		mTabCount++;
		super.addTab(tabSpec);
	}

	public void setCurrentTab(int index) {

		if (null != getCurrentView()) {
			if (isOpenAnimation) {
			}
		}
		super.setCurrentTab(index);
		if (isOpenAnimation) {
		}
	}
}
