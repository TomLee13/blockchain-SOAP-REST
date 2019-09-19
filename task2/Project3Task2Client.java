/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2client;

import java.util.Scanner;

/**
 *
 * @author limingyang
 */
public class Project3Task2Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // initialize the server
        initialize();
        // print the menu to the user
        System.out.println("Block Chain Menu");
        System.out.println("1. Add a transaction to the blockchain.");
        System.out.println("2. Verify the blockchain.");
        System.out.println("3. View the blockchain.");
        System.out.println("4. Exit.");
        System.out.println("Please enter a number through 1 to 4 to perform a task.");
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        
        // message to be sent
        String message = "";
        
        while (input != 4) {
            if (input == 1) {
                // add a transaction to the block chain
                System.out.println("Please a difficulty > 0.");
                int difficulty = scan.nextInt();
                scan.nextLine(); // skip the "\n"
                System.out.println("Please enter a transaction.");
                String data = scan.nextLine();
                // build the csv message
                message = "add," + data + "," + String.valueOf(difficulty);
                long start = System.currentTimeMillis();               
                receiveMessage(message);
                long end = System.currentTimeMillis();
                System.out.println("Total execution time to add this block was " + (end - start) + " milliseconds");
            } else if (input == 2) {
                // verify the entire chain
                System.out.println("Verifying entire chain");
                // build the csv message
                message = "verify";
                long start = System.currentTimeMillis();
                System.out.println("Chain verification: " + receiveMessage(message));
                long end = System.currentTimeMillis();
                //System.out.println(start + " " + end);
                System.out.println("Total time taken for verification: " + (end - start));
            } else if (input == 3) {
                // view the blockchain
                System.out.println("View the Blockchain");
                // build the csv message
                message = "view";
                System.out.println(receiveMessage(message));
            } else {
                System.out.println("Wrong input");
            }
            // print the menu to the user
            System.out.println("Block Chain Menu");
            System.out.println("1. Add a transaction to the blockchain.");
            System.out.println("2. Verify the blockchain.");
            System.out.println("3. View the blockchain.");
            System.out.println("4. Exit.");
            System.out.println("Please enter a number through 1 to 4 to perform a task.");
            // ask for another input
            input = scan.nextInt();
        }
    }

    private static boolean initialize() {
        project3task2client.BlockChainService service = new project3task2client.BlockChainService();
        project3task2client.Project3Task2Server port = service.getProject3Task2ServerPort();
        return port.initialize();
    }

    private static String receiveMessage(java.lang.String message) {
        project3task2client.BlockChainService service = new project3task2client.BlockChainService();
        project3task2client.Project3Task2Server port = service.getProject3Task2ServerPort();
        return port.receiveMessage(message);
    }
    
}
