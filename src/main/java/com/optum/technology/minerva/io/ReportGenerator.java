package com.optum.technology.minerva.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.optum.technology.minerva.entity.Document;
import com.optum.technology.minerva.entity.Inquiry;
import com.optum.technology.minerva.entity.InquiryReply;
import com.optum.technology.minerva.util.MinervaUtils;

public class ReportGenerator {

	public String generateDoc(List<Document> details, String momLibraryPath) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet writableSheet = workbook.createSheet("Document Report");
		String titleHeads[] = { "Document Id", "Title", "Detailed Info", "Filename", "Line Of Business", "Category",
				"Sub-Category", "Tags", "Create Date", "Create User" };
		int rowCtr = 0;
		CellStyle titleStyle = getCellStyle(workbook, true);
		Row titleRow = writableSheet.createRow(rowCtr++);
		int titleRowCtr = 0;
		for (String title : titleHeads) {
			Cell titleCell = titleRow.createCell(titleRowCtr++);
			titleCell.setCellValue(title);
			titleCell.setCellStyle(titleStyle);
		}
		CellStyle normalStyle = getCellStyle(workbook, false);
		for (Document document : details) {
			Row bodyRow = writableSheet.createRow(rowCtr++);
			int bodyCellCtr = 0;
			bodyCellCtr = addRow(normalStyle, String.valueOf(document.getId()), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, document.getTitle(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, document.getDetailedInfo(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, document.getFilename(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, document.getLineOfBusiness().getName(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, document.getCategory().getName(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle,
					null == document.getSubCategory() ? "" : document.getSubCategory().getName(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, document.getTags(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, document.getCreateDate().toString("MM/dd/YYYY hh:mm:ss"), bodyRow,
					bodyCellCtr);
			bodyCellCtr = addRow(normalStyle,
					document.getCreateUser().getFirstName() + " " + document.getCreateUser().getLastName(), bodyRow,
					bodyCellCtr);
		}

		for (int i = 0; i < titleHeads.length + 1; i++) {
			writableSheet.autoSizeColumn(i);
		}
		Date date = new Date();
		String timestamp = new Timestamp(date.getTime()).toString().replace(".", "-").replace(":", "-")
				.replace(" ", "_").replace("-", "");
		String fileName = "DocumentReport_" + timestamp + ".xlsx";
		String path = momLibraryPath + fileName;
		try {

			FileOutputStream out = new FileOutputStream(new File(path));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public String generate(List<Inquiry> details, List<InquiryReply> replies, String momLibraryPath) {
		// HSSFWorkbook workbook = new HSSFWorkbook();
		XSSFWorkbook workbook = new XSSFWorkbook();

		createInquirySheet(details, workbook);
		createInquiryReplySheet(replies, workbook);

		Date date = new Date();
		String timestamp = new Timestamp(date.getTime()).toString().replace(".", "-").replace(":", "-")
				.replace(" ", "_").replace("-", "");
		String fileName = "InquiryReport_" + timestamp + ".xlsx";
		String path = momLibraryPath + fileName;

		try {

			FileOutputStream out = new FileOutputStream(new File(path));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	private void createInquirySheet(List<Inquiry> details, XSSFWorkbook workbook) {
		XSSFSheet inquirySheet = workbook.createSheet("Inquiry Report");
		String titleHeadsInquiry[] = { "Inquiry Id", "Title", "Inquiry Body", "Line Of Business", "Create Date",
				"Create User", "Status" };
		int rowCtr = 0;

		CellStyle titleStyle = getCellStyle(workbook, true);
		Row titleRow = inquirySheet.createRow(rowCtr++);
		int titleRowCtr = 0;
		for (String title : titleHeadsInquiry) {
			Cell titleCell = titleRow.createCell(titleRowCtr++);
			titleCell.setCellValue(title);
			titleCell.setCellStyle(titleStyle);
		}
		CellStyle normalStyle = getCellStyle(workbook, false);
		for (Inquiry inquiry : details) {
			Row bodyRow = inquirySheet.createRow(rowCtr++);
			int bodyCellCtr = 0;
			bodyCellCtr = addRow(normalStyle, String.valueOf(inquiry.getId()), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, inquiry.getTitle(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, inquiry.getBody(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, inquiry.getLineOfBusiness().getName(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, inquiry.getCreateDate().toString("MM/dd/YYYY hh:mm:ss"), bodyRow,
					bodyCellCtr);
			bodyCellCtr = addRow(normalStyle,
					inquiry.getCreateUser().getFirstName() + " " + inquiry.getCreateUser().getLastName(), bodyRow,
					bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, MinervaUtils.getStatusEq(inquiry.getStatus()), bodyRow, bodyCellCtr);

		}

		for (int i = 0; i < titleHeadsInquiry.length + 1; i++) {
			inquirySheet.autoSizeColumn(i);
		}
	}

	private void createInquiryReplySheet(List<InquiryReply> details, XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.createSheet("Inquiry Reply Report");
		String titleHeads[] = { "Inquiry Id", "Reply Id", "Reply Body", "Create Date", "Create User" };

		int rowCtr = 0;

		CellStyle titleStyle = getCellStyle(workbook, true);
		Row titleRow = sheet.createRow(rowCtr++);
		int titleRowCtr = 0;
		for (String title : titleHeads) {
			Cell titleCell = titleRow.createCell(titleRowCtr++);
			titleCell.setCellValue(title);
			titleCell.setCellStyle(titleStyle);
		}
		CellStyle normalStyle = getCellStyle(workbook, false);
		for (InquiryReply inquiryReply : details) {
			Row bodyRow = sheet.createRow(rowCtr++);
			int bodyCellCtr = 0;
			bodyCellCtr = addRow(normalStyle, String.valueOf(inquiryReply.getInquiryId().getId()), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, String.valueOf(inquiryReply.getId()), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, inquiryReply.getReplyBody(), bodyRow, bodyCellCtr);
			bodyCellCtr = addRow(normalStyle, inquiryReply.getCreateDate().toString("MM/dd/YYYY hh:mm:ss"), bodyRow,
					bodyCellCtr);
			bodyCellCtr = addRow(normalStyle,
					inquiryReply.getCreateUser().getFirstName() + " " + inquiryReply.getCreateUser().getLastName(),
					bodyRow, bodyCellCtr);

		}

		for (int i = 0; i < titleHeads.length + 1; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	private int addRow(CellStyle normalStyle, String val, Row bodyRow, int bodyCellCtr) {
		Cell id = bodyRow.createCell(bodyCellCtr++);
		id.setCellValue(val);
		id.setCellStyle(normalStyle);
		return bodyCellCtr;
	}

	private CellStyle getCellStyle(XSSFWorkbook workbook, boolean isBold) {

		XSSFFont titleFont = getFont(workbook, isBold);
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titleFont);
		titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
		titleStyle.setBorderTop(CellStyle.BORDER_THIN);
		titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
		titleStyle.setBorderRight(CellStyle.BORDER_THIN);
		if (isBold) {
			titleStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
			titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		return titleStyle;
	}

	private XSSFFont getFont(XSSFWorkbook workbook, boolean isBold) {
		XSSFFont titleFont = workbook.createFont();
		titleFont.setBold(isBold);
		titleFont.setItalic(false);
		if (isBold) {
			titleFont.setColor(IndexedColors.WHITE.getIndex());
		}
		return titleFont;
	}
}
