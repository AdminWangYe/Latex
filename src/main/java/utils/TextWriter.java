package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author: yyWang
 * @create: 2019/7/12
 * @description: 向TXT 文本写入内容。
 */

public class TextWriter {

    private Writer writer;


    /**
     * 构造函数，根据文件名和是否追内容创建向文件写入流
     *
     * @param filepath 文件名
     * @param isAppend 是否追加
     */
    public TextWriter(String filepath, boolean isAppend) {
        if (fileExist(filepath)) {

            try {

                writer = new FileWriter(filepath, isAppend);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 判断当前填 txt 文件是否存在，如果不存在就创建
     *
     * @param filepath 文件名
     * @return 返回创建结果
     */
    private boolean fileExist(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 向文件写内容，但是没有换行。
     *
     * @param content 需要写的内容
     */
    public void write(String content) {
        try {
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 有换行的写入。
     *
     * @param content 需要写的内容
     */
    public void writeLines(String content) {
        try {
            writer.write(content + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭 IO操作
     */
    public void close() {
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
