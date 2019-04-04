package gr.aueb.cn;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserHelperBuilder {

    private Distributor distributor;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username, password;
    private InetAddress ip;

    public UserHelperBuilder(){

    }

    public UserHelper buildUser(){
        return new UserHelper(distributor,socket, out, in, ip, username, password);
    }

    public UserHelperBuilder distributor(Distributor distributor){
        this.distributor = distributor;
        return this;
    }

    public UserHelperBuilder socket(Socket socket){
        this.socket = socket;
        return this;
    }

    public UserHelperBuilder out(ObjectOutputStream out){
        this.out = out;
        return this;
    }

    public UserHelperBuilder in(ObjectInputStream in){
        this.in = in;
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
