package gr.aueb.cn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EnergyUser {

    private Socket connection;
    private String ip;
    private int port, available_energy, in_need, reserved;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public EnergyUser(int available_energy){
        this.available_energy = available_energy;
    }

    public void issueConnection(String ip, int port){
        try {
            this.connection = new Socket(ip, port);
            out = new ObjectOutputStream(this.connection.getOutputStream());
            in = new ObjectInputStream(this.connection.getInputStream());
            SignIn signIn = new SignIn("test", "123", 100);
            out.writeObject(signIn);
            out.flush();
            //out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestRefresh(){

    }
}
