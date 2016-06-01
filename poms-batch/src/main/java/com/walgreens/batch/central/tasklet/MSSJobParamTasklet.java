package com.walgreens.batch.central.tasklet;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

public class MSSJobParamTasklet implements Tasklet{
	
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MSSJobParamTasklet.class);
	
	/**
     * This method will set the File_SYS_TIME to the job parameter.
     * @param contribution contains StepContribution values.
     * @param chunkContext contains chunkContext values.
     */
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering MSSJobParamTasklet class ");
		}
		try {

			StepContext stepContext = chunkContext.getStepContext();
			StepExecution stepExecution = stepContext.getStepExecution();
			SimpleDateFormat dateFormat = new SimpleDateFormat(PhotoOmniConstants.MSS_FILE_TIME_FORMAT);

			final JobExecution jobExecution = stepExecution.getJobExecution();

			String fileTime = dateFormat.format(new Date());
			jobExecution.getExecutionContext().put(PhotoOmniConstants.FILE_SYS_TIME, fileTime);

		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at execute method of MSSJobParamTasklet - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of MSSJobParamTasklet ");
			}
		}
		return RepeatStatus.FINISHED;
	}
	
}
