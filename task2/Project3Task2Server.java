package task2;

import java.sql.Timestamp;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author limingyang
 */
@WebService(serviceName = "BlockChainService")
public class Project3Task2Server {
    BlockChain bc = new BlockChain();
    
    /**
     * Web service operation
     * Initialize the block chain held by the server
     */
    @WebMethod(operationName = "initialize")
    public boolean initialize() {
        System.out.println("initialization hit");
        if (!bc.isEmpty()) {
            bc = new BlockChain();
        }
        Block genesisBlock = new Block(0, bc.getTime(), "Genensis", 2);
        bc.addBlock(genesisBlock);
        return true;
    }
    
    /**
     * Web service operation
     * @param message
     * @return 
     */
    @WebMethod(operationName = "receiveMessage")
    public String receiveMessage(@WebParam(name = "message") String message) {
        String[] received = message.split(",");
        if (received.length == 1) {
            if (received[0].equals("verify")) {
                boolean isValid = verify();
                if (isValid) {
                    return "true";
                } else {
                    return "false";
                }
            } else if (received[0].equals("view")) {
                return view();
            }
        } else if (received.length == 3) {
            addATransaction(received[1], Integer.parseInt(received[2]));
            return "Block added.";
        } else {
            return "Wrong message.";
        }
        return "Wrong message.";
    }
    
    // add a transaction to the blockchain
    private void addATransaction(String data, int difficulty) {
        // get the index for the new block
        int id = bc.getLatestBlock().getIndex() + 1;
        // add the block
        Block newBlock = new Block(id, bc.getTime(), data, difficulty);
        //long start = System.currentTimeMillis();
        bc.addBlock(newBlock);
    }
    
    // verify the chain
    private boolean verify() {
        return bc.isChainValid();
    }
    
    // view the chain
    private String view() {
        return bc.toString();
    }  
}
