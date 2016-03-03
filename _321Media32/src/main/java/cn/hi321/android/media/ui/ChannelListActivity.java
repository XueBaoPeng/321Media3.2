package cn.hi321.android.media.ui;
 
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.hi321.android.media.adapter.ChannelMovie;
import cn.hi321.android.media.entity.BaiDuChannelSearch;
import cn.hi321.android.media.entity.BaiDuChannelVideo;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.entity.ChannelInfo;
import cn.hi321.android.media.entity.Conds;
import cn.hi321.android.media.entity.ValuesSearch;
import cn.hi321.android.media.http.IBindData;
import cn.hi321.android.media.http.NetWorkTask;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.Contents;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;

import com.android.china.R;
/**
 * @author 杨光福老师
 * 频道列表页面
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class ChannelListActivity extends BaseActivity implements IBindData{

	
	private BaiDuChannelVideo channelVideo;
	private ImageButton returnButton;
	private TextView title;
	private ImageButton btn_search; 
	private LinearLayout myGalleryType;
	private LinearLayout myGalleryArea;
	private LinearLayout myGalleryActor;
	private GridView  channleGridView;
	private ChannelInfo channelInfo;
	private BaiDuChannelSearch channelSearch;  
	private ChannelMovie recommendMovieHot;
	private boolean isGetData = false;
	private LayoutInflater mInflater;
	private ProgressBar progressbar ;
	private LinearLayout progressLinear;
	private TextView bottomText;
	private Bitmap bitmapUp;
	private Bitmap bitmapDown;
	private TextView searchText;
	
	@Override
	protected void onCreate(Bundle paramBundle) { 
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_videolist); 
		 ActivityHolder.getInstance().addActivity(this);
		returnButton =(ImageButton)findViewById(R.id.btn_logo);
		title = (TextView)findViewById(R.id.tv_title); 
		btn_search =(ImageButton)findViewById(R.id.btn_search);
		this.mInflater = LayoutInflater.from(ChannelListActivity.this);
		channleGridView =(GridView)findViewById(R.id.view_main_tab_channle_grid);   
		progressbar = (ProgressBar)findViewById(R.id.progressbar_loading);
		progressLinear = (LinearLayout)findViewById(R.id.progressLinear);
		searchText = (TextView)findViewById(R.id.searchText);
		bottomText = (TextView)findViewById(R.id.load_more_textview);
		searchText.setOnClickListener(myOnClick);
		progressbar.setVisibility(View.GONE);
		progressLinear.setVisibility(View.GONE);
		channelInfo = new ChannelInfo();
		bitmapUp =  BitmapFactory.decodeResource(ChannelListActivity.this.getResources(),
				R.drawable.titlebar_filter_expanded_normal);
		
		bitmapDown =  BitmapFactory.decodeResource(ChannelListActivity.this.getResources(),
				R.drawable.titlebar_filter_shrinked_normal);
		Intent i = getIntent();
		initPopwindow();
		if(i.getSerializableExtra("channelVideoInfo")!=null){
			channelVideo = (BaiDuChannelVideo)i.getSerializableExtra("channelVideoInfo");
			if(channelVideo!=null){
				try {
					String base_url = channelVideo.getBase_url(); 
					title.setText(channelVideo.getName()+"");
					if(base_url!=null&&!base_url.equals("")&&!base_url.equals("null")&&UIUtils.hasNetwork(ChannelListActivity.this)){
						String path =channelVideo.getBase_url()+ urlRequest("","","",0+"",18+"");
						 
						Utils.startWaitingDialog(ChannelListActivity.this); 
						new NetWorkTask().execute(ChannelListActivity.this,UIUtils.Channel_Video_Info,
								path,channelInfo,mainHandler); 
					}
				
				} catch (Exception e) {
					// TODO: handle exception
				}
				
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
			      if ((view.getLastVisiblePosition() == view.getCount() - 1)&&!isGetData&&channelInfo!=null) {
			    	  
			    	  if(channelInfo.getVideo_num() == view.getCount()){
			    		  bottomText.setText("全部加载完了");
			    		  bottomText.setVisibility(View.VISIBLE);
			    		  progressbar.setVisibility(View.GONE); 
			    		  progressLinear.setVisibility(View.VISIBLE);
			    	  }else{
			    		  isGetData = true;
				    	  try {
				    		  progressbar.setVisibility(View.VISIBLE);
					    	  progressLinear.setVisibility(View.VISIBLE);
					    	  bottomText.setVisibility(View.VISIBLE);
					    	  bottomText.setText("正在加载数据");
							  int end = channelInfo.getEnd()+18;
							  String path =channelVideo.getBase_url()+ urlRequest(channelInfo.getCurCondsArr().get(0).getValue(),channelInfo.getCurCondsArr().get(1).getValue(),channelInfo.getCurCondsArr().get(2).getValue(),channelInfo.getEnd()+"",end+"");
							  new NetWorkTask().execute(ChannelListActivity.this,UIUtils.Channel_ScrollStateChanged,
									path,channelInfo,mainHandler);  
							} catch (Exception e) {
								// TODO: handle exception
							}
			    	  }
			    	  
			    	  
//	                   Toast.makeText(getApplicationContext(), "第"+1+"页", Toast.LENGTH_LONG).show();
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
				
				if(UIUtils.hasNetwork(ChannelListActivity.this)){  
					try {
						if(channelInfo!=null&&channelInfo.getVideosArr()!=null
								 &&channelInfo.getVideosArr().size()>arg2){
							 BaiDuRecommend baiDuRecommend = channelInfo.getVideosArr().get(arg2);
							 baiDuRecommend.setWorks_type(channelVideo.getTag());
							 System.out.println(baiDuRecommend.getWorks_id()+"''''"+ baiDuRecommend.getWorks_type());
							 
							 Intent intent = new Intent(); 
			        	     intent.putExtra("BaiDuRecommend",baiDuRecommend);
							if(channelVideo!=null&&channelVideo.getTag().equals("tvshow")){ 
				        		 intent.setClass(ChannelListActivity.this,MediaShowActivity.class);   
							}else{ 
				        		 intent.setClass(ChannelListActivity.this,MediaActivity.class);  
							}
							 ChannelListActivity.this.startActivity(intent);
							 overridePendingTransition(R.anim.fade, R.anim.hold);
						 }
					} catch (Exception e) {
						// TODO: handle exception
					}
					 
				}else{
					UIUtils.showToast(ChannelListActivity.this,ChannelListActivity.this.getText(R.string.tip_network).toString());
				}
				
			}
		});
		  
	}
	
 
	OnClickListener myOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_logo:
				ChannelListActivity.this.finish();
				break;
			case R.id.btn_search://调用筛查接口
				if(channelSearch!=null){
//					ArrayList<Conds> arrConds = channelSearch.getCondsArr(); 
					showAsDropDown() ;
				} 
				break;
			case R.id.searchText:
				if(channelSearch!=null){
//					ArrayList<Conds> arrConds = channelSearch.getCondsArr(); 
					showAsDropDown() ;
				} 
				break;

			default:
				break;
			}
		} 
	};

	
	
	protected void onDestroy() {
		super.onDestroy();
		 ActivityHolder.getInstance().removeActivity(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	private void refleshView( ArrayList<BaiDuRecommend> arrayList){
		if(recommendMovieHot ==null){
		    recommendMovieHot = new ChannelMovie(ChannelListActivity.this,arrayList);
			channleGridView.setAdapter(recommendMovieHot);
		}else{
			recommendMovieHot.setData(arrayList);
			recommendMovieHot.notifyDataSetChanged();
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		try {
			if(object == null ){
				Utils.closeWaitingDialog();
				progressbar.setVisibility(View.GONE);
				progressLinear.setVisibility(View.GONE);
				bottomText.setVisibility(View.GONE);
			}
			
			
			if(tag == UIUtils.Channel_Video_Info){
				if(object!=null){
					channelInfo =(ChannelInfo)object;
//					pagesize = channelInfo.getEnd();
					if( channelVideo.getFilter()!=null){//获取检索数据
						new NetWorkTask().execute(ChannelListActivity.this,UIUtils.Channel_Search,
								 channelVideo.getFilter(),mainHandler);
					}else{
						Utils.closeWaitingDialog();
					}
					 
					refleshView(channelInfo.getVideosArr());
				} 
			
			}else if(tag == UIUtils.Channel_ScrollStateChanged){
				if(object!=null){
					
					channelInfo =(ChannelInfo)object;   
					refleshView(channelInfo.getVideosArr());
					
					isGetData = false;
//					Utils.closeWaitingDialog();
					progressbar.setVisibility(View.GONE);
					progressLinear.setVisibility(View.GONE);
					bottomText.setVisibility(View.GONE);
				}
			}else if(tag == UIUtils.Channel_Search_Video){//检索
				if(object!=null){ 
					channelInfo =(ChannelInfo)object;   
					refleshView(channelInfo.getVideosArr());
					Utils.closeWaitingDialog(); 
				}
			}else if(tag == UIUtils.Channel_Search ){//检索返回数据
				if(object!=null){ 
					channelSearch = (BaiDuChannelSearch)object;
					if(channelSearch!=null){
						ArrayList<Conds> arrConds = channelSearch.getCondsArr(); 
						for(int i=0;i<arrConds.size();i++){  
							Conds conds = arrConds.get(i);
							String field = conds.getField();
							if(field.equals("type")){ 
								arrValueSearchType = conds.getValuesArr();  
								continue;
							}else if(field.equals("area")){ 
								arrValueSearchArea = conds.getValuesArr();  
								continue;	
							}else if(field.equals("start")){ 
								arrValueSearchStart = conds.getValuesArr();  
								continue;
							}
						}
						showAsDropDown() ;
					}
					
					
				}
				Utils.closeWaitingDialog();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Utils.closeWaitingDialog();
		}
		
		
	}
	
	 
		/**
		 * 拼凑请求路径
		 * 检索也是这个接口
		 * */
		private String urlRequest(String type,String area,String start,String beg,String end){
			//?type=&area=&start=&beg=18&end=36&version=3.5.2
			String url = "?type="+type+"&area="+area+"&start="+start+"&beg="+beg+"&end="+end+"&version="+Contents.version;
			return url;
		}
		
 
		ArrayList<ValuesSearch> arrValueSearchType = null;
		ArrayList<ValuesSearch> arrValueSearchArea = null;
		ArrayList<ValuesSearch> arrValueSearchStart = null;
		public PopupWindow popupWindow;
		private HorizontalScrollView horizontalScrollViewType;
		private HorizontalScrollView horizontalScrollViewArea;
		private HorizontalScrollView horizontalScrollViewActor;
		private Boolean pop_flag = false;
		private void initPopwindow(){ 
			View view = LayoutInflater.from(ChannelListActivity.this)
			.inflate(R.layout.popmenu, null); 
//			bagpanelpage = (LinearLayout)view.findViewById(R.id.bagpanelpage);  
			myGalleryType = (LinearLayout)view.findViewById(R.id.myGalleryType);
			myGalleryArea = (LinearLayout)view.findViewById(R.id.myGalleryArea);
			myGalleryActor = (LinearLayout)view.findViewById(R.id.myGalleryActor);
			horizontalScrollViewType = (HorizontalScrollView)view.findViewById(R.id.horizontalScrollViewType);
			horizontalScrollViewArea = (HorizontalScrollView)view.findViewById(R.id.horizontalScrollViewArea);
			horizontalScrollViewActor = (HorizontalScrollView)view.findViewById(R.id.horizontalScrollViewActor);
			popupWindow = new PopupWindow(view,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setBackgroundDrawable(new BitmapDrawable()); 
			popupWindow.setOnDismissListener(new OnDismissListener() {
				public void onDismiss() {
					// TODO Auto-generated method stub
					btn_search.setBackgroundResource(R.drawable.titlebar_filter_expanded_normal);;
				}
			});
			 
		    btn_search.setBackgroundResource(R.drawable.titlebar_filter_shrinked_normal); 
		} 
		
	public void showAsDropDown() {
		try { 
			myGalleryType.removeAllViews();
			myGalleryArea.removeAllViews();
			myGalleryActor.removeAllViews();
			
			btn_search.setBackgroundResource(R.drawable.titlebar_filter_shrinked_normal); 
			if(arrValueSearchType!=null&&arrValueSearchType.size()>0){ 
				horizontalScrollViewType.setVisibility(View.VISIBLE);
				 for(int i=0;i<arrValueSearchType.size();i++){
						myGalleryType.addView(insertType(arrValueSearchType.get(i),i));
						 
			     }
			}else{
				horizontalScrollViewType.setVisibility(View.GONE);
			}
			if(arrValueSearchArea!=null&&arrValueSearchArea.size()>0){
				horizontalScrollViewArea.setVisibility(View.VISIBLE);
				 for(int i=0;i<arrValueSearchArea.size();i++){
						myGalleryArea.addView(insertArea(arrValueSearchArea.get(i),i));
//						myGalleryArea.setBackgroundResource(R.drawable.bg_home_title);
			     }
			}else{
				horizontalScrollViewArea.setVisibility(View.GONE);
			}
			if(arrValueSearchStart!=null&&arrValueSearchStart.size()>0){
				horizontalScrollViewActor.setVisibility(View.VISIBLE);
				 for(int i=0;i<arrValueSearchStart.size();i++){
						myGalleryActor.addView(insertStart(arrValueSearchStart.get(i),i));
//						myGalleryActor.setBackgroundResource(R.drawable.bg_home_title);
			     }
			}else{
				horizontalScrollViewActor.setVisibility(View.GONE);
			}
			popupWindow.showAsDropDown(title, 10, ChannelListActivity.this.getResources().getDimensionPixelSize(
							R.dimen.popmenu_yoff)); 
			popupWindow.setFocusable(true); 
			popupWindow.setOutsideTouchable(true); 
			popupWindow.update();
		} catch (Exception e) { 
		}
		
	} 
//	public void dismiss() {
////		btn_search.setBackgroundResource(R.drawable.titlebar_filter_expanded_normal);
//		popupWindow.dismiss();
//	}
 
	private int selectorPostionType = 0;
	private View insertType(final ValuesSearch valueSearch,final int postion) {
		View  v =  mInflater.inflate(R.layout.pomenu_item, null);  
		 Button textview = (Button)v.findViewById(R.id.button);
		textview.setText(valueSearch.getTitle()+""); 
		if(selectorPostionType == postion){
			textview.setBackgroundResource(R.drawable.ads_accountime_bg); 
		}else{
			textview.setBackgroundResource(R.drawable.channel_gallery_item); 
		} 
		textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					 selectorPostionType = postion; 
					 ChannelInfo chan = new ChannelInfo();
					 requestSearch(valueSearch.getTerm(), channelInfo.getCurCondsArr().get(1).getValue(), channelInfo.getCurCondsArr().get(2).getValue(), 0, 18,chan);
					 myGalleryType.removeAllViews();
					 for(int i=0;i<arrValueSearchType.size();i++){
							myGalleryType.addView(insertType(arrValueSearchType.get(i),i));
				     }
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		 
		return v;
	}
	
	private int selectorPostionArea = 0;
	private View insertArea(final ValuesSearch valueSearch,final int postion) {
		View  v =  mInflater.inflate(R.layout.pomenu_item, null);  
		Button textview = (Button)v.findViewById(R.id.button);
		textview.setText(valueSearch.getTitle()+""); 
		if(selectorPostionArea== postion){
			textview.setBackgroundResource(R.drawable.ads_accountime_bg); 
		}else{
			textview.setBackgroundResource(R.drawable.channel_gallery_item); 
		}
	 
		textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					selectorPostionArea = postion;  
					ChannelInfo chan = new ChannelInfo();
					requestSearch( channelInfo.getCurCondsArr().get(0).getValue(),valueSearch.getTerm(), channelInfo.getCurCondsArr().get(2).getValue(), 0, 18,chan);
					myGalleryArea.removeAllViews();
					 for(int i=0;i<arrValueSearchArea.size();i++){
							myGalleryArea.addView(insertArea(arrValueSearchArea.get(i),i)); 
				     }
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		 
		return v;
	}
	private int selectorPostionStart= 0;
	private View insertStart(final ValuesSearch valueSearch,final int postion) {
		 
		View  v =  mInflater.inflate(R.layout.pomenu_item, null);  
		Button textview = (Button)v.findViewById(R.id.button);
		textview.setText(valueSearch.getTitle()+"");
		 
		if(selectorPostionStart== postion){
			textview.setBackgroundResource(R.drawable.ads_accountime_bg); 
		}else{
			textview.setBackgroundResource(R.drawable.channel_gallery_item); 
		}  
		textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					selectorPostionStart = postion;  
					ChannelInfo chan = new ChannelInfo();
					requestSearch( channelInfo.getCurCondsArr().get(0).getValue(),channelInfo.getCurCondsArr().get(1).getValue(), valueSearch.getTerm(), 0, 18,chan);
					
					myGalleryActor.removeAllViews();
					 for(int i=0;i<arrValueSearchStart.size();i++){
							myGalleryActor.addView(insertStart(arrValueSearchStart.get(i),i)); 
				     }
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}); 
		return v;
	}
	private void requestSearch(String type,String area,String start,int beg,int end, ChannelInfo chan ){
		Utils.startWaitingDialog(ChannelListActivity.this); 
		try {
			String typeEncoder  = "";
			if(type!=null && !type.equals("")&&!type.equals("全部"))
			typeEncoder = URLEncoder.encode(type, "utf-8");
			String areaEncoder  = "";
			if(area!=null && !area.equals("") &&!type.equals("全部"))
			areaEncoder = URLEncoder.encode(area, "utf-8");
			String startEncoder  = "";
			if(start!=null && !start.equals("")&&!type.equals("全部"))
			startEncoder = URLEncoder.encode(start, "utf-8");
			String path =channelVideo.getBase_url()+ urlRequest(typeEncoder,areaEncoder,startEncoder,beg+"",end+"");
			new NetWorkTask().execute(ChannelListActivity.this,UIUtils.Channel_Search_Video,
					path,chan,mainHandler);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
} 


//	
//	
//	
//	 
//	private int imagePostion=0;
//	private class MySearchAdapter extends BaseAdapter{
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return arrayList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return arrayList.get(position);
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
//			 View v;
//				if(convertView == null ){
//					v =  mInflater.inflate(R.layout.popsearchitem, null);
//				}else{
//					v = convertView;
//				} 
//			
//				if(arrayList!=null&& arrayList.size() > position){
//					TextView txt = (TextView)v.findViewById(R.id.txt_id);
//					ImageView imageView = (ImageView)v.findViewById(R.id.imageView1);
//					if(imagePostion == position){
//						imageView.setBackgroundResource(R.drawable.bg_oriange_line_long);	
//					} else{
//						imageView.setBackgroundResource(R.drawable.bg_tab_my);
//					} 
//					final SearchInfo searchInfo = arrayList.get(position);
//			
//					if(searchInfo!=null){
//						txt.setText(searchInfo.getTitle()+""); 
//						
//					}
//					v.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) { 
//							imagePostion = position;
//							if(arrayList.get(position) !=null ){  
//								 sort = searchInfo.getSort();
//								 listViewPop.setAdapter(new PopAdapter(arrayList.get(position).getArrayListValues()));
//								 
//							} 
//							notifyDataSetChanged();
//							
//						}
//					});
//				}
//				 
//				return v;
//		}
//		
//	} 
// 
//	private void setGridView(){
//		if(arrayList!=null){
//			 int viewpage = (int)arrayList.size()%PAGE_SIZE==0?
//					 arrayList.size()/PAGE_SIZE:arrayList.size()/PAGE_SIZE+1;
//				bagpanelpage.removeAllViews();
//			    GridView appPage  = new GridView(ChannelListActivity.this);
//				appPage.setNumColumns(3); 
//				appPage.setHorizontalSpacing(5);
//				appPage.setVerticalSpacing(5);
//				MySearchAdapter myAppAdapter = new MySearchAdapter();
//		    	appPage.setAdapter(myAppAdapter);
//		    	appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		    	bagpanelpage.addView(appPage);
//		    	 
//		}
//		
//	}
//	public void showAsDropDown(View parent,	ArrayList<SearchInfo> arrayList) {
//		try {
//			this.arrayList = arrayList;
//			setGridView();
//			if(arrayList!=null){ 
//				 sort =arrayList.get(0).getSort();
//				listViewPop.setAdapter(new PopAdapter(arrayList.get(0).getArrayListValues()));
//			}
//			popupWindow.showAsDropDown(parent, 10, ChannelListActivity.this.getResources().getDimensionPixelSize(
//							R.dimen.popmenu_yoff)); 
//			popupWindow.setFocusable(true); 
//			popupWindow.setOutsideTouchable(true); 
//			popupWindow.update();
//		} catch (Exception e) { 
//		}
//		
//	} 
//	public void dismiss() {
//		popupWindow.dismiss();
//	}
//
//	 
//	private final class PopAdapter extends BaseAdapter {
//		ArrayList<Values> arr ; 
//		
//		PopAdapter(ArrayList<Values> arr ){
//			this.arr =arr; 
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return arr.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return arr.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = LayoutInflater.from(ChannelListActivity.this).inflate(
//						R.layout.pomenu_item, null);
//				holder = new ViewHolder(); 
//				convertView.setTag(holder); 
//				holder.groupItem = (Button) convertView
//						.findViewById(R.id.button); 
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			if(arr!=null){
//				
//				
//				final Values values = arr.get(position);
//				if(values !=null){
//					holder.groupItem.setText(values.getTitle()+""); 
//////					final String key = values.getKey();
//					holder.groupItem.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) { 
//							try {
//								if(values!=null){
//									String key = values.getKey();
//									if(sort.equals("year") ){
//										yearKey = key;
//									}else if(sort.equals("area")){
//										areaKey = key;
//									}else if(sort.equals("category")){
//										categoryKey = key;
//									}
//									Message msg = new Message();
//									HashMap<String, String> map = new HashMap<String, String>();
//									map.put("year", yearKey);
//									map.put("area", areaKey);
//									map.put("category", categoryKey); 
//									msg.obj = map;
//									msg.what = ChannelListActivity.REFLESH_ADAPTER;
//									myHandller.sendMessage(msg); 
//								}
//								
//							} catch (Exception e) {
//								// TODO: handle exception
//							} 
//						
//						}
//					});
//				}
//				
//			} 
//			return convertView;
//		}
//
//		private final class ViewHolder {
//			Button groupItem; 
//		}
//	}
//	 
//
//	@Override
//	public void bindData(int tag, Object object) {
//		// TODO Auto-generated method stub
//		if(tag == UIUtils.GET_USER_DATA){ 
//			Utils.closeWaitingDialog();
//			++pageindex;
//			isGetData = false;
//			isFirstConnect = true;
//			try {
//				if(object !=null){ 
//					  homeResMovie =  (HomeResponse)object; 
//					  pagesize = homeResMovie.getPage().get("pagesize");
//					  ArrayList<VideoInfo> movie = new ArrayList<VideoInfo>();
//					  movie =  homeResMovie.getResult(); 
//					  if(movie ==null)
//					  return;
//					  for(int i = 0;i < movie.size();i++){
//						  VideoInfo video =  movie.get(i);
//						  arrayVideo.add(video);
//					  }  
//					  if(movieAdapter !=null){
//						  movieAdapter.setData(arrayVideo);
//						  movieAdapter.notifyDataSetChanged();
//					  }else{
//							movieAdapter = new MovieAdapter(ChannelListActivity.this, arrayVideo,myHandller); 
//							listview.setAdapter(movieAdapter);
//					  }
//				}
//			
//			} catch (Exception e) {
//				// TODO: handle exception
//			} 
//		}else if(tag == UIUtils.SHOW_WINDOWPOP){
//			Utils.closeWaitingDialog();
//			if(object!=null){
//				ArrayList<SearchInfo> arr = ( ArrayList<SearchInfo>)object;
//				showAsDropDown(btn_search,arr);
////				popSearch.addItems(arr,flag);
////				popSearch.showAsDropDown(btn_search);
//			}
//	
//		}else if(tag == UIUtils.RefleshView){
//			try {
//				Utils.closeWaitingDialog();
//				if(object !=null){ 
//					  HomeResponse homeResSearch =  (HomeResponse)object; 
//					  ArrayList<VideoInfo> movies = homeResSearch.getResult(); ; 
//					  if(movieAdapter !=null){
//						  movieAdapter.setData(movies);
//						  movieAdapter.notifyDataSetChanged();
//					  }else{
//							movieAdapter = new MovieAdapter(ChannelListActivity.this, movies,myHandller); 
//							listview.setAdapter(movieAdapter);
//					  }
//				}
//			
//			} catch (Exception e) {
//				// TODO: handle exception
//			} 
//		}
//	};
//}
