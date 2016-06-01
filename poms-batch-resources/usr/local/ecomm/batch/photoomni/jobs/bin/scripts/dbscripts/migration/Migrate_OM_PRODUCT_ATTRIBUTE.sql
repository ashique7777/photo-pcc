SET SERVEROUTPUT ON;
-- UPDATE / ADD Downtime Reason IN OM_PF_PRODUCT_TMP

DECLARE
  CURSOR c_product_attr
  IS
   	SELECT
		OM_PF_PRODUCT_TMP.PF_PRODUCT_ID ,
		OM_PF_PRODUCT_TMP.PF_SERVICE_CATEGORY_ID ,
		OM_PF_PRODUCT_TMP.PF_PRODUCT_OUTPUT_ID ,
		OM_PF_PRODUCT_TMP.PF_PRODUCT_FAMILY_ID ,
		OM_PF_PRODUCT_TMP.PF_PROCESSING_TYPE_ID ,
		OM_PF_PRODUCT_TMP.PF_LONG_DESC ,
		OM_PF_PRODUCT_TMP.PF_LABEL_DESC ,
		OM_PF_PRODUCT_TMP.PF_WIC ,
		OM_PF_PRODUCT_TMP.PF_UPC ,
		OM_PF_PRODUCT_TMP.PF_SECOND_WIC ,
		OM_PF_PRODUCT_TMP.PF_SECOND_UPC ,
		OM_PF_PRODUCT_TMP.PF_PRODUCT_TYPE_IND ,
		OM_PF_PRODUCT_TMP.PF_STORE_SUGGEST_IND ,
		OM_PF_PRODUCT_TMP.PF_ADDITIONAL_ATTR_IND ,
		OM_PF_PRODUCT_TMP.PF_SKIPPABLE_PRODUCT_IND ,
		OM_PF_PRODUCT_TMP.PF_CALL_VENDOR_IND ,
		OM_PF_PRODUCT_TMP.PF_PRODUCT_CODE ,
		OM_PF_PRODUCT_TMP.PF_CUSTOMER_HAS ,
		OM_PF_PRODUCT_TMP.PF_CUSTOMER_WANT ,
		OM_PF_PRODUCT_TMP.PF_PRODUCT_CATEGORY ,
		OM_PF_PRODUCT_TMP.PF_HOT_FOLDER_PRODUCT ,
		OM_PF_PRODUCT_TMP.PF_EMPTY_CARTRIDGE_IND ,
		OM_PF_PRODUCT_TMP.PF_HAS_EXTRA_IND ,
		OM_PF_PRODUCT_TMP.PF_COMMERCIAL_DISCNT_IND ,
		OM_PF_PRODUCT_TMP.PF_ACTIVE_IND ,
		OM_PF_PRODUCT_TMP.PF_DEACTIVATION_DTTM ,
		OM_PF_PRODUCT_TMP.PF_TRANSFER_IND ,
		NVL(OM_PF_PRODUCT_TMP.ID_CREATED,' ') ID_CREATED ,
		OM_PF_PRODUCT_TMP.DATE_TIME_CREATED ,
		OM_PF_PRODUCT_TMP.ID_MODIFIED ,
		OM_PF_PRODUCT_TMP.DATE_TIME_MODIFIED ,
		OM_PF_PRODUCT_TMP.PF_INDEX_INCL_IND ,
		OM_PF_PRODUCT_TMP.PF_BUTTON_NAME ,
		OM_PF_PRODUCT_TMP.PF_PRODUCT_URL ,
		OM_PF_PRODUCT_TMP.PF_UPSELLABLE_PRODUCT_IND ,
		OM_PF_PRODUCT_TMP.PF_ITEM_MOVEMENT_REPORT_BY ,
		OM_PF_PRODUCT_TMP.PF_PRODUCT_BINDING_RULE ,
		OM_PF_PRODUCT_TMP.PF_UPLOAD_IMG_IND,
		OM_PRODUCT_TYPE.SYS_PRODUCT_TYPE_ID,
		NVL(OM_PF_PRODUCT_EXTN_TMP.PF_PRINTS_IN_PACK,0) PF_PRINTS_IN_PACK,
		TRIM(OM_PF_PRODUCT_EXTN_TMP.PF_PRINTING_TYPE) PF_PRINTING_TYPE,
		Om_Product.Sys_Product_Id
		From Om_Pf_Product_Tmp Join Om_Product On Om_Pf_Product_Tmp.Pf_Product_Id = Om_Product.Product_Nbr
		Join Om_Product_Type On Trim(Om_Pf_Product_Tmp.Pf_Product_Type_Ind) = Trim(Om_Product_Type.Product_Type)
  		LEFT OUTER JOIN OM_PF_PRODUCT_EXTN_TMP ON OM_PF_PRODUCT_TMP.PF_PRODUCT_ID = OM_PF_PRODUCT_EXTN_TMP.PF_PRODUCT_ID;
  -- DEFINE THE RECORD
  rec_product_attr c_product_attr%ROWTYPE;
  -- DEFINE VARIABLES
  v_product_id OM_PRODUCT_ATTRIBUTE.SYS_PRODUCT_ATTR_ID%TYPE:= 0;
  v_blank VARCHAR2(10):= ' ' ;
  v_deleted_ind OM_PF_PRODUCT_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 0;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_product_attr;
  LOOP
    FETCH c_product_attr INTO rec_product_attr;
    EXIT WHEN c_product_attr%NOTFOUND OR c_product_attr%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Product ID=' || rec_product_attr.PF_PRODUCT_ID );
    BEGIN
	v_active_cd := 0;
	
	-- Check Active Ind
	IF NVL(rec_product_attr.PF_ACTIVE_IND,'N') = 'Y' THEN
		v_active_cd := 1;
	END IF;
	
	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
	IF (rec_product_attr.PF_TRANSFER_IND IS NOT NULL AND upper(rec_product_attr.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
		v_active_cd := 0;
	END IF;
	
	-- CHECK IF RECORD EXISTS
        SELECT SYS_PRODUCT_ATTR_ID INTO v_product_id FROM OM_PRODUCT_ATTRIBUTE WHERE SYS_PRODUCT_ID = rec_product_attr.SYS_PRODUCT_ID;
				
        -- UPDATE PRODUCT
		UPDATE OM_PRODUCT_ATTRIBUTE 
		SET SYS_PRODUCT_ID = rec_product_attr.SYS_PRODUCT_ID,
		 MULTI_IMAGE_CD = 0,
		 ADDITIONAL_SET_WIC = rec_product_attr.PF_SECOND_WIC,
		 ADDITIONAL_SET_UPC = rec_product_attr.PF_SECOND_UPC,
		 PRINTER_FILE_CREATION_TYPE = rec_product_attr.PF_HOT_FOLDER_PRODUCT,
		 INDEX_INCLUDED_CD = DECODE(UPPER(rec_product_attr.PF_INDEX_INCL_IND),'Y',1,'N',0),
		 UPLOAD_IMG_CD = DECODE(UPPER(rec_product_attr.PF_UPLOAD_IMG_IND),'Y',1,'N',0),
		 PRINTING_TYPE = rec_product_attr.PF_PRINTING_TYPE,
		 TAX_CODE = v_blank,
		 CREATE_USER_ID = rec_product_attr.ID_CREATED,
		 CREATE_DTTM = rec_product_attr.DATE_TIME_CREATED,
		 UPDATE_USER_ID = rec_product_attr.ID_MODIFIED,
		 UPDATE_DTTM = rec_product_attr.DATE_TIME_MODIFIED,
		 ACTIVE_CD = v_active_cd 
		WHERE SYS_PRODUCT_ATTR_ID = v_product_id;

		--DBMS_OUTPUT.PUT_LINE ('Updated Product=' || 'Product ID=' || rec_product_attr.PF_PRODUCT_ID );     
        EXCEPTION
        -- INSERT PRODUCT
        WHEN NO_DATA_FOUND THEN
		BEGIN
        
		INSERT INTO OM_PRODUCT_ATTRIBUTE 
			( 
			 SYS_PRODUCT_ATTR_ID ,
			 SYS_PRODUCT_ID ,
			 MULTI_IMAGE_CD ,
			 ADDITIONAL_SET_WIC ,
			 ADDITIONAL_SET_UPC ,
			 PRINTER_FILE_CREATION_TYPE ,
			 INDEX_INCLUDED_CD ,
			 UPLOAD_IMG_CD ,
			 PRINTING_TYPE ,
			 TAX_CODE ,
			 CREATE_USER_ID ,
			 CREATE_DTTM ,
			 UPDATE_USER_ID ,
			 UPDATE_DTTM ,
			 ACTIVE_CD
			 ) 
			 VALUES 
			 (
			     OM_PRODUCT_ATTRIBUTE_SEQ.NEXTVAL,
				 rec_product_attr.SYS_PRODUCT_ID,
				 0,
				 rec_product_attr.PF_SECOND_WIC,
				 rec_product_attr.PF_SECOND_UPC,
				 rec_product_attr.PF_HOT_FOLDER_PRODUCT,
                 DECODE(UPPER(rec_product_attr.PF_INDEX_INCL_IND),'Y',1,'N',0),
				 DECODE(UPPER(rec_product_attr.PF_UPLOAD_IMG_IND),'Y',1,'N',0),
				 rec_product_attr.PF_PRINTING_TYPE,
                 v_blank,
				 rec_product_attr.ID_CREATED,
				 rec_product_attr.DATE_TIME_CREATED,
				 rec_product_attr.ID_MODIFIED,
				 rec_product_attr.DATE_TIME_MODIFIED,
				 v_active_cd
			 );

        --DBMS_OUTPUT.PUT_LINE ('Added Product=' || 'Product ID=' || rec_product_attr.PF_PRODUCT_ID ); 
	    EXCEPTION	
		WHEN OTHERS THEN
			v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
			--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
			
			INSERT INTO OM_PF_PRODUCT_BAD 
			( 
				 PF_PRODUCT_ID ,
				 PF_SERVICE_CATEGORY_ID ,
				 PF_PRODUCT_OUTPUT_ID ,
				 PF_PRODUCT_FAMILY_ID ,
				 PF_PROCESSING_TYPE_ID ,
				 PF_LONG_DESC ,
				 PF_LABEL_DESC ,
				 PF_WIC ,
				 PF_UPC ,
				 PF_SECOND_WIC ,
				 PF_SECOND_UPC ,
				 PF_PRODUCT_TYPE_IND ,
				 PF_STORE_SUGGEST_IND ,
				 PF_ADDITIONAL_ATTR_IND ,
				 PF_SKIPPABLE_PRODUCT_IND ,
				 PF_CALL_VENDOR_IND ,
				 PF_PRODUCT_CODE ,
				 PF_CUSTOMER_HAS ,
				 PF_CUSTOMER_WANT ,
				 PF_PRODUCT_CATEGORY ,
				 PF_HOT_FOLDER_PRODUCT ,
				 PF_EMPTY_CARTRIDGE_IND ,
				 PF_HAS_EXTRA_IND ,
				 PF_COMMERCIAL_DISCNT_IND ,
				 PF_ACTIVE_IND ,
				 PF_DEACTIVATION_DTTM ,
				 PF_TRANSFER_IND ,
				 ID_CREATED ,
				 DATE_TIME_CREATED ,
				 ID_MODIFIED ,
				 DATE_TIME_MODIFIED ,
				 PF_INDEX_INCL_IND ,
				 PF_BUTTON_NAME ,
				 PF_PRODUCT_URL ,
				 PF_UPSELLABLE_PRODUCT_IND ,
				 PF_ITEM_MOVEMENT_REPORT_BY ,
				 PF_PRODUCT_BINDING_RULE ,
				 PF_UPLOAD_IMG_IND ,
				 EXCEPTION_CODE ,
				 EXCEPTION_MSSG ,
				 EXCEPTION_DTTM 
			 ) 
			VALUES  
			(  
				 rec_product_attr.PF_PRODUCT_ID ,
				 rec_product_attr.PF_SERVICE_CATEGORY_ID ,
				 rec_product_attr.PF_PRODUCT_OUTPUT_ID ,
				 rec_product_attr.PF_PRODUCT_FAMILY_ID ,
				 rec_product_attr.PF_PROCESSING_TYPE_ID ,
				 rec_product_attr.PF_LONG_DESC ,
				 rec_product_attr.PF_LABEL_DESC ,
				 rec_product_attr.PF_WIC ,
				 rec_product_attr.PF_UPC ,
				 rec_product_attr.PF_SECOND_WIC ,
				 rec_product_attr.PF_SECOND_UPC ,
				 rec_product_attr.PF_PRODUCT_TYPE_IND ,
				 rec_product_attr.PF_STORE_SUGGEST_IND ,
				 rec_product_attr.PF_ADDITIONAL_ATTR_IND ,
				 rec_product_attr.PF_SKIPPABLE_PRODUCT_IND ,
				 rec_product_attr.PF_CALL_VENDOR_IND ,
				 rec_product_attr.PF_PRODUCT_CODE ,
				 rec_product_attr.PF_CUSTOMER_HAS ,
				 rec_product_attr.PF_CUSTOMER_WANT ,
				 rec_product_attr.PF_PRODUCT_CATEGORY ,
				 rec_product_attr.PF_HOT_FOLDER_PRODUCT ,
				 rec_product_attr.PF_EMPTY_CARTRIDGE_IND ,
				 rec_product_attr.PF_HAS_EXTRA_IND ,
				 rec_product_attr.PF_COMMERCIAL_DISCNT_IND ,
				 rec_product_attr.PF_ACTIVE_IND ,
				 rec_product_attr.PF_DEACTIVATION_DTTM ,
				 rec_product_attr.PF_TRANSFER_IND ,
				 rec_product_attr.ID_CREATED ,
				 rec_product_attr.DATE_TIME_CREATED ,
				 rec_product_attr.ID_MODIFIED ,
				 rec_product_attr.DATE_TIME_MODIFIED ,
				 rec_product_attr.PF_INDEX_INCL_IND ,
				 rec_product_attr.PF_BUTTON_NAME ,
				 rec_product_attr.PF_PRODUCT_URL ,
				 rec_product_attr.PF_UPSELLABLE_PRODUCT_IND ,
				 rec_product_attr.PF_ITEM_MOVEMENT_REPORT_BY ,
				 rec_product_attr.PF_PRODUCT_BINDING_RULE ,
				 rec_product_attr.PF_UPLOAD_IMG_IND ,
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
			INSERT INTO OM_PF_PRODUCT_BAD 
			( 
				 PF_PRODUCT_ID ,
				 PF_SERVICE_CATEGORY_ID ,
				 PF_PRODUCT_OUTPUT_ID ,
				 PF_PRODUCT_FAMILY_ID ,
				 PF_PROCESSING_TYPE_ID ,
				 PF_LONG_DESC ,
				 PF_LABEL_DESC ,
				 PF_WIC ,
				 PF_UPC ,
				 PF_SECOND_WIC ,
				 PF_SECOND_UPC ,
				 PF_PRODUCT_TYPE_IND ,
				 PF_STORE_SUGGEST_IND ,
				 PF_ADDITIONAL_ATTR_IND ,
				 PF_SKIPPABLE_PRODUCT_IND ,
				 PF_CALL_VENDOR_IND ,
				 PF_PRODUCT_CODE ,
				 PF_CUSTOMER_HAS ,
				 PF_CUSTOMER_WANT ,
				 PF_PRODUCT_CATEGORY ,
				 PF_HOT_FOLDER_PRODUCT ,
				 PF_EMPTY_CARTRIDGE_IND ,
				 PF_HAS_EXTRA_IND ,
				 PF_COMMERCIAL_DISCNT_IND ,
				 PF_ACTIVE_IND ,
				 PF_DEACTIVATION_DTTM ,
				 PF_TRANSFER_IND ,
				 ID_CREATED ,
				 DATE_TIME_CREATED ,
				 ID_MODIFIED ,
				 DATE_TIME_MODIFIED ,
				 PF_INDEX_INCL_IND ,
				 PF_BUTTON_NAME ,
				 PF_PRODUCT_URL ,
				 PF_UPSELLABLE_PRODUCT_IND ,
				 PF_ITEM_MOVEMENT_REPORT_BY ,
				 PF_PRODUCT_BINDING_RULE ,
				 PF_UPLOAD_IMG_IND ,
				 EXCEPTION_CODE ,
				 EXCEPTION_MSSG ,
				 EXCEPTION_DTTM 
			 ) 
			VALUES  
			(  
				rec_product_attr.PF_PRODUCT_ID ,
				 rec_product_attr.PF_SERVICE_CATEGORY_ID ,
				 rec_product_attr.PF_PRODUCT_OUTPUT_ID ,
				 rec_product_attr.PF_PRODUCT_FAMILY_ID ,
				 rec_product_attr.PF_PROCESSING_TYPE_ID ,
				 rec_product_attr.PF_LONG_DESC ,
				 rec_product_attr.PF_LABEL_DESC ,
				 rec_product_attr.PF_WIC ,
				 rec_product_attr.PF_UPC ,
				 rec_product_attr.PF_SECOND_WIC ,
				 rec_product_attr.PF_SECOND_UPC ,
				 rec_product_attr.PF_PRODUCT_TYPE_IND ,
				 rec_product_attr.PF_STORE_SUGGEST_IND ,
				 rec_product_attr.PF_ADDITIONAL_ATTR_IND ,
				 rec_product_attr.PF_SKIPPABLE_PRODUCT_IND ,
				 rec_product_attr.PF_CALL_VENDOR_IND ,
				 rec_product_attr.PF_PRODUCT_CODE ,
				 rec_product_attr.PF_CUSTOMER_HAS ,
				 rec_product_attr.PF_CUSTOMER_WANT ,
				 rec_product_attr.PF_PRODUCT_CATEGORY ,
				 rec_product_attr.PF_HOT_FOLDER_PRODUCT ,
				 rec_product_attr.PF_EMPTY_CARTRIDGE_IND ,
				 rec_product_attr.PF_HAS_EXTRA_IND ,
				 rec_product_attr.PF_COMMERCIAL_DISCNT_IND ,
				 rec_product_attr.PF_ACTIVE_IND ,
				 rec_product_attr.PF_DEACTIVATION_DTTM ,
				 rec_product_attr.PF_TRANSFER_IND ,
				 rec_product_attr.ID_CREATED ,
				 rec_product_attr.DATE_TIME_CREATED ,
				 rec_product_attr.ID_MODIFIED ,
				 rec_product_attr.DATE_TIME_MODIFIED ,
				 rec_product_attr.PF_INDEX_INCL_IND ,
				 rec_product_attr.PF_BUTTON_NAME ,
				 rec_product_attr.PF_PRODUCT_URL ,
				 rec_product_attr.PF_UPSELLABLE_PRODUCT_IND ,
				 rec_product_attr.PF_ITEM_MOVEMENT_REPORT_BY ,
				 rec_product_attr.PF_PRODUCT_BINDING_RULE ,
				 rec_product_attr.PF_UPLOAD_IMG_IND ,
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
  INSERT INTO OM_PF_PRODUCT_BAD 
			( 
				 PF_PRODUCT_ID ,
				 PF_SERVICE_CATEGORY_ID ,
				 PF_PRODUCT_OUTPUT_ID ,
				 PF_PRODUCT_FAMILY_ID ,
				 PF_PROCESSING_TYPE_ID ,
				 PF_LONG_DESC ,
				 PF_LABEL_DESC ,
				 PF_WIC ,
				 PF_UPC ,
				 PF_SECOND_WIC ,
				 PF_SECOND_UPC ,
				 PF_PRODUCT_TYPE_IND ,
				 PF_STORE_SUGGEST_IND ,
				 PF_ADDITIONAL_ATTR_IND ,
				 PF_SKIPPABLE_PRODUCT_IND ,
				 PF_CALL_VENDOR_IND ,
				 PF_PRODUCT_CODE ,
				 PF_CUSTOMER_HAS ,
				 PF_CUSTOMER_WANT ,
				 PF_PRODUCT_CATEGORY ,
				 PF_HOT_FOLDER_PRODUCT ,
				 PF_EMPTY_CARTRIDGE_IND ,
				 PF_HAS_EXTRA_IND ,
				 PF_COMMERCIAL_DISCNT_IND ,
				 PF_ACTIVE_IND ,
				 PF_DEACTIVATION_DTTM ,
				 PF_TRANSFER_IND ,
				 ID_CREATED ,
				 DATE_TIME_CREATED ,
				 ID_MODIFIED ,
				 DATE_TIME_MODIFIED ,
				 PF_INDEX_INCL_IND ,
				 PF_BUTTON_NAME ,
				 PF_PRODUCT_URL ,
				 PF_UPSELLABLE_PRODUCT_IND ,
				 PF_ITEM_MOVEMENT_REPORT_BY ,
				 PF_PRODUCT_BINDING_RULE ,
				 PF_UPLOAD_IMG_IND ,
				 EXCEPTION_CODE ,
				 EXCEPTION_MSSG ,
				 EXCEPTION_DTTM 
			 ) SELECT 
			    PRODUCT.PF_PRODUCT_ID ,
				PRODUCT.PF_SERVICE_CATEGORY_ID ,
				PRODUCT.PF_PRODUCT_OUTPUT_ID ,
				PRODUCT.PF_PRODUCT_FAMILY_ID ,
				PRODUCT.PF_PROCESSING_TYPE_ID ,
				PRODUCT.PF_LONG_DESC ,
				PRODUCT.PF_LABEL_DESC ,
				PRODUCT.PF_WIC ,
				PRODUCT.PF_UPC ,
				PRODUCT.PF_SECOND_WIC ,
				PRODUCT.PF_SECOND_UPC ,
				PRODUCT.PF_PRODUCT_TYPE_IND ,
				PRODUCT.PF_STORE_SUGGEST_IND ,
				PRODUCT.PF_ADDITIONAL_ATTR_IND ,
				PRODUCT.PF_SKIPPABLE_PRODUCT_IND ,
				PRODUCT.PF_CALL_VENDOR_IND ,
				PRODUCT.PF_PRODUCT_CODE ,
				PRODUCT.PF_CUSTOMER_HAS ,
				PRODUCT.PF_CUSTOMER_WANT ,
				PRODUCT.PF_PRODUCT_CATEGORY ,
				PRODUCT.PF_HOT_FOLDER_PRODUCT ,
				PRODUCT.PF_EMPTY_CARTRIDGE_IND ,
				PRODUCT.PF_HAS_EXTRA_IND ,
				PRODUCT.PF_COMMERCIAL_DISCNT_IND ,
				PRODUCT.PF_ACTIVE_IND ,
				PRODUCT.PF_DEACTIVATION_DTTM ,
				PRODUCT.PF_TRANSFER_IND ,
				PRODUCT.ID_CREATED ,
				PRODUCT.DATE_TIME_CREATED ,
				PRODUCT.ID_MODIFIED ,
				PRODUCT.DATE_TIME_MODIFIED ,
				PRODUCT.PF_INDEX_INCL_IND ,
				PRODUCT.PF_BUTTON_NAME ,
				PRODUCT.PF_PRODUCT_URL ,
				PRODUCT.PF_UPSELLABLE_PRODUCT_IND ,
				PRODUCT.PF_ITEM_MOVEMENT_REPORT_BY ,
				PRODUCT.PF_PRODUCT_BINDING_RULE ,
				PRODUCT.PF_UPLOAD_IMG_IND
			    , '000' 
			    ,'Master records not found' 
			    ,SYSDATE
			 FROM OM_PF_PRODUCT_TMP PRODUCT
			 WHERE NOT EXISTS
				( SELECT 1
				  FROM (SELECT Om_Pf_Product_Tmp.pf_product_id 
				  From Om_Pf_Product_Tmp Join Om_Product_Type 
				      On Trim(Om_Pf_Product_Tmp.Pf_Product_Type_Ind) = Trim(Om_Product_Type.Product_Type)  
      				      Left Outer Join Om_Pf_Product_Extn_Tmp On Om_Pf_Product_Tmp.Pf_Product_Id = Om_Pf_Product_Extn_Tmp.Pf_Product_Id ) a, OM_PRODUCT
				  WHERE a.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR				  
				  AND a.PF_PRODUCT_ID = PRODUCT.PF_PRODUCT_ID
				);
END;
/
