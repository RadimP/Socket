package socket1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author RadimP
 */
public class InputAndProcessing {

    CaesarCipher caesar;
    Enigma enigma;
    VigenereCipher vigenere;
    SHA1 sha1;
    RSACipher rsa;
    String[] args;
    ArrayList<String> input;
    /* static final String[] THECIPHER = {
        "enigma", "caesar", "vigenere", "sha1", "rsa", "-h"
    };*/
    static final String NAPOVEDA = "Jako první parametr zadejte druh šifry: enigma, caesar, vigenere, sha1, rsa.\nJako druhý parametr zadejte, zda chcete šifrovat nebo dešiftovat: cipher, decipher.\nJako třetí parametr zadejte, zda chcete dešifrovat ze souboru, nebo zadat text: file, text.\nJako čtvrtý parametr zadejte cestu k souboru nebo text. Je-li text víceslovný napište ho do dvojitých uvozovek.\n ";

    /**
     * Konstruktor bez vtsupních parametrů, který voláním jenodltivých metod
     * této třídy zpracovává vstup.
     */
   /* InputAndProcessing() { if(Sifrovani.getArgs().length>0)
    { this.getInputFromCmdline();} else {this.getInputFromConsole();}
    }*/

    /**
     * Tato metoda zpracuje arg[0] z příkazového řádku (rozliší druh šifry) a
     * pak pomocí metody processCMdArg3(CipherAlgorithm ca) zpracuje ostatní
     * argumenty příkazového řádku a vytiskne výsledek (šifrovaný nebo
     * dešifrovaný text).
     */
 /*   public void getInputFromCmdline() {
        this.args = Sifrovani.getArgs();
        if (this.args.length >= 4) {
            switch (args[0].toLowerCase()) {
                case "enigma":
                    enigma = new Enigma();
                    processCMdArg3(this.enigma);
                    break;
                case "caesar":
                    caesar = new CaesarCipher();
                    processCMdArg3(this.caesar);
                    break;
                case "vigenere":
                    vigenere = new VigenereCipher();
                    processCMdArg3(this.vigenere);
                    break;
                case "sha1":
                    sha1 = new SHA1();
                    processCMdArg3(this.sha1);
                    break;
                case "rsa":
                    rsa = new RSACipher();
                    processCMdArg3(this.rsa);
                    break;
                case "-h":
                    System.out.println(NAPOVEDA);
                    break;
                default:
                    System.out.println("Not the supported cipher!");
                    break;
            }
        } else {
            if ("-h".equals(args[0].toLowerCase())) {
                System.out.println(NAPOVEDA);
            } else {
                System.out.println("Not enough arguments!");
            }
        }
    }*/

    /**
     * Tato metoda na základě konkrétní třídy třídy načtené metodou
     * getInputFromCmdline() zpracuje ostatní argumenty příkazového řádku a
     * vytiskně výsledek nebo nápovědu nebo oznámí, že zadané argumenty nelze
     * zpracovat.
     *
     * @param ca instance zadané třídy šifry (CaesarCipher,VigenerCipher,
     * Enigma, SHA1 nebo RSACipher) převzatá z metody getInputFromCmdline().
     * Přestože je jako vstupní třída definována rodičovská třída
     * CipherAlgorithm, metoda vnitřně pracuje s některým potomkem.
     */
/*    public void processCMdArg3(CipherAlgorithm ca) {
        this.args = Sifrovani.getArgs();
        if (this.args.length >= 4) {
            switch (args[2].toLowerCase()) {
                case "file":
                    if ("cipher".equals(args[1].toLowerCase())) {
                        ca.cipher(new File(args[3]));
                        System.out.println(ca.cipheredtext);
                    } else if ("decipher".equals(args[1].toLowerCase())) {
                        ca.decipher(new File(args[3]));
                        System.out.println(ca.decipheredtext);
                    } else if ("-h".equals(args[1].toLowerCase())) {
                        System.out.println(NAPOVEDA);
                    } else {
                        System.out.println("Not the supported operation!");
                    }
                    break;
                case "text":
                    if ("cipher".equals(args[1].toLowerCase())) {
                        ca.cipher(args[3]);
                        System.out.println(ca.cipheredtext);
                    } else if ("decipher".equals(args[1].toLowerCase())) {
                        ca.decipher(args[3]);
                        System.out.println(ca.decipheredtext);
                    } else if ("-h".equals(args[1].toLowerCase())) {
                        System.out.println(NAPOVEDA);
                    } else {
                        System.out.println("Not the supported operation!");
                    }
                    break;
                case "-h":
                    System.out.println(NAPOVEDA);
                    break;
                default:
                    System.out.println("Not the supported operation!");
                    break;

            }
        } else {
            System.out.println("Not enough arguments!");
        }
    }*/

  /*  public void getInputFromConsole() {
        this.input = new ArrayList<String>();
        Scanner sc = new Scanner(System.in);
        System.out.println("Jako první parametr zadejte druh šifry: enigma, caesar, vigenere, sha1, rsa.");
        input.add(sc.nextLine());
        System.out.println("Jako druhý parametr zadejte, zda chcete šifrovat nebo dešiftovat: cipher, decipher.");
        input.add(sc.nextLine());
        System.out.println("Jako třetí parametr zadejte, zda chcete šifrovat či dešifrovat ze souboru, nebo zadat text: file, text.");
        input.add(sc.nextLine());
        System.out.println("Jako čtvrtý parametr zadejte cestu k souboru nebo text.");
        input.add(sc.nextLine());
        if (input.size() >= 4) {
            switch (input.get(0).toLowerCase()) {
                case "enigma":
                    enigma = new Enigma();
                    processInputFromConsole(this.enigma, input);
                    break;
                case "caesar":
                    caesar = new CaesarCipher();
                    processInputFromConsole(this.caesar, input);
                    break;
                case "vigenere":
                    vigenere = new VigenereCipher();
                    processInputFromConsole(this.vigenere, input);
                    break;
                case "sha1":
                    sha1 = new SHA1();
                    processInputFromConsole(this.sha1, input);
                    break;
                case "rsa":
                    rsa = new RSACipher();
                    processInputFromConsole(this.rsa, input);
                    break;
                default:
                    System.out.println("Not the supported cipher!");
                    break;
            }
        } else {
            System.out.println("Not enough arguments!");
        }
    }*/


/*public void processInputFromConsole(CipherAlgorithm ca, ArrayList<String> input) {
        this.input = input;
        if (this.input.size() >= 4) {
            switch (input.get(2).toLowerCase()) {
                case "file":
                    if ("cipher".equals(input.get(1).toLowerCase())) {
                        ca.cipher(new File(input.get(3)));
                        System.out.println(ca.cipheredtext);
                    } else if ("decipher".equals(input.get(1).toLowerCase())) {
                        ca.decipher(new File(input.get(3)));
                        System.out.println(ca.decipheredtext);
                    } else {
                        System.out.println("Not the supported operation!");
                    }
                    break;
                case "text":
                    if ("cipher".equals(input.get(1).toLowerCase())) {
                        ca.cipher(input.get(3));
                        System.out.println(ca.cipheredtext);
                    } else if ("decipher".equals(input.get(1).toLowerCase())) {
                        ca.decipher(input.get(3));
                        System.out.println(ca.decipheredtext);
                    } else {
                        System.out.println("Not the supported operation!");
                    }
                    break;
                default:
                    System.out.println("Not the supported operation!");
                    break;
            }
        } else {
            System.out.println("Not enough arguments!");
        }
    }*/
}
