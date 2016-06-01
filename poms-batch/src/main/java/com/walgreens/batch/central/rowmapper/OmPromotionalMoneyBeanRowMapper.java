package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.OmPromotionalMoneyBean;

/**
 * @author Cognizant
 *
 */
public class OmPromotionalMoneyBeanRowMapper implements RowMapper<OmPromotionalMoneyBean>{

	private static final Logger log = LoggerFactory.getLogger(OmPromotionalMoneyBeanRowMapper.class);
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public OmPromotionalMoneyBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		log.debug("Entering into OmPromotionalMoneyBean: mapRow()");
		
		OmPromotionalMoneyBean omPromotionalMoneyBean = new OmPromotionalMoneyBean();
		
		omPromotionalMoneyBean.setSysPmId(rs.getInt("SYS_PM_ID"));
		omPromotionalMoneyBean.setType(rs.getString("TYPE"));
		omPromotionalMoneyBean.setPmDistributionType(rs.getString("PM_DISTRIBUTION_TYPE"));
		omPromotionalMoneyBean.setPmRuleDesc(rs.getString("PM_RULE_DESC"));
		omPromotionalMoneyBean.setStartDttm(rs.getTimestamp("START_DTTM"));
		omPromotionalMoneyBean.setEndDttm(rs.getTimestamp("END_DTTM"));
		omPromotionalMoneyBean.setTierType(rs.getString("TIER_TYPE"));
		omPromotionalMoneyBean.setActiveInd(rs.getInt("ACTIVE_CD"));
		omPromotionalMoneyBean.setDiscountApplicableInd(rs.getInt("DISCOUNT_APPLICABLE_CD"));
		omPromotionalMoneyBean.setMinimumTier(rs.getDouble("MINIMUM_TIER"));
		omPromotionalMoneyBean.setMaximumTier(rs.getDouble("MAXIMUM_TIER"));
		omPromotionalMoneyBean.setPmAmount(rs.getFloat("PM_AMOUNT"));
		
		/*OmPromotionalMoneyBean omPromotionalMoneyBean = new OmPromotionalMoneyBean();
		
		omPromotionalMoneyBean.setSysPmId(rs.getInt("SYS_PM_ID"));
		omPromotionalMoneyBean.setType(rs.getString("TYPE"));
		omPromotionalMoneyBean.setPmDistributionType(rs.getString("PM_DISTRIBUTION_TYPE"));
		omPromotionalMoneyBean.setPmRuleDesc(rs.getString("PM_RULE_DESC"));
		omPromotionalMoneyBean.setProductId(rs.getInt("SYS_PRODUCT_ID"));
		omPromotionalMoneyBean.setStartDttm(rs.getTimestamp("START_DTTM"));
		omPromotionalMoneyBean.setEndDttm(rs.getTimestamp("END_DTTM"));
		omPromotionalMoneyBean.setPmPaymentType(rs.getString("PM_PAYMENT_TYPE"));
		omPromotionalMoneyBean.setTierType(rs.getString("TIER_TYPE"));
		omPromotionalMoneyBean.setActiveInd(rs.getInt("ACTIVE_CD"));
		omPromotionalMoneyBean.setDeactivationDttm(rs.getDate("DEACTIVATION_DTTM"));
		omPromotionalMoneyBean.setDiscountApplicableInd(rs.getInt("DISCOUNT_APPLICABLE_CD"));
		omPromotionalMoneyBean.setPmRuleType(rs.getString("PM_RULE_TYPE"));
		omPromotionalMoneyBean.setCreateUserID(rs.getString("CREATE_USER_ID"));
		omPromotionalMoneyBean.setCreateDttm(rs.getTimestamp("CREATE_DTTM"));
		omPromotionalMoneyBean.setUpdateUserId(rs.getString("UPDATE_USER_ID"));
		omPromotionalMoneyBean.setUpdateDttm(rs.getTimestamp("UPDATE_DTTM"));
		omPromotionalMoneyBean.setPmNbr(rs.getString("PM_NBR"));*/
		
		log.debug("Exiting from OmPromotionalMoneyBean: mapRow()");
		return omPromotionalMoneyBean;
	}

}
