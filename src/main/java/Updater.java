package main.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

public class Updater {

	public static void main(String[] args) {
		
		//Check we have the right number of args
		if (args.length != 1) { System.out.println("Invalid number of args given."); }
		else
		{
			String path = args[0];
			System.out.println("Matrixonator - Updater");
			System.out.println("Downloading update from > " + path);
			
			try
			{
			URL website = new URL(path);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream("Matrixonator-New.jar");
			fos.getChannel().transferFrom(rbc, 0, Integer.MAX_VALUE);
			fos.close();
			
			}
			catch (Exception e)
			{
				System.out.println("Something went wrong while downloading new version");
				System.exit(1);
			}
			
			File mxOld = new File("Matrixonator.jar");
			File mxBackup = new File("Matrixonator-O.jar");
			File mxNew = new File("Matrixonator-New.jar");
			
			if(!mxOld.exists()) { System.out.println("Can't find Matrixonator.jar"); System.exit(1); }
			else
			{
				try {
					Files.copy(mxOld.toPath(), mxBackup.toPath());
					mxOld.delete();
					Files.copy(mxNew.toPath(), mxOld.toPath());
					mxNew.delete();
					mxBackup.delete();
				} catch (IOException e) {
					System.out.println("Something went wrong with copying of files..."); System.exit(1);
				}
				
			}
			
			System.out.println("Update complete!");
			System.out.println("Starting Matrixonator...");
			try {
				Runtime.getRuntime().exec("java -jar Matrixonator.jar");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
