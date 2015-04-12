package com.powcan.scale.bean;

public class UserInfo {
	private Integer id;
	private String account;
	private String username;
	private String password;
	private String imei;
	private String gender;
	private String birthday;
	private String height;
	private String phone;
	private String qq;
	private String email;
	private String goalWeight;
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
		return goalWeight;
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
