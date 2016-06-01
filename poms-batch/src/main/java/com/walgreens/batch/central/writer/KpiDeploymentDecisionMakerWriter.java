package com.walgreens.batch.central.writer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.utility.CommonUtil;

public class KpiDeploymentDecisionMakerWriter implements
		ItemWriter<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KpiDeploymentDecisionMakerWriter.class);

	private String deployMentType;

	@Override
	public void write(List<? extends Map<String, Object>> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-0:Saving PROPERTY_VALUE to execution context.");
		}
		String startDate = CommonUtil.stringDateFormatChange(
				CommonUtil.getCurrentTimeStamp(),
				PhotoOmniConstants.STORE_DATE_PATTERN,
				PhotoOmniConstants.DATE_FORMAT_FIFTH);
		String endDate = CommonUtil.stringDateFormatChange(
				items.get(0).get("PROPERTY_VALUE").toString(),
				PhotoOmniConstants.DATE_FORMAT_ZERO,
				PhotoOmniConstants.DATE_FORMAT_FIFTH);
		if (CommonUtil.isBeforeDate(startDate, endDate,
				PhotoOmniConstants.DATE_FORMAT_FIFTH)
				&& !startDate.equals(endDate)) {
			this.deployMentType = PhotoOmniBatchConstants.STORE_WISE;
		} else {
			this.deployMentType = PhotoOmniBatchConstants.ALL_STORE;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-0:Finised Saving PROPERTY_VALUE to execution context.");
		}
	}

	@AfterStep
	private void retriveValue(StepExecution stepExecution) {
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		executionContext.put("deployMentType", deployMentType);
	}
}