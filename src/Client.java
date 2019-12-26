import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;

    public Client(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
    }

    public void start() {
        BufferedReader userInput = null;
        DataOutputStream output = null;
        try{
            userInput = new BufferedReader(new InputStreamReader(System.in));
            output = new DataOutputStream(this.socket.getOutputStream());
            String line = "";
            while (!line.equals("Over")){
                line = userInput.readLine();
                output.writeUTF(line);
            }
        }catch (IOException ioException){
            System.out.println("The exception from socket output stream is: " + ioException.getMessage());
        }

        try{
            userInput.close();
            output.close();
            this.socket.close();
        }catch (IOException ioException){
            System.out.println("exception during closing buffer reader or output stream is: " + ioException.getMessage());
        }
    }
}
