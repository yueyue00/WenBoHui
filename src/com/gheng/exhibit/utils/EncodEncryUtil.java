package com.gheng.exhibit.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.google.zxing.common.StringUtils;

/**
 * 加密解密编码
 * 
 * @author ms
 * @datetime 2015-11-12
 */
public class EncodEncryUtil {

	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	public static String Salt(String password,String salt) {
    	password=string2MD5(password);
    	
        password = md5Hex(password + salt);  
        return password;  
    }  
	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return inStr;
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

	/**
	 * 使用base64编码字符串
	 * 
	 * @param data
	 * @return 编码后的字符串
	 */
	public static String compressData(String data) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DeflaterOutputStream zos = new DeflaterOutputStream(bos);
			zos.write(data.getBytes());
			zos.close();
			return new String(getenBASE64inCodec(bos.toByteArray()));
		} catch (Exception ex) {
			ex.printStackTrace();
			return data;
		}
	}

	/**
	 * 调用apache的编码方法
	 */
	public static String getenBASE64inCodec(byte[] b) {
		if (b == null) {
			return null;
		}
		return new String((new Base64()).encode(b));
	}

	/**
	 * 使用base64解码字符串
	 * 
	 * @param encdata
	 * @return 解码后的字符串
	 */
	public static String decompressData(String encdata) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			InflaterOutputStream zos = new InflaterOutputStream(bos);
			zos.write(getdeBASE64inCodec(encdata));
			zos.close();
			return new String(bos.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
			return encdata;
		}
	}

	/**
	 * 调用apache的解码方法
	 */
	public static byte[] getdeBASE64inCodec(String s) {
		if (s == null) {
			return null;
		}
		return new Base64().decode(s.getBytes());
	}

	/**
	 * 生成随机数字和字母
	 */
	public static String getStringRandom(int length) {
		String val = "";
		Random random = new Random();
		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 生成随机数字和字母 内容自己定义
	 */
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyz0123456789";
		// String
		// str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {

			int number = random.nextInt(str.length());

			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * des解密
	 * 
	 * @param decryptString
	 * @param decryptKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptDES(String decryptString, String decryptKey)
			throws Exception {
		byte[] byteMi = decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
		return new String(decryptedData);
	}

	public static byte[] decode(String s) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			decode(s, bos);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		byte[] decodedBytes = bos.toByteArray();
		try {
			bos.close();
			bos = null;
		} catch (IOException ex) {
			System.err.println("Error while decoding BASE64: " + ex.toString());
		}
		return decodedBytes;
	}

	private static void decode(String s, OutputStream os) throws IOException {
		int i = 0;
		int len = s.length();
		while (true) {
			while (i < len && s.charAt(i) <= ' ')
				i++;
			if (i == len)
				break;
			int tri = (decode(s.charAt(i)) << 18)
					+ (decode(s.charAt(i + 1)) << 12)
					+ (decode(s.charAt(i + 2)) << 6)
					+ (decode(s.charAt(i + 3)));
			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=')
				break;
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=')
				break;
			os.write(tri & 255);
			i += 4;
		}
	}

	private static int decode(char c) {
		if (c >= 'A' && c <= 'Z')
			return ((int) c) - 65;
		else if (c >= 'a' && c <= 'z')
			return ((int) c) - 97 + 26;
		else if (c >= '0' && c <= '9')
			return ((int) c) - 48 + 26 + 26;
		else
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new RuntimeException("unexpected code: " + c);
			}
	}

	// public static String md5(String str) {
	// String s = str;
	// if (s == null || s.equals("")) {
	// return "";
	// } else {
	// String value = null;
	// MessageDigest md5 = null;
	// try {
	// md5 = MessageDigest.getInstance("MD5");
	// } catch (Exception ex) {
	// // Logger.getLogger(md5.class.getName()).log(Level.SEVERE, null,
	// // ex);
	// }
	// sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
	// try {
	// value = baseEncoder.encode(md5.digest(s.getBytes("utf-8")));
	// } catch (Exception ex) {
	// }
	// return value;
	// }
	// }

	/**
	 * 生成含有随机盐的密码 ,返回值为盐+盐密码
	 */
	// public static Map<String, String> generateSalt(String password) {
	// password = string2MD5(password);
	// String salt = RandomStringUtils.randomAlphanumeric(32);
	// password = md5Hex(password + salt);
	// char[] cs = new char[48];
	// for (int i = 0; i < 48; i += 3) {
	// cs[i] = password.charAt(i / 3 * 2);
	// char c = salt.charAt(i / 3);
	// cs[i + 1] = c;
	// cs[i + 2] = password.charAt(i / 3 * 2 + 1);
	// }
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("salt", salt);
	// map.put("saltpassword", new String(cs));
	// return map;
	// }

	/**
	 * 生成含有随机盐的密码 ,返回值为盐密码
	 */
	// public static String Salt(String password) {
	// password = string2MD5(password);
	// String salt = RandomStringUtils.randomAlphanumeric(32);
	// password = md5Hex(password + salt);
	// char[] cs = new char[48];
	// for (int i = 0; i < 48; i += 3) {
	// cs[i] = password.charAt(i / 3 * 2);
	// char c = salt.charAt(i / 3);
	// cs[i + 1] = c;
	// cs[i + 2] = password.charAt(i / 3 * 2 + 1);
	// }
	// return new String(cs);
	// }

	/**
	 * 校验密码是否正确
	 */
	// public static boolean verify(String password, String md5) {
	// char[] cs1 = new char[32];
	// char[] cs2 = new char[16];
	// for (int i = 0; i < 48; i += 3) {
	// cs1[i / 3 * 2] = md5.charAt(i);
	// cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
	// cs2[i / 3] = md5.charAt(i + 1);
	// }
	// String salt = new String(cs2);
	// return md5Hex(password + salt).equals(new String(cs1));
	// }

	/**
	 * 获取十六进制字符串形式的MD5摘要
	 */
	public static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(src.getBytes());
			return new String(new Hex().encode(bs));
		} catch (Exception e) {
			return null;
		}
	}

	// 测试主函数
	public static void main(String args[]) {
		String s = new String("ฐ็็็");
		System.out.println("原始：" + s + s.length());
		System.out.println("MD5后：" + string2MD5(s));
		System.out.println("加密的：" + convertMD5(s));
		System.out.println("解密的：" + convertMD5(convertMD5(s)));
		System.out.println("base64后：" + compressData(s));
		System.out.println("解密base64的：" + decompressData(compressData(s)));
		System.out.println("生成随机密码：" + getRandomString(8));
		// System.out.println("生成随机密码：" + md5(s));
		// System.out.println("SALT加密: " + Salt(s));
	}

}
