package cn.hi321.android.media.http;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.entity.ChannelInfo;
import cn.hi321.android.media.entity.ChannelOtherVideos;
//import cn.hi321.android.media.entity.HomeResponse;
import cn.hi321.android.media.entity.BaiduResolution;
import cn.hi321.android.media.utils.UIUtils;



/**
 * @author yanggf
 * 
 */
public class NetWorkTask extends AsyncTask<Object, Integer, Object> {
	private static final String TAG = "NetWorkTask";

	private Context mContext;
	private int mTag;
	private IBindData mBindData;
	private static NetWorkTask nWorkTask;
//	private ProgressDialog dialog;
//	Handler fxHandler;
	
	public NetWorkTask() {
		nWorkTask = this;
	}

	
	@Override
	protected Object doInBackground(Object... params) {

		if (params[0] instanceof IBindData) {
			mBindData = (IBindData) params[0];
		}

		if (params[0] instanceof Context) {
			mContext = (Context) params[0];
		}

		if (mContext == null) {
			return null;
		} 
		
		final  DataMode mode = new  DataMode(mContext); 
		mTag = (Integer) params[1]; 
		switch (mTag) { 
		
			case UIUtils.BaiDuInfoFlag:{//获取主界面信息
				String url = (String)params[2];
				ArrayList<BaiDuRecommend> arrRankList = (ArrayList<BaiDuRecommend>)params[3];
				return  mode.requestRankList(url,arrRankList);
			} 
			case UIUtils.BaiDuRecommend:{//获取推荐界面数据
				String url = (String)params[2];
				return  mode.getRecommendData(url);
			} 
		 
			case UIUtils.GEG_MEDIA_PLAY_URL:{
				String url = (String)params[2];
				return mode.getMedia(url);
			}
			case UIUtils.Channel_Video_Info:{//频道界面数据
				String url = (String)params[2];
				ChannelInfo channelInfo =(ChannelInfo)params[3];
				return mode.getChannelInfo(url,channelInfo);
			}
			case UIUtils.Channel_Search:{//检索接口请求
				String url = (String)params[2];
				return mode.getChannelSearch(url);
			}
			case UIUtils.Channel_Search_Video:{
				String url = (String)params[2];
				ChannelInfo channelInfo =(ChannelInfo)params[3];
				return mode.getChannelInfo(url,channelInfo);
			}
			case UIUtils.Channel_ScrollStateChanged:{
				String url = (String)params[2];
				ChannelInfo channelInfo =(ChannelInfo)params[3];
				return mode.getChannelInfo(url,channelInfo);
			}
			case UIUtils.Channel_Video_View:{
				String url = (String)params[2];
				ArrayList<ChannelOtherVideos> channelInfo =(ArrayList<ChannelOtherVideos>)params[3];
				String tag = (String)params[4];
				return mode.getChannelOtherView(url,channelInfo,tag);
			}
			case UIUtils.Channel_Video_View_hot:{
				String url = (String)params[2];
				ArrayList<ChannelOtherVideos> channelInfo =(ArrayList<ChannelOtherVideos>)params[3];
				String tag = (String)params[4];
				return mode.getChannelOtherView(url,channelInfo,tag);
			}
			case UIUtils.Research_Activity:{//搜索界面
				String url = (String)params[2];
				return mode.getSearchData(url);
			}
			case UIUtils.BaoFeng_Detail:{
				String url = (String)params[2];
				return mode.getSearchResultXiangQing(url);
			}
			case UIUtils.BaoFeng_Play:{
				String url = (String)params[2];
				return mode.getPlayUrl(url);
			}
			
//			case UIUtils.Get_Slices:{
//				String url = (String)params[2];
//				return  mode.getRecommendData(url);
//			}
			
			
			
			
			
			
		
//			case UIUtils.GET_USER_DATA: {//获取列表数据
//				String videoUrl = (String)params[2];
//				HomeResponse homeResMovie = (HomeResponse)params[3];
////				fxHandler = (Handler)params[4];
////				fxHandler.sendEmptyMessage(UIUtils.StrDialog);
//				return  mode.getMediaData(videoUrl, homeResMovie,null);
//			}
//			case UIUtils.SHOW_WINDOWPOP:{//检索
//				String url = (String)params[2];
////				fxHandler = (Handler)params[3];
//				return mode.getSearchInfoData(url, null);
//			} 
			case UIUtils.GEG_MEDIA_DATA:{
				String url = (String)params[2];
//				fxHandler = (Handler)params[3];
				return mode.getVideoData(url,null);
			}
//			case UIUtils.RefleshView:{
//				String videoUrl = (String)params[2];
//				HomeResponse homeResMovie = (HomeResponse)params[3];
////				fxHandler = (Handler)params[4];
////				fxHandler.sendEmptyMessage(UIUtils.StrDialog);
//				return  mode.getMediaData(videoUrl, homeResMovie,null);
//			}
			case UIUtils.GET_PLAY_DATA:{
				String videoUrl = (String)params[2];
				BaiduResolution playDatas = (BaiduResolution)params[3];
//				fxHandler = (Handler)params[4];
//				fxHandler.sendEmptyMessage(UIUtils.StrDialog);
				return  mode.getMediaItem(videoUrl, playDatas,null);
			}
//			
		
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {

		if (mContext instanceof IBindData) { 
			if (mBindData != null) {
				mBindData.bindData(mTag, result);
			}
		} else {
			if (result instanceof Boolean) {
				mBindData.bindData(mTag, result); 
			} else {
				Log.i(TAG,"NetWorkTask --> onPostExecute --> Nothing");
			}
		} 
//		if(fxHandler !=null){
//			fxHandler.sendEmptyMessage(UIUtils.StopDialog);
//		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled(); 
	}

	/** 
	 */
	public static void cancelTask() { 
		if (nWorkTask != null) {
			if (!nWorkTask.isCancelled()) {
				nWorkTask.cancel(true);
			}
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}
