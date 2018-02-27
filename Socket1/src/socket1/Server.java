package socket1;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
public static class Helper {

        public static void serialize(Object o, String name) throws Exception {
            try {
                // Serializace do souboru
                ObjectOutput out = new ObjectOutputStream(
                        new FileOutputStream(name));
                // jméno souboru
                out.writeObject(o);
                out.close();
            } catch (IOException e) {
                throw new Exception("Chyba při zápisu souboru : " + e);
            }

        }

        public static Object deserialize(String name) throws Exception {
            Object o = new Object();
            // Načtení ze souboru
            try {
                File file = new File(name);
                ObjectInputStream in = new ObjectInputStream(
                        new FileInputStream(file));
                // Deserializace objektu
                o = in.readObject();

                in.close();

            } catch (IOException ex) {
                Logger.getLogger(VigenereCipher.class.getName()).log(Level.SEVERE, null, ex);
            }
            return o;
        }

        public static String adjustStringToLettersAndUpperCases(String string) {
            string = string.replaceAll("[^\\p{L} ]", "").replaceAll("\\s", "").toUpperCase();
            String normalized = java.text.Normalizer.normalize(string, java.text.Normalizer.Form.NFD);// change stanart czech diacritic to non diacritic
            string = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");// remove diacritical marks which left after normalizer

            return string;

        }
    }
    public Server(int port) throws IOException {
        int count = 0;
        ServerSocket servsock = new ServerSocket(port);
        while (true) {
            try {
                new ClientHandler(servsock.accept(), count++).start();
            } catch (IOException e) {
                System.out.println("IO error in new client");
            }
        }
    } // Server()

    public static void main(String[] args) {
        try {
            new Server(args.length > 0 ? Integer.parseInt(args[0]) : 65534);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
