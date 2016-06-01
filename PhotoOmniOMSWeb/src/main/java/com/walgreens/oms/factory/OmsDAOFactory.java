package com.walgreens.oms.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.oms.dao.LicenseReportFilterDAO;
import com.walgreens.oms.dao.MBPromotionalMoneyDAO;
import com.walgreens.oms.dao.OmniWebEnvConfigDAO;
import com.walgreens.oms.dao.OrderDAO;
import com.walgreens.oms.dao.OrdersDAO;
import com.walgreens.oms.dao.PosOrdersDAO;
import com.walgreens.oms.dao.PrintSignReportFilterDAO;
import com.walgreens.oms.dao.PromotionalMoneyDAO;

/**
 * @author CTS 
 * Class used to implement factory pattern for DAO
 * 
 */

@Component
@Scope("singleton")
public class OmsDAOFactory {

	/*@Autowired
	@Qualifier("RealTimeOrderBO")
	private OrdersDAO orderMgmtDAO;*/
	
	@Autowired
	@Qualifier("MBPromotionalMoneyDAO")
	private MBPromotionalMoneyDAO mbpromotionalMoneyDAOImpl;
	
	
	@Autowired
	@Qualifier("LicenseReportFilterDAO")
	private LicenseReportFilterDAO licenseReportFilterDAO;

	@Autowired
	@Qualifier("PosOrdersDAO")
	private PosOrdersDAO posOrdersDAO;
	
	@Autowired
	@Qualifier("PrintSignReportFilterDAO")
	private PrintSignReportFilterDAO pSReportDAO;

	/**
	 * @return the pSReportDAO
	 */
	public PrintSignReportFilterDAO getpSReportDAO() {
		return pSReportDAO;
	}
	/**
	 * @param pSReportDAO the pSReportDAO to set
	 */
	public void setpSReportDAO(PrintSignReportFilterDAO pSReportDAO) {
		this.pSReportDAO = pSReportDAO;
	}

	@Autowired
	@Qualifier("OrderDAO")
	private OrderDAO orderDAO;	

	@Autowired
	@Qualifier("OrdersDAO")
	private OrdersDAO ordersDAO;
	
	@Autowired
	@Qualifier("OmniWebEnvConfigDAO")
	private OmniWebEnvConfigDAO omniWebEnvConfigDAO;
	
	@Autowired
	@Qualifier("PromotionalMoneyDAO")
	private PromotionalMoneyDAO promotionalMoneyDAO;
	
		/**
	 * @return the licenseReportFilterDAO
	 */
	public LicenseReportFilterDAO getLicenseReportFilterDAO() {
		return licenseReportFilterDAO;
	}
	/**
	 * @param licenseReportFilterDAO the licenseReportFilterDAO to set
	 */
	public void setLicenseReportFilterDAO(
			LicenseReportFilterDAO licenseReportFilterDAO) {
		this.licenseReportFilterDAO = licenseReportFilterDAO;
	}
	public void setOmniWebEnvConfigDAO(OmniWebEnvConfigDAO omniWebEnvConfigDAO) {
		this.omniWebEnvConfigDAO = omniWebEnvConfigDAO;
	}
	public OmniWebEnvConfigDAO getOmniWebEnvConfigDAO() {
		return omniWebEnvConfigDAO;
	}

	/**
	 * @return
	 * his method finds the DAO class
	 */
	public PosOrdersDAO getPosOrdersDAO() {
		return posOrdersDAO;
	}

	/**
	 * @return
	 * his method finds the DAO class
	 *
	public OrdersDAO getOrderMgmtDAO() {
		return orderMgmtDAO;
	}*/

	/**
	 * This method finds the DAO class
	 * 
	 * @param
	 * @return
	 */
	public OrderDAO getRealTimeOrdersDao() {
		return orderDAO;
	}

	public OrdersDAO getOrdersDAO() {
		return ordersDAO;
	}

	public void setOrdersDAO(OrdersDAO ordersDAO) {
		this.ordersDAO = ordersDAO;
	}

	/**
	 * @return the promotionalMoneyDAO
	 */
	public PromotionalMoneyDAO getPromotionalMoneyDAO() {
		return promotionalMoneyDAO;
	}
	/**
	 * @return the mbpromotionalMoneyDAOImpl
	 */
	public MBPromotionalMoneyDAO getMbpromotionalMoneyDAOImpl() {
		return mbpromotionalMoneyDAOImpl;
	}
	/**
	 * @param mbpromotionalMoneyDAOImpl the mbpromotionalMoneyDAOImpl to set
	 */
	public void setMbpromotionalMoneyDAOImpl(
			MBPromotionalMoneyDAO mbpromotionalMoneyDAOImpl) {
		this.mbpromotionalMoneyDAOImpl = mbpromotionalMoneyDAOImpl;
	}
	
	
	
	
	
}
