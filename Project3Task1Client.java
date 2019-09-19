package project3task1client;

import java.util.Scanner;

/**
 *
 * @author limingyang
 * This class representing the Project 3 Task 1 client side
 */
public class Project3Task1Client {
    
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
        
        while (input != 4) {
            if (input == 1) {
                // add a transaction to the block chain
                System.out.println("Please a difficulty > 0.");
                int difficulty = scan.nextInt();
                scan.nextLine(); // skip the "\n"
                System.out.println("Please enter a transaction.");
                String data = scan.nextLine();
                long start = System.currentTimeMillis();
                addATransaction(data, difficulty);
                long end = System.currentTimeMillis();
                System.out.println("Total execution time to add this block was " + (end - start) + " milliseconds");
            } else if (input == 2) {
                // verify the entire chain
                System.out.println("Verifying entire chain");
                long start = System.currentTimeMillis();
                System.out.println("Chain verification: " + verifyChain());
                long end = System.currentTimeMillis();
                //System.out.println(start + " " + end);
                System.out.println("Total time taken for verification: " + (end - start));
            } else if (input == 3) {
                // view the blockchain
                System.out.println("View the Blockchain");
                System.out.println(viewChain());
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
        edu.cmu.andrew.mingyan2.Project3Task1Service_Service service = new edu.cmu.andrew.mingyan2.Project3Task1Service_Service();
        edu.cmu.andrew.mingyan2.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.initialize();
    }
    
    private static boolean addATransaction(java.lang.String data, int difficulty) {
        edu.cmu.andrew.mingyan2.Project3Task1Service_Service service = new edu.cmu.andrew.mingyan2.Project3Task1Service_Service();
        edu.cmu.andrew.mingyan2.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.addATransaction(data, difficulty);
    }
    
    private static boolean verifyChain() {
        edu.cmu.andrew.mingyan2.Project3Task1Service_Service service = new edu.cmu.andrew.mingyan2.Project3Task1Service_Service();
        edu.cmu.andrew.mingyan2.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.verifyChain();
    }
    
    private static String viewChain() {
        edu.cmu.andrew.mingyan2.Project3Task1Service_Service service = new edu.cmu.andrew.mingyan2.Project3Task1Service_Service();
        edu.cmu.andrew.mingyan2.Project3Task1Service port = service.getProject3Task1ServicePort();
        return port.viewChain();
    }
    
}
