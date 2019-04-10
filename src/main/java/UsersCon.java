import gr.aueb.cn.EnergyUser;

import java.io.IOException;

public class UsersCon {
    public static void main(String[] args) throws IOException {
        EnergyUser u1 = new EnergyUser("kappa", "pass", 100);
        u1.menu();
    }
}
