package gr.aueb.cn;

import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Distributor {

    private ServerSocket server;
    private Socket socket;
    private ArrayList<UserHelper> energyUsers = new ArrayList<>();

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
            String message = (String) in.readObject();
            if (message.equalsIgnoreCase("signin")){
                signIn(socket, in);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void signIn(Socket socket, ObjectInputStream in) {
        try {
            String client_ip = (String) in.readObject();
            String username = (String) in.readObject();
            String password = (String) in.readObject();
            Integer kwhs = (Integer) in.readObject();
            //in.close();
            if(client_ip == null || username == null || password == null ||
                    kwhs == null){
                //socket.close();
                return;
            }

            UserHelper userHelper = new UserHelperBuilder()
                    .socket(socket)
                    .ip(client_ip)
                    .username(username)
                    .password(password)
                    .kwhs(kwhs)
                    .buildUser();

            userHelper.start();
            energyUsers.add(userHelper);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
