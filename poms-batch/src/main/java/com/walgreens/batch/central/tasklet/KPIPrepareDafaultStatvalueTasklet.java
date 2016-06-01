package com.walgreens.batch.central.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.walgreens.batch.central.bo.KPIBO;
import com.walgreens.batch.central.factory.KPIBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class will be used to generate the default value ie Zero value for stats
 * for which data it not returns by spring reader
 * 
 * @author CTS
 * 
 */
public class KPIPrepareDafaultStatvalueTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIPrepareDafaultStatvalueTasklet.class);

	@Autowired
	private KPIBOFactory factory;
	
	@Value("${kpi.close.store.gracetime}")
	private String noOfDays;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-12:Start Populating zero value data for stat");
		}
		List<String> storeNo = new ArrayList<String>();
		try {
			KPIBO kpiBoImpl = factory.getKpibo();
			storeNo = kpiBoImpl.getUniqueStoreNumber(chunkContext
					.getStepContext().getStepExecution().getJobExecution()
					.getExecutionContext().get("deployMentType").toString());
			storeNo = kpiBoImpl.checkStoreClosed(storeNo, noOfDays);
			if (storeNo.size() > 0) {
				kpiBoImpl.populateKPIPmStatZeroValue(storeNo,
						(String) chunkContext.getStepContext()
								.getStepExecution().getJobExecution()
								.getJobParameters().getString("currentDate"));
				kpiBoImpl.populateKPIOrderStatZeroValue(
						storeNo,
						(String) chunkContext.getStepContext()
								.getStepExecution().getJobExecution()
								.getExecutionContext()
								.get("maxTransmissionDate"));

			}
		} catch (PhotoOmniException e) {
			LOGGER.error("Error while populating zero value data for stats :",
					e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Step-12:Finished Populating zero value data for stat");
			}
		}
		return RepeatStatus.FINISHED;
	}
}