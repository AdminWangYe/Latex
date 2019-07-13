package utils;

import java.io.File;
import java.io.IOException;

/**
 * @author: yyWang
 * @create: 2019/7/13
 * @description: 文件管理工具，删除文件，删除文件夹。判断文件是否存在，创建文件
 */

public class FileManage {
    /**
     * 删除文件夹下的所有文件
     *
     * @param path 文件夹的路径
     * @return 返回是否删除成功
     */
    public static boolean delAllFIle(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFIle(path + "/" + tempList[i]);// 删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除单个文件
     *
     * @param path 文件名
     * @return 删除结果
     */
    public static boolean delFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        return file.delete();
    }

    /**
     * 根据文件名判断文件是否存在，返回结果。
     *
     * @param file 文件名
     * @return 返回文件是否存在
     */
    public static boolean fileExist(String file) {
        File file1 = new File(file);
        return file1.exists();
    }

    /**
     * 根据文件名创建文件，返回文件句柄
     *
     * @param file 文件名
     * @return 返回文件描述符
     */
    public static File createFile(String file) {
        File file1 = new File(file);
        if (!file1.exists()){
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file1;
    }
}
