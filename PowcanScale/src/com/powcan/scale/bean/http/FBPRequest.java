package com.powcan.scale.bean.http;

/**
 * 找回密码命令的请求参数定义类
 * @author Administrator
 *
 */
public class FBPRequest extends BaseRequest {
	/**
	 * 	int	用户选取的ID
	 */
	public String account;
	/**
	 * 	QQ、手机号或者email
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
	
	public FBPRequest() {
		cmd = "FBP";
	}
}
