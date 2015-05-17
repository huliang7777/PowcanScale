package com.powcan.scale.bean.http;

/**
 * 设置密码命令的请求参数定义类
 * @author Administrator
 *
 */
public class UPPRequest extends BaseRequest {
	/**
	 * 	int	用户选取的ID
	 */
	public String account;
	/**
	 * 	string	用户密码
	 */
	public String password;
	
	public UPPRequest() {
		cmd = "UPP";
	}
}
