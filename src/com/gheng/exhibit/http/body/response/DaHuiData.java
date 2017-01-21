package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class DaHuiData {
	@Expose
	public String folderName;
	@Expose
	public List<DaHuiDataFiles> filedata = new ArrayList<DaHuiDataFiles>();
	
}
