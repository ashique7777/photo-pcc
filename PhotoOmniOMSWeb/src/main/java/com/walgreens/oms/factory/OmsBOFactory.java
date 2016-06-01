package com.walgreens.oms.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.oms.bo.LicenseReportFilterBO;
import com.walgreens.oms.bo.MBPromotionalMoneyBO;
import com.walgreens.oms.bo.OmniWebEnvConfigBo;
import com.walgreens.oms.bo.OrderBO;
import com.walgreens.oms.bo.OrdersUtilBO;
import com.walgreens.oms.bo.PosOrdersBO;
import com.walgreens.oms.bo.PrintSignReportFilterBO;

/**
 * @author CTS
 * Class used to implements factory patterns for BO
 *
 */

@Component
@Scope("singleton")
public class OmsBOFactory {

	
	@Autowired
	@Qualifier("PosOrdersBO")
	private PosOrdersBO posOrdersBo;
	
	@Autowired
	@Qualifier("LicenseReportFilterBO")
	private LicenseReportFilterBO licenseReportFilterBO;

	@Autowired
	@Qualifier("OrderBO")
	private OrderBO orderBO;
	
	@Autowired
	@Qualifier("PrintSignReportFilterBO")
	private PrintSignReportFilterBO pSReportBO;


	
	@Autowired
	@Qualifier("OrdersUtilBO")
	private OrdersUtilBO ordersUtilBO;
	
	
	@Autowired
	@Qualifier("OmniWebEnvConfigBo")
	private OmniWebEnvConfigBo omniWebEnvConfigBo;
	
	
	@Autowired
	@Qualifier("MBPromotionalMoneyBO")
	private MBPromotionalMoneyBO basketPromotionalMoneyBO;
	
	
	
	
	/**
	 * 
	 * @param orderType
	 * @param sourceType
	 * @return
	 */
	/*
	 * public OrdersBO getOrdersBO(String orderType, String sourceType) { if
	 * (orderType.equals("photo") && sourceType.equals("kiosk")) { return
	 * kioskOrdersBO; } else if (orderType.equals("photo") &&
	 * sourceType.equals("internet")) { return internetOrdersBO; } else
	 * if(orderType.equals("photo") && sourceType.equals("extroffline")){ return
	 * extrOfflineOrderBO; }else if(orderType.equals("photo") &&
	 * sourceType.equals("rollorder")){ return rollOrderBO; }else
	 * if(orderType.equals("photo") && sourceType.equals("pcplusorder")){ return
	 * pcPlusOrderBO; } return null; }
	 */


	/**
	 * @return the licenseReportFilterBO
	 */
	public LicenseReportFilterBO getLicenseReportFilterBO() {
		return licenseReportFilterBO;
	}

	/**
	 * @return the basketPromotionalMoneyBO
	 */
	public MBPromotionalMoneyBO getBasketPromotionalMoneyBO() {
		return basketPromotionalMoneyBO;
	}

	public void setBasketPromotionalMoneyBO(
			MBPromotionalMoneyBO basketPromotionalMoneyBO) {
		this.basketPromotionalMoneyBO = basketPromotionalMoneyBO;
	}

	/**
	 * @param licenseReportFilterBO the licenseReportFilterBO to set
	 */
	public void setLicenseReportFilterBO(LicenseReportFilterBO licenseReportFilterBO) {
		this.licenseReportFilterBO = licenseReportFilterBO;
	}

	/**
	 * 
	 * @param
	 * @return RealTime BO
	 */
	public OrderBO getOrdersBO() {
		return orderBO;
	}

	/**
	 * 
	 * @return
	 * his method finds the BO class
	 */
	public PosOrdersBO getPOSOrdersBo() {
		return posOrdersBo;
	}

	

	public OrdersUtilBO getOrdersUtilBO() {
		return ordersUtilBO;
	}

	public void setOrdersUtilBO(OrdersUtilBO ordersUtilBO) {
		this.ordersUtilBO = ordersUtilBO;
	}
	
	
	/**
	 * @return the pSReportBO
	 */
	public PrintSignReportFilterBO getpSReportBO() {
		return pSReportBO;
	}

	/**
	 * @param pSReportBO the pSReportBO to set
	 */
	public void setpSReportBO(PrintSignReportFilterBO pSReportBO) {
		this.pSReportBO = pSReportBO;
	}

	public OmniWebEnvConfigBo getOmniWebEnvConfigBo() {
		return omniWebEnvConfigBo;
	}

	public void setOmniWebEnvConfigBo(OmniWebEnvConfigBo omniWebEnvConfigBo) {
		this.omniWebEnvConfigBo = omniWebEnvConfigBo;
	}


	
}
