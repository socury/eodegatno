package com.dgsw.eodegatno.support;

import com.dgsw.eodegatno.domain.ItemEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelBuilder {
    private final Workbook workbook;
    private final Sheet sheet;
    private final ByteArrayOutputStream out;
    private Row headerRow;
    private int currentRowNum = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ExcelBuilder(String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet(sheetName);
        this.out = new ByteArrayOutputStream();
    }

    /** 엑셀 빌더 생성*/
    public static ExcelBuilder create(String sheetName) {
        return new ExcelBuilder(sheetName);
    }
    /** 엑셀 헤더 설정*/
    public ExcelBuilder setHeader(String... headers) {
        CellStyle headerStyle = createHeaderStyle();
        headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        return this;
    }
    /** 엑셀 헤더 스타일 설정*/
    private CellStyle createHeaderStyle() {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return headerStyle;
    }
    /** 엑셀 행 설정*/
    public ExcelBuilder writeRows(List<ItemEntity> items) {
        for (ItemEntity item : items) {
            Row row = sheet.createRow(currentRowNum++);
            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(item.getReporterName());
            row.createCell(2).setCellValue(item.getItemName());
            row.createCell(3).setCellValue(dateFormat(item.getLostDateTime()));
            row.createCell(4).setCellValue(item.getLostLocation());
            row.createCell(5).setCellValue(item.getStatus().toString());
            row.createCell(6).setCellValue(item.getContactInfo() != null ? item.getContactInfo() : "");
            row.createCell(7).setCellValue(item.getDescription() != null ? item.getDescription() : "");
            row.createCell(8).setCellValue(dateFormat(item.getCreatedAt()));
            row.createCell(9).setCellValue(dateFormat(item.getUpdatedAt()));
        }
        return this;
    }
    /** 날짜 설정*/
    private String dateFormat(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
    /** 엑셀 파일 반환*/
    public byte[] build() {
        autoSizing();
        try {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 생성 중 오류가 발생했습니다.", e);
        }

        return out.toByteArray();
    }
    /** 행간 사이즈 조절*/
    private void autoSizing() {
        if (headerRow != null) {
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }
        }
    }
}
