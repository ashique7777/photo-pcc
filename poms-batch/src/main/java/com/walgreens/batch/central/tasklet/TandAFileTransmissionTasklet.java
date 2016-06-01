package com.walgreens.batch.central.tasklet;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import com.walgreens.common.utility.PhotoOmniFileUtil;

public class TandAFileTransmissionTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TandAFileTransmissionTasklet.class);

	 @Value("${tanda.work.folder.path}")
	private String workFolderPath;

	 @Value("${tanda.destination.folder.path}")
	private String tandAExactFileLocation;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start feed file Transmission to EXACT Location.");
			LOGGER.debug("Source Location-{}", workFolderPath);
			LOGGER.debug("Destination Location-{}", tandAExactFileLocation);
		}
		File sourceFile = new File(workFolderPath);
		File[] files = new File(workFolderPath).listFiles();
		if (sourceFile.exists()) {
			for (File file : files) {
				if (file.isFile()) {
					File destDir = new File(tandAExactFileLocation);
					PhotoOmniFileUtil.moveFileToDirectory(file, destDir, false);
				}
			}
		}
		return RepeatStatus.FINISHED;
	}

}
