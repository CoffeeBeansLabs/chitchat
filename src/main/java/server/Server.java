package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server(int port) {
        Socket socket = null;
        DataInputStream in = null;

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("server.Server started");
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("client.Client accepted");
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            String line = "";
            while (!line.equals("Over")) {
                try {
                    line = in.readUTF();
                    System.out.println(line);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            System.out.println("Closing connection");
            socket.close();
            in.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
