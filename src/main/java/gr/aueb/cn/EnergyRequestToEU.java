package gr.aueb.cn;

import java.io.Serializable;

public class EnergyRequestToEU extends EnergyRequest implements Serializable {

    private static final long serialVersionUID = 45L;

    private EnergyUser eu;

    public EnergyRequestToEU(int in_need, EnergyUser eu) {
        super(in_need);
        this.eu = eu;
    }

    public void setEu(EnergyUser eu) {
        this.eu = eu;
    }

    public EnergyUser getEu() {
        return eu;
    }

}
