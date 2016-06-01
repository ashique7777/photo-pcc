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
 * This class will be used to calculate the PHALPTOH and PHALCTOH stats
 * 
 * @author CTS
 * 
 */
public class PopulateKPISameDayTnxWriter implements
		ItemWriter<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PopulateKPISameDayTnxWriter.class);

	public static final String[] STATS = { PhotoOmniBatchConstants.PHALPTOH,
			PhotoOmniBatchConstants.PHALCTOH };

	@Autowired
	private KPIBOFactory factory;

	@Override
	public void write(List<? extends Map<String, Object>> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-7:Start Writing SAME DAY ORDER STATS data to KPI transaction table.");
		}
		List<Map<String, Object>> finalData = new ArrayList<Map<String, Object>>();
		KPIBO kpiBoImpl = factory.getKpibo();
		for (String stat : STATS) {
			kpiBoImpl.validateStatAndPrepareTransactionData(stat, items,
					finalData);
		}
		kpiBoImpl.populateKPITranscations(finalData,
				PhotoOmniBatchConstants.OM_KPI_ORDER_TRANSACTION, true);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-7:Finished Writing SAME DAY ORDER STATS data to KPI transaction table.");
		}
	}
}