/**
 * 
 */
package com.walgreens.oms.utility;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.oms.bean.LCDataBean;
import com.walgreens.oms.bo.OrderBOImpl;
import com.walgreens.oms.json.bean.OrderDataRequest;
import com.walgreens.oms.json.bean.OrderDataResponse;
import com.walgreens.oms.json.bean.OrderDetails;
import com.walgreens.oms.json.bean.OrderDetailsList;
import com.walgreens.oms.json.bean.PosOrderRequest;

/**
 * @author CTS
 * 
 */
public class ServiceUtil {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderBOImpl.class);

	/**
	 * This method is used for convert json string to request bean
	 * 
	 * @param strJson
	 * @return
	 */
	public static OrderDataRequest getOrderRequestJson(String strJson) {

		// boolean isValid = CommonUtil.validateJsonSchema(strJson, jsonSchema);
		boolean isValid = true;
		OrderDataRequest jsonRequest = null;
		ObjectMapper objectMapper = new ObjectMapper();

		if (isValid) {
			try {
				jsonRequest = objectMapper.readValue(strJson,
						OrderDataRequest.class);
			} catch (JsonParseException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Json Parsing Exception Occure..");
				}
				e.printStackTrace();
			} catch (JsonMappingException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Json Mapping Exception Occure..");
				}
				e.printStackTrace();
			} catch (IOException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Json IO  Exception Occure..");
				}
				e.printStackTrace();
			}
		} else {
			jsonRequest = null;
		}

		return jsonRequest;

	}

	/**
	 * This method is used for populate Order Details in Response Bean
	 * 
	 * @param orderDtlsBean
	 * @param orderId
	 * @param locationNo
	 */
	public static void createOrderDtl(OrderDetailsList orderDtlsBean,
			String orderId, String locationNo) {
		OrderDetails orderDtl = new OrderDetails();
		orderDtl.setOrderId(orderId);
		orderDtl.setLocationNumber(locationNo);
		orderDtlsBean.setOrderDetails(orderDtl);

	}

	/**
	 * This method is used for populate Error Details in Response Bean
	 * 
	 * @param orderDtlsBean
	 */
	public static void createFailureMessage(OrderDetailsList orderDtlsBean) {
		ErrorDetails errorDtl = new ErrorDetails();
		errorDtl.setErrorCode(PhotoOmniConstants.ERROR_CODE);
		errorDtl.setErrorString(PhotoOmniConstants.ERROR_STRING);
		errorDtl.setErrorSource(PhotoOmniConstants.ERROR_ACTOR);
		errorDtl.setDescription(PhotoOmniConstants.ERROR_DESCRIPTION);
		orderDtlsBean.setErrorDetails(errorDtl);
	}

	public static OrderDataResponse getOrderResponseJson() {
		OrderDataResponse jsonResponse = new OrderDataResponse();
		return jsonResponse;

	}


	/**
	 * method is used to populate PosOrderRequest bean with posJsonRequestMsg
	 * 
	 * @param posJsonRequestMsg
	 * @return PosOrderRequest
	 */
	public PosOrderRequest getPosOrderRequest(String posJsonRequestMsg) {

		LOGGER.debug("Entering ServiceUtil getPosOrderRequest()");
		/*
		 * InputStream is = getClass().getClassLoader().getResourceAsStream(
		 * "schema/PosRequestSchema.txt"); BufferedReader br = new
		 * BufferedReader(new InputStreamReader(is)); StringBuffer lineBuffer =
		 * new StringBuffer(); try { String line = ""; while ((line =
		 * br.readLine()) != null) { lineBuffer.append(line); } } catch
		 * (IOException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } finally { try { br.close(); } catch
		 * (IOException e) { e.printStackTrace(); } } String jsonSchema =
		 * lineBuffer.toString(); Validation of Jeson message request against
		 * Json schenma boolean isValid =
		 * CommonUtil.validateJsonSchema(posJsonRequestMsg, jsonSchema);
		 */
		PosOrderRequest jsonRequest = new PosOrderRequest();
		ObjectMapper objectMapper = new ObjectMapper();

		if (posJsonRequestMsg != null) {
			try {

				jsonRequest = objectMapper.readValue(posJsonRequestMsg,
						PosOrderRequest.class);

			} catch (JsonParseException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Json Parsing Exception Occure..");
				}
				e.printStackTrace();
			} catch (JsonMappingException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("Json Mapping Exception Occure..");
				}
				e.printStackTrace();
			} catch (IOException e) {
				LOGGER.error("Json IO  Exception Occure..");
				e.printStackTrace();
			}
		} else {
			jsonRequest = null;
		}

		LOGGER.debug("Exiting ServiceUtil getPosOrderRequest()");
		return jsonRequest;

	}

	/**
	 * @param posOrderRequest
	 * @return PosOrderResponse
	 */
	public static ErrorDetails createSuccessMessage() {

		LOGGER.debug("Entering ServiceUtil createSuccessMessage");

		ErrorDetails errorDtl = new ErrorDetails();
		errorDtl.setErrorCode("");
		errorDtl.setErrorString("");
		errorDtl.setErrorSource("");
		errorDtl.setDescription("");

		LOGGER.debug("Entering ServiceUtil createSuccessMessage");

		return errorDtl;
	}

	/**
	 * @param posOrderRequest
	 * @return PosOrderResponse
	 */
	public static ErrorDetails createFailureMessage() {

		LOGGER.debug("Entering ServiceUtil createFailureMessage");

		ErrorDetails errorDtl = new ErrorDetails();
		errorDtl.setErrorCode(PhotoOmniConstants.ERROR_CODE);
		errorDtl.setErrorString(PhotoOmniConstants.ERROR_STRING);
		errorDtl.setErrorSource(PhotoOmniConstants.ERROR_ACTOR);
		errorDtl.setDescription(PhotoOmniConstants.ERROR_DESCRIPTION);

		LOGGER.debug("Exiting ServiceUtil createFailureMessage");

		return errorDtl;

	}
	/**
	 * @param posOrderRequest
	 * @return PosOrderResponse
	 */
	public static ErrorDetails createFailureMessage(Exception e) {

		LOGGER.debug("Entering ServiceUtil createFailureMessage");

		ErrorDetails errorDtl = new ErrorDetails();
		errorDtl.setErrorCode("");
		errorDtl.setErrorString(e.getMessage());
		errorDtl.setErrorSource(e.getLocalizedMessage());
		errorDtl.setDescription(e.getStackTrace().toString());

		LOGGER.debug("Exiting ServiceUtil createFailureMessage");

		return errorDtl;

	}
	
	
	/**
	 * Type conversion helper
	 * 
	 * @param objVal
	 * @return
	 */
	public static Integer bigDecimalToInt(Object objVal) {
		LOGGER.info(" Entering bigDecimalToLong method of CommonUtil ");
		Integer longVal = null;
		try {
			if (objVal != null) {
				BigDecimal bigDecimalVal = new BigDecimal(objVal.toString());
				longVal = bigDecimalVal.intValue();
			}
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Error occoured at bigDecimalToLong method of CommonUtil - "
						+ e.getMessage());
			}
		} finally {
			LOGGER.info(" Exiting bigDecimalToLong method of CommonUtil ");
		}

		return longVal;
	}

	/**
	 * This function changes the String format to TimeStamp format
	 * 
	 * @param sourceDate
	 * @return
	 */
	public static Timestamp dateFormatter(String sourceDate) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering dateFormatter method of ServiceUtil ");
		}

		SimpleDateFormat formatter = new SimpleDateFormat(
				PhotoOmniConstants.STORE_DATE_PATTERN);
		Timestamp timestamp2 = null;

		try {
			timestamp2 = new Timestamp(formatter.parse(sourceDate).getTime());
		} catch (ParseException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" ParseException occoured at dateFormatter method of ServiceUtil - "
						+ e.getMessage());
			}
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting dateFormatter method of ServiceUtil ");
			}
		}

		return timestamp2;
	}

	public static double calculatePMPerProduct(double doubleValue,
			long longValue) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering calculatePMPerProduct method of ServiceUtil ");
		}
		double pmPerProduct = 0;
		try {
			pmPerProduct = doubleValue / longValue;
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at calculatePMPerProduct method of ServiceUtil - "
						+ e.getMessage());
			}
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting calculatePMPerProduct method of ServiceUtil ");
			}
		}
		return pmPerProduct;

	}
	
	/**Date formatter one*/
	public static String dateFormatterOne(String sourceDate){
		
		String[] tempDateArray = sourceDate.split("T");
		String tempDate1 = tempDateArray[0];
		String tempDate2 = tempDate1.replaceAll("-", "/");
		@SuppressWarnings("deprecation")
		Date d1 = new Date(tempDate2);
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy");
		String tempd1 = sdf1.format(d1);
		
		return tempd1;
	}
	public static int validateLicenceCntReq(LCDataBean lcreq){
		int status = 0;
		if(lcreq.getOriginOrderId().isEmpty() || lcreq.getOriginOrderId() == null){
			status = 1;			
		}else if(lcreq.getPcpProductId().isEmpty()){
			status = 2;
		}
			
		return status;
	}
	public static String orderValidatemsg(int orderStatus){
		String validationMsg = "";
		switch(orderStatus){
				case 1:
					validationMsg = "Invalid PcpOrderId";	
				break;	
				case 2:
					validationMsg = "Invalid PcpProductID";	
				break;
				default:
					validationMsg = "Order must have one item";
				break;	
			}
		return validationMsg;
	}
	public static String dateformat24(String sourceDate){
		SimpleDateFormat targetFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		
	String date = targetFormatter.format(dateFormatter(sourceDate));
		return date;
		
	}
}
