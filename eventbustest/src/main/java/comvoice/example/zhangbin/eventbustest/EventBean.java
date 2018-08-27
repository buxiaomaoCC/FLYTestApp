package comvoice.example.zhangbin.eventbustest;

import org.greenrobot.eventbus.EventBus;

public class EventBean {
    private String name;
    public EventBean(String name){
        this.name=name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
