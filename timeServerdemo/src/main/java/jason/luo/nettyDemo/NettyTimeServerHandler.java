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
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ChildChannelHandler中的StringDecoder已经将msg转为字符串对象
        String request = (String) msg;
        //byte[] bytes = new byte[buf.readableBytes()];
        //buf.readBytes(bytes);
        //String request = new String(bytes, StandardCharsets.UTF_8);
        count++;  //每收到一条客户端信息就加一
        System.out.println("The "+count+" message from client: " + request);

        if ("time".equals(request)){
            String currentTime = dateFormat.format(new Date());
            String response = "The "+count+" message, current date time is " + currentTime
                    + System.getProperty("line.separator");  //每条数据结尾加上换行符
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
