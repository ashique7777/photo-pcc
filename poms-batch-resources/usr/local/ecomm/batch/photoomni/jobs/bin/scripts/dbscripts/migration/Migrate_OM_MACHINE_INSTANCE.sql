SET SERVEROUTPUT ON;
-- UPDATE / ADD MACHINES IN OM_MACHINE_INSTANCE
DECLARE
  CURSOR c_machine_instance
  IS  
	SELECT OM_PF_STORE_MACHINE_ASSOC_TMP.PF_MACHINE_TYPE_ID ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_MACHINE_ID ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_LOCATION_NUMBER ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_LOCATION_TYPE ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_START_DTTM ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_ACTUAL_START_DTTM ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_REMOVE_CLICK_CNT ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_ADD_CLICK_CNT ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_ACTIVE_IND ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_DEACTIVATION_DTTM ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_LAST_INV_DTTM ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_LAST_DISKETTE_CNT ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_DISKETTE_CNT ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_TRANSFER_IND ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.ID_CREATED ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.DATE_TIME_CREATED ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.ID_MODIFIED ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.DATE_TIME_MODIFIED ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_MACHINE_ACTIVE_IND ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_MACHINE_ENABLE_IND ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_SOFTWARE_VERSION ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_MINILAB_MACHINE_ID ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_HOST_NAME ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_VERSION_UPDATED_IND ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_IP_ADDRESS ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_LAST_REPORTED_DTTM ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_PAPER_TYPE_ID ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_PRINT_FACTOR ,
	  OM_PF_STORE_MACHINE_ASSOC_TMP.PF_ROLL_FACTOR ,
	  OM_LOCATION.SYS_LOCATION_ID ,
	  OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID ,
	  OM_MACHINE_TYPE.MACHINE_DESC ,
	  OM_MACHINE_TYPE.MACHINE_TYPE_NBR
	FROM OM_PF_STORE_MACHINE_ASSOC_TMP, OM_LOCATION, OM_MACHINE_TYPE
	WHERE OM_PF_STORE_MACHINE_ASSOC_TMP.PF_LOCATION_NUMBER = OM_LOCATION.LOCATION_NBR
	AND OM_PF_STORE_MACHINE_ASSOC_TMP.PF_MACHINE_TYPE_ID = OM_MACHINE_TYPE.MACHINE_TYPE_NBR;

 
  -- DEFINE THE RECORD
  rec_machine_instance c_machine_instance%ROWTYPE;
  -- DEFINE VARIABLES
  v_sysmcinstanceid OM_MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID%TYPE;
  v_counter NUMBER := 0;
  v_pf_active_ind VARCHAR2(1) := 'N';
  v_pf_machine_active_ind VARCHAR2(1) := 'N';
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_active_flag VARCHAR2(1) := 'A';
  v_deleted_ind OM_PF_STORE_MACHINE_ASSOC_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_status_cd VARCHAR2(5);
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
	OPEN c_machine_instance;
	LOOP
		FETCH c_machine_instance INTO rec_machine_instance;
		EXIT WHEN c_machine_instance%NOTFOUND OR c_machine_instance%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'sys machine type id=' || rec_machine_instance.SYS_MACHINE_TYPE_ID || 'machine name=' ||  rec_machine_instance.MACHINE_DESC);
		BEGIN
		
			-- IF PF_TRANSFER_IND = 'D' THEN ACTIVE_CD WILL BE 0 ELSE 1
			v_active_cd := 1;
			IF (rec_machine_instance.PF_TRANSFER_IND IS NOT NULL AND upper(rec_machine_instance.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			ELSIF (rec_machine_instance.PF_ACTIVE_IND IS NOT NULL AND upper(rec_machine_instance.PF_ACTIVE_IND) = 'Y') THEN    
				v_active_cd := 1;
			ELSE
				v_active_cd := 0;
			END IF;
			--DBMS_OUTPUT.PUT_LINE ('v_active_cd  :' || v_active_cd);   
		
			IF (rec_machine_instance.PF_ACTIVE_IND IS NOT NULL) THEN        		
				v_pf_active_ind := rec_machine_instance.PF_ACTIVE_IND;	
			ELSE
				v_pf_active_ind := 'N';
			END IF;
			
			IF (rec_machine_instance.PF_MACHINE_ACTIVE_IND IS NOT NULL) THEN        		
				v_pf_machine_active_ind := rec_machine_instance.PF_MACHINE_ACTIVE_IND;		
			ELSE
				v_pf_machine_active_ind := 'N';			
			END IF;			
			
		
			IF (v_pf_active_ind='Y' AND v_pf_machine_active_ind='Y') THEN 
				v_status_cd := 'A';
			ELSIF (v_pf_active_ind='Y' AND v_pf_machine_active_ind='N') THEN 
				v_status_cd := 'C';
			ELSIF (v_pf_active_ind='N' AND v_pf_machine_active_ind='Y') THEN 
				v_status_cd := 'S';
			ELSIF (v_pf_active_ind='N' AND v_pf_machine_active_ind='N') THEN 
				v_status_cd := 'D';				
			END IF;	

			--DBMS_OUTPUT.PUT_LINE ('v_status_cd  :' || v_status_cd);  
			
			-- CHECK IF RECORD EXISTS
			SELECT SYS_MACHINE_INSTANCE_ID INTO v_sysmcinstanceid FROM OM_MACHINE_INSTANCE WHERE SYS_LOCATION_ID = rec_machine_instance.SYS_LOCATION_ID AND SYS_MACHINE_TYPE_ID=rec_machine_instance.SYS_MACHINE_TYPE_ID AND TRIM(MACHINE_NAME)=TRIM(rec_machine_instance.PF_MACHINE_ID);      
			
			-- UPDATE MACHINE INSTANCE 			
			UPDATE OM_MACHINE_INSTANCE 
			SET SYS_LOCATION_ID = rec_machine_instance.SYS_LOCATION_ID , 
				MACHINE_NAME = rec_machine_instance.PF_MACHINE_ID , 
				SYS_MACHINE_TYPE_ID = rec_machine_instance.SYS_MACHINE_TYPE_ID , 
				ESTIMATED_ACTIVATION_DTTM = rec_machine_instance.PF_START_DTTM , 
				ACTUAL_ACTIVATION_DTTM = rec_machine_instance.PF_ACTUAL_START_DTTM , 
				CLICK_COUNT = rec_machine_instance.PF_ADD_CLICK_CNT , 
				STATUS_CD = v_status_cd , 
				SOFTWARE_VERSION = rec_machine_instance.PF_SOFTWARE_VERSION , 
				ESTIMATED_DEACTIVATION_DTTM = rec_machine_instance.PF_DEACTIVATION_DTTM , 
				ACTUAL_DEACTIVATION_DTTM = rec_machine_instance.PF_DEACTIVATION_DTTM , 
				HOST_NAME = rec_machine_instance.PF_HOST_NAME , 
				VERSION_UPDATED_CD = DECODE(rec_machine_instance.PF_VERSION_UPDATED_IND,'Y',1,'N',0) , 
				IP_ADDRESS = rec_machine_instance.PF_IP_ADDRESS , 
				LAST_REPORTED_DTTM = rec_machine_instance.PF_LAST_REPORTED_DTTM , 
				CREATE_USER_ID = rec_machine_instance.ID_CREATED , 
				CREATE_DTTM = rec_machine_instance.DATE_TIME_CREATED , 
				UPDATE_USER_ID = rec_machine_instance.ID_MODIFIED , 
				UPDATE_DTTM = rec_machine_instance.DATE_TIME_MODIFIED , 
				ACTIVE_CD = v_active_cd 
			WHERE SYS_MACHINE_INSTANCE_ID = v_sysmcinstanceid;

			--DBMS_OUTPUT.PUT_LINE ('Updated SYS_MACHINE_INSTANCE_ID:' || v_sysmcinstanceid);          
		EXCEPTION
			-- INSERT MACHINE INSTANCE
			WHEN NO_DATA_FOUND THEN
				BEGIN
				
				--DBMS_OUTPUT.PUT_LINE ('inserting .....:');   
		
					INSERT
					INTO OM_MACHINE_INSTANCE
					  (
						SYS_MACHINE_INSTANCE_ID ,
						SYS_LOCATION_ID ,
						MACHINE_NAME ,
						SYS_MACHINE_TYPE_ID ,
						ESTIMATED_ACTIVATION_DTTM ,
						ACTUAL_ACTIVATION_DTTM ,
						CLICK_COUNT ,
						STATUS_CD ,
						SOFTWARE_VERSION ,
						ESTIMATED_DEACTIVATION_DTTM ,
						ACTUAL_DEACTIVATION_DTTM ,
						HOST_NAME ,
						VERSION_UPDATED_CD ,
						IP_ADDRESS ,
						LAST_REPORTED_DTTM ,
						CREATE_USER_ID ,
						CREATE_DTTM ,
						UPDATE_USER_ID ,
						UPDATE_DTTM ,
						ACTIVE_CD
					  )
					  VALUES
					  (
						OM_MACHINE_INSTANCE_SEQ.NEXTVAL ,
						rec_machine_instance.SYS_LOCATION_ID , 
						rec_machine_instance.PF_MACHINE_ID , 
						rec_machine_instance.SYS_MACHINE_TYPE_ID , 
						rec_machine_instance.PF_START_DTTM , 
						rec_machine_instance.PF_ACTUAL_START_DTTM , 
						rec_machine_instance.PF_ADD_CLICK_CNT , 
						v_status_cd , 
						rec_machine_instance.PF_SOFTWARE_VERSION , 
						rec_machine_instance.PF_DEACTIVATION_DTTM , 
						rec_machine_instance.PF_DEACTIVATION_DTTM , 
						rec_machine_instance.PF_HOST_NAME , 
						DECODE(rec_machine_instance.PF_VERSION_UPDATED_IND,'Y',1,'N',0) , 
						rec_machine_instance.PF_IP_ADDRESS , 
						rec_machine_instance.PF_LAST_REPORTED_DTTM , 
						rec_machine_instance.ID_CREATED , 
						rec_machine_instance.DATE_TIME_CREATED , 
						rec_machine_instance.ID_MODIFIED , 
						rec_machine_instance.DATE_TIME_MODIFIED , 
						v_active_cd 					  
					  );


					--DBMS_OUTPUT.PUT_LINE ('Added SYS_MACHINE_INSTANCE_ID:' || v_sysmcinstanceid);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
						
					INSERT
					INTO OM_PF_STORE_MACHINE_ASSOC_BAD
					  (
						PF_MACHINE_TYPE_ID ,
						PF_MACHINE_ID ,
						PF_LOCATION_NUMBER ,
						PF_LOCATION_TYPE ,
						PF_START_DTTM ,
						PF_ACTUAL_START_DTTM ,
						PF_REMOVE_CLICK_CNT ,
						PF_ADD_CLICK_CNT ,
						PF_ACTIVE_IND ,
						PF_DEACTIVATION_DTTM ,
						PF_LAST_INV_DTTM ,
						PF_LAST_DISKETTE_CNT ,
						PF_DISKETTE_CNT ,
						PF_TRANSFER_IND ,
						ID_CREATED ,
						DATE_TIME_CREATED ,
						ID_MODIFIED ,
						DATE_TIME_MODIFIED ,
						PF_MACHINE_ACTIVE_IND ,
						PF_MACHINE_ENABLE_IND ,
						PF_SOFTWARE_VERSION ,
						PF_MINILAB_MACHINE_ID ,
						PF_HOST_NAME ,
						PF_VERSION_UPDATED_IND ,
						PF_IP_ADDRESS ,
						PF_LAST_REPORTED_DTTM ,
						PF_PAPER_TYPE_ID ,
						PF_PRINT_FACTOR ,
						PF_ROLL_FACTOR ,
						EXCEPTION_CODE ,
						EXCEPTION_MSSG ,
						EXCEPTION_DTTM
					  )
					  VALUES
					  (
						rec_machine_instance.PF_MACHINE_TYPE_ID ,
						rec_machine_instance.PF_MACHINE_ID ,
						rec_machine_instance.PF_LOCATION_NUMBER ,
						rec_machine_instance.PF_LOCATION_TYPE ,
						rec_machine_instance.PF_START_DTTM ,
						rec_machine_instance.PF_ACTUAL_START_DTTM ,
						rec_machine_instance.PF_REMOVE_CLICK_CNT ,
						rec_machine_instance.PF_ADD_CLICK_CNT ,
						rec_machine_instance.PF_ACTIVE_IND ,
						rec_machine_instance.PF_DEACTIVATION_DTTM ,
						rec_machine_instance.PF_LAST_INV_DTTM ,
						rec_machine_instance.PF_LAST_DISKETTE_CNT ,
						rec_machine_instance.PF_DISKETTE_CNT ,
						rec_machine_instance.PF_TRANSFER_IND ,
						rec_machine_instance.ID_CREATED ,
						rec_machine_instance.DATE_TIME_CREATED ,
						rec_machine_instance.ID_MODIFIED ,
						rec_machine_instance.DATE_TIME_MODIFIED ,
						rec_machine_instance.PF_MACHINE_ACTIVE_IND ,
						rec_machine_instance.PF_MACHINE_ENABLE_IND ,
						rec_machine_instance.PF_SOFTWARE_VERSION ,
						rec_machine_instance.PF_MINILAB_MACHINE_ID ,
						rec_machine_instance.PF_HOST_NAME ,
						rec_machine_instance.PF_VERSION_UPDATED_IND ,
						rec_machine_instance.PF_IP_ADDRESS ,
						rec_machine_instance.PF_LAST_REPORTED_DTTM ,
						rec_machine_instance.PF_PAPER_TYPE_ID ,
						rec_machine_instance.PF_PRINT_FACTOR ,
						rec_machine_instance.PF_ROLL_FACTOR ,
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
				INTO OM_PF_STORE_MACHINE_ASSOC_BAD
				  (
					PF_MACHINE_TYPE_ID ,
					PF_MACHINE_ID ,
					PF_LOCATION_NUMBER ,
					PF_LOCATION_TYPE ,
					PF_START_DTTM ,
					PF_ACTUAL_START_DTTM ,
					PF_REMOVE_CLICK_CNT ,
					PF_ADD_CLICK_CNT ,
					PF_ACTIVE_IND ,
					PF_DEACTIVATION_DTTM ,
					PF_LAST_INV_DTTM ,
					PF_LAST_DISKETTE_CNT ,
					PF_DISKETTE_CNT ,
					PF_TRANSFER_IND ,
					ID_CREATED ,
					DATE_TIME_CREATED ,
					ID_MODIFIED ,
					DATE_TIME_MODIFIED ,
					PF_MACHINE_ACTIVE_IND ,
					PF_MACHINE_ENABLE_IND ,
					PF_SOFTWARE_VERSION ,
					PF_MINILAB_MACHINE_ID ,
					PF_HOST_NAME ,
					PF_VERSION_UPDATED_IND ,
					PF_IP_ADDRESS ,
					PF_LAST_REPORTED_DTTM ,
					PF_PAPER_TYPE_ID ,
					PF_PRINT_FACTOR ,
					PF_ROLL_FACTOR ,
					EXCEPTION_CODE ,
					EXCEPTION_MSSG ,
					EXCEPTION_DTTM
				  )
				  VALUES
				  (
					rec_machine_instance.PF_MACHINE_TYPE_ID ,
					rec_machine_instance.PF_MACHINE_ID ,
					rec_machine_instance.PF_LOCATION_NUMBER ,
					rec_machine_instance.PF_LOCATION_TYPE ,
					rec_machine_instance.PF_START_DTTM ,
					rec_machine_instance.PF_ACTUAL_START_DTTM ,
					rec_machine_instance.PF_REMOVE_CLICK_CNT ,
					rec_machine_instance.PF_ADD_CLICK_CNT ,
					rec_machine_instance.PF_ACTIVE_IND ,
					rec_machine_instance.PF_DEACTIVATION_DTTM ,
					rec_machine_instance.PF_LAST_INV_DTTM ,
					rec_machine_instance.PF_LAST_DISKETTE_CNT ,
					rec_machine_instance.PF_DISKETTE_CNT ,
					rec_machine_instance.PF_TRANSFER_IND ,
					rec_machine_instance.ID_CREATED ,
					rec_machine_instance.DATE_TIME_CREATED ,
					rec_machine_instance.ID_MODIFIED ,
					rec_machine_instance.DATE_TIME_MODIFIED ,
					rec_machine_instance.PF_MACHINE_ACTIVE_IND ,
					rec_machine_instance.PF_MACHINE_ENABLE_IND ,
					rec_machine_instance.PF_SOFTWARE_VERSION ,
					rec_machine_instance.PF_MINILAB_MACHINE_ID ,
					rec_machine_instance.PF_HOST_NAME ,
					rec_machine_instance.PF_VERSION_UPDATED_IND ,
					rec_machine_instance.PF_IP_ADDRESS ,
					rec_machine_instance.PF_LAST_REPORTED_DTTM ,
					rec_machine_instance.PF_PAPER_TYPE_ID ,
					rec_machine_instance.PF_PRINT_FACTOR ,
					rec_machine_instance.PF_ROLL_FACTOR ,
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
	INTO OM_PF_STORE_MACHINE_ASSOC_BAD
	  (
		PF_MACHINE_TYPE_ID ,
		PF_MACHINE_ID ,
		PF_LOCATION_NUMBER ,
		PF_LOCATION_TYPE ,
		PF_START_DTTM ,
		PF_ACTUAL_START_DTTM ,
		PF_REMOVE_CLICK_CNT ,
		PF_ADD_CLICK_CNT ,
		PF_ACTIVE_IND ,
		PF_DEACTIVATION_DTTM ,
		PF_LAST_INV_DTTM ,
		PF_LAST_DISKETTE_CNT ,
		PF_DISKETTE_CNT ,
		PF_TRANSFER_IND ,
		ID_CREATED ,
		DATE_TIME_CREATED ,
		ID_MODIFIED ,
		DATE_TIME_MODIFIED ,
		PF_MACHINE_ACTIVE_IND ,
		PF_MACHINE_ENABLE_IND ,
		PF_SOFTWARE_VERSION ,
		PF_MINILAB_MACHINE_ID ,
		PF_HOST_NAME ,
		PF_VERSION_UPDATED_IND ,
		PF_IP_ADDRESS ,
		PF_LAST_REPORTED_DTTM ,
		PF_PAPER_TYPE_ID ,
		PF_PRINT_FACTOR ,
		PF_ROLL_FACTOR ,
		EXCEPTION_CODE ,
		EXCEPTION_MSSG ,
		EXCEPTION_DTTM
	  )
	SELECT "ASSOC".PF_MACHINE_TYPE_ID ,
	  "ASSOC".PF_MACHINE_ID ,
	  "ASSOC".PF_LOCATION_NUMBER ,
	  "ASSOC".PF_LOCATION_TYPE ,
	  "ASSOC".PF_START_DTTM ,
	  "ASSOC".PF_ACTUAL_START_DTTM ,
	  "ASSOC".PF_REMOVE_CLICK_CNT ,
	  "ASSOC".PF_ADD_CLICK_CNT ,
	  "ASSOC".PF_ACTIVE_IND ,
	  "ASSOC".PF_DEACTIVATION_DTTM ,
	  "ASSOC".PF_LAST_INV_DTTM ,
	  "ASSOC".PF_LAST_DISKETTE_CNT ,
	  "ASSOC".PF_DISKETTE_CNT ,
	  "ASSOC".PF_TRANSFER_IND ,
	  "ASSOC".ID_CREATED ,
	  "ASSOC".DATE_TIME_CREATED ,
	  "ASSOC".ID_MODIFIED ,
	  "ASSOC".DATE_TIME_MODIFIED ,
	  "ASSOC".PF_MACHINE_ACTIVE_IND ,
	  "ASSOC".PF_MACHINE_ENABLE_IND ,
	  "ASSOC".PF_SOFTWARE_VERSION ,
	  "ASSOC".PF_MINILAB_MACHINE_ID ,
	  "ASSOC".PF_HOST_NAME ,
	  "ASSOC".PF_VERSION_UPDATED_IND ,
	  "ASSOC".PF_IP_ADDRESS ,
	  "ASSOC".PF_LAST_REPORTED_DTTM ,
	  "ASSOC".PF_PAPER_TYPE_ID ,
	  "ASSOC".PF_PRINT_FACTOR ,
	  "ASSOC".PF_ROLL_FACTOR,
	  '000' , 
	  'Master records not found' ,
	  SYSDATE
	FROM OM_PF_STORE_MACHINE_ASSOC_TMP "ASSOC"
	WHERE NOT EXISTS
	  (SELECT 1
		FROM OM_PF_STORE_MACHINE_ASSOC_TMP, OM_LOCATION, OM_MACHINE_TYPE
		WHERE OM_PF_STORE_MACHINE_ASSOC_TMP.PF_LOCATION_NUMBER = OM_LOCATION.LOCATION_NBR
		AND OM_PF_STORE_MACHINE_ASSOC_TMP.PF_MACHINE_TYPE_ID = OM_MACHINE_TYPE.MACHINE_TYPE_NBR
		AND OM_PF_STORE_MACHINE_ASSOC_TMP.PF_MACHINE_TYPE_ID        = "ASSOC".PF_MACHINE_TYPE_ID
		AND OM_PF_STORE_MACHINE_ASSOC_TMP.PF_LOCATION_NUMBER   = "ASSOC".PF_LOCATION_NUMBER
	 );
	 
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
