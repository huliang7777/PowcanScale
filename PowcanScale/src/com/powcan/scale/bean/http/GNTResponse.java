package com.powcan.scale.bean.http;

/**
 * 获取服务器中某用户最新的记录的时间戳的响应参数定义类
 * @author Administrator
 *
 */
public class GNTResponse extends BaseResponse {
	/**
	 * 	int	获取到的保康号数目
	 */
	public int NUM;
	/**
	 *  string	最新测量记录时间（对应REC命令中的measure_time）
	 */
	public String MMT;
	/**
	 *  string	最新服务器时间（对应记录上传时服务器的时间）
	 */
	public String SST;
}
