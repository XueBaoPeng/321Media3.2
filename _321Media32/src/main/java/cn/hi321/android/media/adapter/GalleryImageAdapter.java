package cn.hi321.android.media.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.utils.ImageLoader;

import com.android.china.R;

public class GalleryImageAdapter extends BaseAdapter {
	
	private ArrayList<BaiDuRecommend> arrayListIndexFlash ;
//	private ArrayList<VideoInfo> items;
	private LayoutInflater mInflater;	
	private ImageLoader imageLoader; 
	
	public GalleryImageAdapter(Context context,  ArrayList<BaiDuRecommend> arrayListIndexFlash) {
		super();
		this.arrayListIndexFlash = arrayListIndexFlash;
		this.mInflater = LayoutInflater.from(context); 
	    this.imageLoader = new ImageLoader(context.getApplicationContext(),R.drawable.bg_default);
	 
	}
	


	public int getCount() {
		if (this.arrayListIndexFlash != null && this.arrayListIndexFlash.size() > 0)
			return Integer.MAX_VALUE;
		return 0;
	}

	public Object getItem(int position) {
		try{
			int index = 0;
			if (arrayListIndexFlash != null && arrayListIndexFlash.size() > 0) {
				if (position < 0) {
					position = position + arrayListIndexFlash.size();
				}
				index = position % arrayListIndexFlash.size();
				return arrayListIndexFlash.get(index);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		try{
			int index = position % this.arrayListIndexFlash.size();
			BaiDuRecommend voide = (BaiDuRecommend) getItem(index);
			convertView = mInflater.inflate(R.layout.item_feature, null);
			final ImageView i = (ImageView) convertView
					.findViewById(R.id.imageView1);
			final ProgressBar mProgressLoading = (ProgressBar) convertView
					.findViewById(R.id.progressLoading);
			
			//add by xj
			i.setScaleType(ImageView.ScaleType.FIT_XY);
//			i.setScaleType(ImageView.ScaleType.MATRIX);
			convertView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT)); 
			imageLoader.DisplayImage(voide.getImg_url(), i, mProgressLoading,640*832);
			
			return convertView;
		}catch (Exception e) {

			e.printStackTrace();
		}
		return convertView;
		
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	 
}