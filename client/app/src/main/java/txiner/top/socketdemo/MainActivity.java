package txiner.top.socketdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

import txiner.top.socketdemo.socket.SocketConnect;
import txiner.top.socketdemo.util.ContentAdapter;
import txiner.top.socketdemo.util.Data;

public class MainActivity extends AppCompatActivity {
    Button sendBtn;
    EditText toSendContent;
    ListView listView;

    long timeBack;

    public static ContentAdapter adapter;
    public static Handler handler = null;

    SocketConnect socketConnect = null;
    Data data = new Data();

    SimpleDateFormat sdf = null;
    private static final int ID = (int) (Math.random() * 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdf = new SimpleDateFormat("HH:mm:ss");

        socketConnect=new SocketConnect("192.168.1.101",8080);

        init();
    }

    private void init() {
        sendBtn = (Button) findViewById(R.id.sned);
        toSendContent = (EditText) findViewById(R.id.tosendcontent);
        listView = (ListView) findViewById(R.id.chatlist);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        adapter = new ContentAdapter(this, ID);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Data data = (Data) msg.getData().getSerializable("data");
                adapter.onReceive(data);
                adapter.notifyDataSetChanged();
            }
        };
        listView.setAdapter(adapter);
        sendBtn.setOnClickListener(new SendListener());
        startService(new Intent(this, SocketConnect.class));
    }

    private class SendListener implements View.OnClickListener {
        String content = null;
        String time = null;

        @Override
        public void onClick(View v) {
            content = String.valueOf(toSendContent.getText());
            data.setContent(content);
            time = sdf.format(new Date());
            data.setTime(time);
            data.setName("" + ID);
            socketConnect.sendMessage(data);
            toSendContent.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - timeBack < 2000) {
            super.onBackPressed();
            socketConnect.disconnect();
            finish();
        } else {
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            timeBack = now;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.setIP){
            setIPDialog();
        }
        return true;
    }

    public void setIPDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("设置IP");
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_setip,null);
        builder.setView(view);
        final EditText ipAddr= (EditText) view.findViewById(R.id.ipaddr);
        final EditText port = (EditText) view.findViewById(R.id.port);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ip=ipAddr.getText().toString();
                int portNum=Integer.parseInt(port.getText().toString());
                if (socketConnect!=null){
                    socketConnect.stopSelf();
                }
                socketConnect=new SocketConnect(ip,portNum);
                startService(new Intent(MainActivity.this, SocketConnect.class));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

}
