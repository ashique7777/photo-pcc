SET SERVEROUTPUT ON;
-- UPDATE / ADD LOCATIONS IN OM_LOCATION
DECLARE
  CURSOR c_location
  IS  
	SELECT PF_STORE_NBR ,
	  PF_DIST_NBR ,
	  PF_REGION_NBR ,
	  PF_DIVISION_NBR ,
	  PF_MARKET_NBR ,
	  PF_LOCATION_TYPE ,
	  PF_NAME_LOCATION ,
	  PF_ADDR_LINE_1 ,
	  PF_ADDR_LINE_1_MAILING ,
	  PF_ADDR_CITY ,
	  PF_ADDR_STATE_CODE ,
	  PF_ADDR_ZIP_CODE5 ,
	  PF_ADDR_ZIP_CODE4 ,
	  PF_TWENTY_FOUR_HOUR_IND ,
	  PF_DATE_OPEN ,
	  PF_DATE_CLOSED ,
	  PF_STORE_TYPE ,
	  PF_PHONE_NBR ,
	  PF_MANAGER_NAME ,
	  PF_DATE_PROPOSED_OPEN ,
	  PF_DATE_PROPOSED_CLOSED ,
	  PF_BUSINESS_TIME_OPEN_SUN ,
	  PF_BUSINESS_TIME_OPEN_MON ,
	  PF_BUSINESS_TIME_OPEN_TUE ,
	  PF_BUSINESS_TIME_OPEN_WED ,
	  PF_BUSINESS_TIME_OPEN_THU ,
	  PF_BUSINESS_TIME_OPEN_FRI ,
	  PF_BUSINESS_TIME_OPEN_SAT ,
	  PF_BUSINESS_TIME_CLOSED_SUN ,
	  PF_BUSINESS_TIME_CLOSED_MON ,
	  PF_BUSINESS_TIME_CLOSED_TUE ,
	  PF_BUSINESS_TIME_CLOSED_WED ,
	  PF_BUSINESS_TIME_CLOSED_THUS ,
	  PF_BUSINESS_TIME_CLOSED_FRI ,
	  PF_BUSINESS_TIME_CLOSED_SAT ,
	  PF_VALID_MWB_STORE_IND ,
	  PF_RELOCATE_FROM_STORE_NBR ,
	  PF_RELOCATE_TO_STORE_NBR ,
	  PF_PRIOR_DIST_NBR ,
	  PF_LOCATION_LONGITUDE ,
	  PF_LOCATION_LATITUDE ,
	  PF_CREATE_DATE ,
	  PF_CLONE_STORE_NBR ,
	  PF_GRAND_OPEN_DATE ,
	  PF_PHOTOINKJETREFILL_INDICATOR ,
	  PF_PHOTOPRINTERPAPERCOMPANY ,
	  PF_AREA_NUMBER ,
	  PF_CONCEPT_TYPE ,
	  PF_BRANDED_NAME_CODE,
	  NVL(PF_TIME_ZONE,'NA') PF_TIME_ZONE,
	  DECODE(NVL(PF_DAYLIGHT_SAVINGS,'N'),'Y',1,'N',0) PF_DAYLIGHT_SAVINGS
	FROM OM_PF_STORE_MASTER_TMP;
 
  -- DEFINE THE RECORD
  rec_location c_location%ROWTYPE;
  -- DEFINE VARIABLES
  v_syslocid OM_LOCATION.SYS_LOCATION_ID%TYPE;
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_active_flag VARCHAR2(1) := 'A';
  v_default_userid VARCHAR2(10) := 'SYSTEM';
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
  
--  v_deleted_ind OM_LOCATION.PF_TRANSFER_IND%TYPE := "D";
BEGIN
	OPEN c_location;
	LOOP
		FETCH c_location INTO rec_location;
		EXIT WHEN c_location%NOTFOUND OR c_location%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'PF_STORE_NBR=' || rec_location.PF_STORE_NBR);
		BEGIN
		
			-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1
			v_active_cd := 1;
			/*IF (rec_location.PF_TRANSFER_IND IS NOT NULL AND upper(rec_location.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			ELSIF 
				v_active_cd := 1;
			END IF;*/
		
			
			-- CHECK IF RECORD EXISTS
			SELECT SYS_LOCATION_ID INTO v_syslocid FROM OM_LOCATION WHERE LOCATION_NBR = rec_location.PF_STORE_NBR;        
			
			-- UPDATE MACHINE INSTANCE 			
			UPDATE OM_LOCATION
			SET SYS_ENT_ID              = 2 ,
			  SYS_ORG_ID                = 2 ,
			  LOCATION_TYPE             = 'S' ,
			  LOCATION_NBR              = rec_location.PF_STORE_NBR ,
			  IP_ADDRESS                = '' ,
			  DISTRICT_NBR              = rec_location.PF_DIST_NBR ,
			  REGION_NBR                = rec_location.PF_REGION_NBR ,
			  DIVISION_NBR              = rec_location.PF_DIVISION_NBR ,
			  MARKET_NBR                = rec_location.PF_MARKET_NBR ,
			  LOCATION_NAME             = rec_location.PF_NAME_LOCATION ,
			  ADDRESS_LINE_1            = rec_location.PF_ADDR_LINE_1 ,
			  ADDRESS_LINE_2            = rec_location.PF_ADDR_LINE_1_MAILING ,
			  ADDRESS_CITY              = rec_location.PF_ADDR_CITY ,
			  ADDRESS_STATE_CODE        = rec_location.PF_ADDR_STATE_CODE ,
			  ADDRESS_ZIP_CODE          = rec_location.PF_ADDR_ZIP_CODE5 ,
			  ADDRESS_ZIP_EXTENSION     = rec_location.PF_ADDR_ZIP_CODE4 ,
			  POSTAL_CODE               = '' ,
			  COUNTRY_CD                = 'USA' ,
			  TWENTY_FOUR_HOUR_CD       = DECODE(rec_location.PF_TWENTY_FOUR_HOUR_IND,'Y',1,'N',0) ,
			  DATE_OPEN                 = rec_location.PF_DATE_OPEN ,
			  DATE_CLOSED               = rec_location.PF_DATE_CLOSED ,
			  STORE_TYPE                = rec_location.PF_STORE_TYPE ,
			  AREA_CODE                 = SUBSTR(rec_location.PF_PHONE_NBR,1,3) ,
			  FULL_PHONE_NBR            = rec_location.PF_PHONE_NBR ,
			  MANAGER_NAME              = rec_location.PF_MANAGER_NAME ,
			  DATE_PROPOSED_OPEN        = rec_location.PF_DATE_PROPOSED_OPEN ,
			  DATE_PROPOSED_CLOSED      = rec_location.PF_DATE_PROPOSED_CLOSED ,
			  BUSINESS_TIME_OPEN_SUN    = rec_location.PF_BUSINESS_TIME_OPEN_SUN ,
			  BUSINESS_TIME_OPEN_MON    = rec_location.PF_BUSINESS_TIME_OPEN_MON ,
			  BUSINESS_TIME_OPEN_TUE    = rec_location.PF_BUSINESS_TIME_OPEN_TUE ,
			  BUSINESS_TIME_OPEN_WED    = rec_location.PF_BUSINESS_TIME_OPEN_WED ,
			  BUSINESS_TIME_OPEN_THU    = rec_location.PF_BUSINESS_TIME_OPEN_THU ,
			  BUSINESS_TIME_OPEN_FRI    = rec_location.PF_BUSINESS_TIME_OPEN_FRI ,
			  BUSINESS_TIME_OPEN_SAT    = rec_location.PF_BUSINESS_TIME_OPEN_SAT ,			  
			  BUSINESS_TIME_CLOSED_SUN  = rec_location.PF_BUSINESS_TIME_CLOSED_SUN ,
			  BUSINESS_TIME_CLOSED_MON  = rec_location.PF_BUSINESS_TIME_CLOSED_MON ,
			  BUSINESS_TIME_CLOSED_TUE  = rec_location.PF_BUSINESS_TIME_CLOSED_TUE ,
			  BUSINESS_TIME_CLOSED_WED  = rec_location.PF_BUSINESS_TIME_CLOSED_WED ,
			  BUSINESS_TIME_CLOSED_THUS = rec_location.PF_BUSINESS_TIME_CLOSED_THUS ,
			  BUSINESS_TIME_CLOSED_FRI  = rec_location.PF_BUSINESS_TIME_CLOSED_FRI ,
			  BUSINESS_TIME_CLOSED_SAT  = rec_location.PF_BUSINESS_TIME_CLOSED_SAT ,
			  VALID_MWB_STORE_CD        = DECODE(rec_location.PF_VALID_MWB_STORE_IND,'Y',1,'N',0) ,
			  RELOCATE_FROM_STORE_NBR   = rec_location.PF_RELOCATE_FROM_STORE_NBR ,
			  RELOCATE_TO_STORE_NBR     = rec_location.PF_RELOCATE_TO_STORE_NBR ,
			  PRIOR_DIST_NBR            = rec_location.PF_PRIOR_DIST_NBR ,
			  LOCATION_LONGITUDE        = rec_location.PF_LOCATION_LONGITUDE ,
			  LOCATION_LATITUDE         = rec_location.PF_LOCATION_LATITUDE ,
			  CREATE_DATE               = rec_location.PF_CREATE_DATE ,
			  CLONE_STORE_NBR           = rec_location.PF_CLONE_STORE_NBR ,
			  GRAND_OPEN_DATE           = rec_location.PF_GRAND_OPEN_DATE ,
			  CONCEPT_TYPE              = rec_location.PF_CONCEPT_TYPE ,
			  BRANDED_NAME_CODE         = rec_location.PF_BRANDED_NAME_CODE ,
			  TIME_ZONE                 = rec_location.PF_TIME_ZONE ,
			  DAYLIGHT_SAVINGS_CD       = rec_location.PF_DAYLIGHT_SAVINGS ,
			  CREATE_USER_ID            = v_default_userid ,
			  CREATE_DTTM               = sysdate ,
			  UPDATE_USER_ID            = v_default_userid ,
			  UPDATE_DTTM               = sysdate ,
			  ACTIVE_CD                 = v_active_cd 
			WHERE SYS_LOCATION_ID       = v_syslocid ;

			--DBMS_OUTPUT.PUT_LINE ('Updated PF_STORE_NBR:' || rec_location.PF_STORE_NBR);          
		EXCEPTION			
			-- INSERT MACHINE INSTANCE
			WHEN NO_DATA_FOUND THEN
				BEGIN
					INSERT
					INTO OM_LOCATION
					  (
						SYS_LOCATION_ID ,
						SYS_ENT_ID ,
						SYS_ORG_ID ,
						LOCATION_TYPE ,
						LOCATION_NBR ,
						IP_ADDRESS ,
						DISTRICT_NBR ,
						REGION_NBR ,
						DIVISION_NBR ,
						MARKET_NBR ,
						LOCATION_NAME ,
						ADDRESS_LINE_1 ,
						ADDRESS_LINE_2 ,
						ADDRESS_CITY ,
						ADDRESS_STATE_CODE ,
						ADDRESS_ZIP_CODE ,
						ADDRESS_ZIP_EXTENSION ,
						POSTAL_CODE ,
						COUNTRY_CD ,
						TWENTY_FOUR_HOUR_CD ,
						DATE_OPEN ,
						DATE_CLOSED ,
						STORE_TYPE ,
						AREA_CODE ,
						FULL_PHONE_NBR ,
						MANAGER_NAME ,
						DATE_PROPOSED_OPEN ,
						DATE_PROPOSED_CLOSED ,
						BUSINESS_TIME_OPEN_SUN ,
						BUSINESS_TIME_OPEN_MON ,
						BUSINESS_TIME_OPEN_TUE ,
						BUSINESS_TIME_OPEN_WED ,
						BUSINESS_TIME_OPEN_THU ,
						BUSINESS_TIME_OPEN_FRI ,
						BUSINESS_TIME_OPEN_SAT ,
						BUSINESS_TIME_CLOSED_SUN ,
						BUSINESS_TIME_CLOSED_MON ,
						BUSINESS_TIME_CLOSED_TUE ,
						BUSINESS_TIME_CLOSED_WED ,
						BUSINESS_TIME_CLOSED_THUS ,
						BUSINESS_TIME_CLOSED_FRI ,
						BUSINESS_TIME_CLOSED_SAT ,
						VALID_MWB_STORE_CD ,
						RELOCATE_FROM_STORE_NBR ,
						RELOCATE_TO_STORE_NBR ,
						PRIOR_DIST_NBR ,
						LOCATION_LONGITUDE ,
						LOCATION_LATITUDE ,
						CREATE_DATE ,
						CLONE_STORE_NBR ,
						GRAND_OPEN_DATE ,
						CONCEPT_TYPE ,
						BRANDED_NAME_CODE ,
						TIME_ZONE ,
						DAYLIGHT_SAVINGS_CD ,
						CREATE_USER_ID ,
						CREATE_DTTM ,
						UPDATE_USER_ID ,
						UPDATE_DTTM ,
						ACTIVE_CD 
					  )
					  VALUES
					  (
					    OM_LOCATION_SEQ.NEXTVAL ,
						2 ,
						2 ,
						'S' ,
						rec_location.PF_STORE_NBR ,
						'' ,
						rec_location.PF_DIST_NBR ,
						rec_location.PF_REGION_NBR ,
						rec_location.PF_DIVISION_NBR ,
						rec_location.PF_MARKET_NBR ,
						rec_location.PF_NAME_LOCATION ,
						rec_location.PF_ADDR_LINE_1 ,
						rec_location.PF_ADDR_LINE_1_MAILING ,
						rec_location.PF_ADDR_CITY ,
						rec_location.PF_ADDR_STATE_CODE ,
						rec_location.PF_ADDR_ZIP_CODE5 ,
						rec_location.PF_ADDR_ZIP_CODE4 ,
						'' ,
						'USA' ,
						DECODE(rec_location.PF_TWENTY_FOUR_HOUR_IND,'Y',1,'N',0) ,
						rec_location.PF_DATE_OPEN ,
						rec_location.PF_DATE_CLOSED ,
						rec_location.PF_STORE_TYPE ,
						SUBSTR(rec_location.PF_PHONE_NBR,1,3) ,
						rec_location.PF_PHONE_NBR ,
						rec_location.PF_MANAGER_NAME ,
						rec_location.PF_DATE_PROPOSED_OPEN ,
						rec_location.PF_DATE_PROPOSED_CLOSED ,
						rec_location.PF_BUSINESS_TIME_OPEN_SUN ,
						rec_location.PF_BUSINESS_TIME_OPEN_MON ,
						rec_location.PF_BUSINESS_TIME_OPEN_TUE ,
						rec_location.PF_BUSINESS_TIME_OPEN_WED ,
						rec_location.PF_BUSINESS_TIME_OPEN_THU ,
						rec_location.PF_BUSINESS_TIME_OPEN_FRI ,
						rec_location.PF_BUSINESS_TIME_OPEN_SAT ,			  
						rec_location.PF_BUSINESS_TIME_CLOSED_SUN ,
						rec_location.PF_BUSINESS_TIME_CLOSED_MON ,
						rec_location.PF_BUSINESS_TIME_CLOSED_TUE ,
						rec_location.PF_BUSINESS_TIME_CLOSED_WED ,
						rec_location.PF_BUSINESS_TIME_CLOSED_THUS ,
						rec_location.PF_BUSINESS_TIME_CLOSED_FRI ,
						rec_location.PF_BUSINESS_TIME_CLOSED_SAT ,
						DECODE(rec_location.PF_VALID_MWB_STORE_IND,'Y',1,'N',0) ,
						rec_location.PF_RELOCATE_FROM_STORE_NBR ,
						rec_location.PF_RELOCATE_TO_STORE_NBR ,
						rec_location.PF_PRIOR_DIST_NBR ,
						rec_location.PF_LOCATION_LONGITUDE ,
						rec_location.PF_LOCATION_LATITUDE ,
						rec_location.PF_CREATE_DATE ,
						rec_location.PF_CLONE_STORE_NBR ,
						rec_location.PF_GRAND_OPEN_DATE ,
						rec_location.PF_CONCEPT_TYPE ,
						rec_location.PF_BRANDED_NAME_CODE ,
						rec_location.PF_TIME_ZONE ,
						rec_location.PF_DAYLIGHT_SAVINGS ,
						v_default_userid ,
						sysdate ,
						v_default_userid ,
						sysdate ,
						v_active_cd	
					  );
                    

					--DBMS_OUTPUT.PUT_LINE ('Added PF_STORE_NBR:' || rec_location.PF_STORE_NBR);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
					
					INSERT
					INTO OM_PF_STORE_MASTER_BAD
					  (
						PF_STORE_NBR ,
						PF_DIST_NBR ,
						PF_REGION_NBR ,
						PF_DIVISION_NBR ,
						PF_MARKET_NBR ,
						PF_LOCATION_TYPE ,
						PF_NAME_LOCATION ,
						PF_ADDR_LINE_1 ,
						PF_ADDR_LINE_1_MAILING ,
						PF_ADDR_CITY ,
						PF_ADDR_STATE_CODE ,
						PF_ADDR_ZIP_CODE5 ,
						PF_ADDR_ZIP_CODE4 ,
						PF_TWENTY_FOUR_HOUR_IND ,
						PF_DATE_OPEN ,
						PF_DATE_CLOSED ,
						PF_STORE_TYPE ,
						PF_PHONE_NBR ,
						PF_MANAGER_NAME ,
						PF_DATE_PROPOSED_OPEN ,
						PF_DATE_PROPOSED_CLOSED ,
						PF_BUSINESS_TIME_OPEN_SUN ,
						PF_BUSINESS_TIME_OPEN_MON ,
						PF_BUSINESS_TIME_OPEN_TUE ,
						PF_BUSINESS_TIME_OPEN_WED ,
						PF_BUSINESS_TIME_OPEN_THU ,
						PF_BUSINESS_TIME_OPEN_FRI ,
						PF_BUSINESS_TIME_OPEN_SAT ,
						PF_BUSINESS_TIME_CLOSED_SUN ,
						PF_BUSINESS_TIME_CLOSED_MON ,
						PF_BUSINESS_TIME_CLOSED_TUE ,
						PF_BUSINESS_TIME_CLOSED_WED ,
						PF_BUSINESS_TIME_CLOSED_THUS ,
						PF_BUSINESS_TIME_CLOSED_FRI ,
						PF_BUSINESS_TIME_CLOSED_SAT ,
						PF_VALID_MWB_STORE_IND ,
						PF_RELOCATE_FROM_STORE_NBR ,
						PF_RELOCATE_TO_STORE_NBR ,
						PF_PRIOR_DIST_NBR ,
						PF_LOCATION_LONGITUDE ,
						PF_LOCATION_LATITUDE ,
						PF_CREATE_DATE ,
						PF_CLONE_STORE_NBR ,
						PF_GRAND_OPEN_DATE ,
						PF_PHOTOINKJETREFILL_INDICATOR ,
						PF_PHOTOPRINTERPAPERCOMPANY ,
						PF_AREA_NUMBER ,
						PF_CONCEPT_TYPE ,
						PF_BRANDED_NAME_CODE ,
						EXCEPTION_CODE ,
						EXCEPTION_MSSG ,
						EXCEPTION_DTTM
					  )
					  VALUES
					  (
						rec_location.PF_STORE_NBR ,
						rec_location.PF_DIST_NBR ,
						rec_location.PF_REGION_NBR ,
						rec_location.PF_DIVISION_NBR ,
						rec_location.PF_MARKET_NBR ,
						rec_location.PF_LOCATION_TYPE ,
						rec_location.PF_NAME_LOCATION ,
						rec_location.PF_ADDR_LINE_1 ,
						rec_location.PF_ADDR_LINE_1_MAILING ,
						rec_location.PF_ADDR_CITY ,
						rec_location.PF_ADDR_STATE_CODE ,
						rec_location.PF_ADDR_ZIP_CODE5 ,
						rec_location.PF_ADDR_ZIP_CODE4 ,
						rec_location.PF_TWENTY_FOUR_HOUR_IND ,
						rec_location.PF_DATE_OPEN ,
						rec_location.PF_DATE_CLOSED ,
						rec_location.PF_STORE_TYPE ,
						rec_location.PF_PHONE_NBR ,
						rec_location.PF_MANAGER_NAME ,
						rec_location.PF_DATE_PROPOSED_OPEN ,
						rec_location.PF_DATE_PROPOSED_CLOSED ,
						rec_location.PF_BUSINESS_TIME_OPEN_SUN ,
						rec_location.PF_BUSINESS_TIME_OPEN_MON ,
						rec_location.PF_BUSINESS_TIME_OPEN_TUE ,
						rec_location.PF_BUSINESS_TIME_OPEN_WED ,
						rec_location.PF_BUSINESS_TIME_OPEN_THU ,
						rec_location.PF_BUSINESS_TIME_OPEN_FRI ,
						rec_location.PF_BUSINESS_TIME_OPEN_SAT ,
						rec_location.PF_BUSINESS_TIME_CLOSED_SUN ,
						rec_location.PF_BUSINESS_TIME_CLOSED_MON ,
						rec_location.PF_BUSINESS_TIME_CLOSED_TUE ,
						rec_location.PF_BUSINESS_TIME_CLOSED_WED ,
						rec_location.PF_BUSINESS_TIME_CLOSED_THUS ,
						rec_location.PF_BUSINESS_TIME_CLOSED_FRI ,
						rec_location.PF_BUSINESS_TIME_CLOSED_SAT ,
						rec_location.PF_VALID_MWB_STORE_IND ,
						rec_location.PF_RELOCATE_FROM_STORE_NBR ,
						rec_location.PF_RELOCATE_TO_STORE_NBR ,
						rec_location.PF_PRIOR_DIST_NBR ,
						rec_location.PF_LOCATION_LONGITUDE ,
						rec_location.PF_LOCATION_LATITUDE ,
						rec_location.PF_CREATE_DATE ,
						rec_location.PF_CLONE_STORE_NBR ,
						rec_location.PF_GRAND_OPEN_DATE ,
						rec_location.PF_PHOTOINKJETREFILL_INDICATOR ,
						rec_location.PF_PHOTOPRINTERPAPERCOMPANY ,
						rec_location.PF_AREA_NUMBER ,
						rec_location.PF_CONCEPT_TYPE ,
						rec_location.PF_BRANDED_NAME_CODE ,
						v_err_code ,
						v_err_msg ,
						SYSDATE
					);
					
					v_counter := v_counter - 1;
				END;
			WHEN OTHERS THEN
			
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	

				INSERT
				INTO OM_PF_STORE_MASTER_BAD
				  (
					PF_STORE_NBR ,
					PF_DIST_NBR ,
					PF_REGION_NBR ,
					PF_DIVISION_NBR ,
					PF_MARKET_NBR ,
					PF_LOCATION_TYPE ,
					PF_NAME_LOCATION ,
					PF_ADDR_LINE_1 ,
					PF_ADDR_LINE_1_MAILING ,
					PF_ADDR_CITY ,
					PF_ADDR_STATE_CODE ,
					PF_ADDR_ZIP_CODE5 ,
					PF_ADDR_ZIP_CODE4 ,
					PF_TWENTY_FOUR_HOUR_IND ,
					PF_DATE_OPEN ,
					PF_DATE_CLOSED ,
					PF_STORE_TYPE ,
					PF_PHONE_NBR ,
					PF_MANAGER_NAME ,
					PF_DATE_PROPOSED_OPEN ,
					PF_DATE_PROPOSED_CLOSED ,
					PF_BUSINESS_TIME_OPEN_SUN ,
					PF_BUSINESS_TIME_OPEN_MON ,
					PF_BUSINESS_TIME_OPEN_TUE ,
					PF_BUSINESS_TIME_OPEN_WED ,
					PF_BUSINESS_TIME_OPEN_THU ,
					PF_BUSINESS_TIME_OPEN_FRI ,
					PF_BUSINESS_TIME_OPEN_SAT ,
					PF_BUSINESS_TIME_CLOSED_SUN ,
					PF_BUSINESS_TIME_CLOSED_MON ,
					PF_BUSINESS_TIME_CLOSED_TUE ,
					PF_BUSINESS_TIME_CLOSED_WED ,
					PF_BUSINESS_TIME_CLOSED_THUS ,
					PF_BUSINESS_TIME_CLOSED_FRI ,
					PF_BUSINESS_TIME_CLOSED_SAT ,
					PF_VALID_MWB_STORE_IND ,
					PF_RELOCATE_FROM_STORE_NBR ,
					PF_RELOCATE_TO_STORE_NBR ,
					PF_PRIOR_DIST_NBR ,
					PF_LOCATION_LONGITUDE ,
					PF_LOCATION_LATITUDE ,
					PF_CREATE_DATE ,
					PF_CLONE_STORE_NBR ,
					PF_GRAND_OPEN_DATE ,
					PF_PHOTOINKJETREFILL_INDICATOR ,
					PF_PHOTOPRINTERPAPERCOMPANY ,
					PF_AREA_NUMBER ,
					PF_CONCEPT_TYPE ,
					PF_BRANDED_NAME_CODE ,
					EXCEPTION_CODE ,
					EXCEPTION_MSSG ,
					EXCEPTION_DTTM
				  )
				  VALUES
				  (
					rec_location.PF_STORE_NBR ,
					rec_location.PF_DIST_NBR ,
					rec_location.PF_REGION_NBR ,
					rec_location.PF_DIVISION_NBR ,
					rec_location.PF_MARKET_NBR ,
					rec_location.PF_LOCATION_TYPE ,
					rec_location.PF_NAME_LOCATION ,
					rec_location.PF_ADDR_LINE_1 ,
					rec_location.PF_ADDR_LINE_1_MAILING ,
					rec_location.PF_ADDR_CITY ,
					rec_location.PF_ADDR_STATE_CODE ,
					rec_location.PF_ADDR_ZIP_CODE5 ,
					rec_location.PF_ADDR_ZIP_CODE4 ,
					rec_location.PF_TWENTY_FOUR_HOUR_IND ,
					rec_location.PF_DATE_OPEN ,
					rec_location.PF_DATE_CLOSED ,
					rec_location.PF_STORE_TYPE ,
					rec_location.PF_PHONE_NBR ,
					rec_location.PF_MANAGER_NAME ,
					rec_location.PF_DATE_PROPOSED_OPEN ,
					rec_location.PF_DATE_PROPOSED_CLOSED ,
					rec_location.PF_BUSINESS_TIME_OPEN_SUN ,
					rec_location.PF_BUSINESS_TIME_OPEN_MON ,
					rec_location.PF_BUSINESS_TIME_OPEN_TUE ,
					rec_location.PF_BUSINESS_TIME_OPEN_WED ,
					rec_location.PF_BUSINESS_TIME_OPEN_THU ,
					rec_location.PF_BUSINESS_TIME_OPEN_FRI ,
					rec_location.PF_BUSINESS_TIME_OPEN_SAT ,
					rec_location.PF_BUSINESS_TIME_CLOSED_SUN ,
					rec_location.PF_BUSINESS_TIME_CLOSED_MON ,
					rec_location.PF_BUSINESS_TIME_CLOSED_TUE ,
					rec_location.PF_BUSINESS_TIME_CLOSED_WED ,
					rec_location.PF_BUSINESS_TIME_CLOSED_THUS ,
					rec_location.PF_BUSINESS_TIME_CLOSED_FRI ,
					rec_location.PF_BUSINESS_TIME_CLOSED_SAT ,
					rec_location.PF_VALID_MWB_STORE_IND ,
					rec_location.PF_RELOCATE_FROM_STORE_NBR ,
					rec_location.PF_RELOCATE_TO_STORE_NBR ,
					rec_location.PF_PRIOR_DIST_NBR ,
					rec_location.PF_LOCATION_LONGITUDE ,
					rec_location.PF_LOCATION_LATITUDE ,
					rec_location.PF_CREATE_DATE ,
					rec_location.PF_CLONE_STORE_NBR ,
					rec_location.PF_GRAND_OPEN_DATE ,
					rec_location.PF_PHOTOINKJETREFILL_INDICATOR ,
					rec_location.PF_PHOTOPRINTERPAPERCOMPANY ,
					rec_location.PF_AREA_NUMBER ,
					rec_location.PF_CONCEPT_TYPE ,
					rec_location.PF_BRANDED_NAME_CODE ,
					v_err_code ,
					v_err_msg ,
					SYSDATE
				) ;
				
				v_counter := v_counter - 1;
		END;      
		v_counter := v_counter + 1;
		--DBMS_OUTPUT.PUT_LINE ('Counter :' || v_counter);
		IF (v_counter = v_commitinterval) THEN        
			COMMIT;
			--DBMS_OUTPUT.PUT_LINE ('Committed ' || v_counter || ' records');  
			v_counter := 0;
		END IF;
	   
	END LOOP;
	-- COMMIT CURSOR RECORDS
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || v_counter || ' records');  
    
	-- INSERT RECORDS THAT DO NOT MATCH JOIN CONDITION OF THE CURSOR QUERY
	 
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || ' mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
