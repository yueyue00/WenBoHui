package com.gheng.exhibit.database.task;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

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
	
	private String outFilePath;
	
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
	
	@Override
	protected void onPreExecute() {
		if(listener == null)
			return;
		listener.onStart();
	}
	
	public String getOutFilePath() {
		return outFilePath;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		int lastIndex = path.lastIndexOf("/");
		String name = path.substring(lastIndex+1);
		try {  
            //连接地址  
            URL u = new URL(path);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();  
            c.setRequestMethod("GET");  
            c.setDoOutput(true);  
            c.setDoInput(true);
            c.connect();  
            //计算文件长度  
            int lenghtOfFile = c.getContentLength();  
            File outFp = new File(outPath);
            if(!outFp.exists()){
            	outFp.mkdirs();
            }
            outFilePath = outPath;
            if(!outFilePath.endsWith(File.separator)){
            	outFilePath+= File.separator;
            }
            outFilePath += name;
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
		listener.onEnd(outFilePath);
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
		
		public void onEnd(String outFileName);
		
		public void onUpdate(int value);
		
		public void onError(String message);
	}

}
