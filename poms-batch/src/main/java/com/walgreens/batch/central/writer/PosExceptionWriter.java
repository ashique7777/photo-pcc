package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
//import com.walgreens.batch.central.writer.JdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.batch.central.bean.PosExceptionTransferBean;
import com.walgreens.batch.central.utility.PosQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * @author CTS
 * 
 */

public class PosExceptionWriter implements ItemWriter<PosExceptionTransferBean> {

	private JdbcTemplate jdbcTemplate;
	private PosQuery posQuery;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(PosExceptionWriter.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void write(List<? extends PosExceptionTransferBean> items)throws Exception,PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Entering PosExceptionWriter write()");
		}

		posQuery = new PosQuery();
		String updatePosExceptionQuery = posQuery.getUpdatePosExceptionQuery();
		String insertOrderHistoryQuery = posQuery.getInsertOrderHistoryQueryOne();
		String updWasteQtyQuery = posQuery.getUpdWasteQtyQuery();
		
		try{
		
		for (PosExceptionTransferBean posExceptionTransferBeanItem : items) {
			
			/**update OM_ORDER_LINE_ATTRIBUTE.WASTED_QTY*/
			if(posExceptionTransferBeanItem.getWasteQtyBeanList() != null 
					&& posExceptionTransferBeanItem.getWasteQtyBeanList().size() >0)
			  {			
				     for(int i = 0; i < posExceptionTransferBeanItem.getWasteQtyBeanList().size(); i++){
				    	   
				    	   if((Integer)posExceptionTransferBeanItem.getWasteQtyBeanList().get(i).getWastQty() != null
				    			&& (Long)posExceptionTransferBeanItem.getWasteQtyBeanList().get(i).getOrderlineId() != null)
				    	   {  
				                 jdbcTemplate.update(updWasteQtyQuery,
						         new Object[] { posExceptionTransferBeanItem.getWasteQtyBeanList().get(i).getWastQty(),
						         posExceptionTransferBeanItem.getWasteQtyBeanList().get(i).getOrderlineId() });
				    	   }
			       }
			 }

			/**insert OrderException details*/
			if(posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList() != null 
					&& posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().size() > 0){
			 for (int i = 0; i < posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().size(); i++) {
				
				if(posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getOrderPlacedDttm() != null
						&& (Long)posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getsysOrderId() != null
						&& (Integer)posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getExctTypeId() != null
						&& posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getNotes() != null
						&& (Long)posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getSysOrderLineId() != null
						&& (Integer)posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getPrevEnvelopeNo() != null
						&& posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getPrevOrderStatus() != null
						&& posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getStatus() != null
						&& (Integer)posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getWasteCalcCd() != null
						&& (Double)posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getWasteCost() != null
						&& (Integer)posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getWasteQty() != null)
				    {				        
				    jdbcTemplate.update(updatePosExceptionQuery, new Object[] {
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getOrderPlacedDttm(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getsysOrderId(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getExctTypeId(),
						PhotoOmniConstants.CREATE_USER_ID,
						PhotoOmniConstants.UPDATE_USER_ID,
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getNotes(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getSysOrderLineId(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getPrevEnvelopeNo(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getPrevOrderStatus(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getStatus(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getWasteCalcCd(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getWasteCost(),
						posExceptionTransferBeanItem.getPosOrderExceptionDataBeanList().get(i).getWasteQty() });
				    }
			    }
			}

			/**insert order history details*/
			if(posExceptionTransferBeanItem.getorderHistoryBeanList() != null
					&& posExceptionTransferBeanItem.getorderHistoryBeanList().size() >0 ){				
			     for (int i = 0; i < posExceptionTransferBeanItem.getorderHistoryBeanList().size(); i++) {
			    	 
			    	 if(posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getOrderPlacedDttm() != null
							&& 	(Long)posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getOrderId() != null
							&&	posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getAction() != null
							&&	posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getActionDttm() != null
							&&	posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getOrderStatus() != null
							&&	posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getActionNotes() != null
							&&	(Long)posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getExceptionId() != null
							&&	posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getCreateUserId() != null){

				        jdbcTemplate.update(insertOrderHistoryQuery, new Object[] {						
						posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getOrderPlacedDttm(),
						posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getOrderId(),
						posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getAction(),
						posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getActionDttm(),
						posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getOrderStatus(),
						posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getActionNotes(),
						posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getExceptionId(),
						posExceptionTransferBeanItem.getorderHistoryBeanList().get(i).getCreateUserId(),
						PhotoOmniConstants.UPDATE_USER_ID
						});
			    	  }
			       }
			   }
		   }

		if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Exiting PosExceptionWriter write()");
		}
		
		}catch (Exception e){
			if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Error at Write method in PosExceptionWriter ....", e);
			}
			throw new PhotoOmniException(e.getMessage());
		}
	}
}
