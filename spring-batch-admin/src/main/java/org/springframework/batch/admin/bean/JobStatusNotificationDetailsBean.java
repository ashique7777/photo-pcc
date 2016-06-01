package org.springframework.batch.admin.bean;

public class JobStatusNotificationDetailsBean {

	private String jobInstanceId;
	private String jobName;
	private String jobExecutionId;
	private String startTime;
	private String endTime;

	/**
	 * @return the jobInstanceId
	 */
	public String getJobInstanceId() {
		return jobInstanceId;
	}

	/**
	 * @param jobInstanceId
	 *            the jobInstanceId to set
	 */
	public void setJobInstanceId(String jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * @param jobName
	 *            the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * @return the jobExecutionId
	 */
	public String getJobExecutionId() {
		return jobExecutionId;
	}

	/**
	 * @param jobExecutionId
	 *            the jobExecutionId to set
	 */
	public void setJobExecutionId(String jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "JobStatusNotificationDetails [jobInstanceId=" + jobInstanceId + ", jobName=" + jobName
				+ ", jobExecutionId=" + jobExecutionId + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
}
