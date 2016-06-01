package com.walgreens.omni.bo;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.omni.dao.ReportsDAOImplTest;
import com.walgreens.omni.json.bean.CannedDropDownDataTest;
import com.walgreens.omni.json.bean.InputChannel;
import com.walgreens.omni.json.bean.OrderType;
import com.walgreens.omni.json.bean.SimRetailBlocOnloadList;
import com.walgreens.omni.json.bean.SimRetailBlockOnloadResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportRespMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReq;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReqMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateRespMsg;

public class ReportsBOImplTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportsBOImplTest.class);

	private ReportsDAOImplTest reportsDAOImplTest;

	/**Added for sims retail block*/	
	private static final SimRetailBlockOnloadResp simRetailBlockOnloadRespExpected = new SimRetailBlockOnloadResp();
	private static final SimRetailBlocOnloadList simRetailBlocOnloadList = new SimRetailBlocOnloadList();
	private static final SimRetailBlockReportRespMsg simRetailBlockReportRespMsgExpected = new SimRetailBlockReportRespMsg();
	private static final SimRetailBlockReportResp simRetailBlockReportResp = new SimRetailBlockReportResp();
	private static final SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg = new SimRetailBlockUpdateReqMsg();
	private static final SimRetailBlockUpdateReq simRetailBlockUpdateReq = new SimRetailBlockUpdateReq();
	private static final SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsgExpected = new SimRetailBlockUpdateRespMsg();
	
	/**Added for canned report*/
	private static final CannedDropDownDataTest cannedDataList = new CannedDropDownDataTest();
	private static final List<OrderType> orderTypeList = new ArrayList<OrderType>();
	private static final List<InputChannel> inputChannelList = new ArrayList<InputChannel>();

	/**
	 * Method to test setUp Mock up Test Data
	 * @throws PhotoOmniException 
	 */
	@Before
	public void setUp() throws PhotoOmniException {

		reportsDAOImplTest = createMock(ReportsDAOImplTest.class);

		simRetailBlocOnloadList.setRetailBlock("30");
		List<SimRetailBlocOnloadList> SimRetailBlocOnloadListTest = new ArrayList<SimRetailBlocOnloadList>();
		SimRetailBlocOnloadListTest.add(simRetailBlocOnloadList);
		simRetailBlockOnloadRespExpected.setSimRetailBlocOnloadList(SimRetailBlocOnloadListTest);

		simRetailBlockReportResp.setDescription("US");
		simRetailBlockReportResp.setRetailBlock("30");
		simRetailBlockReportResp.setStoreNumber("59156");
		simRetailBlockReportResp.setTotalrecords("1");
		List<SimRetailBlockReportResp> simRetailBlockReportRespList = new ArrayList<SimRetailBlockReportResp>();
		simRetailBlockReportRespList.add(simRetailBlockReportResp);
		simRetailBlockReportRespMsgExpected.setSimRetailBlockReportResp(simRetailBlockReportRespList);

		simRetailBlockUpdateReq.setRetailBlock("30");
		List<String> storeNumber = new ArrayList<String>();
		storeNumber.add("59156");
		simRetailBlockUpdateReq.setStoreNumber(storeNumber);
		simRetailBlockUpdateReqMsg.setSimRetailBlockUpdateReq(simRetailBlockUpdateReq);
		simRetailBlockUpdateRespMsgExpected.setUpdateStatusMessage("Update sucessfull");
		
		OrderType orderTypeOne = new OrderType();
		orderTypeOne.setOrderId("SOP");		
		orderTypeOne.setOrderTypeName("Sold Order By Product");
		OrderType orderTypeTwo = new OrderType();
		orderTypeTwo.setOrderId("POP");
		orderTypeTwo.setOrderTypeName("Placed Order By Product");
		
		orderTypeList.add(orderTypeOne);
		orderTypeList.add(orderTypeTwo);
		
		InputChannel inputChannelOne = new InputChannel();
		inputChannelOne.setInputChannelId("Kiosk");
		inputChannelOne.setInputChannelName("Kiosk");
		InputChannel inputChannelTwo = new InputChannel();
		inputChannelTwo.setInputChannelId("Internet");
		inputChannelTwo.setInputChannelName("Internet");
		InputChannel inputChannelThree = new InputChannel();  
		inputChannelThree.setInputChannelId("Mobile");
		inputChannelThree.setInputChannelName("Mobile");
		InputChannel inputChannelFour = new InputChannel();
		inputChannelFour.setInputChannelId("A");
		inputChannelFour.setInputChannelName("All");
		
		inputChannelList.add(inputChannelOne);
		inputChannelList.add(inputChannelTwo);
		inputChannelList.add(inputChannelThree);
		inputChannelList.add(inputChannelFour);
		
		cannedDataList.setInputChannel(inputChannelList);
		cannedDataList.setOrderType(orderTypeList);

		expect(reportsDAOImplTest.updateRetailBlockRequest()).andReturn(simRetailBlockUpdateRespMsgExpected);
		expect(reportsDAOImplTest.genarateSimRetailBlockReport()).andReturn(simRetailBlockReportRespMsgExpected);
		expect(reportsDAOImplTest.submitSimRetailBlockOnloadRequest()).andReturn(simRetailBlockOnloadRespExpected);
		
		expect(reportsDAOImplTest.getCannedInputType()).andReturn(inputChannelList);
		expect(reportsDAOImplTest.getCannedReportOrderType()).andReturn(orderTypeList);

		replay(reportsDAOImplTest);
	}
	   
	    		
	/**
	 * @return
	 * @throws PhotoOmniException
	 */
		@Test
	public void getCannedReportOrderTypeInputChannelRequestTest()
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getCannedReportOrderTypeInputChannelRequest method of MachineReportBOImpl ");
		}
		List<OrderType> cannedOrderTypeList = new ArrayList<OrderType>();
		List<InputChannel> cannedInputChannelList = new ArrayList<InputChannel>();
		CannedDropDownDataTest cannedDropDownData = new CannedDropDownDataTest();
		try {
			
			LOGGER.info("Entering into getCannedReportOrderType of ReportsDAO impl");
			cannedOrderTypeList = reportsDAOImplTest.getCannedReportOrderType();				
			LOGGER.info("Exiting from  getCannedReportOrderType of ReportsDAO impl");
			
			
			cannedDropDownData.setOrderType(cannedOrderTypeList);
		
			LOGGER.info("Entering into getCannedInputType of ReportsDAO impl");
			cannedInputChannelList = reportsDAOImplTest.getCannedInputType();
			LOGGER.info("Entering from getCannedInputType of ReportsDAO impl");
			
			cannedDropDownData.setInputChannel(cannedInputChannelList);

			
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getCannedReportOrderTypeInputChannelRequest method of RealTimeOrderBOImpl - "+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getCannedReportOrderTypeInputChannelRequest method of RealTimeOrderBOImpl - ",e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getCannedReportOrderTypeInputChannelRequest method of RealTimeOrderBOImpl ");
			}
		}

		assertEquals(cannedDataList.getInputChannel().get(0).getInputChannelId().toString(),
				cannedDropDownData.getInputChannel().get(0).getInputChannelId().toString());
	}
	/**
	 * Method to provide mock service
	 * @return
	 */
	
	public CannedDropDownDataTest getCannedReportOrderTypeInputChannelRequest() {
		CannedDropDownDataTest cannedDropDownDataTest = new CannedDropDownDataTest();
		return cannedDropDownDataTest;
	}

	/**
	 * Method to test testSubmitSimRetailBlockOnloadRequest
	 */
	@Test
	public void testSubmitSimRetailBlockOnloadRequest() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSimRetailBlockOnloadRequest method of ReportsBOImplTest ");
		}
		SimRetailBlockOnloadResp simRetailBlockOnloadRespActual = new SimRetailBlockOnloadResp();
		try {
			simRetailBlockOnloadRespActual = reportsDAOImplTest.submitSimRetailBlockOnloadRequest();

		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitSimRetailBlockOnloadRequest method of ReportsBOImplTest - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSimRetailBlockOnloadRequest method of ReportsBOImplTest ");
			}
		}
		assertEquals(simRetailBlockOnloadRespExpected,simRetailBlockOnloadRespActual);

	}

	/**
	 * Method to provide Mock up business object to Service
	 * @return SimRetailBlockOnloadResp
	 */
	public SimRetailBlockOnloadResp submitSimRetailBlockOnloadRequest() {
		SimRetailBlockOnloadResp simRetailBlockOnloadRespActual = new SimRetailBlockOnloadResp();
		return simRetailBlockOnloadRespActual;
	}

	/**
	 * Method to test testGenarateSimRetailBlockReport
	 */
	@Test
	public void testGenarateSimRetailBlockReport() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering genarateSimRetailBlockReport method of ReportsBOImplTest ");
		}
		
		String retailBlock = "30 (US)";

		SimRetailBlockReportRespMsg simRetailBlockReportRespMsg = new SimRetailBlockReportRespMsg();
		SimRetailBlockReportResp simRetailBlockReportResp = new SimRetailBlockReportResp();
		simRetailBlockReportResp.setDescription("US");
		simRetailBlockReportResp.setRetailBlock("30");
		simRetailBlockReportResp.setStoreNumber("59156");
		simRetailBlockReportResp.setTotalrecords("1");
		List<SimRetailBlockReportResp> simRetailBlockReportRespList = new ArrayList<SimRetailBlockReportResp>();
		simRetailBlockReportRespMsg.setSimRetailBlockReportResp(simRetailBlockReportRespList);

		String[] tempArr = retailBlock.replace("(", ",").split(",");
		String retailBlockNo = tempArr[0];
		String tempRetailBlockNo = retailBlockNo.trim();
		
        assertEquals("30", tempRetailBlockNo);
		
		try {
			simRetailBlockReportRespMsg = reportsDAOImplTest.genarateSimRetailBlockReport();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at genarateSimRetailBlockReport method of ReportsBOImplTest - "+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting genarateSimRetailBlockReport method of ReportsBOImplTest ");
			}
		}
		assertEquals(simRetailBlockReportRespMsgExpected,simRetailBlockReportRespMsg);
	}

	/**
	 * Method to provide Mock up business object to Service
	 * @return SimRetailBlockReportRespMsg
	 */
	public SimRetailBlockReportRespMsg genarateSimRetailBlockReport(){
		SimRetailBlockReportRespMsg simRetailBlockReportRespMsgActual = new SimRetailBlockReportRespMsg();
		return simRetailBlockReportRespMsgActual;
	}
	
	/**
	 * Method to test testUpdateRetailBlockRequest
	 */
	@Test
	public void testUpdateRetailBlockRequest() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateRetailBlockRequest method of ReportsBOImplTest ");
		}

		SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsgActual = new SimRetailBlockUpdateRespMsg();
		simRetailBlockUpdateRespMsgActual.setUpdateStatusMessage("Update sucessfull");
		try {
			simRetailBlockUpdateRespMsgActual = reportsDAOImplTest.updateRetailBlockRequest();
		} catch (Exception e) {
			LOGGER.error(" Error occoured updateRetailBlockRequest method of ReportsBOImplTest - "+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting updateRetailBlockRequest method of ReportsBOImplTest ");
			}
		}
		assertEquals(simRetailBlockUpdateRespMsgExpected,simRetailBlockUpdateRespMsgActual);
	}
	
	/**
	 * Method to provide Mock up business object to Service
	 * @return SimRetailBlockUpdateRespMsg
	 */
	public SimRetailBlockUpdateRespMsg updateRetailBlockRequest() {
		SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsgActual = new SimRetailBlockUpdateRespMsg();
		return simRetailBlockUpdateRespMsgActual;
	}

}
