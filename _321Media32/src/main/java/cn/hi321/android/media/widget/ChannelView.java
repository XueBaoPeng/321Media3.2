//package cn.hi321.android.media.widget;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedList;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import cn.hi321.android.media.R;
//import cn.hi321.android.media.entity.BaiDuInfo;
//import cn.hi321.android.media.entity.BaiDuRecommend;
//import cn.hi321.android.media.player.VideoInfo;
//import cn.hi321.android.media.ui.ChannelListActivity;
//import cn.hi321.android.media.ui.MediaActivity;
//import cn.hi321.android.media.ui.ShowActivity;
//import cn.hi321.android.media.utils.UIUtils;
//
//public class ChannelView extends LinearLayout {
//	private Context context;
//	private GridView gv; 
//	int screenWidth; 
//	int imageArray[] = { R.drawable.tv, R.drawable.film,
//			R.drawable.cartoon, R.drawable.cz, R.drawable.zongyi, R.drawable.tvz,
//			R.drawable.filmz };
//
//	// 数据排版顺序 电视剧，电影，动漫，综艺
//    private ArrayList<BaiDuInfo> baiduArr;
//    
//    
//	public ChannelView(Context paramContext) {
//		this(paramContext, null);
//	}
//
//	public ChannelView(Context paramContext, AttributeSet paramAttributeSet) {
//		super(paramContext, paramAttributeSet);
//		LinkedList localLinkedList = new LinkedList();
//		this.context = paramContext;
//		View localView = UIUtils.getLayoutInflater(paramContext).inflate(
//				R.layout.view_main_tab_channel, this);
//		setOrientation(1);
//		setFadingEdgeLength(0);
//		setEnabled(true);
//		((MainTitlebar) findViewById(R.id.main_title)).show("频道");
//		GridView localGridView1 = (GridView) findViewById(R.id.view_main_tab_channle_grid);
//		this.gv = localGridView1;
//		GridView localGridView2 = this.gv;
//		ColorDrawable localColorDrawable = new ColorDrawable(
//				R.color.common_orange_color);
//		localGridView2.setSelector(localColorDrawable);
//
//		ItemAdapter adapter = new ItemAdapter();
//		localGridView2.setAdapter(adapter);
//		GridItemOnClick onIten = new GridItemOnClick();
//		localGridView2.setOnItemClickListener(onIten);
//	}
//	
////	  public void setLoadingData(ArrayList<BaiDuInfo> baiduArr)
////	  { 
////	  }
////	  
//
//	class GridItemOnClick implements AdapterView.OnItemClickListener {
//		private GridItemOnClick() {
//		}
//
//		public void onItemClick(AdapterView<?> paramAdapterView,
//				View paramView, int paramInt, long paramLong) {
//			
//			if(UIUtils.hasNetwork(context)){
//				Intent i = new Intent(context, ChannelListActivity.class);
//				switch (paramInt) {
//				case 0:
//					i.putExtra("flag", "tv");// 电视
//					break;
//				case 1:
//					i.putExtra("flag", "movie");// 电影
//					break;
//				case 2:
//					i.putExtra("flag", "katong");// 动漫
//					break;
//				case 3:
//					i.putExtra("flag", "katongzhuanti");// 动漫专题 
//					break;
//				case 4:
//					i.putExtra("flag", "zy");// 综艺
//					break;
//				case 5:
//
//					i = new Intent(context, ShowActivity.class);
//					// Intent jme = new Intent(MainActivity.this,
//					// ShowActivity.class);
//					Bundle bundle = new Bundle();
//					VideoInfo videoInfo = new VideoInfo();
//					videoInfo.setTitle("直播中国");
//					videoInfo
//							.setUrl("http://m.cctv.com/NBA/NBAyaowen/node_232.htm");
//					bundle.putSerializable("VideoInfo", videoInfo);
//					i.putExtra("extra", bundle);
//					i.setFlags(1);
//
//					break;
//				case 6:
//					i.putExtra("flag", "moviezhuanti");// 电影专题
//					break;
//				default:
//					break;
//				}
//				context.startActivity(i);
//			}else{
//				UIUtils.showToast(context, context.getText(R.string.tip_network).toString());
//				 
//			} 
//		}
//	}
//
//	public class ItemAdapter extends BaseAdapter {
//		public ItemAdapter() {
//		}
//
//		public int getCount() {
//			return imageArray.length;
//		}
//
//		public Object getItem(int paramInt) {
//			return imageArray[paramInt];
//		}
//
//		public long getItemId(int paramInt) {
//			return paramInt;
//		}
//
//		public View getView(int paramInt, View paramView,
//				ViewGroup paramViewGroup) {
//
//			// 总布局
//			LinearLayout layout = new LinearLayout(context);
//			layout.setOrientation(LinearLayout.VERTICAL);
//			layout.setGravity(Gravity.CENTER);
//			ImageView imageview = new ImageView(context);
//
//			imageview.setLayoutParams(new LayoutParams(getWidth() / 2 - 10,
//					getHeight() / 5 - 5));
//			imageview.setBackgroundResource(imageArray[paramInt]);
//			layout.addView(imageview);
//			return layout;
//		}
//	}
//
//}