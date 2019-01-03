package jason.luo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 同步阻塞IO的服务端
 */
public class BioTimeServer {
    //使用阻塞队列创建线程
    private static ExecutorService executor = Executors.newCachedThreadPool();

    public static void main( String[] args ) throws IOException {
        int port = 8001;

        Socket clientSocket;
        try (ServerSocket timeServer = new ServerSocket(port)) {
            System.out.println("The time server connected on port: " + port);
            while (true) {
                clientSocket = timeServer.accept();
                executor.execute(new BioTimeServerHandler(clientSocket));
            }
        }
    }
}
