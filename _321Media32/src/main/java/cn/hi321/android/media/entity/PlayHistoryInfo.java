package cn.hi321.android.media.entity;

import java.io.Serializable;

/**
 * @author yanggf
 */
public class PlayHistoryInfo implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	
	private String mid;
	private String hashid;
	
	private String taskname;
	
	private String fsp;
	
	//add by yangguangfu
	//直接播放地址
	private String purl;
	
	//容灾播放地址
	private String mpurl;
	
	//下载地址
	private String durl;

	private String mediatype;
	
	private String medianame;
	
	private String playedtimeString;
	
	private long playedtime;
	
	private long position;
	
	private int movie_position;
	
	private long movie_playedtime;
	
	private String size;
	
	private String percent;
	
	
	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getPercent() {
		return percent;
	}


	public void setPercent(String percent) {
		this.percent = percent;
	}


	public PlayHistoryInfo(){
			
	}
	
	
	
	public PlayHistoryInfo(String mid,String mediatype,  String medianame, String hashid,String taskname,
			String fsp, String playedtimeString,
			Long playedtime, Long position,int movie_position,long movie_playedtime,String size,String percent,String purl) {
		
		this.mid = mid;
		this.hashid = hashid;
		this.taskname = taskname;
		this.fsp = fsp;
		this.mediatype= mediatype;
		this.medianame = medianame;
		this.playedtimeString = playedtimeString;
		this.playedtime = playedtime;
		this.position = position;
		this.movie_position=movie_position;
		this.movie_playedtime=movie_playedtime;
		this.size = size;
		this.percent = percent;
		this.purl = purl;
	}
	

	public PlayHistoryInfo(String hashid,String mediatype, String medianame, String mid,String taskname,
			String fsp, String playedtimeString,
			Long playedtime, Long position) {
		
		this.mid = mid;
		this.hashid = hashid;
		this.taskname = taskname;
		this.fsp = fsp;
		this.mediatype= mediatype;
		this.medianame = medianame;
		this.playedtimeString = playedtimeString;
		this.playedtime = playedtime;
		this.position = position;
	}


	public String getMid() {
		return mid;
	}


	public void setMid(String mid) {
		this.mid = mid;
	}


	public String getHashid() {
		return hashid;
	}


	public void setHashid(String hashid) {
		this.hashid = hashid;
	}


	public String getTaskname() {
		return taskname;
	}


	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}


	public String getFsp() {
		return fsp;
	}


	public void setFsp(String fsp) {
		this.fsp = fsp;
	}


	public String getMediatype() {
		return mediatype;
	}


	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}


	public String getMedianame() {
		return medianame;
	}


	public void setMedianame(String medianame) {
		this.medianame = medianame;
	}


	public String getLanguage() {
		return playedtimeString;
	}


	public void setLanguage(String playedtimeString) {
		this.playedtimeString = playedtimeString;
	}


	public long getPlayedtime() {
		return playedtime;
	}


	public void setPlayedtime(long playedtime) {
		this.playedtime = playedtime;
	}


	public long getPosition() {
		return position;
	}


	public void setPosition(long position) {
		this.position = position;
	}


	public int getMovie_position() {
		return movie_position;
	}


	public void setMovie_position(int movie_position) {
		this.movie_position = movie_position;
	}


	public long getMovie_playedtime() {
		return movie_playedtime;
	}


	public void setMovie_playedtime(long movie_playedtime) {
		this.movie_playedtime = movie_playedtime;
	}




	public String getPurl() {
		return purl;
	}


	public void setPurl(String purl) {
		this.purl = purl;
	}


	public String getMpurl() {
		return mpurl;
	}


	public void setMpurl(String mpurl) {
		this.mpurl = mpurl;
	}


	public String getDurl() {
		return durl;
	}


	public void setDurl(String durl) {
		this.durl = durl;
	}


	public String getPlayedtimeString() {
		return playedtimeString;
	}


	public void setPlayedtimeString(String playedtimeString) {
		this.playedtimeString = playedtimeString;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "PlayHistoryInfo [mid=" + mid + ", hashid=" + hashid
				+ ", taskname=" + taskname + ", fsp=" + fsp + ", purl=" + purl
				+ ", mpurl=" + mpurl + ", durl=" + durl + ", mediatype="
				+ mediatype + ", medianame=" + medianame
				+ ", playedtimeString=" + playedtimeString + ", playedtime="
				+ playedtime + ", position=" + position + ", movie_position="
				+ movie_position + ", movie_playedtime=" + movie_playedtime
				+ "]";
	}

	

	
	
	
	
	
}

