package edu.cmu.andrew.mingyan2;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author limingyang
 * This class represents a simple block chain
 */
public class BlockChain {
    private List<Block> blockchain; // an ArrayList of Blocks
    private String chainHash; // a chain hash to hold SHA256 hash of the most recently added block
    
    // constructor
    public BlockChain() {
        blockchain = new ArrayList<>();
        chainHash = "";
    }
    
    // check if the blockchain is empty
    public boolean isEmpty() {
        return blockchain.isEmpty();
    }
    
    // A method that add a new Block to the BlockChain
    public void addBlock(Block newBlock) {
        // add the newBlock to the blockchain
        blockchain.add(newBlock);
        // set the hash pointer of the newBlock
        newBlock.setPreviousHash(chainHash);
        // update the chain hash
        chainHash = newBlock.proofOfWork();
    }
    
    // the size of the chain in blocks
    public int getChainSize() {
        return blockchain.size();
    }
    
    // return a reference of the latest added block
    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }
    
    // return a reference of the nth block in the blockchain
    public Block getNthBlock(int index) {
        return blockchain.get(index);
    }
    
    // the current system time
    public Timestamp getTime() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    // hashes per second of the computer holding this chain
    public int hashesPerSecond() {
        long startTime = System.currentTimeMillis();
        int count = 0;
        while (System.currentTimeMillis() - startTime <= 1000) {
            // compute hash for the latest added block
            getLatestBlock().calculateHash();
            // increment the count by 1
            count++;
        }
        return count;
    }
    
    // Check if the chain is valid
    // There are 3 checks to make
    // 1. check every block's leftmost 0's for the hash of the block
    // 2. check the hash pointer
    // 3. check the chain hash
    public boolean isChainValid() {
        boolean isValid = false;
        if (getChainSize() == 1) { // if there is only the Genesis block
            boolean proofOfWork = false;
            boolean chainHashValid = false;
            Block genesis = blockchain.get(0);
            // comput the hash of the Genesis block
            String hash = genesis.calculateHash();
            // check the leading zeros
            int count = 0;
            for (int i = 0; i < hash.length(); i++) {
                if (hash.charAt(i) == '0') {
                    count++;
                } else {
                    break;
                }
            }
            if (count == genesis.getDifficulty()) {
                proofOfWork = true;
            } else {
                System.out.print("...Improper hash on node 0 Does not begin with ");
                for (int k = 0; k < genesis.getDifficulty(); k++) {
                    System.out.print(0);
                }
                System.out.println();
            }
            // check the chainhash
            if (hash.equals(chainHash)) {
                chainHashValid = true;
            } else {
                System.out.println("chainHash incorrect.");
            }
            // set the isValid true if both the boolean values are true
            if (proofOfWork && chainHashValid) {
                isValid = true;
            }
        } else { // if the chain has more blocks than one
            boolean rightHashPointer = true;
            boolean proofOfWork = false;
            boolean chainHashValid = false;
            // verify the first node
            Block genesis = blockchain.get(0);
            // comput the hash of the Genesis block
            String hash = genesis.calculateHash();
            // check the leading zeros
            int count = 0;
            for (int i = 0; i < hash.length(); i++) {
                if (hash.charAt(i) == '0') {
                    count++;
                } else {
                    break;
                }
            }
            if (count != genesis.getDifficulty()) {
                System.out.print("...Improper hash on node 0 Does not begin with ");
                for (int k = 0; k < genesis.getDifficulty(); k++) {
                    System.out.print(0);
                }
                System.out.println();
                return false;
            }
            
            // iterate through the whole blockchain from block 1
            for (int i = 1; i < blockchain.size(); i++) {
                Block currentBlock = blockchain.get(i);
                Block previousBlock = blockchain.get(i - 1);
                // check the hashpointer
                // if the hashpointer is correct
                if (currentBlock.getPreviousHash().equals(previousBlock.calculateHash())) {
                    count = 0;
                    hash = currentBlock.calculateHash();
                    // find the leading zeros
                    for (int j = 0; j < hash.length(); j++) {
                        if (hash.charAt(j) == '0') {
                            count++;
                        } else {
                            break;
                        }
                    }
                    // check the proof of work
                    if (count != currentBlock.getDifficulty()) {
                        System.out.print("...Improper hash on node " + i + " Does not begin with ");
                        for (int k = 0; k < currentBlock.getDifficulty(); k++) {
                            System.out.print(0);
                        }
                        System.out.println();
                        return false;
                    }
                    
                } else { // if the hashpointer is not correct
                    System.out.println("Hashpointer from node " + i + " to node " + (i - 1) + " incorrect");
                    rightHashPointer = false;
                    return false;
                }
            }
            proofOfWork = true;
            // after checking the blocks one by one, check the chain hash
            if (blockchain.get(blockchain.size() - 1).calculateHash().equals(chainHash)) {
                chainHashValid = true;
            }
            // set the isValid true if all the boolean values are true
            if (rightHashPointer && proofOfWork && chainHashValid) {
                isValid = true;
            }
        }
        return isValid;
    }
    
    // repair the block chain
    public void repairChain() {
        // repair each block
        for (int i = 0; i < blockchain.size(); i++) {
            Block current = blockchain.get(i);
            String hash = current.calculateHash();
            // find leading zeros
            int count = 0;
            for (int j = 0; j < hash.length(); j++) {
                if (hash.charAt(j) == '0') {
                    count++;
                } else {
                    break;
                }
            }
            if (count != current.getDifficulty()) {
                // calculate the correct hash given the difficulty
                hash = current.proofOfWork();
                // re-link the blocks by setting the hashpointer of the next block
                if (i < blockchain.size() - 1 && (i + 1) <= blockchain.size()) {
                    blockchain.get(i + 1).setPreviousHash(hash);
                }
            }
        }
        // repair the chainHash
        chainHash = blockchain.get(blockchain.size() - 1).calculateHash();
    }
    
    // clear the blockchain
    public void clear() {
        blockchain.clear();
    } 
    
    @Override
    public String toString() {
        // A StringBuilder to build the whole blockchain
        StringBuilder sb = new StringBuilder();
        
        String head = "{\"ds_chain\":[";
        String chainHashStr = "\"chainHash\":";
        String tail = "\"}";
        sb.append(head);
        
        // Iterate the whole chain
        for (int i = 0; i < blockchain.size(); i++) {
            sb.append(blockchain.get(i).toString());
            if (i != blockchain.size() - 1) {
                sb.append(",");
            }
        }
        // append the chainHash and the tail
        sb.append("],").append(chainHashStr).append("\"").append(chainHash).append(tail);
        
        return sb.toString();
    }
    
    // A menu driven test driver
    public static void main(String[] args) {
        BlockChain bc = new BlockChain();
        // add the genesis block to the blockchain
        Block genesisBlock = new Block(0, new Timestamp(System.currentTimeMillis()), "Genensis", 2);
        bc.addBlock(genesisBlock);
        // Print out the menu
        System.out.println("Block Chain Menu");
        System.out.println("0. View basic blockchain status.");
        System.out.println("1. Add a transaction to the blockchain.");
        System.out.println("2. Verify the blockchain.");
        System.out.println("3. View the blockchain.");
        System.out.println("4. Corrupt the chain.");
        System.out.println("5. Hide the corruption by repairing the chain.");
        System.out.println("6. Exit.");
        System.out.println("Please enter a number through 0 to 6 to perform a task.");
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        
        while (input != 6) {
            // if statements with the user input
            if (input == 0) {
                // print the basic status of the block chain
                System.out.println("Current size of chain: " + bc.getChainSize());
                System.out.println("Current hashes per second by this machine: " + bc.hashesPerSecond());
                System.out.println("Difficulty of most recent block: " + bc.getLatestBlock().getDifficulty());
                System.out.println("Nonce for most recent block: " + bc.getLatestBlock().getNonce());
                System.out.println("Chain hash: " + bc.getLatestBlock().calculateHash());
            } else if (input == 1) {
                // add a transaction to the block chain
                System.out.println("Please a difficulty > 0.");
                int difficulty = scan.nextInt();
                scan.nextLine(); // skip the "\n"
                System.out.println("Please enter a transaction.");
                String data = scan.nextLine();
                // get the index of the new block
                int index = bc.getLatestBlock().getIndex() + 1;
                Block newBlock = new Block(index, new Timestamp(System.currentTimeMillis()), data, difficulty);
                // add the block
                long start = System.currentTimeMillis();
                bc.addBlock(newBlock);
                long end = System.currentTimeMillis();
                System.out.println("Total execution time to add this block was " + (end - start) + " milliseconds");
                //System.out.println(bc.getLatestBlock().toString());
            } else if (input == 2) {
                // verify the entire chain
                System.out.println("Verifying entire chain");
                long start = System.currentTimeMillis();
                System.out.println("Chain verification: " + bc.isChainValid());
                long end = System.currentTimeMillis();
                //System.out.println(start + " " + end);
                System.out.println("Total time taken for verification: " + (end - start));
            } else if (input == 3) {
                // view the blockchain
                System.out.println("View the Blockchain");
                System.out.println(bc.toString());
            } else if (input == 4) {
                // corrupt the chain
                // ask for an input for a block index
                System.out.println("Enter block ID of block to Corrupt");
                int id = scan.nextInt();
                scan.nextLine();
                
                // test
               // System.out.println(bc.getNthBlock(id).toString());
                
                System.out.println("Enter new data for block " + id);
                String data = scan.nextLine();
                // make the corruption
                bc.getNthBlock(id).setData(data);
                
                //test
                //System.out.println(bc.getNthBlock(id).toString());
                
                // print the result
                System.out.println("Block " + id + " now holds " + data);
            } else if (input == 5) {
                // repair the chain
                System.out.println("Repairing the entire chain");
                long start = System.currentTimeMillis();
                bc.repairChain();
                long end = System.currentTimeMillis();
                System.out.println("Total execution time required to repair the chain was " + (end - start) + " milliseconds");
            } else if (input == 6) {
                // Exit the program
            } else {
                System.out.println("Wrong input.");
            }
            // print the menu to the user again
            System.out.println("Block Chain Menu");
            System.out.println("0. View basic blockchain status.");
            System.out.println("1. Add a transaction to the blockchain.");
            System.out.println("2. Verify the blockchain.");
            System.out.println("3. View the blockchain.");
            System.out.println("4. Corrupt the chain.");
            System.out.println("5. Hide the corruption by repairing the chain.");
            System.out.println("6. Exit.");
            System.out.println("Please enter a number through 0 to 6 to perform a task.");
            input = scan.nextInt();
        }
        
        //System.out.println(bc.toString());
    }
}
