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
   static final int NUMBER = 2;
   static final String[] STRCMD = {
      "time", "bye"
   };

   static String[] parseCmd(String str) {
      String[] parts = str.split(" ");
     /* int ret = UNKNOWN;
      for (int i = 0; i < NUMBER; i++) {
	 if (str.compareTo(STRCMD[i]) == 0) {
	    ret = i;
	    break;
	 }
      }*/
      return parts;
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
      String cID = "client["+id+"] ";
      try {
	 log(cID + "Accepted");
	 write("Login:");
	 log(cID + "Username:" + read("", "\n"));
	 write("Password:");
	 log(cID + "Password:" + read("", "\n"));
	 write("Welcome\n");
	 boolean quit = false;
	 while (!quit) {
             String[] parsed = parseCmd(read("", "\n"));
	    switch(parsed[0]) {
                case "vigenere":
                  VigenereCipher v =  new VigenereCipher();
            switch (parsed[1]) {
                case "cipher":
                    v.cipher(parsed[2]);
                    write(v.cipheredtext);
                    break;
                case "decipher":
                    v.decipher(parsed[2]);
                    write(v.decipheredtext);
                    break;
                default:
                    write("This operation is not supported or its name is wrong!");
                    break;
            }
                    break;
	       /*case TIME:
		  write("time is:"+ new Date().toString() + "\n");
		  break;
	       case BYE:
		  log(cID + "Client sends bye");
		  quit = true;
		  break;
	       default:
		  log(cID + "Unknown message, disconnect");
		  quit = true;
		  break;*/
                default:
                write("This cipher is not supported or its name is wrong!");
               // quit = true;
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
