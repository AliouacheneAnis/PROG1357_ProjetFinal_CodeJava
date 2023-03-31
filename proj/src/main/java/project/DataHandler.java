package project;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.faunadb.client.query.Language.*;
import com.faunadb.client.FaunaClient;

public class DataHandler implements HttpHandler {
    private int WaterLevel;
    private String Quality; 

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            // Read the request body as JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            String requestBody = reader.readLine();
            reader.close();

            // Parse the JSON data into a Map
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {
            });

            // Get the temperature value from the Map and save it to the variable
            if (jsonMap.containsKey("data")) {
                try {

                    WaterLevel = Integer.parseInt(jsonMap.get("data").toString());
                    System.out.println("Received data: " + WaterLevel);

                    if (WaterLevel >= 5) {
                      Quality = "Good"  ; 
                    }else{
                        Quality = "NOT GOOD";
                    }

                    FaunaClient client = FaunaClient.builder()
                            .withSecret("fnAE_mGgjoACTTxwwwhPkM7akuitNGHl-aZHIRMV")
                            .withEndpoint("https://db.fauna.com/")
                            .build();

                    System.out.println(
                            client.query(
                                    Create(
                                            Collection("water"),
                                            Obj(
                                                    "data", Obj(
                                                            "level", Value(WaterLevel),
                                                            "quality", Value(Quality)))))
                                    .get());
                    
                } catch (NumberFormatException e) {
                    System.err.println("Invalid data format: " + requestBody);
                } catch (InterruptedException e) {
                    
                    e.printStackTrace();
                } catch (ExecutionException e) {
        
                    e.printStackTrace();
                }
            }
        }
    }
}