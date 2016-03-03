package cn.hi321.android.media.entity;

import java.util.ArrayList;

public class BaiDuNav {

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<BaiDuItems> getBaiduItems() {
		return baiduItems;
	}
	public void setBaiduItems(ArrayList<BaiDuItems> baiduItems) {
		this.baiduItems = baiduItems;
	}
	private int num;
	private String type;
	private ArrayList<BaiDuItems> baiduItems ;//= new ArrayList<BaiDuItems>();
}
