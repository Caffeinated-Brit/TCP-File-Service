package file_service;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
public class FileServer {
    public static void main(String[] args) throws Exception {
        int port = 3000;
        ServerSocketChannel welcomeChannel =
                ServerSocketChannel.open();
        welcomeChannel.socket().bind(new InetSocketAddress(port));

        while (true) {

            SocketChannel serveChannel = welcomeChannel.accept();
            ByteBuffer request = ByteBuffer.allocate(2500);

            int numBytes = 0;

            do {
                numBytes = serveChannel.read(request);
            } while (numBytes >= 0);

            request.flip();

            char command = (char) request.get();

            System.out.println(command);

            switch (command) {
                case 'd':
                    System.out.println("1");
                    byte[] a = new byte[request.remaining()];
                    request.get(a);
                    String fileName = new String(a);
                    System.out.println(fileName);
                    File file = new File("C:\\Users\\almon\\Documents\\Classes\\CS 316\\TCP File Service\\src\\file_service\\ServerFiles\\"+fileName);
                    boolean success = false;

                    if (file.exists()) {
                        success = file.delete();
                    }
                    if (success) {
                        ByteBuffer code =
                                ByteBuffer.wrap("S".getBytes());
                        serveChannel.write(code);
                        System.out.println('s');
                    } else {
                        ByteBuffer code =
                                ByteBuffer.wrap("F".getBytes());
                        serveChannel.write(code);
                        System.out.println('f');
                    }
                    serveChannel.close();
                    break;



            }
        }
    }

}
