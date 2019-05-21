package wet;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.crypto.*;


public class App
{
	private static final int queueSize=2;
	
   public static void main(String[] args) 
      throws IOException, GeneralSecurityException, ClassNotFoundException
   {
	   	WETAES wetaes = new WETAES();
	   	BlockingQueue<String> queue = new PriorityBlockingQueue<String>(queueSize); //Thread safe data structures used blocking queue
	   	String input = "plik5.txt";
	   	String output = "newIS.txt";
	   	String encryptedFile = "irekEncrypted.txt";
		String Keyfile = "KeyFile"; 
		
		try {
			wetaes.keyGen(); //one resource used by all tasks-generation of a key
			
			Runnable task1 = () -> {
				while(true) {  //used flag 
					try {
							
						wetaes.encrypt(input, encryptedFile);
						System.out.println("TASK 1 ->Created "+encryptedFile);
						Thread.sleep(1000);
						
					} catch (ClassNotFoundException | IOException | GeneralSecurityException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			};
			
			Runnable task2 = () -> {
				while(true) {
					try {
						
						//int i = (int)(Math.random()+5);
						int i = 2;
					
						String decrypt = "FileDecrypted"+i+".txt";
						wetaes.decrypt(encryptedFile, output,Keyfile); //encrypt("KeyFIle"+i+".txt", encrypt);
						queue.put(decrypt);
						System.out.println("TAST 2 -- >Created "+decrypt);
						
						Thread.sleep(1000);
						
					} catch (ClassNotFoundException | IOException | GeneralSecurityException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
				
				
			};
			
			ExecutorService execute = Executors.newCachedThreadPool();
			
			execute.execute(task1);  // run task1 followed by task2
			execute.execute(task2);
			execute.shutdown();
			
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
   }
}

