SET SERVEROUTPUT ON;
-- UPDATE / ADD EVENTS IN OM_SIGNS_HEADER
DECLARE
  CURSOR c_signsheader
  IS
	SELECT 
		PF_EVENT_ID , 
		TRIM(PF_EVENT_NAME) PF_EVENT_NAME, 
		PF_SHOW_IN_STORE , 
		PF_EXPIRY_DATE , 
		ID_CREATED , 
		DATE_TIME_CREATED , 
		ID_MODIFIED , 
		DATE_TIME_MODIFIED , 
		PF_SHOW_ON_STORE_TYPE , 
		PF_PURGE_IND 
	FROM OM_PF_EVENT_HEADER_TMP;

	-- DEFINE THE RECORD
	rec_signsheader c_signsheader%ROWTYPE;
	-- DEFINE VARIABLES
	v_syseventid OM_SIGNS_HEADER.SYS_EVENT_ID%TYPE;
	v_counter NUMBER := 0;
	v_commitinterval NUMBER := 20;
	v_active_cd NUMBER := 1;
	v_blank VARCHAR2(1) := '';
	v_err_msg VARCHAR2(200) := '';
	v_err_code VARCHAR2(10) := '';

BEGIN
	OPEN c_signsheader;
	LOOP
		FETCH c_signsheader INTO rec_signsheader;
		EXIT WHEN c_signsheader%NOTFOUND OR c_signsheader%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'eventid=' || rec_signsheader.PF_EVENT_ID || ',eventname=' || rec_signsheader.PF_EVENT_NAME);
		BEGIN

			-- CHECK IF RECORD EXISTS
			SELECT SYS_EVENT_ID INTO v_syseventid FROM OM_SIGNS_HEADER WHERE SYS_EVENT_ID = rec_signsheader.PF_EVENT_ID;  

			-- UPDATE EVENT_NAME
			UPDATE OM_SIGNS_HEADER 
			SET  EVENT_NAME = rec_signsheader.PF_EVENT_NAME, 
			SHOW_IN_STORE_CD = NVL(DECODE(rec_signsheader.PF_SHOW_IN_STORE,'Y',1,'N',0),0), 
			EXPIRY_DTTM = rec_signsheader.PF_EXPIRY_DATE , 
			SHOW_ON_STORE_TYPE = rec_signsheader.PF_SHOW_ON_STORE_TYPE, 
			PURGE_CD = NVL(DECODE(rec_signsheader.PF_PURGE_IND,'Y',1,'N',0),0), 
			CREATE_USER_ID = rec_signsheader.ID_CREATED, 
			CREATE_DTTM = rec_signsheader.DATE_TIME_CREATED, 
			UPDATE_USER_ID = rec_signsheader.ID_MODIFIED, 
			UPDATE_DTTM = rec_signsheader.DATE_TIME_MODIFIED, 
			ACTIVE_CD =v_active_cd
			WHERE SYS_EVENT_ID = v_syseventid ;

			--DBMS_OUTPUT.PUT_LINE ('Updated eventid:' || rec_signsheader.PF_EVENT_ID);          
		EXCEPTION
			-- INSERT EVENT_NAME
			WHEN NO_DATA_FOUND THEN
			BEGIN
				INSERT INTO OM_SIGNS_HEADER 
				( 
					SYS_EVENT_ID , 
					EVENT_NAME , 
					SHOW_IN_STORE_CD , 
					EXPIRY_DTTM , 
					SHOW_ON_STORE_TYPE , 
					PURGE_CD , 
					CREATE_USER_ID , 
					CREATE_DTTM , 
					UPDATE_USER_ID , 
					UPDATE_DTTM , 
					ACTIVE_CD
				)
				VALUES
				(
					rec_signsheader.PF_EVENT_ID,
					rec_signsheader.PF_EVENT_NAME, 
					NVL(DECODE(rec_signsheader.PF_SHOW_IN_STORE,'Y',1,'N',0),0), 
					rec_signsheader.PF_EXPIRY_DATE , 
					rec_signsheader.PF_SHOW_ON_STORE_TYPE, 
					NVL(DECODE(rec_signsheader.PF_PURGE_IND,'Y',1,'N',0),0), 
					rec_signsheader.ID_CREATED, 
					rec_signsheader.DATE_TIME_CREATED, 
					rec_signsheader.ID_MODIFIED, 
					rec_signsheader.DATE_TIME_MODIFIED,
					v_active_cd
				);

				--DBMS_OUTPUT.PUT_LINE ('Added EVENT NAME:' || rec_signsheader.PF_EVENT_NAME);  

			EXCEPTION 
				WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;

				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg ); 

				INSERT INTO OM_PF_EVENT_HEADER_BAD 
				( 
				PF_EVENT_ID , 
				PF_EVENT_NAME , 
				PF_SHOW_IN_STORE , 
				PF_EXPIRY_DATE , 
				ID_CREATED , 
				DATE_TIME_CREATED , 
				ID_MODIFIED , 
				DATE_TIME_MODIFIED , 
				PF_SHOW_ON_STORE_TYPE , 
				PF_PURGE_IND,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM
				)	
				VALUES  
				(  
				rec_signsheader.PF_EVENT_ID ,
				rec_signsheader.PF_EVENT_NAME ,
				rec_signsheader.PF_SHOW_IN_STORE ,
				rec_signsheader.PF_EXPIRY_DATE ,
				rec_signsheader.ID_CREATED ,
				rec_signsheader.DATE_TIME_CREATED ,
				rec_signsheader.ID_MODIFIED ,
				rec_signsheader.DATE_TIME_MODIFIED ,
				rec_signsheader.PF_SHOW_ON_STORE_TYPE ,
				rec_signsheader.PF_PURGE_IND ,
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

				INSERT INTO OM_PF_EVENT_HEADER_BAD 
				( 
				PF_EVENT_ID , 
				PF_EVENT_NAME , 
				PF_SHOW_IN_STORE , 
				PF_EXPIRY_DATE , 
				ID_CREATED , 
				DATE_TIME_CREATED , 
				ID_MODIFIED , 
				DATE_TIME_MODIFIED , 
				PF_SHOW_ON_STORE_TYPE , 
				PF_PURGE_IND,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM
				)
				VALUES  
				(  
				rec_signsheader.PF_EVENT_ID ,
				rec_signsheader.PF_EVENT_NAME ,
				rec_signsheader.PF_SHOW_IN_STORE ,
				rec_signsheader.PF_EXPIRY_DATE ,
				rec_signsheader.ID_CREATED ,
				rec_signsheader.DATE_TIME_CREATED ,
				rec_signsheader.ID_MODIFIED ,
				rec_signsheader.DATE_TIME_MODIFIED ,
				rec_signsheader.PF_SHOW_ON_STORE_TYPE ,
				rec_signsheader.PF_PURGE_IND ,
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
