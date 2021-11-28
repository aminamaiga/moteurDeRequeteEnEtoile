package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVWriter {
	String FileName;
	String header;
	String data;

	public CSVWriter(String FileName, String header, String data) {
		this.FileName = FileName;
		this.header = header;
		this.data = data;
	}

	public void write() {
		File file = new File(FileName);

		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);

			pw.write(data);
			pw.flush();
			pw.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
