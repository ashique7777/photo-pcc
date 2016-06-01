package com.walgreens.batch.central.writer;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.walgreens.batch.central.bean.OmSilverRecoveryDetailBean;
import com.walgreens.batch.central.bean.SilverCanisterDataBean;
import com.walgreens.batch.query.SilverCanisterQuery;
import com.walgreens.common.constant.PhotoOmniConstants;

/**
 * The main functional unit of the class is write method.
 * This method first fetches each instance of SilverCanisterDataBean and calls insertIntoSilverRecDetail() which
 * writes required data into respective database.
 * @author Cognizant
 *
 */
public class SilverCanisterClientWriter implements ItemWriter<SilverCanisterDataBean>, PhotoOmniConstants {

	private static final Logger log = LoggerFactory.getLogger(SilverCanisterClientWriter.class);
	@Autowired
	@Qualifier("omniJdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	/* 
	 * The write method first fetches each instance of SilverCanisterDataBean and calls insertIntoSilverRecDetail() to generate the record into OM_SILVER_RECOVERY_DETAIL
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends SilverCanisterDataBean> items)
			throws Exception {
		log.debug("Entering into SilverCanisterItemWriter:write()");
		try{			
			for(int i=0;i<items.size();i++){
				SilverCanisterDataBean canisterDataBean=items.get(i);
				log.debug("Calling insertIntoSilverRecDetail()");
				insertIntoSilverRecDetail(canisterDataBean);
				log.debug("Called insertIntoSilverRecDetail()");
			}
		}
		catch(Exception ex){
			log.error("Exception occurred while parsing SilverCanisterItemWriter:insertIntoSilverRecDetail()    ",ex);
		}
		log.debug("Entering into SilverCanisterItemWriter:write()");
		
	}
	
	/**
	 * This method is used to generate the record into OM_SILVER_RECOVERY_DETAIL from OmSilverRecoveryDetailBean which is populated from SilverCanisterDataBean
	 * Once the insertion is done it proceeds to insert/ update OM_SILVER_RECOVERY_HEADER based on the location number
	 * @param omSilverRecoveryDetailBean
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public void insertIntoSilvRecDetail(OmSilverRecoveryDetailBean omSilverRecoveryDetailBean) {
		log.debug("Entering into SilverCanisterItemWriter:insertIntoSilvRecDetail()");
		final String silverRecDtl=SilverCanisterQuery.queryInsertIntoSilvRecDetail().toString();
		log.debug("silverRecDtl    "+silverRecDtl);
		final String updateSilverRecDtl=SilverCanisterQuery.queryUpdateSilvRecHeader().toString();
		log.debug("updateSilverRecDtl    "+updateSilverRecDtl);		
		final int locationId=omSilverRecoveryDetailBean.getSysLocationId();
		log.debug("locationId    "+locationId);		
		final int rollsCount=omSilverRecoveryDetailBean.getRollsCount();
		log.debug("rollsCount    "+rollsCount);
		final int printsCount=omSilverRecoveryDetailBean.getPrintsCount();
		log.debug("printsCount    "+printsCount);
		final int printsInSoInch=omSilverRecoveryDetailBean.getPrintsInSoInch();
		log.debug("printsInSoInch    "+printsInSoInch);
		final int silverRecvRolls=omSilverRecoveryDetailBean.getSilverRecvRolls();
		log.debug("silverRecvRolls    "+silverRecvRolls);
		final int silverRecvPrints=omSilverRecoveryDetailBean.getSilverRecvPrints();
		log.debug("silverRecvPrints    "+silverRecvPrints);
		final Timestamp canisterStartDate=omSilverRecoveryDetailBean.getSilverCalcDttm();
		log.debug("canisterStartDate    "+canisterStartDate);
		final String createUserId=omSilverRecoveryDetailBean.getCreateUserId();
		log.debug("createUserId    "+createUserId);
		final String updateUserId=omSilverRecoveryDetailBean.getUpdateUserId();
		log.debug("updateUserId    "+updateUserId);
		final String canisterStatus=omSilverRecoveryDetailBean.getCanisterStatus();
		log.debug("canisterStatus    "+canisterStatus);
		try{
			log.debug("Inserting values into OM_SILVER_RECOVERY_DETAIL");
			
			/*
			 * Below mechanism is written due to transaction roll back is required. The scenario says if OM_SILVER_RECOVERY_HEADER will not be updated,
			 * roll back the insertion of OM_SILVER_RECOVERY_DETAIL table
			 */
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);		
			
			TransactionTemplate transactionTemplate=new TransactionTemplate(new DataSourceTransactionManager(jdbcTemplate.getDataSource()));
			Exception ex = (Exception)transactionTemplate.execute(new TransactionCallback() {
                public Object doInTransaction(TransactionStatus ts) {
                	boolean valueInserted=false;
                    try {
                    	int inserted = jdbcTemplate.update(silverRecDtl,new Object[]{locationId,canisterStartDate,rollsCount,printsCount,printsInSoInch,silverRecvRolls,silverRecvPrints,createUserId,updateUserId});
                    	if(inserted!=0){
            				valueInserted=true;
            			}
                    	log.debug("valueInserted    "+valueInserted);
            			log.debug("Values are inserted succesfully");
            			
            			int updated = jdbcTemplate.update(updateSilverRecDtl,new Object[]{silverRecvRolls,silverRecvPrints,locationId});
        				log.debug("updated    "+updated);
        				
            			if(ZERO==updated){
        					final String insertSilverRecDtl=SilverCanisterQuery.queryInsertIntoSilvRecHeader().toString();
        					log.debug("insertSilverRecDtl    "+insertSilverRecDtl);
        					updated = jdbcTemplate.update(insertSilverRecDtl,new Object[]{locationId,canisterStartDate,canisterStatus,rollsCount,printsCount,printsInSoInch,silverRecvRolls,silverRecvPrints});
        					log.debug("updated    "+updated);
        				}
        				jdbcTemplate.getDataSource().getConnection().setAutoCommit(true); // set autocommit back to true    
                        return null;
                    } catch (Exception e) {
                        ts.setRollbackOnly();
                        log.error("Exception occurred while doing rollback     ",e);
                        try {
							jdbcTemplate.getDataSource().getConnection().setAutoCommit(true);
						} catch (SQLException ex) {
							log.error("Exception occurred while doing autocommit    ",ex);
						} // set autocommit back to true    
                        return e;
                    }
                }
            });
		}
		catch(Exception ex){
			log.error("Exception occurred while parsing SilverCanisterItemWriter:insertIntoSilverRecDetail()    ",ex);		
		}		
		log.debug("Exiting from SilverCanisterItemWriter:insertIntoSilvRecDetail()");
		
	}

/**
 * This method populates OmSilverRecoveryDetailBean from SilverCanisterDataBean and calls insertIntoSilvRecDetail()
 * @param canisterDataBean
 */
public void insertIntoSilverRecDetail(SilverCanisterDataBean canisterDataBean) {
		log.debug("Entering into SilverCanisterItemWriter:insertIntoSilverRecDetail()");
		OmSilverRecoveryDetailBean omSilverRecoveryDetailBean = new OmSilverRecoveryDetailBean();			
		omSilverRecoveryDetailBean.setSysLocationId(canisterDataBean.getLocationNo());
		omSilverRecoveryDetailBean.setCreateDttm(canisterDataBean.getCanisterCalcDate());
		omSilverRecoveryDetailBean.setRollsCount(canisterDataBean.getRollCount());
		omSilverRecoveryDetailBean.setCreateDttm(canisterDataBean.getCanisterCalcDate());
		omSilverRecoveryDetailBean.setPrintsCount(canisterDataBean.getPrintCount());
		omSilverRecoveryDetailBean.setPrintsInSoInch(canisterDataBean.getPrintInSqInchs());
		omSilverRecoveryDetailBean.setSilverRecvRolls(canisterDataBean.getSilverRecvRolls());
		omSilverRecoveryDetailBean.setSilverRecvPrints(canisterDataBean.getSilverRecvPrints());
		omSilverRecoveryDetailBean.setTransferCd(canisterDataBean.getTransferCD());
		omSilverRecoveryDetailBean.setCreateUserId(CREATE_USER_ID);
		omSilverRecoveryDetailBean.setCreateDttm(new Timestamp(new Date().getTime()));
		omSilverRecoveryDetailBean.setUpdateUserId(CREATE_USER_ID);
		omSilverRecoveryDetailBean.setUpdatedDttm(new Timestamp(new Date().getTime()));
		omSilverRecoveryDetailBean.setSilverCalcDttm(canisterDataBean.getCanisterCalcDate());
		omSilverRecoveryDetailBean.setCanisterStatus(canisterDataBean.getCanisterStatus());
		log.debug("Calling insertIntoSilvRecDetail");
		insertIntoSilvRecDetail(omSilverRecoveryDetailBean);
		log.debug("Called insertIntoSilvRecDetail");
		log.debug("Exiting from SilverCanisterItemWriter:insertIntoSilverRecDetail()");
		
	}
	
}
