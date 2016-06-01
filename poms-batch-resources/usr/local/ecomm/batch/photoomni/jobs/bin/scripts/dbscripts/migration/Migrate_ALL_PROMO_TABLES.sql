SET Serveroutput ON;
-- UPDATE / ADD RECORDS INTO OM_PROMOTION, OM_PROMOTION_ATTRIBUTE, OM_PROMO_TRGGR_PROD_ASSOC, OM_PROMO_TRGT_PROD_ASSOC
DECLARE
  CURSOR C_Promotion
  IS
    SELECT PF_PLU,
      PF_PROMOTION_TYPE,
      PF_NEXT_ACTIVATION_DTTM,
      PF_EXPIRE_DTTM,
      PF_ASSOCIATED_RULE,
      PF_PROMOTION_ACTIVE_IND,
      PF_SETS_IND,
      PF_ACTIVE_IND,
      PF_DEACTIVATION_DTTM,
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
      PF_IMMEDIATE_PUTAWAY_IND,
      PF_PROCESSING_TYPE_ID,
      PF_PROMOTION_REMARKS,
      PF_MASTER_RULE,
      PF_NBR_OF_ITERATIONS,
      PF_IS_INPUT_DRIVEN,
      PF_IS_COMBO_RULE,
      PF_TRGT_DISCNT_MULTIPLE,
      PF_COUPON_CODE,
      PF_SHOW_ON_KIOSK,
      PF_DISCLAIMER,
      PF_IMAGE_URL,
      PF_START_DTTM,
      PF_END_DTTM,
      PF_CHANNEL,
      PF_TRANSFER_IND,
      ID_CREATED,
      DATE_TIME_CREATED,
      ID_MODIFIED,
      DATE_TIME_MODIFIED
    FROM Om_Pf_Promotion_Tmp
    ORDER BY PF_PLU ASC;
  -- DEFINE THE RECORD
  Rec_Promotion C_Promotion%Rowtype;
  -- DEFINE VARIABLES
  V_Sys_Plu_Id Om_Promotion.Sys_Plu_Id%Type;
  V_Sys_Promo_Master_Id Om_Promotion_Master.Sys_Promo_Master_Id%Type;
  V_Sys_Active_Cd Om_Promotion_Master.active_cd%Type;
  V_Counter        NUMBER        := 0;
  v_commitinterval NUMBER        := 20;
  V_Active_Cd      NUMBER        := 1;
  v_Inactive_cd    NUMBER        := 0;
  v_err_msg        VARCHAR2(200) := '';
  v_err_code       VARCHAR2(10)  := '';
BEGIN
  OPEN C_Promotion;
  LOOP
    FETCH C_Promotion INTO Rec_Promotion;
    EXIT
  WHEN C_Promotion%NOTFOUND OR C_Promotion%NOTFOUND IS NULL;
    BEGIN
      --Reset Variables
      V_Sys_Plu_Id          := 0;
      V_Sys_Promo_Master_Id := 0;
      V_Sys_Active_Cd       := 0;
      -- DEACTIVE Existing PLU Records in OM_PROMOTION, OM_PROMOTION_ATTRIBUTE, OM_PROMO_TRGGR_PROD_ASSOC, OM_PROMO_TRGT_PROD_ASSOC
      UPDATE Om_Promo_Trgt_Prod_Assoc
      SET Active_Cd     = V_Inactive_Cd
      WHERE Sys_Plu_Id IN
        (SELECT Sys_Plu_Id
        FROM Om_Promotion
        WHERE Plu_Nbr = Rec_Promotion.Pf_Plu
        AND Active_Cd = V_Active_Cd
        );
      UPDATE Om_Promo_Trggr_Prod_Assoc
      SET Active_Cd     = V_Inactive_Cd
      WHERE Sys_Plu_Id IN
        (SELECT Sys_Plu_Id
        FROM Om_Promotion
        WHERE Plu_Nbr = Rec_Promotion.Pf_Plu
        AND Active_Cd = V_Active_Cd
        );
      UPDATE Om_Promotion_Attribute
      SET Active_Cd     = V_Inactive_Cd
      WHERE Sys_Plu_Id IN
        (SELECT Sys_Plu_Id
        FROM Om_Promotion
        WHERE Plu_Nbr = Rec_Promotion.Pf_Plu
        AND Active_Cd = V_Active_Cd
        );
      UPDATE Om_Promotion
      SET Active_Cd = V_Inactive_Cd
      WHERE Plu_Nbr = Rec_Promotion.Pf_Plu;
      --Incrementing SYS_PLU_ID
      SELECT OM_PROMOTION_SEQ.Nextval
      INTO V_Sys_Plu_Id
      FROM dual;
      --Identify Sys_Promo_Master_Id
      SELECT SYS_PROMO_MASTER_ID
      INTO V_Sys_Promo_Master_Id
      FROM Om_Promotion_Master
      WHERE Master_Rule_Nbr= To_Number(Rec_Promotion.Pf_Master_Rule);
      --Manipulate active_cd
      SELECT DECODE(Upper(Rec_Promotion.PF_ACTIVE_IND),'Y',1,'N',0)
      INTO V_Sys_Active_Cd
      FROM dual;
      --INSERT INTO OM_PROMOTION
      INSERT
      INTO Om_Promotion
        (
          Sys_Plu_Id,
          Plu_Nbr,
          Promotion_Type,
          Promotion_Creation_Date,
          Coupon_Code,
          Promotion_Desc,
          Promo_Channel,
          Active_Cd,
          Promotion_Remarks,
          Show_On_Kiosk_Cd,
          Disclaimer,
          Image_Url,
          Start_Dttm,
          End_Dttm,
          Internet_End_Date,
          Create_User_Id,
          Create_Dttm,
          Update_User_Id,
          Update_Dttm
        )
        VALUES
        (
          V_Sys_Plu_Id,
          Rec_Promotion.Pf_Plu,
          Rec_Promotion.Pf_Promotion_Type,
          Rec_Promotion.Date_Time_Modified,
          Rec_Promotion.Pf_Coupon_Code,
          Rec_Promotion.Pf_Promotion_Desc,
          Rec_Promotion.Pf_Channel,
          V_Sys_Active_Cd,
          Rec_Promotion.Pf_Promotion_Remarks,
          DECODE(Upper(Rec_Promotion.Pf_Show_On_Kiosk),'Y',1,'N',0),
          Rec_Promotion.Pf_Disclaimer,
          Rec_Promotion.Pf_Image_Url,
          Rec_Promotion.Pf_Start_Dttm,
          Rec_Promotion.Pf_End_Dttm,
          NULL,
          Rec_Promotion.Id_Created,
          Rec_Promotion.DATE_TIME_CREATED,
          Rec_Promotion.Id_Modified,
          Rec_Promotion.Date_Time_Modified
        );
      --INSERT INTO Om_Promotion_Attribute
      INSERT
      INTO Om_Promotion_Attribute
        (
          Sys_Promotion_Attr_Id,
          Sys_Plu_Id,
          Associated_Rule,
          Sys_Promo_Master_Id,
          Promotion_Value,
          Retail_Price_Code,
          Promotion_Level,
          Minimum_Order_Retail,
          Minimum_Basket_Retail,
          Min_Trigger_Prd_Qty,
          Min_Target_Prd_Qty,
          Max_Trigger_Prd_Qty,
          Max_Target_Prd_Qty,
          Target_Prd_Discount,
          Target_Prd_Discnt_Unit,
          Trigger_Prd_Discount,
          Trggr_Prd_Discnt_Unit,
          Discnt_Applicable_Qty,
          Discnt_Applicable_Lvl,
          Processing_Type_Id,
          Nbr_Of_Iterations,
          Trgt_Discnt_Multiple,
          Sets_Cd,
          Exclusivity_Cd,
          Across_Shoppingcart_Cd,
          Input_Driven_Cd,
          Combo_Rule_Cd,
          Active_Cd,
          Create_User_Id,
          Create_Dttm,
          Update_User_Id,
          Update_Dttm
        )
        VALUES
        (
          OM_PROMOTION_ATTRIBUTE_SEQ.Nextval,
          V_Sys_Plu_Id,
          Rec_Promotion.Pf_Associated_Rule,
          v_Sys_Promo_Master_Id,
          Rec_Promotion.Pf_Promotion_Value,
          Rec_Promotion.Pf_Retail_Price_Code,
          Rec_Promotion.Pf_Promotion_Level,
          Rec_Promotion.Pf_Minimum_Order_Retail,
          Rec_Promotion.Pf_Minimum_Basket_Retail,
          Rec_Promotion.Pf_Min_Trigger_Prd_Qty,
          Rec_Promotion.Pf_Min_Target_Prd_Qty,
          Rec_Promotion.Pf_Max_Trigger_Prd_Qty,
          Rec_Promotion.Pf_Max_Target_Prd_Qty,
          Rec_Promotion.Pf_Target_Prd_Discount,
          Rec_Promotion.Pf_Target_Prd_Discnt_Unit,
          Rec_Promotion.Pf_Trigger_Prd_Discount,
          Rec_Promotion.Pf_Trggr_Prd_Discnt_Unit,
          Rec_Promotion.Pf_Discnt_Applicable_Qty,
          Rec_Promotion.Pf_Discnt_Applicable_Lvl,
          Rec_Promotion.Pf_Processing_Type_Id,
          Rec_Promotion.Pf_Nbr_Of_Iterations,
          Rec_Promotion.Pf_Trgt_Discnt_Multiple,
          DECODE(Upper(Rec_Promotion.Pf_Sets_Ind),'Y',1,'N',0),
          DECODE(Upper(Rec_Promotion.Pf_Exclusivity_Ind),'Y',1,'N',0),
          DECODE(Upper(Rec_Promotion.Pf_Across_Marktbasket_Ind),'Y',1,'N',0),
          DECODE(Upper(Rec_Promotion.PF_IS_INPUT_DRIVEN),'Y',1,'N',0),
          DECODE(Upper(Rec_Promotion.PF_IS_COMBO_RULE),'Y',1,'N',0),
          V_Sys_Active_Cd,
          Rec_Promotion.Id_Created,
          Rec_Promotion.DATE_TIME_CREATED,
          Rec_Promotion.Id_Modified,
          Rec_Promotion.Date_Time_Modified
        );
      --INSERT INTO Om_Promo_Trgt_Prod_Assoc
      INSERT
      INTO OM_PROMO_TRGT_PROD_ASSOC
        (
          SYS_PROMO_TRGT_PROD_ASSOC_ID,
          SYS_PLU_ID,
          TARGET_PRODUCT_ID,
          ACTIVE_CD,
          CREATE_USER_ID,
          CREATE_DTTM,
          UPDATE_USER_ID,
          UPDATE_DTTM
        )
      SELECT OM_PROMO_TRGT_PROD_ASSOC_SEQ.Nextval,
        V_Sys_Plu_Id,
        Sys_Product_Id,
        V_Sys_Active_Cd,
        a.Id_Created,
        a.DATE_TIME_CREATED,
        a.Id_Modified,
        a.Date_Time_Modified
      FROM Om_Pf_Promo_Tgt_Prod_Assoc_Tmp A,
        Om_Product B
      WHERE A.Pf_Target_Product_Id = B.Product_Nbr
      AND A.PF_PLU                 = Rec_Promotion.Pf_Plu;
      --INSERT INTO OM_PROMO_TRGGR_PROD_ASSOC_SEQ
      INSERT
      INTO OM_PROMO_TRGGR_PROD_ASSOC
        (
          Sys_Promo_Trggr_Prod_Assoc_Id,
          Sys_Plu_Id,
          Trigger_Product_Id,
          Min_Trigger_Prd_Qty,
          TRIGGER_PRD_DISCOUNT,
          ACTIVE_CD,
          CREATE_USER_ID,
          CREATE_DTTM,
          UPDATE_USER_ID,
          UPDATE_DTTM
        )
      SELECT OM_PROMO_TRGGR_PROD_ASSOC_SEQ.Nextval,
        V_Sys_Plu_Id,
        Sys_Product_Id,
        A.Pf_Min_Trigger_Prd_Qty,
        A.Pf_Trigger_Prd_Discount,
        V_Sys_Active_Cd,
        a.Id_Created,
        a.DATE_TIME_CREATED,
        a.Id_Modified,
        a.Date_Time_Modified
      FROM Om_Pf_Promo_Trgr_Prd_Assoc_Tmp A,
        Om_Product B
      WHERE A.PF_TRIGGER_PRODUCT_ID = B.Product_Nbr
      AND A.PF_PLU                  = Rec_Promotion.Pf_Plu;
    EXCEPTION
    WHEN OTHERS THEN
      v_err_msg  := SUBSTR(SQLERRM, 1 , 200);
      v_err_code := SQLCODE ;
      INSERT
      INTO OM_PF_PROMOTION_BAD
        (
          Pf_Plu,
          Pf_Promotion_Type,
          Pf_Next_Activation_Dttm,
          Pf_Expire_Dttm,
          Pf_Associated_Rule,
          Pf_Promotion_Active_Ind,
          Pf_Sets_Ind,
          Pf_Active_Ind,
          Pf_Deactivation_Dttm,
          Pf_Promotion_Desc,
          Pf_Promotion_Value,
          Pf_Exclusivity_Ind,
          Pf_Retail_Price_Code,
          Pf_Promotion_Level,
          Pf_Minimum_Order_Retail,
          Pf_Minimum_Basket_Retail,
          Pf_Min_Trigger_Prd_Qty,
          Pf_Min_Target_Prd_Qty,
          Pf_Max_Trigger_Prd_Qty,
          Pf_Max_Target_Prd_Qty,
          Pf_Target_Prd_Discount,
          Pf_Target_Prd_Discnt_Unit,
          Pf_Trigger_Prd_Discount,
          Pf_Trggr_Prd_Discnt_Unit,
          Pf_Across_Marktbasket_Ind,
          Pf_Discnt_Applicable_Qty,
          Pf_Discnt_Applicable_Lvl,
          Pf_Transfer_Ind,
          Pf_Immediate_Putaway_Ind,
          Id_Created,
          Date_Time_Created,
          Id_Modified,
          Date_Time_Modified,
          Pf_Processing_Type_Id,
          Pf_Promotion_Remarks,
          Pf_Master_Rule,
          Pf_Nbr_Of_Iterations,
          Pf_Is_Input_Driven,
          Pf_Is_Combo_Rule,
          Pf_Trgt_Discnt_Multiple,
          Pf_Coupon_Code,
          Pf_Show_On_Kiosk,
          Pf_Disclaimer,
          Pf_Image_Url,
          Pf_Start_Dttm,
          Pf_End_Dttm,
          Pf_Channel
        )
        VALUES
        (
          Rec_Promotion.PF_PLU,
          Rec_Promotion.PF_PROMOTION_TYPE,
          Rec_Promotion.PF_NEXT_ACTIVATION_DTTM,
          Rec_Promotion.PF_EXPIRE_DTTM,
          Rec_Promotion.PF_ASSOCIATED_RULE,
          Rec_Promotion.PF_PROMOTION_ACTIVE_IND,
          Rec_Promotion.PF_SETS_IND,
          Rec_Promotion.PF_ACTIVE_IND,
          Rec_Promotion.PF_DEACTIVATION_DTTM,
          Rec_Promotion.PF_PROMOTION_DESC,
          Rec_Promotion.PF_PROMOTION_VALUE,
          Rec_Promotion.PF_EXCLUSIVITY_IND,
          Rec_Promotion.PF_RETAIL_PRICE_CODE,
          Rec_Promotion.PF_PROMOTION_LEVEL,
          Rec_Promotion.PF_MINIMUM_ORDER_RETAIL,
          Rec_Promotion.PF_MINIMUM_BASKET_RETAIL,
          Rec_Promotion.PF_MIN_TRIGGER_PRD_QTY,
          Rec_Promotion.PF_MIN_TARGET_PRD_QTY,
          Rec_Promotion.PF_MAX_TRIGGER_PRD_QTY,
          Rec_Promotion.PF_MAX_TARGET_PRD_QTY,
          Rec_Promotion.PF_TARGET_PRD_DISCOUNT,
          Rec_Promotion.PF_TARGET_PRD_DISCNT_UNIT,
          Rec_Promotion.PF_TRIGGER_PRD_DISCOUNT,
          Rec_Promotion.PF_TRGGR_PRD_DISCNT_UNIT,
          Rec_Promotion.PF_ACROSS_MARKTBASKET_IND,
          Rec_Promotion.PF_DISCNT_APPLICABLE_QTY,
          Rec_Promotion.PF_DISCNT_APPLICABLE_LVL,
          Rec_Promotion.PF_TRANSFER_IND,
          Rec_Promotion.PF_IMMEDIATE_PUTAWAY_IND,
          Rec_Promotion.ID_CREATED,
          Rec_Promotion.DATE_TIME_CREATED,
          Rec_Promotion.ID_MODIFIED,
          Rec_Promotion.DATE_TIME_MODIFIED,
          Rec_Promotion.PF_PROCESSING_TYPE_ID,
          Rec_Promotion.PF_PROMOTION_REMARKS,
          Rec_Promotion.PF_MASTER_RULE,
          Rec_Promotion.PF_NBR_OF_ITERATIONS,
          Rec_Promotion.PF_IS_INPUT_DRIVEN,
          Rec_Promotion.PF_IS_COMBO_RULE,
          Rec_Promotion.PF_TRGT_DISCNT_MULTIPLE,
          Rec_Promotion.PF_COUPON_CODE,
          Rec_Promotion.PF_SHOW_ON_KIOSK,
          Rec_Promotion.PF_DISCLAIMER,
          Rec_Promotion.PF_IMAGE_URL,
          Rec_Promotion.PF_START_DTTM,
          Rec_Promotion.PF_END_DTTM,
          Rec_Promotion.Pf_Channel
        );
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
