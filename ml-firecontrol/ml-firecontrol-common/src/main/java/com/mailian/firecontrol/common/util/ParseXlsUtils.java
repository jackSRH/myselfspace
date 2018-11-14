package com.mailian.firecontrol.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParseXlsUtils {
	public static List<List<List<String>>> readExcle(InputStream inputStream)
			throws IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
		List<List<List<String>>> res = new ArrayList<>();
		for(int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			List<List<String>> sheetData = new ArrayList<>();
			for(int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				List<String> rowData = new ArrayList<>();
				for (int cellNum = 0; cellNum < hssfRow.getLastCellNum(); cellNum++) {
					HSSFCell hssfCell = hssfRow.getCell(cellNum);
					if (hssfCell == null) {
						continue;
					}else {
						hssfCell.setCellType(Cell.CELL_TYPE_STRING);
					}
					rowData.add(hssfCell.getStringCellValue());
				}
				if (rowData.size() > 0) {
					sheetData.add(rowData);
				}
			}
			res.add(sheetData);
		}
		return res;
	}
	
	
	
	
}
