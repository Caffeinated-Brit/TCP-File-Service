package file_service;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class FileClient {
    public static void main(String[] args) throws Exception{
        if (args.length != 2) {
            System.out.println("Syntax: FileClient <ServerIP> <ServerPort>");
            return;
        }
        int serverPort = Integer.parseInt(args[1]);

        String command;

        do{
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Please type a command: ");
            command = keyboard.nextLine().toLowerCase();
            switch(command) {
                case "d":
                    System.out.println(command);
                    System.out.println("Please enter the file name: ");
                    String fileName = keyboard.nextLine();
                    ByteBuffer request = ByteBuffer.wrap((command+fileName).getBytes());
                    SocketChannel channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(request);
                    channel.shutdownOutput();

                    int bytesToRead = 1;
                    ByteBuffer statusCode = ByteBuffer.allocate(bytesToRead);

                    while((bytesToRead -= channel.read(statusCode)) > 0);
                    statusCode.flip();
                    byte[] a = new byte[1];
                    statusCode.get(a);
                    System.out.println(new String(a));
                    break;

                case "u":
                    System.out.println(command);



                    break;

                case "g":
                    break;
                case "l":
                    break;
                case "r":
                    break;
                default:
                    if (!command.equals("q")) {
                        System.out.println("Unknown command!");
                    }





            }

        }while(!command.equals("q"));

    }
}
