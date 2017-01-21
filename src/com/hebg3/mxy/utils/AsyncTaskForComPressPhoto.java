package com.hebg3.mxy.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Message;

public class AsyncTaskForComPressPhoto extends AsyncTask<Object,Object,Object>{
	
	ArrayList<PhotoInfo> filedirs;
	Message m;
	
	public AsyncTaskForComPressPhoto(ArrayList<PhotoInfo> filedirs,Message m){
		this.filedirs=filedirs;
		this.m=m;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		
		for(int i=0;i<filedirs.size();i++){
			File f=new File(this.filedirs.get(i).smallphotourl);
			if(!f.exists()){
				continue;
			}else{
				Bitmap bitmap=null;
				try {
					BitmapFactory.Options opt = new BitmapFactory.Options();
					//inJustDecodeBounds如果设置为true，解码器将返回null（没有位图的bitmap对象）
					//但是调用者仍然可以查询位图的实际像素，系统并没有为位图像素分配内存
					opt.inJustDecodeBounds = true;
					bitmap = BitmapFactory.decodeFile(this.filedirs.get(i).smallphotourl, opt);

					// 获取到这个图片的原始宽度和高度
					int picWidth = opt.outWidth;
					int picHeight = opt.outHeight;

					// 期望的图片宽和高  比如说800×600
					int requestwidth = 1024;
					int requestheight = 768;

					// isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
					opt.inSampleSize = 1;
					// 根据屏的大小和图片大小计算出缩放比例
					if(picWidth>requestwidth||picHeight>requestheight){
						if(picWidth>picHeight){//横着的图片
							opt.inSampleSize=picWidth/requestwidth;
						}else{//竖着的图片或正方形图片
							opt.inSampleSize=picHeight/requestheight;
						}
					}

					// 这次再真正地生成一个有像素的，经过缩放了的bitmap
					opt.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeFile(this.filedirs.get(i).smallphotourl,opt); 
					
					//解决照片旋转角度
					int degree=readPictureDegree(this.filedirs.get(i).smallphotourl);
					System.out.println("旋转："+degree);
					bitmap=rotaingImage(degree,bitmap);
					
					FileOutputStream fos = new FileOutputStream(this.filedirs.get(i).uploadphotourl);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos);// 降低照片质量，再写入sdcard中

					bos.close();
					fos.close();
					
					bitmap.recycle();// 回收图片所占内存
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 1;
	}
	
	/**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
   /*
    * 旋转图片 
    * @param angle 
    * @param bitmap 
    * @return Bitmap 
    */ 
   public static Bitmap rotaingImage(int angle , Bitmap bitmap) {  
       //旋转图片 动作   
       Matrix matrix = new Matrix();;  
       matrix.postRotate(angle);  
       // 创建新的图片   
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
       return resizedBitmap;  
   }
   
   @Override
   protected void onPostExecute(Object result) {
	   this.m.what=1;
	   this.m.sendToTarget();
   }
}
