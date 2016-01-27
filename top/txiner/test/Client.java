package top.txiner.test;

import java.io.*;
import java.net.Socket;

public class Client {
    public static final String IP_ADDR = "localhost";//服务器地址
    public static final int PORT = 12345;//服务器端口号
    Socket socket = null;
    DataOutputStream out = null;
    DataInputStream input = null;

    public void init() {
        System.out.println("客户端启动...");
        System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");
        //创建一个流套接字并将其连接到指定主机上的指定端口号
        try {
            socket = new Socket(IP_ADDR, PORT);
            out = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new SendThread().start();
        while (true) {
            try {
                //读取服务器端数据
                String ret = null;
                ret = input.readUTF();
                System.out.println("服务器端返回过来的是: " + ret);
                // 如接收到 "OK" 则断开连接
                if ("OK".equals(ret)) {
                    System.out.println("客户端将关闭连接");
                    Thread.sleep(500);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Client().init();

    }

    private class SendThread extends Thread {

        @Override
        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(System
                    .in));
            String str = null;
            //向服务器端发送数据
            try {
                while (true) {
                    System.out.print("请输入: \t");
                    str = br.readLine();
                    out.writeUTF(str + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
