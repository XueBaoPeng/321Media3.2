package cn.hi321.android.media.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ChannelInfo implements Serializable{

	public int getVideo_num() {
		return video_num;
	}
	public void setVideo_num(int video_num) {
		this.video_num = video_num;
	}
	public int getBeg() {
		return beg;
	}
	public void setBeg(int beg) {
		this.beg = beg;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public ArrayList<BaiDuRecommend> getVideosArr() {
		if(videosArr ==null){
			videosArr = new ArrayList<BaiDuRecommend>();
		}
		return videosArr;
	}
	public void setVideosArr(ArrayList<BaiDuRecommend> videosArr) {
		this.videosArr = videosArr;
	}
	
	 
	private int video_num;
	private int beg;
	private int end;
	private ArrayList<BaiDuRecommend> videosArr;;
	
	private ArrayList<CurrentConds> curCondsArr;

	public ArrayList<CurrentConds> getCurCondsArr() {
		return curCondsArr;
	}
	public void setCurCondsArr(ArrayList<CurrentConds> curCondsArr) {
		this.curCondsArr = curCondsArr;
	}
}
