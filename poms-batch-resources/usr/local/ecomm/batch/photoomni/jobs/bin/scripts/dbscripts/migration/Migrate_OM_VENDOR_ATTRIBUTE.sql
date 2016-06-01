SET SERVEROUTPUT ON;
-- UPDATE / ADD VENDORS IN OM_VENDOR
DECLARE
  CURSOR c_vendor_attribute
  IS  
	SELECT OM_PF_VENDOR_TMP.PF_VENDOR_ID ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_NAME ,
	  OM_PF_VENDOR_TMP.PF_ACCT_NBR ,
	  OM_PF_VENDOR_TMP.PF_STREET_ADDRESS ,
	  OM_PF_VENDOR_TMP.PF_CITY ,
	  OM_PF_VENDOR_TMP.PF_STATE ,
	  OM_PF_VENDOR_TMP.PF_ZIP_CODE ,
	  OM_PF_VENDOR_TMP.PF_ZIP_CODE_EXT ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_CONTACT_NAME ,
	  OM_PF_VENDOR_TMP.PF_PHONE_AREA_CODE ,
	  OM_PF_VENDOR_TMP.PF_PHONE_NUMBER ,
	  OM_PF_VENDOR_TMP.PF_EMAIL ,
	  OM_PF_VENDOR_TMP.PF_ELECTRONIC_MANUAL_IND ,
	  OM_PF_VENDOR_TMP.PF_PICK_UP_DAY ,
	  OM_PF_VENDOR_TMP.PF_PICK_UP_DTTM ,
	  OM_PF_VENDOR_TMP.PF_DROP_OFF_DTTM ,
	  OM_PF_VENDOR_TMP.PF_TRANSFER_IND ,
	  OM_PF_VENDOR_TMP.ID_CREATED ,
	  OM_PF_VENDOR_TMP.DATE_TIME_CREATED ,
	  OM_PF_VENDOR_TMP.ID_MODIFIED ,
	  OM_PF_VENDOR_TMP.DATE_TIME_MODIFIED ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_TYPE ,
	  OM_PF_VENDOR_TMP.PF_MARKET_VENDOR_ID ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_ENVLP_PROVIDED ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_SCHEDULE_REQD ,
	  OM_PF_VENDOR_TMP.PF_ORDER_ENTRY_URL_IND ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_TEXT_LINE1 ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_TEXT_LINE2 ,
	  OM_PF_VENDOR_TMP.PF_COST_THRESHOLD_CAP ,
	  OM_PF_VENDOR_TMP.PF_RECALCULATE_COST_IND ,
	  OM_PF_VENDOR_TMP.PF_INVOICE_PROCESS_DAYS ,
	  OM_PF_VENDOR_TMP.PF_THRESH_HOLD_ROYALTY ,
	  OM_PF_VENDOR_TMP.PF_LAST_RUN_DATE ,
	  OM_PF_VENDOR_TMP.PF_NEXT_RUN_DATE ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_COST_REP_IND ,
	  OM_PF_VENDOR_TMP.PF_VENDOR_CATEGORY ,
	  OM_PF_VENDOR_TMP.PF_PARENT_VENDOR ,
	  OM_PF_VENDOR_TMP.PF_REPORT_PRODUCT_ASSOC ,
	  OM_VENDOR.SYS_VENDOR_ID 	  
	FROM OM_PF_VENDOR_TMP, OM_VENDOR 
	WHERE OM_PF_VENDOR_TMP.PF_VENDOR_ID = OM_VENDOR.VENDOR_NBR ;
 
	-- DEFINE THE RECORD
	rec_vendor_attribute c_vendor_attribute%ROWTYPE;
	-- DEFINE VARIABLES
	v_sys_vendor_attribute_id OM_VENDOR_ATTRIBUTE.SYS_VENDOR_ATTR_ID%TYPE;
	v_counter NUMBER := 0;
	v_commitinterval NUMBER := 20;
	v_active_cd NUMBER := 1;
	v_active_flag VARCHAR2(1) := 'A';
	v_deleted_ind OM_PF_VENDOR_TMP.PF_TRANSFER_IND%TYPE := 'D';
	v_err_msg VARCHAR2(200) := '';
	v_err_code VARCHAR2(10) := '';
BEGIN
	OPEN c_vendor_attribute;
	LOOP
		FETCH c_vendor_attribute INTO rec_vendor_attribute;
		EXIT WHEN c_vendor_attribute%NOTFOUND OR c_vendor_attribute%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'vendor id=' || rec_vendor_attribute.PF_VENDOR_ID);
		BEGIN		
			v_active_cd := 1;

			-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
			IF (rec_vendor_attribute.PF_TRANSFER_IND IS NOT NULL AND upper(rec_vendor_attribute.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			END IF;
		
			-- CHECK IF RECORD EXISTS
			SELECT SYS_VENDOR_ATTR_ID INTO v_sys_vendor_attribute_id FROM OM_VENDOR_ATTRIBUTE WHERE SYS_VENDOR_ID=rec_vendor_attribute.SYS_VENDOR_ID; 
			
			-- UPDATE VENDOR ATTRIBUTE		
			UPDATE OM_VENDOR_ATTRIBUTE
			SET SYS_VENDOR_ID               = rec_vendor_attribute.SYS_VENDOR_ID ,
			  PICK_UP_TYPE                  = PICK_UP_TYPE ,
			  PICK_UP_DAYS                  = rec_vendor_attribute.PF_PICK_UP_DAY ,
			  PICK_UP_TIME                  = rec_vendor_attribute.PF_PICK_UP_DTTM ,
			  DROP_OFF_TIME                 = rec_vendor_attribute.PF_DROP_OFF_DTTM ,
			  ENVELOPE_PROVIDED_CD          = DECODE(rec_vendor_attribute.PF_VENDOR_ENVLP_PROVIDED,'Y',1,'N',0,0) ,
			  SCHEDULE_REQUIRED_CD          = DECODE(rec_vendor_attribute.PF_VENDOR_SCHEDULE_REQD,'Y',1,'N',0,0) ,
			  LABEL_VENDOR_TEXT_1           = rec_vendor_attribute.PF_VENDOR_TEXT_LINE1 ,
			  LABEL_VENDOR_TEXT_2           = rec_vendor_attribute.PF_VENDOR_TEXT_LINE2 ,
			  COST_THRESOLD_CAP             = rec_vendor_attribute.PF_COST_THRESHOLD_CAP ,
			  RECALCULATE_COST_CD           = DECODE(rec_vendor_attribute.PF_RECALCULATE_COST_IND,'Y',1,'N',0,0) ,
			  INVOICE_PROCESS_DAYS          = rec_vendor_attribute.PF_INVOICE_PROCESS_DAYS ,
			  THRESHOLD_ROYALTY             = rec_vendor_attribute.PF_THRESH_HOLD_ROYALTY ,
			  VENDOR_COST_CALC_STAGE_CD     = DECODE(NVL(rec_vendor_attribute.PF_VENDOR_COST_REP_IND,'D'),'A',1,0),
			  VENDOR_CATEGORY               = rec_vendor_attribute.PF_VENDOR_CATEGORY ,
			  REFUND_PERCENTAGE             = REFUND_PERCENTAGE ,
			  CJ_SUBSCRIBER                 = CJ_SUBSCRIBER ,
			  CREATE_USER_ID                = rec_vendor_attribute.ID_CREATED ,
			  CREATE_DTTM                   = rec_vendor_attribute.DATE_TIME_CREATED ,
			  UPDATE_USER_ID                = rec_vendor_attribute.ID_MODIFIED ,
			  UPDATE_DTTM                   = rec_vendor_attribute.DATE_TIME_MODIFIED ,
			  ACTIVE_CD                     = v_active_cd
			WHERE SYS_VENDOR_ATTR_ID        = v_sys_vendor_attribute_id ;

			--DBMS_OUTPUT.PUT_LINE ('Updated VENDOR_ID:' || rec_vendor_attribute.PF_VENDOR_ID);          
		EXCEPTION
			-- INSERT VENDOR
			WHEN NO_DATA_FOUND THEN
				BEGIN
									
					INSERT
					INTO OM_VENDOR_ATTRIBUTE
					  (
						SYS_VENDOR_ATTR_ID ,
						SYS_VENDOR_ID ,
						PICK_UP_TYPE ,
						PICK_UP_DAYS ,
						PICK_UP_TIME ,
						DROP_OFF_TIME ,
						ENVELOPE_PROVIDED_CD ,
						SCHEDULE_REQUIRED_CD ,
						LABEL_VENDOR_TEXT_1 ,
						LABEL_VENDOR_TEXT_2 ,
						COST_THRESOLD_CAP ,
						RECALCULATE_COST_CD ,
						INVOICE_PROCESS_DAYS ,
						THRESHOLD_ROYALTY ,
						VENDOR_COST_CALC_STAGE_CD ,
						VENDOR_CATEGORY ,
						REFUND_PERCENTAGE ,
						CJ_SUBSCRIBER ,
						CREATE_USER_ID ,
						CREATE_DTTM ,
						UPDATE_USER_ID ,
						UPDATE_DTTM ,
						ROYALTY_SALES_GEN_TYPE ,
						ROYALTY_REPORT_GENERATION_DAY ,
						ROYALTY_REPORT_GEN_MONTH ,
						ROYALTY_SALES_REPORT_CATEGORY ,
						ROYALTY_REPORT_GEN_QUARTER,
						ACTIVE_CD
					  )
					  VALUES
					  (
					    OM_VENDOR_ATTRIBUTE_SEQ.NEXTVAL ,
						rec_vendor_attribute.SYS_VENDOR_ID ,
						null ,
						rec_vendor_attribute.PF_PICK_UP_DAY ,
						rec_vendor_attribute.PF_PICK_UP_DTTM ,
						rec_vendor_attribute.PF_DROP_OFF_DTTM ,
						DECODE(rec_vendor_attribute.PF_VENDOR_ENVLP_PROVIDED,'Y',1,'N',0,0) ,
						DECODE(rec_vendor_attribute.PF_VENDOR_SCHEDULE_REQD,'Y',1,'N',0,0) ,
						rec_vendor_attribute.PF_VENDOR_TEXT_LINE1 ,
						rec_vendor_attribute.PF_VENDOR_TEXT_LINE2 ,
						rec_vendor_attribute.PF_COST_THRESHOLD_CAP ,
						DECODE(rec_vendor_attribute.PF_RECALCULATE_COST_IND,'Y',1,'N',0,0) ,
						rec_vendor_attribute.PF_INVOICE_PROCESS_DAYS ,
						rec_vendor_attribute.PF_THRESH_HOLD_ROYALTY ,
						DECODE(NVL(rec_vendor_attribute.PF_VENDOR_COST_REP_IND,'D'),'A',1,0) ,
						rec_vendor_attribute.PF_VENDOR_CATEGORY ,
						null ,
						null ,
						rec_vendor_attribute.ID_CREATED ,
						rec_vendor_attribute.DATE_TIME_CREATED ,
						rec_vendor_attribute.ID_MODIFIED ,
						rec_vendor_attribute.DATE_TIME_MODIFIED ,
						' ' ,
						1 ,
						1 ,
						' ' ,
						' ' ,
						v_active_cd			
					  );
									
					--DBMS_OUTPUT.PUT_LINE ('Added VENDOR_ID:' || rec_vendor_attribute.PF_VENDOR_ID);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg); 
					
					INSERT
					INTO OM_PF_VENDOR_BAD
					  (
						PF_VENDOR_ID ,
						PF_VENDOR_NAME ,
						PF_ACCT_NBR ,
						PF_STREET_ADDRESS ,
						PF_CITY ,
						PF_STATE ,
						PF_ZIP_CODE ,
						PF_ZIP_CODE_EXT ,
						PF_VENDOR_CONTACT_NAME ,
						PF_PHONE_AREA_CODE ,
						PF_PHONE_NUMBER ,
						PF_EMAIL ,
						PF_ELECTRONIC_MANUAL_IND ,
						PF_PICK_UP_DAY ,
						PF_PICK_UP_DTTM ,
						PF_DROP_OFF_DTTM ,
						PF_TRANSFER_IND ,
						ID_CREATED ,
						DATE_TIME_CREATED ,
						ID_MODIFIED ,
						DATE_TIME_MODIFIED ,
						PF_VENDOR_TYPE ,
						PF_MARKET_VENDOR_ID ,
						PF_VENDOR_ENVLP_PROVIDED ,
						PF_VENDOR_SCHEDULE_REQD ,
						PF_ORDER_ENTRY_URL_IND ,
						PF_VENDOR_TEXT_LINE1 ,
						PF_VENDOR_TEXT_LINE2 ,
						PF_COST_THRESHOLD_CAP ,
						PF_RECALCULATE_COST_IND ,
						PF_INVOICE_PROCESS_DAYS ,
						PF_THRESH_HOLD_ROYALTY ,
						PF_LAST_RUN_DATE ,
						PF_NEXT_RUN_DATE ,
						PF_VENDOR_COST_REP_IND ,
						PF_VENDOR_CATEGORY ,
						PF_PARENT_VENDOR ,
						PF_REPORT_PRODUCT_ASSOC ,
						EXCEPTION_CODE ,
						EXCEPTION_MSSG ,
						EXCEPTION_DTTM
					  )
					  VALUES
					  (
						rec_vendor_attribute.PF_VENDOR_ID ,
						rec_vendor_attribute.PF_VENDOR_NAME ,
						rec_vendor_attribute.PF_ACCT_NBR ,
						rec_vendor_attribute.PF_STREET_ADDRESS ,
						rec_vendor_attribute.PF_CITY ,
						rec_vendor_attribute.PF_STATE ,
						rec_vendor_attribute.PF_ZIP_CODE ,
						rec_vendor_attribute.PF_ZIP_CODE_EXT ,
						rec_vendor_attribute.PF_VENDOR_CONTACT_NAME ,
						rec_vendor_attribute.PF_PHONE_AREA_CODE ,
						rec_vendor_attribute.PF_PHONE_NUMBER ,
						rec_vendor_attribute.PF_EMAIL ,
						rec_vendor_attribute.PF_ELECTRONIC_MANUAL_IND ,
						rec_vendor_attribute.PF_PICK_UP_DAY ,
						rec_vendor_attribute.PF_PICK_UP_DTTM ,
						rec_vendor_attribute.PF_DROP_OFF_DTTM ,
						rec_vendor_attribute.PF_TRANSFER_IND ,
						rec_vendor_attribute.ID_CREATED ,
						rec_vendor_attribute.DATE_TIME_CREATED ,
						rec_vendor_attribute.ID_MODIFIED ,
						rec_vendor_attribute.DATE_TIME_MODIFIED ,
						rec_vendor_attribute.PF_VENDOR_TYPE ,
						rec_vendor_attribute.PF_MARKET_VENDOR_ID ,
						rec_vendor_attribute.PF_VENDOR_ENVLP_PROVIDED ,
						rec_vendor_attribute.PF_VENDOR_SCHEDULE_REQD ,
						rec_vendor_attribute.PF_ORDER_ENTRY_URL_IND ,
						rec_vendor_attribute.PF_VENDOR_TEXT_LINE1 ,
						rec_vendor_attribute.PF_VENDOR_TEXT_LINE2 ,
						rec_vendor_attribute.PF_COST_THRESHOLD_CAP ,
						rec_vendor_attribute.PF_RECALCULATE_COST_IND ,
						rec_vendor_attribute.PF_INVOICE_PROCESS_DAYS ,
						rec_vendor_attribute.PF_THRESH_HOLD_ROYALTY ,
						rec_vendor_attribute.PF_LAST_RUN_DATE ,
						rec_vendor_attribute.PF_NEXT_RUN_DATE ,
						rec_vendor_attribute.PF_VENDOR_COST_REP_IND ,
						rec_vendor_attribute.PF_VENDOR_CATEGORY ,
						rec_vendor_attribute.PF_PARENT_VENDOR ,
						rec_vendor_attribute.PF_REPORT_PRODUCT_ASSOC ,
						v_err_code ,
						v_err_msg ,
						SYSDATE
					  ) ;

					  
					v_counter := v_counter - 1;
				END;
			WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg); 
				INSERT
				INTO OM_PF_VENDOR_BAD
				  (
					PF_VENDOR_ID ,
					PF_VENDOR_NAME ,
					PF_ACCT_NBR ,
					PF_STREET_ADDRESS ,
					PF_CITY ,
					PF_STATE ,
					PF_ZIP_CODE ,
					PF_ZIP_CODE_EXT ,
					PF_VENDOR_CONTACT_NAME ,
					PF_PHONE_AREA_CODE ,
					PF_PHONE_NUMBER ,
					PF_EMAIL ,
					PF_ELECTRONIC_MANUAL_IND ,
					PF_PICK_UP_DAY ,
					PF_PICK_UP_DTTM ,
					PF_DROP_OFF_DTTM ,
					PF_TRANSFER_IND ,
					ID_CREATED ,
					DATE_TIME_CREATED ,
					ID_MODIFIED ,
					DATE_TIME_MODIFIED ,
					PF_VENDOR_TYPE ,
					PF_MARKET_VENDOR_ID ,
					PF_VENDOR_ENVLP_PROVIDED ,
					PF_VENDOR_SCHEDULE_REQD ,
					PF_ORDER_ENTRY_URL_IND ,
					PF_VENDOR_TEXT_LINE1 ,
					PF_VENDOR_TEXT_LINE2 ,
					PF_COST_THRESHOLD_CAP ,
					PF_RECALCULATE_COST_IND ,
					PF_INVOICE_PROCESS_DAYS ,
					PF_THRESH_HOLD_ROYALTY ,
					PF_LAST_RUN_DATE ,
					PF_NEXT_RUN_DATE ,
					PF_VENDOR_COST_REP_IND ,
					PF_VENDOR_CATEGORY ,
					PF_PARENT_VENDOR ,
					PF_REPORT_PRODUCT_ASSOC ,
					EXCEPTION_CODE ,
					EXCEPTION_MSSG ,
					EXCEPTION_DTTM
				  )
				  VALUES
				  (
					rec_vendor_attribute.PF_VENDOR_ID ,
					rec_vendor_attribute.PF_VENDOR_NAME ,
					rec_vendor_attribute.PF_ACCT_NBR ,
					rec_vendor_attribute.PF_STREET_ADDRESS ,
					rec_vendor_attribute.PF_CITY ,
					rec_vendor_attribute.PF_STATE ,
					rec_vendor_attribute.PF_ZIP_CODE ,
					rec_vendor_attribute.PF_ZIP_CODE_EXT ,
					rec_vendor_attribute.PF_VENDOR_CONTACT_NAME ,
					rec_vendor_attribute.PF_PHONE_AREA_CODE ,
					rec_vendor_attribute.PF_PHONE_NUMBER ,
					rec_vendor_attribute.PF_EMAIL ,
					rec_vendor_attribute.PF_ELECTRONIC_MANUAL_IND ,
					rec_vendor_attribute.PF_PICK_UP_DAY ,
					rec_vendor_attribute.PF_PICK_UP_DTTM ,
					rec_vendor_attribute.PF_DROP_OFF_DTTM ,
					rec_vendor_attribute.PF_TRANSFER_IND ,
					rec_vendor_attribute.ID_CREATED ,
					rec_vendor_attribute.DATE_TIME_CREATED ,
					rec_vendor_attribute.ID_MODIFIED ,
					rec_vendor_attribute.DATE_TIME_MODIFIED ,
					rec_vendor_attribute.PF_VENDOR_TYPE ,
					rec_vendor_attribute.PF_MARKET_VENDOR_ID ,
					rec_vendor_attribute.PF_VENDOR_ENVLP_PROVIDED ,
					rec_vendor_attribute.PF_VENDOR_SCHEDULE_REQD ,
					rec_vendor_attribute.PF_ORDER_ENTRY_URL_IND ,
					rec_vendor_attribute.PF_VENDOR_TEXT_LINE1 ,
					rec_vendor_attribute.PF_VENDOR_TEXT_LINE2 ,
					rec_vendor_attribute.PF_COST_THRESHOLD_CAP ,
					rec_vendor_attribute.PF_RECALCULATE_COST_IND ,
					rec_vendor_attribute.PF_INVOICE_PROCESS_DAYS ,
					rec_vendor_attribute.PF_THRESH_HOLD_ROYALTY ,
					rec_vendor_attribute.PF_LAST_RUN_DATE ,
					rec_vendor_attribute.PF_NEXT_RUN_DATE ,
					rec_vendor_attribute.PF_VENDOR_COST_REP_IND ,
					rec_vendor_attribute.PF_VENDOR_CATEGORY ,
					rec_vendor_attribute.PF_PARENT_VENDOR ,
					rec_vendor_attribute.PF_REPORT_PRODUCT_ASSOC ,
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
	INSERT
	INTO OM_PF_VENDOR_BAD
	  (
		PF_VENDOR_ID ,
		PF_VENDOR_NAME ,
		PF_ACCT_NBR ,
		PF_STREET_ADDRESS ,
		PF_CITY ,
		PF_STATE ,
		PF_ZIP_CODE ,
		PF_ZIP_CODE_EXT ,
		PF_VENDOR_CONTACT_NAME ,
		PF_PHONE_AREA_CODE ,
		PF_PHONE_NUMBER ,
		PF_EMAIL ,
		PF_ELECTRONIC_MANUAL_IND ,
		PF_PICK_UP_DAY ,
		PF_PICK_UP_DTTM ,
		PF_DROP_OFF_DTTM ,
		PF_TRANSFER_IND ,
		ID_CREATED ,
		DATE_TIME_CREATED ,
		ID_MODIFIED ,
		DATE_TIME_MODIFIED ,
		PF_VENDOR_TYPE ,
		PF_MARKET_VENDOR_ID ,
		PF_VENDOR_ENVLP_PROVIDED ,
		PF_VENDOR_SCHEDULE_REQD ,
		PF_ORDER_ENTRY_URL_IND ,
		PF_VENDOR_TEXT_LINE1 ,
		PF_VENDOR_TEXT_LINE2 ,
		PF_COST_THRESHOLD_CAP ,
		PF_RECALCULATE_COST_IND ,
		PF_INVOICE_PROCESS_DAYS ,
		PF_THRESH_HOLD_ROYALTY ,
		PF_LAST_RUN_DATE ,
		PF_NEXT_RUN_DATE ,
		PF_VENDOR_COST_REP_IND ,
		PF_VENDOR_CATEGORY ,
		PF_PARENT_VENDOR ,
		PF_REPORT_PRODUCT_ASSOC ,
		EXCEPTION_CODE ,
		EXCEPTION_MSSG ,
		EXCEPTION_DTTM
	  )
	  SELECT VENDOR.PF_VENDOR_ID ,
		VENDOR.PF_VENDOR_NAME ,
		VENDOR.PF_ACCT_NBR ,
		VENDOR.PF_STREET_ADDRESS ,
		VENDOR.PF_CITY ,
		VENDOR.PF_STATE ,
		VENDOR.PF_ZIP_CODE ,
		VENDOR.PF_ZIP_CODE_EXT ,
		VENDOR.PF_VENDOR_CONTACT_NAME ,
		VENDOR.PF_PHONE_AREA_CODE ,
		VENDOR.PF_PHONE_NUMBER ,
		VENDOR.PF_EMAIL ,
		VENDOR.PF_ELECTRONIC_MANUAL_IND ,
		VENDOR.PF_PICK_UP_DAY ,
		VENDOR.PF_PICK_UP_DTTM ,
		VENDOR.PF_DROP_OFF_DTTM ,
		VENDOR.PF_TRANSFER_IND ,
		VENDOR.ID_CREATED ,
		VENDOR.DATE_TIME_CREATED ,
		VENDOR.ID_MODIFIED ,
		VENDOR.DATE_TIME_MODIFIED ,
		VENDOR.PF_VENDOR_TYPE ,
		VENDOR.PF_MARKET_VENDOR_ID ,
		VENDOR.PF_VENDOR_ENVLP_PROVIDED ,
		VENDOR.PF_VENDOR_SCHEDULE_REQD ,
		VENDOR.PF_ORDER_ENTRY_URL_IND ,
		VENDOR.PF_VENDOR_TEXT_LINE1 ,
		VENDOR.PF_VENDOR_TEXT_LINE2 ,
		VENDOR.PF_COST_THRESHOLD_CAP ,
		VENDOR.PF_RECALCULATE_COST_IND ,
		VENDOR.PF_INVOICE_PROCESS_DAYS ,
		VENDOR.PF_THRESH_HOLD_ROYALTY ,
		VENDOR.PF_LAST_RUN_DATE ,
		VENDOR.PF_NEXT_RUN_DATE ,
		VENDOR.PF_VENDOR_COST_REP_IND ,
		VENDOR.PF_VENDOR_CATEGORY ,
		VENDOR.PF_PARENT_VENDOR ,
		VENDOR.PF_REPORT_PRODUCT_ASSOC ,
		'000' , 
		'Master records not found' ,
		SYSDATE		
		FROM OM_PF_VENDOR_TMP VENDOR
		WHERE NOT EXISTS
			(
			  SELECT 1
			  FROM OM_PF_VENDOR_TMP, OM_VENDOR
			  WHERE OM_PF_VENDOR_TMP.PF_VENDOR_ID = OM_VENDOR.VENDOR_NBR 
			  AND OM_PF_VENDOR_TMP.PF_VENDOR_ID  = "VENDOR".PF_VENDOR_ID
			 );

		
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
