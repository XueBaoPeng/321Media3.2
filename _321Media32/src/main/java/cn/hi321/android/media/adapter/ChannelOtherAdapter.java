package cn.hi321.android.media.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.hi321.android.media.entity.ChannelOtherVideos;
import cn.hi321.android.media.utils.ImageLoader;

import com.android.china.R;

public class ChannelOtherAdapter extends BaseAdapter{
	private ArrayList<ChannelOtherVideos> items;
	private LayoutInflater mInflater;	
	private ImageLoader imageLoader; 
	Context context;
	private Handler myHandler; 
	public ChannelOtherAdapter(Context context, ArrayList<ChannelOtherVideos> items ,Handler myHandler) {
		super();
		this.context = context;
		this.items = items;
		this.myHandler = myHandler;
		this.mInflater = LayoutInflater.from(context);  
		this.imageLoader = new ImageLoader(context.getApplicationContext(),R.drawable.bg_default,true);
			 
	}
	
	public void setData(ArrayList<ChannelOtherVideos> items){
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
				final ChannelOtherVideos voide = (ChannelOtherVideos) items.get(position);
				convertView = mInflater.inflate(R.layout.channel_other_view, null);
				final ImageView imageview = (ImageView) convertView .findViewById(R.id.imageView1);
				TextView name = (TextView)convertView.findViewById(R.id.name);  
				name.setText(voide.getTitle()+"");
				TextView scoreId =(TextView)convertView.findViewById(R.id.scoreId);
				scoreId.setText(voide.getDuration()+""); 
				
				imageLoader.DisplayImage(voide.getImgv_url(), imageview);   
			}
			
		}catch (Exception e) {

			e.printStackTrace();
		}
		return convertView;
		
	} 
	 
}
