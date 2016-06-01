package com.walgreens.batch.central.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bo.KPIBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.KPIBOFactory;

/**
 * This class is used to calculate PHALPPTA stat
 * 
 * @author CTS
 * 
 */
public class PopulateKPIOneHourTnxWriter implements
		ItemWriter<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PopulateKPIOneHourTnxWriter.class);

	public static final String STAT = PhotoOmniBatchConstants.PHALPPTA;

	@Autowired
	private KPIBOFactory factory;

	@Override
	public void write(List<? extends Map<String, Object>> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-9:Start Writing 1HR PROMISED ORDER STATS data to KPI transaction table.");
		}
		List<Map<String, Object>> finalData = new ArrayList<Map<String, Object>>();
		KPIBO kpiBoImpl = factory.getKpibo();
		kpiBoImpl.validateStatAndPrepareTransactionData(STAT, items, finalData);
		kpiBoImpl.populateKPITranscations(finalData,
				PhotoOmniBatchConstants.OM_KPI_ORDER_TRANSACTION, true);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-9:Finsihed Writing 1HR PROMISED ORDER STATS data to KPI transaction table.");
		}
	}
}