package txiner.top.socketdemo.util;

import java.io.Serializable;

/**
 * Created by wzhuo on 2015/12/31.
 */
public class Data implements Serializable {
    private String name;
    private String time;
    private String content;
    private String remain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }
}
