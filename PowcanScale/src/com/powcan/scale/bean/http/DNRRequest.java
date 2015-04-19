package com.powcan.scale.bean.http;

public class DNRRequest extends BaseRequest {
	public String account;//	int	用户选取的ID
	public String from_time;//	datetime 数据起始时间
	public String to_time;//	datetime 数据结束时间
	
	public DNRRequest() {
		cmd = "DNR";
	}
}
