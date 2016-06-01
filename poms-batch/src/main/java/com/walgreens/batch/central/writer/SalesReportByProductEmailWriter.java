package com.walgreens.batch.central.writer;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * 
 * @author CTS
 * 
 */
public class SalesReportByProductEmailWriter implements
		ItemWriter<MimeMessage> {

	private static final Logger logger = LoggerFactory.getLogger(SalesReportByProductEmailWriter.class);
	SalesReportByProductBean salesReportByProductBean;
	
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ReportBOFactory reportBOFactory;
	
	@Override
	public void write(List<? extends MimeMessage> items) throws Exception {
		logger.info(" Entering write method of SalesReportByProductEmailWriter -- >");
		
		try {
			
			 ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
				List<String> fileNames = salesReportByProductBean.getFileNameList();
				String fileLocation = salesReportByProductBean.getFileLocation();
				MimeMessage mimeMessage = items.get(0);
				mailSender.send(mimeMessage);
				reportsUtilBO.deleteFile(fileNames, fileLocation);
		} catch (Exception e) {
			logger.error(" Error occoured at process write of SalesReportByProductEmailWriter ----> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
		
				logger.debug(" Exiting write method of SalesReportByProductEmailWriter -- >");
		
		}
	}
	
	@BeforeStep
	private void retriveValue(StepExecution stepExecution){
		logger.info(" Entering retriveValue method of SalesReportByProductEmailWriter ");
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		salesReportByProductBean = (SalesReportByProductBean) executionContext.get("refDataKey");
	}

}
