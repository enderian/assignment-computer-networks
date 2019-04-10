package gr.aueb.cn;

import java.io.IOException;

public class Controller {

    public static void main(String[] args) throws IOException {
        Distributor server = new Distributor(4200);
    }

}
