package LatexApplication;

import utils.TextReader;
import utils.TextWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * @author: yyWang
 * @create: 2019/7/16
 * @description: 客户端类，实体LaTeX化封装
 */

public class LatexClient {

    private String host = "127.0.0.1";
    private int port = 9994;
    // 从文件里读取要LaTeX的文本
    private List<String> question;
    // 需要转换的文件名
    private String orgin;
    // 转换的结果
    private String result;
    // 向文件TXT写入数据
    private TextWriter textWriter;

    public LatexClient(String orgin, String result) {
        this.orgin = orgin;
        this.result = result;
        initialise();
    }


    /**
     *  数据初始化，将数据读入到 list集合中，为实体 LaTeX 化做准备
     */
    private void initialise() {
        TextReader textReader = new TextReader(orgin);
        question = textReader.ReadLines();
        textReader.Close();
        textWriter = new TextWriter(result, false);

    }

    public LatexClient() {
    }

    /**
     * 客户端函数，
     * @throws IOException
     */
    public void client() throws IOException {
        Socket client = new Socket(host, port);
        // 获取键盘输入
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        // 获取Socket的输出流，用来发送数据到服务端
        PrintStream out = new PrintStream(client.getOutputStream());
        // 获取Socket的输入流，用来接收从服务端发送过来的数据
        BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        boolean flag = true;
        while (flag) {
            System.out.print("输入(1开始，bye退出)：");
            String str = input.readLine();
            // 发送数据到服务端
            out.println(str);
            if ("bye".equals(str)) {
                flag = false;
            } else {
                for (String string : question) {

                    try {
                        // 从服务器端接收数据有个时间限制（系统自设，也可以自己设置client.setSoTimeout(10000);），超过了这个时间，便会抛出该异常
                        out.println(string);
                        String echo = buf.readLine();
//                        System.out.println(echo);
                        textWriter.writeLines(echo.replaceAll("Server回复:", ""));
                    } catch (SocketTimeoutException e) {
                        System.out.println("Time out, No response");
                    }
                }
                System.out.println("实体LaTeX化完成。。。");

            }
        }
        input.close();
        if (client != null) {
            // 如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
            client.close(); // 只关闭socket，其关联的输入输出流也会被关闭
        }

    }

    public static void main(String[] args) throws IOException {
        LatexClient client = new LatexClient("relation_500.txt", "relation.txt");
        client.client();
    }


}
