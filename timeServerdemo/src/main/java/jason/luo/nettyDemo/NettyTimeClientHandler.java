package jason.luo.nettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

class NettyTimeClientHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf msg;

    NettyTimeClientHandler() {
        byte[] request = "time".getBytes();
        this.msg = Unpooled.buffer(request.length);
        msg.writeBytes(request);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String response = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("From server: " + response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
