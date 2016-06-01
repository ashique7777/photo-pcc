package com.walgreens.batch.central.writer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.walgreens.batch.central.bean.PromotionStoresAssocBean;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

public class PCSFeedFileDBWriter  implements ItemWriter<PromotionStoresAssocBean>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PCSFeedFileDBWriter.class);
	static List<String> lstEventType = new ArrayList<String>();

	@Autowired
	@Qualifier(PhotoOmniConstants.OMNI_JDBC_TEMPLATE)
	private JdbcTemplate jdbcTemplate;
	private Date recvDTTM;

	public void setRecvDTTM(Date recvDTTM) {
		this.recvDTTM = recvDTTM;
	}

	private String createdUser ;

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public void write(List<? extends PromotionStoresAssocBean> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PCSFeedFileDBWriter.write()  --- >");
		}

		try{
			long lSys_Store_Promo_Assoc_Id = -1l;
			String strPKQuery = ReportsQuery.getStorePromotionAssocPrimaryKey(),
					strStoreAssocTblInQuery = ReportsQuery.insertQueryToStorePromotionASSOC().toString(),
					strStorelistQuery = ReportsQuery.getStoreList().toString(),
					strUpdateStoreAssocActiveCDQuery = ReportsQuery.updateStoreAssocActiveCD().toString(),
					strdeleteStoreAssocData = ReportsQuery.deletQueryToStorePromotionASSOC().toString(),
					strUpdateEmergencyIndicator = ReportsQuery.UpdateEmergencyIndicator().toString();

			DateFormat df1 = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_SEVENTEEN), 
					df2 = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_EIGHTEEN);
			SimpleDateFormat sdf = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_SIXTEEN);
			String strDate = sdf.format(recvDTTM);
			String strEventTypSeqNBr = "", strPluStartDate = "", strPluEndDate = "";
			Date date = new Date();
			String strTodaysDate = df2.format(date);
			Object[] objstoreParms;


			for(PromotionStoresAssocBean objPromotionStoresAssocBean : items){

				if(objPromotionStoresAssocBean.getAdEventRelInd().equals(PhotoOmniConstants.PCS_EVENT_REL_IND)){

					strEventTypSeqNBr = objPromotionStoresAssocBean.getAdvEventType() 
							+PhotoOmniConstants.COMMA_DELIMITER+ objPromotionStoresAssocBean.getAdvEventSeqNbr()
							+PhotoOmniConstants.COMMA_DELIMITER+strDate;

					if(!lstEventType.contains(strEventTypSeqNBr)){
						jdbcTemplate.update(strdeleteStoreAssocData, new Object[] {objPromotionStoresAssocBean.getAdvEventType(),
								objPromotionStoresAssocBean.getAdvEventSeqNbr()});
						lstEventType.add(strEventTypSeqNBr);
					}

					lSys_Store_Promo_Assoc_Id = jdbcTemplate.queryForObject(strPKQuery, Long.class);
					strPluStartDate =  df2.format(df1.parse(objPromotionStoresAssocBean.getAdvEvVerStartDate().substring(1, objPromotionStoresAssocBean.getAdvEvVerStartDate().length())));
					strPluEndDate = df2.format(df1.parse(objPromotionStoresAssocBean.getAdvEvVerEndDate().substring(1, objPromotionStoresAssocBean.getAdvEvVerEndDate().length())));
					objstoreParms = new Object[] {lSys_Store_Promo_Assoc_Id, objPromotionStoresAssocBean.getStoreNBR(),
							objPromotionStoresAssocBean.getPluNBR(),strPluStartDate,
							strPluEndDate, 1, objPromotionStoresAssocBean.getAdvEventType(),
							objPromotionStoresAssocBean.getAdvEventSeqNbr(),
							createdUser};

					try{
						jdbcTemplate.update(strStoreAssocTblInQuery, objstoreParms);

						//if Emergency feed file comes need to update Emergency indicator in Om_location table.
						if(strTodaysDate.equals(strPluStartDate) || strTodaysDate.equals(strPluEndDate) ){
							jdbcTemplate.update(strUpdateEmergencyIndicator, new Object[] {objPromotionStoresAssocBean.getStoreNBR()});

						}

					}catch(Exception e){
						LOGGER.error(" Error occoured at PCSFeedFileDBWriter.write() --> " + e);
					}

				}else if(objPromotionStoresAssocBean.getAdEventRelInd().equals(PhotoOmniConstants.PCS_EVENT_REL_IND_D)){

					List<Map<String, Object>> lstStores = jdbcTemplate.queryForList(strStorelistQuery,
							new Object[] {strDate, objPromotionStoresAssocBean.getAdvEventType(), 
							objPromotionStoresAssocBean.getAdvEventSeqNbr()});

					for (Map<String, Object> map : lstStores) {
						jdbcTemplate.update(strUpdateStoreAssocActiveCDQuery, new Object[] { map.get(PhotoOmniConstants.STORE_NBR), 
								objPromotionStoresAssocBean.getAdvEventType(), 
								objPromotionStoresAssocBean.getAdvEventSeqNbr() });
					}

				}

			}
		}
		catch(DataAccessException e){
			LOGGER.error(" Error occoured at PCSFeedFileDBWriter.write() --> " + e);
		}
		catch (Exception e) {
			LOGGER.error(" Error occoured at PCSFeedFileDBWriter.write() --> " + e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From PCSFeedFileDBWriter.write()");
			}
		}
	}
}
