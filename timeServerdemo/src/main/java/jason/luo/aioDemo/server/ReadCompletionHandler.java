package jason.luo.aioDemo.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 读取客户端数据并返回当前时间
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel clientChannel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    ReadCompletionHandler(AsynchronousSocketChannel channel) {
        this.clientChannel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        //客户端输入的数据
        String request = new String(bytes, StandardCharsets.UTF_8);
        if ("time".equals(request)){
            String currentTime = dateFormat.format(new Date());
            doWrite(currentTime);
        }
    }

    /**返回当前时间给客户端 */
    private void doWrite(String currentTime) {
        byte[] bytes = currentTime.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        clientChannel.write(writeBuffer, writeBuffer,
                new WriteCompletionHandler(clientChannel));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
