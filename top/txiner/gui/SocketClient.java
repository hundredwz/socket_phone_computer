package top.txiner.gui;

import top.txiner.gui.util.HandleJSON;
import top.txiner.gui.util.Data;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wzhuo on 2015/12/24.
 */
public class SocketClient extends Frame {


    private static Data data = new Data();
    private HandleJSON handleJSON = new HandleJSON();

    private static final int ID = (int) (Math.random() * 100);
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;


    ReceiveThread rcvTh = new ReceiveThread();

    private TextField textField = new TextField();
    private TextArea textArea = new TextArea();

    static SimpleDateFormat sdf = null;


    public void launchFrame() {
        this.setSize(300, 300);
        this.setLocation(200, 300);
        this.setResizable(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        this.add(textField, BorderLayout.SOUTH);
        this.add(textArea, BorderLayout.NORTH);
        pack();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disConnect();
                System.exit(0);
            }
        });
        textField.addActionListener(new TFActionListener());
        this.setVisible(true);
        connect();
        rcvTh.start();
    }


    private void connect() {
        try {
            socket = new Socket("localhost", 8080);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            System.out.println("connect to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void disConnect() {
        try {
            dos.close();
            dis.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sdf = new SimpleDateFormat("HH:mm:ss");
        new SocketClient().launchFrame();
    }


    private class TFActionListener implements ActionListener {
        String content = null;
        String time = null;
        String json = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            content = textField.getText();
            data.setContent(content);
            textField.setText("");
            time = sdf.format(new Date());
            data.setTime(time);
            data.setName("" + ID);
            json = handleJSON.buildJSON(data);
            try {
                dos.writeUTF(json);
                dos.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private class ReceiveThread extends Thread {
        String json = null;

        @Override
        public void run() {
            try {
                while (true) {
                    json = dis.readUTF();
                    data = handleJSON.parseJSON(json);
                    textArea.append("\t\t\t" + data.getTime() + "\n");
                    textArea.append(data.getName() + " : " + data
                            .getContent() + "\n");
                    textArea.setCaretPosition(textArea.getText().length());
                }
            } catch (SocketException e) {
                System.out.println("this is closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
