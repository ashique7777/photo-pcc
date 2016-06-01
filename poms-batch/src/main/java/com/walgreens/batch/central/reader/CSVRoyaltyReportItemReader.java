/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.batch.central.rowmapper.RoyaltySalesReportRowMapper;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	Custom item reader implements Spring itemReader to reader filter criteria
 * for Royalty Report.
 * </p>
 * 
 * <p>
 * This class will read the filter criteria from the adoc reports table based on reportID
 * and populate the same to Royalty report bean for further processing.
 * </p>
 * 
 * {@link CSVRoyaltyReportItemReader} is a business implementation class for {@link ItemReader}
 * This class is used to read the royalty report data
 * from database
 * @author CTS
 * @since v1.0
 */
public class CSVRoyaltyReportItemReader implements ItemReader<RoyaltySalesReportPrefDataBean>{

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ReportBOFactory reportBOFactory;

	private String stepName;

	private Long reportId;

	private  RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;
	
	@SuppressWarnings("unused")
	private long jobSubmitTime;

	int count = 0;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVRoyaltyReportItemReader.class);

	/**
	 * Set the reportId from jobParamters to be used for fetching filter criteria
	 * from the adoc reports table
	 *  
	 * @param reportID
	 */
	public void setReportId(final Long reportId) {
		this.reportId = reportId;
	}
	
	/**
	 * Method to set jobSubmitTime
	 * @return jobSubmitTime
	 */
	public void setJobSubmitTime(long jobSubmitTime) {
		this.jobSubmitTime = jobSubmitTime;
	}
	
	/**
	 * Method to fetch the filter criteria from adoc report and scheduled report table for royalty report based on
	 *  reportId and populate the same to Royalty bean 
	 *  
	 * @return RoyaltySalesReportPrefDataBean -- Contains filter state and reportId 
	 * @throws PhotoOmniException -- Custom exception
	 */
	public RoyaltySalesReportPrefDataBean read() throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into CSVRoyaltyReportItemReader.read() Method");
		}
		royaltySalesReportPrefDataBean = null;
		if(count == 0){
			count++;
			try{
				String reportType;
				final ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
				String reportQuery ;
				if("RoyaltycustomreportJobstep1".equals(stepName)){
					reportQuery = ReportsQuery.getUserPrefQuery(reportId).toString();
					reportType = PhotoOmniConstants.ROYALTY_CUSTOM;
				}else{
					reportQuery = ReportsQuery.getDefaultUserPrefQuery(reportId).toString();
					reportType = PhotoOmniConstants.ROYALTY_MONTHLY;
				}
				royaltySalesReportPrefDataBean = jdbcTemplate.query(reportQuery, new RoyaltySalesReportRowMapper()).get(0);
				if(null != royaltySalesReportPrefDataBean){
					royaltySalesReportPrefDataBean.setReportType(reportType);
					royaltySalesReportPrefDataBean.setFilterMap(reportsUtilBO
							.getfilterParamsForRoyalty(royaltySalesReportPrefDataBean.getFilterState()));
					//Query to fetch group by 
					royaltySalesReportPrefDataBean.setGroupBy(
							jdbcTemplate.queryForObject(ReportsQuery.getRoyaltyGroupByCriteria().toString(), 
									String.class, royaltySalesReportPrefDataBean.getFilterMap().get(PhotoOmniBatchConstants.VENDOR)));
					royaltySalesReportPrefDataBean.setSysUserReportPrefId(reportId);
					if(reportType == PhotoOmniConstants.ROYALTY_MONTHLY){
						/**
						 * If Monthly Report calculate the filter dates
						 */
						generateFilterDates();
					}		
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at CSVRoyaltyReportItemReader.read() Method ----> " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug(" Exiting CSVRoyaltyReportItemReader.read() Method ---> ");
				}
			}
		}
		return royaltySalesReportPrefDataBean;
	}

	/**
	 * Method to get step name which will currently execute
	 * using stepExeuctionContext
	 * 
	 * This method will get executed before the step execution
	 * 
	 * @param stepExecution -- Hold execution context details
	 */
	@BeforeStep
	public void beforeStep(final StepExecution stepExecution) {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug("Entering CSVRoyaltyReportItemReader.beforeStep() Method -- >");
		}
		stepName = stepExecution.getStepName();
	}

	/**
	 * Method to calculate filter dates i.e startDate and endDate for scheduled report 
	 * based on report type and set the same into royalty data bean
	 * 
	 * This method will set the royalty report bean to null if not able to generate report dates
	 * and the report generation will end
	 * 
	 * @throws PhotoOmniException- This will throw custom exception
	 */

	private void generateFilterDates() throws PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug("Entering into CSVRoyaltyReportItemReader.generateFilterDates() method");
		}
		
		String reportType;
		final Map<String , Object> filterMap = royaltySalesReportPrefDataBean.getFilterMap();
			try{
				final SimpleDateFormat format = new SimpleDateFormat(PhotoOmniBatchConstants.DATE_FORMAT_SIX);
				final Map<String, Object> resultMap = jdbcTemplate.queryForMap(
						ReportsQuery.getVendorAttributes().toString(), (String)filterMap.get(PhotoOmniBatchConstants.VENDOR));
				
				if(null !=resultMap.get(PhotoOmniBatchConstants.ROYALTY_SALES_GEN_TYPE) && 
						resultMap.containsKey(PhotoOmniBatchConstants.ROYALTY_SALES_GEN_TYPE)){
					reportType = resultMap.get(PhotoOmniBatchConstants.ROYALTY_SALES_GEN_TYPE).toString();
					if(PhotoOmniBatchConstants.DAILY.equals(reportType)){
						calculateDailyReportDates(filterMap, format);
					}else if(PhotoOmniBatchConstants.WEEKLY.equals(reportType) && null != resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GENERATION_DAY)){
						calculateWeeklyReportDates(filterMap,format, 
								((BigDecimal)resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GENERATION_DAY)).intValue());
					}else if(PhotoOmniBatchConstants.MONTHLY.equals(reportType) && null != resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GENERATION_DAY)){
						calculateMonthlyReportDates(filterMap,format, 
								((BigDecimal)resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GENERATION_DAY)).intValue());
					}else if(PhotoOmniBatchConstants.QUATERLY.equals(reportType) && null != resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GENERATION_DAY)){
						calculateQUraterlyReportDates(filterMap,format, 
								((BigDecimal)resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GENERATION_DAY)).intValue(),
								(resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GEN_QUARTER)));
					}else if(PhotoOmniBatchConstants.YEARLY.equals(reportType) && null != resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GEN_MONTH)){
						calculateYearlyReportDates(filterMap,format, 
								((BigDecimal)resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GENERATION_DAY)).intValue(),
								resultMap.get(PhotoOmniBatchConstants.ROYALTY_REPORT_GEN_MONTH));
					}else {
						LOGGER.error("Report generation details are not maching");
						royaltySalesReportPrefDataBean = null;
					}
				}
			}catch(Exception e){
				LOGGER.error("Exception occured in CSVRoyaltyReportItemReader.generateFilterDates() method --- > " +e);
				throw new PhotoOmniException(e.getMessage());
			}finally{
				if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from CSVRoyaltyReportItemReader.generateFilterDates() method-- >");
				}
			}
		}
	/**
	 * Method to calculate dates for daily report
	 * 
	 * @param filterMap -- Map containing filter details
	 * @param format -- Date format
	 */
	private void calculateDailyReportDates(
			Map<String , Object> filterMap, final SimpleDateFormat format){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into CSVRoyaltyReportItemReader.calculateDailyReportDates() method-- >");
		}
		final Calendar calendarStart = Calendar.getInstance();
		final Calendar calendarEnd=Calendar.getInstance();
		calendarStart.add(Calendar.DATE, -1);
		calendarEnd.add(Calendar.DATE, 0);
		filterMap.put(PhotoOmniBatchConstants.START_DATE, format.format(calendarStart.getTime()));
		filterMap.put(PhotoOmniBatchConstants.END_DATE, format.format(calendarEnd.getTime()));
	}
	
	/**
	 * Method to calculate dates for weekly report
	 * 
	 * @param filterMap -- Map containing filter details
	 * @param format  -- Date format
	 * @param generationDay -- Day of the week on which report should be generated
	 */
	private void calculateWeeklyReportDates(
			Map<String , Object> filterMap, SimpleDateFormat format, int generationDay){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into CSVRoyaltyReportItemReader.calculateWeeklyReportDates() method-- >");
		}
		final Calendar calendarStart = Calendar.getInstance();
		final Calendar calendarEnd=Calendar.getInstance();
		calendarEnd.add(Calendar.DATE,0);
		calendarEnd.setTime(calendarEnd.getTime());
		if(calendarEnd.get(Calendar.DAY_OF_WEEK) == generationDay){
			calendarStart.add(Calendar.DATE, -7);
			filterMap.put(PhotoOmniBatchConstants.START_DATE, format.format(calendarStart.getTime()));
			filterMap.put(PhotoOmniBatchConstants.END_DATE, format.format(calendarEnd.getTime()));
		}else{
			LOGGER.error("Generation date is not matching");
			royaltySalesReportPrefDataBean = null;
		}
	}
	
	/**
	 * Method to calculate dates for monthly report
	 * 
	 * @param filterMap -- Map containing filter details
	 * @param format  -- Date format
	 * @param generationDay -- Day of the week on which report should be generated
	 */
	private void calculateMonthlyReportDates(
			Map<String , Object> filterMap, SimpleDateFormat format, int generationDay){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into CSVRoyaltyReportItemReader.calculateMonthlyReportDates() method-- >");
		}
		final Calendar calendarStart = Calendar.getInstance();
		final Calendar calendarEnd=Calendar.getInstance();
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,0);
		cal.setTime(cal.getTime());
		if( cal.get(Calendar.DAY_OF_MONTH) == generationDay){
			calendarStart.add(Calendar.MONTH, -1);
			calendarEnd.add(Calendar.MONTH, 0);
			filterMap.put(PhotoOmniBatchConstants.START_DATE, format.format(calendarStart.getTime()));
			filterMap.put(PhotoOmniBatchConstants.END_DATE, format.format(calendarEnd.getTime()));
		}else{
			LOGGER.error("Generation date is not matching");
			royaltySalesReportPrefDataBean = null;
		}
	}
	
	/**
	 * Method to calculate dates for quarterly report
	 * @param filterMap -- Map containing filter details
	 * @param format -- Date format
	 * @param generationDay -- Day of the week on which report should be generated
	 * @param generationQurater -- Generation quarter details
	 */
	private void calculateQUraterlyReportDates(
			Map<String , Object> filterMap, SimpleDateFormat format, int generationDay, Object generationQurater){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into CSVRoyaltyReportItemReader.calculateQUraterlyReportDates() method-- >");
		}
		String month = null;
		final Calendar calendarStart = Calendar.getInstance();
		final Calendar calendarEnd=Calendar.getInstance();
		final Calendar cal = Calendar.getInstance();
		int year = 0;
		final List<String> quaterList = Arrays.asList(generationQurater.toString().split(","));
		cal.add(Calendar.DATE,0);
		cal.setTime(cal.getTime());
		final String  monthString = cal.get(Calendar.MONTH)+ 1+ PhotoOmniBatchConstants.EMPTY_SPACE_CHAR;
		for(int i = 0 ; i<quaterList.size() ; i++){
			if(monthString.equals(quaterList.get(i)) && cal.get(Calendar.DAY_OF_MONTH) == generationDay){
				if(i!= 0){
					month = quaterList.get(i-1);
					year = Calendar.getInstance().get(Calendar.YEAR);
				}else{
					month = quaterList.get(quaterList.size() -1);
					year = Calendar.getInstance().get(Calendar.YEAR) -1;
				}
				break;
			}
		}
		if(null != month){
			calendarStart.set(Calendar.YEAR,year);
			calendarStart.set(Calendar.MONTH,(Integer.valueOf(month)-1));
			calendarStart.set(Calendar.DAY_OF_MONTH,cal.get(Calendar.DAY_OF_MONTH));
			filterMap.put(PhotoOmniBatchConstants.START_DATE, format.format(calendarStart.getTime()));
			calendarEnd.set(Calendar.YEAR,year);
			calendarEnd.set(Calendar.MONTH,(Integer.valueOf(monthString)-1));
			calendarEnd.set(Calendar.DAY_OF_MONTH,generationDay);
			filterMap.put(PhotoOmniBatchConstants.END_DATE, format.format(calendarEnd.getTime()));
		}
		else{
			LOGGER.error("Generation date or Quater is not matching");
			royaltySalesReportPrefDataBean = null;
		}
	}
	
	/**
	 * Method to calculate yearly report dates
	 * 
	 * @param filterMap -- Map containing filter details
	 * @param format -- Map containing filter details
	 * @param generationDay -- Day of the week on which report should be generated
	 * @param generationMonth - Generation month details
	 */
	private void calculateYearlyReportDates(
			Map<String , Object> filterMap, SimpleDateFormat format, int generationDay,  Object generationMonth){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into CSVRoyaltyReportItemReader.calculateYearlyReportDates() method-- >");
		}
		if(null !=generationMonth){
			final Calendar calendarStart = Calendar.getInstance();
			final Calendar calendarEnd=Calendar.getInstance();
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,0);
			cal.setTime(cal.getTime());

			if(cal.get(Calendar.MONTH) == (((BigDecimal)generationMonth)
					.intValue() -1) &&  cal.get(Calendar.DAY_OF_MONTH) == 
					generationDay){
				calendarStart.add(Calendar.YEAR , -1);
				calendarEnd.add(Calendar.YEAR , 0);
				filterMap.put(PhotoOmniBatchConstants.START_DATE, format.format(calendarStart.getTime()));
				filterMap.put(PhotoOmniBatchConstants.END_DATE, format.format(calendarEnd.getTime()));
			}else{
				LOGGER.error("Generation date/month is not matching");
				royaltySalesReportPrefDataBean = null;
			}
		}
	}
}

