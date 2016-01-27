package top.txiner.web;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wzhuo on 2015/12/30.
 */
public class GetSourceCode {
    public static void main(String[] args){
        HttpURLConnection conn=null;
        InputStream is=null;
        BufferedReader br=null;
        OutputStreamWriter osw=null;


        try {
            URL url=new URL("http://timle.cn");
            conn= (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode()!=200){
                System.exit(0);
            }
            is=conn.getInputStream();
            br=new BufferedReader(new InputStreamReader(is));
            String msg=null;
            while ((msg=br.readLine())!=null){
                System.out.println(msg);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
