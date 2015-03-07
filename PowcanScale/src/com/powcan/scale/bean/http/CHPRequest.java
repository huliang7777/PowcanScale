package com.powcan.scale.bean.http;

public class CHPRequest extends BaseRequest {
	public String account;//	int	用户选取的ID
	public String oldpsw;//	oldpsw	string	原密码
	public String newpsw;//	string	新密码
	
	public CHPRequest() {
		cmd = "CHP";
	}
}
