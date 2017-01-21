package com.gheng.exhibit.view.checkin.checkin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.BasePojo;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.recyclerview.HorizontalDividerItemDecoration;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.adapter.AdapterForDaHuiDianPingZhuTiListPhotos;
import com.hebg3.mxy.utils.AsyncTaskForComPressPhoto;
import com.hebg3.mxy.utils.AsyncTaskForUpLoadFilesNew;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.PhotoInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

//发表大会点评的界面
public class AddDaHuiDianPingZhuTiActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.goback)
	public Button goback;
	@ViewInject(R.id.fabubutton)
	public Button fabubutton;
	@ViewInject(R.id.titletv)
	public TextView titletv;
	@ViewInject(R.id.ed)
	public EditText ed;
	@ViewInject(R.id.photosrv)
	public RecyclerView photosrv;
	@ViewInject(R.id.tv)
	public TextView tv;
	@ViewInject(R.id.shuoming)
	public TextView shuoming;
	@ViewInject(R.id.photoslayout)
	public RelativeLayout photoslayout;
	private int widpx = 0;
	String userid = "";

	GridLayoutManager glm;
	ArrayList<PhotoInfo> photofiles = new ArrayList<PhotoInfo>();
	AdapterForDaHuiDianPingZhuTiListPhotos adapter;
	// ProgressDialog pd;

	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {

			// if (pd != null) {
			// pd.dismiss();
			// }
			if (msg.what == 1) {// 图片压缩完毕，提交主题
				System.out.println("图片压缩完毕，开始上传主题");
				uploadZhuTi();
				return;
			}
			if (msg.what == 200) {// 发送成功
				fabubutton.setClickable(true);
				BasePojo bp = (BasePojo) msg.obj;
				if (bp == null) {
					Toast.makeText(AddDaHuiDianPingZhuTiActivity.this,
							getLanguageString("发布失败"), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (bp.code.equals("500")) {
					BaseActivity
							.gotoLoginPage(AddDaHuiDianPingZhuTiActivity.this);
					return;
				}
				if (bp.code.equals("200")) {
					finish();
					Toast.makeText(AddDaHuiDianPingZhuTiActivity.this,
							getLanguageString("谢谢参与"), Toast.LENGTH_SHORT)
							.show();
				} else {// 发送失败
					Toast.makeText(AddDaHuiDianPingZhuTiActivity.this,
							getLanguageString("发布失败"), Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 测量手机尺寸
		int widpx = BaseActivity.getPingMuSize(context);
		if (widpx >= 1) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		} else {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		}
		setContentView(R.layout.activity_adddahuidianpingzhuti);

		goback.setOnClickListener(this);
		fabubutton.setOnClickListener(this);

		// 查找
		try {
			DbUtils db = DbUtils.create(context);
			User parent = db.findFirst(Selector.from(User.class).where("id",
					"=", "1"));
			userid = Constant.decode(Constant.key, parent.getUserId());
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		glm = new GridLayoutManager(this, 3);
		glm.setOrientation(GridLayoutManager.VERTICAL);
		photosrv.setLayoutManager(glm);
		photosrv.addItemDecoration(// 为RecyclerView添加divider
		new HorizontalDividerItemDecoration.Builder(this)
				.color(this.getResources().getColor(R.color.touming))
				.size(this.getResources().getDimensionPixelSize(
						R.dimen.recylerviewitemdivider_pchi))
				.margin(this.getResources().getDimensionPixelSize(
						R.dimen.title_padding),
						this.getResources().getDimensionPixelSize(
								R.dimen.title_padding)).build());
		adapter = new AdapterForDaHuiDianPingZhuTiListPhotos(photofiles, this);
		photosrv.setAdapter(adapter);
	}

	@Override
	protected void setI18nValue() {
		// TODO Auto-generated method stub
		ed.setHint(getLanguageString("话题不能超过200字"));
		titletv.setText(getLanguageString("发表话题"));
		fabubutton.setText(getLanguageString("发表"));
		goback.setText(getLanguageString("取消"));
		tv.setText(getLanguageString("正文") + " :");
		shuoming.setText(getLanguageString("最多添加6张图片"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == goback) {
			this.finish();
		}
		if (v == fabubutton) {
			if (!IsWebCanBeUse.isWebCanBeUse(this)) {
				Toast.makeText(this, getLanguageString("网络不给力"),
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 发布主题
			if (ed.getText().toString().trim().equals("")) {
				Toast.makeText(this, getLanguageString("请填写内容"),
						Toast.LENGTH_SHORT).show();
				return;
			} else if (ed.getText().toString().trim().length() > 200) {
				Toast.makeText(this, getLanguageString("话题不能超过200字"),
						Toast.LENGTH_SHORT).show();
				return;
			} else if (isContainEmoji(ed.getText().toString().trim())) {
				Toast.makeText(this, getLanguageString("目前只支持发送纯文字内容"),
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				fabubutton.setClickable(false);
				// File f = new File(Environment.getExternalStorageDirectory()
				// .getPath() + "/wicuploadphotos/");
				File f = new File(Constant.uploadTarget);
				if (!f.exists()) {
					f.mkdirs();
				}
				// pd = new ProgressDialog(this);
				// pd.setMessage(BaseActivity.getLanguageString("加载中..."));
				// pd.setCancelable(true);
				// pd.setCanceledOnTouchOutside(false);
				// pd.show();
				// 启动线程压缩准备上传的图片原图
				AsyncTaskForComPressPhoto at = new AsyncTaskForComPressPhoto(
						photofiles, h.obtainMessage());
				at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
			}
		}
	}

	public void uploadZhuTi() {
		// 启动线程提交主题
		// pd=new ProgressDialog(this);
		// pd.setMessage(BaseActivity.getLanguageString("加载中..."));
		// pd.setCancelable(true);
		// pd.setCanceledOnTouchOutside(false);
		// pd.show();

		HashMap<String, String> params = new HashMap<String, String>();
		// -----lyy接口变动为createTheme2
		// params.put("method", "createTheme");
		params.put("method", "createTheme2");
		params.put("userid", userid);
		params.put("content", ed.getText().toString().trim());

		HashMap<String, File> files = new HashMap<String, File>();

		for (int i = 0; i < photofiles.size(); i++) {
			File photo = new File(photofiles.get(i).uploadphotourl);// 上传压缩后的图片，而不是原图
			if (photo.exists()) {
				System.out.println("图片路径 : " + photo.getPath());
			}
			files.put("" + i, photo);
		}
		AsyncTaskForUpLoadFilesNew at = new AsyncTaskForUpLoadFilesNew(this,
				Constant.DOMAIN + "/subjects.do", params, files,
				h.obtainMessage());
		at.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			this.photofiles.clear();
			ArrayList<String> selectedphotos = (ArrayList<String>) data
					.getSerializableExtra("select_result");
			if (selectedphotos != null && selectedphotos.size() > 0) {
				for (int i = 0; i < selectedphotos.size(); i++) {
					PhotoInfo pi = new PhotoInfo();
					pi.smallphotourl = selectedphotos.get(i);// imageloader
																// 加载本地图片
																// uri前面要加上"file://"
					pi.photourl = "file://" + selectedphotos.get(i);// imageloader
																	// 加载本地图片
																	// uri前面要加上"file://"
																	// pi.uploadphotourl
																	// =
																	// Environment
					// .getExternalStorageDirectory().getPath()
					// + "/wicuploadphotos/"
					// + pi.smallphotourl.substring(
					// pi.smallphotourl.lastIndexOf("/") + 1,
					// pi.smallphotourl.length());
					pi.uploadphotourl = Constant.uploadTarget
							+ pi.smallphotourl.substring(
									pi.smallphotourl.lastIndexOf("/") + 1,
									pi.smallphotourl.length());
					this.photofiles.add(pi);
				}
				changeShowPhotoLayout();
				adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 图片展示布局随动，可大可小！！！
	 */
	public void changeShowPhotoLayout() {
		if (photofiles.size() >= 3) {
			LayoutParams lp = photoslayout.getLayoutParams();
			lp.height = BaseActivity.dip2px(this, 240);
			photoslayout.setLayoutParams(lp);
		} else {
			LayoutParams lp = photoslayout.getLayoutParams();
			lp.height = BaseActivity.dip2px(this, 130);
			photoslayout.setLayoutParams(lp);
		}
	}

}
