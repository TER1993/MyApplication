package com.speedata.xu.myapplication.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xuyan 导入文件.
 */
public class FileInputUtils {

    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param path 文件夹路径
     * @return     排序后的文件名
     */
    public static List<File> listFileSortByModifyTime(String path) {
        List<File> list = getFiles(path, new ArrayList<>());
        if (list.size() > 0) {
            Collections.sort(list, (file, newFile) -> Long.compare(file.lastModified(), newFile.lastModified()));
        }
        return list;
    }

    /**
     *
     * 获取目录下所有文件
     *
     * @param realpath 文件夹路径
     * @param files 文件们
     * @return 所有文件的list
     */
    private static List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }


    /**
     * 拷贝内部存储空间的数据库到外部
     *
     * @throws FileNotFoundException
     */
    public static void copyDBFileITO() throws FileNotFoundException {
        //外部存储文件夹
        File toDir = new File("/storage/usbotg");
        if (!toDir.exists()) {
            Logcat.d( "拷贝内部存储空间的数据库到外部---无外部文件夹");
            return;
        }
        //外部存储数据库
        File toDb = new File("/storage/usbotg/TongHao.db");
        //内部存储数据库
        File fromDir = new File("/data/data/com.spd.tonghao/databases/TongHao.db");

        InputStream is;
        OutputStream os;
        is = new FileInputStream(fromDir);
        os = new FileOutputStream(toDb);
        byte[] buffer = new byte[1024];
        int length;
        try {
            /*
             * 拷贝过程
             */
            while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                os.write(buffer, 0, length);
            }

            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝外部存储空间的数据库到内部
     *
     * @throws FileNotFoundException
     */
    public static void copyDBFileOTI() throws FileNotFoundException {
        //内部存储文件夹
        File toDir = new File("/data/data/com.spd.tonghao/databases");
        if (!toDir.exists()) {
            Logcat.d("拷贝外部存储空间的数据库到内部---无内部文件夹");
            return;
        }
        //外部存储数据库
        File toDb = new File("/data/data/com.spd.tonghao/databases/TongHao.db");
        //内部存储数据库
        File fromDir = new File("/storage/usbotg/TongHao.db");

        InputStream is;
        OutputStream os;
        is = new FileInputStream(fromDir);
        os = new FileOutputStream(toDb);
        byte[] buffer = new byte[1024];
        int length;
        try {
            /*
             * 拷贝过程
             */
            while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                os.write(buffer, 0, length);
            }

            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
