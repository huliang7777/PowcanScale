package com.powcan.scale.bean.http;

/**
 * 基础响应参数定义类
 * @author Administrator
 *
 */
public class BaseResponse {
	/**
	 * 命令
	 */
	public String CMD;//	string	“GAT:zhifangcheng”
	/**
	 * 结果
	 */
	public int RES;//	int	
	
//	101：获取成功，至少获取到1个保康号 
//	102：获取失败
//	201：注册成功
//	202：注册失败，account已被注册
//	203：注册失败，解析注册数据失败
//	204：注册失败，其他错误
//	301：登陆成功
//	302：登陆失败，密码不符
//	303：登陆失败，该账户尚未注册
//	304：登陆失败，服务器内部错误
//	401：更新资料成功
//	402：更新资料部分成功，phone重复
//	403：更新资料部分成功，qq重复
//	404：更新资料部分成功，email重复
//	405：更新资料失败，找不到该用户
//	406：更新资料失败，其他错误
//	501：获取成功
//	502：获取成功，但找到多个账号
//	503：获取失败，没找到对应账号
//	504：获取失败，其他错误
//	601：验证成功，允许修改密码
//	602：验证失败，该账号未注册
//	603：验证失败，ID和手机号/QQ/email不匹配
//	604：验证失败，其他错误
//	701：更新密码成功
//	702：更新密码失败，该账号未注册
//	703：更新密码失败，其他错误
//	801：修改密码成功
//	802：修改密码失败，该账号未注册
//	803：修改密码失败，原密码不对
//	804：修改密码失败，其他错误
//	901：记录上传成功
//	902：记录上传失败
}
