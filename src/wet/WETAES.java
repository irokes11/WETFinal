package wet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class WETAES {

	
	// creation of the key
	private static final String KeyFile = "KeyFile";
	
	public void keyGen() throws NoSuchAlgorithmException, IOException
    {
	   synchronized(this) { // used instead of using in method
       KeyGenerator keygen = KeyGenerator.getInstance("AES");
       SecureRandom random = new SecureRandom();
       keygen.init(random);
       SecretKey key = keygen.generateKey();
       try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(KeyFile)))
       {
          out.writeObject(key);
       }
    } // end of creation key
    }	
	//Encryption of files
	
    public synchronized void encrypt(String input, String encryptedFile) throws FileNotFoundException, IOException, ClassNotFoundException, GeneralSecurityException
    {
       int mode = Cipher.ENCRYPT_MODE; //declared encryption step 
       

       try (
    	  ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(KeyFile)); //key created in part 1
          InputStream in = new FileInputStream(input); // file that is going to be encrypted
          OutputStream out = new FileOutputStream(encryptedFile)) // encrypted file created after encryption of input file
       {
          Key key = (Key) keyIn.readObject();
          Cipher cipher = Cipher.getInstance("AES");
          cipher.init(mode, key);
          Util.crypt(in, out, cipher); // final declaration of encryption 
          System.out.println("Created file "+" "+encryptedFile.hashCode());
       }
    } //End of encryption
    
     	
    //Decryption of files
    public synchronized void decrypt(String encryptedFile, String decryptedFIle,String Key) throws FileNotFoundException, IOException, ClassNotFoundException, GeneralSecurityException
    {
  	  int mode;
  	  mode = Cipher.DECRYPT_MODE; //declaration of decryption step


        try (ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(KeyFile)); // key used to decrypt
           InputStream in = new FileInputStream(encryptedFile);  // encrypted  file that is about to be decryptedFIle
           OutputStream out = new FileOutputStream(decryptedFIle))  // new created decrypted file 
        {
           Key key = (Key) keyIn.readObject();
           Cipher cipher = Cipher.getInstance("AES");
           cipher.init(mode, key);
           Util.crypt(in, out, cipher);
           System.out.println("Created file "+" "+out.hashCode());
        } //End of Decryption
        
    }
}