/**
 * 
 */
package com.walgreens.oms.dao;

/**
 * @author CTS
 *
 */
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
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.MBPMDetailsBean;
import com.walgreens.oms.bean.OmPromotionalMoneyBean;
import com.walgreens.oms.bean.OrderDetailData;
import com.walgreens.oms.bean.OrderPMDataBean;
import com.walgreens.oms.bean.PMRule;
import com.walgreens.oms.bean.PMRuleDetail;
import com.walgreens.oms.rowmapper.MBPMROrderRowMapper;
import com.walgreens.oms.rowmapper.OmPromotionalMoneyBeanRowMapper;
import com.walgreens.oms.rowmapper.OrderMbpmRowmapper;
import com.walgreens.oms.utility.MbpmQuery;
import com.walgreens.oms.utility.PromotionalMoneyQry;

/**
 * @author Cognizant
 * 
 */
@Component("MBPromotionalMoneyDAO")
@SuppressWarnings("deprecation")
public class MBPromotionalMoneyDAOImpl implements MBPromotionalMoneyDAO {

	final Logger LOGGER = LoggerFactory
			.getLogger(MBPromotionalMoneyDAOImpl.class);

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * This method is used to save user entered data to the database
	 * 
	 * @param sysShoppingCartID
	 *            ,orderPlacedDttm
	 * @return orderDtlsList.
	 */
	public List<OrderDetailData> getMbpmOrderDetailData(long sysShoppingCartID,
			String orderPlacedDttm) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getMbpmOrderDetailData method of MBPromotionalMoneyDAOImpl ");
		}
		List<OrderDetailData> orderDtlsList = new ArrayList<OrderDetailData>();
		try {
			String selectQry = MbpmQuery.populateMBPMData().toString();
			Object[] params = new Object[] { sysShoppingCartID, orderPlacedDttm };

			orderDtlsList = jdbcTemplate.query(selectQry, params,
					new MBPMROrderRowMapper());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getMbpmOrderDetailData method of MBPromotionalMoneyDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getMbpmOrderDetailData method of MBPromotionalMoneyDAOImpl ");
			}
		}
		return orderDtlsList;

	}

	/**
	 * This method is used to save user entered data to the database
	 * 
	 * @param startDate
	 *            ,endDate
	 * @return promDataList.
	 */
	public PMRule getMbpmRuleData(String startdate, String endDate) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getMbpmRuleData method of MBPromotionalMoneyDAOImpl ");
		}
		List<OmPromotionalMoneyBean> promDataList = null;
		PMRule pmRuleBean = null;
		OrderDetailData orderBean = new OrderDetailData();
		try {
			String selectQry = MbpmQuery.populatePromotionalMoneyData()
					.toString();
			Object[] params = new Object[] { startdate, endDate };
			promDataList = jdbcTemplate.query(selectQry, params,
					new OmPromotionalMoneyBeanRowMapper(false));
			orderBean.setOmPromotionalMoneyBeanList(promDataList);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getMbpmRuleData method of MBPromotionalMoneyDAOImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getMbpmRuleData method of MBPromotionalMoneyDAOImpl ");
			}
		}
		if (null != promDataList) {
			pmRuleBean = this.getStructuredPmRule(promDataList);
		}
		return pmRuleBean;
	}

	/**
	 * 
	 * @param pmRuleList
	 * @return pmRuleBean
	 */
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
		pmRuleBean.setTierType(pmRuleList.get(0).getTierType());
		return pmRuleBean;
	}

	/**
	 * This method is used to save user entered data to the database
	 * 
	 * @param sysShoppingCartID
	 *            ,orderPlacedDttm
	 * @return sysOrderId.
	 */
	public List<OrderPMDataBean> getOrderMBPMData(long sysOrderId,
			String orderPlacedDttm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getOrderMBPMData method of MBPromotionalMoneyDAOImpl ");
		}
		List<OrderPMDataBean> orderPMList = new ArrayList<OrderPMDataBean>();
		String selectQry = MbpmQuery.getOrderPMQuery().toString();
		Object[] params = new Object[] { sysOrderId, orderPlacedDttm };
		try {
			orderPMList = jdbcTemplate.query(selectQry, params,
					new OrderMbpmRowmapper());
		} catch (InvalidResultSetAccessException ex) {
			LOGGER.error("Exception occurred    ", ex);
		} catch (EmptyResultDataAccessException ex) {
			LOGGER.error("Exception occurred    ", ex);
		} catch (DataAccessException ex) {
			LOGGER.error("Exception occurred    ", ex);
		} catch (Exception ex) {
			LOGGER.error("Exception occurred    ", ex);
		}
		return orderPMList;

	}

	/**
	 * @param pmStatus
	 * @param oderPlacedDTTM
	 * @param sysOderId
	 */
	public void updatePmStatus(long sysShoppingCartID, String orderPlacedDttm,
			String pmStatus) {
		int updateFlag = 0;
		String orderPMQry = MbpmQuery.getPMStatusQuery();
		updateFlag = jdbcTemplate.update(orderPMQry, new Object[] { pmStatus,
				sysShoppingCartID, orderPlacedDttm });
	}

	/**
	 * @param sysOderId
	 * @param oderPlacedDTTM
	 */
	public void updateOrderPmToDefault(long sysOderId, String orderPlacedDttm) {
		int updateFlag = 0;
		String orderPMQry = MbpmQuery.getOrderPmDefaultQuery();
		updateFlag = jdbcTemplate.update(orderPMQry, new Object[] { sysOderId,
				orderPlacedDttm });
	}

	/**
	 * @param orderDetails
	 */
	public void rejectOrderPm(MBPMDetailsBean mbpmBasket) throws SQLException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter into MBPromotionalMoneyDAOImpl.rejectOrderPm()");
		}
		// update order attribute table common for all
		this.updatePmStatus(mbpmBasket.getSysShoppingCartId(),
				mbpmBasket.getOrderPlacedDTTM(), mbpmBasket.getPmStatus());
		for (OrderDetailData orderDetails : mbpmBasket.getOrderDataList()) {
			this.updateOrderPmToDefault(orderDetails.getSysOrderId(),
					orderDetails.getOrderPlacedDttm());
		}
	}

	/**
	 * @param mbpmBasket
	 * @param orderDetails
	 */
	@Override
	public void updateOrderPm(MBPMDetailsBean mbpmBasket) throws SQLException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter into MBPromotionalMoneyDAOImpl.updateOrderPm()");
		}
		// update order attribute table common for all
		this.updatePmStatus(mbpmBasket.getSysShoppingCartId(),
				mbpmBasket.getOrderPlacedDTTM(), mbpmBasket.getPmStatus());
		// update or insert into orderPM table
		String inserPmQry = PromotionalMoneyQry.getOrderPmIsrtQry();
		String updatePmQry = PromotionalMoneyQry.getOrderPmUpQry();
		final List<OrderPMDataBean> insertPmList = this.getInsertList(
				mbpmBasket.getOrderPmList(), true);
		final List<OrderPMDataBean> updatePmList = this.getInsertList(
				mbpmBasket.getOrderPmList(), false);

		// insert block
		if (!insertPmList.isEmpty()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Insert into OM Order PM for MBPM");
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
				LOGGER.debug("Update OM Order PM for MBPM");
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

	/**
	 * 
	 * @param orderPmList
	 * @param flag
	 * @return
	 */
	public List<OrderPMDataBean> getInsertList(
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

}
