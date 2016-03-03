package cn.hi321.android.media.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
 

public class FileCache {
    
    private File cacheDir;
    
    private final int MB = 1024 * 1024;
    private final int CACHE_SIZE = 3; // 限制文件包缓存图片目录大小最大为3M
    private final int SD_CACHE_SIZE = 10; // 限制文件图片缓存目录大小最大为10M
    
    public FileCache(Context context){
        //Find the dir to save cached images  检查有SD卡并且存储空间至少有3Mb的可用空间
        if (UIUtils.isSDcardExist() && UIUtils.getAvailableExternalMemory() >= 3){
        	// /mnt/sdcard/funshion/imgfiles
        	cacheDir = new File(UIUtils.getAppFilesDirBySDCard(context) + UIUtils.CACHE_IMG_DIR_PATH);
        }else {
        	//  /data/data/com.funshion.video.mobile/cache/imgfiles
            cacheDir = new File(context.getCacheDir()+ UIUtils.CACHE_IMG_DIR_PATH);
        } 
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }
    
    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
//        String filename=String.valueOf(url.hashCode());
        final String filename = UIUtils.getMD5Str(url) + ".dat";
        //Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        if(f.exists()){
        	f.setLastModified(System.currentTimeMillis());
        }
        return f;
        
    }
    /**
     * 清理缓存
     * 计算当前缓存目录大小
     * 删除40%最近没有被使用的文件
     * 
     */
    public synchronized void clear(){
        try {
			File[] files = cacheDir.listFiles();
			if (files == null)
				return;
//			for (int i = 0; i < files.length; i++) {
//			      LogUtil.i("TEST", "file i="+i +" "+getFormatDateString(files[i].lastModified()));
//			}
			
			Arrays.sort(files, new FileLastModifSort());
			
//	        for (int i = 0; i < files.length; i++) {
//        	LogUtil.i("TEST", "sort i="+i +" "+getFormatDateString(files[i].lastModified()));
//        }
			double freeSpaceOnSd = UIUtils.getAvailableExternalMemory();
			int cacheDirSize = 0; // 计算当前缓存目录大小
			for (int i = 0; i < files.length; i++) {
				cacheDirSize += files[i].length();
			}
			int removeFactor = (int) ((0.4 * files.length));
			// 有SD卡并且当前图片文件目录缓存大等于10M 或者 SD卡剩余空小于3M
			if (UIUtils.isSDcardExist()
					&& ((cacheDirSize >= SD_CACHE_SIZE * MB) || freeSpaceOnSd < 3)) {
				for (int i = 0; i < removeFactor; i++) {
					files[i].delete();
				}
			}
			// 没有SD卡并且程序内缓存目录大等于3M
			if (!UIUtils.isSDcardExist() && cacheDirSize >= CACHE_SIZE * MB) {
				for (int i = 0; i < removeFactor; i++) {
					files[i].delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 根据文件的最后修改时间进行升序排序 
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
        	 long d1 = arg0.lastModified();
             long d2 = arg1.lastModified();
             if (d1 == d2){
                 return 0;
             } else {
                 return d1 > d2 ? 1 : -1;
             }
//            if (arg0.lastModified() > arg1.lastModified()) {
//                return 1;
//            } else if (arg0.lastModified() == arg1.lastModified()) {
//                return 0;
//            } else {
//                return -1;
//            }
        }
    }
    
//    /**
//	  * 时间戳转化为日期格式字符
//	  * 
//	  * @return 返回日期格式为：2012年6月17日
//	  */
//	private  String getFormatDateString(long time){
//		SimpleDateFormat df = new SimpleDateFormat( "yyyy:MM:dd:HH:mm:ss" );
//		String dateTime = df.format(time );
//		return dateTime;
//	}
}