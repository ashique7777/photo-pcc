SET SERVEROUTPUT ON;
-- UPDATE / ADD records IN OM_PRODUCT_PROD_CAT_ASSOC

DECLARE
  CURSOR c_product_cat_assoc
  IS
	SELECT SYS_PRODUCT_ID,
	  (SELECT SYS_PRODUCT_CATEGORY_TYPE_ID
	  FROM Om_Product_Category_Type
	  WHERE PRODUCT_CATEGORY_TYPE = 'MASTER'
	  ) SYS_CATEGORY_TYPE_ID,
	  (SELECT SYS_PRODUCT_CATEGORY_LVL1_ID
	  FROM Om_Product_Category_Lvl1 a,
	    Om_Product_Category_Type b
	  WHERE a.SYS_CATEGORY_TYPE_ID = b.Sys_Product_Category_Type_Id
	  AND CATEGORY_NAME            = 'PHOTO'
	  ) SYS_PRODUCT_CATEGORY_LVL1_ID
	FROM OM_PF_PRODUCT_TMP A,
	  OM_PRODUCT B
	WHERE A.PF_PRODUCT_ID = B.PRODUCT_NBR
	UNION
	SELECT Sys_Product_Id,
	  Sys_Category_Type_Id,
	  Sys_Product_Category_Lvl1_Id
	FROM Om_Pf_Product_Tmp A,
	  Om_Product B,
	  Om_Product_Category_Lvl1 C,
	  Om_Product_Category_Type D
	WHERE A.PF_PRODUCT_ID       = B.PRODUCT_NBR
	AND A.PF_PRODUCT_CATEGORY   = C.CATEGORY_NAME
	AND C.SYS_CATEGORY_TYPE_ID  = D.Sys_Product_Category_Type_Id
	AND D.PRODUCT_CATEGORY_TYPE = 'SVC_CATEG'
	UNION
	SELECT SYS_PRODUCT_ID,
	  SYS_CATEGORY_TYPE_ID,
	  SYS_PRODUCT_CATEGORY_LVL1_ID
	FROM
	  (SELECT PF_PRODUCT_ID,
	    DECODE(PF_PROCESSING_TYPE_ID,1,'H',2,'N',3,'K',4,'S',5,'0',PF_PROCESSING_TYPE_ID) AS PF_PROCESSING_TYPE
	  FROM Om_Pf_Product_Tmp
	  ) A,
	  OM_PRODUCT B,
	  OM_PRODUCT_CATEGORY_LVL1 C,
	  Om_Product_Category_Type D
	WHERE A.Pf_Product_Id       = B.Product_Nbr
	AND A.PF_PROCESSING_TYPE    = C.CATEGORY_NAME
	AND C.SYS_CATEGORY_TYPE_ID  = D.Sys_Product_Category_Type_Id
	AND D.PRODUCT_CATEGORY_TYPE = 'PROC_TYPE';
	  
	  
  -- DEFINE THE RECORD
  rec_product_cat_assoc c_product_cat_assoc%ROWTYPE;
  -- DEFINE VARIABLES

  v_blank VARCHAR2(10):= ' ' ;

  v_counter NUMBER := 0;
  v_default_id OM_PRODUCT_PROD_CAT_ASSOC.CREATE_USER_ID%TYPE := 'SYSTEM';  
  v_default_date OM_PRODUCT_PROD_CAT_ASSOC.CREATE_DTTM%TYPE;  
  v_commitinterval NUMBER := 20;
  v_active_cd NUMBER := 1;
  v_err_msg VARCHAR2(200) := '';
  v_err_code VARCHAR2(10) := '';
BEGIN
  DBMS_OUTPUT.PUT_LINE ('BEGIN starts');
  OPEN c_product_cat_assoc;
  LOOP
    FETCH c_product_cat_assoc INTO rec_product_cat_assoc;
    EXIT WHEN c_product_cat_assoc%NOTFOUND OR c_product_cat_assoc%NOTFOUND IS NULL;
    DBMS_OUTPUT.PUT_LINE ('Processing the record --> ' || 'Product ID=' || rec_product_cat_assoc.SYS_PRODUCT_ID );
    BEGIN	
        
		SELECT TO_DATE('01-01-0001 01:01:01','MM-DD-YYYY HH:MI:SS') INTO v_default_date FROM DUAL;
		
		
		INSERT INTO OM_PRODUCT_PROD_CAT_ASSOC 
			( 
				SYS_PRODUCT_PROD_CAT_ASSOC_ID,
				SYS_PRODUCT_ID,
				SYS_CATEGORY_TYPE_ID,
				SYS_PRODUCT_CATEGORY_LVL1_ID,
				SYS_PRODUCT_CATEGORY_LVL2_ID,
				ACTIVE_CD,
				CREATE_USER_ID,
				CREATE_DTTM,
				UPDATE_USER_ID,
				UPDATE_DTTM
			 ) 
			 VALUES 
			 (
			     OM_PRODUCT_PROD_CAT_ASSOC_SEQ.NEXTVAL,
				 rec_product_cat_assoc.SYS_PRODUCT_ID,
				 rec_product_cat_assoc.SYS_CATEGORY_TYPE_ID,
				 rec_product_cat_assoc.SYS_PRODUCT_CATEGORY_LVL1_ID,
				 0 , -- THIS WILL BE POPULATED LATER
				 v_active_cd,
				 v_default_id,
				 v_default_date,
				 v_default_id,
				 v_default_date
			 );

        DBMS_OUTPUT.PUT_LINE ('Added SYS_PRODUCT_ID=' || rec_product_cat_assoc.SYS_PRODUCT_ID ); 
	EXCEPTION	
	WHEN OTHERS THEN
		v_err_msg := SUBSTR(SQLERRM, 1 , 200);
		v_err_code := SQLCODE ;
		DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || v_err_code || ' -ERROR- ' || v_err_msg);	
		

	END;
	v_counter := v_counter + 1;
    DBMS_OUTPUT.PUT_LINE ('Counter :' || v_counter);
    IF (v_counter = v_commitinterval) THEN        
        COMMIT;
        DBMS_OUTPUT.PUT_LINE ('Committed ' || v_counter || ' records');  
        v_counter := 0;
    END IF;
       
  END LOOP;
  COMMIT;
 
END;
/
