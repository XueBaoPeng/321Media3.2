package cn.hi321.android.media.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchData implements Serializable{

	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<String> getDirectors_name() {
		return directors_name;
	}
	public void setDirectors_name(ArrayList<String> directors_name) {
		this.directors_name = directors_name;
	}
	public ArrayList<String> getActors_name() {
		return actors_name;
	}
	public void setActors_name(ArrayList<String> actors_name) {
		this.actors_name = actors_name;
	}
	public String getCover_url() {
		return cover_url;
	}
	public void setCover_url(String cover_url) {
		this.cover_url = cover_url;
	}
	public String getMax_site() {
		return max_site;
	}
	public void setMax_site(String max_site) {
		this.max_site = max_site;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getArea_l() {
		return area_l;
	}
	public void setArea_l(String area_l) {
		this.area_l = area_l;
	}
	public String getType_l() {
		return type_l;
	}
	public void setType_l(String type_l) {
		this.type_l = type_l;
	}
	public String getStyle_l() {
		return style_l;
	}
	public void setStyle_l(String style_l) {
		this.style_l = style_l;
	}
	public String getLast_seq() {
		return last_seq;
	}
	public void setLast_seq(String last_seq) {
		this.last_seq = last_seq;
	}
	private String total;
	private String update_time;
	private String title;
	private ArrayList<String> directors_name;
	private ArrayList<String> actors_name;
	private String cover_url;
	private String max_site;//视频来源
	private double score;
	private String area_l;
	private String type_l;
	private String style_l;
	private String last_seq;//最后一集
	
	
//	
//	public String getQueryWord() {
//		return queryWord;
//	}
//	public void setQueryWord(String queryWord) {
//		this.queryWord = queryWord;
//	}
//	public String getDispnum() {
//		return dispnum;
//	}
//	public void setDispnum(String dispnum) {
//		this.dispnum = dispnum;
//	}
//	public String getTime() {
//		return time;
//	}
//	public void setTime(String time) {
//		this.time = time;
//	}
//	public ArrayList<SearchResult> getSearResultArr() {
//		return searResultArr;
//	}
//	public void setSearResultArr(ArrayList<SearchResult> searResultArr) {
//		this.searResultArr = searResultArr;
//	}
//	private String queryWord;
//	private String dispnum ;
//	private String time;
//	private ArrayList<SearchResult> searResultArr;
}
