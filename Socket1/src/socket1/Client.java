package socket1;

/*
 * File name: Client.java
 * Date:      2006/11/27 21:12
 * Author:    Jan Faigl
 */

import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Client extends ParseMessage {

    Socket sock;
Scanner inuser;
    Client(String host, int port) {
        try {
            sock = new Socket();
            sock.connect(new InetSocketAddress(host, port));
            out = sock.getOutputStream();
            in = sock.getInputStream();
            this.inuser = new Scanner( System.in );
            write("user\n");
            read("", "Password:");
            System.out.println("Password prompt readed");
            write("heslo\n");
            read("", "Welcome\n");
            write("time\n");
            out.flush();
            System.out.println("Time on server is " + read("time is:", "\n"));
            String temp = "";
            do
            {System.out.println("Zadej druh šifry: vigenere, rsa, sha1. Udělej mezeru. Zadej cipher nebo decipher. Udělej mezeru. Zadej text k šifrování nebo došifrování. Zmáčkni enter.");
            System.out.println("Pokud chceš komunikaci ukončit, napiš bye a zmáčkni enter.");
            temp = inuser.nextLine();
            write(temp +"\n");
                    
            //write("vigenere cipher kafka žil v praze\n");
            out.flush();
            System.out.println("Text sent back by server is " + read("", "\n"));} while(!"bye".equals(temp));
           write("bye\n");
            sock.shutdownOutput();
            sock.close();
            System.out.println("Communication END");
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client c = new Client(
                args.length > 0 ? args[0] : "localhost",
                args.length > 1 ? Integer.parseInt(args[1]) : 65534
        );
    }
}
