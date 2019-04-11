package gr.aueb.cn.packets;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SignIn implements Serializable {

    private static final long serialVersionUID = 4L;

    private String username, password;
    private InetAddress ip;
    private Integer kwhs;

    public SignIn(String username, String password, Integer kwhs){
        this.username = username;
        this.password = password;
        this.kwhs = kwhs;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress();
        }
        catch (UnknownHostException | SocketException e){
            e.printStackTrace();
        }
    }


    public boolean checkParameters(){
        if(ip == null || username == null || password == null ||
                kwhs == null){
            return false;
        }
        return true;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public InetAddress getIp() {
        return ip;
    }

    public Integer getKwhs() {
        return kwhs;
    }
}
