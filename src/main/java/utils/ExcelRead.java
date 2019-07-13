package utils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yyWang
 * @create: 2019/7/13
 * @description: 读取Excel 里的内容，并将内容保存到list集合中，返回list集合
 */

public class ExcelRead {


    /**
     * 读取excel 表格的内容。并把结果写入list 集合中。
     *
     * @param file 需要读取的excel 文件名
     * @return
     */
    public static List<String> readExcel(String file) {

        List<String> outerList;

        File f = new File(file);
        // 如果文件不存在就返回空
        if (!f.exists()) {
            return null;
        }
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(f.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
//            Map<String, String> outerList = new HashMap<String, String>();
            outerList = new ArrayList<>();
            for (int index = 0; index < sheet_size; index++) {
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
//                第一行为注释，所以不用读取
                for (int i = 1; i < sheet.getRows(); i++) {
                    // sheet.getColumns()返回该页的总列数
//                    for (int j = 0; j < sheet.getColumns(); j++) {
                    String name = sheet.getCell(0, i).getContents();
                    if (name.equals("")) {
                        continue;
                    }
                    //获取Excel第几列(第8列是具体的详细解释）
                    String content = sheet.getCell(8, i).getContents().replaceAll("\n", "");
                    if (content.equals("")) {
                        continue;
                    }
//                    outerList.put(name, content);
                    outerList.add(content);
                }
            }
            return outerList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Excel 内的数据，并将结果写到TXT 文本中
     *
     * @param orgin    excel 表格里的内容
     * @param result   将内容写到TXT文本的名字
     * @param isAppend 写文件是否追加
     */
    public static void WriteData(String orgin, String result, boolean isAppend) {
        List<String> realist = readExcel(orgin);
        TextWriter textWriter = new TextWriter(result, isAppend);
        if (realist != null) {
            for (String str : realist) {
                textWriter.writeLines(str);
            }
            textWriter.close();
        }
    }
}
