package com.walgreens.oms.bean;

import java.util.List;

public class CostCalculationTransferBean {
	
	private List<OmOrderLineBean> omOrderLineBeanList;
	private OmOrderAttributeBean omOrderAttributeBean;
	private String ordPlacedDttm;

	
	public List<OmOrderLineBean> getOmOrderLineBeanList() {
		return omOrderLineBeanList;
	}

	public void setOmOrderLineBeanList(List<OmOrderLineBean> omOrderLineBeanList) {
		this.omOrderLineBeanList = omOrderLineBeanList;
	}

	public OmOrderAttributeBean getOmOrderAttributeBean() {
		return omOrderAttributeBean;
	}

	public void setOmOrderAttributeBean(OmOrderAttributeBean omOrderAttributeBean) {
		this.omOrderAttributeBean = omOrderAttributeBean;
	}

	public String getOrdPlacedDttm() {
		return ordPlacedDttm;
	}

	public void setOrdPlacedDttm(String ordPlacedDttm) {
		this.ordPlacedDttm = ordPlacedDttm;
	}

}
