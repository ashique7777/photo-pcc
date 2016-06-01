package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.EnvelopePopupHeaderBean;

public class EnvelopePopupHeaderRowMapper implements RowMapper<EnvelopePopupHeaderBean> {

	@Override
	public EnvelopePopupHeaderBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		EnvelopePopupHeaderBean popupHeaderBean = new EnvelopePopupHeaderBean();
		popupHeaderBean.setKioskID(rs.getString("KIOSK_ID"));
		popupHeaderBean.setShipmentStatus(rs.getString("SHIPMENT_STATUS"));
		popupHeaderBean.setVendorName(rs.getString("VENDOR_NAME"));
		popupHeaderBean.setAreaCode(rs.getString("AREA_CODE"));
		popupHeaderBean.setVendorPhone(rs.getString("VENDOR_PHONE"));
		popupHeaderBean.setVendorTrackingSite(rs.getString("VENDOR_TRACKING_SITE"));
		popupHeaderBean.setWedID(rs.getString("WEB_ID"));
		return popupHeaderBean;
	}
	

}
