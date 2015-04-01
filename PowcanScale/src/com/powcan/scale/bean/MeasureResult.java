package com.powcan.scale.bean;

/**
 * 测量结果
 * @author Administrator
 *
 */
public class MeasureResult 
{
	private Integer id;
	private String account;
	private String weight; 
	private String bmi; 
	private String bodyFatRate; // 体脂率
	private String muscleProportion; // 肌肉比例
	private String physicalAge; // 身体年龄
	private String subcutaneousFat; // 皮下脂肪
	private String visceralFat; // 内脏脂肪
	private String subBasalMetabolism; // 基础代谢(亚)
	private String europeBasalMetabolism; // 基础代谢(欧)
	private String boneMass; // 骨量
	private String waterContent; // 水含量
	private String date;

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

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getBmi() {
		return bmi;
	}

	public void setBmi(String bmi) {
		this.bmi = bmi;
	}

	public String getBodyFatRate() {
		return bodyFatRate;
	}

	public void setBodyFatRate(String bodyFatRate) {
		this.bodyFatRate = bodyFatRate;
	}

	public String getMuscleProportion() {
		return muscleProportion;
	}

	public void setMuscleProportion(String muscleProportion) {
		this.muscleProportion = muscleProportion;
	}

	public String getPhysicalAge() {
		return physicalAge;
	}

	public void setPhysicalAge(String physicalAge) {
		this.physicalAge = physicalAge;
	}

	public String getSubcutaneousFat() {
		return subcutaneousFat;
	}

	public void setSubcutaneousFat(String subcutaneousFat) {
		this.subcutaneousFat = subcutaneousFat;
	}

	public String getVisceralFat() {
		return visceralFat;
	}

	public void setVisceralFat(String visceralFat) {
		this.visceralFat = visceralFat;
	}

	public String getSubBasalMetabolism() {
		return subBasalMetabolism;
	}

	public void setSubBasalMetabolism(String subBasalMetabolism) {
		this.subBasalMetabolism = subBasalMetabolism;
	}

	public String getEuropeBasalMetabolism() {
		return europeBasalMetabolism;
	}

	public void setEuropeBasalMetabolism(String europeBasalMetabolism) {
		this.europeBasalMetabolism = europeBasalMetabolism;
	}

	public String getBoneMass() {
		return boneMass;
	}

	public void setBoneMass(String boneMass) {
		this.boneMass = boneMass;
	}

	public String getWaterContent() {
		return waterContent;
	}

	public void setWaterContent(String waterContent) {
		this.waterContent = waterContent;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "MeasureResult [id=" + id + ", account=" + account + ", weight="
				+ weight + ", bmi=" + bmi + ", bodyFatRate=" + bodyFatRate
				+ ", muscleProportion=" + muscleProportion + ", physicalAge="
				+ physicalAge + ", subcutaneousFat=" + subcutaneousFat
				+ ", visceralFat=" + visceralFat + ", subBasalMetabolism="
				+ subBasalMetabolism + ", europeBasalMetabolism="
				+ europeBasalMetabolism + ", boneMass=" + boneMass
				+ ", waterContent=" + waterContent + ", date=" + date + "]";
	}
}
