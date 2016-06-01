SET SERVEROUTPUT ON;
-- UPDATE / ADD SIGNS IMAGE MAPPING IN OM_PF_EVENT_IMAGE_MAPPING_TMP
DECLARE
  CURSOR c_signs_image_map
  IS
  
	SELECT OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_IMAGE_ID ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_EVENT_ID ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_IMAGE_NAME ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_IMAGE_URL ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_PRODUCT_ID ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_IMAGE_GROUP ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.ID_CREATED ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.DATE_TIME_CREATED ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.ID_MODIFIED ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.DATE_TIME_MODIFIED ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_PRODUCT_DESC ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_QUANTITY ,
		 OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_BUNDLE_PRODUCT_ID,
		 OM_SIGNS_HEADER.SYS_EVENT_ID,
		 OM_PRODUCT.SYS_PRODUCT_ID
    FROM OM_PF_EVENT_IMAGE_MAPPING_TMP, OM_SIGNS_HEADER, OM_PRODUCT
	WHERE OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_EVENT_ID = OM_SIGNS_HEADER.SYS_EVENT_ID
	AND OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR;

 
  -- DEFINE THE RECORD
  rec_signs_image_map c_signs_image_map%ROWTYPE;
  -- DEFINE VARIABLES
  v_sysimageid OM_SIGNS_IMAGE_MAPPING.SYS_IMAGE_ID%TYPE;
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';

BEGIN
	OPEN c_signs_image_map;
	LOOP
		FETCH c_signs_image_map INTO rec_signs_image_map;
		EXIT WHEN c_signs_image_map%NOTFOUND OR c_signs_image_map%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'ImageId=' || rec_signs_image_map.PF_IMAGE_ID );
		BEGIN
					
			-- CHECK IF RECORD EXISTS
			SELECT SYS_IMAGE_ID INTO v_sysimageid FROM OM_SIGNS_IMAGE_MAPPING 
			Where SYS_IMAGE_ID = rec_signs_image_map.PF_IMAGE_ID;        
			-- UPDATE OM_SIGNS_IMAGE_MAPPING 
			
			UPDATE OM_SIGNS_IMAGE_MAPPING 
			SET  SYS_EVENT_ID = rec_signs_image_map.PF_EVENT_ID,
				 IMAGE_NAME = TRIM(rec_signs_image_map.PF_IMAGE_NAME),
				 IMAGE_URL = NVL(rec_signs_image_map.PF_IMAGE_URL,' '),
				 SYS_PRODUCT_ID = rec_signs_image_map.SYS_PRODUCT_ID,
				 IMAGE_GROUP = rec_signs_image_map.PF_IMAGE_GROUP,
				 QUANTITY = rec_signs_image_map.PF_QUANTITY,
				 CREATE_USER_ID = rec_signs_image_map.ID_CREATED,
				 CREATE_DTTM = rec_signs_image_map.DATE_TIME_CREATED,
				 UPDATE_USER_ID = rec_signs_image_map.ID_MODIFIED,
				 UPDATE_DTTM = rec_signs_image_map.DATE_TIME_MODIFIED,
				 ACTIVE_CD = v_active_cd
			WHERE  SYS_EVENT_ID = v_sysimageid;
			
			--DBMS_OUTPUT.PUT_LINE ('Updated ImageId:' || rec_signs_image_map.PF_IMAGE_ID);          
		EXCEPTION
			-- INSERT OM_SIGNS_IMAGE_MAPPING
			WHEN NO_DATA_FOUND THEN
				BEGIN
				
					INSERT INTO OM_SIGNS_IMAGE_MAPPING 
					( 
						 SYS_IMAGE_ID ,
						 SYS_EVENT_ID ,
						 IMAGE_NAME ,
						 IMAGE_URL ,
						 SYS_PRODUCT_ID ,
						 IMAGE_GROUP ,
						 QUANTITY ,
						 CREATE_USER_ID ,
						 CREATE_DTTM ,
						 UPDATE_USER_ID ,
						 UPDATE_DTTM ,
						 ACTIVE_CD
					) 
					VALUES 
					(
						rec_signs_image_map.PF_IMAGE_ID,
						rec_signs_image_map.PF_EVENT_ID,
						rec_signs_image_map.PF_IMAGE_NAME,
						NVL(rec_signs_image_map.PF_IMAGE_URL,' '),
						rec_signs_image_map.SYS_PRODUCT_ID,
						rec_signs_image_map.PF_IMAGE_GROUP,
						rec_signs_image_map.PF_QUANTITY,
						rec_signs_image_map.ID_CREATED,
						rec_signs_image_map.DATE_TIME_CREATED,
						rec_signs_image_map.ID_MODIFIED,
						rec_signs_image_map.DATE_TIME_MODIFIED,
						v_active_cd
					);

					--DBMS_OUTPUT.PUT_LINE ('Added ImageId:' || rec_signs_image_map.PF_IMAGE_ID);   
					
				EXCEPTION	
					WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
					
					INSERT INTO OM_PF_EVENT_IMAGE_MAPPING_BAD 
					( 
						 PF_IMAGE_ID ,
						 PF_EVENT_ID ,
						 PF_IMAGE_NAME ,
						 PF_IMAGE_URL ,
						 PF_PRODUCT_ID ,
						 PF_IMAGE_GROUP ,
						 ID_CREATED ,
						 DATE_TIME_CREATED ,
						 ID_MODIFIED ,
						 DATE_TIME_MODIFIED ,
						 PF_PRODUCT_DESC ,
						 PF_QUANTITY ,
						 PF_BUNDLE_PRODUCT_ID ,
						 EXCEPTION_CODE ,
						 EXCEPTION_MSSG ,
						 EXCEPTION_DTTM 
					) 
					VALUES  
					(  
						 rec_signs_image_map.PF_IMAGE_ID ,
						 rec_signs_image_map.PF_EVENT_ID ,
						 rec_signs_image_map.PF_IMAGE_NAME ,
						 rec_signs_image_map.PF_IMAGE_URL ,
						 rec_signs_image_map.PF_PRODUCT_ID ,
						 rec_signs_image_map.PF_IMAGE_GROUP ,
						 rec_signs_image_map.ID_CREATED ,
						 rec_signs_image_map.DATE_TIME_CREATED ,
						 rec_signs_image_map.ID_MODIFIED ,
						 rec_signs_image_map.DATE_TIME_MODIFIED ,
						 rec_signs_image_map.PF_PRODUCT_DESC ,
						 rec_signs_image_map.PF_QUANTITY ,
						 rec_signs_image_map.PF_BUNDLE_PRODUCT_ID ,
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
				INSERT INTO OM_PF_EVENT_IMAGE_MAPPING_BAD 
					( 
						 PF_IMAGE_ID ,
						 PF_EVENT_ID ,
						 PF_IMAGE_NAME ,
						 PF_IMAGE_URL ,
						 PF_PRODUCT_ID ,
						 PF_IMAGE_GROUP ,
						 ID_CREATED ,
						 DATE_TIME_CREATED ,
						 ID_MODIFIED ,
						 DATE_TIME_MODIFIED ,
						 PF_PRODUCT_DESC ,
						 PF_QUANTITY ,
						 PF_BUNDLE_PRODUCT_ID ,
						 EXCEPTION_CODE ,
						 EXCEPTION_MSSG ,
						 EXCEPTION_DTTM 
					) 
					VALUES  
					(  
						 rec_signs_image_map.PF_IMAGE_ID ,
						 rec_signs_image_map.PF_EVENT_ID ,
						 rec_signs_image_map.PF_IMAGE_NAME ,
						 rec_signs_image_map.PF_IMAGE_URL ,
						 rec_signs_image_map.PF_PRODUCT_ID ,
						 rec_signs_image_map.PF_IMAGE_GROUP ,
						 rec_signs_image_map.ID_CREATED ,
						 rec_signs_image_map.DATE_TIME_CREATED ,
						 rec_signs_image_map.ID_MODIFIED ,
						 rec_signs_image_map.DATE_TIME_MODIFIED ,
						 rec_signs_image_map.PF_PRODUCT_DESC ,
						 rec_signs_image_map.PF_QUANTITY ,
						 rec_signs_image_map.PF_BUNDLE_PRODUCT_ID ,
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
    
	-- INSERT RECORDS THAT DO NOT MATCH JOIN CONDITION OF THE CURSOR QUERY
	INSERT INTO OM_PF_EVENT_IMAGE_MAPPING_BAD 
	( 
		 PF_IMAGE_ID ,
		 PF_EVENT_ID ,
		 PF_IMAGE_NAME ,
		 PF_IMAGE_URL ,
		 PF_PRODUCT_ID ,
		 PF_IMAGE_GROUP ,
		 ID_CREATED ,
		 DATE_TIME_CREATED ,
		 ID_MODIFIED ,
		 DATE_TIME_MODIFIED ,
		 PF_PRODUCT_DESC ,
		 PF_QUANTITY ,
		 PF_BUNDLE_PRODUCT_ID ,
		 EXCEPTION_CODE ,
		 EXCEPTION_MSSG ,
		 EXCEPTION_DTTM 
	) 
	SELECT "IMAGE_MAP".PF_IMAGE_ID ,
		 "IMAGE_MAP".PF_EVENT_ID ,
		 "IMAGE_MAP".PF_IMAGE_NAME ,
		 "IMAGE_MAP".PF_IMAGE_URL ,
		 "IMAGE_MAP".PF_PRODUCT_ID ,
		 "IMAGE_MAP".PF_IMAGE_GROUP ,
		 "IMAGE_MAP".ID_CREATED ,
		 "IMAGE_MAP".DATE_TIME_CREATED ,
		 "IMAGE_MAP".ID_MODIFIED ,
		 "IMAGE_MAP".DATE_TIME_MODIFIED ,
		 "IMAGE_MAP".PF_PRODUCT_DESC ,
		 "IMAGE_MAP".PF_QUANTITY ,
		 "IMAGE_MAP".PF_BUNDLE_PRODUCT_ID
		 , '000' 
		 ,'Master records not found' 
		 ,SYSDATE
	FROM OM_PF_EVENT_IMAGE_MAPPING_TMP "IMAGE_MAP"
	WHERE NOT EXISTS
	( 
		SELECT 1
		FROM OM_PF_EVENT_IMAGE_MAPPING_TMP, OM_SIGNS_HEADER, OM_PRODUCT
		WHERE OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_EVENT_ID = OM_SIGNS_HEADER.SYS_EVENT_ID
		AND OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR
		AND OM_PF_EVENT_IMAGE_MAPPING_TMP.PF_IMAGE_ID = "IMAGE_MAP".PF_IMAGE_ID
	);
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
  
END;
/
