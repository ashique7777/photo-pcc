SET SERVEROUTPUT ON;
-- UPDATE / ADD  IN OM_PROMOTIONAL_MONEY
DECLARE
	CURSOR c_promotionalmoney
	IS
	SELECT 
		OM_PF_PROMOTION_MONEY_TMP.PF_PM_ID,
		OM_PF_PROMOTION_MONEY_TMP.PF_PRODUCT_ID,
		OM_PF_PROMOTION_MONEY_TMP.PF_START_DTTM,
		OM_PF_PROMOTION_MONEY_TMP.PF_END_DTTM,
		OM_PF_PROMOTION_MONEY_TMP.PF_PM_TYPE,
		OM_PF_PROMOTION_MONEY_TMP.PF_TIER_TYPE,
		OM_PF_PROMOTION_MONEY_TMP.PF_ACTIVE_IND,
		OM_PF_PROMOTION_MONEY_TMP.PF_DEACTIVATION_DTTM,
		OM_PF_PROMOTION_MONEY_TMP.PF_DISCOUNT_APPLICABLE,
		OM_PF_PROMOTION_MONEY_TMP.ID_CREATED,
		OM_PF_PROMOTION_MONEY_TMP.DATE_TIME_CREATED,
		OM_PF_PROMOTION_MONEY_TMP.ID_MODIFIED,
		OM_PF_PROMOTION_MONEY_TMP.DATE_TIME_MODIFIED,
		OM_PF_PROMOTION_MONEY_TMP.PF_GOAL_FACTOR,
		OM_PF_PROMOTION_MONEY_TMP.PF_TRANSFER_IND,
		OM_PRODUCT.SYS_PRODUCT_ID,
		OM_PRODUCT.DESCRIPTION,
		'Chain' PF_DIST_TYPE
	FROM OM_PF_PROMOTION_MONEY_TMP , OM_PRODUCT
	WHERE OM_PF_PROMOTION_MONEY_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR
	UNION
	SELECT 
		a.PF_PM_ID,
		a.PF_PRODUCT_ID,
		a.PF_START_DTTM,
		a.PF_END_DTTM,
		a.PF_PM_TYPE,
		a.PF_TIER_TYPE,
		a.PF_ACTIVE_IND,
		a.PF_DEACTIVATION_DTTM,
		a.PF_DISCOUNT_APPLICABLE,
		a.ID_CREATED,
		a.DATE_TIME_CREATED,
		a.ID_MODIFIED,
		a.DATE_TIME_MODIFIED,
		a.PF_GOAL_FACTOR,
		a.PF_TRANSFER_IND,
		b.SYS_PRODUCT_ID,
		b.DESCRIPTION,
		'Store' PF_DIST_TYPE
	FROM OM_PF_PROMO_MNY_STR_ASSOC_TMP a , OM_PRODUCT b
	WHERE a.PF_PRODUCT_ID = b.PRODUCT_NBR
	group by a.PF_PM_ID,a.PF_PRODUCT_ID,a.PF_START_DTTM,a.PF_END_DTTM,a.PF_PM_TYPE,a.PF_TIER_TYPE,a.PF_ACTIVE_IND,a.PF_DEACTIVATION_DTTM,a.PF_DISCOUNT_APPLICABLE,a.ID_CREATED,a.DATE_TIME_CREATED,a.ID_MODIFIED,a.DATE_TIME_MODIFIED,a.PF_GOAL_FACTOR,a.PF_TRANSFER_IND,b.SYS_PRODUCT_ID,b.DESCRIPTION;

	-- DEFINE THE RECORD
	rec_promotionalmoney c_promotionalmoney%ROWTYPE;
	-- DEFINE VARIABLES
	v_syspmid OM_PROMOTIONAL_MONEY.SYS_PM_ID%TYPE;
	v_deleted_ind OM_PF_PROMOTION_MONEY_TMP.PF_ACTIVE_IND%TYPE := 'D';
	v_counter NUMBER := 0;
	v_commitinterval NUMBER := 20;
	v_active_cd NUMBER := 0;
	v_blank VARCHAR2(1) := ' ';
	v_err_msg VARCHAR2(200) := '';
	v_err_code VARCHAR2(10) := '';
	
BEGIN
	OPEN c_promotionalmoney;
	LOOP
		FETCH c_promotionalmoney INTO rec_promotionalmoney;
		EXIT WHEN c_promotionalmoney%NOTFOUND OR c_promotionalmoney%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> rec_promotionalmoney.PF_PM_TYPE=' || rec_promotionalmoney.PF_PM_TYPE || '    rec_promotionalmoney.PF_PM_ID=' || rec_promotionalmoney.PF_PM_ID);  

		BEGIN
		
			-- CHECK IF RECORD EXISTS
			SELECT SYS_PM_ID INTO v_syspmid FROM OM_PROMOTIONAL_MONEY WHERE TYPE = 'PM' AND PM_NBR = rec_promotionalmoney.PF_PM_ID AND SYS_PRODUCT_ID = rec_promotionalmoney.SYS_PRODUCT_ID ; 			

			-- UPDATE PROMOTIONAL_MONEY
			UPDATE OM_PROMOTIONAL_MONEY
			SET
			TYPE = 'PM',
			PM_NBR = rec_promotionalmoney.PF_PM_ID,
			PM_DISTRIBUTION_TYPE = rec_promotionalmoney.PF_DIST_TYPE,
			PM_RULE_DESC = rec_promotionalmoney.DESCRIPTION,
			SYS_PRODUCT_ID = rec_promotionalmoney.SYS_PRODUCT_ID ,
			START_DTTM = rec_promotionalmoney.PF_START_DTTM,
			END_DTTM = rec_promotionalmoney.PF_END_DTTM,
			PM_PAYMENT_TYPE = rec_promotionalmoney.PF_PM_TYPE,
			TIER_TYPE = rec_promotionalmoney.PF_TIER_TYPE,
			ACTIVE_CD = DECODE(rec_promotionalmoney.PF_ACTIVE_IND,'Y',1,'N',0,0),
			DEACTIVATION_DTTM = rec_promotionalmoney.PF_DEACTIVATION_DTTM,
			DISCOUNT_APPLICABLE_CD = DECODE(rec_promotionalmoney.PF_DISCOUNT_APPLICABLE,'Y',1,'N',0,0),
			PM_RULE_TYPE = rec_promotionalmoney.PF_PM_TYPE,
			CREATE_USER_ID = rec_promotionalmoney.ID_CREATED,
			CREATE_DTTM = rec_promotionalmoney.DATE_TIME_CREATED,
			UPDATE_USER_ID = rec_promotionalmoney.ID_MODIFIED,
			UPDATE_DTTM = rec_promotionalmoney.DATE_TIME_MODIFIED
			WHERE SYS_PM_ID = v_syspmid; 

			--DBMS_OUTPUT.PUT_LINE ('Updated rec_promotionalmoney.PF_PM_TYPE=' || rec_promotionalmoney.PF_PM_TYPE || '    rec_promotionalmoney.PF_PM_ID='||rec_promotionalmoney.PF_PM_ID);          
		EXCEPTION
		WHEN NO_DATA_FOUND THEN
		-- INSERT PROMOTIONAL_MONEY
			BEGIN
				INSERT
				INTO OM_PROMOTIONAL_MONEY
				(
				SYS_PM_ID,
				TYPE,
				PM_NBR,
				PM_DISTRIBUTION_TYPE,
				PM_RULE_DESC,
				SYS_PRODUCT_ID,
				START_DTTM,
				END_DTTM,
				PM_PAYMENT_TYPE,
				TIER_TYPE,
				ACTIVE_CD,
				DEACTIVATION_DTTM,
				DISCOUNT_APPLICABLE_CD,
				PM_RULE_TYPE,
				CREATE_USER_ID,
				CREATE_DTTM,
				UPDATE_USER_ID,
				UPDATE_DTTM
				)
				VALUES
				(
				OM_PROMOTIONAL_MONEY_SEQ.NEXTVAL,
				'PM',
				rec_promotionalmoney.PF_PM_ID,
				rec_promotionalmoney.PF_DIST_TYPE,
				rec_promotionalmoney.DESCRIPTION,
				rec_promotionalmoney.SYS_PRODUCT_ID ,
				rec_promotionalmoney.PF_START_DTTM,
				rec_promotionalmoney.PF_END_DTTM,
				rec_promotionalmoney.PF_PM_TYPE,
				rec_promotionalmoney.PF_TIER_TYPE,
				DECODE(rec_promotionalmoney.PF_ACTIVE_IND,'Y',1,'N',0,0),
				rec_promotionalmoney.PF_DEACTIVATION_DTTM,
				DECODE(rec_promotionalmoney.PF_DISCOUNT_APPLICABLE,'Y',1,'N',0,0),
				rec_promotionalmoney.PF_PM_TYPE,
				rec_promotionalmoney.ID_CREATED,
				rec_promotionalmoney.DATE_TIME_CREATED,
				rec_promotionalmoney.ID_MODIFIED,
				rec_promotionalmoney.DATE_TIME_MODIFIED
				);

				--DBMS_OUTPUT.PUT_LINE ('Inserted rec_promotionalmoney.PF_PM_TYPE=' || rec_promotionalmoney.PF_PM_TYPE || '    rec_promotionalmoney.PF_PM_ID='||rec_promotionalmoney.PF_PM_ID);       

			EXCEPTION	
			WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	


				INSERT INTO OM_PF_PROMOTION_MONEY_BAD 
				( 
				PF_PM_ID,
				PF_PRODUCT_ID,
				PF_START_DTTM,
				PF_END_DTTM,
				PF_PM_TYPE,
				PF_TIER_TYPE,
				PF_ACTIVE_IND,
				PF_DEACTIVATION_DTTM,
				PF_DISCOUNT_APPLICABLE,
				ID_CREATED,
				DATE_TIME_CREATED,
				ID_MODIFIED,
				DATE_TIME_MODIFIED,
				PF_GOAL_FACTOR,
				PF_TRANSFER_IND,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
				) 
				VALUES  
				(  
				rec_promotionalmoney.PF_PM_ID,
				rec_promotionalmoney.PF_PRODUCT_ID,
				rec_promotionalmoney.PF_START_DTTM,
				rec_promotionalmoney.PF_END_DTTM,
				rec_promotionalmoney.PF_PM_TYPE,
				rec_promotionalmoney.PF_TIER_TYPE,
				rec_promotionalmoney.PF_ACTIVE_IND,
				rec_promotionalmoney.PF_DEACTIVATION_DTTM,
				rec_promotionalmoney.PF_DISCOUNT_APPLICABLE,
				rec_promotionalmoney.ID_CREATED,
				rec_promotionalmoney.DATE_TIME_CREATED,
				rec_promotionalmoney.ID_MODIFIED,
				rec_promotionalmoney.DATE_TIME_MODIFIED,
				rec_promotionalmoney.PF_GOAL_FACTOR,
				rec_promotionalmoney.PF_TRANSFER_IND,				
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

				INSERT INTO OM_PF_PROMOTION_MONEY_BAD 
				( 
				PF_PM_ID,
				PF_PRODUCT_ID,
				PF_START_DTTM,
				PF_END_DTTM,
				PF_PM_TYPE,
				PF_TIER_TYPE,
				PF_ACTIVE_IND,
				PF_DEACTIVATION_DTTM,
				PF_DISCOUNT_APPLICABLE,
				ID_CREATED,
				DATE_TIME_CREATED,
				ID_MODIFIED,
				DATE_TIME_MODIFIED,
				PF_GOAL_FACTOR,
				PF_TRANSFER_IND,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
				) 
				VALUES  
				(  
				rec_promotionalmoney.PF_PM_ID,
				rec_promotionalmoney.PF_PRODUCT_ID,
				rec_promotionalmoney.PF_START_DTTM,
				rec_promotionalmoney.PF_END_DTTM,
				rec_promotionalmoney.PF_PM_TYPE,
				rec_promotionalmoney.PF_TIER_TYPE,
				rec_promotionalmoney.PF_ACTIVE_IND,
				rec_promotionalmoney.PF_DEACTIVATION_DTTM,
				rec_promotionalmoney.PF_DISCOUNT_APPLICABLE,
				rec_promotionalmoney.ID_CREATED,
				rec_promotionalmoney.DATE_TIME_CREATED,
				rec_promotionalmoney.ID_MODIFIED,
				rec_promotionalmoney.DATE_TIME_MODIFIED,
				rec_promotionalmoney.PF_GOAL_FACTOR,
				rec_promotionalmoney.PF_TRANSFER_IND,				
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
	INSERT INTO OM_PF_PROMOTION_MONEY_BAD 
	( 
	PF_PM_ID,
	PF_PRODUCT_ID,
	PF_START_DTTM,
	PF_END_DTTM,
	PF_PM_TYPE,
	PF_TIER_TYPE,
	PF_ACTIVE_IND,
	PF_DEACTIVATION_DTTM,
	PF_DISCOUNT_APPLICABLE,
	ID_CREATED,
	DATE_TIME_CREATED,
	ID_MODIFIED,
	DATE_TIME_MODIFIED,
	PF_GOAL_FACTOR,
	PF_TRANSFER_IND,
	EXCEPTION_CODE ,
	EXCEPTION_MSSG ,
	EXCEPTION_DTTM 
	) 
	SELECT 
		"PM_MONEY".PF_PM_ID,
		"PM_MONEY".PF_PRODUCT_ID,
		"PM_MONEY".PF_START_DTTM,
		"PM_MONEY".PF_END_DTTM,
		"PM_MONEY".PF_PM_TYPE,
		"PM_MONEY".PF_TIER_TYPE,
		"PM_MONEY".PF_ACTIVE_IND,
		"PM_MONEY".PF_DEACTIVATION_DTTM,
		"PM_MONEY".PF_DISCOUNT_APPLICABLE,
		"PM_MONEY".ID_CREATED,
		"PM_MONEY".DATE_TIME_CREATED,
		"PM_MONEY".ID_MODIFIED,
		"PM_MONEY".DATE_TIME_MODIFIED,
		"PM_MONEY".PF_GOAL_FACTOR,
		"PM_MONEY".PF_TRANSFER_IND,
		'000' , 
		'Master records not found' ,
		SYSDATE		
		FROM OM_PF_PROMOTION_MONEY_TMP "PM_MONEY"
		WHERE NOT EXISTS
		(
			SELECT 1
			FROM OM_PF_PROMOTION_MONEY_TMP , OM_PRODUCT
			WHERE OM_PF_PROMOTION_MONEY_TMP.PF_PRODUCT_ID = OM_PRODUCT.PRODUCT_NBR
			AND OM_PF_PROMOTION_MONEY_TMP.PF_PRODUCT_ID = "PM_MONEY".PF_PRODUCT_ID
		);
		
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
