/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket1;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for coding text using Caesar sipher.
 *
 * @author zdenek
 */
public class CaesarCipher extends CipherAlgorithm {

    public ArrayList<String> cipher = new ArrayList<>();
    public ArrayList<String> decipher = new ArrayList<>();
    protected String code = "123";
    private String readyText;

    /**
     * Constructor for Caesar ciphre using text on input
     *
     * @author zdenek
     * @param textToCipher text for coding
     */
    public CaesarCipher() {}
    public CaesarCipher(String textToCipher) {

        this.texttocipher = textToCipher;

    }

    /**
     * this method prepare text for ciphre makes all letter uppercase ,delete
     * all non letters signs, remove diacritics from words
     *
     * @return text for code
     */
    
    public String prepareText() {

        this.texttocipher = this.texttocipher.toUpperCase(); // change text to uppercase
        this.texttocipher = this.texttocipher.replaceAll("[^\\p{L} ]", "").replaceAll("\\s", "");// remove specials signs
        String normalized = java.text.Normalizer.normalize(this.texttocipher, java.text.Normalizer.Form.NFD);// change stanart czech diacritic to non diacritic
        this.texttocipher = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");// remove diacritical marks which left after normalizer

        return this.texttocipher; // ready text for coding
    }

    private String Code() {
        this.code = "123";

        return code;
    }

    /**
     * encodes text to cipher 
     *
     * letters
     *
     * @param string text
     */
    @Override
    public void cipher(String string) { // převeď array na string
        this.readyText = string;

        // main algorithm
        int j = 0, s = 1;
        for (int i = 0; i < this.readyText.length(); i++) {
            if (s == 1) {
                if ((int) this.readyText.charAt(i) <= 90 && ((int) this.readyText.charAt(i) + s * ((int) this.code.charAt(j) - 48)) > 90) {
                    // (char) ((int) this.texttocipher.charAt(i) + s * ((int) this.code.charAt(j) - 48) - 25
                    this.cipher.add(Character.toString((char) ((int) this.readyText.charAt(i) + s * (int) (this.code.charAt(j) - 48) - 26)));
                    // System.out.print( )

                } else {

                    this.cipher.add(Character.toString((char) ((int) this.readyText.charAt(i) + s * (int) this.code.charAt(j) - 48)));
                }
            } else if ((int) this.readyText.charAt(i) <= 90 && ((int) this.readyText.charAt(i) + s * ((int) this.code.charAt(j) - 48)) < 65) {

                this.cipher.add(Character.toString((char) ((int) this.readyText.charAt(i) + s * ((int) this.code.charAt(j) - 48) + 26)));

            } else {

                this.cipher.add(Character.toString((char) ((int) this.readyText.charAt(i) + s * ((int) this.code.charAt(j) - 48))));
            }
            if (j == this.code.length() - 1) {
                j = 0;
                s = s * -1;

            } else {
                j++;
            }
        }

    }

    @Override
    public void cipher(File file) {

    }
/**
     * DEcodes CIPHER to text 
     * 
     * letters
     *
     * @param string text
     */
    
    @Override
    public void decipher(String string) {// převeĎ výsledek na string

        this.cipheredtext = "ANXSMLDMROGZB";
        int j = 0, s = 1;
        for (int i = 0; i < this.cipheredtext.length(); i++) {
            if (s == 1) {
                if (((int) this.cipheredtext.charAt(i) - s * ((int) this.code.charAt(j) - 48)) < 65) {

                    this.decipher.add(Character.toString((char) ((int) this.cipheredtext.charAt(i) - s * (int) (this.code.charAt(j) - 48) + 26)));

                } else {

                    this.decipher.add(Character.toString((char) ((int) this.cipheredtext.charAt(i) - s * ((int) this.code.charAt(j) - 48))));
                }
            } else if (((int) this.cipheredtext.charAt(i) - s * ((int) this.code.charAt(j) - 48)) > 90) {

                this.decipher.add(Character.toString((char) ((int) this.cipheredtext.charAt(i) - s * ((int) this.code.charAt(j) - 48) - 26)));

            } else {

                this.decipher.add(Character.toString((char) ((int) this.cipheredtext.charAt(i) - s * ((int) this.code.charAt(j) - 48))));
            }
            if (j == this.code.length() - 1) {
                j = 0;
                s = s * -1;

            } else {
                j++;
            }

        }

    }

    @Override
    public void decipher(File file) {
    }

    public String toStringCipher() {

        return Arrays.toString(this.cipher.toArray());

    }

    public String toStringDeCipher() {

        return Arrays.toString(this.decipher.toArray());

    }
}
