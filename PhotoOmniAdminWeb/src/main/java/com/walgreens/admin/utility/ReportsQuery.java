package com.walgreens.admin.utility;

public class ReportsQuery {

	private ReportsQuery() {

	}

	/*
	 * This method create SQL select query for KIOSK Report which is displayed
	 * on the UI
	 * 
	 * @Return Select query for the KIOSK Report on the UI
	 */
	public static StringBuilder getKioskData(String filter) {

		StringBuilder query = new StringBuilder();
		query.append(" SELECT * FROM ( SELECT a.*, rownum r__ FROM ( ");
		query.append("SELECT OM_LOCATION.LOCATION_NBR as LocationNbr, OM_KIOSK_DEVICE_DETAIL.DEVICE_IP as KioskIp,");
		query.append("OM_KIOSK_DEVICE_DETAIL.SOFTWARE_VERSION as SoftwareVer, OM_KIOSK_DEVICE_DETAIL.TEMPLATE_VERSION as TemplateVer,");
		query.append("OM_KIOSK_DEVICE_DETAIL.IMEMORIES_VERSION as iMemoriesVer, OM_KIOSK_DEVICE_DETAIL.TOMO_VERSION as TOMOVer, OM_KIOSK_DEVICE_DETAIL.LAST_REBOOT as LastReboot,");
		query.append("OM_KIOSK_DEVICE_DETAIL.C_DRIVE_SPACE_MB as CSpace, CAST(OM_KIOSK_DEVICE_DETAIL.D_DRIVE_SPACE_MB as VARCHAR(30)) DSpace FROM OM_KIOSK_DEVICE_DETAIL ");
		query.append(" INNER JOIN OM_LOCATION ON OM_KIOSK_DEVICE_DETAIL.SYS_LOCATION_ID = OM_LOCATION.SYS_LOCATION_ID ");
		query.append(filter);
		query.append(" ORDER BY OM_KIOSK_DEVICE_DETAIL.SYS_LOCATION_ID ");
		query.append(" ) a WHERE rownum < ((? * ?) + 1 )) WHERE r__ >= (((? - 1) * ?) + 1)");
		return query;
	}

	/*
	 * This method create SQL select query for KIOSK filter search criteria on
	 * the UI
	 * 
	 * @Return Select query for the KIOSK filter criteria
	 */
	public static StringBuilder getKioskFilter() {

		StringBuilder query = new StringBuilder();
		query.append(" SELECT CODE.CODE_ID, CODE.DECODE FROM OM_CODE_DECODE CODE WHERE CODE.CODE_TYPE = 'Kiosk_Report_Details' ");
		query.append(" AND CODE.CODE_ID NOT IN ('SYS_DEVICE_INSTANCE_ID', 'DEVICE_TYPE', 'CREATE_USER_ID', 'UPDATE_USER_ID') ORDER BY CODE.DECODE ");
		return query;
	}

	/*
	 * This method create SQL select query for KOISK export excel data
	 * 
	 * @Return Select query for the KIOSK excel export
	 */
	public static StringBuilder getKioskReportExcelData() {

		StringBuilder query = new StringBuilder();
		query.append(" Select OM_LOCATION.LOCATION_NBR, DEVICE_IP, ");
		query.append(" SOFTWARE_VERSION, TEMPLATE_VERSION, HARDWARE_MODEL, SCANNER_MODEL, IMEMORIES_VERSION, FIRMWARE_VERSION, REGION_VERSION, ");
		query.append(" FB_ACTIVE, WAG_ACTIVE, MPL_STORE, IMEMORIES_ACTIVE, TOMO_VERSION, ");
		query.append(" AUTO_UPDATE_CONFIG_VERSION, WG_UPDATE_VERSION, CONTENT_VERSION, LAUNCHPAD_VERSION, ");
		query.append(" OS_VERSION, INSTANT_PRINTER_ATT, WIN7_ACTIVATED_CD, LAST_REBOOT as LastReboot, ");
		query.append(" MINILAB_MODEL, PHASER_MODEL, POSTER_MODEL, MASTER_VERSION, cast(C_DRIVE_SIZE_MB as ");
		query.append(" varchar(30)) C_DRIVE_SIZE_MB, C_DRIVE_SPACE_MB, ");
		query.append(" cast(D_DRIVE_SIZE_MB as varchar(30)) D_DRIVE_SIZE_MB, cast(D_DRIVE_SPACE_MB as varchar(30)) ");
		query.append(" D_DRIVE_SPACE_MB, RECEIPT_PRINTER_MODEL, OM_KIOSK_DEVICE_DETAIL.CREATE_DTTM, ");
		query.append(" OM_KIOSK_DEVICE_DETAIL.UPDATE_DTTM, LC_VERSION  from OM_KIOSK_DEVICE_DETAIL ");
		query.append(" INNER JOIN OM_LOCATION ON OM_KIOSK_DEVICE_DETAIL.SYS_LOCATION_ID = OM_LOCATION.SYS_LOCATION_ID ");
		return query;
	}

	public static StringBuilder getKioskReportDataCount() {

		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(*) FROM OM_KIOSK_DEVICE_DETAIL ");
		query.append(" INNER JOIN OM_LOCATION ON OM_KIOSK_DEVICE_DETAIL.SYS_LOCATION_ID = OM_LOCATION.SYS_LOCATION_ID ");
		return query;
	}

	public static StringBuilder getCodeDecode() {

		StringBuilder query = new StringBuilder();
		query.append("select DECODE from OM_CODE_DECODE where CODE_ID = ? ");
		return query;
	}

	public static StringBuilder getPageCount() {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT CONFIG.PAGE_SIZE  FROM OM_REPORT_CONFIG CONFIG WHERE CONFIG.SYS_REPORT_ID = ");
		sb.append(" (SELECT REPORT.SYS_REPORT_ID FROM OM_REPORT REPORT WHERE REPORT.REPORT_NAME = ? ) ");
		return sb;
	}
}