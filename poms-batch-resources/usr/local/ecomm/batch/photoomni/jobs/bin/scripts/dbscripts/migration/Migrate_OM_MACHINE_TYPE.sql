SET SERVEROUTPUT ON;
-- UPDATE / ADD MACHINES IN OM_MACHINE_TYPE
DECLARE
  CURSOR c_machinetype
  IS
   	SELECT 
		PF_MACHINE_TYPE_ID , 
		PF_MACHINE_TYPE , 
		trim(PF_MACHINE_DESC) PF_MACHINE_DESC , 
		PF_MACHINE_CAT , 
		PF_THERMAL_PRINTER_IND , 
		PF_SCHEDULE_PRIORITY , 
		PF_HOST_NAME , 
		PF_MINILAB_MACHINE_ID , 
		PF_MACHINE_OUTPUT_RATE , 
		PF_INTERNET_CAPABLE_IND , 
		PF_DISKETTE_CAPABLE_IND , 
		PF_CLICK_CNT_CAPABLE_IND , 
		PF_INTERFACED_IND , 
		PF_OUTPUT_APPLICABLE_IND , 
		PF_ACTIVE_IND , 
		PF_DEACTIVATION_DTTM, 
		PF_INV_NECESSARY_IND , 
		PF_TRANSFER_IND , 
		ID_CREATED , 
		DATE_TIME_CREATED,
		ID_MODIFIED , 
		DATE_TIME_MODIFIED,
		PF_WORKQUEUE_MACHINE_DESC , 
		PF_DFLT_MACH_COST_PRCNT , 
		PF_INTERFACE_DESCRIPTION , 
		PF_PAPER_SUPPORT_IND , 
		PF_MACHINE_SUB_CAT , 
		PF_CONTRL_MACHINE_TYPE_ID , 
		PF_NETWORK_TRNSMN_RATE , 
		PF_PROMISE_TIME_ELIGIBLE 
	FROM OM_PF_MACHINE_TYPE_TMP;

  -- DEFINE THE RECORD
  rec_machinetype c_machinetype%ROWTYPE;
  -- DEFINE VARIABLES
  v_sysmachinetypeid OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID%TYPE;
  v_deleted_ind OM_PF_MACHINE_TYPE_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 0;
  v_blank VARCHAR2(1) := '';
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_machinetype;
  LOOP
    FETCH c_machinetype INTO rec_machinetype;
    EXIT WHEN c_machinetype%NOTFOUND OR c_machinetype%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'MachineId=' || rec_machinetype.PF_MACHINE_TYPE_ID || ',MachineDesc=' || rec_machinetype.PF_MACHINE_DESC);
    BEGIN
	
	v_active_cd := 0;
	
	-- Check Active Ind
	IF NVL(rec_machinetype.PF_ACTIVE_IND,'N') = 'Y' THEN
		v_active_cd := 1;
	END IF;
	
	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
	IF (rec_machinetype.PF_TRANSFER_IND IS NOT NULL AND upper(rec_machinetype.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
		v_active_cd := 0;
	END IF;

        -- CHECK IF RECORD EXISTS
        SELECT SYS_MACHINE_TYPE_ID INTO v_sysmachinetypeid FROM OM_MACHINE_TYPE WHERE MACHINE_TYPE_NBR=rec_machinetype.PF_MACHINE_TYPE_ID; 
        
        -- UPDATE EQUIPMENT
        UPDATE OM_MACHINE_TYPE
        SET MACHINE_TYPE_NBR    = rec_machinetype.PF_MACHINE_TYPE_ID,
          	MACHINE_TYPE  		= rec_machinetype.PF_MACHINE_TYPE,
          	MACHINE_DESC        = rec_machinetype.PF_MACHINE_DESC,
          	GENERIC_NAME        = rec_machinetype.PF_MACHINE_DESC,
          	MACHINE_CAT         = rec_machinetype.PF_MACHINE_CAT,
          	MACHINE_SUB_CAT     = rec_machinetype.PF_MACHINE_SUB_CAT,
          	THERMAL_PRINTER_CD  = DECODE(UPPER(rec_machinetype.PF_THERMAL_PRINTER_IND),'Y',1,'N',0),
          	SCHEDULE_PRIORITY	= rec_machinetype.PF_SCHEDULE_PRIORITY,
          	OUTPUT_RATE			= rec_machinetype.PF_MACHINE_OUTPUT_RATE,
          	OUTPUT_RATE_UOM		= v_blank,
          	INTERNET_CAPABLE_CD	= DECODE(UPPER(rec_machinetype.PF_INTERNET_CAPABLE_IND),'Y',1,'N',0),
          	CLICK_CNT_CAPABLE_CD= DECODE(UPPER(rec_machinetype.PF_CLICK_CNT_CAPABLE_IND),'Y',1,'N',0),
          	INTERFACED_CD		= DECODE(UPPER(rec_machinetype.PF_INTERFACED_IND),'Y',1,'N',0),
          	OUTPUT_APPLICABLE_CD= DECODE(UPPER(rec_machinetype.PF_OUTPUT_APPLICABLE_IND),'Y',1,'N',0),
          	ACTIVE_CD			= v_active_cd,
          	INTERFACE_DESCRIPTION = rec_machinetype.PF_INTERFACE_DESCRIPTION,
          	DEACTIVATION_DTTM	= rec_machinetype.PF_DEACTIVATION_DTTM,
          	DFLT_MAC_COST_PERCNT= rec_machinetype.PF_DFLT_MACH_COST_PRCNT,
          	INV_NECESSARY_CD	= DECODE(UPPER(rec_machinetype.PF_INV_NECESSARY_IND),'Y',1,'N',0),
          	PAPER_SUPPORTED_CD	= DECODE(UPPER(rec_machinetype.PF_PAPER_SUPPORT_IND),'Y',1,'N',0),
          	NETWORK_TRNSMN_RATE	= rec_machinetype.PF_NETWORK_TRNSMN_RATE,
          	PROMISED_TIME_ELIGIBLE_CD = DECODE(UPPER(rec_machinetype.PF_PROMISE_TIME_ELIGIBLE),'Y',1,'N',0),
          	PRINT_FACTOR		= v_blank,
          	ROLL_FACTOR			= v_blank,
          	CREATE_USER_ID		= rec_machinetype.ID_CREATED,
          	CREATE_DTTM			= rec_machinetype.DATE_TIME_CREATED,
          	UPDATE_USER_ID		= rec_machinetype.ID_MODIFIED,
          	UPDATE_DTTM			= rec_machinetype.DATE_TIME_MODIFIED
        WHERE SYS_MACHINE_TYPE_ID = v_sysmachinetypeid; 
			
        --DBMS_OUTPUT.PUT_LINE ('Updated MACHINE_TYPE_NBR:' || rec_machinetype.PF_MACHINE_TYPE_ID);          
        EXCEPTION
        -- INSERT MACHINE
        WHEN NO_DATA_FOUND THEN
		BEGIN
        INSERT
        INTO OM_MACHINE_TYPE
          (
            SYS_MACHINE_TYPE_ID,
			MACHINE_TYPE_NBR,
			MACHINE_TYPE,
			MACHINE_DESC,
			GENERIC_NAME,
			MACHINE_CAT,
			MACHINE_SUB_CAT,
			THERMAL_PRINTER_CD,
			SCHEDULE_PRIORITY,
			OUTPUT_RATE,
			OUTPUT_RATE_UOM,
			INTERNET_CAPABLE_CD,
			CLICK_CNT_CAPABLE_CD,
			INTERFACED_CD,
			OUTPUT_APPLICABLE_CD,
			ACTIVE_CD,
			INTERFACE_DESCRIPTION,
			DEACTIVATION_DTTM,
			DFLT_MAC_COST_PERCNT,
			INV_NECESSARY_CD,
			PAPER_SUPPORTED_CD,
			NETWORK_TRNSMN_RATE,
			PROMISED_TIME_ELIGIBLE_CD,
			PRINT_FACTOR,
			ROLL_FACTOR,
			CREATE_USER_ID,
			CREATE_DTTM,
			UPDATE_USER_ID,
			UPDATE_DTTM
          )
          VALUES
          (
            OM_MACHINE_TYPE_SEQ.NEXTVAL,
            rec_machinetype.PF_MACHINE_TYPE_ID,
			rec_machinetype.PF_MACHINE_TYPE,
			rec_machinetype.PF_MACHINE_DESC,
			rec_machinetype.PF_MACHINE_DESC,
			rec_machinetype.PF_MACHINE_CAT,
			rec_machinetype.PF_MACHINE_SUB_CAT,
			DECODE(UPPER(rec_machinetype.PF_THERMAL_PRINTER_IND),'Y',1,'N',0) ,
			rec_machinetype.PF_SCHEDULE_PRIORITY,
			rec_machinetype.PF_MACHINE_OUTPUT_RATE,
			v_blank,
			DECODE(UPPER(rec_machinetype.PF_INTERNET_CAPABLE_IND),'Y',1,'N',0) ,
			DECODE(UPPER(rec_machinetype.PF_CLICK_CNT_CAPABLE_IND),'Y',1,'N',0) ,
			DECODE(UPPER(rec_machinetype.PF_INTERFACED_IND),'Y',1,'N',0) ,
			DECODE(UPPER(rec_machinetype.PF_OUTPUT_APPLICABLE_IND),'Y',1,'N',0) ,
			v_active_cd,
			rec_machinetype.PF_INTERFACE_DESCRIPTION,
			rec_machinetype.PF_DEACTIVATION_DTTM,
			rec_machinetype.PF_DFLT_MACH_COST_PRCNT,
			DECODE(UPPER(rec_machinetype.PF_INV_NECESSARY_IND),'Y',1,'N',0),
			DECODE(UPPER(rec_machinetype.PF_PAPER_SUPPORT_IND),'Y',1,'N',0),
			rec_machinetype.PF_NETWORK_TRNSMN_RATE,
			DECODE(UPPER(rec_machinetype.PF_PROMISE_TIME_ELIGIBLE),'Y',1,'N',0),
			v_blank,
			v_blank,
			rec_machinetype.ID_CREATED,
			rec_machinetype.DATE_TIME_CREATED,
			rec_machinetype.ID_MODIFIED,
			rec_machinetype.DATE_TIME_MODIFIED	
          );
        --DBMS_OUTPUT.PUT_LINE ('Added MACHINE_TYPE_NBR:' || rec_machinetype.PF_MACHINE_TYPE_ID);     

	    EXCEPTION	
			WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				
			INSERT INTO OM_PF_MACHINE_TYPE_BAD 
			( 
				PF_MACHINE_TYPE_ID 
				,PF_MACHINE_TYPE 
				,PF_MACHINE_DESC 
				,PF_MACHINE_CAT 
				,PF_THERMAL_PRINTER_IND 
				,PF_SCHEDULE_PRIORITY 
				,PF_HOST_NAME 
				,PF_MINILAB_MACHINE_ID 
				,PF_MACHINE_OUTPUT_RATE 
				,PF_INTERNET_CAPABLE_IND 
				,PF_DISKETTE_CAPABLE_IND 
				,PF_CLICK_CNT_CAPABLE_IND 
				,PF_INTERFACED_IND 
				,PF_OUTPUT_APPLICABLE_IND 
				,PF_ACTIVE_IND 
				,PF_DEACTIVATION_DTTM 
				,PF_INV_NECESSARY_IND 
				,PF_TRANSFER_IND 
				,ID_CREATED 
				,DATE_TIME_CREATED 
				,ID_MODIFIED 
				,DATE_TIME_MODIFIED 
				,PF_WORKQUEUE_MACHINE_DESC 
				,PF_DFLT_MACH_COST_PRCNT 
				,PF_INTERFACE_DESCRIPTION 
				,PF_PAPER_SUPPORT_IND 
				,PF_MACHINE_SUB_CAT 
				,PF_CONTRL_MACHINE_TYPE_ID 
				,PF_NETWORK_TRNSMN_RATE 
				,PF_PROMISE_TIME_ELIGIBLE 
				,EXCEPTION_CODE 
				,EXCEPTION_MSSG 
				,EXCEPTION_DTTM 
			) 
			VALUES  
			(  
				rec_machinetype.PF_MACHINE_TYPE_ID 
				,rec_machinetype.PF_MACHINE_TYPE 
				,rec_machinetype.PF_MACHINE_DESC 
				,rec_machinetype.PF_MACHINE_CAT 
				,rec_machinetype.PF_THERMAL_PRINTER_IND 
				,rec_machinetype.PF_SCHEDULE_PRIORITY 
				,rec_machinetype.PF_HOST_NAME 
				,rec_machinetype.PF_MINILAB_MACHINE_ID 
				,rec_machinetype.PF_MACHINE_OUTPUT_RATE 
				,rec_machinetype.PF_INTERNET_CAPABLE_IND 
				,rec_machinetype.PF_DISKETTE_CAPABLE_IND 
				,rec_machinetype.PF_CLICK_CNT_CAPABLE_IND 
				,rec_machinetype.PF_INTERFACED_IND 
				,rec_machinetype.PF_OUTPUT_APPLICABLE_IND 
				,rec_machinetype.PF_ACTIVE_IND 
				,rec_machinetype.PF_DEACTIVATION_DTTM 
				,rec_machinetype.PF_INV_NECESSARY_IND 
				,rec_machinetype.PF_TRANSFER_IND 
				,rec_machinetype.ID_CREATED 
				,rec_machinetype.DATE_TIME_CREATED 
				,rec_machinetype.ID_MODIFIED 
				,rec_machinetype.DATE_TIME_MODIFIED 
				,rec_machinetype.PF_WORKQUEUE_MACHINE_DESC 
				,rec_machinetype.PF_DFLT_MACH_COST_PRCNT 
				,rec_machinetype.PF_INTERFACE_DESCRIPTION 
				,rec_machinetype.PF_PAPER_SUPPORT_IND 
				,rec_machinetype.PF_MACHINE_SUB_CAT 
				,rec_machinetype.PF_CONTRL_MACHINE_TYPE_ID 
				,rec_machinetype.PF_NETWORK_TRNSMN_RATE 
				,rec_machinetype.PF_PROMISE_TIME_ELIGIBLE 
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
				
			INSERT INTO OM_PF_MACHINE_TYPE_BAD 
			( 
				PF_MACHINE_TYPE_ID 
				,PF_MACHINE_TYPE 
				,PF_MACHINE_DESC 
				,PF_MACHINE_CAT 
				,PF_THERMAL_PRINTER_IND 
				,PF_SCHEDULE_PRIORITY 
				,PF_HOST_NAME 
				,PF_MINILAB_MACHINE_ID 
				,PF_MACHINE_OUTPUT_RATE 
				,PF_INTERNET_CAPABLE_IND 
				,PF_DISKETTE_CAPABLE_IND 
				,PF_CLICK_CNT_CAPABLE_IND 
				,PF_INTERFACED_IND 
				,PF_OUTPUT_APPLICABLE_IND 
				,PF_ACTIVE_IND 
				,PF_DEACTIVATION_DTTM 
				,PF_INV_NECESSARY_IND 
				,PF_TRANSFER_IND 
				,ID_CREATED 
				,DATE_TIME_CREATED 
				,ID_MODIFIED 
				,DATE_TIME_MODIFIED 
				,PF_WORKQUEUE_MACHINE_DESC 
				,PF_DFLT_MACH_COST_PRCNT 
				,PF_INTERFACE_DESCRIPTION 
				,PF_PAPER_SUPPORT_IND 
				,PF_MACHINE_SUB_CAT 
				,PF_CONTRL_MACHINE_TYPE_ID 
				,PF_NETWORK_TRNSMN_RATE 
				,PF_PROMISE_TIME_ELIGIBLE 
				,EXCEPTION_CODE 
				,EXCEPTION_MSSG 
				,EXCEPTION_DTTM 
			) 
			VALUES  
			(  
				rec_machinetype.PF_MACHINE_TYPE_ID 
				,rec_machinetype.PF_MACHINE_TYPE 
				,rec_machinetype.PF_MACHINE_DESC 
				,rec_machinetype.PF_MACHINE_CAT 
				,rec_machinetype.PF_THERMAL_PRINTER_IND 
				,rec_machinetype.PF_SCHEDULE_PRIORITY 
				,rec_machinetype.PF_HOST_NAME 
				,rec_machinetype.PF_MINILAB_MACHINE_ID 
				,rec_machinetype.PF_MACHINE_OUTPUT_RATE 
				,rec_machinetype.PF_INTERNET_CAPABLE_IND 
				,rec_machinetype.PF_DISKETTE_CAPABLE_IND 
				,rec_machinetype.PF_CLICK_CNT_CAPABLE_IND 
				,rec_machinetype.PF_INTERFACED_IND 
				,rec_machinetype.PF_OUTPUT_APPLICABLE_IND 
				,rec_machinetype.PF_ACTIVE_IND 
				,rec_machinetype.PF_DEACTIVATION_DTTM 
				,rec_machinetype.PF_INV_NECESSARY_IND 
				,rec_machinetype.PF_TRANSFER_IND 
				,rec_machinetype.ID_CREATED 
				,rec_machinetype.DATE_TIME_CREATED 
				,rec_machinetype.ID_MODIFIED 
				,rec_machinetype.DATE_TIME_MODIFIED 
				,rec_machinetype.PF_WORKQUEUE_MACHINE_DESC 
				,rec_machinetype.PF_DFLT_MACH_COST_PRCNT 
				,rec_machinetype.PF_INTERFACE_DESCRIPTION 
				,rec_machinetype.PF_PAPER_SUPPORT_IND 
				,rec_machinetype.PF_MACHINE_SUB_CAT 
				,rec_machinetype.PF_CONTRL_MACHINE_TYPE_ID 
				,rec_machinetype.PF_NETWORK_TRNSMN_RATE 
				,rec_machinetype.PF_PROMISE_TIME_ELIGIBLE 
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
