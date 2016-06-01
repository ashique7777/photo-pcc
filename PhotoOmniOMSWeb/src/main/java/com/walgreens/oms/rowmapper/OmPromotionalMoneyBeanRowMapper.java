package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OmPromotionalMoneyBean;


/**
 * @author Cognizant
 *
 */
public class OmPromotionalMoneyBeanRowMapper implements RowMapper<OmPromotionalMoneyBean>{

	private static final Logger log = LoggerFactory.getLogger(OmPromotionalMoneyBeanRowMapper.class);
	private boolean isPm;
	
	/**
	 * @param isPm
	 */
	public OmPromotionalMoneyBeanRowMapper(boolean isPm) {
		//super();
		this.isPm = isPm;
	}

	@Override
	public OmPromotionalMoneyBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		OmPromotionalMoneyBean omPromotionalMoneyBean = new OmPromotionalMoneyBean();
		omPromotionalMoneyBean.setSysPmId(rs.getInt("PM_ID"));
		omPromotionalMoneyBean.setDiscountApplicableInd(rs.getInt("DISCOUNT_APPLICABLE_CD"));
		omPromotionalMoneyBean.setTierType(rs.getString("TIER_TYPE"));
		omPromotionalMoneyBean.setDiscountApplicableInd(rs.getInt("DISCOUNT_APPLICABLE_CD"));
		omPromotionalMoneyBean.setMinimumTier(rs.getDouble("MINIMUM_TIER"));
		omPromotionalMoneyBean.setMaximumTier(rs.getDouble("MAXIMUM_TIER"));
		omPromotionalMoneyBean.setPmAmount(rs.getFloat("PM_AMOUNT"));
		if(this.isPm){
			omPromotionalMoneyBean.setPmPaymentType(rs.getString("PM_PAYMENT_TYPE"));
			omPromotionalMoneyBean.setPmRuleType(rs.getString("Pm_Rule_Type"));
		}
		log.debug("In OmPromotionalMoneyBeanRowMapper.class");
		return omPromotionalMoneyBean;
	}

}
