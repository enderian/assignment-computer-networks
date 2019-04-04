package gr.aueb.cn;

public class Controller {
    public static void main(String[] args) {
        Distributor server = new Distributor(4200);

        EnergyUser user1 = new EnergyUser(100);
        EnergyUser user2 = new EnergyUser(50);

        user1.issueConnection("localhost", 4200);
        user1.dump();
        user1.setAvailable_energy(20);
        user2.issueConnection("localhost", 4200);

    }
}
