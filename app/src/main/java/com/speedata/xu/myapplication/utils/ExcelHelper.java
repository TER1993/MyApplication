package com.speedata.xu.myapplication.utils;

import android.content.Context;

import com.speedata.xu.myapplication.application.CustomerApplication;
import com.speedata.xu.myapplication.db.bean.BaseInfor;
import com.speedata.xu.myapplication.db.bean.OutputTxt;
import com.speedata.xu.myapplication.db.dao.BaseInforDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


/**
 * @author :Reginer in  2018/7/4 15:55.
 * 联系方式:QQ:282921012
 * 功能描述:读取Ecxcel
 */

public class ExcelHelper {


    /**
     * 读取指定目录中的配置信息表
     *
     * @param file file
     * @return 用户集合
     */
    public static int readExcelFile(File file) {
        InputStream is;
        BaseInforDao baseInforDao = new BaseInforDao(CustomerApplication.getInstance());
        try {
            is = new FileInputStream(file);
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("GBK");
            Workbook workbook = Workbook.getWorkbook(is, workbookSettings);
            Sheet s = workbook.getSheet("Sheet1");
            int rawS = s.getRows();
            List<BaseInfor> listResult = new ArrayList<>();
            for (int i = 1; i < rawS; i++) {
                String number = s.getCell(0, i).getContents();
                String name = s.getCell(1, i).getContents();
                String price = s.getCell(2, i).getContents();
                String countnum = "1";

                BaseInfor bean = new BaseInfor();
                bean.setGoodsNum(number);
                bean.setGoodsName(name);
                bean.setGoodsPrice(price);
                bean.setGoodsCount(countnum);
                listResult.add(bean);
            }

            baseInforDao.imInsertList(listResult);
            is.close();

            return rawS;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 生成excel
     *
     */
    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";


    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化Excel表格
     *
     * @param filePath  存放excel文件的路径（path/demo.xls）
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名（可以有多个）
     */
    public static void initExcel(String filePath, String sheetName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                return;
            }
            workbook = Workbook.createWorkbook(file);
            //设置表格的名字
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            //创建标题栏
            sheet.addCell(new Label(0, 0, filePath, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将制定类型的List写入Excel中
     *
     * @param objList  待写入的list
     * @param fileName 完整路径+文件名
     * @param c  上下文
     * @param <T> 数据
     */
    public static <T> void writeObjListToExcel(List<T> objList, String fileName, Context c) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);

                for (int j = 0; j < objList.size(); j++) {
                    OutputTxt demoBean = (OutputTxt) objList.get(j);
                    List<String> list = new ArrayList<>();

                    list.add(demoBean.getGoodsnumber());
                    list.add(String.valueOf(demoBean.getGoodscount()));

                    for (int i = 0; i < list.size(); i++) {

                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }


                writebook.write();
                workbook.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }


}
