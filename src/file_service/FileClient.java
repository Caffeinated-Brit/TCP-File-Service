package file_service;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
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
            System.out.println(" ");
            System.out.println("R: Rename ");
            System.out.println("L: List");
            System.out.println("G: Download ");
            System.out.println("U: Upload ");
            System.out.println("N: Create empty server file ");
            System.out.println("D: Delete ");
            System.out.println("Please type a command: ");
            System.out.println(" ");
            command = keyboard.nextLine().toLowerCase();

            switch(command) {
                //DELETE
                case "d":
                    System.out.println("Please enter the file name of the file you want to delete: ");
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

                //CREATE NEW EMPTY FILE ON SERVER FOR TESTING
                case "n":
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

                // UPLOAD
                case "u":
                    System.out.println("Please enter the file name you want to upload: ");
                    fileName = keyboard.nextLine();
                    System.out.println("Uploading");

                    File file = new File("src\\file_service\\clientFiles\\" + fileName);
                    if(!file.exists()) {
                        System.out.println("File does not exist, enter an existing file.");
                        break;
                    }

                    request = ByteBuffer.allocate(2000);
                    request.put(command.getBytes());
                    request.putInt(fileName.length());
                    request.put(fileName.getBytes());
                    FileInputStream fs = new FileInputStream(file);
                    FileChannel fc = fs.getChannel();

                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));

                    do {
                        request.flip();
                        channel.write(request);
                        request.clear();
                    }while (fc.read(request) > 0);

                    fs.close();
                    channel.shutdownOutput();

                    bytesToRead = 1;
                    statusCode = ByteBuffer.allocate(bytesToRead);

                    while((bytesToRead -= channel.read(statusCode)) > 0);
                    statusCode.flip();
                    a = new byte[1];
                    statusCode.get(a);
                    System.out.println(new String(a));
                    break;

                //DOWNLOAD / GET
                case "g":
                    System.out.println("Please enter the file name you want to download: ");
                    fileName = keyboard.nextLine();
                    request = ByteBuffer.wrap((command + fileName).getBytes());
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(request);
                    channel.shutdownOutput();

                    // Read the response from the server
                    ByteBuffer responseBuffer = ByteBuffer.allocate(2500);
                    int bytesRead = 0;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    while ((bytesRead = channel.read(responseBuffer)) > 0) {
                        responseBuffer.flip();
                        byte[] bytes = new byte[bytesRead];
                        responseBuffer.get(bytes);
                        byteArrayOutputStream.write(bytes);
                        responseBuffer.clear();
                    }

                    byte[] responseData = byteArrayOutputStream.toByteArray();
                    //System.out.println(new String(responseData));

                    file = new File("src\\file_service\\clientFiles\\" + fileName);

                    if (file.createNewFile()) {
                        System.out.println("File downloaded: " + file.getName());
                        FileWriter myWriter = new FileWriter(file);
                        myWriter.write(new String(responseData));
                        myWriter.close();
                    } else {
                        System.out.println("ERROR: file exists");
                    }
                    break;

                //LIST
                case "l":
                    request = ByteBuffer.wrap((command).getBytes());
                    channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress(args[0], serverPort));
                    channel.write(request);
                    channel.shutdownOutput();


                    // Read the response from the server
                    responseBuffer = ByteBuffer.allocate(2500);
                    bytesRead = 0;
                    byteArrayOutputStream = new ByteArrayOutputStream();

                    while ((bytesRead = channel.read(responseBuffer)) > 0) {
                        responseBuffer.flip();
                        byte[] bytes = new byte[bytesRead];
                        responseBuffer.get(bytes);
                        byteArrayOutputStream.write(bytes);
                        responseBuffer.clear();
                    }
                    responseData = byteArrayOutputStream.toByteArray();
                    System.out.println(new String(responseData));

                    //System.out.println(responseData.length);
                    break;


                case "r":
                    System.out.println(command);
                    System.out.println("Please enter the file name you want to change: ");
                    String oldFileName = keyboard.nextLine();

                    System.out.println("Please enter the file name you want to change to: ");
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
