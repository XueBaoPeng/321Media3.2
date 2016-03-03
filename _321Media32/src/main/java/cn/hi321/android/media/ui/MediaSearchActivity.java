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
import cn.hi321.android.media.entity.BaoFengPlayUrl;
import cn.hi321.android.media.entity.BaiduResolution;
import cn.hi321.android.media.entity.SearchData;
import cn.hi321.android.media.entity.SearchResult;
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

import com.android.china.R;
//import cn.hi321.android.media.entity.Datas;
/**
 * @author 杨光福老师
 * 搜索页面
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class MediaSearchActivity extends BaseActivity implements IBindData{

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
//	private  VideoData mVideoData; 
	public  final int APP_PAGE_SIZE = 20;//一页最多20
//	private int PAGE_SIZE = 12;
	private LinearLayout bagPage; //显示页数 
//	private Videos videos = null; 
	private LinearLayout changePlayView;//切换播放地址
	private TextView describeTextview; 
//	private BaiDuMediaInfo baiduMediaInfo;
//	private ArrayList<Videos> arrVideo;
	private Sites movieSites;
	private String videoMovieUrl;
	private SearchResult search;
	private SearchData voide = null;
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
		this.imageLoader = new ImageLoader(MediaSearchActivity.this.getApplicationContext(),R.drawable.bg_list_default);
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
			voide = (SearchData) mIntent.getSerializableExtra("BaiDuRecommend"); 
		     if(UIUtils.hasNetwork(MediaSearchActivity.this)){
		    	 Utils.startWaitingDialog(MediaSearchActivity.this);
		    	 String url = "http://search.shouji.baofeng.com/mdetail.php?aid="+voide.getId()+"&mtype=normal&ver="+Contents.versionBaoFeng;
				  new NetWorkTask().execute(MediaSearchActivity.this,UIUtils.BaoFeng_Detail,
							url); 
	  		  }else{
	  			UIUtils.showToast(MediaSearchActivity.this,MediaSearchActivity.this.getText(R.string.tip_network).toString());
			 } 
		}  
		returnButton.setOnClickListener(myOnClick); 
		playButton.setOnClickListener(myOnClick); 
	 
	} 
	
 
	
	class EpisodeAdapter extends BaseAdapter{ 
	private LayoutInflater mInflater; 
//	int episode;
	ArrayList<String> arrCurrent;
	ArrayList<String> arr;
	
	public EpisodeAdapter(ArrayList<String> arrCurrent,int page ) {
		this.mInflater = LayoutInflater.from(MediaSearchActivity.this); 
		 this.arrCurrent = arrCurrent;
		 int i = page * APP_PAGE_SIZE;
		 int iEnd = i+APP_PAGE_SIZE;  
		 arr = new ArrayList<String>();
		while ((i<arrCurrent.size()) && (i<iEnd)) {  
			arr.add(arrCurrent.get(i));
			i++;
		} 
	}

	@Override
	public int getCount() { 
		if(arr!=null)
			return arr.size();
		return 0;
	}

	@Override
	public Object getItem(int position) { 
		 if(arr!=null){
			 return arr.get(position);
		 }
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
				v =  mInflater.inflate(R.layout.playbutton_item, null);
			}else{
				v = convertView;
			} 
		    try { 
		    	if( arr!=null&&arr.size() > position){
		    		Button button = (Button)v.findViewById(R.id.button);  
		    	
			    	button.setText(arr.get(position)+"");  
			    	button.setOnClickListener(new OnClickListener() { 
						@Override
						public void onClick(View v) {  
							getPlayUrl(arr.get(position)); 
						}
					}); 
		    	}
		     
			} catch (Exception e) { 
			}  
		return  v;
	} 
} 
	
	
	  protected void onDestroy() {
			super.onDestroy();
			 ActivityHolder.getInstance().removeActivity(this);
		}
	private void setViewPageSize( ){  
			 if(search.getHas()!=null&&search.getHas().size()>0){
				 bagPage.removeAllViews(); 
				  int viewpage = (int) search.getHas().size()%APP_PAGE_SIZE==0?
						  search.getHas().size()/APP_PAGE_SIZE:search.getHas().size()/APP_PAGE_SIZE+1;
				 if(viewpage >= 1){ 
						if(viewpage >=5){
							GridView appPage  = new GridView(MediaSearchActivity.this);
							appPage.setNumColumns(5); 
							appPage.setHorizontalSpacing(5);
							appPage.setVerticalSpacing(5);
							TextViewAdapter myAppAdapter = new TextViewAdapter(viewpage);
					    	appPage.setAdapter(myAppAdapter);
					    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
					    	bagPage.addView(appPage);
					    	bagPage.setBackgroundResource(R.drawable.bg_videodetail_serise_mul);
						}else{
							 
							GridView appPage  = new GridView(MediaSearchActivity.this);
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
		View view = LayoutInflater.from(MediaSearchActivity.this)
				.inflate(R.layout.pagepopwindow, null); 
	    gridview = (GridView)view.findViewById(R.id.view_select_grid); 
		popupWindow = new PopupWindow(view, MediaSearchActivity.this.getResources() .getDimensionPixelSize(R.dimen.popmenu_width),
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(parent,3, MediaSearchActivity.this.getResources().getDimensionPixelSize(
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
 
	
	private int imagePostion=0;
	class TextViewAdapter extends BaseAdapter{
		private LayoutInflater mInflater;	
		private int viewpage; 
	
		public TextViewAdapter(int viewpage ){ 
			this.viewpage = viewpage;
			this.mInflater = LayoutInflater.from(MediaSearchActivity.this); 
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
			this.mInflater = LayoutInflater.from(MediaSearchActivity.this); 
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
	 
	private void setPageView(int i){ 
		 
//		   String seqCount = voide.getTotal(); 
		 if(search.getHas()!=null&&search.getHas().size()>0){
			 mScrollLayout.removeAllViews();
			 GridView appPage  = new GridView(MediaSearchActivity.this);
			appPage.setNumColumns(5);
			appPage.setHorizontalSpacing(5);
			appPage.setVerticalSpacing(5);
			 
			EpisodeAdapter myAppAdapter = new EpisodeAdapter(search.getHas(),i);
	    	appPage.setAdapter(myAppAdapter);
	    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT)); 
	    	mScrollLayout.addView(appPage);
	    	mScrollLayout.setVisibility(View.VISIBLE);
		 } 
	 
	} 
	private void getPlayUrl(String seq){ 
		 if(UIUtils.hasNetwork(MediaSearchActivity.this)){
			 Utils.startWaitingDialog(MediaSearchActivity.this);
	    	 String url = "http://search.shouji.baofeng.com/minfo.php?aid="+voide.getId()+"&seq="+seq+"&mtype=normal&ver="+Contents.versionBaoFeng;
			  new NetWorkTask().execute(MediaSearchActivity.this,UIUtils.BaoFeng_Play,
						url); 
 		  }else{
 			UIUtils.showToast(MediaSearchActivity.this,MediaSearchActivity.this.getText(R.string.tip_network).toString());
		 }
	}

	OnClickListener myOnClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_logo:
					MediaSearchActivity.this.finish();
					break; 
				case R.id.videoPlayButton:  
					if(search !=null&&search.getHas()!=null&&search.getHas().size()>0)
					getPlayUrl(search.getHas().get(0)); 
					break;
				default:
					break;
				}
			}
		};
		
		private void playView(String videoUrl){
			if(!Utils.isEmpty(videoUrl)){
				
				if(videoUrl.contains(".m3u8")||videoUrl.contains(".mp4")){
//					MediaItem  mediaItem = (MediaItem)object;
//					if(mediaItem != null){
						String video_source_url = videoUrl;
						if(!Utils.isEmpty(video_source_url)){
							Intent intent = new Intent(MediaSearchActivity.this,SystemPlayer.class);
							MediaItem  videoInfo = new MediaItem();
							videoInfo.setSourceUrl(videoUrl); 
							videoInfo.setUrl(video_source_url);
							if(!Utils.isEmpty(mItem)){
								mItem ="  第"+mItem;
							}
							String mediaName = mMediaName+mItem;
							videoInfo.setTitle(mediaName);
							videoInfo.setLive(false);
							Bundle mBundle = new Bundle();
							if(videoInfo != null)
							mBundle.putSerializable("VideoInfo", videoInfo);
							intent.putExtras(mBundle);
	//						intent.setAction(Intent.ACTION_VIEW);
	//						intent.setDataAndType(Uri.parse(video_source_url),"video/*");
							startActivity(intent);
							mItem = "";
						}else{
							String sourceUri = videoUrl;
							if(!Utils.isEmpty(sourceUri)){
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(sourceUri));
								startActivity(intent);
							}else{
								Toast.makeText(MediaSearchActivity.this, "该视频无法播放", 1).show();
							} 
						
					} 
				}else{
					try {
						BaiduResolution playDatas = new BaiduResolution();
						playDatas.setSourceUrl(videoUrl); 
						String baiduService = "http://gate.baidu.com/tc?m=8&video_app=1&ajax=1&src="+videoUrl;
						if(!Utils.isEmpty(baiduService)){ 
							if(UIUtils.hasNetwork(MediaSearchActivity.this)){
//								Utils.startWaitingDialog(MediaSearchActivity.this);
								new NetWorkTask().execute(MediaSearchActivity.this,UIUtils.GET_PLAY_DATA,
										baiduService,playDatas,mainHandler);
					  		  }else{
					  			UIUtils.showToast(MediaSearchActivity.this, MediaSearchActivity.this.getText(R.string.tip_network).toString());
							 }
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
			}
		}
		
	private String mMediaName = null;
	private String mItem = "";
	@Override
	public void bindData(int tag, Object object) {
		if(tag == UIUtils.BaoFeng_Detail){ 
			if(object!=null){
				try {
					 search = (SearchResult)object;
					 mMediaName = voide.getTitle(); 
					    title.setText(""+mMediaName); 
					    
					    if(voide.getDirectors_name()!=null &&voide.getDirectors_name().size()>0){
					    	String s = "";
					    	for(int i=0;i<voide.getDirectors_name().size();i++){
					    		String director = voide.getDirectors_name().get(i);
					    		s += director+"  ";
					    	}
					    	director.setText("导演："+s+"");
					    }
					    if(voide.getActors_name()!=null &&voide.getActors_name().size()>0){
					    	String s = "";
					    	for(int i=0;i<voide.getActors_name().size();i++){
					    		String actor = voide.getActors_name().get(i);
					    		s += actor+"   ";
					    	}
					    	craw.setText("演员："+s+"");
					    }
//					    describeTextview.setText("简介");
					    if(search.getDesc()!=null&&!search.getDesc().equals("")&&!search.getDesc().equals("null")){
							describeTextview.setBackgroundResource(R.drawable.bg_video_detail_website);
							describeTextview.setText("简介");
							describe.setText(search.getDesc()+"");
							describeTextview.setVisibility(View.VISIBLE); 
						} 
					     
//					    String seqCount = voide.getTotal();
					  
						 if(voide.getArea_l()!=null&&!voide.getArea_l().equals("")&&!voide.getArea_l().equals("null") ){ 
						    	area.setText("地区："+voide.getArea_l()+"");
						    }
						     
						if(voide.getCover_url()!=null&&!voide.getCover_url().equals("")){
							lin.setBackgroundResource(R.drawable.video_pic_bg);
							imageLoader.DisplayImage(voide.getCover_url(), image);
						}
						
						if(voide.getMax_site() !=null ){  
							 changePlayView.setVisibility(View.VISIBLE);
							 TextView t = new TextView(MediaSearchActivity.this);
							 t.setText("视频来源："+voide.getMax_site()+"");
							 changePlayView.addView(t); 
						} 
						 
							   //默认写0；这里表示有很多家视频可以播放，等以后视频切换的时候在打开

					    bagPage.setVisibility(View.VISIBLE);
					    playButton.setVisibility(View.VISIBLE);
					    
					    if(search.getHas() !=null && search.getHas().size() > 0){
							setViewPageSize(); 
					    	setPageView(0); 	
					    }
					    Utils.closeWaitingDialog();
				} catch (Exception e) { 
					 Utils.closeWaitingDialog();
				} 
			}else{
				 Utils.closeWaitingDialog();
			}
		} 
		else if(tag == UIUtils.GET_PLAY_DATA){ 
			if(object!=null){
				try { 
					BaiduResolution  mediaItem = (BaiduResolution)object;
					if(mediaItem != null){
						String video_source_url =mediaItem.getVideo_source_url();
						if(!Utils.isEmpty(video_source_url)){
							Intent intent = new Intent(MediaSearchActivity.this,SystemPlayer.class);
							MediaItem  videoInfo = new MediaItem();
							videoInfo.setSourceUrl(mediaItem.getmSourceUrl());
//							videoInfo.setOnTheNextSet(onTheNextSet);
							videoInfo.setUrl(video_source_url);
							if(!Utils.isEmpty(mItem)){
								mItem ="  第"+mItem;
							}
							String mediaName = mMediaName+mItem;
							videoInfo.setTitle(mediaName);
							videoInfo.setLive(false);
							Bundle mBundle = new Bundle();
							if(videoInfo != null)
							mBundle.putSerializable("VideoInfo", videoInfo);
							intent.putExtras(mBundle);
	//						intent.setAction(Intent.ACTION_VIEW);
	//						intent.setDataAndType(Uri.parse(video_source_url),"video/*");
							startActivity(intent);
							 overridePendingTransition(R.anim.fade, R.anim.hold); 
							mItem = "";
						}else{
							String sourceUri = mediaItem.getSourceUrl();
							if(!Utils.isEmpty(sourceUri)){
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(sourceUri));
								startActivity(intent);
								 overridePendingTransition(R.anim.fade, R.anim.hold); 
							}else{
								Toast.makeText(MediaSearchActivity.this, "该视频无法播放", 1).show();
							}
							
						}
						
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				Utils.closeWaitingDialog();
			}else{
				 Utils.closeWaitingDialog();
			}
		}else if(tag == UIUtils.BaoFeng_Play){
			try {
				if(object!=null){
					BaoFengPlayUrl playUrl = (BaoFengPlayUrl)object;
					mItem = playUrl.getTitle();
					playView(playUrl.getPage_url());
				}else{
					 Utils.closeWaitingDialog();
				}
//				Utils.closeWaitingDialog();
			} catch (Exception e) {
				// TODO: handle exception
				Utils.closeWaitingDialog();
			}
 
		}
	}  
	
	 
}
 


//package cn.hi321.android.media.ui;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import cn.hi321.android.media.entity.BaiDuMediaInfo;
//import cn.hi321.android.media.entity.BaoFengPlayUrl;
//import cn.hi321.android.media.entity.MediaItem;
//import cn.hi321.android.media.entity.SearchData;
//import cn.hi321.android.media.entity.SearchResult;
//import cn.hi321.android.media.entity.VideoInfo;
//import cn.hi321.android.media.http.IBindData;
//import cn.hi321.android.media.http.NetWorkTask;
//import cn.hi321.android.media.player.SystemPlayer;
//import cn.hi321.android.media.utils.Contents;
//import cn.hi321.android.media.utils.ImageLoader;
//import cn.hi321.android.media.utils.UIUtils;
//import cn.hi321.android.media.utils.Utils;
//import cn.hi321.android.media.widget.MainListview;
//
//import com.android.china.R;
//
//public class MediaSearchActivity extends BaseActivity implements IBindData{
//	private ImageButton returnButton;;
//	private TextView title;
//	private Intent mIntent;
////	private int flags; 
//	private ImageView  image;
//	private  ImageLoader imageLoader; 
//	private TextView director;
//	private TextView craw;
//	private TextView area;
////	private TextView describe; 
////	private RelativeLayout mScrollLayout; 
//	private Button playButton;
////	private int id;
////	private String url; 
////	private  VideoData mVideoData; 
//	public  final int APP_PAGE_SIZE = 20;//一页最多20
////	private int PAGE_SIZE = 12;
//	private LinearLayout changeView;  
////	private int PageCount; 
////	private Videos videos = null; 
//	private LinearLayout changePlayView;//切换播放地址
////	private TextView describeTextview; 
//	private BaiDuMediaInfo baiduMediaInfo;
////	private ArrayList<Videos> arrVideo;
////	private Sites movieSites;
//	private String videoMovieUrl;
//	
//	private SearchData voide = null;
//	private RelativeLayout lin;
//	
//	private TextView describeTextview; 
//	private TextView playListText;
//	private SearchResult search ;
//	private LinearLayout linear;
//	
//	@Override
//	protected void onCreate(Bundle paramBundle) { 
//		super.onCreate(paramBundle);
//		setContentView(R.layout.media_show_activity); 
//		Utils.startWaitingDialog(MediaSearchActivity.this);
//		returnButton =(ImageButton)findViewById(R.id.btn_logo);
//		title = (TextView)findViewById(R.id.tv_title);
//		director =(TextView)findViewById(R.id.director);
//		image = (ImageView)findViewById(R.id.about_video_image); 
//		craw =(TextView)findViewById(R.id.craw);
//		area =(TextView)findViewById(R.id.area);  
//		this.imageLoader = new ImageLoader(MediaSearchActivity.this.getApplicationContext(),R.drawable.bg_list_default);
//		describeTextview = (TextView)findViewById(R.id.describeTextview); 
//		describeTextview.setOnClickListener(myOnClick); 
//		playListText = (TextView)findViewById(R.id.playListText); 
//		playListText.setOnClickListener(myOnClick); 
//		changeView = (LinearLayout)findViewById(R.id.changeView);
//		linear = (LinearLayout)findViewById(R.id.linear);
//		linear.setVisibility(View.GONE);
////		mScrollLayout = (RelativeLayout)findViewById(R.id.gridview);
//	    playButton =(Button)findViewById(R.id.videoPlayButton);  
//	    playButton.setVisibility(View.GONE);
//	    describeTextview = (TextView)findViewById(R.id.describeTextview);
//	    describeTextview.setVisibility(View.GONE);
//	 
//		lin = (RelativeLayout)findViewById(R.id.lin);
//		changePlayView =(LinearLayout)findViewById(R.id.changePlayView); 
//		mIntent = getIntent();  
//		if(mIntent != null){
//			voide = (SearchData) mIntent.getSerializableExtra("BaiDuRecommend");
//	  
//		     if(UIUtils.hasNetwork(MediaSearchActivity.this)){
//		    	 
//		    	 String url = "http://search.shouji.baofeng.com/mdetail.php?aid="+voide.getId()+"&mtype=normal&ver="+Contents.versionBaoFeng;
//				  new NetWorkTask().execute(MediaSearchActivity.this,UIUtils.BaoFeng_Detail,
//							url); 
//	  		  }else{
//	  			UIUtils.showToast(MediaSearchActivity.this,MediaSearchActivity.this.getText(R.string.tip_network).toString());
//			 } 
//		}  
//		returnButton.setOnClickListener(myOnClick); 
//		playButton.setOnClickListener(myOnClick); 
//	 
//	} 
//	
//  
//	class EpisodeAdapter extends BaseAdapter{ 
//		private LayoutInflater mInflater; 
//		int episode;
//		public EpisodeAdapter(int episode ) {
//			this.mInflater = LayoutInflater.from(MediaSearchActivity.this); 
//			 this.episode = episode;
//		}
//
//		@Override
//		public int getCount() { 
//			return episode;
//		}
//
//		@Override
//		public Object getItem(int position) { 
//			 
//			return position;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(final int position, View convertView, ViewGroup parent) { 
//		 
//			 View v;
//				if(convertView == null ){
//					v =  mInflater.inflate(R.layout.listview_button, null);
//				}else{
//					v = convertView;
//				} 
//			    try { 
//			    	if( episode > position){
//			    		Button button = (Button)v.findViewById(R.id.button);  
//			    		final int seq = position +1;
//				    	button.setText("第"+seq+"集:"+voide.getTitle());  
//				    	button.setOnClickListener(new OnClickListener() { 
//							@Override
//							public void onClick(View v) {  
//								getPlayUrl(seq); 
//							}
//						}); 
//			    	}
//			     
//				} catch (Exception e) { 
//				}  
//			return  v;
//		} 
//	} 
//	 
//	OnClickListener myOnClick = new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				switch (v.getId()) {
//				case R.id.btn_logo:
//					MediaSearchActivity.this.finish();
//					break; 
//				case R.id.videoPlayButton: 
//					getPlayUrl(1); 
//					break;
//				case R.id.describeTextview:   
//					changeView(true);
//					break;
//				case R.id.playListText:  
//					changeView(false);
//					break;
//				default:
//					break;
//				}
//			}
//		};
//		
//		private void getPlayUrl(int seq){ 
//			 if(UIUtils.hasNetwork(MediaSearchActivity.this)){
//				 Utils.startWaitingDialog(MediaSearchActivity.this);
//		    	 String url = "http://search.shouji.baofeng.com/minfo.php?aid="+voide.getId()+"&seq="+seq+"&mtype=normal&ver="+Contents.versionBaoFeng;
//				  new NetWorkTask().execute(MediaSearchActivity.this,UIUtils.BaoFeng_Play,
//							url); 
//	  		  }else{
//	  			UIUtils.showToast(MediaSearchActivity.this,MediaSearchActivity.this.getText(R.string.tip_network).toString());
//			 }
//		}
//		
//		/**
//		 * false表示显示列表
//		 * */
//		private void changeView(boolean isDescribe){
//			changeView.removeAllViews();
//			if(isDescribe){//简介
//				describeTextview.setBackgroundResource(R.drawable.collect_edit_ico);
//				playListText.setBackgroundResource(R.drawable.collect_edit_press_ico);
//				TextView textview = new TextView(MediaSearchActivity.this);
//				if(search!=null)
//				textview.setText(search.getDesc()+""); 
//				changeView.addView(textview);
//			}else{
//				describeTextview.setBackgroundResource(R.drawable.collect_edit_press_ico);
//				playListText.setBackgroundResource(R.drawable.collect_edit_ico);
//				 String seqCount = voide.getLast_seq();
//				 if(seqCount!=null&&!seqCount.equals("")&&!seqCount.equals("null")){
////					GridView appPage  = new GridView(MediaSearchActivity.this);
////					appPage.setNumColumns(4); 
////					appPage.setHorizontalSpacing(5);
////					appPage.setVerticalSpacing(5); 
////					
//				    MainListview appPage  = new MainListview(MediaSearchActivity.this); 
//					EpisodeAdapter myAppAdapter = new EpisodeAdapter(Integer.parseInt(seqCount));
//			    	appPage.setAdapter(myAppAdapter);
//			    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT)); 
//			    	changeView.addView(appPage); 
//				}else{
//					changeView(true);
//				}
//				
//			}
//		}
//		
//		private void playView(String videoUrl){
//			if(!Utils.isEmpty(videoUrl)){
//				try {
//					MediaItem playDatas = new MediaItem();
//					playDatas.setSourceUrl(videoUrl);
////					if(arrVideo!=null&&arrVideo.size()>0){
////						mItem = ""+arrVideo.get(0).getEpisode(); 
////					} 
//					String baiduService = "http://gate.baidu.com/tc?m=8&video_app=1&ajax=1&src="+videoUrl;
//					if(!Utils.isEmpty(baiduService)){ 
//						if(UIUtils.hasNetwork(MediaSearchActivity.this)){
//							Utils.startWaitingDialog(MediaSearchActivity.this);
//							new NetWorkTask().execute(MediaSearchActivity.this,UIUtils.GET_PLAY_DATA,
//									baiduService,playDatas,mainHandler);
//				  		  }else{
//				  			UIUtils.showToast(MediaSearchActivity.this, MediaSearchActivity.this.getText(R.string.tip_network).toString());
//						 }
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		}
//		
//	private String mMediaName = null;
//	private String mItem = "";
//	@Override
//	public void bindData(int tag, Object object) {
// 
//		  if(tag == UIUtils.GET_PLAY_DATA){ 
//			  
//			if(object!=null){
//				try { 
//					MediaItem  mediaItem = (MediaItem)object;
//					if(mediaItem != null){
//						String video_source_url =mediaItem.getVideo_source_url();
//						if(!Utils.isEmpty(video_source_url)){
//							Intent intent = new Intent(MediaSearchActivity.this,SystemPlayer.class);
//							VideoInfo  videoInfo = new VideoInfo();
//							videoInfo.setUrl(video_source_url);
//							videoInfo.setSourceUrl(mediaItem.getmSourceUrl());
////							if(!Utils.isEmpty(mItem)){
////								mItem ="  第"+mItem+"集";
////							}
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
//							overridePendingTransition(R.anim.fade, R.anim.hold);
//							mItem = "";
//						}else{
//							String sourceUri = mediaItem.getSourceUrl();
//							if(!Utils.isEmpty(sourceUri)){
//								Intent intent = new Intent();
//								intent.setAction(Intent.ACTION_VIEW);
//								intent.setData(Uri.parse(sourceUri));
//								startActivity(intent);
//							}else{
//								Toast.makeText(MediaSearchActivity.this, "该视频无法播放", 1).show();
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
//		}else if(tag == UIUtils.BaoFeng_Detail){
//			try {
//				if(object!=null){
//					 search = (SearchResult)object;
//					 mMediaName = voide.getTitle(); 
//					    title.setText(""+mMediaName); 
//					    
//					    if(voide.getDirectors_name()!=null &&voide.getDirectors_name().size()>0){
//					    	String s = "";
//					    	for(int i=0;i<voide.getDirectors_name().size();i++){
//					    		String director = voide.getDirectors_name().get(i);
//					    		s += director+"  ";
//					    	}
//					    	director.setText("导演："+s+"");
//					    }
//					    if(voide.getActors_name()!=null &&voide.getActors_name().size()>0){
//					    	String s = "";
//					    	for(int i=0;i<voide.getActors_name().size();i++){
//					    		String actor = voide.getActors_name().get(i);
//					    		s += actor+"   ";
//					    	}
//					    	craw.setText("演员："+s+"");
//					    }
//					    describeTextview.setText("简介");
//					    playListText.setText("播放列表");
//					    String seqCount = voide.getLast_seq();
//					    if(voide!=null&&seqCount!=null&&Integer.parseInt(seqCount)>1){
//					    	//表示没有播放列表 
//					    	   changeView(false); 	
//					    }else{ 
//					    	   playListText.setVisibility(View.GONE);
//					    	   changeView(true); 
//					    }
//					 
//						 if(voide.getArea_l()!=null&&!voide.getArea_l().equals("")&&!voide.getArea_l().equals("null") ){ 
//						    	area.setText("地区："+voide.getArea_l()+"");
//						    }
//						     
//						if(voide.getCover_url()!=null&&!voide.getCover_url().equals("")){
//							lin.setBackgroundResource(R.drawable.video_pic_bg);
//							imageLoader.DisplayImage(voide.getCover_url(), image);
//						}
//						
//						if(voide.getMax_site()!=null ){  
//							 changePlayView.setVisibility(View.VISIBLE);
//							 TextView t = new TextView(MediaSearchActivity.this);
//							 t.setText("视频来源："+voide.getMax_site()+"");
//							 changePlayView.addView(t);
//							 playButton.setVisibility(View.VISIBLE); 
//							 describeTextview.setVisibility(View.VISIBLE); 
//						} 
//						linear.setVisibility(View.VISIBLE);	 
//				}
//				Utils.closeWaitingDialog();
//			} catch (Exception e) {
//				// TODO: handle exception
//				Utils.closeWaitingDialog();
//			}
//			
//		}else if(tag == UIUtils.BaoFeng_Play){
//			try {
//				if(object!=null){
//					BaoFengPlayUrl playUrl = (BaoFengPlayUrl)object;
//					mItem = playUrl.getTitle();
//					playView(playUrl.getPage_url());
//				}
//				Utils.closeWaitingDialog();
//			} catch (Exception e) {
//				// TODO: handle exception
//				Utils.closeWaitingDialog();
//			}
//			
//		}
//		
//	}  
//	 
//	 
//}
