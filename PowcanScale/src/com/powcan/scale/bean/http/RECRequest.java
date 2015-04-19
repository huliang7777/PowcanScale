package com.powcan.scale.bean.http;

public class RECRequest extends BaseRequest {
	public String account;//	int	用户ID
	public String weight;//	float	体重
	public String fat;//	float	脂肪含量
	public String water;//	float	水分含量
	public String muscle;//	float	肌肉数据
	public String bone;//	float	骨量数据
	public String bmr;//	float	基础代谢数据
	public String sfat;//	float	皮下脂肪数据
	public String infat;//	float	内脏脂肪等级数据
	public String bodyage;//	int	身体年龄
	public String amr;//	float	另外一个身体代谢率
	public String measure_time;//	datetime 记录产生时的本地时间（app时间，格式，字符串形式”yyyy-mm-ddhh:mm:ss”）
	
	public RECRequest() {
		cmd = "REC";
	}
}
