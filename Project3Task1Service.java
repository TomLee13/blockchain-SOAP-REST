package edu.cmu.andrew.mingyan2;

import java.sql.Timestamp;
import java.util.ArrayList;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author limingyang
 * This class is the server side code for Project3 Task1
 * This class hold a BlockChain object
 */
@WebService(serviceName = "Project3Task1Service")
public class Project3Task1Service {
    // A BlockChain object held by the server
    BlockChain bc =  new BlockChain();
    
    /**
     * Web service operation
     * Initialize the block chain by adding a genesis block to the chain
     * This method must be run before any other method
     */
    @WebMethod(operationName = "initialize")
    public boolean initialize() {
        System.out.println("initialization hit");
        if (!bc.isEmpty()) {
            bc = new BlockChain();
        }
        Block genesisBlock = new Block(0, new Timestamp(System.currentTimeMillis()), "Genensis", 2);
        bc.addBlock(genesisBlock);
        return true;
    }
    
    /**
     * Web service operation
     * Add a transaction block to the block chain
     * @param data user input of transaction information
     * @param difficulty user input of difficulty
     */
    @WebMethod(operationName = "addATransaction")
    public boolean addATransaction(@WebParam(name = "data") String data, @WebParam(name = "difficulty") int difficulty) {
        // get the index for the new block
        int id = bc.getLatestBlock().getIndex() + 1;
        // add the block
        Block newBlock = new Block(id, new Timestamp(System.currentTimeMillis()), data, difficulty);
        //long start = System.currentTimeMillis();
        bc.addBlock(newBlock);
        //long end = System.currentTimeMillis();
        return true;
    }
    
    /**
     * Web service operation
     * Verify the whole block chain
     * @return a boolean value representing if the chain is valid
     */
    @WebMethod(operationName = "verifyChain")
    public boolean verifyChain() {
        // verify the chain
        return bc.isChainValid();
    }
    
    /**
     * Web service operation
     * Allow the user to view the whole chain
     * @return a String representing the whole chain
     */
    @WebMethod(operationName = "viewChain")
    public String viewChain() {
        return bc.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
}
