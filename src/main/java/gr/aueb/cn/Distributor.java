package gr.aueb.cn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class Distributor {

    private ServerSocket server;
    private Socket socket;
    private ArrayList<UserHelper> energyUsers = new ArrayList<>();
    private Hashtable<UserHelper, Integer> availability = new Hashtable<>();
    private Hashtable<UserHelper, Integer> reservedEnergy = new Hashtable<>();

    public Distributor(int port){
        try {
            this.server = new ServerSocket(port);
            System.out.println("Server live");
            acceptConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnections(){
        Runnable runnable = () -> {
            while(true){
                try {
                    this.socket = server.accept();
                    manageIncoming(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void manageIncoming(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            Object message = in.readObject();
            if (message instanceof SignIn){
                signIn(socket, out, in, (SignIn) message);
            }
            else if(message instanceof UpdateRequest){
                update((UpdateRequest) message);
            }
            else{
                //TODO ANY OTHER CASE
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void signIn(Socket socket, ObjectOutputStream out, ObjectInputStream in, SignIn request) {
        if(!request.checkParameters()) return;

        UserHelper userHelper = new UserHelperBuilder()
                .distributor(this)
                .socket(socket)
                .out(out)
                .in(in)
                .ip(request.getIp())
                .username(request.getUsername())
                .password(request.getPassword())
                .buildUser();

        availability.put(userHelper, request.getKwhs());

        userHelper.start();
        energyUsers.add(userHelper);
    }

    public void update(UpdateRequest request) {
        System.out.println("Hello");
        for(UserHelper u : energyUsers){
            System.out.println(availability.get(u));
        }
        for(UserHelper u : energyUsers){
            request = u.update(request);
            availability.put(u, request.getNewUnits());
            reservedEnergy.put(u, request.getReservedUnits());
        }
    }
}
