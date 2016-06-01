SET SERVEROUTPUT ON;
-- UPDATE / ADD VENDORS IN OM_VENDOR_PRODUCT
DECLARE
  CURSOR c_vendor_product
  IS  
	SELECT OM_PF_VENDOR_PRODUCT_TMP.PF_VENDOR_ID ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_PRODUCT_ID ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_COST ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_DAYS_TO_PROCESS ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_ACTIVE_IND ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_DEACTIVATION_DTTM ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_TRANSFER_IND ,
	  OM_PF_VENDOR_PRODUCT_TMP.ID_CREATED ,
	  OM_PF_VENDOR_PRODUCT_TMP.DATE_TIME_CREATED ,
	  OM_PF_VENDOR_PRODUCT_TMP.ID_MODIFIED ,
	  OM_PF_VENDOR_PRODUCT_TMP.DATE_TIME_MODIFIED ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_RECOMMENDED_SIZE ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_SORT_CODE_IND ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_ELEC_SHIP_SUPPORTED ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_SORT_CODE ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_FORM_ID ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_FORM_IND ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_TRANSMISSION_MODE ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_COST_BY ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_SHIPPING_COST ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_SHIPPING_COST_BY ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_LONG_DESC ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_WIC ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_UPC ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_REPORT_TO_EDI ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_BINDING_COVER_COST ,
	  OM_PF_VENDOR_PRODUCT_TMP.PF_ADDITIONAL_COST ,
	  OM_VENDOR.SYS_VENDOR_ID ,
	  OM_PRODUCT.SYS_PRODUCT_ID
	FROM OM_PF_VENDOR_PRODUCT_TMP, OM_VENDOR, OM_PRODUCT
	WHERE OM_PF_VENDOR_PRODUCT_TMP.PF_VENDOR_ID = OM_VENDOR.VENDOR_NBR
	AND OM_PF_VENDOR_PRODUCT_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR;

 
	-- DEFINE THE RECORD
	rec_vendor_product c_vendor_product%ROWTYPE;
	-- DEFINE VARIABLES
	v_vendor_product_id OM_VENDOR_PRODUCT.SYS_VENDOR_PRODUCT_ID%TYPE;
	v_counter NUMBER := 0;
	v_commitinterval NUMBER := 20;
	v_active_cd NUMBER := 1;
	v_active_flag VARCHAR2(1) := 'A';
	v_deleted_ind OM_PF_VENDOR_TMP.PF_TRANSFER_IND%TYPE := 'D';
	v_err_msg VARCHAR2(200) := '';
	v_err_code VARCHAR2(10) := '';
BEGIN
	OPEN c_vendor_product;
	LOOP
		FETCH c_vendor_product INTO rec_vendor_product;
		EXIT WHEN c_vendor_product%NOTFOUND OR c_vendor_product%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'vendor id=' || rec_vendor_product.PF_VENDOR_ID);
		BEGIN		
		
			v_active_cd := 0;

			-- Check Active Ind
			IF NVL(rec_vendor_product.PF_ACTIVE_IND,'N') = 'Y' THEN
				v_active_cd := 1;
			END IF;

			-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
			IF (rec_vendor_product.PF_TRANSFER_IND IS NOT NULL AND upper(rec_vendor_product.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			END IF;
		
			-- CHECK IF RECORD EXISTS
			SELECT SYS_VENDOR_PRODUCT_ID INTO v_vendor_product_id FROM OM_VENDOR_PRODUCT WHERE  SYS_VENDOR_ID=rec_vendor_product.SYS_VENDOR_ID  AND SYS_PRODUCT_ID=rec_vendor_product.SYS_PRODUCT_ID;
			
			-- UPDATE OM_VENDOR_PRODUCT	
			UPDATE OM_VENDOR_PRODUCT
			SET DAYS_TO_PROCESS       = rec_vendor_product.PF_DAYS_TO_PROCESS ,
			  COST                    = rec_vendor_product.PF_COST ,
			  COST_TYPE               = rec_vendor_product.PF_COST_BY ,
			  SHIPPING_COST_TYPE      = rec_vendor_product.PF_SHIPPING_COST_BY ,
			  SHIPPING_COST           = rec_vendor_product.PF_SHIPPING_COST ,
			  ADDITIONAL_COST         = rec_vendor_product.PF_ADDITIONAL_COST ,
			  BINDING_COST            = rec_vendor_product.PF_BINDING_COVER_COST ,
			  VENDOR_REVENUE_PERCENT  = 0 ,
			  ELECTRONIC_SHIPMENT_CD  = DECODE(rec_vendor_product.PF_ELEC_SHIP_SUPPORTED,'Y',1,'N',0,0) ,
			  FORM_ID                 = rec_vendor_product.PF_FORM_ID ,
			  FORM_CD                 = DECODE(rec_vendor_product.PF_FORM_IND,'Y',1,'N',0,0) ,
			  TRANSMISSION_MODE       = rec_vendor_product.PF_TRANSMISSION_MODE ,
			  ACTIVE_CD               = v_active_cd ,
			  DEACTIVATION_DTTM       = rec_vendor_product.PF_DEACTIVATION_DTTM ,
			  CREATE_USER_ID          = NVL(rec_vendor_product.ID_CREATED,' ') ,
			  CREATE_DTTM             = rec_vendor_product.DATE_TIME_CREATED ,
			  UPDATE_USER_ID          = NVL(rec_vendor_product.ID_MODIFIED,' ') ,
			  UPDATE_DTTM             = rec_vendor_product.DATE_TIME_MODIFIED 
			WHERE SYS_VENDOR_PRODUCT_ID = v_vendor_product_id ;			

			--DBMS_OUTPUT.PUT_LINE ('Updated VENDOR_ID:' || rec_vendor_product.PF_VENDOR_ID);          
		EXCEPTION
			-- INSERT VENDOR
			WHEN NO_DATA_FOUND THEN
				BEGIN
						
					INSERT
					INTO OM_VENDOR_PRODUCT
					  (
						SYS_VENDOR_PRODUCT_ID ,
						SYS_VENDOR_ID ,
						SYS_PRODUCT_ID ,
						DAYS_TO_PROCESS ,
						COST ,
						COST_TYPE ,
						SHIPPING_COST_TYPE ,
						SHIPPING_COST ,
						ADDITIONAL_COST ,
						BINDING_COST ,
						VENDOR_REVENUE_PERCENT ,
						ELECTRONIC_SHIPMENT_CD ,
						FORM_ID ,
						FORM_CD ,
						TRANSMISSION_MODE ,
						ACTIVE_CD ,
						DEACTIVATION_DTTM ,
						CREATE_USER_ID ,
						CREATE_DTTM ,
						UPDATE_USER_ID ,
						UPDATE_DTTM
					  )
					VALUES
					  (
					    OM_VENDOR_PRODUCT_SEQ.NEXTVAL ,
						rec_vendor_product.SYS_VENDOR_ID ,
						rec_vendor_product.SYS_PRODUCT_ID ,
						rec_vendor_product.PF_DAYS_TO_PROCESS ,
						rec_vendor_product.PF_COST ,
						rec_vendor_product.PF_COST_BY ,
						rec_vendor_product.PF_SHIPPING_COST_BY ,
						rec_vendor_product.PF_SHIPPING_COST ,
						rec_vendor_product.PF_ADDITIONAL_COST ,
						rec_vendor_product.PF_BINDING_COVER_COST ,
						0 ,
						DECODE(rec_vendor_product.PF_ELEC_SHIP_SUPPORTED,'Y',1,'N',0,0) ,
						rec_vendor_product.PF_FORM_ID ,
						DECODE(rec_vendor_product.PF_FORM_IND,'Y',1,'N',0,0) ,
						rec_vendor_product.PF_TRANSMISSION_MODE ,
						v_active_cd ,
						rec_vendor_product.PF_DEACTIVATION_DTTM ,
						NVL(rec_vendor_product.ID_CREATED,' ') ,
						rec_vendor_product.DATE_TIME_CREATED ,
						NVL(rec_vendor_product.ID_MODIFIED,' ') ,
						rec_vendor_product.DATE_TIME_MODIFIED 
					  );
									
					--DBMS_OUTPUT.PUT_LINE ('Added VENDOR_ID:' || rec_vendor_product.PF_VENDOR_ID);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg); 
					
					
					INSERT
					INTO OM_PF_VENDOR_PRODUCT_BAD
					  (
						PF_VENDOR_ID ,
						PF_PRODUCT_ID ,
						PF_COST ,
						PF_DAYS_TO_PROCESS ,
						PF_ACTIVE_IND ,
						PF_DEACTIVATION_DTTM ,
						PF_TRANSFER_IND ,
						ID_CREATED ,
						DATE_TIME_CREATED ,
						ID_MODIFIED ,
						DATE_TIME_MODIFIED ,
						PF_RECOMMENDED_SIZE ,
						PF_SORT_CODE_IND ,
						PF_ELEC_SHIP_SUPPORTED ,
						PF_SORT_CODE ,
						PF_FORM_ID ,
						PF_FORM_IND ,
						PF_TRANSMISSION_MODE ,
						PF_COST_BY ,
						PF_SHIPPING_COST ,
						PF_SHIPPING_COST_BY ,
						PF_LONG_DESC ,
						PF_WIC ,
						PF_UPC ,
						PF_REPORT_TO_EDI ,
						PF_BINDING_COVER_COST ,
						PF_ADDITIONAL_COST ,
						EXCEPTION_CODE ,
						EXCEPTION_MSSG ,
						EXCEPTION_DTTM
					  )
					  VALUES
					  (
						rec_vendor_product.PF_VENDOR_ID ,
						rec_vendor_product.PF_PRODUCT_ID ,
						rec_vendor_product.PF_COST ,
						rec_vendor_product.PF_DAYS_TO_PROCESS ,
						rec_vendor_product.PF_ACTIVE_IND ,
						rec_vendor_product.PF_DEACTIVATION_DTTM ,
						rec_vendor_product.PF_TRANSFER_IND ,
						rec_vendor_product.ID_CREATED ,
						rec_vendor_product.DATE_TIME_CREATED ,
						rec_vendor_product.ID_MODIFIED ,
						rec_vendor_product.DATE_TIME_MODIFIED ,
						rec_vendor_product.PF_RECOMMENDED_SIZE ,
						rec_vendor_product.PF_SORT_CODE_IND ,
						rec_vendor_product.PF_ELEC_SHIP_SUPPORTED ,
						rec_vendor_product.PF_SORT_CODE ,
						rec_vendor_product.PF_FORM_ID ,
						rec_vendor_product.PF_FORM_IND ,
						rec_vendor_product.PF_TRANSMISSION_MODE ,
						rec_vendor_product.PF_COST_BY ,
						rec_vendor_product.PF_SHIPPING_COST ,
						rec_vendor_product.PF_SHIPPING_COST_BY ,
						rec_vendor_product.PF_LONG_DESC ,
						rec_vendor_product.PF_WIC ,
						rec_vendor_product.PF_UPC ,
						rec_vendor_product.PF_REPORT_TO_EDI ,
						rec_vendor_product.PF_BINDING_COVER_COST ,
						rec_vendor_product.PF_ADDITIONAL_COST ,
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
				INTO OM_PF_VENDOR_PRODUCT_BAD
				  (
					PF_VENDOR_ID ,
					PF_PRODUCT_ID ,
					PF_COST ,
					PF_DAYS_TO_PROCESS ,
					PF_ACTIVE_IND ,
					PF_DEACTIVATION_DTTM ,
					PF_TRANSFER_IND ,
					ID_CREATED ,
					DATE_TIME_CREATED ,
					ID_MODIFIED ,
					DATE_TIME_MODIFIED ,
					PF_RECOMMENDED_SIZE ,
					PF_SORT_CODE_IND ,
					PF_ELEC_SHIP_SUPPORTED ,
					PF_SORT_CODE ,
					PF_FORM_ID ,
					PF_FORM_IND ,
					PF_TRANSMISSION_MODE ,
					PF_COST_BY ,
					PF_SHIPPING_COST ,
					PF_SHIPPING_COST_BY ,
					PF_LONG_DESC ,
					PF_WIC ,
					PF_UPC ,
					PF_REPORT_TO_EDI ,
					PF_BINDING_COVER_COST ,
					PF_ADDITIONAL_COST ,
					EXCEPTION_CODE ,
					EXCEPTION_MSSG ,
					EXCEPTION_DTTM
				  )
				  VALUES
				  (
					rec_vendor_product.PF_VENDOR_ID ,
					rec_vendor_product.PF_PRODUCT_ID ,
					rec_vendor_product.PF_COST ,
					rec_vendor_product.PF_DAYS_TO_PROCESS ,
					rec_vendor_product.PF_ACTIVE_IND ,
					rec_vendor_product.PF_DEACTIVATION_DTTM ,
					rec_vendor_product.PF_TRANSFER_IND ,
					rec_vendor_product.ID_CREATED ,
					rec_vendor_product.DATE_TIME_CREATED ,
					rec_vendor_product.ID_MODIFIED ,
					rec_vendor_product.DATE_TIME_MODIFIED ,
					rec_vendor_product.PF_RECOMMENDED_SIZE ,
					rec_vendor_product.PF_SORT_CODE_IND ,
					rec_vendor_product.PF_ELEC_SHIP_SUPPORTED ,
					rec_vendor_product.PF_SORT_CODE ,
					rec_vendor_product.PF_FORM_ID ,
					rec_vendor_product.PF_FORM_IND ,
					rec_vendor_product.PF_TRANSMISSION_MODE ,
					rec_vendor_product.PF_COST_BY ,
					rec_vendor_product.PF_SHIPPING_COST ,
					rec_vendor_product.PF_SHIPPING_COST_BY ,
					rec_vendor_product.PF_LONG_DESC ,
					rec_vendor_product.PF_WIC ,
					rec_vendor_product.PF_UPC ,
					rec_vendor_product.PF_REPORT_TO_EDI ,
					rec_vendor_product.PF_BINDING_COVER_COST ,
					rec_vendor_product.PF_ADDITIONAL_COST ,
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
	INTO OM_PF_VENDOR_PRODUCT_BAD
	  (
		PF_VENDOR_ID ,
		PF_PRODUCT_ID ,
		PF_COST ,
		PF_DAYS_TO_PROCESS ,
		PF_ACTIVE_IND ,
		PF_DEACTIVATION_DTTM ,
		PF_TRANSFER_IND ,
		ID_CREATED ,
		DATE_TIME_CREATED ,
		ID_MODIFIED ,
		DATE_TIME_MODIFIED ,
		PF_RECOMMENDED_SIZE ,
		PF_SORT_CODE_IND ,
		PF_ELEC_SHIP_SUPPORTED ,
		PF_SORT_CODE ,
		PF_FORM_ID ,
		PF_FORM_IND ,
		PF_TRANSMISSION_MODE ,
		PF_COST_BY ,
		PF_SHIPPING_COST ,
		PF_SHIPPING_COST_BY ,
		PF_LONG_DESC ,
		PF_WIC ,
		PF_UPC ,
		PF_REPORT_TO_EDI ,
		PF_BINDING_COVER_COST ,
		PF_ADDITIONAL_COST ,
		EXCEPTION_CODE ,
		EXCEPTION_MSSG ,
		EXCEPTION_DTTM 		
	  )
	SELECT "VENDOR_PROD".PF_VENDOR_ID ,
	  "VENDOR_PROD".PF_PRODUCT_ID ,
	  "VENDOR_PROD".PF_COST ,
	  "VENDOR_PROD".PF_DAYS_TO_PROCESS ,
	  "VENDOR_PROD".PF_ACTIVE_IND ,
	  "VENDOR_PROD".PF_DEACTIVATION_DTTM ,
	  "VENDOR_PROD".PF_TRANSFER_IND ,
	  "VENDOR_PROD".ID_CREATED ,
	  "VENDOR_PROD".DATE_TIME_CREATED ,
	  "VENDOR_PROD".ID_MODIFIED ,
	  "VENDOR_PROD".DATE_TIME_MODIFIED ,
	  "VENDOR_PROD".PF_RECOMMENDED_SIZE ,
	  "VENDOR_PROD".PF_SORT_CODE_IND ,
	  "VENDOR_PROD".PF_ELEC_SHIP_SUPPORTED ,
	  "VENDOR_PROD".PF_SORT_CODE ,
	  "VENDOR_PROD".PF_FORM_ID ,
	  "VENDOR_PROD".PF_FORM_IND ,
	  "VENDOR_PROD".PF_TRANSMISSION_MODE ,
	  "VENDOR_PROD".PF_COST_BY ,
	  "VENDOR_PROD".PF_SHIPPING_COST ,
	  "VENDOR_PROD".PF_SHIPPING_COST_BY ,
	  "VENDOR_PROD".PF_LONG_DESC ,
	  "VENDOR_PROD".PF_WIC ,
	  "VENDOR_PROD".PF_UPC ,
	  "VENDOR_PROD".PF_REPORT_TO_EDI ,
	  "VENDOR_PROD".PF_BINDING_COVER_COST ,
	  "VENDOR_PROD".PF_ADDITIONAL_COST ,
	  '000' ,
	  'Master records not found' ,
	  SYSDATE	  
	FROM OM_PF_VENDOR_PRODUCT_TMP "VENDOR_PROD"
		WHERE NOT EXISTS
			(
			  SELECT 1
			  FROM OM_PF_VENDOR_PRODUCT_TMP, OM_VENDOR, OM_PRODUCT
			  WHERE OM_PF_VENDOR_PRODUCT_TMP.PF_VENDOR_ID = OM_VENDOR.VENDOR_NBR
			  AND OM_PF_VENDOR_PRODUCT_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR
			  AND OM_PF_VENDOR_PRODUCT_TMP.PF_VENDOR_ID  = "VENDOR_PROD".PF_VENDOR_ID
			  AND OM_PF_VENDOR_PRODUCT_TMP.PF_PRODUCT_ID = "VENDOR_PROD".PF_PRODUCT_ID
			 );

		
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
