package cn.hi321.android.media.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.android.media.entity.Media;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.http.IPageList;
import cn.hi321.android.media.player.SystemPlayer;

import com.android.china.R;

/**
 * 
 * @author yanggf
 * 
 */
public class Utils {

	private static final String TAG = "Utils";

	// private static String APP_VERSION = "1.5.1.3";

	public static final String UNKNOWN_VERSION = "versionUnknown";
	
	private static Context mContext = null;

	private static String mAppVersion = "versionUnknown";
	
	public static final String UNKNOWN_MAC_ADDRESS = "00:00:00:00:00:00";

	private static String mMacAddress = "00:00:00:00:00:00";
	
	private static String MAK_KEK = "mac_key";
	private static String MAK_VALUE = "mac_value";
	

	public static final String FS_CACHE = "fscache";

	public static final String synchronizeds = "synchronized";

	public static final String CLIENT = "aphone";

	public static final String PLAY_TYPE = "playtype";

	public static final String LIVEFLAG = "liveflag";

	public static final String DEMANDFSPS = "demandata";

	public static int NUM = 0;// 表示精选页面图片更新的数量

	public static boolean isLogined = false;

	// public static boolean isExistMainActivity = false; modify donggx
	// isExistMainActivity()

	public static int onclick_error = 0;

	public static boolean isPlayerCrashSystem = false;

	public static boolean isPlayerCrashVLC = false;

	/**
	 * 是否已经上报过首次缓冲失败的先关信息 add by zhangshuo
	 */
	public static boolean isUploadBfSuccess = false;

	/**
	 * 是否弹出请求不到筛选的Dialog
	 */
	public static boolean isTipFilter = false;

	/**
	 * 是否取到了频道页的数据
	 */
	public static boolean isGetData = false;

	/**
	 * 当前网络状态是否为WIFI
	 */
	public static boolean isWIFISTATE = false;
	/**
	 * 当前网络状态是否为2G�G
	 */
	public static boolean is2Gor3G = false;

	/** Network error */
	public static final String CODE_HTTP_FAIL = "-1";

	public static final String LIVEPOSITION = "liveposition";

	/** No problem of errCode */
	public static final String CODE_ERROR_RIGHT = "0";

	/** Networking success */
	public static final String CODE_HTTP_SUCCEED = "200";

	public static final String CODE_HTTPS_RECONNECT = "002";

	/** Session expires */
	public static final String CODE_SESSION_EXPIRED = "2000";

	/** Server to stop the service */
	public static final String CODE_STOP_SERVER = "5000";

	/** Server did not respond */
	public static final String SERVER_NOT_RESPONDING = "10000";

	/** Did not find interface */
	public static final String CODE_PAGE_NOT_FOUND = "404";

	public static final String CODE_CONNECTION_EXCEPTION = "502";

	/** Restart the client */
	public static final String CODE_HTTP_RESTART_CLIENT = "4000";

	public static final String NET_WORK_INVAILABLE = "netInvailable";

	private static boolean isFirstShow = true;

	private static String HTTPCODE;

	private static String channelUpLoad;

	public static String MSURL = "";

	private static String viewUploadStr;

	private static ArrayList<IPageList> mVideoItems;
	
	public static final int UNKNOWN_DATA = 0;

	public static ArrayList<IPageList> getmVideoItems() {
		return mVideoItems;
	}

	public static void setmVideoItems(ArrayList<IPageList> mVideoItems) {
		Utils.mVideoItems = mVideoItems;
	}

	public static String getHTTPCODE() {
		return HTTPCODE;
	}

	public static void setHTTPCODE(String hTTPCODE) {
		HTTPCODE = hTTPCODE;
	}

	/**
	 * 插入电影数据的SQL
	 */
	public final static String MOVIEINSERSQL = "insert into moviefilterhistory ("
			+ "hashid,_inserttime,"
			+ "filter_cate_key,filter_cate_title,filter_region_key,filter_region_title,"
			+ "filter_rdate_key,filter_rdate_title,filter_karma_key,filter_karma_title,"
			+ "filter_clarity_key,filter_clarity_title,filter_udate_key,filter_udate_title,"
			+ "filter_hotrank_key,filter_hotrank_title )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 插入电视剧数据的SQL
	 */
	public final static String TVINSERTSQL = "insert into tvfilterhistory ("
			+ "hashid,_inserttime,"
			+ "filter_cate_key ,filter_cate_title ,filter_region_key ,filter_region_title ,"
			+ "filter_rdate_key ,filter_rdate_title ,filter_karma_key ,filter_karma_title, "
			+ "filter_udate_key ,filter_udate_title ,"
			+ "filter_hotrank_key ,filter_hotrank_title )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 插入卡通的SQL
	 */
	public final static String CARTOONINSERTSQL = "insert into cartoonfilterhistory ("
			+ "hashid,_inserttime,"
			+ "filter_cate_key ,filter_cate_title ,filter_region_key ,filter_region_title ,"
			+ "filter_rdate_key ,filter_rdate_title ,filter_karma_key ,filter_karma_title, "
			+ "filter_udate_key ,filter_udate_title ,"
			+ "filter_hotrank_key ,filter_hotrank_title ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 插入综艺数据的SQL
	 */
	public final static String VARIETYINSERSQL = "insert into varietyfilterhistory ("
			+ "hashid,_inserttime,"
			+ "filter_cate_key ,filter_cate_title ,filter_region_key ,filter_region_title ,"
			+ "filter_rdate_key ,filter_rdate_title ,filter_karma_key ,filter_karma_title, "
			+ "filter_udate_key ,filter_udate_title ,"
			+ "filter_hotrank_key ,filter_hotrank_title ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static int SRCEEN_WIDTH = 480;

	public static int SRCEEN_HIGHT = 800;

	public static final String TRUE = "true";

	public static final String FALSE = "false";

	public static final String NULL = "null";

	public static String currentLanguage = "gylang";

	public static String historyLanguage = "";

	public static Dialog mMediaDialog;

	public static int CurrentPosInMediaIdList;

	private static boolean isPlayer = true;

	private static String[] yuYanSort = null;

	

	private static Builder customBuilder = null;

	public final static String MEDIA_KEY = "media_key";

	public final static String MEDIA_ITEM = "media_item";

	public final static String DOWNLOAD_KEY = "download_key";

	public final static String LIVE_DATA = "livedata";

	public final static String VIDEONAME = "videoname";

	public final static String IS_ENTERTAINMENT = "entertainment";

	/**
	 * 娱乐页容灾相关的数据
	 */
	public final static String ENTERTAINMEN_DRDATA = "entertainment_drdata";

	/**
	 * Filter
	 */
	public final static String FILTER_KEY = "filter_key";

	public final static String MEDIA_CHANNEL_KEY = "media_channel_key";

	public final static String MEDIA_OPERATION = "MEDIA_OPERATION";

	public final static String PLAY_HISTORY_KEY = "play_history_info_key";

	public final static String PLAY_LOADING_KEY = "play_loading_key";
	public final static String BY_PLAY_HISTORY_KEY = "by_play_histoty_key";

	/** Save the file path:sdcard/funshion/ */

	public final static String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	


	public final static int TAG_LOGINCONFIGURATION = 19;

	/** Start the upgrade request path identification **/

	public final static int START_APP_TAG_LOGINCONFIGURATION = 39;

	/** Cache directory of the picture */
	public static final String CACHE_IMG_DIR_PATH = "/imgfiles/";

	public static final String NET_CMWAP = "cmwap";

	public static final String NET_WAP_3G = "3gwap";

	public static final String NET_UNIWAP = "uniwap";

	public static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");
	public static String CTWAP = "ctwap";
	public static String CTNET = "ctnet";
	public static final int TYPE_NET_WORK_DISABLED = 0;// 网络不可用
	public static final int TYPE_MNO_CM = 1;// 移动
	public static final int TYPE_MNO_CU = 2;// 联通
	public static final int TYPE_MNO_CT = 3;// 电信
	public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
	public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
	public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
	// 设置当前联网类型
	public static int CURRECT_NET_WORK_TYPE = TYPE_OTHER_NET;

	public static final int YES_FAVORITE_HOTEL = 1;
	public static final int NO_FAVORITE_HOTEL = 2;
	public static final int TYPE_PLACE = 2;
	public static final int TYPE_HOTEL = 1;

	public static final int MOBILE_STATE = 1;
	public static final int WIFI_STATE = 2;
	public static int CURRENT_STATE = WIFI_STATE;
	/**
	 * 用户的详细筛选条件（测试用）
	 */
	private static String userFilterInfo;

	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";

	public static final byte HANDLER_SESSION_EXPIRED = 1;

	public static final byte HANDLER_DISMISS_PROGRESSDIALOG = HANDLER_SESSION_EXPIRED + 1;

	public static final byte HANDLER_LOGO_HTTP_FAILED = HANDLER_DISMISS_PROGRESSDIALOG + 1;

	public static final byte HANDLER_SHOW_ERRORMESSAGE = HANDLER_LOGO_HTTP_FAILED + 1;

	public static final byte HANDLER_SHOW_ERRORMESSAGE_GOTO = HANDLER_SHOW_ERRORMESSAGE + 1;

	public static final byte HANDLER_SHOW_START_ERROR_MESSAGE = HANDLER_SHOW_ERRORMESSAGE_GOTO + 1;

	/** Delete cache files */
	public static final byte DELETE_CACHE_FILE = HANDLER_SHOW_START_ERROR_MESSAGE + 1;

	public static final byte HANDLER_SHOW_ERROR_DATA_TOAST = DELETE_CACHE_FILE + 1;

	/** errorMessage */
	public static final String KEY_ERROR_MESSAGE = "errorMessage";

	// ----------------------Database parameters
	// ---------------------------------
	/** Database name */
	public static final String DB_NAME = "funshion.db";
	/** Database version */
	public static final int DB_VERSION = 4;
	/** Play records create table SQL statement */
	public static final String DB_PLAY_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS item (id integer primary key autoincrement, name varchar(20), url varchar(20),numalbum INTEGER,look INTEGER)";

	/** Used to identify the media layer */
	public final static int GET_MEDIA_BY_SERVER = 23;

	// ------------------Selection of interface-related parameters
	// ------------------------

	public final static int IMAGE_SIZE_COMPRESS = 200 * 1024;

	public static int BITMAP_DENSITY = 160;

	public static int POSITION = 1000;

	public static int CURRENT_SYSTEM_DENSITY = 160;

	public final static int REM_GET_ACTIVITY_GALLERYFLOW_BY_SERVER = 20;

	public final static int REM_GET_ACTIVITY_RECOMMEND_LIST_BY_SERVER = 21;

	// ------------------Selection of interface-related parameters
	// -----------------------

	/** Used to identify the film list data */
	public final static int MOVIE_GET_CONTENT_DATA = 22;

	/**
	 * add by lushengbin Used to identify the film left of the page Index page
	 * button data
	 */
	public final static int GET_MAIN_INDEX_ITEM_DATA = 24;

	// add by lushengbin
	// ------------------The parameters of the search
	// interface-----------------------

	/** Used to identify the network request is data of the search results */
	public final static int GET_SEARCH_CONTENT_DATA = 25;

	/** Used to identify the network requests the playback data */
	public final static int GET_PLAY_LIST_DATA = 26;

	/**
	 * Used to identify the network request data prompted by the hot search word
	 */
	public final static int GET_SEARCH_KEY_DATA = 27;

	// ------------------The parameters of public areas-----------------------

	/** Used to identify the reported */
	public final static int REPORTED_MESSAGE_TAG = 28;

	// ------------------Help page reported parameters-----------------------
	/**
	 * Used to identify at this time deal with the data reported by the feedback
	 * page
	 */
	public final static int FEEDBACK_UPLOAD_INFOMATION = 29;

	/**
	 * The data used to identify the network to request the history of play
	 * results
	 */
	public final static int GET_PLAY_HISTORY_LIST_DATA = 30;

	/** Used to identify crash information reported */
	public final static int REPORTED_ERROR_MESSAGE_TAG = 31;

	/** Used to identify the request media */
	public final static int GET_MEDIA_HISTORY_BY_SERVER = 32;

	/** Identify the player out of the number reported to play cards, etc. */
	public final static int REPORTED_PLAY_TIME_TAG = 33;

	/** Used to identify the player for the first time reported to the buffer */
	public final static int FRIST_BUFFERRE_PORTED_MESSAGE_TAG = 34;

	/** Used to identify the player to drag the buffer reported */
	public final static int DRAG_BUFFERRE_PORTED_MESSAGE_TAG = 35;

	/** Used to identify the player to play the kinds of cards reported */
	public final static int STUCK_BUFFERRE_PORTED_MESSAGE_TAG = 36;

	/** Used to identify the player can not play the file reported */
	public final static int ERROR_BUFFERRE_PORTED_MESSAGE_TAG = 37;

	/** Used to identify the program to start the reported */
	public final static int APP_START_REPORTED_MESSAGE_TAG = 38;

	/** Used to identify the program to start the download */
	public final static int GET_DOWNLOAD_DATA = 40;

	public final static int GET_DOWNLOAD_DATA_OFFLINE = 50;

	public final static int GET_RANK_LIST_DATA = 41;

	public final static int GET_DR_DATA_ERROR_DATA = 42;

	/** Used to report the pages of featured data */
	public final static int FEATURED_BROWSINGDATA_REPORT_MESSAGE_TAG = 43;

	/**
	 * Used to identify the sitllpicture data add by donggx
	 */
	public final static int GET_MEDIA_PICTURES_DATA = 44;

	/** 媒体精彩评论 */
	public final static int GET_MEDIA_COMMENT_LIST_DATA = 45;

	/**
	 * 获取热词提示
	 */
	public final static int GET_HOT_WORD_DATA = 46;

	/**
	 * 获取节目详情数据
	 */
	public final static int GET_PROGRAM_DETAIL_BY_SERVER = 55;

	/**
	 * 上报崩溃日志
	 */
	public final static int REPORT_CRASH_INFO = 47;

	/**
	 * 读取精选缓存
	 */
	public final static int CACHE_DATA_FEATURED_READ = 48;

	/**
	 * 写入精选缓存数据
	 */
	public final static int CACHE_DATA_FEATURED_WRITE = 49;

	public final static int UP_LOAD_REPORT = 51;

	public final static int Get_LOG_CONTROL_DATA = 52;

	/**
	 * Used to identify the data of program watch tag add by donggx
	 */
	public final static int GET_PROGRAM_WATCH_FOCUS_TAG_DATA = 53;

	/**
	 * Used to identify the data of program watch add by donggx
	 */
	public final static int GET_PROGRAM_WATCH_FOCUS_DATA = 54;

	/*
	 * live related keywords
	 */
	public final static int GET_LIVE_TV_DATA = 56;

	public final static int GET_LIVE_BROADCASTS_DATA = 57;

	/**
	 * 获取节目页数据的标志
	 */
	public final static int GET_PROGRAM_PAGE_DATA = 58;

	/**
	 * Get user comment list
	 */
	public final static int GET_USER_COMMENT_DATA = 59;

	public final static int GET_MAIN_BUSSINISS_DATA = 60;

	public final static int GET_HOTAPP_PAGE_DATA = 61;

	public final static int GET_PUSH_NOTIFICATION_DATA = 62;


	public final static int CLEAR_HISTORY = 15;
	public final static int DOWNLOAD = 16;
	public final static int MOVIE_TAG = 17;
	public final static int TV_TAG = 18;
	public final static int ANIMATION_TAG = 19;
	public final static int ARTS_TAG = 20;
	public final static int DAILY = 21;
	public final static int WEEKLY = 22;
	public final static int TOTAL_RANK = 23;
	public final static int LIVE_SHOW = 24;
	public final static int HOT_APP = 25;

	// 192.168.1.44 jsonfe.funshion.com

	/**
	 * Flag to judge which interrupt
	 */
	public final static int PLAY_END = 0;
	public final static int PLAY_USER_EXIT = 1;
	public final static int PLAY_BACKGOURND = 2;
	public final static int PLAY_EPISODE_SWITCH = 3;
	public final static int PLAY_OTHER = 10;

	
	//判断是正常登陆还是其他登陆，默认正常登陆
	public static boolean isNormalLogin = true;

	public static String getChannelUpLoadStr() {
		return channelUpLoad;
	}

	public static void setChannelUpLoadStr(String channelUpLoad) {
		Utils.channelUpLoad = channelUpLoad;
	}


	/** Get current system time */
	public static long getcurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	/** Get current system time */
	public static String getSystemTime() {
		try {
			Date sysDate = new Date(System.currentTimeMillis());
			int sysYear = sysDate.getYear();
			int sysMonth = sysDate.getMonth();
			int sysDay = sysDate.getDate();
			int sysHour = sysDate.getHours();
			int sysMinute = sysDate.getMinutes();
			LogUtil.e("The current system time" + sysYear + "-" + sysMonth
					+ "-" + sysDay + " " + sysHour + ":" + sysMinute);
			return sysYear + "-" + sysMonth + "-" + sysDay + " " + sysHour
					+ ":" + sysMinute;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Check the SD card if there
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		try {
			// final String status = Environment.getExternalStorageState();
			// if(isExternalSDCardExist()){
			// return true;
			// }
			// if (status.equals(Environment.MEDIA_MOUNTED)) {
			// FINAL_SAVE_MEDIA_PATH = SAVE_FILE_PATH_DIRECTORY;
			// return true;
			// }
			final String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Detect whether the network address
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static boolean checkUri(Context context, Uri uri) {
		boolean isUri = false;
		try {
			if (uri != null) {
				if (uri.getScheme().contains("http")
						|| uri.getScheme().equals("rtsp")
						|| uri.getScheme().equals("mms")
						) {
					isUri = true;
				} else {
					isUri = false;
				}
				LogUtil.i(TAG, "---checkUri()--getScheme()==" + uri.getScheme());
			}
			return isUri;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isUri;

	}


	/**
	 * check file directory exists.
	 * 
	 * @return boolean
	 */
	public static boolean checkAppFileDirectory(Context context) {

		try {
			final String imageDir = getAppFilesDirByData(context);
			final File imageFileDir = new File(imageDir);
			if (!imageFileDir.exists()) {
				final boolean isMkdirs = imageFileDir.mkdirs();
				return isMkdirs;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public static void showToast(Context context, int resId) {
		showToast(context, resId, Toast.LENGTH_LONG);
	}

	public static void showToast(Activity act, String msg) {
		showToast(act, msg, Toast.LENGTH_LONG);
	}

	public static void showToast(Context act, String msg) {
		showToast(act, msg, Toast.LENGTH_LONG);
	}

	public static void showToast(Activity act, int resId, int length) {
		Toast.makeText(act, resId, length).show();
	}

	public static void showToast(Context act, String content, int length) {
		Toast.makeText(act, content, length).show();
	}

	public static void showToast(Activity act, String content, int length) {
		Toast.makeText(act, content, length).show();
	}

	public static void showToast(Context context, int resId, int length) {
		Toast.makeText(context, resId, length).show();

	}

	private static Toast toast = null;;
	private static TextView errorText = null;


	/**
	 * Verify the mailbox format
	 */
	public static boolean checkEmail(String email) {
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(email);
		return m.find();
	}


	private HashMap<String, Drawable> imgCache;

	public HashMap<String, Drawable> getImgCache() {
		if (imgCache == null) {
			File f = new File("imgs.dat");
			if (!f.exists()) {
				imgCache = new HashMap<String, Drawable>();
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

			}
		}
		return imgCache;
	}

	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || Utils.NULL.equals(str)
				|| (str != null && "".equals(str.trim()))) {
			return true;
		}
		return false;
	}

	public static String getData(String str) {
		String data = null;
		if (TextUtils.isEmpty(str) || str.length() < 10) {
			return data;
		}
		data = str.substring(0, 10);
		return data;
	}

	/**
	 * Image Compression
	 * 
	 * @param bitmap
	 *            Source Picture
	 * @param destWidth
	 *            Target width
	 * @param destHeigth
	 *            Target height
	 * @return
	 */
	public static Bitmap imageCompression(Bitmap bitmap, int destWidth,
			int destHeigth) {
		if (bitmap == null) {
			return null;
		}
		final int w = bitmap.getWidth();
		final int h = bitmap.getHeight();
		/* calculate the scale */
		final float scaleWidth = ((float) destWidth) / w;
		final float scaleHeight = ((float) destHeigth) / h;
		/* create a matrix for the manipulation */
		final Matrix m = new Matrix();
		/* resize the Bitmap */
		m.postScale(scaleWidth, scaleHeight);
		/* recreate the new Bitmap */
		final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m,
				true);
		return resizedBitmap;
	}

	/**
	 * Image transparency processing
	 * 
	 * @param sourceImg
	 *            The original image
	 * @param number
	 *            Transparency
	 * @return
	 */
	public static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
				sourceImg.getWidth(), sourceImg.getHeight());// Pictures ARGB
																// value
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			if ((argb[i] & 0xff000000) != 0x00000000) {// Not deal with
														// transparent color
				argb[i] = (number << 24) | (argb[i] & 0xFFFFFF);// Modify the
																// maximum value
																// of 2
			}
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);
		return sourceImg;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	@SuppressWarnings("unused")
	private static SimpleDateFormat getFormat(String partten) {
		return new SimpleDateFormat(partten);
	}

	public static String getFormatDataString(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static String parseJsonDate(String date) {
		return date.substring(0, 10);
	}

	/**
	 * Detect network is available
	 */
	public static boolean isAvailable(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Access to the file and read data from the assets folder
	 * 
	 * @param context
	 * @param fileName
	 * @param isEncoding
	 * @return
	 */
	public static String getFromAssets(Context context, String fileName,
			boolean isEncoding) {
		String result = "";
		try {
			final InputStream in = context.getResources().getAssets()
					.open(fileName);
			// Get the number of bytes of the file
			final int lenght = in.available();
			// Create an array of byte
			byte[] buffer = new byte[lenght];
			// Data in the file to read the byte array
			in.read(buffer);
			if (isEncoding) {
				result = EncodingUtils.getString(buffer, "GBK");
			} else {
				result = EncodingUtils.getString(buffer, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Determine whether there is a memory card, and returns TRUE, otherwise
	 * FALSE
	 * 
	 * @return
	 */
	public static boolean isSDcardExist() {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Get the time now
	 * 
	 * @return Back to the string format yyyy-MM-the dd HH: mm: ss
	 */
	public static String getStringDate() {
		try {
			final Date currentTime = new Date();
			final SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return formatter.format(currentTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * MD5 Encryption
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e(TAG, e.toString());
			return null;
		} catch (UnsupportedEncodingException e) {
			LogUtil.e(TAG, e.toString());
			return null;
		}

		final byte[] byteArray = messageDigest.digest();

		final StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		// 6-bit encryption, 9 to 25
		return md5StrBuff.substring(8, 24).toString().toUpperCase();
	}

	public static String checkFilemd5(String filename) {
		if (filename == null) {
			return null;
		}
		InputStream fis = null;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}

			return toHexString(md5.digest());
		} catch (Exception e) {
			System.out.println("error");
			return null;
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * A BITMAP for access to the source image, no compression, the local
	 * picture
	 * 
	 * @param sImagePath
	 * @return
	 */
	public static Bitmap getImgCacheFromLocal(String sImagePath) {
		try {
			final File f = new File(sImagePath);
			if (!f.exists()) {
				return null;
			}
			final FileInputStream fis = new FileInputStream(f);
			LogUtil.i(TAG, "fis==" + fis);
			final Bitmap bitmap = BitmapFactory.decodeStream(fis);

			// add by yanggf
			// //压缩，用于节省BITMAP内存空间--解决BUG的关键步�
			// BitmapFactory.Options opts = new BitmapFactory.Options();
			// opts.inSampleSize = 2; //这个的值压缩的倍数�的整数倍），数值越小，压缩率越小，图片越清�
			//
			// if(f== null)
			// return null;
			// //返回原图解码之后的bitmap对象
			// final Bitmap bitmap = BitmapFactory.decodeFile(f.toString(),
			// opts);
			fis.close();
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			LogUtil.e(TAG, ex.toString());
			return null;
		}
	}

	/**
	 * Save the image
	 * 
	 * @param sOldImagePath
	 * @param sNewImagePath
	 *            Image path formats such as /data/data/com.xxx/1.png
	 * @return
	 */
	public static boolean saveImage(Bitmap oldbitmap, String sNewImagePath) {
		LogUtil.e(TAG, "cunrubendihuancun" + sNewImagePath);
		try {
			final FileOutputStream fileout = new FileOutputStream(sNewImagePath);
			oldbitmap.compress(CompressFormat.PNG, 100, fileout);
			fileout.flush();
			fileout.close();
			// add by yanggf
			System.gc();
			LogUtil.e(TAG, "cunrubendichenggong" + sNewImagePath);
			return true;
		} catch (Exception e) {
			LogUtil.e(TAG, "cunrubendichengshibai" + sNewImagePath);
			e.printStackTrace();
			LogUtil.e(TAG, e.toString());
			return false;
		}
	}


	/**
	 * Access to files directory installed on the user's mobile phone
	 * com.funshion.video.mobile
	 * 
	 * @return files
	 */
	public static String getAppFilesDirByData(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}



	/**
	 * Create /data/data/com.funshion.video.mobile/files/imgfiles Cache folder
	 */
	public static void initCacheFileByData(Context context) {
		final String imageDir = getAppFilesDirByData(context)
				+ CACHE_IMG_DIR_PATH;
		final File imageFileDir = new File(imageDir);
		if (!imageFileDir.exists()) {
			imageFileDir.mkdirs();
		}
	}


	public static void mkdirsFile(Context context, String filepath) {
		final File imageFileDir = new File(filepath);
		if (!imageFileDir.exists()) {
			imageFileDir.mkdirs();
		}
	}

	/**
	 * According to the resolution of the phone from the dp unit will become a
	 * px (pixels)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * Turn from the units of px (pixels) become dp according to phone
	 * resolution
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}


	/**
	 * According to the resolution width of the phone
	 */
	public static int getWidthPixels(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		if (null !=  context && null != dm) {
			((Activity) context).getWindowManager().getDefaultDisplay()
			.getMetrics(dm);
			return dm.widthPixels;
		}
		return UNKNOWN_DATA;
	}

	/**
	 * According to phone resolution height
	 */
	public static int getHeightPixels(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		if (null != context && null != dm) {
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			return dm.heightPixels;
		}
		return UNKNOWN_DATA;
	}

	/**
	 * The horizontal dpi of the device
	 */
	public static float getWidthDpi(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.densityDpi;
	}

	/**
	 * Vertical dpi of the device
	 */
	public static float getHeightDpi(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.ydpi;
	}

	/**
	 * According to the resolution of the phone status bar height
	 */
	public static int getStatusBarHeight(Context context) {
		Rect frame = new Rect();
		((Activity) context).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(frame);
		int hight = 0;
		int topsBarHeight = frame.top;
		int bottomsBarHeight = frame.bottom;
		if (bottomsBarHeight >= 0) {
			hight = bottomsBarHeight;
		} else if (topsBarHeight >= 0) {
			hight = topsBarHeight;
		}
		return hight;
	}

	private static Pattern StringPattern = null;

	public static List<String> getBlogSensitiveStrings(CharSequence blogContent) {
		final Set<String> stringSet = new LinkedHashSet<String>();
		final List<String> stringList = new ArrayList<String>();
		// if (StringPattern == null) {
		// English punctuation，Does not include _
		// final String symbol =
		// "[^\\x20-\\x2F]&&[^\\x3A-\\x40]&&[^\\x5B-\\x5E]&&[^\\x60]&&[^\\x7B-\\x7E]";
		final String symbolcn = "[^！]&&[^、]&&[^?]&&[^﹑]&&[^`]&&[^＇]&&[^ˊ]&&[^ˋ]&&[^′]&&[^?]&&[^“]&&[^＃]&&[^＄]&&[^％]&&[^＆]&&[^‘]&&[^（]&&[^）]&&[^＊]&&[^＋]&&[^，]&&[^－]&&[^。]"
				+ "&&[^／]&&[^：]&&[^；]&&[^＜]&&[^＝]&&[^＞]&&[^？]&&[^……]&&[^｀]&&[^｛]&&[^｜]&&[^｝]&&[^～]&&[^［]&&[^］]&&[^《]&&"
				+ "[^》]&&[^「]&&[^」]&&[^『]&&[^』]&&[^〖]&&[^〗]&&[^【]&&[^】]";//

		StringPattern = Pattern.compile("\\@[" + "" + symbolcn + "&&[\\S]]+",
				Pattern.CASE_INSENSITIVE);
		// }

		final Matcher m = StringPattern.matcher(blogContent);
		while (m.find()) {
			stringSet.add(m.group());
		}
		stringList.addAll(stringSet);
		return stringList;
	}

	/**
	 * ID verification is legitimate
	 * 
	 * @param cardNum
	 * @return
	 */
	public static boolean isIDCardLegitimate(String cardNum) {
		if (!TextUtils.isEmpty(cardNum)) {
			if (cardNum.length() == 15) {
				final Pattern pattern = Pattern.compile("^[0-9]*$");
				final Matcher matcher = pattern.matcher(cardNum);
				if (matcher.matches()) {
					return true;
				}
			} else if (cardNum.length() == 18) {
				final int[] ary = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
						8, 4, 2 };
				final char[] ch = { '1', '2', 'X', '9', '8', '7', '6', '5',
						'4', '3', '2' };
				int sum = 0;
				final char[] ary2 = cardNum.toCharArray();
				for (int i = 0; i < ary2.length - 1; i++) {
					sum += (ary2[i] - '0') * ary[i];
				}
				final char data = ch[sum % 11];
				char lastNum = cardNum.charAt(17);
				lastNum = lastNum == 'x' ? 'X' : lastNum;
				if (data == lastNum) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getNetMode(Context context) {
		String netMode = "";
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				netMode = NET_WORK_INVAILABLE;
			} else {
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					netMode = mobNetInfoActivity.getTypeName();
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					netMode = mobNetInfoActivity.getExtraInfo();

				} else {
					// Do nothing
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			netMode = "";
		} finally {
			if ("epc.tmobile.com".equals(netMode) || "".equals(netMode)) {
				netMode = "3G";
				return netMode;
			}
		}
		return netMode;
	}

	public static int getNetType(Context context) {
		int netMode = -1;
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				netMode = -1;
			} else {
				netMode = mobNetInfoActivity.getType();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			netMode = -1;
		}
		return netMode;
	}

	/**
	 * Return to the type of network: one on behalf of wifi, 2 on behalf of 2G
	 * or 3G, 3 on behalf of other
	 * 
	 * @param context
	 * @return
	 */
	public static int reportNetType(Context context) {
		int netMode = 0;
		if(context != null) {
			try {
				final ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				final NetworkInfo mobNetInfoActivity = connectivityManager
						.getActiveNetworkInfo();
				if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
					netMode = -1;
				} else {
					int netType = mobNetInfoActivity.getType();
					if (netType == ConnectivityManager.TYPE_WIFI) {
						netMode = 1;
					} else if (netType == ConnectivityManager.TYPE_MOBILE) {
						netMode = 2;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				netMode = 3;
				return netMode;
			}
		}
		return netMode;
	}

	/**
	 * Detect whether the phone Insert the SIM card
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCheckSimCardAvailable(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getSimState() != TelephonyManager.SIM_STATE_READY) {
			return false;
		}
		return true;
	}

	/**
	 * Detect whether there are network
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCheckNetAvailable(Context context) {
		boolean isCheckNet = false;
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity != null && mobNetInfoActivity.isAvailable()) {
				isCheckNet = true;
				return isCheckNet;
			} else {
				isCheckNet = false;
				return isCheckNet;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return isCheckNet;
	}

	/**
	 * Send SMS
	 * 
	 * @param msg
	 */
	public static void sms(Context context, String msg) {
		if (Utils.isCheckSimCardAvailable(context)) {
			if (TextUtils.isEmpty(msg)) {
				return;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.putExtra("sms_body", msg);
			intent.setType("vnd.android-dir/mms-sms");
			context.startActivity(intent);
		} else {
			// Utils.showToast(context, R.string.str_no_sim);
		}
	}

	/**
	 * Call
	 * 
	 * @param context
	 * @param phone
	 */
	public static void call(Context context, String phone) {
		if (Utils.isCheckSimCardAvailable(context)) {
			if (TextUtils.isEmpty(phone)) {
				return;
			}
			Intent phoneIntent = new Intent(
					Intent./* ACTION_CALL */ACTION_DIAL, Uri.parse("tel:"
							+ phone));
			context.startActivity(phoneIntent);
		} else {
			// Utils.showToast(context, R.string.str_no_sim);
		}

	}

	public static boolean isNEON = false;

	public static boolean intCPUInfo() {

		String cupinfo = getCPUInfos();
		if (cupinfo != null) {
			cupinfo = cupinfo.toLowerCase();
			if (cupinfo != null && cupinfo.contains("neon")) {
				isNEON = true;
			} else {
				isNEON = false;
			}
		}
		return isNEON;
	}

	public static boolean isNEON() {
		return isNEON;
	}

	private static String getCPUInfos() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		StringBuilder resusl = new StringBuilder();
		String resualStr = null;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				resusl.append(str2);
				String cup = str2;
				LogUtil.i(TAG, "---" + cup);
			}

			if (localBufferedReader != null) {
				localBufferedReader.close();
			}
			if (resusl != null) {
				resualStr = resusl.toString();
				return resualStr;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resualStr;
	}

	private static String[] getDivceInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cpuInfo;
	}

	public static String getDeviceCPUInfo() {
		String cpuType = "";

		String[] cpuInfo = getDivceInfo();
		if (cpuInfo != null && cpuInfo.length > 0) {
			cpuType = cpuInfo[0];
		}
		return cpuType;
	}

	/**
	 * Get the device id
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId();
		return DEVICE_ID;
	}

	/**
	 * Get android system version number
	 * 
	 * @param context
	 * @return
	 */
	public static String getOSVersion(Context context) {
		String release = android.os.Build.VERSION.RELEASE; // android release
															// =
															// "android"+release;
		return release;
	}

	/**
	 * Android system sdk version number
	 * 
	 * @param context
	 * @return
	 */
	public static String getOSVersionSDK(Context context) {
		String sdk = android.os.Build.VERSION.SDK;
		return sdk;
	}
	
	public static void playView(Media mMedia  ,Context context){   
		try {  
			
			 if(UIUtils.hasNetwork(context)){
				if(mMedia != null){ 
					Intent intent = new Intent(context,SystemPlayer.class);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("media", mMedia);
					intent.putExtras(mBundle);
					context.startActivity(intent);
					
				}
	  		  }else{
	  			UIUtils.showToast(context, context.getText(R.string.tip_network).toString());
				  
	  		  } 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static int getOSVersionSDKINT(Context context) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		return currentapiVersion;
	}

	/**
	 * Access to device resolution and density information to determine its to
	 * belong aphone or apad criterion here is to determine the resolution and
	 * dpi comprehensive comparison, about the standard Greater than 800x480 for
	 * the pad is less than for the phone
	 */
	public static String getDeviceType(Context context) {
		if (null == context) {
			return "";
		}
		String deviceType = "";
		// int widthPixels = getWidthPixels(context);
		// float widthDpis = getWidthDpi(context);

		DisplayMetrics dm = new DisplayMetrics();
		if (null != dm) {
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			double x = Math.pow(dm.widthPixels, 2);
			double y = Math.pow(dm.heightPixels, 2);
			double screenInches = Math.sqrt(x + y) / (160 * dm.density);

			if (2 < screenInches && 5 > screenInches) {
				deviceType = "aphone";
			} else {
				deviceType = "apad";
			}
		}
		return deviceType;
	}

	/**
	 * Get phone model
	 */
	public static String getDeviceModel() {
		String model = android.os.Build.MODEL;
		String StrContent = null;
		try {
			StrContent = URLEncoder.encode(model, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StrContent;
	}
	
    

	/**  
     * 取字符串的前toCount个字符  ,并加前缀
     * @param str 被处理字符串  
     * @param toCount 截取长度  
     * @param more 前缀字符串  
     * @version 2013.1.17  
     * @author yanggf  
     * @return String  
     */ 
    public static String substringAndAddPrefix(String str, int toCount,String more)  
    {  
      int reInt = 0;  
      String reStr = "";  
      if (str == null)  
        return "";  
      char[] tempChar = str.toCharArray();  
      for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {  
        String s1 = str.valueOf(tempChar[kk]);  
        byte[] b = s1.getBytes();  
        reInt += b.length;  
        reStr += tempChar[kk];  
      }  
      if (toCount == reInt || (toCount == reInt - 1))  
        reStr = more+reStr;  
      return reStr;  
    } 

	/**
	 * Access to the device Mac address
	 * 
	 * @param context
	 * @return
	 */



	


	/**
	 * Access to equipment ip address
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
			LogUtil.e(TAG, "WifiPreference IpAddress");
		}
		return null;
	}



	/**
	 * Get the available phone memory
	 * 
	 */
	public static double getAvailableInternalMemory() {

		double availableInternalMemorySize = 0;

		try {

			File internalPathS = Environment.getDataDirectory();
			StatFs internalStat = new StatFs(internalPathS.getPath());
			long internalBlockSize = internalStat.getBlockSize();
			long availableInternalBlocks = internalStat.getAvailableBlocks();
			availableInternalMemorySize = (internalBlockSize * availableInternalBlocks)
					/ (1024 * 1024);
			return availableInternalMemorySize;

		} catch (Exception e) {
			e.printStackTrace();

		}
		return 0;
	}

	/**
	 * The total memory of the mobile phone SDcard external
	 * 
	 */
	public static double getTotalExternaMemory() {

		File externalPath = null;
		double totalExternaMemorySize = 0;

		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				externalPath = Environment.getExternalStorageDirectory();
				StatFs externaStat = new StatFs(externalPath.getPath());
				long externaBlockSize = externaStat.getBlockSize();
				long externaTotalBlocks = externaStat.getBlockCount();
				totalExternaMemorySize = (externaBlockSize * externaTotalBlocks)
						/ (1024 * 1024);
				return totalExternaMemorySize;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalExternaMemorySize;

	} 

	private static Dialog waitingDialog = null;

	public static void startWaitingDialog(Context context) {

		try {
			// synchronized(Utils.synchronizeds) {
			if (waitingDialog == null) {
				waitingDialog = new Dialog(context, R.style.waiting);
				// dialog.setCanceledOnTouchOutside(true);
				waitingDialog.setContentView(R.layout.waiting);
//				waitingDialog.setCanceledOnTouchOutside(false);
				// dialog.setCancelable(false);
				waitingDialog.show();
			} else if (waitingDialog != null && !waitingDialog.isShowing()) {
				waitingDialog.setContentView(R.layout.waiting);
				waitingDialog.setCanceledOnTouchOutside(false);
				waitingDialog.show();
			}
			//
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void closeWaitingDialog() {
		try {
			// synchronized(Utils.synchronizeds) {
			if (waitingDialog != null) {
				waitingDialog.dismiss();
				waitingDialog = null;
			}
			// }
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private static Dialog mErrorDialog;
	private static Builder aler = null;


	
	/**
	 * Delete a directory
	 */
	public static boolean delDir(File dir) {
		try {
			if (dir == null || !dir.exists() || dir.isFile()) {
				return false;
			}
			for (File file : dir.listFiles()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					delDir(file);// Recursive
				}
			}
			dir.delete();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Interception by the word request string will be converted to an array of
	 * strings, and every element in the array of strings displayed incomplete
	 * on the amputated
	 */
	public static String cutStringLength(String[] st, String s, int length) {

		// If the number of characters exceeds the limit is not shown at the end
		// of the whole of the type remove
		if (s.length() > length) {
			if (st[0].length() > length) {
				s = "";
			} else {
				s = s.substring(0, length);
				if (s.lastIndexOf("  ") != -1) {
					s = s.substring(0, s.lastIndexOf("  "));
				}
			}
		} else {
			if ("".equals(s.trim())) {
				s = "暂无";
			}
		}
		return s;
	}


	
	/**
	 * 方法二：动态获取应用程序的版本名称
	 */
	public static String getAppVersionName(Context context) {
		mContext = context;
		setVersionNameFromPackage();
		return mAppVersion;
	}
	
	
	private static void setVersionNameFromPackage() {
		if(!isNeedToSetVersionNumber()) {
		    return;
		}
		try { 
				PackageManager manager = mContext.getPackageManager(); 
				PackageInfo info = manager.getPackageInfo( mContext.getPackageName(), 0 ); 
				if(info != null&&!isEmpty(info.versionName)){
					mAppVersion =  info.versionName;
				}
			} 
			catch (Exception e) { 
				e.printStackTrace(); 
				mAppVersion = UNKNOWN_VERSION; 
		} 
	}
	
	private static boolean isNeedToSetVersionNumber() {
		return ((UNKNOWN_VERSION.equals(mAppVersion)) && (mContext != null) );
	}
	

	




	/***
	 * 判断Network具体类型（联通移动wap，电信wap，其他net）
	 * 
	 * */
	public static int checkNetworkType(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				// 获取不到可连接网络时候当net处理
				return TYPE_OTHER_NET;
			} else {
				// NetworkInfo不为null开始判断是网络类型
				final int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					// wifi net处理
					return TYPE_OTHER_NET;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {

					// 判断是否是电信的wap
					// 为什么不通过getExtraInfo判断是否电信wap那？
					// 因为通过目前多种机型测试电信apn名称都为#777或者null
					// 根据电信apn有用户名
					final Cursor c = mContext.getContentResolver().query(
							PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c
								.getColumnIndex("user"));
						if (!TextUtils.isEmpty(user)) {
							if (user.startsWith(CTWAP)) {
								return TYPE_CT_WAP;
							}
						}
						c.close();
					}

					// 判断是手机的net还是wap
					final String netMode = mobNetInfoActivity.getExtraInfo();
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						if (netMode.equals(Utils.CMWAP)
								|| netMode.equals(Utils.WAP_3G)
								|| netMode.equals(Utils.UNIWAP)) {
							return TYPE_CM_CU_WAP;
						}

					}

				}
			}
		} catch (Exception ex) {
			return TYPE_OTHER_NET;
		}

		return TYPE_OTHER_NET;

	}



	private static final char SYSTEM_SEPARATOR = File.separatorChar;
	private static final char WINDOWS_SEPARATOR = '\\';

	public static final int MEDIA_CONTENT_PROVIDER_CONTENT = 0;
	public static final int STREAMING_CONTENT = 1;
	public static final int SDCARD_FILE_CONTENT = 2;
	public static final int EMAIL_ATTACHMENT_CONTENT = 3;
	public static final int MMS_ATTACHMENT_CONTENT = 4;
	public static final int DRM_PROTECTED_CONTENT = 5;
	public static final int ON_PHONE_FLASH_CONTENT = 6;

	public static String getStringTime(int position) {
		SimpleDateFormat fmPlayTime;
		if (position <= 0) {
			return "00:00";
		}

		long lCurrentPosition = position / 1000;
		long lHours = lCurrentPosition / 3600;

		if (lHours > 0)
			fmPlayTime = new SimpleDateFormat("HH:mm:ss");
		else
			fmPlayTime = new SimpleDateFormat("mm:ss");

		fmPlayTime.setTimeZone(TimeZone.getTimeZone("GMT"));
		return fmPlayTime.format(position);
	}

	public static String getExternalStoragePath() {
		boolean bExists = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		String path = null;
		if (bExists) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();

			if (path == null) {
				return "/";
			}

			if ("/mnt/flash".equalsIgnoreCase(path)) {
				path = "/mnt/sdcard";
				File file = new File(path);
				if (!file.exists()) {
					path = "/sdcard";
					file = new File(path);
					if (!file.exists()) {
						path = "/";
					}
				}
			}

			return path;
		} else {
			return "/";
		}
	}

	// public static boolean isEmpty(String content) {
	// if (content == null || "".equals(content.trim())) {
	// return true;
	// }
	// return false;
	// }

	public static String getMntPath() {
		String sdCardPath = Utils.getExternalStoragePath();

		if ("/mnt/sdcard".equals(sdCardPath)) {
			return "/mnt";
		} else {
			return "/";
		}

	}

	public static String getThumbnailPath() {
		boolean bExists = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!bExists) {
			return null;
		}

		File file = new File(getExternalStoragePath() + "/DCIM/.thumbnails");
		if (!file.exists()) {
			file.mkdir();
		}
		return file.getAbsolutePath();
	}

	public static boolean deleteFile(String path) {
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}

		if (!file.exists()) {
			return true;
		}

		return false;
	}

	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		if (!isSymlink(directory)) {
			cleanDirectory(directory);
		}

		if (!directory.delete()) {
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}

	private static boolean isSymlink(File file) throws IOException {
		if (file == null) {
			throw new NullPointerException("File must not be null");
		}
		if (isSystemWindows()) {
			return false;
		}
		File fileInCanonicalDir = null;
		if (file.getParent() == null) {
			fileInCanonicalDir = file;
		} else {
			File canonicalDir = file.getParentFile().getCanonicalFile();
			fileInCanonicalDir = new File(canonicalDir, file.getName());
		}

		if (fileInCanonicalDir.getCanonicalFile().equals(
				fileInCanonicalDir.getAbsoluteFile())) {
			return false;
		} else {
			return true;
		}
	}

	private static boolean isSystemWindows() {
		return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
	}

	private static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (File file : files) {
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	private static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: "
							+ file);
				}
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	public static String getPref(Context context, String prefName,
			String defaultValue) {
		SharedPreferences prefs = context
				.getSharedPreferences(
						context.getPackageName() + "_preferences",
						Context.MODE_PRIVATE);
		return prefs.getString(prefName, defaultValue);
	}

	public static void setPref(Context context, String prefName,
			String prefValue) {
		SharedPreferences prefs = context
				.getSharedPreferences(
						context.getPackageName() + "_preferences",
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(prefName, prefValue);
		editor.commit();
	}

	public static long getPref(Context context, String prefName,
			long defaultValue) {
		SharedPreferences prefs = context
				.getSharedPreferences(
						context.getPackageName() + "_preferences",
						Context.MODE_PRIVATE);
		return prefs.getLong(prefName, defaultValue);
	}

	public static void setPref(Context context, String prefName, long prefValue) {
		SharedPreferences prefs = context
				.getSharedPreferences(
						context.getPackageName() + "_preferences",
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(prefName, prefValue);
		editor.commit();
	}

	public static boolean getPref(Context context, String prefName,
			boolean defaultValue) {
		SharedPreferences prefs = context
				.getSharedPreferences(
						context.getPackageName() + "_preferences",
						Context.MODE_PRIVATE);
		return prefs.getBoolean(prefName, defaultValue);
	}

	public static void setPref(Context context, String prefName,
			boolean prefValue) {
		SharedPreferences prefs = context
				.getSharedPreferences(
						context.getPackageName() + "_preferences",
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(prefName, prefValue);
		editor.commit();
	}

	public static void removePref(Context context, String prefName) {
		SharedPreferences prefs = context
				.getSharedPreferences(
						context.getPackageName() + "_preferences",
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(prefName, null);
		editor.commit();
	}

	public static void addToRecents(Context ctx, String url) {
		SharedPreferences prefs = ctx.getSharedPreferences(
				"pref_video_recent_urls", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		ArrayList<String> recents = new ArrayList<String>();

		for (int i = 0; i < 10; i++) {
			String key = "recent" + Integer.toString(i);
			String value = prefs.getString(key, null);

			if (value != null) {
				editor.remove(key);

				if (url.compareToIgnoreCase(value) != 0) {
					recents.add(value);
				}
			} else {
				break;
			}
		}

		recents.add(0, url);

		for (int j = 0; j < recents.size(); j++) {
			String key = "recent" + Integer.toString(j);
			editor.putString(key, recents.get(j));
		}

		editor.commit();
	}

	public static boolean checkNetworkConnection(Context ctx, boolean bWifiOnly) {
		boolean bStatus = false;
		ConnectivityManager cm = ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE));

		if (cm == null
				|| (bWifiOnly && cm
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI) == null)
				|| (!bWifiOnly && cm.getActiveNetworkInfo() == null)) {
			Log.e(TAG, "Network is offline.");
		} else {
			if (bWifiOnly) {
				bStatus = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
						.isConnected();
			} else {
				bStatus = cm.getActiveNetworkInfo().isConnected();
			}
		}

		if (!bStatus) {
			Log.w(TAG, "Network is offline");
		}

		return bStatus;
	}

	// private static FSAlertDialog sdUnmountedDlg=null;
	// public static void sdUnmountedAlert(final Activity activity, final String
	// confirmationMsg) {
	// removeSdUmountedAlert();
	// sdUnmountedDlg = new FSAlertDialog.Builder(activity).create();
	// sdUnmountedDlg.setTitle(R.string.sdcard_missing_title);
	// sdUnmountedDlg.setMessage(confirmationMsg);
	// sdUnmountedDlg.setButton(DialogInterface.BUTTON_POSITIVE,
	// activity.getString(R.string.ok),
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// activity.finish();
	// }
	// });
	// sdUnmountedDlg.show();
	// }
	//
	// public static void removeSdUmountedAlert() {
	// if (sdUnmountedDlg != null) {
	// sdUnmountedDlg.dismiss();
	// sdUnmountedDlg = null;
	// }
	// }
	//
	// public static void networkLostAlert(final Activity activity, final String
	// confirmationMsg) {
	// FSAlertDialog noNetworkDlg = new
	// FSAlertDialog.Builder(activity).create();
	// noNetworkDlg.setTitle(R.string.lost_network_connection);
	// noNetworkDlg.setMessage(confirmationMsg);
	// noNetworkDlg.setButton(DialogInterface.BUTTON_POSITIVE,
	// activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// activity.finish();
	// }
	// });
	// noNetworkDlg.show();
	// }

	public static int getContentType(String title) {
		int retVal = MEDIA_CONTENT_PROVIDER_CONTENT;
		if (title.startsWith("http://") || title.startsWith("https://")
				|| title.startsWith("rtsp://")) {
			retVal = STREAMING_CONTENT;
		} else if (title.startsWith("/mnt/") || title.startsWith("/sdcard/")) {
			retVal = SDCARD_FILE_CONTENT;
		} else if (title.startsWith("content://com.htc.android.mail")
				|| title.startsWith("content://gmail")) {
			retVal = EMAIL_ATTACHMENT_CONTENT;
		} else if (title.startsWith("content://mms/")) {
			retVal = MMS_ATTACHMENT_CONTENT;
		} else if (title.startsWith("content://drm/")) {
			retVal = DRM_PROTECTED_CONTENT;
		} else if (title.startsWith("/")) {
			retVal = ON_PHONE_FLASH_CONTENT;
		}
		return retVal;
	}

	public static boolean sdcardExists() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	public static boolean checkFileIsPlayed(String path) {
		if (path == null || path.trim().equals("")) {
			return false;
		} else if (path.startsWith("/")) {
			File file = new File(path);
			if (file.exists()) {
				if (file.isFile()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (path.startsWith("file://")) {
			path = path.replaceAll("file://", "");
			File file = new File(path);
			if (file.exists()) {
				if (file.isFile()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (path.startsWith("http://") || path.startsWith("https://")
				|| path.startsWith("rtsp://") || path.startsWith("content://")) {
			return true;
		}

		return false;
	}

	public static String formatTime(long nTimeMS) {
		if (nTimeMS <= 0) {
			return "0:00";
		}

		long seconds = nTimeMS / 1000;
		long sec = seconds % 60;
		long min = seconds / 60;
		long hour = min / 60;
		min %= 60;

		StringBuilder sb = new StringBuilder();
		if (hour > 0) {
			sb.append(hour);
			sb.append(':');
		}
		if (hour > 0 && min < 10) {
			sb.append('0');
		}
		sb.append(min);
		sb.append(':');
		if (sec < 10) {
			sb.append('0');
		}
		sb.append(sec);
		return sb.toString();
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 判断是否存在缓存文件
	 * 
	 * @return add by jiyx at 2012-8-27 16:00:59
	 */
	public static boolean isExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 从网络下载图片
	 * 
	 * @param url
	 * @param file
	 * @throws IOException
	 */
	public static void getImageFromNet(String url, File file)
			throws IOException {
		InputStream is = null;
		URL imageUrl = new URL(url);
		LogUtil.i(TAG, "从网络下载图片url :" + url);
		HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		conn.setInstanceFollowRedirects(true);
		is = conn.getInputStream();
		saveImage(is, file);
	}

	/**
	 * 图片下载到本地文件
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static void saveImage(InputStream is, File file) throws IOException {
		OutputStream os = null;
		if (!file.exists()) {
			file.createNewFile();
		}
		os = new FileOutputStream(file);
		CopyStream(is, os);
		if (os != null) {
			os.close();
		}
		if (is != null) {
			is.close();
		}
	}

	public static void CopyStream(InputStream is, OutputStream os)
			throws IOException {
		final int buffer_size = 1024;
		byte[] bytes = new byte[buffer_size];
		for (;;) {
			int count = is.read(bytes, 0, buffer_size);
			if (count == -1)
				break;
			os.write(bytes, 0, count);
		}
	}

	public static String getDeviceManufacturer() {
		String model = android.os.Build.MANUFACTURER;
		String StrContent = null;
		try {
			StrContent = URLEncoder.encode(model, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StrContent;
	}

	/**
	 * 将字符串写入文件
	 * @param path
	 * @param rptContent
	 */
	public static void writeStateToFile(String path, String rpt) {
		//写入文本文件
		File file = new File(path);
		BufferedWriter writer = null;
		String str="";
		try{
			if (!file.exists())
				file.createNewFile();
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			writer.write(rpt);
		}catch (Exception e){
			str=e.getMessage();
		}
		finally{
			try{
				if (writer != null)
					writer.close();
			}catch (IOException e){
				str=str+"保存报文出错,未能正确关闭文件流。";
			}
		}
	}
	
	public static void startSystemPlayer(Context context,ArrayList<MediaItem> mCurrentPlayList,int position){
		Intent intent = new Intent(context,SystemPlayer.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("MediaIdList", mCurrentPlayList);
		intent.putExtras(mBundle);
		intent.putExtra("CurrentPosInMediaIdList", position);
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.fade, R.anim.hold);
	}
	
	public static String getFileName(String uri) {
		String name = uri;
		if (name != null) {
			String[] content = name.split("/");
			if (content != null && content.length > 1) {
				name = content[content.length - 1];
			}
		}

		return name;
	} 
	
	public static boolean isCheckUriByM3u8(Context context, Uri uri) {
		boolean isUri = false;
		try {
			if (uri != null) {
				if (uri.toString() != null && uri.toString().toLowerCase().contains("m3u8")
						) {
					isUri = true;
				} else {
					isUri = false;
				}
			
			}
		} catch (Exception e) {
			return isUri;
		}
		return isUri;

	}
	
	public static String readAssetsToString(Activity context,String assetsFileName) {
		InputStream is = null;
		String temStr = null;
		try {
			is = context.getAssets().open(assetsFileName);
			
			if (is != null) {
	               StringBuilder sb = new StringBuilder();
	               String line;
	                  BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	                   while ((line = reader.readLine()) != null) {
	                     sb.append(line);
	                   }
	                   if(sb !=null){
	      				 temStr = sb.toString();
	      		      }
	      		     return temStr;

	             } 
			

		} catch (IOException e) {
			e.printStackTrace();
			  return temStr;

		} finally {
			try {
				if(is != null)
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return temStr;

	}
	
	
	public static boolean checkLive(Context context, Uri uri) {
		boolean isUri = false;
		try {
			if (uri != null) {
				if (uri.toString() != null && uri.toString().contains("m3u8")) {
					isUri = true;
				}else if (uri.toString() != null && uri.toString().contains("rtsp")){
					isUri = true;
				}else if (uri.getScheme()!=null&&uri.getScheme().toLowerCase().contains("mms")){
					isUri = true;
				}else{
					isUri = false;
				}
				
			}
		} catch (Exception e) {
			return isUri;
		}
		return isUri;

	}
	
	public static void netNoPlayeDialog(Context context) {// 退出确认
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle("提示");
		ad.setMessage("暂时只支持android_2.1以上系统");
		ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 退出按钮
					
					public void onClick(DialogInterface dialog, int i) {
					}
				});
		
		ad.show();// 显示对话框
	}
	
	public static boolean  isErrorNum =  false;
	
}
