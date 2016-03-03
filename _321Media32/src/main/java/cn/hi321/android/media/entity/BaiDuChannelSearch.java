package cn.hi321.android.media.entity;

import java.util.ArrayList;

public class BaiDuChannelSearch {
 
	private ArrayList<Conds> condsArr;
	public ArrayList<Conds> getCondsArr() {
		return condsArr;
	}
	public void setCondsArr(ArrayList<Conds> condsArr) {
		this.condsArr = condsArr;
	}
	public ArrayList<Orders> getOrdersArra() {
		return ordersArra;
	}
	public void setOrdersArra(ArrayList<Orders> ordersArra) {
		this.ordersArra = ordersArra;
	}
	private ArrayList<Orders> ordersArra;
 }
