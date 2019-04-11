package gr.aueb.cn;

import java.io.IOException;

public class Controller {

    public static void main(String[] args) throws IOException {
        if(args[0].equalsIgnoreCase("S")){
            Distributor server = new Distributor(Integer.parseInt(args[1]));
        }
        else{
            EnergyUser user = new EnergyUser(args[1], args[2], Integer.parseInt(args[3]));
        }
    }
}
