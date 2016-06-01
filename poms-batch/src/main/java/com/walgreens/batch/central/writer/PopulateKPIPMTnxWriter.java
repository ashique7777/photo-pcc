package com.walgreens.batch.central.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bo.KPIBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.KPIBOFactory;

/**
 * This class is used to calculate PHALDPMS and PHALDPMP stats
 * 
 * @author CTS
 * 
 */
public class PopulateKPIPMTnxWriter implements ItemWriter<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PopulateKPIPMTnxWriter.class);

	public static final String[] STATS = { PhotoOmniBatchConstants.PHALDPMS,
			PhotoOmniBatchConstants.PHALDPMP };

	@Autowired
	private KPIBOFactory factory;

	private String kpiCurrentDate;

	@BeforeStep
	private void retriveValue(StepExecution stepExecution) {
		this.kpiCurrentDate = stepExecution.getJobExecution()
				.getJobParameters().getString("currentDate");
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Step-5:Getting current date from the job execution {}",
					kpiCurrentDate);
		}
	}

	@Override
	public void write(List<? extends Map<String, Object>> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-5:Start Writing PM STATS data to KPI transaction table.");
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
			LOGGER.debug("Step-5:Finished Writing PM STATS data to KPI transaction table.");
		}
	}

	@AfterStep
	private void updatePOSTransaction() {
		try {
			KPIBO kpiBoImpl = factory.getKpibo();
			kpiBoImpl.updateOrderPMTransaction(kpiCurrentDate);
		} catch (Exception e) {
			LOGGER.error(
					"Error while updating pos transaction after step execution",
					e);
		}
	}
}