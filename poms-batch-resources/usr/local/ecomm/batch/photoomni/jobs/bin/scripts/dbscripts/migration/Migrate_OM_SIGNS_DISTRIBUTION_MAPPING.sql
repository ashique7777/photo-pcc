SET SERVEROUTPUT ON;
-- UPDATE / ADD SIGNS Distribution MAPPING IN OM_SIGNS_DISTRIBUTION_MAPPING
DECLARE
  CURSOR c_signs_dist_map
  IS
 
	SELECT OM_PF_EVENT_DIST_MAPPING_TMP.PF_DISTRIBUTION_ID ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.PF_EVENT_ID ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.PF_DISTRIBUTION_NAME ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.PF_PRINT_START_DATE ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.ID_CREATED ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.DATE_TIME_CREATED ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.ID_MODIFIED ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.DATE_TIME_MODIFIED ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.PF_CRITERIA_TYPE ,
		 OM_PF_EVENT_DIST_MAPPING_TMP.PF_FORCE_PUSH_IND,
		 OM_SIGNS_HEADER.SYS_EVENT_ID
	FROM OM_PF_EVENT_DIST_MAPPING_TMP,  OM_SIGNS_HEADER
	WHERE OM_PF_EVENT_DIST_MAPPING_TMP.PF_EVENT_ID = OM_SIGNS_HEADER.SYS_EVENT_ID; 

 
  -- DEFINE THE RECORD
  rec_signs_dist_map c_signs_dist_map%ROWTYPE;
  -- DEFINE VARIABLES
  v_sys_dist_id OM_SIGNS_DISTRIBUTION_MAPPING.SYS_DISTRIBUTION_ID%TYPE;
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';

BEGIN
	OPEN c_signs_dist_map;
	LOOP
		FETCH c_signs_dist_map INTO rec_signs_dist_map;
		EXIT WHEN c_signs_dist_map%NOTFOUND OR c_signs_dist_map%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Distribution=' || rec_signs_dist_map.PF_DISTRIBUTION_ID );
		BEGIN
					
			-- CHECK IF RECORD EXISTS
			SELECT SYS_DISTRIBUTION_ID INTO v_sys_dist_id FROM OM_SIGNS_DISTRIBUTION_MAPPING 
			WHERE SYS_DISTRIBUTION_ID=rec_signs_dist_map.PF_DISTRIBUTION_ID ;        
			-- UPDATE SIGNS_DISTRIBUTION 
			
			UPDATE OM_SIGNS_DISTRIBUTION_MAPPING 
			SET  DISTRIBUTION_NAME = rec_signs_dist_map.PF_DISTRIBUTION_NAME,
				 PRINT_START_DATE = rec_signs_dist_map.PF_PRINT_START_DATE,
				 CRITERIA_TYPE = rec_signs_dist_map.PF_CRITERIA_TYPE,
				 FORCE_PUSH_CD = DECODE(UPPER(rec_signs_dist_map.PF_FORCE_PUSH_IND),'Y',1,'N',0,0),
				 ACTIVE_CD = v_active_cd,
				 CREATE_USER_ID = rec_signs_dist_map.ID_CREATED,
				 CREATE_DTTM = rec_signs_dist_map.DATE_TIME_CREATED,
				 UPDATE_USER_ID = rec_signs_dist_map.ID_MODIFIED,
				 UPDATE_DTTM = rec_signs_dist_map.DATE_TIME_MODIFIED 
			WHERE SYS_DISTRIBUTION_ID = v_sys_dist_id ;
			
			--DBMS_OUTPUT.PUT_LINE ('Updated Distribution:' || rec_signs_dist_map.PF_DISTRIBUTION_ID);          
		EXCEPTION
			-- INSERT SIGNS_DISTRIBUTION
			WHEN NO_DATA_FOUND THEN
				BEGIN
				
					INSERT INTO OM_SIGNS_DISTRIBUTION_MAPPING 
					( 
						SYS_DISTRIBUTION_ID ,
						SYS_EVENT_ID ,
						DISTRIBUTION_NAME ,
						PRINT_START_DATE ,
						CRITERIA_TYPE ,
						FORCE_PUSH_CD ,
						ACTIVE_CD ,
						CREATE_USER_ID ,
						CREATE_DTTM ,
						UPDATE_USER_ID ,
						UPDATE_DTTM
					) 
					VALUES 
					(
						rec_signs_dist_map.PF_DISTRIBUTION_ID,
						rec_signs_dist_map.PF_EVENT_ID,
						rec_signs_dist_map.PF_DISTRIBUTION_NAME,
						rec_signs_dist_map.PF_PRINT_START_DATE,
						rec_signs_dist_map.PF_CRITERIA_TYPE,
						DECODE(UPPER(rec_signs_dist_map.PF_FORCE_PUSH_IND),'Y',1,'N',0,0),
						v_active_cd,
						rec_signs_dist_map.ID_CREATED,
						rec_signs_dist_map.DATE_TIME_CREATED,
						rec_signs_dist_map.ID_MODIFIED,
						rec_signs_dist_map.DATE_TIME_MODIFIED
					);

					--DBMS_OUTPUT.PUT_LINE ('Added Distribution:' || rec_signs_dist_map.PF_DISTRIBUTION_ID);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
					
					INSERT INTO OM_PF_EVENT_DIST_MAPPING_BAD 
					( 
						 PF_DISTRIBUTION_ID ,
						 PF_EVENT_ID ,
						 PF_DISTRIBUTION_NAME ,
						 PF_PRINT_START_DATE ,
						 ID_CREATED ,
						 DATE_TIME_CREATED ,
						 ID_MODIFIED ,
						 DATE_TIME_MODIFIED ,
						 PF_CRITERIA_TYPE ,
						 PF_FORCE_PUSH_IND /*,
						 EXCEPTION_CODE ,
						 EXCEPTION_MSSG ,
						 EXCEPTION_DTTM */
					) 
					VALUES  
					(  
						 rec_signs_dist_map.PF_DISTRIBUTION_ID ,
						 rec_signs_dist_map.PF_EVENT_ID ,
						 rec_signs_dist_map.PF_DISTRIBUTION_NAME ,
						 rec_signs_dist_map.PF_PRINT_START_DATE ,
						 rec_signs_dist_map.ID_CREATED ,
						 rec_signs_dist_map.DATE_TIME_CREATED ,
						 rec_signs_dist_map.ID_MODIFIED ,
						 rec_signs_dist_map.DATE_TIME_MODIFIED ,
						 rec_signs_dist_map.PF_CRITERIA_TYPE ,
						 rec_signs_dist_map.PF_FORCE_PUSH_IND /*,
						 v_err_code ,
						 v_err_msg ,
						 SYSDATE*/
					) ;

				v_counter := v_counter - 1;
				END;
			WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
				INSERT INTO OM_PF_EVENT_DIST_MAPPING_BAD 
					( 
						 PF_DISTRIBUTION_ID ,
						 PF_EVENT_ID ,
						 PF_DISTRIBUTION_NAME ,
						 PF_PRINT_START_DATE ,
						 ID_CREATED ,
						 DATE_TIME_CREATED ,
						 ID_MODIFIED ,
						 DATE_TIME_MODIFIED ,
						 PF_CRITERIA_TYPE ,
						 PF_FORCE_PUSH_IND /*,
						 EXCEPTION_CODE ,
						 EXCEPTION_MSSG ,
						 EXCEPTION_DTTM */
					) 
					VALUES  
					(  
						 rec_signs_dist_map.PF_DISTRIBUTION_ID ,
						 rec_signs_dist_map.PF_EVENT_ID ,
						 rec_signs_dist_map.PF_DISTRIBUTION_NAME ,
						 rec_signs_dist_map.PF_PRINT_START_DATE ,
						 rec_signs_dist_map.ID_CREATED ,
						 rec_signs_dist_map.DATE_TIME_CREATED ,
						 rec_signs_dist_map.ID_MODIFIED ,
						 rec_signs_dist_map.DATE_TIME_MODIFIED ,
						 rec_signs_dist_map.PF_CRITERIA_TYPE ,
						 rec_signs_dist_map.PF_FORCE_PUSH_IND /*,
						 v_err_code ,
						 v_err_msg ,
						 SYSDATE */
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
    
	-- INSERT RECORDS THAT DO NOT MATCH JOIN CONDITION OF THE CURSOR QUERY
    		INSERT INTO OM_PF_EVENT_DIST_MAPPING_BAD 
			( 
				 PF_DISTRIBUTION_ID ,
				 PF_EVENT_ID ,
				 PF_DISTRIBUTION_NAME ,
				 PF_PRINT_START_DATE ,
				 ID_CREATED ,
				 DATE_TIME_CREATED ,
				 ID_MODIFIED ,
				 DATE_TIME_MODIFIED ,
				 PF_CRITERIA_TYPE ,
				 PF_FORCE_PUSH_IND /*,
				 EXCEPTION_CODE ,
				 EXCEPTION_MSSG ,
				 EXCEPTION_DTTM */
			) 
			SELECT   "DIST".PF_DISTRIBUTION_ID ,
					 "DIST".PF_EVENT_ID ,
					 "DIST".PF_DISTRIBUTION_NAME ,
					 "DIST".PF_PRINT_START_DATE ,
					 "DIST".ID_CREATED ,
					 "DIST".DATE_TIME_CREATED ,
					 "DIST".ID_MODIFIED ,
					 "DIST".DATE_TIME_MODIFIED ,
					 "DIST".PF_CRITERIA_TYPE ,
					 "DIST".PF_FORCE_PUSH_IND
					/* , '000' 
					 ,'Master records not found' 
					 ,SYSDATE */
			FROM OM_PF_EVENT_DIST_MAPPING_TMP "DIST"
			WHERE NOT EXISTS
			( 
				SELECT 1
				FROM OM_PF_EVENT_DIST_MAPPING_TMP, OM_SIGNS_HEADER
				WHERE OM_PF_EVENT_DIST_MAPPING_TMP.PF_EVENT_ID = OM_SIGNS_HEADER.SYS_EVENT_ID
				AND OM_PF_EVENT_DIST_MAPPING_TMP.PF_EVENT_ID = "DIST".PF_EVENT_ID
			); 
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
  
END;
/
