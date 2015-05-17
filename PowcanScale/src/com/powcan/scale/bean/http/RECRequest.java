package com.powcan.scale.bean.http;

/**
 * 上传一次用户的测量数据的请求参数定义类
 * @author Administrator
 *
 */
public class RECRequest extends BaseRequest {
	/**
	 * 	int	用户ID
	 */
	public String account;
	/**
	 * 	float	体重
	 */
	public String weight;
	/**
	 * 	float	脂肪含量
	 */
	public String fat;
	/**
	 * 	float	水分含量
	 */
	public String water;
	/**
	 * 	float	肌肉数据
	 */
	public String muscle;
	/**
	 * 	float	骨量数据
	 */
	public String bone;
	/**
	 * 	float	基础代谢数据
	 */
	public String bmr;
	/**
	 * 	float	皮下脂肪数据
	 */
	public String sfat;
	/**
	 * 	float	内脏脂肪等级数据
	 */
	public String infat;
	/**
	 * 	int	身体年龄
	 */
	public String bodyage;
	/**
	 * 	float	另外一个身体代谢率
	 */
	public String amr;
	/**
	 * 	datetime 记录产生时的本地时间（app时间，格式，字符串形式”yyyy-mm-ddhh:mm:ss”）
	 */
	public String measure_time;
	
	public RECRequest() {
		cmd = "REC";
	}
}
