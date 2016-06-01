package com.walgreens.batch.central.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DailyPLUQuery {

	private static final Logger logger = LoggerFactory
			.getLogger(DailyPLUQuery.class);

	public static StringBuilder getPLURrecord() {

		logger.info(" Entering into getPLURrecord() method of DailyPLUQuery ");
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM ( ");
		sb.append(" SELECT \"Order Date\",\"PLU #\",\"Coupon Code\",\"Promotion Description\",\"Channel\",\"Retail $\", ");
		sb.append(" \"Discount $\",\"# of Orders\",\"# of Units\", ROWNUM AS \"RNK\" FROM ( ");
		sb.append(" SELECT TO_CHAR(ORD.ORDER_PLACED_DTTM,'MM-DD-YYYY') AS \"Order Date\", PROMO.PLU_NBR AS \"PLU #\", PROMO.COUPON_CODE AS \"Coupon Code\", ");
		sb.append(" PROMO.PROMOTION_DESC AS \"Promotion Description\", PROMO.PROMO_CHANNEL AS \"Channel\", SUM(ORD.FINAL_PRICE) AS \"Retail $\", ");
		sb.append(" SUM(PLU.PLU_DISCOUNT_AMOUNT) AS \"Discount $\", COUNT(1) AS \"# of Orders\", 0 AS \"# of Units\" FROM OM_ORDER ORD, OM_ORDER_PLU PLU, ");
		sb.append(" OM_PROMOTION PROMO WHERE ORD.SYS_ORDER_ID = PLU.SYS_ORDER_ID AND ORD.ORDER_PLACED_DTTM = PLU.ORDER_PLACED_DTTM AND ");
		sb.append(" PLU.SYS_PLU_ID = PROMO.SYS_PLU_ID AND PLU.ACTIVE_CD = 1 AND ORD.ORDER_PLACED_DTTM BETWEEN TO_DATE(SYSDATE-1) AND TO_DATE(SYSDATE) ");
		sb.append(" GROUP BY TO_CHAR(ORD.ORDER_PLACED_DTTM,'MM-DD-YYYY'), PROMO.PLU_NBR, PROMO.COUPON_CODE, PROMO.PROMOTION_DESC, PROMO.PROMO_CHANNEL ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT TO_CHAR(ORDL.ORDER_PLACED_DTTM,'MM-DD-YYYY') AS \"Order Date\", PROMO.PLU_NBR AS \"PLU #\", PROMO.COUPON_CODE AS \"Coupon Code\", ");
		sb.append(" PROMO.PROMOTION_DESC AS \"Promotion Description\", PROMO.PROMO_CHANNEL AS \"Channel\", SUM(ORDL.FINAL_PRICE) AS \"Retail $\", ");
		sb.append(" SUM(PLU.PLU_DISCOUNT_AMOUNT) AS \"Discount $\", COUNT(1) AS \"# of Orders\", SUM(ORDL.QUANTITY) AS \"# of Units\" ");
		sb.append(" FROM OM_ORDER_LINE ORDL, OM_ORDER_LINE_PLU PLU, OM_PROMOTION PROMO WHERE ORDL.SYS_ORDER_LINE_ID = PLU.SYS_ORDER_LINE_ID ");
		sb.append(" AND ORDL.ORDER_PLACED_DTTM = PLU.ORDER_PLACED_DTTM AND PLU.SYS_PLU_ID = PROMO.SYS_PLU_ID AND PLU.ACTIVE_CD = 1 AND ORDL.ORDER_PLACED_DTTM ");
		sb.append(" BETWEEN TO_DATE(SYSDATE-1) AND TO_DATE(SYSDATE) GROUP BY TO_CHAR(ORDL.ORDER_PLACED_DTTM,'MM-DD-YYYY'), PROMO.PLU_NBR, ");
		sb.append(" PROMO.COUPON_CODE, PROMO.PROMOTION_DESC, PROMO.PROMO_CHANNEL ");
		sb.append(" ) ) WHERE  RNK BETWEEN ? AND ? ");
		return sb;
	}

	public static StringBuilder getPLURrecordAdhoc(String pluNum) {

		logger.info(" Entering into getPLURrecordAdhoc() method of DailyPLUQuery ");
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM ( ");
		sb.append(" SELECT \"Order Date\",\"PLU #\",\"Coupon Code\",\"Promotion Description\",\"Channel\",\"Retail $\", ");
		sb.append(" \"Discount $\",\"# of Orders\",\"# of Units\", ROWNUM as RNK  FROM ( ");
		sb.append(" select * from ( ");
		sb.append(" SELECT to_char(Ord.Order_Placed_Dttm,'MM-DD-YYYY') AS \"Order Date\", Promo.Plu_Nbr AS \"PLU #\", Promo.Coupon_Code AS \"Coupon Code\", ");
		sb.append(" Promo.Promotion_Desc AS \"Promotion Description\", Promo.Promo_Channel AS \"Channel\", SUM(Ord.Final_Price) AS \"Retail $\", ");
		sb.append(" SUM(Plu.Plu_Discount_Amount) AS \"Discount $\", COUNT(1) AS \"# of Orders\", 0 AS \"# of Units\" FROM Om_Order Ord, Om_Order_Plu Plu, ");
		sb.append(" Om_Promotion Promo WHERE Ord.Sys_Order_Id = Plu.Sys_Order_Id AND Ord.Order_Placed_Dttm = Plu.Order_Placed_Dttm AND ");
		sb.append(" Plu.Sys_Plu_Id = Promo.Sys_Plu_Id AND PLU.ACTIVE_CD = 1 AND Ord.Order_Placed_Dttm BETWEEN To_Date( ? , 'yyyy-mm-dd HH24:MI:SS') AND To_Date( ? , 'yyyy-mm-dd HH24:MI:SS') ");
		if(!pluNum.equalsIgnoreCase("true"))sb.append(" AND Promo.Plu_Nbr IN ( " + pluNum + ")");
		//sb.append( pluNum.equalsIgnoreCase("true") ? " SELECT DISTINCT PROMO.PLU_NBR FROM OM_PROMOTION " : pluNum);
		sb.append(" GROUP BY to_char(Ord.Order_Placed_Dttm,'MM-DD-YYYY'), Promo.Plu_Nbr, Promo.Coupon_Code, Promo.Promotion_Desc, Promo.Promo_Channel ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT to_char(Ordl.Order_Placed_Dttm,'MM-DD-YYYY')  AS \"Order Date\", Promo.Plu_Nbr AS \"PLU #\", Promo.Coupon_Code AS \"Coupon Code\", ");
		sb.append(" Promo.Promotion_Desc AS \"Promotion Description\", Promo.Promo_Channel AS \"Channel\", SUM(Ordl.Final_Price) AS \"Retail $\", ");
		sb.append(" SUM(Plu.Plu_Discount_Amount) AS \"Discount $\", COUNT(1) AS \"# of Orders\", SUM(Ordl.Quantity) AS \"# of Units\" ");
		sb.append(" FROM Om_Order_Line Ordl, Om_Order_Line_Plu Plu, Om_Promotion Promo WHERE Ordl.Sys_Order_Line_Id = Plu.Sys_Order_Line_Id ");
		sb.append(" AND Ordl.Order_Placed_Dttm = Plu.Order_Placed_Dttm AND Plu.Sys_Plu_Id = Promo.Sys_Plu_Id AND PLU.ACTIVE_CD = 1 AND Ordl.Order_Placed_Dttm ");
		sb.append(" BETWEEN To_Date( ? , 'yyyy-mm-dd HH24:MI:SS') AND To_Date( ? , 'yyyy-mm-dd HH24:MI:SS') ");
		if(!pluNum.equalsIgnoreCase("true"))sb.append(" AND Promo.Plu_Nbr IN ( " + pluNum + ")");
		//sb.append( pluNum.equalsIgnoreCase("true") ? " SELECT DISTINCT PROMO.PLU_NBR FROM OM_PROMOTION " : pluNum);
		sb.append(" GROUP BY to_char(Ordl.Order_Placed_Dttm,'MM-DD-YYYY'), Promo.Plu_Nbr, Promo.Coupon_Code, Promo.Promotion_Desc, Promo.Promo_Channel ");
		sb.append(" ) ORDER BY \"Order Date\") ");
		sb.append(" ) WHERE  RNK BETWEEN ? AND ? ");
		return sb;
	}
}