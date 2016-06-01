SET SERVEROUTPUT ON;
-- UPDATE / ADD PARAMETERS IN OM_PARAMETER_MASTER
DECLARE
  CURSOR c_parammaster
  IS
   	SELECT 
		PF_PARAMETER_ID,
		PF_PARAMETER_TYPE,
		PF_PARAMETER_NAME,
		PF_DESC,
		PF_PARAMETER_VALUE,
		PF_ACTIVE_IND,
		PF_DEACTIVATION_DTTM,
		PF_TRANSFER_IND,
		ID_CREATED,
		DATE_TIME_CREATED,
		ID_MODIFIED,
		DATE_TIME_MODIFIED
	FROM OM_PF_PARAMETER_MASTER_TMP;

  -- DEFINE THE RECORD
  rec_parammaster c_parammaster%ROWTYPE;
  -- DEFINE VARIABLES
  v_sys_parameter_id OM_PARAMETER_MASTER.SYS_PARAMETER_ID%TYPE;
  v_deleted_ind OM_PF_PARAMETER_MASTER_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 0;
  v_blank VARCHAR2(1) := '';
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_parammaster;
  LOOP
    FETCH c_parammaster INTO rec_parammaster;
    EXIT WHEN c_parammaster%NOTFOUND;
    
    BEGIN
	
	v_active_cd := 0;
	
	-- Check Active Ind
	IF NVL(rec_parammaster.PF_ACTIVE_IND,'N') = 'Y' THEN
		v_active_cd := 1;
	END IF;
	
	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
	IF (rec_parammaster.PF_TRANSFER_IND IS NOT NULL AND upper(rec_parammaster.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
		v_active_cd := 0;
	END IF;

        -- CHECK IF RECORD EXISTS
        SELECT SYS_PARAMETER_ID INTO v_sys_parameter_id FROM OM_PARAMETER_MASTER WHERE parameter_name=rec_parammaster.PF_PARAMETER_NAME; 
        
        -- UPDATE parameter master
        UPDATE OM_PARAMETER_MASTER
        SET PARAMETER_TYPE    		= rec_parammaster.PF_PARAMETER_TYPE,
          	PARAMETER_NAME  	= rec_parammaster.PF_PARAMETER_NAME,
          	DESCRIPTION        	= rec_parammaster.PF_DESC,
          	PARAMETER_VALUE       	= rec_parammaster.PF_PARAMETER_VALUE,
          	ACTIVE_CD         	= v_active_cd,
          	CREATE_USER_ID		= rec_parammaster.ID_CREATED,
          	CREATE_DTTM		= rec_parammaster.DATE_TIME_CREATED,
          	UPDATE_USER_ID		= rec_parammaster.ID_MODIFIED,
          	UPDATE_DTTM		= rec_parammaster.DATE_TIME_MODIFIED
        WHERE SYS_PARAMETER_ID = v_sys_parameter_id; 
			
       EXCEPTION
        -- INSERT PARAMETER
        WHEN NO_DATA_FOUND THEN
		BEGIN
        INSERT
        INTO OM_PARAMETER_MASTER
          (
            SYS_PARAMETER_ID,
	    PARAMETER_TYPE,
	    PARAMETER_NAME,
	    DESCRIPTION,
	    PARAMETER_VALUE,
	    ACTIVE_CD,
	    CREATE_USER_ID,
	    CREATE_DTTM,
	    UPDATE_USER_ID,
	    UPDATE_DTTM
          )
          VALUES
          (rec_parammaster.PF_PARAMETER_ID,
          rec_parammaster.PF_PARAMETER_TYPE,
          rec_parammaster.PF_PARAMETER_NAME,
          rec_parammaster.PF_DESC,
          rec_parammaster.PF_PARAMETER_VALUE,
          v_active_cd,
          rec_parammaster.ID_CREATED,
	  rec_parammaster.DATE_TIME_CREATED,
	  rec_parammaster.ID_MODIFIED,
	  rec_parammaster.DATE_TIME_MODIFIED	
          );
        
	    EXCEPTION	
			WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				
			INSERT INTO OM_PF_PARAMETER_MASTER_BAD 
			( 
				PF_PARAMETER_ID,
				PF_PARAMETER_TYPE,
				PF_PARAMETER_NAME,
				PF_DESC,
				PF_PARAMETER_VALUE,
				PF_ACTIVE_IND,
				PF_DEACTIVATION_DTTM,
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
				rec_parammaster.PF_PARAMETER_ID 
				,rec_parammaster.PF_PARAMETER_TYPE 
				,rec_parammaster.PF_PARAMETER_NAME 
				,rec_parammaster.PF_DESC 
				,rec_parammaster.PF_PARAMETER_VALUE 
				,rec_parammaster.PF_ACTIVE_IND 
				,rec_parammaster.PF_DEACTIVATION_DTTM 
				,rec_parammaster.PF_TRANSFER_IND 
				,rec_parammaster.ID_CREATED 
				,rec_parammaster.DATE_TIME_CREATED 
				,rec_parammaster.ID_MODIFIED 
				,rec_parammaster.DATE_TIME_MODIFIED 
				,v_err_code 
				,v_err_msg 
				,SYSDATE
			) ;
		 v_counter := v_counter + 1;
        END;      
	    WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				
			INSERT INTO OM_PF_PARAMETER_MASTER_BAD 
			( 
				PF_PARAMETER_ID,
				PF_PARAMETER_TYPE,
				PF_PARAMETER_NAME,
				PF_DESC,
				PF_PARAMETER_VALUE,
				PF_ACTIVE_IND,
				PF_DEACTIVATION_DTTM,
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
				rec_parammaster.PF_PARAMETER_ID 
				,rec_parammaster.PF_PARAMETER_TYPE 
				,rec_parammaster.PF_PARAMETER_NAME 
				,rec_parammaster.PF_DESC 
				,rec_parammaster.PF_PARAMETER_VALUE 
				,rec_parammaster.PF_ACTIVE_IND 
				,rec_parammaster.PF_DEACTIVATION_DTTM 
				,rec_parammaster.PF_TRANSFER_IND 
				,rec_parammaster.ID_CREATED 
				,rec_parammaster.DATE_TIME_CREATED 
				,rec_parammaster.ID_MODIFIED 
				,rec_parammaster.DATE_TIME_MODIFIED 
				,v_err_code 
				,v_err_msg 
				,SYSDATE
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
