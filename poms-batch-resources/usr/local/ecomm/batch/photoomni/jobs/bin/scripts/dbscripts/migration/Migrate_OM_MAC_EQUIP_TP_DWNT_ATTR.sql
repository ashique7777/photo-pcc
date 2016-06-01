SET SERVEROUTPUT ON;
-- UPDATE / ADD Downtime Reason IN OM_PF_DOWNTIME_REASON_TMP

DECLARE
  CURSOR c_mac_equip_downtime
  IS
   	SELECT OM_PF_DOWNTIME_REASON_TMP.PF_MACHINE_TYPE_ID ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_DOWNTIME_REASON_ID ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_DOWNTIME_DESC ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_TRANSFER_IND ,
	  OM_PF_DOWNTIME_REASON_TMP.ID_MODIFIED ,
	  OM_PF_DOWNTIME_REASON_TMP.DATE_TIME_MODIFIED ,
	  OM_PF_DOWNTIME_REASON_TMP.ID_CREATED ,
	  OM_PF_DOWNTIME_REASON_TMP.DATE_TIME_CREATED,
	  OM_PF_DOWNTIME_REASON_TMP.PF_EQUIPMENT_ID ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_TICKET_CATEGORY ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_DOWNTIME_REASON_URL ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_DOWNTIME_KPI_CAT_LVL1 ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_DOWNTIME_KPI_CAT_LVL2 ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_ACTIVE_IND ,
	  OM_PF_DOWNTIME_REASON_TMP.PF_DISPLAY_IND,
	  OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID,
	  NVL(OM_EQUIPMENT_TYPE.SYS_EQUIPMENT_TYPE_ID,0) SYS_EQUIPMENT_TYPE_ID,
	  Om_Downtime_Reason.Sys_Downtime_Reason_Id
	FROM Om_Pf_Downtime_Reason_Tmp
	JOIN Om_Downtime_Reason
	ON Trim(Om_Pf_Downtime_Reason_Tmp.Pf_Downtime_Desc) = Trim(Om_Downtime_Reason.Downtime_Reason)
	JOIN OM_MACHINE_TYPE
	ON Om_Pf_Downtime_Reason_Tmp.Pf_Machine_Type_Id = Om_Machine_Type.Machine_Type_Nbr
	LEFT OUTER JOIN OM_EQUIPMENT_TYPE
	ON OM_PF_DOWNTIME_REASON_TMP.PF_EQUIPMENT_ID = OM_EQUIPMENT_TYPE.EQUIPMENT_TYPE_NBR;

  -- DEFINE THE RECORD
  rec_mac_equip_downtime c_mac_equip_downtime%ROWTYPE;
  -- DEFINE VARIABLES
  v_mac_equip_downtime OM_MAC_EQUIP_TP_DWNT_ATTR.SYS_MAC_EQUIP_DWNT_ATTR_ID%TYPE:= 0;
  v_deleted_ind OM_PF_DOWNTIME_REASON_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 0;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_mac_equip_downtime;
  LOOP
    FETCH c_mac_equip_downtime INTO rec_mac_equip_downtime;
    EXIT WHEN c_mac_equip_downtime%NOTFOUND OR c_mac_equip_downtime%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Machine Number=' || rec_mac_equip_downtime.SYS_MACHINE_TYPE_ID || ',Equipment Number=' || rec_mac_equip_downtime.SYS_EQUIPMENT_TYPE_ID);
    BEGIN
        
	v_active_cd := 0;
	
	-- Check Active Ind
	IF NVL(rec_mac_equip_downtime.PF_ACTIVE_IND,'N') = 'Y' THEN
		v_active_cd := 1;
	END IF;
	
	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
	IF (rec_mac_equip_downtime.PF_TRANSFER_IND IS NOT NULL AND upper(rec_mac_equip_downtime.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
		v_active_cd := 0;
	END IF;
	
        -- CHECK IF RECORD EXISTS
        SELECT SYS_MAC_EQUIP_DWNT_ATTR_ID INTO v_mac_equip_downtime 
		FROM OM_MAC_EQUIP_TP_DWNT_ATTR 
		WHERE SYS_MACHINE_TYPE_ID = rec_mac_equip_downtime.SYS_MACHINE_TYPE_ID
		AND SYS_EQUIPMENT_TYPE_ID = rec_mac_equip_downtime.SYS_EQUIPMENT_TYPE_ID
		AND SYS_DOWNTIME_REASON_ID = rec_mac_equip_downtime.SYS_DOWNTIME_REASON_ID; 
		
		   
        -- UPDATE KIOSK DEVICES
		UPDATE OM_MAC_EQUIP_TP_DWNT_ATTR 
		SET SYS_MACHINE_TYPE_ID = rec_mac_equip_downtime.SYS_MACHINE_TYPE_ID,
			SYS_EQUIPMENT_TYPE_ID = rec_mac_equip_downtime.SYS_EQUIPMENT_TYPE_ID,
			SYS_DOWNTIME_REASON_ID = rec_mac_equip_downtime.SYS_DOWNTIME_REASON_ID,
			DOWNTIME_URL = rec_mac_equip_downtime.PF_DOWNTIME_REASON_URL,
			DISPLAY_CD = DECODE(UPPER(rec_mac_equip_downtime.PF_DISPLAY_IND),'Y',1,'N',0) , 
			ACTIVE_CD = v_active_cd , 
			CREATE_USER_ID = rec_mac_equip_downtime.ID_CREATED,
			CREATE_DTTM = rec_mac_equip_downtime.DATE_TIME_CREATED,
			UPDATE_USER_ID = rec_mac_equip_downtime.ID_MODIFIED,
			UPDATE_DTTM = rec_mac_equip_downtime.DATE_TIME_MODIFIED
		WHERE SYS_MAC_EQUIP_DWNT_ATTR_ID = v_mac_equip_downtime;

		--DBMS_OUTPUT.PUT_LINE ('Updated Machine Number=' || rec_mac_equip_downtime.SYS_MACHINE_TYPE_ID || ',Equipment Number=' || rec_mac_equip_downtime.SYS_EQUIPMENT_TYPE_ID);       
        EXCEPTION
        -- INSERT MACHINE
        WHEN NO_DATA_FOUND THEN
		BEGIN
        
		INSERT INTO OM_MAC_EQUIP_TP_DWNT_ATTR 
		( 
			SYS_MAC_EQUIP_DWNT_ATTR_ID,
			SYS_MACHINE_TYPE_ID, 
			SYS_EQUIPMENT_TYPE_ID, 
			SYS_DOWNTIME_REASON_ID,
			DOWNTIME_URL ,
			DISPLAY_CD, 
			ACTIVE_CD ,
			CREATE_USER_ID ,
			CREATE_DTTM ,
			UPDATE_USER_ID ,
			UPDATE_DTTM 
		 ) 
		 VALUES 
		 (
			 OM_MAC_EQUIP_TP_DWNT_ATTR_SEQ.NEXTVAL,
			 rec_mac_equip_downtime.SYS_MACHINE_TYPE_ID,
			 rec_mac_equip_downtime.SYS_EQUIPMENT_TYPE_ID,
			 rec_mac_equip_downtime.SYS_DOWNTIME_REASON_ID,
			 rec_mac_equip_downtime.PF_DOWNTIME_REASON_URL,
			 DECODE(UPPER(rec_mac_equip_downtime.PF_DISPLAY_IND),'Y',1,'N',0) , 
			 v_active_cd , 
			 rec_mac_equip_downtime.ID_CREATED,
			 rec_mac_equip_downtime.DATE_TIME_CREATED,
			 rec_mac_equip_downtime.ID_MODIFIED,
			 rec_mac_equip_downtime.DATE_TIME_MODIFIED
		  );

        --DBMS_OUTPUT.PUT_LINE ('Updated Machine Number=' || rec_mac_equip_downtime.SYS_MACHINE_TYPE_ID || ',Equipment Number=' || rec_mac_equip_downtime.SYS_EQUIPMENT_TYPE_ID);       
	    EXCEPTION	
			WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
			
			INSERT INTO OM_PF_DOWNTIME_REASON_BAD ( 
				PF_MACHINE_TYPE_ID ,
				 PF_DOWNTIME_REASON_ID ,
				 PF_DOWNTIME_DESC ,
				 PF_TRANSFER_IND ,
				 ID_MODIFIED ,
				 DATE_TIME_MODIFIED ,
				 ID_CREATED ,
				 DATE_TIME_CREATED ,
				 PF_EQUIPMENT_ID ,
				 PF_TICKET_CATEGORY ,
				 PF_DOWNTIME_REASON_URL ,
				 PF_DOWNTIME_KPI_CAT_LVL1 ,
				 PF_DOWNTIME_KPI_CAT_LVL2 ,
				 PF_ACTIVE_IND ,
				 PF_DISPLAY_IND ,
				 EXCEPTION_CODE ,
				 EXCEPTION_MSSG ,
				 EXCEPTION_DTTM 
				 ) 
			VALUES  
				 (  
				 rec_mac_equip_downtime.PF_MACHINE_TYPE_ID ,
				 rec_mac_equip_downtime.PF_DOWNTIME_REASON_ID ,
				 rec_mac_equip_downtime.PF_DOWNTIME_DESC ,
				 rec_mac_equip_downtime.PF_TRANSFER_IND ,
				 rec_mac_equip_downtime.ID_MODIFIED ,
				 rec_mac_equip_downtime.DATE_TIME_MODIFIED ,
				 rec_mac_equip_downtime.ID_CREATED ,
				 rec_mac_equip_downtime.DATE_TIME_CREATED ,
				 rec_mac_equip_downtime.PF_EQUIPMENT_ID ,
				 rec_mac_equip_downtime.PF_TICKET_CATEGORY ,
				 rec_mac_equip_downtime.PF_DOWNTIME_REASON_URL ,
				 rec_mac_equip_downtime.PF_DOWNTIME_KPI_CAT_LVL1 ,
				 rec_mac_equip_downtime.PF_DOWNTIME_KPI_CAT_LVL2 ,
				 rec_mac_equip_downtime.PF_ACTIVE_IND ,
				 rec_mac_equip_downtime.PF_DISPLAY_IND ,
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
			INSERT INTO OM_PF_DOWNTIME_REASON_BAD ( 
				PF_MACHINE_TYPE_ID ,
				 PF_DOWNTIME_REASON_ID ,
				 PF_DOWNTIME_DESC ,
				 PF_TRANSFER_IND ,
				 ID_MODIFIED ,
				 DATE_TIME_MODIFIED ,
				 ID_CREATED ,
				 DATE_TIME_CREATED ,
				 PF_EQUIPMENT_ID ,
				 PF_TICKET_CATEGORY ,
				 PF_DOWNTIME_REASON_URL ,
				 PF_DOWNTIME_KPI_CAT_LVL1 ,
				 PF_DOWNTIME_KPI_CAT_LVL2 ,
				 PF_ACTIVE_IND ,
				 PF_DISPLAY_IND ,
				 EXCEPTION_CODE ,
				 EXCEPTION_MSSG ,
				 EXCEPTION_DTTM 
				 ) 
			VALUES  
				 (  
				 rec_mac_equip_downtime.PF_MACHINE_TYPE_ID ,
				 rec_mac_equip_downtime.PF_DOWNTIME_REASON_ID ,
				 rec_mac_equip_downtime.PF_DOWNTIME_DESC ,
				 rec_mac_equip_downtime.PF_TRANSFER_IND ,
				 rec_mac_equip_downtime.ID_MODIFIED ,
				 rec_mac_equip_downtime.DATE_TIME_MODIFIED ,
				 rec_mac_equip_downtime.ID_CREATED ,
				 rec_mac_equip_downtime.DATE_TIME_CREATED ,
				 rec_mac_equip_downtime.PF_EQUIPMENT_ID ,
				 rec_mac_equip_downtime.PF_TICKET_CATEGORY ,
				 rec_mac_equip_downtime.PF_DOWNTIME_REASON_URL ,
				 rec_mac_equip_downtime.PF_DOWNTIME_KPI_CAT_LVL1 ,
				 rec_mac_equip_downtime.PF_DOWNTIME_KPI_CAT_LVL2 ,
				 rec_mac_equip_downtime.PF_ACTIVE_IND ,
				 rec_mac_equip_downtime.PF_DISPLAY_IND ,
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
  INSERT INTO OM_PF_DOWNTIME_REASON_BAD ( 
				PF_MACHINE_TYPE_ID ,
				 PF_DOWNTIME_REASON_ID ,
				 PF_DOWNTIME_DESC ,
				 PF_TRANSFER_IND ,
				 ID_MODIFIED ,
				 DATE_TIME_MODIFIED ,
				 ID_CREATED ,
				 DATE_TIME_CREATED ,
				 PF_EQUIPMENT_ID ,
				 PF_TICKET_CATEGORY ,
				 PF_DOWNTIME_REASON_URL ,
				 PF_DOWNTIME_KPI_CAT_LVL1 ,
				 PF_DOWNTIME_KPI_CAT_LVL2 ,
				 PF_ACTIVE_IND ,
				 PF_DISPLAY_IND ,
				 EXCEPTION_CODE ,
				 EXCEPTION_MSSG ,
				 EXCEPTION_DTTM 
				 ) 
			 SELECT 	 
			 "REASON".PF_MACHINE_TYPE_ID ,
			 "REASON".PF_DOWNTIME_REASON_ID ,
			 "REASON".PF_DOWNTIME_DESC ,
			 "REASON".PF_TRANSFER_IND ,
			 "REASON".ID_MODIFIED ,
			 "REASON".DATE_TIME_MODIFIED ,
			 "REASON".ID_CREATED ,
			 "REASON".DATE_TIME_CREATED,
			 "REASON".PF_EQUIPMENT_ID ,
			 "REASON".PF_TICKET_CATEGORY ,
			 "REASON".PF_DOWNTIME_REASON_URL ,
			 "REASON".PF_DOWNTIME_KPI_CAT_LVL1 ,
			 "REASON".PF_DOWNTIME_KPI_CAT_LVL2 ,
			 "REASON".PF_ACTIVE_IND ,
			 "REASON".PF_DISPLAY_IND ,
			 '000' ,
			 'Master records not found' ,
			 SYSDATE
			 FROM OM_PF_DOWNTIME_REASON_TMP "REASON"
			 WHERE NOT EXISTS
				( SELECT 1			
				  FROM OM_PF_DOWNTIME_REASON_TMP, OM_DOWNTIME_REASON, OM_MACHINE_TYPE, OM_EQUIPMENT_TYPE
				  WHERE trim(OM_PF_DOWNTIME_REASON_TMP.PF_DOWNTIME_DESC) = trim(OM_DOWNTIME_REASON.DOWNTIME_REASON)
				  AND OM_PF_DOWNTIME_REASON_TMP.PF_MACHINE_TYPE_ID = OM_MACHINE_TYPE.MACHINE_TYPE_NBR
				  AND OM_PF_DOWNTIME_REASON_TMP.PF_EQUIPMENT_ID = OM_EQUIPMENT_TYPE.EQUIPMENT_TYPE_NBR
				  AND OM_PF_DOWNTIME_REASON_TMP.PF_MACHINE_TYPE_ID = "REASON".PF_MACHINE_TYPE_ID
				  AND OM_PF_DOWNTIME_REASON_TMP.PF_EQUIPMENT_ID = "REASON".PF_EQUIPMENT_ID
				  AND trim(OM_PF_DOWNTIME_REASON_TMP.PF_DOWNTIME_DESC) = trim("REASON".PF_DOWNTIME_DESC)
				);
END;
/
