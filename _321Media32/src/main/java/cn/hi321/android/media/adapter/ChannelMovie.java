package cn.hi321.android.media.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.utils.ImageLoader;

import com.android.china.R;

public class ChannelMovie extends BaseAdapter {

	private ArrayList<BaiDuRecommend> items;
	private LayoutInflater mInflater;	
	private ImageLoader imageLoader; 
	Context context; 
	public ChannelMovie(Context context, ArrayList<BaiDuRecommend> items ) {
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
				final BaiDuRecommend voide = (BaiDuRecommend) items.get(position);
				convertView = mInflater.inflate(R.layout.item_gridview, null);
				final ImageView imageviewVideo = (ImageView) convertView .findViewById(R.id.imageView1);
				TextView txt = (TextView)convertView.findViewById(R.id.txt_loading); 
				final ProgressBar mProgressLoading = (ProgressBar) convertView
						.findViewById(R.id.progressLoading);
				mProgressLoading.setVisibility(View.GONE);
				TextView scoreId =(TextView)convertView.findViewById(R.id.scoreId);
				String updata = voide.getUpdate();
				int rating = voide.getRating(); 
				System.out.println("rating==="+rating);
				 
				if(rating ==0){
					scoreId.setText("");
				}else{
					float ratingF = Float.valueOf(rating);
					scoreId.setText(ratingF/10+"分");
				}
				 
				txt.setText(voide.getTitle()+""); 
				txt.setVisibility(View.VISIBLE); 
				imageLoader.DisplayImage(voide.getImg_url(), imageviewVideo);//(voide.getImg_url(), imageviewVideo );  
				 
				 
			}
			
		}catch (Exception e) {

			e.printStackTrace();
		}
		return convertView;
		
	} 
}
