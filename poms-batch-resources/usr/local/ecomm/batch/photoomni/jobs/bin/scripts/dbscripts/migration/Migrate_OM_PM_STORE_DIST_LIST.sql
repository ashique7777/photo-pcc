SET SERVEROUTPUT ON;
-- UPDATE / ADD Store dist list OM_PM_STORE_DIST_LIST

DECLARE
  CURSOR c_pmstoredistlist
  IS
   	SELECT 	 
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_LOCATION_TYPE ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_LOCATION_NUMBER ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_PM_ID ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_PRODUCT_ID ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_START_DTTM ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_END_DTTM ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_GOAL_FACTOR ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_PM_TYPE ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_TIER_TYPE , 
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_ACTIVE_IND ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_DEACTIVATION_DTTM ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_TRANSFER_IND ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.ID_CREATED ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.DATE_TIME_CREATED ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.ID_MODIFIED ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.DATE_TIME_MODIFIED ,
		OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_DISCOUNT_APPLICABLE ,
		OM_PROMOTIONAL_MONEY.SYS_PM_ID ,
		OM_LOCATION.SYS_LOCATION_ID
	FROM OM_PF_PROMO_MNY_STR_ASSOC_TMP, OM_PROMOTIONAL_MONEY, OM_LOCATION
	WHERE OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_PM_ID=OM_PROMOTIONAL_MONEY.PM_NBR
	AND OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_LOCATION_NUMBER = OM_LOCATION.LOCATION_NBR
	AND OM_PROMOTIONAL_MONEY.TYPE = 'PM';


  -- DEFINE THE RECORD
  rec_pmstoredistlist c_pmstoredistlist%ROWTYPE;
  -- DEFINE VARIABLES
  v_syspmdistid OM_PM_STORE_DIST_LIST.SYS_PM_DIST_ID%TYPE:= 0;
  v_deleted_ind OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_pmstoredistlist;
  LOOP
    FETCH c_pmstoredistlist INTO rec_pmstoredistlist;
    EXIT WHEN c_pmstoredistlist%NOTFOUND OR c_pmstoredistlist%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Location Number=' || rec_pmstoredistlist.PF_LOCATION_NUMBER);
    BEGIN

        -- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1
			IF (rec_pmstoredistlist.PF_TRANSFER_IND IS NOT NULL AND upper(rec_pmstoredistlist.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			ELSIF (rec_pmstoredistlist.PF_ACTIVE_IND IS NOT NULL AND upper(rec_pmstoredistlist.PF_ACTIVE_IND) = 'Y') THEN
				v_active_cd := 1;
			ELSE
				v_active_cd := 0;
			END IF;
			
        -- CHECK IF RECORD EXISTS
        SELECT SYS_PM_DIST_ID INTO v_syspmdistid FROM OM_PM_STORE_DIST_LIST 
		WHERE SYS_LOCATION_ID = rec_pmstoredistlist.SYS_LOCATION_ID AND SYS_PM_ID = rec_pmstoredistlist.SYS_PM_ID;
		
			
        -- UPDATE Store Distribution
		UPDATE OM_PM_STORE_DIST_LIST 
		SET CREATE_USER_ID = rec_pmstoredistlist.ID_CREATED,
			CREATE_DTTM = rec_pmstoredistlist.DATE_TIME_CREATED,
			UPDATE_USER_ID = rec_pmstoredistlist.ID_MODIFIED,
			UPDATE_DTTM = rec_pmstoredistlist.DATE_TIME_MODIFIED,
			ACTIVE_CD = v_active_cd
		WHERE  SYS_PM_DIST_ID = v_syspmdistid;

		--DBMS_OUTPUT.PUT_LINE ('Location Number=' || rec_pmstoredistlist.PF_LOCATION_NUMBER) ;       
        EXCEPTION
        -- INSERT STORE_DIST_LIST 
        WHEN NO_DATA_FOUND THEN
		BEGIN
        
		INSERT INTO OM_PM_STORE_DIST_LIST 
		( 
			SYS_PM_DIST_ID,
			SYS_PM_ID,
			SYS_LOCATION_ID,
			CREATE_USER_ID,
			CREATE_DTTM,
			UPDATE_USER_ID,
			UPDATE_DTTM,
			ACTIVE_CD
		 ) 
		 VALUES 
		 (
			OM_PM_STORE_DIST_LIST_SEQ.NEXTVAL,
			rec_pmstoredistlist.SYS_PM_ID ,
			rec_pmstoredistlist.SYS_LOCATION_ID,
			rec_pmstoredistlist.ID_CREATED,
			rec_pmstoredistlist.DATE_TIME_CREATED,
			rec_pmstoredistlist.ID_MODIFIED,
			rec_pmstoredistlist.DATE_TIME_MODIFIED,
			v_active_cd
		  );

        --DBMS_OUTPUT.PUT_LINE ('Added Location Number=' || rec_pmstoredistlist.PF_LOCATION_NUMBER); 
	    EXCEPTION	
			WHEN OTHERS THEN
			v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
			--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				
			
			INSERT INTO OM_PF_PROMO_MNY_STORE_ASOC_BAD 
			( 
				PF_LOCATION_TYPE ,
				PF_LOCATION_NUMBER ,
				PF_PM_ID ,
				PF_PRODUCT_ID ,
				PF_START_DTTM ,
				PF_END_DTTM ,
				PF_GOAL_FACTOR ,
				PF_PM_TYPE ,
				PF_TIER_TYPE , 
				PF_ACTIVE_IND ,
				PF_DEACTIVATION_DTTM ,
				PF_TRANSFER_IND ,
				ID_CREATED ,
				DATE_TIME_CREATED ,
				ID_MODIFIED ,
				DATE_TIME_MODIFIED ,
				PF_DISCOUNT_APPLICABLE ,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
			 ) 
			 VALUES  
			 (  
				rec_pmstoredistlist.PF_LOCATION_TYPE ,
				rec_pmstoredistlist.PF_LOCATION_NUMBER ,
				rec_pmstoredistlist.PF_PM_ID ,
				rec_pmstoredistlist.PF_PRODUCT_ID ,
				rec_pmstoredistlist.PF_START_DTTM ,
				rec_pmstoredistlist.PF_END_DTTM ,
				rec_pmstoredistlist.PF_GOAL_FACTOR ,
				rec_pmstoredistlist.PF_PM_TYPE ,
				rec_pmstoredistlist.PF_TIER_TYPE , 
				rec_pmstoredistlist.PF_ACTIVE_IND ,
				rec_pmstoredistlist.PF_DEACTIVATION_DTTM ,
				rec_pmstoredistlist.PF_TRANSFER_IND ,
				rec_pmstoredistlist.ID_CREATED ,
				rec_pmstoredistlist.DATE_TIME_CREATED ,
				rec_pmstoredistlist.ID_MODIFIED ,
				rec_pmstoredistlist.DATE_TIME_MODIFIED ,
				rec_pmstoredistlist.PF_DISCOUNT_APPLICABLE ,		
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
				
			INSERT INTO OM_PF_PROMO_MNY_STORE_ASOC_BAD 
			( 
				PF_LOCATION_TYPE ,
				PF_LOCATION_NUMBER ,
				PF_PM_ID ,
				PF_PRODUCT_ID ,
				PF_START_DTTM ,
				PF_END_DTTM ,
				PF_GOAL_FACTOR ,
				PF_PM_TYPE ,
				PF_TIER_TYPE , 
				PF_ACTIVE_IND ,
				PF_DEACTIVATION_DTTM ,
				PF_TRANSFER_IND ,
				ID_CREATED ,
				DATE_TIME_CREATED ,
				ID_MODIFIED ,
				DATE_TIME_MODIFIED ,
				PF_DISCOUNT_APPLICABLE ,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
			 ) 
			 VALUES  
			 (  
				rec_pmstoredistlist.PF_LOCATION_TYPE ,
				rec_pmstoredistlist.PF_LOCATION_NUMBER ,
				rec_pmstoredistlist.PF_PM_ID ,
				rec_pmstoredistlist.PF_PRODUCT_ID ,
				rec_pmstoredistlist.PF_START_DTTM ,
				rec_pmstoredistlist.PF_END_DTTM ,
				rec_pmstoredistlist.PF_GOAL_FACTOR ,
				rec_pmstoredistlist.PF_PM_TYPE ,
				rec_pmstoredistlist.PF_TIER_TYPE , 
				rec_pmstoredistlist.PF_ACTIVE_IND ,
				rec_pmstoredistlist.PF_DEACTIVATION_DTTM ,
				rec_pmstoredistlist.PF_TRANSFER_IND ,
				rec_pmstoredistlist.ID_CREATED ,
				rec_pmstoredistlist.DATE_TIME_CREATED ,
				rec_pmstoredistlist.ID_MODIFIED ,
				rec_pmstoredistlist.DATE_TIME_MODIFIED ,
				rec_pmstoredistlist.PF_DISCOUNT_APPLICABLE ,		
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
	INSERT INTO OM_PF_PROMO_MNY_STORE_ASOC_BAD  
	( 
		PF_LOCATION_TYPE ,
		PF_LOCATION_NUMBER ,
		PF_PM_ID ,
		PF_PRODUCT_ID ,
		PF_START_DTTM ,
		PF_END_DTTM ,
		PF_GOAL_FACTOR ,
		PF_PM_TYPE ,
		PF_TIER_TYPE , 
		PF_ACTIVE_IND ,
		PF_DEACTIVATION_DTTM ,
		PF_TRANSFER_IND ,
		ID_CREATED ,
		DATE_TIME_CREATED ,
		ID_MODIFIED ,
		DATE_TIME_MODIFIED ,
		PF_DISCOUNT_APPLICABLE ,
		EXCEPTION_CODE ,
		EXCEPTION_MSSG ,
		EXCEPTION_DTTM 
	 ) 
	 SELECT 	 
		"PROMO".PF_LOCATION_TYPE ,
		"PROMO".PF_LOCATION_NUMBER ,
		"PROMO".PF_PM_ID ,
		"PROMO".PF_PRODUCT_ID ,
		"PROMO".PF_START_DTTM ,
		"PROMO".PF_END_DTTM ,
		"PROMO".PF_GOAL_FACTOR ,
		"PROMO".PF_PM_TYPE ,
		"PROMO".PF_TIER_TYPE , 
		"PROMO".PF_ACTIVE_IND ,
		"PROMO".PF_DEACTIVATION_DTTM ,
		"PROMO".PF_TRANSFER_IND ,
		"PROMO".ID_CREATED ,
		"PROMO".DATE_TIME_CREATED ,
		"PROMO".ID_MODIFIED ,
		"PROMO".DATE_TIME_MODIFIED ,
		"PROMO".PF_DISCOUNT_APPLICABLE ,
		'000' ,
		'Master records not found' ,
		SYSDATE
	 FROM OM_PF_PROMO_MNY_STR_ASSOC_TMP "PROMO"
	 WHERE NOT EXISTS
		( SELECT 1
		  	FROM OM_PF_PROMO_MNY_STR_ASSOC_TMP, OM_PROMOTIONAL_MONEY, OM_LOCATION
			WHERE OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_PM_ID=OM_PROMOTIONAL_MONEY.PM_NBR
			AND OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_LOCATION_NUMBER = OM_LOCATION.LOCATION_NBR
			AND OM_PROMOTIONAL_MONEY.TYPE = 'PM'
		    	AND OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_PM_ID = "PROMO".PF_PM_ID
			AND OM_PF_PROMO_MNY_STR_ASSOC_TMP.PF_LOCATION_NUMBER = "PROMO".PF_LOCATION_NUMBER
		);
END;
/
