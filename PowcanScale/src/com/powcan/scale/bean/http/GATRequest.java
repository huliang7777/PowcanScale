package com.powcan.scale.bean.http;

/**
 * 获取尚未注册的ID号的请求参数定义类
 * @author Administrator
 *
 */
public class GATRequest extends BaseRequest {
	/**
	 * 	int	希望获取的保康号的数目	是
	 */
	public String amount;
	
	public GATRequest(int amount) {
		cmd = "GAT";
		this.amount = amount + "";
	}
}
