SET SERVEROUTPUT ON;
-- UPDATE / ADD EXCEPTION TYPE IN OM_EXCEPTION_TYPE
DECLARE
  CURSOR c_exceptiontype
  IS
	SELECT PF_EXCEPTION_ID_SEQ ,
	  PF_EXCEPTION_TYPE ,
	  PF_EXCEPTION_REASON ,
	  PF_ACTIVE_IND ,
	  PF_ORDER_STATUS ,
	  ID_CREATED ,
	  ID_MODIFIED ,
	  DATE_TIME_CREATED ,
	  DATE_TIME_MODIFIED ,
	  PF_TRANSFER_IND ,
	  PF_DISPLAY_IND ,
	  PF_EXCEPTION_RPT_BY ,
	  PF_WASTE_RPT_BY ,
	  PF_EXCEPTION_REPORT_LABOR ,
	  PF_ORDER_TYPE_IND ,
	  PF_TXT_MSG_ELIGIBLE_IND 
	FROM OM_PF_EXCEPTION_TYPE_TMP;
 
	-- DEFINE THE RECORD
	rec_exceptiontype c_exceptiontype%ROWTYPE;
	-- DEFINE VARIABLES
	v_sysexceptypeid OM_EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID%TYPE;
	v_deleted_ind VARCHAR2(1) := 'D';
	v_counter NUMBER := 0;
	v_commitinterval NUMBER := 20;
	v_active_cd NUMBER := 0;
	v_blank VARCHAR2(1) := '';
	v_err_msg VARCHAR2(200) := '';
	v_err_code VARCHAR2(10) := '';
BEGIN
	OPEN c_exceptiontype;
	LOOP
		FETCH c_exceptiontype INTO rec_exceptiontype;
		EXIT WHEN c_exceptiontype%NOTFOUND OR c_exceptiontype%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'exceptionid=' || rec_exceptiontype.PF_EXCEPTION_ID_SEQ || ',exceptiontype=' || rec_exceptiontype.PF_EXCEPTION_TYPE);
		BEGIN

			v_active_cd := 0;

			-- Check Active Ind
			IF NVL(rec_exceptiontype.PF_ACTIVE_IND,'N') = 'Y' THEN
				v_active_cd := 1;
			END IF;

			-- IF PF_TRANSFER_IND = "D" THEN ACTIVE_CD WILL BE 0 ELSE 1		
			IF (rec_exceptiontype.PF_TRANSFER_IND IS NOT NULL AND upper(rec_exceptiontype.PF_TRANSFER_IND) = v_deleted_ind) THEN        			
				v_active_cd := 0;
			END IF;

			-- CHECK IF RECORD EXISTS
			SELECT SYS_EXCEPTION_TYPE_ID INTO v_sysexceptypeid FROM OM_EXCEPTION_TYPE WHERE SYS_EXCEPTION_TYPE_ID=rec_exceptiontype.PF_EXCEPTION_ID_SEQ; 

			-- UPDATE EXCEPTION_TYPE
			UPDATE OM_EXCEPTION_TYPE
			SET EXCEPTION_TYPE=rec_exceptiontype.PF_EXCEPTION_TYPE,
				REASON=rec_exceptiontype.PF_EXCEPTION_REASON,
				ACTIVE_CD=v_active_cd,
				ORDER_STATUS=rec_exceptiontype.PF_ORDER_STATUS,
				DISPLAY_CD=DECODE(rec_exceptiontype.PF_DISPLAY_IND,'Y',1,'N',0,0),
				EXCEPTION_RPT_BY=rec_exceptiontype.PF_EXCEPTION_RPT_BY,
				ORDER_TYPE_CD=NVL(DECODE(rec_exceptiontype.PF_ORDER_TYPE_IND,'Q',1,'S',2,0),0),
				TXT_MSG_ELIGIBLE_CD=DECODE(rec_exceptiontype.PF_TXT_MSG_ELIGIBLE_IND,'Y',1,'N',0,0) ,
				CREATE_USER_ID=rec_exceptiontype.ID_CREATED,
				CREATE_DTTM=rec_exceptiontype.DATE_TIME_CREATED,
				UPDATE_USER_ID=rec_exceptiontype.ID_MODIFIED,
				UPDATE_DTTM=rec_exceptiontype.DATE_TIME_MODIFIED
			WHERE SYS_EXCEPTION_TYPE_ID = v_sysexceptypeid;
			
			--DBMS_OUTPUT.PUT_LINE ('Updated exceptionid:' || v_sysexceptypeid);           
		EXCEPTION
			-- INSERT EXCEPTION_TYPE
			WHEN NO_DATA_FOUND THEN
			BEGIN
				INSERT 
				INTO OM_EXCEPTION_TYPE
				(
					SYS_EXCEPTION_TYPE_ID,
					EXCEPTION_TYPE,
					REASON,
					ACTIVE_CD,
					ORDER_STATUS,
					DISPLAY_CD,
					EXCEPTION_RPT_BY,
					ORDER_TYPE_CD,
					TXT_MSG_ELIGIBLE_CD,
					CREATE_USER_ID,
					CREATE_DTTM,
					UPDATE_USER_ID,
					UPDATE_DTTM
				)
				VALUES
				(
					rec_exceptiontype.PF_EXCEPTION_ID_SEQ,
					rec_exceptiontype.PF_EXCEPTION_TYPE,
					rec_exceptiontype.PF_EXCEPTION_REASON,
					v_active_cd,
					rec_exceptiontype.PF_ORDER_STATUS,
					DECODE(rec_exceptiontype.PF_DISPLAY_IND,'Y',1,'N',0,0),
					rec_exceptiontype.PF_EXCEPTION_RPT_BY,
					NVL(DECODE(rec_exceptiontype.PF_ORDER_TYPE_IND,'Q',1,'S',2,0),0),
					DECODE(rec_exceptiontype.PF_TXT_MSG_ELIGIBLE_IND,'Y',1,'N',0,0),
					rec_exceptiontype.ID_CREATED,
					rec_exceptiontype.DATE_TIME_CREATED,
					rec_exceptiontype.ID_MODIFIED,
					rec_exceptiontype.DATE_TIME_MODIFIED
				);

				--DBMS_OUTPUT.PUT_LINE ('Added EXCEPTION TYPE:' || rec_exceptiontype.PF_EXCEPTION_TYPE);           

			EXCEPTION 
				WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg); 

				INSERT INTO OM_PF_EXCEPTION_TYPE_BAD 
				( 
					PF_EXCEPTION_ID_SEQ,
					PF_EXCEPTION_TYPE,
					PF_ORDER_STATUS,
					PF_ACTIVE_IND,
					PF_DISPLAY_IND,
					PF_EXCEPTION_RPT_BY,
					PF_ORDER_TYPE_IND,
					PF_TXT_MSG_ELIGIBLE_IND,
					PF_EXCEPTION_REASON,
					PF_TRANSFER_IND,
					PF_WASTE_RPT_BY,
					PF_EXCEPTION_REPORT_LABOR,
					ID_CREATED,
					ID_MODIFIED,
					DATE_TIME_CREATED,
					DATE_TIME_MODIFIED,
					EXCEPTION_CODE ,
					EXCEPTION_MSSG ,
					EXCEPTION_DTTM 
				) 
				VALUES  
				(  
					rec_exceptiontype.PF_EXCEPTION_ID_SEQ ,
					rec_exceptiontype.PF_EXCEPTION_TYPE ,
					rec_exceptiontype.PF_ORDER_STATUS ,
					rec_exceptiontype.PF_ACTIVE_IND ,
					rec_exceptiontype.PF_DISPLAY_IND ,
					rec_exceptiontype.PF_EXCEPTION_RPT_BY ,
					rec_exceptiontype.PF_ORDER_TYPE_IND ,
					rec_exceptiontype.PF_TXT_MSG_ELIGIBLE_IND ,
					rec_exceptiontype.PF_EXCEPTION_REASON,
					rec_exceptiontype.PF_TRANSFER_IND,
					rec_exceptiontype.PF_WASTE_RPT_BY,
					rec_exceptiontype.PF_EXCEPTION_REPORT_LABOR,
					rec_exceptiontype.ID_CREATED ,
					rec_exceptiontype.ID_MODIFIED ,
					rec_exceptiontype.DATE_TIME_CREATED ,
					rec_exceptiontype.DATE_TIME_MODIFIED ,
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

			INSERT INTO OM_PF_EXCEPTION_TYPE_BAD 
			( 
				PF_EXCEPTION_ID_SEQ,
				PF_EXCEPTION_TYPE,
				PF_ORDER_STATUS,
				PF_ACTIVE_IND,
				PF_DISPLAY_IND,
				PF_EXCEPTION_RPT_BY,
				PF_ORDER_TYPE_IND,
				PF_TXT_MSG_ELIGIBLE_IND,
				PF_EXCEPTION_REASON,
				PF_TRANSFER_IND,
				PF_WASTE_RPT_BY,
				PF_EXCEPTION_REPORT_LABOR,
				ID_CREATED,
				ID_MODIFIED,
				DATE_TIME_CREATED,
				DATE_TIME_MODIFIED,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
			) 
			VALUES  
			(  
				rec_exceptiontype.PF_EXCEPTION_ID_SEQ ,
				rec_exceptiontype.PF_EXCEPTION_TYPE ,
				rec_exceptiontype.PF_ORDER_STATUS ,
				rec_exceptiontype.PF_ACTIVE_IND ,
				rec_exceptiontype.PF_DISPLAY_IND ,
				rec_exceptiontype.PF_EXCEPTION_RPT_BY ,
				rec_exceptiontype.PF_ORDER_TYPE_IND ,
				rec_exceptiontype.PF_TXT_MSG_ELIGIBLE_IND ,
				rec_exceptiontype.PF_EXCEPTION_REASON,
				rec_exceptiontype.PF_TRANSFER_IND,
				rec_exceptiontype.PF_WASTE_RPT_BY,
				rec_exceptiontype.PF_EXCEPTION_REPORT_LABOR,
				rec_exceptiontype.ID_CREATED ,
				rec_exceptiontype.ID_MODIFIED ,
				rec_exceptiontype.DATE_TIME_CREATED ,
				rec_exceptiontype.DATE_TIME_MODIFIED ,
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
	EXCEPTION
	WHEN OTHERS THEN
	DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);

END;
/
