package jason.luo.aioDemo.server;

/**
 * NIO 2.0 异步非阻塞IO
 */
public class AioTimeServerMain {
    public static void main(String[] args){
        int port = 8001;
        AioTimeServerHandler timeServer = new AioTimeServerHandler(port);
        new Thread(timeServer).start();
    }
}
