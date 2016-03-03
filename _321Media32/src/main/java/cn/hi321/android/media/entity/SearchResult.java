package cn.hi321.android.media.entity;

import java.util.ArrayList;

public class SearchResult {

//	public String getSrcShortUrl() {
//		return srcShortUrl;
//	}
//	public void setSrcShortUrl(String srcShortUrl) {
//		this.srcShortUrl = srcShortUrl;
//	}
//	public String getTitleUrl() {
//		return titleUrl;
//	}
//	public void setTitleUrl(String titleUrl) {
//		this.titleUrl = titleUrl;
//	}
//	public String getImgUrl() {
//		return imgUrl;
//	}
//	public void setImgUrl(String imgUrl) {
//		this.imgUrl = imgUrl;
//	}
//	public String getVideoUrl() {
//		return videoUrl;
//	}
//	public void setVideoUrl(String videoUrl) {
//		this.videoUrl = videoUrl;
//	}
//	public String getDuration() {
//		return duration;
//	}
//	public void setDuration(String duration) {
//		this.duration = duration;
//	}
//	public String getRestitle() {
//		return restitle;
//	}
//	public void setRestitle(String restitle) {
//		this.restitle = restitle;
//	}
//	public String getSite_logo() {
//		return site_logo;
//	}
//	public void setSite_logo(String site_logo) {
//		this.site_logo = site_logo;
//	}
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public ArrayList<String> getHas() {
		return has;
	}
	public void setHas(ArrayList<String> has) {
		this.has = has;
	}
	public String getCover_url() {
		return cover_url;
	}
	public void setCover_url(String cover_url) {
		this.cover_url = cover_url;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getLast_seq() {
		return last_seq;
	}
	public void setLast_seq(String last_seq) {
		this.last_seq = last_seq;
	}
	public String getMax_site() {
		return max_site;
	}
	public void setMax_site(String max_site) {
		this.max_site = max_site;
	}
	public ArrayList<String> getArea_name() {
		return area_name;
	}
	public void setArea_name(ArrayList<String> area_name) {
		this.area_name = area_name;
	}
	private String  title ;
	private String year;
	private String desc ;
	private String total ;
	private ArrayList<String> has;
	
	private String cover_url ;
	private String type_name ;
	private String last_seq;
	private String max_site;
	private ArrayList<String> area_name;
}
