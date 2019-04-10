package gr.aueb.cn;

import java.io.Serializable;

public class EnergyRequest implements Serializable {

    private static final long serialVersionUID = -5126351414779181669L;

    protected int in_need;

    public EnergyRequest(int in_need) {
        this.in_need = in_need;
    }

    public void setIn_need(int in_need) {
        this.in_need = in_need;
    }

    public int getIn_need() {
        return in_need;
    }
}
