
import java.net.*;
import java.io.*;
public class TCPEchoServer {

    public static void main(String[] args) throws IOException {
        int servPort=8888;
        ServerSocket servSocket = new ServerSocket(servPort);

        while (true) {
            // 阻塞等待，每接收到一个请求就创建一个新的连接实例
            Socket clntSocket = servSocket.accept();
            // 获取连接的客户端的 SocketAddress
            SocketAddress clientAddress = clntSocket.getRemoteSocketAddress();
            // 打印输出连接客户端地址信息
            System.out.println("Handling client at" + clientAddress);
            // 从客户端接收数据的对象
            DataInputStream in =new DataInputStream( clntSocket.getInputStream());
            // 向客户端发送数据的对象
            DataOutputStream out = new DataOutputStream(clntSocket.getOutputStream());
            String inmsg,data;
            // 读取客户端发送的数据后，再发送到客户端
            while((inmsg=in.readUTF())!=null){
                System.out.println(inmsg);
                data=getMessage(inmsg);
                out.writeUTF(data);
            }
            // 客户端关闭连接时，关闭连接
            System.out.println(" 客户端关闭连接");
            clntSocket.close();
        }
    }
    public static String getMessage(String inmsg){
        String info="zhaobudao";
        switch (inmsg){
            case "xuehao":
                info="20131518";
                break;
            case "xingming":
                info="liaohonghai";
                break;
            case "chengji":
                info="manfen";
                break;
            default:
                info="zhaobudao";
                break;
        }
        return info;
    }

}
