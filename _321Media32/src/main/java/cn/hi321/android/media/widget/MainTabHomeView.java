//package cn.hi321.android.media.widget;
// 
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Timer;
//import java.util.TimerTask;
// 
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Message;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import cn.hi321.android.media.R;
//import cn.hi321.android.media.adapter.GalleryImageAdapter;
//import cn.hi321.android.media.adapter.MovieAdapter;
//import cn.hi321.android.media.adapter.RecommendDt;
//import cn.hi321.android.media.adapter.RecommendMovice;
//import cn.hi321.android.media.adapter.RecommendTv;
//import cn.hi321.android.media.adapter.RecommendZy;
//import cn.hi321.android.media.entity.BaiDuInfo;
//import cn.hi321.android.media.entity.BaiDuNav;
//import cn.hi321.android.media.entity.BaiDuRecommend;
//import cn.hi321.android.media.entity.HomeResponse;
//import cn.hi321.android.media.entity.VideoInfo;
//import cn.hi321.android.media.http.DataMode;
////import cn.hi321.android.media.ui.ChannelListActivity;
//import cn.hi321.android.media.ui.MediaActivity;
////import cn.hi321.android.media.ui.VideoListActivity;
////import cn.hi321.android.media.ui.ChannelListActivity.GetPlayInfoAsyncTask;
//import cn.hi321.android.media.utils.Contents;
////import cn.hi321.android.media.utils.ShowPlay;
//import cn.hi321.android.media.utils.UIUtils;
//import cn.hi321.android.media.utils.Utils;
//
//
// public class MainTabHomeView extends LinearLayout 
//{ 
////	private ArrayList<BaiDuInfo> baiduArr;  
//	private ArrayList<HashMap<String, ArrayList<BaiDuRecommend>>> arrRecommend;
//	private ArrayList<BaiDuRecommend> arrayListIndexFlash ;
//	private ArrayList<BaiDuRecommend> arrayListMovieHot;
//	private ArrayList<BaiDuRecommend> arrayListTvplayHot;
//	private ArrayList<BaiDuRecommend> arrayListTvshowHot;
//	private ArrayList<BaiDuRecommend> arrayListComicHot;
//	private TextView recommendMoreMovie;
//	private GridView gridviewMovie;
//	private TextView recommendMoreTv;
//	private GridView gridviewTv;
//	private TextView recommendMoreZy;
//	private GridView gridviewZy;
//	private TextView recommendMoreDm;
//	private GridView gridviewDm;
//	  
//	  
//	  
//	private RadioGroup mRadioGroupGallery; 
//	
//	private GalleryView mGalleryView;  
//	private GalleryImageAdapter mGalleryImageAdapter; 
////	private MovieAdapter movieAdapter  ; 
//	private int pagesize = 6;
//	private View localView; 
//	private Activity context; 
// 
//
//	
//    public MainTabHomeView(Context paramContext)
//   {
//       this(paramContext, null);
//   }
//
//  public MainTabHomeView(Context paramContext, AttributeSet paramAttributeSet)
//  {
//    super(paramContext, paramAttributeSet); 
//	 Activity localActivity = (Activity)paramContext;
//	 this.context = localActivity;
//	
//    setOrientation(1);
//    localView = UIUtils.getLayoutInflater(paramContext).inflate(R.layout.view_main_tab_home, this); 
//    baiduArr = new ArrayList<BaiDuInfo>(); 
//    initViews();
//
//  }
//  public void setLoadingData( ArrayList<HashMap<String, ArrayList<BaiDuRecommend>>> arrRecommend)
//  { 
//    this.arrRecommend = arrRecommend;
//    
//    for(int i=0;i<arrRecommend.size();i++){
//    	HashMap<String, ArrayList<BaiDuRecommend>> map = arrRecommend.get(i);
//    	if(map.containsKey("index_flash")){
//    		 arrayListIndexFlash = map.get("index_flash");	
//    		 continue;
//    	}
//    	if(map.containsKey("movie_hot")){
//    		 arrayListMovieHot = map.get("movie_hot");	
//    		 continue;
//    	}
//    	if(map.containsKey("tvplay_hot")){
//    		 arrayListTvplayHot = map.get("tvplay_hot");
//    		 continue;
//    	}
//    	if(map.containsKey("tvshow_hot")){
//    		 arrayListTvshowHot = map.get("tvshow_hot");
//    		 continue;
//    	}
//    	if(map.containsKey("comic_hot")){
//    		 arrayListComicHot = map.get("comic_hot");
//    		 continue;
//    	} 
//    }
//    
//    try {
//		if(arrayListIndexFlash!=null&&arrayListIndexFlash.size()>0){  
//		    mGalleryImageAdapter = new GalleryImageAdapter(context, arrayListIndexFlash);
//			mGalleryView.setAdapter(mGalleryImageAdapter); 
//			setRadioButton(arrayListIndexFlash); 
//		} 
//		
//		if(arrayListMovieHot!=null && arrayListMovieHot.size()>0){
//			RecommendMovice recommendMovieHot = new RecommendMovice(context,arrayListMovieHot);
//			gridviewMovie.setAdapter(recommendMovieHot);
//		}
//		
//		if(arrayListTvplayHot!=null && arrayListTvplayHot.size()>0){
//			RecommendTv recommendMovieHot = new RecommendTv(context,arrayListTvplayHot);
//			gridviewTv.setAdapter(recommendMovieHot);
//		}
//		
//		if(arrayListTvshowHot!=null && arrayListTvshowHot.size()>0){
//			RecommendZy recommendMovieHot = new RecommendZy(context,arrayListTvshowHot);
//			gridviewZy.setAdapter(recommendMovieHot);
//		}
//		
//		if(arrayListComicHot!=null && arrayListComicHot.size()>0){
//			RecommendDt recommendMovieHot = new RecommendDt(context,arrayListComicHot);
//			gridviewDm.setAdapter(recommendMovieHot);
//		}
//	} catch (Exception e) {
//		// TODO: handle exception
//	} 
//     
//  } 
//  
//
//  private void initViews(){ 
//	  ((MainTitlebar)findViewById(R.id.main_title)).show("首页"); 
//	  
//	  mGalleryView = (GalleryView)findViewById(R.id.newImageGallery);  
//	  mRadioGroupGallery = (RadioGroup) localView.findViewById(R.id.mainRadioGallery);
//	  
//	  recommendMoreMovie = (TextView)localView.findViewById(R.id.recommendMoreMovie);
//	  gridviewMovie =(GridView)localView.findViewById(R.id.gridviewMovie);
//	  recommendMoreTv = (TextView)localView.findViewById(R.id.recommendMoreTv);
//	  gridviewTv = (GridView)localView.findViewById(R.id.gridviewTv);
//	  recommendMoreZy = (TextView)localView.findViewById(R.id.recommendMoreZy);
//	  gridviewZy = (GridView)localView.findViewById(R.id.gridviewZy);
//	  recommendMoreDm = (TextView)localView.findViewById(R.id.recommendMoreDm);
//	  gridviewDm = (GridView)localView.findViewById(R.id.gridviewDm); 
//	  mGalleryView.setOnItemClickListener(new GallaryClick());
//	  mGalleryView.setOnItemSelectedListener(new MainPageGallarySelect( pagesize, mRadioGroupGallery));
//	 
//	  Timer timer = new Timer();
//	  timer.schedule(task, 3000, 3000);   
//  }
//  
//  private OnClickListener myOnClick = new OnClickListener() {
//	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
////			case R.id.title_activity_main_zongyi:
////				Intent i = new Intent(context,VideoListActivity.class);
////		        i.putExtra("flag", "zy");//动漫  
////		    	context.startActivity(i);
////				break;
////			case R.id.title_activity_main_dongman:
////				Intent j = new Intent(context,VideoListActivity.class); 
////		        j.putExtra("flag", "japankatong");//动漫 
////		    	context.startActivity(j); 
////				break; 
////			case R.id.title_activity_main_new://2013年新片
////				Intent k = new Intent(context,VideoListActivity.class); 
////		        k.putExtra("flag", "newTV");//动漫 
////		    	context.startActivity(k); 
////				break;
//		}
//		
//	}
//};
// 
//   
//  private class GallaryClick implements AdapterView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) { 
//			
//			try {
//				int myPostion = 0;
//				if(position >= arrayListIndexFlash.size()){
//					myPostion = position %(arrayListIndexFlash.size());
//				}else{
//					myPostion = position;
//				}
//				
//				if(arrayListIndexFlash !=null&&arrayListIndexFlash.size() > myPostion){
//					if(UIUtils.hasNetwork(context)){
//						 BaiDuRecommend video = arrayListIndexFlash.get(myPostion);
//		        	     Intent intent = new Intent(); 
//		        	     intent.putExtra("BaiDuRecommend",video);
//		        		 intent.setClass(context,MediaActivity.class);  
//		        		 context.startActivity(intent);
//					}else{
//						UIUtils.showToast(context, context.getText(R.string.tip_network).toString());
//					}
//	        		
//	        	 
//	        	}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			
//		}
//  };
//  
//   
//  
//  /**
//   * 定时器，实现自动播放
//   */
//   private int index = 0;
//   private TimerTask task = new TimerTask() {
// 	  @Override
// 	  public void run() {
// 		  Message message = new Message();
// 		  message.what = 2;
// 		  index = mGalleryView.getSelectedItemPosition();
// 		  index++;
// 		  handler.sendMessage(message);
// 	  }
//   };
//   	private Handler handler = new Handler() {
// 	  @Override
// 	  public void handleMessage(Message msg) {
// 		  super.handleMessage(msg);
// 		  	switch (msg.what) {
// 		  		case 1: 
//// 		  		  if(UIUtils.hasNetwork(context)){
//// 		  			MovieDataAsyncTask movieTask = new MovieDataAsyncTask();  
//// 					movieTask.execute();
//// 		  		  }else{
//// 		  			UIUtils.showToast(context, context.getText(R.string.tip_network).toString());
//// 		  		  }
// 		  		
// 		  			break;
// 		  		case 2: 
// 		  			mGalleryView.setSelection(index);   
// 		  			break;
// 		  		case UIUtils.SHOW_PLAY:
//// 		  			String urls =(String)msg.obj;
////					if(urls!=null){
////						ShowPlay showPlay = new ShowPlay(context, urls);
////					}
// 		  			break;
// 		  		default:
// 		  			break;
// 		  }
// 	  }
//   	};
//   	
//   	/**
//   	 * 设置
//   	 * */
//   	private void setRadioButton(ArrayList<BaiDuRecommend> arrayListIndexFlash ){
//   		
//   		try {
//   			ViewGroup.LayoutParams lp_fullWidth = new ViewGroup.LayoutParams(
//					UIUtils.convertDipOrPx(context, 15),
//					LayoutParams.WRAP_CONTENT);
//	   		if(arrayListIndexFlash!=null)
//	   		for (int i = 0; i < arrayListIndexFlash.size(); i++) {
//	   			RadioButton mRadioButton = new RadioButton(context);
//				mRadioButton.setButtonDrawable(R.drawable.feature_radio_selector);
//				mRadioButton.setBackgroundColor(0x00000000);
//				mRadioButton.setWidth(UIUtils.convertDipOrPx(context, 15));
//				mRadioButton.setTag(i);
//				mRadioGroupGallery.addView(mRadioButton, lp_fullWidth);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		} 
//			
//   	} 
////  class ProgressBarAsyncTask extends AsyncTask<Integer, Integer, Object> { 
////		/**
////		 * 这里的Integer参数对应AsyncTask中的第一个参数 
////		 * 这里的String返回值对应AsyncTask的第三个参数
////		 * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
////		 * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
////		 */
////		protected Object doInBackground(Integer... params) {
////			DataMode dataMode = new DataMode(context);  
////			String url = Contents.BaiDuUrl;
//////			String path = Contents.url+"pageindex="+pageindex+"&pagesize=20"+"&type="+"tv"+"&year="
//////		    		+"2013"+"&area="+"all"+"&cli="+Contents.cli+"&ver="+Contents.version+"&category="+"all";
////			baiduArr = dataMode.getBaiduData(url);
////			BaiDuNav navRecommend = null;
////			BaiDuNav navChannelVideos = null;
////			BaiDuNav navShortVideos = null;
////			if(baiduArr!=null ){
////				for(int i=0;i<baiduArr.size();i++){
////					BaiDuInfo baiduInfo  = baiduArr.get(i);
////					HashMap<String, BaiDuNav>  map = baiduInfo.getMap();
////					if(map.containsKey("recommend")){
////						navRecommend = map.get("recommend");
////					}
////					if(map.containsKey("channel_videos")){
////						navChannelVideos = map.get("channel_videos");
////					}
////					if(map.containsKey("short_videos")){
////						navShortVideos = map.get("short_videos");
////					}
////				}
////			}
////			
////			return null;
//////			return   dataMode.getMediaData(path, homeResTv,handler);
////		}
////
////
////		/**
////		 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
////		 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
////		 */
////		protected void onPostExecute(Object result) { 
////			try {
////				if(result!=null){
////					handler.sendEmptyMessage(1); 
////			        homeResTv = (HomeResponse)result;
////				    arrayVideo = homeResTv.getResult(); 
////				    mGalleryImageAdapter = new GalleryImageAdapter(context, arrayVideo);
////					mGalleryView.setAdapter(mGalleryImageAdapter); 
////					setRadioButton(); 
////				}else{
////					UIUtils.showToast(context, "返回数据为空");
////					if(mLoadingPreView !=null){
////						 MainTabHomeView.this.mLoadingPreView.setVisibility(View.GONE);
////						 MainTabHomeView.this.context.findViewById(R.id.layout_loading).setVisibility(View.GONE);
////					 }
////				}
////			} catch (Exception e) {
////				// TODO: handle exception
////			} 
////		}
////
////
////		//该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
////		protected void onPreExecute() {  
////		
////		}
////
////
////		/**
////		 * 这里的Intege参数对应AsyncTask中的第二个参数
////		 * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
////		 * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
////		 */
////		protected void onProgressUpdate(Integer... values) {
////			int vlaue = values[0]; 
////		} 
////
////	}
//  
////  class  MovieDataAsyncTask extends AsyncTask<Integer, Integer, Object> { 
////		/**
////		 * 这里的Integer参数对应AsyncTask中的第一个参数 
////		 * 这里的String返回值对应AsyncTask的第三个参数
////		 * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
////		 * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
////		 */
////		protected Object doInBackground(Integer... params) {
////			DataMode dataMode = new DataMode(context); 
////			 	String path = Contents.url+"pageindex="+pageindex+"&pagesize=20"+"&type="+"movie"+"&year="
////			 			+"2013"+"&area="+"all"+"&cli="+Contents.cli+"&ver="+Contents.version+"&category="+"all";
////
////			return  dataMode.getMediaData(path, homeResMovie,handler);
////		}
////
////
////		/**
////		 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
////		 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
////		 */
////		protected void onPostExecute(Object result) {
////			try {
////				if(result !=null){ 
////					  homeResMovie =  (HomeResponse)result; 
////					  ArrayList<VideoInfo> movie = new ArrayList<VideoInfo>();
////					  movie =  homeResMovie.getResult(); 
////					  for(int i = 0;i < movie.size();i++){
////						  VideoInfo video =  movie.get(i);
////						  movieVideo.add(video);
////					  }  
////						movieAdapter = new MovieAdapter(context, movieVideo,handler); 
////						listviewTop.setAdapter(movieAdapter); 
////				} 
////				 if(mLoadingPreView !=null){
////					 MainTabHomeView.this.mLoadingPreView.setVisibility(View.GONE);
////					 MainTabHomeView.this.context.findViewById(R.id.layout_loading).setVisibility(View.GONE);
////				 }
////			} catch (Exception e) { 
////			} 
////		}
////
////
////		//该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
////		protected void onPreExecute() { 
//////			mainHandler.sendEmptyMessage(UIUtils.StrDialog);
////		}
////
////
////		/**
////		 * 这里的Intege参数对应AsyncTask中的第二个参数
////		 * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
////		 * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
////		 */
////		protected void onProgressUpdate(Integer... values) {
////			int vlaue = values[0]; 
////		}  
////  }; 
//  
// 
//} 