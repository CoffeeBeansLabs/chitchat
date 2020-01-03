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
    private final ServerSocket server;

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
    }

    public static ClientHandler getClient(String name) {
        List<ClientHandler> filteredClientHandlers = clientHandlers.stream().filter(clientHandler -> clientHandler.isSameClient(name)).collect(Collectors.toList());
        if(!filteredClientHandlers.isEmpty()){
            return filteredClientHandlers.get(0);
        }
        return null;
    }

    public void start() throws IOException {
        Socket socket;

        while (true) {
            socket = server.accept();
            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            outputStream.writeUTF("Please type client name: ");
            String clientName = inputStream.readUTF();
            ClientHandler clientHandler = getClient(clientName);
            if(clientHandler == null){

                System.out.println("Assigning new thread for: client "+ clientName);
                clientHandler = new ClientHandler(socket, "client " + clientName, inputStream, outputStream);
                clientHandlers.add(clientHandler);
            }else{
                System.out.println("Logged in: client "+ clientHandler.getName());
                socket = server.accept();
                clientHandler.setSocket(socket);
            }

            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    }
}
