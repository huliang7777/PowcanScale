package com.powcan.scale.bean;

public class GATRequest {
	public String cmd = "GAT";//	string	“GAT”	是
	public String app = "zhifangcheng";//	string	“zhifangcheng”	是
	public int amount;//	int	希望获取的保康号的数目	是
	
	public GATRequest(int amount) {
		this.amount = amount;
	}
}
