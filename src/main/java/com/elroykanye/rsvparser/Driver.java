package com.elroykanye.rsvparser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Driver {

    public static List<Person> excelReader(java.lang.String fileLocation, boolean skipFirstRow) {
        List<Person> rsvps = new ArrayList<>();
        File excelFile  = new File(fileLocation);
        try {
            // Open the file
            FileInputStream fileInputStream = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(fileInputStream);


            // Retrieve the sheet
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();

            int rowNumber = 0;
            while(rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Person rsvp = new Person();

                if(rowNumber == 0 && skipFirstRow) {
                    rowNumber++;
                    continue;
                }

                while(cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();

                    int colIndex = nextCell.getColumnIndex();

                    switch (colIndex) {
                        case 0 -> rsvp.setId(Math.round((double) getCellValue(nextCell)));
                        case 5 -> rsvp.setName((java.lang.String) getCellValue(nextCell));
                        case 6 -> rsvp.setEmail((java.lang.String) getCellValue(nextCell));
                        case 7 -> rsvp.setGender((String) getCellValue(nextCell));
                    }

                }
                rsvps.add(rsvp);
            }
            workbook.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsvps;
    }

    private static Object getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            default -> "Nan";
        };
    }
}
