package cn.hi321.android.media.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.UIUtils;
import cn.waps.AppConnect;

import com.android.china.R;
/**
 * @author 杨光福老师
 * 关于页面
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class AboutMeActivity extends Activity {

	 private Button btnBack; 
	  private int[] imageArray;
	  private ListView listView;  
	  private String[] strArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_main_tab_my_message);
		String[] arrayOfString = new String[4];
		ActivityHolder.getInstance().addActivity(this);
	    arrayOfString[0] = "意见反馈";
	    arrayOfString[1] = "升级";
	    arrayOfString[2] = "自己应用";
	    arrayOfString[3] = "推荐应用";
//	    arrayOfString[4] = "自家应用";
	    this.strArray = arrayOfString;// R.drawable.ic_listitem_favorite, R.drawable.ic_listitem_record,
	    int[] arrayOfInt = {R.drawable.ic_listitem_feedback, R.drawable.ic_listitem_upgrade, R.drawable.ic_listitem_about
	    		,R.drawable.ic_listitem_record};
	    this.imageArray = arrayOfInt;  
	    initView();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart(); 
	}

	private void initView()
	  {
//		 ((MainTitlebar)findViewById(R.id.main_title)).show("我的影视大全");
	    ListView localListView1 = (ListView)findViewById(R.id.view_main_tab_my_listview); 
	    this.listView = localListView1; 
	    ListViewAdapter localListViewAdapter = new ListViewAdapter(strArray, imageArray, AboutMeActivity.this);
	    listView.setAdapter(localListViewAdapter);
	    ListView localListView3 = this.listView;
	    ItemOnClickListener localItemOnClickListener = new ItemOnClickListener();
	    localListView3.setOnItemClickListener(localItemOnClickListener); 
	  }
	class ItemOnClickListener  implements AdapterView.OnItemClickListener
	  {
	    private ItemOnClickListener() {}

	    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
	    {
	     
	      Intent localIntent = null;
	      switch (paramInt) {
	      
//		      case 0:
//		    	  Context localContext1 = MainTabMyMessage.this.context;
//		          localIntent = new Intent(localContext1, FavoriteActivity.class);//收藏夹
//		          break;
//		      case 0:
//		    	  Context localContext2 = MainTabMyMessage.this.context;
//		          localIntent = new Intent(localContext2, RecordActivity.class);//观看记录
//		    	  break;
		      case 0:
//		    	   Context localContext3 = MainTabMyMessage.this.context;
//		           localIntent = new Intent(localContext3, FeedBackActivity.class);//意见反馈
		    	  AppConnect.getInstance(AboutMeActivity.this).showFeedback();
		    	  break;
		      case 1:
		    	  AppConnect.getInstance(AboutMeActivity.this).checkUpdate(AboutMeActivity.this);//升级 
		          break;
		      case 2:
//		    	  AppConnect.getInstance(AboutMeActivity.this).showMore(AboutMeActivity.this, "3f42a4b02948a51cc987af6b833d1720");
//		    	  第二参数app_id为开发者指定的某个应用的WAPS_ID，调用该方法，可直接展示该应用的下载详情页面
		    	  AppConnect.getInstance(AboutMeActivity.this).showMore(AboutMeActivity.this);
		          break;
			  case 3:
				  AppConnect.getInstance(AboutMeActivity.this).showOffers(AboutMeActivity.this);//推荐软件 
		    	  break;
	      	default: 
	      }
	     
	      
	    }
	  }

	  class ListViewAdapter extends BaseAdapter {
	    private Context context;
	    private int[] imageArray;
	    private String[] strArray;

	    public ListViewAdapter(String[] paramArrayOfInt, int[] paramContext, Context arg4)
	    {
	      this.strArray = paramArrayOfInt; 
	      this.context = arg4;
	      this.imageArray = paramContext;
	    }

	    public int getCount()
	    {
	      return this.strArray.length;
	    }

	    public Object getItem(int paramInt)
	    {
	      return this.strArray[paramInt];
	    }

	    public long getItemId(int paramInt)
	    {
	      return paramInt;
	    }

	    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
	    {
	      if (paramView == null) {
	        paramView = UIUtils.getLayoutInflater(this.context).inflate(R.layout.listitem_main_tab_my, null);
	        ViewHolder localViewHolder1 = new ViewHolder();
	        TextView localTextView1 = (TextView)paramView.findViewById(R.id.listitem_main_tab_my_tv);
	        localViewHolder1.tv = localTextView1;
	        TextView localTextView2 = localViewHolder1.tv;
	        String str = this.strArray[paramInt];
	        localTextView2.setText(str);
	        TextView localTextView3 = localViewHolder1.tv;
	        int i = this.imageArray[paramInt];
	        localTextView3.setCompoundDrawablesWithIntrinsicBounds(i, 0, 0, 0);
	        paramView.setTag(localViewHolder1);
	      }else{
	    	  ViewHolder localViewHolder2 = (ViewHolder)paramView.getTag();
	      }
	       
	        return paramView; 
	    }
	  }

	  OnClickListener myBtnOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) { 
			
		}
	};
	    
	  
	  class ViewHolder
	  {
	    ImageView iv;
	    TextView tv;
	  }  
	  protected void onDestroy() {
			super.onDestroy();
			 ActivityHolder.getInstance().removeActivity(this);
		}
		
	  
	  int isExit = 0;
	  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
	  { 
		  
	    if ((paramKeyEvent.getAction() == 0) && (paramInt == 4) ){
	    	
	       if (isExit == 0){
	    	   isExit ++;
	    	   UIUtils.showToast(this, "再点一次可退出");
	    	   return true;
	      }
	       if(isExit == 1){
	    		//以下方法将用于释放SDK占用的系统资源
	    	   AppConnect.getInstance(this).finalize(); 
	   		   this.finish();
	    	}
	    }
	    return super.onKeyDown(paramInt, paramKeyEvent); 

	  }
	  
	  @Override
	 	protected void onResume() {
	 		// TODO Auto-generated method stub
	 		super.onResume();
	 		isExit = 0;
	 		
	 	}
}
