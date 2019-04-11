package gr.aueb.cn;

import gr.aueb.cn.packets.*;

import java.io.*;
import java.net.Socket;

public class EnergyUser implements Serializable {

    private static final long serialVersionUID = -2961923979657577088L;

    private String username;
    private int available_energy, in_need, reserved;

    private transient Socket connection;
    private transient ObjectOutputStream out;
    private transient ObjectInputStream in;
    private transient String ip, password;
    private transient int port;

    public EnergyUser(String username, String password, int available_energy) {
        this.username = username;
        this.password = password;
        this.available_energy = available_energy;
        try {
            menu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAvailable_energy() {
        return available_energy;
    }

    public void setAvailable_energy(int available_energy) {
        this.available_energy = available_energy;
    }

    public int getIn_need() {
        return in_need;
    }

    public void setIn_need(int in_need) {
        this.in_need = in_need;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    private void menu() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String option = "";
        System.out.println("Menu:");
        System.out.println("0. Sign in");
        System.out.println("1. Request energy");
        System.out.println("2. Request energy from specific user");

        while ((option = reader.readLine()) != null) {
            if (option.equalsIgnoreCase("0")) {

                System.out.println("Give IP of server");
                ip = reader.readLine();
                System.out.println("Give port of server");
                port = Integer.parseInt(reader.readLine());
                issueConnection(ip, port);

            } else if (option.equalsIgnoreCase("1")) {

                System.out.println("1. Request with second backup");
                System.out.println("2. Request with delay");
                String opt2 = reader.readLine();

                if (opt2.equalsIgnoreCase("1")) {
                    System.out.println("How much energy do you need?");
                    int en = Integer.parseInt(reader.readLine());
                    //Sends request
                    out.writeObject(new RequestEnergy(null, username, en, true));
                    out.flush();
                } else if (opt2.equalsIgnoreCase("2")) {
                    System.out.println("How much energy do you need?");
                    int en = Integer.parseInt(reader.readLine());
                    System.out.println("How much delay do you want before trying again?");
                    long time = Long.parseLong(reader.readLine());
                    out.writeObject(new RequestEnergy(null, username, en, time));
                    out.flush();
                }

            } else if (option.equalsIgnoreCase("2")) {

                System.out.println("Give the username of the user you want to receive energy");
                String req_username = reader.readLine();
                System.out.println("Give energy required");
                int en = Integer.parseInt(reader.readLine());
                out.writeObject(new RequestEnergy(req_username, username, en));
                out.flush();
            } else {
                System.out.println("Wrong option! Select again!");
            }
            System.out.println("Menu:");
            System.out.println("0. Sign in");
            System.out.println("1. Request energy");
            System.out.println("2. Request energy from specific user");
        }
    }

    private void issueConnection(String ip, int port) {
        try {
            connection = new Socket(ip, port);
            out = new ObjectOutputStream(this.connection.getOutputStream());
            in = new ObjectInputStream(this.connection.getInputStream());

            out.writeObject(new SignIn(username, password, available_energy));
            out.flush();

            Runnable runnable = () -> {
                while (true) {
                    try {
                        Object message = in.readObject();
                        if (message instanceof RequestEnergy) {
                            requestEnergy((RequestEnergy) message);
                        } else if (message instanceof SendEnergy) {
                            receiveEnergy((SendEnergy) message);
                        } else if (message instanceof Update) {
                            if (((Update) message).getUsername().equals(username)) {
                                sendUpdate();
                            }
                        }
                        else if (message instanceof RequestEnergyFailure){
                            System.out.println(((RequestEnergyFailure) message).getDestination() + " failed.");
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            connection.close();
        }*/
    }

    private void receiveEnergy(SendEnergy message) throws IOException {
        System.out.println("Received");
        if (message.getDestination().equals(username)) {
            available_energy += message.getEnergy();

            sendUpdate();
        }
        System.out.println(toString());
    }

    private void requestEnergy(RequestEnergy message) throws IOException {
        System.out.println("Sending Energy");
        if (available_energy < message.getEnergyNeeded() || in_need > 0) {
            return;
        }

        available_energy -= message.getEnergyNeeded();
        reserved += message.getEnergyNeeded();
        System.out.println(message.getDestination());
        out.writeObject(new SendEnergy(username, message.getDestination(), message.getEnergyNeeded()));
        out.flush();

        sendUpdate();
        System.out.println(toString());
    }

    private void sendUpdate() throws IOException {
        System.out.println("Sending Update");
        out.writeObject(new Update(username,this));
        out.flush();
    }

    @Override
    public String toString() {
        return "Available_energy=" + available_energy +
                ", in_need=" + in_need +
                ", reserved=" + reserved;
    }
}
