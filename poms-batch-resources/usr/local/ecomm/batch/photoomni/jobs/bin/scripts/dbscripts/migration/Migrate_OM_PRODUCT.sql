SET SERVEROUTPUT ON;
-- UPDATE / ADD Downtime Reason IN OM_PF_PRODUCT_TMP

DECLARE
  CURSOR c_product
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
		nvl(Om_Pf_Product_Extn_Tmp.Pf_Prints_In_Pack,0) Pf_Prints_In_Pack
	From Om_Pf_Product_Tmp Join Om_Product_Type 
	    On Trim(Om_Pf_Product_Tmp.Pf_Product_Type_Ind) = Trim(Om_Product_Type.Product_Type)  
      LEFT OUTER JOIN OM_PF_PRODUCT_EXTN_TMP ON OM_PF_PRODUCT_TMP.PF_PRODUCT_ID = OM_PF_PRODUCT_EXTN_TMP.PF_PRODUCT_ID;

  -- DEFINE THE RECORD
  rec_product c_product%ROWTYPE;
  -- DEFINE VARIABLES
  v_product_id OM_PRODUCT.SYS_PRODUCT_ID%TYPE:= 0;
  v_dimension_id OM_PRODUCT.SYS_PRODUCT_DIMENSION_ID%TYPE:= 0;
  v_blank VARCHAR2(10) := ' ';
  v_deleted_ind OM_PF_PRODUCT_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 0;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  
  OPEN c_product;
  LOOP
    FETCH c_product INTO rec_product;
    EXIT WHEN c_product%NOTFOUND OR c_product%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Product ID=' || rec_product.PF_PRODUCT_ID );
    BEGIN
    
	v_active_cd := 0;
	
	-- Check Active Ind
	IF NVL(rec_product.PF_ACTIVE_IND,'N') = 'Y' THEN
		v_active_cd := 1;
	END IF;
	
	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
	IF (rec_product.PF_TRANSFER_IND IS NOT NULL AND upper(rec_product.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
		v_active_cd := 0;
	END IF;
	
        -- CHECK IF RECORD EXISTS
        SELECT SYS_PRODUCT_ID INTO v_product_id FROM OM_PRODUCT WHERE PRODUCT_NBR = rec_product.PF_PRODUCT_ID;
				
        -- UPDATE PRODUCT
		UPDATE OM_PRODUCT 
		 SET PRODUCT_NBR = rec_product.PF_PRODUCT_ID,
			 DESCRIPTION = rec_product.PF_LONG_DESC,
			 SYS_PRODUCT_TYPE_ID = rec_product.SYS_PRODUCT_TYPE_ID,
			 SYS_PRODUCT_DIMENSION_ID = v_dimension_id,
			 WIC = rec_product.PF_WIC,
			 SKU = v_blank,
			 UPC = rec_product.PF_UPC,
			 PACK_SIZE = rec_product.PF_PRINTS_IN_PACK,
			 ACTIVE_CD = v_active_cd,
			 ACTIVATION_DTTM = rec_product.DATE_TIME_CREATED,
			 DEACTIVATION_DTTM = rec_product.PF_DEACTIVATION_DTTM,
			 CREATE_USER_ID = rec_product.ID_CREATED,
			 CREATE_DTTM = rec_product.DATE_TIME_CREATED,
			 UPDATE_USER_ID = rec_product.ID_MODIFIED,
			 UPDATE_DTTM = rec_product.DATE_TIME_MODIFIED
		 WHERE SYS_PRODUCT_ID = v_product_id ;

		--DBMS_OUTPUT.PUT_LINE ('Updated Product=' || 'Product ID=' || rec_product.PF_PRODUCT_ID );     
        EXCEPTION
        -- INSERT PRODUCT
        WHEN NO_DATA_FOUND THEN
		BEGIN
        
		INSERT INTO 
			OM_PRODUCT 
			( 
				 SYS_PRODUCT_ID ,
				 PRODUCT_NBR ,
				 DESCRIPTION ,
				 SYS_PRODUCT_TYPE_ID ,
				 SYS_PRODUCT_DIMENSION_ID ,
				 WIC ,
				 SKU ,
				 UPC ,
				 PACK_SIZE ,
				 ACTIVE_CD ,
				 ACTIVATION_DTTM ,
				 DEACTIVATION_DTTM ,
				 CREATE_USER_ID ,
				 CREATE_DTTM ,
				 UPDATE_USER_ID ,
				 UPDATE_DTTM
			 ) 
			 VALUES 
			 (
			     OM_PRODUCT_SEQ.NEXTVAL,
				 rec_product.PF_PRODUCT_ID,
				 rec_product.PF_LONG_DESC,
				 rec_product.SYS_PRODUCT_TYPE_ID,
				 v_dimension_id,
				 rec_product.PF_WIC,
                 v_blank,
				 rec_product.PF_UPC,
				 rec_product.PF_PRINTS_IN_PACK,
                 v_active_cd,
				 rec_product.DATE_TIME_CREATED,
				 rec_product.PF_DEACTIVATION_DTTM,
				 rec_product.ID_CREATED,
				 rec_product.DATE_TIME_CREATED,
				 rec_product.ID_MODIFIED,
				 rec_product.DATE_TIME_MODIFIED

			 );

        --DBMS_OUTPUT.PUT_LINE ('Added Product=' || 'Product ID=' || rec_product.PF_PRODUCT_ID ); 
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
				 rec_product.PF_PRODUCT_ID ,
				 rec_product.PF_SERVICE_CATEGORY_ID ,
				 rec_product.PF_PRODUCT_OUTPUT_ID ,
				 rec_product.PF_PRODUCT_FAMILY_ID ,
				 rec_product.PF_PROCESSING_TYPE_ID ,
				 rec_product.PF_LONG_DESC ,
				 rec_product.PF_LABEL_DESC ,
				 rec_product.PF_WIC ,
				 rec_product.PF_UPC ,
				 rec_product.PF_SECOND_WIC ,
				 rec_product.PF_SECOND_UPC ,
				 rec_product.PF_PRODUCT_TYPE_IND ,
				 rec_product.PF_STORE_SUGGEST_IND ,
				 rec_product.PF_ADDITIONAL_ATTR_IND ,
				 rec_product.PF_SKIPPABLE_PRODUCT_IND ,
				 rec_product.PF_CALL_VENDOR_IND ,
				 rec_product.PF_PRODUCT_CODE ,
				 rec_product.PF_CUSTOMER_HAS ,
				 rec_product.PF_CUSTOMER_WANT ,
				 rec_product.PF_PRODUCT_CATEGORY ,
				 rec_product.PF_HOT_FOLDER_PRODUCT ,
				 rec_product.PF_EMPTY_CARTRIDGE_IND ,
				 rec_product.PF_HAS_EXTRA_IND ,
				 rec_product.PF_COMMERCIAL_DISCNT_IND ,
				 rec_product.PF_ACTIVE_IND ,
				 rec_product.PF_DEACTIVATION_DTTM ,
				 rec_product.PF_TRANSFER_IND ,
				 rec_product.ID_CREATED ,
				 rec_product.DATE_TIME_CREATED ,
				 rec_product.ID_MODIFIED ,
				 rec_product.DATE_TIME_MODIFIED ,
				 rec_product.PF_INDEX_INCL_IND ,
				 rec_product.PF_BUTTON_NAME ,
				 rec_product.PF_PRODUCT_URL ,
				 rec_product.PF_UPSELLABLE_PRODUCT_IND ,
				 rec_product.PF_ITEM_MOVEMENT_REPORT_BY ,
				 rec_product.PF_PRODUCT_BINDING_RULE ,
				 rec_product.PF_UPLOAD_IMG_IND ,
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
				 rec_product.PF_PRODUCT_ID ,
				 rec_product.PF_SERVICE_CATEGORY_ID ,
				 rec_product.PF_PRODUCT_OUTPUT_ID ,
				 rec_product.PF_PRODUCT_FAMILY_ID ,
				 rec_product.PF_PROCESSING_TYPE_ID ,
				 rec_product.PF_LONG_DESC ,
				 rec_product.PF_LABEL_DESC ,
				 rec_product.PF_WIC ,
				 rec_product.PF_UPC ,
				 rec_product.PF_SECOND_WIC ,
				 rec_product.PF_SECOND_UPC ,
				 rec_product.PF_PRODUCT_TYPE_IND ,
				 rec_product.PF_STORE_SUGGEST_IND ,
				 rec_product.PF_ADDITIONAL_ATTR_IND ,
				 rec_product.PF_SKIPPABLE_PRODUCT_IND ,
				 rec_product.PF_CALL_VENDOR_IND ,
				 rec_product.PF_PRODUCT_CODE ,
				 rec_product.PF_CUSTOMER_HAS ,
				 rec_product.PF_CUSTOMER_WANT ,
				 rec_product.PF_PRODUCT_CATEGORY ,
				 rec_product.PF_HOT_FOLDER_PRODUCT ,
				 rec_product.PF_EMPTY_CARTRIDGE_IND ,
				 rec_product.PF_HAS_EXTRA_IND ,
				 rec_product.PF_COMMERCIAL_DISCNT_IND ,
				 rec_product.PF_ACTIVE_IND ,
				 rec_product.PF_DEACTIVATION_DTTM ,
				 rec_product.PF_TRANSFER_IND ,
				 rec_product.ID_CREATED ,
				 rec_product.DATE_TIME_CREATED ,
				 rec_product.ID_MODIFIED ,
				 rec_product.DATE_TIME_MODIFIED ,
				 rec_product.PF_INDEX_INCL_IND ,
				 rec_product.PF_BUTTON_NAME ,
				 rec_product.PF_PRODUCT_URL ,
				 rec_product.PF_UPSELLABLE_PRODUCT_IND ,
				 rec_product.PF_ITEM_MOVEMENT_REPORT_BY ,
				 rec_product.PF_PRODUCT_BINDING_RULE ,
				 rec_product.PF_UPLOAD_IMG_IND ,
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
				( 
        			Select 1 
        			from (SELECT Om_Pf_Product_Tmp.pf_product_id 
				  From Om_Pf_Product_Tmp Join Om_Product_Type 
				      On Trim(Om_Pf_Product_Tmp.Pf_Product_Type_Ind) = Trim(Om_Product_Type.Product_Type)  
      				      Left Outer Join Om_Pf_Product_Extn_Tmp On Om_Pf_Product_Tmp.Pf_Product_Id = Om_Pf_Product_Extn_Tmp.Pf_Product_Id ) A 
        		            where a.pf_product_id = Product.Pf_Product_Id);
END;
/
