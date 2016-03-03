package cn.hi321.android.media.entity;

import java.io.Serializable;

public class Sites implements Serializable{
    public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public String getSite_url() {
		return site_url;
	}
	public void setSite_url(String site_url) {
		this.site_url = site_url;
	}
	public int getSite_no() {
		return site_no;
	}
	public void setSite_no(int site_no) {
		this.site_no = site_no;
	}
	public String getSite_logo() {
		return site_logo;
	}
	public void setSite_logo(String site_logo) {
		this.site_logo = site_logo;
	}
	public int getMax_episode() {
		return max_episode;
	}
	public void setMax_episode(int max_episode) {
		this.max_episode = max_episode;
	}
	private String site_name;
    private String site_url;
    private int site_no;
    private String site_logo;
    private int max_episode;
}
