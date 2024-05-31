package com.livecard.front.common.file;

import org.apache.poi.ss.usermodel.Row;

public interface ExcelObject {
    void fillUpFromRow(Row row);
}
