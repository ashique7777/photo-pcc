package com.walgreens.batch.central.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.query.EmailReportQuery;
import com.walgreens.common.utility.CommonUtil;

@Component("EmailPhotoDailyMachineDowntimeReport")
public class PhotoDailyMachineDowntimeReportProvider extends
		AbstractEmailReportDataProvider {

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate omniJdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public EmailReportBean populateData(EmailReportBean emailReportBean) {
		Map<String, Object> data = populateData();
		emailReportBean.setData(data);
		Map<String, Object> yesterdayMap = (Map<String, Object>) data
				.get("yesterdayMap");
		emailReportBean.setSubject("Daily Photo Machine Downtime Report for "
				+ convertDateToString((String) yesterdayMap
						.get("DATEWITHOUTTIME")));
		return emailReportBean;
	}

	private Map<String, Object> populateData() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> yesterdayMap = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getYesterdayDate());
		map.put("yesterdayMap", yesterdayMap);
		map.put("equipmentData", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport6EquipmentQuery(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME") }));
		map.put("machineData", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport6MachineQuery(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME") }));
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String processEmailBody(EmailReportBean emailReportBean) {
		Map<String, Object> data = emailReportBean.getData();
		List<Map<String, Object>> equipmentData = (List<Map<String, Object>>) data
				.get("equipmentData");

		List<Map<String, Object>> machineData = (List<Map<String, Object>>) data
				.get("machineData");

		StringBuffer buffer = new StringBuffer();
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Machine Downtime Details:</div>");
		if(machineData.size() > 0){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			buffer.append("<tr>");
			buffer.append("<th align='left' style='border-bottom:1px solid black;'>Machine Description</th><th align='right' style='border-bottom:1px solid black;'>Downtime(Hrs)</th>");
			buffer.append("</tr>");
			for (Map<String, Object> map : machineData) {
				buffer.append("<tr>");
				buffer.append("<td align='left'>"+((String) map.get("MachineDescription"))+"</td><td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("Downtime")))+"</td>");
				buffer.append("</tr>");
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Equipment Downtime Details:</div>");
		if(equipmentData.size() > 0){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			buffer.append("<tr>");
			buffer.append("<th align='left' style='border-bottom:1px solid black;'>Equipment Description</th><th align='right' style='border-bottom:1px solid black;'>Downtime(Hrs)</th>");
			buffer.append("</tr>");
			for (Map<String, Object> map : equipmentData) {
				buffer.append("<tr>");
				buffer.append("<td align='left'>"+((String) map.get("EquipmentDescription"))+"</td><td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("Downtime")))+"</td>");
				buffer.append("</tr>");
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		return buffer.toString();
	}

}
