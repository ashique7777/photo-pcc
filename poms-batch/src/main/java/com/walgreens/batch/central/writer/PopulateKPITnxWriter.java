package com.walgreens.batch.central.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bo.KPIBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.KPIBOFactory;

/**
 * This class will be used to calculate the PHALTOS,PHALCCDT and PHALCSDT stats
 * 
 * @author CTS
 * 
 */
public class PopulateKPITnxWriter implements ItemWriter<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PopulateKPITnxWriter.class);

	public static final String[] STATS = { PhotoOmniBatchConstants.PHALTOS,
			PhotoOmniBatchConstants.PHALCSDT, PhotoOmniBatchConstants.PHALCCDT };

	@Autowired
	private KPIBOFactory factory;

	@Override
	public void write(List<? extends Map<String, Object>> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-4:Start Writing SALES STATS data to KPI transaction table.");
		}
		List<Map<String, Object>> finalData = new ArrayList<Map<String, Object>>();
		KPIBO kpiBoImpl = factory.getKpibo();
		for (String stat : STATS) {
			kpiBoImpl.validateStatAndPrepareTransactionData(stat, items,
					finalData);
		}
		kpiBoImpl.populateKPITranscations(finalData,
				PhotoOmniBatchConstants.OM_KPI_POS_PM_TRANSACTION, false);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-4:Finised Writing SALES STATS data to KPI transaction table.");
		}
	}

	@AfterStep
	private void updatePOSTransaction() {
		try {
			KPIBO kpiBoImpl = factory.getKpibo();
			kpiBoImpl.updatePOSTransaction();
		} catch (Exception e) {
			LOGGER.error(
					"Error while updating pos transaction after step execution",
					e);
		}
	}
}