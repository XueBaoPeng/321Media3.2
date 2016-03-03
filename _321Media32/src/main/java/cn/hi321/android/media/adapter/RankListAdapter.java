package cn.hi321.android.media.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.hi321.android.media.entity.BaiDuRecommend; 
import cn.hi321.android.media.ui.MediaActivity;
import cn.hi321.android.media.ui.MediaShowActivity;
import cn.hi321.android.media.ui.RankingListActivity;
import cn.hi321.android.media.utils.ImageLoader;
import cn.hi321.android.media.utils.UIUtils;

import com.android.china.R;
 

public class RankListAdapter extends BaseAdapter {

	private ArrayList<BaiDuRecommend> items;
	private LayoutInflater mInflater;	
	private ImageLoader imageLoader; 
	Context context;
	public RankListAdapter(Context context, ArrayList<BaiDuRecommend> items ) {
		super();
		this.context = context;
		this.items = items;
		this.mInflater = LayoutInflater.from(context); 
		this.imageLoader = new ImageLoader(context.getApplicationContext(),R.drawable.bg_list_default,true);
		 
	}
	
	public void setData(ArrayList<BaiDuRecommend> items){
		this.items = items;
	}
	
	@Override
	public int getCount() {
		if (this.items != null && this.items.size() > 0)
			return items.size();
		return 0;
	}

	public Object getItem(int position) { 
		return items.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{ 
			if(items !=null&& items.size() >position){
				final BaiDuRecommend voide = (BaiDuRecommend)items.get(position);
				convertView = mInflater.inflate(R.layout.tab_home_listitem_video, null);
				final ImageView imageviewVideo = (ImageView) convertView .findViewById(R.id.imageview_video);
				TextView name = (TextView)convertView.findViewById(R.id.textview_name);
				TextView updatetip = (TextView)convertView.findViewById(R.id.textview_updatetip); 
				TextView act = (TextView)convertView.findViewById(R.id.textview_act);
				Button button = (Button)convertView.findViewById(R.id.btn_play);
				TextView status_day = (TextView)convertView.findViewById(R.id.status_day);
				button.setVisibility(View.GONE);
				if(position==0){ 
					status_day.setBackgroundResource(R.drawable.video_icon_ranking_1_bg);
				}else if(position==1){ 
					status_day.setBackgroundResource(R.drawable.video_icon_ranking_2_3_bg);
				}else if(position==2){ 
					status_day.setBackgroundResource(R.drawable.video_icon_ranking_2_3_bg);
				}else{
					status_day.setBackgroundResource(R.drawable.video_icon_ranking_bg);
				}
				status_day.setText((position+1)+"");
				if(voide.getWorks_type().equals("movie")){
					int rating = voide.getRating();   
					if(rating ==0){
						act.setText("");
					}else{
						float ratingF = Float.valueOf(rating);
						act.setText(ratingF/10+"分");
					} 
					name.setText(voide.getTitle()+"");
					updatetip.setText(voide.getActor()+""); 
				}else if(voide.getWorks_type().equals("tvplay")){
					name.setText(voide.getTitle()+"");
					act.setText(voide.getActor()+""); 
					updatetip.setText(voide.getUpdate()+""); 
				}else if(voide.getWorks_type().equals("tvshow")){
					name.setText(voide.getTitle()+"");
					act.setText(voide.getActor()+""); 
					updatetip.setText(voide.getUpdate()+""); 
				}else if(voide.getWorks_type().equals("comic")){ 
					name.setText(voide.getTitle()+"");
					act.setText(voide.getType()+""); 
					updatetip.setText(voide.getUpdate()+""); 
				}
				
				imageLoader.DisplayImage(voide.getImg_url(), imageviewVideo);  
				button.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) { 
						
					}
				});
				convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {  
						if(UIUtils.hasNetwork(context)){ 
//							
							try {  
								 
								 Intent intent = new Intent(); 
				        	     intent.putExtra("BaiDuRecommend",voide);
				        	     if(voide.getWorks_type().equals("tvshow")){
				        	    	 intent.setClass(context,MediaShowActivity.class);  
				        	     }else{
				        	    	 intent.setClass(context,MediaActivity.class);  
				        	     }  
								 context.startActivity(intent);
								 
								
							} catch (Exception e) {
								// TODO: handle exception
							}
							 
						}else{
							UIUtils.showToast(context,context.getText(R.string.tip_network).toString());
						}
					}
				}) ;
				 
			}
			
		}catch (Exception e) {

			e.printStackTrace();
		}
		return convertView;
		
	}

}
