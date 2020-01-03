package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client2 {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(5000);
        SocketChannel client = SocketChannel.open(inetSocketAddress);

        String message = "Hi";
        ByteBuffer buffer = ByteBuffer.allocate(74);
        buffer.put(message.getBytes());
        buffer.flip();

        client.write(buffer);

    }
}
