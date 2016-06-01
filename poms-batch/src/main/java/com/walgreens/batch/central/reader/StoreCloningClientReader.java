package com.walgreens.batch.central.reader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.OldStoreDetailBean;
import com.walgreens.batch.central.bean.StoreCloningBean;
import com.walgreens.batch.central.rowmapper.OldStoreCloningDataRowMapper;
import com.walgreens.batch.central.rowmapper.StoreCloningDataRowMapper;
import com.walgreens.batch.central.utility.StoreCloningQuery;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * @author CTS
 * This batch is used to update the system price level of new store having '0' SYS_PRICE_LEVEL_ID from OM_LOCATION TABLE
 * This Job is triggered after LDB update.
 */

public class StoreCloningClientReader implements ItemReader<StoreCloningBean>{
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreCloningClientReader.class);

	private JdbcTemplate jdbcTemplate;
	
	/** FLAG USED TO RESTRICT QUERY EXECUTION EVERY TIME READ() IS CALLED */
	private boolean queryFlag = false;

	/**
	 * COUNTER USED TO READ & CALCULATE AN StoreCloning & SEND TO PROCESSOR */
	private int counter = 0;

	/** FIELD USED TO INDICATE STARTING ROWNUM FOR PAGINATION */
	private int pageBegin = 1;
	
	/** FIELD USED TO CONFIGURE PAGINATION */
	private int paginationCounter = 10;
	
	private List<StoreCloningBean> finalLocationDtlList = new ArrayList<StoreCloningBean>();	//This list contain all the new location with price level id.
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * This method return's the 'storeCloningBean' to the processor.  
	 * @return storeCloningBean.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@SuppressWarnings("unused")
	@Override
	public StoreCloningBean read() throws Exception, PhotoOmniException, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		
		LOGGER.debug("Entering StoreCloningClientReader read()");
		
		StoreCloningQuery storeCloningQuery = new StoreCloningQuery();				
		String newLocationDtlQuery = storeCloningQuery.getNewLocationDetail();				//SQL query of all new location details for OM_LOCATION table where PRICE_LEVELID=0.
		String existingLocationDtlQuery = storeCloningQuery.getOldLocationDetail();			//SQL query of all existing location details having district number from OM_LOCATION table.
		
		List<StoreCloningBean> newLocationDtlList = new ArrayList<StoreCloningBean>();		//This list store all the new location. 
		List<OldStoreDetailBean> oldLocationDtlList = new ArrayList<OldStoreDetailBean>();	//This list store all the existing location.
		
		StoreCloningBean storeCloningBean = null;
		OldStoreDetailBean oldStoreDetailBean = null;
		StoreCloningBean newStoreCloningBean=null;
		try{
			
			if(!queryFlag){
				
				/** Get all the new location details having zero price level id from OM_LOCATION*/
				newLocationDtlList=jdbcTemplate.query(newLocationDtlQuery, new StoreCloningDataRowMapper());  
				if(newLocationDtlList != null && newLocationDtlList.size() > 0){
					
					for(int i=0; i<newLocationDtlList.size(); i++){
						storeCloningBean = newLocationDtlList.get(i);
						
							try{
								/*
								 * Find the system price level id depends on the district number of the new location. If the district number of the new location 
								 * does not present then set the system price level id depends on the address state code.  
								 */
								oldLocationDtlList = jdbcTemplate.query(existingLocationDtlQuery, new Object []{storeCloningBean.getDistrictNbr()},new OldStoreCloningDataRowMapper()); /** Get all the existing location details having price level id from OM_LOCATION*/
								if(oldLocationDtlList!=null && oldLocationDtlList.size()>0){
									oldStoreDetailBean = oldLocationDtlList.get(0);
									if(oldStoreDetailBean.getAddressStateCode()==storeCloningBean.getAddressStateCode()){
									storeCloningBean.setSysPriceLevelId(oldStoreDetailBean.getSysPriceLevelId());
									}else{
										if(storeCloningBean.getAddressStateCode().equalsIgnoreCase("PR")){
											storeCloningBean.setSysPriceLevelId(4);
										}else {
											storeCloningBean.setSysPriceLevelId(1);
										}
									}
								}else{
									if(storeCloningBean.getAddressStateCode().equalsIgnoreCase("PR")){
										storeCloningBean.setSysPriceLevelId(4);
									}else {
										storeCloningBean.setSysPriceLevelId(1);
									}
								}
								
							}catch(Exception e){
								LOGGER.error("Error in existing location details "+ e.getMessage());
								throw new PhotoOmniException(e.getMessage());
							}
							finalLocationDtlList.add(storeCloningBean);
						
						
					}
					queryFlag = true;
					counter = 0;
				}else{
					LOGGER.debug("Exiting StoreCloningClientReader read() and going to writer");
					return null;
				}
			
			}
			
			if (finalLocationDtlList != null && counter < finalLocationDtlList.size())
			{
				if (counter == (finalLocationDtlList.size() - 1)) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				LOGGER.debug("send storeCloningClientReader to Writer");
				newStoreCloningBean = finalLocationDtlList.get(counter++);
				
		}else {
				
				newStoreCloningBean = null;
			}
			
		}catch(Exception e){
			LOGGER.error("Error at Read method in StoreCloningClientReader ...."+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}
		
		return newStoreCloningBean;
	}
	
	

}
