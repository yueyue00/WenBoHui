package com.gheng.exhibit.view.checkin;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.a.a.ad;
import cn.jpush.a.a.o;
import cn.jpush.a.a.v;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.CommonVip;
import com.gheng.exhibit.http.body.response.EmptyJiabinBean;
import com.gheng.exhibit.http.body.response.InitData;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean.InfoBean.CommTaskBean;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean.InfoBean.JiabinBean;
import com.gheng.exhibit.http.body.response.MytaskGridViewBean;
import com.gheng.exhibit.http.body.response.VipBeanForVipList;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.MyTaskGridViewAdapter;
import com.gheng.exhibit.view.adapter.MyTaskListAdapter;
import com.gheng.exhibit.widget.CustomGridView;
import com.hebg3.mxy.utils.MyTaskListAsyncTask;
import com.hebg3.mxy.utils.MyTaskVipListTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.drawable;
import com.smartdot.wenbo.huiyi.R.id;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MytaskListActivity extends BaseActivity implements OnClickListener {
    private Button ib_back;
    private View view;
    private TextView tv_title;
    private TextView emptyView;
    private ListView listView;
    private CustomGridView mytask_cusgridView;
	
	private Context mContext;
	private String title;
	private List<CommonVip> data;
	private List<MytaskGridViewBean> gridData;
	private MyTaskListAdapter adapter;
	private MyTaskGridViewAdapter gridAdapter;
	
	private User user;
	private String userid;
	/**
	 * handlerForVipList 该handler是请求vip列表返回的信息，用于存取当嘉宾为
	 */
	private Handler handlerForVipList = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
              VipBeanForVipList bean = (VipBeanForVipList) msg.obj;
              Constant.SIGN = bean.getInfo().get(0).getSign();
              Constant.INVITATION_CODE = bean.getInfo().get(0).getVipJiabin().get(0).getInvitationCode();
              Constant.VIP_NAME = bean.getInfo().get(0).getVipJiabin().get(0).getName();
              System.out.println("========vip嘉宾列表的人数为：---》"+Constant.SIGN);
              System.out.println("=======单个vip嘉宾的唯一标识为：---》"+Constant.INVITATION_CODE);
				break;
			case -1:
               Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
				break;
			case -2:
//				Constant.SIGN = -1;
	               Toast.makeText(mContext, "请求数据info为空", Toast.LENGTH_LONG).show();
					break;
			case 300:
//	               Toast.makeText(mContext, "您没有绑定嘉宾  hahahaah", Toast.LENGTH_LONG).show();
					break;
			case 400:
	               Toast.makeText(mContext, "请求失败", Toast.LENGTH_LONG).show();
					break;
			case 500:
	               Toast.makeText(mContext, "请求超时", Toast.LENGTH_LONG).show();
					break;
			default:
				break;
			}
		};
	};
	/**
	 * 请求到固定任务已经普通嘉宾列表的数据
	 */
	private List<CommTaskBean> commTask  = new ArrayList<>();
	private List<JiabinBean> jiabin = new ArrayList<>();
     private Handler handler = new Handler(){
    	 public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//                String result = (String) msg.obj;
//                System.out.println("========MyTaskListActivity===>"+result);
				MyTaskListDataBean result = (MyTaskListDataBean) msg.obj;
				//普通嘉宾列表
				 jiabin.clear();
		     	 jiabin.addAll(result.getInfo().get(0).getJiabin());
		     	 adapter.notifyDataSetChanged();
//		     	   addHeaderViewForListView();
//		     		adapter = new MyTaskListAdapter(mContext, jiabin);
//		     		listView.setAdapter(adapter);
				//接机等固定任务
		     	 commTask.clear();
				 commTask.addAll(result.getInfo().get(0).getCommTask());
				 gridAdapter.notifyDataSetChanged();
//				gridAdapter = new MyTaskGridViewAdapter(mContext, commTask);
//		     	mytask_cusgridView.setAdapter(gridAdapter);
		    	
				System.out.println("------MyTaskListDataBean---->"+result.getMessage());
				break;
			case -1:
				Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
				break;
			case -2:
				Toast.makeText(mContext, "请求数据info为空", Toast.LENGTH_LONG).show();
				break;
			case 3:
				EmptyJiabinBean emptyJiabinBean =  (EmptyJiabinBean) msg.obj;
				System.out.println("------EmptyJiabinBean---->"+emptyJiabinBean.getMessage());
				Toast.makeText(mContext, "请求数据info为空", Toast.LENGTH_LONG).show();
				break;
			case 300:
//				  String resultStr = (String) msg.obj;
//                Toast.makeText(mContext, resultStr, Toast.LENGTH_LONG).show();
                  emptyView.setVisibility(View.VISIBLE);
                  listView.setVisibility(View.GONE);
                 
				break;
			case 400:
				Toast.makeText(mContext, "请求失败", Toast.LENGTH_LONG).show();
				break;
			case 500:
				Toast.makeText(mContext, "请求超时", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
    	 };
     };	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mytask_list);
		mContext = this;
		Intent intent = getIntent();
        title = intent.getStringExtra("title");
		initView();
//		setStatusBar();
		getUserId();
//		getVipListSign();
//		loadData();
		addHeaderViewForListView();
 		adapter = new MyTaskListAdapter(mContext, jiabin);
 		listView.setAdapter(adapter);
 		gridAdapter = new MyTaskGridViewAdapter(mContext, commTask);
     	mytask_cusgridView.setAdapter(gridAdapter);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getVipListSign();
		loadData();
		
	}
	/**
	 * 请求MyTaskListActivity所需要的数据
	 */
	private void loadData() {
		try {
			new MyTaskListAsyncTask(handler.obtainMessage(), userid, mContext)
					.execute(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取到请求网络数据的userid参数
	 */
	public void getUserId() {
		// 查找
				try {
					DbUtils db = DbUtils.create(mContext);
					user = db
							.findFirst(Selector.from(User.class).where("id", "=", "1"));
					// user.getUserjuese().equals("3") ?
					// Constant.decode(Constant.key,user.getVipid()) :
					// Constant.decode(Constant.key,user.getUserId())
					// URLEncoder.encode(Constant.encode(Constant.key, userid),"UTF-8")
					userid = Constant.decode(Constant.key, user.getUserId());
					Log.d("tag", "=======MyTaskVipListActivity--->userId=" + userid);
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	/**
	 * 获取到vip列表的vip人数，sign属性  为0--》1个嘉宾  为1----》多个嘉宾
	 */
	private void getVipListSign() {
		
			try {
				new MyTaskVipListTask(handlerForVipList.obtainMessage(), userid, mContext)
						.execute(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	/**
	 * 初始化view
	 */
	private void initView() {
     		ib_back = (Button) findViewById(R.id.but_fanhui);
//     		view = findViewById(R.id.mytasklist_view);
     		tv_title = (TextView) findViewById(R.id.in_title);
     		tv_title.setText(title);
     		emptyView = (TextView) findViewById(R.id.mytasklist_tv_empty);
     		listView = (ListView) findViewById(R.id.mytasklist_listView);
     		ib_back.setOnClickListener(this);
     		
	}
	/**
	 * 给listview添加headerView，且添加的headerview的布局为一个GridView
	 * 这样的话会出现一个问题就是gridview不显示的问题
	 * 解决办法就是自定义一个GridView重写其OnMeasure（）方法
	 */
	public void addHeaderViewForListView() {
		View headerView = LayoutInflater.from(mContext).inflate(R.layout.mytask_list_headerview, null);
		listView.addHeaderView(headerView);
        mytask_cusgridView = (CustomGridView) headerView.findViewById(R.id.mytask_customgridView);

        
	}

	/**
	 * 透明状态栏 
	 */
//	public void setStatusBar() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  
//            Window window = getWindow();  
//            // Translucent status bar  
//            window.setFlags(  
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
//        } else {
//			view.setVisibility(View.GONE);
//		}
//	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.but_fanhui:
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		
	}
}
