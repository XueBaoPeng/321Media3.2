package cn.hi321.android.media.ui;

   
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.hi321.android.media.adapter.GalleryImageAdapter;
import cn.hi321.android.media.adapter.RecommendMovice;
import cn.hi321.android.media.adapter.RecommendMusic;
import cn.hi321.android.media.entity.BaiDuChannelVideo;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.entity.Media;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.http.IBindData;
import cn.hi321.android.media.http.NetWorkTask;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.Contents;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;
import cn.hi321.android.media.widget.GalleryView;
import cn.hi321.android.media.widget.MainPageGallarySelect;
import cn.hi321.android.media.widget.MainTitlebar;
import cn.waps.AppConnect;

import com.android.china.R;
//import cn.hi321.android.media.adapter.RecommendDt;
//import cn.hi321.android.media.adapter.RecommendTv;
//import cn.hi321.android.media.adapter.RecommendZy;
/**
 * @author 杨光福老师
 * 首页面
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class MainActivity extends BaseActivity implements IBindData{
	ArrayList<BaiDuRecommend> arrayListComicHot = null;
    ArrayList<BaiDuRecommend> arrayListIndexFlash = null;
    ArrayList<BaiDuRecommend> arrayListMovieHot= null;
    ArrayList<BaiDuRecommend> arrayListTvplayHot= null;
    ArrayList<BaiDuRecommend> arrayListTvshowHot= null; 
    ArrayList<BaiDuRecommend> arrayListWoman= null; 
    ArrayList<BaiDuRecommend> arrayListMusic= null; 
    ArrayList<BaiDuRecommend> arrayListAmuse= null; 
    ArrayList<BaiDuRecommend> arrayListSport= null; 
    ArrayList<BaiDuRecommend> arrayListInfo= null; 
    
    private ArrayList<HashMap<String, ArrayList<BaiDuRecommend>>> arrRecommend;  
//    private RelativeLayout view; 
	private TextView recommendMoreMovie;
	private GridView gridviewMovie;
	private TextView recommendMoreTv;
	private GridView gridviewTv;
	private TextView recommendMoreZy;
	private GridView gridviewZy;
	private TextView recommendMoreDm;
	private GridView gridviewDm;
	
	
	private TextView recommendMoreWoman;
	private GridView gridviewWoman;
	private TextView recommendMoreMusic;
	private GridView gridviewMusic;
	private TextView recommendMoreAmuse;
	private GridView gridviewAmuse;
	private TextView recommendMoreSport;
	private GridView gridviewSport;
	private TextView recommendMoreInfo;
	private GridView gridviewInfo;
	
	
	private RadioGroup mRadioGroupGallery;  
	private GalleryView mGalleryView;  
	private GalleryImageAdapter mGalleryImageAdapter; 
	private int pagesize = 6;
 // IWXAPI 是第三方app和微信通信的openapi接口
//  private IWXAPI api;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.view_main_tab_home); 
    AppConnect.getInstance(this);	 
    ActivityHolder.getInstance().addActivity(this);
	if(UIUtils.hasNetwork(MainActivity.this)){  
    	startLoadingDialog(MainActivity.this);
    	String url =  Contents.BaiDuUrlRecommend+"?version="+Contents.version;//arrayItem.get(0).getBase_url();
	   System.out.println("url==="+url);
    	new NetWorkTask().execute(MainActivity.this,UIUtils.BaiDuRecommend,
				url,mainHandler); 
	  }else{
		  UIUtils.showToast(MainActivity.this, MainActivity.this.getText(R.string.tip_network).toString());
			 
	  } 
    initViews();
  } 
  
  
 
  
  @Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart(); 
}




private void initViews(){ 
	  ((MainTitlebar)findViewById(R.id.main_title)).show("首页"); 
	  
	  mGalleryView = (GalleryView)findViewById(R.id.newImageGallery);  
	  mRadioGroupGallery = (RadioGroup) findViewById(R.id.mainRadioGallery);
	  
	  recommendMoreMovie = (TextView)findViewById(R.id.recommendMoreMovie);
	 
	  gridviewMovie =(GridView)findViewById(R.id.gridviewMovie);
	  recommendMoreTv = (TextView)findViewById(R.id.recommendMoreTv);
	  gridviewTv = (GridView)findViewById(R.id.gridviewTv);
	  recommendMoreZy = (TextView)findViewById(R.id.recommendMoreZy);
	  gridviewZy = (GridView)findViewById(R.id.gridviewZy);
	  recommendMoreDm = (TextView)findViewById(R.id.recommendMoreDm);
	  gridviewDm = (GridView)findViewById(R.id.gridviewDm);  
	  gridviewWoman =(GridView)findViewById(R.id.gridviewWoman);
	  recommendMoreWoman = (TextView)findViewById(R.id.recommendMoreWoman);
	  gridviewMusic = (GridView)findViewById(R.id.gridviewMusic);
	  recommendMoreMusic = (TextView)findViewById(R.id.recommendMoreMusic);
	  gridviewAmuse = (GridView)findViewById(R.id.gridviewAmuse);
	  recommendMoreAmuse = (TextView)findViewById(R.id.recommendMoreAmuse);
	  gridviewSport = (GridView)findViewById(R.id.gridviewSport);
	  recommendMoreSport = (TextView)findViewById(R.id.recommendMoreSport);
	  gridviewInfo = (GridView)findViewById(R.id.gridviewInfo);
	  recommendMoreInfo = (TextView)findViewById(R.id.recommendMoreInfo);  
	  mGalleryView.setOnItemClickListener(new GallaryClick());
	  mGalleryView.setOnItemSelectedListener(new MainPageGallarySelect( pagesize, mRadioGroupGallery)); 
	  recommendMoreMovie.setOnClickListener(myOnClick);
	  recommendMoreTv.setOnClickListener(myOnClick);
	  recommendMoreZy.setOnClickListener(myOnClick);
	  recommendMoreDm.setOnClickListener(myOnClick); 
	  recommendMoreWoman.setOnClickListener(myOnClick);
	  recommendMoreMusic.setOnClickListener(myOnClick);
	  recommendMoreAmuse.setOnClickListener(myOnClick);
	  recommendMoreSport.setOnClickListener(myOnClick);
	  recommendMoreInfo.setOnClickListener(myOnClick);
	  
	  Timer timer = new Timer();
	  timer.schedule(task, 3000, 3000);   
  }
  OnClickListener myOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			Intent i = null ;
			switch (v.getId()) {
			case R.id.recommendMoreMovie:
				    i = new Intent(MainActivity.this, ChannelListActivity.class);
					BaiDuChannelVideo infoMovie= new BaiDuChannelVideo();
					infoMovie.setBase_url("http://app.video.baidu.com/adnativemovie/");
					infoMovie.setExtra("");
					infoMovie.setFilter("http://app.video.baidu.com/conds/?worktype=adnativemovie");
					infoMovie.setMask(3);
					infoMovie.setName("电影");
					infoMovie.setTag("movie");
					infoMovie.setType("channel_video");
					i.putExtra("channelVideoInfo", infoMovie);
					
				break;
			case R.id.recommendMoreTv: 
				    i = new Intent(MainActivity.this, ChannelListActivity.class);
					BaiDuChannelVideo infoTv = new BaiDuChannelVideo();
					infoTv.setBase_url("http://app.video.baidu.com/adnativetvplay/");
					infoTv.setExtra("");
					infoTv.setFilter("http://app.video.baidu.com/conds/?worktype=adnativetvplay");
					infoTv.setMask(3);
					infoTv.setName("电视剧");
					infoTv.setTag("tvplay");
					infoTv.setType("channel_video");
					i.putExtra("channelVideoInfo", infoTv);
				break;
			case R.id.recommendMoreZy: 
				    i = new Intent(MainActivity.this, ChannelListActivity.class);
					BaiDuChannelVideo infoZy = new BaiDuChannelVideo();
					infoZy.setBase_url("http://app.video.baidu.com/adnativetvshow/");
					infoZy.setExtra("");
					infoZy.setFilter("http://app.video.baidu.com/conds/?worktype=adnativetvshow");
					infoZy.setMask(3);
					infoZy.setName("综艺");
					infoZy.setTag("tvshow");
					infoZy.setType("channel_video");
					i.putExtra("channelVideoInfo", infoZy);
				break;
			case R.id.recommendMoreDm: 
				    i = new Intent(MainActivity.this, ChannelListActivity.class);
					BaiDuChannelVideo infoComic = new BaiDuChannelVideo();
					infoComic.setBase_url("http://app.video.baidu.com/adnativecomic/");
					infoComic.setExtra("");
					infoComic.setFilter("http://app.video.baidu.com/conds/?worktype=adnativecomic");
					infoComic.setMask(3);
					infoComic.setName("动漫");
					infoComic.setTag("comic");
					infoComic.setType("channel_video");
					i.putExtra("channelVideoInfo", infoComic);
				break;
			case R.id.recommendMoreWoman: 
				    i = new Intent(MainActivity.this, VideoListActivity.class);
					BaiDuChannelVideo infowWoman = new BaiDuChannelVideo();
					infowWoman.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/woman/");
//					infowWoman.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/woman/hot/1.js");
					infowWoman.setExtra("");
					infowWoman.setFilter("");
					infowWoman.setMask(2);
					infowWoman.setName("美女");
					infowWoman.setTag("woman");
					infowWoman.setType("short_video");
					i.putExtra("channelVideoInfo", infowWoman);
				break;
			case R.id.recommendMoreMusic: 
				    i = new Intent(MainActivity.this, VideoListActivity.class);
					BaiDuChannelVideo infoMusic = new BaiDuChannelVideo();
					infoMusic.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/music/");
//					infoMusic.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/music/hot/1.js");
					infoMusic.setExtra("");
					infoMusic.setFilter("");
					infoMusic.setMask(2);
					infoMusic.setName("音乐");
					infoMusic.setTag("music");
					infoMusic.setType("short_video");
					i.putExtra("channelVideoInfo", infoMusic);
				break;
			case R.id.recommendMoreAmuse: 
				    i = new Intent(MainActivity.this, VideoListActivity.class);
					BaiDuChannelVideo infoFunny = new BaiDuChannelVideo();
					infoFunny.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/amuse/");
//					infoFunny.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/amuse/");//hot/1.js
					infoFunny.setExtra("");
					infoFunny.setFilter("");
					infoFunny.setMask(2);
					infoFunny.setName("搞笑");
					infoFunny.setTag("amuse");
					infoFunny.setType("short_video");
					i.putExtra("channelVideoInfo", infoFunny);
				break;
			case R.id.recommendMoreSport: 
				    i = new Intent(MainActivity.this, VideoListActivity.class);
					BaiDuChannelVideo infoSport = new BaiDuChannelVideo();
					infoSport.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/sport/");
//					infoSport.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/sport/hot/1.js");
					infoSport.setExtra("");
					infoSport.setFilter("");
					infoSport.setMask(2);
					infoSport.setName("体育");
					infoSport.setTag("sport");
					infoSport.setType("short_video");
					i.putExtra("channelVideoInfo", infoSport);
				break;
			case R.id.recommendMoreInfo: 
				    i = new Intent(MainActivity.this, VideoListActivity.class);
					BaiDuChannelVideo infoNew = new BaiDuChannelVideo();
					infoNew.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/info/"); 
					infoNew.setExtra("");
					infoNew.setFilter("");
					infoNew.setMask(2);
					infoNew.setName("新闻");
					infoNew.setTag("info");
					infoNew.setType("short_video");
					i.putExtra("channelVideoInfo", infoNew);
				break; 

			default:
				break;
				
			}
			MainActivity.this.startActivity(i);
			 overridePendingTransition(R.anim.fade, R.anim.hold); 
		} 
	};
	
	 @Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			 ActivityHolder.getInstance().removeActivity(this);
		}
  
  int isExit = 0;
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  { 
	  
    if ((paramKeyEvent.getAction() == 0) && (paramInt == 4) ){
    	
       if (isExit == 0){
    	   isExit ++;
    	   UIUtils.showToast(this, "再点一次可退出");
    	   return true;
      }
       if(isExit == 1){
    		//以下方法将用于释放SDK占用的系统资源
    	   AppConnect.getInstance(this).finalize(); 
   		   this.finish();
    	}
    }
    return super.onKeyDown(paramInt, paramKeyEvent); 

  }
  
  @Override
 	protected void onResume() {
 		// TODO Auto-generated method stub
 		super.onResume();
 		isExit = 0;
 		
 	} 
  protected void onPause()
  {
    super.onPause();
  }

  protected void onRestart()
  {
    super.onRestart(); 
//    Toast.makeText(MainActivity.this, "重启了", 1).show();
    if(arrRecommend==null||arrRecommend.size()==0){
		  if(UIUtils.hasNetwork(MainActivity.this)){  
		    	Utils.startWaitingDialog(MainActivity.this);
		    	String url =  Contents.BaiDuUrlRecommend+"?version="+Contents.version;//arrayItem.get(0).getBase_url();
			    new NetWorkTask().execute(MainActivity.this,UIUtils.BaiDuRecommend,
						url,mainHandler); 
			  }else{
				  UIUtils.showToast(MainActivity.this, MainActivity.this.getText(R.string.tip_network).toString());
			  } 
	}
  }
  
  

   



    protected void onStop()
   {
    super.onStop(); 
//    this.tabHomeView.onStop();
  }
 
	@Override
	public void bindData(int tag, Object object) { 
		try { 
		if(tag == UIUtils.BaiDuRecommend && object!=null){ 
			 arrRecommend = (ArrayList<HashMap<String, ArrayList<BaiDuRecommend>>>)object;
			 if(arrRecommend!=null){
				 
				 for(int i=0;i<arrRecommend.size();i++){
				    	HashMap<String, ArrayList<BaiDuRecommend>> map = arrRecommend.get(i);
				    	if(map.containsKey("index_flash")){
				    		 arrayListIndexFlash = map.get("index_flash");	
				    		 continue;
				    	}
				    	if(map.containsKey("movie_hot")){
				    		 arrayListMovieHot = map.get("movie_hot");	
				    		 continue;
				    	}
				    	if(map.containsKey("tvplay_hot")){
				    		 arrayListTvplayHot = map.get("tvplay_hot");
				    		 continue;
				    	}
				    	if(map.containsKey("tvshow_hot")){
				    		 arrayListTvshowHot = map.get("tvshow_hot");
				    		 continue;
				    	}
				    	if(map.containsKey("comic_hot")){
				    		 arrayListComicHot = map.get("comic_hot");
				    		 continue;
				    	} 
				    	//
				    	if(map.containsKey("woman")){
				    		 arrayListWoman= map.get("woman");
				    		 continue;
				    	} 
				    	if(map.containsKey("music")){
				    		 arrayListMusic = map.get("music");
				    		 continue;
				    	} 
				    	if(map.containsKey("amuse")){
				    		 arrayListAmuse = map.get("amuse");
				    		 continue;
				    	} 
				    	if(map.containsKey("sport")){
				    		 arrayListSport = map.get("sport");
				    		 continue;
				    	} 
				    	if(map.containsKey("info")){
				    		 arrayListInfo = map.get("info");
				    		 continue;
				    	} 
				    }
				    
				    try {
						if(arrayListIndexFlash!=null&&arrayListIndexFlash.size()>0){  
						    mGalleryImageAdapter = new GalleryImageAdapter(MainActivity.this, arrayListIndexFlash);
							mGalleryView.setAdapter(mGalleryImageAdapter); 
							setRadioButton(arrayListIndexFlash); 
						} 
						
						if(arrayListMovieHot!=null && arrayListMovieHot.size()>0){
							RecommendMovice recommendMovieHot = new RecommendMovice(MainActivity.this,arrayListMovieHot);
							gridviewMovie.setAdapter(recommendMovieHot);
						}
						
						if(arrayListTvplayHot!=null && arrayListTvplayHot.size()>0){
							RecommendMovice recommendMovieHot = new RecommendMovice(MainActivity.this,arrayListTvplayHot);
							gridviewTv.setAdapter(recommendMovieHot);
						}
						
						if(arrayListTvshowHot!=null && arrayListTvshowHot.size()>0){
							RecommendMovice recommendMovieHot = new RecommendMovice(MainActivity.this,arrayListTvshowHot);
							gridviewZy.setAdapter(recommendMovieHot);
						}
						
						if(arrayListComicHot!=null && arrayListComicHot.size()>0){
							RecommendMovice recommendMovieHot = new RecommendMovice(MainActivity.this,arrayListComicHot);
							gridviewDm.setAdapter(recommendMovieHot);
						}
						///////
						if(arrayListMusic!=null && arrayListMusic.size()>0){
							RecommendMusic recommendMusic = new RecommendMusic(MainActivity.this,arrayListMusic,handler);
							gridviewMusic.setAdapter(recommendMusic);
						}
						
						if(arrayListWoman!=null && arrayListWoman.size()>0){
							RecommendMusic recommendMusic = new RecommendMusic(MainActivity.this,arrayListWoman,handler);
							gridviewWoman.setAdapter(recommendMusic);
						}
						
						if(arrayListAmuse!=null && arrayListAmuse.size()>0){
							RecommendMusic recommendMusic = new RecommendMusic(MainActivity.this,arrayListAmuse,handler);
							gridviewAmuse.setAdapter(recommendMusic);
						}
						
						if(arrayListSport!=null && arrayListSport.size()>0){
							RecommendMusic recommendMusic = new RecommendMusic(MainActivity.this,arrayListSport,handler);
							gridviewSport.setAdapter(recommendMusic);
						}
						
						if(arrayListInfo!=null && arrayListInfo.size()>0){
							RecommendMusic recommendMusic = new RecommendMusic(MainActivity.this,arrayListInfo,handler);
							gridviewInfo.setAdapter(recommendMusic);
						}
						
					} catch (Exception e) {
						// TODO: handle exception
					}  
			 } 
			 Utils.closeWaitingDialog();
				closeLoadingDialog(); 
			}  
		} catch (Exception e) { 
			closeLoadingDialog(); 
		}
		
	}  
	 
	
	/**
   	 * 设置
   	 * */
   	private void setRadioButton(ArrayList<BaiDuRecommend> arrayListIndexFlash ){
   		
   		try {
   			ViewGroup.LayoutParams lp_fullWidth = new ViewGroup.LayoutParams(
					UIUtils.convertDipOrPx(MainActivity.this, 15),
					LayoutParams.WRAP_CONTENT);
	   		if(arrayListIndexFlash!=null)
	   		for (int i = 0; i < arrayListIndexFlash.size(); i++) {
	   			RadioButton mRadioButton = new RadioButton(MainActivity.this);
				mRadioButton.setButtonDrawable(R.drawable.feature_radio_selector);
				mRadioButton.setBackgroundColor(0x00000000);
				mRadioButton.setWidth(UIUtils.convertDipOrPx(MainActivity.this, 15));
				mRadioButton.setTag(i);
				mRadioGroupGallery.addView(mRadioButton, lp_fullWidth);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} 
			
   	} 
   	
	 private class GallaryClick implements AdapterView.OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) { 
				
				try {
					int myPostion = 0;
					if(position >= arrayListIndexFlash.size()){
						myPostion = position %(arrayListIndexFlash.size());
					}else{
						myPostion = position;
					}
					
					if(arrayListIndexFlash !=null&&arrayListIndexFlash.size() > myPostion){
						if(UIUtils.hasNetwork(MainActivity.this)){
							 BaiDuRecommend video = arrayListIndexFlash.get(myPostion);
							String workType =  video.getWorks_type();
			        	   if(workType.equals("browser")){
			        			Utils.playView(setMedia(video), MainActivity.this);
			        	   }else{
			        		   if(video.getWorks_id().equals("")&&video.getUrl().contains(".html")){
									String tag = video.getTag();
								    Intent i = new Intent(MainActivity.this, VideoListActivity.class);
									BaiDuChannelVideo infoFunny = new BaiDuChannelVideo();
									String url = "http://m.baidu.com/video?static=utf8_data/android_channel/json/"+tag+"/";
									infoFunny.setBase_url(url.replaceAll(" ", ""));
//									infoFunny.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/amuse/");//hot/1.js
									infoFunny.setExtra("");
									infoFunny.setFilter("");
									infoFunny.setMask(2);
									infoFunny.setName(video.getTitle());
									infoFunny.setTag(video.getTag());
									infoFunny.setType(workType);
									i.putExtra("channelVideoInfo", infoFunny);
									MainActivity.this.startActivity(i);
									overridePendingTransition(R.anim.fade, R.anim.hold);
								
							}else{
								 if(workType.equals("tvshow")){
				        	    	  Intent intent = new Intent(); 
						        	     intent.putExtra("BaiDuRecommend",video);
				        	    	 intent.setClass(MainActivity.this,MediaShowActivity.class); 
				        	    	 MainActivity.this.startActivity(intent);
				        	     }else if(workType.equals("info")||workType.equals("amuse")||workType.equals("music")
				        	    		 ||workType.equals("sport")||workType.equals("woman")||workType.equals("player")||workType.equals("browser")){ 
										 
											Utils.playView(setMedia(video), MainActivity.this);
											 
				        	     } else{
				        	    	  Intent intent = new Intent(); 
						        	     intent.putExtra("BaiDuRecommend",video);
				        	    	 intent.setClass(MainActivity.this,MediaActivity.class);  
				        	    	 MainActivity.this.startActivity(intent);
				        	     } 
				        			
							}
			        	   }
							
			        	    
						}else{
							UIUtils.showToast(MainActivity.this, MainActivity.this.getText(R.string.tip_network).toString());
						}
		        		
		        	 
		        	}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
	  };
	  
	  private Media setMedia(BaiDuRecommend video){
		    Media mMedia = new Media();
			ArrayList<MediaItem> mMediaItemArrayList = new ArrayList<MediaItem>();
			MediaItem mMediaItem = new MediaItem();
			mMediaItem.setLive(false);
			mMediaItem.setTitle(video.getTitle());
			mMediaItem.setSourceUrl(video.getUrl());
			mMediaItem.setImage(video.getImg_url()); 
			mMediaItemArrayList.add(mMediaItem);
			mMedia.setMediaItemArrayList(mMediaItemArrayList);
			mMedia.setPosition(0);
			mMedia.setMediaType(video.getWorks_type());
			return mMedia;
	  }
	  
	   
	  
	  /**
	   * 定时器，实现自动播放
	   */
	   private int index = 0;
	   private TimerTask task = new TimerTask() {
	 	  @Override
	 	  public void run() {
	 		  Message message = new Message();
	 		  message.what = 2;
	 		  index = mGalleryView.getSelectedItemPosition();
	 		  index++;
	 		  handler.sendMessage(message);
	 	  }
	   };
	   	private Handler handler = new Handler() {
	 	  @Override
	 	  public void handleMessage(Message msg) {
	 		  super.handleMessage(msg);
	 		  	switch (msg.what) {
	 		  		case 1: 
//	 		  		  if(UIUtils.hasNetwork(context)){
//	 		  			MovieDataAsyncTask movieTask = new MovieDataAsyncTask();  
//	 					movieTask.execute();
//	 		  		  }else{
//	 		  			UIUtils.showToast(context, context.getText(R.string.tip_network).toString());
//	 		  		  }
	 		  		
	 		  			break;
	 		  		case 2: 
	 		  			mGalleryView.setSelection(index);   
	 		  			break;
	 		  		case UIUtils.SHOW_PLAY:
	 		  			BaiDuRecommend baiDuRecommends =(BaiDuRecommend)msg.obj;
						if(baiDuRecommends!=null){ 
							Utils.playView(setMedia(baiDuRecommends),MainActivity.this);
						}
	 		  			break;
	 		  		default:
	 		  			break;
	 		  }
	 	  }
	   	};
   
	   	
		private Dialog dialog = null;
		public void startLoadingDialog(Context context){
			try {
				// synchronized(Utils.synchronizeds) {
				if (dialog == null) {
					dialog = new Dialog(context, R.style.waitingLoading); 
					dialog.setContentView(R.layout.loading_pre); 
//					dialog.set
//					RelativeLayout re = (RelativeLayout)findViewById(R.id.layout_loading);
					dialog.show();
				} else if (dialog != null && !dialog.isShowing()) {
					dialog.setContentView(R.layout.loading_pre);
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				}
				//
				// }
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void closeLoadingDialog() {
			try {
				// synchronized(Utils.synchronizeds) {
				if (dialog != null) {
					dialog.dismiss();
				}
				// }
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
}  