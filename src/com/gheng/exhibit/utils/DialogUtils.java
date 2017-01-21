package com.gheng.exhibit.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * 弹出对话框显示
 * @author lileixing
 */
public class DialogUtils {

	private DialogUtils(){}
	
	private static Context _context;
	
	public static void init(Context context){
		_context = context;
	}
	
	public static AlertDialog show(String title,String message,boolean cancel,
			String positiveText,String neutralText ,String negativeText,
			OnDialogClickListener onDialogClickListener){
		Builder builder = new Builder(_context);
		builder.setCancelable(true);
		ClickImpl clickImpl = new ClickImpl(onDialogClickListener);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveText, clickImpl);
		builder.setNegativeButton(negativeText, clickImpl);
		builder.setNeutralButton(neutralText, clickImpl);
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(cancel);
		dialog.show();
		return dialog;
	}
	
	public static AlertDialog show(String title,String message,String positiveText,String negativeText,
			OnDialogClickListener onDialogClickListener){
		return show(title, message, false, positiveText, null, negativeText, onDialogClickListener);
	}
	

	public static AlertDialog show(String message,String positiveText,String negativeText,
			OnDialogClickListener onDialogClickListener){
		return show(null, message, positiveText, negativeText, onDialogClickListener);
	}
	
	public interface OnDialogClickListener{
		
		public void onClickPositive(DialogInterface dialog);
		public void onClickNeutral(DialogInterface dialog);
		public void onClickNegative(DialogInterface dialog);
	}
	
	private static class ClickImpl implements OnClickListener{
		
		private OnDialogClickListener onDialogClickListener;
		
		public ClickImpl(OnDialogClickListener onDialogClickListener){
			this.onDialogClickListener = onDialogClickListener;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(this.onDialogClickListener == null)
				return;
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				onDialogClickListener.onClickPositive(dialog);
				break;
			case DialogInterface.BUTTON_NEUTRAL:
				onDialogClickListener.onClickNeutral(dialog);
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				onDialogClickListener.onClickNegative(dialog);
				break;
			}
		}
	}
	
}
