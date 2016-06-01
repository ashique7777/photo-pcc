package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.batch.central.bean.StoreCloningBean;
import com.walgreens.batch.central.utility.StoreCloningQuery;
import com.walgreens.common.exception.PhotoOmniException;

/**
* 
* @author CTS
* 
*/
public class StoreCloningClientWriter implements ItemWriter<StoreCloningBean>{

	private JdbcTemplate jdbcTemplate;
	private StoreCloningQuery storeCloningQuery;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreCloningClientWriter.class);
	
		public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	/**
	* Write method update the SYS_PRICE_LEVEL_ID to OM_LOCATION table.
	*/
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void write(List<? extends StoreCloningBean> items)	throws Exception,PhotoOmniException {
		
		LOGGER.debug("Entering StoreCloningClientWriter write()");
		storeCloningQuery = new StoreCloningQuery();
		String updateLocationDtlQuery = storeCloningQuery.updateNewLocationDetail();
		
		try{
			if(items.size()>0){
				for(int i=0; i<items.size(); i++){
					StoreCloningBean storeCloningBean = (StoreCloningBean)items.get(i);
					jdbcTemplate.update(updateLocationDtlQuery, new Object[]{storeCloningBean.getSysPriceLevelId(), storeCloningBean.getSysLocationId()});
				}
			}	
		}catch (Exception e){
			LOGGER.error("Error at Write method in StoreCloiningClientWriter ...."+ e.getMessage());
		    throw new PhotoOmniException(e.getMessage());
		}
		
		
		LOGGER.debug("Exiting StoreCloningClientWriter write()");
	}

}
