/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * 	class to launch the ProcessPCSFeedJob
 * 	to process feed files
 * </p>
 * 
 * <p>
 * This class will launch feed file processor job which will process the 
 * feed files from the advertisement team and process the same and update the 
 * required tables.
 * 
 * Once the data is processed the feed files will be archived
 * </p>
 * 
 * <p>
 * {@link DailyDeleteTask}
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
public class PromotionFeedtoCentralTask {

	/**
	 * jdbcTemplate
	 */
	private static JdbcTemplate jdbcTemplate;

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PromotionFeedtoCentralTask.class);

	/**
	 * Context file loader
	 */
	private static ApplicationContext context;

	public static void main(String[] args)  throws  PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into PromotionFeedtoCentralTask.main()");
		}
		JobExecution execution = null;
		try {

			String[] springConfig  = 
				{	"spring/batch/config/database.xml", 
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_PromotionFeedtocentral.xml"
				};
			context = new ClassPathXmlApplicationContext(springConfig);
			Properties prop = new Properties();
			List<String> fileNameList = new ArrayList<String>();

			InputStream inputStream = 
					PromotionFeedtoCentralTask.class.getClassLoader().getResourceAsStream(PhotoOmniConstants.PHOTOOMNIBATCH_PROPERTIES);
			prop.load(inputStream);
			String strInboundFeedLocation = prop.get("promotion.inbound.feed.location").toString().trim();
			File[] lstFiles = new File(strInboundFeedLocation).listFiles();
			Map<String, List<String>> mpPFCList = new TreeMap<String, List<String>>();
			String StrFileName = "", strLstFileNames = "", startDate = "";
			SimpleDateFormat sf = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_FOURTEEN);
			SimpleDateFormat sdf = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_FIFTEEN);
			String strEventTypeExistsQuery = ReportsQuery.checkEventTypeExists().toString();
			int iCount = 0;

			if(null != lstFiles && lstFiles.length >0){
				for (File file : lstFiles) {
					try{
						if (file.isFile()) {
							StrFileName = file.getName().toUpperCase();
							if(FilenameUtils.isExtension(StrFileName,PhotoOmniConstants.DAT_FILE_EXT) 
									|| FilenameUtils.isExtension(StrFileName,PhotoOmniConstants.OK_FILE_EXT)) {

								StrFileName = StrFileName.substring(0, StrFileName.lastIndexOf('.'));
								StrFileName =  StrFileName.substring(StrFileName.length() - 15);
								fileNameList = mpPFCList.containsKey(StrFileName) ? mpPFCList.get(StrFileName) : new ArrayList<String>();
								fileNameList.add(file.getName().toUpperCase());
								mpPFCList.put(StrFileName, fileNameList);
							}
						}
					}catch(Exception e){
						LOGGER.error(" Error occoured at PromotionFeedtoCentralTask.main() while checking filetype--->  " + e);
					}
				}
			}else{
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("No files to process or invalid InBound folder.");
				}
			}

			for (Entry<String, List<String>> entry : mpPFCList.entrySet()) {

				ArrayList<String> lstFileSet = (ArrayList<String>) entry.getValue();

				if((lstFileSet.size() == 4) 
						&&(lstFileSet.contains(
								PhotoOmniConstants.AD_EVENT_COUPON+entry.getKey()+PhotoOmniConstants.DOT_DELIMITER+PhotoOmniConstants.OK_FILE_EXT)
								&& lstFileSet.contains(
										PhotoOmniConstants.AD_EVENT_HEADER+entry.getKey()+PhotoOmniConstants.DOT_DELIMITER+PhotoOmniConstants.DAT_FILE_EXT) 
										&& lstFileSet.contains(
												PhotoOmniConstants.AD_EVENT_COUPON+entry.getKey()+PhotoOmniConstants.DOT_DELIMITER+PhotoOmniConstants.DAT_FILE_EXT)
												&& lstFileSet.contains(
														PhotoOmniConstants.AD_EVENT_STORE+entry.getKey()+PhotoOmniConstants.DOT_DELIMITER+PhotoOmniConstants.DAT_FILE_EXT)
								)){

					JobLauncher jobLauncher = null;
					JobParametersBuilder jobBuilder = new JobParametersBuilder();

					startDate = sdf.format(sf.parse(entry.getKey()));
					Date RecvDTTM = sdf.parse(startDate);

					String[] strLstFiles = lstFileSet.toArray(new String[lstFileSet.size()]);
					strLstFileNames = Arrays.toString(strLstFiles);
					strLstFileNames = strLstFileNames.replaceAll("[\\[\\]]","");

					jdbcTemplate = (JdbcTemplate) context.getBean(PhotoOmniConstants.OMNI_JDBC_TEMPLATE);
					iCount = jdbcTemplate.queryForObject(strEventTypeExistsQuery, new Object[] { startDate }, Integer.class);

					if(iCount == 0){
						jobBuilder.addLong(PhotoOmniConstants.JOB_SUBMIT_TIME, new Long(System.currentTimeMillis()));
						jobBuilder.addString("FilePath", entry.getKey());
						jobBuilder.addDate("RecvDTTM", RecvDTTM);
						jobBuilder.addString("ListofFiles", strLstFileNames);
						jobBuilder.addString("strRecvDTTM", startDate);
						jobBuilder.addString("feedLocation", strInboundFeedLocation);
						jobBuilder.addString("CreatedUser", PhotoOmniConstants.DEFUALT_USER_ID);

						JobParameters jobParameters =  jobBuilder.toJobParameters();	
						jobLauncher = (JobLauncher) context.getBean(PhotoOmniConstants.JOB_LAUNCHER);
						Job job = (Job) context.getBean(PhotoOmniConstants.PROCESS_PCS_FEED_JOB);
						execution = jobLauncher.run(job, jobParameters);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Exit Status : " + execution.getStatus());
							LOGGER.debug("Exit Status : " + execution.getExitStatus());
						}
					}else {
						throw new PhotoOmniException("File already processed.");
					}

				}else{
					throw new PhotoOmniException("Ok file not found or the set of feed files are invalid.");
				}
			}
		}
		catch (Exception e) {
			LOGGER.error(" Error occoured at PromotionFeedtoCentralTask.main()--->  " + e);
			System.exit(1);
		}finally{
			if(!CommonUtil.isNull(execution) && !"COMPLETED".equalsIgnoreCase(execution.getStatus().toString())){
				LOGGER.error("Job execution failed for promotionFeedToCentral.");
				System.exit(1);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from PromotionFeedtoCentralTask.main() method");
			}
		}
		System.exit(0);
	}
}
