package com.walgreens.oms.dao;

import java.text.ParseException;
import java.util.List;

import com.walgreens.oms.bean.LicenceContentPosBean;
import com.walgreens.oms.bean.OmPosTranByWicUpcBean;
import com.walgreens.oms.bean.OrderBean;
import com.walgreens.oms.bean.OrderHistoryBean;
import com.walgreens.oms.bean.OrderLineBean;
import com.walgreens.oms.bean.OrderLineTemplateBean;
import com.walgreens.oms.bean.POSOrder;
import com.walgreens.common.exception.*;
import com.walgreens.oms.json.bean.PosDetails;
import com.walgreens.oms.json.bean.PosList;

/**
 * @author CTS interface used to process POS realtime orders
 * 
 */

public interface PosOrdersDAO {

	/**
	 * @param envelopeNbr
	 * @param locationNumber
	 * @return
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	OrderBean getOrderId(String envelopeNbr,String posTrnTypeDTTM,String locationNumber) throws RuntimeException,
			PhotoOmniException,ParseException;
	
	/**
	 * @param orderNo
	 * @param posDetails
	 * @return
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 * @throws ParseException
	 * @throws Exception 
	 */
	Long insertPosDetails(Long orderNo, PosDetails posDetails)
			throws RuntimeException, PhotoOmniException, ParseException, Exception;

	/**
	 * @param orderHistoryBean
	 * @return boolean
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	void insertOrderHistoryDetails(OrderHistoryBean orderHistoryBean)
			throws RuntimeException, PhotoOmniException,ParseException;

	/**
	 * @param orderId
	 * @return List<OrderLineBean>
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	List<OrderLineBean> getOrderLineDetails(long orderId,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException;

	/**
	 * @param sysOrderLineId
	 * @return List<LicenceContentPosBean>
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	List<LicenceContentPosBean> getLicenceContentDetails(long sysOrderLineId,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException;

	/**
	 * @param lcAmtPaid
	 * @param sysOlLcId
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	void updatelcAmtPaidToLicemceContent(double lcAmtPaid, long sysOlLcId,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException;

	/**
	 * @param sysOrderLineId
	 * @return List<OrderLineTemplateBean>
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	List<OrderLineTemplateBean> getOrderLineTemplateDetails(long sysOrderLineId,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException;

	/**
	 * @param SysOlTemplateId
	 * @param orderLineId
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	void updatelcTemplateSoldAmt(double templateSoldAmt, long sysOlTemplateId,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException;

	/**
	 * @param lineSoldAmt
	 * @param orderLineID
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	void updateLineSoldAmt(double lineSoldAmt, long orderLineID,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException;

	/**
	 * @param orderNo
	 * @return
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	public POSOrder getOrderDetailsOmOrder(long orderNo,String orderPlacedDttm) throws RuntimeException,
			PhotoOmniException;

	/**
	 * @param soldAmtOmOrder
	 * @param orderId
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	void updateSoldAmtOmOrder(double soldAmtOmOrder, long orderId,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException;

	/**
	 * @param posTransactionDttm
	 * @return calenderId
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	int getCalenderId(String posTransactionDttm) throws RuntimeException,
			PhotoOmniException;

	/**
	 * @param calenderId
	 * @param orderId
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	void updatePosTranCalIdRtnQty(int printsReturnedQty,int calenderId, long orderId,String orderPlacedDttm)
			throws RuntimeException, PhotoOmniException;


	/**
	 * @param finalOrderStatus
	 * @param orderId
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 */
	void updateOrderStatusSoldDttm(String finalOrderStatus, String PosTrnTypeDTTM,long sysOrderId, String orderPlacedDttm)throws RuntimeException, PhotoOmniException;

	/**
	 * @param posList
	 * @param reconFlag
	 * @return posList
	 * @throws PhotoOmniException
	 * @throws RuntimeException
	 * @throws ParseException
	 * @throws Exception
	 */
	PosList processPosRequest(PosList posList,boolean reconFlag,OrderBean orderBean) throws RuntimeException,PhotoOmniException, ParseException, Exception;

	/**
	 * @param posOrderSoldAmt
	 * @param posTranType
	 * @param sysOrderId
	 * @param posPrintsReturned
	 * @param posList
	 * @param orderNbr
	 * @return OmPosTranByWicUpcBeanList
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	List<OmPosTranByWicUpcBean> calculateSoldAmount(double posOrderSoldAmt,String posTranType, long sysOrderId,String orderPlacedDttm,
			int posPrintsReturned,PosList posList,int orderNbr,OrderBean orderBean) throws RuntimeException, PhotoOmniException,Exception;
	
	/**
	 * @param posTranType
	 * @return
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	public String getDecode(String posTranType) throws RuntimeException, PhotoOmniException; 
	
	/**
	 * @param orderId
	 * @return SysExceptionId
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	public long getSysExceptionId(long orderId) throws NullPointerException;
	
	/**
	 * @param orderId
	 * @param posTrnTypeDTTM
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	public void updateOrderSoldDttm(long orderId, String posTrnTypeDTTM,String orderPlacedDttm) throws RuntimeException, PhotoOmniException;
	
	/**
	 * @param omPosTranByWicUpcBean
	 * @return
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	public void inserOmPosTransByWicUpc(List<OmPosTranByWicUpcBean> omPosTranByWicUpcBeanList)throws RuntimeException, PhotoOmniException,ParseException,Exception;
	
	/**
	 * @param posList
	 * @param orderNbr
	 * @param sysOrderId
	 * @return OmPosTranByWicUpcBean
	 */
	public OmPosTranByWicUpcBean getPosMssFeedOrderNotFoundMethod(PosList posList,Long sysStorePosId)throws Exception;
	
	/**
	 * @param OmPosTranByWicUpcBean
	 * @return boolean
	 */
	public void deleteMssRecord(final List<Long> sysPosTransByProductIdList);

	/**
	 * @param printsReturnedQty
	 * @param sysOrderId
	 * @param orderPlacedDttm
	 * @return
	 * @throws RuntimeException
	 * @throws PhotoOmniException
	 */
	void updatePrintsReturnedQty(int printsReturnedQty, long sysOrderId,
			String orderPlacedDttm) throws RuntimeException,
			PhotoOmniException;
	
	/**
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
			long sysOrderId, int orderNbr, String orderPlacedDttm, 
			PosList posList,POSOrder posOrder,OmPosTranByWicUpcBean omPosTranByWicUpcBean,OrderBean orderBean) throws Exception ;
	
	/**
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
	public OmPosTranByWicUpcBean getPosMssFeedProductNotFoundMethod(double salesDollers, long sysOrderId, int orderNbr, long sysOrderLineID, String orderPlacedDttm, PosList posList,
			OmPosTranByWicUpcBean omPosTranByWicUpcBean, double posOrderSoldAmt, POSOrder posOrder, long sysProductId, int orderLineQuantity,OrderBean orderBean,double orderLineSoldAmt) throws Exception;
	
	/**
	 * @param sysOrderId
	 * @param orderNbr
	 * @param sysOrderLineID
	 * @param orderPlacedDttm
	 * @param posList
	 * @param omPosTranByWicUpcBean
	 * @param soldAmtOmOrder
	 * @param sysProductId
	 * @param i 
	 * @param d 
	 * @param posOrder 
	 * @return
	 * @throws Exception
	 */
	public OmPosTranByWicUpcBean getPosMssFeedProductFoundMethod(double salesDollers, long sysOrderId, int orderNbr, long sysOrderLineID, String orderPlacedDttm, PosList posList, 
			OmPosTranByWicUpcBean omPosTranByWicUpcBean, double posOrderSoldAmt, long sysProductId, POSOrder posOrder, double omOrderLineCost, int orderLineQuantity,OrderBean orderBean) throws Exception; 
	
	
	public long getShoppingCartId(long SysOrderId,String orderDate) throws NullPointerException;
}
