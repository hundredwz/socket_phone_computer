package top.txiner.gui.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * Created by wzhuo on 2016/1/1.
 */
public class HandleJSON {

    /**
     * 解析json数据
     * @param msg
     * @return message
     */
    public Data parseJSON(String msg){
        Data data =new Data();
        JSONObject jsonObject = JSONObject.fromObject(msg);
        JSONArray jsonArray=jsonObject.getJSONArray("data");
        data.setName(jsonArray.getJSONObject(0).getString("name"));
        data.setTime(jsonArray.getJSONObject(0).getString("time"));
        data.setContent(jsonArray.getJSONObject(0).getString("content"));
        return data;
    }

    /**
     * 生成json数据
     * @param data
     * @return json
     */
    public String buildJSON(Data data){
        String json=null;
        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray=JSONArray.fromObject(data);
        jsonObject.put("data",jsonArray);
        json=jsonObject.toString();
        return json;
    }
}
