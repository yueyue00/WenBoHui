package com.hebg3.mxy.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.gheng.exhibit.utils.Constant;
import com.smartdot.wenbo.huiyi.R;

public class AsyncTaskForSSlcer extends AsyncTask<String, String, String>{

	public Context cont;
	public Message m;
	
	
	public AsyncTaskForSSlcer(Context cont,Message m){
		this.m=m;
		this.cont=cont;
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
        try {
        	KeyManagerFactory keyManager = KeyManagerFactory.getInstance("X509");  
            KeyStore keyKeyStore = KeyStore.getInstance("BKS");  
            
            //==============lyy修改证书
            keyKeyStore.load(cont.getResources().openRawResource(R.raw.server),"qazwsx".toCharArray()); 
            keyManager.init(keyKeyStore,"qazwsx".toCharArray());
            
            
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManager.getKeyManagers(), new TrustManager[] { tm }, null);
            
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            HttpsURLConnection conn = (HttpsURLConnection) new URL(Constant.DOMAIN).openConnection();
            
			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(10 * 1000);
			conn.setDoInput(true);
			conn.connect();
			
			String msg = conn.getResponseMessage();
			int code = conn.getResponseCode();

            if (code == HttpURLConnection.HTTP_OK) {//只有200，才获取响应数据，否则不获取
            	InputStream in = conn.getInputStream();
            	String content=HttpUrlConnectionutil.convertStreamToStringUTF8(in);
                System.out.println("content:"+content);
            }
            System.out.println("code:"+code);
            System.out.println("msg:"+msg);
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
		return null;
	}  
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
	}
	
	class NullHostNameVerifier implements HostnameVerifier {

	    @Override   
	    public boolean verify(String hostname, SSLSession session) {
	        Log.i("RestUtilImpl", "Approving certificate for " + hostname);
	        return true;
	    }
	}
}
