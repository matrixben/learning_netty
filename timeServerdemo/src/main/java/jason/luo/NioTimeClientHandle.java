package jason.luo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioTimeClientHandle implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel clientChannel;
    private volatile boolean stop;

    NioTimeClientHandle(String localhost, int port) {
        host = localhost;
        this.port = port;
        try {
            selector = Selector.open();
            clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!stop){
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> i = keys.iterator();

                while (i.hasNext()){
                    SelectionKey key = i.next();
                    i.remove();
                    try {
                        if (key.isValid()){
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            if (key.isConnectable()){
                                if (clientChannel.finishConnect()){
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                    doWrite(clientChannel);
                                }else {
                                    System.out.println("客户端连接失败。");
                                    System.exit(1);
                                }
                            }
                            if (key.isReadable()){
                                doRead(key, clientChannel);
                            }
                        }
                    }catch (IOException e){
                        key.cancel();
                        if (key.channel() != null) key.channel().close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doRead(SelectionKey key, SocketChannel clientChannel) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(8192);
        int readBytes = clientChannel.read(readBuffer);
        if (readBytes > 0){
            readBuffer.flip();
            byte[] bytes = new byte[readBuffer.remaining()];
            readBuffer.get(bytes);
            String response = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("From server: " + response);
            this.stop = true;
        }else if (readBytes < 0){
            key.cancel();
            clientChannel.close();
        }
    }

    /** 直接发起连接 */
    private void doConnect() throws IOException {
        if (clientChannel.connect(new InetSocketAddress(host, port))){
            clientChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("直接连接成功.");
            doWrite(clientChannel);
        }else {
            clientChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    /** 向服务端发送消息 */
    private void doWrite(SocketChannel clientChannel) throws IOException {
        byte[] request = "time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(request.length);
        writeBuffer.put(request);
        writeBuffer.flip();
        clientChannel.write(writeBuffer);
    }
}
