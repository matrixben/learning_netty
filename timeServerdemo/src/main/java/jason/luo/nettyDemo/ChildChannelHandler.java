package jason.luo.nettyDemo;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

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
        //增加两个解码器，用于解决TCP粘包问题，根据ByteBuf数据中的换行符分割数据，然后转换成字符串对象
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(this.handlerAdapter);
    }
}
