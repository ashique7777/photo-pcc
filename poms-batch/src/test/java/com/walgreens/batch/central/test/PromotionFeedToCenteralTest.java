/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.walgreens.batch.central.bean.PromotionCouponsDataBean;
import com.walgreens.batch.central.bean.PromotionHeaderDataBean;
import com.walgreens.batch.central.bean.PromotionStoresDataBean;
import com.walgreens.batch.central.processor.PromoCouponsFileProcessor;
import com.walgreens.batch.central.processor.PromoHeaderFileProcessor;
import com.walgreens.batch.central.processor.PromoStoresFileProcessor;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * A J-unit test class to test promotion feed to central task
 * 
 * This call will test each module individually and validate response
 * </p>
 * 
 * <p>
 * {@link PromotionFeedToCenteralTest} is a test class
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
@ContextConfiguration(locations = {
		"classpath*:**/spring/batch/config/database.xml",
		"classpath*:**/spring/batch/config/context.xml",
		"classpath*:**/spring/batch/jobs/test-job-report.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		StepScopeTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class PromotionFeedToCenteralTest {

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PromotionFeedToCenteralTest.class);

	@Autowired
	@Qualifier("promotionTestUtils")
	private JobLauncherTestUtils jobLauncherTest;

	/**
	 * jdbcTemplate
	 */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Value( "${promotion.inbound.feed.location}" )
	private  String INBOUND_URL;
	@Value( "${promotion.archival.feed.location}" )
	private  String ARCHIVE_URL ;

	/**
	 * <P>
	 * J-unit test method to test PromoHeaderFileProcessor success scenario
	 * </P>
	 * 
	 * <P>
	 * Here received date will be sent as a parameter the same will be validated
	 * with the response
	 */
	@Test
	public void testPromoHeaderFileProcessorSuccess() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoHeaderFileProcessorSuccess()");

		final Date receivedDate = new Date();
		final PromotionHeaderDataBean promotionHeaderDataBeanRequest = new PromotionHeaderDataBean();
		final PromoHeaderFileProcessor promoHeaderFileProcessor = new PromoHeaderFileProcessor();

		promotionHeaderDataBeanRequest.setAdvEventType("A");
		promotionHeaderDataBeanRequest.setAdvEventSeqNbr("R962");
		promotionHeaderDataBeanRequest.setAdEventDateStmp(1150811);
		promotionHeaderDataBeanRequest.setAdEventTimeStmp(40655);
		promotionHeaderDataBeanRequest.setAdEventUserStmp("ADVTEST01");
		promotionHeaderDataBeanRequest.setAdEventRelInd("D");

		promoHeaderFileProcessor.setJobSubmitTime(receivedDate.getTime());
		promoHeaderFileProcessor.setRecvDTTM(receivedDate);
		try {
			final PromotionHeaderDataBean promotionHeaderDataBeanResponse = promoHeaderFileProcessor
					.process(promotionHeaderDataBeanRequest);
			assertEquals(
					"Failed to set received date in header file processor",
					promotionHeaderDataBeanResponse.getRecvDTTM(), receivedDate);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Excetion occured  atPromotionFeedToCenteralTest.testPromoHeaderFileProcessorSuccess() --->  "
					+ e.getMessage());
		}
	}

	/**
	 * <P>
	 * J-unit test method to test PromoHeaderFileProcessor failure scenario
	 * </P>
	 * 
	 * @throws PhotoOmniException
	 */
	@Test(expected = PhotoOmniException.class)
	public void testPromoHeaderFileProcessorFailure() throws PhotoOmniException {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoHeaderFileProcessorFailure()");

		final Date receivedDate = new Date();
		final PromoHeaderFileProcessor promoHeaderFileProcessor = new PromoHeaderFileProcessor();

		promoHeaderFileProcessor.setJobSubmitTime(receivedDate.getTime());
		promoHeaderFileProcessor.setRecvDTTM(receivedDate);
		try {
			promoHeaderFileProcessor.process(null);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Excetion occured  atPromotionFeedToCenteralTest.testPromoHeaderFileProcessorFailure() --->  "
					+ e.getMessage());
			throw e;
		}
	}

	/**
	 * <P>
	 * J-unit test method to test PromoCouponFileProcessor success scenario
	 * </P>
	 * 
	 * <P>
	 * Here received date will be sent as a parameter the same will be validated
	 * with the response
	 */
	@Test
	public void testPromoCouponsFileProcessor() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoCouponsFileProcessor()");

		final Date receivedDate = new Date();
		final PromotionCouponsDataBean promotionCouponsDataBean = new PromotionCouponsDataBean();
		promotionCouponsDataBean.setAdvEventType("A");
		promotionCouponsDataBean.setAdvEventSeqNbr("R962");
		promotionCouponsDataBean.setAdvPrintVerNbr(1);
		promotionCouponsDataBean.setItemNumber(100034);
		promotionCouponsDataBean.setAdvItemCouponNbr(6028);
		promotionCouponsDataBean.setAdvEvVerStartDate(1150728);
		promotionCouponsDataBean.setAdvEvVerEndDate(1150828);
		promotionCouponsDataBean.setAdvEvVerStatus("A");

		final PromoCouponsFileProcessor promoCouponsFileProcessor = new PromoCouponsFileProcessor();
		promoCouponsFileProcessor.setRecvDTTM(receivedDate);
		try {
			final PromotionCouponsDataBean promotionCouponsDataBeanResponse = promoCouponsFileProcessor
					.process(promotionCouponsDataBean);
			assertEquals(
					"Failed to set received date in coupons file processor",
					promotionCouponsDataBeanResponse.getRecvDTTM(),
					receivedDate);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoCouponsFileProcessor()--->  "
					+ e.getMessage());
		}
	}

	/**
	 * <P>
	 * J-unit test method to test PromoCouponFileProcessor failure scenario
	 * </P>
	 * 
	 * @throws PhotoOmniException
	 */
	@Test(expected = PhotoOmniException.class)
	public void testPromoCouponFileProcessorFailure() throws PhotoOmniException {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoCouponFileProcessorFailure()");

		final Date receivedDate = new Date();
		final PromoCouponsFileProcessor promoCouponsFileProcessor = new PromoCouponsFileProcessor();
		try {
			promoCouponsFileProcessor.setRecvDTTM(receivedDate);
			promoCouponsFileProcessor.process(null);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Excetion occured  atPromotionFeedToCenteralTest.testPromoCouponFileProcessorFailure() --->  "
					+ e.getMessage());
			throw e;
		}
	}

	/**
	 * <P>
	 * J-unit test method to test testPromoStoreFileProcessor business login
	 * </P>
	 */
	@Test
	public void testPromoStoreFileProcessor() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoStoreFileProcessor()");

		final Date receivedDate = new Date();
		final PromotionStoresDataBean promotionStoresDataBean = new PromotionStoresDataBean();
		promotionStoresDataBean.setAdvEventType("A");
		promotionStoresDataBean.setAdvEventSeqNbr("R911");
		promotionStoresDataBean.setAdvPrintVerNbr(1);
		promotionStoresDataBean.setAdvLocationNbr(1139);
		promotionStoresDataBean.setLocationType("S");

		final PromoStoresFileProcessor promoStoresFileProcessor = new PromoStoresFileProcessor();
		promoStoresFileProcessor.setRecvDTTM(receivedDate);
		try {
			final PromotionStoresDataBean promotionStoresDataBeanResponse = promoStoresFileProcessor
					.process(promotionStoresDataBean);
			assertEquals("Failed to set received date in store file processor",
					promotionStoresDataBeanResponse.getRecvDTTM(), receivedDate);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoStoreFileProcessor()--->  "
					+ e.getMessage());
		}
	}

	/**
	 * <P>
	 * J-unit test method to test PromoStoreFileProcessor failure scenario
	 * </P>
	 * 
	 * @throws PhotoOmniException
	 */
	@Test(expected = PhotoOmniException.class)
	public void testPromoStoreFileProcessorFailure() throws PhotoOmniException {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoStoreFileProcessorFailure()");

		final Date receivedDate = new Date();
		final PromoStoresFileProcessor promoStoresFileProcessor = new PromoStoresFileProcessor();
		try {
			promoStoresFileProcessor.setRecvDTTM(receivedDate);
			promoStoresFileProcessor.process(null);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Excetion occured  atPromotionFeedToCenteralTest.testPromoStoreFileProcessorFailure() --->  "
					+ e.getMessage());
			throw e;
		}
	}
	
	/**
	 * <P>
	 * J-unit test method to test Promotion feed to central step 1 This method
	 * will inject the file name from which the data should be read and
	 * processed
	 * </P>
	 * 
	 * </p> This is a success scenario where a proper file URL will be set as a
	 * resource URL </p>.
	 */
	@Test
	public void testPromoFeedStep1Success() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoFeedStep1Success()");
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
			final Date receivedDate = sdf.parse("01-Jan-1947 12:00:00");
			final JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString("FilePath", "20150806_004821");

			final File url = new File(this.INBOUND_URL);
			jobBuilder.addString("feedLocation", url.getAbsolutePath());
			jobBuilder.addDate("RecvDTTM", receivedDate);
			jobBuilder.addLong("jobSubmitTime",
					Long.valueOf(System.currentTimeMillis()));
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobExecution jobExecution = jobLauncherTest.launchStep(
					"ProcessPCSFeedJobstep1", jobParameters);
			// Check if job completed successfully
			assertEquals("Step1 Job Execution Failed", BatchStatus.COMPLETED.name(),
					jobExecution.getExitStatus().getExitCode());
			final String countQuery = "SELECT COUNT(1) FROM OM_AD_EVENT_HEADER_STG WHERE FEEDFILE_RCVD_DTTM = ? ";
			final int count = jdbcTemplate.queryForObject(countQuery,
					Integer.class, receivedDate);
			// Check count of data inserted
			assertEquals(
					"Data present in the feed file and inserted into DB are not equal",
					count, 1);
			final String deleteQuery = "DELETE FROM OM_AD_EVENT_HEADER_STG WHERE FEEDFILE_RCVD_DTTM = ? ";
			// deleted data inserted after test
			jdbcTemplate.update(deleteQuery, receivedDate);
		} catch (Exception e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoFeedStep1Success()--->  "
					+ e.getMessage());
		}
	}

	
	/**
	 * <P>
	 * J-unit test method to test Promotion feed to central step 1 This method
	 * will inject the file name from which the data should be read and
	 * processed
	 * </P>
	 * 
	 * </p> This is a failure scenario where a invalid file URL will be set as a
	 * resource URL </p>
	 */
	@Test
	public void testPromoFeedStep1Failure() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoFeedStep1Failure()");
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
			final Date receivedDate = sdf.parse("01-Jan-1947 12:00:00");
			final JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString("FilePath", "20150806_004821");

			final File url = new File(this.ARCHIVE_URL);
			jobBuilder.addString("feedLocation", url.getAbsolutePath());
			jobBuilder.addDate("RecvDTTM", receivedDate);
			jobBuilder.addLong("jobSubmitTime",
					Long.valueOf(System.currentTimeMillis()));
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobExecution jobExecution = jobLauncherTest.launchStep(
					"ProcessPCSFeedJobstep1", jobParameters);
			// Check if job completed successfully
			assertEquals("Step1 Job Execution ", BatchStatus.FAILED.name(),
					jobExecution.getExitStatus().getExitCode());
		} catch (Exception e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoFeedStep1Failure()--->  "
					+ e.getMessage());
		}
	}
	/**
	 * <P>
	 * J-unit test method to test Promotion feed to central step 1 This method
	 * will inject the file name from which the data should be read and
	 * processed
	 * </P>
	 * 
	 * </p> This is a success scenario where a proper file URL will be set as a
	 * resource URL </p>
	 */
	@Test
	public void testPromoFeedStep2Success() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoFeedStep2Success()");
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
			final Date receivedDate = sdf.parse("01-Jan-1947 12:00:00");
			final JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString("FilePath", "20150806_004821");

			final File url = new File(this.INBOUND_URL);
			jobBuilder.addString("feedLocation", url.getAbsolutePath());
			jobBuilder.addDate("RecvDTTM", receivedDate);
			jobBuilder.addLong("jobSubmitTime",
					Long.valueOf(System.currentTimeMillis()));
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobExecution jobExecution = jobLauncherTest.launchStep(
					"ProcessPCSFeedJobstep2", jobParameters);
			// Check if job completed successfully
			assertEquals("Step2 Job Execution Failed", BatchStatus.COMPLETED.name(),
					jobExecution.getExitStatus().getExitCode());
			final String countQuery = "SELECT COUNT(1) FROM OM_AD_EVENT_COUPONS_STG WHERE FEEDFILE_RCVD_DTTM = ? ";
			final int count = jdbcTemplate.queryForObject(countQuery,
					Integer.class, receivedDate);
			// Check count of data inserted
			assertEquals(
					"Data present in the feed file and inserted into DB are not equal",
					count, 4);
			final String deleteQuery = "DELETE FROM OM_AD_EVENT_COUPONS_STG WHERE FEEDFILE_RCVD_DTTM = ? ";
			// deleted data inserted after test
			jdbcTemplate.update(deleteQuery, receivedDate);
		} catch (Exception e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoFeedStep2Success()--->  "
					+ e.getMessage());
		}
	}

	/**
	 * <P>
	 * J-unit test method to test Promotion feed to central step 1 This method
	 * will inject the file name from which the data should be read and
	 * processed
	 * </P>
	 * 
	 * </p> This is a failure scenario where a proper file URL will be set as a
	 * resource URL </p>
	 */
	 @Test
	public void testPromoFeedStep2Failure() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoFeedStep2Failure()");
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
			final Date receivedDate = sdf.parse("01-Jan-1947 12:00:00");
			final JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString("FilePath", "20150806_004821");

			final File url = new File(this.ARCHIVE_URL);
			jobBuilder.addString("feedLocation", url.getAbsolutePath());
			jobBuilder.addDate("RecvDTTM", receivedDate);
			jobBuilder.addLong("jobSubmitTime",
					Long.valueOf(System.currentTimeMillis()));
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobExecution jobExecution = jobLauncherTest.launchStep(
					"ProcessPCSFeedJobstep2", jobParameters);
			// Check if job completed successfully
			assertEquals("Step2 Job Execution ", BatchStatus.FAILED.name(),
					jobExecution.getExitStatus().getExitCode());
		} catch (Exception e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoFeedStep2Failure()--->  "
					+ e.getMessage());
		}
	}
	 
	/**
	 * <P>
	 * J-unit test method to test Promotion feed to central step 1 This method
	 * will inject the file name from which the data should be read and
	 * processed
	 * </P>
	 * 
	 * </p> This is a success scenario where a proper file URL will be set as a
	 * resource URL </p>
	 */
	@Test
	public void testPromoFeedStep3Success() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoFeedStep3Success()");
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
			final Date receivedDate = sdf.parse("01-Jan-1947 12:00:00");
			final JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString("FilePath", "20150806_004821");

			final File url = new File(this.INBOUND_URL);
			jobBuilder.addString("feedLocation", url.getAbsolutePath());
			jobBuilder.addDate("RecvDTTM", receivedDate);
			jobBuilder.addLong("jobSubmitTime",
					Long.valueOf(System.currentTimeMillis()));
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobExecution jobExecution = jobLauncherTest.launchStep(
					"ProcessPCSFeedJobstep3", jobParameters);
			// Check if job completed successfully
			assertEquals("Step3 Job Execution Failed", BatchStatus.COMPLETED.name(),
					jobExecution.getExitStatus().getExitCode());
			final String countQuery = "SELECT COUNT(1) FROM OM_AD_EVENT_STORES_STG WHERE FEEDFILE_RCVD_DTTM = ? ";
			final int count = jdbcTemplate.queryForObject(countQuery,
					Integer.class, receivedDate);
			// Check count of data inserted
			assertEquals(
					"Data present in the feed file and inserted into DB are not equal",
					count, 5);
			final String deleteQuery = "DELETE FROM OM_AD_EVENT_STORES_STG WHERE FEEDFILE_RCVD_DTTM = ? ";
			// deleted data inserted after test
			jdbcTemplate.update(deleteQuery, receivedDate);
		} catch (Exception e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoFeedStep3Success()--->  "
					+ e.getMessage());
		}
	}
	
	/**
	 * <P>
	 * J-unit test method to test Promotion feed to central step 1 This method
	 * will inject the file name from which the data should be read and
	 * processed
	 * </P>
	 * 
	 * </p> This is a failure scenario where a proper file URL will be set as a
	 * resource URL </p>
	 */
	@Test
	public void testPromoFeedStep3Failure() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoFeedStep3Failure()");
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
			final Date receivedDate = sdf.parse("01-Jan-1947 12:00:00");
			final JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString("FilePath", "20150806_004821");

			final File url = new File(this.ARCHIVE_URL);
			jobBuilder.addString("feedLocation", url.getAbsolutePath());
			jobBuilder.addDate("RecvDTTM", receivedDate);
			jobBuilder.addLong("jobSubmitTime",
					Long.valueOf(System.currentTimeMillis()));
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobExecution jobExecution = jobLauncherTest.launchStep(
					"ProcessPCSFeedJobstep3", jobParameters);
			// Check if job completed successfully
			assertEquals("Step3 Job Execution Failed", BatchStatus.FAILED.name(),
					jobExecution.getExitStatus().getExitCode());
		} catch (Exception e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoFeedStep3Failure()--->  "
					+ e.getMessage());
		}
	}

	/**
	 * jobBuilder.addString("CreatedUser", PhotoOmniConstants.DEFUALT_USER_ID);
	 * J-unit test case to test promotion feed to central 
	 * job
	 */
	@Test
	public void testPromoFeedjobSucess() {
		LOGGER.info("Entering into PromotionFeedToCenteralTest.testPromoFeedjobSucess()");
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
			final Date receivedDate = sdf.parse("01-Jan-1947 12:00:00");
			final JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString("FilePath", "20150806_004821");

			final File url = new File(this.INBOUND_URL);
			jobBuilder.addString("feedLocation", url.getAbsolutePath());
			jobBuilder.addDate("RecvDTTM", receivedDate);
			jobBuilder.addLong("jobSubmitTime",
					Long.valueOf(System.currentTimeMillis()));
			jobBuilder.addString("CreatedUser", "Test");
			jobBuilder.addString("strRecvDTTM", "01-Jan-1947 12:00:00");
			String strLstFileNames = "AD_EVENT_COUPON_20150806_004821.DAT,AD_EVENT_COUPON_20150806_004821.OK,"
					+ "AD_EVENT_HEADER_20150806_004821.DAT,AD_EVENT_STORE_20150806_004821.DAT";
			jobBuilder.addString("ListofFiles", strLstFileNames);
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobExecution jobExecution = jobLauncherTest.launchJob(jobParameters);

			assertEquals("Step3 Job Execution Failed", BatchStatus.COMPLETED.name(),
					jobExecution.getExitStatus().getExitCode());
			// Move the files from arhcival to inbound folder
			for(String fileName :strLstFileNames.split(PhotoOmniConstants.DELIMITER)){
			File f1 = new File(this.ARCHIVE_URL+"\\"+fileName.trim());
			 f1.renameTo(new File(this.INBOUND_URL+"\\"+fileName.trim()));
			}
			deleteTempData(receivedDate);
		} catch (Exception e) {
			LOGGER.error(" Excetion occured  at PromotionFeedToCenteralTest.testPromoFeedjobSucess()--->  "
					+ e.getMessage());
		}
	}
	
	private void  deleteTempData(Date receivedDate){
		LOGGER.info("Entering into PromotionFeedToCenteralTest.deleteTempData()");
		
		String deleteQuery = "DELETE FROM OM_AD_EVENT_HEADER_STG WHERE FEEDFILE_RCVD_DTTM = ? ";
		jdbcTemplate.update(deleteQuery, receivedDate);
		 deleteQuery = "DELETE FROM OM_AD_EVENT_COUPONS_STG WHERE FEEDFILE_RCVD_DTTM = ? ";
		jdbcTemplate.update(deleteQuery, receivedDate);
		 deleteQuery = "DELETE FROM OM_AD_EVENT_STORES_STG WHERE FEEDFILE_RCVD_DTTM = ?";
		jdbcTemplate.update(deleteQuery, receivedDate);
		 deleteQuery = "DELETE FROM OM_STORE_PROMOTION_ASSOC WHERE CREATE_USER_ID = 'Test'";
			jdbcTemplate.update(deleteQuery);
		LOGGER.info("Temp data deleted sucessfully");
	}
}

