package file_service;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

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
                //System.out.println(numBytes);
            } while (numBytes >= 0);

            request.flip();


            char command = (char) request.get();

            System.out.println(command);

            //

            byte[] a = new byte[0];
            byte[] b = new byte[0];
            String fileName = new String(a);
            File file;
            boolean success = false;

            //


            switch (command) {

                // DELETE FILE
                case 'd':
                    System.out.println("1");
                    a = new byte[request.remaining()];
                    request.get(a);
                    fileName = new String(a);
                    System.out.println(fileName);
                    file = new File("src\\file_service\\files\\" + fileName);

                    if (file.exists()) {
                        success = file.delete();
                    }
                    if (success) {
                        ByteBuffer code = ByteBuffer.wrap("S".getBytes());
                        serveChannel.write(code);
                        System.out.println('s');
                    } else {
                        ByteBuffer code = ByteBuffer.wrap("F".getBytes());
                        serveChannel.write(code);
                        System.out.println('f');
                    }
                    serveChannel.close();
                    break;


                // CREATE FILE
                case 'u':
                    System.out.println("2");
                    a = new byte[request.remaining()];
                    request.get(a);
                    fileName = new String(a);
                    System.out.println(fileName);
                    file = new File("src\\file_service\\files\\" + fileName);

                    if (!file.exists()) {
                        success = file.createNewFile();
                    }
                    if (success) {
                        ByteBuffer code = ByteBuffer.wrap("S".getBytes());
                        serveChannel.write(code);
                        System.out.println('s');
                    } else {
                        ByteBuffer code = ByteBuffer.wrap("F".getBytes());
                        serveChannel.write(code);
                        System.out.println('f');
                    }
                    serveChannel.close();
                    break;


                // RENAME
                case 'r':
                    System.out.println("3");
                    a = new byte[request.remaining()];
                    request.get(a);

                    String requestString = new String(a);
                    int index = requestString.indexOf("\\");
                    String oldFileName = null;
                    String newFileName = null;
                    if (index != -1) {
                        oldFileName = requestString.substring(0, index);
                        newFileName = requestString.substring(index + 1);

                        System.out.println(oldFileName);
                        System.out.println(newFileName);
                    }

                    File oldNamefile = new File("src\\file_service\\files\\" + oldFileName);
                    File newNameFile = new File("src\\file_service\\files\\" + newFileName);

                    if (oldNamefile.exists()) {
                        success = oldNamefile.renameTo(newNameFile);
                    }
                    if (success) {
                        ByteBuffer code = ByteBuffer.wrap("S".getBytes());
                        serveChannel.write(code);
                        System.out.println('s');
                    } else {
                        ByteBuffer code = ByteBuffer.wrap("F".getBytes());
                        serveChannel.write(code);
                        System.out.println('f');
                    }
                    serveChannel.close();
                    break;



                // LIST FILES
                case 'l':
                    System.out.println("4");
                    a = new byte[request.remaining()];
                    request.get(a);
                    fileName = new String(a);
                    System.out.println(fileName);
                    file = new File("src\\file_service\\files");

                    if (file.exists()) {


                        String[] files = file.list();

                        System.out.println(Arrays.toString(files));

                        String stringFiles = Arrays.toString(files);

                        System.out.println(stringFiles);

                        ByteBuffer code = ByteBuffer.wrap(stringFiles.getBytes());
                        while (code.hasRemaining()) {
                            serveChannel.write(code);
                        }
                        System.out.println('s');



                    }
                    /*
                    if (success) {
                        ByteBuffer code = ByteBuffer.wrap("S".getBytes());
                        serveChannel.write(code);
                        System.out.println('s');
                    } else {
                        ByteBuffer code = ByteBuffer.wrap("F".getBytes());
                        serveChannel.write(code);
                        System.out.println('f');
                    }

                     */
                    serveChannel.close();
                    break;

            }
        }
    }

}
