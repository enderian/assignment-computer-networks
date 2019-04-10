package gr.aueb.cn;

import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class EnergyUser {

    private Socket connection;
    private String ip, username, password;
    private int port, available_energy, in_need, reserved;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public EnergyUser(String username, String password, int available_energy){
        this.username = username;
        this.password = password;
        this.available_energy = available_energy;
    }

    public void menu() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String option = "";
        System.out.println("Menu:");
        System.out.println("0. Sign in");
        System.out.println("1. Request energy");
        System.out.println("2. Request energy from specific user");
        while((option = reader.readLine()) != null){
            if(option.equalsIgnoreCase("0")){
                System.out.println("Give IP of server");
                ip = reader.readLine();
                System.out.println("Give port of server");
                port = Integer.parseInt(reader.readLine());
                issueConnection(ip, port);
            }
            else if(option.equalsIgnoreCase("1")){
                System.out.println("1. Request with second backup");
                System.out.println("2. Request with delay");
                String opt2 = reader.readLine();
                if(opt2.equalsIgnoreCase("1")){
                    System.out.println("How much energy do you need?");
                    int en = Integer.parseInt(reader.readLine());
                    EnergyRequest er = new EnergyRequest(en);
                    sendEnergyRequest(er);
                }
                else if(opt2.equalsIgnoreCase("2")){
                    System.out.println("How much energy do you need?");
                    int en = Integer.parseInt(reader.readLine());
                    System.out.println("How much delay do you want before trying again?");
                    long time = Long.parseLong(reader.readLine());
                    EnergyRequest er = new EnergyRequestWait(en, time);
                    sendEnergyRequest(er);
                }
            }
            else if(option.equalsIgnoreCase("3")){
                try {
                    ArrayList<EnergyUser> currentUsers = (ArrayList<EnergyUser>)in.readObject();
                    for(EnergyUser eu : currentUsers){
                        System.out.println("User with ip: " + eu.ip);
                    }
                    System.out.println("Give the IP of the user you want to receive energy");
                    String req_ip = reader.readLine();
                    EnergyUser found = null;
                    for(EnergyUser eu : currentUsers){
                        if(eu.ip.equalsIgnoreCase(req_ip)){
                            found = eu;
                        }
                    }
                    if(found != null){
                        System.out.println("Give energy required");
                        int en = Integer.parseInt(reader.readLine());
                        EnergyRequestToEU req = new EnergyRequestToEU(en, found);
                        sendEnergyRequest(req);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Wrong option! Select again!");
                System.out.println("Menu:");
                System.out.println("0. Sign in");
                System.out.println("1. Request energy");
                System.out.println("2. Request energy from specific user");
            }
        }
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

    public void sendEnergyRequest(EnergyRequest er){
        try {
            out.writeObject(er);
            out.flush();
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
