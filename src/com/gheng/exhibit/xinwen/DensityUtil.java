package com.gheng.exhibit.xinwen;

import android.content.Context;


/**
 * 屏幕尺寸工具
 * @author Administrator
 *
 */
public class DensityUtil {  
  
   
	/***
     * dp==>px
     * @param context 
     * @param dpValue dp
     * @return px
     */
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /***
     *  px==>dp
     * @param context 
     * @param pxValue px
     * @return dp
     */
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    
}
