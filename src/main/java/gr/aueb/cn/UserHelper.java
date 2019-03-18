package gr.aueb.cn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserHelper extends Thread{

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username, password;
    private InetAddress ip;

    public UserHelper(Socket socket, InetAddress ip, String username, String password){
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Success");
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

    public InetAddress getIp() {
        return ip;
    }

}
