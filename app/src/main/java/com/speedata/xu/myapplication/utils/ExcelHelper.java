package com.speedata.xu.myapplication.utils;

import com.speedata.xu.myapplication.application.CustomerApplication;
import com.speedata.xu.myapplication.db.bean.BaseInfor;
import com.speedata.xu.myapplication.db.dao.BaseInforDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;


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


}
