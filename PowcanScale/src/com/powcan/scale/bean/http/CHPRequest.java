package com.powcan.scale.bean.http;

/**
 * 密码修改请求参数定义类
 * @author Administrator
 *
 */
public class CHPRequest extends BaseRequest {
	/**
	 * int	用户选取的ID
	 */
	public String account;	
	/**
	 * oldpsw	string	原密码
	 */
	public String oldpsw;
	/**
	 * string	新密码
	 */
	public String newpsw;
	
	public CHPRequest() {
		cmd = "CHP";
	}
}
