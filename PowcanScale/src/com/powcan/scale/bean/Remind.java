package com.powcan.scale.bean;

/**
 * 测量提醒类
 * @author Administrator
 *
 */
public class Remind {
	/**
	 * 提醒时间
	 */
	private String time;
	/**
	 * 显示内容
	 */
	private String content; 
	/**
	 * 是否打开
	 */
	private boolean isOn;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

}
