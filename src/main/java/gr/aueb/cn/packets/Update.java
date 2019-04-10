package gr.aueb.cn.packets;

import gr.aueb.cn.EnergyUser;

public class Update {

    private String username;
    private EnergyUser energyUser;

    public Update(String username, EnergyUser energyUser) {
        this.username = username;
        this.energyUser = energyUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public EnergyUser getEnergyUser() {
        return energyUser;
    }

    public void setEnergyUser(EnergyUser energyUser) {
        this.energyUser = energyUser;
    }

}
