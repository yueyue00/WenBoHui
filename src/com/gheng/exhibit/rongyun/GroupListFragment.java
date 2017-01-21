package com.gheng.exhibit.rongyun;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GroupInfoProvider;
import io.rong.imlib.model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.RongGroup;
import com.gheng.exhibit.http.body.response.RongGroup.RenyuanBean;
import com.gheng.exhibit.model.databases.User;
import com.gheng.exhibit.rongyun.adapter.GroupListAdapter;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.hebg3.mxy.utils.IsWebCanBeUse;
import com.hebg3.mxy.utils.zAsyncTaskForRongGroup;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.smartdot.wenbo.huiyi.R;

public class GroupListFragment extends Fragment {

	private Context mContext;
	private SwipeRefreshLayout swipe;
	/** 群组列表 */
	private ListView groupListView;
	/** 群组列表的adapter */
	private GroupListAdapter mAdapter;
	/** 群组信息 */
	private List<RenyuanBean> mList;
	private TextView nodatafoundtv;
	User muser;
	// 获取群组列表的返回值
	Handler users_handler = new Handler() {
		@SuppressWarnings("static-access")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 融云获取token时返回内容
				RongGroup rongroup = (RongGroup) msg.obj;
				ArrayList<RenyuanBean> renyuan = (ArrayList<RenyuanBean>) rongroup.renyuan;
				ArrayList<RenyuanBean> tuan = (ArrayList<RenyuanBean>) rongroup.qunzu;
				try {
					for (int i = 0; i < renyuan.size(); i++) {

						if (renyuan.get(i).shortname.equals(Constant.decode(
								Constant.key, muser.getUserId()))) {
							renyuan.remove(i);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mList.clear();
				mList.addAll(renyuan);
				mList.addAll(tuan);
				mAdapter.setmList(mList);
				mAdapter.notifyDataSetChanged();
				if (RongIM.getInstance() != null) {
					// zyj 这里干什么用的？
					RongIM.getInstance().setGroupInfoProvider(
							new GroupInfoProvider() {

								@Override
								public Group getGroupInfo(String groupId) {
									// Group group;
									// try {
									// group = new Group(groupId,
									// GlobalConfig.RongYun.groupMap
									// .get(groupId), null);
									// } catch (RuntimeException e) {
									// group = new Group(groupId, "默认群组名",
									// null);
									// }
									return null;
								}
							}, true);
				}
				break;
			case 1:
				Toast.makeText(mContext, "请求错误！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mContext, "请求失败！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				nodatafoundtv.setVisibility(View.VISIBLE);
				Toast.makeText(mContext, "数据为空！", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mContext, "cookie失效，请求超时！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
			swipe.setRefreshing(false);
			swipe.setEnabled(true);
			nodatafoundtv.setVisibility(View.GONE);
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.zyjfragment_group_list,
				container, false);
		mContext = getActivity();
		try {
			DbUtils db = DbUtils.create(mContext);
			muser = db.findFirst(Selector.from(User.class)
					.where("id", "=", "1"));
			db.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		initListView(view);
		return view;
	}

	/**
	 * 初始化列表
	 */
	private void initListView(View view) {
		mList = new ArrayList<RenyuanBean>();
		swipe = (SwipeRefreshLayout) view.findViewById(R.id.group_list_srl);
		nodatafoundtv = (TextView) view.findViewById(R.id.nodatafoundtv);
		groupListView = (ListView) view.findViewById(R.id.group_list);
		mAdapter = new GroupListAdapter(mContext, mList);
		groupListView.setAdapter(mAdapter);

		nodatafoundtv.setText(BaseActivity.getLanguageString("暂时没有数据"));
		swipe.setColorSchemeColors(this.getResources().getColor(
				R.color.swiperefreshlayoutcolor));

		addlistener();
	}

	/**
	 * 获取数据
	 */
	private void getData(String pagesize, String pagenum) {
		if (!IsWebCanBeUse.isWebCanBeUse(mContext)) {
			Toast.makeText(mContext, BaseActivity.getLanguageString("网络不给力"),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			try {
				zAsyncTaskForRongGroup at = new zAsyncTaskForRongGroup(
						users_handler.obtainMessage(), mContext,
						Constant.decode(Constant.key, muser.getUserId()));
				at.execute(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addlistener() {
		// 下拉刷新回调 getData("10", "1");
		swipe.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				swipe.setEnabled(false);
				getData("20", "1");
			}
		});

		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RenyuanBean bean = mList.get(position);
				if (bean.truename != null && bean.USER_ID != null) {
					if (RongIM.getInstance() != null) {
						try {
							RongIM.getInstance().startPrivateChat(mContext,
									bean.USER_ID, bean.truename);
						} catch (Exception e) {
						}
					}
				} else if (bean.group_id != null && bean.name != null) {
					if (RongIM.getInstance() != null) {
						try {
							RongIM.getInstance().startGroupChat(mContext,
									bean.group_id, bean.name);
						} catch (Exception e) {
						}
					}
				}
			}
		});

	}

	/**
	 * 过滤emoj表情
	 */
	InputFilter emojiFilter = new InputFilter() {

		Pattern emoji = Pattern
				.compile(
						"[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
						Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {

				return "";
			}
			return null;
		}
	};

	@Override
	public void onResume() {
		mList = new ArrayList<RenyuanBean>();
		getData("20", "1");
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
