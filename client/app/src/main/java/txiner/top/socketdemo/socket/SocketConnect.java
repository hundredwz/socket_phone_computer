package txiner.top.socketdemo.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import txiner.top.socketdemo.MainActivity;
import txiner.top.socketdemo.util.HandleJSON;
import txiner.top.socketdemo.util.Data;

/**
 * Created by wzhuo on 2015/12/31.
 */
public class SocketConnect extends Service {

    HandleThread handleThread = null;

    Data data = new Data();

    Socket socket = null;
    static DataOutputStream dos = null;
    static DataInputStream dis = null;
    HandleJSON handleJSON = new HandleJSON();


    static String ip;
    static int port;

    boolean conn=false;

    public SocketConnect() {
    }

    public SocketConnect(String ip,int port) {
        this.ip=ip;
        this.port=port;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleThread = new HandleThread();
        handleThread.start();
        return Service.START_STICKY;
    }

    /**
     * 创建socket进行通信
     */
    private void init() {
        try {
            socket = new Socket(ip,port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            conn=true;
        } catch (IOException e) {
            conn=false;
            stopSelf();

        }
    }

    /**
     * 从服务端获取信息
     *
     * @return msg
     */
    public Data getData() {
        String msg = null;
        Data data = null;
        try {
            msg = dis.readUTF();
            data = handleJSON.parseJSON(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 关闭连接
     */
    public void disconnect() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dis != null) {
            try {
                dis.close();
                dis = null;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (dos != null) {
            try {
                dos.close();
                dos = null;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 向服务端发送消息
     *
     * @param data
     */
    public void sendMessage(Data data) {
        String msg = null;
        try {
            msg = handleJSON.buildJSON(data);
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class HandleThread extends Thread {
        Data data=null;
        Message message=null;
        Bundle bundle=null;
        @Override
        public void run() {
            init();
            while (conn) {
                data = getData();
                bundle=new Bundle();
                bundle.putSerializable("data",data);
                message=new Message();
                message.setData(bundle);
                MainActivity.handler.sendMessage(message);
            }
        }

    }
}
