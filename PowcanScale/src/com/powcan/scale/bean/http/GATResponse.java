package com.powcan.scale.bean.http;

import java.util.List;

/**
 * 获取尚未注册的ID号的响应参数定义类
 * @author Administrator
 *
 */
public class GATResponse extends BaseResponse {
	/**
	 * 	int	获取到的保康号数目
	 */
	public int NUM;
	/**
	 * 	string	获取到的保康号（数组），如果一个都没获取到，该字段为空
	 */
	public List<Integer> BKH;
}
