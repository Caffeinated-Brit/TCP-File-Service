package file_service;

import java.io.ByteArrayOutputStream;
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
                    System.out.println("Please enter the file name: ");
                    fileName = keyboard.nextLine();
                    request = ByteBuffer.wrap((command+fileName).getBytes());
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(request);
                    channel.shutdownOutput();

                    bytesToRead = 1;
                    statusCode = ByteBuffer.allocate(bytesToRead);

                    while((bytesToRead -= channel.read(statusCode)) > 0);
                    statusCode.flip();
                    a = new byte[1];
                    statusCode.get(a);
                    System.out.println(new String(a));
                    break;



                case "g":
                    break;



                case "l":
                    System.out.println(command);
                    request = ByteBuffer.wrap((command).getBytes());
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(request);
                    channel.shutdownOutput();

                    // Read the response from the server
                    ByteBuffer responseBuffer = ByteBuffer.allocate(2500);
                    int bytesRead;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    while ((bytesRead = channel.read(responseBuffer)) > 0) {
                        responseBuffer.flip();
                        byte[] bytes = new byte[bytesRead];
                        responseBuffer.get(bytes);
                        byteArrayOutputStream.write(bytes);
                        responseBuffer.clear();
                    }

                    byte[] responseData = byteArrayOutputStream.toByteArray();

                    System.out.println(new String(responseData));

                    //System.out.println(responseData.length);


                    /*
                    bytesToRead = responseLength + 1;
                    statusCode = ByteBuffer.allocate(bytesToRead);
                    statusCode.flip();
                    a = new byte[responseLength + 1];
                    statusCode.get(a);
                    System.out.println(new String(a));

                     */
                    break;





                case "r":

                    System.out.println(command);
                    System.out.println("Please enter the file name: ");
                    String oldFileName = keyboard.nextLine();

                    System.out.println("Please enter the file name: ");
                    String newFileName = keyboard.nextLine();

                    request = ByteBuffer.wrap((command+oldFileName+"\\"+newFileName).getBytes());
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(request);
                    channel.shutdownOutput();

                    bytesToRead = 1;
                    statusCode = ByteBuffer.allocate(bytesToRead);

                    while((bytesToRead -= channel.read(statusCode)) > 0);
                    statusCode.flip();
                    a = new byte[1];
                    statusCode.get(a);
                    System.out.println(new String(a));
                    break;
                default:
                    if (!command.equals("q")) {
                        System.out.println("Unknown command!");
                    }





            }

        }while(!command.equals("q"));

    }
}
