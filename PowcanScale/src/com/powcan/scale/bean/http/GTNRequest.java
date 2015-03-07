package com.powcan.scale.bean.http;

public class GTNRequest extends BaseRequest {
	public String number;//	number	string	IMEI、phone、qq或email
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
