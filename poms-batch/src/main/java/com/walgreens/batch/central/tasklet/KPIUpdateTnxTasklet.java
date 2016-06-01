package com.walgreens.batch.central.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bo.KPIBO;
import com.walgreens.batch.central.factory.KPIBOFactory;

/**
 * This class will be used to update the db tables once KPI Feed File
 * transmitted to EXACT location
 * 
 * @author CTS
 * 
 */
public class KPIUpdateTnxTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIUpdateTnxTasklet.class);

	@Autowired
	private KPIBOFactory factory;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-14:Start updating KPI flag after file transmission.");
		}
		KPIBO kpiBoImpl = factory.getKpibo();
		kpiBoImpl.updateKPITransactionAfterTransmit(chunkContext
				.getStepContext().getStepExecution().getJobExecution().getId());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-14:Finished updating KPI flag after file transmission.");
		}
		return RepeatStatus.FINISHED;
	}
}