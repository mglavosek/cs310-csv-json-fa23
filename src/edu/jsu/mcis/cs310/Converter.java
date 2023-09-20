package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.*;
import java.util.*;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "";
                      
        try {
        // Create JSON objects
            JsonObject jsonObject = new JsonObject();
            JsonArray prodNumsArray = new JsonArray();
            JsonArray colHeadingsArray = new JsonArray();
            JsonArray dataArray = new JsonArray();

            // Create a CSV reader
            CSVReader csvReader = new CSVReader(new StringReader(csvString));
            List<String[]> csvRows = csvReader.readAll();

            // Get headers from the first row
            String[] headers = csvRows.get(0);

            for (String header : headers) {
                colHeadingsArray.add(header);
            }

            // Loop through the rows and convert them to JSON arrays
            for (int i = 1; i < csvRows.size(); i++) {
                String[] row = csvRows.get(i);
                JsonArray rowDataArray = new JsonArray();

                for (String cell : row) {
                    rowDataArray.add(cell);
                }

                dataArray.add(rowDataArray);
            }

            jsonObject.put("ProdNums", prodNumsArray);
            jsonObject.put("ColHeadings", colHeadingsArray);
            jsonObject.put("Data", dataArray);

            // Serialize the JSON object to a string
            return jsonObject.toJson();
        
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        var result = "";
        
        try {
        
            // Create a JSON object from the input string
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(jsonString);

            // Extract column headings
            JsonArray colHeadingsArray = (JsonArray) jsonObject.get("ColHeadings");
            String[] headers = new String[colHeadingsArray.size()];
            for (int i = 0; i < colHeadingsArray.size(); i++) {
                headers[i] = (String) colHeadingsArray.get(i);
            }

            // Initialize a CSV writer
            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);

            // Write the headers to the CSV
            csvWriter.writeNext(headers);

            // Extract data rows and write them to the CSV
            JsonArray dataArray = (JsonArray) jsonObject.get("Data");
            for (Object row : dataArray) {
                JsonArray rowDataArray = (JsonArray) row;
                String[] csvRow = new String[rowDataArray.size()];
                for (int i = 0; i < rowDataArray.size(); i++) {
                    csvRow[i] = rowDataArray.get(i).toString(); 
                }
                csvWriter.writeNext(csvRow);
            }

            // Close the CSV writer
            csvWriter.close();

            // Get the CSV content from the StringWriter
            return stringWriter.toString(); 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
        return result;
        
    }
    
}
