package cn.hi321.android.media.local;


import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.android.media.entity.MediaItem;
import cn.hi321.android.media.local.DisplayListAdapter.ViewHolder;
import cn.hi321.android.media.player.SystemPlayer;
import cn.hi321.android.media.ui.BaseActivity;
import cn.hi321.android.media.utils.UIUtils;
import cn.hi321.android.media.utils.UserPreference;
import cn.hi321.android.media.widget.MainTitlebar;
import cn.waps.AppConnect;

import com.android.china.R;
//import cn.hi321.android.media.widget.MyListView;


public class PlayListsActivity extends BaseActivity implements
		ListView.OnItemClickListener, ListView.OnItemLongClickListener,
		ListView.OnScrollListener {

	private static final String TAG = "PlayListsActivity";
	private static final String CALLER_VIDEOPLAYER = "VIDEOPLAYER";
	private static final String CALLER_MMS = "MMS";
	private static final String CALLER_CAMERA = "CAMERA";
	private static final String CALLER_WATCHMSG = "WATCHMSG";
	private static final int PROCESS_DIALOG_START_KEY = 0;
	private static final int PROCESS_MEDIA_SCANNING_KEY = 1;
	private static final int INTENT_TERMINATED = 0;

	/**
	 * change camera video sql path 2010.11.03 add by W.Y
	 */
	// private static final long CAMERAFOLDER_USERDATA_BUCKET_ID = 1712717414;
	private static final String CAMERAFOLDER_SDCARD_PATH = "/mnt/sdcard/Camera/Videos";

	private enum ListEnum {
		NormalVideo, CameraVideo
	};

	private static final String NORMAL_VIDEO = "NORMAL_VIDEO";
	private static final String CAMERA_VIDEO = "CAMERA_VIDEO";
	private static final int TAB_INDEX_NORMAL_VIDEO = 0;
	private static final int TAB_INDEX_CAMERA_VIDEO = 1;
	private static final int LIST_STATE_IDLE = 0;
	private static final int LIST_STATE_BUSY = 1;
	private static final int LIST_STATE_REQUEST_REFRESH = 2;
	private static final int SORT_LIST_BY_DATE = 0;
	private static final int SORT_LIST_BY_TITLE = 1;
	private static final int APPSTATE_FIRST_START = 0;
	private static final int APPSTATE_INITIALIZED = 1;
	private static final int APPSTATE_FINISHED = 2;

	public class VideoWorkItem {
		public VideoObject object;
		public long dataModified = 0;
		public String datapath;
		public String name;
		public String duration;
		public String size;
		public boolean isHighlight = false;
		public int lastPos = 0;
	}

	public class ListLastPosition {
		public int normalVideo = 0;
		public int cameraVideo = 0;
	}

	private int mAppState;
	private boolean mRequest_stop_thread;
	private boolean mIsVideoPlaying;
	private boolean mFinishScanning;
	private int mCurrentListState;
	private String mCaller;
	private ListLastPosition listLastPosition = new ListLastPosition();
	// private ManagePreference mManagePreference = new ManagePreference();
	private VideoWorkItem mLastPlayedItem;
	private ListView listview;
	private DisplayListAdapter mListAdapter;
	// private BottomTabHost tabHost;
	private static Display mDisplay;
	private EditText mRenameEditText;
	private AlertDialog mListOperationDialog;
	private AlertDialog mCurrrentActiveDialog;
	private VideoList mAllImages;
	private List<VideoWorkItem> mAllVideoList = new ArrayList<VideoWorkItem>();
	private List<VideoWorkItem> mNormalVideoList = new ArrayList<VideoWorkItem>();
	private List<VideoWorkItem> mCameraList = new ArrayList<VideoWorkItem>();
	private List<VideoWorkItem> mActiveList;
	private ArrayList<MediaItem> mCurrentPlayList;
	private RelativeLayout layout;
	/** hejn, optimizing thumbnail list, 20101210 begin */
	private Hashtable<Integer, Bitmap> mThumbHash = new Hashtable<Integer, Bitmap>();
	private Bitmap mDefaultBitmap;
	private RelativeLayout laytout_beij;
	/** hejn, optimizing thumbnail list, 20101210 end */

	private Thread mLoadingThread = null;

	public LayoutInflater getLayoutInflater() {
		return getWindow().getLayoutInflater();
	}
	private ImageView noSdcard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.play_list_activity);
		((MainTitlebar)findViewById(R.id.main_title)).show("本地视频");
		initialize(); 
		if (isSDcardEjected()) {
			// showDialog(PROCESS_DIALOG_START_KEY);
			mLoadingThread = createLoadingThread();
			mLoadingThread.start();
		}
	

	}

	private View convertView;
	private TextView mNoFileTextView;

	private void initialize() {
		Log.v(TAG, "VideoPlayerActivity  initialize");
		
		mAppState = APPSTATE_FIRST_START;
		mCaller = CALLER_VIDEOPLAYER;
		mIsVideoPlaying = false;
		mFinishScanning = false;
  
		// tabHost = getTabHost();
		// LayoutInflater.from(this).inflate(R.layout.main,
		// null, true);
//		setContentView(R.layout.play_list_activity);
		
//		 DisplayMetrics  dm = new DisplayMetrics();
//	       getWindowManager().getDefaultDisplay().getMetrics(dm);  
//	       int screenHeight = dm.heightPixels;
//	        RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.layout);
//		    ImpressionAdView ad = new ImpressionAdView(this, layout, 0, screenHeight - 30, 0xFFFFFFFF, false, null);
//			ad.show(40);
//			ad.setAdListener(this); 
		
		// convertView = mInflater.inflate(R.layout.main,
		// null);
		// tabHost.setOnTabChangedListener(this);
		mNoFileTextView = (TextView) this.findViewById(R.id.playListView);
		listview = (ListView) this.findViewById(R.id.list);
		noSdcard = (ImageView) findViewById(R.id.icon_nocard);
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
		listview.setOnScrollListener(this);

		// mManagePreference.initialize(VideoPlayerActivity.this);
		mDisplay = getWindow().getWindowManager().getDefaultDisplay();

		String caller = getIntent().getStringExtra("Caller");
		if (caller != null) {
			mCaller = caller;
		}

		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		iFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		iFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		iFilter.addDataScheme("file");
		registerReceiver(mBroadcastReceiver, iFilter);
		/** hejn, optimizing thumbnail list, 20101210 begin */
		mThumbHash.clear();
		mDefaultBitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.bg_default);
		/** hejn, optimizing thumbnail list, 20101210 end */
		// createTab();
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		boolean mountState = false;

		@Override
		public void onReceive(Context context, Intent intent) {
			if (mAppState == APPSTATE_FINISHED) {
				return;
			}
			String action = intent.getAction();
			Log.v(TAG, "BroadcastReceiver action : " + action);
			// action.equals(Intent.ACTION_MEDIA_MOUNTED)

			if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
				if (!mountState) {
					Log.v(TAG, "BroadcastReceiver sdcard ejected/mounted");
					if (mAppState == APPSTATE_INITIALIZED) {
						uninitialize();
					}
					mountState = true;
				}
			} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
				Log.v(TAG, "BroadcastReceiver start scan media");
				// if (mountState) {
				// showDialog(PROCESS_DIALOG_SCAN_KEY);
				// }
			} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
				if (isSDcardEjected() && mAppState != APPSTATE_FINISHED) {
					Log.v(TAG, "BroadcastReceiver stop scan media");
					if (mAppState == APPSTATE_FIRST_START) {
						showDialog(PROCESS_DIALOG_START_KEY);
						createLoadingThread().start();
					} else {
						removeDialog(PROCESS_MEDIA_SCANNING_KEY);
						refreshLastest(true);
					}
					mountState = false;
					mFinishScanning = true;
				}
			}
		}
	};
	private static final int REFRESH = 1;
	private Handler mRefreshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == REFRESH) {
				Log
						.d(TAG,
								"handleMessage()===============receive REFRESH message+++++++++++");
				// if (mSongsAdapter != null) {
				// mSongsAdapter.notifyDataSetChanged();
				// }
				// refresh();
				showEmptyView();
			}
		}
	};

	public void showEmptyView() {
		if (mAllImages != null) {
			int totalNum = mAllImages.getCount();
			if (totalNum == 0) {
				setEmptyView(true);
			} else {
				setEmptyView(false);
			}
		}

	}

	public void refreshLastest(boolean isRefreshData) {
		if (isRefreshData) {
			getVideoData();
		}
		if (mActiveList == mNormalVideoList) {
			refreshList(ListEnum.NormalVideo);
		} else if (mActiveList == mCameraList) {
			refreshList(ListEnum.CameraVideo);
		}
		if (isRefreshData) {
			//modify by yangguangfu
//			Toast.makeText(this, getString(R.string.list_refresh), 1500).show();
		}
	}

	private void refreshList(ListEnum list) {
		int lastPos = listview.getFirstVisiblePosition();

		if (mActiveList == mNormalVideoList) {
			listLastPosition.normalVideo = lastPos;
		} else if (mActiveList == mCameraList) {
			listLastPosition.cameraVideo = lastPos;
		}
		if (list.equals(ListEnum.NormalVideo)) {
			mActiveList = mNormalVideoList;
			lastPos = listLastPosition.normalVideo;
		} else if (list.equals(ListEnum.CameraVideo)) {
			mActiveList = mCameraList;
			lastPos = listLastPosition.cameraVideo;
		}

		if (mListAdapter != null) {
			mListAdapter.destory();
		}

		mListAdapter = new DisplayListAdapter(this);
		/** hejn, optimizing thumbnail list, 20101210 begin */
		mListAdapter.setThumbHashtable(mThumbHash, mDefaultBitmap);
		/** hejn, optimizing thumbnail list, 20101210 end */
		mListAdapter.setListItems(mActiveList);

		listview.setAdapter(mListAdapter);
		listview.setSelection(lastPos);

		mCurrentListState = LIST_STATE_REQUEST_REFRESH;
	}

	private VideoList allImages() {
		mAllImages = null;
		return ImageManager.instance().allImages(this, getContentResolver(),
				ImageManager.INCLUDE_VIDEOS, ImageManager.SORT_ASCENDING);
	}

	public void getVideoData() {
		Log.v(TAG, "getVideoData()");

		mAllVideoList.clear();
		mNormalVideoList.clear();
		mCameraList.clear();

		mAllImages = allImages(); // Video List

		if (mAllImages != null) {
			int totalNum = mAllImages.getCount();
			for (int i = 0; i < totalNum; i++) {
				VideoObject image = mAllImages.getImageAt(i);

				VideoWorkItem videoDisplayObj = new VideoWorkItem();
				videoDisplayObj.object = image;
				videoDisplayObj.name = image.getTitle();
				videoDisplayObj.duration = getString(R.string.duration_tag)
						+ " " + image.getDuration();
				videoDisplayObj.size = image.getSize();
				videoDisplayObj.datapath = image.getMediapath();

				long bucketId = image.getBucketId();

				if (PublicTools.getBucketId(CAMERAFOLDER_SDCARD_PATH) == bucketId) {
					videoDisplayObj.dataModified = image.getDateModified();
					mCameraList.add(videoDisplayObj);
				} else {
					mNormalVideoList.add(videoDisplayObj);
				}


				mAllVideoList.add(videoDisplayObj);
			}
			mRefreshHandler.sendEmptyMessage(REFRESH);
			// sortList(mNormalVideoList, SORT_LIST_BY_TITLE);
			// sortList(mCameraList, SORT_LIST_BY_DATE);

			Log.v(TAG, "LoadDataThread  totalNum : " + totalNum);
		}
	}

	public String getEmptyString() {
		return getResources().getString(R.string.no_vide_file);
	}

	public void setEmptyView(boolean show) {
		if (show) {
			mNoFileTextView.setText(getEmptyString());
			mNoFileTextView.setVisibility(View.VISIBLE);
		} else {
			mNoFileTextView.setText(getEmptyString());
			mNoFileTextView.setVisibility(View.GONE);
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.v(TAG, "LoadDataThread  handleMessage APPSTATE_FIRST_START");
			mAppState = APPSTATE_INITIALIZED;

			if (mCaller.equals(CALLER_CAMERA)) {
				mActiveList = mCameraList;
				// setTitle(getString(R.string.cameravideo_list));
				// tabHost.setCurrentTab(TAB_INDEX_CAMERA_VIDEO);
			} else {
				mActiveList = mNormalVideoList;
				// setTitle(getString(R.string.allvideo_list));
				// tabHost.setCurrentTab(TAB_INDEX_NORMAL_VIDEO);
				refreshLastest(false);
			}
			removeDialog(PROCESS_DIALOG_START_KEY);
			checkListScanning();
		}
	};

	public void checkListScanning() {
		if (PublicTools.isMediaScannerScanning(getContentResolver())
				&& !mFinishScanning) {
			showDialog(PROCESS_MEDIA_SCANNING_KEY);
		}
	}

	private Thread createLoadingThread() {
		return new Thread(new Runnable() {
			private static final int STATE_STOP = 0;
			private static final int STATE_IDLE = 1;
			private static final int STATE_TERMINATE = 2;
			private int workStatus;
			private int currentPos;
			private int maxPos;
			private Object[] items;

			public void run() {
				Log.v(TAG, "LoadDataThread  run");
				mRequest_stop_thread = false;

				getVideoData();
				mHandler.sendMessage(mHandler.obtainMessage());

				init();
				loadThumbnails();
			}

			private void init() {
				mCurrentListState = LIST_STATE_IDLE;
				workStatus = STATE_STOP;

				items = mAllVideoList.toArray();
				maxPos = items.length;
				currentPos = 0;

				Log.v("LoadDataThread", "maxPos : " + maxPos);
			}

			private void loadThumbnails() {
				while (workStatus != STATE_TERMINATE) {
					switch (workStatus) {
					case STATE_STOP:
						workStatus = onStop();
						break;
					case STATE_IDLE:
						workStatus = onIdle();
						break;
					default:
						break;
					}
				}
				Log.v("LoadDataThread", "STATE_TERMINATE!!!");
			}

			private int onIdle() {
				Log.v(TAG, "createLoadingThread : onIdle");

				while (true) {
					if (mRequest_stop_thread || (currentPos == maxPos)) {
						return STATE_TERMINATE;
					}
					if (mCurrentListState == LIST_STATE_REQUEST_REFRESH) {
						mCurrentListState = LIST_STATE_IDLE;
						return STATE_STOP;
					}

					PublicTools.sleep(PublicTools.LONG_INTERVAL);
				}
			}

			private int onStop() {
				if (mRequest_stop_thread) {
					return STATE_TERMINATE;
				}
				if (mActiveList == null || listview == null) {
					PublicTools.sleep(PublicTools.SHORT_INTERVAL);
					return STATE_STOP;
				}
				if (mActiveList.isEmpty()) {
					return STATE_IDLE;
				}
				if (-1 == listview.getLastVisiblePosition()) {
					PublicTools.sleep(PublicTools.SHORT_INTERVAL);
					return STATE_STOP;
				}

				Log.v(TAG, "createLoadingThread : onStop");

				Object[] viewHolders = mListAdapter.getHolderObjects();
				int count = viewHolders.length;
				for (int i = 0; i < count; i++) {
					if (mCurrentListState == LIST_STATE_BUSY) {
						return STATE_IDLE;
					} else if (mCurrentListState == LIST_STATE_REQUEST_REFRESH) {
						mCurrentListState = LIST_STATE_IDLE;
						return STATE_STOP;
					}
					RefreshThumbnail((ViewHolder) viewHolders[i]);
					PublicTools.sleep(PublicTools.MINI_INTERVAL);
				}

				PublicTools.sleep(PublicTools.MIDDLE_INTERVAL);

				if (count < mListAdapter.getHolderObjects().length) {
					return STATE_STOP;
				}
				if (mCurrentListState == LIST_STATE_IDLE) {
					return STATE_IDLE;
				} else {
					mCurrentListState = LIST_STATE_IDLE;
					return STATE_STOP;
				}
			}

			private void RefreshThumbnail(ViewHolder holder) {
				if (holder == null) {
					return;
				}
				if (!holder.mUseDefault
						|| holder.mItem == null
						|| PublicTools.THUMBNAIL_CORRUPTED == holder.mItem.object
								.getThumbnailState()) {
					return;
				}
				/** hejn, optimizing thumbnail list, 20101210 begin */
				holder.mBitmap = holder.mItem.object.miniThumbBitmap(false,
						mThumbHash, mDefaultBitmap);
				/** hejn, optimizing thumbnail list, 20101210 end */
				if (PublicTools.THUMBNAIL_PREPARED == holder.mItem.object
						.getThumbnailState()) {
					mListAdapter.sendRefreshMessage(holder);
					holder.mUseDefault = false;
				} else {
					holder.mUseDefault = true;
				}
			}
		});
	}

	public static class PublicTools {
		public static final int THUMBNAIL_PREPARED = 1;
		public static final int THUMBNAIL_EMPTY = 0;
		public static final int THUMBNAIL_CORRUPTED = -1;
		public static final int MINI_INTERVAL = 50;
		public static final int SHORT_INTERVAL = 150;
		public static final int MIDDLE_INTERVAL = 300;
		public static final int LONG_INTERVAL = 600;
		public static final int LONG_LONG_INTERVAL = 6000;
		private static final int FILENAMELENGTH = 80;

		public static long getBucketId(String path) {
			return path.toLowerCase().hashCode();
		}

		public static String cutString(String origin, int length) {
			char[] c = origin.toCharArray();
			int len = 0;
			int strEnd = 0;
			for (int i = 0; i < c.length; i++) {
				strEnd++;
				len = (c[i] / 0x80 == 0) ? (len + 1) : (len + 2);
				if (len > length || (len == length && i != (c.length - 1))) {
					origin = origin.substring(0, strEnd) + "...";
					break;
				}
			}
			return origin;
		}

		public static String replaceFilename(String filepath, String name) {
			String newPath = "";
			int lastSlash = filepath.lastIndexOf('/');
			if (lastSlash >= 0) {
				lastSlash++;
				if (lastSlash < filepath.length()) {
					newPath = filepath.substring(0, lastSlash);
				}
			}
			newPath = newPath + name;
			int lastDot = filepath.lastIndexOf('.');
			if (lastDot > 0) {
				newPath = newPath
						+ filepath.substring(lastDot, filepath.length());
			}
			return newPath;
		}

		public static boolean isFilenameIllegal(String filename) {
			return (filename.length() <= FILENAMELENGTH);
		}

		public static boolean isLandscape() {
			// Log.v(TAG,"isLandscape : "+ mDisplay.getOrientation());
			return (1 == mDisplay.getOrientation());
		}

		public static boolean isFileExist(String filepath) {
			File file = new File(filepath);
			return file.exists();
		}

		public static void sleep(int interval) {
			try {
				Thread.sleep(interval);
			} catch (Exception e) {
			}
		}

		public static AlertDialog hint(Context context, int StringId) {
			return new AlertDialog.Builder(context).setMessage(
					context.getString(StringId)).setNeutralButton(
					R.string.button_ok, null).show();
		}

		public static Cursor query(ContentResolver resolver, Uri uri,
				String[] projection, String selection, String[] selectionArgs,
				String sortOrder) {
			try {
				if (resolver == null) {
					return null;
				}
				return resolver.query(uri, projection, selection,
						selectionArgs, sortOrder);
			} catch (UnsupportedOperationException ex) {
				return null;
			}
		}

		public static boolean isMediaScannerScanning(ContentResolver cr) {
			boolean result = false;
			Cursor cursor = query(cr, MediaStore.getMediaScannerUri(),
					new String[] { MediaStore.MEDIA_SCANNER_VOLUME }, null,
					null, null);
			if (cursor != null) {
				if (cursor.getCount() == 1) {
					cursor.moveToFirst();
					result = "external".equals(cursor.getString(0));
				}
				cursor.close();
			}

			return result;
		}

		public static boolean isVideoStreaming(Uri uri) {
			return ("http".equalsIgnoreCase(uri.getScheme()) || "rtsp"
					.equalsIgnoreCase(uri.getScheme()));
		}
	}

	// help functions
	private void uninitialize() {
		Log.v(TAG, "uninitialize");
		Toast.makeText(this, getString(R.string.sd_shared), 1500).show();
		if (mAllImages != null) {
			mAllImages.onDestory();
		}
		if (mCurrrentActiveDialog != null) {
			if (mCurrrentActiveDialog.isShowing()) {
				mCurrrentActiveDialog.dismiss();
			}
		}
		listLastPosition.cameraVideo = 0;
		listLastPosition.normalVideo = 0;
		mAllImages = null;
		mAllVideoList.clear();
		mNormalVideoList.clear();
		mCameraList.clear();
		if (mCurrentPlayList != null)
			mCurrentPlayList.clear();
		if (mLastPlayedItem != null) {
			mLastPlayedItem.object = null;
			mLastPlayedItem.isHighlight = false;
			mLastPlayedItem.lastPos = 0;
		}
		refreshLastest(false);
	}

	private boolean isSDcardEjected() {
		boolean isSdcard_ok = false;
		String status = Environment.getExternalStorageState();
		Log.v(TAG, "status : " + status
				+ status.equals(Environment.MEDIA_REMOVED));

		if (status.equals(Environment.MEDIA_MOUNTED)) {
			isSdcard_ok = true;
			return true;
		}


		if (!isSdcard_ok) {
			if(noSdcard!=null)
			noSdcard.setVisibility(View.VISIBLE);
			if (status.equals(Environment.MEDIA_UNMOUNTED)) {
				Toast.makeText(this, getString(R.string.sd_unmounted), 1500)
						.show();
			} else if (status.equals(Environment.MEDIA_SHARED)) {
				Toast.makeText(this, getString(R.string.sd_shared), 1500)
						.show();
			} else if (status.equals(Environment.MEDIA_REMOVED)) {
				Toast.makeText(this, getString(R.string.sd_removed), 1500)
						.show();
			} else {
				Toast.makeText(this, getString(R.string.sd_noinsert), 1500)
						.show();
			}
		}else{
			noSdcard.setVisibility(View.GONE);
		}

		return isSdcard_ok;
	}


	@Override
	protected void onDestroy() {
		Log.v(TAG, "call onDestroy");

		mRequest_stop_thread = true;
		mAppState = APPSTATE_FINISHED;

		if (mListAdapter != null) {
			mListAdapter.destory();
		}
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
			mBroadcastReceiver = null;
		}
		if (mAllImages != null) {
			mAllImages.onDestory();
		}
		/** hejn, optimizing thumbnail list, 20101210 begin */
		Enumeration<Bitmap> e = mThumbHash.elements();
		while (e.hasMoreElements()) {
			Bitmap tmp = e.nextElement();
			if (!tmp.isRecycled()) {
				tmp.recycle();
			}
		}
		mThumbHash.clear();
		/** hejn, optimizing thumbnail list, 20101210 end */
		if (mLoadingThread != null) {
			// mLoadingThread.stop();
		}
		super.onDestroy();
	}
	
//	 public boolean onKeyDown(int keyCode, KeyEvent event) {
//		    super.onKeyDown(keyCode, event);
//			if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0 ) {
//				finish();
//				overridePendingTransition(R.anim.fade, R.anim.hold);
//				return true;
//			}
//			return false;
//		}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		if(this.isFinishing()) 
//		setImageView02(R.drawable.changefile_normal);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub

		super.onRestart();
		Log.v(TAG, "onRestart()");
	} 

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.v(TAG, "onSaveInstanceState()");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart(); 
	}

	@Override
	protected void onStop() {
//		finish();
		super.onStop();
		Log.v(TAG, "onStop()");
	}

	private void showExternalCallerOperationDialog(final int position) {
		String name = mActiveList.get(position).name;

		mListOperationDialog = new AlertDialog.Builder(PlayListsActivity.this)
				.setTitle(name).setItems(R.array.items_for_externalCaller,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								/* Play */
								case 0:
									startActivity(position);
									break;
								/* setActivityResult */
								case 1:
									setActivityResult(position);
									break;
								default:
									break;
								}
							}

						}).create();

		mCurrrentActiveDialog = mListOperationDialog;
		mListOperationDialog.show();
		mRefreshHandler.sendEmptyMessage(REFRESH);
	}

	private void setActivityResult(final int position) {
		if (!PublicTools.isFileExist(mActiveList.get(position).datapath)) {
			PublicTools.hint(PlayListsActivity.this, R.string.file_notexist);
			return;
		}

		mCurrrentActiveDialog = new AlertDialog.Builder(this).setMessage(
				getString(R.string.confirm_addvideo)).setPositiveButton(
				R.string.button_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						Uri uri = ContentUris.withAppendedId(
								MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
								mActiveList.get(position).object.getMediaId());
						intent.setData(uri);
						setResult(Activity.RESULT_OK, intent);
						//modify by yangguangfu
//						finish();
					}
				}).setNegativeButton(R.string.button_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
		mRefreshHandler.sendEmptyMessage(REFRESH);
	}
	private long lastTimeonItemClick;
	private long lastTimeonItemLongClick;
	private static long CLICK_INTERVAL = 800;
	 
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		long time = System.currentTimeMillis();
		if (time - lastTimeonItemClick < CLICK_INTERVAL) {
			return ;
		}
		lastTimeonItemClick = time;
		Log.v(TAG, "onItemClick position  = " + position);
		if (mCaller.equals(CALLER_VIDEOPLAYER) || mCaller.equals(CALLER_CAMERA)) {
			startActivity(position);
		} else if (mCaller.equals(CALLER_MMS)
				|| mCaller.equals(CALLER_WATCHMSG)) {
			showExternalCallerOperationDialog(position);
		}
	}

	private void setPlayList() {
		if (mCurrentPlayList != null){
			mCurrentPlayList.clear();
		}
		
		mCurrentPlayList = new ArrayList<MediaItem>();
		int i = 0;
		while (i < mActiveList.size()) {
//			String path = ((VideoWorkItem) mActiveList.get(i)).name;
			MediaItem info = new MediaItem();
			info.setTitle(((VideoWorkItem) mActiveList.get(i)).name);
			info.setUrl(((VideoWorkItem) mActiveList.get(i)).datapath);
			mCurrentPlayList.add(info);
			// Log.v(TAG, "video id : " + idList[i]);
			i++;
		}
	}
	private void deleteNoFileVideo(final int position) {
		Log.v(TAG, "deleteVideo  :   " + position);

		mCurrrentActiveDialog = new AlertDialog.Builder(this).setMessage(
				getString(R.string.confirm_deletefile_isnofile)).setPositiveButton(
				R.string.button_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						VideoWorkItem item = mActiveList.get(position);
						VideoObject object = item.object;

						if (item.datapath.equals(object.getMediapath())) {
							if (mAllImages.removeImage(object)) {
								mAllVideoList.set(mAllVideoList.indexOf(item),
										null);
								mActiveList.remove(item);
								refreshLastest(false);
								mRefreshHandler.sendEmptyMessage(REFRESH);
								return;
							}
						}
						PublicTools.hint(PlayListsActivity.this,
								R.string.fail_deletefile);
					}
				})/*.setNegativeButton(R.string.button_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})*/.show();
		mRefreshHandler.sendEmptyMessage(REFRESH);
	}

	private void startActivity(int position) {
		if (!mIsVideoPlaying) {
			if (!PublicTools.isFileExist(mActiveList.get(position).datapath)) {
//				PublicTools
//						.hint(PlayListsActivity.this, R.string.file_notexist);
				deleteNoFileVideo(position);
				return;
			}
			mCurrentListState = LIST_STATE_BUSY;
//			  Intent i = new Intent(PlayListsActivity.this, MediaPlaybackService.class);
//	          i.setAction(MediaPlaybackService.SERVICECMD);
//	          i.putExtra(MediaPlaybackService.CMDNAME, MediaPlaybackService.CMDPAUSE);
//	          startService(i);
			
			//modify by yangguangfu
//			mIsVideoPlaying = true;
			setPlayList();
//			Intent intent = new Intent(Intent.ACTION_VIEW);
			Intent intent = new Intent(PlayListsActivity.this,SystemPlayer.class);
//			if (mActiveList.get(position) == mLastPlayedItem) {
//				intent.putExtra("LastPosition", mLastPlayedItem.lastPos);
//			}
			//modify by yangguangfu
//			intent.putExtra("Caller", CALLER_VIDEOPLAYER);
			
//			intent.putExtra("CurrentPosInMediaIdList", position);
//			intent.putStringArrayListExtra("MediaIdList", mCurrentPlayList);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("MediaIdList", mCurrentPlayList);
			intent.putExtras(mBundle);
			intent.putExtra("CurrentPosInMediaIdList", position);
//			String uri = mCurrentPlayList.get(position).getUrl();
//			intent.putExtra("localuri", uri);
//			intent.setClass(this, ChinaVideoPlayer.class);
//			startActivityForResult(intent, INTENT_TERMINATED);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			mRefreshHandler.sendEmptyMessage(REFRESH);
		}
	}

 
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Log.v(TAG, "onItemLongClick position =" + position);
		long time = System.currentTimeMillis();
		if (time - lastTimeonItemLongClick < CLICK_INTERVAL) {
			return true ;
		}
		lastTimeonItemLongClick = time;

		if (mCaller.equals(CALLER_VIDEOPLAYER) || mCaller.equals(CALLER_CAMERA)) {
			showUserOperationDialog(position);
		}
		mRefreshHandler.sendEmptyMessage(REFRESH);
		return true;
	}

	private void showRenameDialog(final int position) {
		LayoutInflater factory = LayoutInflater.from(PlayListsActivity.this);
		View renameView = factory.inflate(R.layout.rename_alert_dialog, null);
		mRenameEditText = (EditText) renameView.findViewById(R.id.renameEdit);
		mRenameEditText.setText(mActiveList.get(position).name);
		mRenameEditText.setSelectAllOnFocus(true);

		final String oldItemPath = mActiveList.get(position).object
				.getMediapath();

		Log.v(TAG, "showRenameDialog pos : " + position + " path :"
				+ mActiveList.get(position).object.getMediapath());

		OnClickListener renameConfirmListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String modName = mRenameEditText.getText().toString();
				VideoWorkItem item = mActiveList.get(position);

				Log.i(TAG, "showRenameDialog => oldname modname :  "
						+ item.name + " " + modName);

				if (!PublicTools.isFileExist(item.datapath)) {
					PublicTools.hint(PlayListsActivity.this,
							R.string.file_notexist);
					return;
				}

				if (item.name.equals(modName) || modName == null)
					return;

				if (modName.length() == 0 || modName.trim().length() == 0) {
					PublicTools.hint(PlayListsActivity.this,
							R.string.fail_renameempty);
					return;
				}

				if (!PublicTools.isFilenameIllegal(modName)) {
					PublicTools.hint(PlayListsActivity.this,
							R.string.fail_renametoolong);
					return;
				}

				if (mAllImages.isFilenameExist(item.object, modName)) {
					PublicTools.hint(PlayListsActivity.this,
							R.string.fail_renameexist);
					return;
				}
				// fix bug 82969 by W.Y begin 2011.1.5
				if (modName.startsWith(".") || modName.contains("/")
						|| modName.contains("\\") || modName.contains(":")
						|| modName.contains("?") || modName.contains("\uff1f")
						|| modName.contains("<") || modName.contains(">")
						|| modName.contains("\"") || modName.contains("\t")
						|| modName.contains("|") || modName.contains("*")
						|| modName.contains("\n") || modName.contains("\r")
						|| modName.contains("'")) {
					PublicTools.hint(PlayListsActivity.this,
							R.string.fail_renamefile);
					return;
				}
				// fix bug 82969 by W.Y end

				VideoObject object = item.object;
				if (item.datapath.equals(object.getMediapath())) {
					if (mAllImages.renameImage(PlayListsActivity.this, object,
							modName)) {
						item.name = modName;
						item.datapath = PublicTools.replaceFilename(
								item.datapath, modName);

						// sendBroadcast(new
						// Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						// Uri.parse("file://"
						// + item.datapath)));
						// sendBroadcast(new
						// Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						// Uri.parse("file://"
						// + oldItemPath)));
						refreshLastest(false);

						return;
					}
				}

				PublicTools.hint(PlayListsActivity.this,
						R.string.fail_renamefile);
			}
		};

		AlertDialog renameDialog = new AlertDialog.Builder(
				PlayListsActivity.this).create();
		renameDialog.setView(renameView);
		renameDialog.setTitle(R.string.rename_dialog);
		renameDialog.setButton(getString(R.string.button_ok),
				renameConfirmListener);
		renameDialog.setButton2(getString(R.string.button_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		mCurrrentActiveDialog = renameDialog;
		renameDialog.show();
		mRefreshHandler.sendEmptyMessage(REFRESH);
	}

	private void showUserOperationDialog(final int position) {
		String name = mActiveList.get(position).name;

		if (mActiveList == mNormalVideoList || mActiveList == mCameraList) {
			mListOperationDialog = new AlertDialog.Builder(
					PlayListsActivity.this).setTitle(name).setItems(
					R.array.items_for_allVideo,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							/* Play */
							case 0:
								startActivity(position);
								break;
							/* Rename */
							case 1:
								showRenameDialog(position);
								break;
							/* Delete */
							case 2:
								deleteVideo(position);
								break;
							/* default */
							default:
								break;
							}
						}
					}).create();
		}
		mCurrrentActiveDialog = mListOperationDialog;
		mListOperationDialog.show();
		mRefreshHandler.sendEmptyMessage(REFRESH);
	}

	private void deleteVideo(final int position) {
		Log.v(TAG, "deleteVideo  :   " + position);

		mCurrrentActiveDialog = new AlertDialog.Builder(this).setMessage(
				getString(R.string.confirm_deletefile)).setPositiveButton(
				R.string.button_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						VideoWorkItem item = mActiveList.get(position);
						VideoObject object = item.object;

						if (item.datapath.equals(object.getMediapath())) {
							if (mAllImages.removeImage(object)) {
								mAllVideoList.set(mAllVideoList.indexOf(item),
										null);
								mActiveList.remove(item);
								refreshLastest(false);

								return;
							}
						}
						PublicTools.hint(PlayListsActivity.this,
								R.string.fail_deletefile);
					}
				}).setNegativeButton(R.string.button_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
		mRefreshHandler.sendEmptyMessage(REFRESH);
	}

	 
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	 
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			mCurrentListState = LIST_STATE_REQUEST_REFRESH;
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		case OnScrollListener.SCROLL_STATE_FLING:
			mCurrentListState = LIST_STATE_BUSY;
			break;
		}
	}

//overridePendingTransition(R.anim.fade, R.anim.hold);
	  int isExit = 0;
	  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
	  { 
		  
	    if ((paramKeyEvent.getAction() == 0) && (paramInt == 4) ){
	    	
	       if (isExit == 0){
	    	   isExit ++;
	    	   UIUtils.showToast(this, "再点一次可退出");
	    	   return true;
	      }
	       if(isExit == 1){
	    		//以下方法将用于释放SDK占用的系统资源
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
	 		mRefreshHandler.sendEmptyMessage(REFRESH); 
	 	}

	

}
