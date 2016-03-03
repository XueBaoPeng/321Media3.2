package cn.hi321.android.media.http;

 
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
 
import cn.hi321.android.media.utils.LogUtil;
import cn.hi321.android.media.utils.UIUtils;

/**
 * 联网引擎，真正联网的功能
 * 
 * @author yanggf
 * 
 */
public class HttpAgent {
	private static final String TAG = "HttpAgent";
	
	public static final String SESSIONID = "sessionId";
	public static final String ERRORCODE = "ErrorCode";
	public static final String ERRORMESSAGE = "ErrorMessage";
	public static final int FIRST_TIMEOUT = 3;
	public static final int TIMEOUT = 5;
	private Context mContext;

	public final String GET_ADDRESS_URL = "http://www.funshion.com";
	private InputStream is;
	
//	private Preferences pref;
	// 主域
	public final static String TAG_MAIN_SERVERS = "1";
	// 辅域
	public final static String TAG_AUXILIARY_SERVERS = "2";

	public HttpAgent(Context context) {
		this.mContext = context;
//		pref = Preferences.getInstance(context);
	}

	public HttpAgent(Context context, Handler handler) {
		this.mContext = context;
//		pref = Preferences.getInstance(context);
	}
	
 

	/**
	 * send message 首先判断是否200 如果不是200 则返回联网失败 如果是 则看errorcode和errorstring
	 * 如果头信息里没有两个error则返回正确数据 如果有 则返回errocode和errorstring
	 * 
	 * @param action
	 *            方法名
	 * @param request
	 *            发送数据
	 * @param sessionId
	 * @return
	 */
	public synchronized String[] sendMessageByPost(String action, String request, String sessionId, int requestOrder) {
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		DefaultHttpClient httpClient = null;
		String state = null;
		String response = null;
 
		String url =   request;
 
		if (!UIUtils.isAvailable(mContext)) {
			return new String[] { UIUtils.CODE_HTTP_FAIL, "网络不可用，请检查网络" };
		}
		try {

			HttpParams params = createPostHttpParams(requestOrder);
			
			HttpPost httpPost = new HttpPost(url);

			if (sessionId != null) {
				httpPost.addHeader(SESSIONID, sessionId);
				httpPost.addHeader("Accept-Encoding", "gzip,deflate"); // TODO GZIP
			}
//			LogUtil.v(TAG, "HttpAgent==>sendMessage==>sessionId=" + sessionId);

			byte[] sendData;

			if (action != null) {
				sendData = action.getBytes("UTF-8");

				ByteArrayEntity byteArrayEntity = new ByteArrayEntity(sendData);
				httpPost.setEntity(byteArrayEntity);
			}

			httpClient = new DefaultHttpClient(params);
			
			HttpHost proxy = null;
			if (UIUtils.getNetMode(mContext).equals(UIUtils.NET_CMWAP) || UIUtils.getNetMode(mContext).equals(UIUtils.NET_WAP_3G)
			        || UIUtils.getNetMode(mContext).equals(UIUtils.NET_UNIWAP)) {
				proxy = new HttpHost("10.0.0.172", 80, "http");
			}
			
			if (UIUtils.getNetMode(mContext).equals(UIUtils.NET_CMWAP) || UIUtils.getNetMode(mContext).equals(UIUtils.NET_WAP_3G)
			        || UIUtils.getNetMode(mContext).equals(UIUtils.NET_UNIWAP)) {
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			

			StatusLine sl = httpResponse.getStatusLine();
			if (sl == null) {
				return new String[] { UIUtils.SERVER_NOT_RESPONDING, "" /*mContext.getString(R.string.str_connect_failed)*/};
			}

			int httpCode = sl.getStatusCode();

			HttpEntity entity = httpResponse.getEntity();
			if (entity == null) {
				return new String[] { String.valueOf(httpCode), null };
			}

			Header ecHeader = httpResponse.getFirstHeader(ERRORCODE);
			Header emHeader = httpResponse.getFirstHeader(ERRORMESSAGE);
			Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
			
			String errorCode = null;
			String errorMessage = null;

			if (ecHeader != null) {
				errorCode = ecHeader.getValue();
			}
			if (emHeader != null) {
				errorMessage = emHeader.getValue();
			}
			if (!TextUtils.isEmpty(errorCode) && !UIUtils.CODE_ERROR_RIGHT.equals(errorCode)) {
					String errMessage;
//					LogUtil.v(TAG, "responseHeader =================>>");
					if (TextUtils.isEmpty(errorMessage)) {
						errMessage = "";
					} else {
						errMessage = new String(errorMessage.getBytes("iso-8859-1"), "UTF-8");
					}
					
					
//					LogUtil.v(TAG, "responseHeader <<=================");
					return new String[] { errorCode, errMessage };
			}
			
			final long len = entity.getContentLength();
//			LogUtil.v(TAG, "HttpAgent==>sendMessage==>len=" + len);
			
			state = "" + httpResponse.getStatusLine().getStatusCode();

			try {
				is = entity.getContent();
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) { 
					 
					response = readDataForZgip(is, "UTF-8"); 
				} else {
					
					response = readData(is, "UTF-8");
				}
			} catch (OutOfMemoryError e) {
			
			}
 
		} catch (Exception e) {
			e.printStackTrace(); 
		
			return new String[] { UIUtils.SERVER_NOT_RESPONDING, "请求数据失败！" };// TODO 
		} finally {
//			LogUtil.i("HttpAgent==>sendMessage==>finally");
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
		
		return new String[] { state, response };

	}
	
	/**
	 * send message 首先判断是否200 如果不是200 则返回联网失败 如果是 则看errorcode和errorstring
	 * 如果头信息里没有两个error则返回正确数据 如果有 则返回errocode和errorstring
	 * 
	 * @param action
	 *            方法名
	 * @param request
	 *            发送数据
	 * @param sessionId
	 * @return
	 */
	public synchronized String[] requestMessageByPost(String request ,List <NameValuePair> params) {
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		DefaultHttpClient httpClient = null;
		String state = null;
		String response = null;
		String url =   request;
		 
		
		if (!UIUtils.isAvailable(mContext)) {
			return new String[] { UIUtils.CODE_HTTP_FAIL, "网络不可用，请检查网络" };
		}
		try { 
			
			HttpPost httpPost = new HttpPost(url);

			  /*
		        * NameValuePair实现请求参数的封装
		        */

		      httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpClient= new DefaultHttpClient();
			
			HttpHost proxy = null;
			if (UIUtils.getNetMode(mContext).equals(UIUtils.NET_CMWAP) || UIUtils.getNetMode(mContext).equals(UIUtils.NET_WAP_3G)
			        || UIUtils.getNetMode(mContext).equals(UIUtils.NET_UNIWAP)) {
				proxy = new HttpHost("10.0.0.172", 80, "http");
			}
			
			if (UIUtils.getNetMode(mContext).equals(UIUtils.NET_CMWAP) || UIUtils.getNetMode(mContext).equals(UIUtils.NET_WAP_3G)
			        || UIUtils.getNetMode(mContext).equals(UIUtils.NET_UNIWAP)) {
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			

			StatusLine sl = httpResponse.getStatusLine();
			if (sl == null) {
				return new String[] { UIUtils.SERVER_NOT_RESPONDING, "" /*mContext.getString(R.string.str_connect_failed)*/};
			}

			int httpCode = sl.getStatusCode();

			HttpEntity entity = httpResponse.getEntity();
			if (entity == null) {
				return new String[] { String.valueOf(httpCode), null };
			}

			Header ecHeader = httpResponse.getFirstHeader(ERRORCODE);
			Header emHeader = httpResponse.getFirstHeader(ERRORMESSAGE);
			Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
			
			String errorCode = null;
			String errorMessage = null;

			if (ecHeader != null) {
				errorCode = ecHeader.getValue();
			}
			if (emHeader != null) {
				errorMessage = emHeader.getValue();
			}
			if (!TextUtils.isEmpty(errorCode) && !UIUtils.CODE_ERROR_RIGHT.equals(errorCode)) {
					String errMessage;
//					LogUtil.v(TAG, "responseHeader =================>>");
					if (TextUtils.isEmpty(errorMessage)) {
						errMessage = "";
					} else {
						errMessage = new String(errorMessage.getBytes("iso-8859-1"), "UTF-8");
					}
				 
//					LogUtil.v(TAG, "responseHeader <<=================");
					return new String[] { errorCode, errMessage };
			}
			
			final long len = entity.getContentLength();
			
			state = "" + httpResponse.getStatusLine().getStatusCode();

			try {
				is = entity.getContent();
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) { 
					
					response = readDataForZgip(is, "UTF-8"); 
				} else {
				
					response = readData(is, "UTF-8");
				}
			} catch (OutOfMemoryError e) {
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			 return new String[] { UIUtils.SERVER_NOT_RESPONDING, "请求数据失败！" };// TODO 
		} finally {
		
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
		
		return new String[] { state, response };

	}
	
	/**
	 * send message 首先判断是否200 如果不是200 则返回联网失败 如果是 则看errorcode和errorstring
	 * 如果头信息里没有两个error则返回正确数据 如果有 则返回errocode和errorstring
	 * 
	 * @param action
	 *            方法名
	 * @param request
	 *            发送数据
	 * @param sessionId
	 * @return
	 */
	public synchronized String[] sendMessageByGet(String action, String request, String sessionId, int requestOrder,int mConnectionTimeout) {
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		DefaultHttpClient httpClient = null;
		String state = null;
		String response = null;

		
		String url =   request;
		 
		if (!UIUtils.isAvailable(mContext)) {
			return new String[] { UIUtils.CODE_HTTP_FAIL, "网络不可用，请检查网络" };
		}
		try {

			HttpParams params = createGetHttpParams(requestOrder,mConnectionTimeout);
			
			HttpGet httpGet = new HttpGet(url);

			if (sessionId != null) {
				httpGet.addHeader(SESSIONID, sessionId);
				httpGet.addHeader("Accept-Encoding", "gzip,deflate"); // TODO GZIP
			}
			httpClient = new DefaultHttpClient(params);
			
			HttpHost proxy = null;
			if (UIUtils.getNetMode(mContext).equals(UIUtils.NET_CMWAP) || UIUtils.getNetMode(mContext).equals(UIUtils.NET_WAP_3G)
			        || UIUtils.getNetMode(mContext).equals(UIUtils.NET_UNIWAP)) {
				proxy = new HttpHost("10.0.0.172", 80, "http");
			}
			
			if (UIUtils.getNetMode(mContext).equals(UIUtils.NET_CMWAP) || UIUtils.getNetMode(mContext).equals(UIUtils.NET_WAP_3G)
			        || UIUtils.getNetMode(mContext).equals(UIUtils.NET_UNIWAP)) {
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}

			HttpResponse httpResponse = httpClient.execute(httpGet);
			

			StatusLine sl = httpResponse.getStatusLine();
			if (sl == null) {
				return new String[] { UIUtils.SERVER_NOT_RESPONDING, "" /*mContext.getString(R.string.str_connect_failed)*/};
			}

			int httpCode = sl.getStatusCode();
			System.out.println("httpCode ==="+httpCode );
			HttpEntity entity = httpResponse.getEntity();
			if (entity == null) {
				return new String[] { String.valueOf(httpCode), null };
			}

			Header ecHeader = httpResponse.getFirstHeader(ERRORCODE);
			Header emHeader = httpResponse.getFirstHeader(ERRORMESSAGE);
			Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
			
			String errorCode = null;
			String errorMessage = null;

			if (ecHeader != null) {
				errorCode = ecHeader.getValue();
			}
			if (emHeader != null) {
				errorMessage = emHeader.getValue();
			}
			if (!TextUtils.isEmpty(errorCode) && !UIUtils.CODE_ERROR_RIGHT.equals(errorCode)) {
					String errMessage;
//					LogUtil.v(TAG, "responseHeader =================>>");
					if (TextUtils.isEmpty(errorMessage)) {
						errMessage = "";
					} else {
						errMessage = new String(errorMessage.getBytes("iso-8859-1"), "UTF-8");
					}
					 
					return new String[] { errorCode, errMessage };
			}
			
			final long len = entity.getContentLength();
//			LogUtil.v(TAG, "HttpAgent==>sendMessage==>len=" + len);
			
			state = "" + httpResponse.getStatusLine().getStatusCode();
			System.out.println("state ==="+state );
			try {
				is = entity.getContent();
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) { 
					
					response = readDataForZgip(is, "UTF-8"); 
				} else {
					
					response = readData(is, "UTF-8");
				}
				System.out.println("response==="+response );
			} catch (OutOfMemoryError e) {
			
			}
			
 
		} catch (Exception e) {
			e.printStackTrace();
			 return new String[] { UIUtils.SERVER_NOT_RESPONDING, "请求数据失败！" };// TODO 
		} finally {
//			LogUtil.i("HttpAgent==>sendMessage==>finally");
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
	
		return new String[] { state, response };

	}
	
	
	  
	
	/**
	 *  第一个参数为输入流,第二个参数为字符集编码，最后返回一个字符串.
	 *  
	 */
	public String readData(InputStream inSream, String charsetName) throws Exception {
		final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		
		final byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		
		return new String(data, charsetName);
	}
	
	
	/**
	 * 第一个参数为输入流,第二个参数为字符集编码
	 */
	public String readDataForZgip(InputStream inStream, String charsetName) throws Exception {
		final GZIPInputStream gzipStream = new GZIPInputStream(inStream);
		final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		final byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = gzipStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		
		final byte[] data = outStream.toByteArray();
		outStream.close();
		gzipStream.close();
		inStream.close();
		return new String(data, charsetName);
	}
	
	
	
	/**
	 * 调用Google API使用的联网方式
	 * 
	 * @param action
	 * @return
	 */
	public synchronized String[] getNetMessage(String action,int requestOrder) {
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		String state = null;
		String response = null;
		byte[] responseByteArray;
		DefaultHttpClient httpClient = null;

		String url = "";
		if (!TextUtils.isEmpty(action)) {
			url = GET_ADDRESS_URL + action;
		}
 
		if (!UIUtils.isAvailable(mContext)) {
			return new String[] { UIUtils.CODE_HTTP_FAIL, "" };
		}

		try {

			HttpParams params = createPostHttpParams(requestOrder);
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept-Language", "zh,zh-cn");
			httpClient = new DefaultHttpClient(params);

			
			HttpHost proxy = null;
			if (UIUtils.getNetMode(mContext).equals(UIUtils.NET_CMWAP) || UIUtils.getNetMode(mContext).equals(UIUtils.NET_WAP_3G)
			        || UIUtils.getNetMode(mContext).equals(UIUtils.NET_UNIWAP)) {
				proxy = new HttpHost("10.0.0.172", 80, "http");
			}
			
			if (UIUtils.getNetMode(mContext).equals(UIUtils.NET_CMWAP) || UIUtils.getNetMode(mContext).equals(UIUtils.NET_WAP_3G)
			        || UIUtils.getNetMode(mContext).equals(UIUtils.NET_UNIWAP)) {
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			
			HttpResponse httpResponse = httpClient.execute(httpGet);

			StatusLine sl = httpResponse.getStatusLine();
			if (sl == null) {
				return new String[] { UIUtils.CODE_HTTP_FAIL, "网络不可用，请检查网络" };
			}

			int httpCode = sl.getStatusCode();

			HttpEntity entity = httpResponse.getEntity();
			if (entity == null) {
				return new String[] { String.valueOf(httpCode), null };
			}

			Header ecHeader = httpResponse.getFirstHeader(ERRORCODE);
			Header emHeader = httpResponse.getFirstHeader(ERRORMESSAGE);

			String errorCode = null;
			String errorMessage = null;

			if (ecHeader != null) {
				errorCode = ecHeader.getValue();
			}
			if (emHeader != null) {
				errorMessage = emHeader.getValue();
			}

			if (!TextUtils.isEmpty(errorCode) && !UIUtils.CODE_ERROR_RIGHT.equals(errorCode)) {
				 
				return new String[] { errorCode, new String(errorMessage.getBytes("iso-8859-1"), "UTF-8") };
			}

			is = entity.getContent();
			
			long len = entity.getContentLength();
			 
			state = "" + httpResponse.getStatusLine().getStatusCode()/*String.valueOf(HttpURLConnection.HTTP_OK)*/;

			ByteArrayBuffer bab = new ByteArrayBuffer(1024);
			int line = -1;

			responseByteArray = new byte[1024];
			while ((line = is.read(responseByteArray)) != -1) {
				bab.append(responseByteArray, 0, line);
				responseByteArray = new byte[1024];
			}

			byte[] tmp = bab.toByteArray();

			response = new String(tmp, "UTF-8");
			bab = null;
  
		} catch (Exception e) {
			e.printStackTrace();
		
			return new String[] { UIUtils.CODE_HTTP_FAIL, "" }; 
		} finally {
		
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					
				}
			}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
		return new String[] { state, response };
	}

	private HttpParams createPostHttpParams(int requestOrder) {
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		if(requestOrder==0){
			HttpConnectionParams.setConnectionTimeout(params, FIRST_TIMEOUT * 1000);
			HttpConnectionParams.setSoTimeout(params, FIRST_TIMEOUT * 1000);
		}else if(requestOrder==1){
			HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
			HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		}
		HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);

		return params;
	}
	
	private HttpParams createGetHttpParams(int requestOrder,int mConnectionTimeout ) {
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		if(requestOrder==0){
			if(mConnectionTimeout<=0){
				HttpConnectionParams.setConnectionTimeout(params, FIRST_TIMEOUT * 1000);
				HttpConnectionParams.setSoTimeout(params, FIRST_TIMEOUT * 1000);
			}else{
				HttpConnectionParams.setConnectionTimeout(params, mConnectionTimeout * 1000);
				HttpConnectionParams.setSoTimeout(params, mConnectionTimeout * 1000);
			}
			
		}else if(requestOrder==1){
			if(mConnectionTimeout<=0){
				HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
				HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
			}else{
				HttpConnectionParams.setConnectionTimeout(params, mConnectionTimeout * 1000);
				HttpConnectionParams.setSoTimeout(params, mConnectionTimeout * 1000);
			}
			
		}
		HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);

		return params;
	}
	
	/************************************Https******************************************************/
	
	public synchronized String[] sendHttpsMessage(String action, String request, String sessionId) {
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		String state = null;
		String response = null;
		HttpsURLConnection cnx = null;
		
//		final String availableServers = pref.getCurrentAvailableServers();
//		// 根据服务器状态缓存，使用相应地址
//		if (!TextUtils.isEmpty(availableServers)) {
//			if (availableServers.equals(TAG_MAIN_SERVERS)) {
//				HTTPS_URL = MAIN_HTTPS_URL;
//			} else if (availableServers.equals(TAG_AUXILIARY_SERVERS)){
//				HTTPS_URL = AUXILIARY_HTTPS_URL;
//			}
//		}

		String url = GET_ADDRESS_URL + "/" + action;
		 
		if (!UIUtils.isAvailable(mContext)) {
			return new String[] { UIUtils.CODE_HTTP_FAIL, "" };
		}
		byte[] responseByteArray = request.getBytes();//

		try {
			cnx = getConnection(url);
			cnx.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			if (sessionId != null) {
				cnx.setRequestProperty(SESSIONID, sessionId);
			}
			cnx.setRequestProperty("Content-Length", String.valueOf(responseByteArray.length));
			 
			((HttpURLConnection) cnx).setRequestMethod("POST");
			cnx.setDoOutput(true);
			cnx.setDoInput(true);
			cnx.connect();

			// 把封装好的实体数据发送到输出流
			OutputStream outStream = cnx.getOutputStream();
			outStream.write(responseByteArray);
			outStream.flush();
			outStream.close();
			
			String errorCode = "" + cnx.getHeaderField(ERRORCODE);
			String errorMessage = "" + cnx.getHeaderField(ERRORMESSAGE);
			if (!TextUtils.isEmpty(errorCode) && !UIUtils.CODE_ERROR_RIGHT.equals(errorCode)) {
			
				errorMessage = new String(errorMessage.getBytes("iso-8859-1"), "UTF-8");
				 
				if (errorCode.equals("null")) {
					errorCode = UIUtils.CODE_HTTPS_RECONNECT;
					errorMessage = request;
				}

				return new String[] {errorCode, errorMessage };
			}

			 

			state = "" + cnx.getResponseCode()/*String.valueOf(HttpsURLConnection.HTTP_OK)*/;

			// 服务器返回输入流并读写
			BufferedReader in = new BufferedReader(new InputStreamReader(cnx
					.getInputStream()));
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = in.read()) != -1) {
				b.append((char) ch);
			}
			try {
				in.close();
				in = null;
			} catch (IOException e) {
				 
			}

			response = new String(b.toString().getBytes(), "UTF-8");
			 
//			 // 当前没用可用的地址，初次使用该客户端时用到
//			if (TextUtils.isEmpty(availableServers)) {
//				if (HTTPS_URL.equals(MAIN_HTTPS_URL)) {
////					pref.setCurrentAvailableServers(TAG_MAIN_SERVERS);
//				} else if (HTTPS_URL.equals(AUXILIARY_HTTPS_URL)){
////					pref.setCurrentAvailableServers(TAG_AUXILIARY_SERVERS);
//				}
//			}

		} catch (KeyManagementException e) {
		
			return new String[] { UIUtils.SERVER_NOT_RESPONDING, "" };
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e(TAG, "DXHttpAgent==>sendHttpsMessage==>" + e.toString());
			return new String[] { UIUtils.SERVER_NOT_RESPONDING, "" }; 
		} catch (IOException e) {
			LogUtil.e(TAG, "DXHttpAgent==>sendHttpsMessage==>" + e.toString());
			return new String[] { UIUtils.SERVER_NOT_RESPONDING, "" };
		} finally {
			LogUtil.i("DXHttpAgent==>sendHttpsMessage==>finally");
			if (cnx != null) {
				cnx.disconnect();
				cnx = null;
			}
		}
		return new String[] { state, response };
	}

//	使用HttpsURLConnection时需要实现HostnameVerifier 和 X509TrustManager，这两个实现是必须的，要不会报安全验证异常。
//	然后初始化X509TrustManager中的SSLContext，为javax.net.ssl.HttpsURLConnection设置默认的SocketFactory和HostnameVerifier
	private static final TrustManager[] TRUST_MANAGER = { new NaiveTrustManager() };
	private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();

	private static HttpsURLConnection getConnection(String url)
			throws IOException, NoSuchAlgorithmException,
			KeyManagementException {
		HttpsURLConnection conn = (HttpsURLConnection) new URL(url)
				.openConnection();
		if (conn instanceof HttpsURLConnection) {
			// Trust all certificates
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(new KeyManager[0], TRUST_MANAGER, new SecureRandom());
			SSLSocketFactory socketFactory = context.getSocketFactory();
			(conn).setSSLSocketFactory(socketFactory);
			// Allow all hostnames
			(conn).setHostnameVerifier(HOSTNAME_VERIFIER);

		}
		conn.setConnectTimeout(TIMEOUT * 1000);
		conn.setReadTimeout(TIMEOUT * 1000);
		return conn;
	}

	public static class NaiveTrustManager implements X509TrustManager {
		private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public boolean isClientTrusted(X509Certificate[] chain) {
			return (true);
		}

		public boolean isServerTrusted(X509Certificate[] chain) {
			return (true);
		}

		public X509Certificate[] getAcceptedIssuers() {
			return (_AcceptedIssuers);
		}
	}
	
}
