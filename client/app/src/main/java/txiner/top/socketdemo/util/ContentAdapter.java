package txiner.top.socketdemo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import txiner.top.socketdemo.R;

/**
 * Created by wzhuo on 2015/12/31.
 */
public class ContentAdapter extends BaseAdapter {

    Context context;
    int ID;
    ArrayList<Data> datas;

    public ContentAdapter(Context context, int ID) {
        this.context = context;
        this.ID = ID;
        datas = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    public void onReceive(Data msg) {
        datas.add(msg);
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.user = (ImageView) convertView.findViewById(R.id.user);
            viewHolder.me = (ImageView) convertView.findViewById(R.id.me);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        Data msg = datas.get(position);
        if (msg.getName().equals("" + ID)) {
            viewHolder.user.setVisibility(View.INVISIBLE);
            viewHolder.me.setVisibility(View.VISIBLE);
            viewHolder.content.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
            viewHolder.time.setGravity(View.TEXT_ALIGNMENT_VIEW_START);
        } else {
            viewHolder.me.setVisibility(View.INVISIBLE);
            viewHolder.user.setVisibility(View.VISIBLE);
            viewHolder.content.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
            viewHolder.time.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
        }
        viewHolder.time.setText(msg.getTime());
        viewHolder.content.setText(msg.getContent());
        return convertView;
    }


    private class ViewHolder {
        ImageView user, me;
        TextView time, content;
    }
}
