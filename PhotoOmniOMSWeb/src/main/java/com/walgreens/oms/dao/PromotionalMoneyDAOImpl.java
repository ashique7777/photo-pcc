/**
 * 
 */
package com.walgreens.oms.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.oms.bean.OmPromotionalMoneyBean;
import com.walgreens.oms.bean.OrderPMDataBean;
import com.walgreens.oms.bean.PMOrderDetailBean;
import com.walgreens.oms.bean.PMOrderLineBean;
import com.walgreens.oms.bean.PMRule;
import com.walgreens.oms.bean.PMRuleDetail;
import com.walgreens.oms.rowmapper.OmPromotionalMoneyBeanRowMapper;
import com.walgreens.oms.rowmapper.OrderPmRwmapper;
import com.walgreens.oms.rowmapper.PMOrderLineRWMapper;
import com.walgreens.oms.utility.PromotionalMoneyQry;

/**
 * @author CTS
 * @param <PMOrderLineBean>
 * 
 */
@Repository("PromotionalMoneyDAO")
@Transactional(propagation = Propagation.REQUIRED)
@SuppressWarnings("deprecation")
public class PromotionalMoneyDAOImpl implements PromotionalMoneyDAO {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PromotionalMoneyDAOImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walgreens.common.dao.PromotionalMoneyDAO#getOrderPMDetails(long,
	 * java.lang.String)
	 */
	@Override
	public PMOrderDetailBean getOrderPMDetails(long sys_order_id,
			String order_placed_dttm) {

		String orderQry = PromotionalMoneyQry.getPMOrderDetailsQuery();
		PMOrderDetailBean orderDetails = null;
		try {
			orderDetails = jdbcTemplate.queryForObject(orderQry, new Object[] {
					sys_order_id, order_placed_dttm },
					new BeanPropertyRowMapper<PMOrderDetailBean>(
							PMOrderDetailBean.class));
		} catch (DataAccessException e) {
			LOGGER.error("DB Exception at getOrderPMDetails method", e);
		}

		return orderDetails;
	}

	@Override
	public void getPMOrderLine(PMOrderDetailBean orderDetails) {
		String orderLineQry = PromotionalMoneyQry.getPMOrderLineQuery();
		List<PMOrderLineBean> orderlineList = new ArrayList<PMOrderLineBean>();
		try {
			orderlineList = jdbcTemplate.query(
					orderLineQry,
					new Object[] { orderDetails.getOrderId(),
							orderDetails.getOrderPlacedDTTM() },
					new PMOrderLineRWMapper());
		} catch (DataAccessException e) {
			LOGGER.error("DB Exception at getPMOrderLine", e);
		}
		orderDetails.setItemList(orderlineList);
	}

	@Override
	public PMRule getPMRule(long productId, String orderPlacedDTTM,
			long locationId) {
		List<OmPromotionalMoneyBean> productPMRule = new ArrayList<OmPromotionalMoneyBean>();
		PMRule pmRule = null;
		String ruleQry = PromotionalMoneyQry.getProdPMChainRuleQuery();
		// get Chain rule
		try {
			productPMRule.addAll(jdbcTemplate.query(ruleQry, new Object[] {
					orderPlacedDTTM, orderPlacedDTTM, productId },
					new OmPromotionalMoneyBeanRowMapper(true)));
		} catch (EmptyResultDataAccessException e) {
			productPMRule.addAll(this.getPMStoreRule(productId,
					orderPlacedDTTM, locationId));
		}
		if (productPMRule.isEmpty()) {
			productPMRule.addAll(this.getPMStoreRule(productId,
					orderPlacedDTTM, locationId));
		}
		if (!productPMRule.isEmpty()) {
			pmRule = this.getStructuredPmRule(productPMRule);
		}
		return pmRule;
	}

	@Override
	public List<OmPromotionalMoneyBean> getPMStoreRule(long productId,
			String orderPlacedDTTM, long locationId) {
		List<OmPromotionalMoneyBean> productPMRule = new ArrayList<OmPromotionalMoneyBean>();
		String ruleQry = PromotionalMoneyQry.getProdPMStoreRuleQuery();
		// get Chain rule
		try {
			productPMRule.addAll(jdbcTemplate.query(ruleQry, new Object[] {
					locationId, orderPlacedDTTM, orderPlacedDTTM, productId },
					new OmPromotionalMoneyBeanRowMapper(true)));

		} catch (EmptyResultDataAccessException e) {			
			// return null;
		}
		return productPMRule;
	}

	@Override
	public List<OrderPMDataBean> fetchOrderPM(long sysOrderID,
			String orderPlacedDTTM) {

		String orderPMQry = PromotionalMoneyQry.getOrderPMQuery();
		List<OrderPMDataBean> orderPMList = new ArrayList<OrderPMDataBean>();
		try {
			orderPMList = jdbcTemplate.query(orderPMQry, new Object[] {
					sysOrderID, orderPlacedDTTM }, new OrderPmRwmapper());
		} catch (DataAccessException e) {
			LOGGER.error("DB Exception at fetchOrderPM", e);
		}
		return orderPMList;

	}

	public PMRule getStructuredPmRule(List<OmPromotionalMoneyBean> pmRuleList) {
		PMRule pmRuleBean = new PMRule();

		// do the structure to make in a single bean
		List<PMRuleDetail> ruleDetailsList = new ArrayList<PMRuleDetail>();
		for (OmPromotionalMoneyBean omPMBean : pmRuleList) {
			PMRuleDetail ruleDetails = new PMRuleDetail();
			ruleDetails.setMaxTier((double) omPMBean.getMaximumTier());
			ruleDetails.setMinTier((double) omPMBean.getMinimumTier());
			ruleDetails.setPmAmount(omPMBean.getPmAmount());
			ruleDetailsList.add(ruleDetails);
		}
		pmRuleBean.setRuleDetails(ruleDetailsList);
		pmRuleBean.setPmId(pmRuleList.get(0).getSysPmId());
		pmRuleBean.setDiscountApplicableCd((pmRuleList.get(0)
				.getDiscountApplicableInd() == 1) ? true : false);
		pmRuleBean.setPmPaymentType(pmRuleList.get(0).getPmPaymentType());
		pmRuleBean.setPmRuleType(pmRuleList.get(0).getPmRuleType());
		pmRuleBean.setTierType(pmRuleList.get(0).getTierType());
		return pmRuleBean;
	}

	@Override
	public void updatePmStatus(long sysOderId, String oderPlacedDTTM,
			String pmStatus) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter into PromotionalMoneyDAOImpl.updatePmStatus() method");
		}
		String orderPMQry = PromotionalMoneyQry.getPMStatusQuery();
		 jdbcTemplate.update(orderPMQry, new Object[] { pmStatus,
				sysOderId, oderPlacedDTTM });
		// return updateFlag;
	}

	private void updateOrderPmToDefault(long sysOderId, String oderPlacedDTTM) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter into PromotionalMoneyDAOImpl.updateOrderPmToDefault() method");
		}
		//int updateFlag = 0;
		String orderPMQry = PromotionalMoneyQry.getOrderPmDefaultQuery();
		jdbcTemplate.update(orderPMQry, new Object[] { sysOderId,
				oderPlacedDTTM });
		// return updateFlag;
	}

	@Override
	public void rejectOrderPm(PMOrderDetailBean orderDetails)
			throws SQLException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter into PromotionalMoneyDAOImpl.rejectOrderPm() method");
		}
		// update order attribute table common for all
		this.updatePmStatus(orderDetails.getOrderId(),
				orderDetails.getOrderPlacedDTTM(), orderDetails.getPmStatus());
		this.updateOrderPmToDefault(orderDetails.getOrderId(),
				orderDetails.getOrderPlacedDTTM());

	}

	@Override
	public void updateOrderPm(PMOrderDetailBean orderDetails)
			throws SQLException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter into PromotionalMoneyDAOImpl.updateOrderPm() method");
		}
		// update order attribute table common for all
		this.updatePmStatus(orderDetails.getOrderId(),
				orderDetails.getOrderPlacedDTTM(), orderDetails.getPmStatus());

		// update or insert into orderPM table
		String inserPmQry = PromotionalMoneyQry.getOrderPmIsrtQry();
		String updatePmQry = PromotionalMoneyQry.getOrderPmUpQry();
		final List<OrderPMDataBean> insertPmList = this.getInsertList(
				orderDetails.getOrderPmList(), true);
		final List<OrderPMDataBean> updatePmList = this.getInsertList(
				orderDetails.getOrderPmList(), false);

		// insert block
		if (!insertPmList.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Inserting data to OM_ORDER_PM table");
			}
			jdbcTemplate.batchUpdate(inserPmQry,
					new BatchPreparedStatementSetter() {

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {

							OrderPMDataBean orderPmBean = insertPmList.get(i);

							ps.setInt(1, orderPmBean.getEarnedQty());
							ps.setInt(2, orderPmBean.getPotentialQty());
							ps.setInt(3, orderPmBean.getPendingQty());
							ps.setDouble(4, orderPmBean.getEarnedAmt());
							ps.setDouble(5, orderPmBean.getPotentialAmt());
							ps.setDouble(6, orderPmBean.getPendingAmt());
							ps.setLong(7, orderPmBean.getPmId());
							ps.setInt(8, orderPmBean.getActiveCd());
							ps.setLong(9, orderPmBean.getOrderId());
							ps.setLong(10, orderPmBean.getProductId());
							ps.setTimestamp(11, this.dateFormatter(orderPmBean
									.getOrderPlacedDttm()));

							ps.setLong(12, orderPmBean.getEmpId());
							ps.setString(13, PhotoOmniConstants.CREATE_USER_ID);
							ps.setDate(14, getCurrentDate());
							ps.setLong(15,
									this.getTableSequenceId("OM_ORDER_PM"));
							ps.setString(16, PhotoOmniConstants.UPDATE_USER_ID);
							ps.setDate(17, getCurrentDate());

						}

						public long getTableSequenceId(String sequenceName) {

							long sequence = 0;

							StringBuilder seqSql = new StringBuilder();
							seqSql.append("SELECT ").append(sequenceName)
									.append("_SEQ.NEXTVAL AS id FROM DUAL");
							sequence = jdbcTemplate.queryForLong(seqSql
									.toString());
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug(" OM_ORDER_PM next sequence val"
										+ sequence);
							}
							return sequence;
						}

						private Date getCurrentDate() {
							java.util.Date today = new java.util.Date();
							return new Date(today.getTime());
						}

						private Timestamp dateFormatter(String sourceDate) {
							SimpleDateFormat formatter = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							Timestamp timestamp2 = null;

							try {
								timestamp2 = new Timestamp(formatter.parse(
										sourceDate).getTime());
							} catch (ParseException e) {
								if (LOGGER.isErrorEnabled()) {
									LOGGER.error(" ParseException occoured at dateFormatter method of ServiceUtil - "
											+ e.getMessage());
								}
							} finally {
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("Exiting dateFormatter method of ServiceUtil ");
								}
							}

							return timestamp2;
						}

						public int getBatchSize() {
							return insertPmList.size();
						}
					});
		}
		// Update block
		if (!updatePmList.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Updating data to OM_ORDER_PM table");
			}
			jdbcTemplate.batchUpdate(updatePmQry,
					new BatchPreparedStatementSetter() {

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {

							OrderPMDataBean orderPmBean = updatePmList.get(i);

							ps.setInt(1, orderPmBean.getEarnedQty());
							ps.setInt(2, orderPmBean.getPotentialQty());
							ps.setInt(3, orderPmBean.getPendingQty());
							ps.setDouble(4, orderPmBean.getEarnedAmt());
							ps.setDouble(5, orderPmBean.getPotentialAmt());
							ps.setDouble(6, orderPmBean.getPendingAmt());
							ps.setLong(7, orderPmBean.getPmId());
							ps.setInt(8, orderPmBean.getActiveCd());
							ps.setString(9, PhotoOmniConstants.UPDATE_USER_ID);
							ps.setDate(10, getCurrentDate());
							ps.setLong(11, orderPmBean.getOrderId());
							ps.setLong(12, orderPmBean.getProductId());
							ps.setString(13, orderPmBean.getOrderPlacedDttm());

						}

						private Date getCurrentDate() {
							java.util.Date today = new java.util.Date();
							return new Date(today.getTime());
						}

						@SuppressWarnings("unused")
						private Timestamp dateFormatter(String sourceDate) {
							// TODO Auto-generated method stub
							SimpleDateFormat formatter = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							Timestamp timestamp2 = null;

							try {
								timestamp2 = new Timestamp(formatter.parse(
										sourceDate).getTime());
							} catch (ParseException e) {
								if (LOGGER.isErrorEnabled()) {
									LOGGER.error(" ParseException occoured at dateFormatter method of ServiceUtil - "
											+ e.getMessage());
								}
							} finally {
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("Exiting dateFormatter method of ServiceUtil ");
								}
							}

							return timestamp2;
						}

						public int getBatchSize() {
							return updatePmList.size();
						}
					});
		}

	}

	private List<OrderPMDataBean> getInsertList(
			List<OrderPMDataBean> orderPmList, boolean flag) {
		List<OrderPMDataBean> filteredList = new ArrayList<OrderPMDataBean>();
		for (OrderPMDataBean pmBean : orderPmList) {
			if (flag) {
				if (pmBean.isInsrtInd()) {
					filteredList.add(pmBean);
				}
			} else {
				if (pmBean.isUpdateInd()) {
					filteredList.add(pmBean);
				}
			}
		}
		return filteredList;
	}
	
	@Override
	public int getOrderPLUCnt(long sysOderId, String oderPlacedDTTM){
		int count = 0;
		String orderPluQry = PromotionalMoneyQry.getOrderPLUCntQry();
		count = jdbcTemplate.queryForInt(orderPluQry, new Object[] {sysOderId, oderPlacedDTTM});
		return count;
	}


	@Override
	public int getOrderLinePLUCnt(long sysOderLineId, String oderPlacedDTTM){
		int count = 0;
		String orderLinePluQry = PromotionalMoneyQry.getOrderlinePLUCntQry();
		count = jdbcTemplate.queryForInt(orderLinePluQry, new Object[] {sysOderLineId, oderPlacedDTTM});
		return count;
	}
}
