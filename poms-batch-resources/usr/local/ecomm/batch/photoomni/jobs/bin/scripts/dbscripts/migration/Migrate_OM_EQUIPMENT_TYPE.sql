SET SERVEROUTPUT ON;
-- UPDATE / ADD EQUIPMENTS IN OM_EQUIPMENT_TYPE
DECLARE
  CURSOR c_equipment_type
  IS  
	SELECT PF_EQUIPMENT_ID , 
		PF_EQUIPMENT_DESC , 
		PF_TRANSFER_IND , 
		ID_CREATED , 
		DATE_TIME_CREATED , 
		ID_MODIFIED , 
		DATE_TIME_MODIFIED , 
		PF_ECOM_EQUIPMENT_DESC , 
		PF_INDEPENDENT_EQUIP_IND , 
		PF_COST_APPLICABLE_IND , 
		PF_EQUIPMENT_GROUP , 
		PF_TIMING_APPLICABLE_IND 
	FROM OM_PF_EQUIPMENT_TMP;
 
  -- DEFINE THE RECORD
  rec_equipment_type c_equipment_type%ROWTYPE;
  -- DEFINE VARIABLES
  v_sysequiptypeid OM_EQUIPMENT_TYPE.SYS_EQUIPMENT_TYPE_ID%TYPE;
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_deleted_ind OM_PF_EQUIPMENT_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
	OPEN c_equipment_type;
	LOOP
		FETCH c_equipment_type INTO rec_equipment_type;
		EXIT WHEN c_equipment_type%NOTFOUND OR c_equipment_type%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'EquipmentId=' || rec_equipment_type.PF_EQUIPMENT_ID || ',EquipmentDesc=' || rec_equipment_type.PF_EQUIPMENT_DESC);
		BEGIN
		
			v_active_cd := 1;

			-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
			IF (rec_equipment_type.PF_TRANSFER_IND IS NOT NULL AND upper(rec_equipment_type.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			END IF;
		
			-- CHECK IF RECORD EXISTS
			SELECT SYS_EQUIPMENT_TYPE_ID INTO v_sysequiptypeid FROM OM_EQUIPMENT_TYPE WHERE EQUIPMENT_TYPE_NBR=rec_equipment_type.PF_EQUIPMENT_ID;        
			
			-- UPDATE EQUIPMENT 
			
			UPDATE OM_EQUIPMENT_TYPE 
			SET EQUIPMENT_TYPE_NBR = rec_equipment_type.PF_EQUIPMENT_ID , 
				EQUIPMENT_TYPE_DESC = rec_equipment_type.PF_EQUIPMENT_DESC , 
				EXTERNAL_EQUIP_TYPE_DESC = rec_equipment_type.PF_EQUIPMENT_DESC , 
				COST_APPLICABLE_CD = DECODE(UPPER(rec_equipment_type.PF_COST_APPLICABLE_IND),'Y',1,'N',0) ,
				CREATE_USER_ID = rec_equipment_type.ID_CREATED,
				CREATE_DTTM = rec_equipment_type.DATE_TIME_CREATED,
				UPDATE_USER_ID = rec_equipment_type.ID_MODIFIED,
				UPDATE_DTTM = rec_equipment_type.DATE_TIME_MODIFIED,
				ACTIVE_CD = v_active_cd
			 WHERE SYS_EQUIPMENT_TYPE_ID = v_sysequiptypeid; 
			
			--DBMS_OUTPUT.PUT_LINE ('Updated EQUIPMENT_TYPE_NBR:' || rec_equipment_type.PF_EQUIPMENT_ID);          
		EXCEPTION
			-- INSERT EQUIPMENT
			WHEN NO_DATA_FOUND THEN
				BEGIN
				
					INSERT 
					INTO OM_EQUIPMENT_TYPE 
					(   
						SYS_EQUIPMENT_TYPE_ID , 
						EQUIPMENT_TYPE_NBR , 
						EQUIPMENT_TYPE_DESC , 
						EXTERNAL_EQUIP_TYPE_DESC , 
						COST_APPLICABLE_CD , 
						CREATE_USER_ID , 
						CREATE_DTTM , 
						UPDATE_USER_ID , 
						UPDATE_DTTM , 
						ACTIVE_CD
					) 
					VALUES 
					(
						OM_EQUIPMENT_TYPE_SEQ.NEXTVAL ,
						rec_equipment_type.PF_EQUIPMENT_ID , 
						rec_equipment_type.PF_EQUIPMENT_DESC , 
						rec_equipment_type.PF_EQUIPMENT_DESC , 
						DECODE(UPPER(rec_equipment_type.PF_COST_APPLICABLE_IND),'Y',1,'N',0) ,
						rec_equipment_type.ID_CREATED,
						rec_equipment_type.DATE_TIME_CREATED,
						rec_equipment_type.ID_MODIFIED,
						rec_equipment_type.DATE_TIME_MODIFIED,
						v_active_cd
					);

					--DBMS_OUTPUT.PUT_LINE ('Added EQUIPMENT_TYPE_NBR:' || rec_equipment_type.PF_EQUIPMENT_ID);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
						
					INSERT INTO OM_PF_EQUIPMENT_BAD ( 
						PF_EQUIPMENT_ID , 
						PF_EQUIPMENT_DESC , 
						PF_TRANSFER_IND , 
						ID_CREATED , 
						DATE_TIME_CREATED , 
						ID_MODIFIED , 
						DATE_TIME_MODIFIED , 
						PF_ECOM_EQUIPMENT_DESC , 
						PF_INDEPENDENT_EQUIP_IND , 
						PF_COST_APPLICABLE_IND , 
						PF_EQUIPMENT_GROUP , 
						PF_TIMING_APPLICABLE_IND,
						EXCEPTION_CODE,
						EXCEPTION_MSSG,
						EXCEPTION_DTTM
						) 
					VALUES  (  
						rec_equipment_type.PF_EQUIPMENT_ID , 
						rec_equipment_type.PF_EQUIPMENT_DESC , 
						rec_equipment_type.PF_TRANSFER_IND , 
						rec_equipment_type.ID_CREATED , 
						rec_equipment_type.DATE_TIME_CREATED , 
						rec_equipment_type.ID_MODIFIED , 
						rec_equipment_type.DATE_TIME_MODIFIED , 
						rec_equipment_type.PF_ECOM_EQUIPMENT_DESC , 
						rec_equipment_type.PF_INDEPENDENT_EQUIP_IND , 
						rec_equipment_type.PF_COST_APPLICABLE_IND , 
						rec_equipment_type.PF_EQUIPMENT_GROUP , 
						rec_equipment_type.PF_TIMING_APPLICABLE_IND ,
						v_err_code ,
						v_err_msg ,
						SYSDATE
					);
					v_counter := v_counter - 1;
				END;
			WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				
				INSERT INTO OM_PF_EQUIPMENT_BAD ( 
					PF_EQUIPMENT_ID , 
					PF_EQUIPMENT_DESC , 
					PF_TRANSFER_IND , 
					ID_CREATED , 
					DATE_TIME_CREATED , 
					ID_MODIFIED , 
					DATE_TIME_MODIFIED , 
					PF_ECOM_EQUIPMENT_DESC , 
					PF_INDEPENDENT_EQUIP_IND , 
					PF_COST_APPLICABLE_IND , 
					PF_EQUIPMENT_GROUP , 
					PF_TIMING_APPLICABLE_IND ,
					EXCEPTION_CODE ,
					EXCEPTION_MSSG ,
					EXCEPTION_DTTM
				) 
				VALUES  (  
					rec_equipment_type.PF_EQUIPMENT_ID , 
					rec_equipment_type.PF_EQUIPMENT_DESC , 
					rec_equipment_type.PF_TRANSFER_IND , 
					rec_equipment_type.ID_CREATED , 
					rec_equipment_type.DATE_TIME_CREATED , 
					rec_equipment_type.ID_MODIFIED , 
					rec_equipment_type.DATE_TIME_MODIFIED , 
					rec_equipment_type.PF_ECOM_EQUIPMENT_DESC , 
					rec_equipment_type.PF_INDEPENDENT_EQUIP_IND , 
					rec_equipment_type.PF_COST_APPLICABLE_IND , 
					rec_equipment_type.PF_EQUIPMENT_GROUP , 
					rec_equipment_type.PF_TIMING_APPLICABLE_IND ,
					v_err_code ,
					v_err_msg ,
					SYSDATE
				);	
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

EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
