SET SERVEROUTPUT ON;
-- UPDATE / ADD PHOTO IN OM_PHOTO_PRICING
DECLARE
  CURSOR c_photo_pricing
  IS  
 
 	SELECT 	OM_PF_PROD_RET_LVL_PRICE_TMP.PF_RETAIL_LEVEL_ID ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_PRODUCT_ID ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_MINIMUM_TIER_QTY ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_MAXIMUM_TIER_QTY ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_TIER_TYPE ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_GUARANTEED_IND ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_DEFAULT_ORDER_QTY ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_ESTIM_RETAIL ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_DEVELOPMENT_RETAIL ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_PRINT_RETAIL ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_PANORAMIC_PRINT_RETAIL ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_FUDGE_FACTOR ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_MINIMUM_PRODUCT_RETAIL ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_MAXIMUM_PRODUCT_RETAIL ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_MINIMUM_ORDER_QTY ,
		    DECODE(UPPER(OM_PF_PROD_RET_LVL_PRICE_TMP.PF_RETAIL_PRICE_BASED),'INPUT','I','OUTPUT','O') PF_RETAIL_PRICE_BASED ,			
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_TRANSFER_IND ,
		    NVL(OM_PF_PROD_RET_LVL_PRICE_TMP.ID_CREATED,' ') ID_CREATED ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.DATE_TIME_CREATED ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.ID_MODIFIED ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.DATE_TIME_MODIFIED ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_ACTIVE_IND ,
		    OM_PF_PROD_RET_LVL_PRICE_TMP.PF_DEACTIVATION_DTTM ,
		    OM_PRICE_LEVEL.SYS_PRICE_LEVEL_ID ,
		    OM_PRODUCT.SYS_PRODUCT_ID 
    FROM OM_PF_PROD_RET_LVL_PRICE_TMP, OM_PRICE_LEVEL, OM_PRODUCT
    WHERE OM_PF_PROD_RET_LVL_PRICE_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR
    AND OM_PF_PROD_RET_LVL_PRICE_TMP.PF_RETAIL_LEVEL_ID=OM_PRICE_LEVEL.PRICE_LEVEL;
 
  -- DEFINE THE RECORD
  rec_photo_pricing c_photo_pricing%ROWTYPE;
  -- DEFINE VARIABLES
  v_sysphotopricingid OM_PHOTO_PRICING.SYS_PRICE_ID%TYPE;
  v_deleted_ind OM_PF_PROD_RET_LVL_PRICE_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_currency_cd VARCHAR2(3) := 'USD';
  v_active_cd NUMBER := 0;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';

BEGIN

 OPEN c_photo_pricing;
 LOOP
    FETCH c_photo_pricing INTO rec_photo_pricing;
    EXIT WHEN c_photo_pricing%NOTFOUND OR c_photo_pricing%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'RETAIL LEVEL ID=' || rec_photo_pricing.PF_RETAIL_LEVEL_ID || 'tier type=' ||  rec_photo_pricing.PF_TIER_TYPE);
  BEGIN
  
  	v_active_cd := 0;
  	
  	-- Check Active Ind
  	IF NVL(rec_photo_pricing.PF_ACTIVE_IND,'N') = 'Y' THEN
  		v_active_cd := 1;
  	END IF;
  	
  	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
  	IF (rec_photo_pricing.PF_TRANSFER_IND IS NOT NULL AND upper(rec_photo_pricing.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
  		v_active_cd := 0;
	END IF;
   
 	-- CHECK IF RECORD EXISTS
   	SELECT SYS_PRICE_ID INTO v_sysphotopricingid FROM OM_PHOTO_PRICING WHERE SYS_PRODUCT_ID=rec_photo_pricing.SYS_PRODUCT_ID and SYS_PRICE_LEVEL_ID=rec_photo_pricing.SYS_PRICE_LEVEL_ID and TIER_MIN_QTY=rec_photo_pricing.PF_MINIMUM_TIER_QTY;
          
	-- UPDATE PHOTO PRICING    
   	    UPDATE OM_PHOTO_PRICING 
   	    SET SYS_PRICE_LEVEL_ID =rec_photo_pricing.SYS_PRICE_LEVEL_ID, 
		    SYS_PRODUCT_ID =rec_photo_pricing.SYS_PRODUCT_ID,
		    TIER_MIN_QTY =rec_photo_pricing.PF_MINIMUM_TIER_QTY,
		    TIER_MAX_QTY =rec_photo_pricing.PF_MAXIMUM_TIER_QTY,
		    TIER_TYPE =rec_photo_pricing.PF_TIER_TYPE,
		    ESTIMATED_PRICE =rec_photo_pricing.PF_ESTIM_RETAIL ,
		    PRINT_PRICE =rec_photo_pricing.PF_PRINT_RETAIL ,
		    PANOROMIC_PRINT_PRICE =rec_photo_pricing.PF_PANORAMIC_PRINT_RETAIL ,
		    DEVELOPMENT_PRINT_PRICE =rec_photo_pricing.PF_DEVELOPMENT_RETAIL ,
		    CURRENCY_CODE_ID = v_currency_cd ,
		    FUDGE_FACTOR =rec_photo_pricing.PF_FUDGE_FACTOR ,
		    MINIMUM_PRICE =rec_photo_pricing.PF_MINIMUM_PRODUCT_RETAIL ,
		    MAXIMUM_PRICE =rec_photo_pricing.PF_MAXIMUM_PRODUCT_RETAIL ,
		    MINIMUM_ORDER_QTY =rec_photo_pricing.PF_MINIMUM_ORDER_QTY ,
		    RETAIL_PRICE_BASED =rec_photo_pricing.PF_RETAIL_PRICE_BASED ,
			ACTIVATION_DTTM = rec_photo_pricing.DATE_TIME_CREATED , 
			DEACTIVATION_DTTM = rec_photo_pricing.PF_DEACTIVATION_DTTM ,
		    CREATE_USER_ID = rec_photo_pricing.ID_CREATED , 
		    CREATE_DTTM = rec_photo_pricing.DATE_TIME_CREATED , 
		    UPDATE_USER_ID = rec_photo_pricing.ID_MODIFIED , 
		    UPDATE_DTTM = rec_photo_pricing.DATE_TIME_MODIFIED , 
		    ACTIVE_CD = v_active_cd 
   	   WHERE SYS_PRICE_ID = v_sysphotopricingid;

   	   --DBMS_OUTPUT.PUT_LINE ('Updated SYS_PRICE_ID:' || v_sysphotopricingid);          
  EXCEPTION
   -- INSERT PHOTO PRICING
   WHEN NO_DATA_FOUND THEN
    	BEGIN
    
     	INSERT INTO OM_PHOTO_PRICING
     	(
	      SYS_PRICE_ID ,
	      SYS_PRICE_LEVEL_ID ,
	      SYS_PRODUCT_ID ,
	      TIER_MIN_QTY ,
	      TIER_MAX_QTY ,
	      TIER_TYPE ,
	      ESTIMATED_PRICE ,
	      PRINT_PRICE ,
	      PANOROMIC_PRINT_PRICE ,
	      DEVELOPMENT_PRINT_PRICE ,
	      CURRENCY_CODE_ID ,
	      FUDGE_FACTOR ,
	      MINIMUM_PRICE ,
	      MAXIMUM_PRICE ,
	      MINIMUM_ORDER_QTY ,
	      RETAIL_PRICE_BASED ,
	      ACTIVATION_DTTM ,
	      DEACTIVATION_DTTM ,
	      ACTIVE_CD ,
	      CREATE_USER_ID ,
	      CREATE_DTTM ,
	      UPDATE_USER_ID ,
	      UPDATE_DTTM
     )
      VALUES
     (
		OM_PHOTO_PRICING_SEQ.NEXTVAL ,
		rec_photo_pricing.SYS_PRICE_LEVEL_ID,
		rec_photo_pricing.SYS_PRODUCT_ID,
		rec_photo_pricing.PF_MINIMUM_TIER_QTY,
		rec_photo_pricing.PF_MAXIMUM_TIER_QTY,
		rec_photo_pricing.PF_TIER_TYPE ,
		rec_photo_pricing.PF_ESTIM_RETAIL ,
		rec_photo_pricing.PF_PRINT_RETAIL ,
		rec_photo_pricing.PF_PANORAMIC_PRINT_RETAIL ,
		rec_photo_pricing.PF_DEVELOPMENT_RETAIL ,
		v_currency_cd ,
		rec_photo_pricing.PF_FUDGE_FACTOR ,
		rec_photo_pricing.PF_MINIMUM_PRODUCT_RETAIL ,
		rec_photo_pricing.PF_MAXIMUM_PRODUCT_RETAIL ,
		rec_photo_pricing.PF_MINIMUM_ORDER_QTY ,
		rec_photo_pricing.PF_RETAIL_PRICE_BASED ,
		rec_photo_pricing.DATE_TIME_CREATED,
		rec_photo_pricing.PF_DEACTIVATION_DTTM,
		v_active_cd,
		rec_photo_pricing.ID_CREATED , 
		rec_photo_pricing.DATE_TIME_CREATED , 
		rec_photo_pricing.ID_MODIFIED , 
		rec_photo_pricing.DATE_TIME_MODIFIED 
	);

--DBMS_OUTPUT.PUT_LINE ('Added SYS_PRICE_ID:' || v_sysphotopricingid);   
     EXCEPTION 
     		WHEN OTHERS THEN
     		v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
     		--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg); 
     
	     INSERT INTO OM_PF_PROD_RET_LVL_PRICE_BAD
	     (
	      PF_RETAIL_LEVEL_ID ,
	      PF_PRODUCT_ID ,
	      PF_MINIMUM_TIER_QTY ,
	      PF_MAXIMUM_TIER_QTY ,
	      PF_TIER_TYPE ,
	      PF_GUARANTEED_IND ,
	      PF_DEFAULT_ORDER_QTY ,
	      PF_ESTIM_RETAIL ,
	      PF_DEVELOPMENT_RETAIL ,
	      PF_PRINT_RETAIL ,
	      PF_PANORAMIC_PRINT_RETAIL ,
	      PF_FUDGE_FACTOR ,
	      PF_MINIMUM_PRODUCT_RETAIL ,
	      PF_MAXIMUM_PRODUCT_RETAIL ,
	      PF_MINIMUM_ORDER_QTY ,
	      PF_RETAIL_PRICE_BASED ,
	      PF_TRANSFER_IND ,
	      ID_CREATED ,
	      DATE_TIME_CREATED ,
	      ID_MODIFIED ,
	      DATE_TIME_MODIFIED ,
	      PF_ACTIVE_IND ,
	      PF_DEACTIVATION_DTTM ,
	      EXCEPTION_CODE ,
	      EXCEPTION_MSSG ,
	      EXCEPTION_DTTM 
		      )
	      VALUES
	      (
	     rec_photo_pricing.PF_RETAIL_LEVEL_ID ,
	     rec_photo_pricing.PF_PRODUCT_ID ,
	     rec_photo_pricing.PF_MINIMUM_TIER_QTY ,
	     rec_photo_pricing.PF_MAXIMUM_TIER_QTY ,
	     rec_photo_pricing.PF_TIER_TYPE ,
	     rec_photo_pricing.PF_GUARANTEED_IND ,
	     rec_photo_pricing.PF_DEFAULT_ORDER_QTY ,
	     rec_photo_pricing.PF_ESTIM_RETAIL ,
	     rec_photo_pricing.PF_DEVELOPMENT_RETAIL ,
	     rec_photo_pricing.PF_PRINT_RETAIL ,
	     rec_photo_pricing.PF_PANORAMIC_PRINT_RETAIL ,
	     rec_photo_pricing.PF_FUDGE_FACTOR ,
	     rec_photo_pricing.PF_MINIMUM_PRODUCT_RETAIL ,
	     rec_photo_pricing.PF_MAXIMUM_PRODUCT_RETAIL ,
	     rec_photo_pricing.PF_MINIMUM_ORDER_QTY ,
	     rec_photo_pricing.PF_RETAIL_PRICE_BASED ,
	     rec_photo_pricing.PF_TRANSFER_IND ,
	     rec_photo_pricing.ID_CREATED ,
	     rec_photo_pricing.DATE_TIME_CREATED ,
	     rec_photo_pricing.ID_MODIFIED ,
	     rec_photo_pricing.DATE_TIME_MODIFIED ,
	     rec_photo_pricing.PF_ACTIVE_IND ,
	     rec_photo_pricing.PF_DEACTIVATION_DTTM ,
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
    
    		INSERT INTO OM_PF_PROD_RET_LVL_PRICE_BAD
      		(
		     PF_RETAIL_LEVEL_ID ,
		     PF_PRODUCT_ID ,
		     PF_MINIMUM_TIER_QTY ,
		     PF_MAXIMUM_TIER_QTY ,
		     PF_TIER_TYPE ,
		     PF_GUARANTEED_IND ,
		     PF_DEFAULT_ORDER_QTY ,
		     PF_ESTIM_RETAIL ,
		     PF_DEVELOPMENT_RETAIL ,
		     PF_PRINT_RETAIL ,
		     PF_PANORAMIC_PRINT_RETAIL ,
		     PF_FUDGE_FACTOR ,
		     PF_MINIMUM_PRODUCT_RETAIL ,
		     PF_MAXIMUM_PRODUCT_RETAIL ,
		     PF_MINIMUM_ORDER_QTY ,
		     PF_RETAIL_PRICE_BASED ,
		     PF_TRANSFER_IND ,
		     ID_CREATED ,
		     DATE_TIME_CREATED ,
		     ID_MODIFIED ,
		     DATE_TIME_MODIFIED ,
		     PF_ACTIVE_IND ,
		     PF_DEACTIVATION_DTTM ,      
		     EXCEPTION_CODE ,
		     EXCEPTION_MSSG ,
		     EXCEPTION_DTTM
      		)
      	  	VALUES
      	 	(
	     	rec_photo_pricing.PF_RETAIL_LEVEL_ID ,
	     	rec_photo_pricing.PF_PRODUCT_ID ,
	     	rec_photo_pricing.PF_MINIMUM_TIER_QTY ,
	     	rec_photo_pricing.PF_MAXIMUM_TIER_QTY ,
	     	rec_photo_pricing.PF_TIER_TYPE ,
	     	rec_photo_pricing.PF_GUARANTEED_IND ,
	     	rec_photo_pricing.PF_DEFAULT_ORDER_QTY ,
	     	rec_photo_pricing.PF_ESTIM_RETAIL ,
	     	rec_photo_pricing.PF_DEVELOPMENT_RETAIL ,
	     	rec_photo_pricing.PF_PRINT_RETAIL ,
	     	rec_photo_pricing.PF_PANORAMIC_PRINT_RETAIL ,
	     	rec_photo_pricing.PF_FUDGE_FACTOR ,
	     	rec_photo_pricing.PF_MINIMUM_PRODUCT_RETAIL ,
	     	rec_photo_pricing.PF_MAXIMUM_PRODUCT_RETAIL ,
	     	rec_photo_pricing.PF_MINIMUM_ORDER_QTY ,
	     	rec_photo_pricing.PF_RETAIL_PRICE_BASED ,
	     	rec_photo_pricing.PF_TRANSFER_IND ,
	     	rec_photo_pricing.ID_CREATED ,
			rec_photo_pricing.DATE_TIME_CREATED ,
	     	rec_photo_pricing.ID_MODIFIED ,
	     	rec_photo_pricing.DATE_TIME_MODIFIED ,
	     	rec_photo_pricing.PF_ACTIVE_IND ,
	     	rec_photo_pricing.PF_DEACTIVATION_DTTM ,
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
 	INSERT INTO OM_PF_PROD_RET_LVL_PRICE_BAD
   	(  
		  PF_RETAIL_LEVEL_ID ,
		  PF_PRODUCT_ID ,
		  PF_MINIMUM_TIER_QTY ,
		  PF_MAXIMUM_TIER_QTY ,
		  PF_TIER_TYPE ,
		  PF_GUARANTEED_IND ,
		  PF_DEFAULT_ORDER_QTY ,
		  PF_ESTIM_RETAIL ,
		  PF_DEVELOPMENT_RETAIL ,
		  PF_PRINT_RETAIL ,
		  PF_PANORAMIC_PRINT_RETAIL ,
		  PF_FUDGE_FACTOR ,
		  PF_MINIMUM_PRODUCT_RETAIL ,
		  PF_MAXIMUM_PRODUCT_RETAIL ,
		  PF_MINIMUM_ORDER_QTY ,
		  PF_RETAIL_PRICE_BASED ,
		  PF_TRANSFER_IND ,
		  ID_CREATED ,
		  DATE_TIME_CREATED ,
		  ID_MODIFIED ,
		  DATE_TIME_MODIFIED ,
		  PF_ACTIVE_IND ,
		  PF_DEACTIVATION_DTTM , 
		  EXCEPTION_CODE ,
		  EXCEPTION_MSSG ,
		  EXCEPTION_DTTM
	   )
	   SELECT 
		   "PHOTO".PF_RETAIL_LEVEL_ID ,
		   "PHOTO".PF_PRODUCT_ID ,
		   "PHOTO".PF_MINIMUM_TIER_QTY ,
		   "PHOTO".PF_MAXIMUM_TIER_QTY ,
		   "PHOTO".PF_TIER_TYPE ,
		   "PHOTO".PF_GUARANTEED_IND ,
		   "PHOTO".PF_DEFAULT_ORDER_QTY ,
		   "PHOTO".PF_ESTIM_RETAIL ,
		   "PHOTO".PF_DEVELOPMENT_RETAIL ,
		   "PHOTO".PF_PRINT_RETAIL ,
		   "PHOTO".PF_PANORAMIC_PRINT_RETAIL ,
		   "PHOTO".PF_FUDGE_FACTOR ,
		   "PHOTO".PF_MINIMUM_PRODUCT_RETAIL ,
		   "PHOTO".PF_MAXIMUM_PRODUCT_RETAIL ,
		   "PHOTO".PF_MINIMUM_ORDER_QTY ,
		   "PHOTO".PF_RETAIL_PRICE_BASED ,
		   "PHOTO".PF_TRANSFER_IND ,
		   "PHOTO".ID_CREATED ,
		   "PHOTO".DATE_TIME_CREATED ,
		   "PHOTO".ID_MODIFIED ,
		   "PHOTO".DATE_TIME_MODIFIED ,
		   "PHOTO".PF_ACTIVE_IND ,
		   "PHOTO".PF_DEACTIVATION_DTTM ,
		   '000' , 
		   'Master records not found' ,
		   SYSDATE
 FROM OM_PF_PROD_RET_LVL_PRICE_TMP "PHOTO"
 WHERE NOT EXISTS
   (SELECT 1 FROM OM_PF_PROD_RET_LVL_PRICE_TMP, OM_PRICE_LEVEL, OM_PRODUCT
     WHERE 
	 OM_PF_PROD_RET_LVL_PRICE_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR
     AND OM_PF_PROD_RET_LVL_PRICE_TMP.PF_RETAIL_LEVEL_ID=OM_PRICE_LEVEL.PRICE_LEVEL
	 AND OM_PF_PROD_RET_LVL_PRICE_TMP.PF_PRODUCT_ID = "PHOTO".PF_PRODUCT_ID
     AND OM_PF_PROD_RET_LVL_PRICE_TMP.PF_RETAIL_LEVEL_ID="PHOTO".PF_RETAIL_LEVEL_ID
  ); 

  
 COMMIT;
 --DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);
END;
/
