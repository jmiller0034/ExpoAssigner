import java.io.*;
import org.json.*;

public class ExpoAssigner {
	public static void main (String[] args) {
		try {
			File file = new File("/Users/jakemiller/Downloads/Fall EXPO 2017_ Volunteer Sign Up.csv");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			int counter = 0;
			line = bufferedReader.readLine();
			String[] header = line.split(",");
			int headerCount = header.length;
			String[] data;
			while ((line = bufferedReader.readLine()) != null) {
				data = line.split("\",");
				for (int i = 0; i < headerCount; i++) {
					System.out.println(header[i] + ": " + data[i]);
				}
				
				
				stringBuffer.append(line);
				stringBuffer.append("\n");
				counter++;				
			}
			fileReader.close();
		//	System.out.println("Contents of file:");
		//	System.out.println(stringBuffer.toString());
			System.out.println(counter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
