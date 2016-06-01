SET SERVEROUTPUT ON;
-- UPDATE / ADD RECORDS INTO OM_PRODUCT_MC_TYPE_EQUIP_TYPE
DECLARE
  CURSOR C_Product_Equip_Cost
  IS
    SELECT B.Sys_Product_Id,
      C.Sys_Equipment_Type_Id,
      A.Pf_Product_Id,
      A.Pf_Equipment_Id,
      A.Pf_Print_Cost,
      A.Pf_Development_Cost,
      A.Pf_Binding_Cover_Cost,
      A.Pf_Additional_Cost,
      A.PF_TRANSFER_IND,
      NVL(A.Id_Created,'SYSTEM') Id_Created,
      NVL(A.DATE_TIME_CREATED, SYSDATE) DATE_TIME_CREATED,
      A.ID_MODIFIED,
      A.Date_Time_Modified
    FROM Om_Pf_Product_Equip_cost_Tmp A
    LEFT JOIN Om_Product B
    ON A.Pf_Product_Id=B.Product_Nbr
    LEFT JOIN Om_Equipment_Type C
    ON A.Pf_Equipment_Id = C.Equipment_Type_Nbr;
  -- DEFINE THE RECORD
  Rec_Product_Equip_Cost C_Product_Equip_Cost%ROWTYPE;
  -- DEFINE VARIABLES
  v_rec_present    NUMBER        :=0 ;
  V_Counter        NUMBER        := 0;
  v_commitinterval NUMBER        := 20;
  V_Active_Cd      NUMBER        := 1;
  v_err_msg        VARCHAR2(200) := '';
  v_err_code       VARCHAR2(10)  := '';
BEGIN
  --Deactivate all records in Om_Product_Equip_Type_Cost pertaining to products in TMP tables
  UPDATE Om_Product_Equip_Type_Cost
  SET active_cd         = 0
  WHERE SYS_PRODUCT_ID IN
    (SELECT B.SYS_PRODUCT_ID
    FROM Om_Pf_Product_equip_cost_tmp A,
      Om_Product B
    WHERE A.Pf_Product_Id=B.Product_Nbr
    GROUP BY B.SYS_PRODUCT_ID
    );
  COMMIT;
  OPEN C_Product_Equip_Cost;
  LOOP
    FETCH C_Product_Equip_Cost INTO Rec_Product_Equip_Cost;
    EXIT
  WHEN C_Product_Equip_Cost%NOTFOUND OR C_Product_Equip_Cost%NOTFOUND IS NULL;
    BEGIN
      IF ((Rec_Product_Equip_Cost.Sys_Product_Id IS NOT NULL) AND (Rec_Product_Equip_Cost.Sys_Equipment_Type_Id IS NOT NULL)) THEN
        v_rec_present                            := 0;
        -- CHECK IF RECORD EXISTS
        SELECT COUNT(1)
        INTO v_rec_present
        FROM Om_Product_Equip_Type_Cost
        WHERE Sys_Product_Id      = Rec_Product_Equip_Cost.Sys_Product_Id
        AND Sys_Equipment_Type_Id = Rec_Product_Equip_Cost.Sys_Equipment_Type_Id;
        IF v_rec_present         <> 0 THEN
          UPDATE Om_Product_Equip_Type_Cost
          SET Active_Cd             = V_Active_Cd
          WHERE Sys_Product_Id      = Rec_Product_Equip_Cost.Sys_Product_Id
          AND Sys_Equipment_Type_Id = Rec_Product_Equip_Cost.Sys_Equipment_Type_Id;
        ELSE
          INSERT
          INTO Om_Product_Equip_Type_Cost
            (
              SYS_PROD_EQUIP_COST_ID,
              Sys_Product_Id,
              Sys_Equipment_Type_Id,
              Print_Cost,
              Development_Cost,
              Binding_Cover_Cost,
              ADDITIONAL_COST,
              Active_Cd,
              Create_User_Id,
              Create_Dttm,
              Update_User_Id,
              Update_Dttm
            )
            VALUES
            (
              OM_PRODUCT_EQUIP_TYPE_COST_SEQ.Nextval,
              Rec_Product_Equip_Cost.Sys_Product_Id,
              Rec_Product_Equip_Cost.Sys_Equipment_Type_Id ,
              Rec_Product_Equip_Cost.Pf_Print_Cost,
              Rec_Product_Equip_Cost.Pf_Development_Cost,
              Rec_Product_Equip_Cost.Pf_Binding_Cover_Cost,
              Rec_Product_Equip_Cost.Pf_Additional_Cost,
              V_Active_Cd,
              Rec_Product_Equip_Cost.Id_Created,
              Rec_Product_Equip_Cost.Date_Time_Created,
              Rec_Product_Equip_Cost.ID_MODIFIED,
              Rec_Product_Equip_Cost.DATE_TIME_MODIFIED
            );
        END IF;
      ELSE
        --Insert into BAD table
        INSERT
        INTO Om_Pf_Product_equip_cost_Bad
          (
            Pf_Product_Id,
            pf_Equipment_Id,
            Pf_Print_Cost,
            Pf_Development_Cost,
            Pf_Binding_Cover_Cost,
            Pf_Additional_Cost,
            Pf_Transfer_Ind,
            Id_Created,
            Date_Time_Created,
            Id_Modified,
            Date_Time_Modified,
            Exception_Mssg,
            Exception_Dttm,
            Exception_Code
          )
          VALUES
          (
            Rec_Product_Equip_Cost.Pf_Product_Id,
            Rec_Product_Equip_Cost.Pf_Equipment_Id,
            Rec_Product_Equip_Cost.Pf_Print_Cost,
            Rec_Product_Equip_Cost.Pf_Development_Cost,
            Rec_Product_Equip_Cost.Pf_Binding_Cover_Cost,
            Rec_Product_Equip_Cost.Pf_Additional_Cost,
            Rec_Product_Equip_Cost.Pf_Transfer_Ind,
            Rec_Product_Equip_Cost.Id_Created,
            Rec_Product_Equip_Cost.Date_Time_Created,
            Rec_Product_Equip_Cost.Id_Modified,
            Rec_Product_Equip_Cost.Date_Time_Modified,
            'NO DATA FOUND',
            SYSDATE,
            'NA'
          );
      END IF;
    EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE ('An error was encountered - ' || SQLCODE || ' -ERROR- ' || SQLERRM);
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
