package com.gheng.exhibit.view.checkin.checkin;

import io.rong.imkit.RongIM;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import com.gheng.exhibit.http.body.response.VipBaseInfoDynamic;
import com.gheng.exhibit.http.body.response.VipInfoBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.adapter.VipBaseInfoAdapter;
import com.gheng.exhibit.view.checkin.VIPInfoActivity;
import com.gheng.exhibit.widget.CircleImageView;
import com.google.gson.Gson;
import com.hebg3.mxy.utils.VipInfoTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartdot.wenbo.huiyi.R;

public class VipBaseInfoFragment extends Fragment {
	private Context mContext;
	private User user = null;
	private String userid = "";
	private VipInfoBean vipInfo = null;// 本地数据

	private VipBaseInfoAdapter adapter;

	private ListView listView;
	// listview----->footerView
	private Button btn_call;
	private Button btn_sms;
	private Button btn_sendinfo;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:

				// String ss = (String) msg.obj;
				// System.out.println("================baseinfofragment的json数据=====》"+ss);
				VipBaseInfoDynamic vipBaseInfoDynamic = (VipBaseInfoDynamic) msg.obj;
				System.out.println("=====vipBaseInfoDynamic  code=>"
						+ vipBaseInfoDynamic.getCode());
				System.out.println("=====vipBaseInfoDynamic  message=>"
						+ vipBaseInfoDynamic.getMessage());
				System.out.println("=====vipBaseInfoDynamic  fwinfobean=>"
						+ vipBaseInfoDynamic.getFwInfoBean().toString());
				System.out.println("=====vipBaseInfoDynamic  jbjbinfobean=>"
						+ vipBaseInfoDynamic.getJbjbInfoBean().toString());
				System.out.println("=====vipBaseInfoDynamic  jbxxinfomap=>"
						+ vipBaseInfoDynamic.getJbxxInfoMap().toString());
				System.out.println("=====vipBaseInfoDynamic  kvinfomap=>"
						+ vipBaseInfoDynamic.getKvInfoMap().toString());

				if (vipBaseInfoDynamic != null) {
					getDataNew(vipBaseInfoDynamic);
					setDataIntoListviewNew(vipBaseInfoDynamic);
				}

				break;
			case -1:
				Toast.makeText(mContext, "请求数据有误", Toast.LENGTH_LONG).show();
				break;
			case -2:
				Toast.makeText(mContext, "请求数据为空", Toast.LENGTH_LONG).show();
				break;
			case 300:
				// Toast.makeText(mContext, "数据为空 code = 300",
				// Toast.LENGTH_LONG).show();
				break;
			case 500:
				// Toast.makeText(mContext, "请求超时误 code = 500",
				// Toast.LENGTH_LONG).show();
				break;
			case 400:
				// Toast.makeText(mContext, "请求失败 code = 400",
				// Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = getActivity();

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.fragment_vip_baseinfo, container, false);

		initView(view);
		initData();// 请求网络数据

		// vipInfo = initDataLocal(getDataFromAssets());
		// Log.d("tag", "======vipinfo--->"+vipInfo.getJbxxInfo().getMobile());
		// getData(resultData);
		// setDataIntoListview();
		return view;
	}

	/**
	 * 初始化界面
	 * 
	 * @param view
	 */
	private void initView(View view) {
		listView = (ListView) view.findViewById(R.id.vipinfo_listview);
		btn_call = (Button) view.findViewById(R.id.vipinfo_call);
		btn_sms = (Button) view.findViewById(R.id.vipinfo_sms);
		btn_sendinfo = (Button) view.findViewById(R.id.vipinfo_sendinfo);

	}

	/**
	 * 从网络请求数据
	 */
	private void initData() {

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
			Log.d("tag", "=======VipBaseInfoFragment--->userid=" + userid);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		VIPInfoActivity vipInfoActivity = (VIPInfoActivity) getActivity();
		System.out
				.println("------------------VipBaseInfoFragment---->vipid--->"
						+ vipInfoActivity.vIPIDString);
		if (vipInfoActivity.vIPIDString != null) {
			try {
				new VipInfoTask(handler.obtainMessage(), userid,
						vipInfoActivity.vIPIDString, mContext).execute(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), "暂时没有对应的嘉宾信息", Toast.LENGTH_LONG)
					.show();

		}
	}

	/**
	 * 设置嘉宾信息
	 * 
	 * @param vipBaseInfoDynamic
	 */
	private void setDataIntoListviewNew(
			final VipBaseInfoDynamic vipBaseInfoDynamic) {

		View headerView = LayoutInflater.from(mContext).inflate(
				R.layout.vipinfo_list_headerview, null);
		// zyj 二维码
		View footerView = LayoutInflater.from(mContext).inflate(
				R.layout.vipinfo_list_footerview, null);
		ImageView iv_english = (ImageView) footerView
				.findViewById(R.id.iv_english);
		System.out.println("vipBaseInfoDynamic.iscan"+vipBaseInfoDynamic.getJbjbInfoBean().getIsScan());
		if (vipBaseInfoDynamic.getJbjbInfoBean().getIsScan().equals("1")) {// 扫码过了
			// 不给他加footerview
		} else {
			String myurl = vipBaseInfoDynamic.getJbjbInfoBean().getUserid();
			System.out.println("VipBaseInfoFragment:myurl" + myurl);
			createEnglishQRCode(iv_english, myurl);
			listView.addFooterView(footerView);
		} 

		//
		CircleImageView vip_icon = (CircleImageView) headerView
				.findViewById(R.id.vipinfo_icon);
		TextView vip_tvName = (TextView) headerView
				.findViewById(R.id.vipinfo_tv_name);
		TextView vip_tv_workplace = (TextView) headerView
				.findViewById(R.id.vipinfo_tv_workplace);
		TextView vip_tv_job = (TextView) headerView
				.findViewById(R.id.vipinfo_tv_job);
		if (!TextUtils.isEmpty(vipBaseInfoDynamic.getJbjbInfoBean().getName())) {
			vip_tvName.setText(vipBaseInfoDynamic.getJbjbInfoBean().getName());
		} else {
			vip_tvName.setText("嘉宾姓名");
		}
		if (!TextUtils.isEmpty(vipBaseInfoDynamic.getJbjbInfoBean()
				.getWorkplace())) {
			vip_tv_workplace.setText(vipBaseInfoDynamic.getJbjbInfoBean()
					.getWorkplace());
		} else {
			vip_tv_workplace.setText("嘉宾工作单位");
		}
		if (!TextUtils.isEmpty(vipBaseInfoDynamic.getJbjbInfoBean().getJob())) {
			vip_tv_job.setText(vipBaseInfoDynamic.getJbjbInfoBean().getJob());
		} else {
			vip_tv_job.setText("嘉宾职务");
		}
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage(vipBaseInfoDynamic.getJbjbInfoBean()
				.getPhotourl(), vip_icon, options);
		listView.addHeaderView(headerView);

		/**
		 * 给listview添加footerview（暂时不用）
		 */
		// View footerView =
		// LayoutInflater.from(mContext).inflate(R.layout.vipinfo_list_footer,
		// null);
		// btn_call = (Button) footerView.findViewById(R.id.vipinfo_call);
		// btn_sms = (Button) footerView.findViewById(R.id.vipinfo_sms);
		// btn_sendinfo = (Button)
		// footerView.findViewById(R.id.vipinfo_sendinfo);
		// listView.addFooterView(footerView);
		adapter = new VipBaseInfoAdapter(mContext, kvInfoNewList,
				jbxxInfoNewList);
		listView.setAdapter(adapter);
		btn_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				if (!TextUtils.isEmpty(vipBaseInfoDynamic.getJbjbInfoBean()
						.getMobile())) {
					intent.setData(Uri.parse("tel:"
							+ vipBaseInfoDynamic.getJbjbInfoBean().getMobile()));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} else {
					// intent.setData(Uri.parse("tel:18515834121"));
					Toast.makeText(mContext, "所拨电话号码为空", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
		btn_sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SENDTO);
				if (!TextUtils.isEmpty(vipBaseInfoDynamic.getJbjbInfoBean()
						.getMobile())) {
					intent.setData(Uri.parse("smsto:"
							+ vipBaseInfoDynamic.getJbjbInfoBean().getMobile()));
					startActivity(intent);
				} else {
					// intent.setData(Uri.parse("smsto:18515834121"));
					Toast.makeText(mContext, "所发短讯电话号码为空", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		btn_sendinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(mContext, "您点击了发消息",
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new Intent();
				// intent.setClass(mContext, MyTaskDriverActivity.class);
				// startActivity(intent);
				/**
				 * 跳转融云单聊界面
				 */
				if (vipBaseInfoDynamic.getJbjbInfoBean().getRy_userId() != null
						&& RongIM.getInstance() != null) {
					try {
						RongIM.getInstance().startPrivateChat(
								getActivity(),
								vipBaseInfoDynamic.getJbjbInfoBean()
										.getRy_userId(),
								vipBaseInfoDynamic.getJbjbInfoBean().getName());
					} catch (Exception e) {
					}
				}
			}
		});
	}

	/**
	 * 动态获取后台返回的数据 将左侧和右侧放入对应的map
	 * 
	 * @param vipBaseInfoDynamic
	 */
	private List<String> kvInfoNewList;// 左侧数据
	private List<String> jbxxInfoNewList;// 右侧数据

	private void getDataNew(VipBaseInfoDynamic vipBaseInfoDynamic) {
		kvInfoNewList = new ArrayList<String>();
		jbxxInfoNewList = new ArrayList<String>();
		kvInfoNewList.clear();
		jbxxInfoNewList.clear();

		Map<String, Object> kvInfoMap = vipBaseInfoDynamic.getKvInfoMap();
		Map<String, Object> jbxxInfoMap = vipBaseInfoDynamic.getJbxxInfoMap();
		for (Map.Entry<String, Object> entry : jbxxInfoMap.entrySet()) {
			System.out.println("key= " + entry.getKey() + " and value= "
					+ entry.getValue());
			if (entry.getValue() != null && !entry.getValue().equals("")) {// 右侧的值不为空
				jbxxInfoNewList.add((String) entry.getValue());
				kvInfoNewList.add((String) kvInfoMap.get(entry.getKey()));

			} else {
				System.out
						.println("=======VipBaseInfoFragment==entry.getKey()="
								+ entry.getKey() + ",entry.getValue()="
								+ entry.getValue());
			}
		}
	}

	/**
	 * 读取数据---》assets 起初没有接口数据，编造假数据到assets中的文件中然后读取
	 */
	public String getDataFromAssets() {
		try {
			InputStream inputStream = mContext.getAssets().open("vipinfo.txt");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buf = new byte[inputStream.available()];
			while ((len = inputStream.read(buf)) != -1) {
				baos.write(buf, 0, buf.length);
				return new String(baos.toByteArray());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取listview的数据
	 * 
	 * @param json
	 *            json字串 起初没有接口数据，编造假数据到assets中的文件中然后读取
	 * @return
	 */
	public VipInfoBean initDataLocal(String json) {

		Gson gson = new Gson();
		VipInfoBean bean = null;
		bean = gson.fromJson(json, VipInfoBean.class);
		return bean;
	}

	private void createEnglishQRCode(final ImageView view, final String myurl) {
		/*
		 * 这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
		 * 请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考
		 * https://github.com/GeniusVJR
		 * /LearningNotes/blob/master/Part1/Android/Android
		 * %E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
		 */
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... params) {
				return QRCodeEncoder.syncEncodeQRCode(myurl,
						BGAQRCodeUtil.dp2px(mContext, 150),
						Color.parseColor("#ff0000"));
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				if (bitmap != null) {
					view.setImageBitmap(bitmap);
				} else {
					Toast.makeText(mContext, "生成英文二维码失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}.execute();
	}
}
