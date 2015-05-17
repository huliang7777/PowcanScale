package com.powcan.scale.bean.http;

import java.util.List;

/**
 * 根据输入IMEI号、手机号、qq或者email返回用户ID的响应参数定义类
 * @author Administrator
 *
 */
public class GTNResponse extends BaseResponse {
	/**
	 * 	int	返回的账户id个数
	 */
	public int NUM;
	/**
	 * 	int	账户id（数组，但正常情况下应只返回一个）
	 */
	public List<Integer> BKH;
}
