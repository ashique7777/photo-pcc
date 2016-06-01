package com.walgreens.omni.bean;

import java.sql.Date;


public class SilverCanisterDetailsBean {
	
	private int Store;
	private Date LastCanisterChangeDate;
	private int RollsCount;
	private int PrintsCount;
	private double SilverContentRolls;
	private double SilverContentPrints;
	private double TotalSilver;
	private int PaperSquereInch;
	private int totalRowCnt;
	
	public int getStore() {
		return Store;
	}
	public void setStore(int store) {
		Store = store;
	}
	public Date getLastCanisterChangeDate() {
		return LastCanisterChangeDate;
	}
	public void setLastCanisterChangeDate(Date lastCanisterChangeDate) {
		LastCanisterChangeDate = lastCanisterChangeDate;
	}
	public int getRollsCount() {
		return RollsCount;
	}
	public void setRollsCount(int rollsCount) {
		RollsCount = rollsCount;
	}
	public int getPrintsCount() {
		return PrintsCount;
	}
	public void setPrintsCount(int printsCount) {
		PrintsCount = printsCount;
	}
	public double getSilverContentRolls() {
		return SilverContentRolls;
	}
	public void setSilverContentRolls(double silverContentRolls) {
		SilverContentRolls = silverContentRolls;
	}
	public double getSilverContentPrints() {
		return SilverContentPrints;
	}
	public void setSilverContentPrints(double silverContentPrints) {
		SilverContentPrints = silverContentPrints;
	}
	public double getTotalSilver() {
		return TotalSilver;
	}
	public void setTotalSilver(double totalSilver) {
		TotalSilver = totalSilver;
	}
	public int getPaperSquereInch() {
		return PaperSquereInch;
	}
	public void setPaperSquereInch(int paperSquereInch) {
		PaperSquereInch = paperSquereInch;
	}
	public int getTotalRowCnt() {
		return totalRowCnt;
	}
	public void setTotalRowCnt(int totalRowCnt) {
		this.totalRowCnt = totalRowCnt;
	}
	
	
	}
