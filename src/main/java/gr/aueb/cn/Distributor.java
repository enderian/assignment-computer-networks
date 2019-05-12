package gr.aueb.cn;

import gr.aueb.cn.model.User;
import gr.aueb.cn.model.UserBuilder;
import gr.aueb.cn.packets.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Distributor {

    private ServerSocket server;

    private Hashtable<String, User> energyUsers = new Hashtable<>();
    private final Hashtable<String, ObjectOutputStream> outputs = new Hashtable<>();
    private Timer timer = new Timer(true);

    public Distributor(int port) {
        try {

            this.server = new ServerSocket(port);
            while (true) {
                try {
                    Socket socket = server.accept();
                    new Thread(() -> manageIncoming(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
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
                        continue;
                    }
                }
            } catch (Exception ignored) {

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

        System.out.println(user.getUsername() + " connected!");
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

        // Debug output.
        System.out.println("Status:");
        new Hashtable<>(energyUsers).forEach((key, value) -> System.out.println(value.toString()));
        System.out.println();
    }

    private synchronized void requestedEnergy(RequestEnergy message) throws IOException {
        System.out.println("Incoming request...");

        // Sending requests directly to the requested energy user.
        if (message.getUsername() != null) {
            if (outputs.containsKey(message.getUsername())) {
                // Send to the destination.
                System.out.println("Routing to " + message.getUsername());

                outputs.get(message.getUsername()).writeObject(message);
                outputs.get(message.getUsername()).flush();
            } else {
                // No username - failed.
                outputs.get(message.getDestination()).writeObject(new RequestEnergyFailure(message.getUsername()));
                outputs.get(message.getDestination()).flush();
            }
            return;
        }

        // Can pick multiple users.
        List<User> toSendTo = energyUsers.values().stream()
                .filter(it -> (it.getNeededPower() == 0) && !it.getUsername().equals(message.getDestination()))
                .collect(Collectors.toList());

        // The total available power for all the users.
        int availablePower = toSendTo.stream().map(User::getAvailablePower).reduce(Integer::sum).orElse(0);

        if (!toSendTo.isEmpty() && availablePower >= message.getEnergyNeeded()) {

            // Figure out how many are needed and request power.
            ArrayList<User> select = new ArrayList<>(energyUsers.values());

            // Sortable by probability.
            Collections.sort(select);

            int needed = message.getEnergyNeeded();
            for (User user : select) {
                int toUse = Math.min(user.getAvailablePower(), needed);

                System.out.println("Requesting " + toUse + " from " + user.getUsername() + " to " + message.getDestination());

                RequestEnergy requestEnergy = new RequestEnergy(message.getUsername(), message.getDestination(), toUse);
                outputs.get(user.getUsername()).writeObject(requestEnergy);
                outputs.get(user.getUsername()).flush();
                user.setLastSelected(System.currentTimeMillis());

                if ((needed -= toUse) <= 0) {
                    break;
                }
            }

        } else if (message.getDelay() != null) {
            System.out.println("Delaying " + message.getDestination() + "'s request.");
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    message.setDelay(null);
                    try {
                        requestedEnergy(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, message.getDelay());
        } else {
            outputs.get(message.getDestination()).writeObject(new RequestEnergyFailure(message.getUsername()));
            outputs.get(message.getDestination()).flush();
        }
    }

    private void sentEnergy(SendEnergy message) throws IOException {
        synchronized (outputs) {
            if (outputs.containsKey(message.getDestination())) {
                outputs.get(message.getDestination()).writeObject(message);
                outputs.get(message.getDestination()).flush();
            }
        }
    }

}
