package com.gheng.exhibit.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.YongHuMingDengLu;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;
import com.gheng.exhibit.view.checkin.checkin.ModifyPasswordActivity;
import com.gheng.exhibit.view.checkin.checkin.SettingErweimaActivity;
import com.gheng.exhibit.view.checkin.checkin.ShowWebViewActivity;
import com.gheng.exhibit.widget.CircleImageView;
import com.hebg3.mxy.utils.AsyncTaskForCheckApkVersion;
import com.hebg3.mxy.utils.AsyncTaskForDownloadTask;
import com.hebg3.mxy.utils.AsyncTaskForUpdateUserInfo;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.NewApkVersionPojo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smartdot.wenbo.huiyi.R;

public class SettingActivity extends BaseActivity implements OnClickListener {

	RelativeLayout updateapklayout;// 检查版本
	RelativeLayout connectuslayout;// 联系我们
	RelativeLayout aboutuslayout;// 关于我们
	@ViewInject(R.id.erweimalayout)
	RelativeLayout erweimalayout;// 二维码
	RelativeLayout signoutlayout;// 注销
	@ViewInject(R.id.xiugaimimalayout)
	RelativeLayout xiugaimimalayout;// 修改密码
	ImageButton goback;
	Context context = this;

	@ViewInject(R.id.genbuju)
	RelativeLayout genbuju;
	@ViewInject(R.id.checkapkversiontv)
	TextView checkapkversiontv;
	@ViewInject(R.id.connectustv)
	TextView connectustv;
	@ViewInject(R.id.erweimatv)
	TextView erweimatv;// 二维码
	@ViewInject(R.id.xiugaimimatv)
	TextView xiugaimimatv;// 修改密码
	@ViewInject(R.id.aboutustv)
	TextView aboutustv;
	@ViewInject(R.id.signouttv)
	TextView signouttv;
	@ViewInject(R.id.titletv)
	TextView titletv;
	@ViewInject(R.id.userphoto)
	CircleImageView userphoto;
	@ViewInject(R.id.username)
	TextView username;
	@ViewInject(R.id.userdescription)
	TextView userdescription;
	User parent = null;
	@ViewInject(R.id.apkversioncodetv)
	TextView apkversioncodetv;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	ProgressDialog pd;
	int myversioncode = 1;// 当前系统版本号
	String myversionname = "";// 当前系统版本名称

	Handler h = new Handler() {// 下载更新请求
		public void handleMessage(android.os.Message msg) {
			if (pd != null) {
				pd.dismiss();
			}
			if (msg.what == 1000) {
				NewApkVersionPojo pojo = (NewApkVersionPojo) msg.obj;
				if (pojo.versioncode <= myversioncode) {// 没有新版本
					Toast.makeText(SettingActivity.this,
							BaseActivity.getLanguageString("已经是最新版本"),
							Toast.LENGTH_SHORT).show();
				} else {// 有新版本
					showDialogDownloadNewApk(pojo.downloadurl);
				}
			} else if (msg.what == 200) {// 请求成功，更新个人信息
				YongHuMingDengLu denglu = (YongHuMingDengLu) msg.obj;
				// // 创建 初始化数据库
				// DbUtils db = DbUtils.create(SettingActivity.this);
				// if (!denglu.userid.equals("")) {
				// try {
				// db.dropTable(User.class);
				// } catch (DbException e) {
				// e.printStackTrace();
				// }
				// }
				User user = new User(); // 这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
				if (denglu.userid.equals("")) {// 判断服务器返回的userid是否为空，如果为空则保存手机号。
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
				if ("".equals(denglu.vipid)) {// 防止当前用户不是vip，造成加密异常，手动将vip字段就是0
					denglu.vipid = "0";
				}
				if (denglu.vipid != null)
					user.setVipid(Constant.encode(Constant.key, denglu.vipid));
				user.setWorkplace(Constant.encode(Constant.key,
						denglu.workplace));
				System.out.println("aaa" + "settingactivity界面的数据库保存成功！"
						+ denglu.name + denglu.zhiwei + denglu.workplace);
				username.setText(denglu.name);
				if (denglu.zhiwei != null && !denglu.zhiwei.equals("")
						&& denglu.workplace != null
						&& !denglu.workplace.equals("")) {
					userdescription.setText(denglu.workplace + " | "
							+ denglu.zhiwei);
				} else if (parent.getWorkplace() != null) {
					userdescription.setText(denglu.workplace);
				} else if (parent.getZhiWei() != null) {
					userdescription.setText(denglu.zhiwei);
				}
				// try {
				// db.save(user); // 服务器返回数据后，保存到本地数据库
				// db.close();
				// } catch (DbException e) {
				// e.printStackTrace();
				// }
			} else {
				Toast.makeText(SettingActivity.this,
						BaseActivity.getLanguageString("请求失败"),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyApplication.add(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnLoading(R.drawable.a_huiyiricheng_guesticon)
				// 加载时显示选择进度条
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				// 加载失败时显示缺省头像
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(false)
				.cacheOnDisk(true).resetViewBeforeLoading(true).build();
		// 启动线程更新个人信息// 查找
		try {
			DbUtils db = DbUtils.create(context);
			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
			db.close();
			if (!IsWebCanBeUse.isWebCanBeUse(this)) {
				username.setText(Constant.decode(Constant.key, parent.getName()));
				if (parent.getZhiWei() != null
						&& !parent.getZhiWei().equals("")
						&& parent.getWorkplace() != null
						&& !parent.getWorkplace().equals("")) {
					userdescription
							.setText(Constant.decode(Constant.key,
									parent.getWorkplace())
									+ " | "
									+ Constant.decode(Constant.key,
											parent.getZhiWei()));
				} else if (parent.getWorkplace() != null) {
					userdescription.setText(Constant.decode(Constant.key,
							parent.getWorkplace()));
				} else if (parent.getZhiWei() != null) {
					userdescription.setText(Constant.decode(Constant.key,
							parent.getZhiWei()));
				}
				Toast.makeText(this, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
			} else {
				AsyncTaskForUpdateUserInfo at = new AsyncTaskForUpdateUserInfo(
						this,
						Constant.decode(Constant.key, parent.getUserId()),
						h.obtainMessage());
				at.execute(1);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		updateapklayout = (RelativeLayout) findViewById(R.id.updateapklayout);
		connectuslayout = (RelativeLayout) findViewById(R.id.connectuslayout);
		aboutuslayout = (RelativeLayout) findViewById(R.id.aboutuslayout);
		signoutlayout = (RelativeLayout) findViewById(R.id.signoutlayout);

		updateapklayout.setOnClickListener(this);
		connectuslayout.setOnClickListener(this);
		aboutuslayout.setOnClickListener(this);
		signoutlayout.setOnClickListener(this);
		erweimalayout.setOnClickListener(this);
		xiugaimimalayout.setOnClickListener(this);

		goback = (ImageButton) findViewById(R.id.goback);
		goback.setOnClickListener(this);

		try {
			imageLoader.displayImage(
					Constant.decode(Constant.key, parent.getSmallPhotoUrl()),
					userphoto, options);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			// 获取程序版本号
			PackageManager manager = this.getPackageManager();// 程序包管理器
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			myversioncode = info.versionCode;// 当前系统版本号
			myversionname = info.versionName;// 当前系统版本名称
			apkversioncodetv.setText(myversionname);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}// 获取PackageInfo对象就可以获取版本名称和版本号

	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		checkapkversiontv.setText(getLanguageString("检查版本"));
		connectustv.setText(getLanguageString("联系我们"));
		aboutustv.setText(getLanguageString("关于我们"));
		signouttv.setText(getLanguageString("注销"));
		titletv.setText(getLanguageString("设置"));
		erweimatv.setText(getLanguageString("二维码"));
		xiugaimimatv.setText(getLanguageString("修改密码"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String contantus = Constant.DOMAIN
				+ "/InfoPublish.do?method=viewInfo&language=1&infoid=4601";
		String guanyuus = Constant.DOMAIN
				+ "/InfoPublish.do?method=viewInfo&language=1&infoid=4600";
		if (SharedData.getInt("i18n") == 1) {
			contantus = contantus = Constant.DOMAIN
					+ "/InfoPublish.do?method=viewInfo&language=1&infoid=4601";
			guanyuus = Constant.DOMAIN
					+ "/InfoPublish.do?method=viewInfo&language=1&infoid=4600";
		} else {
			contantus = contantus = Constant.DOMAIN
					+ "/InfoPublish.do?method=viewInfo&language=2&infoid=4602";
			guanyuus = Constant.DOMAIN
					+ "/InfoPublish.do?method=viewInfo&language=2&infoid=4603";
		}
		if (v == goback) {
			this.finish();
		}
		if (v == updateapklayout) {// 检查版本
			if (!IsWebCanBeUse.isWebCanBeUse(getApplicationContext())) {
				Toast.makeText(this, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
				return;
			}
			pd = ProgressDialog.show(this, "",
					BaseActivity.getLanguageString("加载中..."));
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			AsyncTaskForCheckApkVersion at = new AsyncTaskForCheckApkVersion(
					h.obtainMessage(), this.getApplicationContext());
			at.execute(1);
		}
		if (v == connectuslayout) {// 联系我们
			// Intent i = new Intent();
			// if (SharedData.getInt("i18n") == 1) {// 中文
			// i.setClass(this, ConnectUsActivity_zh.class);
			// } else {// 英文
			// i.setClass(this, ConnectUsActivity_zh.class);
			// }
			// this.startActivity(i);
			Intent tongxiangzhichuang = new Intent();
			tongxiangzhichuang.setClass(this, ShowWebViewActivity.class);
			tongxiangzhichuang.putExtra("title", getLanguageString("联系我们"));
			tongxiangzhichuang.putExtra("url", contantus);
			this.startActivity(tongxiangzhichuang);
		}
		if (v == aboutuslayout) {// 关于我们
			// Intent i = new Intent();
			// if (SharedData.getInt("i18n") == 1) {// 中文
			// i.setClass(this, AboutUsActivity_zh.class);
			// } else {// 英文
			// i.setClass(this, AboutUsActivity_en.class);
			// }
			// this.startActivity(i);
			Intent tongxiangzhichuang = new Intent();
			tongxiangzhichuang.setClass(this, ShowWebViewActivity.class);
			tongxiangzhichuang.putExtra("title", getLanguageString("关于我们"));
			tongxiangzhichuang.putExtra("url", guanyuus);
			this.startActivity(tongxiangzhichuang);
		}
		if (v == erweimalayout) {// 二维码
			Intent i = new Intent();
			i.setClass(this, SettingErweimaActivity.class);
			this.startActivity(i);
		}
		if (v == signoutlayout) {// 注销

			new AlertDialog.Builder(context)
					.setTitle(getLanguageString("确认"))
					.setMessage(getLanguageString("确定注销登录") + "?")
					.setPositiveButton(getLanguageString("确定"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									setResult(100);
									finish();
								}
							})
					.setNegativeButton(getLanguageString("取消"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}

							}).show();
		}
		if (v == xiugaimimalayout) {// 修改密码
			Intent i = new Intent();
			i.setClass(this, ModifyPasswordActivity.class);
			this.startActivityForResult(i, 105);
		}
	}

	/**
	 * 如果发现新版本，调用本方法询问用户是否下载
	 */
	public void showDialogDownloadNewApk(final String downloadurl) {
		new AlertDialog.Builder(context)
				.setTitle(getLanguageString("发现新版本") + "!")
				.setMessage(getLanguageString("现在下载吗") + "?")
				.setPositiveButton(getLanguageString("确定"),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 启动线程下载apk
								// 判断下载服务是否可用，如果未启用，启用
								int state = getPackageManager()
										.getApplicationEnabledSetting(
												"com.android.providers.downloads");

								if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
										|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
										|| state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
									System.out.println("下载服务未启用");
									String packageName = "com.android.providers.downloads";
									try {
										// Open the specific App Info page:
										Intent intent = new Intent(
												android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
										intent.setData(Uri.parse("package:"
												+ packageName));
										startActivity(intent);
									} catch (ActivityNotFoundException e) {
										e.printStackTrace();
										// Open the generic Apps page:
										Intent intent = new Intent(
												android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
										startActivity(intent);
										Toast.makeText(
												getApplicationContext(),
												BaseActivity
														.getLanguageString("下载失败"),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									StatService.onEvent(
											getApplicationContext(),
											"updateAPK", "参会人员更新App", 1);// 百度统计
									System.out.println("下载服务已经启用");
									AsyncTaskForDownloadTask at = new AsyncTaskForDownloadTask(
											getApplicationContext(),
											downloadurl);
									at.execute(1);
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
	}

	@Override
	protected void onDestroy() {// 退出应用，清除图片缓存

		MyApplication.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {// 修改密码重新登录
		// TODO Auto-generated method stub
		if (requestCode == 105 && resultCode == 1) {// 修改密码成功，重新登录
			setResult(100);
			finish();
		}
	}
}
