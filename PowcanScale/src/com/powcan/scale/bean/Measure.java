package com.powcan.scale.bean;

/**
 * 测量显示数据类
 * @author Administrator
 *
 */
public class Measure {
	/**
	 * 名称
	 */
	public String name; 
	/**
	 * 数据
	 */
	public String data; 
	/**
	 * 结果提示
	 */
	public String result; 
	
	public Measure() {
		
	}
	
	public Measure(String name, String data, String result) {
		this.name = name;
		this.data = data;
		this.result = result;
	}
}
