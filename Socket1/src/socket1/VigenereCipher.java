package socket1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RadimP
 */
public class VigenereCipher extends CipherAlgorithm implements Serializable, CipherInterface {
    private String key = "";
   
    

    public VigenereCipher() {
    }

    public VigenereCipher(String texttocipher) {
        this.texttocipher = texttocipher;
    }

    @Override
    public void cipher(String string) {
        if (string.length() < 1) {
            throw new IllegalArgumentException("Text, který se má šifrovat, neobsahuje žádné znaky");
        }
        this.texttocipher = string;
        this.encipher();

    }

    public void cipher(String string, String key) {
        if (string.length() < 1) {
            throw new IllegalArgumentException("Text, který se má šifrovat, neobsahuje žádné znaky");
        }
        this.texttocipher = string;
        this.key = key;
        this.encipher(key);

    }

    public String encipher() {
        StringBuilder builder = new StringBuilder();
        String s = Server.Helper.adjustStringToLettersAndUpperCases(this.texttocipher);
        generateKey();
      //  System.out.println(this.key);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 65 || s.charAt(i) > 90) { //znak v ASCII
                throw new IllegalArgumentException("Sifrovany retezec neobsahuje jen velka pismena");
            }
            //modularne pricteme shift
            char encyphered = s.charAt(i) + getShift(key, i) > 90 ? (char) ((s.charAt(i) + getShift(key, i)) - 26) : (char) (s.charAt(i) + getShift(key, i));
            builder.append(encyphered);
        }
        return this.cipheredtext = builder.toString();
    }

    private int getShift(String key, int i) {
        if (key.charAt(i % key.length()) < 65 || key.charAt(i % key.length()) > 90) { //znak v ASCII
            throw new IllegalArgumentException("Klic retezec neobsahuje jen velka pismena");
        }
        return ((int) key.charAt(i % key.length())) - 65;
    }

    public String encipher(String key) {
        StringBuilder builder = new StringBuilder();
        String s = Server.Helper.adjustStringToLettersAndUpperCases(this.texttocipher);
        key = Server.Helper.adjustStringToLettersAndUpperCases(key);
        this.key = key;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 65 || s.charAt(i) > 90) { //znak v ASCII
                throw new IllegalArgumentException("Sifrovany retezec neobsahuje jen velka pismena");
            }
            //modularne pricteme shift
            char encyphered = s.charAt(i) + getShift(key, i) > 90 ? (char) ((s.charAt(i) + getShift(key, i)) - 26) : (char) (s.charAt(i) + getShift(key, i));
            builder.append(encyphered);
        }
        return this.cipheredtext = builder.toString();
    }

    /**
     * Tato metoda nastaví délku klíče minimálně jeden znak a maximálně dvě
     * třetiny délky textu, který se má šifrovat. V rozmězí délky jeden až pět
     * znaků je minimální délka klíče 1 a maximální délka shodná s délkou textu.
     *
     * @return délka klíče
     */
    private int setLengthOfKey() {
        Random rand = new Random();
        int lengthofkey = 0;
        if (Server.Helper.adjustStringToLettersAndUpperCases(this.texttocipher).length() < 1) {
            throw new IllegalArgumentException("Text, který se má šifrovat, neobsahuje žádné znaky");
        }
        if (Server.Helper.adjustStringToLettersAndUpperCases(this.texttocipher).length() > 5) {
            lengthofkey = rand.nextInt(2 * Server.Helper.adjustStringToLettersAndUpperCases(this.texttocipher).length() / 3 - 2) + 3;
        } else {
            lengthofkey = rand.nextInt(Server.Helper.adjustStringToLettersAndUpperCases(this.texttocipher).length() - 2) + 1;
        }
        return lengthofkey;
    }

    /**
     * Tato metoda vytvoří šifrovací klíč o délce minimálně jeden znak a
     * maximálně dvě třetiny délky textu, který se má šifrovat. Text, který se
     * má šifrovat, musí být dlouhý minimálně jeden znak. V rozmězí délky jedna
     * až pět znaků je minimální délka klíče 3 a maximální délka shodná s délkou
     * textu.
     *
     * @return šifrovací klíč
     */
    private String generateKey() {
        int lenghtofkey = this.setLengthOfKey();
        StringBuilder builder = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < lenghtofkey; i++) {
            int character = rand.nextInt(26) + 65;
            builder.append((char) character);
        }
        return this.key = builder.toString();
    }

    public String getKey() {
        return this.key;
    }

    public void putDataIntoFile() throws Exception {        
        File file = new File("vigenereciphered.txt");        
        FileWriter out = new FileWriter(file);
        out.write(this.cipheredtext);
        out.write("@" + this.key);
        out.close();
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

    private String readFromFileUTF8(File file) throws FileNotFoundException, IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        InputStream inputstream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputstream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString("UTF-8");
    }

    private void getTextToCipherFromFile(File file) throws IOException {
        String[] tmp = this.readFromFileCp1250(file).split("@");
        this.texttocipher = Server.Helper.adjustStringToLettersAndUpperCases(tmp[0]);
       // System.out.println(tmp[0]);
    }

    private void getCipheredTextFromFile(File file) throws IOException {
        String[] tmp = this.readFromFileUTF8(file).split("@");
        this.cipheredtext = Server.Helper.adjustStringToLettersAndUpperCases(tmp[0]);
       // System.out.println(tmp[0]);
    }

    private String getKeyFromFile(File file) throws IOException {
        String keyfromfile = "";
        String[] tmp = this.readFromFileCp1250(file).split("@");
        if (tmp.length > 1) {
            keyfromfile = Server.Helper.adjustStringToLettersAndUpperCases(tmp[1]);
        }
       // System.out.println(keyfromfile);
        return keyfromfile;
    }

    private String getKeyFromFileUTF8(File file) throws IOException {
        String keyfromfile = "";
        String[] tmp = this.readFromFileUTF8(file).split("@");
        if (tmp.length > 1) {
            keyfromfile = Server.Helper.adjustStringToLettersAndUpperCases(tmp[1]);
        }
    //    System.out.println(keyfromfile);
        return keyfromfile;
    }

    @Override
    public void cipher(File file) {

        try {
            if (this.getKeyFromFile(file) != "") {
                this.getTextToCipherFromFile(file);
                this.encipher(this.getKeyFromFile(file));
                this.putDataIntoFile();
            } else {
                this.getTextToCipherFromFile(file);
              //  System.out.println(this.texttocipher);
                this.cipher(this.texttocipher);
                this.putDataIntoFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(VigenereCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(VigenereCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void decipher(String string) {
        this.cipheredtext = string;
        decipherText();
    }

    public void decipher(String string, String key) {
        this.cipheredtext = string;
        decipherText(key);

    }

    public String decipherText() {
        StringBuilder builder = new StringBuilder();
        String s = this.cipheredtext;
        String key = getKey();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 65 || s.charAt(i) > 90) {
                throw new IllegalArgumentException(
                        "Desifrovany retezec neobsahuje jen velka pismena");
            }

            char decyphered = s.charAt(i) - getShift(key, i) < 65 ? (char) ((s.charAt(i) - getShift(key, i)) + 26) : (char) (s.charAt(i) - getShift(key, i));
            builder.append(decyphered);
        }
        return decipheredtext = builder.toString();
    }

    public String decipherText(String key) {
        StringBuilder builder = new StringBuilder();
        String s = this.cipheredtext;
        this.key = key;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 65 || s.charAt(i) > 90) {
                throw new IllegalArgumentException(
                        "Desifrovany retezec neobsahuje jen velka pismena");
            }

            char decyphered = s.charAt(i) - getShift(key, i) < 65 ? (char) ((s.charAt(i) - getShift(key, i)) + 26) : (char) (s.charAt(i) - getShift(key, i));
            builder.append(decyphered);
        }
        return decipheredtext = builder.toString();
    }

    public String getDecipheredText() {
        return this.decipheredtext;
    }

    @Override
    public void decipher(File file) {
        try {
            if (this.getKeyFromFileUTF8(file) != null) {
                this.getCipheredTextFromFile(file);
                this.decipherText(this.getKeyFromFileUTF8(file));
            } else {
                this.getCipheredTextFromFile(file);
                this.decipherText();
            }
        } catch (IOException ex) {
            Logger.getLogger(VigenereCipher.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
