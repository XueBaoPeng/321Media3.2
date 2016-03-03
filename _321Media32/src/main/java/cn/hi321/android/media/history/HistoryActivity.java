package cn.hi321.android.media.history;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.android.media.db.PlayHistoryDao;
import cn.hi321.android.media.entity.BaiDuMediaInfo;
import cn.hi321.android.media.entity.Media;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.entity.PlayHistoryInfo;
import cn.hi321.android.media.entity.Sites;
import cn.hi321.android.media.http.IBindData;
import cn.hi321.android.media.http.NetWorkTask;
import cn.hi321.android.media.player.SystemPlayer;
import cn.hi321.android.media.ui.BaseActivity;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.Contents;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;

import com.android.china.R;

public class HistoryActivity extends BaseActivity implements IBindData {
	private static final String TAG = "HistoryActivity";

	private PlayHistoryDao mPlayHistoryDao;

	private Media mMedia = null;
	
	private MediaItem mediaItem;

	private ListView mHistoryListView;
	
	private PlayHistoryAdapter mHistoryListAdapter;
	
	private List<PlayHistoryInfo> mHistoryList;

	private PlayHistoryInfo mPlayHistoryInfo;

	private LinearLayout mNoDataLayout = null;
	
	private TextView mNoDataTextView = null;
	private TextView mTitle = null;
	
	private Dialog mCleanDataDialog = null;
	
	private Toast mToast = null;
	
	private ImageButton mLeftButton = null;
	private Button mRigtButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityHolder.getInstance().addActivity(this);
		setContentView(R.layout.playhistory_popupwindow);
		
		initPlayHistoryView();
		initPlayHistoryData();
		initCleanDataDialog();
		initHistoryTitle();
	}
	
	private void initPlayHistoryView() {
		mNoDataLayout = (LinearLayout) findViewById(R.id.havanodata_layout);
		mNoDataTextView = (TextView) findViewById(R.id.nodat_tv);
		mTitle = (TextView)findViewById(R.id.tv_title);
		mLeftButton = (ImageButton) findViewById(R.id.left_button);
		mRigtButton = (Button) findViewById(R.id.right_button);
		mNoDataTextView.setText(R.string.noviewinghistory);
		mHistoryListView = (ListView) findViewById(R.id.lv_history);
		mHistoryListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mHistoryListView.setOnItemClickListener(mPlayHistoryItemClickListener);
		mPlayHistoryDao = new PlayHistoryDao(this);
		mToast = new Toast(this);
		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		mLeftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickLeftButton();
				
			}
		});
		mRigtButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickRightButton();
			}
		});
	}

	private void initHistoryTitle() {
		mRigtButton.setText(getString(R.string.cleanup));
		mTitle.setText("播放历史");
	}
	
	private void initPlayHistoryData() {
		try {
			mPlayHistoryDao.autoDelete(100);
			mHistoryList = mPlayHistoryDao.findByOrder("desc");
			mMedia = new Media();
			mediaItem = new MediaItem();
			if (hasPlayHistory()) {
				mNoDataLayout.setVisibility(View.GONE);
				mHistoryListAdapter = new PlayHistoryAdapter(this, mHistoryList,mMedia,null);
				mHistoryListView.setAdapter(mHistoryListAdapter);
			} else {
//				setRightButtonHideOrShow(false);
				mNoDataLayout.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initPlayHistoryData();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		ActivityHolder.getInstance().removeActivity(this);
		mHistoryList = null;
		mPlayHistoryDao = null;
		mMedia = null;
		mHistoryListView = null;
		mHistoryListAdapter = null;
		mNoDataLayout = null;
		mNoDataTextView = null;
		super.onDestroy();
	}



	private boolean hasPlayHistory() {
		return mHistoryList != null && mHistoryList.size() > 0;
	}

	private OnItemClickListener mPlayHistoryItemClickListener = new OnItemClickListener() {
		private final static String URL_DOMAIN_JOBSFE = "jobsfe.funshion.com";
		private final static String URL_DOMAIN_P = "p.funshion.com";

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (!hasPlayHistory()) {
				return;
			}
			mPlayHistoryInfo = mHistoryList.get(position);
			if (mPlayHistoryInfo != null) {
				  String  url =  Contents.XiangQingInfo+"?worktype=adnative"+mPlayHistoryInfo.getMediatype()+"&id="+mPlayHistoryInfo.getMid()+"&site=";//voide.getUrl();
					if(UIUtils.hasNetwork(HistoryActivity.this)){
						Utils.startWaitingDialog(HistoryActivity.this); 
							new NetWorkTask().execute(HistoryActivity.this,UIUtils.GEG_MEDIA_DATA,
									url,mainHandler); 
					
			  		  }else{
			  			UIUtils.showToast(HistoryActivity.this, HistoryActivity.this.getText(R.string.tip_network).toString());
						 
			  	     }
				
//				String mediaURL = mPlayHistoryInfo.getPurl();
//				if (isRemoteAddr(mediaURL)) {
//					String url = Utils.GET_MEDIA_DATA_URL+ mPlayHistoryInfo.getMid();
//					LogUtil.v(TAG, "url = " + url);
//					new NetWorkTask().execute(HistoryActivity.this,Utils.GET_MEDIA_HISTORY_BY_SERVER,fxHandler, mMedia, url);
//				} else {
//					playByLocalData(mediaURL);
//				}
			}
		}

		private boolean isRemoteAddr(String url) {
			return url.contains(URL_DOMAIN_JOBSFE) || url.contains(URL_DOMAIN_P);
		}

//		private PlayData configPlayData(String videoURL,PlayHistoryInfo playHistory) {
//			PlayData data = new PlayData();
//			data.setUrl(videoURL);
//
//			String videoName = playHistory.getMedianame();
//			if (!playHistory.getMedianame().equals(playHistory.getTaskname())) {
//				videoName += playHistory.getTaskname();
//			}
//			data.setName(videoName);
//
//			if (playHistory.getPercent() != null) {
//				data.setWatchablePercent(Double.parseDouble(playHistory.getPercent()));
//			}
//
//			data.setLocalFiel(true);
//			data.setSize(playHistory.getSize());
//			return data;
//		}

		/**
		 * 从本地播放进入播放器
		 * 
		 * @param videoURL
		 */
		private void playByLocalData(String videoURL) {/*
			String fileName = videoURL.substring(videoURL.lastIndexOf('/') + 1);
			File file = new File(DownloadHelper.getDownloadPath(), fileName);
			if (file.exists()) {
				Intent intent = new Intent(HistoryActivity.this,PlayerAgent.getPlayerClass());
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Utils.DOWNLOAD_KEY,configPlayData(videoURL, mPlayHistoryInfo));
				mBundle.putSerializable(Utils.PLAY_HISTORY_KEY,mPlayHistoryInfo);
				intent.putExtras(mBundle);
				startActivity(intent);
			} else {
				Toast.makeText(HistoryActivity.this, R.string.find_not_file,Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						mPlayHistoryDao.delete(mPlayHistoryInfo.getMid());
					}
				}).start();

			}
		*/}
	};

	private void initCleanDataDialog() {
		Builder customBuilder = new Builder(HistoryActivity.this);
		customBuilder
				.setTitle(R.string.tip)
				.setMessage(R.string.deleteallhistory)
				.setPositiveButton(R.string.queren,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								mPlayHistoryDao.deleteAllData();
								mHistoryList.clear();
								mHistoryListAdapter.notifyDataSetChanged();
								mNoDataLayout.setVisibility(View.VISIBLE);
								mNoDataTextView.setText(R.string.noviewinghistory);
								dialog.dismiss();
//								setRightButtonHideOrShow(false);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

		mCleanDataDialog = customBuilder.create();
	}

	private BaiDuMediaInfo baiduMediaInfo;
	private ArrayList<MediaItem> arrVideo;
	/**
	 * Parent class bindData method is called. Note: movies, TV shows,
	 * animation, variety page bindData methods need to call the parent class
	 * bindData
	 * 
	 * @author lushengbin
	 */
	@Override
	public void bindData(int tag, Object object) {
		if(tag == UIUtils.GEG_MEDIA_DATA){ 
		if(object!=null){
			try {
				   mMedia = (Media)object;    
				   
					   if(mMedia!=null){
						    mMedia.setMediaType(mPlayHistoryInfo.getMediatype());
//						    mMediaName = mMedia.getTitle(); 
//						  
//						    title.setText(""+mMediaName); 
//						    
//						    if(mMedia.getDirectorArr()!=null ){
//						    	String s = "";
//						    	for(int i=0;i<mMedia.getDirectorArr().size();i++){
//						    		String director = mMedia.getDirectorArr().get(i);
//						    		s += director+"  ";
//						    	}
//						    	director.setText("导演："+s+"");
//						    }
//						    if(mMedia.getActorArr()!=null ){
//						    	String s = "";
//						    	for(int i=0;i<mMedia.getActorArr().size();i++){
//						    		String actor = mMedia.getActorArr().get(i);
//						    		s += actor+"   ";
//						    	}
//						    	craw.setText("演员："+s+"");
//						    }
//						    
						    
//							if(mMedia.getIntro()!=null&&!mMedia.getIntro().equals("")&&!mMedia.getIntro().equals("null")){
//								describeTextview.setBackgroundResource(R.drawable.bg_video_detail_website);
//								describeTextview.setText("简介");
//								describe.setText(mMedia.getIntro()+"");
//							} 
//						 
//							
//							 if(mMedia.getAreaArr()!=null ){
//							    	String s = "";
//							    	for(int i=0;i<mMedia.getAreaArr().size();i++){
//							    		String actor = mMedia.getAreaArr().get(i);
//							    		s += actor+"   ";
//							    	}
//							    	area.setText("地区："+s+"");
//							    }
//							     
//							if(mMedia.getImg_url()!=null&&!mMedia.getImg_url().equals("")){
//								lin.setBackgroundResource(R.drawable.video_pic_bg);
//								imageLoader.DisplayImage(mMedia.getImg_url(), image);
//							}
							
							if(mMedia.getSitesArr()!=null&&mMedia.getSitesArr().size()>0){ 
								//电影就不用再次请求，电影里面有getSitesArr有播放地址 site_url: "http://www.iqiyi.com/dianying/20111013/7d2c41e9e79c7def.html",
							 
//								 movieSites = mMedia.getSitesArr().get(0);
//							 	 bagPage.setVisibility(View.GONE);
//								 mScrollLayout.setVisibility(View.GONE);
//								 changePlayView.setVisibility(View.VISIBLE);
//								 playButton.setVisibility(View.VISIBLE); 
//								 describeTextview.setVisibility(View.VISIBLE);
//								 setChangePlayUrlButton(mMedia.getSitesArr());
								 Utils.closeWaitingDialog();
								 startPlayer();
							}else{
								//如果是电视或者其他则
								//再次请求获取播放地址
								String getPlayUrl = Contents.XiangQingSingle +"?worktype=adnative"+mPlayHistoryInfo.getMediatype()
										+"&id="+mMedia.getId()+"&site=";
								new NetWorkTask().execute(HistoryActivity.this,UIUtils.GEG_MEDIA_PLAY_URL,
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
//			   if(arrVideo!=null&&arrVideo.size() >= 1){
//				   //默认写0；这里表示有很多家视频可以播放，等以后视频切换的时候在打开
//
//					  
//						if(arrVideo!=null && !arrVideo.equals("")&& !arrVideo.equals("null")){  
//							setViewPageSize();
//					    	setPageView(0);
//							 
//						}  
//						if(baiduMediaInfo.getSitesArray()!=null){
//							setChangePlayUrlButton(baiduMediaInfo.getSitesArray());
//						}
//			   } else{
//				   playButton.setVisibility(View.VISIBLE);
//			   } 
			} catch (Exception e) {
				// TODO: handle exception
			}
		    startPlayer();
		
			Utils.closeWaitingDialog();
			
		}
		
	}
}

	private void startPlayer() {
		if(mMedia!=null ){ 
				if(mPlayHistoryInfo.getMediatype().equals("movie")){ 
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
	}
	
	private void setPlayerUrl(){
		try {  
			if(mMedia != null){ 
				Intent intent = new Intent(HistoryActivity.this,SystemPlayer.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("media", mMedia);
				intent.putExtras(mBundle);
				HistoryActivity.this.startActivity(intent);
				 overridePendingTransition(R.anim.fade, R.anim.hold); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param foundHistoryOnServer
	 */
	private void playByHistoryRecordOnServer(Boolean foundHistoryOnServer) {/*
		if (!foundHistoryOnServer) {
			return;
		}

		if (!Utils.isNetworkAvailable(this)) {
			Toast.makeText(this, R.string.netdown, Toast.LENGTH_SHORT).show();
		}

		MediaItem mSelection = parseWithMType(mMedia);
		String url = "";
		if (mSelection == null && null != mPlayHistoryInfo) {
			mSelection = parseWithPlayHistory();
			url = mPlayHistoryInfo.getFsp();
		}
		if (null == mSelection) {
			DialogUtil.closeWaitingDialog();
			setTextAndShow(R.string.player_error_default_text);
			return;
		}
		VideoClarityInfo videoClarityInof = mSelection.getmDefinitionInfo();
		if  ((null != videoClarityInof && null !=videoClarityInof.getAllDefinition()) || "none".equals(mSelection.getPurl())) {
			if ((VideoClarityUtils.checkDefaultPlayTypeExist(videoClarityInof) 
					&& VideoClarityUtils.checkDefaultPlayUrlExist(videoClarityInof))
					|| "none".equals(mSelection.getPurl())) {
				PlayerUtil.startPlayerFirst(this, mMedia,mSelection, url);
			}else {
				//Tip 视频加载失败看看其他的吧 add by zhangshuo 
				DialogUtil.setContext(HistoryActivity.this);
				DialogUtil
						.showDialog(
								null,
								getString(R.string.player_sure),
								null,
								getString(R.string.player_error_default_text),
								false);
				DialogUtil.closeWaitingDialog();
				LogUtil.e(TAG,
						"VideoClarityInfo errro In MediaInfoUtil-----------");
				return;
			}
		} else if(!Utils.isEmpty(mSelection.getFsp())) {
			new NetWorkTask().execute(this,Utils.GET_PLAY_HISTORY_LIST_DATA, mPlayHistoryInfo.getFsp(), mediaItem);
		} else {
			setTextAndShow(R.string.player_error_default_text);
		}
	
		DialogUtil.closeWaitingDialog();
	*/}

//	private MediaItem parseWithMType(Media media) {
//		MediaItem result = null;
//		if ("movie".equals(media.getMtype())) {
//			ArrayList<MediaItem> items = media.getGylang();
//			if (items != null && items.size() > 0) {
//				result = items.get(0);
//			}
//		}
//		return result;
//	}

//	private MediaItem parseWithPlayHistory() {
//		String language = mPlayHistoryInfo.getLanguage();
//		int position = mPlayHistoryInfo.getMovie_position();
//
//		ArrayList<MediaItem> items = mMedia.getLangAgent(language).getMediaItems();
//
//		MediaItem result = null;
//		if (null != items && items.size() > 0 && position >= 0 && position <  items.size() )  {
//			result = items.get(position);
//		}
//		return result;
//	}

//	private void play(MediaItem mediaItem, PlayHistoryInfo playHistory) {
//		if (mediaItem != null && mMedia != null && playHistory != null && null != mediaItem.getPlayList() && (mediaItem.getPlayList()).size() > 0) {
//			PlayerUtil.startPlayerByPlayHistory(HistoryActivity.this, mMedia,playHistory, mediaItem);
//			DialogUtil.closeWaitingDialog();
//		}else {
//			setTextAndShow(R.string.player_error_default_text);
//		}
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
//			overridePendingTransition(R.anim.hold, R.anim.push_bottom_out);
			return true;
		}
		return false;
	}

	protected void onClickLeftButton() {
		finish();
//		overridePendingTransition(R.anim.hold, R.anim.push_bottom_out);
	}
//
	protected void onClickRightButton() {
		if (hasPlayHistory()) {
			mCleanDataDialog.show();
		} else {
//			ToastUtil.toastPrompt(this, R.string.no_playhistory_data, Toast.LENGTH_SHORT);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(null != mCleanDataDialog){
			mCleanDataDialog.dismiss();
		}
		
		if (null != mToast ) {
			mToast.cancel();
		}
	}
	
	private void setTextAndShow(int strId) {
		if (null != mToast) {
			mToast.setText(strId);
			mToast.show();
		}
	}
}
