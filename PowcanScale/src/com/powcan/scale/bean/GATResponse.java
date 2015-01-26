package com.powcan.scale.bean;

public class GATResponse {
	public String CMD;//	string	“GAT:zhifangcheng”
	public int RES;//	int	101：获取成功，至少获取到1个保康号 102：获取失败
	public int NUM;//	int	获取到的保康号数目
	public String BKH;//	string	获取到的保康号（数组），如果一个都没获取到，该字段为空
}
