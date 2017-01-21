package com.gheng.exhibit.view.checkin.checkin;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.HuiChangMap;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.HackyViewPager;
import com.hebg3.mxy.utils.AsyncTaskForDownLoadPhoto;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.PhotoInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smartdot.wenbo.huiyi.R;

/**
 * 展示耨一个主题的图片集合
 * 
 * @author 马晓勇
 * 
 */
public class ShowDaHuiDianPingPhotosActivity extends BaseActivity implements
		OnClickListener {

	private static final String ISLOCKED_ARG = "isLocked";// 是否锁定当前图片
	private ViewPager mViewPager;
	private MenuItem menuLockItem;// 当前页面的系统menu菜单，暂时屏蔽
	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options;
	@ViewInject(R.id.but_fanhui)
	public Button but_fanhui;
	@ViewInject(R.id.in_title)
	public TextView in_title;
	@ViewInject(R.id.tv_number)
	public TextView tv_number;
	public int position = 0;
	public ArrayList<PhotoInfo> photolist;// 图片集合
	public ArrayList<HuiChangMap> maplist;
	public ProgressDialog pd;

	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (pd != null) {
				pd.dismiss();
			}
			if (msg.what == 1) {// 图片下载完毕
				Toast.makeText(
						ShowDaHuiDianPingPhotosActivity.this,
						BaseActivity.getLanguageString("图片已保存至")
								+ Constant.dianpingTarget, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(ShowDaHuiDianPingPhotosActivity.this,
						BaseActivity.getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showdahuidianpingzhutilist_photos);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);

		but_fanhui.setOnClickListener(this);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.imageloading)
				.showImageOnLoading(R.drawable.imageloading)
				// 加载时显示图片
				.showImageOnFail(R.drawable.imageloading)
				// 加载失败时显示缺省图片
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)// 考虑图片拍照时被旋转问题
				.resetViewBeforeLoading(true).build();
		// --------------lyy 添加上传图片 放开注释 s
		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG,
					false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
		// --------------lyy 添加上传图片 放开注释 e
		if (getIntent().getExtras().getSerializable("photolist") != null) {
			photolist = (ArrayList<PhotoInfo>) getIntent().getExtras()
					.getSerializable("photolist");
		} else {
			photolist = new ArrayList<PhotoInfo>();
		}
		if (getIntent().getExtras().getSerializable("maplist") != null) {
			maplist = (ArrayList<HuiChangMap>) getIntent().getExtras()
					.getSerializable("maplist");
			System.out.println("aaa:showdahui:maplist" + maplist.toString());
		} else {
			maplist = new ArrayList<HuiChangMap>();
		}
		position = getIntent().getIntExtra("position", 0);
		mViewPager.setAdapter(new SamplePagerAdapter(photolist, maplist));
		mViewPager.setCurrentItem(position);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (photolist.size() > 0) {
					tv_number.setText(position + 1 + "/" + photolist.size());
				} else if (maplist.size() > 0) {
					tv_number.setText(position + 1 + "/" + maplist.size());
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 图片轮播viewPager适配器
	 */
	class SamplePagerAdapter extends PagerAdapter {

		public ArrayList<PhotoInfo> list;
		public ArrayList<HuiChangMap> maplist;

		public SamplePagerAdapter(ArrayList<PhotoInfo> list,
				ArrayList<HuiChangMap> maplist) {
			this.list = list;
			this.maplist = maplist;
		}

		@Override
		public int getCount() {
			if (list.size() > 0) {
				return list.size();
			} else if (maplist.size() > 0) {
				return maplist.size();
			}
			return 0;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setOnLongClickListener(new PhotoLongClicklistener(
					position));
			if (list.size() > 0) {
				imageLoader.displayImage(list.get(position).photourl,
						photoView, options);
			} else if (maplist.size() > 0) {
				imageLoader.displayImage(maplist.get(position).imgurl,
						photoView, options);
			}

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		if (photolist.size() > 0) {
			in_title.setText(getLanguageString("图片"));
			tv_number.setText(position + 1 + "/" + photolist.size());
		} else if (maplist.size() > 0) {
			in_title.setText(getLanguageString("会场图"));
			tv_number.setText(position + 1 + "/" + maplist.size());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == but_fanhui) {
			this.finish();
		}
	}

	/**
	 * 图片长按，弹出下载保存提示
	 */
	class PhotoLongClicklistener implements OnLongClickListener {

		public int position;

		public PhotoLongClicklistener(int position) {
			this.position = position;
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(context)
					.setTitle(getLanguageString("确认") + ":")
					.setMessage(getLanguageString("要保存图片吗") + "?")
					.setPositiveButton(getLanguageString("确定"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (!IsWebCanBeUse
											.isWebCanBeUse(getApplicationContext())) {
										Toast.makeText(
												ShowDaHuiDianPingPhotosActivity.this,
												BaseActivity
														.getLanguageString("网络不给力"),
												Toast.LENGTH_SHORT).show();
										return;
									}

									// File f = new File(Environment
									// .getExternalStorageDirectory()
									// .getPath()
									// + "/WICPhotos/");
									File photo = new File(
											Constant.dianpingTarget);
									if (!photo.exists()) {
										photo.mkdirs();
									}
									pd = new ProgressDialog(
											ShowDaHuiDianPingPhotosActivity.this);
									pd.setMessage(BaseActivity.getLanguageString("加载中..."));
									pd.setCancelable(true);
									pd.setCanceledOnTouchOutside(false);
									pd.show();
									if (photolist.size() > 0) {
										// 启动线程下载图片
										AsyncTaskForDownLoadPhoto at = new AsyncTaskForDownLoadPhoto(
												h.obtainMessage(),
												photolist.get(position).photourl,
												ShowDaHuiDianPingPhotosActivity.this);
										at.executeOnExecutor(
												AsyncTask.THREAD_POOL_EXECUTOR,
												1);
									} else if (maplist.size() > 0) {
										// 启动线程下载图片
										AsyncTaskForDownLoadPhoto at = new AsyncTaskForDownLoadPhoto(
												h.obtainMessage(),
												maplist.get(position).imgurl,
												ShowDaHuiDianPingPhotosActivity.this);
										at.executeOnExecutor(
												AsyncTask.THREAD_POOL_EXECUTOR,
												1);
									}
								}
							})
					.setNegativeButton(getLanguageString("取消"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}

							}).show();
			return false;
		}

	}

	// -----------------------------lyy 添加上传图片 解除屏蔽s-----------------------以下
	// 暂时屏蔽 不使用menu 马晓勇
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.viewpager_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menuLockItem = menu.findItem(R.id.menu_lock);
		toggleLockBtnTitle();
		menuLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				toggleViewPagerScrolling();
				toggleLockBtnTitle();
				return true;
			}
		});

		return super.onPrepareOptionsMenu(menu);
	}

	private void toggleViewPagerScrolling() {
		if (isViewPagerActive()) {
			((HackyViewPager) mViewPager).toggleLock();
		}
	}

	private void toggleLockBtnTitle() {
		boolean isLocked = false;
		if (isViewPagerActive()) {
			isLocked = ((HackyViewPager) mViewPager).isLocked();
		}
		String title = (isLocked) ? getString(R.string.menu_unlock)
				: getString(R.string.menu_lock);
		if (menuLockItem != null) {
			menuLockItem.setTitle(title);
		}
	}

	private boolean isViewPagerActive() {
		return (mViewPager != null && mViewPager instanceof HackyViewPager);
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG,
					((HackyViewPager) mViewPager).isLocked());
		}
		super.onSaveInstanceState(outState);
	}
	// -----------------------------lyy 添加上传图片 解除屏蔽e-----------------------以下
	// 暂时屏蔽 不使用menu 马晓勇
}
