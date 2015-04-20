package com.powcan.scale.bean.http;

import java.util.List;


public class DNRResponse extends BaseResponse {
	public int NUM;//	int	返回的账户id个数
	public List<String> WET;// String 体重（数组）
	public List<String> FAT;// String 脂肪含量（数组）
	public List<String> WAT;// String 水分含量（数组）
	public List<String> MUS;// String 肌肉数据（数组）
	public List<String> BON;// String 骨量数据（数组）
	public List<String> BMR;// String 基础代谢数据（数组）
	public List<String> SFT;// String 皮下脂肪数据（数组）
	public List<String> IFT;// String 内脏脂肪等级数据（数组）
	public List<String> BDA;// String 身体年龄（数组）
	public List<String> AMR;// String 另外一个身体代谢率（数组）
	public List<String> TIM;// String 测量时间（数组）
}
