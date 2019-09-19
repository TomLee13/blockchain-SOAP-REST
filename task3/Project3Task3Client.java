package project3task3client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author limingyang
 * This class represents the Project3 Task3 client
 */

// A simple class to wrap a result.
class Result {
    String value;
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}

public class Project3Task3Client {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // initialize the server
        init();
        // print the menu
        System.out.println("Block Chain Menu");
        System.out.println("1. Add a transaction to the blockchain.");
        System.out.println("2. Verify the blockchain.");
        System.out.println("3. View the blockchain.");
        System.out.println("4. Exit.");
        System.out.println("Please enter a number through 1 to 4 to perform a task.");
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        
        String operation = "";
        while (input != 4) {
            if (input == 1) {
                operation = "add";
                // add a transaction to the block chain
                System.out.println("Please a difficulty > 0.");
                int difficulty = scan.nextInt();
                scan.nextLine(); // skip the "\n"
                System.out.println("Please enter a transaction.");
                String data = scan.nextLine();
                
                long start = System.currentTimeMillis();
                add(data, difficulty);
                long end = System.currentTimeMillis();
                System.out.println("Total execution time to add this block was " + (end - start) + " milliseconds");
            } else if (input == 2) {
                operation = "verify";
                // verify the entire chain
                System.out.println("Verifying entire chain");
                long start = System.currentTimeMillis();
                System.out.println("Chain verification: " + verify(operation));
                long end = System.currentTimeMillis();
                //System.out.println(start + " " + end);
                System.out.println("Total time taken for verification: " + (end - start));
            } else if (input == 3) {
                operation = "view";
                // view the blockchain
                System.out.println("View the Blockchain");
                System.out.println(view(operation));
            } else {
                System.out.println("Wrong input");
            }
            
            System.out.println("Block Chain Menu");
            System.out.println("1. Add a transaction to the blockchain.");
            System.out.println("2. Verify the blockchain.");
            System.out.println("3. View the blockchain.");
            System.out.println("4. Exit.");
            System.out.println("Please enter a number through 1 to 4 to perform a task.");
            input = scan.nextInt();
        }
    }
    
    // Initialize the blockchain server
    public static boolean init() {
        //String operation = "init";
        int status = doPost();
        if (status == 200) {
            return true;
        } else {
            System.out.println("Error: " + status);
            System.out.println("Block chain exits. Please try other operations.");
            return false;
        }
    }
    
    // Add a block to the existing block chain
    public static boolean add(String data, int difficulty) {
        if (doPut(data, difficulty) == 200) {
            return true;
        } else {
            System.out.println("Block is not added. Please try again.");
            return false;
        }
    }
    
    // Verify the whole chain
    public static boolean verify(String operation) {
        Result r = new Result();
        if (doGet(operation, r) == 200) {
            return true;
        } else {
            return false;
        }
    }
    
    // View the whole chain
    public static String view(String operation) {
        Result r = new Result();
        int status = doGet(operation, r);
        if (status == 200) {
            return r.getValue();
        } else {
            return "Error message " + status;
        }
    }
    
    public static int doGet(String operation, Result r) {
        // Make an HTTP GET passing the name on the URL line
        
        r.setValue("");
        String response = "";
        HttpURLConnection conn;
        int status = 0;
        
        try {
            
            // pass the name on the URL line
            // there is a operation after the slash to be parsed by the server
            URL url = new URL("http://localhost:8080/Project3Task3Server/BlockChainService" + "//"+operation);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // tell the server what format we want back
            conn.setRequestProperty("Accept", "text/plain");
            
            // wait for response
            status = conn.getResponseCode();
            
            // If things went poorly, don't try to read any response, just return.
            if (status != 200) {
                // not using msg
                String msg = conn.getResponseMessage();
                return conn.getResponseCode();
            }
            String output = "";
            // things went well so let's read the response
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            
            while ((output = br.readLine()) != null) {
                response += output;
                
            }
            
            conn.disconnect();
            
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }   catch (IOException e) {
            e.printStackTrace();
        }
        
        // return value from server
        // set the response object
        r.setValue(response);
        // return HTTP status to caller
        return status;
    }
    
    public static int doPost() {
        
        int status = 0;
        String output;
        
        try {
            // Make call to a particular URL
            URL url = new URL("http://localhost:8080/Project3Task3Server/BlockChainService/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // set request method to POST and send name value pair
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // write to POST data area
            //OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            
            //out.close();
            
            // get HTTP response code sent by server
            status = conn.getResponseCode();
            
            //close the connection
            conn.disconnect();
        }
        // handle exceptions
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // return HTTP status
        return status;
    }
    
    // Low level routine to make an HTTP PUT request
    // Note, PUT does not use the URL line for its message to the server
    public static int doPut(String data, int difficulty) {
        
        
        int status = 0;
        try {
            URL url = new URL("http://localhost:8080/Project3Task3Server/BlockChainService/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(data + "," + difficulty);
            out.close();
            status = conn.getResponseCode();
            
            conn.disconnect();
            
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }
}
