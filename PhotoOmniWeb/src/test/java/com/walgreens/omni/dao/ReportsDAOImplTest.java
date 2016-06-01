package com.walgreens.omni.dao;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.omni.bean.OmPriceLevelBean;
import com.walgreens.omni.bean.OmSimRetailBlockReportBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
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
import com.walgreens.omni.rowmapper.InputChannelRowmapper;
import com.walgreens.omni.rowmapper.OmPriceLevelRowMapper;
import com.walgreens.omni.rowmapper.OmSimRetailBlockReportBeanRowMapper;
import com.walgreens.omni.rowmapper.OrderTypeRowmapper;
import com.walgreens.omni.utility.ReportsQuery;

public class ReportsDAOImplTest {

	private static final Logger LOGGER=LoggerFactory.getLogger(ReportsDAOImplTest.class);
	
	private JdbcTemplate jdbcTemplate;
	//public ReportsDAOImplTest(){ jdbcTemplate = new DBConfig().getJdbcTemplate();}
	
	private static final SimRetailBlockOnloadResp simRetailBlockOnloadRespExpected = new SimRetailBlockOnloadResp();
	private static final SimRetailBlocOnloadList simRetailBlocOnloadList = new SimRetailBlocOnloadList();
	private static final SimRetailBlockReportRespMsg simRetailBlockReportRespMsgExpected = new SimRetailBlockReportRespMsg();
	private static final SimRetailBlockReportResp simRetailBlockReportResp = new SimRetailBlockReportResp();
	private static final SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg = new SimRetailBlockUpdateReqMsg();
	private static final SimRetailBlockUpdateReq simRetailBlockUpdateReq = new SimRetailBlockUpdateReq();
	private static final SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsgExpected = new SimRetailBlockUpdateRespMsg();
	private static final CannedDropDownDataTest cannedDataList = new CannedDropDownDataTest();
	private static final List<OrderType> orderTypeList = new ArrayList<OrderType>();
	private static final List<InputChannel> inputChannelList = new ArrayList<InputChannel>();
	
	/**
	 * Method to test setUp Mock up Test Data
	 */
	@Before
	public void setUp() {

		simRetailBlocOnloadList.setRetailBlock("30 (US)");
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
		simRetailBlockUpdateRespMsgExpected.setUpdateStatusMessage("1 Recordes were updated");
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
		

	}
	
	
	/**
	 * @return
	 * @throws PhotoOmniException
	 */
	public void testGetCannedReportOrderType() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getCannedReportOrderType method of MachineReportDAOImpl ");
		}
		List<OrderType> cannedOrderTypeList = null;
		String sqlQuery = null;
		try {
			LOGGER.info("Entering into ReportsQuery");
			sqlQuery = ReportsQuery.getOrderTypeQuery().toString();
			cannedOrderTypeList = jdbcTemplate.query(sqlQuery,
					new OrderTypeRowmapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getCannedReportOrderType method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at getCannedReportOrderType method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getCannedReportOrderType method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getCannedReportOrderType method of ReportsDAOImpl ");
			}
		}
		assertEquals(cannedDataList.getOrderType().get(0).getOrderId().toString(),
				((CannedDropDownDataTest) cannedOrderTypeList).getOrderType().get(0).getOrderId().toString());


	}
	
	public List<OrderType> getCannedReportOrderType() throws PhotoOmniException {
		List<OrderType> cannedOrderTypeList = new ArrayList<OrderType>();
		return cannedOrderTypeList;
	}

	/**
	 * Canned Report: Method to get the drop down list populated for input type.
	 * The following method "getCannedInputType" fetches data from the database.
	 * Table Name-OM-CODE_DECODE
	 */
	public void testGetCannedInputType() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getCannedInputType method of CannedReport DAOImpl ");
		}
		List<InputChannel> cannedInputChannelList = null;
		String sqlQuery = null;
		try {
			LOGGER.info("Entering into ReportsQuery");
			sqlQuery = ReportsQuery.getInputChannelQuery().toString();
			cannedInputChannelList = jdbcTemplate.query(sqlQuery,
					new InputChannelRowmapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getCannedInputType method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at getCannedInputType method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getCannedInputType method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getCannedInputType method of ReportsDAOImpl ");
			}
		}
	
		assertEquals(cannedDataList.getInputChannel().get(0).getInputChannelId().toString(),
				((CannedDropDownDataTest) cannedInputChannelList).getInputChannel().get(0).getInputChannelId().toString());

	}
	
	public List<InputChannel> getCannedInputType() throws PhotoOmniException {
		List<InputChannel> cannedInputChannelList = new ArrayList<InputChannel>();
		return cannedInputChannelList;
	}
	
	/**
	 * Method to test testSubmitSimRetailBlockOnloadRequest method
	 */
	@Test
	public void testSubmitSimRetailBlockOnloadRequest() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering testSubmitSimRetailBlockOnloadRequest method of ReportsDAOImplTest ");
		}

		String simRetailBlockOnloadRespQuery = ReportsQuery.getSimRetailBlockOnloadRespQuery();
		SimRetailBlockOnloadResp simRetailBlockOnloadRespActual = new SimRetailBlockOnloadResp();
		List<OmPriceLevelBean> omPriceLevelBeanList = new ArrayList<OmPriceLevelBean>();
		List<SimRetailBlocOnloadList> simRetailBlocOnloadList = new ArrayList<SimRetailBlocOnloadList>();
		try {

			omPriceLevelBeanList = jdbcTemplate.query(simRetailBlockOnloadRespQuery, new OmPriceLevelRowMapper());
			for (int i = 0; i < omPriceLevelBeanList.size(); i++) {
				
				SimRetailBlocOnloadList simRetailBlocOnloadBeanList = new SimRetailBlocOnloadList();
				simRetailBlocOnloadBeanList.setRetailBlock(omPriceLevelBeanList.get(i).getPriceLevel()
						+ " ("+ omPriceLevelBeanList.get(i).getDescription() + ")");
				simRetailBlocOnloadList.add(simRetailBlocOnloadBeanList);
			}
			simRetailBlockOnloadRespActual.setSimRetailBlocOnloadList(simRetailBlocOnloadList);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at testSubmitSimRetailBlockOnloadRequest method of ReportsDAOImplTest - "+ e.getMessage());
			
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting testSubmitSimRetailBlockOnloadRequest method of ReportsDAOImplTest ");
			}
		}
		assertEquals(simRetailBlockOnloadRespExpected.getSimRetailBlocOnloadList().get(0).getRetailBlock().toString(),
				     simRetailBlockOnloadRespActual.getSimRetailBlocOnloadList().get(0).getRetailBlock().toString());
	}
	
	/**
	 * Method to provide Mock up business object to Service
	 * @return SimRetailBlockOnloadResp
	 */
	public SimRetailBlockOnloadResp submitSimRetailBlockOnloadRequest(){
		SimRetailBlockOnloadResp simRetailBlockOnloadRespActual = new SimRetailBlockOnloadResp();
		return simRetailBlockOnloadRespActual;
	}
	
	/**
	 * Method to test testGenarateSimRetailBlockReport method
	 * @throws PhotoOmniException 
	 */
	@Test
	public void testGenarateSimRetailBlockReport() throws PhotoOmniException{

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering testGenarateSimRetailBlockReport method of ReportsDAOImplTest ");
		}

		String retailBlock = "30";
		String locationType = "Store";
		List<String> number = new ArrayList<String>();
		number.add("59156");
		String pageNo = "1";
		String sortColumnName = "storeNumber"; 
		String sortOrder = "ASC";
		String downLoadCsv ="downLoadReport";

		String locationNoQuery = ReportsQuery.getLocationNoList(locationType,number);
		List<String> locNumList = new ArrayList<String>();
		locNumList = jdbcTemplate.queryForList(locationNoQuery, String.class);

		/** Build map for pagination */
		Map<String, Long> pageNoMap = new HashMap<String, Long>();
		if (!CommonUtil.isNull(pageNo) && pageNo != "") {
			pageNoMap = CommonUtil.getPaginationLimit(pageNo,
					PhotoOmniConstants.SILVER_CANISTER_PAGINATION_SIZE);
		} else {
			pageNoMap = null;
		}

		/** Assign respected sort column */
		String sortColoumnOne = PhotoOmniConstants.BLANK;
		if (!CommonUtil.isNull(sortColumnName)&& sortColumnName.equalsIgnoreCase("storeNumber")) {
			sortColoumnOne = "LOCATION_NBR";
		} else if (!CommonUtil.isNull(sortColumnName)&& sortColumnName.equalsIgnoreCase("retailBlock")) {
			sortColoumnOne = "SYS_PRICE_LEVEL_ID";
		} else {
			sortColoumnOne = "LOCATION_NBR";
		}

		String genarateSimRetailBlockReportQuery = ReportsQuery.getGenarateSimRetailBlockReportQuery(locationType, locNumList,sortColoumnOne, sortOrder);
		String genarateSimRetailBlockReportCSVQuery = ReportsQuery.getGenarateSimRetailBlockReportCSVQuery(locationType, locNumList,sortColoumnOne, sortOrder);

		SimRetailBlockReportRespMsg simRetailBlockReportRespMsg = new SimRetailBlockReportRespMsg();
		List<SimRetailBlockReportResp> SimRetailBlockReportRespList = new ArrayList<SimRetailBlockReportResp>();
		List<OmSimRetailBlockReportBean> omSimRetailBlockReportBeanList = new ArrayList<OmSimRetailBlockReportBean>();

		try {
			if(downLoadCsv.equalsIgnoreCase("downLoadCsv")){
				omSimRetailBlockReportBeanList = jdbcTemplate.query(
						genarateSimRetailBlockReportCSVQuery,
						new Object[] { retailBlock, retailBlock},
						new OmSimRetailBlockReportBeanRowMapper());
			}
			else {
				omSimRetailBlockReportBeanList = jdbcTemplate.query(
						genarateSimRetailBlockReportQuery,
						new Object[] { retailBlock, retailBlock,
								pageNoMap.get("START_LIMIT"),
								pageNoMap.get("END_LIMIT") },
						new OmSimRetailBlockReportBeanRowMapper());
			}

			
			for (int i = 0; i < omSimRetailBlockReportBeanList.size(); i++) {

				SimRetailBlockReportResp simRetailBlockReportResp = new SimRetailBlockReportResp();
				simRetailBlockReportResp.setStoreNumber(String.valueOf(omSimRetailBlockReportBeanList.get(i).getLocationNbr()));
				simRetailBlockReportResp.setRetailBlock(String.valueOf(omSimRetailBlockReportBeanList.get(i).getRetailBlock()));
				simRetailBlockReportResp.setDescription(omSimRetailBlockReportBeanList.get(i).getDescription());
				simRetailBlockReportResp.setTotalrecords(String.valueOf(omSimRetailBlockReportBeanList.get(i).getTotalRows()));

				SimRetailBlockReportRespList.add(simRetailBlockReportResp);
			}
			simRetailBlockReportRespMsg.setSimRetailBlockReportResp(SimRetailBlockReportRespList);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at testGenarateSimRetailBlockReport method of ReportsDAOImplTest - "+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting testGenarateSimRetailBlockReport method of ReportsDAOImplTest ");
			}
		}
		assertEquals(simRetailBlockReportRespMsgExpected.getSimRetailBlockReportResp().get(0).getRetailBlock().toString(),
				     simRetailBlockReportRespMsg.getSimRetailBlockReportResp().get(0).getRetailBlock().toString());
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
	 * Method to test testUpdateRetailBlockRequest method
	 * @throws PhotoOmniException 
	 */
	@Test
	public void testUpdateRetailBlockRequest() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering testUpdateRetailBlockRequest method of ReportsDAOImplTest ");
		}
		
		SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsgActual = new SimRetailBlockUpdateRespMsg();
		
		List<String> storeNoList =  simRetailBlockUpdateReqMsg.getSimRetailBlockUpdateReq().getStoreNumber();

		String retailBlock = "30 (US)";
		String[] tempArr = retailBlock .replace("(", ",").split(",");
		@SuppressWarnings("unused")
		String retailBlockNo = tempArr[0];
		String retailBlockDescription = tempArr[1];
		String tempRetailBlockDescription = retailBlockDescription.replace(")", "").trim();
		
		String updateSimRetailBlockQuery = ReportsQuery.getUpdateSimRetailBlockQuery(storeNoList,tempRetailBlockDescription);
		try {
			
			int updateCnt = jdbcTemplate.update(updateSimRetailBlockQuery);
			if(updateCnt<0){
				simRetailBlockUpdateRespMsgActual.setUpdateStatusMessage("Failed to update records");
			}else{
				simRetailBlockUpdateRespMsgActual.setUpdateStatusMessage(updateCnt+" Recordes were updated");
			}

		} catch (Exception e) {
			LOGGER.error(" Error occoured at testUpdateRetailBlockRequest method of ReportsDAOImplTest - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting testUpdateRetailBlockRequest method of ReportsDAOImplTest ");
			}
		}
		assertEquals(simRetailBlockUpdateRespMsgExpected.getUpdateStatusMessage().toString(),
				     simRetailBlockUpdateRespMsgActual.getUpdateStatusMessage().toString());
	}
	
	/**
	 * Method to provide Mock up business object to Service
	 * @return SimRetailBlockUpdateRespMsg
	 */
	public SimRetailBlockUpdateRespMsg updateRetailBlockRequest(){
		SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsgActual = new SimRetailBlockUpdateRespMsg();
		return simRetailBlockUpdateRespMsgActual;
	}
	/**
	 * @param locationType
	 * @param locNumList
	 * @param sortColoumnOne
	 * @param sortOrder
	 * @return String
	 * @throws PhotoOmniException 
	 */
	public static String getGenarateSimRetailBlockReportCSVQuery(String locationType, List<String> number, 
			  String sortColoumnOne, String sortOrder) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		String inQuery = getInQueryforSimRetailBlockOne(number);
		query.append("SELECT COUNT(*) OVER () AS TOTAL_ROWS,");
		query.append("OMLOC.LOCATION_NBR  AS LOCATION_NBR,OMLOC.SYS_PRICE_LEVEL_ID AS SYS_PRICE_LEVEL_ID,");
		query.append("OMPRLVL.PRICE_LEVEL AS PRICE_LEVEL,OMPRLVL.DESCRIPTION AS DESCRIPTION");
		query.append(" FROM OM_LOCATION OMLOC,OM_PRICE_LEVEL OMPRLVL ");
		query.append("WHERE (");
		query.append(inQuery);
		query.append(")");
		query.append(" AND OMPRLVL.PRICE_LEVEL = ? AND OMLOC.SYS_PRICE_LEVEL_ID = ? ORDER BY ");
		query.append(sortColoumnOne +" "+sortOrder);
		return query.toString();
	}
	/**
	 * This method creates multiple in block for SqlQuery if storeDataList size
	 * is more than 1000.
	 * 
	 * @param storeDataList
	 *            contains store.
	 * @return inQuery.
	 * @throws PhotoOmniException
	 *             custom exception.
	 */

	private static String getInQueryforSimRetailBlockOne(List<String> storeDataList) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getInQueryforimRetailBlockOne method of SimRetailBlockReportsQuery ");
		}
		
		StringBuilder inQuery = new StringBuilder();
		int listSize = storeDataList.size();
		int IN_QUERY_VALUE_SIZE = 1000;
		try {
			if (listSize > IN_QUERY_VALUE_SIZE) {
				int loop = (listSize) / IN_QUERY_VALUE_SIZE;
				int remainder = (listSize) % IN_QUERY_VALUE_SIZE;
				if (remainder > 0) {
					loop++;
				}
				int start = 0;
				int end = IN_QUERY_VALUE_SIZE;
				for (int i = 0; i < loop; i++) {
					inQuery.append(" OMLOC.LOCATION_NBR IN(");
					for (int j = start; j < end; j++) {
						if (j == (listSize - 1)) {
							inQuery.append(storeDataList.get(listSize - 1)
									+ ",");
							break;
						} else {
							inQuery.append(storeDataList.get(j) + ",");
						}
					}
					int lastIndex = inQuery.lastIndexOf(",");
					inQuery.deleteCharAt(lastIndex);
					inQuery.append(" ) ");
					if (loop != (i + 1)) {
						inQuery.append(" OR ");
					}
					start = end;
					end = end + IN_QUERY_VALUE_SIZE;
				}

			} else {
				inQuery.append(" OMLOC.LOCATION_NBR IN(");
				for (int i = 0; i < listSize; i++) {
					inQuery.append(storeDataList.get(i) + ",");
				}
				int lastIndex = inQuery.lastIndexOf(",");
				inQuery.deleteCharAt(lastIndex);
				inQuery.append(" ) ");

			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getInQueryforimRetailBlockOne method of SimRetailBlockReportsQuery - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getInQueryforimRetailBlockOne method of SimRetailBlockReportsQuery ");
			}
		}
		return inQuery.toString();
	}


}
