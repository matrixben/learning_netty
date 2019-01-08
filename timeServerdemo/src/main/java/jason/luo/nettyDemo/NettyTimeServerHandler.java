package jason.luo.nettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 实现读写事件处理逻辑
 */
class NettyTimeServerHandler extends ChannelInboundHandlerAdapter {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String request = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("From client: " + request);

        if ("time".equals(request)){
            String currentTime = dateFormat.format(new Date());
            String response = "Current date time is " + currentTime;
            ByteBuf res = Unpooled.copiedBuffer(response.getBytes());
            ctx.write(res);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        //当有异常时关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
