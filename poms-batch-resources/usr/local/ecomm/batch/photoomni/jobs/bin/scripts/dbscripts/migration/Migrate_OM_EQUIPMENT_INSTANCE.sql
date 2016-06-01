SET SERVEROUTPUT ON;
-- UPDATE / ADD EQUIPMENTS IN OM_EQUIPMENT_INSTANCE
DECLARE
  CURSOR c_equipment_instance
  IS  
	SELECT OM_PF_STORE_MACHINE_EQUIP_TMP.PF_MACHINE_ID ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.PF_EQUIPMENT_ID ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.PF_TRANSFER_IND ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.ID_CREATED ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.DATE_TIME_CREATED ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.ID_MODIFIED ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.DATE_TIME_MODIFIED ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.PF_CENTRAL_ACTIVATED_IND ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.PF_STORE_ACTIVATED_IND ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.PF_ACTUAL_START_DTTM ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.PF_DEACTIVATION_DTTM ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.PF_TOTAL_INSTANCE_COUNT ,
	  OM_PF_STORE_MACHINE_EQUIP_TMP.PF_AVAILABLE_INSTANCE_COUNT ,
	  OM_MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID ,
	  OM_EQUIPMENT_TYPE.SYS_EQUIPMENT_TYPE_ID ,
	  OM_EQUIPMENT_TYPE.EQUIPMENT_TYPE_DESC	  
	FROM OM_PF_STORE_MACHINE_EQUIP_TMP, OM_MACHINE_INSTANCE, OM_EQUIPMENT_TYPE
	WHERE OM_PF_STORE_MACHINE_EQUIP_TMP.PF_MACHINE_ID = TO_NUMBER(OM_MACHINE_INSTANCE.MACHINE_NAME)
	AND OM_PF_STORE_MACHINE_EQUIP_TMP.PF_EQUIPMENT_ID = OM_EQUIPMENT_TYPE.EQUIPMENT_TYPE_NBR ;
  
  -- DEFINE THE RECORD
  rec_equipment_instance c_equipment_instance%ROWTYPE;
  -- DEFINE VARIABLES
  v_sys_equip_instance_id OM_EQUIPMENT_INSTANCE.SYS_EQUIPMENT_INSTANCE_ID%TYPE;
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_pf_central_active_ind VARCHAR2(1) := 'N';
  v_pf_store_active_ind VARCHAR2(1) := 'N';  
  v_active_cd NUMBER := 1;
  v_deleted_ind OM_PF_STORE_MACHINE_EQUIP_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_status_cd VARCHAR2(5);
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
	OPEN c_equipment_instance;
	LOOP
		FETCH c_equipment_instance INTO rec_equipment_instance;
		EXIT WHEN c_equipment_instance%NOTFOUND OR c_equipment_instance%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'machine id=' || rec_equipment_instance.PF_MACHINE_ID || '  and equipment id=' ||  rec_equipment_instance.PF_EQUIPMENT_ID);
		BEGIN
		
			-- IF PF_TRANSFER_IND = 'D' THEN ACTIVE_CD WILL BE 0 ELSE 1
			v_active_cd := 1;
			IF (rec_equipment_instance.PF_TRANSFER_IND IS NOT NULL AND upper(rec_equipment_instance.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			ELSE 
				v_active_cd := 1;
			END IF;
		
		
			IF (rec_equipment_instance.PF_CENTRAL_ACTIVATED_IND IS NOT NULL) THEN        		
				v_pf_central_active_ind := rec_equipment_instance.PF_CENTRAL_ACTIVATED_IND;	
			ELSE
				v_pf_central_active_ind := 'N';
			END IF;
			
			IF (rec_equipment_instance.PF_STORE_ACTIVATED_IND IS NOT NULL) THEN        		
				v_pf_store_active_ind := rec_equipment_instance.PF_STORE_ACTIVATED_IND;		
			ELSE
				v_pf_store_active_ind := 'N';			
			END IF;			
			
		
			IF (upper(v_pf_central_active_ind)='Y' AND upper(v_pf_store_active_ind)='Y') THEN 
				v_status_cd := 'A';
			ELSIF (upper(v_pf_central_active_ind)='Y' AND upper(v_pf_store_active_ind)='N') THEN 
				v_status_cd := 'C';
			ELSIF (upper(v_pf_central_active_ind)='N' AND upper(v_pf_store_active_ind)='Y') THEN 
				v_status_cd := 'S';
			ELSIF (upper(v_pf_central_active_ind)='N' AND upper(v_pf_store_active_ind)='N') THEN 
				v_status_cd := 'D';	
			END IF;			

			
			-- CHECK IF RECORD EXISTS
			SELECT SYS_EQUIPMENT_INSTANCE_ID INTO v_sys_equip_instance_id FROM OM_EQUIPMENT_INSTANCE WHERE SYS_MACHINE_INSTANCE_ID = rec_equipment_instance.SYS_MACHINE_INSTANCE_ID AND SYS_EQUIPMENT_TYPE_ID = rec_equipment_instance.SYS_EQUIPMENT_TYPE_ID;        
			
			--DBMS_OUTPUT.PUT_LINE ('Updating:' || v_sys_equip_instance_id);    
			
			-- UPDATE EQUIPMENT INSTANCE 	
			UPDATE OM_EQUIPMENT_INSTANCE
			SET SYS_MACHINE_INSTANCE_ID   = rec_equipment_instance.SYS_MACHINE_INSTANCE_ID ,
			  SYS_EQUIPMENT_TYPE_ID       = rec_equipment_instance.SYS_EQUIPMENT_TYPE_ID ,
			  EQUIPMENT_NBR              = rec_equipment_instance.EQUIPMENT_TYPE_DESC ,
			  STATUS_CD                   = v_status_cd ,
			  ESTIMATED_ACTIVATION_DTTM   = rec_equipment_instance.PF_ACTUAL_START_DTTM ,
			  ACTUAL_ACTIVATION_DTTM      = rec_equipment_instance.PF_ACTUAL_START_DTTM ,
			  ESTIMATED_DEACTIVATION_DTTM = rec_equipment_instance.PF_DEACTIVATION_DTTM ,
			  ACTUAL_DEACTIVATION_DTTM    = rec_equipment_instance.PF_DEACTIVATION_DTTM ,
			  CREATE_USER_ID              = rec_equipment_instance.ID_CREATED ,
			  CREATE_DTTM                 = rec_equipment_instance.DATE_TIME_CREATED ,
			  UPDATE_USER_ID              = rec_equipment_instance.ID_MODIFIED ,
			  UPDATE_DTTM                 = rec_equipment_instance.DATE_TIME_MODIFIED ,
			  ACTIVE_CD                   = v_active_cd 
			WHERE SYS_EQUIPMENT_INSTANCE_ID = v_sys_equip_instance_id;

			--DBMS_OUTPUT.PUT_LINE ('Updated SYS_EQUIPMENT_INSTANCE_ID:' || v_sys_equip_instance_id);          
		EXCEPTION
			-- INSERT MACHINE INSTANCE
			WHEN NO_DATA_FOUND THEN
				BEGIN
				
					INSERT
					INTO OM_EQUIPMENT_INSTANCE
					  (
						SYS_EQUIPMENT_INSTANCE_ID ,
						SYS_MACHINE_INSTANCE_ID ,
						SYS_EQUIPMENT_TYPE_ID ,
						EQUIPMENT_NBR ,
						STATUS_CD ,
						ESTIMATED_ACTIVATION_DTTM ,
						ACTUAL_ACTIVATION_DTTM ,
						ESTIMATED_DEACTIVATION_DTTM ,
						ACTUAL_DEACTIVATION_DTTM ,
						CREATE_USER_ID ,
						CREATE_DTTM ,
						UPDATE_USER_ID ,
						UPDATE_DTTM ,
						ACTIVE_CD
					  )
					  VALUES
					  (
						  OM_EQUIPMENT_INSTANCE_SEQ.NEXTVAL ,
						  rec_equipment_instance.SYS_MACHINE_INSTANCE_ID ,
						  rec_equipment_instance.SYS_EQUIPMENT_TYPE_ID ,
						  rec_equipment_instance.EQUIPMENT_TYPE_DESC ,
						  v_status_cd ,
						  rec_equipment_instance.PF_ACTUAL_START_DTTM ,
						  rec_equipment_instance.PF_ACTUAL_START_DTTM ,
						  rec_equipment_instance.PF_DEACTIVATION_DTTM ,
						  rec_equipment_instance.PF_DEACTIVATION_DTTM ,
						  rec_equipment_instance.ID_CREATED ,
						  rec_equipment_instance.DATE_TIME_CREATED ,
						  rec_equipment_instance.ID_MODIFIED ,
						  rec_equipment_instance.DATE_TIME_MODIFIED ,
						  v_active_cd 										
					  );


					--DBMS_OUTPUT.PUT_LINE ('Added for MACHINE_INSTANCE_ID:' || rec_equipment_instance.SYS_MACHINE_INSTANCE_ID || '  and SYS_EQUIPMENT_TYPE_ID:' || rec_equipment_instance.SYS_EQUIPMENT_TYPE_ID);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
					
					INSERT
					INTO OM_PF_STORE_MACHINE_EQUIP_BAD
					  (
						PF_MACHINE_ID ,
						PF_EQUIPMENT_ID ,
						PF_TRANSFER_IND ,
						ID_CREATED ,
						DATE_TIME_CREATED ,
						ID_MODIFIED ,
						DATE_TIME_MODIFIED ,
						PF_CENTRAL_ACTIVATED_IND ,
						PF_STORE_ACTIVATED_IND ,
						PF_ACTUAL_START_DTTM ,
						PF_DEACTIVATION_DTTM ,
						PF_TOTAL_INSTANCE_COUNT ,
						PF_AVAILABLE_INSTANCE_COUNT ,
						EXCEPTION_CODE ,
						EXCEPTION_MSSG ,
						EXCEPTION_DTTM
					  )
					  VALUES
					  (
						rec_equipment_instance.PF_MACHINE_ID ,
						rec_equipment_instance.PF_EQUIPMENT_ID ,
						rec_equipment_instance.PF_TRANSFER_IND ,
						rec_equipment_instance.ID_CREATED ,
						rec_equipment_instance.DATE_TIME_CREATED ,
						rec_equipment_instance.ID_MODIFIED ,
						rec_equipment_instance.DATE_TIME_MODIFIED ,
						rec_equipment_instance.PF_CENTRAL_ACTIVATED_IND ,
						rec_equipment_instance.PF_STORE_ACTIVATED_IND ,
						rec_equipment_instance.PF_ACTUAL_START_DTTM ,
						rec_equipment_instance.PF_DEACTIVATION_DTTM ,
						rec_equipment_instance.PF_TOTAL_INSTANCE_COUNT ,
						rec_equipment_instance.PF_AVAILABLE_INSTANCE_COUNT ,
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
				INTO OM_PF_STORE_MACHINE_EQUIP_BAD
				  (
					PF_MACHINE_ID ,
					PF_EQUIPMENT_ID ,
					PF_TRANSFER_IND ,
					ID_CREATED ,
					DATE_TIME_CREATED ,
					ID_MODIFIED ,
					DATE_TIME_MODIFIED ,
					PF_CENTRAL_ACTIVATED_IND ,
					PF_STORE_ACTIVATED_IND ,
					PF_ACTUAL_START_DTTM ,
					PF_DEACTIVATION_DTTM ,
					PF_TOTAL_INSTANCE_COUNT ,
					PF_AVAILABLE_INSTANCE_COUNT ,
					EXCEPTION_CODE ,
					EXCEPTION_MSSG ,
					EXCEPTION_DTTM
				  )
				  VALUES
				  (
					rec_equipment_instance.PF_MACHINE_ID ,
					rec_equipment_instance.PF_EQUIPMENT_ID ,
					rec_equipment_instance.PF_TRANSFER_IND ,
					rec_equipment_instance.ID_CREATED ,
					rec_equipment_instance.DATE_TIME_CREATED ,
					rec_equipment_instance.ID_MODIFIED ,
					rec_equipment_instance.DATE_TIME_MODIFIED ,
					rec_equipment_instance.PF_CENTRAL_ACTIVATED_IND ,
					rec_equipment_instance.PF_STORE_ACTIVATED_IND ,
					rec_equipment_instance.PF_ACTUAL_START_DTTM ,
					rec_equipment_instance.PF_DEACTIVATION_DTTM ,
					rec_equipment_instance.PF_TOTAL_INSTANCE_COUNT ,
					rec_equipment_instance.PF_AVAILABLE_INSTANCE_COUNT ,
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
	INTO OM_PF_STORE_MACHINE_EQUIP_BAD
	  (
		PF_MACHINE_ID ,
		PF_EQUIPMENT_ID ,
		PF_TRANSFER_IND ,
		ID_CREATED ,
		DATE_TIME_CREATED ,
		ID_MODIFIED ,
		DATE_TIME_MODIFIED ,
		PF_CENTRAL_ACTIVATED_IND ,
		PF_STORE_ACTIVATED_IND ,
		PF_ACTUAL_START_DTTM ,
		PF_DEACTIVATION_DTTM ,
		PF_TOTAL_INSTANCE_COUNT ,
		PF_AVAILABLE_INSTANCE_COUNT ,
		EXCEPTION_CODE ,
		EXCEPTION_MSSG ,
		EXCEPTION_DTTM
	  )
	SELECT "EQUIP".PF_MACHINE_ID ,
	  "EQUIP".PF_EQUIPMENT_ID ,
	  "EQUIP".PF_TRANSFER_IND ,
	  "EQUIP".ID_CREATED ,
	  "EQUIP".DATE_TIME_CREATED ,
	  "EQUIP".ID_MODIFIED ,
	  "EQUIP".DATE_TIME_MODIFIED ,
	  "EQUIP".PF_CENTRAL_ACTIVATED_IND ,
	  "EQUIP".PF_STORE_ACTIVATED_IND ,
	  "EQUIP".PF_ACTUAL_START_DTTM ,
	  "EQUIP".PF_DEACTIVATION_DTTM ,
	  "EQUIP".PF_TOTAL_INSTANCE_COUNT ,
	  "EQUIP".PF_AVAILABLE_INSTANCE_COUNT,
	  '000' , 
	  'Master records not found' ,
	  SYSDATE
	FROM OM_PF_STORE_MACHINE_EQUIP_TMP "EQUIP"
	WHERE NOT EXISTS
	  (SELECT 1
		FROM OM_PF_STORE_MACHINE_EQUIP_TMP, OM_MACHINE_INSTANCE, OM_EQUIPMENT_TYPE
		WHERE OM_PF_STORE_MACHINE_EQUIP_TMP.PF_MACHINE_ID = TO_NUMBER(OM_MACHINE_INSTANCE.MACHINE_NAME)
		AND OM_PF_STORE_MACHINE_EQUIP_TMP.PF_EQUIPMENT_ID = OM_EQUIPMENT_TYPE.EQUIPMENT_TYPE_NBR 
		AND OM_PF_STORE_MACHINE_EQUIP_TMP.PF_MACHINE_ID = "EQUIP".PF_MACHINE_ID
		AND OM_PF_STORE_MACHINE_EQUIP_TMP.PF_EQUIPMENT_ID = "EQUIP".PF_EQUIPMENT_ID
	 );
	 
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
