package com.walgreens.oms.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.EnvelopeOrderDtlsBean;
import com.walgreens.oms.bean.EnvelopeOrderPromotionBean;
import com.walgreens.oms.bean.EnvelopePopupHeaderBean;
import com.walgreens.oms.bean.EnvelopeProductDtlsBean;

public class EnvelopeDtlsDataRespBean {
	
	private MessageHeader messageHeader;
	private EnvelopeOrderDtlsBean  orderInfo;
	private List<EnvelopeOrderPromotionBean> orderPromotion;
	private List<EnvelopeProductDtlsBean> OrderLineInfo;
	private List<EnvHistoryBean> envHistoryList;
	private EnvelopePopupHeaderBean envelopePopupHeaderBean;
	private String envelopeNbr;
	
	
	
	/**
	 * @return the envelopeNbr
	 */
	public String getEnvelopeNbr() {
		return envelopeNbr;
	}
	/**
	 * @param envelopeNbr the envelopeNbr to set
	 */
	public void setEnvelopeNbr(String envelopeNbr) {
		this.envelopeNbr = envelopeNbr;
	}
	/**
	 * @return the messageHeader
	 */
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	
	/**
	 * @return the orderInfo
	 */
	public EnvelopeOrderDtlsBean getOrderInfo() {
		return orderInfo;
	}
	/**
	 * @param orderInfo the orderInfo to set
	 */
	public void setOrderInfo(EnvelopeOrderDtlsBean orderInfo) {
		this.orderInfo = orderInfo;
	}
	/**
	 * @return the envHistoryList
	 */
	public List<EnvHistoryBean> getEnvHistoryList() {
		return envHistoryList;
	}
	/**
	 * @param envHistoryList the envHistoryList to set
	 */
	public void setEnvHistoryList(List<EnvHistoryBean> envHistoryList) {
		this.envHistoryList = envHistoryList;
	}
	/**
	 * @return the orderPromotion
	 */
	public List<EnvelopeOrderPromotionBean> getOrderPromotion() {
		return orderPromotion;
	}
	/**
	 * @param orderPromotion the orderPromotion to set
	 */
	public void setOrderPromotion(List<EnvelopeOrderPromotionBean> orderPromotion) {
		this.orderPromotion = orderPromotion;
	}
	/**
	 * @return the orderLineInfo
	 */
	public List<EnvelopeProductDtlsBean> getOrderLineInfo() {
		return OrderLineInfo;
	}
	/**
	 * @param orderLineInfo the orderLineInfo to set
	 */
	public void setOrderLineInfo(List<EnvelopeProductDtlsBean> orderLineInfo) {
		OrderLineInfo = orderLineInfo;
	}
	/**
	 * @return the envelopePopupHeaderBean
	 */
	public EnvelopePopupHeaderBean getEnvelopePopupHeaderBean() {
		return envelopePopupHeaderBean;
	}
	/**
	 * @param envelopePopupHeaderBean the envelopePopupHeaderBean to set
	 */
	public void setEnvelopePopupHeaderBean(
			EnvelopePopupHeaderBean envelopePopupHeaderBean) {
		this.envelopePopupHeaderBean = envelopePopupHeaderBean;
	}
	
	
	
}
