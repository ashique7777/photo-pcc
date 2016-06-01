package com.walgreens.batch.central.tasklet;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.walgreens.common.utility.PhotoOmniFileUtil;

/**
 * 
 * This will be used to transmit the KPI Feed File to EAXCT location. this class
 * will get called once there is not record for any stat for any store number in
 * that case system will generate the default value and rewrite in the file and
 * new file will get transmitted to EAXCT location
 * 
 * @author CTS
 * 
 */
@Configuration
public class KPIFileTransmissionTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIFileTransmissionTasklet.class);

	@Value("${kpi.source.folder.path}")
	private String source;
	@Value("${kpi.exact.folder.path}")
	private String destination;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Step-13:Start feed file Transmission to EXACT Location.");
			LOGGER.debug("Step-13:Source Location-{}", source);
			LOGGER.debug("Step-13:Destination Location-{}", destination);
		}
		File sourceFile = new File(source);
		File[] files = new File(source).listFiles();
		if (sourceFile.exists()) {
			for (File file : files) {
				if (file.isFile()) {
					File destDir = new File(destination);
					PhotoOmniFileUtil.moveFileToDirectory(file, destDir, false);
				}
			}
		}
		return RepeatStatus.FINISHED;
	}
}