package com.livecard.front.common.file;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ExcelUtil {
    public <T> List<T> parseExcelToObject(MultipartFile file, Class<T> clazz) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /* read workbook & sheet */
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        /* parse header & body */
        parseHeader(sheet, clazz);
        return parseBody(sheet, clazz);
    }

    /**
     * 엑셀 업로드 직전에 파일의 헤더가 T 클래스와 호환 가능한지 검사하는 메서드이다.
     * 엑셀의 헤더 목록과 DTO 클래스의 필드 목록을 담은 두 HashSet 을 비교한다.
     * 단, 클래스 필드의 경우 위에서 만들어 놓은 @ExcelColumn 어노테이션이 붙은 필드만 스캔한다.
     * 이 때 어노테이션에 headerName이 주어지면 이 값으로 매핑해 준다.
     * 클래스의 헤더가 엑셀 헤더의 부분집합이면 검증에 성공하도록 만들었다.
     * 예를 들어 두 HashSet이 아래와 같을 때, 모든 클래스 헤더를 엑셀이 커버할 수 있으므로 처리가 가능하다.
     * excelHeaders : {"순번", "이름", "나이", "성별"}
     * classHeaders : {"순번", "이름", "나이", "성별", "부서", "비고"}
     */
    private <T> void parseHeader(Sheet sheet, Class<T> clazz) {
        Set<String> excelHeaders = new HashSet<>();
        Set<String> classHeaders = new HashSet<>();

        int headerStartRowToParse = 0;

        sheet.getRow(headerStartRowToParse).cellIterator()
                .forEachRemaining(e -> excelHeaders.add(e.getStringCellValue()));

        Arrays.stream(clazz.getDeclaredFields())
                .filter(e -> e.isAnnotationPresent(ExcelColumn.class))
                .forEach(e -> {
                    if (e.getAnnotation(ExcelColumn.class).headerName().equals("")) {
                        classHeaders.add(e.getName());
                    }
                    else {
                        classHeaders.add(e.getAnnotation(ExcelColumn.class).headerName());
                    }
                });

        if (!excelHeaders.containsAll(classHeaders)) {
//            throw new BusinessException(
//                    String.format("Excel file headers are not compatible with given class %s", clazz.getName()),
//                    ErrorCode.BAD_REQUEST_ERROR
//            );
        }
    }

    /**
     * 이 함수는 엑셀의 바디를 T 클래스의 리스트로 변환한다. 개선 2에서 설명한 fillUpFromRow 추상 메서드를 여기서 활용한다.
     * 임의의 객체를 생성하기 위해 리플렉션 API로 새 객체를 만들고, 값을 채운 뒤 ArrayList에 담아 반환한다
     */
    private <T> List<T> parseBody(Sheet sheet, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> objects = new ArrayList<>();
        clazz.getDeclaredConstructor().setAccessible(true);

        for(int i = 1; i <= sheet.getLastRowNum(); i++) {
            T object = clazz.getDeclaredConstructor().newInstance();
            clazz.getMethod("fillUpFromRow", Row.class).invoke(object, sheet.getRow(i));
            objects.add(object);
        }
        return objects;
    }

    public <T> void renderObjectToExcel(OutputStream stream, List<T> data, Class<T> clazz) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // XSSF는 .xlsx 파일 형식에 사용
        Sheet sheet = workbook.createSheet("Data");

        // 헤더와 바디 렌더링
        renderHeader(sheet, clazz);
        renderBody(sheet, data, clazz);

        // 스트림에 쓰기
        workbook.write(stream);
        workbook.close();
    }

    private <T> void renderHeader(Sheet sheet, Class<T> clazz) {
        Row headerRow = sheet.createRow(0);

        int columnCount = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                String headerName = field.getAnnotation(ExcelColumn.class).headerName();
                headerName = headerName.isEmpty() ? field.getName() : headerName;

                Cell cell = headerRow.createCell(columnCount++);
                cell.setCellValue(headerName);
            }
        }
    }

    private <T> void renderBody(Sheet sheet, List<T> data, Class<T> clazz) {
        int rowCount = 1;

        for (T item : data) {
            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    field.setAccessible(true);

                    Cell cell = row.createCell(columnCount++);
                    try {
                        Object value = field.get(item);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(""); // null 값이면 빈 문자열로 설정
                        }
                    } catch (IllegalAccessException e) {
                        // 예외 처리
                    }
                }
            }
        }
    }


    public File createZipFile(List<File> files,String name) throws IOException {
        File zipFile = File.createTempFile(name, ".zip");
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                Files.copy(file.toPath(), zos);
                zos.closeEntry();
            }
        }
        return zipFile;
    }
}
