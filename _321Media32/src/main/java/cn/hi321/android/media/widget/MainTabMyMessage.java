//package cn.hi321.android.media.widget;
// 
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import cn.hi321.android.media.R; 
//import cn.hi321.android.media.utils.UIUtils;
//import cn.waps.AppConnect;
//
//
//public class MainTabMyMessage extends LinearLayout
//{
////  private static final int ITEM_ABOUT = 4;
////  private static final int ITEM_FAVORITE = 0;
////  private static final int ITEM_FEEDBACK = 2;
////  private static final int ITEM_RECORD = 1;
////  private static final int ITEM_UPGRADE = 3;
//  private Button btnBack;
//  private Context context;
//  private int[] imageArray;
//  private ListView listView; 
////  private Button 
//  private String[] strArray;
//
//  public MainTabMyMessage(Context paramContext)
//  {
//    this(paramContext, null);
//  }
//
//  public MainTabMyMessage(Context paramContext, AttributeSet paramAttributeSet)
//  {
//    super(paramContext, paramAttributeSet);
//    String[] arrayOfString = new String[4];
////    arrayOfString[0] = "收藏夹";
//   
//    arrayOfString[0] = "意见反馈";
//    arrayOfString[1] = "升级";
//    arrayOfString[2] = "自己应用";
//    arrayOfString[3] = "推荐应用";
////    arrayOfString[4] = "自家应用";
//    this.strArray = arrayOfString;// R.drawable.ic_listitem_favorite, R.drawable.ic_listitem_record,
//    int[] arrayOfInt = {R.drawable.ic_listitem_feedback, R.drawable.ic_listitem_upgrade, R.drawable.ic_listitem_about
//    		,R.drawable.ic_listitem_record};
//    this.imageArray = arrayOfInt;
//    this.context = paramContext;
//    setOrientation(1); 
//    setGravity(1);
//    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
//    setLayoutParams(localLayoutParams);
//    setBackgroundResource(R.drawable.bg_fravorite_record);
//    View localView = UIUtils.getLayoutInflater(paramContext).inflate(R.layout.view_main_tab_my_message, this);
//    initView();
//  } 
//  private void initView()
//  {
//    ((MainTitlebar)findViewById(R.id.main_title)).show("我的影视大全");
//    ListView localListView1 = (ListView)findViewById(R.id.view_main_tab_my_listview); 
//    this.listView = localListView1; 
//    ListViewAdapter localListViewAdapter = new ListViewAdapter(strArray, imageArray, context);
//    listView.setAdapter(localListViewAdapter);
//    ListView localListView3 = this.listView;
//    ItemOnClickListener localItemOnClickListener = new ItemOnClickListener();
//    localListView3.setOnItemClickListener(localItemOnClickListener); 
//  }
//    
//  class ItemOnClickListener  implements AdapterView.OnItemClickListener
//  {
//    private ItemOnClickListener() {}
//
//    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
//    {
//     
//      Intent localIntent = null;
//      switch (paramInt) {
//      
////	      case 0:
////	    	  Context localContext1 = MainTabMyMessage.this.context;
////	          localIntent = new Intent(localContext1, FavoriteActivity.class);//收藏夹
////	          break;
////	      case 0:
////	    	  Context localContext2 = MainTabMyMessage.this.context;
////	          localIntent = new Intent(localContext2, RecordActivity.class);//观看记录
////	    	  break;
//	      case 0:
////	    	   Context localContext3 = MainTabMyMessage.this.context;
////	           localIntent = new Intent(localContext3, FeedBackActivity.class);//意见反馈
//	    	  AppConnect.getInstance(context).showFeedback();
//	    	  break;
//	      case 1:
//	    	  AppConnect.getInstance(context).checkUpdate(context);//升级 
//	          break;
//	      case 2:
//	    	  AppConnect.getInstance(context).showMore(context, "3f42a4b02948a51cc987af6b833d1720");
////	    	  第二参数app_id为开发者指定的某个应用的WAPS_ID，调用该方法，可直接展示该应用的下载详情页面
////	    	  Context localContext5 = MainTabMyMessage.this.context;
////	          localIntent = new Intent(localContext5, MediaActivity.class);//关于我们
////	          MainTabMyMessage.this.context.startActivity(localIntent);
//	          break;
//		  case 3:
//			  AppConnect.getInstance(context).showOffers(context);//推荐软件 
//	    	  break;
//      	default: 
//      }
//     
//      
//    }
//  }
//
//  class ListViewAdapter extends BaseAdapter {
//    private Context context;
//    private int[] imageArray;
//    private String[] strArray;
//
//    public ListViewAdapter(String[] paramArrayOfInt, int[] paramContext, Context arg4)
//    {
//      this.strArray = paramArrayOfInt; 
//      this.context = arg4;
//      this.imageArray = paramContext;
//    }
//
//    public int getCount()
//    {
//      return this.strArray.length;
//    }
//
//    public Object getItem(int paramInt)
//    {
//      return this.strArray[paramInt];
//    }
//
//    public long getItemId(int paramInt)
//    {
//      return paramInt;
//    }
//
//    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
//    {
//      if (paramView == null) {
//        paramView = UIUtils.getLayoutInflater(this.context).inflate(R.layout.listitem_main_tab_my, null);
//        MainTabMyMessage.ViewHolder localViewHolder1 = new MainTabMyMessage.ViewHolder();
//        TextView localTextView1 = (TextView)paramView.findViewById(R.id.listitem_main_tab_my_tv);
//        localViewHolder1.tv = localTextView1;
//        TextView localTextView2 = localViewHolder1.tv;
//        String str = this.strArray[paramInt];
//        localTextView2.setText(str);
//        TextView localTextView3 = localViewHolder1.tv;
//        int i = this.imageArray[paramInt];
//        localTextView3.setCompoundDrawablesWithIntrinsicBounds(i, 0, 0, 0);
//        paramView.setTag(localViewHolder1);
//      }else{
//    	  MainTabMyMessage.ViewHolder localViewHolder2 = (MainTabMyMessage.ViewHolder)paramView.getTag();
//      }
//       
//        return paramView; 
//    }
//  }
//
//  OnClickListener myBtnOnClick = new OnClickListener() {
//	
//	@Override
//	public void onClick(View v) { 
//		
//	}
//};
//    
//  
//  class ViewHolder
//  {
//    ImageView iv;
//    TextView tv;
//  } 
//}