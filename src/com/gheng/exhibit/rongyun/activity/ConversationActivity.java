package com.gheng.exhibit.rongyun.activity;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.ConversationBehaviorListener;
import io.rong.imkit.RongIM.OnSendMessageListener;
import io.rong.imkit.RongIM.SentMessageErrorCode;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.application.DemoContext;
import com.gheng.exhibit.application.MyApplication;
import com.gheng.exhibit.http.body.response.GroupInfoBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.rongyun.utils.RongCloudEvent;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.hebg3.wl.net.Base;
import com.hebg3.wl.net.ClientParams;
import com.hebg3.wl.net.NetTask;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.nostra13.universalimageloader.utils.L;
import com.smartdot.wenbo.huiyi.R;

/**
 * 会话界面
 * 
 * @author Administrator
 */
public class ConversationActivity extends FragmentActivity {

	private TextView mTitle;
	private Button mBack;
	private ImageView addImageView;
	private Button unReadButton;
	User parent;
	private String mTargetId;
	private String mTargetName;

	/**
	 * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
	 */
	private String mTargetIds;

	/**
	 * 会话类型
	 */
	private Conversation.ConversationType mConversationType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zactivity_conversation);
		RongCloudEvent.init(this);
		try {
			DbUtils db = DbUtils.create(this);
			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
		} catch (Exception e) {
		}
		Intent intent = getIntent();
		setActionBar();
		getIntentDate(intent);
		isReconnect(intent);
	}

	/**
	 * 设置 actionbar 事件
	 */
	private void setActionBar() {

		mTitle = (TextView) findViewById(R.id.title_tv);
		mBack = (Button) findViewById(R.id.back_img);
		addImageView = (ImageView) findViewById(R.id.add_group_img);

		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(getCurrentFocus()
						.getApplicationWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				// InputMethodManager imm = (InputMethodManager)
				// getSystemService(Context.INPUT_METHOD_SERVICE);
				// if (imm.isActive()) {
				// // 如果开启
				// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
				// InputMethodManager.HIDE_NOT_ALWAYS);
				// // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
				// }
				finish();
			}
		});

		addImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// CustomToast.showToast(ConversationActivity.this, mTargetId +
				// mTargetName);
				Intent intent = new Intent(ConversationActivity.this,
						GroupInfoActivity.class);
				GroupInfoBean bean = new GroupInfoBean();
				bean.GROUP_ID = mTargetId;
				bean.groupName = mTargetName;
				intent.putExtra("groupInfo", bean);
				startActivityForResult(intent, 101);
			}
		});

	}

	/**
	 * 设置 actionbar title
	 */
	private void setActionBarTitle(String targetid) {
		mTitle.setText(targetid);
	}

	/**
	 * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
	 */
	@SuppressWarnings("static-access")
	private void getIntentDate(Intent intent) {
		mTargetId = intent.getData().getQueryParameter("targetId");
		mTargetIds = intent.getData().getQueryParameter("targetIds");
		mTargetName = intent.getData().getQueryParameter("title");
		unReadButton = (Button) findViewById(R.id.rc_unread_message_count);

		System.out.println("mTargetId---------" + mTargetId);
		System.out.println("mTargetName---------" + mTargetName);
		// intent.getData().getLastPathSegment();//获得当前会话类型
		mConversationType = Conversation.ConversationType.valueOf(intent
				.getData().getLastPathSegment()
				.toUpperCase(Locale.getDefault()));
		System.out.println("当前回话类型是----------" + mConversationType);
		if (mConversationType.getValue() == 3) {
			addImageView.setImageResource(R.drawable.btn_addressbook_chart);
			addImageView.setVisibility(View.VISIBLE);
		} else {
			addImageView.setImageResource(R.drawable.selector_creategrp);
			addImageView.setVisibility(View.GONE);
		}
		if (RongIM.getInstance() != null) {
			RongIM.getInstance().setConversationBehaviorListener(
					new ConversationBehaviorListener() {
						@Override
						public boolean onUserPortraitLongClick(Context arg0,
								ConversationType arg1, UserInfo arg2) {
							System.out.println("头像长按");
							return false;
						}

						@Override
						public boolean onUserPortraitClick(Context arg0,
								ConversationType arg1, UserInfo arg2) {
							System.out.println("点击头像");
							return false;
						}

						@Override
						public boolean onMessageLongClick(Context arg0,
								View arg1, Message arg2) {
							System.out.println("信息长按");
							return false;
						}

						@Override
						public boolean onMessageLinkClick(Context arg0,
								String arg1) {
							System.out.println("点击连接");
							return false;
						}

						@Override
						public boolean onMessageClick(Context arg0, View arg1,
								Message message) {
							System.out.println("点击信息");
							if (message.getContent() instanceof ImageMessage) {
								ImageMessage imageMessage = (ImageMessage) message
										.getContent();
								Intent intent = new Intent(
										ConversationActivity.this,
										PhotoActivity.class);

								intent.putExtra(
										"photo",
										imageMessage.getLocalUri() == null ? imageMessage
												.getRemoteUri() : imageMessage
												.getLocalUri());
								if (imageMessage.getThumUri() != null)
									intent.putExtra("thumbnail",
											imageMessage.getThumUri());

								ConversationActivity.this.startActivity(intent);
								L.d("当点击照片后执行");
							}
							return false;
						}
					});
			RongIM.getInstance().setSendMessageListener(
					new OnSendMessageListener() {

						@Override
						public boolean onSent(Message message,
								SentMessageErrorCode sentMessageErrorCode) {
							if (message.getSentStatus() == Message.SentStatus.FAILED) {
								if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
									// 不在聊天室
								} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
									// 不在讨论组
								} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
									// 不在群组
								} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
									// 你在他的黑名单中
								}
							} else {
								// 发送成功之后保存消息
								MessageContent messageContent = message
										.getContent();
								if (messageContent instanceof TextMessage) {// 文本消息
									TextMessage textMessage = (TextMessage) messageContent;
									System.out.println("textMessage");
									if (mConversationType == Conversation.ConversationType.PRIVATE) {
										String mcontent = AppTools
												.filterEmoji(textMessage
														.getContent());
										if (mcontent.equals("")) {// 只有表情
											saveTalkHistory(
													parent.getRy_userId(),
													mTargetId, "[表情]", "", "",
													"0");
										} else {// 过滤掉表情之后的文字
											saveTalkHistory(
													parent.getRy_userId(),
													mTargetId, mcontent, "",
													"", "0");
										}
									} else {
										String mcontent = AppTools
												.filterEmoji(textMessage
														.getContent());
										if (mcontent.equals("")) {// 只有表情
											saveTalkHistory(
													parent.getRy_userId(), "",
													"[表情]", mTargetName,
													mTargetId, "1");
										} else {// 过滤掉表情之后的文字
											saveTalkHistory(
													parent.getRy_userId(), "",
													mcontent, mTargetName,
													mTargetId, "1");
										}
									}
								} else if (messageContent instanceof ImageMessage) {// 图片消息
									ImageMessage imageMessage = (ImageMessage) messageContent;
									if (mConversationType == Conversation.ConversationType.PRIVATE) {
										saveTalkHistory(parent.getRy_userId(),
												mTargetId, "[图片]", "", "", "0");
									} else {
										saveTalkHistory(parent.getRy_userId(),
												"", "[图片]", mTargetName,
												mTargetId, "1");
									}
								} else if (messageContent instanceof VoiceMessage) {// 语音消息
									VoiceMessage voiceMessage = (VoiceMessage) messageContent;
									if (mConversationType == Conversation.ConversationType.PRIVATE) {
										saveTalkHistory(parent.getRy_userId(),
												mTargetId, "[语音]", "", "", "0");
									} else {
										saveTalkHistory(parent.getRy_userId(),
												"", "[语音]", mTargetName,
												mTargetId, "1");
									}
								} else if (messageContent instanceof RichContentMessage) {// 图文消息
									RichContentMessage richContentMessage = (RichContentMessage) messageContent;
								} else {

								}
							}
							return false;
						}

						@Override
						public Message onSend(Message arg0) {
							return arg0;
						}
					});
		}
		enterFragment(mConversationType, mTargetId);
		setActionBarTitle(mTargetName);
		UserInfo userInfo = RongContext.getInstance().getUserInfoCache()
				.get(mTargetId);

	}

	// 融云 - zyj 获取token
	private void saveTalkHistory(String senderRongId, String receivedRongId,
			String content, String groupName, String groupRongId, String type) {
		ClientParams client = new ClientParams(); // 创建一个新的Http请求
		client.url = "/atAgenda.do?"; // Http 请求的地址 前面的域名封装好了
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("method=saveMessage&userid=");
		try {
			strbuf.append(Constant.decode(Constant.key, parent.getUserId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		strbuf.append("&senderName=");
		try {
			strbuf.append(Constant.decode(Constant.key, parent.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		strbuf.append("&senderRongId=");
		strbuf.append(senderRongId);
		strbuf.append("&receivedRongId=");
		strbuf.append(receivedRongId);
		strbuf.append("&content=");
		strbuf.append(content);
		strbuf.append("&groupName=");
		strbuf.append(groupName);
		strbuf.append("&groupRongId=");
		strbuf.append(groupRongId);
		strbuf.append("&type=");
		strbuf.append(type);
		strbuf.append("&language=1");

		String str = strbuf.toString();
		client.params = str;

		NetTask<Base> net = new NetTask<Base>(ronghand.obtainMessage(), client,
				0, ConversationActivity.this);
		net.execute();
	}

	/**
	 * 加载会话页面 ConversationFragment
	 */
	@SuppressLint("NewApi")
	private void enterFragment(Conversation.ConversationType mConversationType,
			String mTargetId) {

		ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
				.findFragmentById(R.id.conversation);

		Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon().appendPath("conversation")
				.appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);
	}

	/**
	 * 判断消息是否是 push 消息
	 */
	private void isReconnect(Intent intent) {

		String token = null;

		if (DemoContext.getInstance() != null) {

			token = DemoContext.getInstance().getSharedPreferences()
					.getString("DEMO_TOKEN", "default");
		}
		// push或通知过来
		if (intent != null && intent.getData() != null
				&& intent.getData().getScheme().equals("rong")) {

			// 通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
			if (intent.getData().getQueryParameter("push") != null
					&& intent.getData().getQueryParameter("push")
							.equals("true")) {

				reconnect(token);
			} else {
				// 程序切到后台，收到消息后点击进入,会执行这里
				if (RongIM.getInstance() == null
						|| RongIM.getInstance().getRongIMClient() == null) {

					reconnect(token);
				} else {
					enterFragment(mConversationType, mTargetId);
				}
			}
		}
	}

	/**
	 * 重连
	 * 
	 * @param token
	 */
	@SuppressLint("NewApi")
	private void reconnect(String token) {

		if (getApplicationInfo().packageName.equals(MyApplication
				.getCurProcessName(getApplicationContext()))) {

			RongIM.connect(token, new RongIMClient.ConnectCallback() {
				@Override
				public void onTokenIncorrect() {

				}

				@Override
				public void onSuccess(String s) {
					RongCloudEvent.getInstance().setOtherListener();
					enterFragment(mConversationType, mTargetId);
				}

				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (resultCode == 0) {
				// 如果 解散/退出 退出群成功 将关闭界面
				int bindSuccess = data.getIntExtra("exitSuccess", -1);
				if (bindSuccess == 1) {
					finish();
				}
			}
			if (resultCode == 101) {
				// 改群名
				mTargetName = data.getStringExtra("groupTitle");
				mTitle.setText(mTargetName);
			}
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	Handler ronghand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				String message = (String) msg.obj;
				// Toast.makeText(ConversationActivity.this, message,
				// Toast.LENGTH_SHORT).show();
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
}
