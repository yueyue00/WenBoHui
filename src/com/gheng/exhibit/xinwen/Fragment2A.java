package com.gheng.exhibit.xinwen;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.NewsLieBiao;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForXinWen;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.XiaoXiPojo;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.smartdot.wenbo.huiyi.R;

@SuppressLint("ValidFragment")
public class Fragment2A extends Fragment implements OnRefreshListener {
	public String serviceid = "1";
	private Activity mContext;
	// private String cat_id;
	public boolean flag;// 编辑模式
	public String cat_position;

	public SwipeRefreshLayout swipe;
	public RecyclerView rv;
	public LinearLayoutManager llm;
	public AdapterForXinWen adapter;
	private String cat_id;
	public int pagecount = 1;// 总页数
	public int loadmode = 0;// 0 下拉刷新 1 上提分页
	public int isloading = 0;// 是否正在加载数据 0未加载 1正在加载
	/**
	 * 当前页数
	 */
	private int pagenum = 1;

	public ArrayList<XiaoXiPojo> list = new ArrayList<XiaoXiPojo>();// 消息提醒数据集合
	private List<NewsLieBiao> list_news = new ArrayList<NewsLieBiao>();
	private ProgressDialog pro;
	/** 暂无数据提示textview */
	TextView nodatafound;

	public String newsliebiaocachename = "xinwenliebiao";
	public SharedPreferences sp;
	public Gson g = new Gson();

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public List<NewsLieBiao> getList_news() {
		return list_news;
	}

	public void setList_news(List<NewsLieBiao> list_news) {
		this.list_news = list_news;
	}

	Fragment2A(ProgressDialog pro) {
		this.pro = pro;
	}
	private int ka = 0;
	/**
	 * 访问新闻列表接口返回数据
	 */
	Handler hand = new Handler() {
		public void handleMessage(Message msg) {
			swipe.setRefreshing(false);
			swipe.setEnabled(true);
			nodatafound.setVisibility(View.GONE);
			isloading = 0;
			pro.dismiss();
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				NewsLieBiao news = (NewsLieBiao) msg.obj;
				if (news.liebiao.size() != 0 || news.lunbo.size() != 0) {
					pagecount = msg.arg1;
					if (pagenum == 1) {
						if (list_news.size() != 0) {
							ka = list_news.get(0).lunbo.size();
						}

						if (ka != news.lunbo.size()) {
							list_news.clear();
							list_news.add(news);
							adapter.setList(list_news);
							rv.setAdapter(adapter);
						} else {
							list_news.clear();
							list_news.add(news);
							adapter.setList(list_news);
							adapter.notifyDataSetChanged();
						}

					} else {
						list_news.get(0).liebiao.addAll(news.liebiao);
						adapter.setList(list_news);
						adapter.notifyDataSetChanged();
					}

				} else {
					adapter.getList().clear();
					adapter.notifyDataSetChanged();
					Toast.makeText(mContext,
							BaseActivity.getLanguageString("暂时没有数据"),
							Toast.LENGTH_SHORT).show();
				}
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(mContext, R.string.network, Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 3) { // 表示没有拿到列表数据
				adapter.getList().clear();
				adapter.notifyDataSetChanged();
				nodatafound.setVisibility(View.VISIBLE);
				// Toast.makeText(mContext,
				// BaseActivity.getLanguageString("暂时没有数据"),
				// Toast.LENGTH_SHORT).show();
			} else if (msg.what == 4) {// cookie失效
				BaseActivity.gotoLoginPage(getActivity());
			}
		}
	};

	public Fragment2A() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View view = inflater.inflate(R.layout.fragment_xinwen, container);
		swipe = (SwipeRefreshLayout) view
				.findViewById(R.id.xiaoxizhongxinlistswiperefreshlayout);
		nodatafound = (TextView) view.findViewById(R.id.nodatafoundnews);
		nodatafound.setText(BaseActivity.getLanguageString("暂时没有数据"));
		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		rv = (RecyclerView) view
				.findViewById(R.id.xiaoxizhongxinlistrecyclerview);
		rv.setHasFixedSize(true);// 强制item高度一致，加强加载效率

		sp = mContext.getSharedPreferences(newsliebiaocachename,
				Activity.MODE_PRIVATE);

		llm = new LinearLayoutManager(mContext);// 设置RecyclerView的布局管理器（必须设置，否则RecyclerView会崩溃）
		llm.setOrientation(LinearLayoutManager.VERTICAL);// 设置横向或纵向展示item
		rv.setLayoutManager(llm);// 为RecyclerView配置布局管理器 必须手动配置！
		// 注册滑动监听
		rv.addOnScrollListener(new rvonscrolllistener());

		adapter = new AdapterForXinWen(mContext);// 初始化用户自定义适配器
		rv.setAdapter(adapter);// 为recyclerView设置适配器

		if (pro == null && cat_id != null) {
			pro = ProgressDialog.show(mContext, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
			pro.setCancelable(true);// 点击dialog外空白位置是否消失
			pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
		}
		if (cat_id != null) {
			/**
			 * 访问接口
			 */
			loadData();
		}

		return view;
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
			if (getDataFromCache(pagenum, cat_id, NewsLieBiao.class,
					new NewsLieBiao(),hand.obtainMessage()).equals("")) {
				if (list_news.size() != 0) {
					if (list_news.get(0).liebiao.size() <10) {
						pro.dismiss();
					}else{
						pro.dismiss();
						Toast.makeText(mContext, BaseActivity.getLanguageString("网络不给力"),
								Toast.LENGTH_SHORT).show();
					}
				}else{
					pro.dismiss();
					Toast.makeText(mContext, BaseActivity.getLanguageString("网络不给力"),
							Toast.LENGTH_SHORT).show();
				}
				
			}
			return;
		}else{
			
		// 以下内容将来会是网络访问获取数据
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/InfoPublish.do"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer(); // 封装需要请求的字段
		strbuf.append("method=MeetingNewsAction&language="); // 请求的字段名
																// 要和接口文档保持一致
		if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
			strbuf.append("2"); // 获取是中英文 2是英文 1是中文
		} else {
			strbuf.append("1");
		}
		strbuf.append("&pagenum=");
		strbuf.append(pagenum);
		strbuf.append("&pagesize=10&newstype=");
		strbuf.append(cat_id);
		String str = strbuf.toString(); // 转换成String类型
		client.params = str; // 把请求的参数封装到params 这个属性里面

		NetTask<NewsLieBiao> net = new NetTask<NewsLieBiao>(
				hand.obtainMessage(), client, NewsLieBiao.class,
				new NewsLieBiao(), mContext, 2, cat_id, pagenum); // Htpp的异步类
		net.execute(); // 相当于线程的Star方法 开始运行
		}
	}

	/**
	 * 从缓存获取json解析成对象并返回
	 * 
	 * @param pagenum
	 *            页码
	 * @return 解析后的数据集合
	 */
	@SuppressWarnings("unchecked")
	public String getDataFromCache(int pagenum, String cat_id, Class clazz,
			Object obj,Message msg) {
		if (!sp.getString("cat_id" + cat_id + "pagenum" + pagenum, "").equals(
				"")&&cat_id != null) {
			try {
				obj = (Object) Constant.gson.fromJson(
						Constant.decode(Constant.key,sp.getString("cat_id" + cat_id + "pagenum" + pagenum, "")),
						clazz);
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.obj = obj;
			msg.sendToTarget();
			return "cache";
		} else {
			return "";
		}
	}

	@Override
	// swipelayout的下拉刷新监听方法
	public void onRefresh() {
		// TODO Auto-generated method stub
		swipe.setEnabled(false);
		pagenum = 1;
		isloading = 0;
		loadData();
	}

	/**
	 * RecyclerView 的滑动监听事件，用来判断是否拉倒最后一项，自动加载下一页
	 */
	class rvonscrolllistener extends RecyclerView.OnScrollListener {

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			if (llm.findLastCompletelyVisibleItemPosition() == list_news.get(0).liebiao
					.size()-1) {// 显示到最后一行启动线程加载下一页
				System.out.println("\\==================\\"+pagecount);
				if (isloading == 0 && pagecount != 1 && cat_id != null
						&& pagecount != pagenum) {
					System.out.println("加载下一页中 请稍后");
					pro = ProgressDialog.show(mContext, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
					pro.setCancelable(true);// 点击dialog外空白位置是否消失
					pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
					loadmode = 1;
					isloading = 1;
					pagenum++;
					loadData();
				}
			} else {
				System.out.println("pagenum:+" + pagenum);
				System.out.println("pagecount:+" + pagecount);
			}
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
