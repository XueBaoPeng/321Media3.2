package cn.hi321.android.media.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.android.media.entity.BaiDuMediaInfo;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.entity.BaiduResolution;
import cn.hi321.android.media.entity.Media;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.entity.Sites;
import cn.hi321.android.media.http.IBindData;
import cn.hi321.android.media.http.NetWorkTask;
import cn.hi321.android.media.player.SystemPlayer;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.Contents;
import cn.hi321.android.media.utils.ImageLoader;
import cn.hi321.android.media.utils.LogUtil;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;

import com.android.china.R;
/**
 * @author 杨光福老师
 * 媒体播放页面
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class MediaActivity extends BaseActivity implements IBindData{

	private ImageButton returnButton;;
	private TextView title;
	private Intent mIntent;
	private int flags; 
	private ImageView  image;
	private  ImageLoader imageLoader; 
	private TextView director;
	private TextView craw;
	private TextView area;
	private TextView describe; 
	private RelativeLayout mScrollLayout; 
	private Button playButton;
//	private int id;
	private String url; 
	private Media mMedia; 
	public  final int APP_PAGE_SIZE = 20;//一页最多20
	private int PAGE_SIZE = 12;
	private LinearLayout bagPage; //显示页数
	private int PageCount; 
//	private Videos videos = null; 
	private LinearLayout changePlayView;//切换播放地址
	private TextView describeTextview; 
	private BaiDuMediaInfo baiduMediaInfo;
	private ArrayList<MediaItem> arrVideo;
	private Sites movieSites;
	private String videoMovieUrl;
	
	private BaiDuRecommend voide = null;
	private RelativeLayout lin; 
	
	
	@Override
	protected void onCreate(Bundle paramBundle) { 
		super.onCreate(paramBundle);
		setContentView(R.layout.about_video); 
		 ActivityHolder.getInstance().addActivity(this);
		returnButton =(ImageButton)findViewById(R.id.btn_logo);
		title = (TextView)findViewById(R.id.tv_title);
		director =(TextView)findViewById(R.id.director);
		image = (ImageView)findViewById(R.id.about_video_image); 
		craw =(TextView)findViewById(R.id.craw);
		area =(TextView)findViewById(R.id.area);  
		this.imageLoader = new ImageLoader(MediaActivity.this.getApplicationContext(),R.drawable.bg_list_default);
		describe = (TextView)findViewById(R.id.describe); 
		mScrollLayout = (RelativeLayout)findViewById(R.id.gridview);
	    playButton =(Button)findViewById(R.id.videoPlayButton);  
	    playButton.setVisibility(View.GONE);
	    describeTextview = (TextView)findViewById(R.id.describeTextview);
	    describeTextview.setVisibility(View.GONE);
		bagPage = (LinearLayout)findViewById(R.id.bagpanelpage); 
		bagPage.setVisibility(View.GONE);
		lin = (RelativeLayout)findViewById(R.id.lin);
		changePlayView =(LinearLayout)findViewById(R.id.changePlayView); 
		mIntent = getIntent();  
		if(mIntent != null){
			voide = (BaiDuRecommend) mIntent.getSerializableExtra("BaiDuRecommend");
			 
		     url =  Contents.XiangQingInfo+"?worktype=adnative"+voide.getWorks_type()+"&id="+voide.getWorks_id()+"&site=";//voide.getUrl();
			
			if(UIUtils.hasNetwork(MediaActivity.this)){
				Utils.startWaitingDialog(MediaActivity.this); 
					new NetWorkTask().execute(MediaActivity.this,UIUtils.GEG_MEDIA_DATA,
							url,mainHandler); 
			
	  		  }else{
	  			UIUtils.showToast(MediaActivity.this, MediaActivity.this.getText(R.string.tip_network).toString());
				 
	  		  }
		}  
		returnButton.setOnClickListener(myOnClick); 
		playButton.setOnClickListener(myOnClick); 
	 
	} 
	
 
	 
	class EpisodeAdapter extends BaseAdapter{
//		ArrayList<Videos> videosArr;
		private int mPage; 
		private LayoutInflater mInflater;
		private ArrayList<MediaItem>  bagItemList;
		 
		public EpisodeAdapter(ArrayList<MediaItem> episode,int page) {
			this.mInflater = LayoutInflater.from(MediaActivity.this); 
//			this.videosArr = episode ;
			mPage = page;
			bagItemList = new ArrayList<MediaItem>(); 
			int i = page * APP_PAGE_SIZE;
			int iEnd = i+APP_PAGE_SIZE;  
			
			while ((i<episode.size()) && (i<iEnd)) { 
				bagItemList.add(episode.get(i));
				i++;
			} 
			 
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(bagItemList!=null&&bagItemList.size()>0){
				return bagItemList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) { 
			if(bagItemList!=null&&bagItemList.size()>0){
				return bagItemList.get(position);
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
					v =  mInflater.inflate(R.layout.playbutton_item, null);
				}else{
					v = convertView;
				}
			  
			  
			    try { 
			    	if(bagItemList!=null&&bagItemList.size() > position){
			    		Button button = (Button)v.findViewById(R.id.button); 
			    		final MediaItem video = bagItemList.get(position);
			    		 
			    		String seq = video.getEpisode(); 
				    	button.setText( seq+"");  
				    	button.setOnClickListener(new OnClickListener() {
//							
							@Override
							public void onClick(View v) {
								try {
									int myPosition = (mPage * APP_PAGE_SIZE)+position;
									mMedia.setPosition(myPosition  ) ; 
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
			if(mMedia != null){ 
				Intent intent = new Intent(MediaActivity.this,SystemPlayer.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("media", mMedia);
				intent.putExtras(mBundle);
				MediaActivity.this.startActivity(intent);
				 overridePendingTransition(R.anim.fade, R.anim.hold); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void setChangePlayUrlButton(ArrayList<Sites> arrSites){ 
		
		if(arrSites!=null &&arrSites.size()>0){
//			int viewpage = (int)arrSites.size()%PAGE_SIZE==0?
//					arrSites.size()/PAGE_SIZE:arrSites.size()/PAGE_SIZE+1;
					changePlayView.removeAllViews();
				    GridView appPage  = new GridView(MediaActivity.this);
					appPage.setNumColumns(5); 
					appPage.setHorizontalSpacing(5);
					appPage.setVerticalSpacing(5);
					ChangePlayAdapter myAppAdapter = new ChangePlayAdapter(arrSites);
			    	appPage.setAdapter(myAppAdapter);
			    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
			    	changePlayView.addView(appPage);
		}
		   
	}

	private void setViewPageSize( ){ 
		   int viewpage = (int) arrVideo.size()%APP_PAGE_SIZE==0?
				   arrVideo.size()/APP_PAGE_SIZE:arrVideo.size()/APP_PAGE_SIZE+1;
			bagPage.removeAllViews();
			if(viewpage >= 1){ 
				if(viewpage >=5){
					GridView appPage  = new GridView(MediaActivity.this);
					appPage.setNumColumns(5); 
					appPage.setHorizontalSpacing(5);
					appPage.setVerticalSpacing(5);
					TextViewAdapter myAppAdapter = new TextViewAdapter(viewpage);
			    	appPage.setAdapter(myAppAdapter);
			    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
			    	bagPage.addView(appPage);
			    	bagPage.setBackgroundResource(R.drawable.bg_videodetail_serise_mul);
				}else{
					
					if(arrVideo!=null&&arrVideo.size()>0){
						GridView appPage  = new GridView(MediaActivity.this);
						appPage.setNumColumns(5); 
						appPage.setHorizontalSpacing(5);
						appPage.setVerticalSpacing(5);
						TextViewAdapter myAppAdapter = new TextViewAdapter(viewpage);
				    	appPage.setAdapter(myAppAdapter);
				    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
				    	bagPage.addView(appPage);
				    	bagPage.setBackgroundResource(R.drawable.bg_video_detail_website);
					}
					
				}
				    
			}
		   
	} 
	
	
	public PopupWindow popupWindow;
	private GridView gridview;
	private void initPopwindow(View parent, int viewpage ){ 
		View view = LayoutInflater.from(MediaActivity.this)
				.inflate(R.layout.pagepopwindow, null); 
	    gridview = (GridView)view.findViewById(R.id.view_select_grid); 
		popupWindow = new PopupWindow(view, MediaActivity.this.getResources() .getDimensionPixelSize(R.dimen.popmenu_width),
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(parent,3, MediaActivity.this.getResources().getDimensionPixelSize(
				R.dimen.popmenu_yoff)); 
		popupWindow.setFocusable(true); 
		popupWindow.setOutsideTouchable(true); 
		popupWindow.update();
		TextViewAdapterPoP myAppAdapter = new TextViewAdapterPoP(viewpage);
		gridview.setAdapter(myAppAdapter);
	} 
	 
	public void dismiss() {
		popupWindow.dismiss();
	}
	 @Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			 ActivityHolder.getInstance().removeActivity(this);
		}
	
	private int changeButtonIndex =0;
	class ChangePlayAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		private ArrayList<Sites> mVideosPlay ;
		private ImageLoader changeImageLoader;
//		private
		ChangePlayAdapter(ArrayList<Sites> mVide  ){
			this.mVideosPlay = mVide;
			this.mInflater = LayoutInflater.from(MediaActivity.this); 
			changeImageLoader = new ImageLoader(MediaActivity.this.getApplicationContext());
			
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
							changeButtonIndex = position; 
							notifyDataSetChanged(); 
							if(voide.getWorks_type().equals("movie")){
								if(mMedia.getSitesArr()!=null&&mMedia.getSitesArr().size()>0){  
								    setMediaItemMovie(vid);
								}
							} 
							refleshPlaySite(vid.getSite_url()); 
							imagePostion = 0; 
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
			setPlayerUrl(); 
		} else{
			//再次请求
			String getPlayUrl = Contents.XiangQingSingle +"?worktype=adnative"+voide.getWorks_type()
					+"&id="+mMedia.getId()+"&site="+site;
			new NetWorkTask().execute(MediaActivity.this,UIUtils.GEG_MEDIA_PLAY_URL,
					getPlayUrl,mainHandler);
		}
	
	}
	
	private int imagePostion=0;
	class TextViewAdapter extends BaseAdapter{
		private LayoutInflater mInflater;	
		private int viewpage; 
	
		public TextViewAdapter(int viewpage ){ 
			this.viewpage = viewpage;
			this.mInflater = LayoutInflater.from(MediaActivity.this); 
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewpage;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
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
			if(imagePostion == position){
				imageView.setBackgroundResource(R.drawable.bg_oriange_line_long);	
			} else{
				imageView.setBackgroundResource(R.drawable.bg_tab_my);
			} 
		    int page = position+1;
		    String text = "第"+page+"页";
			txt.setText(text);   
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(position == 4){
						initPopwindow(v, viewpage);
					}else{
						imagePostion = position;
						notifyDataSetChanged();
						setPageView(position);
					} 
				}
			});
			return v;
		}
		
	}
	
	private int imagePostionPop=0;
	class TextViewAdapterPoP extends BaseAdapter{
		private LayoutInflater mInflater;	
		private int viewpage; 
	
		public TextViewAdapterPoP(int viewpage ){ 
			this.viewpage = viewpage;
			this.mInflater = LayoutInflater.from(MediaActivity.this); 
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewpage;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
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
			if(imagePostion == position){
				imageView.setBackgroundResource(R.drawable.bg_oriange_line_long);	
			} else{
				imageView.setBackgroundResource(R.drawable.bg_tab_my);
			} 
		    int page = position+1;
		    String text = "第"+page+"页";
			txt.setText(text);   
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
				    imagePostionPop = position;
					notifyDataSetChanged();
					setPageView(position); 
					dismiss();
				}
			});
			return v;
		}
		
	}
	 
	private void setPageView( int i){
		PageCount = (int) arrVideo.size()%APP_PAGE_SIZE==0?
				arrVideo.size()/APP_PAGE_SIZE: arrVideo.size()/APP_PAGE_SIZE+1; 
		mScrollLayout.removeAllViews();
		if(PageCount > 0){
			    GridView appPage  = new GridView(MediaActivity.this);
				appPage.setNumColumns(5);
				appPage.setHorizontalSpacing(5);
				appPage.setVerticalSpacing(5);
				 
				EpisodeAdapter myAppAdapter = new EpisodeAdapter(arrVideo,i);
		    	appPage.setAdapter(myAppAdapter);
		    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT)); 
		    	mScrollLayout.addView(appPage);
		    	mScrollLayout.setVisibility(View.VISIBLE);
		}
	 
	} 

	OnClickListener myOnClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_logo:
					MediaActivity.this.finish();
					break; 
				case R.id.videoPlayButton:  
					if(mMedia!=null ){ 
						if(voide.getWorks_type().equals("movie")){ 
								if(mMedia.getSitesArr()!=null&&mMedia.getSitesArr().size()>0){ 
								    Sites vid = mMedia.getSitesArr().get(0); 
									ArrayList<MediaItem> mMediaItemArrayList = new ArrayList<MediaItem>();
									MediaItem mMediaItem = new MediaItem(); 
									mMediaItem.setTitle(mMedia.getTitle());
									mMediaItem.setSourceUrl(vid.getSite_url());
									mMediaItem.setImage(vid.getSite_logo());
									mMediaItemArrayList.add(mMediaItem); 
									mMedia.setMediaItemArrayList(mMediaItemArrayList);
									 
								} 
						} 
						mMedia.setPosition(0) ;
						setPlayerUrl();
					}
					 
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
				mMediaItem.setTitle(mMedia.getTitle());
				mMediaItem.setSourceUrl(vid.getSite_url());
				mMediaItem.setImage(vid.getSite_logo());
				mMediaItemArrayList.add(mMediaItem); 
				mMedia.setMediaItemArrayList(mMediaItemArrayList); 
		}
		 
	private String mMediaName = null; 
	@Override
	public void bindData(int tag, Object object) {
		if(tag == UIUtils.GEG_MEDIA_DATA){ 
			if(object!=null){
				try {
					   mMedia = (Media)object;    
					   
						   if(mMedia!=null){
							    mMedia.setMediaType(voide.getWorks_type());
							    mMediaName = mMedia.getTitle(); 
							  
							    title.setText(""+mMediaName); 
							    
							    if(mMedia.getDirectorArr()!=null ){
							    	String s = "";
							    	for(int i=0;i<mMedia.getDirectorArr().size();i++){
							    		String director = mMedia.getDirectorArr().get(i);
							    		s += director+"  ";
							    	}
							    	director.setText("导演："+s+"");
							    }
							    if(mMedia.getActorArr()!=null ){
							    	String s = "";
							    	for(int i=0;i<mMedia.getActorArr().size();i++){
							    		String actor = mMedia.getActorArr().get(i);
							    		s += actor+"   ";
							    	}
							    	craw.setText("演员："+s+"");
							    }
							    
							    
								if(mMedia.getIntro()!=null&&!mMedia.getIntro().equals("")&&!mMedia.getIntro().equals("null")){
									describeTextview.setBackgroundResource(R.drawable.bg_video_detail_website);
									describeTextview.setText("简介");
									describe.setText(mMedia.getIntro()+"");
								} 
							 
								
								 if(mMedia.getAreaArr()!=null ){
								    	String s = "";
								    	for(int i=0;i<mMedia.getAreaArr().size();i++){
								    		String actor = mMedia.getAreaArr().get(i);
								    		s += actor+"   ";
								    	}
								    	area.setText("地区："+s+"");
								    }
								     
								if(mMedia.getImg_url()!=null&&!mMedia.getImg_url().equals("")){
									lin.setBackgroundResource(R.drawable.video_pic_bg);
									imageLoader.DisplayImage(mMedia.getImg_url(), image);
								}
								
								if(mMedia.getSitesArr()!=null&&mMedia.getSitesArr().size()>0){ 
									//电影就不用再次请求，电影里面有getSitesArr有播放地址 site_url: "http://www.iqiyi.com/dianying/20111013/7d2c41e9e79c7def.html",
								 
									 movieSites = mMedia.getSitesArr().get(0);
								 	 bagPage.setVisibility(View.GONE);
									 mScrollLayout.setVisibility(View.GONE);
									 changePlayView.setVisibility(View.VISIBLE);
									 playButton.setVisibility(View.VISIBLE); 
									 describeTextview.setVisibility(View.VISIBLE);
									 setChangePlayUrlButton(mMedia.getSitesArr());
									 Utils.closeWaitingDialog();
								}else{
									//如果是电视或者其他则
									//再次请求获取播放地址
									String getPlayUrl = Contents.XiangQingSingle +"?worktype=adnative"+voide.getWorks_type()
											+"&id="+mMedia.getId()+"&site=";
									new NetWorkTask().execute(MediaActivity.this,UIUtils.GEG_MEDIA_PLAY_URL,
											getPlayUrl,mainHandler);
								}
							
						   }
						  
						 
				} catch (Exception e) { 
				} 
			}
		}else if(tag == UIUtils.GEG_MEDIA_PLAY_URL){
			if(object!=null){
				try {
				   baiduMediaInfo = (BaiDuMediaInfo)object; 
				   arrVideo = baiduMediaInfo.getVideosArray();
				   mMedia.setMediaItemArrayList(arrVideo);
				   if(arrVideo!=null&&arrVideo.size() >= 1){
					   //默认写0；这里表示有很多家视频可以播放，等以后视频切换的时候在打开

						    describeTextview.setVisibility(View.VISIBLE);
						    bagPage.setVisibility(View.VISIBLE);
						    playButton.setVisibility(View.VISIBLE);
						  
							if(arrVideo!=null && !arrVideo.equals("")&& !arrVideo.equals("null")){  
								setViewPageSize();
						    	setPageView(0);
								 
							}  
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
		
	}  
	
	 
}
