package gr.aueb.cn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class UserHelper extends Thread{

    private Distributor distributor;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username, password;
    private InetAddress ip;


    public UserHelper(Distributor distributor, Socket socket, ObjectOutputStream out, ObjectInputStream in, InetAddress ip, String username, String password){
        this.distributor = distributor;
        this.ip = ip;
        this.out = out;
        this.in = in;
        this.username = username;
        this.password = password;
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Success");
        while (true){
            try {
//                this.out = new ObjectOutputStream(socket.getOutputStream());
//                this.in = new ObjectInputStream(socket.getInputStream());
                Object message = in.readObject();
                if(message instanceof UpdateRequest){
                    distributor.update((UpdateRequest) message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public UpdateRequest update(UpdateRequest updateRequest){
        try {
            out.writeObject(updateRequest);
            out.flush();
            updateRequest = (UpdateRequest) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return updateRequest;
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
