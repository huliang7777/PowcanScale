package com.powcan.scale.bean.http;

/**
 * 根据输入IMEI号、手机号、qq或者email返回用户ID的请求参数定义类
 * @author Administrator
 *
 */
public class GTNRequest extends BaseRequest {
	/**
	 * 帐号
	 */
	public String number;
	/**
	 * 类型
	 */
	public String type;
	/*	enum	
	“phone”：手机号
	“imei”：imei号
	“qq”：qq号
	“email”：电子邮件
	*/
	
	public GTNRequest() {
		cmd = "GTN";
	}
}
