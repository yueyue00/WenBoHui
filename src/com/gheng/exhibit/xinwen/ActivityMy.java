package com.gheng.exhibit.xinwen;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.NewsLanMu;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.widget.MyGridAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.hebg3.wl.net.ResponseBody;
import com.smartdot.wenbo.huiyi.R;

/**
 * @author 赵跃俊
 */
@SuppressLint("ResourceAsColor")
public class ActivityMy extends FragmentActivity implements OnClickListener {

	private FragmentManager fm;
	private ArrayList<Fragment2A> fragments = new ArrayList<Fragment2A>();
	private ArrayList<Fragment_TongXiangZhiChuang> fragments_tongxiang = new ArrayList<Fragment_TongXiangZhiChuang>();
	private ArrayList<Fragment_JingCaiHuiGu> fragments_jchg = new ArrayList<Fragment_JingCaiHuiGu>();
	private Context mContext = this;
	private MyAdapter adapter2;
	private MyAdapterTX adaptertx;
	private MyAdapterJingCaiHuiGu adapterjchg;
	private MyViewPager viewPager;
	private Boolean isActive = true;
	private WindowManager wm;
	private HorizontalScrollView myhorizontalScrollView;
	private GridView mygridView;
	private TextView nodatafound;
	// 上一个界面传递过来判断是大会新闻还是桐乡之窗还是精彩瞬间
	private String my;
	TextView in_title;
	Button but_fanhui;
	// 请求网络提示对话框
	ProgressDialog pro;
	// 顶部滑动需要的属性
	private List<NewsLanMu> list;
	private ArrayList<String> titles = new ArrayList<String>();
	private MyGridAdapter mygadapter;
	private int ScreenWidth;
	LocalActivityManager manager = null;

	private int _id = 1000;
	private int widpx = 0;
	ResponseBody<NewsLanMu> body;
	public SharedPreferences sp;
	public String newslanmucachename = "xinwenlanmu";
	public Gson g = new Gson();

	/**
	 * 获取大会新闻类型handler
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				ResponseBody<NewsLanMu> res = (ResponseBody<NewsLanMu>) msg.obj; // 首先创建接收方法
				list = res.list; // 声明List的泛型
									// 其实就是拿到ResponseBody<HuiYiRiChengRiQiHuoQu>里面的list对象
				initGridView();
				initFragment();
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				pro.dismiss();
				Toast.makeText(mContext,
						BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				pro.dismiss();
				Toast.makeText(mContext,
						BaseActivity.getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 3) { // 表示没有拿到列表数据
				pro.dismiss();
				Toast.makeText(mContext,
						BaseActivity.getLanguageString("暂时没有数据"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(ActivityMy.this);
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// TODO Auto-generated method stub
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment2);
		registerBoradcastReceiver();// 注册广播
		initBaiduTongji();// 百度统计
		// 测量手机尺寸
		DisplayMetrics dm = new DisplayMetrics();
		dm = this.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		widpx = px2dip(this, screenWidth);

		sp = getSharedPreferences(newslanmucachename, Activity.MODE_PRIVATE);

		my = getIntent().getExtras().getString("activitymy");

		in_title = (TextView) findViewById(R.id.in_title);
		but_fanhui = (Button) findViewById(R.id.but_fanhui);

		myhorizontalScrollView = (HorizontalScrollView) findViewById(R.id.fragment2_horizontalScrollView);
		mygridView = (GridView) findViewById(R.id.GridView);
		nodatafound = (TextView) findViewById(R.id.nodatafound);
		in_title.setText(BaseActivity.getLanguageString(my));

		fm = getSupportFragmentManager();
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		ScreenWidth = wm.getDefaultDisplay().getWidth();
		viewPager = (MyViewPager) findViewById(R.id.fragment2_vPager);

		but_fanhui.setOnClickListener(this);
		loadData();
		setAllClick();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (!IsWebCanBeUse.isWebCanBeUse(this)) {
			if (my.equals("大会新闻")) {
				if (getDataFromCache(hand.obtainMessage()).equals("")) {
					pro.dismiss();
					Toast.makeText(this,
							BaseActivity.getLanguageString("网络不给力"),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			}
		} else {

			// 加载网络时等待对话框
			pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
			pro.setCancelable(true);// 点击dialog外空白位置是否消失
			pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
			// 以下内容将来会是网络访问获取数据
			ClientParams client = new ClientParams(); // 创建一个新的Http请求
			client.url = "/InfoPublish.do"; // Http 请求的地址 前面的域名封装好了
			StringBuffer strbuf = new StringBuffer(); // 封装需要请求的字段
			strbuf.append("method="); // 请求的字段名
										// 要和接口文档保持一致
			if (my.equals("大会新闻")) {
				strbuf.append("MeetingNewsTypeAction");
			} else if (my.equals("嘉兴之窗")) {
				strbuf.append("TongXaingTypeAction");
			} else if (my.equals("精彩瞬间")) {
				strbuf.append("MultimediaAction");
			}
			strbuf.append("&language=");
			if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				strbuf.append("2"); // 获取是中英文 2是英文 1是中文
			} else {
				strbuf.append("1");
			}
			String str = strbuf.toString(); // 转换成String类型
			client.params = str; // 把请求的参数封装到params 这个属性里面

			// 调用数组
			Type type = new TypeToken<ArrayList<NewsLanMu>>() { // json返回值为数组时需要创建一个Type对象
				// Json 解析
			}.getType();
			if (my.equals("大会新闻")) {
				NetTask<NewsLanMu> net = new NetTask<NewsLanMu>(
						hand.obtainMessage(), client, type, mContext, 1); // Htpp的异步类
				net.execute(); // 相当于线程的Star方法 开始运行
			} else {// 大会新闻才缓存，其他不缓存
				NetTask<NewsLanMu> net = new NetTask<NewsLanMu>(
						hand.obtainMessage(), client, type, mContext, 100); // Htpp的异步类
				net.execute(); // 相当于线程的Star方法 开始运行
			}
		}
	}

	/**
	 * 从缓存获取json解析成对象并返回
	 * 
	 * @param pagenum
	 *            页码
	 * @return 解析后的数据集合
	 */
	public String getDataFromCache(Message msg) {
		if (!sp.getString("newslanmu", "").equals("")) {
			body = new ResponseBody<NewsLanMu>();
			Type type = new TypeToken<ArrayList<NewsLanMu>>() {
			}.getType();// 设置集合type
			try {
				body.list = g.fromJson(
						Constant.decode(Constant.key,
								sp.getString("newslanmu", "")), type);
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.obj = body;
			msg.sendToTarget();
			return "cache";
		} else {
			return "";
		}
	}

	// 初始化GridView
	private void initGridView() {
		if (list.size() <= 1) {
			myhorizontalScrollView.setVisibility(View.GONE);
		} else {
			myhorizontalScrollView.setVisibility(View.VISIBLE);
		}
		ViewGroup.LayoutParams params = mygridView.getLayoutParams();
		if (list.size() > 4) {
			int gvwid = 90;
			if (widpx > 400) {
				gvwid = 150;
			}
			params.width = DensityUtil.dip2px(mContext, gvwid) * list.size();
			mygridView.setColumnWidth(DensityUtil.dip2px(mContext, gvwid));
		} else {
			params.width = ScreenWidth;
			if (list.size() > 0) {
				mygridView.setColumnWidth(ScreenWidth / list.size());
			}
		}
		mygridView.setNumColumns(list.size());// 设置行数
		mygridView.setLayoutParams(params);
		mygridView.setGravity(Gravity.CENTER);
		mygridView.setChoiceMode(1);
		for (NewsLanMu iterable_element : list) {
			String str = iterable_element.newsname;
			titles.add(str);
		}
		mygadapter = new MyGridAdapter(mContext, titles);
		mygridView.setAdapter(mygadapter);
	}

	// 初始化Fragment
	private void initFragment() {
		if (my.equals("大会新闻")) {
			fragments.clear();// 清空
			int count = list.size();
			if (count <= 0) {
				nodatafound.setText(BaseActivity.getLanguageString("暂时没有数据"));
				nodatafound.setVisibility(View.VISIBLE);
			} else {
				viewPager.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < count; i++) {
				Fragment2A frag1 = new Fragment2A(pro);
				frag1.setCat_id(list.get(i).newstype);
				fragments.add(frag1);
			}

			adapter2 = new MyAdapter(mContext, fragments, fm);
			// mViewPager.setOffscreenPageLimit(0);
			viewPager.setAdapter(adapter2);
			viewPager.setOnPageChangeListener(pageListener);
		} else if (my.equals("嘉兴之窗")) {

			fragments_tongxiang.clear();// 清空
			int count = list.size();
			if(count<=0){
				nodatafound.setText(BaseActivity.getLanguageString("暂时没有数据"));
				nodatafound.setVisibility(View.VISIBLE);
			}else{
				viewPager.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < count; i++) {
				Fragment_TongXiangZhiChuang frag1 = new Fragment_TongXiangZhiChuang(
						pro);
				frag1.setCat_id(list.get(i).newstype);
				fragments_tongxiang.add(frag1);
			}

			adaptertx = new MyAdapterTX(mContext, fragments_tongxiang, fm);
			// mViewPager.setOffscreenPageLimit(0);
			viewPager.setAdapter(adaptertx);
			viewPager.setOnPageChangeListener(pageListener);
		} else if (my.equals("精彩瞬间")) {
			fragments_jchg.clear();// 清空
			int count = list.size();
			if(count<=0){
				nodatafound.setText(BaseActivity.getLanguageString("暂时没有数据"));
				nodatafound.setVisibility(View.VISIBLE);
			}else{
				viewPager.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < count; i++) {
				Fragment_JingCaiHuiGu frag1 = new Fragment_JingCaiHuiGu(pro);
				frag1.setCat_id(list.get(i).newstype);
				fragments_jchg.add(frag1);
			}

			adapterjchg = new MyAdapterJingCaiHuiGu(mContext, fragments_jchg,
					fm);
			// mViewPager.setOffscreenPageLimit(0);
			viewPager.setAdapter(adapterjchg);
			viewPager.setOnPageChangeListener(pageListener);
		}
	}

	private void setAllClick() {
		mygridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				viewPager.setCurrentItem(position);
				mygadapter.refresh(position);
			}
		});

	}

	/**
	 * ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			if (list.size() > 4) {
				int scrollX = myhorizontalScrollView.getScrollX();
				int left = mygridView.getChildAt(position).getLeft();
				int leftScreen = left - scrollX;
				myhorizontalScrollView.smoothScrollBy(leftScreen
						- wm.getDefaultDisplay().getWidth() / 2
						+ mygridView.getChildAt(position).getWidth() / 2, 0);
			}
			mygadapter.refresh(position);
		}
	};

	private class MyAdapter extends PagerAdapter {
		private List<Fragment2A> fragments; // 每个Fragment对应一个Page
		private FragmentManager fragmentManager;

		private MyAdapter(Context context, List<Fragment2A> fragments,
				FragmentManager fragmentManager) {
			this.fragments = fragments;
			this.fragmentManager = fragmentManager;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(fragments.get(position).getView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = fragments.get(position);
			if (!fragment.isAdded()) { // 如果fragment还没有added
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.add(fragment, fragment.getClass().getSimpleName());
				ft.commit();
				/**
				 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
				 * 会在进程的主线程中，用异步的方式来执行。 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
				 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
				 */
				fragmentManager.executePendingTransactions();
			}

			if (fragment.getView().getParent() == null) {
				container.addView(fragment.getView()); // 为viewpager增加布局
			}

			return fragment.getView();
		}

	}

	private class MyAdapterTX extends PagerAdapter {
		private List<Fragment_TongXiangZhiChuang> fragments; // 每个Fragment对应一个Page
		private FragmentManager fragmentManager;

		private MyAdapterTX(Context context,
				List<Fragment_TongXiangZhiChuang> fragments,
				FragmentManager fragmentManager) {
			this.fragments = fragments;
			this.fragmentManager = fragmentManager;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(fragments.get(position).getView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = fragments.get(position);
			if (!fragment.isAdded()) { // 如果fragment还没有added
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.add(fragment, fragment.getClass().getSimpleName());
				ft.commit();
				/**
				 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
				 * 会在进程的主线程中，用异步的方式来执行。 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
				 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
				 */
				fragmentManager.executePendingTransactions();
			}

			if (fragment.getView().getParent() == null) {
				container.addView(fragment.getView()); // 为viewpager增加布局
			}

			return fragment.getView();
		}

	}

	// -------------------------------------马晓勇加----------
	// 精彩瞬间------------------------------------------------
	/**
	 * 马晓勇加 精彩瞬间
	 */
	private class MyAdapterJingCaiHuiGu extends PagerAdapter {
		private List<Fragment_JingCaiHuiGu> fragments; // 每个Fragment对应一个Page
		private FragmentManager fragmentManager;

		private MyAdapterJingCaiHuiGu(Context context,
				List<Fragment_JingCaiHuiGu> fragments,
				FragmentManager fragmentManager) {
			this.fragments = fragments;
			this.fragmentManager = fragmentManager;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(fragments.get(position).getView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = fragments.get(position);
			if (!fragment.isAdded()) { // 如果fragment还没有added
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.add(fragment, fragment.getClass().getSimpleName());
				ft.commit();
				/**
				 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
				 * 会在进程的主线程中，用异步的方式来执行。 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
				 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
				 */
				fragmentManager.executePendingTransactions();
			}

			if (fragment.getView().getParent() == null) {
				container.addView(fragment.getView()); // 为viewpager增加布局
			}

			return fragment.getView();
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == but_fanhui.getId()) {
			ActivityMy.this.finish();
		}
	}

	/**
	 * 手动设置背景图片 降低分辨率 Resources对象 背景图片id 控件
	 */
	public void compressPhotoAndSetPhoto(Resources re, Integer id, View v) {
		Bitmap bitmap = null;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// inJustDecodeBounds如果设置为true，解码器将返回null（没有位图的bitmap对象）
		// 但是调用者仍然可以查询位图的实际像素，系统并没有为位图像素分配内存
		opt.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeResource(re, id, opt);

		// 获取到这个图片的原始宽度和高度
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		// 期望的图片宽和高 比如说320×240
		int requestwidth = 480;
		int requestheight = 240;

		// isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
		opt.inSampleSize = 1;
		// 根据屏的大小和图片大小计算出缩放比例
		if (picWidth > requestwidth || picHeight > requestheight) {
			if (picWidth > picHeight) {// 横着的图片
				opt.inSampleSize = picWidth / requestwidth;
			} else {// 竖着的图片或正方形图片
				opt.inSampleSize = picHeight / requestheight;
			}
		}
		System.out.println(opt.inSampleSize);
		// 这次再真正地生成一个有像素的，分辨率经过处理的bitmap
		opt.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeResource(re, id, opt);
		v.setBackgroundDrawable(new BitmapDrawable(bitmap));
	}

	// public void onResume() {
	// super.onResume();
	// 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
	// 不能与StatService.onPageStart一级onPageEnd函数交叉使用
	// StatService.onResume(this);
	// }

	public void onPause() {
		super.onPause();

		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onPause(this);
	}

	/**
	 * 初始化百度统计
	 */
	public void initBaiduTongji() {

		/*
		 * 设置渠道的推荐方法。该方法同setAppChannel（String），
		 * 如果第三个参数设置为true（防止渠道代码设置会丢失的情况），将会保存该渠道，每次设置都会更新保存的渠道，
		 * 如果之前的版本使用了该函数设置渠道
		 * ，而后来的版本需要AndroidManifest.xml设置渠道，那么需要将第二个参数设置为空字符串,并且第三个参数设置为false即可。
		 * appChannel是应用的发布渠道，不需要在mtj网站上注册，直接填写就可以 该参数也可以设置在AndroidManifest.xml中
		 */
		StatService.setAppChannel(this, "乌镇会议", true);
		// 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动退出会产生大量日志。
		StatService.setSessionTimeOut(1);
		/*
		 * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s到30s之间<br/>
		 * 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
		 * 
		 * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/>
		 * 这个参数的设置暂时只支持代码加入， 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
		 */
		StatService.setLogSenderDelayed(0);
		/*
		 * 用于设置日志发送策略 嵌入位置：Activity的onCreate()函数中
		 * 
		 * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum.
		 * SET_TIME_INTERVAL, 1, false); 第二个参数可选： SendStrategyEnum.APP_START
		 * SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
		 * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、
		 * 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
		 * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
		 */
		StatService.setSendLogStrategy(this,
				SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
		// 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
		StatService.setDebugOn(false);

	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存
		if (mBroadcastReceiver != null) {
			this.unregisterReceiver(mBroadcastReceiver);
		}
		MyApplication.remove(this);
		super.onDestroy();
	}

	/** 记录前台切后台时间 */
	private Long TimeStart;
	/** 记录后台切前台时间 */
	private Long TimeEnd;
	/**
	 * 接收广播
	 **/
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				if (BaseActivity.isAppOnForeground(context)) {
					isActive = false;
					TimeStart = System.currentTimeMillis() / 1000;
				}
			}
		}
	};

	/**
	 * 注册广播
	 **/
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 挂起时调用的方法
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (!BaseActivity.isAppOnForeground(this)) {
			isActive = false;
			TimeStart = System.currentTimeMillis() / 1000;
		}
	}

	/**
	 * 唤醒时调用的方法
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onResume(this);

		if (!isActive) {
			TimeEnd = System.currentTimeMillis() / 1000;
			Integer time = getResources().getInteger(R.integer.time);
			if (TimeStart == null || TimeEnd - TimeStart >= time) {// 切换后台记录时间为空或者切换后台记录时间减去进入前台记录时间等于1800秒(30分钟)进入判断
				BaseActivity.loginValidation(this);
			}
		}
	}

	public static int px2dip(Context context, int pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
