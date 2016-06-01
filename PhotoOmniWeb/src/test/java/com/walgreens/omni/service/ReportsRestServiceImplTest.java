package com.walgreens.omni.service;

import static org.junit.Assert.assertEquals;
import static org.easymock.EasyMock.replay;
import java.util.ArrayList;
import java.util.List;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.omni.bo.ReportsBOImplTest;
import com.walgreens.omni.json.bean.CannedDropDownDataTest;
import com.walgreens.omni.json.bean.InputChannel;
import com.walgreens.omni.json.bean.OrderType;

public class ReportsRestServiceImplTest {
	Logger LOGGER=LoggerFactory.getLogger(ReportsRestServiceImplTest.class);


	/**
	 * For JUnit Testing
	 */
	private ReportsBOImplTest reportsBOImplTest;
	private static final com.walgreens.omni.json.bean.CannedDropDownDataTest cannedDataList = new com.walgreens.omni.json.bean.CannedDropDownDataTest();
	private static final List<OrderType> orderTypeList = new ArrayList<OrderType>();
	private static final List<InputChannel> inputChannelList = new ArrayList<InputChannel>();				
		OrderType orderType = new OrderType();
		InputChannel inputChannel = new InputChannel();
		/**
		 * Method to set up mock data 
		 * @throws PhotoOmniException
		 */
		@Before
	public void setup() throws PhotoOmniException
	{
		 
		    reportsBOImplTest = createMock(ReportsBOImplTest.class);

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
	
		expect(reportsBOImplTest.getCannedReportOrderTypeInputChannelRequest()).andReturn(cannedDataList);
		replay(reportsBOImplTest);
		
		/*cannedDataList.setInputChannel(inputChannelList);
		cannedDataList.setOrderType(orderTypeList);*/
/*        cannedDataList.addAll(inputChannelList);
        cannedDataList.addAll(orderTypeList);
		cannedDataList.setOrderType((inputChannelList);
		cannedDataList.setInputChannel((List<InputChannel>) inputChannel);*/
		
	}
		
    /**
     * Method to get the data 
     */

	@Test
	public void testGetCannedReportOrderTypeInputChannelRequest() {
	
	
		
		CannedDropDownDataTest cannedDataListTest = new CannedDropDownDataTest();
		try {	  

		   
		    cannedDataListTest =reportsBOImplTest.getCannedReportOrderTypeInputChannelRequest();
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getCannedReportOrderTypeInputChannelRequest method of ReportsRestServiceImpl - ",
					e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getCannedReportOrderTypeInputChannelRequest method of ReportsRestServiceImpl ");
			}
		}
		
		assertEquals(cannedDataList.getInputChannel().get(0).getInputChannelId().toString(),
				cannedDataListTest.getInputChannel().get(0).getInputChannelId().toString());
	}
	
	
		


	}


