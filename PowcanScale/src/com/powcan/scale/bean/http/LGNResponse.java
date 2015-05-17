package com.powcan.scale.bean.http;

/**
 * 用户登录的响应参数定义类
 * @author Administrator
 *
 */
public class LGNResponse extends BaseResponse {
	/**
	 * 	int	用户选取的ID
	 */
	public int ACT;
	/**
	 * 	string	昵称
	 */
	public String NKN;
	/**
	 * 	string	性别(M,F)
	 */
	public String GDR;
	/**
	 * 	string	出身年月
	 */
	public String AGE;
	/**
	 * 	int	身高
	 */
	public int HET;
	/**
	 * 	string	电话
	 */
	public String PHN;
	/**
	 * 	string	qq
	 */
	public String QQN;
	/**
	 * 	string	电子邮件
	 */
	public String EML;
}
