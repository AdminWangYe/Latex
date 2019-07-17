package LatexApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: yyWang
 * @create: 2019/7/16
 * @description: LaTeX服务端
 */

public class LatexServer {

    private static final int port = 9994;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务器已经启动了");
        Socket client;
        boolean flag  = true;
        while (flag){
            client = serverSocket.accept();
            System.out.println("与"+client.getRemoteSocketAddress()+"客户端连接成功！");
            new Thread(new ServerThread(client)).start();
        }
        serverSocket.close();
    }

}
