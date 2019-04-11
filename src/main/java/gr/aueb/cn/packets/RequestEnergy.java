package gr.aueb.cn.packets;

import java.io.Serializable;

public class RequestEnergy implements Serializable {

    private static final long serialVersionUID = -176592021009657617L;

    private String username;

    private String destination;

    private int energyNeeded;

    private Long delay = null;

    private boolean backup = false;

    public RequestEnergy(String username, String destination, int energyNeeded) {
        this.username = username;
        this.destination = destination;
        this.energyNeeded = energyNeeded;
    }

    public RequestEnergy(String username, String destination, int energyNeeded, boolean backup) {
        this.username = username;
        this.destination = destination;
        this.energyNeeded = energyNeeded;
        this.backup = backup;
    }

    public RequestEnergy(String username, String destination, int energyNeeded, Long delay) {
        this.username = username;
        this.destination = destination;
        this.energyNeeded = energyNeeded;
        this.delay = delay;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setEnergyNeeded(int energyNeeded) {
        this.energyNeeded = energyNeeded;
    }

    public String getUsername() {
        return username;
    }

    public String getDestination() {
        return destination;
    }

    public int getEnergyNeeded() {
        return energyNeeded;
    }

    public Long getDelay() {
        return delay;
    }

    public boolean wantsBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }
}

