package com.powcan.scale.bean.http;

public class FBPRequest extends BaseRequest {
	public String account;//	int	用户选取的ID
	public String number;//	QQ、手机号或者email
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
