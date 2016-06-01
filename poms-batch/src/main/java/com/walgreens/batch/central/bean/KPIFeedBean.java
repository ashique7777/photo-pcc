package com.walgreens.batch.central.bean;

public class KPIFeedBean {

	private String sotreNumber;
	private String statId;
	private String sampleValue;
	private String sampleSize;
	private String dateTime;
	private String referenceData;

	/**
	 * @return the sotreNumber
	 */
	public String getSotreNumber() {
		return sotreNumber;
	}

	/**
	 * @param sotreNumber
	 *            the sotreNumber to set
	 */
	public void setSotreNumber(String sotreNumber) {
		this.sotreNumber = sotreNumber;
	}

	/**
	 * @return the statId
	 */
	public String getStatId() {
		return statId;
	}

	/**
	 * @param statId
	 *            the statId to set
	 */
	public void setStatId(String statId) {
		this.statId = statId;
	}

	/**
	 * @return the sampleValue
	 */
	public String getSampleValue() {
		return sampleValue;
	}

	/**
	 * @param sampleValue
	 *            the sampleValue to set
	 */
	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	/**
	 * @return the sampleSize
	 */
	public String getSampleSize() {
		return sampleSize;
	}

	/**
	 * @param sampleSize
	 *            the sampleSize to set
	 */
	public void setSampleSize(String sampleSize) {
		this.sampleSize = sampleSize;
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            the dateTime to set
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the referenceData
	 */
	public String getReferenceData() {
		return referenceData;
	}

	/**
	 * @param referenceData
	 *            the referenceData to set
	 */
	public void setReferenceData(String referenceData) {
		this.referenceData = referenceData;
	}

}