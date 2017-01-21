package com.gheng.exhibit.view;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.YongHuMingDengLu;
import com.gheng.exhibit.model.databases.Language;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetLoginTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

//重新登陆界面
public class NotactivationActivity extends Activity implements OnClickListener {

	Button reloginbutton;
	ProgressDialog pro;
	public String phonenumbername = "phonenumber";
	public SharedPreferences sp;
	private Context context = this;
	TextView noactivationtextview;

	/**
	 * 输入用户名密码登陆访问接口返回的handler
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) { // 等于0表示请求成功，返回正常数据
				// 将 cookie保存到SharedPreferences

				YongHuMingDengLu denglu = (YongHuMingDengLu) msg.obj;				
				// 创建 初始化数据库
				DbUtils db = DbUtils.create(context);

				if (!denglu.userid.equals("")) {
					try {
						db.dropTable(User.class);
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				User user = new User(); // 这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
				if (denglu.userid.equals("")) {// 判断服务器返回的userid是否为空，如果为空则保存手机号。
					user.setUserId("");
				} else {
					user.setUserId(Constant.encode(Constant.key,denglu.userid));
				}
				
				user.setUserjuese(denglu.userjuese);//不需要加密  数值
				user.setPassword(denglu.password);//不需要加密，已经是MD5加密过的
				
				user.setName(Constant.encode(Constant.key,denglu.name));
				user.setZhiWei(Constant.encode(Constant.key,denglu.zhiwei));
				user.setSmallPhotoUrl(Constant.encode(Constant.key,denglu.smallphotourl));
				if(denglu.vipid.equals("")){//防止当前用户不是vip，造成加密异常，手动将vip字段就是0
					denglu.vipid="0";
				}
				user.setVipid(Constant.encode(Constant.key,denglu.vipid));
				user.setWorkplace(Constant.encode(Constant.key,denglu.workplace));
				
				try {
					db.save(user); // 服务器返回数据后，保存到本地数据库
				} catch (DbException e) {
					e.printStackTrace();
				} // 使用saveBindingId保存实体时会为实体的id赋值
				if (denglu.userid.equals("")) {
				} else {
					Intent intent = new Intent(NotactivationActivity.this,MainActivity.class);
					startActivity(intent);
					NotactivationActivity.this.finish();
				}

			} else if (msg.what == 1) { // 网络连接超时的情况 "网络不给力" 其实应该封装到String文件里面
				Toast.makeText(context,
						BaseActivity.getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			}
			pro.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notactivation);
		sp = getSharedPreferences(phonenumbername, Activity.MODE_PRIVATE);
		reloginbutton = (Button) findViewById(R.id.reloginbutton);
		noactivationtextview = (TextView) findViewById(R.id.noactivationtextview);
		reloginbutton.setOnClickListener(this);

		noactivationtextview.setText(BaseActivity
				.getLanguageString("手机号未激活,请联系会务服务人员获取登录账户"));
		reloginbutton.setText(BaseActivity.getLanguageString("重新登录"));

		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		// 加载网络时等待对话框
		pro = ProgressDialog.show(this, "", BaseActivity.getLanguageString("加载中..."));// google自带dialog
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
		strbuf.append("method=phoneCheck&phonenumber=");
		// System.out.println(sp.getString("phonequhao", "") + "-"
		// + sp.getString("phonenumber", ""));
		try {
			String phontquhao = Constant.decode(Constant.key,
					sp.getString("phonequhao", ""));
			String phonenumber = Constant.decode(Constant.key,
					sp.getString("phonenumber", ""));
			System.out.println(phontquhao+"====="+phonenumber);
			strbuf.append(URLEncoder.encode(
					Constant.encode(Constant.key, phontquhao + "-"
							+ phonenumber), "UTF-8"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		strbuf.append("&lg=");
		if (SharedData.getInt("i18n", Language.ZH) == Language.EN) {
			strbuf.append("2"); // 获取是中英文
			// 2是英文 1是中文
		} else {
			strbuf.append("1");
		}
		Constant.removeCookie(context);//激活未激活接口也返回新cookie，所以删除旧cookie
		
		String str = strbuf.toString();
		client.params = str;
		NetLoginTask<YongHuMingDengLu> net = new NetLoginTask<YongHuMingDengLu>(
				hand.obtainMessage(), client, YongHuMingDengLu.class,
				new YongHuMingDengLu(), context);
		net.execute();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		DbUtils db = DbUtils.create(this);
		try {
			db.dropTable(User.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent intent = new Intent(NotactivationActivity.this,DengLuActivity.class);
		intent.putExtra("yanzheng", 3);
		startActivity(intent);
		NotactivationActivity.this.finish();
	}

}
