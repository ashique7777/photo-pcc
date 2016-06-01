package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.PMReportResponseBean;
import com.walgreens.oms.utility.ServiceUtil;

public class PMReportByProductRowMapperTest implements RowMapper<PMReportResponseBean> {

	@Override
	public PMReportResponseBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PMReportResponseBean responseBean = new PMReportResponseBean();
		responseBean.setEmployeeFirstName(rs.getString("FIRST_NAME"));
		responseBean.setEmployeeLastName(rs.getString("LAST_NAME"));
		responseBean.setPmEarnedQty(CommonUtil.bigDecimalToLong(rs.getString("EARNED_QTY")));
		responseBean.setPmEnteredQty(CommonUtil.bigDecimalToLong(rs.getString("POTENTIAL_QTY")));
		responseBean.setPmPendingQty(CommonUtil.bigDecimalToLong(rs.getString("PENDING_QTY")));
		responseBean.setPmEarnedAmt(CommonUtil.bigDecimalToDouble(rs.getString("EARNED_AMOUNT")));
		responseBean.setPmEnteredAmt(CommonUtil.bigDecimalToDouble(rs.getString("POTENTIAL_AMOUNT")));
		responseBean.setPmPendingAmt(CommonUtil.bigDecimalToDouble(rs.getString("PENDING_AMOUNT")));
		
	
	if(responseBean.getPmPendingAmt() != 0){
		responseBean.setPmPerProduct(ServiceUtil.calculatePMPerProduct(responseBean.getPmPendingAmt(),responseBean.getPmPendingQty()));
	}else{
		responseBean.setPmPerProduct(ServiceUtil.calculatePMPerProduct(responseBean.getPmEarnedAmt(),responseBean.getPmEarnedQty()));;
	}	
		responseBean.setProductDesc(rs.getString("DESCRIPTION"));
		//responseBean.setTotalRows(rs.getInt("TotalRows"));
		return responseBean;
	}

}
