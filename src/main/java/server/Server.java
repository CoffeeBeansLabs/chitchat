package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Server {
    private static List<ClientHandler> clientHandlers = new ArrayList<>();

    public Server(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        int i = 1;
        Socket socket;

        while (true) {

            socket = server.accept();
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Assigning new thread for: client "+ i);
            ClientHandler clientHandler = new ClientHandler(socket, "client " + i, inputStream, outputStream);
            Thread thread = new Thread(clientHandler);
            clientHandlers.add(clientHandler);
            thread.start();
            i++;
        }
    }

    public static ClientHandler getRecipientClient(String recipientName) {
        List<ClientHandler> filteredClientHandlers = clientHandlers.stream().filter(clientHandler -> clientHandler.isSameClient(recipientName)).collect(Collectors.toList());
        if(!filteredClientHandlers.isEmpty()){
            return filteredClientHandlers.get(0);
        }
        return null;
    }
}
