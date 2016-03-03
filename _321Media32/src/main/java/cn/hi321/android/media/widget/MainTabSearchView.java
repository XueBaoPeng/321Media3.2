//package cn.hi321.android.media.widget; 
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.ViewPager;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.Toast;
//import cn.hi321.android.media.R;
//import cn.hi321.android.media.adapter.MovieAdapter;
//import cn.hi321.android.media.adapter.ViewPagerAdapter;
//import cn.hi321.android.media.entity.HomeResponse;
//import cn.hi321.android.media.entity.VideoInfo;
//import cn.hi321.android.media.http.DataMode;
//import cn.hi321.android.media.http.NetWorkTask;
//import cn.hi321.android.media.ui.MediaActivity;
////import cn.hi321.android.media.utils.ShowPlay;
//import cn.hi321.android.media.utils.UIUtils;
//import cn.hi321.android.media.utils.Utils;
//
//public class MainTabSearchView extends LinearLayout
//{
//
//  private Context context;
//  private LayoutInflater flater;
//
//  private ViewPagerAdapter paperAdapter;
//  private EditText editSearch;
//  private ViewPager viewPager;
//  private ImageView searchButton;
//  private ListView searchListview;
//  private String path;
//  private HomeResponse homeRespone;
//  private ArrayList<VideoInfo> movieVideo;
//  private MovieAdapter movieAdapter  ;	
//  private String mySearchText = "";
//  ImageView iamge;
//  
//  public MainTabSearchView(Context paramContext)
//  {
//    this(paramContext, null);
//  }
//
//  public MainTabSearchView(Context paramContext, AttributeSet paramAttributeSet)
//  {
//    super(paramContext, paramAttributeSet);
//    this.context = paramContext;
//    setOrientation(1);
//    LayoutInflater localLayoutInflater = UIUtils.getLayoutInflater(paramContext);
//    this.flater = localLayoutInflater;
//    View localView = this.flater.inflate(R.layout.view_main_tab_search, this);
//    editSearch = (EditText)localView.findViewById(R.id.edit_search);
//    searchButton = (ImageView)localView.findViewById(R.id.searchButton);
//    searchListview =(ListView)localView.findViewById(R.id.view_main_tab_my_listview);
//    iamge =(ImageView)findViewById(R.id.iamge);
//    iamge.setVisibility(View.VISIBLE);
//    homeRespone = new HomeResponse();
//    movieVideo = new ArrayList<VideoInfo>();  
//    editSearch.addTextChangedListener(new TextWatcher() {
//		
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//	 
//			if (s.length() != 0) {
//				mySearchText = s.toString();
//			} else { 
//				
//			} 	
//		}
//		
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) { 
//			
//		}
//		
//		public void afterTextChanged(Editable s) { 
//		}
//	});
//    searchButton.setOnClickListener(new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			try {
//				
//				if(mySearchText !=null &&  !mySearchText.equals("")){
//					  mySearchText  = URLDecoder.decode(mySearchText, "utf-8");
//					  path ="http://4.doukan.sinaapp.com/api/?"+"q="+mySearchText;
//					   
//				      if(UIUtils.hasNetwork(context)){
//				    	  Utils.startWaitingDialog(context);
//				    	  ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask();  
//					      asyncTask.execute(); 
//			  		  }else{
//			  			UIUtils.showToast(context,context.getText(R.string.tip_network).toString());
//					 }
//				} 
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//	});
//	
//  } 
//  
//  
//  class ProgressBarAsyncTask extends AsyncTask<Integer, Integer, Object> { 
//		/**
//		 * 这里的Integer参数对应AsyncTask中的第一个参数 
//		 * 这里的String返回值对应AsyncTask的第三个参数
//		 * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
//		 * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
//		 */
//		protected Object doInBackground(Integer... params) {
//			DataMode dataMode = new DataMode(context);  
//			return dataMode.getMediaData(path, homeRespone, mainHandler);
//		}
//
//
//		/**
//		 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
//		 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
//		 */
//		protected void onPostExecute(Object result) {  
////				mainHandler.sendEmptyMessage(UIUtils.StopDialog);
//				if(result !=null){
//				    homeRespone = (HomeResponse)result;
//					movieVideo = homeRespone.getResult();
////					Toast.makeText(context, "movieVideo == "+movieVideo.size(), 1).show();
//					movieAdapter = new MovieAdapter(context, movieVideo,myHandler); 
//					searchListview.setAdapter(movieAdapter);  
//				}
//				 iamge.setVisibility(View.GONE);
//				Utils.closeWaitingDialog();
//		}
//
//
//		//该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
//		protected void onPreExecute() {  
////			mainHandler.sendEmptyMessage(UIUtils.StrDialog);
//		} 
//		protected void onProgressUpdate(Integer... values) {
//			int vlaue = values[0]; 
//		} 
//
//	}
//  
//  private Handler myHandler = new Handler(){
//
//	@Override
//	public void handleMessage(Message msg) {
//		// TODO Auto-generated method stub
//		super.handleMessage(msg);
//		
//		switch (msg.what) {
//  		case UIUtils.SHOW_PLAY:
////	  			String urls =(String)msg.obj;
////			if(urls!=null){
////				ShowPlay showPlay = new ShowPlay(context, urls);
////			}
//	  			break;
//
//		default:
//			break;
//		}
//	}
//	  
//  };
//  
//}
