package jason.luo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NioTimeServer implements Runnable{
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NioTimeServer(int port) {
        try {
            selector = Selector.open();
            //所有客户端连接的父管道channel
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);

            InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), port);
            serverChannel.socket().bind(isa);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server connected on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!stop){
            try {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> i = keys.iterator();
                while (i.hasNext()){
                    SelectionKey key = i.next();
                    i.remove();

                    if (key.isValid()){
                        if (key.isAcceptable()){
                            doAccept(key);
                        }else if (key.isReadable()){
                            doRead(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //关闭资源
        if (selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doAccept(SelectionKey key) throws IOException {
        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();
        //接收客户端的连接请求，完成TCP三次握手
        SocketChannel clientChannel = socketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void doRead(SelectionKey key) throws IOException {
        SocketChannel readChannel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(8192);
        int readBytes = readChannel.read(readBuffer);
        if (readBytes > 0){
            readBuffer.flip();
            byte[] bytes = new byte[readBuffer.remaining()];
            readBuffer.get(bytes);
            String request = new String(bytes, StandardCharsets.UTF_8);
            String response = "Current date time is " + dateFormat.format(new Date());
            if ("time".equals(request)){
                doWrite(readChannel, response);
            }
        }else if (readBytes < 0){
            key.cancel();
            readChannel.close();
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {
        if (response != null && !response.isEmpty()){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }

    public void stop(){
        this.stop = true;
    }
}
