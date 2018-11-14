package com.mailian.firecontrol.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil {
	
	public static class CellEntity {
		private String name;
		private int columns = 1;
		private int fontSize;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getColumns() {
			return columns;
		}
		public void setColumns(int columns) {
			this.columns = columns;
		}
		public int getFontSize() {
			return fontSize;
		}
		public void setFontSize(int fontSize) {
			this.fontSize = fontSize;
		}
	}
	
	public static void printXls(HSSFWorkbook workbook, OutputStream out,
								List<List<String>> datas, List<List<CellEntity>> header,
								String sheetName, int sheetnum, int columnWidth) throws IOException {
		
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(sheetnum, sheetName);
		// 设置列宽
		sheet.setDefaultColumnWidth(columnWidth);
		// 冰冻表头
		sheet.setFitToPage(true);
		// 生成一个样式
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		
		// 设置这些样式
		headerStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		headerStyle.setWrapText(true);
		
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);
		
		// 生成并设置另一个样式
		HSSFCellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		dataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		dataStyle.setFont(font2);
		
		int index = 0;
		HSSFRow row;
		
		if (header != null) {
			int rowsCount = header.size();
			sheet.createFreezePane(0, rowsCount);
			for (int i = 0; i < header.size(); i++) {
				row = sheet.createRow(index);
				index++;
				List<CellEntity> data = header.get(i);
				if (data == null)
					continue;
				int columnIndex = 0;
				for (int j = 0; j < data.size(); j++) {
					for (int k = 0; k < data.get(j).columns; k++) {
						HSSFCell cell = row.createCell(columnIndex + k);
						cell.setCellStyle(headerStyle);
						HSSFRichTextString text = new HSSFRichTextString(
								data.get(j).name);
						if (k == 0) {
							cell.setCellValue(text);
						}
					}
					if (data.get(j).columns - 1 > 0)
						sheet.addMergedRegion(new CellRangeAddress(i, i,
								columnIndex, columnIndex + data.get(j).columns - 1));
					columnIndex += data.get(j).columns;
				}
			}
		}
		
		if (datas != null) {
			for (int i = 0; i < datas.size(); i++) {
				List<String> data = datas.get(i);
				row = sheet.createRow(index);
				if (data == null)
					continue;
				for (int j = 0; j < data.size(); j++) {
					HSSFCell cell = row.createCell(j);
					cell.setCellStyle(dataStyle);
					HSSFRichTextString text = null;
					if (data.get(j) != null) {
						text = new HSSFRichTextString(data.get(j));
					} else {
						text = new HSSFRichTextString("");
					}
					cell.setCellValue(text);
				}
				index++;
			}
		}
		
	}
	
	public static void printSortXls(HSSFWorkbook workbook, OutputStream out,
									List<CellEntity> listHead,
									Map<String, Map<String, String>> mapTokey, String sheetName,
									int sheetnum, String deviceCode, String deviceName)
			throws IOException {
		
		HSSFSheet sheet = workbook.createSheet();
		
		workbook.setSheetName(sheetnum, sheetName);
		// 设置列宽
		sheet.setDefaultColumnWidth(24);
		// 冰冻表头
		sheet.setFitToPage(true);
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		
		// rownum是行的索引，从0开始
		HSSFRow row1 = sheet.createRow(0);
		HSSFRow row2 = sheet.createRow(1);
		HSSFRow row3 = sheet.createRow(2);
		
		HSSFCell cell01 = row1.createCell(0);
		cell01.setCellValue("通信机名称");
		HSSFCell cell02 = row2.createCell(0);
		cell02.setCellValue("通信机编码");
		
		// 站点名称 值
		HSSFCell cell03 = row1.createCell(1);
		// 参数1：起始行， 参数2：终止行， 参数3：起始列 ， 参数4：终止列
		CellRangeAddress region1 = new CellRangeAddress(0, 0, 1, 7);
		cell03.setCellValue(deviceName);
		cell03.setCellStyle(style);
		// 合并单元格
		sheet.addMergedRegion(region1);
		
		HSSFCell cell04 = row2.createCell(1);
		cell04.setCellStyle(style);
		cell04.setCellValue(deviceCode);
		CellRangeAddress region2 = new CellRangeAddress(1, 1, 1, 7);
		sheet.addMergedRegion(region2);
		
		// 设置名称
		for (int i = 0; i < listHead.size(); i++) {
			HSSFCell cell = row3.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(listHead.get(i).getName());
			if (text != null) {
				cell.setCellValue(text);
			}
		}
		
		// 设置值
		Set<Map.Entry<String, Map<String, String>>> entrySet = mapTokey.entrySet();
		List<Double> listNum = new ArrayList<Double>();
		List<String> listStr = new ArrayList<String>();
		
		for (Map.Entry<String, Map<String, String>> entry : entrySet) {
			String name = entry.getKey();

			if (isNumeric(name)) {
				double parseDouble = Double.parseDouble(name);
				listNum.add(parseDouble);
				Collections.sort(listNum);
			} else {
				listStr.add(name);
			}

		}
		
		int index = 3;
		for (Double dou : listNum) {
			String name = doubleToStr(dou);
			Map<String, String> typeData = mapTokey.get(name);
			// 设置行
			HSSFRow dataRow = sheet.createRow(index);
			index++;
			if (typeData == null || typeData.size() == 0
					|| (typeData.containsKey("0") && typeData.size() == 1)
					||typeData.containsKey("开关")||typeData.containsKey("通信")||typeData.containsKey("跳闸")
					) 
			{
				--index;
				continue;
			}
			
			for (int i = 0; i < listHead.size(); i++) {
				HSSFCell cell = dataRow.createCell(i);
				cell.setCellStyle(style2);

				if (i == 0) {
					cell.setCellValue(dou);
				} else if (i == 1) {
					String str01 = typeData.get("水表");
					if (str01 != null) {
						cell.setCellValue(str01);
					}

				} else if (i == 2) {
					String str02 = typeData.get("电表");
					if (str02 != null) {
						cell.setCellValue(str02);
					}

				} else if (i == 3) {
					String str03 = typeData.get("电压");
					if (str03 != null) {
						cell.setCellValue(str03);
					}

				} else if (i == 4) {
					String str04 = typeData.get("电流");
					if (str04 != null) {
						cell.setCellValue(str04);
					}

				} else if (i == 5) {
					String str05 = typeData.get("烟感");
					if (str05 != null) {
						cell.setCellValue(str05);
					}

				} else if (i == 6) {
					String str06 = typeData.get("门禁");
					if (str06 != null) {
						cell.setCellValue(str06);
					}

				} else if (i == 7) {
					String str07 = typeData.get("燃气");
					if (str07 != null) {
						cell.setCellValue(str07);
					}
				}
			}
		}
		
		for (String str : listStr) {
			Map<String, String> typeData = mapTokey.get(str);
			// 设置行
			HSSFRow dataRow = sheet.createRow(index);
			index++;
			
			if (typeData == null || typeData.size() == 0
					|| (typeData.containsKey("0") && typeData.size() == 1)
					||typeData.containsKey("开关")||typeData.containsKey("通信")||typeData.containsKey("跳闸")
					) {
				--index;
				continue;
			}

			for (int i = 0; i < listHead.size(); i++) {
				HSSFCell cell = dataRow.createCell(i);
				cell.setCellStyle(style2);

				if (i == 0) {
					cell.setCellValue(str);
				} else if (i == 1) {
					String str01 = typeData.get("水表");
					if (str01 != null) {
						cell.setCellValue(str01);
					}

				} else if (i == 2) {
					String str02 = typeData.get("电表");
					if (str02 != null) {
						cell.setCellValue(str02);
					}

				} else if (i == 3) {
					String str03 = typeData.get("电压");
					if (str03 != null) {
						cell.setCellValue(str03);
					}

				} else if (i == 4) {
					String str04 = typeData.get("电流");
					if (str04 != null) {
						cell.setCellValue(str04);
					}

				} else if (i == 5) {
					String str05 = typeData.get("烟感");
					if (str05 != null) {
						cell.setCellValue(str05);
					}

				} else if (i == 6) {
					String str06 = typeData.get("门禁");
					if (str06 != null) {
						cell.setCellValue(str06);
					}

				} else if (i == 7) {
					String str07 = typeData.get("燃气");
					if (str07 != null) {
						cell.setCellValue(str07);
					}
				}
			}
		}
	}
	
	
	
	// 判断字符串是是否是数字
	private static boolean isNumeric(String str) {
		// 该正则表达式可以匹配所有的数字 包括负数
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
		} catch (Exception e) {
			return false;// 异常 说明包含非数字。
		}

		Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	private static String doubleToStr(Double douStr) {
		String s = douStr + "";
		int index = s.indexOf(".");
		if (index != -1) {
			String substring = s.substring(index + 1, s.length());

			if (Integer.valueOf(substring) != 0) {
				return s;
			} else {
				return s.substring(0, index);

			}
		}
		return s;
	}
	
	
}
