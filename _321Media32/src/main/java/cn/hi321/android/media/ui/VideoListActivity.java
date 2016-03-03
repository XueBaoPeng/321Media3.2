package cn.hi321.android.media.ui;
 
import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.android.media.adapter.ChannelOtherAdapter;
import cn.hi321.android.media.entity.BaiDuChannelVideo;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.entity.ChannelOtherVideos;
import cn.hi321.android.media.entity.BaiduResolution;
import cn.hi321.android.media.entity.Media;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.http.IBindData;
import cn.hi321.android.media.http.NetWorkTask;
import cn.hi321.android.media.player.SystemPlayer;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;

import com.android.china.R;

public class VideoListActivity extends BaseActivity implements IBindData
{
	
	private BaiDuChannelVideo channelVideo;
	private ImageButton returnButton;
	private TextView title;
	private ImageButton btn_search;  
	private GridView  channleGridView;
//	private ChannelInfo channelInfo; 
//	RecommendMovice recommendMovieHot;
	private boolean isGetData = false;
	private TextView channelHot;
	private TextView channelNew;
	private ArrayList<ChannelOtherVideos> arrayList;
	private ArrayList<ChannelOtherVideos> arrayListHot;
	private ChannelOtherAdapter adapter;
	private String tag ;
	private boolean isHostData = false;
	private int indexHost = 1;
	private int indexNew = 1;
	
	private ProgressBar progressbar ;
	private LinearLayout progressLinear;
	TextView bottomText;
	
	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.videolist_activity);
		 ActivityHolder.getInstance().addActivity(this);
		returnButton =(ImageButton)findViewById(R.id.btn_logo);
		title = (TextView)findViewById(R.id.tv_title); 
		btn_search =(ImageButton)findViewById(R.id.btn_search);
		btn_search.setVisibility(View.GONE);
		channelHot = (TextView)findViewById(R.id.channel_video_hot);
		channelHot.setOnClickListener(myOnClick);
		channelNew = (TextView)findViewById(R.id.channel_video_new);
		channelNew.setOnClickListener(myOnClick);
		channleGridView =(GridView)findViewById(R.id.view_main_tab_channle_grid);  
		progressbar = (ProgressBar)findViewById(R.id.progressbar_loading);
		progressLinear = (LinearLayout)findViewById(R.id.progressLinear);
		progressbar.setVisibility(View.GONE);
		progressLinear.setVisibility(View.GONE); 
	     bottomText = (TextView)findViewById(R.id.load_more_textview);
//		channelInfo = new ChannelInfo();
		arrayList =new ArrayList<ChannelOtherVideos>();
		arrayListHot = new ArrayList<ChannelOtherVideos>();
		Intent i = getIntent(); 
		if(i.getSerializableExtra("channelVideoInfo")!=null){
			try {
				channelVideo = (BaiDuChannelVideo)i.getSerializableExtra("channelVideoInfo");
				if(channelVideo!=null){
					String base_url = channelVideo.getBase_url()+"1.js"; 
				    tag = channelVideo.getTag();
					title.setText(channelVideo.getName()+"");
					if(base_url!=null&&!base_url.equals("")&&!base_url.equals("null")&&UIUtils.hasNetwork(VideoListActivity.this)){ 
						Utils.startWaitingDialog(VideoListActivity.this); 
						new NetWorkTask().execute(VideoListActivity.this,UIUtils.Channel_Video_View,
								base_url,arrayList,tag,mainHandler); 
					}
				
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} 
		
		returnButton.setOnClickListener(myOnClick);
		btn_search.setOnClickListener(myOnClick);
		channleGridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
			   if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			      //判断是否滚动到底部
			      if ((view.getLastVisiblePosition() == view.getCount() - 1)&&!isGetData) { 
			    	   
			    		  try {
					    	   if(isHostData){//表示最热 
					    		
					    		   if(arrayListHot!=null&&arrayListHot.get(0).getVideo_num() !=null){
					    			   int count =Integer.parseInt(arrayListHot.get(0).getVideo_num() ) ;
					    			   if(count ==  view.getCount()){
					    					progressbar.setVisibility(View.GONE); 
					    					 bottomText.setText("全部加载完了");
					    					 bottomText.setVisibility(View.VISIBLE);
									    	progressLinear.setVisibility(View.VISIBLE);
					    			   }else{
					    				    indexHost = indexHost + 1; 
							    			progressbar.setVisibility(View.VISIBLE);
									    	progressLinear.setVisibility(View.VISIBLE);
									    	 bottomText.setText("正在加载数据");
									    	 bottomText.setVisibility(View.VISIBLE);
							    			String hotUrl = channelVideo.getBase_url()+"hot/"+indexHost+".js";  
											
											if(hotUrl!=null&&!hotUrl.equals("")&&!hotUrl.equals("null")&&UIUtils.hasNetwork(VideoListActivity.this)){ 
//												Utils.startWaitingDialog(VideoListActivity.this);  
												new NetWorkTask().execute(VideoListActivity.this,UIUtils.Channel_Video_View_hot,
														hotUrl,arrayListHot,tag,mainHandler); 
											}
					    			   }
					    		   }
					    			
					    	  }else{//最新
					    		  if(arrayList!=null&&arrayList.get(0).getVideo_num() !=null){
					    			   int count =Integer.parseInt(arrayList.get(0).getVideo_num() ) ;
					    			   if(count ==  view.getCount()){
					    					progressbar.setVisibility(View.GONE); 
					    					 bottomText.setText("全部加载完了");
					    					 bottomText.setVisibility(View.VISIBLE);
									    	progressLinear.setVisibility(View.VISIBLE);
					    			   }else{
					    				   indexNew = indexNew + 1;
					    					progressbar.setVisibility(View.VISIBLE);
									    	progressLinear.setVisibility(View.VISIBLE);
									    	 bottomText.setText("正在加载数据");
									    	 bottomText.setVisibility(View.VISIBLE);
							    		    String base_url = channelVideo.getBase_url()+indexNew+".js";  
											if(base_url!=null&&!base_url.equals("")&&!base_url.equals("null")&&UIUtils.hasNetwork(VideoListActivity.this)){ 
											  new NetWorkTask().execute(VideoListActivity.this,UIUtils.Channel_Video_View,
														base_url,arrayList,tag,mainHandler); 
											}
					    			   }
					    		   } 
					    	  }
							} catch (Exception e) {
								// TODO: handle exception
							}  
				      
			      }
			     } 
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			
			
			}
		});
		
		channleGridView.setOnItemClickListener(new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Toast.makeText(VideoListActivity.this, "jintu", 1).show();
				if(UIUtils.hasNetwork(VideoListActivity.this)){  
					try {
						if(isHostData){ 
							if(arrayListHot!=null&&arrayListHot.size()>arg2){ 
								ChannelOtherVideos video = arrayListHot.get(arg2);
								 if(video!=null){
									 Utils.playView(setMedia(video),VideoListActivity.this); 
								 }	
							}
						}else{
							if(arrayList!=null&&arrayList.size()>arg2){ 
								ChannelOtherVideos video = arrayList.get(arg2);
								 if(video!=null){
									 Utils.playView(setMedia(video),VideoListActivity.this);
								 }	
							}
							
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}else{
					UIUtils.showToast(VideoListActivity.this,VideoListActivity.this.getText(R.string.tip_network).toString());
				}
				
				
			}
		});
		  
	}
	
	
	  
	  private Media setMedia(ChannelOtherVideos video){
		    Media mMedia = new Media();
			ArrayList<MediaItem> mMediaItemArrayList = new ArrayList<MediaItem>();
			MediaItem mMediaItem = new MediaItem();
			mMediaItem.setLive(false);
			mMediaItem.setTitle(video.getTitle());
			mMediaItem.setSourceUrl(video.getUrl());
			mMediaItem.setImage(video.getImgh_url()); 
			mMediaItemArrayList.add(mMediaItem);
			mMedia.setMediaItemArrayList(mMediaItemArrayList);
			mMedia.setPosition(0);
			mMedia.setMediaType(channelVideo.getTag());
			return mMedia;
	  }
	
	OnClickListener myOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_logo:
				VideoListActivity.this.finish();
				break;
			case R.id.channel_video_hot://最热 
				try {
					isHostData = true;
					channelHot.setBackgroundResource(R.drawable.ad_title_right_new_press);
					channelNew.setBackgroundResource(R.drawable.ad_title_left_new);
					if(arrayListHot==null||(arrayListHot!=null&&arrayListHot.size()==0)){
						if(channelVideo!=null){
							String hotUrl = channelVideo.getBase_url()+"hot/1.js";  
							
							if(hotUrl!=null&&!hotUrl.equals("")&&!hotUrl.equals("null")&&UIUtils.hasNetwork(VideoListActivity.this)){ 
								Utils.startWaitingDialog(VideoListActivity.this);  
								new NetWorkTask().execute(VideoListActivity.this,UIUtils.Channel_Video_View_hot,
										hotUrl,arrayListHot,tag,mainHandler); 
							}
						
						}
					}else{
						refleshView(arrayListHot);
					}
				
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
				break;
			case R.id.channel_video_new://最新
				try {
					isHostData = false;
					channelHot.setBackgroundResource(R.drawable.ad_title_right_new);
					channelNew.setBackgroundResource(R.drawable.ad_title_left_new_press);
					if(arrayList == null||(arrayList!=null&&arrayList.size() == 0 )){
						if(channelVideo!=null){
							String base_url = channelVideo.getBase_url()+"1.js"; 
							
							if(base_url!=null&&!base_url.equals("")&&!base_url.equals("null")&&UIUtils.hasNetwork(VideoListActivity.this)){ 
								Utils.startWaitingDialog(VideoListActivity.this); 
								arrayList =new ArrayList<ChannelOtherVideos>();
								new NetWorkTask().execute(VideoListActivity.this,UIUtils.Channel_Video_View,
										base_url,arrayList,tag,mainHandler); 
							}
						
						}
					}else{
						refleshView(arrayList);
					}
				} catch (Exception e) {
					// TODO: handle exception
				} 
		
				break; 
			default:
				break;
			}
		} 
	};
	
	 @Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			 ActivityHolder.getInstance().removeActivity(this);
		}

	private Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
		
	};
	private void refleshView(ArrayList<ChannelOtherVideos> array){
		if(adapter ==null){
			adapter = new ChannelOtherAdapter(VideoListActivity.this, array, myHandler);
			channleGridView.setAdapter(adapter);
		}else{
			adapter.setData(array);
			adapter.notifyDataSetChanged();
		}
	}
	private String mMediaName = null;
//	private String mItem = "";
	@Override
	public void bindData(int tag, Object object) {
		try {
			if(object == null ){
				Utils.closeWaitingDialog();
				progressbar.setVisibility(View.GONE);
				progressLinear.setVisibility(View.GONE);
				 bottomText.setVisibility(View.GONE);
			}
			
			if(tag == UIUtils.Channel_Video_View){
				if(object!=null){
					arrayList =(ArrayList<ChannelOtherVideos>)object ;  
					refleshView(arrayList);
					Utils.closeWaitingDialog();
				}
				progressbar.setVisibility(View.GONE);
		    	progressLinear.setVisibility(View.GONE); 
		    	 bottomText.setVisibility(View.GONE);
			
			}else if(tag == UIUtils.Channel_Video_View_hot){
				if(object!=null){
					arrayListHot =(ArrayList<ChannelOtherVideos>)object ;  
					refleshView(arrayListHot);
					Utils.closeWaitingDialog();
				}
			}else if(tag == UIUtils.Channel_ScrollStateChanged){
				if(object!=null){ 
					if(isHostData){
						arrayListHot = (ArrayList<ChannelOtherVideos>)object;
						refleshView(arrayListHot);
					}else{
						arrayList = (ArrayList<ChannelOtherVideos>)object;
						refleshView(arrayList);
					}
					isGetData = false; 
				}
				progressbar.setVisibility(View.GONE);
				progressLinear.setVisibility(View.GONE);
				bottomText.setVisibility(View.GONE);
			}
//			else if(tag == UIUtils.GET_PLAY_DATA){ 
//				if(object!=null){
//					try { 
//						BaiduResolution  mediaItem = (BaiduResolution)object;
//						if(mediaItem != null){
//							String video_source_url =mediaItem.getVideo_source_url();
//							if(!Utils.isEmpty(video_source_url)){
//								Intent intent = new Intent(VideoListActivity.this,SystemPlayer.class);
//								MediaItem  videoInfo = new MediaItem();
//								videoInfo.setUrl(video_source_url);
//								videoInfo.setSourceUrl(mediaItem.getmSourceUrl());
//								
//								
////								if(!Utils.isEmpty(mItem)){
////									mItem ="  第"+mItem+"集";
////								}
//								String mediaName = mMediaName;
//								videoInfo.setTitle(mediaName);
//								videoInfo.setLive(false);
//								Bundle mBundle = new Bundle();
//								if(videoInfo != null)
//								mBundle.putSerializable("VideoInfo", videoInfo);
//								intent.putExtras(mBundle);
//		//						intent.setAction(Intent.ACTION_VIEW);
//		//						intent.setDataAndType(Uri.parse(video_source_url),"video/*");
//								startActivity(intent);
//								 overridePendingTransition(R.anim.fade, R.anim.hold); 
////								mItem = "";
//							}else{
//								String sourceUri = mediaItem.getSourceUrl();
//								if(!Utils.isEmpty(sourceUri)){
//									Intent intent = new Intent();
//									intent.setAction(Intent.ACTION_VIEW);
//									intent.setData(Uri.parse(sourceUri));
//									startActivity(intent);
//									 overridePendingTransition(R.anim.fade, R.anim.hold); 
//								}else{
//									Toast.makeText(VideoListActivity.this, "该视频无法播放", 1).show();
//								}
//								
//							}
//							
//						}
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//					Utils.closeWaitingDialog();
//				}
//			}
		} catch (Exception e) {
			// TODO: handle exception
			Utils.closeWaitingDialog();
		}
		
		
	}
	
	
//	private void playView(String videoUrl,ChannelOtherVideos video ){
//		if(!Utils.isEmpty(videoUrl)){
//			try {
//				BaiduResolution playDatas = new BaiduResolution();
//				playDatas.setSourceUrl(videoUrl);
//				if(video!=null ){
//					mMediaName = ""+video.getTitle(); 
//				} 
//				String baiduService = "http://gate.baidu.com/tc?m=8&video_app=1&ajax=1&src="+videoUrl;
//				if(!Utils.isEmpty(baiduService)){ 
//					if(UIUtils.hasNetwork(VideoListActivity.this)){
//						Utils.startWaitingDialog(VideoListActivity.this);
//						new NetWorkTask().execute(VideoListActivity.this,UIUtils.GET_PLAY_DATA,
//								baiduService,playDatas,mainHandler);
//			  		  }else{
//			  			UIUtils.showToast(VideoListActivity.this, VideoListActivity.this.getText(R.string.tip_network).toString());
//					 }
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//	}
//	
 
}