SET SERVEROUTPUT ON;
-- UPDATE / ADD STORE PARAMETER IN OM_STORE_PARAMETER
-- PF_PARAMETER_ID and PF_LOCATION_NUMBER are unique key
DECLARE
  CURSOR c_storeparam
  IS
   	SELECT 
   		PF_PARAMETER_ID,
		PF_LOCATION_NUMBER,
		PF_LOCATION_TYPE,
		PF_PARAMETER_VALUE,
		PF_TRANSFER_IND,
		ID_CREATED,
		DATE_TIME_CREATED,
		ID_MODIFIED,
		DATE_TIME_MODIFIED
	FROM OM_PF_STORE_PARAMETER_TMP;

  -- DEFINE THE RECORD
  rec_storeparam c_storeparam%ROWTYPE;
  -- DEFINE VARIABLES
  v_sys_store_param_id om_store_parameter.sys_store_param_id%TYPE;
  v_deleted_ind om_pf_store_parameter_tmp.pf_transfer_ind%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_blank VARCHAR2(1) := '';
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_storeparam;
  LOOP
    FETCH c_storeparam INTO rec_storeparam;
    EXIT WHEN c_storeparam%NOTFOUND ;
        BEGIN
	
	v_active_cd := 1;
	
	-- Check Active Ind
	
	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
	IF (rec_storeparam.PF_TRANSFER_IND IS NOT NULL AND upper(rec_storeparam.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
		v_active_cd := 0;
	END IF;

        -- CHECK IF RECORD EXISTS
        SELECT sys_store_param_id INTO v_sys_store_param_id FROM om_store_parameter 
        WHERE 
        sys_parameter_id=rec_storeparam.PF_PARAMETER_ID and 
        location_nbr = rec_storeparam.PF_LOCATION_NUMBER;
        
        -- UPDATE STORE PARAMETER
        UPDATE om_store_parameter
        SET 	parameter_value         = rec_storeparam.pf_parameter_value,
                active_cd		= v_active_cd,
          	UPDATE_USER_ID		= rec_storeparam.ID_MODIFIED,
          	UPDATE_DTTM		= rec_storeparam.DATE_TIME_MODIFIED
        WHERE sys_store_param_id = v_sys_store_param_id; 
			
      EXCEPTION

        WHEN NO_DATA_FOUND THEN
		BEGIN
        INSERT
        INTO om_store_parameter
          (
            SYS_STORE_PARAM_ID,
	    SYS_PARAMETER_ID,
	    LOCATION_NBR,
	    LOCATION_TYPE,
	    PARAMETER_VALUE,
	    TEST_PARAMETER_VALUE,
	    CREATE_USER_ID,
	    CREATE_DTTM,
	    UPDATE_USER_ID,
	    UPDATE_DTTM,
	    ACTIVE_CD
          )
          VALUES
          (
            om_store_parameter_seq.NEXTVAL,
            rec_storeparam.PF_PARAMETER_ID,
            rec_storeparam.PF_LOCATION_NUMBER,
            rec_storeparam.PF_LOCATION_TYPE,
	    rec_storeparam.PF_PARAMETER_VALUE,
	    ' ',
	    rec_storeparam.ID_CREATED,
	    rec_storeparam.DATE_TIME_CREATED,
	    rec_storeparam.ID_MODIFIED,
	   rec_storeparam.DATE_TIME_MODIFIED,
           v_active_cd
	  );
        
	    EXCEPTION	
			WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				
			INSERT INTO OM_PF_STORE_PARAMETER_BAD 
			( 
				PF_PARAMETER_ID,
				PF_LOCATION_NUMBER,
				PF_LOCATION_TYPE,
				PF_PARAMETER_VALUE,
				PF_TRANSFER_IND,
				ID_CREATED,
				DATE_TIME_CREATED,
				ID_MODIFIED,
				DATE_TIME_MODIFIED,
				EXCEPTION_CODE,
				EXCEPTION_MSSG,
				EXCEPTION_DTTM
			) 
			VALUES  
			(  
				rec_storeparam.PF_PARAMETER_ID,
				rec_storeparam.PF_LOCATION_NUMBER,
				rec_storeparam.PF_LOCATION_TYPE,
				rec_storeparam.PF_PARAMETER_VALUE,
				rec_storeparam.PF_TRANSFER_IND,
				rec_storeparam.ID_CREATED,
				rec_storeparam.DATE_TIME_CREATED,
				rec_storeparam.ID_MODIFIED,
				rec_storeparam.DATE_TIME_MODIFIED,
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
				
			INSERT INTO OM_PF_STORE_PARAMETER_BAD 
			( 
				PF_PARAMETER_ID,
				PF_LOCATION_NUMBER,
				PF_LOCATION_TYPE,
				PF_PARAMETER_VALUE,
				PF_TRANSFER_IND,
				ID_CREATED,
				DATE_TIME_CREATED,
				ID_MODIFIED,
				DATE_TIME_MODIFIED,
				EXCEPTION_CODE,
				EXCEPTION_MSSG,
				EXCEPTION_DTTM
			) 
			VALUES  
			(  
				rec_storeparam.PF_PARAMETER_ID,
				rec_storeparam.PF_LOCATION_NUMBER,
				rec_storeparam.PF_LOCATION_TYPE,
				rec_storeparam.PF_PARAMETER_VALUE,
				rec_storeparam.PF_TRANSFER_IND,
				rec_storeparam.ID_CREATED,
				rec_storeparam.DATE_TIME_CREATED,
				rec_storeparam.ID_MODIFIED,
				rec_storeparam.DATE_TIME_MODIFIED,
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
  EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);

END;
/
