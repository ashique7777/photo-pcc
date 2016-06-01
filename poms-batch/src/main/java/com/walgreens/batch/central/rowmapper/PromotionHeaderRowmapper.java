package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.PromotionHeaderDataBean;

public class PromotionHeaderRowmapper implements RowMapper<PromotionHeaderDataBean>  {

	@Override
	public PromotionHeaderDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PromotionHeaderDataBean objPromotionHeaderDataBean = new PromotionHeaderDataBean();

		objPromotionHeaderDataBean.setAdvEventType(rs.getString("AD_EVENT_TYPE_CD"));
		objPromotionHeaderDataBean.setAdvEventSeqNbr(rs.getString("AD_EVENT_SEQ_ID"));
		objPromotionHeaderDataBean.setAdEventRelInd(rs.getString("AD_EVENT_REL_IND"));
		objPromotionHeaderDataBean.setRecvDTTM(rs.getDate("FEEDFILE_RCVD_DTTM"));

		return objPromotionHeaderDataBean;
	}

}
