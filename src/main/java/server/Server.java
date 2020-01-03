package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Server {
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    public Server(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
    }

    public static ClientHandler getClient(String name) {
        return null;
    }

    public void start() throws IOException {
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int numberOfReadyChannels = selector.select();
            if (numberOfReadyChannels == 0) {
                continue;
            }

            Set<SelectionKey> readyChannelsKeys = selector.selectedKeys();
            for (SelectionKey readyChannelKey : readyChannelsKeys) {
                readyChannelsKeys.remove(readyChannelKey);
                if (readyChannelKey.isAcceptable()) {
                    ServerSocketChannel channelWithServer = (ServerSocketChannel) readyChannelKey.channel();
                    SocketChannel client = channelWithServer.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    continue;
                }
                if (readyChannelKey.isReadable()) {
                    SocketChannel client = (SocketChannel) readyChannelKey.channel();
                    int BUFFER_SIZE = 1024;
                    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                    int numOfBytes = -1;
                    numOfBytes = client.read(buffer);
                    if (numOfBytes == -1) {
                        Socket socket = client.socket();
                        SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
                        System.out.println("Connection closed by client " + remoteSocketAddress);
                        client.close();
                        readyChannelKey.cancel();
                        return;
                    }

                    System.out.println("Got message from client is: " + new String(buffer.array()));

                }

                if (readyChannelKey.isWritable()) {

                }
            }

        }
    }
}
