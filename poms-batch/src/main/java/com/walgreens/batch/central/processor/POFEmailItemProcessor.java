package com.walgreens.batch.central.processor;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.POFEmailDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
/**
 * This class creates the send mail object. 
 * @author CTS
 * @version 1.1 March 16, 2015
 */
public class POFEmailItemProcessor implements ItemProcessor<List<POFEmailDataBean>, MimeMessage>{
	
	/**
     * oMSBOFactory
     */
	@Autowired
	private OMSBOFactory oMSBOFactory;
	/**
     * pofOrderVCRepBean
     */
	POFEmailDataBean pofEmailDataBean;
	/**
	 * LOGGER for Logging
	 */
	
	/**
	 * mailSender
	 */
	@Autowired
	private JavaMailSender mailSender;
	/**
	 * mailSender
	 */
	@Autowired
	private SimpleMailMessage simpleMailMessage;
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(POFEmailItemProcessor.class);
	
	/**
	 * This method call the sendEmail method for sendEmail object creation. 
	 * @param items contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public MimeMessage process(final List<POFEmailDataBean> item) throws PhotoOmniException {
		MimeMessage mimeMessage = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of POFEmailItemProcessor ");
		}
		try {
			StringBuffer emailIdsTO = new StringBuffer();
			StringBuffer emailIdsFrom = new StringBuffer();
			for (POFEmailDataBean pofEmailDataBean : item) {
				if(pofEmailDataBean.getActiveCd() == 1){
					if(pofEmailDataBean.getEmailType().equalsIgnoreCase("TO")){
						emailIdsTO.append(pofEmailDataBean.getEmailId()).append(",");
					}
					if(pofEmailDataBean.getEmailType().equalsIgnoreCase("FROM")){
						emailIdsFrom.append(pofEmailDataBean.getEmailId()).append(",");
					}
				}
			}
			String[] email = emailIdsFrom.toString().split(",");
			String fromEmailId = email[0];
			int indexVal = emailIdsTO.lastIndexOf(",");
			emailIdsTO.deleteCharAt(indexVal);
			
			final OrderUtilBO orderUtilBO = oMSBOFactory.getOrderUtilBO();
			String reportType = PhotoOmniConstants.PAY_ON_FULLFILLMENT;
			mimeMessage = orderUtilBO.getMimeMessage(mailSender,emailIdsTO,fromEmailId,reportType);
			LOGGER.debug(" POF Email Sending Success ");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of POFEmailItemProcessor - " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of POFEmailItemProcessor ");
			}
		}
	    return mimeMessage;
	}

}
