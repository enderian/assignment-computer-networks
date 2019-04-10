package gr.aueb.cn.model;

import java.net.InetAddress;

public class User {

    private InetAddress ip;
    private String username, password;
    private int availablePower, reservedPower, neededPower;

    User(InetAddress ip, String username, String password, int availablePower, int reservedPower, int neededPower) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.availablePower = availablePower;
        this.reservedPower = reservedPower;
        this.neededPower = neededPower;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAvailablePower() {
        return availablePower;
    }

    public void setAvailablePower(int availablePower) {
        this.availablePower = availablePower;
    }

    public int getReservedPower() {
        return reservedPower;
    }

    public void setReservedPower(int reservedPower) {
        this.reservedPower = reservedPower;
    }

    public int getNeededPower() {
        return neededPower;
    }

    public void setNeededPower(int neededPower) {
        this.neededPower = neededPower;
    }
}
