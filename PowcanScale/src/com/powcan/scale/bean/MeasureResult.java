package com.powcan.scale.bean;

/**
 * 测量结果
 * @author Administrator
 *
 */
public class MeasureResult 
{
	/**
	 * 唯一标识符
	 */
	private int id;
	/**
	 * 帐号
	 */
	private String account;
	/**
	 * 体重
	 */
	private float weight; 
	/**
	 * bmi
	 */
	private float bmi;
	/**
	 * 体脂率
	 */
	private float bodyFatRate;
	/**
	 * 肌肉比例
	 */
	private float muscleProportion;
	/**
	 * 身体年龄
	 */
	private float physicalAge; 
	/**
	 * 皮下脂肪
	 */
	private float subcutaneousFat;
	/**
	 * 内脏脂肪
	 */
	private float visceralFat;
	/**
	 *  基础代谢(亚)
	 */
	private float subBasalMetabolism; 
	/**
	 * 基础代谢(欧)
	 */
	private float europeBasalMetabolism;
	/**
	 * 骨量
	 */
	private float boneMass; 
	/**
	 * 水含量
	 */
	private float waterContent;
	/**
	 * 时间
	 */
	private String date;
	/**
	 * 是否已经上传
	 */
	private int upload;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getBmi() {
		return bmi;
	}

	public void setBmi(float bmi) {
		this.bmi = bmi;
	}

	public float getBodyFatRate() {
		return bodyFatRate;
	}

	public void setBodyFatRate(float bodyFatRate) {
		this.bodyFatRate = bodyFatRate;
	}

	public float getMuscleProportion() {
		return muscleProportion;
	}

	public void setMuscleProportion(float muscleProportion) {
		this.muscleProportion = muscleProportion;
	}

	public float getPhysicalAge() {
		return physicalAge;
	}

	public void setPhysicalAge(float physicalAge) {
		this.physicalAge = physicalAge;
	}

	public float getSubcutaneousFat() {
		return subcutaneousFat;
	}

	public void setSubcutaneousFat(float subcutaneousFat) {
		this.subcutaneousFat = subcutaneousFat;
	}

	public float getVisceralFat() {
		return visceralFat;
	}

	public void setVisceralFat(float visceralFat) {
		this.visceralFat = visceralFat;
	}

	public float getSubBasalMetabolism() {
		return subBasalMetabolism;
	}

	public void setSubBasalMetabolism(float subBasalMetabolism) {
		this.subBasalMetabolism = subBasalMetabolism;
	}

	public float getEuropeBasalMetabolism() {
		return europeBasalMetabolism;
	}

	public void setEuropeBasalMetabolism(float europeBasalMetabolism) {
		this.europeBasalMetabolism = europeBasalMetabolism;
	}

	public float getBoneMass() {
		return boneMass;
	}

	public void setBoneMass(float boneMass) {
		this.boneMass = boneMass;
	}

	public float getWaterContent() {
		return waterContent;
	}

	public void setWaterContent(float waterContent) {
		this.waterContent = waterContent;
	}

	public String getDate() 
	{
		if ( date.length() > 10 )
		{
			return date.substring( 0, 10 );
		}
		return date; 
	}
	
	public String getDateTime() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public int getUpload() {
		return upload;
	}

	public void setUpload(int upload) {
		this.upload = upload;
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
