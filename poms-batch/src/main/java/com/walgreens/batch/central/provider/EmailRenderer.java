/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.common.exception.PhotoOmniException;
/**
 * <p>
 * 	To get E-mail report data based on reportType and render the same
 * into required context for the report
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
@Component
public class EmailRenderer {

	/**
	 * Variable to load spring context file
	 */
	@Autowired
	private ApplicationContext context;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRenderer.class);

	public EmailReportBean render(EmailReportBean reportBean)
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into EmailRenderer.render() method ");
		}
		try {
			EmailReportDataProvider provider = context.getBean(
					reportBean.getReportName(), EmailReportDataProvider.class);
			String formattedBody = provider.processEmailBody(reportBean);
			reportBean.setProcessedFragment(formattedBody);
		} catch (BeansException e) {
			LOGGER.error(" Error occoured while loading bean for at EmailRenderer.render() method "
					+ reportBean.getReportName() + e);
			throw new PhotoOmniException(e.getMessage());
		}catch (Exception  e) {
			LOGGER.error(" Error occured at mailRenderer.render() method "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		}finally{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from EmailRenderer.render() method ");
			}
		}
		return reportBean;
	}

}
