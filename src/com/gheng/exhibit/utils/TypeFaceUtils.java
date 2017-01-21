package com.gheng.exhibit.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.widget.TitleBar;

/**
 *
 * @author lileixing
 */
public class TypeFaceUtils {
	
	private TypeFaceUtils(){}
	
	private static Typeface tf = null;
	
/*	static{
		AssetManager mgr = MyApplication.getInstance().getAssets();//得到AssetManager
		tf=Typeface.createFromAsset(mgr, "msyahei.ttf");//根据路径得到Typeface
	}*/
	
	public static void setTypeValue(Object obj){
		/*Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(TextView.class.isAssignableFrom(field.getType())
					|| TitleBar.class.isAssignableFrom(field.getType())){
				try {
					field.setAccessible(true);
					Object fobj = field.get(obj);
					Method m = fobj.getClass().getDeclaredMethod("setTypeface", Typeface.class);
					if(m != null){
						m.setAccessible(true);
						m.invoke(fobj, tf);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(RadioGroup.class.isAssignableFrom(field.getType())){
				try {
					field.setAccessible(true);
					RadioGroup rb = (RadioGroup) field.get(obj);
					int childCount = rb.getChildCount();
					for(int i = 0; i < childCount;i++){
						RadioButton rbtn = (RadioButton) rb.getChildAt(i);
						rbtn.setTypeface(tf);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}*/
	}
}
