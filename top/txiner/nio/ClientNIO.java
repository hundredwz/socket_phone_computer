package top.txiner.nio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
/**
 * Created by hundred on 2015/12/20.
 */
public class ClientNIO {
    public Selector selector = null;
    static int BLOCK = 1024;
    public int ID = 0;
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);
    public static final InetSocketAddress ADDRESS = new InetSocketAddress
            ("localhost", 8080);
    public ClientNIO() {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(ADDRESS);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_CONNECT);
            ID = (int) (Math.random() * 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ClientNIO clientNIO = new ClientNIO();
        clientNIO.send();
    }
    private void send() {
        int count = 0;
        SelectionKey key = null;
        SocketChannel channel = null;
        String noticeMsg = "connect with you now.I am the " + ID;
        String receiveText;
        String sendText;
        while (true) {
            try {
                selector.select();
                Iterator iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    key = (SelectionKey) iter.next();
                    if (key.isConnectable()) {
                        System.out.println("connect to server");
                        channel = (SocketChannel) key.channel();
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                            sendBuffer.clear();
                            sendBuffer.put(noticeMsg.getBytes());
                            sendBuffer.flip();
                            channel.write(sendBuffer);
                        }
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        channel = (SocketChannel) key.channel();
                        receiveBuffer.clear();
                        count = channel.read(receiveBuffer);
                        if (count > 0) {
                            receiveText = new String(receiveBuffer.array(),
                                    0, count);
                            System.out.println("data from server: " +
                                    receiveText);
                            channel.register(selector, SelectionKey.OP_WRITE);
                        }
                    } else if (key.isWritable()) {
                        channel = (SocketChannel) key.channel();
                        sendBuffer.clear();
                        BufferedReader br = new BufferedReader(new
                                InputStreamReader(System.in));
                        sendText = br.readLine();
                        sendBuffer.put(sendText.getBytes());
                        sendBuffer.flip();
                        channel.write(sendBuffer);
                        channel.register(selector, SelectionKey.OP_READ);
                    }
                }
                selector.selectedKeys().clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
