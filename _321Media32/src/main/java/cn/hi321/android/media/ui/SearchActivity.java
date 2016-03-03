package cn.hi321.android.media.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.hi321.android.media.adapter.MovieAdapter;
import cn.hi321.android.media.entity.BaiduResolution;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.entity.SearchData;
import cn.hi321.android.media.http.IBindData;
import cn.hi321.android.media.http.NetWorkTask;
import cn.hi321.android.media.player.SystemPlayer;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.Contents;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;

import com.android.china.R;
//import cn.hi321.android.media.adapter.ViewPagerAdapter;

public class SearchActivity extends Activity implements IBindData {

	  private LayoutInflater flater; 
	  private EditText editSearch; 
	  private ImageView searchButton;
	  private ListView searchListview; 
	  private String mySearchText = "";
	  ImageView iamge;
	  private ArrayList<SearchData>  searchData;
	  private MovieAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_main_tab_search);
		 ActivityHolder.getInstance().addActivity(this);
		 editSearch = (EditText)findViewById(R.id.edit_search);
	    searchButton = (ImageView)findViewById(R.id.searchButton);
	    searchListview =(ListView)findViewById(R.id.view_main_tab_my_listview);
	    iamge =(ImageView)findViewById(R.id.iamge);
	    iamge.setVisibility(View.VISIBLE); 
//	    arrayResult = new ArrayList<SearchResult>(); 
	    myAdapter = new MovieAdapter(SearchActivity.this, searchData, myHandler);
		searchListview.setAdapter(myAdapter);
	    editSearch.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
		 
				if (s.length() != 0) {
					iamge.setVisibility(View.GONE);
					mySearchText = s.toString(); 
				}  
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { 
				
			}
			
			public void afterTextChanged(Editable s) { 
			}
		});
	    searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					 
					if(mySearchText !=null &&  !mySearchText.equals("")){
						  mySearchText  = URLEncoder.encode(mySearchText, "utf-8") ;
					      if(UIUtils.hasNetwork(SearchActivity.this)){
					    	  Utils.startWaitingDialog(SearchActivity.this);
					    	 String url = "http://search.shouji.baofeng.com/msearch_type.php?query="+mySearchText+"&offset=0&limit=10&mtype=normal&ver="+Contents.versionBaoFeng;
							  new NetWorkTask().execute(SearchActivity.this,UIUtils.Research_Activity,
										url); 
				  		  }else{
				  			UIUtils.showToast(SearchActivity.this,SearchActivity.this.getText(R.string.tip_network).toString());
						 }
					} 
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}); 
	   
	}

	 
	@Override
	public void bindData(int tag, Object object) {
		 try {
			if(tag == UIUtils.Research_Activity){
				if(object!=null){
					searchData =(ArrayList<SearchData> )object; 
					myAdapter.setData(searchData);
					myAdapter.notifyDataSetChanged();
					Utils.closeWaitingDialog();
				}
				
			} else if(tag == UIUtils.GET_PLAY_DATA){ 
				if(object!=null){
					try { 
						BaiduResolution  mediaItem = (BaiduResolution)object;
						if(mediaItem != null){
							String video_source_url =mediaItem.getVideo_source_url();
							if(!Utils.isEmpty(video_source_url)){
								Intent intent = new Intent(SearchActivity.this,SystemPlayer.class);
								MediaItem  videoInfo = new MediaItem();
								videoInfo.setUrl(video_source_url); 
								videoInfo.setSourceUrl(mediaItem.getmSourceUrl());
								String mediaName = mMediaName;
								videoInfo.setTitle(mediaName);
								videoInfo.setLive(false);
								Bundle mBundle = new Bundle();
								if(videoInfo != null)
								mBundle.putSerializable("VideoInfo", videoInfo);
								intent.putExtras(mBundle); 
								startActivity(intent);
								 overridePendingTransition(R.anim.fade, R.anim.hold); 
//								mItem = "";
							}else{
								String sourceUri = mediaItem.getSourceUrl();
								if(!Utils.isEmpty(sourceUri)){
									Intent intent = new Intent();
									intent.setAction(Intent.ACTION_VIEW);
									intent.setData(Uri.parse(sourceUri));
									startActivity(intent);
									 overridePendingTransition(R.anim.fade, R.anim.hold); 
								}else{
									Toast.makeText(SearchActivity.this, "该视频无法播放", 1).show();
								}
								
							}
							
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					Utils.closeWaitingDialog();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
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
			switch (msg.what) {
//			case UIUtils.SHOW_PLAY:
//				String videoUrl = (String)msg.obj;
//				playView(videoUrl);
//				break;

			default:
				break;
			}
		}
		
	};

	
	private String mMediaName = null;
	private void playView(String videoUrl ){
		if(!Utils.isEmpty(videoUrl)){
			try {
				BaiduResolution playDatas = new BaiduResolution();
				playDatas.setSourceUrl(videoUrl);
//				if(searchData.getQueryWord()!=null ){
//					mMediaName = ""+searchData.getQueryWord(); 
//				} 
				String baiduService = "http://gate.baidu.com/tc?m=8&video_app=1&ajax=1&src="+videoUrl;
				if(!Utils.isEmpty(baiduService)){ 
					if(UIUtils.hasNetwork(SearchActivity.this)){
						Utils.startWaitingDialog(SearchActivity.this);
						new NetWorkTask().execute(SearchActivity.this,UIUtils.GET_PLAY_DATA,
								baiduService,playDatas,myHandler);
			  		  }else{
			  			UIUtils.showToast(SearchActivity.this, SearchActivity.this.getText(R.string.tip_network).toString());
					 }
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
