package client;

import java.io.IOException;

public class ClientExecutor1 {
    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 5000);
        client.start();
    }
}
