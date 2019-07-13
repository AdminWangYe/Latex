package utils;

import java.io.File;

/**
 * @author: yyWang
 * @create: 2019/7/13
 * @description: 删除文件工具，或者删除文件目录
 */

public class DeleFile {
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
     *  删除单个文件
     * @param path
     * @return
     */
    public static boolean delFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        return file.delete();
    }
}
