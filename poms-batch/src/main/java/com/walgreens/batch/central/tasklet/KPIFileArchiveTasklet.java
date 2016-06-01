package com.walgreens.batch.central.tasklet;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.walgreens.batch.central.bo.KPIBO;
import com.walgreens.batch.central.factory.KPIBOFactory;

/**
 * 
 * This class will be used for archive process for KPI Integration. It will pick
 * the file from EXACT location and archive it or backup it the provided
 * location.
 * 
 * @author CTS
 * 
 */
@Configuration
public class KPIFileArchiveTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIFileArchiveTasklet.class);

	@Value("${kpi.exact.folder.path}")
	private String kPIExactFileLocation;

	@Value("${kpi.archive.folder.path}")
	private String kPIArchiveFileLocation;

	@Autowired
	private KPIBOFactory factory;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Step-1:Starting KPI feed file Archive Process from - {} to - {}",
					kPIExactFileLocation, kPIArchiveFileLocation);
		}
		String[] filePath = { kPIArchiveFileLocation, kPIExactFileLocation };
		ArrayList<String> arrayList = new ArrayList<String>(
				Arrays.asList(filePath));
		KPIBO kpiBoImpl = factory.getKpibo();
		kpiBoImpl.checkDirectoryExists(arrayList);
		kpiBoImpl.getRemoteFile(kPIExactFileLocation, kPIArchiveFileLocation);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Step-1:Finished KPI feed file Archive Process from - {} to - {}",
					kPIExactFileLocation, kPIArchiveFileLocation);
		}
		return RepeatStatus.FINISHED;
	}
}