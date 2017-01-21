package com.hebg3.mxy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.gheng.exhibit.utils.Constant;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author 马晓勇的专属 http请求工具类
 *
 */
public class HttpUrlConnectionutil {
	
	public static int timeout=30*1000;//请求超时时限
	
	/**
	 * 请求服务器POST 方法 设置了httpurlc.setDoOutput(true) 
	 * 请求字段内容可直接通过httpurlc.getOutputStream()写入
	 * @param url              请求地址
	 * @param requestContent   请求内容
	 * @param context          上下文  设置cookie时使用
	 * @return                 服务器响应的输入流
	 * @throws IOException
	 */
	public static InputStream getHttpUrlConnectionisPOST(URL url,String requestContent,Context context) throws IOException{
		HttpURLConnection httpurlc=(HttpURLConnection)url.openConnection();
		httpurlc.setRequestMethod("POST");
		httpurlc.setConnectTimeout(timeout);
		httpurlc.setReadTimeout(timeout);
		httpurlc.setDoInput(true);
		httpurlc.setDoOutput(true);
		httpurlc.setDefaultUseCaches(false);

			try {
				System.out.println(Constant.getCookie(context,Constant.domain));
				httpurlc.setRequestProperty("Cookie", Constant.getCookie(context,Constant.domain));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//请求头加入cookie信息
		
		httpurlc.setRequestProperty("charset", "UTF-8");
		
		//POST请求方式 请求内容可直接写入输出流
		OutputStream os=httpurlc.getOutputStream();
		os.write(requestContent.getBytes("UTF-8"));
		os.flush();
		
		if (httpurlc.getResponseCode() == 200) {
			
			InputStream is = httpurlc.getInputStream();
			return is;
		} else {
			return null;
		}
	}
	
	
	/**
	 * 请求服务器GET 方法 不能设置httpurlc.setDoOutput(true) 
	 * @param url           请求地址   ?后面跟着参数
	 * @param context       上下文 设置cookie时使用
	 * @return              服务器响应的输入流
	 * @throws IOException
	 */
	public static InputStream getHttpUrlConnectionisGET(URL url,Context context) throws IOException{
		HttpURLConnection httpurlc=(HttpURLConnection)url.openConnection();
		httpurlc.setRequestMethod("GET");
		httpurlc.setConnectTimeout(timeout);
		httpurlc.setReadTimeout(timeout);
		httpurlc.setDoInput(true);
		httpurlc.setDefaultUseCaches(false);
		System.out.println("aaa:getHttpUrlConnectionisGET:getCookie:"+Constant.getCookie(context,Constant.domain));

			try {
				httpurlc.setRequestProperty("Cookie",Constant.getCookie(context,Constant.domain));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//请求头加入cookie信息
		
		if (httpurlc.getResponseCode() == 200) {
			
			InputStream is = httpurlc.getInputStream();
			return is;
		} else {
			return null;
		}
	}
		
	/**
	 * ①如果要从网络中下载文件时，我们知道网络是不稳定的，
	 * 也就是说网络下载时，read()方法是阻塞的，说明这时我们用inputStream.available()获取不到文件的总大小。 
	 * 此时就需要通过 HttpURLConnection httpconn =(HttpURLConnection)url.openConnection();
	 * 然后 httpconn.getContentLength();//获取文件长度 来获取文件的大小。
	 * ②如果是本地文件的话，用此方法就返回实际文件的大小。
	 * ③这个方法其实是通过文件描述符获取文件的总大小，而并不是事先将磁盘上的文件数据全部读入流中，再获取文件总大小。
	 */
	
	//流转字符串 GB2312
	public static String convertStreamToStringGB2312(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		String line = null;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"GB2312"));
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		is.close();
		return sb.toString();
	}
	//流转字符串 utf-8
	public static String convertStreamToStringUTF8(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		String line = null;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		is.close();
		return sb.toString();
	}
	
	//流转字节数组  生成图片
	public static Bitmap convertStreamToBitmap(InputStream is) {

		Bitmap b=BitmapFactory.decodeStream(is);
		return b;
	}
	
}
