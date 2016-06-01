SET SERVEROUTPUT ON;
-- UPDATE / ADD ASSOCIATIONS IN OM_STORE_VENDOR_ASSOC
DECLARE
  CURSOR c_store_vendor_assoc
  IS  
	SELECT OM_PF_STORE_VENDOR_ASSOC_TMP.PF_VENDOR_ID ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.PF_LOCATION_NUMBER ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.PF_LOCATION_TYPE ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.PF_ACTIVE_IND ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.PF_DEACTIVATION_DTTM ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.PF_TRANSFER_IND ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.ID_CREATED ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.DATE_TIME_CREATED ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.ID_MODIFIED ,
	  OM_PF_STORE_VENDOR_ASSOC_TMP.DATE_TIME_MODIFIED ,
	  OM_VENDOR.SYS_VENDOR_ID ,
	  OM_LOCATION.SYS_LOCATION_ID 
	FROM OM_PF_STORE_VENDOR_ASSOC_TMP, OM_VENDOR, OM_LOCATION
	WHERE OM_PF_STORE_VENDOR_ASSOC_TMP.PF_VENDOR_ID = OM_VENDOR.VENDOR_NBR 
	AND OM_PF_STORE_VENDOR_ASSOC_TMP.PF_LOCATION_NUMBER = OM_LOCATION.LOCATION_NBR ;
 
	-- DEFINE THE RECORD
	rec_store_vendor_assoc c_store_vendor_assoc%ROWTYPE;
	-- DEFINE VARIABLES
	v_sys_store_vendor_assoc_id OM_STORE_VENDOR_ASSOC.SYS_STORE_VENDOR_ASSOC_ID%TYPE;
	v_counter NUMBER := 0;
	v_commitinterval NUMBER := 20;
	v_active_cd NUMBER := 0;
	v_deleted_ind OM_PF_VENDOR_TMP.PF_TRANSFER_IND%TYPE := 'D';
	v_err_msg VARCHAR2(200) := '';
	v_err_code VARCHAR2(10) := '';
BEGIN
	OPEN c_store_vendor_assoc;
	LOOP
		FETCH c_store_vendor_assoc INTO rec_store_vendor_assoc;
		EXIT WHEN c_store_vendor_assoc%NOTFOUND OR c_store_vendor_assoc%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'vendor id=' || rec_store_vendor_assoc.PF_VENDOR_ID);
		BEGIN		
		
			v_active_cd := 0;

			-- Check Active Ind
			IF NVL(rec_store_vendor_assoc.PF_ACTIVE_IND,'N') = 'Y' THEN
				v_active_cd := 1;
			END IF;

			-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
			IF (rec_store_vendor_assoc.PF_TRANSFER_IND IS NOT NULL AND upper(rec_store_vendor_assoc.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			END IF;
		
			-- CHECK IF RECORD EXISTS
			SELECT SYS_STORE_VENDOR_ASSOC_ID INTO v_sys_store_vendor_assoc_id FROM OM_STORE_VENDOR_ASSOC WHERE SYS_LOCATION_ID = rec_store_vendor_assoc.SYS_LOCATION_ID AND SYS_VENDOR_ID = rec_store_vendor_assoc.SYS_VENDOR_ID ; 
			
			-- UPDATE OM_STORE_VENDOR_ASSOC 		
			UPDATE OM_STORE_VENDOR_ASSOC
			SET SYS_LOCATION_ID           = rec_store_vendor_assoc.SYS_LOCATION_ID ,
			  SYS_VENDOR_ID               = rec_store_vendor_assoc.SYS_VENDOR_ID ,
			  ACTIVE_CD                   = v_active_cd ,
			  DEACTIVATION_DTTM           = rec_store_vendor_assoc.PF_DEACTIVATION_DTTM ,
			  CREATE_USER_ID              = rec_store_vendor_assoc.ID_CREATED ,
			  CREATE_DTTM                 = rec_store_vendor_assoc.DATE_TIME_CREATED ,
			  UPDATE_USER_ID              = rec_store_vendor_assoc.ID_MODIFIED ,
			  UPDATE_DTTM                 = rec_store_vendor_assoc.DATE_TIME_MODIFIED 
			WHERE SYS_STORE_VENDOR_ASSOC_ID = v_sys_store_vendor_assoc_id ;

			--DBMS_OUTPUT.PUT_LINE ('Updated VENDOR_ID:' || rec_store_vendor_assoc.PF_VENDOR_ID);          
		EXCEPTION
			-- INSERT VENDOR
			WHEN NO_DATA_FOUND THEN
				BEGIN
					INSERT
					INTO OM_STORE_VENDOR_ASSOC
					  (
						SYS_STORE_VENDOR_ASSOC_ID ,
						SYS_LOCATION_ID ,
						SYS_VENDOR_ID ,
						ACTIVE_CD ,
						DEACTIVATION_DTTM ,
						CREATE_USER_ID ,
						CREATE_DTTM ,
						UPDATE_USER_ID ,
						UPDATE_DTTM
					  )
					  VALUES
					  (
						OM_STORE_VENDOR_ASSOC_SEQ.NEXTVAL ,					  
						rec_store_vendor_assoc.SYS_LOCATION_ID ,
						rec_store_vendor_assoc.SYS_VENDOR_ID ,
						v_active_cd ,
						rec_store_vendor_assoc.PF_DEACTIVATION_DTTM ,
						rec_store_vendor_assoc.ID_CREATED ,
						rec_store_vendor_assoc.DATE_TIME_CREATED ,
						rec_store_vendor_assoc.ID_MODIFIED ,
						rec_store_vendor_assoc.DATE_TIME_MODIFIED 					  
					  );
														
					--DBMS_OUTPUT.PUT_LINE ('Added VENDOR_ID:' || rec_store_vendor_assoc.PF_VENDOR_ID);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg); 
										
					INSERT
					INTO OM_PF_STORE_VENDOR_ASSOC_BAD
					  (
						PF_VENDOR_ID ,
						PF_LOCATION_NUMBER ,
						PF_LOCATION_TYPE ,
						PF_ACTIVE_IND ,
						PF_DEACTIVATION_DTTM ,
						PF_TRANSFER_IND ,
						ID_CREATED ,
						DATE_TIME_CREATED ,
						ID_MODIFIED ,
						DATE_TIME_MODIFIED ,
						EXCEPTION_CODE ,
						EXCEPTION_MSSG ,
						EXCEPTION_DTTM
					  )
					  VALUES
					  (
						rec_store_vendor_assoc.PF_VENDOR_ID ,
						rec_store_vendor_assoc.PF_LOCATION_NUMBER ,
						rec_store_vendor_assoc.PF_LOCATION_TYPE ,
						rec_store_vendor_assoc.PF_ACTIVE_IND ,
						rec_store_vendor_assoc.PF_DEACTIVATION_DTTM ,
						rec_store_vendor_assoc.PF_TRANSFER_IND ,
						rec_store_vendor_assoc.ID_CREATED ,
						rec_store_vendor_assoc.DATE_TIME_CREATED ,
						rec_store_vendor_assoc.ID_MODIFIED ,
						rec_store_vendor_assoc.DATE_TIME_MODIFIED ,
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
				INTO OM_PF_STORE_VENDOR_ASSOC_BAD
				  (
					PF_VENDOR_ID ,
					PF_LOCATION_NUMBER ,
					PF_LOCATION_TYPE ,
					PF_ACTIVE_IND ,
					PF_DEACTIVATION_DTTM ,
					PF_TRANSFER_IND ,
					ID_CREATED ,
					DATE_TIME_CREATED ,
					ID_MODIFIED ,
					DATE_TIME_MODIFIED ,
					EXCEPTION_CODE ,
					EXCEPTION_MSSG ,
					EXCEPTION_DTTM
				  )
				  VALUES
				  (
					rec_store_vendor_assoc.PF_VENDOR_ID ,
					rec_store_vendor_assoc.PF_LOCATION_NUMBER ,
					rec_store_vendor_assoc.PF_LOCATION_TYPE ,
					rec_store_vendor_assoc.PF_ACTIVE_IND ,
					rec_store_vendor_assoc.PF_DEACTIVATION_DTTM ,
					rec_store_vendor_assoc.PF_TRANSFER_IND ,
					rec_store_vendor_assoc.ID_CREATED ,
					rec_store_vendor_assoc.DATE_TIME_CREATED ,
					rec_store_vendor_assoc.ID_MODIFIED ,
					rec_store_vendor_assoc.DATE_TIME_MODIFIED ,
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
	INTO OM_PF_STORE_VENDOR_ASSOC_BAD
	  (
		PF_VENDOR_ID ,
		PF_LOCATION_NUMBER ,
		PF_LOCATION_TYPE ,
		PF_ACTIVE_IND ,
		PF_DEACTIVATION_DTTM ,
		PF_TRANSFER_IND ,
		ID_CREATED ,
		DATE_TIME_CREATED ,
		ID_MODIFIED ,
		DATE_TIME_MODIFIED ,
		EXCEPTION_CODE ,
		EXCEPTION_MSSG ,
		EXCEPTION_DTTM
	  )
	SELECT "VENDOR".PF_VENDOR_ID ,
		"VENDOR".PF_LOCATION_NUMBER ,
		"VENDOR".PF_LOCATION_TYPE ,
		"VENDOR".PF_ACTIVE_IND ,
		"VENDOR".PF_DEACTIVATION_DTTM ,
		"VENDOR".PF_TRANSFER_IND ,
		"VENDOR".ID_CREATED ,
		"VENDOR".DATE_TIME_CREATED ,
		"VENDOR".ID_MODIFIED ,
		"VENDOR".DATE_TIME_MODIFIED ,
		'000' , 
		'Master records not found' ,
		SYSDATE		
		FROM OM_PF_STORE_VENDOR_ASSOC_TMP "VENDOR"
		WHERE NOT EXISTS
		(
			SELECT 1
			FROM OM_PF_STORE_VENDOR_ASSOC_TMP, OM_VENDOR, OM_LOCATION
			WHERE OM_PF_STORE_VENDOR_ASSOC_TMP.PF_VENDOR_ID = OM_VENDOR.VENDOR_NBR 
			AND OM_PF_STORE_VENDOR_ASSOC_TMP.PF_LOCATION_NUMBER = OM_LOCATION.LOCATION_NBR 
			AND OM_PF_STORE_VENDOR_ASSOC_TMP.PF_VENDOR_ID = "VENDOR".PF_VENDOR_ID 
			AND OM_PF_STORE_VENDOR_ASSOC_TMP.PF_LOCATION_NUMBER = "VENDOR".PF_LOCATION_NUMBER 
		);
		
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
