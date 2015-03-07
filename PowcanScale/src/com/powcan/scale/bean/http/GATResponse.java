package com.powcan.scale.bean.http;

import java.util.List;

public class GATResponse extends BaseResponse {
	public int NUM;//	int	获取到的保康号数目
	public List<Integer> BKH;//	string	获取到的保康号（数组），如果一个都没获取到，该字段为空
}
