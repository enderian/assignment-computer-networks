package gr.aueb.cn;

import java.io.Serializable;

public class UpdateRequest implements Serializable {

    private static final long serialVersionUID = 5L;

    private int newUnits, reservedUnits;

    public UpdateRequest() {

    }

    public void setNewUnits(int newUnits) {
        this.newUnits = newUnits;
    }

    public void setReservedUnits(int reservedUnits) {
        this.reservedUnits = reservedUnits;
    }

    public int getNewUnits() {
        return newUnits;
    }

    public int getReservedUnits() {
        return reservedUnits;
    }

}
