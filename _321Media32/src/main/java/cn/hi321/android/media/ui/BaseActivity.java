package cn.hi321.android.media.ui;

import android.app.Activity; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message; 
/**
 * @author 杨光福老师
 * 基类
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class BaseActivity extends Activity 
{  

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle); 
  } 
  protected void onResume()
  {
    super.onResume();
   
  }

  protected void onStop()
  {
    super.onStop(); 
  }
  
  public Handler mainHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
//			case UIUtils.StrDialog:
//				startWationDialog();
//				
//				break;
//			case UIUtils.StopDialog:
//				closeWationDialog();
//				break;

			default:
				break;
			}
		}
		
	};

//@Override
//public void onTabChanged(int paramInt) {
//	// TODO Auto-generated method stub
//	
//}
// 
}