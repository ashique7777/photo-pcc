package com.walgreens.batch.central.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.batch.central.bean.LicenceContentPosBean;
import com.walgreens.batch.central.bean.OrderLineBean;
import com.walgreens.batch.central.bean.OrderLineTemplateBean;
import com.walgreens.batch.central.bean.POSOrder;
import com.walgreens.batch.central.rowmapper.LicenceContentPosBeanRowMapper;
import com.walgreens.batch.central.rowmapper.OrderLineBeanRowMapper;
import com.walgreens.batch.central.rowmapper.OrderLineTemplateBeanRowMapper;
import com.walgreens.batch.central.rowmapper.PosOrderRowMapper;
import com.walgreens.batch.central.utility.PosQuery;

/**
 * 
 * @author CTS
 * @version 1.1 January 12, 2015
 * 
 * 
 */

public class PosDAOImpl {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	private PosQuery posQuery;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PosDAOImpl.class);

	@Transactional
	public POSOrder getOrderDetailsOmOrder(int orderId) {

		LOGGER.debug("Entering PosDAOImpl getOrderDetailsOmOrder()");
		POSOrder posOrder = new POSOrder();
		posQuery = new PosQuery();
		String omOrderDetailsQuery = posQuery.getOmOrderDetailsQuery();

		try {
			posOrder = jdbcTemplate.queryForObject(omOrderDetailsQuery,
					new Object[] { orderId },
					new PosOrderRowMapper());

		} catch (Exception ex) {

		} finally {

		}
		LOGGER.debug("Exiting PosDAOImpl getOrderDetailsOmOrder()");
		return posOrder;
	}

	@Transactional
	public void updateSoldAmtOmOrder(double soldAmtOmOrder, int orderId) {

		LOGGER.debug("Entering PosDAOImpl updateSoldAmtOmOrder()");
		posQuery = new PosQuery();
		String updateSoldAmtQuery = posQuery.getupdateSoldAmtQuery();

		try {
			jdbcTemplate.update(updateSoldAmtQuery, new Object[] {
					soldAmtOmOrder, orderId });

		} catch (Exception ex) {

		} finally {

		}

		LOGGER.debug("Exiting PosDAOImpl updateSoldAmtOmOrder()");
	}

	@Transactional
	public List<OrderLineBean> getOrderLineDetails(int orderId) {

		LOGGER.debug("Entering PosDAOImpl getOrderLineDetails()");
		List<OrderLineBean> orderLineBeansList = new ArrayList<OrderLineBean>();
		posQuery = new PosQuery();
		String orderLineDetailsQuery = posQuery.getOrderLineDetailsQuery();

		try {
			orderLineBeansList = jdbcTemplate.query(orderLineDetailsQuery,
					new OrderLineBeanRowMapper(),
					new Object[] { orderId });

		} catch (Exception ex) {

		} finally {
			
		}
		LOGGER.debug("Exiting PosDAOImpl getOrderLineDetails()");
		return orderLineBeansList;
	}


	@Transactional
	public List<LicenceContentPosBean> getLicenceContentDetails(int sysOrderLineId) {
		
		LOGGER.debug("Entering PosDAOImpl getLicenceContentDetails()");
		List<LicenceContentPosBean> licenceContentBeanList = new ArrayList<LicenceContentPosBean>();
		posQuery = new PosQuery();
		String licenceContentDetailsQuery = posQuery
				.getlicenceContentDetailsQuery();

		try {
			licenceContentBeanList = jdbcTemplate.query(
					licenceContentDetailsQuery, 
					new LicenceContentPosBeanRowMapper(),
					new Object[] { sysOrderLineId });

		} catch (Exception ex) {

		} finally {
			
		}
		LOGGER.debug("Entering PosDAOImpl getLicenceContentDetails()");
		return licenceContentBeanList;
	}

	@Transactional
	public List<OrderLineTemplateBean> getOrderLineTemplateDetails(
			int orderLineID) {
		
		LOGGER.debug("Entering PosDAOImpl getOrderLineTemplateDetails()");
		List<OrderLineTemplateBean> orderLineTemplateBeansList = new ArrayList<OrderLineTemplateBean>();
		posQuery = new PosQuery();
		String orderLineTemplateBeansListQuery = posQuery
				.getorderLineTemplateBeansListQuery();

		try {
			orderLineTemplateBeansList = jdbcTemplate.query(
					orderLineTemplateBeansListQuery, 
					new OrderLineTemplateBeanRowMapper(),
					new Object[] { orderLineID });

		} catch (Exception ex) {

		} finally {
			
		}
		LOGGER.debug("Exiting PosDAOImpl getOrderLineTemplateDetails()");
		return orderLineTemplateBeansList;
	}

	@Transactional
	public void updatelcAmtPaidToLicemceContent(double lcAmtPaid,
			int sysOlLcId) {

		LOGGER.debug("Entering PosDAOImpl updatelcAmtPaidToLicemceContent()");
		posQuery = new PosQuery();
		String updatecAmtPaidToLicemceContentQuery = posQuery
				.getcAmtPaidToLicemceContentQuery();

		try {
			jdbcTemplate.update(updatecAmtPaidToLicemceContentQuery,
					new Object[] { lcAmtPaid, sysOlLcId });

		} catch (Exception ex) {

		} finally {

		}
		LOGGER.debug("Exiting PosDAOImpl updatelcAmtPaidToLicemceContent()");

	}

	@Transactional
	public void updatelcTemplateSoldAmt(double templateSoldAmt1, int sysOlTemplateId) {

		LOGGER.debug("Entering PosDAOImpl updatelcTemplateSoldAmt()");
		posQuery = new PosQuery();
		String updatecTemplateSoldAmtQuery = posQuery
				.getupdatecTemplateSoldAmtQuery();

		try {
			jdbcTemplate.update(updatecTemplateSoldAmtQuery, new Object[] {
					templateSoldAmt1, sysOlTemplateId });

		} catch (Exception ex) {

		} finally {

		}
		LOGGER.debug("Exiting PosDAOImpl updatelcTemplateSoldAmt()");
		
	}

	@Transactional
	public void updateLineSoldAmt(double lineSoldAmt1, int orderLineID) {

		LOGGER.debug("Entering PosDAOImpl updateLineSoldAmt()");
		posQuery = new PosQuery();
		String updateLineSoldAmtQuery = posQuery.getupdateLineSoldAmtQuery();

		try {
			jdbcTemplate.update(updateLineSoldAmtQuery, new Object[] {
					lineSoldAmt1, orderLineID });

		} catch (Exception ex) {

		} finally {

		}

		LOGGER.debug("Exiting PosDAOImpl updateLineSoldAmt()");
	}

}
