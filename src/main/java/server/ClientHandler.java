package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private boolean isLoggedIn;
    private String name;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public ClientHandler(Socket socket, String name, DataInputStream inputStream, DataOutputStream outputStream) {
        this.socket = socket;
        this.name = name;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.isLoggedIn = true;
    }

    @Override
    public void run() {
        try {
            outputStream.writeUTF("From server - Client is: " + this.name);
            outputStream.writeUTF("Type logout to terminate the connection");
        } catch (IOException e) {
            System.out.println("Exception while writing utf stream: " + e.getMessage());
        }
        while (true) {
            try {
                String received = inputStream.readUTF();

                if (received.equals("logout")) {
                    System.out.println("Closing connection with client using socket: " + socket);
                    this.isLoggedIn = false;
                    this.socket.close();
                    System.out.println("Connection closed");
                    break;
                }

                List<String> messageWithRecipient = Arrays.asList(received.split("#"));
                if (!messageWithRecipient.isEmpty()) {
                    String message = messageWithRecipient.get(0).trim();
                    String recipient = messageWithRecipient.get(1).trim();
                    ClientHandler recipientClient = Server.getRecipientClient(recipient);
                    if (recipientClient != null) {
                        this.writeMessage("Sent to: "+ recipientClient.name);
                        recipientClient.writeMessage("Received from "+ this.name + " : " + message);
                    }else{
                        this.writeMessage("Given recipient does not exist: "+ recipient);
                    }
                }
            } catch (IOException e) {
                System.out.println("Exception unknown: " + e.getMessage());
            }
        }

        try {
            System.out.println("Closing input and output stream on client: " + this.name);
            inputStream.close();
            outputStream.close();
        } catch (IOException exception) {
            System.out.println("Exception while closing input or output stream");
        }
    }

    private void writeMessage(String message) throws IOException {
        this.outputStream.writeUTF(message);
    }

    public boolean isSameClient(String recipientName) {
        return this.name.equals(recipientName) && this.isLoggedIn;
    }
}
