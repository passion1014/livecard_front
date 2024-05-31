package com.livecard.front.common.file;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHandler {
    @Transactional
    public List<?> uploadAccountWithExcel(MultipartFile file) throws IOException {
        List<String> header = new ArrayList<>();
//        List<AccountExcelRequestDto> body = new ArrayList<>();

        Iterator<Row> rowIterator = new XSSFWorkbook(file.getInputStream())
                .getSheetAt(0)
                .rowIterator();

        /* parse header */
        rowIterator.next()
                .cellIterator()
                .forEachRemaining(cell -> header.add(cell.getStringCellValue()));

        /* validate header */
        if (!header.equals(List.of("순번", "이름", "나이", "성별"))) {
//            throw new BusinessException("파일의 헤더가 잘못되었습니다.", ErrorCode.INVALID_EXCEL_HEADER)
        }

        /* parse body */
//        rowIterator.forEachRemaining(row -> body.add(new AccountExcelRequestDto.from(
//                row.getCell(0).getStringCellValue(),
//                row.getCell(1).getNumericCellValue(),
//                row.getCell(2).getNumericCellValue(),
//                row.getCell(3).getBooleanCellValue(),
//                row.getCell(4).getStringCellValue()
//        )));

        /* your business logic here */
        return null;
    }
}
