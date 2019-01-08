package jason.luo.nettyDemo;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 处理网络IO事件
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    private ChannelInboundHandlerAdapter handlerAdapter;

    ChildChannelHandler(ChannelInboundHandlerAdapter handlerAdapter){
        this.handlerAdapter = handlerAdapter;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast(this.handlerAdapter);
    }
}
