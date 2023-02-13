import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class tests_PUT {

    ////////////////////  2. /todos/:id  ///////////////////////////////
    // /todos/:id PUT
    /*This test shows that the PUT API call returns a successful status code (200) and modifies the amended fields. */
    @Test
    public void testTodoPut() throws IOException, InterruptedException {
        int id = 2; 
        var val = new HashMap<String, Object>() {{
            put("title", "Modified Todo");
            put("doneStatus", true);
            put("description", "this is a modified desc using PUT ");
        }};

        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id)) // adding id in endpoint as per API doc
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode()); 

        System.out.println(response.body());
        // Parse the response body as a JSON object
        JsonNode responseBody = om.readTree(response.body());
        // Check each field in the response body against the values in the request body map
        assertEquals(val.get("title"), responseBody.get("title").asText());
        assertEquals(val.get("doneStatus"), responseBody.get("doneStatus").asBoolean());
        assertEquals(val.get("description"), responseBody.get("description").asText());
    }

    // /todos/:id PUT
    /*This test shows that the PUT API call returns a successful status code (200) and modifies the amended fields. */
    @Test
    public void testTodoPut_xml() throws IOException, InterruptedException {
        int id = 2; 
        // Create the XML string to be sent in the request body
        var xmlString = "<todo>" +
            "<doneStatus>false</doneStatus>" +
            "<description>The meeting purpose is to explain ... </description>" +
            "<title>Meeting at 2 pm</title>" +
            "</todo>";

        HttpClient client = HttpClient.newHttpClient();
        // Create the HttpRequest object, specifying the POST method and the URL endpoint
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos" + id))
                .header("Content-Type", "application/xml") // Set the Content-Type header to indicate the format of the request body
                .PUT(HttpRequest.BodyPublishers.ofString(xmlString))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200,response.statusCode()); 
    }

    // /todos/:id PUT - nonexisting 
    /*This test shows that the PUT API call for a nonexisting todo returns status code (404) Not Found. */
    @Test
    public void testTodoPut_nonexisting() throws IOException, InterruptedException {
        int id = 0; 
        // Create the XML string to be sent in the request body
        var xmlString = "<todo>" +
            "<doneStatus>false</doneStatus>" +
            "<description>The meeting purpose is to explain ... </description>" +
            "<title>Meeting at 2 pm</title>" +
            "</todo>";

        HttpClient client = HttpClient.newHttpClient();
        // Create the HttpRequest object, specifying the POST method and the URL endpoint
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos" + id))
                .header("Content-Type", "application/xml") // Set the Content-Type header to indicate the format of the request body
                .PUT(HttpRequest.BodyPublishers.ofString(xmlString))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(404,response.statusCode()); 
    }


    // /todos/:id/categories GET - After put
    /* This test confirms that PUT requests is causing the categories relationship with the todo to get deleted. Resulting in wrong output.
     * Discovered in exploratory testing session
    */
    @Test
    public void testTodoCategoriesGet_afterPut() throws IOException, InterruptedException {
        int id = 1; 
        HttpClient client = HttpClient.newHttpClient();
    
        // Create a GET request for the todo categories
        HttpRequest getCategoriesRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/categories"))
                .GET()
                .build();

        // Send the GET request and store the response (before PUT)
        HttpResponse<String> categoriesResponse_BeforePut = client.send(getCategoriesRequest, HttpResponse.BodyHandlers.ofString());
        String bodyBeforePut = categoriesResponse_BeforePut.body();

        // Create a map with the new title to be updated using PUT request
        var val = new HashMap<String, Object>() {{
            put("title", "Modified Todo using PUT");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        
        // Create a PUT request for the todo
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id)) // adding id in endpoint as per API doc
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        // Send the PUT request
        client.send(putRequest, HttpResponse.BodyHandlers.ofString());        
        
        // Send the GET request and store the response (after PUT)
        HttpResponse<String> categoriesResponse_AfterPut = client.send(getCategoriesRequest, HttpResponse.BodyHandlers.ofString());
        String bodyAfterPut = categoriesResponse_AfterPut.body();

        // Compare the two responses (before and after the PUT request)
        assertEquals(bodyBeforePut, bodyAfterPut);
    }

    // /todos/:id/tasksof GET - After put
    /* This test confirms that PUT request is causing the tasksof relationship with the todo to get deleted. Resulting in wrong output.
     * Discovered in exploratory testing session
    */
    @Test
    public void testTodoTasksofGet_afterPut() throws IOException, InterruptedException {
        int id = 2; 
        HttpClient client = HttpClient.newHttpClient();
    
        // Create a GET request for the todo tasksof
        HttpRequest getTasksofRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/tasksof"))
                .GET()
                .build();

        // Send the GET request and store the response (before PUT)
        HttpResponse<String> tasksofResponse_BeforePut = client.send(getTasksofRequest, HttpResponse.BodyHandlers.ofString());
        String bodyBeforePut = tasksofResponse_BeforePut.body();

        // Create a map with the new title to be updated using PUT request
        var val = new HashMap<String, Object>() {{
            put("title", "Modified Todo using PUT");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        
        // Create a PUT request for the todo
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id)) // adding id in endpoint as per API doc
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        // Send the PUT request
        client.send(putRequest, HttpResponse.BodyHandlers.ofString());        
        
        // Send the GET request and store the response (after PUT)
        HttpResponse<String> tasksofResponse_AfterPut = client.send(getTasksofRequest, HttpResponse.BodyHandlers.ofString());
        String bodyAfterPut = tasksofResponse_AfterPut.body();
        // Compare the two responses (before and after the PUT request)
        assertEquals(bodyBeforePut, bodyAfterPut);
    }


}
