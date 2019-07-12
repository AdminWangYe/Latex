import latex.LatexConvert;
import utils.TextReader;
import utils.TextWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yyWang
 * @create: 2019/7/12
 * @description: 主函数，调用LaTeX类进行 文本实例化转换。
 */

public class LaTeXMain {
    public static void main(String[] args) {
        LatexConvert latexConvert = new LatexConvert();
        TextWriter textWriter = new TextWriter("result.txt", false);
        TextReader textReader = new TextReader("tmp.txt");
        // 保存读取的题目内容
        List<String> question;
        // 题目转换的结果
        List<String> result = new ArrayList<>();

        question = textReader.ReadLines();
        for (String str : question) {
            result.add(latexConvert.getPattern(str));
        }
        for (String strs : result) {
            textWriter.writeLines(strs);
        }

    }
}
