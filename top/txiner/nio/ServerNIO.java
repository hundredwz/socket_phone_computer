package top.txiner.nio;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
/**
 * Created by hundred on 2015/12/20.
 */
public class ServerNIO {
    public static final InetSocketAddress ADDRESS = new InetSocketAddress(8080);
    public Selector selector = null;
    static int BLOCK = 1024;
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);
    public ServerNIO() {
        try {
            ServerSocketChannel channel = null;
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            ServerSocket serverSocket = channel.socket();
            serverSocket.bind(ADDRESS);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ServerNIO serverNIO = new ServerNIO();
        serverNIO.listen();
    }
    private void listen() {
        Iterator iter = null;
        while (true) {
            try {
                selector.select();
                iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = (SelectionKey) iter.next();
                    iter.remove();
                    handle(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void handle(SelectionKey key) {
        String receiveText;
        String sendText;
        int count = 0;
        SocketChannel channel = null;
        if (key.isAcceptable()) {
            try {
                ServerSocketChannel serverSocketChannel =
                        (ServerSocketChannel) key.channel();
                channel = serverSocketChannel.accept();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (key.isReadable()) {
            try {
                channel = (SocketChannel) key.channel();
                receiveBuffer.clear();
                count = channel.read(receiveBuffer);
                if (count > 0) {
                    receiveText = new String(receiveBuffer.array(), 0, count);
                    System.out.println("data from client: " + receiveText);
                    channel.register(selector, SelectionKey.OP_WRITE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (key.isWritable()) {
            try {
                sendBuffer.clear();
                channel = (SocketChannel) key.channel();
                BufferedReader br = new BufferedReader(new InputStreamReader
                        (System.in));
                sendText = br.readLine();
                sendBuffer.put(sendText.getBytes());
                sendBuffer.flip();
                channel.write(sendBuffer);
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
