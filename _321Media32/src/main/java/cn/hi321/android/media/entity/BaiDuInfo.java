package cn.hi321.android.media.entity;

import java.util.HashMap;

public class BaiDuInfo {
    public HashMap<String, BaiDuNav> getMap() {
		return map;
	}

	public void setMap(HashMap<String, BaiDuNav> map) {
		this.map = map;
	}

	private HashMap<String, BaiDuNav> map = new HashMap<String, BaiDuNav>() ;
}
