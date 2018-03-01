package socket1;

/*
 * File name: ClientHandler.java
 * Date:      2006/11/27 21:23
 * Author:    Jan Faigl
 */
import java.util.Date;
import java.net.Socket;
import java.io.IOException;

class ClientHandler extends ParseMessage implements Runnable {

    static final int UNKNOWN = -1;
    static final int TIME = 0;
    static final int BYE = 1;
    static final int CAESAR = 2;
    static final int ENIGMA = 3;
    static final int VIGENERE = 4;
    static final int SHA1 = 5;
    static final int RSA = 6;
    static final int NUMBER = 7;
    static final String[] STRCMD = {
        "time", "bye", "caesar", "enigma", "vigenere", "sha1", "rsa"
    };
    private static String[] parts;

    static int parseCmd(String str) {
        parts = str.split(" ", 3);
        str = parts[0].replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", "");
        int ret = UNKNOWN;
        for (int i = 0; i < NUMBER; i++) {
            if (str.compareTo(STRCMD[i]) == 0) {
                ret = i;
                break;
            }
        }
        return ret;
    }

    Socket sock;
    int id;

    ClientHandler(Socket iSocket, int iID) throws IOException {
        sock = iSocket;
        id = iID;
        out = sock.getOutputStream();
        in = sock.getInputStream();

    }

    public void start() {
        new Thread(this).start();
    }

    void log(String str) {
        System.out.println(str);
    }

    @Override
    public void run() {
        String cID = "client[" + id + "] ";
        try {
            log(cID + "Accepted");
            write("Login:");
            log(cID + "Username:" + read("", "\n"));
            write("Password:");
            log(cID + "Password:" + read("", "\n"));
            write("Welcome\n");
            boolean quit = false;
            while (!quit) {
                //String[] parsed = parseCmd(read("", "\n"));
                switch (parseCmd(read("", "\n"))) {
                    case TIME:
                        write("time is:" + new Date().toString() + "\n");
                        break;
                    case BYE:
                        log(cID + "Client sends bye");
                        quit = true;
                        break;
                    case CAESAR:
                        CaesarCipher c = new CaesarCipher();
                        switch (parts[1]) {
                            case "cipher":
                                c.cipher(parts[2]);
                                write(c.cipheredtext + "\n");
                                break;
                            case "decipher":
                                c.decipher(parts[2]);
                                write(c.decipheredtext + "\n");
                                break;
                            default:
                                write("This operation is not supported or its name is wrong!\n");
                                break;
                        }
                        break;

                    case ENIGMA:
                        Enigma e = new Enigma();
                        switch (parts[1]) {
                            case "cipher":
                                e.cipher(parts[2]);
                                write(e.cipheredtext + "\n");
                                break;
                            case "decipher":
                                e.decipher(parts[2]);
                                write(e.decipheredtext + "\n");
                                break;
                            default:
                                write("This operation is not supported or its name is wrong!\n");
                                //   read("", "\n");
                                break;
                        }
                        break;

                    case VIGENERE:
                        VigenereCipher v = new VigenereCipher();
                        switch (parts[1]) {
                            case "cipher":
                                v.cipher(parts[2]);
                                write(v.cipheredtext + "\n");
                                break;
                            case "decipher":
                                v.decipher(parts[2]);
                                write(v.decipheredtext + "\n");
                                break;
                            default:
                                write("This operation is not supported or its name is wrong!\n");
                                //   read("", "\n");
                                break;
                        }
                        break;

                    case SHA1:
                        SHA1 sha1 = new SHA1();
                        switch (parts[1]) {
                            case "cipher":
                                sha1.cipher(parts[2]);
                                write(sha1.cipheredtext + "\n");
                                break;
                            case "decipher":
                                sha1.decipher(parts[2]);
                                write(sha1.decipheredtext + "\n");
                                break;
                            default:
                                write("This operation is not supported or its name is wrong!\n");
                                //   read("", "\n");
                                break;
                        }
                        break;

                    case RSA:
                        RSACipher rsa = new RSACipher();
                        switch (parts[1]) {
                            case "cipher":
                                rsa.cipher(parts[2]);
                                write(rsa.cipheredtext + "\n");
                                break;
                            case "decipher":
                                rsa.decipher(parts[2]);
                                write(rsa.decipheredtext + "\n");
                                break;
                            default:
                                write("This operation is not supported or its name is wrong!\n");
                                //   read("", "\n");
                                break;
                        }
                        break;

                    default:
                        log(cID + "Unknown message, disconnect");
                        quit = true;
                        break;

                }
            }
            sock.shutdownOutput();
            sock.close();
        } catch (Exception e) {
            System.out.println(cID + "Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
