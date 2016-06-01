SET SERVEROUTPUT ON;
-- UPDATE / ADD Template IN OM_TEMPLATE

DECLARE
  CURSOR c_template
  IS
SELECT 
		TMP.PF_TEMPLATE_ID,
		TMP.PF_CATAGORY ,
		TMP.PF_STATUS ,
		TMP.ID_CREATED ,
		TMP.DATE_TIME_CREATED ,
		TMP.ID_MODIFIED ,
		TMP.DATE_TIME_MODIFIED ,
		sysdate+365 as PF_EXPIRATION_DATE ,
		sysdate as PF_ENABLE_DATE ,
		sysdate+365 as PF_DISABLE_DATE ,
		0 as PF_EXPIRE_IND ,
		1 as PF_ENABLE_IND ,
		0 as PF_DISABLE_IND ,
		1 as PF_LICENSED_IND ,
	V.SYS_VENDOR_ID as SYS_SERVICE_VENDOR_ID ,
  V.SYS_VENDOR_ID as SYS_ROYALTY_VENDOR_ID ,
		'Rate' as PF_ROYALTY_TYPE ,
		0 as PF_ROYALTY_COST ,
		5 as PF_ROYALTY_RATE ,
		TO_NUMBER(TRIM(TMP.PF_TEMPLATE_ID))  "KIOSK_TEMPLATE",
		TMP.PF_DESCRIPTIONS,
		TMP.PF_SIZE,
		OM_TEMPLATE_CATEGORY_LVL1.SYS_TEMPLATE_CATEGORY_LVL1_ID,
		OM_TEMPLATE_CATEGORY_LVL2.SYS_TEMPLATE_CATEGORY_LVL2_ID,
		OM_TEMPLATE_CATEGORY_TYPE.SYS_TEMPLATE_CATEGORY_TYPE_ID,
		OM_PRODUCT.SYS_PRODUCT_ID,
    TMP.PF_DATE_DEACTIVATED,
    TMP.PF_NOTE,
    TMP.PF_TEMPLATE_LOAD,
    TMP.PF_NO_OF_PHOTOS,
    TMP.PF_CONFIG_FILE,
    TMP.PF_FILTER,
    TMP.PF_PRODUCT_TYPE,
    TMP.PF_VENDOR
     FROM 	OM_PF_KIOSK_TEMPLATE_TMP TMP, 
		OM_TEMPLATE_CATEGORY_LVL1, 
		OM_TEMPLATE_CATEGORY_LVL2,
      		OM_TEMPLATE_CATEGORY_TYPE, 
		OM_PF_KIOSK_PRODUCT_MAP_TMP, 
		OM_PRODUCT,  
		OM_VENDOR V
    WHERE  	TMP.PF_TEMPLATE_ID = OM_PF_KIOSK_PRODUCT_MAP_TMP.PF_TEMPLATE_ID
    		AND TRIM(TMP.PF_PRODUCT_TYPE) = TRIM(OM_TEMPLATE_CATEGORY_LVL1.CATEGORY_NAME)
                AND TRIM(TMP.PF_CATAGORY) = TRIM(OM_TEMPLATE_CATEGORY_LVL2.CATEGORY_NAME)
                AND OM_TEMPLATE_CATEGORY_LVL1.SYS_CATEGORY_TYPE_ID = OM_TEMPLATE_CATEGORY_TYPE.SYS_TEMPLATE_CATEGORY_TYPE_ID
                AND OM_TEMPLATE_CATEGORY_LVL2.TEMPLATE_CATEGORY_LVL1_ID = OM_TEMPLATE_CATEGORY_LVL1.SYS_TEMPLATE_CATEGORY_LVL1_ID
                AND OM_PF_KIOSK_PRODUCT_MAP_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR
AND Trim(upper(TMP.PF_VENDOR)) = trim(upper(V.DESCRIPTION));


   -- DEFINE THE RECORD
  rec_template c_template%ROWTYPE;
  -- DEFINE VARIABLES
  v_sys_tempalte_id OM_TEMPLATE.SYS_TEMPLATE_ID%TYPE:= 0;
  v_sys_tmplate_cat_asso OM_TEMPLATE_TEMPL_CAT_ASSO.SYS_TEMPLATE_TEMPL_CAT_ASSO_ID%TYPE :=0;
  v_sys_product_asso OM_TEMPLATE_PRODUCT_ASSOC.SYS_TEMPLATE_PRODUCT_ASSOC_ID%TYPE :=0;
  --v_deleted_ind OM_PF_TEMPLATE_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
  
BEGIN
  OPEN c_template;
  LOOP
    FETCH c_template INTO rec_template;
    EXIT WHEN c_template%NOTFOUND OR c_template%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Template ID=' || rec_template.KIOSK_TEMPLATE );
    BEGIN
	
	v_active_cd := 0;
	
	-- Check Active Ind
	IF NVL(rec_template.PF_STATUS,'N') = 'Active' THEN
		v_active_cd := 1;
	END IF;
	
	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
	IF (rec_template.PF_STATUS = 'Inactive') THEN        			
		v_active_cd := 0;
	END IF;
			
        -- CHECK IF RECORD EXISTS in OM_TEMPLATE
        SELECT SYS_TEMPLATE_ID INTO v_sys_tempalte_id FROM OM_TEMPLATE WHERE TEMPLATE_NBR = rec_template."KIOSK_TEMPLATE"
		AND SYS_SERVICE_VENDOR_ID = rec_template.SYS_SERVICE_VENDOR_ID;
			
        -- UPDATE OM_Template
		UPDATE OM_TEMPLATE SET 
				 DESCRIPTION = NVL(rec_template.PF_DESCRIPTIONS,' '),
				 OUTPUT_SIZE = rec_template.PF_SIZE,
				 ACTIVE_CD = v_active_cd,
				 EXPIRATION_DTTM = rec_template.PF_EXPIRATION_DATE,
				 ENABLE_DATE = rec_template.PF_ENABLE_DATE,
				 DISABLE_DATE = rec_template.PF_DISABLE_DATE,
				 EXPIRE_CD = rec_template.PF_EXPIRE_IND,
				 ENABLE_CD = rec_template.PF_ENABLE_IND,
				 DISABLE_CD = rec_template.PF_DISABLE_IND,
				 LICENSED_CD = rec_template.PF_LICENSED_IND,
				 ROYALTY_TYPE = rec_template.PF_ROYALTY_TYPE,
				 ROYALTY_COST = rec_template.PF_ROYALTY_COST,
				 ROYALTY_RATE = rec_template.PF_ROYALTY_RATE,
				 SYS_ROYALTY_VENDOR_ID = rec_template.SYS_ROYALTY_VENDOR_ID,
				 CREATE_USER_ID = rec_template.ID_CREATED,
				 CREATE_DTTM = rec_template.DATE_TIME_CREATED,
				 UPDATE_USER_ID = rec_template.ID_MODIFIED,
				 UPDATE_DTTM = rec_template.DATE_TIME_MODIFIED
		WHERE  SYS_TEMPLATE_ID = v_sys_tempalte_id;  

		--DBMS_OUTPUT.PUT_LINE ('Updated OM_TEMPLATE  ' || 'Template ID=' || rec_template."KIOSK_TEMPLATE" ); 
		
		BEGIN
			-- CHECK IF RECORD EXISTS in OM_TEMPLATE_TEMPL_CAT_ASSO
			SELECT SYS_TEMPLATE_TEMPL_CAT_ASSO_ID INTO v_sys_tmplate_cat_asso 
			FROM OM_TEMPLATE_TEMPL_CAT_ASSO
			WHERE SYS_TEMPLATE_ID = v_sys_tempalte_id
			AND SYS_CATEGORY_TYPE_ID = rec_template.SYS_TEMPLATE_CATEGORY_TYPE_ID
			AND SYS_TEMPLATE_CATEGORY_LVL1_ID = rec_template.SYS_TEMPLATE_CATEGORY_LVL1_ID
			AND SYS_TEMPLATE_CATEGORY_LVL2_ID = rec_template.SYS_TEMPLATE_CATEGORY_LVL2_ID;
			
			--Update OM_TEMPLATE_TEMPL_CAT_ASSO
			UPDATE OM_TEMPLATE_TEMPL_CAT_ASSO 
			SET 
				 ACTIVE_CD = v_active_cd,
				 CREATE_USER_ID = rec_template.ID_CREATED,
				 CREATE_DTTM = rec_template.DATE_TIME_CREATED,
				 UPDATE_USER_ID = rec_template.ID_MODIFIED,
				 UPDATE_DTTM = rec_template.DATE_TIME_MODIFIED
			WHERE  SYS_TEMPLATE_TEMPL_CAT_ASSO_ID = v_sys_tmplate_cat_asso;
		
		EXCEPTION
			WHEN NO_DATA_FOUND THEN		
			
				SELECT OM_TEMPLATE_TEMPL_CAT_ASSO_SEQ.NEXTVAL INTO v_sys_tmplate_cat_asso FROM DUAL;
				
				-- INSERT OM_TEMPLATE_TEMPL_CAT_ASSO
				INSERT INTO OM_TEMPLATE_TEMPL_CAT_ASSO 
				( 
					 SYS_TEMPLATE_TEMPL_CAT_ASSO_ID ,
					 SYS_TEMPLATE_ID ,
					 SYS_CATEGORY_TYPE_ID ,
					 SYS_TEMPLATE_CATEGORY_LVL1_ID ,
					 SYS_TEMPLATE_CATEGORY_LVL2_ID ,
					 ACTIVE_CD ,
					 CREATE_USER_ID ,
					 CREATE_DTTM ,
					 UPDATE_USER_ID ,
					 UPDATE_DTTM
				) 
				VALUES 
				(
					v_sys_tmplate_cat_asso,
					v_sys_tempalte_id,
					rec_template.SYS_TEMPLATE_CATEGORY_TYPE_ID,
					rec_template.SYS_TEMPLATE_CATEGORY_LVL1_ID,
					rec_template.SYS_TEMPLATE_CATEGORY_LVL2_ID,
					v_active_cd,
					rec_template.ID_CREATED,
					rec_template.DATE_TIME_CREATED,
					rec_template.ID_MODIFIED,
					rec_template.DATE_TIME_MODIFIED
				);

				--DBMS_OUTPUT.PUT_LINE ('Added OM_TEMPLATE_TEMPL_CAT_ASSO =' || 'Template ID=' || v_sys_tempalte_id ); 

		END;

		BEGIN
			-- CHECK IF RECORD EXISTS in OM_TEMPLATE_PRODUCT_ASSOC
			SELECT SYS_TEMPLATE_PRODUCT_ASSOC_ID INTO v_sys_product_asso 
			FROM OM_TEMPLATE_PRODUCT_ASSOC
			WHERE SYS_TEMPLATE_TEMPL_CAT_ASSO_ID = v_sys_tmplate_cat_asso
			AND SYS_PRODUCT_ID = rec_template.SYS_PRODUCT_ID;

			--Update OM_TEMPLATE_PRODUCT_ASSOC
			UPDATE OM_TEMPLATE_PRODUCT_ASSOC 
			SET 
				 CREATE_USER_ID = rec_template.ID_CREATED,
				 CREATE_DTTM =  rec_template.DATE_TIME_CREATED,
				 UPDATE_USER_ID = rec_template.ID_MODIFIED,
				 UPDATE_DTTM = rec_template.DATE_TIME_MODIFIED
			WHERE  SYS_TEMPLATE_PRODUCT_ASSOC_ID = v_sys_product_asso;
		
		EXCEPTION
			WHEN NO_DATA_FOUND THEN		
				-- INSERT OM_TEMPLATE_PRODUCT_ASSOC	
				INSERT INTO OM_TEMPLATE_PRODUCT_ASSOC 
				( 
					 SYS_TEMPLATE_PRODUCT_ASSOC_ID ,
					 SYS_PRODUCT_ID ,
					 SYS_TEMPLATE_TEMPL_CAT_ASSO_ID ,
					 CREATE_USER_ID ,
					 CREATE_DTTM ,
					 UPDATE_USER_ID ,
					 UPDATE_DTTM
				) 
				VALUES 
				(
					OM_TEMPLATE_PRODUCT_ASSOC_SEQ.NEXTVAL,
					rec_template.SYS_PRODUCT_ID,
					v_sys_tmplate_cat_asso,
					rec_template.ID_CREATED,
					rec_template.DATE_TIME_CREATED,
					rec_template.ID_MODIFIED,
					rec_template.DATE_TIME_MODIFIED
				);	
					
				--DBMS_OUTPUT.PUT_LINE ('Added OM_TEMPLATE_PRODUCT_ASSOC=' || 'Template ID=' || v_sys_tmplate_cat_asso ); 
		
		END;
		
    EXCEPTION
		WHEN NO_DATA_FOUND THEN
			BEGIN
				
				SELECT OM_TEMPLATE_SEQ.NEXTVAL INTO v_sys_tempalte_id FROM DUAL;
				
				-- INSERT OM_Template       
				INSERT INTO OM_TEMPLATE 
					( 
						 SYS_TEMPLATE_ID ,
						 TEMPLATE_NBR ,
						 DESCRIPTION ,
						 OUTPUT_SIZE ,
						 ACTIVE_CD ,
						 EXPIRATION_DTTM ,
						 ENABLE_DATE ,
						 DISABLE_DATE ,
						 EXPIRE_CD ,
						 ENABLE_CD ,
						 DISABLE_CD ,
						 LICENSED_CD ,
						 SYS_SERVICE_VENDOR_ID ,
						 SYS_ROYALTY_VENDOR_ID ,
						 ROYALTY_TYPE ,
						 ROYALTY_COST ,
						 ROYALTY_RATE ,
						 CREATE_USER_ID ,
						 CREATE_DTTM ,
						 UPDATE_USER_ID ,
						 UPDATE_DTTM
					 ) 
					 VALUES 
					 (
						 v_sys_tempalte_id,
						 rec_template."KIOSK_TEMPLATE",
						 NVL(rec_template.PF_DESCRIPTIONS,' '),
						 rec_template.PF_SIZE,
						 v_active_cd,
						 rec_template.PF_EXPIRATION_DATE,
						 rec_template.PF_ENABLE_DATE,
						 rec_template.PF_DISABLE_DATE,
						 rec_template.PF_EXPIRE_IND,
						 rec_template.PF_ENABLE_IND,
						 rec_template.PF_DISABLE_IND,
						 rec_template.PF_LICENSED_IND,
						 rec_template.SYS_SERVICE_VENDOR_ID,
						 rec_template.SYS_ROYALTY_VENDOR_ID,
						 rec_template.PF_ROYALTY_TYPE,
						 rec_template.PF_ROYALTY_COST,
						 rec_template.PF_ROYALTY_RATE,
						 rec_template.ID_CREATED,
						 rec_template.DATE_TIME_CREATED,
						 rec_template.ID_MODIFIED,
						 rec_template.DATE_TIME_MODIFIED

					 );

				--DBMS_OUTPUT.PUT_LINE ('Added Template=' || 'Template ID=' || rec_template."KIOSK_TEMPLATE" ); 
				
				SELECT OM_TEMPLATE_TEMPL_CAT_ASSO_SEQ.NEXTVAL INTO v_sys_tmplate_cat_asso FROM DUAL;
				
				-- INSERT OM_TEMPLATE_TEMPL_CAT_ASSO
				INSERT INTO OM_TEMPLATE_TEMPL_CAT_ASSO 
				( 
					 SYS_TEMPLATE_TEMPL_CAT_ASSO_ID ,
					 SYS_TEMPLATE_ID ,
					 SYS_CATEGORY_TYPE_ID ,
					 SYS_TEMPLATE_CATEGORY_LVL1_ID ,
					 SYS_TEMPLATE_CATEGORY_LVL2_ID ,
					 ACTIVE_CD ,
					 CREATE_USER_ID ,
					 CREATE_DTTM ,
					 UPDATE_USER_ID ,
					 UPDATE_DTTM
				) 
				VALUES 
				(
					v_sys_tmplate_cat_asso,
					v_sys_tempalte_id,
					rec_template.SYS_TEMPLATE_CATEGORY_TYPE_ID,
					rec_template.SYS_TEMPLATE_CATEGORY_LVL1_ID,
					rec_template.SYS_TEMPLATE_CATEGORY_LVL2_ID,
					v_active_cd,
					rec_template.ID_CREATED,
					rec_template.DATE_TIME_CREATED,
					rec_template.ID_MODIFIED,
					rec_template.DATE_TIME_MODIFIED
				);

				--DBMS_OUTPUT.PUT_LINE ('Added OM_TEMPLATE_TEMPL_CAT_ASSO=' || 'Template ID=' || v_sys_tempalte_id ); 			

				-- INSERT OM_TEMPLATE_PRODUCT_ASSOC	
				INSERT INTO OM_TEMPLATE_PRODUCT_ASSOC 
				( 
					 SYS_TEMPLATE_PRODUCT_ASSOC_ID ,
					 SYS_PRODUCT_ID ,
					 SYS_TEMPLATE_TEMPL_CAT_ASSO_ID ,
					 CREATE_USER_ID ,
					 CREATE_DTTM ,
					 UPDATE_USER_ID ,
					 UPDATE_DTTM
				) 
				VALUES 
				(
					OM_TEMPLATE_PRODUCT_ASSOC_SEQ.NEXTVAL,
					rec_template.SYS_PRODUCT_ID,
					v_sys_tmplate_cat_asso,
					rec_template.ID_CREATED,
					rec_template.DATE_TIME_CREATED,
					rec_template.ID_MODIFIED,
					rec_template.DATE_TIME_MODIFIED
				);	
					
				--DBMS_OUTPUT.PUT_LINE ('Added OM_TEMPLATE_TEMPL_CAT_ASSO=' || 'Template ID=' || v_sys_tmplate_cat_asso ); 	
		
			EXCEPTION	
				WHEN OTHERS THEN
					v_err_msg := SUBSTR(SQLERRM, 1 , 200);
					v_err_code := SQLCODE ;
					--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
					ROLLBACK;
					
	INSERT INTO OM_PF_KIOSK_TEMPLATE_BAD 
				( 
				PF_TEMPLATE_ID,
        PF_VENDOR,
        PF_PRODUCT_TYPE,
PF_SIZE,
PF_CATAGORY,
PF_FILTER,
PF_CONFIG_FILE,
PF_STATUS,
PF_NO_OF_PHOTOS,
PF_DESCRIPTIONS,
PF_TEMPLATE_LOAD,
PF_NOTE,
ID_CREATED,
DATE_TIME_CREATED,
ID_MODIFIED,
DATE_TIME_MODIFIED,
PF_DATE_DEACTIVATED,
EXCEPTION_CODE,
EXCEPTION_MSSG,
EXCEPTION_DTTM
			 )
VALUES(
        rec_template.PF_TEMPLATE_ID ,
				 rec_template.PF_VENDOR ,
				 rec_template.PF_PRODUCT_TYPE ,
				rec_template.PF_SIZE ,
				 rec_template.PF_CATAGORY ,
				 rec_template.PF_FILTER ,
				 rec_template.PF_CONFIG_FILE ,
				 rec_template.PF_STATUS ,
				 rec_template.PF_NO_OF_PHOTOS ,
				 rec_template.PF_DESCRIPTIONS ,
				 rec_template.PF_TEMPLATE_LOAD ,
				 rec_template.PF_NOTE ,
				 rec_template.ID_CREATED ,
				rec_template.DATE_TIME_CREATED ,
				 rec_template.ID_MODIFIED ,
				 rec_template.DATE_TIME_MODIFIED ,
				 rec_template.PF_DATE_DEACTIVATED ,
							 v_err_code ,
							 v_err_msg ,
							 SYSDATE);
					v_counter := v_counter + 1;
			END;      
	    WHEN OTHERS THEN
			v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
			--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
			ROLLBACK;
INSERT INTO OM_PF_KIOSK_TEMPLATE_BAD 
				( 
				PF_TEMPLATE_ID,
        PF_VENDOR,
        PF_PRODUCT_TYPE,
PF_SIZE,
PF_CATAGORY,
PF_FILTER,
PF_CONFIG_FILE,
PF_STATUS,
PF_NO_OF_PHOTOS,
PF_DESCRIPTIONS,
PF_TEMPLATE_LOAD,
PF_NOTE,
ID_CREATED,
DATE_TIME_CREATED,
ID_MODIFIED,
DATE_TIME_MODIFIED,
PF_DATE_DEACTIVATED,
EXCEPTION_CODE,
EXCEPTION_MSSG,
EXCEPTION_DTTM
			 ) 
values(			     rec_template.PF_TEMPLATE_ID ,
				 rec_template.PF_VENDOR ,
				 rec_template.PF_PRODUCT_TYPE ,
				rec_template.PF_SIZE ,
				 rec_template.PF_CATAGORY ,
				 rec_template.PF_FILTER ,
				 rec_template.PF_CONFIG_FILE ,
				 rec_template.PF_STATUS ,
				 rec_template.PF_NO_OF_PHOTOS ,
				 rec_template.PF_DESCRIPTIONS ,
				 rec_template.PF_TEMPLATE_LOAD ,
				 rec_template.PF_NOTE ,
				 rec_template.ID_CREATED ,
				rec_template.DATE_TIME_CREATED ,
				 rec_template.ID_MODIFIED ,
				 rec_template.DATE_TIME_MODIFIED ,
				 rec_template.PF_DATE_DEACTIVATED ,
							 v_err_code ,
							 v_err_msg ,
							 SYSDATE);
			 
				v_counter := v_counter - 1;
	END;
	v_counter := v_counter + 1;
    --DBMS_OUTPUT.PUT_LINE ('Counter :' || v_counter);
/*  SINCE WE ARE DOING ROLLBACK FOR EXCEPTIONS, HENCE COMMITTING EVERY RECORD IS NEEDED. 
	IF (v_counter = v_commitinterval) THEN        
        COMMIT;
        --DBMS_OUTPUT.PUT_LINE ('Committed ' || v_counter || ' records');  
        v_counter := 0;
    END IF; */
	COMMIT;
       
  END LOOP;
  -- COMMIT CURSOR RECORDS
  COMMIT;
  INSERT INTO OM_PF_KIOSK_TEMPLATE_BAD 
				( 
				PF_TEMPLATE_ID,
PF_VENDOR,
PF_PRODUCT_TYPE,
PF_SIZE,
PF_CATAGORY,
PF_FILTER,
PF_CONFIG_FILE,
PF_STATUS,
PF_NO_OF_PHOTOS,
PF_DESCRIPTIONS,
PF_TEMPLATE_LOAD,
PF_NOTE,
ID_CREATED,
DATE_TIME_CREATED,
ID_MODIFIED,
DATE_TIME_MODIFIED,
PF_DATE_DEACTIVATED,
EXCEPTION_CODE,
EXCEPTION_MSSG,
EXCEPTION_DTTM
			 ) SELECT 
			     "TEMPLATE_TMP".PF_TEMPLATE_ID ,
				 "TEMPLATE_TMP".PF_VENDOR ,
				 "TEMPLATE_TMP".PF_PRODUCT_TYPE ,
				 "TEMPLATE_TMP".PF_SIZE ,
				 "TEMPLATE_TMP".PF_CATAGORY ,
				 "TEMPLATE_TMP".PF_FILTER ,
				 "TEMPLATE_TMP".PF_CONFIG_FILE ,
				 "TEMPLATE_TMP".PF_STATUS ,
				 "TEMPLATE_TMP".PF_NO_OF_PHOTOS ,
				 "TEMPLATE_TMP".PF_DESCRIPTIONS ,
				 "TEMPLATE_TMP".PF_TEMPLATE_LOAD ,
				 "TEMPLATE_TMP".PF_NOTE ,
				 "TEMPLATE_TMP".ID_CREATED ,
				 "TEMPLATE_TMP".DATE_TIME_CREATED ,
				 "TEMPLATE_TMP".ID_MODIFIED ,
				 "TEMPLATE_TMP".DATE_TIME_MODIFIED ,
				 "TEMPLATE_TMP".PF_DATE_DEACTIVATED ,
				 '000' ,
			     'Master records not found' ,
			     SYSDATE
			 FROM OM_PF_KIOSK_TEMPLATE_TMP "TEMPLATE_TMP"
			 WHERE NOT EXISTS
				(   SELECT 
                1
                FROM 	OM_PF_KIOSK_TEMPLATE_TMP TMP, 
			OM_TEMPLATE_CATEGORY_LVL1, 
			OM_TEMPLATE_CATEGORY_LVL2,
      OM_TEMPLATE_CATEGORY_TYPE, 
			OM_PF_KIOSK_PRODUCT_MAP_TMP, 
			OM_PRODUCT,  
			OM_VENDOR V
                WHERE
		TRIM(TMP.PF_PRODUCT_TYPE) = TRIM(OM_TEMPLATE_CATEGORY_LVL1.CATEGORY_NAME)
                AND TRIM(TMP.PF_CATAGORY) = TRIM(OM_TEMPLATE_CATEGORY_LVL2.CATEGORY_NAME)
                AND OM_TEMPLATE_CATEGORY_LVL1.SYS_CATEGORY_TYPE_ID = OM_TEMPLATE_CATEGORY_TYPE.SYS_TEMPLATE_CATEGORY_TYPE_ID
                AND TMP.PF_TEMPLATE_ID = OM_PF_KIOSK_PRODUCT_MAP_TMP.PF_TEMPLATE_ID
                AND OM_PF_KIOSK_PRODUCT_MAP_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR
AND Trim(upper(TMP.PF_VENDOR)) = trim(upper(V.DESCRIPTION)));
				
			COMMIT;   
END;
/
