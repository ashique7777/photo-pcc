package com.walgreens.batch.central.bean;

import java.util.Map;

public class EmailReportBean {

	private long sysReportId;
	private String reportName;
	private String from;
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private String subject;
	private long sysConfigId;
	private Map<String, Object> data;
	private String processedFragment;

	public String getProcessedFragment() {
		return processedFragment;
	}

	public void setProcessedFragment(String processedFragment) {
		this.processedFragment = processedFragment;
	}

	public long getSysReportId() {
		return sysReportId;
	}

	public void setSysReportId(long sysReportId) {
		this.sysReportId = sysReportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public long getSysConfigId() {
		return sysConfigId;
	}

	public void setSysConfigId(long sysConfigId) {
		this.sysConfigId = sysConfigId;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
