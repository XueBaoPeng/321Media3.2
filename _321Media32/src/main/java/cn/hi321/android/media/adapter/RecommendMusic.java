package cn.hi321.android.media.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.utils.ImageLoader;
import cn.hi321.android.media.utils.UIUtils;

import com.android.china.R;

public class RecommendMusic extends BaseAdapter{
	private ArrayList<BaiDuRecommend> items;
	private LayoutInflater mInflater;	
	private ImageLoader imageLoader; 
	Context context; 
	Handler handler ;
	public RecommendMusic(Context context, ArrayList<BaiDuRecommend> items,Handler handler ) {
		super();
		this.context = context;
		this.items = items; 
		this.handler = handler;
		this.mInflater = LayoutInflater.from(context); 
		this.imageLoader = new ImageLoader(context.getApplicationContext(),R.drawable.bg_default,true);
		
	 
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
				final BaiDuRecommend voide = (BaiDuRecommend) items.get(position);
				convertView = mInflater.inflate(R.layout.channel_other_view, null);
				final ImageView imageview = (ImageView) convertView .findViewById(R.id.imageView1);
				TextView name = (TextView)convertView.findViewById(R.id.name);  
				name.setText(voide.getTitle()+"");
				TextView scoreId =(TextView)convertView.findViewById(R.id.scoreId);
				scoreId.setText(voide.getDuration()+""); 
				imageLoader.DisplayImage(voide.getImg_url(), imageview);    
				convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) { 
						if(voide!=null){
							 Message msg = new Message();
							 msg.obj = voide; 
							 msg.what = UIUtils.SHOW_PLAY;
							 handler.sendMessage(msg);
						 }	
					}
				});
			}
			
		}catch (Exception e) {

			e.printStackTrace();
		}
		return convertView;
		
	} 
	 
}
