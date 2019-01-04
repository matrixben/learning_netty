package jason.luo.aioDemo.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;
    ReadCompletionHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        clientChannel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        String response = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("From server: " + response);
        latch.countDown();
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            latch.countDown();
        }
    }
}
