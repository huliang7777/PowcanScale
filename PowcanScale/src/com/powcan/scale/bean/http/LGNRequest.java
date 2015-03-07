package com.powcan.scale.bean.http;

public class LGNRequest extends BaseRequest {
	public String number;//	int	希望获取的保康号的数目	是
	public String pswd;
	
	public LGNRequest() {
		cmd = "LGN";
	}
}
