package com.soft.mydemo.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImportExcel {

    /**
     * 从输入流中获取excel工作表
     *
     * @param ins        输入流
     * @param fileName   带 .xls或.xlsx 后缀的文件名
     * @param rowHeadIdx 表头行总数个数
     * @param colsCount  总列数
     * @param dataSize   可导入数据的大小
     * @param headList   表头list
     * @return
     */
    public static List<Map<String, String>> importExcel(InputStream ins, String fileName, int rowHeadIdx, int colsCount,
                                                        int dataSize, LinkedList<String> headList) {
        //返回数据
        List<Map<String, String>> dataList = new LinkedList<Map<String, String>>();
        int rowNum = 0;// 已取值的行数
        int realRowCount = 0;// 真正有数据的行数

        // 得到工作空间
        Workbook workbook = getWorkbookByInputStream(ins, fileName);
        //得到工作表
        Sheet sheet = getSheetByWorkbook(workbook, 0);
        //获取总行数，排除空行
        realRowCount = sheet.getPhysicalNumberOfRows();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Row row : sheet) {
            if (realRowCount == rowNum) {
                break;
            }
            if (isBlankRow(row)) {//空行跳过
                continue;
            }

            if (row.getRowNum() == -1) {
                continue;
            }
            if (row.getRowNum() < rowHeadIdx) {//表头跳过
                rowNum++;
                continue;
            }
            rowNum++;
            //根据表头获取总列数，不排除空行
            realRowCount = sheet.getPhysicalNumberOfRows();
            Map<String, String> valuesMap = new HashMap<String, String>();
            for (int colNum = 0; colNum < colsCount; colNum++) {
                //获取行中单元格的值
                valuesMap.put(headList.get(colNum), getCellValue(sheet, row, colNum));
            }
            dataList.add(valuesMap);
        }
        return dataList;
    }

    /**
     * 从输入流中获取excel工作表
     *
     * @param iStream  输入流
     * @param fileName 带 .xls或.xlsx 后缀的文件名
     * @return 文件名为空返回空; 格式不正确抛出异常; 正常返回excel工作空间对象
     */
    public static Workbook getWorkbookByInputStream(InputStream iStream, String fileName) {
        Workbook workbook = null;
        try {
            if (null == fileName) {
                return null;
            }
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(iStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(iStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return workbook;
    }

    /**
     * 从Workbook中获取一个sheet,如果没有就创建一个
     *
     * @param workbook 工作空间
     * @param index    第几个sheet
     * @return 返回sheet
     */
    public static Sheet getSheetByWorkbook(Workbook workbook, int index) {
        Sheet sheet = workbook.getSheetAt(index);
        if (null == sheet) {
            sheet = workbook.createSheet();
        }

        sheet.setDefaultRowHeightInPoints(20);// 行高
        sheet.setDefaultColumnWidth(20);// 列宽

        return sheet;
    }

    /**
     * 获取指定sheet指定row中指定column的cell值
     *
     * @param sheet  工作表
     * @param row    行
     * @param column 第几列
     * @return 返回单元格的值或""
     */
    public static String getCellValue(Sheet sheet, Row row, int column) {
        if (sheet == null || row == null) {
            return "";
        }

        return getCellValue(row.getCell(column));
    }

    /**
     * 从单元格中获取单元格的值
     *
     * @param cell 单元格
     * @return 返回值或""
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case 0:
                Number number = cell.getNumericCellValue();
                String numberStr = String.valueOf(number);

                if (numberStr.endsWith(".0")) {
                    numberStr = numberStr.replace(".0", "");// 取整数
                }
                if (numberStr.contains("E")) {
                    numberStr = new DecimalFormat("#").format(number);// 取整数
                }
                if (DateUtil.isCellDateFormatted(cell)) {
                    //用于转化为日期格式
                    Date d = cell.getDateCellValue();
                    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    numberStr = formater.format(d);
                }
                return numberStr;
            case 1:
                return cell.getStringCellValue().trim();
            case 2:// 公式
                return "";
            case 3:
                return "";
            case 4:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                break;
        }

        return "";
    }

    /**
     * 判断该行是否为空行
     *
     * @param row 行
     * @return 为空行返回true, 不为空行返回false
     */
    public static boolean isBlankRow(Row row) {
        if (row == null) {
            return true;
        }

        Iterator<Cell> iter = row.cellIterator();
        while (iter.hasNext()) {
            Cell cell = iter.next();
            if (cell == null) {
                continue;
            }

            String value = getCellValue(cell);
            if (!isNULLOrBlank(value)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断一个对象是否为空
     *
     * @param obj 对象
     * @return 为空返回true, 不为空返回false
     */
    public static boolean isNULLOrBlank(Object obj) {
        if (obj != null && !"".equals(obj.toString())) {
            return false;
        }

        return true;
    }

    /**
     * 校验是否为excel
     *
     * @param filename
     * @return
     */
    public static String isExcel(String filename) {
        if (!filename.endsWith(".xls") && !filename.endsWith(".xlsx")) {
            return "文件格式不正确,请选择文件格式为Excel的文件！";
        }
        return "";
    }

    /**
     * 判断excel是否符合指定规则
     *
     * @param ins
     * @param fileName
     * @param rowHeadIdx
     * @param dataSize
     * @return
     */
    public static String validateExcel(InputStream ins, String fileName, int rowHeadIdx, int dataSize) {
        StringBuffer sb = new StringBuffer();
        //判断是否为excel
        String result = isExcel(fileName);
        if (!result.equals("")) {
            sb.append(result);
            return sb.toString();
        }
        // 得到工作空间，如没有则返回未读取到文件
        Workbook workbook = getWorkbookByInputStream(ins, fileName);
        if (workbook == null) {
            sb.append("未读取到excel文件");
            return sb.toString();
        }
        //得到工作表,判断表中是否为空或数据量超过指定大小
        Sheet sheet = getSheetByWorkbook(workbook, 0);
        int size = dataSize + rowHeadIdx - 1;
        System.out.println("getLastRowNum=" + sheet.getLastRowNum());
        System.out.println("getPhysicalNumberOfRows=" + sheet.getPhysicalNumberOfRows());
        if (sheet.getLastRowNum() > size) {
            sb.append("本次最多可导入" + dataSize + "条，请核对导入数量！");
            return sb.toString();
        } else if (sheet.getPhysicalNumberOfRows() <= rowHeadIdx) {
            sb.append("文件里没有数据，请核对！");
            return sb.toString();
        }
        return sb.toString().trim();
    }

    //a by ljm for L1517 on 2021年7月4日16:18:47 start
    public static String validateExcel2(InputStream ins, String fileName, int rowHeadIdx, int MaxSize) {
        StringBuffer sb = new StringBuffer();
        //判断是否为excel
        String result = isExcel(fileName);
        if (!result.equals("")) {
            sb.append(result);
            return sb.toString();
        }
        // 得到工作空间，如没有则返回未读取到文件
        Workbook workbook = getWorkbookByInputStream(ins, fileName);
        if (workbook == null) {
            sb.append("未读取到excel文件");
            return sb.toString();
        }
        //得到工作表,判断表中是否为空
        int size = MaxSize + rowHeadIdx - 1;
        Sheet sheet = getSheetByWorkbook(workbook, 0);
        int row = sheet.getPhysicalNumberOfRows();
        if (sheet.getLastRowNum() > size) {
            sb.append("本次最多可导入" + MaxSize + "条，请核对导入数量！");
            return sb.toString();
        } else if (row <= rowHeadIdx) {
            sb.append("文件里没有数据，请核对！");
            return sb.toString();
        }

        return sb.toString().trim();
    }

    /**
     * 从输入流中获取excel工作表
     *
     * @param ins        输入流
     * @param fileName   带 .xls或.xlsx 后缀的文件名
     * @param rowHeadIdx 表头行总数个数
     * @param colsCount  总列数
     * @param headList   表头list
     * @return
     */
    public static List<Map<String, String>> importExcel2(InputStream ins, String fileName, int rowHeadIdx, int colsCount,
                                                         LinkedList<String> headList) {
        //返回数据
        List<Map<String, String>> dataList = new LinkedList<Map<String, String>>();
        int rowNum = 0;// 已取值的行数
        int realRowCount = 0;// 真正有数据的行数

        // 得到工作空间
        Workbook workbook = getWorkbookByInputStream(ins, fileName);
        //得到工作表
        Sheet sheet = getSheetByWorkbook(workbook, 0);
        //获取总行数，排除空行
        realRowCount = sheet.getPhysicalNumberOfRows();
        for (Row row : sheet) {
            if (realRowCount == rowNum) {
                break;
            }
            if (isBlankRow(row)) {//空行跳过
                continue;
            }

            if (row.getRowNum() == -1) {
                continue;
            }
            if (row.getRowNum() < rowHeadIdx) {//表头跳过
                rowNum++;
                continue;
            }
            rowNum++;
            //根据表头获取总列数，不排除空行
            realRowCount = sheet.getPhysicalNumberOfRows();
            Map<String, String> valuesMap = new HashMap<String, String>();
            for (int colNum = 0; colNum < colsCount; colNum++) {
                //获取行中单元格的值
                valuesMap.put(headList.get(colNum), getCellValue(sheet, row, colNum));
            }
            dataList.add(valuesMap);
        }
        return dataList;
    }
    //a by ljm for L1517 on 2021年7月4日16:18:47 end
}
