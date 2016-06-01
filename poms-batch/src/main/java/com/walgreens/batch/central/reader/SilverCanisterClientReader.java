package com.walgreens.batch.central.reader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.SilverCanisterDataBean;
import com.walgreens.batch.central.rowmapper.SilverCanisterRowMapper;
import com.walgreens.batch.query.SilverCanisterQuery;

/**
 * This user defined ItemReader implements the business flow of SilverCanisterReport by executing the specified query from SilverCanisterQuery
 * @author Cognizant
 *
 */
public class SilverCanisterClientReader implements
		ItemReader<SilverCanisterDataBean> {

	private static final Logger log = LoggerFactory.getLogger(SilverCanisterClientReader.class);
	

	// FLAG USED TO RESTRICT QUERY EXECUTION EVERY TIME READ() IS CALLED
	private boolean queryFlag = false;

	// COUNTER USED TO READ & CALCULATE AN ORDER BEAN & SEND TO PROCESSOR
	private int counter = 0;

	// FIELD USED TO INDICATE STARTING ROWNUM FOR PAGINATION
	private int pageBegin = 1;
		
	// FIELD USED TO CONFIGURE PAGINATION
	private int paginationCounter = 20;
	
	List<SilverCanisterDataBean> listSilverCanisterDB = new ArrayList<SilverCanisterDataBean>();
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	JdbcTemplate jdbcTemplate;

	/*
	 * Below overridden read method is written to describe the functionalities
	 * of SilverCanisterReport. First it will call the respective method to
	 * populate SilverCanisterDataBean based on the query defined. Then it will
	 * instantiate each record of the result set to SilverCanisterDataBean and
	 * pass to to SilverCanisterItemWriter for further processing
	 * 
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public SilverCanisterDataBean read() throws Exception,
			UnexpectedInputException, ParseException,
			NonTransientResourceException {
		
		log.debug("Entering into SilverCanisterItemReader:read()");
		SilverCanisterDataBean canisterDataBean = null;
		try {
			
			
			log.debug("queryFlag    "+queryFlag);
			if (!queryFlag) {				
				int lastIndex = (pageBegin + paginationCounter-1);
				listSilverCanisterDB = populateSilverCanisterDB(pageBegin,lastIndex);				
				queryFlag = true;
				counter = 0;
			}
			if (null!=listSilverCanisterDB && listSilverCanisterDB.size() >= 1 && counter < listSilverCanisterDB.size()) {
				if (counter == listSilverCanisterDB.size() - 1) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				log.debug("Proceed further as more record exists in canisterDataBean");
				canisterDataBean = listSilverCanisterDB.get(counter++);
				
			} else {
				log.debug("No record exist in canisterDataBean. So assiging it to null");
				canisterDataBean = null;
			}

		} catch (Exception ex) {
			log.error("Exception occurred while processing SilverCanisterItemReader    ",ex);
		}
		log.debug("Exiting from SilverCanisterItemReader:read()");
		return canisterDataBean;
	}
	
	public List<SilverCanisterDataBean> populateSilverCanisterDB(int pageBegin,int paginationCounter) {

		log.debug("Entering into SilverCanisterDAOImpl:populateSilverCanisterDB()");
		final String silverRecDtl = SilverCanisterQuery
				.populateSilvCanDBQuery().toString();
		log.debug("silverRecDtl   "+silverRecDtl);
		List<SilverCanisterDataBean> listSilverCanisterDB = null;		
		try {
			log.debug("Populating SilverCanisterDataBean");
			listSilverCanisterDB = jdbcTemplate.query(silverRecDtl,new Object[]{pageBegin,paginationCounter},
					new SilverCanisterRowMapper());
			log.debug("Populated SilverCanisterDataBean");
		} 
		catch (Exception ex) {
			log.error(
					"Exception occurred while parsing SilverCanisterDAOImpl    ",
					ex);
		}
		log.debug("Exiting from SilverCanisterDAOImpl:populateSilverCanisterDB()");
		return listSilverCanisterDB;

	}

}
