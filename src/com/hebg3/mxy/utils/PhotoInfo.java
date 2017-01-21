package com.hebg3.mxy.utils;

import java.io.Serializable;
import com.google.gson.annotations.Expose;

public class PhotoInfo implements Serializable{
	@Expose 
	public String smallphotourl="";//缩略图路径
	@Expose 
	public String photourl="";//原图路径
	@Expose 
	public String uploadphotourl="";//上传压缩过后的文件的路径
}
