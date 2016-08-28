package com.bunchiestudios.cahserver;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by rdelfin on 8/27/16.
 */
public class ClientHandler implements Runnable {
    private static Logger log = Logger.getLogger(ClientHandler.class);

    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private Protocol protocol;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        out = new DataOutputStream(socket.getOutputStream());
        in =  new DataInputStream(socket.getInputStream());
        protocol = new Protocol();
    }

    @Override
    public void run() {
        try {
            log.info("ClientHandler called");
            sendData(protocol.receive(receiveData()));
        } catch(Exception e) {
            System.err.println("Exception caught while executing protocol message.");
            e.printStackTrace();
        }
    }

    public byte[] receiveData() throws IOException {
        int length = in.readInt();

        byte[] data = new byte[length];
        in.read(data, 0, length);

        return data;
    }

    public void sendData(byte[] data) throws IOException {
        ByteBuffer lengthBuf = ByteBuffer.allocate(4);
        lengthBuf.order(ByteOrder.BIG_ENDIAN);
        lengthBuf.putInt(data.length);

        out.write(lengthBuf.array());
        out.write(data);
    }
}
