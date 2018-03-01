/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Student
 */
public class SHA1 extends CipherAlgorithm implements Serializable, CipherInterface {

    

    public SHA1() {
    }

    public SHA1(String texttocipher) {
        this.texttocipher = texttocipher;
    }

    @Override
    public void cipher(String string) {
        this.texttocipher = Server.Helper.adjustStringToLettersAndUpperCases(string);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(this.texttocipher.getBytes("utf8"));
            this.cipheredtext = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void putDataIntoFile() throws Exception {
        File file = new File("sha1ciphered.txt");
        FileWriter out = new FileWriter(file);
        out.write(this.cipheredtext);
        out.close();
    }

    @Override
    public void cipher(File file) {
        try {
            this.getTextToCipherFromFile(file); //načtení textu ze souboru
        } catch (IOException ex) {
            Logger.getLogger(SHA1.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.texttocipher = Server.Helper.adjustStringToLettersAndUpperCases(this.texttocipher); //převod textu na kapitálky a odstraněšní mezer, interounkce a diakritiky
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(this.texttocipher.getBytes("utf8"));
            this.cipheredtext = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.putDataIntoFile();
        } catch (Exception ex) {
            Logger.getLogger(SHA1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String readFromFileCp1250(File file) throws FileNotFoundException, IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        InputStream inputstream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputstream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString("Cp1250");
    }

    private void getTextToCipherFromFile(File file) throws IOException {
        this.texttocipher = this.readFromFileCp1250(file);

    }

    @Override
    public void decipher(String string) {
        throw new UnsupportedOperationException("This operation is not possible."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void decipher(File file) {
        throw new UnsupportedOperationException("This operation is not possible."); //To change body of generated methods, choose Tools | Templates.
    }

}
