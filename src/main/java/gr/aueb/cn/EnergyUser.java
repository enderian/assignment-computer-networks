package gr.aueb.cn;

import org.omg.PortableServer.THREAD_POLICY_ID;

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
            Runnable runnable = () -> {
                while(true){
                    try {
                        Object message = in.readObject();
                        if (message instanceof UpdateRequest){
                            requestRefresh((UpdateRequest) message);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
            //out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dump(){
        try{
            UpdateRequest request = new UpdateRequest();
            out.writeObject(request);
            out.flush();
            System.out.println("Flushed");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void requestRefresh(UpdateRequest request){
        request.setNewUnits(available_energy);
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAvailable_energy(int available_energy) {
        this.available_energy = available_energy;
    }
}
