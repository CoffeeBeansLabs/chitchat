import client.Client;

import java.io.IOException;

public class ClientExecutor {
    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 5000);
        client.start();
    }
}
