package com.ffms.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.ffms.domain.Dump;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;

/**
 * excel导入文件，解析为泛型为String的List,单元格数值以    SEPARATOR  分割
 * @author zhangxs
 *
 */
public class ImportExcel {
    /**
     * Excel 2003
     */
    private final static String XLS = "xls";
    /**
     * Excel 2007
     */
    private final static String XLSX = "xlsx";


    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 由Excel文件的Sheet导出至List
     *暂时不支持，xlsx格式的数据
     * @param file
     * @param sheetNum
     * @return
     */
    public static List<Dump> exportListFromExcel(File file, int sheetNum)
            throws IOException {
        return exportListFromExcel(new FileInputStream(file),
                FilenameUtils.getExtension(file.getName()), sheetNum);
    }

    /**
     * 由Excel流的Sheet导出至List
     * 暂时不支持，xlsx格式的数据
     * @param is
     * @param extensionName
     * @param sheetNum
     * @return
     * @throws IOException
     */
    public static List<Dump> exportListFromExcel(InputStream is,
                                                   String extensionName, int sheetNum) throws IOException {
        Workbook workbook = null;
        if (extensionName.toLowerCase().equals(XLS)) {
            workbook = new HSSFWorkbook(is);
        } else if (extensionName.toLowerCase().equals(XLSX)) {
            workbook = new XSSFWorkbook(is);
        }
        return exportListFromExcel(workbook, sheetNum);
    }

    /**
     * 由指定的Sheet导出至List
     *
     * @param workbook
     * @param sheetNum
     * @return
     * @throws IOException
     */
    private static List<Dump> exportListFromExcel(Workbook workbook,
                                                    int sheetNum) {

        Sheet sheet = workbook.getSheetAt(sheetNum);

        // 解析公式结果
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        List<Dump> list = new ArrayList<Dump>();

        int minRowIx = sheet.getFirstRowNum();
        int maxRowIx = sheet.getLastRowNum();
        for (int rowIx = 5; rowIx <= maxRowIx; rowIx++) {
            Row row = sheet.getRow(rowIx);
            Dump dump=new Dump();

            short minColIx = row.getFirstCellNum();
            short maxColIx = row.getLastCellNum();
            for (short colIx = minColIx; colIx <= maxColIx; colIx++) {
                Cell cell = row.getCell(new Integer(colIx));
                CellValue cellValue = evaluator.evaluate(cell);
                if (cellValue == null) {
                    continue;
                }
                // 经过公式解析，最后只存在Boolean、Numeric和String三种数据类型，此外就是Error了
                // 其余数据类型，根据官方文档，完全可以忽略http://poi.apache.org/spreadsheet/eval.html
                switch (cellValue.getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:

                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if(colIx==9){//对应交易金额
                            dump.setConsumerAmount(cellValue.formatAsString().trim());
                        }
                        if(colIx==3){
                            if (DateUtil.isCellDateFormatted(cell)) {
                                DateTime dateTime = new DateTime(cell.getDateCellValue());
                                dump.setConsumerNameTime(dateTime.toString(STANDARD_FORMAT).trim());
                            }
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                         if(colIx==8){//对应商品名
                             dump.setConsumerName(cellValue.getStringValue().trim());
                         }
                         if(colIx==7){//对应交易对方
                             dump.setTradingParty(cellValue.getStringValue().trim());
                         }

                        if(colIx==14){//对应备注
                         dump.setRemarks(cellValue.getStringValue().trim());
                        }
                        if(colIx==10){
                            dump.setType(cellValue.getStringValue().trim());
                        }
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        break;
                    default:
                        break;
                }
            }
            list.add(dump);
        }
        return list;
    }


}