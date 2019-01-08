package jason.luo.nettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

class NettyTimeClientHandler extends ChannelInboundHandlerAdapter {
    private byte[] request;

    NettyTimeClientHandler() {
        //每条数据结尾加上换行符
        request = ("time" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        for (int i = 0; i < 50; i++) {
            ByteBuf buf = Unpooled.buffer(request.length);
            buf.writeBytes(request);
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        String response = (String) msg;
        //byte[] bytes = new byte[buf.readableBytes()];
        //buf.readBytes(bytes);
        //String response = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("From server: " + response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
