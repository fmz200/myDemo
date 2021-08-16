package com.soft.mydemo.utils;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelExportUtils {

    private static String extracted(String string) throws ParseException {
        Date orderDateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string);
        return new SimpleDateFormat("yyyy-MM-dd").format(orderDateStart);
    }

    private static String extracted1(Date string) {
//		Date orderDateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(string);
        return new SimpleDateFormat("yyyy-MM-dd").format(string);
    }

    /**
     * @param workbook
     * @param fontsize
     * @return 单元格样式
     */
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize, boolean flag, boolean flag1, boolean flag2) {

        HSSFCellStyle style = workbook.createCellStyle();

//		//设置居中
//		HSSFCellStyle cellStyle = workBook.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 
//		//设置字体
//		HSSFFont font = workBook.createFont();  
//		font.setFontName("Times New Roman");  
//		font.setFontHeightInPoints((short) 10);//设置字体大小

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  //上边框

        //是否水平居中
//        if(flag1){
//        	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
//        }
//        if(flag2){
//        	style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//靠右
//        }

        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建字体
        HSSFFont font = workbook.createFont();
//        //是否加粗字体
//        if(flag){
//            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        }
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints(fontsize);
        //加载字体
        style.setFont(font);
        return style;
    }

    /**
     * @param workbook
     * @param sheet
     * @return 合并后单元格样式
     */
    private static void createCellsStyle(HSSFWorkbook workbook, HSSFSheet sheet, CellRangeAddress callRangeAddress, boolean flag) {

        if (flag) {
            RegionUtil.setBorderBottom(1, callRangeAddress, sheet, workbook); // 下边框
            RegionUtil.setBorderLeft(1, callRangeAddress, sheet, workbook); // 左边框
            RegionUtil.setBorderRight(1, callRangeAddress, sheet, workbook); // 右边框
            RegionUtil.setBorderTop(1, callRangeAddress, sheet, workbook); // 上边框
        }

    }


    public static void inputstreamtofile(InputStream ins, File file) {

        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception {

        ExcelExportUtils export = new ExcelExportUtils();
//		String srcFilePath = "/sharePage/exportTemplate/bancassurance/13024000000000_增员审核导出.xls";
        String srcFilePath = "d:/13024000000000_增员审核导出.xls";
        String fileName = "增员审核.xlsx";
        String desFilePath = "d://" + fileName;

        File file = new File("/smis/WebRoot/sharePage/exportTemplate/personalInsurance/自助入司基本信息导出.xls");

//		export.exportExcel(srcFilePath,desFilePath);
    }


}













