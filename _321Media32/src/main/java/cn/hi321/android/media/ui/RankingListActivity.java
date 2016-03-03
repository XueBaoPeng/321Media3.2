package cn.hi321.android.media.ui;
 
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import cn.hi321.android.media.adapter.RankListAdapter;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.http.IBindData;
import cn.hi321.android.media.http.NetWorkTask;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;

import com.android.china.R;
 
/**
 * @author 杨光福老师
 * 评分页面
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class RankingListActivity extends Activity implements IBindData{

	private RankListAdapter myRankListAdapter;
	private ImageButton returnButton,btn_search;
	private TextView rankMovie,rankTV,rankTVShow,rankComic;
	private ProgressBar progressbar ;
	private LinearLayout progressLinear;
	private ListView listview;
	private ArrayList<BaiDuRecommend> arrRankListMovie,arrRankListTV,arrRankListTVShow,arrRankListComic; 
	private String req = null;
	private int pageMovie = 1,pageTV =1,pageTVShow=1,pageComic=1;
	private boolean isGetData = false;
	private TextView bottomText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rankinglist);
		returnButton =(ImageButton)findViewById(R.id.btn_logo);
		btn_search =(ImageButton)findViewById(R.id.btn_search);
		rankMovie = (TextView)findViewById(R.id.rank_movie);
		rankTV = (TextView)findViewById(R.id.rank_tv);
		rankTVShow = (TextView)findViewById(R.id.rank_tvshow);
		rankComic = (TextView)findViewById(R.id.rank_comic);
		listview = (ListView)findViewById(R.id.listview);
		 
		bottomText = (TextView)findViewById(R.id.load_more_textview);
		
		arrRankListMovie = new ArrayList<BaiDuRecommend>();
		arrRankListTV = new ArrayList<BaiDuRecommend>();
		arrRankListTVShow = new ArrayList<BaiDuRecommend>();
		arrRankListComic = new ArrayList<BaiDuRecommend>(); 
		
		progressbar = (ProgressBar)findViewById(R.id.progressbar_loading);
		progressLinear = (LinearLayout)findViewById(R.id.progressLinear);
		progressbar.setVisibility(View.GONE);
		progressLinear.setVisibility(View.GONE);
		
		
		rankMovie.setOnClickListener(myOnClick);
		rankTV.setOnClickListener(myOnClick);
		rankTVShow.setOnClickListener(myOnClick);
		rankComic.setOnClickListener(myOnClick);
		
		btn_search.setVisibility(View.GONE);
		returnButton.setOnClickListener(myOnClick);
		btn_search.setOnClickListener(myOnClick);
		setTitleButton(0);
		listview.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
			   if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			      //判断是否滚动到底部 
				   
			      if ((view.getLastVisiblePosition() == view.getCount() - 1)&&!isGetData) {
			    	   
//			    		  bottomText.setText("全部加载完了");
//			    		  bottomText.setVisibility(View.VISIBLE);
//			    		  progressbar.setVisibility(View.GONE); 
//			    		  progressLinear.setVisibility(View.VISIBLE); 
			    		  isGetData = true;
				    	  try {
				    		  progressbar.setVisibility(View.VISIBLE);
					    	  progressLinear.setVisibility(View.VISIBLE);
					    	  bottomText.setVisibility(View.VISIBLE);
					    	  bottomText.setText("正在加载数据");
						  
					   	   	 if(req.equals("movie")){ 
					   	   		 pageMovie++;
					   	   		 setRequestData(pageMovie,arrRankListMovie);
							 }else if(req.equals("tvplay")){ 
								 pageTV ++;
								 setRequestData(pageTV,arrRankListTV);
							 }else if(req.equals("tvshow")){ 
								 pageTVShow++;
								 setRequestData(pageTVShow,arrRankListTVShow);
							 }else if(req.equals("comic")){ 
								 pageComic++;
								 setRequestData(pageComic,arrRankListComic);
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
  
	}
	
	OnClickListener myOnClick = new OnClickListener() {
			
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.btn_logo:
				RankingListActivity.this.finish();
				break;
			case R.id.rank_movie:
				setTitleButton(1);
				break;
			case R.id.rank_tv:
				setTitleButton(0);
				break;
			case R.id.rank_tvshow:
				setTitleButton(2);
				break;
			case R.id.rank_comic:
				setTitleButton(3);
				break;

			default:
				break;
			}
		}
	};
	
	private void setTitleButton(int i){
		rankMovie.setBackgroundResource(R.color.common_transparent_color);
		rankTV.setBackgroundResource(R.color.common_transparent_color);
		rankTVShow.setBackgroundResource(R.color.common_transparent_color);
		rankComic.setBackgroundResource(R.color.common_transparent_color);
		
		switch (i) {
		case 0: 
			req = "tvplay";
			pageTV=1; 
			rankTV.setBackgroundResource(R.drawable.filter_text_bg_selected);
		 
			if(arrRankListTV.size() > 0 ){
				 refleshView(arrRankListTV);
			}else{
				setRequestData(pageTV,arrRankListTV);
			}
			break;
		case 1:
			req = "movie";
			pageMovie = 1; 
			rankMovie.setBackgroundResource(R.drawable.filter_text_bg_selected);
			 
			if(arrRankListMovie.size() > 0 ){
				 refleshView(arrRankListMovie);
			}else{
				setRequestData(pageMovie,arrRankListMovie);
			}
			
			break;
		case 2:
			req = "tvshow";
			pageTVShow=1; 
			rankTVShow.setBackgroundResource(R.drawable.filter_text_bg_selected);
		 	
			if(arrRankListTVShow.size() > 0 ){
				 refleshView(arrRankListTVShow);
			}else{
				setRequestData(pageTVShow,arrRankListTVShow);
			}
			break;
		case 3:
			req = "comic";
			pageComic=1; 
			rankComic.setBackgroundResource(R.drawable.filter_text_bg_selected);
			if(arrRankListComic.size() > 0 ){
				 refleshView(arrRankListComic);
			}else{
				setRequestData(pageComic,arrRankListComic);
			}
		
			break; 
		default:
			break;
		}
		
	}
	private void setRequestData(int page , ArrayList<BaiDuRecommend> arrRankList ){
		try {
			String base_url =getUrl(req, page); 
			if(base_url!=null&&!base_url.equals("")&&!base_url.equals("null")&&UIUtils.hasNetwork(RankingListActivity.this)){
				 
				Utils.startWaitingDialog(RankingListActivity.this); 
				new NetWorkTask().execute(RankingListActivity.this,UIUtils.BaiDuInfoFlag,
						base_url,arrRankList ); 
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private String getUrl(String req,int page){ 
		String url =  "http://app.video.baidu.com/adnativelist/?req="+req+"&page="+pageMovie;
		return url;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void bindData(int tag, Object object) {
		 if(tag == UIUtils.BaiDuInfoFlag&&object!=null){ 
			
			 if(req.equals("movie")){
				 arrRankListMovie = (ArrayList<BaiDuRecommend>)object; 
				 refleshView(arrRankListMovie);
			 }else if(req.equals("tvplay")){
				 arrRankListTV = (ArrayList<BaiDuRecommend>)object; 
				 refleshView(arrRankListTV);
			 }else if(req.equals("tvshow")){
				 arrRankListTVShow = (ArrayList<BaiDuRecommend>)object; 
				 refleshView(arrRankListTVShow);
			 }else if(req.equals("comic")){
				 arrRankListComic = (ArrayList<BaiDuRecommend>)object; 
				 refleshView(arrRankListComic);
			 }
			 isGetData = false;
		 }
		 Utils.closeWaitingDialog();
	}
	
	private void refleshView(ArrayList<BaiDuRecommend> items){
		if(myRankListAdapter==null){
			myRankListAdapter = new RankListAdapter(RankingListActivity.this, items);	
			listview.setAdapter(myRankListAdapter);
		}else{
			myRankListAdapter.setData(items);
			myRankListAdapter.notifyDataSetChanged();
		}
		
		  progressbar.setVisibility(View.GONE);
    	  progressLinear.setVisibility(View.GONE);
    	  bottomText.setVisibility(View.GONE);
    	  bottomText.setText(" ");
	
	}



}
