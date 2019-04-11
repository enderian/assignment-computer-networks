package gr.aueb.cn.packets;

import java.io.Serializable;

public class RequestEnergyFailure implements Serializable {

    private static final long serialVersionUID = -176592021009657617L;

    private String destination;

    public RequestEnergyFailure(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

