package com.walgreens.batch.central.bean;

import java.io.Serializable;

public class MovePromotionFeedFileBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String strLstFileNames;
	private String inboundFeedLocation;
	private String archivalFeedLocation;

	public String getStrLstFileNames() {
		return strLstFileNames;
	}
	public void setStrLstFileNames(String strLstFileNames) {
		this.strLstFileNames = strLstFileNames;
	}

	public String getInboundFeedLocation() {
		return inboundFeedLocation;
	}
	public void setInboundFeedLocation(String inboundFeedLocation) {
		this.inboundFeedLocation = inboundFeedLocation;
	}

	public String getArchivalFeedLocation() {
		return archivalFeedLocation;
	}
	public void setArchivalFeedLocation(String archivalFeedLocation) {
		this.archivalFeedLocation = archivalFeedLocation;
	}
}
