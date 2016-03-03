package cn.hi321.android.media.http;

 
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Gallery;
import cn.hi321.android.media.entity.BaiDuChannelSearch;
import cn.hi321.android.media.entity.BaiDuMediaInfo;
import cn.hi321.android.media.entity.BaiDuRecommend;
import cn.hi321.android.media.entity.BaoFengPlayUrl;
import cn.hi321.android.media.entity.ChannelInfo;
import cn.hi321.android.media.entity.ChannelOtherVideos;
import cn.hi321.android.media.entity.Conds;
import cn.hi321.android.media.entity.CurrentConds;
import cn.hi321.android.media.entity.BaiduResolution;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.entity.Orders;
import cn.hi321.android.media.entity.SearchData;
import cn.hi321.android.media.entity.SearchResult;
import cn.hi321.android.media.entity.Sites;
import cn.hi321.android.media.entity.ValuesSearch;
import cn.hi321.android.media.entity.Media; 
import cn.hi321.android.media.utils.UIUtils;

 
/**
 * 接口操作
 * 具体的联网方法功能写在该类
 * 1,请求电影数据的方法；
 * 2，请求升级的数据方法；
 * 3，请求数据上报的方法
 * 
 * @author yanggf
 * 
 */
public class DataMode {
	private static final String TAG = "DataMode";

	private HttpAgent httpAgent; 
	private Context mContext;

	public DataMode(Context context) {
		this.httpAgent = new HttpAgent(context); 
		mContext = context;
	} 
	
	
	public ArrayList<HashMap<String, ArrayList<BaiDuRecommend>>> getRecommendData(String url){ 
		final String[] responsesFirst =httpAgent.sendMessageByGet("", url, "sessionId",0,0); 
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserRecommendData(responsesFirst[1]);
		} else{
			final String[] responsesFirst1 =httpAgent.sendMessageByGet("", url, "sessionId",1,0);
			if (comparisonNetworkStatus(responsesFirst1)) {
	        	return parserRecommendData(responsesFirst1[1]);
			}
		}
		 return null;
		 
		 
	}
	
	public ArrayList<ChannelOtherVideos> getChannelOtherView(String url,ArrayList<ChannelOtherVideos> arrayList,String tag ){ 
		final String[] responsesFirst = httpAgent.sendMessageByGet("", url, "sessionId",0,0);
		
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserChannelView(responsesFirst[1],arrayList,tag);
    	}else{
    		final String[] responsesFirst1 = httpAgent.sendMessageByGet("", url, "sessionId",1,0);
			if (comparisonNetworkStatus(responsesFirst1)) {
	        	return parserChannelView(responsesFirst1[1],arrayList,tag);
			}
    	} 
		 return null;
	}
	
	private ArrayList<ChannelOtherVideos> parserChannelView(String js,ArrayList<ChannelOtherVideos> arr,String tag ){
	 
		try {
		 
			String key = null;
			if(js!=null){
				String s = js ;
				if(s.contains(":{")){
					int index = s.indexOf(":{");
				     key = s.substring(2, index-1);
				} 
			}
			if(key!=null){
				JSONObject json = new JSONObject(js); 
				JSONObject channel_info_object = null; ;
				channel_info_object = json.optJSONObject(key);//生活
				
				JSONArray videosArr = channel_info_object.optJSONArray("videos"); 
				for(int i=0;i<videosArr.length();i++){
					JSONObject jsonO =videosArr.optJSONObject(i);
					ChannelOtherVideos video = new ChannelOtherVideos();
					video.setDomain(jsonO.optString("domain"));
					video.setDuration(jsonO.optString("duration"));
					video.setImgh_url(jsonO.optString("imgh_url"));
					video.setImgv_url(jsonO.optString("imgv_url"));
					video.setIs_play(jsonO.optString("is_play"));
					video.setTitle(jsonO.optString("title"));
					video.setUrl(jsonO.optString("url"));
					
					video.setVideo_num(channel_info_object.optString("video_num"));
					video.setEnd(channel_info_object.optString("end"));
					video.setBeg(channel_info_object.optString("beg"));
					
					
					arr.add(video);
				}
			}
//			
//			JSONObject channel_info_object = null; ;
//			if(tag.equals("amuse") ){
//				channel_info_object = json.optJSONObject("channel_amuse");//生活
//			}else if(tag.equals("info") ){
//				channel_info_object = json.optJSONObject("channel_info");//channel_info生活
//			}else if(tag.equals("music")){
//				 channel_info_object = json.optJSONObject("channel_music");//channel_info生活
//			}else if(tag.equals("sport")){
//				 channel_info_object = json.optJSONObject("channel_sport");//channel_info生活
//			}else if(tag.equals("woman")){
//				 channel_info_object = json.optJSONObject("channel_woman");//channel_info生活
//			}else if(tag.equals("channel_boshidun")){
//				 channel_info_object = json.optJSONObject("channel_boshidun");//channel_boshidun生活
//			}	
				
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}
	
	public SearchResult getSearchResultXiangQing(String url){
		final String[] responsesFirst = httpAgent.sendMessageByGet("", url, "sessionId",0,0);
		
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserSearchResult(responsesFirst[1]);
    	} else{
    		final String[] responsesFirst1 = httpAgent.sendMessageByGet("", url, "sessionId",1,0);
    		
    		if (comparisonNetworkStatus(responsesFirst1)) {
            	return parserSearchResult(responsesFirst1[1]) ;
        	} 
    	}
		 return null;
	}
	private SearchResult parserSearchResult(String s){
		SearchResult res = new SearchResult();
		try {
			JSONObject jsonobject = new JSONObject(s);
			res.setId(jsonobject.optString("id"));
			res.setDesc(jsonobject.optString("desc"));

			if(jsonobject.has("has")){
				
				String hasJson = jsonobject.optString("has");
				if(hasJson!=null){
					JSONArray hasArr = new JSONArray(hasJson); 
					ArrayList<String> arr = new ArrayList<String>();
					if(hasArr!=null)
					for(int i=0;i<hasArr.length();i++){
						arr.add(hasArr.optString(i));
					}
					res.setHas(arr);
				}
			
			} 
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public BaoFengPlayUrl getPlayUrl(String url){
		final String[] responsesFirst = httpAgent.sendMessageByGet("", url, "sessionId",0,0);
		
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserPlayUrl(responsesFirst[1]);
    	} else{
    		final String[] responsesFirst1 = httpAgent.sendMessageByGet("", url, "sessionId",1,0);
    		
    		if (comparisonNetworkStatus(responsesFirst1)) {
            	return parserPlayUrl(responsesFirst1[1]) ;
        	} 
    	}
		 return null;
	}
	private BaoFengPlayUrl parserPlayUrl(String js){
		//HashMap<String, String> map  = new HashMap<String, String>();
		BaoFengPlayUrl playUrl = new BaoFengPlayUrl();
		 
		try {
			JSONArray jsarr = new JSONArray(js);
			for(int i=0;i<jsarr.length();i++){
				JSONObject json =  jsarr.optJSONObject(i); 
				playUrl.setCover_url(json.optString("cover_url"));
				playUrl.setId(json.optString("id"));
				playUrl.setPage_url(json.optString("page_url"));
				playUrl.setSeq(json.optString("seq"));
				playUrl.setSite(json.optString("site"));
				playUrl.setSubseq(json.optString("subseq"));
				playUrl.setTitle(json.optString("title"));
 
			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return playUrl;
	}
	
	public ArrayList<SearchData>  getSearchData(String url){
		final String[] responsesFirst = httpAgent.sendMessageByGet("", url, "sessionId",0,0);
		
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserSearch(responsesFirst[1]);
    	} else{
    		final String[] responsesFirst1 = httpAgent.sendMessageByGet("", url, "sessionId",1,0);
    		
    		if (comparisonNetworkStatus(responsesFirst1)) {
            	return parserSearch(responsesFirst1[1]) ;
        	} 
    	}
		 return null;
	}
	
	private ArrayList<SearchData>  parserSearch(String s){
		ArrayList<SearchData> resultArr = new ArrayList<SearchData>();
		try {
			JSONObject js = new JSONObject(s);
			JSONArray  resultArray = js.optJSONArray("result");
			for(int i=0;i<resultArray.length();i++){
				JSONObject resultObject = resultArray.optJSONObject(i);
				SearchData searchData = new SearchData(); 
				
				JSONArray actorsNameJsonArray = resultObject.optJSONArray("actors_name");
				ArrayList<String> actorsName = new ArrayList<String>();
				for(int j=0;j<actorsNameJsonArray.length();j++){
					actorsName.add(actorsNameJsonArray.optString(j));
				}
				searchData.setActors_name(actorsName);
				
				searchData.setArea_l(resultObject.optString("area_l"));
				searchData.setCover_url(resultObject.optString("cover_url"));
				
				JSONArray directorsNameJsonArray = resultObject.optJSONArray("directors_name");
				ArrayList<String> directorsName = new ArrayList<String>();
				for(int j=0;j<directorsNameJsonArray.length();j++){
					actorsName.add(directorsNameJsonArray.optString(j));
				}
			
				searchData.setDirectors_name(directorsName);
				
				searchData.setId(resultObject.optInt("id"));
				searchData.setLast_seq(resultObject.optString("last_seq"));
				searchData.setMax_site(resultObject.optString("max_site"));
				searchData.setScore(resultObject.optDouble("score"));
				searchData.setStyle_l(resultObject.optString("style_l"));
				searchData.setTitle(resultObject.optString("title"));
				searchData.setTotal(resultObject.optString("total"));
				searchData.setType_l(resultObject.optString("type_l"));
				searchData.setUpdate_time(resultObject.optString("update_time"));
				
				resultArr.add(searchData);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		return resultArr;
	}
	
	/**
	 * 频道界面检索数据
	 * */
	public BaiDuChannelSearch getChannelSearch(String url){ 
		final String[] responsesFirst = httpAgent.sendMessageByGet("", url, "sessionId",0,0);
		
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserChannelSearch(responsesFirst[1]);
    	} else{
    		final String[] responsesFirst1 = httpAgent.sendMessageByGet("", url, "sessionId",1,0);
    		
    		if (comparisonNetworkStatus(responsesFirst1)) {
            	return parserChannelSearch(responsesFirst1[1]);
        	} 
    	}
		 return null;
	}
	
	private BaiDuChannelSearch parserChannelSearch(String js){
		BaiDuChannelSearch channelSearch = new BaiDuChannelSearch();
		try {
			JSONObject json = new JSONObject(js);
			if(json!=null){
				JSONArray condsArr = json.optJSONArray("conds");
				ArrayList<Conds> arrConds = new ArrayList<Conds>();
				for(int i=0;i<condsArr.length();i++){
					JSONObject jsoConds = condsArr.optJSONObject(i);
					Conds conds = new Conds();
					conds.setField(jsoConds.optString("field"));
					conds.setName(jsoConds.optString("name"));
					JSONArray valuesArray = jsoConds.optJSONArray("values");
					ArrayList<ValuesSearch> arrValuesSearch = new ArrayList<ValuesSearch>();
					for(int j= 0;j<valuesArray.length();j++){
						JSONObject valuesObject = valuesArray.optJSONObject(j);
						
						if(j == 0){//表示到最后一个
							ValuesSearch serOne = new ValuesSearch();
							serOne.setTitle("全部");
							serOne.setTerm("");
							arrValuesSearch.add(serOne);
						}
						
						
						ValuesSearch ser = new ValuesSearch();
						ser.setTitle(valuesObject.optString("title"));
						ser.setTerm(valuesObject.optString("term"));
						arrValuesSearch.add(ser);
					
					}
					conds.setValuesArr(arrValuesSearch);
					arrConds.add(conds);
				}
				channelSearch.setCondsArr(arrConds);
				if(json.has("orders")){
					JSONArray  ordersArr = json.optJSONArray("orders");
					
					ArrayList<Orders> ordersList = new ArrayList<Orders>();
					for(int i=0;i<ordersArr.length();i++){
						JSONObject jsonOrder = ordersArr.optJSONObject(i); 
						Orders orders = new Orders(); 
						orders.setField(jsonOrder.optString("field"));
						orders.setName(jsonOrder.optString("name"));
						ordersList.add(orders);
					}
					channelSearch.setOrdersArra(ordersList);
				}
				
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return channelSearch;
	}
	
	/**
	 * 频道界面数据
	 * */
	public ChannelInfo getChannelInfo(String url,ChannelInfo channelInfo){ 
		final String[] responsesFirst =httpAgent.sendMessageByGet("", url, "sessionId",0,0);
		
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserChannelData(responsesFirst[1],channelInfo);
    	} else{
    		final String[] responsesFirst1 =httpAgent.sendMessageByGet("", url, "sessionId",1,0);
    		
    		if (comparisonNetworkStatus(responsesFirst1)) {
            	return parserChannelData(responsesFirst1[1],channelInfo);
        	} 
    	}
		 return null;
	}
	
	private ChannelInfo parserChannelData(String js,ChannelInfo info){
//		ChannelInfo info = channelInfo;
		try {
			JSONObject jsonObject = new JSONObject(js);
			if(jsonObject.has("video_list")){
				JSONObject json = jsonObject.optJSONObject("video_list");
				
				if(json!=null){
					info.setVideo_num(json.optInt("video_num"));
					info.setBeg(json.optInt("beg"));
					info.setEnd(json.getInt("end"));
					JSONArray jsonVideoArr = json.optJSONArray("videos");
					ArrayList<BaiDuRecommend> videosArr = info.getVideosArr() ;
					for(int i=0;i<jsonVideoArr.length();i++){
						BaiDuRecommend video = new BaiDuRecommend();
						JSONObject jsonVideo = jsonVideoArr.optJSONObject(i);
						video.setDuration(jsonVideo.optString("duration"));
						video.setImg_url(jsonVideo.optString("img_url"));
						video.setPlay_filter(jsonVideo.optString("play_filter"));
						video.setTitle(jsonVideo.optString("title"));
						video.setUpdate(jsonVideo.optString("update"));
						video.setUrl(jsonVideo.optString("url"));
						video.setWorks_id(jsonVideo.optString("works_id"));
						video.setWorks_type(jsonVideo.optString("works_type"));
						video.setRating(jsonVideo.optInt("rating"));
						videosArr.add(video);
					}
					info.setVideosArr(videosArr);
				}
				JSONArray curCondsJson = jsonObject.optJSONArray("cur_conds");
				if(curCondsJson!=null){
					ArrayList<CurrentConds> arr = new ArrayList<CurrentConds>();
					for(int i =0;i<curCondsJson.length();i++){
						JSONObject jsonO = curCondsJson.optJSONObject(i);
						CurrentConds currentC = new CurrentConds();
						currentC.setKey(jsonO.optString("key"));
						currentC.setValue(jsonO.optString("value"));
						arr.add(currentC);
					}
					info.setCurCondsArr(arr);
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
	
	public BaiDuMediaInfo getMedia(String url){ 
		final String[] responsesFirst =httpAgent.sendMessageByGet("", url, "sessionId",0,0);
		
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserBaiDuMediaInfo(responsesFirst[1]);
    	} else{
    		final String[] responsesFirst1 =httpAgent.sendMessageByGet("", url, "sessionId",1,0);
    		
    		if (comparisonNetworkStatus(responsesFirst1)) {
            	return parserBaiDuMediaInfo(responsesFirst1[1]);
        	} 
    	}
		 return null;
	}
	
	private BaiDuMediaInfo parserBaiDuMediaInfo(String json){
		BaiDuMediaInfo media = new BaiDuMediaInfo();
		
		try {
			JSONObject jsonObject = new JSONObject(json);
			media.setId(jsonObject.optString("id"));
			media.setSite(jsonObject.optString("site"));
			media.setTotal_num(jsonObject.optInt("total_num"));
			if(jsonObject.has("sites")){
				ArrayList<Sites> sitesArrayList = new ArrayList<Sites>();
				JSONArray sitesArr = jsonObject.optJSONArray("sites");
				for(int i =0;i<sitesArr.length();i++){
					Sites sites = new Sites(); 
					JSONObject jso = sitesArr.optJSONObject(i); 
						sites.setSite_name(jso.optString("site_name"));
						sites.setMax_episode(jso.optInt("max_episode"));
						sites.setSite_logo(jso.getString("site_logo"));
						sites.setSite_no(jso.optInt("site_no"));
						sites.setSite_url(jso.optString("site_url"));
						sitesArrayList.add(sites); 
				}
				media.setSitesArray(sitesArrayList);
			}
			
			if(jsonObject.has("videos")){
				ArrayList<MediaItem> videosArrayList = new ArrayList<MediaItem>();
				JSONArray videosArr = jsonObject.optJSONArray("videos");
				for(int i =0;i<videosArr.length();i++){
					MediaItem videos  = new MediaItem(); 
					JSONObject jso = videosArr.optJSONObject(i);
					videos.setTitle(jso.optString("title"));
					videos.setSourceUrl(jso.optString("url"));
					videos.setIs_play(jso.optString("is_play"));
					videos.setEpisode(jso.optString("episode"));
					videos.setImage(jso.optString("img_url"));
//					videos.setTvid(jso.optString("tvid"));
//					videos.setDownload(jso.optString("download"));
//					videos.setSec(jso.optInt("sec"));
//					videos.setDi(jso.optString("di"));
					videosArrayList.add(videos);
				}
				media.setVideosArray(videosArrayList);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return media;
	}
	
	public ArrayList<BaiDuRecommend> requestRankList(String url, ArrayList<BaiDuRecommend> arrRankList){
		final String[] responsesFirst =httpAgent.sendMessageByGet("", url, "sessionId",0,0);
		
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserRankList(responsesFirst[1],  arrRankList);
    	} else{
    		final String[] responsesFirst1 =httpAgent.sendMessageByGet("", url, "sessionId",1,0);
    		
    		if (comparisonNetworkStatus(responsesFirst1)) {
            	return parserRankList(responsesFirst1[1], arrRankList);
        	} 
    	}
		 return null;
	}
	
	private ArrayList<BaiDuRecommend>  parserRankList(String js, ArrayList<BaiDuRecommend> arrRankList){
		JSONArray jsonArr; 
		try {
			jsonArr = new JSONArray(js);  
			if(jsonArr!=null){  
				for(int i=0;i<jsonArr.length();i++){
					JSONObject jsonObject = jsonArr.optJSONObject(i); 
					BaiDuRecommend baiduRe = new BaiDuRecommend(); 
					baiduRe.setTitle(jsonObject.optString("title"));
					baiduRe.setImg_url(jsonObject.optString("imgv_url"));
					baiduRe.setRating(jsonObject.optInt("rating"));
					baiduRe.setTerminal_type(jsonObject.optInt("terminal_type")); 
					baiduRe.setWorks_id(jsonObject.optString("works_id"));
					baiduRe.setWorks_type(jsonObject.optString("works_type")); 
					baiduRe.setActor(jsonObject.optString("actor"));
					baiduRe.setUpdate(jsonObject.optString("update"));
					baiduRe.setType(jsonObject.optString("type"));
					baiduRe.setStatus_day(jsonObject.optString("status_day"));
					arrRankList.add(baiduRe);
				}
			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return arrRankList;
	}
	
	private ArrayList<HashMap<String, ArrayList<BaiDuRecommend>>> parserRecommendData(String json){
		ArrayList<HashMap<String, ArrayList<BaiDuRecommend>>> arrayList = new ArrayList<HashMap<String,ArrayList<BaiDuRecommend>>>();
		try {
			
		 
			JSONObject jsonobS = new JSONObject(json);
			JSONArray jsonArr = jsonobS.optJSONArray("slices");
			for(int m=0;m<jsonArr.length();m++){
				JSONObject jsonobject = jsonArr.optJSONObject(m);
				String tag = jsonobject.optString("tag");
				if(tag.equals("index_flash")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					JSONArray json_index_flash = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_index_flash);
					map.put("index_flash", arr);
					arrayList.add(map);
				}else if(tag.equals("movie")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					JSONArray json_movie_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_movie_hot);
					map.put("movie_hot", arr);
					arrayList.add(map);
				}else if(tag.equals("tvplay")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					
					JSONArray json_tvplay_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_tvplay_hot);
					map.put("tvplay_hot", arr);
					arrayList.add(map);
				}else if(tag.equals("tvshow")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					
					JSONArray json_tvshow_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_tvshow_hot);
					map.put("tvshow_hot", arr);
					arrayList.add(map);
				}else if(tag.equals("comic")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					JSONArray json_comic_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_comic_hot);
					map.put("comic_hot", arr);
					arrayList.add(map);
				}else if(tag.equals("woman")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					JSONArray json_comic_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_comic_hot);
					map.put("woman", arr);
					arrayList.add(map);
				}else if(tag.equals("music")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					JSONArray json_comic_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_comic_hot);
					map.put("music", arr);
					arrayList.add(map);
				}else if(tag.equals("amuse")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					JSONArray json_comic_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_comic_hot);
					map.put("amuse", arr);
					arrayList.add(map);
				}else if(tag.equals("sport")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					JSONArray json_comic_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_comic_hot);
					map.put("sport", arr);
					arrayList.add(map);
				}else if(tag.equals("info")){
					HashMap<String, ArrayList<BaiDuRecommend>> map = new HashMap<String, ArrayList<BaiDuRecommend>>();
					JSONArray json_comic_hot = jsonobject.optJSONArray("hot");
					ArrayList<BaiDuRecommend> arr = parserRecommend(json_comic_hot);
					map.put("info", arr);
					arrayList.add(map);
				}
				
			}
		   
			 
			 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	private ArrayList<BaiDuRecommend>  parserRecommend(JSONArray jo){
		ArrayList<BaiDuRecommend> arrRecom = new ArrayList<BaiDuRecommend>();
		for(int i=0;i<jo.length();i++){
			JSONObject jsonObject = jo.optJSONObject(i); 
			BaiDuRecommend baiduRe = new BaiDuRecommend(); 
			baiduRe.setTitle(jsonObject.optString("title"));
			baiduRe.setImg_url(jsonObject.optString("img_url"));
			baiduRe.setRating(jsonObject.optInt("rating"));
			baiduRe.setTerminal_type(jsonObject.optInt("terminal_type"));
			baiduRe.setUpdate(jsonObject.optString("update"));
			baiduRe.setUrl(jsonObject.optString("url"));
			baiduRe.setWorks_id(jsonObject.optString("works_id"));
			baiduRe.setWorks_type(jsonObject.optString("works_type"));
			baiduRe.setDuration(jsonObject.optString("duration")); 
			baiduRe.setTag(jsonObject.optString("tag"));
			arrRecom.add(baiduRe);
		}
		return arrRecom;
	}
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	
//	
//	/**
//	 * 获得媒体的数据
//	 */
//	public HomeResponse getMediaData(String request, HomeResponse homeRe,Handler handler  ) {
//		final String[] responsesFirst = httpAgent.sendMessageByGet("", request, "sessionId",0,0);
//		System.out.println("json=========="+responsesFirst[1]);
//		if (comparisonNetworkStatus(responsesFirst)) {
//        	return parserHomeResponse(responsesFirst[1], homeRe);
//		}else{
//			final String[] responses = httpAgent.sendMessageByGet("", request, "sessionId",1,0);
//			if (comparisonNetworkStatus(responses)) {
//	        	return parserHomeResponse(responses[1], homeRe);
//			}
//		
//			return null;
//		}
//	}
//	
//	/**
//	 * 获得媒体的数据
//	 */
	public BaiduResolution getMediaItem(String request, BaiduResolution homeRe,Handler handler  ) {
		final String[] responsesFirst = httpAgent.sendMessageByGet("", request, "sessionId",0,0);
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserMediaItem(responsesFirst[1], homeRe);
		}else{
			final String[] responses = httpAgent.sendMessageByGet("", request, "sessionId",1,0);
			if (comparisonNetworkStatus(responses)) {
	        	return parserMediaItem(responses[1], homeRe);
			}
		}
		return homeRe;
	}
//	
//
	public BaiduResolution parserMediaItem(String js  ,BaiduResolution homeRes){
		
		 try { 
			JSONObject jsonOb = new JSONObject(js);
			if(jsonOb !=null){
				String logo = jsonOb.optString("logo");
				homeRes.setLogo(logo);
				
				String STRUCT_PAGE_TYPE = jsonOb.optString("STRUCT_PAGE_TYPE");
				homeRes.setSTRUCT_PAGE_TYPE(STRUCT_PAGE_TYPE);
				
				String video_source_url = jsonOb.optString("video_source_url");
				homeRes.setVideo_source_url(video_source_url);
				
				String video_source_type =jsonOb.optString("video_source_type");
				homeRes.setVideo_source_type(video_source_type);
				
				String video_trans_url =jsonOb.optString("video_trans_url");
				homeRes.setVideo_trans_url(video_trans_url);
				
				
				String content =jsonOb.optString("content");
				homeRes.setContent(content);
				
				
				String src_url_processed =jsonOb.optString("src_url_processed");
				homeRes.setSrc_url_processed(src_url_processed);
				
				String page_title =jsonOb.optString("page_title");
				homeRes.setPage_title(page_title);
				
				String src_url =jsonOb.optString("src_url");
				homeRes.setSrc_url(src_url);
				
				 return homeRes;
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return homeRes;
	  }
//	public HomeResponse getMediaDataPost(String request, HomeResponse homeRe,Handler handler  ) {
//		final String[] responsesFirst = httpAgent.sendMessageByPost("", request, "",0);
//		System.out.println(responsesFirst[1]);
//		if (comparisonNetworkStatus(responsesFirst)) {
//        	return parserHomeResponse(responsesFirst[1], homeRe);
//		}else{
//			final String[] responses = httpAgent.sendMessageByPost("", request, "",0);
//			if (comparisonNetworkStatus(responses)) {
//	        	return parserHomeResponse(responses[1], homeRe);
//			}
//		
//			return null;
//		}
//	}
//	
//	
//	public HomeResponse parserHomeResponse(String js  ,HomeResponse homeRes){
//		
//		 try { 
//			JSONObject jsonOb = new JSONObject(js);
//			if(jsonOb !=null && jsonOb.has("result")){
//				JSONArray jsonArray = jsonOb.getJSONArray("result");
//				 ArrayList<VideoInfo> result = new ArrayList<VideoInfo>();
//				for(int i= 0;i < jsonArray.length();i++ ){
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					VideoInfo video = new VideoInfo();
//					if(jsonObject.has("craw"))
//					video.setCraw(jsonObject.getString("craw"));
//					if(jsonObject.has("average"))
//					video.setAverage(jsonObject.getInt("average"));
//					if(jsonObject.has("episode"))
//					video.setEpisode(jsonObject.getString("episode"));
//					if(jsonObject.has("vthumburl"))
//					video.setVthumburl(jsonObject.getString("vthumburl"));
//					if(jsonObject.has("programname"))
//					video.setProgramname(jsonObject.getString("programname"));
//					if(jsonObject.has("shortdesc"))
//					video.setShortdesc(jsonObject.getString("shortdesc"));
//					if(jsonObject.has("id"))
//					video.setId(jsonObject.getInt("id"));
//					result.add(video);
//				}
//				homeRes.setResult(result);
//			}
//			if(jsonOb !=null && jsonOb.has("page")){
//				JSONObject jsonPage = jsonOb.getJSONObject("page");
//				HashMap<String, Integer> page = new HashMap<String, Integer>();
//				if(jsonPage.has("totalcount"))
//				page.put("totalcount", jsonPage.getInt("totalcount"));
//				if(jsonPage.has("pageindex"))
//				page.put("pageindex", jsonPage.getInt("pageindex"));
//				if(jsonPage.has("pagesize"))
//				page.put("pagesize", jsonPage.getInt("pagesize"));
//				if(jsonPage.has("pagecount"))
//				page.put("pagecount", jsonPage.getInt("pagecount"));
//				homeRes.setPage(page);
//			}
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  
//		  return homeRes;
//	  }
//	
//
//	/**
//	 * 获得媒体的数据
//	 */
	public Media getVideoData(String request ,Handler handler) {
		final String[] responsesFirst = httpAgent.sendMessageByGet("", request, "sessionId",0,0);
		if (comparisonNetworkStatus(responsesFirst)) {
        	return parserVideoData(responsesFirst[1]);
		}else{
			final String[] responses = httpAgent.sendMessageByGet("", request, "sessionId",1,0);
			if (comparisonNetworkStatus(responses)) {
	        	return parserVideoData(responses[1]);
			}
		
			return null;
		}
	}
//	
	private Media parserVideoData(String jso){
		Media videoData = new Media();
		try {
			JSONObject jsonOb = new JSONObject(jso); 
			videoData.setId(jsonOb.optString("id"));
			videoData.setTitle(jsonOb.optString("title"));
			videoData.setImg_url(jsonOb.optString("img_url"));
			videoData.setIntro(jsonOb.optString("intro"));
			videoData.setIs_finish(jsonOb.optString("is_finish"));
			videoData.setPubtime(jsonOb.optString("pubtime"));
			videoData.setCur_episode(jsonOb.optInt("cur_episode"));
			videoData.setMax_episode(jsonOb.optString("max_episode"));
			
			if(jsonOb.has("director")){
				JSONArray jsonArray = jsonOb.optJSONArray("director");
				ArrayList<String> arr = new ArrayList<String>();
				for(int i= 0;i< jsonArray.length();i++){ 
					arr.add(jsonArray.optString(i));
				}
				videoData.setDirectorArr(arr);
			}
			
			if(jsonOb.has("actor")){
				JSONArray jsonArray = jsonOb.optJSONArray("actor");
				ArrayList<String> arr = new ArrayList<String>();
				for(int i= 0;i< jsonArray.length();i++){ 
					arr.add(jsonArray.optString(i));
				}
				videoData.setActorArr(arr);
			}
			
			
			if(jsonOb.has("area")){
				JSONArray jsonArray = jsonOb.optJSONArray("area");
				ArrayList<String> arr = new ArrayList<String>();
				for(int i= 0;i< jsonArray.length();i++){ 
					arr.add(jsonArray.optString(i));
				}
				videoData.setAreaArr(arr);
			}
			videoData.setSeason_num(jsonOb.optInt("season_num"));
			
			if(jsonOb.has("type")){
				JSONArray jsonArray = jsonOb.optJSONArray("type");
				ArrayList<String> arr = new ArrayList<String>();
				for(int i= 0;i< jsonArray.length();i++){ 
					arr.add(jsonArray.optString(i));
				}
				videoData.setTypeArr(arr);
			}
			videoData.setRating(jsonOb.optString("rating"));
			videoData.setPlay_filter(jsonOb.optString("play_filter"));
			videoData.setForeign_ip(jsonOb.optString("foreign_ip"));
			if(jsonOb.has("sites")){
				JSONArray sitesJsonArr = jsonOb.optJSONArray("sites");
				ArrayList<Sites> sitesArr = new ArrayList<Sites>();
				for(int i =0;i<sitesJsonArr.length();i++){
					Sites sites = new Sites();
					JSONObject jo = sitesJsonArr.optJSONObject(i);
//					sites.setMax_episode(jo.optInt("max_episode"));
					sites.setSite_logo(jo.optString("site_logo"));
					sites.setSite_name(jo.optString("site_name"));
//					sites.setSite_no(site_no)
					sites.setSite_url(jo.optString("site_url"));
					sitesArr.add(sites);
				}
				videoData.setSitesArr(sitesArr);
			}
			 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return videoData;
	}
//	
//	
////	/**
////	 * 获得媒体的数据
////	 */
//	public  ArrayList<SearchInfo> getSearchInfoData(String request,Handler handler  ) {
//		final String[] responsesFirst = httpAgent.sendMessageByGet("", request, "sessionId",0,0);
//		if (comparisonNetworkStatus(responsesFirst)) {
//			return getSearchInfoParser(responsesFirst[1]);
//		}else{
//			final String[] responses = httpAgent.sendMessageByGet("", request, "sessionId",1,0);
//			if (comparisonNetworkStatus(responses)) {
//	        	return getSearchInfoParser(responses[1]);
//			}
//		
//			return null;
//		}
//	}
//	private  ArrayList<SearchInfo> getSearchInfoParser(String json){
//		ArrayList<SearchInfo> searchInfo = new ArrayList<SearchInfo>();
//		try {
//			JSONObject jsonobject = new JSONObject(json);
//			if(jsonobject !=null){
//				if(jsonobject.has("filter")){
//					JSONArray filterJsonArr = jsonobject.getJSONArray("filter");
//					if(filterJsonArr!=null){
//						for(int i=0;i<filterJsonArr.length();i++){
//							JSONObject filterObject = filterJsonArr.getJSONObject(i);
//							
//							SearchInfo info = new SearchInfo();
//							if(filterObject.has("sort"))
//							info.setSort(filterObject.getString("sort"));
//							if(filterObject.has("title"))
//							info.setTitle(filterObject.getString("title")); 
//							ArrayList<Values> arr = new ArrayList<Values>();
//							if(filterObject.has("values")){ 
//								JSONArray valuesArray = filterObject.getJSONArray("values");
//								for(int j = 0;j <valuesArray.length();j++){
//									JSONObject json_value = valuesArray.getJSONObject(j);
//									
//									Values value = new Values();
//									if(json_value.has("key"))
//									value.setKey(json_value.getString("key"));
//									if(json_value.has("title"))
//									value.setTitle(json_value.getString("title"));
//									arr.add(value);
//								}
//							}
//							info.setArrayListValues(arr);
//							searchInfo.add(info);
//						}
//					}
//				}
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return searchInfo;
//	}
//	
//	 
//	 
//	 
	public void showErrorMessage(Handler handler, String errMessage) { 
		final Bundle bundle = new Bundle();
		bundle.putString(UIUtils.KEY_ERROR_MESSAGE, errMessage);
		
//		final Message msg = new Message();
//		msg.what =UIUtils.HANDLER_SHOW_ERRORMESSAGE;
//		msg.setData(bundle);
//		handler.sendMessage(msg); 
		
	}
//	
//
//	 
//	
//	
	/**
	 * 判断返回状态吗是否是200
	 * 
	 * @author yangguangfu
	 * @param response
	 * @return
	 */
	private boolean comparisonNetworkStatus(String[] responses ) {
		final String code = responses[0];
		Log.i(TAG,"CODE_HTTP " + code);
		if (UIUtils.CODE_HTTP_SUCCEED.equals(code) /*&& responses[1].indexOf(RequestUtils.RESPONSEDATA) > -1*/) {
			return true;
		} else if (UIUtils.CODE_SESSION_EXPIRED.equals(code)) {
			Log.i(TAG,"会话过期，重新请求session"); 
			return true;
		} else if (UIUtils.CODE_HTTP_FAIL.equals(code) 
				|| UIUtils.CODE_STOP_SERVER.equals(code) 
				|| UIUtils.SERVER_NOT_RESPONDING.equals(code)
				|| UIUtils.CODE_HTTP_RESTART_CLIENT.equals(code)
				|| UIUtils.CODE_PAGE_NOT_FOUND.equals(code)) { 
			return false;
		}
		return false;
	}
	
	 


}
