import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.*;

public class ExpoAssigner {
	
	private static Properties props;
	
	public static void main (String[] args) {
		try {
			init();
			File file = new File(props.getProperty("inputFilePath"));
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			int counter = 0;
			line = bufferedReader.readLine();
			String[] header = line.split(",");
			int headerCount = header.length;
			String[] data;
			JSONArray jArray = new JSONArray();
			JSONObject jObject = new JSONObject();
			String headerName;
			String dataEntry;
			while ((line = bufferedReader.readLine()) != null) {
				data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				jObject = new JSONObject();
				for (int i = 0; i < headerCount; i++) {
					headerName = header[i].replaceAll("\"", "");
					dataEntry = data[i].replaceAll("\"", "");
					if (!headerName.isEmpty() && !dataEntry.isEmpty()) {
						jObject.put(headerName, dataEntry);
					}
					
				}
				jArray.put(jObject);
				
				stringBuffer.append(line);
				stringBuffer.append("\n");
				counter++;				
			}
			fileReader.close();
			System.out.println(counter);
			assigner(jArray, "Training Time - First Choice");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public static void assigner(JSONArray responses, String header){
		JSONObject entry;
		String value;
		List<String> outputString = new ArrayList<String>();
		List<String> dataForCsvLine;
		outputString.add("\"Name\",\"Email\",\"First choice\"\n");
		System.out.println(outputString.get(0));
		try {
			for(int i=0; i< responses.length(); i++) {
				entry= responses.getJSONObject(i);
				value=entry.getString(header);
				dataForCsvLine = new ArrayList<String>();
				dataForCsvLine.add(entry.getString("First Name") + " " + entry.getString("Last Name"));
				dataForCsvLine.add(entry.getString("Email"));
				dataForCsvLine.add(value);
				System.out.println(csvWriterHelper(dataForCsvLine));
				outputString.add(csvWriterHelper(dataForCsvLine));
				
			}
			csvWriter(outputString);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public static String csvWriterHelper(List<String> inputValue) {
		// input - data to display in a line
		// output - csv line 
		String csvLine = "";
		
		for (String value : inputValue) {
			if (csvLine.length() != 0) {
				csvLine += ",";
			}
			csvLine += "\"" +  value + "\"";
		}
		csvLine += "\n";
		
		return csvLine;
	}
	
	public static void csvWriter (List<String> inputLines){
		String filename = props.getProperty("outputFilePath");
		FileWriter fw = null;
		try {
		fw = new FileWriter(filename, true);
		BufferedWriter vw = new BufferedWriter(fw);
		for (String line : inputLines) {
			vw.write(line);
		}
		vw.close();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
					
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static void init() {
		try {
			props = new Properties();
			InputStream is = new FileInputStream(new File(".").getCanonicalPath() + "/props/AssignerProperties.properties");
			props.load(is);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
} 
