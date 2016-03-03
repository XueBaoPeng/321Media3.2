package cn.hi321.android.media.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.Toast;

public class UIUtils { 
	
	public static final int Get_Slices = 10131;
	public static final int GEG_MEDIA_PLAY_URL = 1020;
	public static final int BaiDuInfoFlag = 1018;
	public static final int BaiDuRecommend = 1019;
	public static final int Channel_Video_Info = 1021;
	public static final int Channel_Search = 1022;//检索接口
	public static final int Channel_Search_Video = 1023;
	public static final int Channel_ScrollStateChanged = 1024;
	public static final int Channel_Video_View = 1025;
	public static final int Channel_Video_View_hot = 1026;
	public static final int Research_Activity = 1027;
	public static final int BaoFeng_Detail = 1028;
	public static final int BaoFeng_Play = 1029;
	
	
	public static final int StopDialog = 1008;
	public static final  int StrDialog = 1009;
	public static final  int MAIN_DATA = 1011;
	
	public static final int GET_USER_DATA = 1012;
	public static final int SHOW_WINDOWPOP =1013;
	public static final int GEG_MEDIA_DATA= 1014;
	public static final int RefleshView = 1015;
	public static final int GET_PLAY_DATA = 1016;
	public static final int SHOW_PLAY = 1017;
	
	public static int onclick_error = 0;
	/** 网络出错 */
	public static final String CODE_HTTP_FAIL = "-1";
	/** dxMember取不到id，默认返回值 */
	public static final int ID_DXMEMBER = -1;
	/** 无问题的errCode */
	public static final String CODE_ERROR_RIGHT = "0";
	/** 联网成功 */
	public static final String CODE_HTTP_SUCCEED = "200";
	public static final String CODE_HTTPS_RECONNECT = "002";
	/** 会话过期 */
	public static final String CODE_SESSION_EXPIRED = "2000";
	/** 服务器停止服务 */
	public static final String CODE_STOP_SERVER = "5000";
	public static final String SERVER_NOT_RESPONDING = "10000";
	/** 没有找到界面 */
	public static final String CODE_PAGE_NOT_FOUND = "404";
	/** 重启客户端 */
	public static final String CODE_HTTP_RESTART_CLIENT = "4000"; 
	public static final String NET_WORK_INVAILABLE = "netInvailable";
	/** 手机网络cmwap */
	public static final String NET_CMWAP = "cmwap";
	/** 手机网络3gwap */
	public static final String NET_WAP_3G = "3gwap";
	/** 手机网络uniwap */
	public static final String NET_UNIWAP = "uniwap"; 
	

	/** errorMessage */
	public static final String KEY_ERROR_MESSAGE = "errorMessage";

	/** 文件保存路径sdcard/funshion/ */

	public static final String SAVE_FILE_PATH_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/" + "321";
	/** 缓存图片的目录 */
	public static final String CACHE_IMG_DIR_PATH = "/imgfiles/";
	
	
	/**
	 * 获取视频信息路径
	 * */
	public static String getUrl(int pageindex,int pagesize,String type ){
		String url = Contents.url+"pageindex="+pageindex+"&pagesize="+pagesize+"&type="+type
				+"&cli="+Contents.cli+"&ver="+Contents.version;
//		http://4.doukan.sinaapp.com/api/top/?pageindex=9&pagesize=20&type=movie&cli=iPhone%204%20(GSM)&ver=1.0.6
//			电影，电视剧，动漫，综艺；娱乐（movie,tv,katong,zy）娱乐暂时没有做
		return url;
	}
	
	
	public static int convertDipOrPx(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}
	
	public static String replaceBlank(String str) {
		  String dest = "";
		  if (str!=null) {
		   Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		   Matcher m = p.matcher(str);
		   dest = m.replaceAll("");
		  }
		  return dest;
		 }
	
	 
	
	/**
	 * 获取安装在用户手机上的sdcard/下的321目录
	 * 
	 */
	public static String getAppFilesDirBySDCard(Context context) {
		return SAVE_FILE_PATH_DIRECTORY;
	}

	 public static LayoutInflater getLayoutInflater(Context paramContext) {
	    return (LayoutInflater)paramContext.getSystemService("layout_inflater");
	  }
	 

	  public static void showToast(Context paramContext,String s )
	  {
	    Toast.makeText(paramContext, "亲网络异常，请检查网络", 0).show();
	  }
	  
	  public static boolean hasNetwork(Context context) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
			        .getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo(); 
			if (info != null && info.isAvailable()) { 
				 return true ; 
			} 
			return false;
		}
		
	  
	  
		public static boolean isCMWAP(Context context) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo(); 
			if (info == null || !info.isAvailable()) { 
				return false;
			} else if (info.getTypeName().equals("WIFI")) {
				return false;
			} else if (info.getTypeName() != null
					&& info.getTypeName().equals("MOBILE")
					&& info.getExtraInfo() != null
					&& info.getExtraInfo().toLowerCase().equals("cmwap")) {

				return true; 
			}
			return false;
		}
		
	  public static int getScreenWidth(Activity paramActivity)
	  {
	    return paramActivity.getWindowManager().getDefaultDisplay().getWidth();
	  }

	  public static int getScreenWidth(Context paramContext)
	  {
	    return ((Activity)paramContext).getWindowManager().getDefaultDisplay().getWidth();
	  }
	  
	  public static int dip2px(Context paramContext, float paramFloat)
	  {
	    float f = paramContext.getResources().getDisplayMetrics().density;
	    return (int)(paramFloat * f + 0.5F);
	  }
	  
	//截取数字  
	   public static String getNumbers(String content) {  
	       Pattern pattern = Pattern.compile("\\d+");  
	       Matcher matcher = pattern.matcher(content);  
	       while (matcher.find()) {  
	           return matcher.group(0);  
	       }  
	       return "";  
	   }  
	
		/**
		 * 检测网络是否可用
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

		/**
		 * 判断是否有存储卡，有返回TRUE，否则FALSE
		 * 
		 * @return
		 */
		public static boolean isSDcardExist() {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * 得到手机SDcard外部可用内存(剩余空间)
		 * 
		 */
		public static double getAvailableExternalMemory() {
			double availableExternalMemorySize = 0;

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File externalPath = Environment.getExternalStorageDirectory();
				StatFs externaStat = new StatFs(externalPath.getPath());
				// 参数
				long externaBlockSize = externaStat.getBlockSize();
				// 外部可用内存
				long externaAvailableBlocks = externaStat.getAvailableBlocks();

				availableExternalMemorySize = (externaBlockSize * externaAvailableBlocks)
						/ (1024 * 1024);
				return availableExternalMemorySize;
			}
			return availableExternalMemorySize;

		}
		
		/**
		 * MD5 加密
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
				
				return null;
			} catch (UnsupportedEncodingException e) {
				
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
			// 16位加密，从第9位到25位
			return md5StrBuff.substring(8, 24).toString().toUpperCase();
		}
		/**
		 * 根据手机的分辨率宽度
		 */
		public static int getWidthPixels(Context context) {
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			return dm.widthPixels;
		}

		/**
		 * 根据手机的分辨率高度
		 */
		public static int getHeightPixels(Context context) {
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			return dm.heightPixels;
		}
		 

}
