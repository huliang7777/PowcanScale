package com.powcan.scale.bean.http;

public class REGRequest extends BaseRequest {
	public String account;//	int	用户选取的ID
	public String pswd;//	string	用户密码
	public String imei;//	string	用户注册手机的IMEI号
	public String gender;//	string	性别(M,F)
	public String birthday;//	string	出身年月
	public String height;//	int	身高
	public String phone;//	string	电话
	public String qq;//	string	qq
	public String email;//	string	电子邮件
	
	public REGRequest() {
		cmd = "REG";
	}
}
