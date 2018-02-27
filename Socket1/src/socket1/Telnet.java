package socket1;

/*
 * File name: Telnet.java
 * Date:      2006/11/27 21:32
 * Author:    Jan Faigl
 */

import java.io.OutputStream;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;

public class Telnet {

    static final int SIZE = 1024;

    byte[] buf;
    int size;

    class ByeException extends RuntimeException {
    }

    class Reader extends Thread {

        InputStream in;
        boolean error;

        Reader(InputStream iIn) {
            in = iIn;
            start();
        }

        @Override
        public void run() {
            int i;
            error = false;
            try {
                while ((i = (byte) in.read()) != -1) {
                    System.out.print((char) i);
                }
            } catch (IOException e) {
                error = true;
                System.out.println("Connection error: " + e.getMessage());
            }
        }
    }

    class Key extends Thread {

        OutputStream out;

        Key(OutputStream iOut) {
            out = iOut;
            start();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    out.write(System.in.read());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Telnet(String host, int port) {
        buf = new byte[SIZE];
        boolean bye = false;
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(host, port));
            Reader r = new Reader(sock.getInputStream());
            Key k = new Key(sock.getOutputStream());
            r.join();
            System.in.close();
            k.join();
        } catch (ByeException e) {
            System.out.println("Connection Lost");
            bye = true;
            try {
                System.in.close();
            } catch (Exception e2) {
            }
        } catch (Exception e) {
            if (!bye) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Telnet(
                args.length > 0 ? args[0] : "localhost",
                args.length > 1 ? Integer.parseInt(args[1]) : 9000);
    }
}
