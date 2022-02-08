package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following:
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        (Tabs and other whitespace have been added here for clarity.)  Note the
        curly braces, square brackets, and double-quotes!  These indicate which
        values should be encoded as strings, and which values should be encoded
        as integers!  The data files which contain this CSV and JSON data are
        given in the "resources" package of this project.
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity and readability.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            //making separate string iterator for easier commands/understanding
            String[] records = iterator.next();
            
            //separating each section needed for JSONarrays (modified from
            //JSON to CSV converter)
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = new JSONObject();
            
            JSONArray column = new JSONArray();
            JSONArray row =  new JSONArray();
            JSONArray data =  new JSONArray();
            
            //for loop for iteration through CSV columns
            for(int x = 0; x < records.length; ++x)
            {
                column.add(records[x]);
            }
            
            //nested while/for loop to iterate through data and rows
            while(iterator.hasNext())
            {
                  
                    //new JSONArray to temporarily store data
                    JSONArray dataTemp;
                    dataTemp = new JSONArray();
                    
                    records = iterator.next();
                    row.add(records[0]);
                    
                for(int x = 1; x < records.length; ++x)
                {
                    //another new variable to parse records and add data
                    int recData = Integer.parseInt(records[x]);
                    dataTemp.add(recData);
                }
                data.add(dataTemp);
                    
            }
            //utilization of jsonObj function to place headings and data
            jsonObj.put("colHeaders", column);
            jsonObj.put("rowHeaders", row);
            jsonObj.put("data", data);
            
            
            //JSON tostring method for results of CSV to JSON
            results = JSONValue.toJSONString(jsonObj);
            
        }        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
            
            //parser and object from lecture notes           
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            //separating each section needed for JSONarrays
            JSONArray column = (JSONArray)jsonObject.get("colHeaders");
            JSONArray data = (JSONArray)jsonObject.get("data");
            JSONArray row = (JSONArray)jsonObject.get("rowHeaders");
            
            //for/nested if loop to go through columns according to the
            //outcome example at the top
            for(int x = 0; x < column.size(); ++x)
            {
                if(x != column.size() - 1)
                {
                    writer.append("\"" + column.get(x) + "\",");
                }
                else
                {
                    writer.append("\"" + column.get(x) + "\"" + "\n");
                }
            }
            
            //data variables required for rows step as this will act as a
            //counter to input the data properly
            int dat = 0;
            int cnt = 1;
            
            //for loop to go through rows and data according to top outcome
            //nested while/if loops inside to sort through data 
            for(int x = 0; x < row.size(); ++x)
            {
                writer.append("\"" + row.get(x) + "\",");
                
                while(dat < cnt)
                {
                    //another JSON array to temporarily store the data
                    JSONArray dataTemp = (JSONArray)data.get(dat);
                    
                    //last nested loop to parse through the temp data
                    //extremely close to the column code above
                    for(int y = 0; y < dataTemp.size(); ++y)
                    {
                        if(y != dataTemp.size() - 1)
                        {
                            writer.append("\"" + dataTemp.get(y) + "\",");
                        }
                        else
                        {
                            writer.append("\"" + dataTemp.get(y) + "\"" + "\n");
                        }
                    }
                    //data exit for while loop
                    ++dat;
                }
                //counter exit for initial for loop
                ++cnt;
            }
            
            //tostring print for results of JSON to CSV
            results = writer.toString();
        }
        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }

}