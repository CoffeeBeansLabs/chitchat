package client;

import java.io.IOException;
import java.net.UnknownHostException;

public class Client2 {
    public static void main(String[] args) throws IOException {
        try{
            Client client = new Client("127.0.0.1", 5000);
            System.out.println("Connected");
            client.start();
        }catch (UnknownHostException unknownHostException){
            System.out.println("Given host is not valid and error is "+ unknownHostException.getMessage());
        }

    }
}
