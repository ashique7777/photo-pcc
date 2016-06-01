SET SERVEROUTPUT ON;
-- UPDATE / ADD VENDORS IN OM_VENDOR
DECLARE
  CURSOR c_vendor
  IS  
	SELECT PF_VENDOR_ID ,
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
	  PF_REPORT_PRODUCT_ASSOC
	FROM OM_PF_VENDOR_TMP;
 
	-- DEFINE THE RECORD
	rec_vendor c_vendor%ROWTYPE;
	-- DEFINE VARIABLES
	v_sys_vendor_id OM_VENDOR.SYS_VENDOR_ID%TYPE;
	v_counter NUMBER := 0;
	v_commitinterval NUMBER := 20;
	v_active_cd NUMBER := 1;
	v_vendor_type VARCHAR2(64) := '';
	v_sub_vendor_type VARCHAR2(64) := '';
	v_active_flag VARCHAR2(1) := 'A';
	v_deleted_ind OM_PF_VENDOR_TMP.PF_TRANSFER_IND%TYPE := 'D';
	v_err_msg VARCHAR2(200) := '';
	v_err_code VARCHAR2(10) := '';
BEGIN
	OPEN c_vendor;
	LOOP
		FETCH c_vendor INTO rec_vendor;
		EXIT WHEN c_vendor%NOTFOUND OR c_vendor%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'vendor id=' || rec_vendor.PF_VENDOR_ID || 'vendor name=' ||  rec_vendor.PF_VENDOR_NAME);
		BEGIN		
			v_active_cd := 1;

			-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
			IF (rec_vendor.PF_TRANSFER_IND IS NOT NULL AND upper(rec_vendor.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			END IF;
			
			v_vendor_type := 'FULFILLMENT';
			v_sub_vendor_type := '';
			
			IF rec_vendor.PF_VENDOR_TYPE is not null AND UPPER(rec_vendor.PF_VENDOR_TYPE) = 'INTERNET' THEN
				v_vendor_type := 'SOURCE';
				v_sub_vendor_type := 'INTERNET';
			ELSIF rec_vendor.PF_VENDOR_CATEGORY is not null AND UPPER(rec_vendor.PF_VENDOR_CATEGORY) = 'ROYALTY' THEN
				v_vendor_type := 'ROYALTY';
				v_sub_vendor_type := '';			
			END IF;			
		
			-- CHECK IF RECORD EXISTS
			SELECT SYS_VENDOR_ID INTO v_sys_vendor_id FROM OM_VENDOR WHERE VENDOR_NBR=rec_vendor.PF_VENDOR_ID;        
			
			-- UPDATE VENDOR 			
			UPDATE OM_VENDOR
			SET 
			  VENDOR_NBR                   = rec_vendor.PF_VENDOR_ID ,
			  DESCRIPTION                  = rec_vendor.PF_VENDOR_NAME ,
			  BILLING_ACCOUNT_ID           = rec_vendor.PF_MARKET_VENDOR_ID ,
			  ACCOUNT_NBR                  = rec_vendor.PF_ACCT_NBR ,
			  STREET_ADDRESS               = rec_vendor.PF_STREET_ADDRESS ,
			  CITY                         = rec_vendor.PF_CITY ,
			  STATE                        = rec_vendor.PF_STATE ,
			  ZIP_CODE                     = rec_vendor.PF_ZIP_CODE ,
			  ZIP_CODE_EXT                 = rec_vendor.PF_ZIP_CODE_EXT ,
			  POSTAL_CD                    = ' ' ,
			  COUNTRY                      = 'USA' ,
			  CONTACT_NAME                 = rec_vendor.PF_VENDOR_CONTACT_NAME ,
			  AREA_CODE                    = rec_vendor.PF_PHONE_AREA_CODE ,
			  PHONE_NBR                    = rec_vendor.PF_PHONE_NUMBER ,
			  INTL_PHONE_NBR               = null ,
			  EMAIL_ADDR                   = rec_vendor.PF_EMAIL ,
			  VENDOR_TYPE                  = v_vendor_type ,
			  NPT_VENDOR_CD                = 1 ,
			  BOOTS_AFFILIATE              = BOOTS_AFFILIATE ,
			  BOOTS_PARTNER_AFFILIATE      = BOOTS_PARTNER_AFFILIATE ,
			  DEFAULT_CURRENCY_CODE_ID     = 'USD' ,
			  AP_VENDOR_ID                 = rec_vendor.PF_MARKET_VENDOR_ID ,
			  STORE_AVAILABILITY_STATUS_CD = STORE_AVAILABILITY_STATUS_CD ,
			  AVERAGE_ORDER_DELAY          = AVERAGE_ORDER_DELAY ,
			  SCHED_PT_BUFFER              = SCHED_PT_BUFFER ,
			  STATUS_ENCRYPT               = STATUS_ENCRYPT ,
			  PROMISE_TIME_INTERVAL        = PROMISE_TIME_INTERVAL ,
			  CREDIT_CARD_LIMIT            = CREDIT_CARD_LIMIT ,
			  DOWNLOAD_PROTOCOL            = DOWNLOAD_PROTOCOL ,
			  MOBILE_AFFILIATE             = MOBILE_AFFILIATE ,
			  INTERNET_AFFILIATE           = INTERNET_AFFILIATE ,
			  WAG_MOBILE_AFFILIATE         = WAG_MOBILE_AFFILIATE ,
			  ACTIVE_CD                    = v_active_cd ,
			  SUB_TYPE_CD                  = SUB_TYPE_CD ,
			  AFFILIATE_CD                 = AFFILIATE_CD ,
			  CREATE_USER_ID               = NVL(rec_vendor.ID_CREATED,' ') ,
			  CREATE_DTTM                  = rec_vendor.DATE_TIME_CREATED ,
			  UPDATE_USER_ID               = rec_vendor.ID_MODIFIED ,
			  UPDATE_DTTM                  = rec_vendor.DATE_TIME_MODIFIED
			WHERE SYS_VENDOR_ID            = v_sys_vendor_id ;

			--DBMS_OUTPUT.PUT_LINE ('Updated VENDOR_ID:' || rec_vendor.PF_VENDOR_ID);          
		EXCEPTION
			-- INSERT VENDOR
			WHEN NO_DATA_FOUND THEN
				BEGIN
				
				INSERT
				INTO OM_VENDOR
				  (
					SYS_VENDOR_ID ,
					VENDOR_NBR ,
					DESCRIPTION ,
					BILLING_ACCOUNT_ID ,
					ACCOUNT_NBR ,
					STREET_ADDRESS ,
					CITY ,
					STATE ,
					ZIP_CODE ,
					ZIP_CODE_EXT ,
					POSTAL_CD ,
					COUNTRY ,
					CONTACT_NAME ,
					AREA_CODE ,
					PHONE_NBR ,
					INTL_PHONE_NBR ,
					EMAIL_ADDR ,
					VENDOR_TYPE ,
					NPT_VENDOR_CD ,
					BOOTS_AFFILIATE ,
					BOOTS_PARTNER_AFFILIATE ,
					DEFAULT_CURRENCY_CODE_ID ,
					AP_VENDOR_ID ,
					STORE_AVAILABILITY_STATUS_CD ,
					AVERAGE_ORDER_DELAY ,
					SCHED_PT_BUFFER ,
					STATUS_ENCRYPT ,
					PROMISE_TIME_INTERVAL ,
					CREDIT_CARD_LIMIT ,
					DOWNLOAD_PROTOCOL ,
					MOBILE_AFFILIATE ,
					INTERNET_AFFILIATE ,
					WAG_MOBILE_AFFILIATE ,
					ACTIVE_CD ,
					SUB_TYPE_CD ,
					AFFILIATE_CD ,
					CREATE_USER_ID ,
					CREATE_DTTM ,
					UPDATE_USER_ID ,
					UPDATE_DTTM
				  )
				  VALUES
				  (
					  OM_VENDOR_SEQ.NEXTVAL ,
					  rec_vendor.PF_VENDOR_ID ,
					  rec_vendor.PF_VENDOR_NAME ,
					  rec_vendor.PF_MARKET_VENDOR_ID ,
					  rec_vendor.PF_ACCT_NBR ,
					  rec_vendor.PF_STREET_ADDRESS ,
					  rec_vendor.PF_CITY ,
					  rec_vendor.PF_STATE ,
					  rec_vendor.PF_ZIP_CODE ,
					  rec_vendor.PF_ZIP_CODE_EXT ,
					  ' ' ,
					  'USA' ,
					  rec_vendor.PF_VENDOR_CONTACT_NAME ,
					  rec_vendor.PF_PHONE_AREA_CODE ,
					  rec_vendor.PF_PHONE_NUMBER ,
					  null ,
					  rec_vendor.PF_EMAIL ,
					  v_vendor_type,
					  1 ,
					  null ,
					  null ,
					  'USD' ,
					  rec_vendor.PF_MARKET_VENDOR_ID ,
					  null ,
					  null ,
					  null ,
					  null ,
					  null ,
					  null ,
					  null ,
					  null ,
					  null ,
					  null ,
					  v_active_cd ,
					  v_sub_vendor_type ,
					  0 ,
					  NVL(rec_vendor.ID_CREATED,' ') ,
					  rec_vendor.DATE_TIME_CREATED ,
					  rec_vendor.ID_MODIFIED ,
					  rec_vendor.DATE_TIME_MODIFIED			  
				  );

					--DBMS_OUTPUT.PUT_LINE ('Added VENDOR_ID:' || rec_vendor.PF_VENDOR_ID);   
					
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
						rec_vendor.PF_VENDOR_ID ,
						rec_vendor.PF_VENDOR_NAME ,
						rec_vendor.PF_ACCT_NBR ,
						rec_vendor.PF_STREET_ADDRESS ,
						rec_vendor.PF_CITY ,
						rec_vendor.PF_STATE ,
						rec_vendor.PF_ZIP_CODE ,
						rec_vendor.PF_ZIP_CODE_EXT ,
						rec_vendor.PF_VENDOR_CONTACT_NAME ,
						rec_vendor.PF_PHONE_AREA_CODE ,
						rec_vendor.PF_PHONE_NUMBER ,
						rec_vendor.PF_EMAIL ,
						rec_vendor.PF_ELECTRONIC_MANUAL_IND ,
						rec_vendor.PF_PICK_UP_DAY ,
						rec_vendor.PF_PICK_UP_DTTM ,
						rec_vendor.PF_DROP_OFF_DTTM ,
						rec_vendor.PF_TRANSFER_IND ,
						rec_vendor.ID_CREATED ,
						rec_vendor.DATE_TIME_CREATED ,
						rec_vendor.ID_MODIFIED ,
						rec_vendor.DATE_TIME_MODIFIED ,
						rec_vendor.PF_VENDOR_TYPE ,
						rec_vendor.PF_MARKET_VENDOR_ID ,
						rec_vendor.PF_VENDOR_ENVLP_PROVIDED ,
						rec_vendor.PF_VENDOR_SCHEDULE_REQD ,
						rec_vendor.PF_ORDER_ENTRY_URL_IND ,
						rec_vendor.PF_VENDOR_TEXT_LINE1 ,
						rec_vendor.PF_VENDOR_TEXT_LINE2 ,
						rec_vendor.PF_COST_THRESHOLD_CAP ,
						rec_vendor.PF_RECALCULATE_COST_IND ,
						rec_vendor.PF_INVOICE_PROCESS_DAYS ,
						rec_vendor.PF_THRESH_HOLD_ROYALTY ,
						rec_vendor.PF_LAST_RUN_DATE ,
						rec_vendor.PF_NEXT_RUN_DATE ,
						rec_vendor.PF_VENDOR_COST_REP_IND ,
						rec_vendor.PF_VENDOR_CATEGORY ,
						rec_vendor.PF_PARENT_VENDOR ,
						rec_vendor.PF_REPORT_PRODUCT_ASSOC ,
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
					rec_vendor.PF_VENDOR_ID ,
					rec_vendor.PF_VENDOR_NAME ,
					rec_vendor.PF_ACCT_NBR ,
					rec_vendor.PF_STREET_ADDRESS ,
					rec_vendor.PF_CITY ,
					rec_vendor.PF_STATE ,
					rec_vendor.PF_ZIP_CODE ,
					rec_vendor.PF_ZIP_CODE_EXT ,
					rec_vendor.PF_VENDOR_CONTACT_NAME ,
					rec_vendor.PF_PHONE_AREA_CODE ,
					rec_vendor.PF_PHONE_NUMBER ,
					rec_vendor.PF_EMAIL ,
					rec_vendor.PF_ELECTRONIC_MANUAL_IND ,
					rec_vendor.PF_PICK_UP_DAY ,
					rec_vendor.PF_PICK_UP_DTTM ,
					rec_vendor.PF_DROP_OFF_DTTM ,
					rec_vendor.PF_TRANSFER_IND ,
					rec_vendor.ID_CREATED ,
					rec_vendor.DATE_TIME_CREATED ,
					rec_vendor.ID_MODIFIED ,
					rec_vendor.DATE_TIME_MODIFIED ,
					rec_vendor.PF_VENDOR_TYPE ,
					rec_vendor.PF_MARKET_VENDOR_ID ,
					rec_vendor.PF_VENDOR_ENVLP_PROVIDED ,
					rec_vendor.PF_VENDOR_SCHEDULE_REQD ,
					rec_vendor.PF_ORDER_ENTRY_URL_IND ,
					rec_vendor.PF_VENDOR_TEXT_LINE1 ,
					rec_vendor.PF_VENDOR_TEXT_LINE2 ,
					rec_vendor.PF_COST_THRESHOLD_CAP ,
					rec_vendor.PF_RECALCULATE_COST_IND ,
					rec_vendor.PF_INVOICE_PROCESS_DAYS ,
					rec_vendor.PF_THRESH_HOLD_ROYALTY ,
					rec_vendor.PF_LAST_RUN_DATE ,
					rec_vendor.PF_NEXT_RUN_DATE ,
					rec_vendor.PF_VENDOR_COST_REP_IND ,
					rec_vendor.PF_VENDOR_CATEGORY ,
					rec_vendor.PF_PARENT_VENDOR ,
					rec_vendor.PF_REPORT_PRODUCT_ASSOC ,
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
	--COMMIT;
	----DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
