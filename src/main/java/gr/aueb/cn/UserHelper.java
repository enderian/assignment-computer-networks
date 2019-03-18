package gr.aueb.cn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserHelper extends Thread{

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username, password, ip;
    private Integer kwhs;

    public UserHelper(Socket socket, String ip, String username, String password, Integer kwhs){
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.kwhs = kwhs;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true){
            try {
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public Integer getKwhs() {
        return kwhs;
    }
}
