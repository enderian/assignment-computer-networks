package gr.aueb.cn;

import gr.aueb.cn.model.User;
import gr.aueb.cn.model.UserBuilder;
import gr.aueb.cn.packets.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;

public class Distributor {

    private ServerSocket server;

    private Hashtable<String, User> energyUsers = new Hashtable<>();
    private Hashtable<String, ObjectOutputStream> outputs = new Hashtable<>();

    private Hashtable<RequestEnergy, Long> delayedRequests = new Hashtable<>();

    public Distributor(int port) {
        try {
            this.server = new ServerSocket(port);
            System.out.println("Server live");
            while (true) {
                try {
                    Socket socket = server.accept();
                    new Thread(() -> manageIncoming(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void manageIncoming(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String username = null;

            try {
                while (true) {
                    Object message = in.readObject();
                    if (message instanceof SignIn) {
                        signIn(out, (SignIn) message);
                        username = ((SignIn) message).getUsername();
                    } else if (message instanceof RequestEnergy) {
                        requestedEnergy((RequestEnergy) message);
                    } else if (message instanceof SendEnergy) {
                        sentEnergy((SendEnergy) message);
                    } else if (message instanceof Update) {
                        handleUpdate((Update) message);
                    } else {
                        //TODO ANY OTHER CASE
                    }

                    runDelayed();
                }
            } catch (Exception ignored) {

                //TODO Later.

            } finally {
                if (username != null) {
                    energyUsers.remove(username);
                    outputs.remove(username);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runDelayed() {
        new Hashtable<>(delayedRequests).entrySet().stream().filter(it -> it.getValue() < System.currentTimeMillis()).forEach(req -> {
            req.getKey().setDelay(null);
            try {
                requestedEnergy(req.getKey());
            } catch (IOException e) {
                e.printStackTrace();
            }
            delayedRequests.remove(req.getKey());
        });
    }

    private void signIn(ObjectOutputStream out, SignIn request) {
        if (!request.checkParameters()) return;

        User user = new UserBuilder()
                .setIp(request.getIp())
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .setAvailablePower(request.getKwhs())
                .createUser();

        energyUsers.put(request.getUsername(), user);
        outputs.put(request.getUsername(), out);
    }

    private void handleUpdate(Update message) {
        EnergyUser user = message.getEnergyUser();
        if (user != null) {
            Optional.ofNullable(energyUsers.get(message.getUsername())).ifPresent(energyUser -> {
                energyUser.setAvailablePower(user.getAvailable_energy());
                energyUser.setNeededPower(user.getIn_need());
                energyUser.setReservedPower(user.getReserved());
            });
        }
    }

    private void requestedEnergy(RequestEnergy message) throws IOException {
        if (message.getDestination() != null) {
            //Send to the destination.
            if (outputs.contains(message.getDestination())) {
                outputs.get(message.getDestination()).writeObject(message);
            } else {
                outputs.get(message.getUsername()).writeObject(new RequestEnergyFailure(message.getUsername()));
            }
            return;
        }

        User toSendTo = energyUsers.values().stream()
                .filter(it -> it.getAvailablePower() >= message.getEnergyNeeded() && it.getNeededPower() < 1)
                .findFirst().orElse(null);

        if (toSendTo != null) {
            try {
                outputs.get(toSendTo.getUsername()).writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (message.getDelay() != null) {
            //Store for later access.
            delayedRequests.put(message, System.currentTimeMillis() + message.getDelay());
        } else {
            outputs.get(message.getUsername()).writeObject(new RequestEnergyFailure(message.getUsername()));
        }
    }

    private void sentEnergy(SendEnergy message) throws IOException {
        if (outputs.contains(message.getDestination())) {
            outputs.get(message.getDestination()).writeObject(message);
        }
    }

}
