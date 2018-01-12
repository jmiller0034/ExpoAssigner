import java.io.*;
import org.json.*;

public class ExpoAssigner {
	public static void main (String[] args) {
		try {
			//File file = new File("/Users/jakemiller/Downloads/Fall EXPO 2017_ Volunteer Sign Up.csv");
			File file = new File("/Users/zachmiller/Downloads/Fall EXPO 2017_ Volunteer Sign Up.csv");
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
					//System.out.println(header[i] + ": " + data[i]);
					
				}
			//	System.out.println(jObject.toString());
				jArray.put(jObject);
				
				stringBuffer.append(line);
				stringBuffer.append("\n");
				counter++;				
			}
			fileReader.close();
		//	System.out.println("Contents of file:");
		//	System.out.println(stringBuffer.toString());
			System.out.println(counter);
			//JSONObject jo = jArray.getJSONObject(1);
			//System.out.println(jo.get("First Name"));
			assigner(jArray, "Training Time - First Choice");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public static void assigner(JSONArray responses, String header){
		JSONObject entry;
		String value;
		try {
		for(int i=0; i< responses.length(); i++) {
			entry= responses.getJSONObject(i);
			value=entry.getString(header);
			System.out.println(value);
			
		}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
} 
