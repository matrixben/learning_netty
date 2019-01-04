package jason.luo.aioDemo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AioTimeClientHandler implements Runnable, CompletionHandler<Void, AioTimeClientHandler> {
    private String host;
    private int port;
    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    AioTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            clientChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        //连接服务端，成功后回调自身，
        // 第一个回调用于传递数据，第二个回调用于实现逻辑
        clientChannel.connect(new InetSocketAddress(host, port),
                this,this);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                clientChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void completed(Void result, AioTimeClientHandler attachment) {
        byte[] request = "time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(request.length);
        //客户端发送请求
        writeBuffer.put(request);
        writeBuffer.flip();
        clientChannel.write(writeBuffer, writeBuffer,
                new WriteCompletionHandler(clientChannel, latch));
    }

    @Override
    public void failed(Throwable exc, AioTimeClientHandler attachment) {
        exc.printStackTrace();
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            latch.countDown();
        }
    }
}
