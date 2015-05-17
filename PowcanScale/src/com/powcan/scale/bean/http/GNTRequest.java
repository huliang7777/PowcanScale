package com.powcan.scale.bean.http;

/**
 * 获取服务器中某用户最新的记录的时间戳的请求参数定义类
 * @author Administrator
 *
 */
public class GNTRequest extends BaseRequest {
	/**
	 * int	用户选取的ID
	 */
	public String account;
	
	public GNTRequest() {
		cmd = "GNT";
	}
}
