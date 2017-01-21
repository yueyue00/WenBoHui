package com.gheng.exhibit.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.zxing.common.StringUtils;

import android.os.AsyncTask;
import android.text.TextUtils;

/**
 *	接口调用
 * @author lileixing
 */
public class DownLoadTask extends AsyncTask<Void, Integer,Void> {

	/**
	 * 服务器文件路径
	 */
	private String path;
	
	private DownLoadListener listener;
	/**
	 * 文本输出目录
	 */
	private String outPath;
	/**
	 * 文件原始名字
	 */
	private String fileOriginalName;
	
	private String fileOutName;
	
	public DownLoadTask(DownLoadListener listener){
		this.listener = listener;
	}
	
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @param outPath the outPath to set
	 */
	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}
	
	/**
	 * @param listener the listener to set
	 */
	public void setListener(DownLoadListener listener) {
		this.listener = listener;
	}
	
	/**
	 * @return the fileOriginalName
	 */
	public String getFileOriginalName() {
		return fileOriginalName;
	}

	/**
	 * @param fileOriginalName the fileOriginalName to set
	 */
	public void setFileOriginalName(String fileOriginalName) {
		this.fileOriginalName = fileOriginalName;
	}

	/**
	 * @return the fileOutName
	 */
	public String getFileOutName() {
		return fileOutName;
	}

	/**
	 * @param fileOutName the fileOutName to set
	 */
	public void setFileOutName(String fileOutName) {
		this.fileOutName = fileOutName;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the outPath
	 */
	public String getOutPath() {
		return outPath;
	}

	@Override
	protected void onPreExecute() {
		if(listener == null)
			return;
		listener.onStart();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		int lastIndex = path.lastIndexOf("/");
		this.fileOriginalName = path.substring(lastIndex+1);
		String name = this.fileOriginalName;
		if(!TextUtils.isEmpty(this.fileOutName)){
			name = this.fileOutName;
		}
		try {  
            //连接地址  
            URL u = new URL(path);  
            HttpURLConnection c = (HttpURLConnection) u.openConnection();  
            c.setRequestMethod("GET");  
            c.setDoOutput(true);  
            c.connect();  
            //计算文件长度  
            int lenghtOfFile = c.getContentLength();  
            File outFp = new File(outPath);
            if(!outFp.exists()){
            	outFp.mkdirs();
            }
            FileOutputStream f = new FileOutputStream(new File(outFp, name));  
            InputStream in = c.getInputStream();  
           //下载的代码  
            byte[] buffer = new byte[1024];  
            int len = 0;  
            int total = 0;  
            while ((len = in.read(buffer)) > 0) {  
                total += len;
                publishProgress(total*100/lenghtOfFile);
                f.write(buffer, 0, len);  
            }  
            f.close();  
        } catch (Exception e) {  
        	e.printStackTrace();
           if(listener != null){
        	   listener.onError(e.getMessage());
           }
        }  
        return null;  
	}
	
	@Override
	protected void onPostExecute(Void result) {
		if(listener == null){
			return;
		}
		listener.onEnd();
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		if(listener == null){
			return;
		}
		listener.onUpdate(values[0]);
	}
	
	/**
	 * 回调接口
	 * @author lileixing
	 */
	public static interface DownLoadListener{
		public void onStart();
		
		public void onEnd();
		
		public void onUpdate(int value);
		
		public void onError(String message);
	}

}
