package com.powcan.scale.bean.http;

/**
 * 修改用户资料的请求参数定义类
 * @author Administrator
 *
 */
public class UTURequest extends BaseRequest {
	/**
	 * 	int	用户选取的ID
	 */
	public String account;
	/**
	 * 	string	用户密码
	 */
	public String pswd;
	/**
	 * 	string	用户昵称
	 */
	public String nickname;
	/**
	 * 	string	用户注册手机的IMEI号
	 */
	public String imei;
	/**
	 * 	string	性别(M,F)
	 */
	public String gender;
	/**
	 * 	string	出身年月
	 */
	public String birthday;
	/**
	 * 	int	身高
	 */
	public String height;
	/**
	 * 	string	电话
	 */
	public String phone;
	/**
	 * 	string	qq
	 */
	public String qq;
	/**
	 * 	string	电子邮件
	 */
	public String email;
	
	public UTURequest() {
		cmd = "UTU";
	}
}
