package com.elroykanye.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Driver {

    public static Person arrangePersonName(Person person) {
        String[] names = person.getName().split(" ");
        System.out.println(person);
        StringBuilder newName = new StringBuilder();
        for (String name: names) {
            char[] nameAsArray = name.toCharArray();
            if (nameAsArray.length > 0) {
                nameAsArray[0] = Character.toUpperCase(nameAsArray[0]);
                for (int i = 1; i < nameAsArray.length; i++) {
                    nameAsArray[i] = Character.toLowerCase(nameAsArray[i]);
                }
                newName.append(" ").append(String.copyValueOf(nameAsArray));
            }

        }
        person.setName(newName.toString());
        return person;
    }
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
        return rsvps.stream().map(Driver::arrangePersonName).collect(Collectors.toList());
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
