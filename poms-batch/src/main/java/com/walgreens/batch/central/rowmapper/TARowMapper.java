package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.TADataBean;
import com.walgreens.common.utility.CommonUtil;

/**This class implements the functionality for mapping the rows from
 * database to TADataBean
 * 
 * @author Cognizant
 *
 */
public class TARowMapper implements RowMapper<TADataBean> { 
	final private static Logger log = LoggerFactory
			.getLogger(TARowMapper.class);
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public TADataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("Entering into TARowMapper:mapRow()");
		}
		TADataBean taDataBean = new TADataBean();
		taDataBean.setTotalRecors(rs.getInt("TOTAL_RECORDS")); 
		taDataBean.setOrderId(rs.getInt("SYS_ORDER_ID"));
		taDataBean.setEffectiveDATE(CommonUtil.convertDateToString(rs.getDate("EFFECTIVE_DATE"),"MM/dd/YYYY"));
		//taDataBean.setEffectiveDATE(rs.getString("EFFECTIVE_DATE"));
		taDataBean.setEmployeeID((String)new DecimalFormat("0000000").format(rs.getLong(("EMPLOYEE_ID"))));
		taDataBean.setCommentText(" ");
		taDataBean.setDollarPMAmount(rs.getString("DOLLAR_PM_AMOUNT"));
		taDataBean.setPayCodeName("PHOTO PM");
		taDataBean.setStoreFeedCode(" ");
		taDataBean.setStoreNumber(rs.getString("STORE_NUMBER"));
		taDataBean.setUndefined(" ");
		
		if (log.isDebugEnabled()) {
			log.debug("Exiting from TARowMapper:mapRow()");
		}
		return taDataBean;
	}

}
