package gr.aueb.cn;

import java.net.Socket;

public class UserHelperBuilder {

    private Socket socket;
    private String username, password, ip;
    private Integer kwhs;

    public UserHelperBuilder(){

    }

    public UserHelper buildUser(){
        return new UserHelper(socket, ip, username, password, kwhs);
    }

    public UserHelperBuilder socket(Socket socket){
        this.socket = socket;
        return this;
    }

    public UserHelperBuilder ip(String ip){
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

    public UserHelperBuilder kwhs(Integer kwhs){
        this.kwhs = kwhs;
        return this;
    }
}
