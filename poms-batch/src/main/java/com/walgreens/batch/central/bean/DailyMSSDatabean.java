package com.walgreens.batch.central.bean;


public class DailyMSSDatabean{

	
	private String mssFeed;
	
	private String jobReturnTyp;
	
	private String jobSubmitDate;
	
	private String dataEndDttm;
	
	private String dataStartDttm;
	
	private boolean isTrue;
	
	

	/**
	 * @return the isTrue
	 */
	public boolean isTrue() {
		return isTrue;
	}

	/**
	 * @param isTrue the isTrue to set
	 */
	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

	/**
	 * @return the jobReturnTyp
	 */
	public String getJobReturnTyp() {
		return jobReturnTyp;
	}

	/**
	 * @param jobReturnTyp the jobReturnTyp to set
	 */
	public void setJobReturnTyp(String jobReturnTyp) {
		this.jobReturnTyp = jobReturnTyp;
	}

	/**
	 * @return the jobSubmitDate
	 */
	public String getJobSubmitDate() {
		return jobSubmitDate;
	}

	/**
	 * @param jobSubmitDate the jobSubmitDate to set
	 */
	public void setJobSubmitDate(String jobSubmitDate) {
		this.jobSubmitDate = jobSubmitDate;
	}

	/**
	 * @return the dataEndDttm
	 */
	public String getDataEndDttm() {
		return dataEndDttm;
	}

	/**
	 * @param dataEndDttm the dataEndDttm to set
	 */
	public void setDataEndDttm(String dataEndDttm) {
		this.dataEndDttm = dataEndDttm;
	}

	/**
	 * @return the dataStartDttm
	 */
	public String getDataStartDttm() {
		return dataStartDttm;
	}

	/**
	 * @param dataStartDttm the dataStartDttm to set
	 */
	public void setDataStartDttm(String dataStartDttm) {
		this.dataStartDttm = dataStartDttm;
	}

	/**
	 * @return the mssFeed
	 */
	public String getMssFeed() {
		return mssFeed;
	}

	/**
	 * @param mssFeed the mssFeed to set
	 */
	public void setMssFeed(String mssFeed) {
		this.mssFeed = mssFeed;
	}
}
