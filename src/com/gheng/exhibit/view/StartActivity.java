package com.gheng.exhibit.view;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.RongInfo;
import com.gheng.exhibit.http.body.response.YongHuMingDengLu;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.rongyun.utils.MSharePreferenceUtils;
import com.gheng.exhibit.rongyun.utils.RongCloudEvent;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.hebg3.mxy.utils.AsyncTaskForUpdateUserInfo;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.zAsyncTaskForRongGlobalGroup;
import com.hebg3.mxy.utils.zAsyncTaskForRongGlobalUser;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

public class StartActivity extends Activity implements AnimationListener {

	// ImageView startimage;
	User parent;

	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 200) {// 请求成功，更新个人信息
				YongHuMingDengLu denglu = (YongHuMingDengLu) msg.obj;
				// 创建 初始化数据库
				DbUtils db = DbUtils.create(StartActivity.this);

				if (!denglu.userid.equals("")) {
					try {
						db.dropTable(User.class);
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				User user = new User(); // 这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
				if ("".equals(denglu.userid)) {// 判断服务器返回的userid是否为空，如果为空则保存手机号。
					user.setUserId("");
				} else {
					user.setUserId(Constant.encode(Constant.key, denglu.userid));
				}

				user.setUserjuese(denglu.userjuese);// 不需要加密 数值
				user.setPassword(denglu.password);// 不需要加密，已经是MD5加密过的
				user.setName(Constant.encode(Constant.key, denglu.name));
				if (denglu.zhiwei != null)
					user.setZhiWei(Constant.encode(Constant.key, denglu.zhiwei));
				if (denglu.smallphotourl != null)
					user.setSmallPhotoUrl(Constant.encode(Constant.key,
							denglu.smallphotourl));
				if ("".equals(denglu.vipid) || denglu.vipid == null) {// 防止当前用户不是vip，造成加密异常，手动将vip字段就是0
					user.setVipid(Constant.encode(Constant.key, "0"));
				} else {
					user.setVipid(Constant.encode(Constant.key, denglu.vipid));
				}
				user.setWorkplace(Constant.encode(Constant.key,
						denglu.workplace));
				user.setRy_userId(denglu.ry_userId);
				System.out.println("aaa" + "startactivity界面的数据库保存成功！"
						+ denglu.name + denglu.password);
				try {
					db.save(user); // 服务器返回数据后，保存到本地数据库
					db.close();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
			// 不管成功与否，最后都跳转主界面
			if (parent.getUserjuese().equals("1")
					|| parent.getUserjuese().equals("0")) {
				Intent intent = new Intent(StartActivity.this,
						MainActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(StartActivity.this,
						FWMainActivity.class);
				startActivity(intent);
			}
			StartActivity.this.finish();
		}
	};
	// zyj 融云功能
	RongInfo mtoken;
	String musername;
	Context context;
	Handler ronghand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				mtoken = (RongInfo) msg.obj;
				musername = mtoken.ry_name;
				String token = mtoken.ry_token;
				connect(token);
				break;
			case 1:
				String message = (String) msg.obj;
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(context, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(context, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(context, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		context = StartActivity.this;
		if (!isTaskRoot()) {
			finish();
			return;
		}
		setContentView(R.layout.activity_start);
 
		// startimage = (ImageView) findViewById(R.id.startimage);
		// Animation anim = AnimationUtils.loadAnimation(this, R.anim.shanping);
		// anim.setAnimationListener(this);
		// startimage.startAnimation(anim);

		if (SharedData.getInt("i18n", -1) == -1) {// 首次打开程序 设置语言
			String locale = Locale.getDefault().getLanguage();
			if (locale.equals("zh")) {
				SharedData.commit("i18n", Language.ZH);
			} else {
				SharedData.commit("i18n", Language.EN);
			}
		}
		// 查找
		try {
			DbUtils db = DbUtils.create(this);

			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));

			if (parent != null) {

				if (parent.getUserId().equals("")) {// 跳转未激活界面

					new Thread() {
						public void run() {
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Intent intent = new Intent(StartActivity.this,
									NotactivationActivity.class);
							startActivity(intent);
							StartActivity.this.finish();
						}
					}.start();
				} else {// 用户已经登录成功过

					if (!IsWebCanBeUse.isWebCanBeUse(getApplicationContext())) {// 没联网直接跳转主界面
						System.out.println("用户登录过，但没联网");
						new Thread() {
							public void run() {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (parent.getUserjuese().equals("1")
										|| parent.getUserjuese().equals("0")) {
									Intent intent = new Intent(
											StartActivity.this,
											MainActivity.class);
									startActivity(intent);
								} else {
									Intent intent = new Intent(
											StartActivity.this,
											FWMainActivity.class);
									startActivity(intent);
								}
								StartActivity.this.finish();
							}
						}.start();
						return;
					}
					System.out.println("更新个人信息");
					// 启动线程更新个人信息
					AsyncTaskForUpdateUserInfo at = new AsyncTaskForUpdateUserInfo(
							this, Constant.decode(Constant.key,
									parent.getUserId()), h.obtainMessage());
					at.execute(1);
					// zyj添加在这里使用融云注册《不管网络好不好都执行》
					if (Constant.decode(Constant.key, parent.getUserId())
							.contains("fw")) {
						getToken(
								Constant.decode(Constant.key,
										parent.getUserId()), "2");
					} else {
						getToken(
								Constant.decode(Constant.key,
										parent.getUserId()), "1");
					}
				}
			} else {// 首次登录

				new Thread() {
					public void run() {
						try {
							Thread.sleep(2000);
							Intent intent = new Intent(StartActivity.this,
									DengLuActivity.class);
							intent.putExtra("yanzheng", 0);
							startActivity(intent);
							StartActivity.this.finish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 融云 - zyj 获取token
	private void getToken(String userid, String type) {

		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/vipmembers.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=getUserToken&userid=");
		strbuf.append(userid);
		strbuf.append("&type=");
		strbuf.append(type);

		String str = strbuf.toString();
		client.params = str;
		System.out.println("aaa:DengLuActivity:getToken:" + client.toString());

		NetTask<RongInfo> net = new NetTask<RongInfo>(ronghand.obtainMessage(),
				client, RongInfo.class, new RongInfo(), context);
		net.execute();
	}

	// 融云 - 建立与融云服务器的连接
	private void connect(String token) {

		if (getApplicationInfo().packageName.equals(MyApplication
				.getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
				 * Token
				 */
				@Override
				public void onTokenIncorrect() {
					// Toast.makeText(context, "token错误,连接融云失败,请重新登录",
					// Toast.LENGTH_SHORT).show();
				}

				/**
				 * 连接融云成功
				 * 
				 * @param userid
				 *            当前 token
				 */
				@Override
				public void onSuccess(String userid) {
					// Toast.makeText(context, "融云连接成功!", Toast.LENGTH_SHORT)
					// .show();

					RongCloudEvent.getInstance().setOtherListener();
					RongIM.getInstance().enableNewComingMessageIcon(true);// 显示新消息提醒
					RongIM.getInstance().enableUnreadMessageIcon(true);// 显示未读消息数目
					// 设置当前用户信息
					if (RongIM.getInstance() != null) {
						RongIM.getInstance().setCurrentUserInfo(
								new UserInfo(userid, musername, null));
					}
					// 设置消息体内是否携带用户信息
					RongIM.getInstance().setMessageAttachedUserInfo(true);

				}

				/**
				 * 连接融云失败
				 * 
				 * @param errorCode
				 *            错误码，可到官网 查看错误码对应的注释
				 *            http://www.rongcloud.cn/docs/android.html#常见错误码
				 */
				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {
					// Toast.makeText(context, "连接融云失败,请重新登录",
					// Toast.LENGTH_SHORT)
					// .show();
				}
			});
		}
	}

	@Override
	// 动画结束，开始做数据处理，并跳转登陆界面
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}
}
