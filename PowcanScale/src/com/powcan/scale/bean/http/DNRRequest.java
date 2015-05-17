package com.powcan.scale.bean.http;

/**
 * 根据请求的时间范围下载用户的测量数据的请求参数定义类
 * @author Administrator
 *
 */
public class DNRRequest extends BaseRequest {
	/**
	 * 	int	用户选取的ID
	 */
	public String account;
	/**
	 * 	datetime 数据起始时间
	 */
	public String from_time;
	/**
	 * 	datetime 数据结束时间
	 */
	public String to_time;
	
	public DNRRequest() {
		cmd = "DNR";
	}
}
