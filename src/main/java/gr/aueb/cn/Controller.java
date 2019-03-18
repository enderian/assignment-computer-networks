package gr.aueb.cn;

public class Controller {
    public static void main(String[] args) {
        Distributor server = new Distributor(4200);

        EnergyUser user1 = new EnergyUser(100);
        EnergyUser user2 = new EnergyUser(100);

        user1.issueConnection("127.0.0.1", 4200);
        //user2.issueConnection("localhost", 4200);
    }
}
