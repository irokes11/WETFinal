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

import javax.crypto.*;


public class App
{
	private static final int queueSize=5;
	
   public static void main(String[] args) 
      throws IOException, GeneralSecurityException, ClassNotFoundException
   {
	   	WETAES wetaes = new WETAES();
	   	BlockingQueue<String> queue = new ArrayBlockingQueue(queueSize); //Thread safe data structures
	   
		
		try {
			wetaes.keyGen(); //one resource used by all tasks
			
			Runnable task1 = () -> {
				while(true) {
					try {
						
						String szyfruj = queue.take();
						String rozszyfruj = "FileDecrypted"+szyfruj.substring(5);
						wetaes.decrypt(szyfruj, rozszyfruj);
						System.out.println("Created "+rozszyfruj+" file");
						Thread.sleep(100);
						
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
						int i = 1;
						String szyfruj = "FileDecrypted"+i+".txt";
						wetaes.encrypt("KeyFIle"+i+".txt", szyfruj);
						queue.put(szyfruj);
						System.out.println("Created "+szyfruj+" file");
						
						Thread.sleep(100);
						
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
			execute.execute(task1);
			execute.execute(task2);
			execute.shutdown();
			
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
   }
}

