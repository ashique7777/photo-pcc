package com.walgreens.omni.utility;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.common.exception.PhotoOmniException;

/**
 * @author dassuv
 *
 */
public class SimRetailBlockReportQuery {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimRetailBlockReportQuery.class);
	
	/**
	 * Method to generate SimRetailBlockOnloadResp Query
	 * @return String getSimRetailBlockOnloadRespQuery
	 */
	public static String getSimRetailBlockOnloadRespQuery() {

		StringBuilder query = new StringBuilder();
		query.append("SELECT PRICE_LEVEL,DESCRIPTION,SYS_PRICE_LEVEL_ID FROM OM_PRICE_LEVEL");
		return query.toString();
	}
	
	/**
	 * Method to generate Location No List
	 * @param locationType
	 * @param number
	 * @return String
	 */
	public static String getLocationNoList(String locationType,List<String> number) {
		
		StringBuilder query = new StringBuilder();
		String LOCATION_TYPE = "";
		
		if(locationType.equalsIgnoreCase("Store")){
			 LOCATION_TYPE = "OMLOC.LOCATION_NBR";
		}else if(locationType.equalsIgnoreCase("District")){
			 LOCATION_TYPE = "OMLOC.DISTRICT_NBR";
		}else if(locationType.equalsIgnoreCase("Region")){
			 LOCATION_TYPE = "OMLOC.REGION_NBR";
		}else if(locationType.equalsIgnoreCase("Chain")){
			 LOCATION_TYPE = "CHAIN";
		}else if(locationType.equalsIgnoreCase("")){
			 LOCATION_TYPE = "OMLOC.LOCATION_NBR";
		}
		
		if(LOCATION_TYPE.equalsIgnoreCase("CHAIN")){
			query.append("SELECT OMLOC.LOCATION_NBR FROM OM_LOCATION OMLOC");
		}else{		
			query.append("SELECT OMLOC.LOCATION_NBR FROM OM_LOCATION OMLOC WHERE "+LOCATION_TYPE+" IN(");	    
			  for(int i=0;i<number.size();i++){			
			    if(i != (number.size()-1) ){
			      query.append(number.get(i));
			      query.append(",");
		        }else{
		    	  query.append(number.get(i));
		          }		
		       }query.append(")");
		 }
		return query.toString();
	}
	
	/**
	 * Method to generate SimRetailBlockReport Query
	 * @param number
	 * @param locationType
	 * @param sortOrder 
	 * @param sortColoumnOne 
	 * @param retailBlock
	 * @throws PhotoOmniException 
	 * @return getGenarateSimRetailBlockReportQuery
	 */
	public static String getGenarateSimRetailBlockReportQuery(String locationType, List<String> number, 
			  String sortColoumnOne, String sortOrder) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		String inQuery = getInQueryforSimRetailBlockOne(number);
		query.append("SELECT * FROM (SELECT T.*,ROWNUM AS RNK FROM(SELECT COUNT(*) OVER () AS TOTAL_ROWS,");
		query.append("OMLOC.LOCATION_NBR  AS LOCATION_NBR,OMLOC.SYS_PRICE_LEVEL_ID AS SYS_PRICE_LEVEL_ID,");
		query.append("OMPRLVL.PRICE_LEVEL AS PRICE_LEVEL,OMPRLVL.DESCRIPTION AS DESCRIPTION");
		query.append(" FROM OM_LOCATION OMLOC,OM_PRICE_LEVEL OMPRLVL ");
		query.append("WHERE (");
		query.append(inQuery);
		query.append(")");
		query.append(" AND OMPRLVL.PRICE_LEVEL = ? AND OMLOC.SYS_PRICE_LEVEL_ID = ? ORDER BY ");
		query.append(sortColoumnOne +" "+sortOrder);
		query.append(")T )OMSILVERRECHEAD WHERE RNK BETWEEN ? AND ? ");
		return query.toString();
	}
	
	/**
	 * Method to Generate SimRetailBlockReport CSV Query
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
	 * Method to get Update SimRetailBlock Query
	 * @param storeNoList
	 * @param tempRetailBlockDescription
	 * @return String
	 * @throws PhotoOmniException 
	 */
	public static String getUpdateSimRetailBlockQuery(List<String> storeNoList,String tempRetailBlockDescription) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		String inQuery = getInQueryforSimRetailBlockOne(storeNoList);
		query.append("UPDATE OM_LOCATION OMLOC SET OMLOC.SYS_PRICE_LEVEL_ID = ");
		query.append("(SELECT OMPRLVL.SYS_PRICE_LEVEL_ID FROM OM_PRICE_LEVEL OMPRLVL WHERE OMPRLVL.DESCRIPTION =");
		query.append("'"+tempRetailBlockDescription+"'");
		query.append(")");
		query.append("WHERE (");
		query.append(inQuery);
		query.append(")");
		return query.toString();
	}
	
	
	/**
	 * This method creates multiple in block for SqlQuery if storeDataList size
	 * is more than 1000.
	 * @param storeDataList contains store.
	 * @return inQuery.
	 * @throws PhotoOmniException custom exception.
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
	
	/**
	 * @return
	 */
	public static String getSimRetailBlockPriceLevelQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT PRICE_LEVEL,DESCRIPTION,SYS_PRICE_LEVEL_ID FROM OM_PRICE_LEVEL WHERE DESCRIPTION = ?");
		return query.toString();
	}

}
