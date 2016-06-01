package com.walgreens.batch.central.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.walgreens.batch.central.bo.TimeAttendanceBO;
import com.walgreens.batch.central.factory.OMSBOFactory;

public class TandAFileArchiveTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TandAFileArchiveTasklet.class);

	
	 @Value("${tanda.archive.folder.path}")
		private String tandAArchiveFileLocation;

		 @Value("${tanda.work.folder.path}")
		private String workFolderPath;

		 @Value("${tanda.destination.folder.path}")
		private String tandAExactFileLocation;

	@Autowired
	private OMSBOFactory factory;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Step-1:Starting Time and Attendance feed file Archive Process from - {} to - {}",
					tandAExactFileLocation, tandAArchiveFileLocation);
		}
		TimeAttendanceBO taBO = factory.getTimeAttendanceBO();
		taBO.transferExactFileToArchiveFolder(tandAExactFileLocation, tandAArchiveFileLocation);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Step-1:Finished Time and Attendance feed file Archive Process from - {} to - {}",
					tandAExactFileLocation, tandAArchiveFileLocation);
		}
		return RepeatStatus.FINISHED;
	}

}
