package jason.luo.aioDemo.client;

public class AioTimeClientMain {
    public static void main(String[] args){
        int port = 8001;
        new Thread(new AioTimeClientHandler("localhost", port)).start();
    }
}
