package com.gheng.exhibit.rongyun.utils;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AlterDialogFragment;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.TextInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.location.RealTimeLocationConstant;
import io.rong.imlib.location.message.RealTimeLocationStartMessage;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.DiscussionNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.PublicServiceMultiRichContentMessage;
import io.rong.message.PublicServiceRichContentMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.notification.PushNotificationMessage;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gheng.exhibit.application.DemoContext;
import com.gheng.exhibit.model.databases.RongGroupInfo;
import com.gheng.exhibit.model.databases.RongUserInfo;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.StartActivity;
import com.hebg3.mxy.utils.zAsyncTaskForRongGlobalGroup;
import com.hebg3.mxy.utils.zAsyncTaskForRongGlobalUser;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.utils.L;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有： 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。 5、群组信息提供者：GetGroupInfoProvider。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent implements
		RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,
		RongIM.UserInfoProvider, RongIM.GroupInfoProvider,
		RongIM.ConversationBehaviorListener,
		RongIMClient.ConnectionStatusListener, RongIM.LocationProvider,
		RongIMClient.OnReceivePushMessageListener,
		RongIM.ConversationListBehaviorListener, ApiCallback, Handler.Callback,
		RongIM.GroupUserInfoProvider {

	private static final String TAG = RongCloudEvent.class.getSimpleName();

	private static RongCloudEvent mRongCloudInstance;

	private Context mContext;
	private DbUtils db;
	User parent;

	// private AbstractHttpRequest<User> geInfoByUserIdHttpRequest;
	// private AbstractHttpRequest<User> getFriendByUserIdHttpRequest;

	/**
	 * 初始化 RongCloud.
	 * 
	 * @param context
	 *            上下文。
	 */
	public static void init(Context context) {

		if (mRongCloudInstance == null) {

			synchronized (RongCloudEvent.class) {

				if (mRongCloudInstance == null) {
					mRongCloudInstance = new RongCloudEvent(context);
				}
			}
		}
	}

	/**
	 * 构造方法。
	 * 
	 * @param context
	 *            上下文。
	 */
	private RongCloudEvent(Context context) {
		mContext = context;
		initDefaultListener();
		MSharePreferenceUtils.getAppConfig(context);
		//
		try {
			DbUtils db = DbUtils.create(mContext);
			parent = db.findFirst(Selector.from(User.class).where("id", "=",
					"1"));
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取RongCloud 实例。
	 * 
	 * @return RongCloud。
	 */
	public static RongCloudEvent getInstance() {
		return mRongCloudInstance;
	}

	/**
	 * RongIM.init(this) 后直接可注册的Listener。
	 */
	private void initDefaultListener() {

		RongIM.setUserInfoProvider(this, true);// 设置用户信息提供者。
		RongIM.setGroupInfoProvider(this, true);// 设置群组信息提供者。
		RongIM.setConversationBehaviorListener(this);// 设置会话界面操作的监听器。
		RongIM.setLocationProvider(this);// 设置地理位置提供者,不用位置的同学可以注掉此行代码
		RongIM.setConversationListBehaviorListener(this);// 会话列表界面操作的监听器
		RongIM.getInstance().setSendMessageListener(this);// 设置发出消息接收监听器.
		RongIM.setGroupUserInfoProvider(this, true);
		RongIM.setOnReceivePushMessageListener(this);// 自定义 push 通知。
		RongIM.getInstance().setMessageAttachedUserInfo(true);// 消息体内是否有
		// // userinfo 这个属性

		RongIM.getInstance().enableNewComingMessageIcon(true);// 显示新消息提醒
		RongIM.getInstance().enableUnreadMessageIcon(true);// 显示未读消息数目
	}

	/**
	 * 连接成功注册。
	 * <p/>
	 * 在RongIM-connect-onSuc cess后调用。
	 */
	@SuppressWarnings("static-access")
	public void setOtherListener() {

		RongIM.getInstance().getRongIMClient()
				.setOnReceiveMessageListener(this);// 设置消息接收监听器。
		RongIM.getInstance().getRongIMClient()
				.setConnectionStatusListener(this);// 设置连接状态监听器。

		TextInputProvider textInputProvider = new TextInputProvider(
				RongContext.getInstance());
		RongIM.setPrimaryInputProvider(textInputProvider);

		// 扩展功能自定义
		InputProvider.ExtendProvider[] provider = {
				new ImageInputProvider(RongContext.getInstance()),// 图片
				new CameraInputProvider(RongContext.getInstance()),// 相机
		// new
		// RealTimeLocationInputProvider(RongContext.getInstance()),//
		// 地理位置
		// new VoIPInputProvider(RongContext.getInstance()),// 语音通话
		// new ContactsProvider(RongContext.getInstance()),// 通讯录
		};

		InputProvider.ExtendProvider[] provider1 = {
				new ImageInputProvider(RongContext.getInstance()),// 图片
				new CameraInputProvider(RongContext.getInstance()),// 相机
		// new
		// RealTimeLocationInputProvider(RongContext.getInstance()),//地理位置
		// new ContactsProvider(RongContext.getInstance()),// 通讯录
		};

		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.PRIVATE, provider);

		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.DISCUSSION, provider1);
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.GROUP, provider1);
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.CUSTOMER_SERVICE, provider1);
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.CHATROOM, provider1);
	}

	/**
	 * 自定义 push 通知。
	 * 
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public boolean onReceivePushMessage(PushNotificationMessage msg) {
		// zyj修改StartActivity-MainActivity
		Intent intent = new Intent(mContext, StartActivity.class);
		// Uri uri;
		//
		// intent.setAction(Intent.ACTION_VIEW);
		//
		// Conversation.ConversationType conversationType =
		// msg.getConversationType();
		//
		// uri = Uri.parse("rong://" +
		// RongContext.getInstance().getPackageName()).buildUpon().appendPath("conversationlist").build();
		// intent.setData(uri);
		// Log.d(TAG, "onPushMessageArrive-url:" + uri.toString());

		Notification notification = null;

		PendingIntent pendingIntent = PendingIntent.getActivity(
				RongContext.getInstance(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		String contentSext = msg.getPushContent();
		System.out.println("zyj:RongCloudEvent" + contentSext);
		Toast.makeText(mContext, "zyj:RongCloudEvent" + contentSext,
				Toast.LENGTH_SHORT).show();
		// 私聊会话的时候要手动把senderName加到contextString里面
		if (msg.getConversationType().getValue() == 1) {
			contentSext = msg.getSenderName() + ":" + msg.getPushContent();
		}
		// zyj 这里好像没有用
		if (android.os.Build.VERSION.SDK_INT < 11) {
			notification = new Notification(RongContext.getInstance()
					.getApplicationInfo().icon, "移动门户",
					System.currentTimeMillis());

			notification.setLatestEventInfo(RongContext.getInstance(), "移动门户",
					contentSext, pendingIntent);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND;
		} else {

			notification = new Notification.Builder(RongContext.getInstance())
					.setLargeIcon(getAppIcon()).setTicker("移动门户")
					.setContentTitle("移动门户").setContentText(contentSext)
					.setContentIntent(pendingIntent).setAutoCancel(true)
					.setDefaults(Notification.DEFAULT_ALL).build();

		}

		NotificationManager nm = (NotificationManager) RongContext
				.getInstance().getSystemService(
						RongContext.getInstance().NOTIFICATION_SERVICE);

		nm.notify(0, notification);

		return true;
	}

	private Bitmap getAppIcon() {
		BitmapDrawable bitmapDrawable;
		Bitmap appIcon;
		bitmapDrawable = (BitmapDrawable) RongContext.getInstance()
				.getApplicationInfo()
				.loadIcon(RongContext.getInstance().getPackageManager());
		appIcon = bitmapDrawable.getBitmap();
		return appIcon;
	}

	/**
	 * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
	 * 
	 * @param message
	 *            接收到的消息的实体信息。
	 * @param left
	 *            剩余未拉取消息数目。
	 */
	@Override
	public boolean onReceived(Message message, int left) {

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			textMessage.getExtra();
			Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onReceived-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onReceived-RichContentMessage:"
							+ richContentMessage.getContent());
		} else if (messageContent instanceof InformationNotificationMessage) {// 小灰条消息

		} else if (messageContent instanceof ContactNotificationMessage) {// 好友添加消息
			ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-ContactNotificationMessage:getExtra;"
					+ contactContentMessage.getExtra());
			Log.d(TAG, "onReceived-ContactNotificationMessage:+getmessage:"
					+ contactContentMessage.getMessage().toString());
			Intent in = new Intent();
			in.setAction("action_demo_agree_request");
			in.putExtra("rongCloud", contactContentMessage);
			in.putExtra("has_message", true);
			mContext.sendBroadcast(in);
		} else if (messageContent instanceof DiscussionNotificationMessage) {// 讨论组通知消息
			DiscussionNotificationMessage discussionNotificationMessage = (DiscussionNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-discussionNotificationMessage:getExtra;"
					+ discussionNotificationMessage.getOperator());
			setDiscussionName(message.getTargetId());
		} else {
			Log.d(TAG, "onReceived-其他消息，自己来判断处理");
		}

		return false;

	}

	/**
	 * 讨论组名称修改后刷新本地缓存
	 * 
	 * @param targetId
	 *            讨论组 id
	 */
	private void setDiscussionName(String targetId) {

		if (RongIM.getInstance() != null
				&& RongIM.getInstance().getRongIMClient() != null) {
			RongIM.getInstance()
					.getRongIMClient()
					.getDiscussion(targetId,
							new RongIMClient.ResultCallback<Discussion>() {
								@Override
								public void onSuccess(Discussion discussion) {

									RongIM.getInstance()
											.refreshDiscussionCache(discussion);
									Log.i(TAG, "------discussion.getName---"
											+ discussion.getName());
								}

								@Override
								public void onError(RongIMClient.ErrorCode e) {

								}
							});
		}
	}

	/**
	 * 消息发送前监听器处理接口（是否发送成功可以从SentStatus属性获取）。
	 * 
	 * @param message
	 *            发送的消息实例。
	 * @return 处理后的消息实例。
	 */
	@Override
	public Message onSend(Message message) {

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.e("qinxiao", "--onSend:" + textMessage.getContent()
					+ ", extra=" + message.getExtra());
		}

		return message;
	}

	/**
	 * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
	 * 
	 * @param message
	 *            消息。
	 */
	@Override
	public boolean onSent(Message message,
			RongIM.SentMessageErrorCode sentMessageErrorCode) {
		Log.e("qinxiao", "onSent:" + message.getObjectName() + ", extra="
				+ message.getExtra());

		if (message.getSentStatus() == Message.SentStatus.FAILED) {

			if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {// 不在聊天室

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {// 不在讨论组

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {// 不在群组

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {// 你在他的黑名单中
				// CustomToast.showToast(mContext, "你在对方的黑名单中");
				Toast.makeText(mContext, "你在对方的黑名单中", Toast.LENGTH_SHORT)
						.show();
			}
		}

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.e(TAG, "onSent-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onSent-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onSent-RichContentMessage:"
							+ richContentMessage.getContent());
		} else {
			Log.d(TAG, "onSent-其他消息，自己来判断处理");
		}
		return false;
	}

	/**
	 * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
	 * 
	 * @param userId
	 *            用户 Id。
	 * @return 用户信息，（注：由开发者提供用户信息）。
	 */
	@SuppressWarnings("unused")
	@Override
	public UserInfo getUserInfo(String userId) {
		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		if (userId == null)
			return null;
		if (DemoContext.getInstance() == null)
			return null;
		// RongUserInfo rongUserInfo;
		String new_username = "";
		try {
			// System.out.println("getUserInfo1" + userId);
			// db = DbUtils.create(mContext);
			// rongUserInfo = db.findFirst(Selector.from(RongUserInfo.class)
			// .where("userId", "=", userId));
			// db.close();
			// System.out.println("getUserInfo2" + rongUserInfo.toString());
			new_username = (String) MSharePreferenceUtils.getParam(userId, "");
		} catch (Exception e) {
			System.out.println("RongCloudEvent:getUserInfo" + "出错了");
			return null;
		}
		// if (rongUserInfo != null) {
		// UserInfo userInfo = new UserInfo(rongUserInfo.userId,
		// rongUserInfo.name, null);
		// return userInfo;
		// }
		if (!new_username.equals("")) {
			UserInfo userInfo = new UserInfo(userId, new_username, null);
			System.out.println("RongCloudEvent:getUserInfo:" + new_username);
			return userInfo;
		}
		return null;
	}

	// UserInfo userInfo;

	@SuppressWarnings("unused")
	/**
	 * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
	 * 
	 * @param groupId
	 *            群组 Id.
	 * @return 群组信息，（注：由开发者提供群组信息）。
	 */
	@Override
	public Group getGroupInfo(String groupId) {

		if (DemoContext.getInstance() == null)
			return null;
		// RongGroupInfo rongGroupInfo;
		String new_groupname = "";
		try {
			// System.out.println("getGroupInfo1" + groupId);
			// db = DbUtils.create(mContext);
			// rongGroupInfo = db.findFirst(Selector.from(RongGroupInfo.class)
			// .where("groupId", "=", groupId));
			// db.close();
			// System.out.println("getGroupInfo2" + rongGroupInfo.toString());
			new_groupname = (String) MSharePreferenceUtils
					.getParam(groupId, "");
		} catch (Exception e) {
			return null;
		}
		// if (rongGroupInfo != null) {
		// Group ronggGroup = new Group(rongGroupInfo.groupId,
		// rongGroupInfo.name, null);
		// return ronggGroup;
		// }
		if (!new_groupname.equals("")) {
			Group ronggGroup = new Group(groupId, new_groupname, null);
			return ronggGroup;
		}
		return null;
	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param conversationType
	 *            会话类型。
	 * @param user
	 *            被点击的用户的信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onUserPortraitClick(Context context,
			Conversation.ConversationType conversationType, UserInfo user) {

		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		if (user != null) {

			if (conversationType
					.equals(Conversation.ConversationType.PUBLIC_SERVICE)
					|| conversationType
							.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
				RongIM.getInstance().startPublicServiceProfile(mContext,
						conversationType, user.getUserId());
			} else {
				// Intent in = new Intent(context,
				// PersonalDetailActivity.class);
				// in.putExtra("USER", user);
				// context.startActivity(in);
				L.d("当点击用户头像后执行");
			}
		}

		return false;
	}

	@Override
	public boolean onUserPortraitLongClick(Context context,
			Conversation.ConversationType conversationType, UserInfo userInfo) {
		Log.e(TAG, "----onUserPortraitLongClick");

		return true;
	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param message
	 *            被点击的消息的实体信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onMessageClick(final Context context, final View view,
			final Message message) {
		Log.e(TAG, "----onMessageClick");

		// real-time location message begin
		if (message.getContent() instanceof RealTimeLocationStartMessage) {
			RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient
					.getInstance().getRealTimeLocationCurrentState(
							message.getConversationType(),
							message.getTargetId());

			// if (status ==
			// RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE)
			// {
			// startRealTimeLocation(context, message.getConversationType(),
			// message.getTargetId());
			// } else
			if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {

				final AlterDialogFragment alterDialogFragment = AlterDialogFragment
						.newInstance("", "加入位置共享", "取消", "加入");
				alterDialogFragment
						.setOnAlterDialogBtnListener(new AlterDialogFragment.AlterDialogBtnListener() {

							@Override
							public void onDialogPositiveClick(
									AlterDialogFragment dialog) {
								RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient
										.getInstance()
										.getRealTimeLocationCurrentState(
												message.getConversationType(),
												message.getTargetId());

								if (status == null
										|| status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE) {
									startRealTimeLocation(context,
											message.getConversationType(),
											message.getTargetId());
								} else {
									joinRealTimeLocation(context,
											message.getConversationType(),
											message.getTargetId());
								}

							}

							@Override
							public void onDialogNegativeClick(
									AlterDialogFragment dialog) {
								alterDialogFragment.dismiss();
							}
						});

				alterDialogFragment.show(((FragmentActivity) context)
						.getSupportFragmentManager());
			} else {

				if (status != null
						&& (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_OUTGOING || status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_CONNECTED)) {

					// Intent intent = new Intent(((FragmentActivity) context),
					// RealTimeLocationActivity.class);
					// intent.putExtra("conversationType",
					// message.getConversationType().getValue());
					// intent.putExtra("targetId", message.getTargetId());
					// context.startActivity(intent);
					L.d("当点消息后执行");
				}
			}
			return true;
		}

		// real-time location message end
		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		if (message.getContent() instanceof LocationMessage) {
			// Intent intent = new Intent(context, SOSOLocationActivity.class);
			// intent.putExtra("location", message.getContent());
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(intent);
			L.d("当点击地理位置后执行");
		} else if (message.getContent() instanceof RichContentMessage) {
			RichContentMessage mRichContentMessage = (RichContentMessage) message
					.getContent();
			Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());
			Log.e(TAG, "----RichContentMessage-------");

		} else if (message.getContent() instanceof ImageMessage) {
			// ImageMessage imageMessage = (ImageMessage) message.getContent();
			// Intent intent = new Intent(context, PhotoActivity.class);
			//
			// intent.putExtra("photo", imageMessage.getLocalUri() == null ?
			// imageMessage.getRemoteUri() : imageMessage.getLocalUri());
			// if (imageMessage.getThumUri() != null)
			// intent.putExtra("thumbnail", imageMessage.getThumUri());
			//
			// context.startActivity(intent);
			L.d("当点击照片后执行");
		} else if (message.getContent() instanceof PublicServiceMultiRichContentMessage) {
			Log.e(TAG, "----PublicServiceMultiRichContentMessage-------");

		} else if (message.getContent() instanceof PublicServiceRichContentMessage) {
			Log.e(TAG, "----PublicServiceRichContentMessage-------");

		}

		Log.d("Begavior",
				message.getObjectName() + ":" + message.getMessageId());

		return false;
	}

	private void startRealTimeLocation(Context context,
			Conversation.ConversationType conversationType, String targetId) {
		// RongIMClient.getInstance().startRealTimeLocation(conversationType,
		// targetId);
		//
		// Intent intent = new Intent(((FragmentActivity) context),
		// RealTimeLocationActivity.class);
		// intent.putExtra("conversationType", conversationType.getValue());
		// intent.putExtra("targetId", targetId);
		// context.startActivity(intent);
		L.d("当点击用户头像后执行");
	}

	private void joinRealTimeLocation(Context context,
			Conversation.ConversationType conversationType, String targetId) {
		// RongIMClient.getInstance().joinRealTimeLocation(conversationType,
		// targetId);
		//
		// Intent intent = new Intent(((FragmentActivity) context),
		// RealTimeLocationActivity.class);
		// intent.putExtra("conversationType", conversationType.getValue());
		// intent.putExtra("targetId", targetId);
		// context.startActivity(intent);
	}

	/**
	 * 当点击链接消息时执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param link
	 *            被点击的链接。
	 * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
	 */
	@Override
	public boolean onMessageLinkClick(Context context, String link) {
		return false;
	}

	@Override
	public boolean onMessageLongClick(Context context, View view,
			Message message) {

		Log.e(TAG, "----onMessageLongClick");
		return false;
	}

	/**
	 * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
	 * 
	 * @param status
	 *            网络状态。
	 */
	@Override
	public void onChanged(ConnectionStatus status) {
		Log.d(TAG, "onChanged:" + status);

		if (status.getMessage().equals(
				ConnectionStatus.DISCONNECTED.getMessage())) {
			/** 未连接 */
		}
		if (status.getMessage().equals(ConnectionStatus.CONNECTED.getMessage())) {
			/** 已连接 */
		}
		if (status.getMessage().equals(
				ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT.getMessage())) {
			/** 用户账户在其他设备登录，本机会被踢掉线。 */
			handler.sendEmptyMessage(1);
		}
		if (status.getMessage().equals(
				ConnectionStatus.NETWORK_UNAVAILABLE.getMessage())) {
			/** 网络不可用。 */
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// CustomToast.showToast(mContext, "您的账号在别的设备上登录，您被迫下线！");
				Toast.makeText(mContext, "您的账号在别的设备上登录！", Toast.LENGTH_SHORT)
						.show();
				// zyj注销
				// Intent it = new Intent(mContext, LoginActivity.class);
				// it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// mContext.startActivity(it);
				// BaseActivity._instance.exitAllAct();
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
	 * 
	 * @param context
	 *            上下文
	 * @param callback
	 *            回调
	 */
	@Override
	public void onStartLocation(Context context, LocationCallback callback) {
		/**
		 * demo 代码 开发者需替换成自己的代码。
		 */
		// DemoContext.getInstance().setLastLocationCallback(callback);
		//
		// Intent intent = new Intent(context, SOSOLocationActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(intent);
	}

	/**
	 * 点击会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationClick(Context context, View view,
			UIConversation conversation) {
		MessageContent messageContent = conversation.getMessageContent();

		Log.e(TAG, "--------onConversationClick-------");
		if (messageContent instanceof TextMessage) {// 文本消息

			TextMessage textMessage = (TextMessage) messageContent;

			textMessage.getExtra();
		} else if (messageContent instanceof ContactNotificationMessage) {
			Log.e(TAG, "---onConversationClick--ContactNotificationMessage-");
			L.d("点击会话列表 item 后执行。");
			// context.startActivity(new Intent(context,
			// NewFriendListActivity.class));
			return true;
		}

		return false;
	}

	/**
	 * 长按会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            长按会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationLongClick(Context context, View view,
			UIConversation conversation) {
		return false;
	}

	@Override
	public void onComplete(AbstractHttpRequest arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(AbstractHttpRequest arg0, BaseException arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public GroupUserInfo getGroupUserInfo(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean handleMessage(android.os.Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onConversationPortraitClick(Context arg0,
			ConversationType arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onConversationPortraitLongClick(Context arg0,
			ConversationType arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}

}
