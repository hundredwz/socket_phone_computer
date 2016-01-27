package top.txiner.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wzhuo on 2015/12/24.
 */
public class SocketServer {

    List<Client> clients = null;

    public static void main(String[] args) {
        new SocketServer().init();
    }

    private void init() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
            clients = new ArrayList<>();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("one connect");
                Client c = new Client(socket);
                c.start();
                clients.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Client extends Thread {
        private Socket socket=null;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;

        public Client(Socket socket) {
            this.socket = socket;
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String str) {
            try {
                dos.writeUTF(str);
                dos.flush();
            } catch (IOException e) {
                clients.remove(this);
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String str = dis.readUTF();
                    Iterator<Client> iter=clients.iterator();
                    while (iter.hasNext()){
                        Client client=iter.next();
                        client.send(str);
                    }
                }
            } catch (EOFException e) {
                clients.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dis != null)
                        dis.close();
                    if (dos != null)
                        dos.close();
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
