//package cn.hi321.android.media.utils;
//
//import java.lang.Thread.UncaughtExceptionHandler;
//
//import android.app.AlarmManager;
//import android.app.Application;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//
//public class ApplicationActivity extends Application {  
////    PendingIntent restartIntent;  
//  
//    @Override  
//    public void onCreate() {  
//        super.onCreate();  
//  
////        // 以下用来捕获程序崩溃异常  
////        Intent intent = new Intent();  
////        // 参数1：包名，参数2：程序入口的activity  
////        intent.setClassName("com.hk.shop", "com.hk.shop.WelcomeActivity");  
////        restartIntent = PendingIntent.getActivity(getApplicationContext(), 0,  
////                intent, Intent.FLAG_ACTIVITY_NEW_TASK);  
//        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程  
//    }  
//  
//    public UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {  
//        @Override  
//        public void uncaughtException(Thread thread, Throwable ex) {  
////            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);  
////            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,  
////                    restartIntent); // 1秒钟后重启应用  
////            ActivityContrl.finishProgram(); // 自定义方法，关闭当前打开的所有avtivity  
// 
//			Intent i = getBaseContext().getPackageManager() 
//			.getLaunchIntentForPackage(getBaseContext().getPackageName()); 
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
//			startActivity(i);
//        }  
//    };  
//}  