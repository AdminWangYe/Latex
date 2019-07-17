package LatexApplication;

import latex.LatexConvert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author: yyWang
 * @create: 2019/7/16
 * @description: 服务端的多线程类
 */

public class ServerThread implements Runnable {
    private Socket client;

    public ServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // 获取socket 的输入流，用来向客户端发送数据
            PrintStream out = new PrintStream(client.getOutputStream());
            // 获取Socket的输入流，用来接收从客户端发送过来的数据
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            boolean flag = true;
            while (flag) {
                // 接收从客户端发送过来的数据
                String str = buf.readLine();
                if ("bye".equals(str)) {// 读到bye字符串时退出循环
                    flag = false;
                }else if ("1".equals(str)){
                    out.println("Server回复:我准备好了，开始吧");
                }else {
                    // Thread.sleep(20000);
                    // 将接收到的字符串前面加上“Server回复”，发送到对应的客户端
                    System.out.println(client.getRemoteSocketAddress() + "客户端消息：" + str);
//                    input.readLine();

                    LatexConvert latexConvert = new LatexConvert();
                    out.println("Server回复:" + latexConvert.getPattern(str));
                }
            }
            out.close();
            client.close();
            System.out.println("关闭连接");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
