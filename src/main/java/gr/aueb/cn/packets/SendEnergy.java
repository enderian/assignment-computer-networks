package gr.aueb.cn.packets;

import java.io.Serializable;

public class SendEnergy implements Serializable {

    private static final long serialVersionUID = -2343976696454617381L;
    private String source;
    private String destination;
    private int energy;

    public SendEnergy(String source, String destination, int energy) {
        this.source = source;
        this.destination = destination;
        this.energy = energy;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getEnergy() {
        return energy;
    }
}
