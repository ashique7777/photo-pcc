package com.walgreens.batch.central.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bo.KPIBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.KPIBOFactory;

/**
 * This class will be called before stats calculation starts
 * 
 * @author CTS
 * 
 */
public class KPIFlagUpdateTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIFlagUpdateTasklet.class);

	@Autowired
	private KPIBOFactory factory;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-3:Starting KPI flag updation before feed generate.");
		}
		KPIBO kpiBoImpl = factory.getKpibo();
		if (chunkContext.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext().get("deployMentType")
				.equals(PhotoOmniBatchConstants.STORE_WISE)) {
			kpiBoImpl.updateKPIIndicator(chunkContext.getStepContext()
					.getStepExecution().getJobExecution().getJobParameters()
					.getString("currentDate"), true);
		} else {
			kpiBoImpl.updateKPIIndicator(chunkContext.getStepContext()
					.getStepExecution().getJobExecution().getJobParameters()
					.getString("currentDate"), false);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-3:Finished KPI flag updation before feed generate.");
		}
		return RepeatStatus.FINISHED;
	}
}