SET SERVEROUTPUT ON;
-- UPDATE / ADD Downtime Reason IN OM_PF_PRODUCT_TMP

DECLARE
  CURSOR c_bundle_product
  IS
	SELECT 
	OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_PRODUCT_ID ,
	OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_CHILD_PRODUCT_ID ,
	OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_CHILD_PRODUCT_QTY ,
	OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_TRANSFER_IND ,
	OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.ID_CREATED ,
	OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.DATE_TIME_CREATED ,
	OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.ID_MODIFIED ,
	OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.DATE_TIME_MODIFIED ,
	NVL(DECODE(OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_PRODUCT_PRIORITY, 'A',2,'P',3, '1', 1),0) PF_PRODUCT_PRIORITY ,
	"A".SYS_PRODUCT_ID  "SYS_PRODUCT_ID",
	"B".SYS_PRODUCT_ID  "SYS_CHILD_PRODUCT_ID"
	FROM OM_PF_BUNDLE_PRODUCT_ASSOC_TMP , OM_PRODUCT "A", OM_PRODUCT "B"
	WHERE OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_PRODUCT_ID = "A".PRODUCT_NBR
	AND OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_CHILD_PRODUCT_ID = "B".PRODUCT_NBR;

  -- DEFINE THE RECORD
  rec_bundle_product c_bundle_product%ROWTYPE;
  -- DEFINE VARIABLES
  v_blank VARCHAR2(2) := ' ';
  v_id VARCHAR2(10) := 'SYSTEM' ;
  v_product_id OM_BUNDLE_PRODUCT.SYS_BUNDLE_PRODUCT_ID%TYPE:= 0;
  v_deleted_ind OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_bundle_product;
  LOOP
    FETCH c_bundle_product INTO rec_bundle_product;
    EXIT WHEN c_bundle_product%NOTFOUND OR c_bundle_product%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Product ID=' || rec_bundle_product.PF_PRODUCT_ID );
    BEGIN
    
	v_active_cd := 1;
	
	-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
	IF (rec_bundle_product.PF_TRANSFER_IND IS NOT NULL AND upper(rec_bundle_product.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
		v_active_cd := 0;
	END IF;
	
        -- CHECK IF RECORD EXISTS
        SELECT SYS_BUNDLE_PRODUCT_ID INTO v_product_id FROM OM_BUNDLE_PRODUCT WHERE SYS_PRODUCT_ID = rec_bundle_product.SYS_PRODUCT_ID AND SYS_CHILD_PRODUCT_ID = rec_bundle_product.SYS_CHILD_PRODUCT_ID;
				
        -- UPDATE PRODUCT
		UPDATE OM_BUNDLE_PRODUCT 
		SET 
			SYS_PRODUCT_ID = rec_bundle_product.SYS_PRODUCT_ID,
			SYS_CHILD_PRODUCT_ID = rec_bundle_product.SYS_CHILD_PRODUCT_ID,
			CHILD_PRODUCT_QTY = rec_bundle_product.PF_CHILD_PRODUCT_QTY,
			PROCESSING_SEQUENCE = rec_bundle_product.PF_PRODUCT_PRIORITY,
			CREATE_USER_ID = rec_bundle_product.ID_CREATED,
			CREATE_DTTM = rec_bundle_product.DATE_TIME_CREATED,
			UPDATE_USER_ID = rec_bundle_product.ID_MODIFIED,
			UPDATE_DTTM = rec_bundle_product.DATE_TIME_MODIFIED,
			ACTIVE_CD = v_active_cd
		WHERE  SYS_BUNDLE_PRODUCT_ID = v_product_id;

		--DBMS_OUTPUT.PUT_LINE ('Updated Product=' || 'Product ID=' || rec_bundle_product.PF_PRODUCT_ID );     
        EXCEPTION
        -- INSERT PRODUCT
        WHEN NO_DATA_FOUND THEN
		BEGIN
        INSERT 
			INTO OM_BUNDLE_PRODUCT 
			( 
				SYS_BUNDLE_PRODUCT_ID ,
				SYS_PRODUCT_ID ,
				SYS_CHILD_PRODUCT_ID ,
				CHILD_PRODUCT_QTY ,
				PROCESSING_SEQUENCE ,
				CREATE_USER_ID ,
				CREATE_DTTM ,
				UPDATE_USER_ID ,
				UPDATE_DTTM ,
				ACTIVE_CD
			) 
			VALUES 
			(
				OM_BUNDLE_PRODUCT_SEQ.NEXTVAL,
				rec_bundle_product.SYS_PRODUCT_ID,
				rec_bundle_product.SYS_CHILD_PRODUCT_ID,
				rec_bundle_product.PF_CHILD_PRODUCT_QTY,
				rec_bundle_product.PF_PRODUCT_PRIORITY,
				rec_bundle_product.ID_CREATED,
				rec_bundle_product.DATE_TIME_CREATED,
				rec_bundle_product.ID_MODIFIED,
				rec_bundle_product.DATE_TIME_MODIFIED,
				v_active_cd
			);

        --DBMS_OUTPUT.PUT_LINE ('Added Product=' || 'Product ID=' || rec_bundle_product.PF_PRODUCT_ID ); 
	    EXCEPTION	
			WHEN OTHERS THEN
			v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
			--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
			
			INSERT 
			INTO OM_PF_BUNDLE_PRODUCT_ASSOC_BAD 
			( 
				PF_PRODUCT_ID ,
				PF_CHILD_PRODUCT_ID ,
				PF_CHILD_PRODUCT_QTY ,
				PF_TRANSFER_IND ,
				ID_CREATED ,
				DATE_TIME_CREATED ,
				ID_MODIFIED ,
				DATE_TIME_MODIFIED ,
				PF_PRODUCT_PRIORITY ,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
			) 
			VALUES  
			(  
				rec_bundle_product.PF_PRODUCT_ID ,
				rec_bundle_product.PF_CHILD_PRODUCT_ID ,
				rec_bundle_product.PF_CHILD_PRODUCT_QTY ,
				rec_bundle_product.PF_TRANSFER_IND ,
				rec_bundle_product.ID_CREATED ,
				rec_bundle_product.DATE_TIME_CREATED ,
				rec_bundle_product.ID_MODIFIED ,
				rec_bundle_product.DATE_TIME_MODIFIED ,
				rec_bundle_product.PF_PRODUCT_PRIORITY ,
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
			
			INSERT 
			INTO OM_PF_BUNDLE_PRODUCT_ASSOC_BAD 
			( 
				PF_PRODUCT_ID ,
				PF_CHILD_PRODUCT_ID ,
				PF_CHILD_PRODUCT_QTY ,
				PF_TRANSFER_IND ,
				ID_CREATED ,
				DATE_TIME_CREATED ,
				ID_MODIFIED ,
				DATE_TIME_MODIFIED ,
				PF_PRODUCT_PRIORITY ,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
			) 
			VALUES  
			(  
				rec_bundle_product.PF_PRODUCT_ID ,
				rec_bundle_product.PF_CHILD_PRODUCT_ID ,
				rec_bundle_product.PF_CHILD_PRODUCT_QTY ,
				rec_bundle_product.PF_TRANSFER_IND ,
				rec_bundle_product.ID_CREATED ,
				rec_bundle_product.DATE_TIME_CREATED ,
				rec_bundle_product.ID_MODIFIED ,
				rec_bundle_product.DATE_TIME_MODIFIED ,
				rec_bundle_product.PF_PRODUCT_PRIORITY ,
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
  INSERT 
	INTO OM_PF_BUNDLE_PRODUCT_ASSOC_BAD 
	( 
		PF_PRODUCT_ID ,
		PF_CHILD_PRODUCT_ID ,
		PF_CHILD_PRODUCT_QTY ,
		PF_TRANSFER_IND ,
		ID_CREATED ,
		DATE_TIME_CREATED ,
		ID_MODIFIED ,
		DATE_TIME_MODIFIED ,
		PF_PRODUCT_PRIORITY ,
		EXCEPTION_CODE ,
		EXCEPTION_MSSG ,
		EXCEPTION_DTTM 
	) 
	 SELECT
		"BUNDLE".PF_PRODUCT_ID ,
		"BUNDLE".PF_CHILD_PRODUCT_ID ,
		"BUNDLE".PF_CHILD_PRODUCT_QTY ,
		"BUNDLE".PF_TRANSFER_IND ,
		"BUNDLE".ID_CREATED ,
		"BUNDLE".DATE_TIME_CREATED ,
		"BUNDLE".ID_MODIFIED ,
		"BUNDLE".DATE_TIME_MODIFIED ,
		"BUNDLE".PF_PRODUCT_PRIORITY ,
		'000' ,
		'Master records not found' ,
		SYSDATE
	 FROM OM_PF_BUNDLE_PRODUCT_ASSOC_TMP "BUNDLE"
	 WHERE NOT EXISTS
		( SELECT 1
		  FROM OM_PF_BUNDLE_PRODUCT_ASSOC_TMP , OM_PRODUCT "A", OM_PRODUCT "B"
		  WHERE OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_PRODUCT_ID    ="A".PRODUCT_NBR
		  AND OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_CHILD_PRODUCT_ID="B".PRODUCT_NBR
		  AND OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_PRODUCT_ID = "BUNDLE".PF_PRODUCT_ID
		  AND OM_PF_BUNDLE_PRODUCT_ASSOC_TMP.PF_CHILD_PRODUCT_ID = "BUNDLE".PF_CHILD_PRODUCT_ID
		);
END;
/
