package cn.hi321.android.media.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class BaiDuMediaInfo implements Serializable{
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getTotal_num() {
		return total_num;
	}
	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}
	public ArrayList<Sites> getSitesArray() {
		return sitesArray;
	}
	public void setSitesArray(ArrayList<Sites> sitesArray) {
		this.sitesArray = sitesArray;
	}
	public ArrayList<String> getYearsArr() {
		return yearsArr;
	}
	public void setYearsArr(ArrayList<String> yearsArr) {
		this.yearsArr = yearsArr;
	}
	public ArrayList<MediaItem> getVideosArray() {
		return videosArray;
	}
	public void setVideosArray(ArrayList<MediaItem> videosArray) {
		this.videosArray = videosArray;
	}
	private String id;
	private String site;
	private int total_num;
	private ArrayList<Sites> sitesArray ;
	private ArrayList<String> yearsArr;
	private ArrayList<MediaItem> videosArray ;
}
