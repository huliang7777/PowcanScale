package com.powcan.scale.bean.http;

public class BaseResponse {

	public String CMD;//	string	“GAT:zhifangcheng”
	public int RES;//	int	
	
	/*
	101：获取成功，至少获取到1个保康号 
	102：获取失败
	201：注册成功
	202：注册失败，account已被注册
	203：注册失败，解析注册数据失败
	204：注册失败，其他错误
	*/
}
