package cn.hi321.android.media.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.android.media.entity.BaiDuMediaInfo;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.entity.BaiduResolution;
import cn.hi321.android.media.entity.Sites;
import cn.hi321.android.media.entity.Media;
import cn.hi321.android.media.entity.MediaItem; 
import cn.hi321.android.media.http.IBindData;
import cn.hi321.android.media.http.NetWorkTask;
import cn.hi321.android.media.player.SystemPlayer;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.Contents;
import cn.hi321.android.media.utils.ImageLoader;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;
import cn.hi321.android.media.widget.MainListview;

import com.android.china.R;
/**
 * @author 杨光福老师
 * 媒体列表页面
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class MediaShowActivity   extends BaseActivity implements IBindData{

	private ImageButton returnButton;;
	private TextView title;
	private Intent mIntent;
	private int flags; 
	private ImageView  image;
	private  ImageLoader imageLoader; 
	private TextView director;
	private TextView craw;
	private TextView area;
//	private TextView describe; 
//	private RelativeLayout mScrollLayout; 
	private Button playButton;
//	private int id;
	private String url; 
	private  Media mMeida; 
	public  final int APP_PAGE_SIZE = 20;//一页最多20
	private int PAGE_SIZE = 12;
	private LinearLayout changeView;  
	private int PageCount; 
//	private Videos videos = null; 
	private LinearLayout changePlayView;//切换播放地址
//	private TextView describeTextview; 
	private BaiDuMediaInfo baiduMediaInfo;
	private ArrayList<MediaItem> arrVideo;
	private Sites movieSites;
	private String videoMovieUrl;
	
	private BaiDuRecommend voide = null;
	private RelativeLayout lin;
	
	private TextView describeTextview; 
	private TextView playListText;
	private LinearLayout linear;
	
	@Override
	protected void onCreate(Bundle paramBundle) { 
		super.onCreate(paramBundle);
		setContentView(R.layout.media_show_activity); 
		 ActivityHolder.getInstance().addActivity(this);
		returnButton =(ImageButton)findViewById(R.id.btn_logo);
		title = (TextView)findViewById(R.id.tv_title);
		director =(TextView)findViewById(R.id.director);
		image = (ImageView)findViewById(R.id.about_video_image); 
		craw =(TextView)findViewById(R.id.craw);
		area =(TextView)findViewById(R.id.area);  
		this.imageLoader = new ImageLoader(MediaShowActivity.this.getApplicationContext(),R.drawable.bg_list_default);
		describeTextview = (TextView)findViewById(R.id.describeTextview); 
		describeTextview.setOnClickListener(myOnClick); 
		playListText = (TextView)findViewById(R.id.playListText); 
		playListText.setOnClickListener(myOnClick); 
		changeView = (LinearLayout)findViewById(R.id.changeView);
		linear = (LinearLayout)findViewById(R.id.linear);
		linear.setVisibility(View.GONE);
		
//		mScrollLayout = (RelativeLayout)findViewById(R.id.gridview);
	    playButton =(Button)findViewById(R.id.videoPlayButton);  
	    playButton.setVisibility(View.GONE);
	    describeTextview = (TextView)findViewById(R.id.describeTextview);
	    describeTextview.setVisibility(View.GONE);
	 
		lin = (RelativeLayout)findViewById(R.id.lin);
		changePlayView =(LinearLayout)findViewById(R.id.changePlayView);
	 //
		 
		mIntent = getIntent();  
		if(mIntent != null){
			voide = (BaiDuRecommend) mIntent.getSerializableExtra("BaiDuRecommend");
			 
		     url =  Contents.XiangQingInfo+"?worktype=adnative"+voide.getWorks_type()+"&id="+voide.getWorks_id()+"&site=";//voide.getUrl();
			
			if(UIUtils.hasNetwork(MediaShowActivity.this)){
				Utils.startWaitingDialog(MediaShowActivity.this); 
					new NetWorkTask().execute(MediaShowActivity.this,UIUtils.GEG_MEDIA_DATA,
							url,mainHandler); 
	  		  }else{
	  			UIUtils.showToast(MediaShowActivity.this, MediaShowActivity.this.getText(R.string.tip_network).toString());
				 
	  		  }
		}  
		returnButton.setOnClickListener(myOnClick); 
		playButton.setOnClickListener(myOnClick); 
	 
	} 
	  protected void onDestroy() {
			super.onDestroy();
			 ActivityHolder.getInstance().removeActivity(this);
		}
  
	class EpisodeAdapter extends BaseAdapter{
		ArrayList<MediaItem> videosArr;  
		private LayoutInflater mInflater; 
		
		public EpisodeAdapter(ArrayList<MediaItem> episode ) {
			this.mInflater = LayoutInflater.from(MediaShowActivity.this); 
			this.videosArr = episode ; 
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(videosArr!=null&&videosArr.size()>0){
				return videosArr.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) { 
			if(videosArr!=null&&videosArr.size()>0){
				return videosArr.get(position);
			}
			return 0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) { 
		 
			 View v;
				if(convertView == null ){
					v =  mInflater.inflate(R.layout.listview_button, null);
				}else{
					v = convertView;
				} 
			    try { 
			    	if(videosArr!=null&&videosArr.size() > position){
			    		 Button button = (Button)v.findViewById(R.id.button); 
			    		final MediaItem video = videosArr.get(position); 
			    		if(mMeida!=null) 
				    	button.setText("  "+video.getEpisode()+":"+video.getTitle());  
				    	button.setOnClickListener(new OnClickListener() { 
							@Override
							public void onClick(View v) {
								try {
									mMeida.setPosition(position)  ;
									setPlayerUrl(); 
									
								} catch (Exception e) { 
								}
								
							}
						}); 
			    	}
			     
				} catch (Exception e) { 
				}  
			return  v;
		} 
	} 
	private void setPlayerUrl(){
		try {  
			if(mMeida != null){ 
				  
				
				Intent intent = new Intent(MediaShowActivity.this,SystemPlayer.class);  
				Bundle mBundle = new Bundle();
				if(mMeida != null)
				mBundle.putSerializable("media", mMeida);
				intent.putExtras(mBundle); 
				startActivity(intent); 
				 overridePendingTransition(R.anim.fade, R.anim.hold); 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void setChangePlayUrlButton(ArrayList<Sites> arrSites){ 
		
		if(arrSites!=null &&arrSites.size()>0){ 
				changePlayView.removeAllViews();
			    GridView appPage  = new GridView(MediaShowActivity.this);
				appPage.setNumColumns(5); 
				appPage.setHorizontalSpacing(5);
				appPage.setVerticalSpacing(5);
				ChangePlayAdapter myAppAdapter = new ChangePlayAdapter(arrSites);
		    	appPage.setAdapter(myAppAdapter);
		    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
		    	changePlayView.addView(appPage);
		}
		   
	}
 
	
	private int changeButtonIndex =0;
	class ChangePlayAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		private ArrayList<Sites> mVideosPlay ;
		private ImageLoader changeImageLoader; 
		ChangePlayAdapter(ArrayList<Sites> mVide  ){
			this.mVideosPlay = mVide;
			this.mInflater = LayoutInflater.from(MediaShowActivity.this); 
			changeImageLoader = new ImageLoader(MediaShowActivity.this.getApplicationContext());
			
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mVideosPlay.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mVideosPlay.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			 View v;
				if(convertView == null ){
					v =  mInflater.inflate(R.layout.tvactivity_item, null);
				}else{
					v = convertView;
				}
				TextView txt = (TextView)v.findViewById(R.id.txt_id);
				ImageView imageView = (ImageView)v.findViewById(R.id.imageView1);
				if(changeButtonIndex == position){
					imageView.setBackgroundResource(R.drawable.bg_oriange_line_long);	
				} else{
					imageView.setBackgroundResource(R.drawable.bg_tab_my);
				} 
				ImageView VideoSource = (ImageView)v.findViewById(R.id.VideoSource);
				VideoSource.setVisibility(View.VISIBLE);
		 
			    if(mVideosPlay !=null&& mVideosPlay.size() >position){    
				
					final Sites vid = mVideosPlay.get(position); 
					String name = vid.getSite_name();  
					txt.setText(name+ "");  
					changeImageLoader.DisplayImage(vid.getSite_logo(), VideoSource);
					txt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
//							changeButtonIndex = position; 
//							notifyDataSetChanged(); 
//							refleshPlaySite(vid.getSite_url()); 
//							imagePostion = 0; 
							
							changeButtonIndex = position; 
							notifyDataSetChanged(); 
							if(voide.getWorks_type().equals("movie")){
								if(mMeida.getSitesArr()!=null&&mMeida.getSitesArr().size()>0){  
								    setMediaItemMovie(vid);
								}
							} 
							refleshPlaySite(vid.getSite_url()); 
//							imagePostion = 0; 
						}
					});  
					
			}
			
			return v;
		}
		
	}
	/**
	 * 刷新界面，更换播放源地址
	 * */
	private void refleshPlaySite(String site){
		//如果是电影就直接播放了
		if( voide.getWorks_type()!=null&&voide.getWorks_type().equals("movie")&& movieSites!=null){
			videoMovieUrl = site; 
		} else{
			//再次请求
			String getPlayUrl = Contents.XiangQingSingle +"?worktype=adnative"+voide.getWorks_type()
					+"&id="+mMeida.getId()+"&site="+site;
			new NetWorkTask().execute(MediaShowActivity.this,UIUtils.GEG_MEDIA_PLAY_URL,
					getPlayUrl,mainHandler);
		}
	
	}
	 

	OnClickListener myOnClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_logo:
					MediaShowActivity.this.finish();
					break; 
				case R.id.videoPlayButton: 
					if(mMeida!=null ){
						if(voide.getWorks_type().equals("movie")){
							if(mMeida.getSitesArr()!=null&&mMeida.getSitesArr().size()>0){ 
							    Sites vid = mMeida.getSitesArr().get(0); 
							    setMediaItemMovie(vid);
							}
						} 
						mMeida.setPosition(0)  ;
						setPlayerUrl();
					}
					break;
				case R.id.describeTextview:   
					changeView(true);
					break;
				case R.id.playListText:  
					changeView(false);
					break;
				default:
					break;
				}
			}
		};
		
		private void setMediaItemMovie(Sites vids ){
			 
			    Sites vid = vids ; 
				ArrayList<MediaItem> mMediaItemArrayList = new ArrayList<MediaItem>();
				MediaItem mMediaItem = new MediaItem(); 
				mMediaItem.setTitle(mMeida.getTitle());
				mMediaItem.setSourceUrl(vid.getSite_url());
				mMediaItem.setImage(vid.getSite_logo());
				mMediaItemArrayList.add(mMediaItem); 
				mMeida.setMediaItemArrayList(mMediaItemArrayList); 
		}
		
		/**
		 * false表示显示列表
		 * */
		private void changeView(boolean isDescribe){
			changeView.removeAllViews();
			if(isDescribe){//简介
				describeTextview.setBackgroundResource(R.drawable.collect_edit_ico);
				playListText.setBackgroundResource(R.drawable.collect_edit_press_ico);
				TextView textview = new TextView(MediaShowActivity.this);
				if(mMeida!=null)
				textview.setText(mMeida.getIntro()+""); 
				changeView.addView(textview);
			}else{
				describeTextview.setBackgroundResource(R.drawable.collect_edit_press_ico);
				playListText.setBackgroundResource(R.drawable.collect_edit_ico);
			
				MainListview appPage  = new MainListview(MediaShowActivity.this); 
				EpisodeAdapter myAppAdapter = new EpisodeAdapter(arrVideo);
		    	appPage.setAdapter(myAppAdapter);
		    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
		    	changeView.addView(appPage); 
			}
		}
		
//		private void playView(String videoUrl){
//			if(!Utils.isEmpty(videoUrl)){
//				try {
//					BaiduResolution playDatas = new BaiduResolution();
//					playDatas.setSourceUrl(videoUrl);
//					if(arrVideo!=null&&arrVideo.size()>0){
//						mItem = ""+arrVideo.get(0).getEpisode(); 
//					} 
//					String baiduService = "http://gate.baidu.com/tc?m=8&video_app=1&ajax=1&src="+videoUrl;
//					if(!Utils.isEmpty(baiduService)){ 
//						if(UIUtils.hasNetwork(MediaShowActivity.this)){
//							Utils.startWaitingDialog(MediaShowActivity.this);
//							new NetWorkTask().execute(MediaShowActivity.this,UIUtils.GET_PLAY_DATA,
//									baiduService,playDatas,mainHandler);
//				  		  }else{
//				  			UIUtils.showToast(MediaShowActivity.this, MediaShowActivity.this.getText(R.string.tip_network).toString());
//						 }
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		}
		
	private String mMediaName = null;
	private String mItem = "";
	@Override
	public void bindData(int tag, Object object) {
		if(tag == UIUtils.GEG_MEDIA_DATA){ 
			if(object!=null){
				try {
					   mMeida = (Media)object;    
						   if(mMeida!=null){
							    mMediaName = mMeida.getTitle(); 
							    mMeida.setMediaType(voide.getWorks_type());
							    title.setText(""+mMediaName); 
							    
							    if(mMeida.getDirectorArr()!=null &&mMeida.getDirectorArr().size()>0){
							    	String s = "";
							    	for(int i=0;i<mMeida.getDirectorArr().size();i++){
							    		String director = mMeida.getDirectorArr().get(i);
							    		s += director+"  ";
							    	}
							    	director.setText("导演："+s+"");
							    }
							    if(mMeida.getActorArr()!=null &&mMeida.getActorArr().size()>0){
							    	String s = "";
							    	for(int i=0;i<mMeida.getActorArr().size();i++){
							    		String actor = mMeida.getActorArr().get(i);
							    		s += actor+"   ";
							    	}
							    	craw.setText("演员："+s+"");
							    }
							    describeTextview.setText("简介");
							    playListText.setText("播放列表");
							    changeView(false); 
								
								 if(mMeida.getAreaArr()!=null &&mMeida.getAreaArr().size()>0){
								    	String s = "";
								    	for(int i=0;i<mMeida.getAreaArr().size();i++){
								    		String actor = mMeida.getAreaArr().get(i);
								    		s += actor+"   ";
								    	}
								    	area.setText("地区："+s+"");
								 }
								     
								if(mMeida.getImg_url()!=null&&!mMeida.getImg_url().equals("")){
									lin.setBackgroundResource(R.drawable.video_pic_bg);
									imageLoader.DisplayImage(mMeida.getImg_url(), image);
								}
								
								if(mMeida.getSitesArr()!=null&&mMeida.getSitesArr().size()>0){ 
									 movieSites = mMeida.getSitesArr().get(0); 
									 changePlayView.setVisibility(View.VISIBLE);
									 playButton.setVisibility(View.VISIBLE); 
									 describeTextview.setVisibility(View.VISIBLE);
									 setChangePlayUrlButton(mMeida.getSitesArr());
									 Utils.closeWaitingDialog();
								}else{
									//再次请求
									String getPlayUrl = Contents.XiangQingSingle +"?worktype=adnative"+voide.getWorks_type()
											+"&id="+mMeida.getId()+"&site=";
									new NetWorkTask().execute(MediaShowActivity.this,UIUtils.GEG_MEDIA_PLAY_URL,
											getPlayUrl,mainHandler);
								}
							
						   }
						  
						   linear.setVisibility(View.VISIBLE);	 
				} catch (Exception e) { 
				} 
			}
		}else if(tag == UIUtils.GEG_MEDIA_PLAY_URL){
			if(object!=null){
				try {
				   baiduMediaInfo = (BaiDuMediaInfo)object; 
				   arrVideo = baiduMediaInfo.getVideosArray();
				   mMeida.setMediaItemArrayList(arrVideo);
				   if(arrVideo!=null&&arrVideo.size() >= 1){
					   //默认写0；这里表示有很多家视频可以播放，等以后视频切换的时候在打开

						    describeTextview.setVisibility(View.VISIBLE); 
						    playButton.setVisibility(View.VISIBLE);
						    changeView(false); 
							if(baiduMediaInfo.getSitesArray()!=null){
								setChangePlayUrlButton(baiduMediaInfo.getSitesArray());
							}
				   } else{
					   playButton.setVisibility(View.VISIBLE);
				   } 
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			Utils.closeWaitingDialog();
		}
//		else if(tag == UIUtils.GET_PLAY_DATA){ 
//			if(object!=null){
//				try { 
//					BaiduResolution  mediaItem = (BaiduResolution)object;
//					if(mediaItem != null){
//						String video_source_url =mediaItem.getVideo_source_url();
//						if(!Utils.isEmpty(video_source_url)){
//							Intent intent = new Intent(MediaShowActivity.this,SystemPlayer.class);
//							MediaItem  videoInfo = new MediaItem();
//							videoInfo.setUrl(video_source_url);
//							videoInfo.setSourceUrl(mediaItem.getmSourceUrl());
//							if(!Utils.isEmpty(mItem)){
//								mItem ="  第"+mItem+"集";
//							}
//							String mediaName = mMediaName+mItem;
//							videoInfo.setTitle(mediaName);
//							videoInfo.setLive(false);
//							Bundle mBundle = new Bundle();
//							if(videoInfo != null)
//							mBundle.putSerializable("VideoInfo", videoInfo);
//							intent.putExtras(mBundle);
//	//						intent.setAction(Intent.ACTION_VIEW);
//	//						intent.setDataAndType(Uri.parse(video_source_url),"video/*");
//							startActivity(intent);
//							mItem = "";
//						}else{
//							String sourceUri = mediaItem.getSourceUrl();
//							if(!Utils.isEmpty(sourceUri)){
//								Intent intent = new Intent();
//								intent.setAction(Intent.ACTION_VIEW);
//								intent.setData(Uri.parse(sourceUri));
//								startActivity(intent);
//							}else{
//								Toast.makeText(MediaShowActivity.this, "该视频无法播放", 1).show();
//							}
//							
//						}
//						
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				Utils.closeWaitingDialog();
//			}
//		}
		
	}  
	
	 
}
