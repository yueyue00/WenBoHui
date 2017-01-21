package com.gheng.exhibit.rongyun;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gheng.exhibit.rongyun.adapter.ConversationListAdapterEx;
import com.gheng.exhibit.rongyun.contact.ContactActivity;
import com.smartdot.wenbo.huiyi.R;

public class RongDemoTabs extends FragmentActivity implements OnClickListener {

	private RelativeLayout fuwudd_conversation_liner, fuwudd_qunzu_rl,
			fuwudd_contact_rl;
	private TextView right_tv, title_tv, fuwudd_conversation_tv,
			fuwudd_qunzu_tv, fuwudd_contact_tv, rong_de_num;
	private ImageView fwdd_xiaoxi_image, fuwudd_qunzu_image, add_group_img,
			fuwudd_switch_img;
	private Button back_img;
	private ViewPager fuwudd_viewpager;
	// 下划线长度
	int indicatorWidth;
	MyPagerAdapter mpageradapter;
	Fragment mConversationFragment;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 透明状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zyjactivity_rongdemotab);
		initData();
		initView();
		process();
	}

	private void initData() {
		ConversationListFragment listFragment = ConversationListFragment
				.getInstance();
		listFragment.setAdapter(new ConversationListAdapterEx(RongContext
				.getInstance()));
		Uri uri = Uri
				.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon()
				.appendPath("conversationlist")
				.appendQueryParameter(
						Conversation.ConversationType.PRIVATE.getName(),
						"false") // 设置私聊会话是否聚合显示
				.appendQueryParameter(
						Conversation.ConversationType.GROUP.getName(), "false")// 群组
				.appendQueryParameter(
						Conversation.ConversationType.DISCUSSION.getName(),
						"false")
				// 讨论组
				.appendQueryParameter(
						Conversation.ConversationType.PUBLIC_SERVICE.getName(),
						"false")
				// 公共服务号
				.appendQueryParameter(
						Conversation.ConversationType.APP_PUBLIC_SERVICE
								.getName(),
						"false")// 订阅号
				.appendQueryParameter(
						Conversation.ConversationType.SYSTEM.getName(), "false")// 系统
				.build();
		listFragment.setUri(uri);
		mConversationFragment = listFragment;
		//
		final Conversation.ConversationType[] conversationTypes = {
				Conversation.ConversationType.PRIVATE,
				Conversation.ConversationType.DISCUSSION,
				Conversation.ConversationType.GROUP,
				Conversation.ConversationType.SYSTEM,
				Conversation.ConversationType.PUBLIC_SERVICE,
				Conversation.ConversationType.APP_PUBLIC_SERVICE };
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				RongIM.getInstance().setOnReceiveUnreadCountChangedListener(
						mCountListener, conversationTypes);
			}
		}, 500);
	}

	private void initView() {
		right_tv = (TextView) findViewById(R.id.right_tv);
		title_tv = (TextView) findViewById(R.id.title_tv);
		back_img = (Button) findViewById(R.id.back_img);
		add_group_img = (ImageView) findViewById(R.id.add_group_img);
		// zyj需求更改-去掉进入和创建群组的通道
		// add_group_img.setVisibility(View.GONE);
		// right_tv.setVisibility(View.VISIBLE);
		//
		fwdd_xiaoxi_image = (ImageView) findViewById(R.id.fwdd_xiaoxi_image);
		fuwudd_qunzu_image = (ImageView) findViewById(R.id.fuwudd_qunzu_image);
		fuwudd_conversation_tv = (TextView) findViewById(R.id.fuwudd_conversation_tv);
		fuwudd_qunzu_tv = (TextView) findViewById(R.id.fuwudd_qunzu_tv);
		fuwudd_contact_tv = (TextView) findViewById(R.id.fuwudd_contact_tv);
		fuwudd_viewpager = (ViewPager) findViewById(R.id.fuwudd_viewpager);
		fuwudd_switch_img = (ImageView) findViewById(R.id.fuwudd_switch_img);
		rong_de_num = (TextView) findViewById(R.id.rong_de_num);
		//
		fuwudd_conversation_liner = (RelativeLayout) findViewById(R.id.fuwudd_conversation_liner);
		fuwudd_qunzu_rl = (RelativeLayout) findViewById(R.id.fuwudd_qunzu_rl);
		fuwudd_contact_rl = (RelativeLayout) findViewById(R.id.fuwudd_contact_rl);
		fuwudd_conversation_liner.setOnClickListener(this);
		fuwudd_qunzu_rl.setOnClickListener(this);
		fuwudd_contact_rl.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	private void process() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取屏幕信息
		indicatorWidth = dm.widthPixels / 3;// 指示器宽度为屏幕宽度的4/1
		ViewGroup.LayoutParams cursor_Params = fuwudd_switch_img
				.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		fuwudd_switch_img.setLayoutParams(cursor_Params);
		// 给viewpager设置适配器
		mfragments.add(mConversationFragment);
		mfragments.add(new GroupListFragment());
		// mfragments.add(new HMeetingSchedRQiFragment());

		mpageradapter = new MyPagerAdapter(getSupportFragmentManager(),
				mfragments);
		fuwudd_viewpager.setAdapter(mpageradapter);
		// title设置属性
		title_tv.setText("消息");
		right_tv.setText("通讯录");
		fuwudd_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					selectNavSelection(0);
					title_tv.setText("消息");
					// zyj需求更改-去掉进入和创建群组的通道
					// add_group_img.setVisibility(View.GONE);
					// right_tv.setVisibility(View.VISIBLE);
					break;
				case 1:
					selectNavSelection(1);
					title_tv.setText("群组");
					// zyj需求更改-去掉进入和创建群组的通道
					// right_tv.setVisibility(View.GONE);
					// add_group_img.setVisibility(View.VISIBLE);
					break;
				// case 2:
				// selectNavSelection(2);
				// title_tv.setText("通讯录 ");
				// break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		back_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		right_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RongDemoTabs.this,
						ContactActivity.class);
				startActivity(intent);
			}
		});
		add_group_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RongDemoTabs.this,
						ContactActivity.class);
				intent.putExtra("creategroup", true);
				startActivity(intent);
			}
		});
	}

	public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
		@Override
		public void onMessageIncreased(int count) {
			if (count == 0) {
				rong_de_num.setVisibility(View.GONE);
			} else if (count > 0 && count < 100) {
				rong_de_num.setVisibility(View.VISIBLE);
				rong_de_num.setText(count + "");
			} else {
				rong_de_num.setVisibility(View.VISIBLE);
				rong_de_num.setText(R.string.no_read_message);
			}
		}
	};

	private void selectNavSelection(int index) {
		clearSelection();
		switch (index) {
		case 0:
			fwdd_xiaoxi_image
					.setBackgroundResource(R.drawable.btn_info_highlight);
			fuwudd_conversation_tv.setTextColor(getResources().getColor(
					R.color.xiaoguo_title_bg));
			TranslateAnimation animation = new TranslateAnimation(0, 0, 0f, 0f);
			animation.setInterpolator(new LinearInterpolator());
			animation.setDuration(100);
			animation.setFillAfter(true);
			fuwudd_switch_img.startAnimation(animation);

			break;
		case 1:
			fuwudd_qunzu_image
					.setBackgroundResource(R.drawable.btn_group_highlight);
			fuwudd_qunzu_tv.setTextColor(getResources().getColor(
					R.color.xiaoguo_title_bg));
			TranslateAnimation animation1 = new TranslateAnimation(
					indicatorWidth, indicatorWidth, 0f, 0f);
			animation1.setInterpolator(new LinearInterpolator());
			animation1.setDuration(100);
			animation1.setFillAfter(true);
			fuwudd_switch_img.startAnimation(animation1);

			break;
		case 2:
			fuwudd_contact_tv.setTextColor(getResources().getColor(
					R.color.xiaoguo_title_bg));
			TranslateAnimation animation2 = new TranslateAnimation(
					2 * indicatorWidth, indicatorWidth * 2, 0f, 0f);
			animation2.setInterpolator(new LinearInterpolator());
			animation2.setDuration(100);
			animation2.setFillAfter(true);
			fuwudd_switch_img.startAnimation(animation2);
			break;
		}
	}

	private void clearSelection() {
		fwdd_xiaoxi_image.setBackgroundResource(R.drawable.btn_info);
		fuwudd_qunzu_image.setBackgroundResource(R.drawable.btn_group);
		fuwudd_conversation_tv.setTextColor(getResources().getColor(
				R.color.Gray));
		fuwudd_qunzu_tv.setTextColor(getResources().getColor(R.color.Gray));
		fuwudd_contact_tv.setTextColor(getResources().getColor(R.color.Gray));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fuwudd_conversation_liner:
			fuwudd_viewpager.setCurrentItem(0);
			break;
		case R.id.fuwudd_qunzu_rl:
			fuwudd_viewpager.setCurrentItem(1);
			break;
		case R.id.fuwudd_contact_rl:
			fuwudd_viewpager.setCurrentItem(2);
			break;
		}
	}

	private List<Fragment> mfragments = new ArrayList<Fragment>();

	private class MyPagerAdapter extends FragmentPagerAdapter {
		List<Fragment> fragments;

		public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int i) {
			return fragments.get(i);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}

}
