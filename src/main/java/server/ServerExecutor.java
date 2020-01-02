package server;

import java.io.IOException;

public class ServerExecutor {
    public static void main(String[] args) throws IOException {
        Server server = new Server(5000);
        server.start();
    }
}
