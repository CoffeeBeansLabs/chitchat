package client;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;

    public Client(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
    }

    public void start() {
        BufferedReader userInput = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        try {
            userInput = new BufferedReader(new InputStreamReader(System.in));
            inputStream = new DataInputStream(this.socket.getInputStream());
            outputStream = new DataOutputStream(this.socket.getOutputStream());

            Thread sendMessageThread = getSendMessageThread(userInput, outputStream);
            Thread readMessageThread = getReadMessageThread(inputStream);


            sendMessageThread.start();
            readMessageThread.start();


        } catch (IOException ioException) {
            System.out.println("The exception from socket output stream is: " + ioException.getMessage());
        }

    }

    private Thread getReadMessageThread(DataInputStream inputStream) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String messageFromServer = inputStream.readUTF();
                        System.out.println("Message: " + messageFromServer);
                    } catch (IOException exception) {
                        System.out.println("Exception while reading message from server is: " + exception.getMessage());
                        break;
                    }
                }
                System.out.println("Reading message thread is stopped....");
            }
        });
    }

    private Thread getSendMessageThread(BufferedReader userInput, DataOutputStream outputStream) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = userInput.readLine();
                        outputStream.writeUTF(message);
                    } catch (IOException exception) {
                        System.out.println("Exception while reading and writing from terminal is: " + exception.getMessage());
                        break;
                    }
                }
                System.out.println("Sending message thread is stopped....");
            }
        });
    }
}
