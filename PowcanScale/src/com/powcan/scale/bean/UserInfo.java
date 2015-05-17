package com.powcan.scale.bean;

import android.text.TextUtils;

/**
 * 用户信息类
 * @author Administrator
 *
 */
public class UserInfo {
	/**
	 * 唯一标识
	 */
	private Integer id;
	/**
	 *  帐号
	 */
	private String account;
	/**
	 * 用户名
	 */
	private String username; 
	/**
	 * 密码
	 */
	private String password; 
	/**
	 *  设备标识
	 */
	private String imei; 
	/**
	 *  性别
	 */
	private String gender; 
	/**
	 *  生日
	 */
	private String birthday; 
	/**
	 *  身高
	 */
	private String height; 
	/**
	 *  电话
	 */
	private String phone; 
	/**
	 * QQ号码
	 */
	private String qq; 
	/**
	 *  邮箱
	 */
	private String email; 
	/**
	 *  目标体重
	 */
	private String goalWeight; 
	/**
	 *  提醒开关
	 */
	private String remind; 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUsername() {
		return ("NULL".equals(username)) ? "" : username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return "0000-00-00".equals(birthday) ? "" : birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getGoalWeight() {
		return TextUtils.isEmpty( goalWeight ) ? "0" : goalWeight;
	}

	public void setGoalWeight(String goalWeight) {
		this.goalWeight = goalWeight;
	}
	
	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", account=" + account + ", username="
				+ username + ", imei=" + imei + ", gender=" + gender
				+ ", birthday=" + birthday + ", height=" + height + ", phone="
				+ phone + ", qq=" + qq + ", email=" + email + ", goalWeight="
				+ goalWeight + ", remind=" + remind
				+ "]";
	}
}
