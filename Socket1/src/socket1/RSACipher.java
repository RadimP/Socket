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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author RadimP
 */
public class RSACipher extends CipherAlgorithm implements Serializable, CipherInterface {

    public static final String PRIVATE_KEY_FILE = "c:\\Users\\RadimP\\Documents\\NetBeansProjects\\Projekt-sifrovani\\sifrovani\\private.key";
    public static final String PUBLIC_KEY_FILE = "c:\\Users\\RadimP\\Documents\\NetBeansProjects\\Projekt-sifrovani\\sifrovani\\public.key";
    private KeyPair keypair;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    public Cipher RSAciph;

    RSACipher() {
    }

    RSACipher(String texttocipher) {
        this.texttocipher = texttocipher;
    }

    @Override
    public void cipher(String string) {
        this.generateKey();
        try {
            this.RSAciph = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.cipheredtext = this.encrypt(string);
        } catch (Exception ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String encrypt(String plaintext) throws Exception {
        this.texttocipher = plaintext;
        this.RSAciph.init(Cipher.ENCRYPT_MODE, this.publicKey);
        byte[] bytes = this.texttocipher.getBytes("UTF-8");

        byte[] encrypted = blockCipher(bytes, Cipher.ENCRYPT_MODE);

        char[] encryptedTranspherable = Hex.encodeHex(encrypted);
        return new String(encryptedTranspherable);
    }

    private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException {
        // string initialize 2 buffers.
        // scrambled will hold intermediate results
        byte[] scrambled = new byte[0];

        // toReturn will hold the total result
        byte[] toReturn = new byte[0];
        // if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
        int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

        // another buffer. this one will hold the bytes that have to be modified in this step
        byte[] buffer = new byte[length];

        for (int i = 0; i < bytes.length; i++) {

            // if we filled our buffer array we have our block ready for de- or encryption
            if ((i > 0) && (i % length == 0)) {
                //execute the operation
                scrambled = RSAciph.doFinal(buffer);
                // add the result to our total result.
                toReturn = append(toReturn, scrambled);
                // here we calculate the length of the next buffer required
                int newlength = length;

                // if newlength would be longer than remaining bytes in the bytes array we shorten it.
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                // clean the buffer array
                buffer = new byte[newlength];
            }
            // copy byte into our buffer.
            buffer[i % length] = bytes[i];
        }

        // this step is needed if we had a trailing buffer. should only happen when encrypting.
        // example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
        scrambled = RSAciph.doFinal(buffer);

        // final step before we can return the modified data.
        toReturn = append(toReturn, scrambled);

        return toReturn;
    }

    private byte[] append(byte[] prefix, byte[] suffix) {
        byte[] toReturn = new byte[prefix.length + suffix.length];
        System.arraycopy(prefix, 0, toReturn, 0, prefix.length);
        System.arraycopy(suffix, 0, toReturn, prefix.length, suffix.length);
        return toReturn;
    }

    public void generateKey() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            this.keypair = kpg.generateKeyPair();
            this.publicKey = this.keypair.getPublic();
            this.privateKey = this.keypair.getPrivate();
            this.savepublicKey();
            this.saveprivateKey();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void savepublicKey() {
        ObjectOutputStream publicKeyOS = null;
        try {
            publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(new File(PUBLIC_KEY_FILE)));
            publicKeyOS.writeObject(this.publicKey);
            publicKeyOS.close();
        } catch (IOException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                publicKeyOS.close();
            } catch (IOException ex) {
                Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveprivateKey() {
        ObjectOutputStream publicKeyOS = null;
        try {
            publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(new File(PRIVATE_KEY_FILE)));
            publicKeyOS.writeObject(this.privateKey);
            publicKeyOS.close();
        } catch (IOException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                publicKeyOS.close();
            } catch (IOException ex) {
                Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda ověří, zda byl vygenereován veřejný a soukormý klíč.
     *
     * @return vrátí pravdivostní hodnotu true, pokud byl pár klíčů vygenerován.
     */
    public static boolean areKeysPresent() {

        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    public void readPrivateKeyFromFile() throws IOException {
        ObjectInputStream inputStream = null;
        try {

            inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
            this.privateKey = (PrivateKey) inputStream.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            inputStream.close();
        }
    }

    public void readPublicKeyFromFile() throws IOException {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            this.publicKey = (PublicKey) inputStream.readObject();
            inputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            inputStream.close();
        }
    }

    public String decrypt(String encrypted) throws Exception {
        this.RSAciph.init(Cipher.DECRYPT_MODE, this.privateKey);
        byte[] bts = Hex.decodeHex(encrypted.toCharArray());

        byte[] decrypted = blockCipher(bts, Cipher.DECRYPT_MODE);

        return new String(decrypted, "UTF-8");
    }

    @Override
    public void cipher(File file) {
        try {
            this.getTextToCipherFromFile(file);
        } catch (IOException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.generateKey();
        try {
            this.RSAciph = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.cipheredtext = this.encrypt(this.texttocipher);
            this.putDataIntoFile();
        } catch (Exception ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getTextToCipherFromFile(File file) throws IOException {
        this.texttocipher = this.readFromFileCp1250(file);
    }

    private void getCipheredTextFromFile(File file) throws IOException {
                this.cipheredtext = this.readFromFileUTF8(file);
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

    public void putDataIntoFile() throws Exception {
        File file = new File("c:\\Users\\RadimP\\Documents\\NetBeansProjects\\Projekt-sifrovani\\sifrovani\\RSAciphered.txt");
        FileWriter out = new FileWriter(file);
        out.write(this.cipheredtext);
        out.close();
    }

    @Override
    public void decipher(String string) {
        try {
            this.readPrivateKeyFromFile();
            this.decipheredtext = this.decrypt(string);
        } catch (Exception ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDecipheredText() {
        return this.decipheredtext;
    }

    @Override
    public void decipher(File file) {
try {
            this.getCipheredTextFromFile(file);
           // System.out.println(this.cipheredtext);
            this.readPrivateKeyFromFile();
        } catch (IOException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
                try {
            this.RSAciph = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.decipheredtext = this.decrypt(this.cipheredtext);
          //  System.out.println(this.decipheredtext);
                    } catch (Exception ex) {
            Logger.getLogger(RSACipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
