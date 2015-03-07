package com.powcan.scale.bean.http;

public class GATRequest extends BaseRequest {
	public String amount;//	int	希望获取的保康号的数目	是
	
	public GATRequest(int amount) {
		cmd = "GAT";
		this.amount = amount + "";
	}
}
