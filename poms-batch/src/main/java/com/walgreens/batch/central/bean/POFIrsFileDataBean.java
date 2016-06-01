package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.List;

public class POFIrsFileDataBean implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> fileNameList;
	private String FileLocation;
	/**
	 * @return the fileNameList
	 */
	public List<String> getFileNameList() {
		return fileNameList;
	}
	/**
	 * @param fileNameList the fileNameList to set
	 */
	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}
	/**
	 * @return the fileLocation
	 */
	public String getFileLocation() {
		return FileLocation;
	}
	/**
	 * @param fileLocation the fileLocation to set
	 */
	public void setFileLocation(String fileLocation) {
		FileLocation = fileLocation;
	}


}
