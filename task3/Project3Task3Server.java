package task3server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author limingyang
 * This class represents the Project3 Task3 server
 */
@WebServlet(name = "Project3Task3Server", urlPatterns = {"/BlockChainService/*"})
public class Project3Task3Server extends HttpServlet {
    BlockChain bc = new BlockChain();
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Console: doGET visited");
        
        String result = "";
        
        // The operation is on the path /operation so skip over the '/'
        String operation = (request.getPathInfo()).substring(1);
        
        // if operation gotten is "verify"
        if(operation.equals("verify")) {
            boolean isValid = bc.isChainValid();
            if (isValid) {
                result = "true";
            } else {
                result = "false";
            }
            System.out.println(result);
            // return the value from a GET request
            PrintWriter out = response.getWriter();
            out.println(result);
            
            response.setStatus(200);
            //return;
        } else if (operation.equals("view")) { // if the operation gotten is "view"
            result = bc.toString();
            // return the value from a GET request
            PrintWriter out = response.getWriter();
            out.println(result);
            
            response.setStatus(200);
            //return;
        } else {
            response.setStatus(401);
            //return;
        }
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Console: doGET visited");
        
        // initialze the blockchain held in this server
             
        System.out.println("initialization hit");
        if (!bc.isEmpty()) { // if the chain is not empty
            response.setStatus(401);
        } else {
            Block genesisBlock = new Block(0, new Timestamp(System.currentTimeMillis()), "Genensis", 2);
            bc.addBlock(genesisBlock);
            response.setStatus(200);
        }       
    }
    
    /* In this example, we use PUT to add a new block to an existing blockchain.  */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Console: doPut visited");
        // Read what the client has placed in the PUT data area
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String in = br.readLine();
        String[] inArr = in.split(",");
        
        // extract data field from request data
        String data = inArr[0];
        
        // extract difficulty field from request data       
        String difficultyStr = inArr[1];
        
        int index = bc.getLatestBlock().getIndex() + 1;       
        
        if(data.equals("") || difficultyStr.equals("")) {
            // missing input return 401
            response.setStatus(401);
            return;
        } else {
            int difficulty = Integer.parseInt(difficultyStr);
            Block newBlock = new Block(index, new Timestamp(System.currentTimeMillis()), data, difficulty);
            bc.addBlock(newBlock);
        }
    }

}
