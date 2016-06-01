package com.walgreens.omni.utility.dashboard;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author CTS 
 * Represent a utility class for generate excel files this class can
 *         be used from any place just inject this class and call export method
 *         with required parameter it will generate excel file and give it.
 * 
 */
@SuppressWarnings("deprecation")
@Component("exportToExcel")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ExportToExcel {

	private static final Logger logger = LoggerFactory
			.getLogger(ExportToExcel.class);

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

/**
	 * 
	 * @param response : write the content in response and set headers
	 * @param sql : string sql query which will provide data for excel file
	 * @param parameters : List<Object> for sql query prepared statement
	 * @param headerTitles : Map<String,String> for Map<'dataIndex','Description'>
	 * @param title : add title to generated excel file
	 * @param fileName : generate file with the given name 
	 */
	public void export(HttpServletResponse response, String sql,
			List<Object> parameters, Map<String, String> headerTitles,
			String title, String fileName) {
		try {
			HSSFWorkbook wb = generateExcel(sql, parameters, headerTitles,
					title);

			response.setHeader("Content-Disposition", "attachment;filename="
					+ fileName + ".xls");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");

			wb.write(response.getOutputStream());
		} catch (Exception e) {
			logger.error("Error while exporting to excel: ERROR: ", e);
		}
	}

	/**
	 * 
	 * Called from export method
	 * 
	 * @return : generated excel file
	 */
	private HSSFWorkbook generateExcel(String sql, List<Object> parameters,
			Map<String, String> headerTitles, String title) {
		HSSFWorkbook wb = new HSSFWorkbook();
		wb.createSheet();
		Integer columnCount = getColumnCount(sql, parameters);
		int resultRowNo = 0;
		if (!title.isEmpty()) {
			writeTitle(wb, title, columnCount);
			resultRowNo++;
		}

		short[] columnWidth = writeColumnHeadings(wb, headerTitles, sql,
				parameters, resultRowNo);

		writeResults(wb, sql, parameters, columnWidth, resultRowNo);
		return wb;
	}

	/**
	 * it is responsible to create a excel file header columns
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private short[] writeColumnHeadings(final HSSFWorkbook wb,
			final Map<String, String> headerTitles, String sqlQuery,
			List<Object> parameters, final int resultRowNo) {
		final Map<String, String> titles = headerTitles;

		ResultSetExtractor resultSetExtractor = new ResultSetExtractor() {
			public Object extractData(ResultSet rst) throws SQLException,
					DataAccessException {
				ResultSetMetaData rsmd = rst.getMetaData();
				int fieldCount = rsmd.getColumnCount();
				short[] columnWidth = new short[fieldCount];
				HSSFSheet sheet = wb.getSheetAt(0);
				HSSFRow row = sheet.createRow(resultRowNo);
				for (int y = 1; y <= fieldCount; y++) {
					HSSFCell cell = row.createCell((short) (y - 1));
					String columnLabel = rsmd.getColumnLabel(y);
					String name = titles.containsKey(columnLabel) ? titles
							.get(columnLabel) : columnLabel;
					columnWidth[y - 1] = (short) (name.length() + 2);
					cell.setCellValue(new HSSFRichTextString(name));
					HSSFCellStyle style = wb.createCellStyle();
					style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
					HSSFFont font = wb.createFont();
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					style.setFont(font);
					cell.setCellStyle(style);

				}
				return columnWidth;
			}
		};

		if (parameters != null && parameters.size() > 0) {
			return (short[]) jdbcTemplate.query(sqlQuery, parameters.toArray(),
					resultSetExtractor);
		} else {
			return (short[]) jdbcTemplate.query(sqlQuery, resultSetExtractor);
		}
	}

	/**
	 * it is responsible to write actual data to excel file
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private void writeResults(final HSSFWorkbook wb, String sqlQuery,
			List<Object> parameters, final short[] columnWidth,
			final int resultRowNo) {
		ResultSetExtractor resultSetExtractor = new ResultSetExtractor() {
			public Object extractData(ResultSet rst) throws SQLException,
					DataAccessException {
				HSSFSheet sheet = wb.getSheetAt(0);
				ResultSetMetaData rsmd = rst.getMetaData();
				int fieldCount = rsmd.getColumnCount();
				int rowNum = resultRowNo + 1;
				HSSFCellStyle style = wb.createCellStyle();
				HSSFDataFormat format = wb.createDataFormat();
				while (rst.next()) {
					HSSFRow row = sheet.createRow(rowNum++);
					for (short i = 1; i <= fieldCount; i++) {
						String columnName = rsmd.getColumnLabel(i);
						HSSFCell cell = row.createCell((short) (i - 1));
						int type = rsmd.getColumnType(i);
						short width = 10;
						switch (type) {
						case Types.INTEGER:
						case Types.BIGINT:
						case Types.SMALLINT:
						case Types.TINYINT:
							writeIntegerValue(rst, i, cell);
							break;
						case Types.FLOAT:
						case Types.DECIMAL:
						case Types.DOUBLE:
						case Types.NUMERIC:
							writeFloatValue(wb, rst, i, cell);
							break;
						case Types.DATE:
							cell.setCellStyle(getDateStyle(style, format));
							Date d = rst.getDate(i);
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd'T'HH:mm:ss.SSS");
							if (d != null) {
								cell.setCellValue(new HSSFRichTextString(sdf
										.format(d)));
							}
							width = 12;
							break;
						case Types.BOOLEAN:
							cell.setCellStyle(getDateStyle(style, format));
							Boolean bool = rst.getBoolean(i);
							if (bool != null) {
								cell.setCellValue(new HSSFRichTextString(
										bool ? "Yes" : "No"));
							}
							width = 5;
							break;
						case Types.CLOB:
							Clob clob = rst.getClob(i);
							String s = (clob != null ? clob.getSubString(1,
									(int) clob.length()) : "");
							cell.setCellValue(new HSSFRichTextString(s));
							if (clob != null) {
								width = (short) (s.length() + 3);
							}
							break;
						default:
							s = rst.getString(i);
							cell.setCellValue(new HSSFRichTextString(rst
									.getString(i)));
							if (rst.getString(i) != null) {
								width = (short) (rst.getString(i).length() + 3);
							}
						}
						if (columnWidth[i - 1] < width) {
							columnWidth[i - 1] = width;
						}

					}
					for (short i = 0; i < fieldCount; i++) {
						short width = columnWidth[i];
						sheet.setColumnWidth(i, (short) (width * 256));
					}
				}
				return null;
			}
		};
		if (parameters != null && parameters.size() > 0) {
			jdbcTemplate.query(sqlQuery, parameters.toArray(),
					resultSetExtractor);
		} else {
			jdbcTemplate.query(sqlQuery, resultSetExtractor);
		}
	}

	private static HSSFCellStyle getDateStyle(HSSFCellStyle style,
			HSSFDataFormat format) {
		style.setDataFormat(format.getFormat("dd MMM yyyy"));
		return style;
	}

	protected void writeIntegerValue(ResultSet rst, short fieldIndex,
			HSSFCell cell) throws SQLException {
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		String s = rst.getString(fieldIndex);
		if (s != null) {
			double n = new Double(s).doubleValue();
			cell.setCellValue(n);
		}
	}

	protected void writeFloatValue(final HSSFWorkbook wb, ResultSet rst,
			short fieldIndex, HSSFCell cell) throws SQLException {
		writeIntegerValue(rst, fieldIndex, cell);
	}

	/**
	 * it is responsible to write a title in excel file
	 */
	private void writeTitle(HSSFWorkbook wb, String title, int fieldCount) {
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue(new HSSFRichTextString(title));
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 16);
		style.setFont(font);
		cell.setCellStyle(style);
		sheet.addMergedRegion(new Region(0, (short) 0, 0,
				(short) (fieldCount - 1)));
	}

	/**
	 * it is responsible to get column count based on provided query which will
	 * be used to create cells in excel file
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Integer getColumnCount(String sqlQuery, List<Object> parameters) {
		ResultSetExtractor resultSetExtractor = new ResultSetExtractor() {
			public Object extractData(ResultSet rst) throws SQLException,
					DataAccessException {
				return rst.getMetaData().getColumnCount();
			}
		};
		if (parameters != null && parameters.size() > 0) {
			return (Integer) jdbcTemplate.query(sqlQuery, parameters.toArray(),
					resultSetExtractor);
		} else {
			return (Integer) jdbcTemplate.query(sqlQuery, resultSetExtractor);
		}

	}

}
