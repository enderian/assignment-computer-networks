package gr.aueb.cn;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class EnergyUser {

    private Socket connection;
    private String ip;
    private int port, available_energy, in_need;


    public EnergyUser(int available_energy){
        this.available_energy = available_energy;
    }

    public void issueConnection(String ip, int port){
        try {
            this.connection = new Socket(ip, port);
            ObjectOutputStream out = new ObjectOutputStream(this.connection.getOutputStream());
            out.writeObject("signin");
            out.flush();
            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
            }
            out.writeObject(ip);
            out.writeObject("test");
            out.writeObject("123");
            out.writeObject(100);
            out.flush();
            //out.close();
            System.out.println("Success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
