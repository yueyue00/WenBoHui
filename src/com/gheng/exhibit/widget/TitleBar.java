package com.gheng.exhibit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.DipUtils;
import com.smartdot.wenbo.huiyi.R;

/**
 * Title栏
 * 
 * @author lileixing
 */
public class TitleBar extends RelativeLayout {

	private static final int LEFT_IMG_ID = 1;
	private static final int LEFT_LINE_ID = 2;
	private static final int TEXT_ID = 3;
	private static final int RIGHT_LINE_ID = 4;
	private static final int RIGHT_IMG_ID = 5;
	private static final int BOTTOM_IMG_ID = 5;

	/**
	 * 左边图片
	 */
	private ImageView leftImage;
	/**
	 * 左边线
	 */
	private View leftLine;
	/**
	 * 中间文字
	 */
	private TextView tv;
	/**
	 * 右边线
	 */
	private View rightLine;
	/**
	 * 右边图片
	 */
	private ImageView rightImage;
	


	/**
	 * 左边图片资源地址
	 */
	private Drawable leftDrawable = null;
	/**
	 * 显示左线
	 */
	private boolean leftLineShow = true;
	/**
	 * 左侧线颜色
	 */
	private int leftLineColor = 0;
	/**
	 * 左侧线宽
	 */
	private int leftLineWidth = 1;
	/**
	 * 右侧线宽
	 */
	private int rightLineWidth = 1;
	/**
	 * 显示右线
	 */
	private boolean rightLineShow = true;
	/**
	 * 右侧线颜色
	 */
	private int rightLineColor = 0;
	/**
	 * 右边图片
	 */
	private Drawable rightDrawable = null;
	/**
	 * 字体大小
	 */
	private int textSize = 15;
	/**
	 * 字体颜色
	 */
	private int textColor = 0;
	/**
	 * 内容
	 */
	private String text;
	/**
	 * 点击事件监听器
	 */
	private TitleBar.OnClickListener onClickListener;

	private int imageSize = 40;

	private ClickImpl clickImpl = new ClickImpl();

	private Boolean bottomLineShow = true;
	private View bottomLine;

	/**
	 * @param context
	 */
	public TitleBar(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
		initView(context);
	}

	/**
	 * 读取view自定义的属性
	 */
	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TitleBar);
//		Resources resources = this.getResources();
//		leftDrawable = resources.getDrawable(R.drawable.book_meeting);
		leftLineShow = a.getBoolean(R.styleable.TitleBar_leftLineShow, true);
		leftLineColor = a.getColor(R.styleable.TitleBar_leftLineColor,
				Color.GRAY);
		leftLineWidth = a
				.getDimensionPixelSize(R.styleable.TitleBar_leftLineWidth,
						DipUtils.dip2px(context, 1));

		rightDrawable = a.getDrawable(R.styleable.TitleBar_rightImageSrc);
		rightLineShow = a.getBoolean(R.styleable.TitleBar_rightLineShow, true);
		rightLineColor = a.getColor(R.styleable.TitleBar_rightLineColor,
				Color.GRAY);
		rightLineWidth = a.getDimensionPixelSize(
				R.styleable.TitleBar_rightLineWidth,
				DipUtils.dip2px(context, 1));

		textSize = a.getDimensionPixelSize(R.styleable.TitleBar_textSize, 15);
		textColor = a.getColor(R.styleable.TitleBar_textColor, getContext()
				.getResources().getColor(R.color.title_font));
		text = a.getString(R.styleable.TitleBar_text);

		imageSize = a
				.getDimensionPixelSize(
						R.styleable.TitleBar_imageSize,
						getResources().getDimensionPixelOffset(
								R.dimen.title_icon_size));
		bottomLineShow = a
				.getBoolean(R.styleable.TitleBar_bottomLineShow, true);
		a.recycle();
	}

	/**
	 * 初始化View
	 */
	private void initView(Context context) {
		int height = -1;
		int linePadding = DipUtils.dip2px(context, 8);
		int lineMargin = DipUtils.dip2px(context, 2);
		int imagePadding = DipUtils.dip2px(context, 20);

		int bottomLineH = DipUtils.dip2px(context, 1);
		int imageMargin = DipUtils.dip2px(context, 10);

		LinearLayout ll = null;
		if (leftDrawable != null) {
			ll = new LinearLayout(context);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			ll.setLayoutParams(rlp);
			ll.setPadding(imageMargin, 0, imagePadding, 0);
			ll.setGravity(Gravity.CENTER_VERTICAL);
			ll.setBackgroundColor(Color.TRANSPARENT);
			ll.setId(LEFT_IMG_ID);

			leftImage = new ImageView(context);
			RelativeLayout.LayoutParams leftImageParam = new RelativeLayout.LayoutParams(
					imageSize, imageSize);
			leftImage.setLayoutParams(leftImageParam);
			leftImage.setImageDrawable(leftDrawable);
			ll.addView(leftImage);

			addView(ll);
			ll.setOnClickListener(clickImpl);
		}

		// if(leftLineShow && leftDrawable != null){
		// leftLine = new View(context);
		// leftLine.setId(LEFT_LINE_ID);
		// LayoutParams leftLineParma = new
		// RelativeLayout.LayoutParams(leftLineWidth,height);
		// leftLineParma.setMargins(lineMargin,linePadding, 0, linePadding);
		// leftLineParma.addRule(RelativeLayout.RIGHT_OF, leftImage.getId());
		// leftLine.setLayoutParams(leftLineParma);
		// leftLine.setBackgroundColor(leftLineColor);
		// addView(leftLine);
		// }
		rightImage = new ImageView(context);
		rightImage.setId(RIGHT_IMG_ID);
		LayoutParams rightImageParma = new RelativeLayout.LayoutParams(
				imageSize, imageSize);
		rightImageParma.setMargins(0, 0, imageMargin, 0);
		rightImage.setLayoutParams(rightImageParma);
		rightImageParma.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rightImageParma.addRule(RelativeLayout.CENTER_VERTICAL);
		rightImage.setImageDrawable(rightDrawable);
		addView(rightImage);
		rightImage.setOnClickListener(clickImpl);
		if (rightDrawable != null) {
			rightImage.setVisibility(View.VISIBLE);
		}

		if (rightLineShow && rightDrawable != null) {
			rightLine = new View(context);
			rightLine.setId(RIGHT_LINE_ID);
			LayoutParams rightLineParma = new RelativeLayout.LayoutParams(
					rightLineWidth, height);
			rightLineParma.setMargins(0, linePadding, lineMargin, linePadding);
			rightLineParma.addRule(RelativeLayout.LEFT_OF, rightImage.getId());
			rightLine.setLayoutParams(rightLineParma);
			rightLine.setBackgroundColor(rightLineColor);
			addView(rightLine);
		}

		tv = new TextView(context);
		tv.setId(TEXT_ID);
		LayoutParams tvParam = new RelativeLayout.LayoutParams(
				Constant.SCREEN_WIDTH - DipUtils.dip2px(context, 80), -1);
		// if(leftDrawable != null){
		// tvParam.addRule(RelativeLayout.RIGHT_OF, LEFT_IMG_ID);
		// }
		// if(rightDrawable != null){
		// tvParam.addRule(RelativeLayout.LEFT_OF, RIGHT_IMG_ID);
		// }
		tvParam.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv.setLayoutParams(tvParam);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		tv.setTextColor(textColor);
		tv.setGravity(Gravity.CENTER);
		tv.setSingleLine(true);
		tv.setEllipsize(TruncateAt.END);

		addView(tv);
		if (bottomLineShow) {
			bottomLine = new View(context);
			bottomLine.setId(BOTTOM_IMG_ID);
			LayoutParams bottomLineParma = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, bottomLineH);
			bottomLineParma.setMargins(0, 0, 0, 0);
			bottomLineParma.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					this.getId());
			bottomLine.setLayoutParams(bottomLineParma);
			bottomLine.setBackgroundColor(this.getResources().getColor(
					R.color.line_gray));
			addView(bottomLine);
		}
	}

	/**
	 * @return the leftDrawable
	 */
	public Drawable getLeftDrawable() {
		return leftDrawable;
	}

	/**
	 * @param leftDrawable
	 *            the leftDrawable to set
	 */
	public void setLeftDrawable(Drawable leftDrawable) {
		this.leftDrawable = leftDrawable;
		leftImage.setImageDrawable(leftDrawable);
	}

	/**
	 * @return the rightDrawable
	 */
	public Drawable getRightDrawable() {
		return rightDrawable;
	}

	/**
	 * @param rightDrawable
	 *            the rightDrawable to set
	 */
	public void setRightDrawable(Drawable rightDrawable) {
		this.rightDrawable = rightDrawable;
		rightImage.setImageDrawable(rightDrawable);
	}

	/**
	 * @return the textSize
	 */
	public int getTextSize() {
		return textSize;
	}

	/**
	 * @param textSize
	 *            the textSize to set
	 */
	public void setTextSize(int textSize) {
		this.textSize = textSize;
		tv.setTextSize(textSize);
	}

	/**
	 * @return the textColor
	 */
	public int getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor
	 *            the textColor to set
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
		tv.setTextColor(textColor);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
		tv.setText(text);
	}

	/**
	 * @param onClickListener
	 *            the onClickListener to set
	 */
	public void setOnClickListener(TitleBar.OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	private class ClickImpl implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case LEFT_IMG_ID:
				if (onClickListener != null)
					onClickListener.clickLeftImage();
				break;
			case RIGHT_IMG_ID:
				if (onClickListener != null)
					onClickListener.clickRightImage();
				break;
			}
		}
	}

	public void setTypeface(Typeface tf) {
		tv.setTypeface(tf);
	}

	public void showRightImage(boolean show) {
		if (rightImage != null) {
			if (show) {
				rightImage.setVisibility(View.VISIBLE);
			} else {
				rightImage.setVisibility(View.GONE);
			}
		}
	}

	public interface OnClickListener {

		public void clickLeftImage();

		public void clickRightImage();
	}

}
