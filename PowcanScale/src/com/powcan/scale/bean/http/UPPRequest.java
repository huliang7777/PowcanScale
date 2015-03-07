package com.powcan.scale.bean.http;

public class UPPRequest extends BaseRequest {
	public String account;//	int	用户选取的ID
	public String password;//	string	用户密码
	
	public UPPRequest() {
		cmd = "UPP";
	}
}
