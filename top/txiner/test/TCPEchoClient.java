
import java.io.*;
import java.net.Socket;


public class TCPEchoClient {
    public static void main(String[] args) throws IOException {
        String server="127.0.0.1";
        int servPort=8888;
        String data="sfjsh";
        Socket socket = new Socket(server, servPort);
        System.out.println("Connected to server...sending echo string");
        while(true){
            // 返回此套接字的输入流，即从服务器接受的数据对象
            DataInputStream in =new DataInputStream( socket.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            data=br.readLine();
            // 返回此套接字的输出流，即向服务器发送的数据对象
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            // 向服务器发送从控制台接收的数据
            out.writeUTF(data);
            String msg=in.readUTF();
            // 打印服务器发送来的数据
            System.out.println("Received:" + msg);
        }

    }
}
