package com.powcan.scale.bean.http;

/**
 * 用户登录的请求参数定义类
 * @author Administrator
 *
 */
public class LGNRequest extends BaseRequest {
	/**
	 * 帐号
	 */
	public String number;
	/**
	 * 密码
	 */
	public String pswd;
	
	public LGNRequest() {
		cmd = "LGN";
	}
}
