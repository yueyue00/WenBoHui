package com.gheng.exhibit.view.checkin.checkin;

import java.util.ArrayList;
import java.util.List;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.InitData;
import com.gheng.exhibit.http.body.response.VipBeanForVipList;
import com.gheng.exhibit.http.body.response.VipBeanForVipList.InfoBean.VipJiabinBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.MyTaskActivity;
import com.gheng.exhibit.view.adapter.MyTaskVipListAdapter;
import com.gheng.exhibit.view.checkin.VIPInfoActivity;
import com.hebg3.mxy.utils.MyTaskVipListTask;
import com.hebg3.mxy.utils.VipInfoTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.smartdot.wenbo.huiyi.R;
import com.smartdot.wenbo.huiyi.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MytaskVipListActivity extends Activity implements OnClickListener {
     
	private Button ib_back;
	private TextView tv_title;
	private View view;
	private ListView listView;
	
	private Context mContext;
	private String title;
	private List<VipBeanForVipList> data;
	private MyTaskVipListAdapter adapter;
	private User user;
	private String userid;
	private List<VipJiabinBean> vipJiabin ;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//               String result = (String) msg.obj;
//               System.out.println("========>vip嘉宾列表："+result);
				VipBeanForVipList bean = (VipBeanForVipList) msg.obj;
				  vipJiabin = bean.getInfo().get(0).getVipJiabin();
				 adapter = new MyTaskVipListAdapter(mContext, vipJiabin);
				 listView.setAdapter(adapter);
				 listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

						Intent intent = new Intent(MytaskVipListActivity.this,MyTaskActivity.class);
						intent.putExtra("title", vipJiabin.get(position).getName());
						intent.putExtra("vipid", vipJiabin.get(position).getInvitationCode());
						startActivity(intent);
						
					}
				});
				break;
			case -1:
               Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
				break;
			case -2:
	               Toast.makeText(mContext, "请求数据info为空", Toast.LENGTH_LONG).show();
					break;
			case 300:
	               Toast.makeText(mContext, "请求数据为空", Toast.LENGTH_LONG).show();
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
            window.setFlags(  
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,  
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
        }
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mytask_vip_list);
		mContext = this;
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		initView();
//		setStatusBar();
//		initData();
		loadData();
	}
	/**
	 * 从网络请求数据
	 */
	private void loadData() {
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
					Log.d("tag", "=======MyTaskVipListActivity--->userid=" + userid);
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
					try {
						new MyTaskVipListTask(handler.obtainMessage(), userid, mContext)
								.execute(1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	}
	/**
	 * 初始化数据
	 */
//	private void initData() {
//     data = new ArrayList<>();
//     data.clear();
//     for (int i = 0; i < 10; i++) {
//		VipBeanForVipList bean = new VipBeanForVipList();
//		bean.setId(i+"");
//		bean.setName("冈崎朋也");
//		if (i % 2 == 0) {
//			bean.setImgPath("http://img.blog.163.com/photo/E3PpZjvsLyvscTmXcVp9HA==/5735897075410318057.jpg");
//		}else {
//			bean.setImgPath("http://imgsrc.baidu.com/forum/w%3D580/sign=83808419184c510faec4e212505b2528/0cf431adcbef76094ff2c34e2cdda3cc7dd99e61.jpg");
//		}
//		data.add(bean);
//	}
//     
//     adapter = new MyTaskVipListAdapter(mContext, data);
//	 listView.setAdapter(adapter);
//	 listView.setOnItemClickListener(new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//
//			Intent intent = new Intent(MytaskVipListActivity.this,MyTaskActivity.class);
//			intent.putExtra("title", "vip嘉宾任务");
//			intent.putExtra("vipid", data.get(position).getId());
//			startActivity(intent);
//			
//		}
//	});
//	}
	/**
	 * 初始化view
	 */
	private void initView() {
     		ib_back = (Button) findViewById(R.id.but_fanhui);
     		tv_title = (TextView) findViewById(R.id.in_title);
     		tv_title.setText(title);
//     		view = findViewById(R.id.mtvip_view);
     		listView = (ListView) findViewById(R.id.mtvip_lv);
     		ib_back.setOnClickListener(this);
     		
	}
	/**
	 * 沉浸式状态栏
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
}
