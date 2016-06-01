SET SERVEROUTPUT ON;
-- UPDATE / ADD  IN OM_PROMOTIONAL_MONEY
DECLARE
	CURSOR c_promotionalmoney
	IS
	SELECT 
		Om_Pf_Mb_Promotion_Money_Tmp.PF_MB_PM_ID PF_PM_ID,
		Om_Pf_Mb_Promotion_Money_Tmp.PF_START_DTTM,
		Om_Pf_Mb_Promotion_Money_Tmp.PF_END_DTTM,
		Om_Pf_Mb_Promotion_Money_Tmp.PF_TIER_TYPE,
		Om_Pf_Mb_Promotion_Money_Tmp.PF_ACTIVE_IND,
		Om_Pf_Mb_Promotion_Money_Tmp.PF_DEACTIVATION_DTTM,
		Om_Pf_Mb_Promotion_Money_Tmp.PF_DISCOUNT_APPLICABLE,
		Om_Pf_Mb_Promotion_Money_Tmp.ID_CREATED,
		Om_Pf_Mb_Promotion_Money_Tmp.DATE_TIME_CREATED,
		Om_Pf_Mb_Promotion_Money_Tmp.ID_MODIFIED,
		Om_Pf_Mb_Promotion_Money_Tmp.DATE_TIME_MODIFIED,
		Om_Pf_Mb_Promotion_Money_Tmp.PF_TRANSFER_IND,
		Om_Pf_Mb_Promotion_Money_Tmp.PF_MB_PM_RULE_DESC DESCRIPTION,
		'Chain' PF_DIST_TYPE
	FROM Om_Pf_Mb_Promotion_Money_Tmp;

	-- DEFINE THE RECORD
	rec_promotionalmoney c_promotionalmoney%ROWTYPE;
	-- DEFINE VARIABLES
	v_syspmid OM_PROMOTIONAL_MONEY.SYS_PM_ID%TYPE;
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
			SELECT SYS_PM_ID INTO v_syspmid FROM OM_PROMOTIONAL_MONEY WHERE TYPE = 'MBPM' AND PM_NBR = rec_promotionalmoney.PF_PM_ID AND SYS_PRODUCT_ID = 0 ; 

			-- UPDATE PROMOTIONAL_MONEY
			UPDATE OM_PROMOTIONAL_MONEY
			SET
			TYPE = 'MBPM',
			PM_NBR = rec_promotionalmoney.PF_PM_ID,
			PM_DISTRIBUTION_TYPE = rec_promotionalmoney.PF_DIST_TYPE,
			PM_RULE_DESC = rec_promotionalmoney.DESCRIPTION,
			SYS_PRODUCT_ID = 0 ,
			START_DTTM = rec_promotionalmoney.PF_START_DTTM,
			END_DTTM = rec_promotionalmoney.PF_END_DTTM,
			PM_PAYMENT_TYPE = ' ',
			TIER_TYPE = rec_promotionalmoney.PF_TIER_TYPE,
			ACTIVE_CD = DECODE(rec_promotionalmoney.PF_ACTIVE_IND,'Y',1,'N',0,0),
			DEACTIVATION_DTTM = rec_promotionalmoney.PF_DEACTIVATION_DTTM,
			DISCOUNT_APPLICABLE_CD = DECODE(rec_promotionalmoney.PF_DISCOUNT_APPLICABLE,'Y',1,'N',0,0),
			PM_RULE_TYPE = ' ',
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
				'MBPM',
				rec_promotionalmoney.PF_PM_ID,
				rec_promotionalmoney.PF_DIST_TYPE,
				rec_promotionalmoney.DESCRIPTION,
				0 ,
				rec_promotionalmoney.PF_START_DTTM,
				rec_promotionalmoney.PF_END_DTTM,
				' ',
				rec_promotionalmoney.PF_TIER_TYPE,
				DECODE(rec_promotionalmoney.PF_ACTIVE_IND,'Y',1,'N',0,0),
				rec_promotionalmoney.PF_DEACTIVATION_DTTM,
				DECODE(rec_promotionalmoney.PF_DISCOUNT_APPLICABLE,'Y',1,'N',0,0),
				' ',
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
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || v_counter || ' records');  
    
	-- INSERT RECORDS THAT DO NOT MATCH JOIN CONDITION OF THE CURSOR QUERY			
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
