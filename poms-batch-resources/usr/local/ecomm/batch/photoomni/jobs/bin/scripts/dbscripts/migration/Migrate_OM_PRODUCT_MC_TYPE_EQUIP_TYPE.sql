SET SERVEROUTPUT ON;
-- UPDATE / ADD RECORDS INTO OM_PRODUCT_MC_TYPE_EQUIP_TYPE
DECLARE
  CURSOR C_Product_Machine_Equip
  IS
    SELECT B.SYS_PRODUCT_ID,
      C.SYS_MACHINE_TYPE_ID,
      D.Sys_Equipment_Type_Id,
      A.Pf_Product_Id,
      A.Pf_Machine_Type_Id,
      A.PF_EQUIPMENT_ID,
      A.Pf_Transfer_Ind,
      Nvl(A.Id_Created,'SYSTEM') Id_Created,
      NVL(A.DATE_TIME_CREATED, SYSDATE) DATE_TIME_CREATED,
      A.ID_MODIFIED,
      A.DATE_TIME_MODIFIED
    FROM Om_Pf_Prod_Machine_Equip_Tmp A
    LEFT JOIN Om_Product B
    ON A.Pf_Product_Id=B.Product_Nbr
    LEFT JOIN Om_Machine_Type C
    ON A.Pf_Machine_Type_Id = C.Machine_Type_Nbr
    LEFT JOIN Om_Equipment_Type D
    ON A.Pf_Equipment_Id = D.Equipment_Type_Nbr;
  -- DEFINE THE RECORD
  Rec_Product_Machine_Equip C_Product_Machine_Equip%ROWTYPE;
  -- DEFINE VARIABLES
  v_rec_present    NUMBER        :=0 ;
  V_Counter        NUMBER        := 0;
  v_commitinterval NUMBER        := 20;
  V_Active_Cd      NUMBER        := 1;
  v_err_msg        VARCHAR2(200) := '';
  v_err_code       VARCHAR2(10)  := '';
BEGIN
  --Deactivate all records in OM_PRODUCT_MC_TYPE_EQUIP_TYPE pertaining to products in TMP tables
  UPDATE OM_PRODUCT_MC_TYPE_EQUIP_TYPE
  SET active_cd         = 0
  WHERE SYS_PRODUCT_ID IN
    (SELECT B.SYS_PRODUCT_ID
    FROM Om_Pf_Prod_Machine_Equip_Tmp A,
      Om_Product B
    WHERE A.Pf_Product_Id=B.Product_Nbr
    GROUP BY B.SYS_PRODUCT_ID
    );
  COMMIT;
  OPEN C_Product_Machine_Equip;
  LOOP
    FETCH C_Product_Machine_Equip INTO Rec_Product_Machine_Equip;
    EXIT
  WHEN C_Product_Machine_Equip%NOTFOUND OR C_Product_Machine_Equip%NOTFOUND IS NULL;
    BEGIN
      IF ((Rec_Product_Machine_Equip.Sys_Product_Id IS NOT NULL) AND (Rec_Product_Machine_Equip.Sys_Machine_Type_Id IS NOT NULL) AND (Rec_Product_Machine_Equip.Sys_Equipment_Type_Id IS NOT NULL)) THEN
        v_rec_present                               := 0;
        -- CHECK IF RECORD EXISTS
        SELECT COUNT(1)
        INTO v_rec_present
        FROM Om_Product_Mc_Type_Equip_Type
        WHERE Sys_Product_Id      = Rec_Product_Machine_Equip.Sys_Product_Id
        AND Sys_Machine_Type_Id   = Rec_Product_Machine_Equip.Sys_Machine_Type_Id
        AND Sys_Equipment_Type_Id = Rec_Product_Machine_Equip.Sys_Equipment_Type_Id;
        IF v_rec_present         <> 0 THEN
          UPDATE Om_Product_Mc_Type_Equip_Type
          SET Active_Cd             = V_Active_Cd
          WHERE Sys_Product_Id      = Rec_Product_Machine_Equip.Sys_Product_Id
          AND Sys_Machine_Type_Id   = Rec_Product_Machine_Equip.Sys_Machine_Type_Id
          AND Sys_Equipment_Type_Id = Rec_Product_Machine_Equip.Sys_Equipment_Type_Id;
        ELSE
          INSERT
          INTO Om_Product_Mc_Type_Equip_Type
            (
              Sys_Prod_Mac_Equip_Id,
              Sys_Product_Id,
              Sys_Machine_Type_Id,
              Sys_Equipment_Type_Id,
              Active_Cd,
              Create_User_Id,
              Create_Dttm,
              Update_User_Id,
              Update_Dttm
            )
            VALUES
            (
              Om_Prod_Mc_Tp_Equip_Tp_Seq.Nextval,
              Rec_Product_Machine_Equip.Sys_Product_Id,
              Rec_Product_Machine_Equip.Sys_Machine_Type_Id,
              Rec_Product_Machine_Equip.Sys_Equipment_Type_Id ,
              V_Active_Cd,
              Rec_Product_Machine_Equip.Id_Created,
              Rec_Product_Machine_Equip.Date_Time_Created,
              Rec_Product_Machine_Equip.ID_MODIFIED,
              Rec_Product_Machine_Equip.DATE_TIME_MODIFIED
            );
        END IF;
      ELSE
        --Insert into BAD table
        INSERT
        INTO Om_Pf_Prod_Machine_Equip_Bad
          (
            Pf_Product_Id,
            Pf_Machine_Type_Id,
            Pf_Equipment_Id,
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
            Rec_Product_Machine_Equip.Pf_Product_Id,
            Rec_Product_Machine_Equip.Pf_Machine_Type_Id,
            Rec_Product_Machine_Equip.Pf_Equipment_Id,
            Rec_Product_Machine_Equip.Pf_Transfer_Ind,
            Rec_Product_Machine_Equip.Id_Created,
            Rec_Product_Machine_Equip.Date_Time_Created,
            Rec_Product_Machine_Equip.Id_Modified,
            Rec_Product_Machine_Equip.Date_Time_Modified,
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
