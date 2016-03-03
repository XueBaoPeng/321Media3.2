package cn.hi321.android.media.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.china.R;
 

public class ImageLoader {
	
	private static final String TAG = "ImageLoader";
//	
//	private boolean isUseOldGetBirmap = true;
//	
//	private ImageView tragerhideig = null;

	MemoryCache memoryCache = new MemoryCache();
	
	FileCache fileCache;
	
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	
	ExecutorService executorService;
	
	ExecutorService clearCacheService;
	// 单个CPU线程池大小 
    private int POOL_SIZE = 5;
	Context context;
	boolean isImageSuperposition = false;
	int res_Id; 
	/**
	 * 
	 * @param context
	 * 
	 * @param bg_id
	 *        默认背景图片
	 */
	public ImageLoader(Context context, int bg_id) { 
		fileCache = new FileCache(context);
		 // 获取当前系统的CPU 数目 
        final int cpuNums = Runtime.getRuntime().availableProcessors();
		executorService = Executors.newFixedThreadPool(cpuNums * POOL_SIZE);
		clearCacheService = Executors.newSingleThreadExecutor();
		res_Id = bg_id;
		clearCacheService.execute(new ClearCacheFile());

	}
	/*
	 * isImageSuperposition  == true 表示图片叠加
	 * */
	public ImageLoader(Context context, int bg_id,boolean isImageSuperposition) {
		this.context =context; 
		
		this.isImageSuperposition = isImageSuperposition;
		fileCache = new FileCache(context);
		 // 获取当前系统的CPU 数目 
        final int cpuNums = Runtime.getRuntime().availableProcessors();
		executorService = Executors.newFixedThreadPool(cpuNums * POOL_SIZE);
		clearCacheService = Executors.newSingleThreadExecutor();
		res_Id = bg_id;
		clearCacheService.execute(new ClearCacheFile());

	}

	 
	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		 // 获取当前系统的CPU 数目 
        final int cpuNums = Runtime.getRuntime().availableProcessors();
		executorService = Executors.newFixedThreadPool(cpuNums * POOL_SIZE);
		clearCacheService = Executors.newSingleThreadExecutor();
		clearCacheService.execute(new ClearCacheFile());
	}

	
	/**
	 * 开启一个单线程用来根据缓存大小来清理最近没有使用过的缓存图片文件	
	 * @author donggx
	 *
	 */
	class ClearCacheFile implements Runnable{

		@Override
		public void run() {
			fileCache.clear();
		}
		
	}
	public void DisplayImage(String url, ImageView imageView  ) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);  
		if (bitmap != null){ 
			 if(isImageSuperposition){
				    Bitmap bitmap1 = ((BitmapDrawable)context.getResources().getDrawable(res_Id)).getBitmap();
				 
				    Bitmap bitmap2 =bitmap; 
			        Drawable[] array = new Drawable[2];
			        array[0] = new BitmapDrawable(bitmap1);
			        array[1] = new BitmapDrawable(bitmap2);
			        LayerDrawable la = new LayerDrawable(array);
			        // 其中第一个参数为层的索引号，后面的四个参数分别为left、top、right和bottom
			     
			        la.setLayerInset(0, 0, 0, 0, 0);
			        la.setLayerInset(1, 3, 3, 3, 3);  
			        imageView.setImageDrawable(la);
			       
			 }else{
					imageView.setImageBitmap(bitmap);
			 } 
		}else {
			queuePhoto(url,imageView,null,0,0);
			imageView.setImageResource(res_Id);
		}
	}
 
	
  
	
	/**
	 * 
	 * @param url
	 * @param imageView
	 * @param bgImageResId   默认图片资源
	 */
	public void DisplayImage(String url, ImageView imageView,int bgImageResId) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){  
			imageView.setImageBitmap(bitmap);
		}else {
			queuePhoto(url,imageView,null,0,bgImageResId);
			imageView.setImageResource(bgImageResId);
		}
	} 
	
	/**
	 * 
	 * @param url
	 * @param imageView
	 * @param progressBar  loading圈
	 * @param widthHightPixels  图片分辨率大小
	 */
	public void DisplayImage(String url,ImageView imageView,ProgressBar progressBar,int widthHightPixels) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
			if(progressBar != null){
				progressBar.setVisibility(View.GONE);
			}
		}else {
			queuePhoto(url,imageView ,progressBar,widthHightPixels,0);
			imageView.setImageResource(res_Id);
		}
	}

	
	/**
	 * 
	 * @param url
	 * @param imageView
	 * @param progressBar  loading圈
	 * @param widthHightPixels  图片分辨率大小
	 * @param bgImageResId  默认图片资源
	 */
	public void DisplayImage(String url,ImageView imageView,ProgressBar progressBar,int widthHightPixels,int bgImageResId ) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
			progressBar.setVisibility(View.GONE);
		}else {
			queuePhoto(url,imageView ,progressBar,widthHightPixels,bgImageResId);
			imageView.setImageResource(bgImageResId);
		}
	}
	
 
	private void queuePhoto(String url, ImageView imageView, ProgressBar progressBar,int widthHightPixels,int bgImageResId) {
		PhotoToLoad p = null;
		if(bgImageResId != 0){
			if(progressBar != null){
				p = new PhotoToLoad(url,imageView,progressBar,widthHightPixels,bgImageResId);
			}else{
				p = new PhotoToLoad(url,imageView,bgImageResId);
			}
		}else{
			if(progressBar != null){
				p = new PhotoToLoad(url,imageView,progressBar,widthHightPixels);
			}else{
				p = new PhotoToLoad(url,imageView);
			}
		}
		executorService.submit(new PhotosLoader(p));
	}
	
	/**
	 * add by zhangshuo用于指定图标消失的情况
	 * @param url
	 * @param widthHightPixels
	 * @param tragethideiv
	 * @return
	 */
//	private Bitmap getBitmap(String url,int widthHightPixels,ImageView tragethideiv) {
//		
//		File f = fileCache.getFile(url);
//
//		Bitmap bitmap = null ;
//		// from SD cache
//		if(f.exists()){
//			bitmap = decodeFile(f,widthHightPixels);
//			if (bitmap != null){
//				if(null!=tragethideiv) {
//					tragethideiv.setVisibility(View.GONE);
//				}
//				return bitmap;
//			}	
//		}
//		// from web loading
//		if(widthHightPixels == 0 ){
//			bitmap = fromWebLoadNormalImage(url, widthHightPixels, f);
//		}else{
//			bitmap = fromWebLoadLargerImage(url, widthHightPixels, f);
//		}
//		if (bitmap != null){
//			if(null!=tragethideiv) {
//				tragethideiv.setVisibility(View.GONE);
//			}
//		}	else {
//			if(null!=tragethideiv) {
//				tragethideiv.setVisibility(View.VISIBLE);
//			}
//		}
//		return bitmap;
//	}
	
	
	private Bitmap getBitmap(String url,int widthHightPixels) {
		
		File f = fileCache.getFile(url);

		Bitmap bitmap = null ;
		// from SD cache
		if(f.exists()){
			bitmap = decodeFile(f,widthHightPixels);
			if (bitmap != null){
				return bitmap;
			}	
		}
		// from web loading
		if(widthHightPixels == 0 ){
			bitmap = fromWebLoadNormalImage(url, widthHightPixels, f);
		}else{
			bitmap = fromWebLoadLargerImage(url, widthHightPixels, f);
		}
		return bitmap;
	}

	/**
	 * 从网络下载大图  加锁为了解决由于多线程导致图片花屏问题
	 * @param url
	 * @param widthHightPixels
	 * @param f
	 * @return
	 */
	private synchronized Bitmap fromWebLoadLargerImage(String url, int widthHightPixels, File f) {
		InputStream is = null;
		OutputStream os = null;
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			LogUtil.i(TAG,"下载图片URL地址 :"+url);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			is = conn.getInputStream();
			if(!f.exists()){
				f.createNewFile();
			}
			os = new FileOutputStream(f);
			CopyStream(is, os);
			bitmap = decodeFile(f,widthHightPixels);
			return bitmap;
		} catch (Exception ex) {
			if(f.exists()){
				f.delete();
			}
			LogUtil.i(TAG,"下载图片失败"+ex.toString()+" 图片URL地址 :"+url);
//			ex.printStackTrace();
			return null;
		} finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	

	/**
	 * 从网络下载一般图片
	 * @param url
	 * @param widthHightPixels
	 * @param f
	 * @return
	 */
	 
	private  Bitmap fromWebLoadNormalImage(String url, int widthHightPixels, File file) {
		InputStream is = null;
		OutputStream os = null;
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			LogUtil.i(TAG,"下载图片URL地址 :"+url);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			is = conn.getInputStream();
			if(!file.exists()){
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			CopyStream(is, os);
			bitmap = decodeFile(file,widthHightPixels);  
			return bitmap;
		} catch (Exception ex) {
			if(file.exists()){
				file.delete();
			}
			LogUtil.i(TAG,"下载图片失败"+ex.toString()+" 图片URL地址 :"+url);
//			ex.printStackTrace();
			return null;
		} finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} 
	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}
		
		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
//			Bitmap bmp  = null;
//			if(isUseOldGetBirmap) {
				Bitmap bmp = getBitmap(photoToLoad.url,photoToLoad.imageWidthHightPixels);
//				LogUtil.e(TAG, "老方法执行了");
//			}else {
//				bmp=	getBitmap(photoToLoad.url,photoToLoad.imageWidthHightPixels, tragerhideig) ;
//				LogUtil.e(TAG, "新方法执行了");
//			}
		
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		} 
		public void run() {
			if(imageViewReused(photoToLoad))
				return;
			if(bitmap != null){ 
				 if(isImageSuperposition){
					    Bitmap bitmap1 = ((BitmapDrawable)context.getResources().getDrawable(res_Id)).getBitmap();
					  
					    Bitmap bitmap2 =bitmap; 
				        Drawable[] array = new Drawable[2];
				        array[0] = new BitmapDrawable(bitmap1);
				        array[1] = new BitmapDrawable(bitmap2);
				        LayerDrawable la = new LayerDrawable(array);
				        // 其中第一个参数为层的索引号，后面的四个参数分别为left、top、right和bottom
				        la.setLayerInset(0, 0, 0, 0, 0);
				        la.setLayerInset(1, 3, 3, 3, 3);   
				        photoToLoad.imageView.setImageDrawable(la);
				 }else{
					 photoToLoad.imageView.setImageBitmap(bitmap);
				 } 
			}else if(photoToLoad.bgImageResId != 0){
				photoToLoad.imageView.setImageResource(photoToLoad.bgImageResId);
			}else if(res_Id != 0){
				photoToLoad.imageView.setImageResource(res_Id);
			}else{
				photoToLoad.imageView.setImageResource(R.color.common_transparent_color);
			}
			
			if(photoToLoad.progressBar!=null){
				photoToLoad.progressBar.setVisibility(View.GONE);
			}
		}
	}

	private void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	
	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f,int widthHightPixels) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FlushedInputStream(new FileInputStream(f)), null, o);
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
				
			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			if(widthHightPixels ==0){
				final int REQUIRED_SIZE = 70;
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}else{
				scale = computeSampleSize(o,-1,widthHightPixels);
			}
//				scale = computeSampleSize(o,-1,width_tmp * height_tmp);
				// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FlushedInputStream(new FileInputStream(f)), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public int computeSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
		int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);  
		int roundedSize;  
		if (initialSize <= 8 ) {  
			roundedSize = 1;  
		    while (roundedSize < initialSize) {  
		        roundedSize <<= 1;  
		    }  
		} else {  
		    roundedSize = (initialSize + 7) / 8 * 8;  
		}  
		 return roundedSize;  
	}  
		  
	private int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
		double w = options.outWidth;  
		double h = options.outHeight;  
		  
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),Math.floor(h / minSideLength));  
		  
		if(upperBound < lowerBound) {  
		   // return the larger one when there is no overlapping zone.  
		   return lowerBound;  
		}  
		  
		if((maxNumOfPixels == -1) && (minSideLength == -1)) {  
			return 1;  
		}else if (minSideLength == -1) {  
		    return lowerBound;  
		} else {  
		    return upperBound;  
		}  
	}  
	
}
