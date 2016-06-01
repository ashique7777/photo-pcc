package com.walgreens.batch.central.tasklet;

import java.io.File;
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
import com.walgreens.common.utility.CommonUtil;

/**
 * This class will be used to delete old KPI Feed File before generating new KPI
 * Feed File
 * 
 * @author CTS
 * 
 */
@Configuration
public class KPIFileDeletingTasklet implements Tasklet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIFileDeletingTasklet.class);

	@SuppressWarnings("unused")
	private long jobSubmitTime;

	/**
	 * Method to set jobSubmitTime
	 * 
	 * @return jobSubmitTime
	 */
	public void setJobSubmitTime(long jobSubmitTime) {
		this.jobSubmitTime = jobSubmitTime;
	}

	@Value("${kpi.source.folder.path}")
	private String kPIFlatFileLocation;

	@Autowired
	private KPIBOFactory factory;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Step-2:Start deleting KPI feed file from centralization server location -- {}",
					kPIFlatFileLocation);
		}
		KPIBO kpibo = factory.getKpibo();
		ArrayList<String> arrayList = new ArrayList<String>(
				Arrays.asList(kPIFlatFileLocation));
		kpibo.checkDirectoryExists(arrayList);
		if (!CommonUtil.isNull(kPIFlatFileLocation)
				&& !kPIFlatFileLocation.equals("")) {
			File[] files = new File(kPIFlatFileLocation).listFiles();
			for (File file : files) {
				if (file.isFile()) {
					file.delete();
				}
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Step-2:Finshed deleting KPI feed file from centralization server location -- {}",
					kPIFlatFileLocation);
		}
		return RepeatStatus.FINISHED;
	}
}