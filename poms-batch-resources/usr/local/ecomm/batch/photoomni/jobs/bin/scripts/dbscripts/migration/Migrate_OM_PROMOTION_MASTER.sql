SET SERVEROUTPUT ON;
-- UPDATE / ADD RECORDS INTO Om_Promotion_Master
DECLARE
  CURSOR C_Promotion_Master
  IS
    SELECT To_Number(Pf_Master_Rule) Pf_Master_Rule,
      PF_RULE_SEQUENCE,
      SUBSTR(PF_RULE_SCREEN_DESC,1,400) PF_RULE_SCREEN_DESC,
      PF_PROMOTION_TYPE,
      PF_ASSOCIATED_RULE,
      PF_SETS_IND,
      PF_MASTER_RULE_DESC,
      PF_PROMOTION_DESC,
      PF_PROMOTION_VALUE,
      PF_EXCLUSIVITY_IND,
      PF_RETAIL_PRICE_CODE,
      PF_PROMOTION_LEVEL,
      PF_MINIMUM_ORDER_RETAIL,
      PF_MINIMUM_BASKET_RETAIL,
      PF_MIN_TRIGGER_PRD_QTY,
      PF_MIN_TARGET_PRD_QTY,
      PF_MAX_TRIGGER_PRD_QTY,
      PF_MAX_TARGET_PRD_QTY,
      PF_TARGET_PRD_DISCOUNT,
      PF_TARGET_PRD_DISCNT_UNIT,
      PF_TRIGGER_PRD_DISCOUNT,
      PF_TRGGR_PRD_DISCNT_UNIT,
      PF_ACROSS_MARKTBASKET_IND,
      PF_DISCNT_APPLICABLE_QTY,
      PF_DISCNT_APPLICABLE_LVL,
      PF_PROCESSING_TYPE_ID,
      PF_PROMOTION_REMARKS,
      PF_NBR_OF_ITERATIONS,
      PF_IS_INPUT_DRIVEN,
      PF_IS_COMBO_RULE,
      PF_TRGT_DISCNT_MULTIPLE,
      ID_CREATED,
      DATE_TIME_CREATED,
      ID_MODIFIED,
      DATE_TIME_MODIFIED
    FROM Om_Pf_Promotion_Rule_Tmp
    ORDER BY PF_MASTER_RULE ASC;
  -- DEFINE THE RECORD
  rec_Promotion_Master C_Promotion_Master%ROWTYPE;
  -- DEFINE VARIABLES
  V_Sys_Promo_Master_Id Om_Promotion_Master.SYS_PROMO_MASTER_ID%Type;
  V_Counter        NUMBER        := 0;
  v_commitinterval NUMBER        := 20;
  v_active_cd      NUMBER        := 1;
  v_err_msg        VARCHAR2(200) := '';
  v_err_code       VARCHAR2(10)  := '';
BEGIN
  OPEN C_Promotion_Master;
  LOOP
    FETCH C_Promotion_Master INTO rec_Promotion_Master;
    EXIT
  WHEN C_Promotion_Master%NOTFOUND OR C_Promotion_Master%NOTFOUND IS NULL;
    BEGIN
      -- CHECK IF RECORD EXISTS
      SELECT SYS_PROMO_MASTER_ID
      INTO V_Sys_Promo_Master_Id
      FROM Om_Promotion_Master
      WHERE MASTER_RULE_NBR=rec_Promotion_Master.PF_MASTER_RULE;
      -- UPDATE EQUIPMENT
      UPDATE Om_Promotion_Master
      SET Master_Rule_Nbr       = Rec_Promotion_Master.Pf_Master_Rule ,
        Screen_Desc             = Rec_Promotion_Master.Pf_Rule_Screen_Desc ,
        Description             = Rec_Promotion_Master.Pf_Master_Rule_Desc ,
        Example                 = Rec_Promotion_Master.Pf_Promotion_Desc ,
        Active_Cd               = v_active_cd,
        CREATE_USER_ID          = Rec_Promotion_Master.ID_CREATED,
        CREATE_DTTM             = Rec_Promotion_Master.DATE_TIME_CREATED,
        UPDATE_USER_ID          = Rec_Promotion_Master.ID_MODIFIED,
        UPDATE_DTTM             = Rec_Promotion_Master.DATE_TIME_MODIFIED
      WHERE SYS_PROMO_MASTER_ID = V_Sys_Promo_Master_Id;
    EXCEPTION
      -- INSERT EQUIPMENT
    WHEN NO_DATA_FOUND THEN
      BEGIN
        INSERT
        INTO Om_Promotion_Master
          (
            SYS_PROMO_MASTER_ID ,
            MASTER_RULE_NBR ,
            SCREEN_DESC ,
            DESCRIPTION ,
            EXAMPLE ,
            ACTIVE_CD ,
            CREATE_USER_ID ,
            CREATE_DTTM ,
            UPDATE_USER_ID ,
            UPDATE_DTTM
          )
          VALUES
          (
            Om_Promotion_Master_Seq.Nextval ,
            Rec_Promotion_Master.Pf_Master_Rule ,
            Rec_Promotion_Master.Pf_Rule_Screen_Desc ,
            Rec_Promotion_Master.Pf_Master_Rule_Desc ,
            Rec_Promotion_Master.Pf_Promotion_Desc ,
            v_active_cd,
            Rec_Promotion_Master.ID_CREATED,
            Rec_Promotion_Master.DATE_TIME_CREATED,
            Rec_Promotion_Master.ID_MODIFIED,
            Rec_Promotion_Master.DATE_TIME_MODIFIED
          );
      EXCEPTION
      WHEN OTHERS THEN
        v_err_msg  := SUBSTR(SQLERRM, 1 , 200);
        v_err_code := SQLCODE ;
        INSERT
        INTO OM_PF_PROMOTION_RULE_BAD
          (
            PF_MASTER_RULE,
            PF_RULE_SEQUENCE,
            PF_RULE_SCREEN_DESC,
            PF_PROMOTION_TYPE,
            PF_ASSOCIATED_RULE,
            PF_SETS_IND,
            PF_MASTER_RULE_DESC,
            PF_PROMOTION_DESC,
            PF_PROMOTION_VALUE,
            PF_EXCLUSIVITY_IND,
            PF_RETAIL_PRICE_CODE,
            PF_PROMOTION_LEVEL,
            PF_MINIMUM_ORDER_RETAIL,
            PF_MINIMUM_BASKET_RETAIL,
            PF_MIN_TRIGGER_PRD_QTY,
            PF_MIN_TARGET_PRD_QTY,
            PF_MAX_TRIGGER_PRD_QTY,
            PF_MAX_TARGET_PRD_QTY,
            PF_TARGET_PRD_DISCOUNT,
            PF_TARGET_PRD_DISCNT_UNIT,
            PF_TRIGGER_PRD_DISCOUNT,
            PF_TRGGR_PRD_DISCNT_UNIT,
            PF_ACROSS_MARKTBASKET_IND,
            PF_DISCNT_APPLICABLE_QTY,
            PF_DISCNT_APPLICABLE_LVL,
            PF_PROCESSING_TYPE_ID,
            PF_PROMOTION_REMARKS,
            PF_NBR_OF_ITERATIONS,
            PF_IS_INPUT_DRIVEN,
            PF_IS_COMBO_RULE,
            PF_TRGT_DISCNT_MULTIPLE,
            ID_CREATED,
            DATE_TIME_CREATED,
            ID_MODIFIED,
            DATE_TIME_MODIFIED,
            EXCEPTION_CODE,
            EXCEPTION_MSSG,
            EXCEPTION_DTTM
          )
          VALUES
          (
            Rec_Promotion_Master.PF_MASTER_RULE,
            Rec_Promotion_Master.PF_RULE_SEQUENCE,
            Rec_Promotion_Master.PF_RULE_SCREEN_DESC,
            Rec_Promotion_Master.PF_PROMOTION_TYPE,
            Rec_Promotion_Master.PF_ASSOCIATED_RULE,
            Rec_Promotion_Master.PF_SETS_IND,
            Rec_Promotion_Master.PF_MASTER_RULE_DESC,
            Rec_Promotion_Master.PF_PROMOTION_DESC,
            Rec_Promotion_Master.PF_PROMOTION_VALUE,
            Rec_Promotion_Master.PF_EXCLUSIVITY_IND,
            Rec_Promotion_Master.PF_RETAIL_PRICE_CODE,
            Rec_Promotion_Master.PF_PROMOTION_LEVEL,
            Rec_Promotion_Master.PF_MINIMUM_ORDER_RETAIL,
            Rec_Promotion_Master.PF_MINIMUM_BASKET_RETAIL,
            Rec_Promotion_Master.PF_MIN_TRIGGER_PRD_QTY,
            Rec_Promotion_Master.PF_MIN_TARGET_PRD_QTY,
            Rec_Promotion_Master.PF_MAX_TRIGGER_PRD_QTY,
            Rec_Promotion_Master.PF_MAX_TARGET_PRD_QTY,
            Rec_Promotion_Master.PF_TARGET_PRD_DISCOUNT,
            Rec_Promotion_Master.PF_TARGET_PRD_DISCNT_UNIT,
            Rec_Promotion_Master.PF_TRIGGER_PRD_DISCOUNT,
            Rec_Promotion_Master.PF_TRGGR_PRD_DISCNT_UNIT,
            Rec_Promotion_Master.PF_ACROSS_MARKTBASKET_IND,
            Rec_Promotion_Master.PF_DISCNT_APPLICABLE_QTY,
            Rec_Promotion_Master.PF_DISCNT_APPLICABLE_LVL,
            Rec_Promotion_Master.PF_PROCESSING_TYPE_ID,
            Rec_Promotion_Master.PF_PROMOTION_REMARKS,
            Rec_Promotion_Master.PF_NBR_OF_ITERATIONS,
            Rec_Promotion_Master.PF_IS_INPUT_DRIVEN,
            Rec_Promotion_Master.PF_IS_COMBO_RULE,
            Rec_Promotion_Master.PF_TRGT_DISCNT_MULTIPLE,
            Rec_Promotion_Master.ID_CREATED,
            Rec_Promotion_Master.DATE_TIME_CREATED,
            Rec_Promotion_Master.ID_MODIFIED,
            Rec_Promotion_Master.DATE_TIME_MODIFIED ,
            v_err_code ,
            v_err_msg ,
            SYSDATE
          );
        v_counter := v_counter - 1;
      END;
    WHEN OTHERS THEN
      v_err_msg  := SUBSTR(SQLERRM, 1 , 200);
      v_err_code := SQLCODE ;
      INSERT
      INTO OM_PF_PROMOTION_RULE_BAD
        (
          PF_MASTER_RULE,
          PF_RULE_SEQUENCE,
          PF_RULE_SCREEN_DESC,
          PF_PROMOTION_TYPE,
          PF_ASSOCIATED_RULE,
          PF_SETS_IND,
          PF_MASTER_RULE_DESC,
          PF_PROMOTION_DESC,
          PF_PROMOTION_VALUE,
          PF_EXCLUSIVITY_IND,
          PF_RETAIL_PRICE_CODE,
          PF_PROMOTION_LEVEL,
          PF_MINIMUM_ORDER_RETAIL,
          PF_MINIMUM_BASKET_RETAIL,
          PF_MIN_TRIGGER_PRD_QTY,
          PF_MIN_TARGET_PRD_QTY,
          PF_MAX_TRIGGER_PRD_QTY,
          PF_MAX_TARGET_PRD_QTY,
          PF_TARGET_PRD_DISCOUNT,
          PF_TARGET_PRD_DISCNT_UNIT,
          PF_TRIGGER_PRD_DISCOUNT,
          PF_TRGGR_PRD_DISCNT_UNIT,
          PF_ACROSS_MARKTBASKET_IND,
          PF_DISCNT_APPLICABLE_QTY,
          PF_DISCNT_APPLICABLE_LVL,
          PF_PROCESSING_TYPE_ID,
          PF_PROMOTION_REMARKS,
          PF_NBR_OF_ITERATIONS,
          PF_IS_INPUT_DRIVEN,
          PF_IS_COMBO_RULE,
          PF_TRGT_DISCNT_MULTIPLE,
          ID_CREATED,
          DATE_TIME_CREATED,
          ID_MODIFIED,
          DATE_TIME_MODIFIED,
          EXCEPTION_CODE,
          EXCEPTION_MSSG,
          EXCEPTION_DTTM
        )
        VALUES
        (
          Rec_Promotion_Master.PF_MASTER_RULE,
          Rec_Promotion_Master.PF_RULE_SEQUENCE,
          Rec_Promotion_Master.PF_RULE_SCREEN_DESC,
          Rec_Promotion_Master.PF_PROMOTION_TYPE,
          Rec_Promotion_Master.PF_ASSOCIATED_RULE,
          Rec_Promotion_Master.PF_SETS_IND,
          Rec_Promotion_Master.PF_MASTER_RULE_DESC,
          Rec_Promotion_Master.PF_PROMOTION_DESC,
          Rec_Promotion_Master.PF_PROMOTION_VALUE,
          Rec_Promotion_Master.PF_EXCLUSIVITY_IND,
          Rec_Promotion_Master.PF_RETAIL_PRICE_CODE,
          Rec_Promotion_Master.PF_PROMOTION_LEVEL,
          Rec_Promotion_Master.PF_MINIMUM_ORDER_RETAIL,
          Rec_Promotion_Master.PF_MINIMUM_BASKET_RETAIL,
          Rec_Promotion_Master.PF_MIN_TRIGGER_PRD_QTY,
          Rec_Promotion_Master.PF_MIN_TARGET_PRD_QTY,
          Rec_Promotion_Master.PF_MAX_TRIGGER_PRD_QTY,
          Rec_Promotion_Master.PF_MAX_TARGET_PRD_QTY,
          Rec_Promotion_Master.PF_TARGET_PRD_DISCOUNT,
          Rec_Promotion_Master.PF_TARGET_PRD_DISCNT_UNIT,
          Rec_Promotion_Master.PF_TRIGGER_PRD_DISCOUNT,
          Rec_Promotion_Master.PF_TRGGR_PRD_DISCNT_UNIT,
          Rec_Promotion_Master.PF_ACROSS_MARKTBASKET_IND,
          Rec_Promotion_Master.PF_DISCNT_APPLICABLE_QTY,
          Rec_Promotion_Master.PF_DISCNT_APPLICABLE_LVL,
          Rec_Promotion_Master.PF_PROCESSING_TYPE_ID,
          Rec_Promotion_Master.PF_PROMOTION_REMARKS,
          Rec_Promotion_Master.PF_NBR_OF_ITERATIONS,
          Rec_Promotion_Master.PF_IS_INPUT_DRIVEN,
          Rec_Promotion_Master.PF_IS_COMBO_RULE,
          Rec_Promotion_Master.PF_TRGT_DISCNT_MULTIPLE,
          Rec_Promotion_Master.ID_CREATED,
          Rec_Promotion_Master.DATE_TIME_CREATED,
          Rec_Promotion_Master.ID_MODIFIED,
          Rec_Promotion_Master.DATE_TIME_MODIFIED ,
          v_err_code ,
          v_err_msg ,
          SYSDATE
        );
      v_counter := v_counter - 1;
    END;
    v_counter    := v_counter + 1;
    IF (v_counter = v_commitinterval) THEN
      COMMIT;
      v_counter := 0;
    END IF;
  END LOOP;
  COMMIT;
EXCEPTION
WHEN OTHERS THEN
  DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
END;
/
