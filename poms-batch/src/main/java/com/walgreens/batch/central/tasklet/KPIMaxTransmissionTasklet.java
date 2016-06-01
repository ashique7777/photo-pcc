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
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class will be used to fetch max transmission date which will be put
 * inside execution context so that it can be used in Order related stat
 * calculation.
 * 
 * @author CTS
 * 
 */
public class KPIMaxTransmissionTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIMaxTransmissionTasklet.class);

	@Autowired
	private KPIBOFactory factory;

	private String maxTransmissionDate;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step:6:Getting MAX TRANSMISSION DATE");
		}
		KPIBO kpiBoImpl = factory.getKpibo();
		maxTransmissionDate = kpiBoImpl.getMaxTransmissionDate();
		chunkContext
				.getStepContext()
				.getStepExecution()
				.getJobExecution()
				.getExecutionContext()
				.put("maxTransmissionDate",
						((maxTransmissionDate == null) ? kpiBoImpl
								.getFormattedDate(kpiBoImpl.getYesterdayDate(),
										PhotoOmniConstants.DATE_FORMAT_TWO)
								: CommonUtil
										.stringDateFormatChange(
												maxTransmissionDate,
												PhotoOmniConstants.DATE_FORMAT_THIRTEENTH,
												PhotoOmniConstants.DATE_FORMAT_TWO)));

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Step:6:Finished MAX TRANSMISSION DATE -  {}",
					chunkContext.getStepContext().getStepExecution()
							.getJobExecution().getExecutionContext()
							.get("maxTransmissionDate"));
		}
		return RepeatStatus.FINISHED;
	}
}