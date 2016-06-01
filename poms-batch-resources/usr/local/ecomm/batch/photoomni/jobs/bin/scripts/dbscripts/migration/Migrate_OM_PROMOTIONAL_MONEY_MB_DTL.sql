SET SERVEROUTPUT ON;
-- UPDATE / ADD Machine Equipment IN OM_PROMOTIONAL_MONEY_DTL

DECLARE
  CURSOR c_promotionalmoneydtl
  IS
   	SELECT 	 
		OM_PF_MBPRMOTNMNY_TIER_TMP.PF_MINIMUM_TIER_AMT PF_MINIMUM_TIER_QTY,
		OM_PF_MBPRMOTNMNY_TIER_TMP.PF_MAXIMUM_TIER_AMT PF_MAXIMUM_TIER_QTY,
		OM_PF_MBPRMOTNMNY_TIER_TMP.PF_MB_PM_AMT PF_PM_AMT,
		OM_PF_MBPRMOTNMNY_TIER_TMP.ID_CREATED,
		OM_PF_MBPRMOTNMNY_TIER_TMP.DATE_TIME_CREATED,
		OM_PF_MBPRMOTNMNY_TIER_TMP.ID_MODIFIED,
		OM_PF_MBPRMOTNMNY_TIER_TMP.DATE_TIME_MODIFIED,
		OM_PF_MBPRMOTNMNY_TIER_TMP.PF_TRANSFER_IND,
		OM_PF_MBPRMOTNMNY_TIER_TMP.PF_MB_PM_ID PF_PM_ID,
		OM_PROMOTIONAL_MONEY.SYS_PM_ID
	FROM OM_PF_MBPRMOTNMNY_TIER_TMP, OM_PROMOTIONAL_MONEY
	WHERE OM_PF_MBPRMOTNMNY_TIER_TMP.PF_MB_PM_ID=OM_PROMOTIONAL_MONEY.PM_NBR
	AND OM_PROMOTIONAL_MONEY.TYPE = 'MBPM';

  -- DEFINE THE RECORD
  rec_promotionalmoneydtl c_promotionalmoneydtl%ROWTYPE;
  -- DEFINE VARIABLES
  v_syspmdtlid OM_PROMOTIONAL_MONEY_DTL.SYS_PM_DTL_ID%TYPE:= 0;
  v_deleted_ind OM_PF_PROMOTION_MONEY_TIER_TMP.PF_TRANSFER_IND%TYPE := 'D';
  v_counter NUMBER := 0;
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  OPEN c_promotionalmoneydtl;
  LOOP
    FETCH c_promotionalmoneydtl INTO rec_promotionalmoneydtl;
    EXIT WHEN c_promotionalmoneydtl%NOTFOUND OR c_promotionalmoneydtl%NOTFOUND IS NULL;
    --DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Minimum tier qty=' || rec_promotionalmoneydtl.PF_MINIMUM_TIER_QTY || ',Maximum tier qty=' || rec_promotionalmoneydtl.PF_MAXIMUM_TIER_QTY);
    BEGIN
        -- CHECK IF RECORD EXISTS
        SELECT SYS_PM_DTL_ID INTO v_syspmdtlid FROM OM_PROMOTIONAL_MONEY_DTL 
		WHERE MINIMUM_TIER = rec_promotionalmoneydtl.PF_MINIMUM_TIER_QTY AND MAXIMUM_TIER = rec_promotionalmoneydtl.PF_MAXIMUM_TIER_QTY
		AND SYS_PM_ID = rec_promotionalmoneydtl.SYS_PM_ID ; 
		
        -- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1
			IF (rec_promotionalmoneydtl.PF_TRANSFER_IND IS NOT NULL AND upper(rec_promotionalmoneydtl.PF_TRANSFER_IND) = v_deleted_ind) THEN       			
				v_active_cd := 0;
			ELSE 
				v_active_cd := 1;
			END IF;
			
        -- UPDATE PROMOTIONAL DETAIL
		UPDATE OM_PROMOTIONAL_MONEY_DTL 
		SET SYS_PM_ID= rec_promotionalmoneydtl.SYS_PM_ID ,
			MINIMUM_TIER= rec_promotionalmoneydtl.PF_MINIMUM_TIER_QTY,
			MAXIMUM_TIER= rec_promotionalmoneydtl.PF_MAXIMUM_TIER_QTY,
			PM_AMOUNT= rec_promotionalmoneydtl.PF_PM_AMT,
			CREATE_USER_ID= rec_promotionalmoneydtl.ID_CREATED,
			CREATE_DTTM= rec_promotionalmoneydtl.DATE_TIME_CREATED,
			UPDATE_USER_ID= rec_promotionalmoneydtl.ID_MODIFIED,
			UPDATE_DTTM= rec_promotionalmoneydtl.DATE_TIME_MODIFIED,
			ACTIVE_CD= v_active_cd
		WHERE  SYS_PM_DTL_ID = v_syspmdtlid;

		--DBMS_OUTPUT.PUT_LINE ('Minimum tier qty=' || rec_promotionalmoneydtl.PF_MINIMUM_TIER_QTY || 'Maximum tier qty=' || rec_promotionalmoneydtl.PF_MAXIMUM_TIER_QTY) ;       
        EXCEPTION
        -- INSERT PROMOTIONAL_MONEY
        WHEN NO_DATA_FOUND THEN
		BEGIN
       
		INSERT INTO OM_PROMOTIONAL_MONEY_DTL 
		( 
			SYS_PM_DTL_ID,
			SYS_PM_ID,
			MINIMUM_TIER,
			MAXIMUM_TIER,
			PM_AMOUNT,
			CREATE_USER_ID,
			CREATE_DTTM,
			UPDATE_USER_ID,
			UPDATE_DTTM,
			ACTIVE_CD
		 ) 
		 VALUES 
		 (
			 OM_PROMOTIONAL_MONEY_DTL_SEQ.NEXTVAL,
			rec_promotionalmoneydtl.SYS_PM_ID ,
			rec_promotionalmoneydtl.PF_MINIMUM_TIER_QTY,
			rec_promotionalmoneydtl.PF_MAXIMUM_TIER_QTY,
			rec_promotionalmoneydtl.PF_PM_AMT,
			rec_promotionalmoneydtl.ID_CREATED,
			rec_promotionalmoneydtl.DATE_TIME_CREATED,
			rec_promotionalmoneydtl.ID_MODIFIED,
			rec_promotionalmoneydtl.DATE_TIME_MODIFIED,
			v_active_cd
		  );

        --DBMS_OUTPUT.PUT_LINE ('Added Minimum tier qty=' || rec_promotionalmoneydtl.PF_MINIMUM_TIER_QTY); 
	    EXCEPTION	
			WHEN OTHERS THEN
			v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
			--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);				

		 v_counter := v_counter + 1;
        END;      
	    WHEN OTHERS THEN
			v_err_msg := SUBSTR(SQLERRM, 1 , 200);
			v_err_code := SQLCODE ;
			--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);					
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
END;
/
