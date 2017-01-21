package com.hebg3.wl.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.util.Log;

import com.gheng.exhibit.utils.Constant;
import com.google.gson.JsonSyntaxException;
import com.smartdot.wenbo.huiyi.R;

public class HttpUrlConnectionLoginLoader {
	/**
	 * 建立连接的超时值
	 */
	private static final int TIMEOUT = 10 * 1000;
	private String url;
	private String content;

	public void postDataFromSelf(ClientParams params,
			OnEntityLoadCompleteListener<Base> listener, Context context) {
		Base base = new Base();
		// Https请求
		HttpsURLConnection connection = null;
		// http请求
		// HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			url = params.domain + params.url;
			// https请求
			connection = (HttpsURLConnection) new URL(url).openConnection();
			// http请求
			// connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);// 不使用缓存
			connection.setRequestMethod("POST");
			System.out.println("aaa" + "HttpUrlConnectionLoginLoader:getcookie"
					+ Constant.getCookie(context, Constant.domain));
			try {
				connection.setRequestProperty("Cookie",
						Constant.getCookie(context, Constant.domain));
			} catch (Exception e) {
				e.printStackTrace();
			}// 请求头加入cookie信息

			connection.setRequestProperty("charset", "UTF-8");
			connection.setConnectTimeout(TIMEOUT);
			connection.setReadTimeout(TIMEOUT);

			/**
			 * 登录接口，加入ssl证书单向验证 带着解析后的服务器端证书进行请求 马晓勇加
			 **/
			//
			// KeyManagerFactory keyManager =
			// KeyManagerFactory.getInstance("X509");
			// KeyStore keyKeyStore = KeyStore.getInstance("BKS");
			//
			// //===========lyy 使用慧点的ssl证书
			// keyKeyStore.load(context.getResources().openRawResource(R.raw.server),"password".toCharArray());
			// keyManager.init(keyKeyStore,"password".toCharArray());
			//
			// //原版的
			// SSLContext sslContext = SSLContext.getInstance("TLS");
			// sslContext.init(keyManager.getKeyManagers(),createTrustManagers(context),
			// null);
			// HttpsURLConnection.setDefaultHostnameVerifier(new
			// NullHostNameVerifier());
			// connection.setSSLSocketFactory(sslContext.getSocketFactory());

			// ssl验证代码结束

			connection.connect();// 打开链接
			os = connection.getOutputStream();
			os.write(params.params.getBytes("UTF-8"));
			os.flush();

			int code = connection.getResponseCode();// 注意，进行ssl验证后，只有服务器返回code200，才能执行connection.getInputStream()，否则报错
			String msg = connection.getResponseMessage();

			System.out.println("code:" + code);
			System.out.println("msg:" + msg);

			switch (code) {
			case HttpURLConnection.HTTP_OK:// code 200 开始获取服务器响应数据流
				// 数据传递成功

				Constant.setCookie(context, Constant.domain, connection);

				is = connection.getInputStream();
				content = convertStreamToString(is);

				System.out.println("aaa:HttpUrlConnectionLoginLoader:HTTP_OK"
						+ url + params.params);
				System.out.println("aaa:HttpUrlConnectionLoginLoader:HTTP_OK"
						+ content);
				if (content.equals("")) {
					listener.onError(base);
				} else {
					base = Constant.gson.fromJson(content, Base.class);
					base.responseCode = connection.getResponseCode();
					base.params = params;// 将请求参数带出
					listener.onEntityLoadComplete(base);
				}
				break;
			default:// ssl验证失败或请求超时
				// 网络异常
				if (listener != null) {
					base.responseCode = connection.getResponseCode();
					base.params = params;// 将请求参数带出
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 网络异常
			if (listener != null) {
				if (e instanceof JsonSyntaxException) {
					base.message = "json数据格式错误";
				} else if (e instanceof MalformedURLException) {
					base.message = "URL格式错误";
				} else if (e instanceof UnknownHostException) {
					base.message = "不能解析服务器地址";
				} else if (e instanceof SocketTimeoutException) {
					base.message = "网络不给力";
				} else if (e instanceof ConnectException) {
					base.message = "连接失败";
				} else {
					base = new Base();
					base.params = params;
					base.message = "未知错误";
				}
				base.params = params;// 将请求参数带出
				listener.onError(base);
			}
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					if (listener != null) {
						base.params = params;// 将请求参数带出
						listener.onError(base);
					}
				}
				os = null;
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					if (listener != null) {
						base.params = params;// 将请求参数带出
						listener.onError(base);
					}
				}
				is = null;
			}
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
	}

	private String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static TrustManager[] createTrustManagers(Context cont)
			throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {

		KeyStore trustStore = KeyStore.getInstance("BKS");

		// ===============lyy修改证书
		trustStore.load(cont.getResources().openRawResource(R.raw.server),
				"password".toCharArray());

		printKeystoreInfo(trustStore);// for debug

		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(trustStore);

		return trustManagerFactory.getTrustManagers();
	}

	private static void printKeystoreInfo(KeyStore keystore)
			throws KeyStoreException {
		System.out.println("Provider : " + keystore.getProvider().getName());
		System.out.println("Type : " + keystore.getType());
		System.out.println("Size : " + keystore.size());

		Enumeration en = keystore.aliases();
		while (en.hasMoreElements()) {
			System.out.println("Alias: " + en.nextElement());
		}
	}

	/**
	 * @author 马晓勇 ssl验证使用
	 */
	class NullHostNameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			Log.i("RestUtilImpl", "Approving certificate for " + hostname);
			return true;
		}
	}
}
