SET SERVEROUTPUT ON;
-- UPDATE / ADD Signs Distribution IN OM_SIGNS_DIST_CRIT

DECLARE
	CURSOR c_signsdistcrit
	IS
	SELECT 	 
		OM_SIGNS_DISTRIBUTION_MAPPING.SYS_DISTRIBUTION_ID,
		OM_PF_EVENT_DIST_CRIT_TMP.PF_DISTRIBUTION_ID,
		OM_PF_EVENT_DIST_CRIT_TMP.PF_CRITERIA_VALUE,
		OM_PF_EVENT_DIST_CRIT_TMP.ID_CREATED,
		OM_PF_EVENT_DIST_CRIT_TMP.DATE_TIME_CREATED,
		OM_PF_EVENT_DIST_CRIT_TMP.ID_MODIFIED,
		OM_PF_EVENT_DIST_CRIT_TMP.DATE_TIME_MODIFIED
	FROM OM_PF_EVENT_DIST_CRIT_TMP, OM_SIGNS_DISTRIBUTION_MAPPING
	WHERE OM_PF_EVENT_DIST_CRIT_TMP.PF_DISTRIBUTION_ID = OM_SIGNS_DISTRIBUTION_MAPPING.SYS_DISTRIBUTION_ID;	

	-- DEFINE THE RECORD
	rec_signsdistcrit c_signsdistcrit%ROWTYPE;
	-- DEFINE VARIABLES
	v_syseventdistcritid OM_SIGNS_DIST_CRIT.SYS_EVENT_DIST_CRIT_ID%TYPE:= 0;

	v_counter NUMBER := 0;
	v_commitinterval NUMBER := 20;
	v_active_cd NUMBER := 1;
	v_err_msg VARCHAR2(200) := '';
	v_err_code VARCHAR2(10) := '';
	
BEGIN
	OPEN c_signsdistcrit;
	LOOP
		FETCH c_signsdistcrit INTO rec_signsdistcrit;
		EXIT WHEN c_signsdistcrit%NOTFOUND OR c_signsdistcrit%NOTFOUND IS NULL;
		--DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Criteria Value=' || rec_signsdistcrit.PF_CRITERIA_VALUE);
		BEGIN
			-- CHECK IF RECORD EXISTS
			SELECT SYS_EVENT_DIST_CRIT_ID INTO v_syseventdistcritid 
			FROM OM_SIGNS_DIST_CRIT 
			WHERE SYS_DISTRIBUTION_ID = rec_signsdistcrit.SYS_DISTRIBUTION_ID; 


			-- UPDATE OM_SIGNS_DIST_CRIT
			UPDATE OM_SIGNS_DIST_CRIT 
			SET 
			CRITERIA_VALUE = rec_signsdistcrit.PF_CRITERIA_VALUE,
			CREATE_USER_ID = rec_signsdistcrit.ID_CREATED,
			CREATE_DTTM = rec_signsdistcrit.DATE_TIME_CREATED,
			UPDATE_USER_ID = rec_signsdistcrit.ID_MODIFIED,
			UPDATE_DTTM = rec_signsdistcrit.DATE_TIME_MODIFIED,
			ACTIVE_CD = v_active_cd
			WHERE  SYS_EVENT_DIST_CRIT_ID = v_syseventdistcritid;

			--DBMS_OUTPUT.PUT_LINE ('Updated Criteria Value=' || rec_signsdistcrit.PF_CRITERIA_VALUE) ;       
		EXCEPTION
			-- INSERT MACHINE
			WHEN NO_DATA_FOUND THEN
			BEGIN

				INSERT INTO OM_SIGNS_DIST_CRIT 
				( 
				SYS_EVENT_DIST_CRIT_ID ,
				SYS_DISTRIBUTION_ID,
				CRITERIA_VALUE,
				CREATE_USER_ID ,
				CREATE_DTTM ,
				UPDATE_USER_ID ,
				UPDATE_DTTM ,
				ACTIVE_CD
				) 
				VALUES 
				(
				OM_SIGNS_DIST_CRIT_SEQ.NEXTVAL,
				rec_signsdistcrit.SYS_DISTRIBUTION_ID,
				rec_signsdistcrit.PF_CRITERIA_VALUE,
				rec_signsdistcrit.ID_CREATED ,
				rec_signsdistcrit.DATE_TIME_CREATED ,
				rec_signsdistcrit.ID_MODIFIED ,
				rec_signsdistcrit.DATE_TIME_MODIFIED ,
				v_active_cd

				);

				--DBMS_OUTPUT.PUT_LINE ('Added Criteria Value=' || rec_signsdistcrit.PF_CRITERIA_VALUE) ; 
			EXCEPTION	
			WHEN OTHERS THEN
				v_err_msg := SUBSTR(SQLERRM, 1 , 200);
				v_err_code := SQLCODE ;
				--DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	


				INSERT INTO OM_PF_EVENT_DIST_CRIT_BAD 
				( 
				PF_DISTRIBUTION_ID,
				PF_CRITERIA_VALUE,
				ID_CREATED ,
				DATE_TIME_CREATED ,
				ID_MODIFIED ,
				DATE_TIME_MODIFIED ,
				EXCEPTION_CODE ,
				EXCEPTION_MSSG ,
				EXCEPTION_DTTM 
				) 
				VALUES  
				(  
				rec_signsdistcrit.PF_DISTRIBUTION_ID ,
				rec_signsdistcrit.PF_CRITERIA_VALUE ,
				rec_signsdistcrit.ID_CREATED ,
				rec_signsdistcrit.DATE_TIME_CREATED ,
				rec_signsdistcrit.ID_MODIFIED ,
				rec_signsdistcrit.DATE_TIME_MODIFIED,
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

			INSERT INTO OM_PF_EVENT_DIST_CRIT_BAD 
			( 
			PF_DISTRIBUTION_ID,
			PF_CRITERIA_VALUE,
			ID_CREATED ,
			DATE_TIME_CREATED ,
			ID_MODIFIED ,
			DATE_TIME_MODIFIED ,
			EXCEPTION_CODE ,
			EXCEPTION_MSSG ,
			EXCEPTION_DTTM 
			) 
			VALUES  
			(  
			rec_signsdistcrit.PF_DISTRIBUTION_ID ,
			rec_signsdistcrit.PF_CRITERIA_VALUE ,
			rec_signsdistcrit.ID_CREATED ,
			rec_signsdistcrit.DATE_TIME_CREATED ,
			rec_signsdistcrit.ID_MODIFIED ,
			rec_signsdistcrit.DATE_TIME_MODIFIED ,
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

	INSERT INTO OM_PF_EVENT_DIST_CRIT_BAD 
	( 
	PF_DISTRIBUTION_ID,
	PF_CRITERIA_VALUE,
	ID_CREATED ,
	DATE_TIME_CREATED ,
	ID_MODIFIED ,
	DATE_TIME_MODIFIED ,
	EXCEPTION_CODE ,
	EXCEPTION_MSSG ,
	EXCEPTION_DTTM 
	) 
	SELECT 	 
	"EVENT_DIST".PF_DISTRIBUTION_ID ,
	"EVENT_DIST".PF_CRITERIA_VALUE ,
	"EVENT_DIST".ID_CREATED ,
	"EVENT_DIST".DATE_TIME_CREATED ,
	"EVENT_DIST".ID_MODIFIED ,
	"EVENT_DIST".DATE_TIME_MODIFIED,
	'000' ,
	'Master records not found' ,
	SYSDATE
	FROM OM_PF_EVENT_DIST_CRIT_TMP "EVENT_DIST"
	WHERE NOT EXISTS
	( SELECT 1
	FROM OM_PF_EVENT_DIST_CRIT_TMP, OM_SIGNS_DISTRIBUTION_MAPPING
	WHERE OM_PF_EVENT_DIST_CRIT_TMP.PF_DISTRIBUTION_ID=OM_SIGNS_DISTRIBUTION_MAPPING.SYS_DISTRIBUTION_ID
	AND EVENT_DIST.PF_DISTRIBUTION_ID = OM_PF_EVENT_DIST_CRIT_TMP.PF_DISTRIBUTION_ID
	);
	COMMIT;
	--DBMS_OUTPUT.PUT_LINE ('Committed ' || TO_CHAR(SQL%ROWCOUNT) || 'mismatching records'); 
	EXCEPTION
	WHEN OTHERS THEN
	DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);

END;
/
