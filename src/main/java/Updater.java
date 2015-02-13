package main.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Updater {

  
  /**
   * Utility program to download a Matrixonator update from the given location. The program will
   * keep a backup of the old, working version at Marixonator-O.jar
   * 
   * @param args - Pathname for Matrixonator Update
   */
  public static void main(String[] args) {

    // Check we have the right number of args
    if (args.length != 1) {
      System.out.println("Invalid number of args given.");
    } else {     
      // Pause for a bit before starting. Allow Matrixonator to close! :)
      try {
        Thread.sleep(2500);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }

      // Setting up and downloading...
      String path = args[0];
      System.out.println("Matrixonator - Updater");
      System.out.println("Downloading update from > " + path);
      
      
      try {
        URL website = new URL(path);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("Matrixonator-New.jar");
        fos.getChannel().transferFrom(rbc, 0, Integer.MAX_VALUE);
        fos.close();
      } catch (Exception e) {
        System.out.println("Something went wrong while downloading new version");
        System.exit(1);
      }

      // Performing a local copy in working directory so user will open
      // the new version next time they run the progrm
      File mxOld = new File("Matrixonator.jar");
      File mxNew = new File("Matrixonator-New.jar");
      File mxBkup = new File("Matrixonator-O.jar");

      if (!mxOld.exists()) {
        System.out.println("Can't find Matrixonator.jar");
        System.exit(1);
      }
      if (!mxNew.exists()) {
        System.out.println("Can't find downloaded update");
        System.exit(1);
      }

      if (mxBkup.exists()) {
        System.out.println("Backup already exists, updating backup to current version...");
        mxBkup.delete();
      }

      try {
        mxOld.renameTo(mxBkup);
        mxNew.renameTo(mxOld);

        System.out.println("Update complete. Cleaning up...");
      } catch (SecurityException e) {
        System.out.println("Something went wrong with copying of files...");
        System.exit(1);
      }

      // Just pause for a bit before reopening!
      try {
        Thread.sleep(2500);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }

      // Restart the Matrixonator program
      System.out.println("Update complete!");
      System.out.println("Starting Matrixonator...");
      try {
        Runtime.getRuntime().exec("java -jar Matrixonator.jar update");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
