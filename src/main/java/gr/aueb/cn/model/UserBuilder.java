package gr.aueb.cn.model;

import java.net.InetAddress;

public class UserBuilder {
    private InetAddress ip;
    private String username;
    private String password;
    private int availablePower;
    private int reservedPower;
    private int neededPower;

    public UserBuilder setIp(InetAddress ip) {
        this.ip = ip;
        return this;
    }

    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setAvailablePower(int availablePower) {
        this.availablePower = availablePower;
        return this;
    }

    public UserBuilder setReservedPower(int reservedPower) {
        this.reservedPower = reservedPower;
        return this;
    }

    public UserBuilder setNeededPower(int neededPower) {
        this.neededPower = neededPower;
        return this;
    }

    public User createUser() {
        return new User(ip, username, password, availablePower, reservedPower, neededPower);
    }
}