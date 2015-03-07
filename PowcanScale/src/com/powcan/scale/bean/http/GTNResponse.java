package com.powcan.scale.bean.http;

import java.util.List;


public class GTNResponse extends BaseResponse {
	public int NUM;//	int	返回的账户id个数
	public List<Integer> BKH;//	int	账户id（数组，但正常情况下应只返回一个）
}
