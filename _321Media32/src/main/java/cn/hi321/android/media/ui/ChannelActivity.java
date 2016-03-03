package cn.hi321.android.media.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import cn.hi321.android.media.entity.BaiDuChannelVideo;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.utils.ActivityHolder;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.Utils;
import cn.hi321.android.media.widget.MainTitlebar;
import cn.waps.AppConnect;

import com.android.china.R;
/**
 * @author 杨光福老师
 * 频道页面
 * www.itlanbao.com
 * IT蓝豹创始人
 * 阿福老师在2012年业余时间写的播放器，当时还荣获最佳应用奖
 * 微博http://t.qq.com/yangguangfu
 */
public class ChannelActivity extends BaseActivity {
	int screenWidth;
	private int size;
	int imageArray[] = { R.drawable.channel_gridview_ranking,
			R.drawable.channel_gridview_tv, R.drawable.channel_gridview_movie,
			R.drawable.channel_gridview_comic,
			R.drawable.channle_gridview_variety,
			R.drawable.channel_gridview_life,
			R.drawable.channel_griditem_funny,
			R.drawable.channel_gridview_music,
			R.drawable.channel_gridview_sport,
			R.drawable.channel_gridview_trailer,
			R.drawable.channle_gridview_hot/*, R.drawable.channle_gridview_ent,
			R.drawable.channel_gridview_radiate*/ };

	private GridView localGridView1;

	@Override
	protected void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.view_main_tab_channel);
		ActivityHolder.getInstance().addActivity(this);
		((MainTitlebar) findViewById(R.id.main_title)).show("频道");
		localGridView1 = (GridView) findViewById(R.id.view_main_tab_channle_grid);

		ColorDrawable localColorDrawable = new ColorDrawable(
				R.color.common_orange_color);
		localGridView1.setSelector(localColorDrawable);
		ItemAdapter adapter = new ItemAdapter();
		localGridView1.setAdapter(adapter);
		GridItemOnClick onIten = new GridItemOnClick();
		localGridView1.setOnItemClickListener(onIten);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	class GridItemOnClick implements AdapterView.OnItemClickListener {
		private GridItemOnClick() {
		}

		public void onItemClick(AdapterView<?> paramAdapterView,
				View paramView, int paramInt, long paramLong) {

			if (UIUtils.hasNetwork(ChannelActivity.this)) {
				Intent i = null;// new Intent(ChannelActivity.this,
								// ChannelListActivity.class);
				switch (paramInt) {
				case 1:// 电影
					i = new Intent(ChannelActivity.this,
							ChannelListActivity.class);
					BaiDuChannelVideo infoTv = new BaiDuChannelVideo();
					infoTv.setBase_url("http://app.video.baidu.com/adnativetvplay/");
					infoTv.setExtra("");
					infoTv.setFilter("http://app.video.baidu.com/conds/?worktype=adnativetvplay");
					infoTv.setMask(3);
					infoTv.setName("电视剧");
					infoTv.setTag("tvplay");
					infoTv.setType("channel_video");
					i.putExtra("channelVideoInfo", infoTv);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);

					break;
				case 2:// 电视
					i = new Intent(ChannelActivity.this,
							ChannelListActivity.class);
					BaiDuChannelVideo infoMovie = new BaiDuChannelVideo();
					infoMovie
							.setBase_url("http://app.video.baidu.com/adnativemovie/");
					infoMovie.setExtra("");
					infoMovie
							.setFilter("http://app.video.baidu.com/conds/?worktype=adnativemovie");
					infoMovie.setMask(3);
					infoMovie.setName("电影");
					infoMovie.setTag("movie");
					infoMovie.setType("channel_video");
					i.putExtra("channelVideoInfo", infoMovie);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 3:// 动漫
					i = new Intent(ChannelActivity.this,
							ChannelListActivity.class);
					BaiDuChannelVideo infoComic = new BaiDuChannelVideo();
					infoComic
							.setBase_url("http://app.video.baidu.com/adnativecomic/");
					infoComic.setExtra("");
					infoComic
							.setFilter("http://app.video.baidu.com/conds/?worktype=adnativecomic");
					infoComic.setMask(3);
					infoComic.setName("动漫");
					infoComic.setTag("comic");
					infoComic.setType("channel_video");
					i.putExtra("channelVideoInfo", infoComic);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 4:// 搞笑
					i = new Intent(ChannelActivity.this,
							ChannelListActivity.class);
					BaiDuChannelVideo infoZy = new BaiDuChannelVideo();
					infoZy.setBase_url("http://app.video.baidu.com/adnativetvshow/");
					infoZy.setExtra("");
					infoZy.setFilter("http://app.video.baidu.com/conds/?worktype=adnativetvshow");
					infoZy.setMask(3);
					infoZy.setName("综艺");
					infoZy.setTag("tvshow");
					infoZy.setType("channel_video");
					i.putExtra("channelVideoInfo", infoZy);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 5:// 新闻
					i = new Intent(ChannelActivity.this,
							VideoListActivity.class);
					BaiDuChannelVideo infoNew = new BaiDuChannelVideo();
					infoNew.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/info/");
					// infoNew.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/info/hot/1.js");
					infoNew.setExtra("");
					infoNew.setFilter("");
					infoNew.setMask(2);
					infoNew.setName("新闻");
					infoNew.setTag("info");
					infoNew.setType("short_video");
					i.putExtra("channelVideoInfo", infoNew);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 6:// 综艺
					i = new Intent(ChannelActivity.this,
							VideoListActivity.class);
					BaiDuChannelVideo infoFunny = new BaiDuChannelVideo();
					infoFunny
							.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/amuse/");
					// infoFunny.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/amuse/");//hot/1.js
					infoFunny.setExtra("");
					infoFunny.setFilter("");
					infoFunny.setMask(2);
					infoFunny.setName("搞笑");
					infoFunny.setTag("amuse");
					infoFunny.setType("short_video");
					i.putExtra("channelVideoInfo", infoFunny);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);

					break;
				case 7:// 音乐
					i = new Intent(ChannelActivity.this,
							VideoListActivity.class);
					BaiDuChannelVideo infoMusic = new BaiDuChannelVideo();
					infoMusic
							.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/music/");
					// infoMusic.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/music/hot/1.js");
					infoMusic.setExtra("");
					infoMusic.setFilter("");
					infoMusic.setMask(2);
					infoMusic.setName("音乐");
					infoMusic.setTag("music");
					infoMusic.setType("short_video");
					i.putExtra("channelVideoInfo", infoMusic);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 8:// 体育
					i = new Intent(ChannelActivity.this,
							VideoListActivity.class);
					BaiDuChannelVideo infoSport = new BaiDuChannelVideo();
					infoSport
							.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/sport/");
					// infoSport.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/sport/hot/1.js");
					infoSport.setExtra("");
					infoSport.setFilter("");
					infoSport.setMask(2);
					infoSport.setName("体育");
					infoSport.setTag("sport");
					infoSport.setType("short_video");
					i.putExtra("channelVideoInfo", infoSport);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 9:// 花片 这里是 美女
					i = new Intent(ChannelActivity.this,
							VideoListActivity.class);
					BaiDuChannelVideo infowWoman = new BaiDuChannelVideo();
					infowWoman
							.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/woman/");
					// infowWoman.setHotUrl("http://m.baidu.com/video?static=utf8_data/android_channel/json/woman/hot/1.js");
					infowWoman.setExtra("");
					infowWoman.setFilter("");
					infowWoman.setMask(2);
					infowWoman.setName("美女");
					infowWoman.setTag("woman");
					infowWoman.setType("short_video");
					i.putExtra("channelVideoInfo", infowWoman);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 11:// 娱乐 这里放福利
					i = new Intent(ChannelActivity.this, TVActivity.class);
					startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					//
					break;
				case 12:
					i = new Intent(ChannelActivity.this, RadiaActivity.class);
					startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 10:
					i = new Intent(ChannelActivity.this,
							VideoListActivity.class);
					BaiDuChannelVideo hot = new BaiDuChannelVideo();
					hot.setBase_url("http://m.baidu.com/video?static=utf8_data/android_channel/json/boshidun/");
					hot.setExtra("");
					hot.setFilter("");
					hot.setMask(2);
					hot.setName("热点");
					hot.setTag("boshidun");
					hot.setType("channel_short");
					i.putExtra("channelVideoInfo", hot);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);
					break;
				case 0:
					i = new Intent(ChannelActivity.this,
							RankingListActivity.class);
					ChannelActivity.this.startActivity(i);
					overridePendingTransition(R.anim.fade, R.anim.hold);

					break;

				default:
					break;
				}

			} else {
				UIUtils.showToast(ChannelActivity.this, ChannelActivity.this
						.getText(R.string.tip_network).toString());

			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityHolder.getInstance().removeActivity(this);
	}

	public class ItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ItemAdapter() {
			this.mInflater = LayoutInflater.from(ChannelActivity.this);
		}

		public int getCount() {
			return imageArray.length;
		}

		public Object getItem(int paramInt) {
			return imageArray[paramInt];
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			View convertView = mInflater.inflate(
					R.layout.channel_gridview_item, null);
			ImageView imageview = (ImageView) convertView
					.findViewById(R.id.imageView1);
			imageview.setBackgroundResource(imageArray[paramInt]);

			return convertView;
		}
	}

	int isExit = 0;

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {

		if ((paramKeyEvent.getAction() == 0) && (paramInt == 4)) {

			if (isExit == 0) {
				isExit++;
				UIUtils.showToast(this, "再点一次可退出");
				return true;
			}
			if (isExit == 1) {
				// 以下方法将用于释放SDK占用的系统资源
				AppConnect.getInstance(this).finalize();
				this.finish();
			}
		}
		return super.onKeyDown(paramInt, paramKeyEvent);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isExit = 0;

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (this.isFinishing()) {
			this.finish();
		}
	}

}
