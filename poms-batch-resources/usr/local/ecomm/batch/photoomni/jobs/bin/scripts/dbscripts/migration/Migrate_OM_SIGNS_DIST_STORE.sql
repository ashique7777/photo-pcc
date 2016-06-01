SET SERVEROUTPUT ON;
-- UPDATE / ADD Store Dist list OM_SIGNS_DIST_STORE

DECLARE
  CURSOR c_signsdiststore
  IS
   	SELECT 	 
		OM_PF_EVENT_DIST_STORE_TMP.PF_DISTRIBUTION_ID,
		OM_PF_EVENT_DIST_STORE_TMP.PF_STORE_NBR,
		OM_PF_EVENT_DIST_STORE_TMP.PF_TRANSFER_IND,		
		OM_PF_EVENT_DIST_STORE_TMP.ID_CREATED,
		OM_PF_EVENT_DIST_STORE_TMP.DATE_TIME_CREATED,
		OM_PF_EVENT_DIST_STORE_TMP.ID_MODIFIED,
		OM_PF_EVENT_DIST_STORE_TMP.DATE_TIME_MODIFIED,
		OM_LOCATION.SYS_LOCATION_ID		
	FROM OM_PF_EVENT_DIST_STORE_TMP, OM_LOCATION
	WHERE OM_PF_EVENT_DIST_STORE_TMP.PF_STORE_NBR=OM_LOCATION.LOCATION_NBR;

  -- DEFINE THE RECORD
  rec_signsdiststore c_signsdiststore%ROWTYPE;
  -- DEFINE VARIABLES
  v_syseventdiststoreid OM_SIGNS_DIST_STORE.SYS_EVENT_DIST_STORE_ID%TYPE:= 0;
  v_deleted_ind OM_PF_EVENT_DIST_STORE_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_signsdiststore;
  LOOP
    FETCH c_signsdiststore INTO rec_signsdiststore;
    EXIT WHEN c_signsdiststore%NOTFOUND OR c_signsdiststore%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Store Number=' || rec_signsdiststore.PF_STORE_NBR);
    BEGIN
        -- CHECK IF RECORD EXISTS
        SELECT SYS_EVENT_DIST_STORE_ID INTO v_syseventdiststoreid 
		FROM OM_SIGNS_DIST_STORE 
		WHERE SYS_LOCATION_ID = rec_signsdiststore.SYS_LOCATION_ID 
		AND SYS_DISTRIBUTION_ID = rec_signsdiststore.PF_DISTRIBUTION_ID;
		 
		-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1
			
			IF (rec_signsdiststore.PF_TRANSFER_IND IS NOT NULL AND upper(rec_signsdiststore.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			ELSE 
				v_active_cd := 1;
			END IF;
			
        -- UPDATE Store Distribution
		UPDATE OM_SIGNS_DIST_STORE 
		SET 
			UPDATE_USER_ID=rec_signsdiststore.ID_MODIFIED,
			UPDATE_DTTM=rec_signsdiststore.DATE_TIME_MODIFIED,
			ACTIVE_CD=v_active_cd
		WHERE  SYS_EVENT_DIST_STORE_ID = v_syseventdiststoreid;

		--DBMS_OUTPUT.PUT_LINE ('Store Number=' || rec_signsdiststore.PF_STORE_NBR) ;       
        EXCEPTION
        -- INSERT STORE_DIST_LIST 
        WHEN NO_DATA_FOUND THEN
		BEGIN
        
		INSERT INTO OM_SIGNS_DIST_STORE 
		( 
			SYS_EVENT_DIST_STORE_ID,
			SYS_DISTRIBUTION_ID,
			SYS_LOCATION_ID,
			CREATE_USER_ID,
			CREATE_DTTM,
			UPDATE_USER_ID,
			UPDATE_DTTM,
			ACTIVE_CD
		 ) 
		 VALUES 
		 (
			OM_SIGNS_DIST_STORE_SEQ.NEXTVAL,
			rec_signsdiststore.PF_DISTRIBUTION_ID,
			rec_signsdiststore.SYS_LOCATION_ID,
			rec_signsdiststore.ID_CREATED,
			rec_signsdiststore.DATE_TIME_CREATED,
			rec_signsdiststore.ID_MODIFIED,
			rec_signsdiststore.DATE_TIME_MODIFIED,
			v_active_cd
		  );

        --DBMS_OUTPUT.PUT_LINE ('Added Store Number=' || rec_signsdiststore.PF_STORE_NBR); 
	    EXCEPTION	
			WHEN OTHERS THEN
			v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
			--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				
			INSERT INTO OM_PF_EVENT_DIST_STORE_BAD 
			( 
				PF_DISTRIBUTION_ID,
				PF_STORE_NBR,
				PF_TRANSFER_IND,
				ID_CREATED,
				DATE_TIME_CREATED,
				ID_MODIFIED,
				DATE_TIME_MODIFIED,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
			 ) 
			 VALUES  
			 (  
				rec_signsdiststore.PF_DISTRIBUTION_ID,
				rec_signsdiststore.PF_STORE_NBR,
				rec_signsdiststore.PF_TRANSFER_IND,
				rec_signsdiststore.ID_CREATED,
				rec_signsdiststore.DATE_TIME_CREATED,
				rec_signsdiststore.ID_MODIFIED,
				rec_signsdiststore.DATE_TIME_MODIFIED ,
				v_err_code ,
				v_err_msg ,
				SYSDATE 
			 ) ;

		 v_counter := v_counter + 1;
        END;      
	    WHEN OTHERS THEN
			v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
			--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				
			INSERT INTO OM_PF_EVENT_DIST_STORE_BAD 
			( 
				PF_DISTRIBUTION_ID,
				PF_STORE_NBR,
				PF_TRANSFER_IND,
				ID_CREATED,
				DATE_TIME_CREATED,
				ID_MODIFIED,
				DATE_TIME_MODIFIED,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
			 ) 
			 VALUES  
			 (  
				rec_signsdiststore.PF_DISTRIBUTION_ID,
				rec_signsdiststore.PF_STORE_NBR,
				rec_signsdiststore.PF_TRANSFER_IND,
				rec_signsdiststore.ID_CREATED,
				rec_signsdiststore.DATE_TIME_CREATED,
				rec_signsdiststore.ID_MODIFIED,
				rec_signsdiststore.DATE_TIME_MODIFIED,
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
  INSERT INTO OM_PF_EVENT_DIST_STORE_BAD 
	( 
		PF_DISTRIBUTION_ID,
		PF_STORE_NBR,
		PF_TRANSFER_IND,
		ID_CREATED,
		DATE_TIME_CREATED,
		ID_MODIFIED,
		DATE_TIME_MODIFIED,
		EXCEPTION_CODE ,
		EXCEPTION_MSSG ,
		EXCEPTION_DTTM 
	 ) 
	 SELECT 	 
		"SIGNS".PF_DISTRIBUTION_ID,
		"SIGNS".PF_STORE_NBR,
		"SIGNS".PF_TRANSFER_IND,
		"SIGNS".ID_CREATED,
		"SIGNS".DATE_TIME_CREATED,
		"SIGNS".ID_MODIFIED,
		"SIGNS".DATE_TIME_MODIFIED,
		'000' ,
		'Master records not found' ,
		SYSDATE 
	 FROM OM_PF_EVENT_DIST_STORE_TMP "SIGNS"
	 WHERE NOT EXISTS
		( SELECT 1
		  FROM OM_PF_EVENT_DIST_STORE_TMP, OM_LOCATION
		  WHERE OM_PF_EVENT_DIST_STORE_TMP.PF_STORE_NBR=OM_LOCATION.LOCATION_NBR
		  AND OM_PF_EVENT_DIST_STORE_TMP.PF_STORE_NBR = "SIGNS".PF_STORE_NBR
		);
END;
/
