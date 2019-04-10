package gr.aueb.cn;

import java.io.Serializable;

public class EnergyRequestWait extends EnergyRequest implements Serializable {

    private static final long serialVersionUID = 656263783489938432L;

    private long time;

    public EnergyRequestWait(int in_need, long time) {
        super(in_need);
        this.time = time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
