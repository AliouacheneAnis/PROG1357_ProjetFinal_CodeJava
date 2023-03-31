package project;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;



public class server {

    public static void main(String[] args) throws Exception {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        

        // Bind the "/temperature" endpoint to a handler that saves the temperature data
        // to a variable
        server.createContext("/data", new DataHandler());

        // Start the server
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();

        System.out.println("Server started on port 8080");
    }

}