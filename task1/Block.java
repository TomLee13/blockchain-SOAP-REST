package edu.cmu.andrew.mingyan2;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

/**
 *
 * @author limingyang
 * This class is represents a simple block object
 */
public class Block {
    // Invariants of the Block class
    private int index; // the position of the block on the chain
    private Timestamp timestamp; // the time of the block's creation
    private String data; // the block's single transaction details
    private String previousHash; // a hash pointer, the SHA256 hash of a block's parent
    private BigInteger nonce; // a BigInteger value determined by a proof of work routine
    private int difficulty; // this specifies the exact number of left most hex digits needed by a proper hash
    
    // constructor
    public Block(int index, Timestamp timestamp, String data, int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        previousHash = "";
        nonce = BigInteger.ZERO;
    }
    
    // This method computes a hash of the concatenation of 
    // the index, timestamp, data, previousHash, nonce, and difficulty.
    // Here we will compute the SHA-256 hash
    public String calculateHash() {
        String indexStr = String.valueOf(index);
        String time = timestamp.toString();
        String nonceStr = nonce.toString();
        String diffStr = String.valueOf(difficulty);
        String blockStr = indexStr + time + data + previousHash + nonceStr + diffStr;
        
        return ComputeSHA_256_as_Hex_String(blockStr);
    }
    
    // This helper method takes a String input and compute its SHA-256 hash value  
    // copied from BabyHash.java
    private String ComputeSHA_256_as_Hex_String(String text) { 
    
        try { 
             // Create a SHA256 digest
             MessageDigest digest;
             digest = MessageDigest.getInstance("SHA-256");
             // allocate room for the result of the hash
             byte[] hashBytes;
             // perform the hash
             digest.update(text.getBytes("UTF-8"), 0, text.length());
             // collect result
             hashBytes = digest.digest();
             return convertToHex(hashBytes);
        }
        catch (NoSuchAlgorithmException nsa) {
            System.out.println("No such algorithm exception thrown " + nsa);
        }
        catch (UnsupportedEncodingException uee ) {
            System.out.println("Unsupported encoding exception thrown " + uee);
        }
        return null;
    } 
    
    // code from Stack overflow
    // converts a byte array to a string.
    // each nibble (4 bits) of the byte array is represented 
    // by a hex characer (0,1,2,3,...,9,a,b,c,d,e,f)
    private String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    }
    
    // getter method
    public String getData() {
        return data;
    }
    
    // getter method
    public int getDifficulty() {
        return difficulty;
    }
    
    // getter method
    public int getIndex() {
        return index;
    }
    
    // getter method
    public BigInteger getNonce() {
        return nonce;
    }
    
    // getter method
    public String getPreviousHash() {
        return previousHash;
    }
    
    // getter method
    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    // The proof of work methods finds a good hash. 
    // It increments the nonce until it produces a good hash.
    public String proofOfWork() {
        boolean goodHash = false;
        String hash = "";
        int count = 0;
        while (!goodHash) {
            //System.out.println("Operation " + count);
            hash = calculateHash();
            //System.out.println(hash);
            int zeroCount = findLeadingZeros(hash);
            if (zeroCount == difficulty) {
                goodHash = true;
            } else {
                nonce = nonce.add(BigInteger.ONE);
            }
            //System.out.println("good hash? " + goodHash);
            //System.out.println("nonce now: " + nonce);
            count++;
        }
        return hash;
    }
    
    // helper method
    // find the leading zeros of a hash String
    private int findLeadingZeros(String hash) {
        int count = 0;
        for (int i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) == '0') { 
                count++;
            } else {
                break;
            }
        }
        return count;
    }
    
    // setter method
    public void setIndex(int index) {
        this.index = index;
    }
    
    // setter method
    public void setData(String data) {
        this.data = data;
    }
    
    // setter method
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    
    // setter method
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
    
    // setter method
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    // override the java toString() method
    @Override
    public String toString() {
        //JSONObject json = new JSONObject();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"index\" : ").append(index)
                .append(",\"time stamp \" : \"").append(timestamp.toString()).append("\"")
                .append(",\"Tx \" : \"").append(data).append("\"")
                .append(",\"PrevHash\" : \"").append(previousHash).append("\"")
                .append(",\"nonce\" : ").append(nonce)
                .append(",\"difficulty\" : ").append(difficulty).append("}");
        
        return sb.toString();
    }
    
    // the main method
    public static void main(String[] args) {
        Block genesisBlock = new Block(0, new Timestamp(System.currentTimeMillis()), "Genensis", 2);
        System.out.println(genesisBlock.toString());
        System.out.println(genesisBlock.proofOfWork());
    }
    
}
