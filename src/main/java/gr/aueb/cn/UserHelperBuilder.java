package gr.aueb.cn;

import java.net.InetAddress;
import java.net.Socket;

public class UserHelperBuilder {

    private Socket socket;
    private String username, password;
    private InetAddress ip;

    public UserHelperBuilder(){

    }

    public UserHelper buildUser(){
        return new UserHelper(socket, ip, username, password);
    }

    public UserHelperBuilder socket(Socket socket){
        this.socket = socket;
        return this;
    }

    public UserHelperBuilder ip(InetAddress ip){
        this.ip = ip;
        return this;
    }

    public UserHelperBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserHelperBuilder password(String password){
        this.password = password;
        return this;
    }

}
