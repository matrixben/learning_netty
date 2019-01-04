package jason.luo.aioDemo.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 处理与客户端的连接
 */
public class AcceptCompletionHandler
        implements CompletionHandler<AsynchronousSocketChannel, AioTimeServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AioTimeServerHandler attachment) {
        attachment.getServerChannel().accept(attachment,this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AioTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.getLatch().countDown();
    }
}
