package com.walgreens.oms.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.LicenceContentPosBean;
import com.walgreens.oms.bean.OmPosTranByWicUpcBean;
import com.walgreens.oms.bean.OmProductDetailBean;
import com.walgreens.oms.bean.OrderBean;
import com.walgreens.oms.bean.OrderHistoryBean;
import com.walgreens.oms.bean.OrderLineBean;
import com.walgreens.oms.bean.OrderLineTemplateBean;
import com.walgreens.oms.bean.POSOrder;
import com.walgreens.oms.bean.PosTransactionDetailBean;
import com.walgreens.oms.json.bean.PosDetails;
import com.walgreens.oms.json.bean.PosList;
import com.walgreens.oms.rowmapper.LicenceContentPosBeanRowMapper;
import com.walgreens.oms.rowmapper.OmProductDetailBeanRowMapper;
import com.walgreens.oms.rowmapper.OrderLineBeanRowMapper;
import com.walgreens.oms.rowmapper.OrderLineTemplateBeanRowMapper;
import com.walgreens.oms.rowmapper.PosOrderBeanRowMapper;
import com.walgreens.oms.rowmapper.PosOrderRowMapper;
import com.walgreens.oms.rowmapper.PosTransactionDetailBeanRowMapper;
import com.walgreens.oms.utility.OmPosTranByWicUpcComparator;
import com.walgreens.oms.utility.PosOrderQuery;
import com.walgreens.oms.utility.ServiceUtil;

/**
 * @author CTS Class used to process POS real time Order requests
 */
@Repository
@Component("PosOrdersDAO")
@SuppressWarnings("static-access")
@Transactional(propagation = Propagation.REQUIRED)
public class PosOrdersDAOImpl implements PosOrdersDAO {
		
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private PosOrderQuery posOrderQuery = new PosOrderQuery();
	private static SimpleDateFormat formatter = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_THIRTEENTH);
	private static DecimalFormat decimalFormatter = new DecimalFormat("0.00");
	
	/*** logger to log the details.*/
	private static final Logger LOGGER = LoggerFactory.getLogger(PosOrdersDAOImpl.class);
	
	/** Added in phase 2.0 for reuse of code in POS reconciliation batch also 
	    added parameter boolean reconFlag in processPosRequest() method */
	@Transactional(propagation = Propagation.REQUIRED)
	public PosList processPosRequest(PosList posList, boolean reconFlag,OrderBean orderBean)
    throws PhotoOmniException, RuntimeException, ParseException,Exception {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering processPosRequest method of PosOrdersDAOImpl");}
		
		/** IF reconFlag = true && if sysOrderId NOT found */
		if (reconFlag && CommonUtil.isNull(orderBean) && CommonUtil.isNull(orderBean.getSysOrderID())) {
			return null;
		}
		
		/** IF reconFlag = true && if sysOrderId is found */
	    if(reconFlag && !CommonUtil.isNull(orderBean) && !CommonUtil.isNull(orderBean.getSysOrderID())) {
			
			/** update SysOrderId in OM_POS_TRANSACTION table */
			String updatePosTranDetailQuery = posOrderQuery.getUpdatePosDetailQuery();
			jdbcTemplate.update(updatePosTranDetailQuery,new Object[] {orderBean.getSysOrderID() ,
					Integer.parseInt(posList.getPosDetails().getEnvelopeNbr()),posList.getPosDetails().getPosTrnTypeDTTM()});
			
			/**Get particular sysStorePosIdQuery related to the sysOrderId from OM_POS_TRANSACTION table*/
			Long sysStorePosIdTemp = getSysStorePosId(orderBean.getSysOrderID(),posList.getPosDetails().getEnvelopeNbr(),
					                 formatter.format(formatter.parse(posList.getPosDetails().getPosTrnTypeDTTM())));
			orderBean.setSysStorePosId(sysStorePosIdTemp);			

			/** process common functions for POS Transactions*/
			List<OmPosTranByWicUpcBean> omPosTranByWicUpcBeanList = new ArrayList<OmPosTranByWicUpcBean>();
			omPosTranByWicUpcBeanList = processPOSCommonTransactions(orderBean,posList,reconFlag);
			
			/**Check for valid records need to be inserted or deleted based on MSS_TRANSFER_CD in table OM_POS_TRANS_BY_WIC_UPC*/
			List<Long> sysPosTransByProductIdList = getSysStorePosIdForReconciliationInsertDelete(orderBean);
			if(sysPosTransByProductIdList.size()>0){
				
				/** Delete default record from MSS Tables */
				deleteMssRecord(sysPosTransByProductIdList);
				/** Insert record in MSS tables */			
				inserOmPosTransByWicUpc(omPosTranByWicUpcBeanList);
			}
						
		}
		
		/** IF reconFlag = false && if sysOrderId is found */
		else if (!reconFlag && !CommonUtil.isNull(orderBean)&& !CommonUtil.isNull(orderBean.getSysOrderID())) {
			
			/**format posBusinessDate,posTrnTypeDTTM,posSoldAmt*/
			posList = formatPosMessageDate(posList);
			
			/** insert details in POS transaction table */
			Long sysStorePosId = insertPosDetails(orderBean.getSysOrderID(),posList.getPosDetails());
			orderBean.setSysStorePosId(sysStorePosId);						
						
			/** process common functions for POS Transactions*/
			List<OmPosTranByWicUpcBean> omPosTranByWicUpcBeanList = new ArrayList<OmPosTranByWicUpcBean>();
			omPosTranByWicUpcBeanList = processPOSCommonTransactions(orderBean,posList,reconFlag);			

			/** Insert record in MSS */
			inserOmPosTransByWicUpc(omPosTranByWicUpcBeanList);

		}

		/** IF reconFlag = false && if sysOrderId NOT found */
		else if (!reconFlag && CommonUtil.isNull(orderBean.getSysOrderID())) {
			
			/**format posBusinessDate,posTrnTypeDTTM,posSoldAmt*/
			posList = formatPosMessageDate(posList);
			
			/** insert details in POS transaction table */
			Long sysStorePosId = insertPosDetails(orderBean.getSysOrderID(),posList.getPosDetails());
						
			/** Calculate default values for MSS Table */
			List<OmPosTranByWicUpcBean> omPosTranByWicUpcBeanList = new ArrayList<OmPosTranByWicUpcBean>();
			omPosTranByWicUpcBeanList.add(getPosMssFeedOrderNotFoundMethod(posList,sysStorePosId));

			/** Insert default value record in MSS */
			inserOmPosTransByWicUpc(omPosTranByWicUpcBeanList);
		
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting processPosRequest method of PosOrdersDAOImpl");
		}
		return posList;
	}
	

	/**Method to process common functions for POS update and ReConciliation 
	/**
	 * @param orderBean
	 * @param posList
	 * @return
	 * @throws PhotoOmniException
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private List<OmPosTranByWicUpcBean> processPOSCommonTransactions(OrderBean orderBean,PosList posList,boolean reconFlag) 
	throws PhotoOmniException,NumberFormatException, Exception {
								
		/**Apply and update sold amount with proportion logic and calculate values for POS MSS tables*/
		List<OmPosTranByWicUpcBean> omPosTranByWicUpcBeanList = new ArrayList<OmPosTranByWicUpcBean>();	 
		omPosTranByWicUpcBeanList = calculateSoldAmount(
				Double.parseDouble(posList.getPosDetails().getPosSoldAmount()),
				posList.getPosDetails().getPosTrnType(),orderBean.getSysOrderID(),
				formatter.format(formatter.parse(orderBean.getOrderPlacedDTTM().toString())), 
				Integer.parseInt(posList.getPosDetails().getPrintsReturned()),
				posList,Integer.parseInt(orderBean.getOrderNBR()),orderBean);
	

		/** get Order details from OM_ORDER with sysOrderId */
		POSOrder posOrder = new POSOrder();
		posOrder = getOrderDetailsOmOrder(orderBean.getSysOrderID(),
				formatter.format(formatter.parse(orderBean.getOrderPlacedDTTM().toString())));				
				
		/** update order status and Order sold dttm in order tables */
		String finalOrderStatus = updateOmOrderStatusOrderSoldDttm(
				posOrder.getSoldAmount(),posOrder.getStatus(), posOrder.getSysOrderId(), 
				posList.getPosDetails().getPosTrnType(),
				formatter.format(formatter.parse(orderBean.getOrderPlacedDTTM().toString())),
				posOrder.getOwningLocId(),posOrder.getOrderCompletedDttm(),
				formatter.format(formatter.parse(posList.getPosDetails().getPosTrnTypeDTTM())));

		/*** Update OM_ORDER_ATTRIBUTE table with PRINTS_RETURNED_QTY 
		      and calendar id of the day the POS transaction took place */
		updateCalendarIdPrintsReturnedQty(posList.getPosDetails().getPosTrnTypeDTTM(),
				posOrder.getSysOrderId(),
				formatter.format(formatter.parse(orderBean.getOrderPlacedDTTM().toString())),
				posList.getPosDetails().getPosTrnType(),posOrder.getPrintsReturned(),
				Integer.parseInt(posList.getPosDetails().getPrintsReturned()));

		/** insert record into order history table */
		insertOrderHistory(
				posList.getPosDetails().getPosTrnType(),posList.getPosDetails().getPosTrnTypeDTTM(), 
				posOrder.getSysOrderId(),posOrder.getOrderPlacedDttm(),finalOrderStatus, 
				posList.getPosDetails().getEmployeeId());
		
		return omPosTranByWicUpcBeanList;
     }
 	
	/**
	 * Method to calculate cost for POS transactions 
	 * @param orderBean
	 * @param posList
	 * @throws ParseException 
	 * @throws DataAccessException 
	 *@throws PhotoOmniException 
	 */

	
	/**
	 * Method to delete MSS record form OM_POS_TRANS_BY_WIC_UPC
	 * @param omPosTranByWicUpcBean
	 * @return
	 */
	public void deleteMssRecord(final List<Long> sysPosTransByProductIdList) {
						
		String deleteMssRecordQuery = posOrderQuery.getDeleteMssRecordQuery();		
		jdbcTemplate.batchUpdate(deleteMssRecordQuery, new BatchPreparedStatementSetter() {			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {					 
					ps.setLong(1, sysPosTransByProductIdList.get(i));}						
			@Override
			public int getBatchSize() { 
				return sysPosTransByProductIdList.size();}
		  });					
	}

	/**
	 * Method to set default value for MSS table
	 * @param posList
	 * @param orderNbr
	 * @param sysOrderId
	 * @return
	 * @throws Exception
	 */
	public OmPosTranByWicUpcBean getPosMssFeedOrderNotFoundMethod(PosList posList,Long sysStorePosId) throws Exception {	
		
		/**Create instance of omPosTranByWicUpcBean*/
		OmPosTranByWicUpcBean omPosTranByWicUpcBean = new OmPosTranByWicUpcBean();		
		/**Set Upc,Wic,SalesDollers*/	
		omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_DEFAULT_PATTERN);
		omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_DEFAULT_PATTERN);
		
		/** Changes for JIRA#624 starts here **/
		// Set default product number when product is not found
		omPosTranByWicUpcBean.setProductNbr("0");
	    /** Changes for JIRA#624 ends here **/
		
		
		/**set SalesDollers as Sold Amount of the transaction*/
		omPosTranByWicUpcBean.setSalesDollers(Double.parseDouble(posList.getPosDetails().getPosSoldAmount()));
		/**calculate CostDollers*/
		if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)|| 
				posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {		
			 omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format((40*(Double.parseDouble(posList.getPosDetails().getPosSoldAmount())))/100)));	
		}else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {		
			 omPosTranByWicUpcBean.setCostDollers((Double.parseDouble(decimalFormatter.format((40*(Double.parseDouble(posList.getPosDetails().getPosSoldAmount())))/100))));	
		} else if (posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)|| 
				posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {
			 omPosTranByWicUpcBean.setCostDollers(0);
		}
		/**calculate sales unit*/
		if (posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)|| 
				posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)|| 
		    posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {		
			        omPosTranByWicUpcBean.setSalesUnits(1);	
		}else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)|| 
				posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)){
			omPosTranByWicUpcBean.setSalesUnits(-1);
		}
		
        /**set DefaultCostCd*/
		omPosTranByWicUpcBean.setDefaultCostCd(1);
		/**Set BusinessDate*/
		omPosTranByWicUpcBean.setBusinessDate(posList.getPosDetails().getPosBusinessDate());
        /**Set EnvelopNbr*/
		omPosTranByWicUpcBean.setEnvelopNbr(Integer.parseInt(posList.getPosDetails().getEnvelopeNbr()));
		/**Set LocationNumber*/
		omPosTranByWicUpcBean.setLocationNbr(Integer.parseInt(posList.getPosDetails().getLocationNumber()));
         /**Set MssTransfarCd*/ 
		omPosTranByWicUpcBean.setMssTransfarCd("N");
		/**Set MssTransmissionDate */
		omPosTranByWicUpcBean.setMssTransmissionDate(null);
         /**Set OrderNbr*/
		omPosTranByWicUpcBean.setOrderNbr(0);
        /**Set sysStorePosId*/
		omPosTranByWicUpcBean.setSysStorePosId(sysStorePosId);
        /**Set TransactionTypeCd*/
		omPosTranByWicUpcBean.setTransactionTypeCd(posList.getPosDetails().getPosTrnType());	
		return omPosTranByWicUpcBean;
	}

	/**
	 * Method to insert record in POS mss table
	 * @param omPosTranByWicUpcBean
	 * @return
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 * @throws ParseException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void inserOmPosTransByWicUpc(final List<OmPosTranByWicUpcBean> omPosTranByWicUpcBeanList)
			throws RuntimeException, PhotoOmniException, ParseException,Exception {
		 if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl inserOmPosTransByWicUpc() ");}
				   
		    /** insert new record in OM_POS_TRANS_BY_WIC_UPC */		   		
		    	String inserOmPosTransByWicUpcQuery = posOrderQuery.getInserOmPosTransByWicUpc();
			    jdbcTemplate.batchUpdate(inserOmPosTransByWicUpcQuery, new BatchPreparedStatementSetter() {			
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					OmPosTranByWicUpcBean omPosTranByWicUpcBean = omPosTranByWicUpcBeanList.get(i);
					
					try {ps.setString(1, omPosTranByWicUpcBean.getBusinessDate());
					} catch (Exception e) {throw new SQLException();}					  
					ps.setString(2, String.valueOf(omPosTranByWicUpcBean.getCostDollers()));
					ps.setString(3, String.valueOf(omPosTranByWicUpcBean.getDefaultCostCd()));
					ps.setString(4, String.valueOf(omPosTranByWicUpcBean.getEnvelopNbr()));
					ps.setString(5, String.valueOf(omPosTranByWicUpcBean.getLocationNbr()));
					/**default value for MssTransfarCd while inserting record through POS*/
					ps.setString(6, String.valueOf("N"));
					/**default value for MssTransmissionDate while inserting record through POS*/
					try {ps.setString(7, null);
					} catch (Exception e) {throw new SQLException();}										
					ps.setString(8, String.valueOf(omPosTranByWicUpcBean.getOrderNbr()));
					ps.setString(9, String.valueOf(omPosTranByWicUpcBean.getSalesDollers()));
					ps.setString(10, String.valueOf(omPosTranByWicUpcBean.getSalesUnits()));
					ps.setString(11, String.valueOf(omPosTranByWicUpcBean.getSysStorePosId()));
					ps.setString(12, String.valueOf(omPosTranByWicUpcBean.getUpc()));
					ps.setString(13, String.valueOf(omPosTranByWicUpcBean.getWic()));
					ps.setString(14, String.valueOf(omPosTranByWicUpcBean.getTransactionTypeCd()));
					ps.setString(15, PhotoOmniConstants.CREATE_USER_ID);
					ps.setString(16, PhotoOmniConstants.UPDATE_USER_ID);
					
					/** Changes for JIRA#624 starts here **/
					// Set default product number when product is not found
					if(null != omPosTranByWicUpcBean.getProductNbr()){
						ps.setString(17, omPosTranByWicUpcBean.getProductNbr());
					}else {
						ps.setString(17, "0");
					}
					/** Changes for JIRA#624 ends here **/
					
				}						
				@Override
				public int getBatchSize() { return omPosTranByWicUpcBeanList.size();}
			  });		    
			if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDAOImpl inserOmPosTransByWicUpc()");
			}
	}

	/**
	 * Method to insert record in Order history table
	 * @param posTrnType
	 * @param PosTrnTypeDTTM
	 * @param sysOrderId
	 * @param orderPlacedDttm
	 * @param finalOrderStatus
	 * @param employeeId
	 * @return
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 * @throws ParseException 
	 */
	public void insertOrderHistory(String posTrnType, String PosTrnTypeDTTM,
			long sysOrderId, Timestamp orderPlacedDttm,String finalOrderStatus, String employeeId)throws RuntimeException, PhotoOmniException, ParseException {

		String action = posTrnType;
		String actionNotes = getDecode(posTrnType.trim());

		long sysExceptionId = 0;
		try {
			sysExceptionId = getSysExceptionId(sysOrderId);
		}catch (NullPointerException e) {
			if (LOGGER.isErrorEnabled()) {LOGGER.error(" sysException ID not present for current order ");}
			sysExceptionId = 0;
			}
		OrderHistoryBean orderHistoryBean = new OrderHistoryBean();
		orderHistoryBean.setAction(action);
		orderHistoryBean.setActionDttm(Timestamp.valueOf(PosTrnTypeDTTM));
		orderHistoryBean.setActionNotes(actionNotes);
		/** exception id filled with Zero/null for first time order submission */
		orderHistoryBean.setExceptionId(sysExceptionId);
		orderHistoryBean.setOrderId(sysOrderId);
		orderHistoryBean.setOrderPlacedDttm(orderPlacedDttm);
		orderHistoryBean.setOrderStatus(finalOrderStatus);
		/** insert EmployeeID from POS message received */
		if (employeeId.equalsIgnoreCase("")|| employeeId.equalsIgnoreCase(PhotoOmniConstants.BLANK)|| employeeId == null) {
			orderHistoryBean.setCreateUserId(PhotoOmniConstants.CREATE_USER_ID);
		} else {
			orderHistoryBean.setCreateUserId(employeeId);
		}
		orderHistoryBean.setUpdateUserId(PhotoOmniConstants.UPDATE_USER_ID);
		insertOrderHistoryDetails(orderHistoryBean);
	}
	
	/**
	 * method to get SysStorePosId For Reconciliation batch Insert Delete in OM_POS_TRANS_BY_WIC_UPC table
	 * @param posList
	 * @return
	 * @throws Exception 
	 */
	private List<Long> getSysStorePosIdForReconciliationInsertDelete(OrderBean orderBean) throws DataAccessException, ParseException {
				
		/**Check if record exist for the SYS_STORE_POS_ID in OM_POS_TRANS_BY_WIC_UPC if MSS_TRANSFER_CD <> ‘Y’ */
		String queryForSysPosTransByProductId = posOrderQuery.getQueryForSysPosTransByProductId(); 
		List<Long> sysPosTransByProductIdList = jdbcTemplate.queryForList(
				queryForSysPosTransByProductId,new Object[]{orderBean.getSysStorePosId()},Long.class);
				
			return sysPosTransByProductIdList;
	}


	/**
	 * method to format PosList dates
	 * @param posList
	 * @return
	 * @throws Exception 
	 */
	public PosList formatPosMessageDate(PosList posList) throws Exception {		
		
		Timestamp posTrnTypeDTTMTimeStamp = ServiceUtil.dateFormatter(posList.getPosDetails().getPosTrnTypeDTTM());				
		Timestamp posBusinessDateTimeStamp = CommonUtil.formatDateTwo(posList.getPosDetails().getPosBusinessDate());					
				
		posList.getPosDetails().setPosTrnTypeDTTM(formatter.format(formatter.parse(posTrnTypeDTTMTimeStamp.toString())));
		posList.getPosDetails().setPosBusinessDate(formatter.format(formatter.parse(posBusinessDateTimeStamp.toString())));	
								
		return posList;
	}
	
	/**
	 * Method to update  Prints Returned Qty
	 * @param posTrnType
	 * @param printsReturned
	 * @param posPrintsReturned
	 * @param sysOrderId
	 * @param orderPlacedDttm 
	 * @return
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	public void updatePosPrintsReturnedQty(String posTrnType,int printsReturned, int posPrintsReturned, long sysOrderId, String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException {

		int printsReturnedQty = 0;
		if (posTrnType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {
			printsReturnedQty = (printsReturned - posPrintsReturned);
		} else {
			printsReturnedQty = (printsReturned + posPrintsReturned);
		}
		updatePrintsReturnedQty(printsReturnedQty, sysOrderId,orderPlacedDttm);
	}
	
	/**
	 * Method to update updateCalendarId
	 * @param tempPosTransactionDttm
	 * @param sysOrderId
	 * @return
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	public void updateCalendarIdPrintsReturnedQty(String tempPosTransactionDttm,long sysOrderId,
			String orderPlacedDTTM,String posTrnType,int printsReturned, int posPrintsReturned) 
			throws RuntimeException, PhotoOmniException {
		
		int calenderId = getCalenderId(tempPosTransactionDttm);
		
		int printsReturnedQty = 0;
		if (posTrnType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {
			printsReturnedQty = (printsReturned - posPrintsReturned);
		} else {
			printsReturnedQty = (printsReturned + posPrintsReturned);
		}
		updatePosTranCalIdRtnQty(printsReturnedQty,calenderId, sysOrderId,orderPlacedDTTM);		
	}

	/**
	 * method to update OmOrder Status
	 * @param updSoldAmt
	 * @param orderStatus
	 * @param sysOrderId
	 * @param posTransactionType
	 * @param orderPlacedDttm
	 * @param timestamp 
	 * @param owningLocId
	 * @param orderCompletedDttm
	 * @return
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	public String updateOmOrderStatusOrderSoldDttm(double updSoldAmt, String orderStatus,
			long sysOrderId, String posTransactionType,String orderPlacedDttm, int owningLocId,
			Timestamp orderCompletedDttm,String posTrnTypeDTTM) throws RuntimeException,PhotoOmniException {

		String finalOrderStatus = PhotoOmniConstants.BLANK;

		if (updSoldAmt > 0) {
			finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_SOLD;
			updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);		
		}
		if (updSoldAmt <= 0) {
			/** update order status for earlier order status PROC */
			if (orderStatus
					.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_PROC)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {

				finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_SOLD;
				updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
				
			} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_PROC)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {

				if (null != orderCompletedDttm) {
					finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_DONE;
					updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
				} else {
					finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_PROC;
					updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
				}
			} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_PROC)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {

				finalOrderStatus = PhotoOmniConstants.CANCEL;
				updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
			}

			/** update order status for earlier order status DONE */
			else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_DONE)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {

				if (updSoldAmt == 0) {
					finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_SOLD;
					updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
				}
			} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_DONE)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {

				if (null != orderCompletedDttm) {
					finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_DONE;
					updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);

				} else {
					finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_PROC;
					updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
				}
			} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_DONE)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {

				finalOrderStatus = PhotoOmniConstants.CANCEL;
				updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
			}
			/** update order status for earlier order status SOLD */
			else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {

				if (updSoldAmt == 0) {
					finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_SOLD;
					updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
				}

			} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {

				if (null != orderCompletedDttm) {
					finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_DONE;
					updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);

				} else {
					finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_PROC;
					updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
				}
			} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {

				finalOrderStatus = PhotoOmniConstants.CANCEL;
				updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);

			}
			/** update order status for earlier order status CANCELLED */
			else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.CANCEL)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {

				finalOrderStatus = PhotoOmniConstants.CANCEL;
				updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
			}
			/** update order status for earlier order status COMPLETE */
			else if (orderStatus
					.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_COMPLETE)
					&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
					|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {

				finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_COMPLETE;
				updateOrderStatusSoldDttm(finalOrderStatus, posTrnTypeDTTM,sysOrderId,orderPlacedDttm);
			}
			/**else if order status not update able update only order sold dttm */
			else {
				updateOrderSoldDttm(sysOrderId,posTrnTypeDTTM,orderPlacedDttm);
			}
		}
		return finalOrderStatus;
	}

	/** method used to calculate sold amount proportion and POS MSS feed population 
	 * @param orderPlacedDttm 
	 * @param omPosTranByWicUpcBean 
	 * @param posPrintsReturned 
	 * @throws Exception */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<OmPosTranByWicUpcBean> calculateSoldAmount(double posOrderSoldAmt,
			String posTranType, long sysOrderId,String orderPlacedDttm, 
			int posPrintsReturned,PosList posList,int orderNbr,OrderBean orderBean) 
			throws Exception {		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosProportionatingSoldAmt calculateSoldAmount() method ");}	
		
		/**Boolean flag used for product not found scenario */
		boolean defaultProductNotFoundFlag = false;
		/**Comparator for OmPosTranByWicUpcBean for removing duplicates */
		OmPosTranByWicUpcComparator omPosTranByWicUpcComparator = new OmPosTranByWicUpcComparator();
		/** Initialize OmPosTranByWicUpcBeanList */
		List<OmPosTranByWicUpcBean> omPosTranByWicUpcBeanList = new ArrayList<OmPosTranByWicUpcBean>();	
		
		/** get order details for OM_ORDER table with SYS_ORDER_ID */
		POSOrder posOrder = new POSOrder();
		posOrder = getOrderDetailsOmOrder(sysOrderId,orderPlacedDttm);
		
		double posOrderOmFinalPrice = posOrder.getFinalPrice();
		double soldAmtOmOrder = 0.00;
		
		    /** update  soldAmtOmOrder logic based on posTranType */
		    soldAmtOmOrder = posOrder.getSoldAmount() + posOrderSoldAmt;
			/** UPDATE SOLD_AMOUNT in OM_ORDER table */
			updateSoldAmtOmOrder(soldAmtOmOrder, sysOrderId,orderPlacedDttm);
			
			/** get Order line details from OM_ORDER_LINE table with ORDER_ID */
			List<OrderLineBean> orderLineBeanList = new ArrayList<OrderLineBean>();
			orderLineBeanList = getOrderLineDetails(sysOrderId,orderPlacedDttm);

			/** Calculate OrderLine Sold Amount with proportion */			
			if (orderLineBeanList != null && orderLineBeanList.size() > 0) {
				Long sysOrderLineID = null;  Double orderLineQuentity = null;
				Double orderLineFinalPrice = null;Double lineSoldAmtSum = 0.0000;
				
				for (int p = 0; p < orderLineBeanList.size(); p++) {
					 orderLineFinalPrice = orderLineBeanList.get(p).getFinalPrice();			
					  double lineSoldAmt = getLineSoldAmt(posOrderOmFinalPrice,orderLineFinalPrice,soldAmtOmOrder);	
					  /**Add line sold amount to orderLineBeanList*/
					  orderLineBeanList.get(p).setLineSoldAmt(lineSoldAmt);
					  /**Sum all order line sold amount of each order line*/
					  lineSoldAmtSum = lineSoldAmtSum + lineSoldAmt;
				}
				double lineSoldAmtDiff = 0.00;
				DecimalFormat formatter = new DecimalFormat("0.00");
				if (lineSoldAmtSum < soldAmtOmOrder || lineSoldAmtSum > soldAmtOmOrder) {				
					  lineSoldAmtDiff = Double.parseDouble(formatter.format(soldAmtOmOrder - lineSoldAmtSum));
					}				
				/**sumSalesDollars variable used to proportionate MSS SalesDollers */
				double sumSalesDollars = 0.00;
				double lastLineSoldAmt = 0.00;
				for (int q = 0; q < orderLineBeanList.size(); q++) {
					sysOrderLineID = orderLineBeanList.get(q).getSysOrderLineId();
					orderLineQuentity = (double) orderLineBeanList.get(q).getQuantity();
					orderLineFinalPrice = orderLineBeanList.get(q).getFinalPrice();
					
					if (q == (orderLineBeanList.size() - 1)) {
						/**Add lineSoldAmtDiff to last Order line */						
						lastLineSoldAmt = Double.parseDouble(formatter.format(orderLineBeanList.get(q).getLineSoldAmt()+lineSoldAmtDiff));						
						orderLineBeanList.get(q).setLineSoldAmt(lastLineSoldAmt);
					} 					
					/** update LINE_SOLD_AMOUNT in OM_ORDER_LINE with ORDER_ID */
					updateLineSoldAmt(orderLineBeanList.get(q).getLineSoldAmt(), sysOrderLineID,orderPlacedDttm);														
								      					
					/**Get particular sysProductId related to the sysOrderLineID from OM_ORDER_LINE table*/
					long sysProductId = orderLineBeanList.get(q).getSysProductId();	
					int orderLineQuantity = orderLineBeanList.get(q).getQuantity();
					double orderLineCost = orderLineBeanList.get(q).getCost();
					double orderLineSoldAmt = orderLineBeanList.get(q).getLineSoldAmt();
					
					/**Check if sysProductId is present in OM_PRODUCT_ATTRIBUTE table*/
					Long tempSysProductId = null;
					try { tempSysProductId = jdbcTemplate.queryForObject(posOrderQuery.getSysProductIdTemp(),new Object[]{sysProductId},Long.class);
					       } catch (Exception e) {
						        LOGGER.error("PRODUCT NOT FOUND FOR THE ORDER LINE "+sysOrderLineID+" ASSOCIATED WITH THE ORDER "+sysOrderId);
					          }
					
					/**Set MSS SalesDollers with proportion */									
					double salesDollers = getMssSalesDollers(posList,orderLineFinalPrice,posOrderOmFinalPrice,posOrderSoldAmt,orderNbr);
					sumSalesDollars = sumSalesDollars + salesDollers;
					if(q == (orderLineBeanList.size() - 1)){
					   if(sumSalesDollars < posOrderSoldAmt || sumSalesDollars > posOrderSoldAmt){
						    double salesDollersDiff = Double.parseDouble(formatter.format(posOrderSoldAmt - sumSalesDollars));
						    salesDollers = salesDollers + salesDollersDiff;
					      }
					   }
										
					/**If Order NBR is FOUND, then if Item is FOUND, and PRODUCT is FOUND */
					if(!CommonUtil.isNull(tempSysProductId)){
						if (LOGGER.isDebugEnabled()) {LOGGER.debug(" Order Number :"+orderNbr+" is FOUND,"
								+ "Associated Item/Order Line :"+sysOrderLineID+" is FOUND AND Associated PRODUCT:"+sysProductId+" is FOUND FOR POS MSS FEED");}		
						/**Initialize OmPosTranByWicUpcBean*/
						 OmPosTranByWicUpcBean omPosTranByWicUpcBean = new OmPosTranByWicUpcBean();
						    omPosTranByWicUpcBean = getPosMssFeedProductFoundMethod(
								salesDollers,sysOrderId,orderNbr,sysOrderLineID,orderPlacedDttm,posList,
								omPosTranByWicUpcBean,posOrderSoldAmt,sysProductId,posOrder,orderLineCost,orderLineQuantity,orderBean);						    
						    omPosTranByWicUpcBeanList.add(omPosTranByWicUpcBean);
					}					
					/**If Order NBR is FOUND, then if Item is FOUND, but PRODUCT is NOT FOUND */
					else {
						if (LOGGER.isDebugEnabled()) {LOGGER.debug("Order Number :"+orderNbr+" is FOUND,"
								+"Associated Item/Order Line :"+sysOrderLineID+" is Found but PRODUCT: "+sysProductId+" is NOT FOUND FOR POS MSS FEED");}							    						
						    /**Initialize OmPosTranByWicUpcBean*/
							OmPosTranByWicUpcBean omPosTranByWicUpcBean = new OmPosTranByWicUpcBean();
						    omPosTranByWicUpcBean = getPosMssFeedProductNotFoundMethod(
								salesDollers,sysOrderId,orderNbr,sysOrderLineID,orderPlacedDttm,
								posList,omPosTranByWicUpcBean,posOrderSoldAmt,posOrder,sysProductId,orderLineQuantity,orderBean,orderLineSoldAmt);
						    /**Comparator used to check for duplicate omPosTranByWicUpcBean with same UPC and WIC numbers,if duplicate
						       details found don't add in omPosTranByWicUpcBeanList*/
						    for(int i=0;i<omPosTranByWicUpcBeanList.size();i++){						    
						    	int compVal = omPosTranByWicUpcComparator.compare(omPosTranByWicUpcBeanList.get(i), omPosTranByWicUpcBean);
						            if(compVal == 0){
						    	            if (LOGGER.isDebugEnabled()){LOGGER.debug("SAME PRODUCT :"+sysProductId+" FOUND FOR POS MSS FEED For Order Number :"+orderNbr);}	
						              }else{
						    	            omPosTranByWicUpcBeanList.add(omPosTranByWicUpcBean);
						                   }
						             }	
						      if(!defaultProductNotFoundFlag){
						    	omPosTranByWicUpcBeanList.add(omPosTranByWicUpcBean);
						    	defaultProductNotFoundFlag = true;
						        }						    						   					
					    }					
					
					/*** get license content details from OM_OL_LICENSE_CONTENT*/
					List<LicenceContentPosBean> licenceContentBeanList = new ArrayList<LicenceContentPosBean>();
					licenceContentBeanList = getLicenceContentDetails(sysOrderLineID,orderPlacedDttm);

					/** get order line template details for form OM_ORDER_LINE_TEMPLATE*/
					List<OrderLineTemplateBean> orderLineTemplateBeanList = new ArrayList<OrderLineTemplateBean>();
					orderLineTemplateBeanList = getOrderLineTemplateDetails(sysOrderLineID,orderPlacedDttm);

					/** Calculate License Amount paid with proportion */
					if (licenceContentBeanList != null && licenceContentBeanList.size() > 0) {
						double lcAmtPaidSum = 0.00;
						for (int j = 0; j < licenceContentBeanList.size(); j++) {
							double finalLcPrice = licenceContentBeanList.get(j).getFinalLcPrice();
							double lcAmtPaid = getlcAmtPaid(orderLineFinalPrice,finalLcPrice,orderLineBeanList.get(q).getLineSoldAmt());
							/**Set lcAmtPaid to each license content*/
							licenceContentBeanList.get(j).setLcAmountPaid(lcAmtPaid);
							/**Sum all LcAmtPaid to  lcAmtPaidSum*/
							lcAmtPaidSum = lcAmtPaidSum + licenceContentBeanList.get(j).getLcAmountPaid();							
						}
						double lcAmtPaidDiff = 0.00;
						if (lcAmtPaidSum < orderLineBeanList.get(q).getLineSoldAmt() || lcAmtPaidSum > orderLineBeanList.get(q).getLineSoldAmt()) {
							lcAmtPaidDiff = Double.parseDouble(formatter.format(orderLineBeanList.get(q).getLineSoldAmt() - lcAmtPaidSum));
						}
						double lastLcAmtPaid = 0.00;
						for (int k = 0; k < licenceContentBeanList.size(); k++){
							if(k == (licenceContentBeanList.size() - 1)){	
								/**Add lcAmtPaidDiff to last licenceContent */
								lastLcAmtPaid = Double.parseDouble(formatter.format(licenceContentBeanList.get(k).getLcAmountPaid() + lcAmtPaidDiff));	
								licenceContentBeanList.get(k).setLcAmountPaid(lastLcAmtPaid);
							}
							/** update LC_AMOUNT_PAID in OM_OL_LICENSE_CONTENT with SYS_OL_LC_ID*/
							updatelcAmtPaidToLicemceContent(licenceContentBeanList.get(k).getLcAmountPaid(),
									licenceContentBeanList.get(k).getSysOlLcId(),orderPlacedDttm);
						}
					}
					/** Calculate template Sold Amount with proportion */
					if (orderLineTemplateBeanList != null && orderLineTemplateBeanList.size() > 0) {
						double templateSoldAmtSum = 0.00;
						for (int m = 0; m < orderLineTemplateBeanList.size(); m++) {
							double templateQuentity = orderLineTemplateBeanList.get(m).getTemplateQty();							
							double templateSoldAmt = getTemplateSoldAmt(orderLineQuentity,orderLineBeanList.get(q).getLineSoldAmt(),templateQuentity);	
							/**Set each templateSoldAmt to orderLineTemplateBeanList*/
							orderLineTemplateBeanList.get(m).setTemplateSoldAmt(templateSoldAmt);
							/**Sum all templateSoldAmt in templateSoldAmtSum*/
							templateSoldAmtSum = templateSoldAmtSum + orderLineTemplateBeanList.get(m).getTemplateSoldAmt();
						}
						double templateSoldAmtDiff = 0.00;
						if (templateSoldAmtSum < orderLineBeanList.get(q).getLineSoldAmt() || templateSoldAmtSum > orderLineBeanList.get(q).getLineSoldAmt()) {
							templateSoldAmtDiff = Double.parseDouble(formatter.format(orderLineBeanList.get(q).getLineSoldAmt() - templateSoldAmtSum));
						}
						double lastTemplateSoldAmt = 0.00;
						for (int n = 0; n < orderLineTemplateBeanList.size(); n++) {
							if (n == (orderLineTemplateBeanList.size() - 1)) {
								/**Add templateSoldAmtDiff to last order line template */
								lastTemplateSoldAmt = Double.parseDouble(formatter.format(orderLineTemplateBeanList.get(n).getTemplateSoldAmt() + templateSoldAmtDiff));
								orderLineTemplateBeanList.get(n).setTemplateSoldAmt(lastTemplateSoldAmt);		
							}
							/*** update TEMPLATE_SOLD_AMT in OM_ORDER_LINE_TEMPLATE with SYS_OL_TEMPLATE_ID*/
							updatelcTemplateSoldAmt(orderLineTemplateBeanList.get(n).getTemplateSoldAmt(),
									orderLineTemplateBeanList.get(n).getSysOlTemplateId(),orderPlacedDttm);
						}
					}
				}
			}			
			/**If Order Nbr is FOUND, then if Item is NOT FOUND then send data for default wic and upc*/	
			else{						    
				if (LOGGER.isDebugEnabled()) {LOGGER.debug("ORDER NBR :"+orderNbr+" is FOUND BUT NO ITEM/OrderLine  AND PRODUC FOUND For the Order");}			
				    OmPosTranByWicUpcBean omPosTranByWicUpcBean = new OmPosTranByWicUpcBean();
				    omPosTranByWicUpcBean = getPosMssFeedProductItemNotFoundMethod(sysOrderId, orderNbr, orderPlacedDttm,
				    		posList,posOrder, omPosTranByWicUpcBean,orderBean);								
				    omPosTranByWicUpcBeanList.add(omPosTranByWicUpcBean);
			}						
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosProportionatingSoldAmt calculateSoldAmount()");}
		return omPosTranByWicUpcBeanList;
	}

	
	/**Method to calculate MssSalesDollers
	 * @param orderNbr 
	 * @param orderLineNo */
	private Double getMssSalesDollers(PosList posList,double orderLineFinalPrice, 
			double posOrderOmFinalPrice,double posOrderSoldAmt, int orderNbr) {	
		DecimalFormat formatter = new DecimalFormat("0.00");
		Double salesDollers = null;
			  if(posOrderOmFinalPrice != 0){
			      salesDollers = Double.parseDouble(formatter.format(((orderLineFinalPrice*posOrderSoldAmt)/posOrderOmFinalPrice)));	
			      }else{ 
				        salesDollers = Double.parseDouble(formatter.format(((orderLineFinalPrice*posOrderSoldAmt))));}
		return salesDollers;
	 }

	/**
	 * method to calculate templateSoldAmt and Logic to count the fractions in consideration
	 * @param orderLineQuentity
	 * @param lineSoldAmt1
	 * @param templateQuentity
	 * @return
	 */
	private double getTemplateSoldAmt(double orderLineQuentity,double lineSoldAmt1, double templateQuentity) {
		DecimalFormat formatter = new DecimalFormat("0.00");
		String tempTemplateStringOne = "";
			if (orderLineQuentity != 0) {
				tempTemplateStringOne = formatter.format((lineSoldAmt1 * templateQuentity)/ orderLineQuentity);
			} else {
				tempTemplateStringOne = formatter.format((lineSoldAmt1 * templateQuentity));
			}
		return Double.parseDouble(tempTemplateStringOne);	
	}

	/**
	 * method to calculate lcAmtPaid and Logic to count the fractions in consideration
	 * @param orderLineFinalPrice
	 * @param finalLcPrice
	 * @param lineSoldAmt1
	 * @return
	 */
	private double getlcAmtPaid(double orderLineFinalPrice,double finalLcPrice, double lineSoldAmt1) {
		DecimalFormat formatter = new DecimalFormat("0.00");
		String tempLicenceStringOne = "";
			if (orderLineFinalPrice != 0) {
				tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1)/ (orderLineFinalPrice));
			} else {
				tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
			}
		return Double.parseDouble(tempLicenceStringOne);
	}

	/**
	 * method to calculate linesoldAmt and Logic to count the fractions in consideration
	 * @param posOrderOmFinalPrice
	 * @param orderLineFinalPrice
	 * @param soldAmtOmOrder
	 * @return
	 */
	private double getLineSoldAmt(double posOrderOmFinalPrice,double orderLineFinalPrice, double soldAmtOmOrder) {
		DecimalFormat formatter = new DecimalFormat("0.00");
		String tempOrderLineStringOne = "";
		if (posOrderOmFinalPrice != 0) {
				tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder)/ (posOrderOmFinalPrice));
			} else {
				tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
			}
		return Double.parseDouble(tempOrderLineStringOne);	
	}

	/**
	 * Method to get PosMssFeedProduct when ItemNotFound
	 * @param sysOrderId
	 * @param orderNbr
	 * @param orderPlacedDttm
	 * @param posList
	 * @param omPosTranByWicUpcBean
	 * @param soldAmtOmOrder
	 * @param posPrintsReturned
	 * @return
	 * @throws Exception
	 */
	public OmPosTranByWicUpcBean getPosMssFeedProductItemNotFoundMethod(
			long sysOrderId, int orderNbr, String orderPlacedDttm,PosList posList,POSOrder posOrder,
			OmPosTranByWicUpcBean omPosTranByWicUpcBean,OrderBean orderBean) throws Exception {
				
		/** Changes for JIRA#624 starts here **/
		// Set default product number when product is not found
		omPosTranByWicUpcBean.setProductNbr("0");
	    /** Changes for JIRA#624 ends here **/
		
		/**fetch processingTypeCd from OM_ORDER_ATTRIBUTE*/					
		String processingTypeCd = posOrder.getProcessingTypeCd();	 
		
		/**set LocationNbr,EnvelopNbr,OrderNbr,BusinessDate,SysStorePosId,TransactionTypeCd into omPosTranByWicUpcBean*/
		omPosTranByWicUpcBean.setLocationNbr(Integer.parseInt(posList.getPosDetails().getLocationNumber()));
		omPosTranByWicUpcBean.setEnvelopNbr(Integer.parseInt(posList.getPosDetails().getEnvelopeNbr()));
		omPosTranByWicUpcBean.setOrderNbr(orderNbr);
		omPosTranByWicUpcBean.setBusinessDate(posList.getPosDetails().getPosBusinessDate());							   
		omPosTranByWicUpcBean.setSysStorePosId(orderBean.getSysStorePosId());
		omPosTranByWicUpcBean.setTransactionTypeCd(posList.getPosDetails().getPosTrnType());	
		
		/**Set SalesDollers as Sold Amount of the transaction*/
		omPosTranByWicUpcBean.setSalesDollers(Double.parseDouble(posList.getPosDetails().getPosSoldAmount()));	
		
		/**Set Upc,Wic,CostDollers*/				
		if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_H)){
			omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_H_PATTERN);
			omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_DEFAULT_PATTERN);
			/**calculate CostDollers*/
			if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)||
					posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)){
				   omPosTranByWicUpcBean.setCostDollers(0);
			}else{
				   omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format((25*(Double.parseDouble(posList.getPosDetails().getPosSoldAmount())))/100)));
			}	
		}else if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_S)){
			omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_S_PATTERN);
			omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_DEFAULT_PATTERN);
			/**calculate CostDollers*/
			if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)||
					posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)){
				   omPosTranByWicUpcBean.setCostDollers(0);
			}else{
				   omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format((67*(Double.parseDouble(posList.getPosDetails().getPosSoldAmount())))/100)));
			}
		}		
		/**calculate sells unit*/
		if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)|| 
				posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)|| 
			    posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {			
			       omPosTranByWicUpcBean.setSalesUnits(1);			
		}else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)|| 
				     posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)){
			 omPosTranByWicUpcBean.setSalesUnits(-1);
		}
		/**Set DefaultCostCd*/
		omPosTranByWicUpcBean.setDefaultCostCd(1);	
		return omPosTranByWicUpcBean;
	}

	/**
	 * Method to get PosMssFeed when ProductNotFound
	 * @param sysOrderId
	 * @param orderNbr
	 * @param sysOrderLineID
	 * @param orderPlacedDttm
	 * @param posList
	 * @param omPosTranByWicUpcBean
	 * @param soldAmtOmOrder
	 * @param sysProductId
	 * @param posPrintsReturned
	 * @return
	 * @throws Exception
	 */
	public OmPosTranByWicUpcBean getPosMssFeedProductNotFoundMethod(double salesDollers,
			long sysOrderId, int orderNbr, long sysOrderLineID, String orderPlacedDttm, 
			PosList posList, OmPosTranByWicUpcBean omPosTranByWicUpcBean, double posOrderSoldAmt,POSOrder posOrder, 
			long sysProductId,int orderLineQuantity,OrderBean orderBean,double orderLineSoldAmt) throws Exception {
						
		
		/** Changes for JIRA#624 starts here **/
		// Set default product number when product is not found
		omPosTranByWicUpcBean.setProductNbr("0");
	    /** Changes for JIRA#624 ends here **/
	    
		/**Set LocationNbr,EnvelopNbr,OrderNbr,BusinessDate,SysStorePosId,TransactionTypeCd related to a particular order*/
		omPosTranByWicUpcBean.setLocationNbr(Integer.parseInt(posList.getPosDetails().getLocationNumber()));
		omPosTranByWicUpcBean.setEnvelopNbr(Integer.parseInt(posList.getPosDetails().getEnvelopeNbr()));
		omPosTranByWicUpcBean.setOrderNbr(orderNbr);
		omPosTranByWicUpcBean.setBusinessDate(posList.getPosDetails().getPosBusinessDate());
		omPosTranByWicUpcBean.setSysStorePosId(orderBean.getSysStorePosId());
		omPosTranByWicUpcBean.setTransactionTypeCd(posList.getPosDetails().getPosTrnType());
		
		/**Queries to fetch processingTypeCd from OM_ORDER_ATTRIBUTE*/						
		String processingTypeCd = posOrder.getProcessingTypeCd();	
		
		/**Set SalesDollers*/
		omPosTranByWicUpcBean.setSalesDollers(salesDollers);		
		/**Set Wic,Upc,CostDollers*/
		if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_H)){
			omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_H_PATTERN);
			omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_H_PATTERN);
			/**calculate CostDollers */
			if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)|| 
			   posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)){
				      omPosTranByWicUpcBean.setCostDollers(0);
			}else{
				    //omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format((25*(Double.parseDouble(posList.getPosDetails().getPosSoldAmount())))/100)));
				    omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format((25*(salesDollers))/100)));
			}			
		}else if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_S)){
			omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_S_PATTERN);
			omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_S_PATTERN);
			/**calculate CostDollers */
			if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)|| 
					posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)){
				      omPosTranByWicUpcBean.setCostDollers(0);
			}else{
				     //omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format((67*(Double.parseDouble(posList.getPosDetails().getPosSoldAmount())))/100)));
				     omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format((67*(salesDollers))/100)));
			}
		}						
		/**calculate sells unit*/
		if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS) || 
				posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {							
			          omPosTranByWicUpcBean.setSalesUnits(orderLineQuantity);			
		}else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {
			          omPosTranByWicUpcBean.setSalesUnits(orderLineQuantity*(-1));  		
		}else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)|| 
			     posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)){
		 omPosTranByWicUpcBean.setSalesUnits(-1);
	     }
		/**Set DefaultCostCd*/
		omPosTranByWicUpcBean.setDefaultCostCd(1);
		
		return omPosTranByWicUpcBean;
		}


	/**
	 * Method to get PosMssFeed when ProductFound
	 * @param sysOrderId
	 * @param orderNbr
	 * @param sysOrderLineID
	 * @param orderPlacedDttm
	 * @param posList
	 * @param omPosTranByWicUpcBean
	 * @param soldAmtOmOrder
	 * @param sysProductId
	 * @param orderLineQuantity 
	 * @param omOrderLineCost 
	 * @return
	 * @throws Exception
	 */
	public OmPosTranByWicUpcBean getPosMssFeedProductFoundMethod(double salesDollers,
			long sysOrderId, int orderNbr, long sysOrderLineID,String orderPlacedDttm, PosList posList,
			OmPosTranByWicUpcBean omPosTranByWicUpcBean,double posOrderSoldAmt,
			long sysProductId, POSOrder posOrder,double omOrderLineCost, int orderLineQuantity,OrderBean orderBean) throws Exception {
								
		/**Set LocationNbr,EnvelopNbr,OrderNbr,BusinessDate,SysStorePosId,TransactionTypeCd related to a particular order*/
		omPosTranByWicUpcBean.setLocationNbr(Integer.parseInt(posList.getPosDetails().getLocationNumber()));
		omPosTranByWicUpcBean.setEnvelopNbr(Integer.parseInt(posList.getPosDetails().getEnvelopeNbr()));
		omPosTranByWicUpcBean.setOrderNbr(orderNbr);
		omPosTranByWicUpcBean.setBusinessDate(posList.getPosDetails().getPosBusinessDate());   
		omPosTranByWicUpcBean.setSysStorePosId(orderBean.getSysStorePosId());
		omPosTranByWicUpcBean.setTransactionTypeCd(posList.getPosDetails().getPosTrnType());		
		
		/**get processingTypeCd,costCalStatusCd from OM_ORDER_ATTRIBUTE*/
		String processingTypeCd = posOrder.getProcessingTypeCd();
		String costCalStatusCd = posOrder.getCostCalculationCd();
		
		/**get MSS_REPORT_BY from OM_PRODUCT_ATTRIBUTE table */
		String itemMovementRepQuery = posOrderQuery.getItemMovementRepQuery();
		String mssReportByType = jdbcTemplate.queryForObject(itemMovementRepQuery,new Object[]{sysProductId},String.class);		
	    
		/**Queries to fetch details from OM_PRODUCT*/
	    String productDetailQuery = posOrderQuery.getProductDetailQuery();
	    List<OmProductDetailBean> omProductDetailBeanList = new ArrayList<OmProductDetailBean>();
	    omProductDetailBeanList = jdbcTemplate.query(productDetailQuery,new OmProductDetailBeanRowMapper(),new Object[]{sysProductId});
	    
	    /** Changes for JIRA#624 starts here **/
	    if(null != omProductDetailBeanList && omProductDetailBeanList.size() > 0){
	    	omPosTranByWicUpcBean.setProductNbr(omProductDetailBeanList.get(0).getProductNbr());
	    }else{
	    	// Set default product number when product is not found
	    	omPosTranByWicUpcBean.setProductNbr("0");
	    }
	    /** Changes for JIRA#624 ends here **/
	    
		//-----------------------------------------------------
	    /**pattern matcher for WIC and UPC*/
		/*Pattern wicPattern = Pattern.compile("[0]{1,6}$");
		Matcher wicMatcher = null;
		if(omProductDetailBeanList.get(0).getWic().toString().equalsIgnoreCase(PhotoOmniConstants.BLANK)){
		     wicMatcher = wicPattern.matcher("000000");
		}else {
			 wicMatcher = wicPattern.matcher(omProductDetailBeanList.get(0).getWic().toString());
		}		
		Pattern upcPattern = Pattern.compile("[0]{1,11}$");
		Matcher upcMatcher = null;
		if(omProductDetailBeanList.get(0).getUpc().toString().equalsIgnoreCase(PhotoOmniConstants.BLANK)){
		      upcMatcher = upcPattern.matcher("00000000000");
		}else {
			  upcMatcher = upcPattern.matcher(omProductDetailBeanList.get(0).getUpc().toString());
		}*/
	    //String BLANK = " ";
	    //Change for JIRA 526 - STARTS
	    //-----------------------------------------------------
	    
	    //Initializes the variables
	    boolean isWicNull = false, isUpcNull = false;
	    Pattern wicPattern = Pattern.compile("[0]{1,6}$");
		//public static Matcher wicMatcher = null;
		Pattern upcPattern = Pattern.compile("[0]{1,11}$");
		//public static Matcher upcMatcher = null;
		
		
		
		//get the WIC and UPC from OM_PRODUCT Table
		String wic = omProductDetailBeanList.get(0).getWic().toString();
	    String upc = omProductDetailBeanList.get(0).getUpc().toString();
	    
	    //check if WIC = "" OR {000000}
	    if (wic.equalsIgnoreCase(PhotoOmniConstants.BLANK) || wic.trim().equalsIgnoreCase("") || wicPattern.matcher(wic).matches()) {
			isWicNull = true;
		}
	    //check if UPC = "" OR {00000000000}
		if (upc.equalsIgnoreCase(PhotoOmniConstants.BLANK) || upc.trim().equalsIgnoreCase("") || upcPattern.matcher(upc).matches()) {
			isUpcNull = true;
		}
		
		if (isWicNull && !isUpcNull) {
			/** Report as it is */
			omPosTranByWicUpcBean.setUpc(omProductDetailBeanList.get(0).getUpc().toString());
	    	omPosTranByWicUpcBean.setWic(omProductDetailBeanList.get(0).getWic().toString());

		} else if (!isWicNull && isUpcNull) {
			/** Report as it is */
			omPosTranByWicUpcBean.setUpc(omProductDetailBeanList.get(0).getUpc().toString());
	    	omPosTranByWicUpcBean.setWic(omProductDetailBeanList.get(0).getWic().toString());
		} else if (isWicNull && isUpcNull) {
			/** set default Upc and Wic */
			if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_H)){
				omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_H_PATTERN);
				omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_H_PATTERN);									
			}else if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_S)){
				omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_S_PATTERN);
				omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_S_PATTERN);								
			}
			//**Set DefaultCostCd *//*    
	         omPosTranByWicUpcBean.setDefaultCostCd(1);
		} else {
			omPosTranByWicUpcBean.setUpc(omProductDetailBeanList.get(0).getUpc().toString());
	    	omPosTranByWicUpcBean.setWic(omProductDetailBeanList.get(0).getWic().toString());
		}

	    //Change for JIRA 526 - ENDS
	/*	if( (upcMatcher.matches() || omProductDetailBeanList.get(0).getUpc().toString().equalsIgnoreCase(PhotoOmniConstants.BLANK))	
	     && (!wicMatcher.matches() || !omProductDetailBeanList.get(0).getWic().toString().equalsIgnoreCase(PhotoOmniConstants.BLANK))){
	    	*//**	Report as it is *//*
	    	omPosTranByWicUpcBean.setUpc(omProductDetailBeanList.get(0).getUpc().toString());
	    	omPosTranByWicUpcBean.setWic(omProductDetailBeanList.get(0).getWic().toString());
	    	
	    }else if( (!upcMatcher.matches() || !omProductDetailBeanList.get(0).getUpc().toString().equalsIgnoreCase(PhotoOmniConstants.BLANK))	
			     && (wicMatcher.matches() || omProductDetailBeanList.get(0).getWic().toString().equalsIgnoreCase(PhotoOmniConstants.BLANK))){					    	
	    	*//**	Report as it is *//*
	    	omPosTranByWicUpcBean.setUpc(omProductDetailBeanList.get(0).getUpc().toString());
	    	omPosTranByWicUpcBean.setWic(omProductDetailBeanList.get(0).getWic().toString());
	    }						     
	    else if( (upcMatcher.matches() || omProductDetailBeanList.get(0).getUpc().toString().equalsIgnoreCase(PhotoOmniConstants.BLANK))
	    	   && (wicMatcher.matches() || omProductDetailBeanList.get(0).getWic().toString().equalsIgnoreCase(PhotoOmniConstants.BLANK))){
	    	*//**set Upc and Wic *//*				    	
			if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_H)){
				omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_H_PATTERN);
				omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_H_PATTERN);									
			}else if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_S)){
				omPosTranByWicUpcBean.setUpc(PhotoOmniConstants.UPC_S_PATTERN);
				omPosTranByWicUpcBean.setWic(PhotoOmniConstants.WIC_S_PATTERN);								
			}
			*//**Set DefaultCostCd *//*    
	         omPosTranByWicUpcBean.setDefaultCostCd(1);
	     }else{
	    	    omPosTranByWicUpcBean.setUpc(omProductDetailBeanList.get(0).getUpc().toString());
				omPosTranByWicUpcBean.setWic(omProductDetailBeanList.get(0).getWic().toString());
	     }*/			
	        /**Set SalesDollers */
			omPosTranByWicUpcBean.setSalesDollers(salesDollers);																		
			/**Set CostDollers */
			if(omOrderLineCost >= 0){
				if (posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)|| posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)){
					      omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format(omOrderLineCost)));
				}else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)){
					     double tempCostDollersOne = (omOrderLineCost*(-1));
					     omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format(tempCostDollersOne)));
				}else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)|| posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)){
					        omPosTranByWicUpcBean.setCostDollers(0);
				}
			}else if(!costCalStatusCd.equalsIgnoreCase(PhotoOmniConstants.COST_CALCULATION_STATUS_CD)){	
				         if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_H)){															
					         double tempCostDollers = ((25*(omPosTranByWicUpcBean.getSalesDollers()))/100);
					         omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format(tempCostDollers))); 
				         }else if(processingTypeCd.equalsIgnoreCase(PhotoOmniConstants.PROCESSING_TYPE_CD_S)){  				
					         double tempCostDollers = ((67*(omPosTranByWicUpcBean.getSalesDollers()))/100);	
					         omPosTranByWicUpcBean.setCostDollers(Double.parseDouble(decimalFormatter.format(tempCostDollers))); 
				         }
				         /**Set DefaultCostCd*/    
				         omPosTranByWicUpcBean.setDefaultCostCd(1);
			}																	
	    /**calculate sells unit*/
	    if(!CommonUtil.isNull(mssReportByType)){
		    if(mssReportByType.equalsIgnoreCase(PhotoOmniConstants.MSS_REPORT_BY_ITEM)){			
			    if (posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)|| 
			    		posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {					
				             omPosTranByWicUpcBean.setSalesUnits(1);			
			    }else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)){				     
				             omPosTranByWicUpcBean.setSalesUnits(-1);	
			    }else if (posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)|| 
			    		posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {			     
			                 omPosTranByWicUpcBean.setSalesUnits(-1);	
			                 }
			}else if(mssReportByType.equalsIgnoreCase(PhotoOmniConstants.MSS_REPORT_BY_QUANTITY)){																											
				if (posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)|| 
						posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {					
					         omPosTranByWicUpcBean.setSalesUnits(orderLineQuantity);			
				}else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)){
					         omPosTranByWicUpcBean.setSalesUnits((-1)*orderLineQuantity);	
		        }else if(posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)|| 
						     posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)){
								 omPosTranByWicUpcBean.setSalesUnits(-1);
				     }								
		        }	
			}
		
	    return omPosTranByWicUpcBean;
	}

	
	/**
	 * Method to get SysStorePosId
	 * @param sysOrderId
	 * @param envelopeNbr
	 * @param posTrnTypeDTTM
	 * @return
	 * @throws ParseException 
	 * @throws DataAccessException 
	 */
	public Long getSysStorePosId(Long sysOrderId, String envelopeNbr,String posTrnTypeDTTM) throws DataAccessException, ParseException {
		String sysStorePosIdQuery = posOrderQuery.getSysStorePosIdQuery();
			Long sysStorePosId = null;
			try {
				sysStorePosId = jdbcTemplate.queryForObject(sysStorePosIdQuery,new Object[] { sysOrderId,envelopeNbr,
						                       formatter.format(formatter.parse(posTrnTypeDTTM))}, Long.class);
			           } catch (Exception e) {
				}
			return sysStorePosId;
			}

	/**
	 * Method to get SysOrderId
	 * @param envelopeNbr
	 * @param string
	 * @param locationNumber
	 * @return
	 * @throws ParseException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OrderBean getOrderId(String envelopeNbr, String posTrnTypeDTTM,String locationNumber) throws RuntimeException, PhotoOmniException, ParseException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering PosOrdersDAOImpl getOrderId");
		}
		
		String getOrdernoQuery = posOrderQuery.getOrderNoQuery();
		OrderBean orderBean = new OrderBean();
		List<OrderBean> omOrderBeanList = new ArrayList<OrderBean>();		
		
		omOrderBeanList = jdbcTemplate.query(getOrdernoQuery, new Object[] {locationNumber,
				Integer.parseInt(envelopeNbr) },new PosOrderBeanRowMapper());
		orderBean = omOrderBeanList.get(0);		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting PosOrdersDAOImpl getOrderId");
		}
		return orderBean;
	}

	/**
	 * Method to insert in OM_POS_TRansaction 
	 * @param orderNo
	 * @param posDetails
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long insertPosDetails(Long sysOrderId, PosDetails posDetails)throws Exception {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl insertPosDetails");
		}		
		/***sysStorePosIdTemp used to catch sequence generated value from OM_POS_TRANSACTION */
		Long sysStorePosIdTemp = null;
		
		String insertPosDetailsQuery = posOrderQuery.getInsertPosDetailsQuery();
			
		String posDiscountUsedInd = null;
		if (posDetails.getPosDiscountUsedInd().equalsIgnoreCase(PhotoOmniConstants.YES_IND)) {
			posDiscountUsedInd = PhotoOmniConstants.POS_CONSTANT_ONE;
		} else if (posDetails.getPosDiscountUsedInd().equalsIgnoreCase(PhotoOmniConstants.NO_IND)) {
			posDiscountUsedInd = PhotoOmniConstants.POS_CONSTANT_ZERO;
		}
		String discountProcessInd = null;
		if (posDetails.getDiscountProcessInd().equalsIgnoreCase(PhotoOmniConstants.POS_CONSTANT_P) ||
		          posDetails.getDiscountProcessInd().equalsIgnoreCase(PhotoOmniConstants.YES_IND)) {
			discountProcessInd = PhotoOmniConstants.POS_CONSTANT_ONE;
		} else if (posDetails.getDiscountProcessInd().equalsIgnoreCase(PhotoOmniConstants.NO_IND)) {
			discountProcessInd = PhotoOmniConstants.POS_CONSTANT_ZERO;
		}
		/**Get sysLocationId for the location number present in POS message*/
		String sysLocationId = getSysLocationId(posDetails.getLocationNumber());	
					
		/** If NULL sysOrderId received */
		if(CommonUtil.isNull(sysOrderId)) {			
			/**Check if record already exists for the Same POS message in the  OM_POS_TRANSACTION table */
		    String nullSysStorePosIdQuery = posOrderQuery.checkNullSysStorePosIdQuery();			    
		    List<PosTransactionDetailBean> tempNullPosTransactionDetailBeanList = new ArrayList<PosTransactionDetailBean>();
		    try{		    	
		    	tempNullPosTransactionDetailBeanList = jdbcTemplate.query(nullSysStorePosIdQuery, 
		    		            new Object[]{posDetails.getEnvelopeNbr(),posDetails.getPosBusinessDate(),posDetails.getPosSoldAmount(),
		    			        posDetails.getPosSequenceNbr(),posDetails.getPosRegisterNbr(),posDetails.getPrintsReturned(),
		    			        posDetails.getPosLedgerNbr(),posDetails.getPosTrnTypeDTTM(),posDetails.getEmployeeId(),posDetails.getPosTrnType()},
		    		            new PosTransactionDetailBeanRowMapper());			    	
		    }catch (Exception e){
		    	if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("No POS details already Exist in the OM_POS_TRANSACTION table for the POS message Received ");}
		    }
		   
		    /**If record don't exist for the POS message Received insert new record in OM_POS_TRANSACTION table*/
		    if(tempNullPosTransactionDetailBeanList.size() == 0){
		    	
		    	String sysStorePosIdTempQuery = posOrderQuery.getSysStorePosIdTempQuery();
		    	sysStorePosIdTemp = jdbcTemplate.queryForObject(sysStorePosIdTempQuery,Long.class);
		    	
			  jdbcTemplate.update(insertPosDetailsQuery,new Object[] {
					        sysStorePosIdTemp,
					        null,
					        sysLocationId,
					        posDetails.getPosTrnTypeDTTM(),
							posDetails.getPosTrnType(), 
							posDetails.getPosSoldAmount(),
							posDetails.getPosBusinessDate(),
							posDetails.getPosSequenceNbr(),
							posDetails.getPosRegisterNbr(),
							posDetails.getEnvelopeNbr(),
							posDetails.getPrintsReturned(),
							posDetails.getPosLedgerNbr(),
							posDetails.getEmployeeId(), 
							posDiscountUsedInd,
							discountProcessInd,
							PhotoOmniConstants.CREATE_USER_ID,							
							PhotoOmniConstants.UPDATE_USER_ID
					        });
		       } else {
			    	if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Order details already present for the POS message received");
					}
			    	throw new PhotoOmniException("Order details already present for the POS message received");
			    }
		    
		}else{
			/** If Order details present in Order related tables */
		    /**Check if record already exists for the Same POS message in the  OM_POS_TRANSACTION table */
		    String sysStorePosIdQuery = posOrderQuery.checkSysStorePosIdQuery();
		    List<PosTransactionDetailBean> tempPosTransactionDetailBeanList = new ArrayList<PosTransactionDetailBean>();
		    try{		    	
		    	tempPosTransactionDetailBeanList = jdbcTemplate.query(sysStorePosIdQuery, 
		    		            new Object[]{sysOrderId,posDetails.getEnvelopeNbr(),
		    			        posDetails.getPosBusinessDate(),posDetails.getPosSoldAmount(),posDetails.getPosSequenceNbr(),
		    			        posDetails.getPosRegisterNbr(),posDetails.getPrintsReturned(),posDetails.getPosLedgerNbr(),
		    			        posDetails.getPosTrnTypeDTTM(),posDetails.getEmployeeId(),posDetails.getPosTrnType()},
		    		new PosTransactionDetailBeanRowMapper());			    	
		    }catch (Exception e){
		    	if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("No POS details already Exist for the current sysOrderId in OM_POS_TRANSACTION table");
				}
		    }
		    
		    /**If record don't exist for the POS message Received insert new record in OM_POS_TRANSACTION table*/
		    if(tempPosTransactionDetailBeanList.size() == 0){
		    	
		    	String sysStorePosIdTempQuery = posOrderQuery.getSysStorePosIdTempQuery();
		    	sysStorePosIdTemp = jdbcTemplate.queryForObject(sysStorePosIdTempQuery,Long.class);	
			  
		    	jdbcTemplate.update(insertPosDetailsQuery,new Object[] {
		    			    sysStorePosIdTemp,
					        sysOrderId,
					        sysLocationId,
					        posDetails.getPosTrnTypeDTTM(),
							posDetails.getPosTrnType(),
							posDetails.getPosSoldAmount(),
							posDetails.getPosBusinessDate(),
							posDetails.getPosSequenceNbr(),
							posDetails.getPosRegisterNbr(),
							posDetails.getEnvelopeNbr(),
							posDetails.getPrintsReturned(),
							posDetails.getPosLedgerNbr(),
							posDetails.getEmployeeId(), 
							posDiscountUsedInd,
							discountProcessInd,
							PhotoOmniConstants.CREATE_USER_ID,						
							PhotoOmniConstants.UPDATE_USER_ID
					        });
		    } else {		    	
	    	    if (LOGGER.isDebugEnabled()) {
				   LOGGER.debug("Order details already present for the POS message received");
			       }
	    	         throw new PhotoOmniException("Order details already present for the POS message received");
	    	    }		    	
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting PosOrdersDAOImpl insertPosDetails");
		}
		
		return sysStorePosIdTemp;
	}

	/**
	 * @param locationNumber
	 * @return sysLocationId
	 */
	public String getSysLocationId(String locationNumber) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering PosOrdersDAOImpl getSysLocationId()");
		}

		String sysLocationIdQuery = posOrderQuery.getSysLocationIdQuery();
		String sysLocationId = "";
		sysLocationId = jdbcTemplate.queryForObject(sysLocationIdQuery,new Object[] { locationNumber }, String.class);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting PosOrdersDAOImpl getSysLocationId()");
		}
		return sysLocationId;
	}

	/**
	 * @param orderHistoryBean
	 * @return boolean
	 * @throws ParseException 
	 */

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertOrderHistoryDetails(OrderHistoryBean orderHistoryBean)throws RuntimeException, PhotoOmniException, ParseException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl insertOrderHistoryDetails() ");
		}
		String insertOrderHistoryQuery = posOrderQuery.getInsertOrderHistoryQuery();	
		jdbcTemplate.update(insertOrderHistoryQuery,
				        new Object[] {formatter.format(formatter.parse(orderHistoryBean.getOrderPlacedDttm().toString())),
						orderHistoryBean.getOrderId(),
						orderHistoryBean.getAction(),
						formatter.format(formatter.parse(orderHistoryBean.getActionDttm().toString())),
						orderHistoryBean.getOrderStatus(),
						orderHistoryBean.getActionNotes(),
						orderHistoryBean.getExceptionId(),
						orderHistoryBean.getCreateUserId(),
						orderHistoryBean.getUpdateUserId()
				        });		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDAOImpl insertOrderHistoryDetails");
		}		
	}

	/**
	 * @param sysOrderId
	 * @param orderPlacedDttm 
	 * @return orderLineBeansList
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<OrderLineBean> getOrderLineDetails(long sysOrderId, String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl getOrderLineDetails()");}

		List<OrderLineBean> orderLineBeansList = new ArrayList<OrderLineBean>();
		String orderLineDetailsQuery = posOrderQuery.getOrderLineDetailsQuery();
		orderLineBeansList = jdbcTemplate.query(orderLineDetailsQuery,
				new OrderLineBeanRowMapper(), new Object[] { sysOrderId ,orderPlacedDttm});

		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDAOImpl getOrderLineDetails()");}
		return orderLineBeansList;
	}
	/**
	 * @param orderPlacedDttm 
	 * @param orderLineId
	 * @return List<LicenceContentPosBean>
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LicenceContentPosBean> getLicenceContentDetails(long sysOrderLineId, String orderPlacedDttm) throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl getLicenceContentDetails()");}
		
		List<LicenceContentPosBean> licenceContentPosBeanList = new ArrayList<LicenceContentPosBean>();
		String licenceContentDetailsQuery = posOrderQuery.getlicenceContentDetailsQuery();
		licenceContentPosBeanList = jdbcTemplate.query(licenceContentDetailsQuery,new LicenceContentPosBeanRowMapper(),new Object[] { sysOrderLineId,orderPlacedDttm });
		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDAOImpl getLicenceContentDetails()");}
		return licenceContentPosBeanList;
	}
	/**
	 * @param sysOrderLineId
	 * @param orderPlacedDttm 
	 * @return List<OrderLineTemplateBean>
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<OrderLineTemplateBean> getOrderLineTemplateDetails(long sysOrderLineId, String orderPlacedDttm) throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl getOrderLineTemplateDetails()");
		}
		List<OrderLineTemplateBean> orderLineTemplateBeansList = new ArrayList<OrderLineTemplateBean>();
		String orderLineTemplateBeansListQuery = posOrderQuery.getorderLineTemplateBeansListQuery();
		orderLineTemplateBeansList = jdbcTemplate.query(orderLineTemplateBeansListQuery,
				new OrderLineTemplateBeanRowMapper(),new Object[] { sysOrderLineId ,orderPlacedDttm});
		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDAOImpl getOrderLineTemplateDetails()");
		}
		return orderLineTemplateBeansList;
	}
	/**
	 * @param lcAmtPaid
	 * @param sysOlLcId
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatelcAmtPaidToLicemceContent(double lcAmtPaid, long sysOlLcId,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering  PosOrdersDAOImpl updatelcAmtPaidToLicemceContent()");}

		String updatecAmtPaidToLicemceContentQuery = posOrderQuery.getcAmtPaidToLicemceContentQuery();
		jdbcTemplate.update(updatecAmtPaidToLicemceContentQuery, new Object[] {lcAmtPaid, sysOlLcId ,orderPlacedDttm});

		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting  PosOrdersDAOImpl updatelcAmtPaidToLicemceContent()");}
	}

	/**
	 * @param templateSoldAmt
	 * @param sysOlTemplateId
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatelcTemplateSoldAmt(double templateSoldAmt,
			long sysOlTemplateId,String orderPlacedDttm) throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering  PosOrdersDAOImpl updatelcTemplateSoldAmt()");
		}
		String updatecTemplateSoldAmtQuery = posOrderQuery.getupdatecTemplateSoldAmtQuery();
		jdbcTemplate.update(updatecTemplateSoldAmtQuery, new Object[] {templateSoldAmt, sysOlTemplateId,orderPlacedDttm });
		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting  PosOrdersDAOImpl updatelcTemplateSoldAmt()");
		}
	}
	/**
	 * @param lineSoldAmt
	 * @param sysOrderLineId
	 * @param orderPlacedDttm 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateLineSoldAmt(double lineSoldAmt, long sysOrderLineId, String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug(" Entering PosOrdersDaoImpl updateLineSoldAmt() ");}
		
		String updateLineSoldAmtQuery = posOrderQuery.getupdateLineSoldAmtQuery();
		jdbcTemplate.update(updateLineSoldAmtQuery, new Object[] { lineSoldAmt,sysOrderLineId ,orderPlacedDttm});
		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDaoImpl updateLineSoldAmt");}
	}

	/**
	 * @param sysOrderId
	 * @param orderPlacedDttm 
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public POSOrder getOrderDetailsOmOrder(long sysOrderId, String orderPlacedDttm)throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl getOrderDetailsOmOrder");}

		POSOrder posOrder = new POSOrder();
		String omOrderDetailsQuery = posOrderQuery.getOmOrderDetailsQuery();
		posOrder = jdbcTemplate.queryForObject(omOrderDetailsQuery,new Object[] { sysOrderId,orderPlacedDttm }, new PosOrderRowMapper());
		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl getOrderDetailsOmOrder");}
		return posOrder;
	}
	/**
	 * @param soldAmtOmOrder
	 * @param sysOrderId
	 * @param orderPlacedDttm 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSoldAmtOmOrder(double soldAmtOmOrder, long sysOrderId, String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl updateSoldAmtOmOrder()");}
		
		String updateSoldAmtQuery = posOrderQuery.getupdateSoldAmtQuery();
		jdbcTemplate.update(updateSoldAmtQuery, new Object[] { soldAmtOmOrder,sysOrderId,orderPlacedDttm });
		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDAOImpl updateSoldAmtOmOrder()");}
	}
	/**
	 * @param posTransactionDttm
	 * @return calenderId
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int getCalenderId(String posTransactionDttm)throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDAOImpl getCalenderId()");
		}
		String calenderIdQuery = posOrderQuery.getCalenderIdQuery();
		int calenderId = 0;
		Date tempDate = new Date(Timestamp.valueOf(posTransactionDttm).getTime());
		SimpleDateFormat formatter = new SimpleDateFormat(PhotoOmniConstants.CALENDER_DATE_PATTERN);
		calenderId = jdbcTemplate.queryForObject(calenderIdQuery,new Object[] { formatter.format(tempDate) }, Integer.class);
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDAOImpl getCalenderId()");
		}
		return calenderId;
	}
	/**
	 * @param calenderId
	 * @return boolean
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePosTranCalIdRtnQty(int printsReturnedQty,int calenderId, long sysOrderId,String orderPlacedDTTM)
			throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDaoImpl updatePosTranCalId()");
		}	
		String updatePosTranCalIdRtnQtyQuery = posOrderQuery.getUpdatePosTranCalIdRtnQtyQuery();
		jdbcTemplate.update(updatePosTranCalIdRtnQtyQuery, new Object[] {printsReturnedQty,calenderId, sysOrderId ,orderPlacedDTTM});
		
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDaoImpl updatePosTranCalId()");
		}	
	}

	/**
	 * @param printsReturnedQty
	 *            ,sysOrderId
	 * @param orderPlacedDttm 
	 * @return boolean
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updatePrintsReturnedQty(int printsReturnedQty,
			long sysOrderId, String orderPlacedDttm) throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDaoImpl updatePrintsReturnedQty()");
		}
		String updatePrintsReturnedQtyQuery = posOrderQuery.getUpdatePrintsReturnedQty();
		jdbcTemplate.update(updatePrintsReturnedQtyQuery, new Object[] {printsReturnedQty, sysOrderId ,orderPlacedDttm});
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDaoImpl updatePrintsReturnedQty()");
		}
	}

	/**
	 * @param finalOrderStatus
	 *            ,sysOrderId
	 * @param orderPlacedDttm 
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateOrderStatusSoldDttm(String finalOrderStatus, String PosTrnTypeDTTM,long sysOrderId, String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering PosOrdersDaoImpl updateOrderStatus()");
		}
		String updateOrderStatusSoldDttmQuery = posOrderQuery.getUpdateOrderStatusSoldDttm();
		jdbcTemplate.update(updateOrderStatusSoldDttmQuery, new Object[] {finalOrderStatus, PosTrnTypeDTTM,sysOrderId ,orderPlacedDttm});

		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDaoImpl updateOrderStatus()");
		}

	}

	/**
	 * Method to get Decode value
	 * @param string
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String getDecode(String posTranType) throws RuntimeException,
			PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDaoImpl getDecode()");
		}
		String decodeQuery = posOrderQuery.getDecodeQuery();
		String decodeValue = null;
		decodeValue = jdbcTemplate.queryForObject(decodeQuery,new Object[] { posTranType }, String.class);

		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDaoImpl getDecode()");
		}
		return decodeValue;
	}

	/**
	 * Method to get SysExceptionId
	 */
	@Override
	public long getSysExceptionId(long sysOrderId) throws NullPointerException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDaoImpl getSysExceptionId()");
		}
		String sysExceptionIdQuery = posOrderQuery.getSysExceptionIdQuery();
		long sysExceptionId = 0;
		try {
			sysExceptionId = jdbcTemplate.queryForObject(sysExceptionIdQuery,new Object[] { sysOrderId }, Long.class);
		} catch (Exception e) {
			throw new NullPointerException();
		}
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDaoImpl getSysExceptionId()");
		}
		return sysExceptionId;

	}

	/**
	 * Method to update Order SoldDttm
	 * @param sysOrderId
	 * @param posTrnTypeDTTM
	 * @param orderPlacedDttm 
	 */
	@Override
	@Transactional
	public void updateOrderSoldDttm(long sysOrderId, String posTrnTypeDTTM, String orderPlacedDttm)throws RuntimeException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Entering PosOrdersDaoImpl updateOrderSoldDttm()");
		}
		String updateOrderSoldDttmQuery = posOrderQuery.getUpdateOrderSoldDttmQuery();
	    jdbcTemplate.update(updateOrderSoldDttmQuery,new Object[] {posTrnTypeDTTM,sysOrderId,orderPlacedDttm });

		if (LOGGER.isDebugEnabled()) {LOGGER.debug("Exiting PosOrdersDaoImpl updateOrderSoldDttm()");
		}
	}
	
	/**
	 * @param orderDate
	 * @param SysOrderId
	 * @return sysShoppingCartId
	 */
	public long getShoppingCartId(long SysOrderId, String orderDate)
			throws NullPointerException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getShoppingCartId updateOrderSoldDttm()");
		}
		long sysShoppingCartId = 0;
		String shoppingCartQuery = posOrderQuery.getShoppingCartIDquery();
		try {
			sysShoppingCartId = jdbcTemplate.queryForObject(shoppingCartQuery,
					new Object[] { SysOrderId, orderDate }, Long.class);
		} catch (Exception e) {
			throw new NullPointerException();
		}
		return sysShoppingCartId;
	}

}
