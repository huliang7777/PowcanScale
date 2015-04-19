package com.powcan.scale.bean.http;


public class GNTResponse extends BaseResponse {
	public int NUM;//	int	获取到的保康号数目
	public String MMT;// string	最新测量记录时间（对应REC命令中的measure_time）
	public String SST;// string	最新服务器时间（对应记录上传时服务器的时间）
}
