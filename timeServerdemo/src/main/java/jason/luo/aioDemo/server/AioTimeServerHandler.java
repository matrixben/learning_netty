package jason.luo.aioDemo.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AioTimeServerHandler implements Runnable{
    private AsynchronousServerSocketChannel serverChannel;
    private CountDownLatch latch;

    AioTimeServerHandler(int port) {
        try {
            serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server connected on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        AcceptCompletionHandler acceptHandler = new AcceptCompletionHandler();
        serverChannel.accept(this, acceptHandler);
    }

    AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    CountDownLatch getLatch(){
        return latch;
    }
}
