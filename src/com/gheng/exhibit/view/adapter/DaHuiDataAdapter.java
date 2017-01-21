package com.gheng.exhibit.view.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gheng.exhibit.http.body.response.DaHuiData;
import com.gheng.exhibit.http.body.response.DaHuiDataFiles;
import com.gheng.exhibit.http.download.DownloadInfo;
import com.gheng.exhibit.http.download.DownloadManager;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.support.FileManager;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView;
import com.gheng.exhibit.widget.PinnedHeaderExpandableListView.HeaderAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.smartdot.wenbo.huiyi.R;

/**
 * 
 * @author lileixing
 */
public class DaHuiDataAdapter extends BaseExpandableListAdapter implements
		HeaderAdapter {

	private List<DaHuiData> datas = new ArrayList<DaHuiData>();

	private Context context;
	private boolean isMine = false;
	private DownloadManager downloadManager;
	private PinnedHeaderExpandableListView listView;
	private String smallPorurl = "";
	int listsize = 0;
	private DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();

	public DaHuiDataAdapter(Context context,
			PinnedHeaderExpandableListView listView,
			DownloadManager downloadManager) {
		this.context = context;
		this.listView = listView;
		this.downloadManager = downloadManager;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showStubImage(R.drawable.a_huiyiricheng_guesticon)
				.showImageForEmptyUri(R.drawable.a_huiyiricheng_guesticon)
				.showImageOnFail(R.drawable.a_huiyiricheng_guesticon)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(180)).build();
	}

	public boolean isDownload(DaHuiDataFiles file) {

		String needdown = file.dataUploadName + file.fileFormat;
		String target = AppTools.getRootPath() + "/wenbohui/cache/";
		File cache = new File(target);
		// 如果本地没有这个目录先创建，不然会崩溃
		if (!cache.exists()) {
			cache.mkdirs();
		}
		File[] files = new File(target).listFiles();
		for (File fff : files) {
			if (fff.getName().equals(needdown)) {
				return true;
			}
		}
		return false;
	}

	private boolean isDownloadHalf(DaHuiDataFiles file) {
		if (downloadManager != null)
			for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
				DownloadInfo info = downloadManager.getDownloadInfo(i);
				if (info.getDownloadUrl().equals(file.dataUploadUrl)) {
					HttpHandler.State state = info.getState();
					if (state != state.SUCCESS)
						return true;
				}
			}
		return false;
	}

	public String getSmallPorurl() {
		return smallPorurl;
	}

	public void setSmallPorurl(String smallPorurl) {
		this.smallPorurl = smallPorurl;
	}

	public List<DaHuiData> getDatas() {
		return datas;
	}

	public void setDatas(List<DaHuiData> datas) {
		this.datas = datas;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	@Override
	public int getGroupCount() {
		for (int i = 0; i < datas.size(); i++) {
			listsize = datas.get(i).filedata.size() + listsize;
		}
		return datas.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition >= listsize || listsize == 0)
			return 0;
		return datas.get(groupPosition % listsize).filedata.size();
	}

	@Override
	public DaHuiData getGroup(int groupPosition) {
		if (listsize == 0)
			return null;
		groupPosition %= listsize;
		return datas.get(groupPosition);
	}

	@Override
	public DaHuiDataFiles getChild(int groupPosition, int childPosition) {
		groupPosition %= listsize;
		return datas.get(groupPosition).filedata.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		if (listsize != 0)
			groupPosition %= listsize;
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		if (listsize != 0)
			groupPosition %= listsize;
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (listsize != 0)
			groupPosition %= listsize;
		ViewHolderTitle holder = null;
		if (convertView == null) {
			holder = new ViewHolderTitle();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_dahuidata_group, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderTitle) convertView.getTag();
		}
		holder.tv_file.setText(datas.get(groupPosition).folderName);
		if (groupPosition % 3 == 0) {
			if (groupPosition == 1)
				holder.item_dahuidata_line.setVisibility(View.GONE);
			holder.iv_file
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao1);
			holder.tv_file.setTextColor(context.getResources().getColor(
					R.color.hong));
		} else if (groupPosition % 3 == 1) {
			holder.iv_file
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao2);
			holder.tv_file.setTextColor(context.getResources().getColor(
					R.color.cheng));
		} else if (groupPosition % 3 == 2) {
			holder.iv_file
					.setImageResource(R.drawable.wuzhen_mineroute_ziliao3);
			holder.tv_file.setTextColor(context.getResources().getColor(
					R.color.lv));
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (listsize != 0) {
			groupPosition %= listsize;
		}
		DaHuiDataFiles dfile = datas.get(groupPosition).filedata
				.get(childPosition);
		// 对已经删除的没下载完的任务进行删除
		DownloadInfo downloadInfo = mgetDownloadInfo(dfile);
		ViewHolderItem holder = new ViewHolderItem(downloadInfo);
		convertView = LayoutInflater.from(context).inflate(
				R.layout.item_dahuidata_child, null);
		ViewUtils.inject(holder, convertView);
		// 给布局设置holder标签及复用时会出现图标错乱的问题
		// if (convertView == null) {
		// holder = new ViewHolderItem(downloadInfo);
		// convertView = LayoutInflater.from(context).inflate(
		// R.layout.item_dahuidata_child, null);
		// ViewUtils.inject(holder, convertView);
		// convertView.setTag(holder);
		// holder.refresh();
		// } else {
		// holder = (ViewHolderItem) convertView.getTag();
		// holder.update(downloadInfo);
		// }

		holder.filename_tv.setText(dfile.dataUploadName + dfile.fileFormat);
		switch (dfile.fileFormat) {
		case ".avi":
			holder.type_icon.setImageResource(R.drawable.icon_avi);
			break;
		case ".mp4":
			holder.type_icon.setImageResource(R.drawable.icon_avi);
			break;
		case ".mpg":
			holder.type_icon.setImageResource(R.drawable.icon_avi);
			break;
		case ".flv":
			holder.type_icon.setImageResource(R.drawable.icon_avi);
			break;
		case ".rmvb":
			holder.type_icon.setImageResource(R.drawable.icon_avi);
			break;
		case ".txt":
			holder.type_icon.setImageResource(R.drawable.icon_txt);
			break;
		case ".doc":
			holder.type_icon.setImageResource(R.drawable.icon_doc);
			break;
		case ".docx":
			holder.type_icon.setImageResource(R.drawable.icon_doc);
			break;
		case ".csv":
			holder.type_icon.setImageResource(R.drawable.icon_csv);
			break;
		case ".jpg":
			holder.type_icon.setImageResource(R.drawable.icon_jpg);
			break;
		case ".mov":
			holder.type_icon.setImageResource(R.drawable.icon_mov);
			break;
		case ".mp3":
			holder.type_icon.setImageResource(R.drawable.icon_mp3);
			break;
		case ".pdf":
			holder.type_icon.setImageResource(R.drawable.icon_pdf);
			break;
		case ".png":
			holder.type_icon.setImageResource(R.drawable.icon_png);
			break;
		case ".ppt":
			holder.type_icon.setImageResource(R.drawable.icon_ppt);
			break;
		case ".pptx":
			holder.type_icon.setImageResource(R.drawable.icon_ppt);
			break;
		case ".rar":
			holder.type_icon.setImageResource(R.drawable.icon_rar);
			break;
		case ".xls":
			holder.type_icon.setImageResource(R.drawable.icon_xls);
			break;
		case ".xlsx":
			holder.type_icon.setImageResource(R.drawable.icon_xls);
			break;
		case ".zip":
			holder.type_icon.setImageResource(R.drawable.icon_zip);
			break;
		default:
			holder.type_icon.setImageResource(R.drawable.icon_txt);
			break;
		}
		if (isDownloadHalf(dfile)) {
			holder.download_icon.setVisibility(View.GONE);
			holder.progressBar.setVisibility(View.VISIBLE);
		} else if (isDownload(dfile)) {
			holder.download_icon.setImageResource(R.drawable.icon_downloaded);
		} else {
			holder.download_icon.setImageResource(R.drawable.icon_download);
		}
		if (downloadInfo != null) {
			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				RequestCallBack callBack = handler.getRequestCallBack();
				if (callBack instanceof DownloadManager.ManagerCallBack) {
					DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
					if (managerCallBack.getBaseCallBack() == null) {
						managerCallBack
								.setBaseCallBack(new DownloadRequestCallBack());
					}
				}
				callBack.setUserTag(new WeakReference<ViewHolderItem>(holder));
			}
		}
		if (childPosition == 0) {
			holder.item_guibingxingcheng_line.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolderTitle {
		@ViewInject(R.id.item_dahuidata_line)
		View item_dahuidata_line;
		@ViewInject(R.id.tv_file)
		TextView tv_file;
		@ViewInject(R.id.iv_file)
		ImageView iv_file;
	}

	class ViewHolderItem {
		@ViewInject(R.id.item_guibingxingcheng_line)
		View item_guibingxingcheng_line;
		@ViewInject(R.id.filename_tv)
		TextView filename_tv;
		@ViewInject(R.id.download_pb)
		ProgressBar progressBar;
		@ViewInject(R.id.tvpe_icon)
		ImageView type_icon;
		@ViewInject(R.id.download_icon)
		ImageView download_icon;

		// @ViewInject(R.id.download_ptv)
		// TextView pbtv;
		// @ViewInject(R.id.open_tv)
		// TextView opentv;
		// @ViewInject(R.id.download_tv)
		// TextView download_tv;
		private DownloadInfo downloadInfo;

		public ViewHolderItem(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}

		public void update(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
			refresh();
		}

		@OnClick(R.id.open_tv)
		public void open(View view) {
			File file = new File(Constant.target + downloadInfo.getFileName());
			// Toast.makeText(context, "打开" + localname,
			// Toast.LENGTH_SHORT).show();
			FileManager.openFile(context, file);
		}

		public void refresh() {
			if (downloadInfo != null) {
				if (downloadInfo.getFileLength() > 0) {
					progressBar
							.setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo
									.getFileLength()));
				} else {
					progressBar.setProgress(0);
				}
				// opentv.setVisibility(View.INVISIBLE);
				HttpHandler.State state = downloadInfo.getState();
				switch (state) {
				case WAITING:
					download_icon.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					break;
				case STARTED:
					download_icon.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					break;
				case LOADING:
					download_icon.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					break;
				case CANCELLED:
					// 下载完成出现这个。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
					progressBar.setVisibility(View.GONE);
					download_icon.setImageResource(R.drawable.icon_downloaded);
					download_icon.setVisibility(View.VISIBLE);
					break;
				case SUCCESS:
					// opentv.setVisibility(View.VISIBLE);
					// progressBar.setVisibility(View.INVISIBLE);
					// pbtv.setVisibility(View.INVISIBLE);
					download_icon.setImageResource(R.drawable.icon_downloaded);
					progressBar.setVisibility(View.GONE);
					download_icon.setVisibility(View.VISIBLE);

					break;
				case FAILURE:
					progressBar.setVisibility(View.GONE);
					Toast.makeText(context, "下载失败！", Toast.LENGTH_SHORT).show();
					download_icon.setImageResource(R.drawable.icon_download);
					download_icon.setVisibility(View.VISIBLE);
					break;
				default:
					progressBar.setVisibility(View.GONE);
					download_icon.setImageResource(R.drawable.icon_download);
					download_icon.setVisibility(View.VISIBLE);
					break;
				}
			}
		}
	}

	static final int TYPE_ZAN = 1;
	static final int TYPE_BAOMING = 2;

	class OnClickListenerImpl {

		long id; // 点击的管理ID
		int type;
		int groupPosition;
		int childPosition;

		OnClickListenerImpl(long id, int type, int groupPosition,
				int childPosition) {
			this.id = id;
			this.type = type;
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
		}

	}

	@Override
	public int getHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		// ScheduleListData groupData = getGroup(groupPosition);
		// if (groupData != null) {
		// ((TextView) header.findViewById(R.id.tv)).setText(groupData.title);
		// }
	}

	private SparseIntArray groupStatusMap = new SparseIntArray();

	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getGroupClickStatus(int groupPosition) {
		if (groupStatusMap.keyAt(groupPosition) >= 0) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

	public class ClickListener implements OnClickListener {

		private int groupPosition, childPosition;

		public ClickListener(int groupPosition, int childPosition) {
			// TODO Auto-generated constructor stub
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
		}

		@Override
		public void onClick(View v) {
			/*
			 * AsyncTaskForDownloadFile at = new AsyncTaskForDownloadFile( a,
			 * datas
			 * .get(groupPosition).filedata.get(childPosition).dataUploadUrl);
			 * at.execute(1);
			 */
		}
	}

	public DownloadInfo mgetDownloadInfo(DaHuiDataFiles dfile) {
		if (downloadManager != null)
			for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
				DownloadInfo info = downloadManager.getDownloadInfo(i);
				if (info.getDownloadUrl().equals(dfile.dataUploadUrl))
					return info;
			}
		return null;
	}

	private class DownloadRequestCallBack extends RequestCallBack<File> {

		@SuppressWarnings("unchecked")
		private void refreshListItem() {
			if (userTag == null)
				return;
			WeakReference<ViewHolderItem> tag = (WeakReference<ViewHolderItem>) userTag;
			ViewHolderItem holder = tag.get();
			if (holder != null) {
				holder.refresh();
			}
		}

		@Override
		public void onStart() {
			refreshListItem();
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			refreshListItem();
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			refreshListItem();
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			refreshListItem();
		}

		@Override
		public void onCancelled() {
			refreshListItem();
		}
	}

}
