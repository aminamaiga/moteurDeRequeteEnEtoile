package helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FilesManager {
	public FileWriter myWriter;

	public FilesManager() {
	}
	
	public FilesManager(FileWriter myWriter) {
		this.myWriter = myWriter;
	}
	
	
	public FileWriter getMyWriter() {
		return myWriter;
	}

	public void setMyWriter(FileWriter myWriter) {
		this.myWriter = myWriter;
	}

	public File createFile(String filename) {
		try {
			File myObj = new File(filename);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
				System.out.println("File created: " + myObj.getName());
			}
			return myObj;
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return null;
	}

	public void writeInfile(String text) {
		try {
			myWriter.append("oll");
	        myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	// read all queryset files from specific folder
		public static ArrayList<File> listJavaFilesForFolder(final File folder) {
			ArrayList<File> javaFiles = new ArrayList<File>();
			for (File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					javaFiles.addAll(listJavaFilesForFolder(fileEntry));
				} else if (fileEntry.getName().contains(".queryset")) {
					javaFiles.add(fileEntry);
				}
			}

			return javaFiles;
		}
}
