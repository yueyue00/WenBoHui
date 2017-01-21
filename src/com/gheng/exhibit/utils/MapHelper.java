package com.gheng.exhibit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import android.content.Context;

import com.gheng.exhibit.model.databases.Language;

public class MapHelper {

	/**
	 * 获取地图路径
	 * @param city
	 * @param indoorId
	 * @return
	 */
	public static String getMapPath() {
		return getDataPath() + File.separator+"7.2"+File.separator;
	}
	
	public static String getDataPath(){
		String root = AppTools.getRootPath();
		String path = root + File.separator + "ghmap" + File.separator
				+ SharedData.getBatchId() ;
		File pFile = new File(path);
		if (!pFile.exists()) {
			pFile.mkdirs();
		}
		return path;
	}
	
	/**
	 * 
	 * @param floorName
	 * @return
	 */
	public static String getMapFloorPath(String floorName) {
		String root = AppTools.getRootPath();
		String path = root + File.separator + "ghmap" + File.separator
				+ SharedData.getBatchId() + File.separator + floorName + File.separator;
		File pFile = new File(path);
		if (!pFile.exists()) {
			pFile.mkdirs();
		}
		return path;
	}

	public static String getWifiPath() {
		return getMapPath() + File.separator + "r";
	}

	public static String getBeaconPath() {
		return getMapPath() + File.separator + "b";
	}

	public static String getMapVersion() {
		return SharedData.getString(SharedData.M_VERSION, "0");
	}

	public static String getWifiVersion() {
		return SharedData.getString(SharedData.R_VERSION, "0");
	}

	public static String getBeaconVersion() {
		return SharedData.getString(SharedData.B_VERSION, "0");
	}

	public static void writeMapVersion(String mapVersion) {
		SharedData.commit(SharedData.M_VERSION, mapVersion);
	}

	public static void writeWifiVersion(String radioVersion) {
		SharedData.commit(SharedData.R_VERSION, radioVersion);
	}

	public static void writeBeaconVersion(String bcVersion) {
		SharedData.commit(SharedData.B_VERSION, bcVersion);
	}

	public static boolean isExistMap() {
		String file = getMapPath() + File.separator + "ghmap.dat";
		File f = new File(file);
		if (f.exists()) {
			return true;
		}
		return false;
	}

	public static boolean isExistWifi() {
		String file = getWifiPath() + File.separator + "info.dat";
		File f = new File(file);
		if (f.exists()) {
			return true;
		}
		return false;
	}

	public static boolean isExistBeacon() {
		String file = getBeaconPath() + File.separator + "info.dat";
		File f = new File(file);
		if (f.exists()) {
			return true;
		}
		return false;
	}

	public static void upZipFile(String zipFile, String folderPath)
			throws ZipException, IOException {
		File file = new File(folderPath);
		if (!file.exists()) {
			// 创建目标目录
			file.mkdirs();
		}
		FileInputStream is = new FileInputStream(zipFile);
		ZipInputStream zipInputStream = new ZipInputStream(is);
		// 读取一个进入点
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		byte[] buffer = new byte[1024 * 1024];
		// 解压时字节计数
		int count = 0;
		// 如果进入点为空说明已经遍历完所有压缩包中文件和目录
		while (zipEntry != null) {
			if (zipEntry.isDirectory()) {
				file = new File(folderPath + File.separator
						+ zipEntry.getName());
				file.mkdirs();
			} else {
				file = new File(folderPath + File.separator
						+ zipEntry.getName());
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				while ((count = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, count);
				}
				fileOutputStream.close();
			}
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
		is.close();
	}

	/**
	 * 下载指纹
	 */
	public static boolean download(String mapurl, String mapVersion,
			String wifiurl, String radioVersion, String beaconUrl,
			String bcVersion) {
		boolean mapFlag = true;
		boolean wifiFlag = true;
		boolean beaconFlag = true;
		try {
			if (!StringTools.isBlank(mapurl)) {
				mapFlag = loadZip(mapurl, getMapPath(), "ghmap.zip");
				upZipFile(getMapPath() + "ghmap.zip", getMapPath());
				writeMapVersion(mapVersion);
			}
			if (!StringTools.isBlank(wifiurl)) {
				wifiFlag = loadZip(wifiurl, getMapPath(), SharedData.getBatchId()
						+ "_r.zip");
				upZipFile(getMapPath() + SharedData.getBatchId() + "_r.zip", getWifiPath());
				writeWifiVersion(radioVersion);
			}
			if (!StringTools.isBlank(beaconUrl)) {
				beaconFlag = loadZip(beaconUrl, getMapPath(),SharedData.getBatchId()
						+ "_b.zip");

				upZipFile(getMapPath() + SharedData.getBatchId() + "_b.zip",
						getBeaconPath());

				writeBeaconVersion(bcVersion);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapFlag = false;
		}
		return mapFlag && wifiFlag && beaconFlag;
	}

	/**
	 * zip数据下载并
	 * 
	 * @param _urlStr
	 * @param path
	 *            绝对地址
	 * @return
	 */
	public static boolean loadZip(String _urlStr, String path, String fileName) {
		File file = new File(path);
		// 如果目标目录不存在，则创建
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			URL url = new URL(Constant.SERVER_URL + _urlStr);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(10 * 1000);
			con.setReadTimeout(60 * 1000);
			// contentLength为-1时，添加这一句
			con.setRequestProperty("Accept-Encoding", "identity");

			byte[] buffer = new byte[1024 * 1024];
			int count = 0;
			InputStream inputStream = con.getInputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(new File(
					file, fileName));

			while ((count = inputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, count);
			}
			fileOutputStream.close();
			inputStream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 从asset中拷贝文件，覆盖 拷贝为zip文件，解压缩
	 */
	public static void copyFromAsset(Context context) {
		InputStream is = null;
		try {
			is = context.getResources().getAssets().open("ghmap.zip");
			upZipFile(is, getDataPath());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 解压zip文件到指定目录
	 * @param is zip文件输入流
	 * @param folderPath 解压目标目录
	 * @throws ZipException 解压异常
	 * @throws IOException 输入输出异常
	 */
	public static void upZipFile(InputStream is, String folderPath)
			throws ZipException, IOException {
		File file = new File(folderPath);
		if (!file.exists()) {
			// 创建目标目录
			file.mkdirs();
		}
		ZipInputStream zipInputStream = new ZipInputStream(is);
		// 读取一个进入点
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		byte[] buffer = new byte[1024 * 1024];
		// 解压时字节计数
		int count = 0;
		// 如果进入点为空说明已经遍历完所有压缩包中文件和目录
		while (zipEntry != null) {
			if (zipEntry.isDirectory()) {
				file = new File(folderPath + File.separator
						+ zipEntry.getName());
				file.mkdirs();
			} else {
				file = new File(folderPath + File.separator
						+ zipEntry.getName());
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				while ((count = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, count);
				}
				fileOutputStream.close();
			}
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
		is.close();
	}
	
	public static String getSpaceNames(String spaceName){
		if(StringTools.isBlank(spaceName)){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String[] split = spaceName.trim().split("@@@");
		String[] results = new String[split.length];
		for (int i = 0 ; i < split.length; i ++) {
			String name = split[i];
			results[i] = getName(name);
		}
		for(int i = 0 ; i < results.length; i ++){
			if(i > 0){
				sb.append(" , ");
			}
			sb.append(results[i]);
		}
		return sb.toString();
	}
	
	public static String getPoiName(String poiName){
		return getName(poiName);
	}
	
	private static String getName(String name){
		if(StringTools.isBlank(name))
			return "";
		String[] split = name.trim().split("###",2);
		if(split.length == 1)
			return split[0];
		String result ;
		if(SharedData.getInt("i18n", Language.ZH) == Language.EN){
			result = split[1];
		}else{
			result = split[0];
		}
		return result;
	}
	
	public static String getFloorName(String floorName) {
		return floorName.replace("H", "");
	}
	
	public static String getFloorNo(String floorName) {
		String f = getFloorName(floorName);
		int index = f.indexOf(".");
		return f.substring(index + 1);
	}
}
