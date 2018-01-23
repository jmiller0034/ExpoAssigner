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
			JSONArray forMethod = jArray;
			assigner(forMethod, "Training Time");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public static void assigner(JSONArray responses, String header){
		JSONObject entry;
		JSONObject daysWithSpotsAvailable = new JSONObject();
		String firstChoice = " - First Choice";
		String secondChoice = " - Second Choice";
		String value;
		String value2;
		String day;
		int cap;
		List<String> outputString = new ArrayList<String>();
		List<String> dataForCsvLine;
		outputString.add("\"Name\",\"Email\",\"First choice\"\n");
		System.out.println(outputString.get(0));
		try {
			for (int j = 0; j < EXPOCONSTANTS.TRAINING_DAYS.length; j++) {
				cap = EXPOCONSTANTS.MAX_TRAINEES_PER_DAY;
				day = EXPOCONSTANTS.TRAINING_DAYS[j];
				for(int i=0; i< responses.length(); i++) {
					entry= responses.getJSONObject(i);
					value=entry.getString(header + firstChoice);
					value2 = entry.getString(header + secondChoice);
					dataForCsvLine = new ArrayList<String>();
					if (value.contains(day)) {
						if (cap > 0) {
							dataForCsvLine.add(entry.getString("First Name") + " " + entry.getString("Last Name"));
							dataForCsvLine.add(entry.getString("Email"));
							dataForCsvLine.add(value);
							--cap;
							System.out.println(csvWriterHelper(dataForCsvLine));
							outputString.add(csvWriterHelper(dataForCsvLine));
						}  else {
							entry.put("missedFirst", true);
							System.out.println("cannot assign " + entry.getString("First Name") + " " + entry.getString("Last Name")  + " " + day);
						}
					} else if (value2.contains(day) && entry.has("missedFirst")) {
						dataForCsvLine.add(entry.getString("First Name") + " " + entry.getString("Last Name"));
						dataForCsvLine.add(entry.getString("Email"));
						dataForCsvLine.add(value2);
						--cap;
						entry.remove("missedFirst");
						System.out.println(csvWriterHelper(dataForCsvLine));
						outputString.add(csvWriterHelper(dataForCsvLine));						
					}
				}
				if (cap != 0) {
					daysWithSpotsAvailable.put(day, cap);
				}
			}
			
			// JSONArray missedFirstChoice = missedFirstChoice();
			// assignRemainingDay(missedFirstChoice, daysWithSpotsAvailable, header, dataForCsvLine) 
			// call missedFirstChoice again
			// do stuff with entries that have not been assigned
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
		fw = new FileWriter(filename, false);
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
	
	public static JSONArray missedFirstChoice(JSONArray inputArray) throws Exception {
		JSONArray missedFirstChoiceArray = new JSONArray();
		JSONObject entry;
		for (int i = 0; i < inputArray.length(); i++) {
			entry = inputArray.getJSONObject(i);
			if (entry.has("missedFirst")) {
				missedFirstChoiceArray.put(entry);
			}
		}
		
		return missedFirstChoiceArray;
	}
	
	
	public static void assignRemainingDay (JSONArray remainingVolunteers, JSONObject remainingDays, String header,
			List<String> dataForCsv) throws Exception {
		String day;
		int cap;
		JSONObject entry;
		String value;
		
		for (int i = 0; i < EXPOCONSTANTS.TRAINING_DAYS.length; i++) {
			day = EXPOCONSTANTS.TRAINING_DAYS[i];
			if (remainingDays.has(day)) {
				cap = remainingDays.getInt(day);
				for (int j = 0; j < remainingVolunteers.length(); j++) {
					entry = remainingVolunteers.getJSONObject(j);
					value = entry.getString(header);
					if (value.contains(day) && cap > 0) {
						//add to dataForCsv
						--cap;
						entry.remove("missedFirst");
					}
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