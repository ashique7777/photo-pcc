package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.mail.SendMailService;
import com.walgreens.common.exception.PhotoOmniException;

public class RoyaltySalesReportEmailCustomWriter  implements ItemWriter<SendMailService>{

	private RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;
	private static final Logger logger = LoggerFactory.getLogger(RoyaltySalesReportEmailCustomWriter.class);
	@Override
	public void write(List<? extends SendMailService> items) throws Exception {
		try {
			SendMailService objSendMailService = items.get(0);
			objSendMailService.sendMail(royaltySalesReportPrefDataBean, PhotoOmniBatchConstants.ROYALTY_CUSTOM);
		} catch (Exception e) {
			logger.error(" Error occoured at process write of RoyaltySalesReportEmailCustomWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(" Exiting write method of RoyaltySalesReportEmailCustomWriter ");
			}
		}
	}
	
	
	@BeforeStep
	private void retriveValue(StepExecution stepExecution){
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		royaltySalesReportPrefDataBean = (RoyaltySalesReportPrefDataBean) executionContext.get("refDataKey");
	}

}
