package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yyWang
 * @create: 2019/7/12
 * @description: 读取 txt 文本里的内容,并将读取的内容保存到list 集合中。
 */

public class TextReader {
    private BufferedReader reader;
    private FileReader r;

    public TextReader(String path) {
        try {
            r = new FileReader(path);
            reader = new BufferedReader(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取text文本里的内容，保存到list集合中
     *
     * @return 返回文本内容
     */
    public List<String> ReadLines() {
        List<String> res = new ArrayList<String>();
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                res.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 关闭IO操作
     */
    public void Close() {
        try {
            reader.close();
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
