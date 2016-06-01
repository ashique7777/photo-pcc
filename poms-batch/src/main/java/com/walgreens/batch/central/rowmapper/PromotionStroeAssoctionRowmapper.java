package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.PromotionStoresAssocBean;

public class PromotionStroeAssoctionRowmapper implements RowMapper<PromotionStoresAssocBean>{

	@Override
	public PromotionStoresAssocBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PromotionStoresAssocBean objPromotionStoresAssocBean = new PromotionStoresAssocBean();
		
		objPromotionStoresAssocBean.setAdvEventType(rs.getString("AD_EVENT_TYPE_CD"));
		objPromotionStoresAssocBean.setAdvEventSeqNbr(rs.getString("AD_EVENT_SEQ_ID"));
		objPromotionStoresAssocBean.setAdEventRelInd(rs.getString("AD_EVENT_REL_IND"));
		objPromotionStoresAssocBean.setPluNBR(rs.getInt("PLU_COUPON_NBR"));
		objPromotionStoresAssocBean.setAdvEvVerStartDate(rs.getString("AD_EVENT_START_DT"));
		objPromotionStoresAssocBean.setAdvEvVerEndDate(rs.getString("AD_EVENT_END_DT"));
		objPromotionStoresAssocBean.setStoreNBR(rs.getString("LOCATION_NBR"));
		
		return objPromotionStoresAssocBean;
	}

}
