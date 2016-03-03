package cn.hi321.android.media.entity;

import java.io.Serializable;

public class BaiDuChannelVideo implements Serializable {
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBase_url() {
		return base_url;
	}
	public void setBase_url(String base_url) {
		this.base_url = base_url;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public int getMask() {
		return mask;
	}
	public void setMask(int mask) {
		this.mask = mask;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	private String name;
    private String tag;
    private String type;
    private String base_url;
    private String filter;
    private int mask;
    private String extra;
//    public String getHotUrl() {
//		return hotUrl;
//	}
//	public void setHotUrl(String hotUrl) {
//		this.hotUrl = hotUrl;
//	}
//	private String hotUrl;
}
