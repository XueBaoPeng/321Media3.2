package cn.hi321.android.media.history;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.hi321.android.media.entity.Media;
import cn.hi321.android.media.entity.PlayHistoryInfo;
import cn.hi321.android.media.utils.FormatUtil.TimeFormat;
import cn.hi321.android.media.utils.LogUtil;
import cn.hi321.android.media.utils.Utils;

import com.android.china.R;

public class PlayHistoryAdapter extends BaseAdapter implements OnClickListener{
	
	private final static String TAG = "PlayHistoryAdapter";
	private Context mContext;
	private List<PlayHistoryInfo> mPlayHistoryLists;
	private LayoutInflater mInflater;
	private Media mMedia;
	private Handler mHandler ;
	private int deviceWidthPixels;
	private final static int SMALL_SCREEN_MAX_EMS = 10;
	private final static int BIG_SCREEN_MAX_EMS = 12;
	private final static int LARGER_SCREEN_MAX_EMS = 14;

	public PlayHistoryAdapter(Context ctx, List<PlayHistoryInfo> phList,Media media,Handler handler) {
		this.mContext = ctx;
		this.mPlayHistoryLists = phList;
		this.mInflater = LayoutInflater.from(mContext);
		this.mMedia = media;
		this.mHandler = handler;
		this.deviceWidthPixels =  Utils.getWidthPixels(mContext);
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mPlayHistoryLists && mPlayHistoryLists.size() > 0) {
			count = mPlayHistoryLists.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		if (mPlayHistoryLists != null) {
			return mPlayHistoryLists.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.new_playhistory_list_item, null);
			holder = new ViewHolder();
			holder.mMediaNameTv = (TextView) convertView.findViewById(R.id.lblVideoName);
			holder.mMediaWatchTimeTv = (TextView) convertView.findViewById(R.id.lblHistoryDate);
			holder.mMediaIconIv = (ImageView) convertView.findViewById(R.id.btnDelete);
//			convertView.setBackgroundResource(R.drawable.playhistory_item_selector);
			matchVideoNameMaxEms(holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mPlayHistoryLists != null && mPlayHistoryLists.size() > 0) {
			PlayHistoryInfo	playHistoryInfo = mPlayHistoryLists.get(position);
			if(null != playHistoryInfo){
				setPlayTime(playHistoryInfo, holder);
				holder.mMediaIconIv.setOnClickListener(this);
				holder.mMediaIconIv.setTag(playHistoryInfo);
			}
		}
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		PlayHistoryInfo	playHistoryInfo = (PlayHistoryInfo)v.getTag();
		switch (v.getId()) {
		case R.id.btnDelete:
			String mid = playHistoryInfo.getMid();
			LogUtil.e(TAG, "mid ="+ mid);
//			if(!StringUtil.isEmpty(mid)){
//				mHandler.sendEmptyMessage(Utils.HANDLER_LOGO_HTTP_FAILED);
//				String url = DataRequestUrl.GET_MEDIA_DATA_URL + mid;
//				LogUtil.v(TAG, "url = " + url);
//				new NetWorkTask().execute(mContext,NetworkUtil.GET_MEDIA_BY_SERVER,mHandler, mMedia, url);
//			}
			break;

		default:
			break;
		}
		
	}

	// Set the playback history of each item's title and last played time
	private void setPlayTime(PlayHistoryInfo playHistoryinf, ViewHolder holder) {
		holder.mMediaNameTv.setText(playHistoryinf.getMedianame());
		Long time = System.currentTimeMillis() - playHistoryinf.getPlayedtime();
		if (time < TimeFormat.DAY) {
			holder.mMediaWatchTimeTv.setText(TimeFormat.relative(mContext,time));
		} else {
			holder.mMediaWatchTimeTv.setText(TimeFormat.absolute(playHistoryinf.getPlayedtime()));
		}
	}

	private void matchVideoNameMaxEms(ViewHolder holder) {
//		if(deviceWidthPixels <= Utils.DEVICE_WIDTH_640X960) {
//			holder.mMediaNameTv.setMaxEms(SMALL_SCREEN_MAX_EMS);
//		} else if(deviceWidthPixels > Utils.DEVICE_WIDTH_640X960 && deviceWidthPixels <= Utils.DEVICE_WIDTH_720X1280){
//			holder.mMediaNameTv.setMaxEms(BIG_SCREEN_MAX_EMS);
//		} else if(deviceWidthPixels > Utils.DEVICE_WIDTH_720X1280 &&  deviceWidthPixels <= Utils.DEVICE_WIDTH_800X1280 ){
//			holder.mMediaNameTv.setMaxEms(LARGER_SCREEN_MAX_EMS);
//		}else{
//			holder.mMediaNameTv.setMaxEms(LARGER_SCREEN_MAX_EMS);
//		}
	}

	
	private class ViewHolder {
		TextView mMediaNameTv;
		TextView mMediaWatchTimeTv;
		ImageView mMediaIconIv;
	}

}
