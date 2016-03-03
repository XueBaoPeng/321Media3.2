package cn.hi321.android.media.ui;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.player.ProxyUtils;
import cn.hi321.android.media.player.SystemPlayer;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.LogUtil;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.UserPreference;
import cn.hi321.android.media.utils.Utils;

import com.android.china.R;

public class ShowActivity extends Activity  {
	private static final String TAG = "ShowActivity";
	private WebView mWebview;
	private ProgressBar progressDialog;
	private Intent mIntent = null;
    private String uri= null;
	private MediaItem mVideoInfo = null;
	private int flags = 0;
    private Boolean isOncreat = false;
	private String mTitle;
	private Button btn_search;
	
	private boolean isNetCheckDialog = false;
	private ImageButton returnButton;
	private TextView title;
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			switch (msg.what) {
			case 0:
				LogUtil.i(TAG, "View.VISIBLE");
				if(progressDialog !=null)
				progressDialog.setVisibility(View.VISIBLE);// 显示进度对话框
				break;
			case 1:
				LogUtil.i(TAG, "View.GONE");
				if(progressDialog !=null)
				progressDialog.setVisibility(View.GONE);// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
				break;
			case 2:
				titleLeftButton();
				break;
			}
		
			super.handleMessage(msg);
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.show_activity);
		 ActivityHolder.getInstance().addActivity(this);	
		System.out.println("onCreate----------------");
//		setContentView(R.layout.show_activity);
		isOncreat = true;
//		setTitleBarVisibility(View.GONE);
		progressDialog = (ProgressBar)findViewById(R.id.progressdialog);
		returnButton =(ImageButton)findViewById(R.id.btn_logo);
		  btn_search = (Button)findViewById(R.id.btn_search);
		  
		returnButton.setOnClickListener(myOnClick);
		btn_search.setVisibility(View.VISIBLE);
		btn_search.setOnClickListener(myOnClick);
//		btn_search.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		})
		title = (TextView)findViewById(R.id.tv_title);
		progressDialog.setVisibility(View.VISIBLE);
	    UserPreference.ensureIntializePreference(this); 
        init();// 执行初始化函数
		Bundle mBundle = getData();
		load(mBundle);
	}
	
	private OnClickListener myOnClick  = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 if(v.getId() == R.id.btn_logo){
				 titleLeftButton();
			 }else if(v.getId() == R.id.btn_search){
				 Intent i = new Intent(ShowActivity.this,TVActivity.class);
				 ShowActivity.this.startActivity(i);
				 overridePendingTransition(R.anim.fade, R.anim.hold); 
			 }
		}
	};
	


	private Bundle getData() {
		mIntent = getIntent();
		Bundle mBundle = null;
		if(mIntent != null){
			 mBundle = mIntent.getBundleExtra("extra");
			 flags =  mIntent.getFlags();
		}
		return mBundle;
	}
	
	private void load(Bundle mBundle) {
		if(mBundle != null){
			mVideoInfo = (MediaItem)mBundle.getSerializable("VideoInfo");
			if(mVideoInfo != null){
				setTopBarTitle(mVideoInfo.getTitle());
				 uri = mVideoInfo.getUrl();
				 mTitle= mVideoInfo.getTitle();
				if(uri != null){
					loadurl(mWebview,uri,false);
				}else{
					uri = "http://m.cctv.com/NBA/NBAyaowen/node_232.htm";
					loadurl(mWebview, uri,false);
				}
			}
		    
			
		}else{
			uri = "http://m.cctv.com/NBA/NBAyaowen/node_232.htm";
			loadurl(mWebview, uri,false);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		 int groupId = 0; 
		    // The order position of the item 
		    int menuItemOrder = Menu.NONE; 
//		 
//		    menu.add(groupId, 1, menuItemOrder, "后退") 
//		        .setIcon(R.drawable.icon_menu_back_htc); 
//		    menu.add(groupId, 2, menuItemOrder, "前进")
//		    .setIcon(R.drawable.icon_menu_forward_htc); 
//		    menu.add(groupId, 3, menuItemOrder, "刷新")
//		    .setIcon(R.drawable.icon_reload_off_htc); 
//		    menu.add(groupId, 4, menuItemOrder, "推荐")
//		    .setIcon(R.drawable.icon_menu_multitab_htc); 
		    return true; 
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
			return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case 1:
			if(mWebview != null&&mWebview.canGoBack()){
				if (handler != null){ 
					handler.sendEmptyMessage(0);
				}
				mWebview.goBack();
			}else if(mWebview != null&&!mWebview.canGoBack()){
				Toast.makeText(this, "不能后退了", 0).show();
			}
			
			break;
		case 2:
			if(mWebview != null&&mWebview.canGoForward()){
				if (handler != null){ 
					handler.sendEmptyMessage(0);
				}
				mWebview.goForward();
			}else if(mWebview != null&&!mWebview.canGoForward()){
				Toast.makeText(this, "不能前进了", 0).show();
			}
			break;
		case 3:
			if(Utils.isCheckNetAvailable(ShowActivity.this)){
				
				if(uri ==null)
					return true;
				
				if (uri.contains("rtsp") ||uri.contains("3gp") || uri.contains("mp4")) {
					loadurl(mWebview, uri, false);
				} else {
					loadurl(mWebview, uri, false);
				}
				if (handler != null){ 
					handler.sendEmptyMessage(0);
				}
			}else{
				if(!isNetCheckDialog){
					isNetCheckDialog = true;
					UIUtils.showToast(ShowActivity.this	, "亲，没有网络了，请检查网络！");
				}
			}
			
			
			break;
		
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setTopBarTitle(String titles){
		title.setText(titles+"");
	}
	

	public void init() {
		
		// 初始化
		mWebview = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebview.getSettings();
		webSettings.setJavaScriptEnabled(true);// 可用JS
		webSettings.setPluginsEnabled(true);
		webSettings.setBuiltInZoomControls(true);//是否支持缩放
		webSettings.setAllowFileAccess(true);
		mWebview.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上  
//		setAllowFileAccess//启动或禁止WebView访问文件数据
//		setBlockNetworkImage//是否显示网络图像
//		setBuiltInZoomControls//是否支持缩放
//		setCacheMode//设置缓冲模式
//		setDefaultFontSize//设置默认字体大小
//		setDefaultTextEncodingName//设置在解码时使用的默认编码
//		setFixedFontFamily//设置固定使用的字体
//		setJavaScriptEnabled//设置是否支持JavaScript
//		setLayoutAlgorithm//设置布局方式
//		setLightTouchEnabled//设置用鼠标激活被选项
//		setSupportZoom//设置是否支持变焦 
		mWebview.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				
//				String info = "uri=" + uri + "--userAgent=" + userAgent
//						+ "--contenDisposition=" + contentDisposition
//						+ "--mimetype=" + mimetype + "--contentLength="
//						+ contentLength;
//				Toast.makeText(ShowActivity.this, info, Toast.LENGTH_SHORT)
//						.show();
				
				if(Utils.isCheckNetAvailable(ShowActivity.this)){
					
					LogUtil.i(TAG,	"---URL==="		+ url.toString());
					if (handler != null){ 
						handler.sendEmptyMessage(0);
					}
					if (url.contains("rtsp") ||url.contains("3gp") || url.contains("mp4")|| url.contains("m3u8")) {
						uri = url;
						loadurl(null, url, true);
					}else{
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						startActivity(intent);
					}
				}



			}
		});
		
		mWebview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				
				if(Utils.isCheckNetAvailable(ShowActivity.this)){
					
					LogUtil.i(TAG,	"---URL==="		+ url.toString());
					if (handler != null){ 
						handler.sendEmptyMessage(0);
					}
					if (url.contains("rtsp") ||url.contains("3gp") || url.contains("mp4")|| url.contains("m3u8")) {
						uri = url;
						loadurl(view, url, false);
					} else {
						uri = url;
						loadurl(view, url, false);
					}
				}else{
					Toast.makeText(ShowActivity.this, "网络不可用，请检查网络再试", 0).show();
					
				} 

				return true;
			}

		});
		mWebview.setWebChromeClient(new WebChromeClient() {
			
			
			
			@Override
			public void onExceededDatabaseQuota(String url,
					String databaseIdentifier, long currentQuota,
					long estimatedSize, long totalUsedQuota,
					QuotaUpdater quotaUpdater) {
				
				String urls = url;
				Log.i("yangguangfu",
						"onExceededDatabaseQuota=="
								+ urls);
				// TODO Auto-generated method stub
				super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
						estimatedSize, totalUsedQuota, quotaUpdater);
			}

			@Override
			public void onReceivedIcon(WebView view, Bitmap icon) {
				// TODO Auto-generated method stub
				super.onReceivedIcon(view, icon);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				if(title!= null&&title.trim().length()>0){
					mTitle = title;
					if(mTitle!= null){
						setTopBarTitle(mTitle);
					}
				}

				Log.i("yangguangfu",
						"onReceivedTitle=="
								+ mTitle);
				
				
				super.onReceivedTitle(view, title);
			}

			@Override
			public void onReceivedTouchIconUrl(WebView view, String url,
					boolean precomposed) {
				
				String urls = url;
				Log.i("yangguangfu",
						"onReceivedTouchIconUrl=="
								+ urls);
				// TODO Auto-generated method stub
				super.onReceivedTouchIconUrl(view, url, precomposed);
			}

			@Override
			public void onRequestFocus(WebView view) {
				// TODO Auto-generated method stub
				super.onRequestFocus(view);
			}

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				// TODO Auto-generated method stub
				super.onShowCustomView(view, callback);
			}

			public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
				setProgress(progress*100); 
				
				System.out.println("progress------------"+progress );
				
				String url = view.getUrl();
				Log.i("yangguangfu",
						"onProgressChanged_URL="
								+ url);
				
				Log.i("yangguangfu",
						"setWebChromeClient___onProgressChanged_URL="
								+ progress);
				if (progress >= 100) {  
					isNetCheckDialog = false;
					handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
				}
				super.onProgressChanged(view, progress); 
			}
		});
		 

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
			if(mWebview!=null&& mWebview.canGoBack()){
				mWebview.goBack();
			}else{ 
				if( mWebview != null){
					mWebview.destroy();
				}
				titleLeftButton();
			}
			
			return true;
		} 
		return false;
	}
    private boolean isLoadPlayed = false;
    
 // 构建Runnable对象，在runnable中更新界面  
    Runnable runnableUi=new  Runnable(){  
        @Override  
        public void run() {  
            //更新界面  
        	if(mWebview!= null&&uri!= null){
        		mWebview.loadUrl(uri);
        	}
        	
        }  
          
    };  

    
	public void loadurl(final WebView view, final String url,
			final boolean isVideoUrl) {
		new Thread() {
			public void run() {
				LogUtil.i(TAG, "run()");
				String uris = url;
				if (isVideoUrl && url != null) {
					
					if (url.contains("rtsp") ||uris.contains("3gp") || uris.contains("mp4")|| uris.contains("m3u8")) {
						if(uris != null&&!isLoadPlayed){
							isLoadPlayed = true;
							LogUtil.i(TAG, "uris="+uris);
							
							
							if(!uris.contains(".3gp") && !uris.contains(".mp4")&& !uris.contains(".m3u8")){
								 uris = ProxyUtils.getRedirectUrl(uris);
							}
					       
					        if(mVideoInfo != null){
					        	  mVideoInfo.setUrl(uris);
					        }else{
					        	mVideoInfo = new MediaItem();
					        	mVideoInfo.setUrl(uris);
					        }
					      
					        try{
					        	 if(mTitle != null&&mTitle.trim().length()>0)
								        mVideoInfo.setTitle(mTitle);
					        }catch(Exception e){
					        	e.printStackTrace();
					        }
					        if(uris.contains("m3u8")){
					        	mVideoInfo.setLive(true);
					        }else{
					        	mVideoInfo.setLive(false);
					        }
					       
					       
							Intent intent = new Intent(ShowActivity.this,SystemPlayer.class);
							Bundle mBundle = new Bundle();
							mBundle.putSerializable("VideoInfo", mVideoInfo);
							intent.putExtras(mBundle);
							LogUtil.i(TAG, "startActivity");
							ShowActivity.this.startActivityForResult(intent, 1);
							overridePendingTransition(R.anim.fade, R.anim.hold);
							if (handler != null){ 
								handler.sendEmptyMessage(1);
							}
							
						}
						
					}
				} else {
					isLoadPlayed = false;
					LogUtil.i(TAG, "uris="+uris);
					mWebview = view;
					if(handler!= null&&runnableUi!= null)
					  handler.post(runnableUi); 
				}

			}
		}.start();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			isLoadPlayed = false;
		}
	}
	
	
	protected void titleLeftButton() {
		onDestroyWebView();
		ShowActivity.this.finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	
		
	}
	
	private void release() {
        if (mWebview != null) {
        	mWebview.setWebViewClient(null);
        	mWebview.setWebChromeClient(null);
        	mWebview.setDownloadListener(null);
            
          
        }
       
    }

	 
	protected void titlRightButton() {
		// TODO Auto-generated method stub
		if(Utils.isCheckNetAvailable(ShowActivity.this)){
			
			if(uri ==null)
				return;
			
			if (uri.contains("rtsp") ||uri.contains("3gp") || uri.contains("mp4")) {
				loadurl(mWebview, uri, false);
			} else {
				loadurl(mWebview, uri, false);
			}
			if (handler != null){ 
				handler.sendEmptyMessage(0);
			}
		}else{
			
			Toast.makeText(ShowActivity.this, "网络不可用，请检查网络再试", 0).show();
				
		}
		
	}
	
	public void getUpdatePoints(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void getUpdatePointsFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}
//	
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.btn_logo:
//			titleLeftButton();
//			break;
//
//		default:
//			break;
//		}
//	}
	@Override
	protected void onResume() {
		
		super.onResume();
		
		 
	}
	
	 
 
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(flags ==2){ 
			System.out.println("onPause--------------"); 
			isOncreat = false;
		}
		super.onPause();  
	}
	
	
	
	public void onDestroyWebView(){
		try{
			release();
			if(mWebview != null){
				System.out.println("onDestroyWebView!!!!!!!!!!!!!!!!!!!!!!-------");
				mWebview.onPause(); 
			}  
		}catch(Exception e){
			e.printStackTrace();
		}
		
	} 
	
	@Override
	protected void onDestroy() {
		System.out.println("onDestroy----------");
		try{
			if(mWebview != null){
				mWebview.destroy(); 	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	    ActivityHolder.getInstance().removeActivity(this);
		super.onDestroy();
	
	}

	

}