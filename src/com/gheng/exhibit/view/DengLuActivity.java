package com.gheng.exhibit.view;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dtr.zxing.activity.CaptureActivity;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.DengLuYanZhi;
import com.gheng.exhibit.http.body.response.RongInfo;
import com.gheng.exhibit.http.body.response.YongHuMingDengLu;
import com.gheng.exhibit.http.body.response.loginYanZhengMa;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.rongyun.utils.RongCloudEvent;
import com.gheng.exhibit.utils.Base64;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.EncodEncryUtil;
import com.gheng.exhibit.utils.SharedData;
import com.hebg3.mxy.utils.zAsyncTaskForRongGlobalGroup;
import com.hebg3.mxy.utils.zAsyncTaskForRongGlobalUser;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetLoginTask;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

@SuppressLint({ "ResourceAsColor", "ShowToast" })
public class DengLuActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.denglu_btv)
	TextView denglu_btv;
	// 发送验证码button
	@ViewInject(R.id.button_fasongyanzhengma)
	Button button_fasongyanzhengma;
	@ViewInject(R.id.iv_logo)
	ImageView iv_logo;
	@ViewInject(R.id.iv_logo2)
	ImageView iv_logo2;
	@ViewInject(R.id.iv_text)
	ImageView iv_text;
	TimeCount time;
	// 切换为手机用户
	@ViewInject(R.id.text_qiehuanshouji)
	TextView text_qiehuanshouji;
	@ViewInject(R.id.linear_shoujifangshi)
	LinearLayout linear_shoujifangshi;
	// 切换为其他方式登陆
	@ViewInject(R.id.text_qiehuanweiqita)
	TextView text_qiehuanweiqita;
	@ViewInject(R.id.linear_qitafangshi)
	LinearLayout linear_qitafangshi;
	// 用户名登陆按钮
	@ViewInject(R.id.but_qitafangshidenglu)
	Button but_qitafangshidenglu;
	// 手机号登陆按钮
	@ViewInject(R.id.but_shoujihaodenglu)
	Button but_shoujihaodenglu;
	// 用户名输入框
	@ViewInject(R.id.login_username)
	EditText login_username;
	// 密码输入框
	@ViewInject(R.id.edit_shurumima)
	EditText edit_shurumima;
	// 登陆等待对话框
	ProgressDialog pro;
	// 手机号码输入框
	@ViewInject(R.id.edit_phone)
	EditText edit_phone;
	// 获取手机号+86
	@ViewInject(R.id.edit_quhao)
	EditText edit_quhao;
	// 请输入验证码
	@ViewInject(R.id.yanzhengmaedittext)
	EditText yanzhengmaedittext;
	// 密码
	@ViewInject(R.id.text_mima)
	TextView text_mima;
	// 用户名
	@ViewInject(R.id.yonghumingtv)
	TextView yonghumingtv;
	// 验证码发送成功后接口返回数据对象
	loginYanZhengMa yanzhengma;

	private Context context = this;
	private int ispd = 0;// 判断是点击手机号登陆还是用户名密码登陆 “0”为手机号登陆 “1” 为用户名密码登陆判断提示语
	public String phonenumbername = "phonenumber";
	public SharedPreferences sp;
	public Editor e;
	DbUtils db;
	/**
	 * 发送验证码handler
	 */
	Handler hand_yzm = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				time.start();
				yanzhengma = (loginYanZhengMa) msg.obj;
				Toast.makeText(context,
						BaseActivity.getLanguageString("验证码发送成功"), 1000).show();
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context,
						BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else { // 表示服务器出现问题，返回的CODE不正确
				System.out.println("msg.obj = " + msg.obj);
				Toast.makeText(context,
						BaseActivity.getLanguageString((String) msg.obj),
						Toast.LENGTH_SHORT).show();
			}
			if (pro != null)
				pro.dismiss();
		}
	};
	/**
	 * 获取盐值访问接口返回的handler
	 */
	Handler handyz = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				DengLuYanZhi yanzhi = (DengLuYanZhi) msg.obj;
				String sal = new String(Base64.decode(yanzhi.salt));

				String username = login_username.getText().toString().trim();
				String password = edit_shurumima.getText().toString().trim();
				// System.out.println(sal+"=-=="+password+"==="+EncodEncryUtil.Salt(password,
				// sal));
				ClientParams client = new ClientParams();
				client.url = "/hylogin.do";
				StringBuffer strbuf = new StringBuffer();
				// des加密后
				strbuf.append("method=passwordLogin&userid=");
				try {
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key, username), "UTF-8"));
					strbuf.append("&password=");
					// System.out.println();
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key,
									EncodEncryUtil.Salt(password, sal)),
							"UTF-8"));
					strbuf.append("&lg=");
					if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
						strbuf.append(URLEncoder.encode(
								Constant.encode(Constant.key, "2"), "UTF-8")); // 获取是中英文
						// 2是英文 1是中文
					} else {
						strbuf.append(URLEncoder.encode(
								Constant.encode(Constant.key, "1"), "UTF-8"));
					}
					strbuf.append("&phonetype=");
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key, "android"), "UTF-8"));
					strbuf.append("&type=");
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key, "2"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String str = strbuf.toString();
				client.params = str;
				NetLoginTask<YongHuMingDengLu> net = new NetLoginTask<YongHuMingDengLu>(
						hand.obtainMessage(), client, YongHuMingDengLu.class,
						new YongHuMingDengLu(), context);
				net.execute();
			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context,
						BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
				if (pro != null)
					pro.dismiss();
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				Toast.makeText(context,
						BaseActivity.getLanguageString((String) msg.obj),
						Toast.LENGTH_SHORT).show();
				if (pro != null)
					pro.dismiss();
			} else {
				if (pro != null)
					pro.dismiss();
			}
		}
	};
	YongHuMingDengLu denglu;
	/**
	 * 输入用户名密码登陆访问接口返回的handler
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				// 将 cookie保存到SharedPreferences
				denglu = (YongHuMingDengLu) msg.obj;
				// 创建 初始化数据库
				User user = new User(); // 这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
				if ("".equals(denglu.userid)) {// 判断服务器返回的userid是否为空，如果为空则保存手机号。
					try {
						e.putString("phonequhao", Constant.encode(Constant.key,
								edit_quhao.getText().toString()));
						e.putString("phonenumber", Constant.encode(
								Constant.key, edit_phone.getText().toString()));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.apply();
					user.setUserId("");
				} else {
					user.setUserId(Constant.encode(Constant.key, denglu.userid));
					e.clear();
					e.commit();
				}
				user.setUserjuese(denglu.userjuese);// 不需要加密 数值
				user.setPassword(denglu.password);// 不需要加密，已经是MD5加密过的

				user.setName(Constant.encode(Constant.key, denglu.name));
				if (denglu.zhiwei != null)
					user.setZhiWei(Constant.encode(Constant.key, denglu.zhiwei));
				if (denglu.smallphotourl != null)
					user.setSmallPhotoUrl(Constant.encode(Constant.key,
							denglu.smallphotourl));
				if ("".equals(denglu.vipid)) {// 防止当前用户不是vip，造成加密异常，手动将vip字段就是0
					denglu.vipid = "0";
				}
				if (denglu.userjuese.equals("1")) {// 登陆角色为VIP是手动将vipid内容填写为userid
					user.setVipid(Constant.encode(Constant.key, denglu.userid));
				} else {
					// user.setVipid(Constant.encode(Constant.key,
					// denglu.vipid));
				}
				user.setWorkplace(Constant.encode(Constant.key,
						denglu.workplace));
				user.setRy_userId(denglu.ry_userId);
				try {
					db.save(user); // 服务器返回数据后，保存到本地数据库
				} catch (DbException e) {
					e.printStackTrace();
				} // 使用saveBindingId保存实体时会为实体的id赋值
				if (denglu.userid.equals("")) {
					Toast.makeText(context,
							BaseActivity.getLanguageString("手机号未激活"),
							Toast.LENGTH_SHORT);
					Intent intent = new Intent(DengLuActivity.this,
							NotactivationActivity.class);
					startActivity(intent);
				} else {
					// // zyj 新增获取用户融云信息和群组融云信息
					// zAsyncTaskForRongGlobalUser at = new
					// zAsyncTaskForRongGlobalUser(
					// ronguser_handler.obtainMessage(), context,
					// denglu.userid,db);
					// at.execute(1);
					// zyj添加在这里使用融云注册
					musername = denglu.name;
					connect(denglu.ry_token);
					if (denglu.userjuese.equals("1")
							|| denglu.userjuese.equals("0")) {
						Intent intent = new Intent(DengLuActivity.this,
								MainActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(DengLuActivity.this,
								FWMainActivity.class);
						startActivity(intent);
					}
				}
				// DengLuActivity.this.finish();

			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				if (pro != null)
					pro.dismiss();
				Toast.makeText(context,
						BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == 2) { // 表示服务器出现问题，返回的CODE不正确
				if (pro != null)
					pro.dismiss();
				if (ispd == 0) {
					Toast.makeText(context,
							BaseActivity.getLanguageString((String) msg.obj),
							Toast.LENGTH_SHORT).show();
				} else if (ispd == 1) {
					Toast.makeText(context,
							BaseActivity.getLanguageString("用户名或密码错误"),
							Toast.LENGTH_SHORT).show();
				}

			}
			// pro.dismiss();
		}
	};
	// zyj融云用户信息的handler
	Handler ronguser_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 获取融云user信息
				String message = (String) msg.obj;
				zAsyncTaskForRongGlobalGroup at = new zAsyncTaskForRongGlobalGroup(
						ronggroup_handler.obtainMessage(), context,
						denglu.userid, db);
				at.execute(1);
				break;
			case 1:
				// Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				// Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				break;

			default:
				break;
			}
		};
	};
	// zyj融云群组信息的handler
	Handler ronggroup_handler = new Handler() {
		public void handleMessage(Message msg) {
			// if (pro != null)
			// pro.dismiss();
			switch (msg.what) {
			case 0:// 获取融云user信息
				String message = (String) msg.obj;
				// finish();
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;

			default:
				break;
			}
		};
	};
	// zyj 融云功能
	RongInfo mtoken;
	String musername;

	// 融云 - 建立与融云服务器的连接
	private void connect(String token) {

		if (getApplicationInfo().packageName.equals(MyApplication
				.getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				@Override
				public void onTokenIncorrect() {
				}

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

				}

				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_denglu);
		sp = context.getSharedPreferences(phonenumbername,
				Activity.MODE_PRIVATE);
		e = sp.edit();

		//
		db = DbUtils.create(context);
		//

		Intent intent = getIntent();
		int yanzheng = intent.getExtras().getInt("yanzheng", 0);
		if (yanzheng == 1) {// cookie失效提示框
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(BaseActivity.getLanguageString("连接超时,请重新登录"))
					.setCancelable(false)
					.setPositiveButton(BaseActivity.getLanguageString("确定"),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			builder.show();
		} else if (yanzheng == 2) {// 验证失败提示框
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(
					BaseActivity.getLanguageString("网络未连接或密码错误,请重新登录"))
					.setCancelable(false)
					.setPositiveButton(BaseActivity.getLanguageString("确定"),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			builder.show();
		} else if (yanzheng == 3) {
			try {
				System.out.println("3333333333333333333333333333333"
						+ sp.getString("phonequhao", "") + "==========="
						+ sp.getString("phonenumber", ""));
				String phontquhao = Constant.decode(Constant.key,
						sp.getString("phonequhao", ""));
				System.out.println("加密前区号 " + sp.getString("phonequhao", "")
						+ "加密后区号 " + phontquhao);
				String phonenumber = Constant.decode(Constant.key,
						sp.getString("phonenumber", ""));
				System.out.println("加密前 " + sp.getString("phonenumber", "")
						+ "加密后 " + phonenumber);
				edit_quhao.setText(phontquhao);
				edit_phone.setText(phonenumber);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		time = new TimeCount(60000, 1000);// 设置连续发送验证码 时间间隔
		yanzhengmaedittext.setHint(BaseActivity.getLanguageString("请输入验证码"));
		edit_phone.setHint(BaseActivity.getLanguageString("输入手机号登录"));
		button_fasongyanzhengma
				.setText(BaseActivity.getLanguageString("发送验证码"));
		login_username.setHint(BaseActivity.getLanguageString("请输入用户名"));
		edit_quhao.setHint(BaseActivity.getLanguageString("区号"));
		button_fasongyanzhengma.setOnClickListener(this);
		denglu_btv.setText("- " + BaseActivity.getLanguageString("扫一扫") + " -");
		denglu_btv.setOnClickListener(this);
		text_qiehuanshouji.setText("- "
				+ BaseActivity.getLanguageString("切换为手机号登录") + " -");
		text_qiehuanshouji.setOnClickListener(this);
		text_qiehuanweiqita.setText("- "
				+ BaseActivity.getLanguageString("其他登录方式") + " -");
		text_qiehuanweiqita.setOnClickListener(this);

		yonghumingtv.setText(BaseActivity.getLanguageString("用户名") + ":");
		text_mima.setText(BaseActivity.getLanguageString("密    码") + ":");
		edit_shurumima.setHint(BaseActivity.getLanguageString("请输入密码"));
		but_shoujihaodenglu.setText(BaseActivity.getLanguageString("登   录"));
		but_shoujihaodenglu.setOnClickListener(this);
		but_qitafangshidenglu.setText(BaseActivity.getLanguageString("登   录"));
		but_qitafangshidenglu.setOnClickListener(this);
	}

	/**
	 * 倒计时方法
	 **/
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			button_fasongyanzhengma.setText(BaseActivity
					.getLanguageString("重新发送"));
			button_fasongyanzhengma.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			button_fasongyanzhengma.setClickable(false);
			button_fasongyanzhengma.setText(millisUntilFinished / 1000 + "s "
					+ BaseActivity.getLanguageString("重发"));
		}
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View arg0) {
		if (button_fasongyanzhengma.getId() == arg0.getId()) {// 发送验证码计时 60 秒

			Constant.removeCookie(context);// 发送验证码接口会生成新cookie，所以先删除旧cookie，等待新cookie

			if (!edit_phone.getText().toString().equals("")
					&& !edit_quhao.getText().toString().equals("")) {
				// 加载网络时等待对话框
				// pro = new ProgressDialog(this);
				pro = ProgressDialog.show(this, "",
						BaseActivity.getLanguageString("加载中..."));// google自带dialog
				pro.setCancelable(true);// 点击dialog外空白位置是否消失
				pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失

				ClientParams client = new ClientParams();
				client.url = "/hylogin.do";
				StringBuffer strbuf = new StringBuffer();
				// strbuf.append("method=yzCode&phonenumber=");
				// strbuf.append(edit_quhao.getText().toString() + "-"
				// + edit_phone.getText().toString());
				// strbuf.append("&language=");
				// if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				// strbuf.append("2"); // 获取是中英文 2是英文 1是中文
				// } else {
				// strbuf.append("1");
				// }
				// strbuf.append("&phonetype=android");
				// des加密后
				strbuf.append("method=yzCode&phonenumber=");
				try {
					strbuf.append(URLEncoder.encode(Constant.encode(
							Constant.key, edit_quhao.getText().toString() + "-"
									+ edit_phone.getText().toString()), "UTF-8"));
					strbuf.append("&lg=");
					if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
						strbuf.append(URLEncoder.encode(
								Constant.encode(Constant.key, "2"), "UTF-8")); // 获取是中英文
						// 2是英文 1是中文
					} else {
						strbuf.append(URLEncoder.encode(
								Constant.encode(Constant.key, "1"), "UTF-8"));
					}
					strbuf.append("&phonetype=");
					strbuf.append(URLEncoder.encode(
							Constant.encode(Constant.key, "android"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String str = strbuf.toString();
				client.params = str;
				NetLoginTask<loginYanZhengMa> net = new NetLoginTask<loginYanZhengMa>(
						hand_yzm.obtainMessage(), client,
						loginYanZhengMa.class, new loginYanZhengMa(), context);
				net.execute();
			} else if (edit_quhao.getText().toString().equals("")) {
				Toast.makeText(context,
						BaseActivity.getLanguageString("请输入区号"),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context,
						BaseActivity.getLanguageString("请输入手机号码"),
						Toast.LENGTH_SHORT).show();
			}
		} else if (text_qiehuanshouji.getId() == arg0.getId()) {// 点击切换为手机用户隐藏输入用户名密码界面
			linear_shoujifangshi.setVisibility(View.VISIBLE);
			linear_qitafangshi.setVisibility(View.GONE);
			iv_logo.setVisibility(View.GONE);
			iv_logo2.setVisibility(View.VISIBLE);
		} else if (text_qiehuanweiqita.getId() == arg0.getId()) {// 点击切换为其他方式登陆
																	// 隐藏手机用户登陆界面
			linear_shoujifangshi.setVisibility(View.GONE);
			linear_qitafangshi.setVisibility(View.VISIBLE);
			iv_logo.setVisibility(View.VISIBLE);
			iv_logo2.setVisibility(View.GONE);
		} else if (but_shoujihaodenglu.getId() == arg0.getId()) {// 根据手机号登陆button监听

			if (edit_phone.getText().toString() != null) {
				if (yanzhengma != null
						&& !yanzhengmaedittext.getText().toString().equals("")
						&& !edit_phone.getText().toString().equals("")
						&& !edit_quhao.getText().toString().equals("")) {
					// pro = new ProgressDialog(this);
					pro = ProgressDialog.show(this, "",
							BaseActivity.getLanguageString("加载中..."));// google自带dialog
					pro.setCancelable(true);// 点击dialog外空白位置是否消失
					pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
					ispd = 0;

					ClientParams client = new ClientParams();
					client.url = "/hylogin.do";
					StringBuffer strbuf = new StringBuffer();
					// strbuf.append("method=phoneLogin&phonenumber=");
					// strbuf.append(edit_quhao.getText().toString() + "-"
					// + edit_phone.getText().toString());
					// strbuf.append("&yanzhengcode=");
					// strbuf.append(yanzhengmaedittext.getText().toString());
					// des加密后
					strbuf.append("method=phoneLogin&phonenumber=");
					try {
						strbuf.append(URLEncoder.encode(Constant
								.encode(Constant.key, edit_quhao.getText()
										.toString()
										+ "-"
										+ edit_phone.getText().toString()),
								"UTF-8"));
						strbuf.append("&yanzhengcode=");
						strbuf.append(URLEncoder.encode(Constant.encode(
								Constant.key, yanzhengmaedittext.getText()
										.toString()), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String str = strbuf.toString();
					client.params = str;
					NetLoginTask<YongHuMingDengLu> net = new NetLoginTask<YongHuMingDengLu>(
							hand.obtainMessage(), client,
							YongHuMingDengLu.class, new YongHuMingDengLu(),
							context);
					net.execute();
				} else if (edit_quhao.getText().toString().equals("")) {
					Toast.makeText(context,
							BaseActivity.getLanguageString("请输入区号"),
							Toast.LENGTH_SHORT).show();
				} else if (edit_phone.getText().toString().equals("")) {
					Toast.makeText(context,
							BaseActivity.getLanguageString("请输入手机号码"),
							Toast.LENGTH_SHORT).show();
				} else if (yanzhengmaedittext.getText().toString().equals("")) {
					Toast.makeText(context,
							BaseActivity.getLanguageString("请输入验证码"),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context,
							BaseActivity.getLanguageString("验证码错误"),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(context,
						BaseActivity.getLanguageString("请输入手机号码"), 1000).show();
			}
			// Toast.makeText(context, "请使用用户名密码登陆", 1000).show();
		} else if (but_qitafangshidenglu.getId() == arg0.getId()) {// 根据用户名密码登陆button监听

			Constant.removeCookie(context);// 根据用户名密码登陆会产生新cookie，先删除旧cookie，等待新cookie

			String username = login_username.getText().toString().trim();
			String password = edit_shurumima.getText().toString().trim();
			if (!username.equals("") && !password.equals("")) {

				// 加载网络时等待对话框
				// pro = new ProgressDialog(this);
				pro = ProgressDialog.show(this, "",
						BaseActivity.getLanguageString("加载中..."));// google自带dialog
				pro.setCancelable(true);// 点击dialog外空白位置是否消失
				pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
				ispd = 1;

				ClientParams client = new ClientParams();
				client.url = "/saltaction.do";
				StringBuffer strbuf = new StringBuffer();
				// strbuf.append("method=passwordLogin&userid=");
				// strbuf.append(username);
				// strbuf.append("&password=");
				// strbuf.append(password);
				// strbuf.append("&lg=");
				// if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				// strbuf.append("2"); // 获取是中英文 2是英文 1是中文
				// } else {
				// strbuf.append("1");
				// }
				// strbuf.append("&phonetype=android");
				// des加密后
				strbuf.append("method=getSalt&userid=");
				try {
					strbuf.append(URLEncoder.encode(
							new String(Base64.encode(username.getBytes())),
							"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String str = strbuf.toString();
				client.params = str;
				NetLoginTask<DengLuYanZhi> net = new NetLoginTask<DengLuYanZhi>(
						handyz.obtainMessage(), client, DengLuYanZhi.class,
						new DengLuYanZhi(), context);
				net.execute();
			} else if (username.equals("")) {
				Toast.makeText(context,
						BaseActivity.getLanguageString("请输入用户名"),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context,
						BaseActivity.getLanguageString("请输入密码"),
						Toast.LENGTH_SHORT).show();
			}
			Intent intent = new Intent(DengLuActivity.this,
					MainActivity.class);
			startActivity(intent);
		} else if (denglu_btv.getId() == arg0.getId()) {
			// zyj新加
			Intent intent = new Intent(DengLuActivity.this,
					CaptureActivity.class);
			startActivityForResult(intent, 100);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			// Bundle b = data.getExtras(); // data为B中回传的Intent
			// String usercode = b.getString("result");// str即为回传的值
			String usercode = data.getStringExtra("result");// str即为回传的值
			System.out.println("DengluActivity:onActivityResult" + usercode);
			pro = ProgressDialog.show(this, "",
					BaseActivity.getLanguageString("加载中..."));// google自带dialog
			pro.setCancelable(true);// 点击dialog外空白位置是否消失
			pro.setCanceledOnTouchOutside(false);// 点击返回键对话框是否消失
			loginByCode(usercode);
			break;
		default:
			break;
		}
	}

	public void loginByCode(String code) {
		ClientParams client = new ClientParams();
		client.url = "/hylogin.do";
		StringBuffer strbuf = new StringBuffer();
		try {
			strbuf.append("method=scanLogin&userid=");
			strbuf.append(URLEncoder.encode(
					Constant.encode(Constant.key, code), "UTF-8"));
			strbuf.append("&lg=");
			if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
				strbuf.append(URLEncoder.encode(
						Constant.encode(Constant.key, "2"), "UTF-8")); // 获取是中英文
				// 2是英文 1是中文
			} else {
				strbuf.append(URLEncoder.encode(
						Constant.encode(Constant.key, "1"), "UTF-8"));
			}
			strbuf.append("&phonetype=");
			strbuf.append(URLEncoder.encode(
					Constant.encode(Constant.key, "android"), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String str = strbuf.toString();
		client.params = str;
		NetLoginTask<YongHuMingDengLu> net = new NetLoginTask<YongHuMingDengLu>(
				hand.obtainMessage(), client, YongHuMingDengLu.class,
				new YongHuMingDengLu(), context);
		net.execute();
	}

	@Override
	protected void setI18nValue() {

	}
}
