/* ECSE429 Software Validation - Automation Project Part A 
 * Unit test suite of "rest api todo list" 
 * Rania Ouassif 260861621
 */

/* This class includes additional unit tests considerations of invalid operations. */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.Random.class)
public class invalid_tests {
    
    ////////////////////  1. /todos ///////////////////////////////

    // /todos POST - empty
    /**Trying to post a todo without empty body request*/
    @Test
    public void testTodosPost_Empty() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(HttpRequest.BodyPublishers.ofString("")) // Sending empty string as the request body
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Assert the status code is 400 (Bad Request)
        assertEquals(400,response.statusCode());
    }

    ////////////////////  2. /todos/:id  ///////////////////////////////
    // Trying to delete an already deleted todo
    @Test
    public void testTodoDelete_alreadyDeleted() throws IOException, InterruptedException {
        // Set the ID of the to-do item to be deleted
        int id = 2;
        
        // Create an HTTP client
        HttpClient client = HttpClient.newHttpClient();
        
        // Build the DELETE request for the specified to-do item ID
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .DELETE()
                .build();
        
        // Send the DELETE request, discarding the response body
        client.send(request,  HttpResponse.BodyHandlers.discarding());
    
        // Send the DELETE request again and retrieve the response
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        
        // Assert that the response status code is 404 (Not Found)
        assertEquals(404, response.statusCode());
    }


    ////////////////////  3. /todos/:id/tasksof ///////////////////////////////

    // /todos/:id/tasksof GET - with missing id (expected)
    /*Expected behavior is to return 404 not found*/
    @Test
    public void testTodosTasksofsGet_missingId_expected() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/tasksof")) // missing id
                .GET()
                .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(404,response.statusCode());
    }

    // /todos/:id/categories GET - with missing id (actual)
    /*Should return 404 not found, instead returns a list of all projects*/
    @Test
    public void testTodosTasksofGet_missingId_actual() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/tasksof")) // missing id
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode());

        // Convert the response body to a JSON object
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.body());

        // Verify that the "projects" field is a list
        JsonNode projectsNode = jsonResponse.get("projects");
        assertTrue(projectsNode.isArray());
    }

    ////////////////////  4. /todos/:id/categories ///////////////////////////////

    // /todos/:id/categories GET - with missing id (expected)
    /*Should return 404 not found, instead returns a list of all categories*/
    @Test
    public void testTodosCategoriesGet_missingId_expected() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/categories")) // missing id
                .GET()
                .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(404,response.statusCode());
    }

    // /todos/:id/categories GET - with missing id (actual)
    /*Should return 404 not found, instead returns a list of all categories*/
    @Test
    public void testTodosCategoriesGet_missingId_actual() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/categories")) // missing id
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode());

        // Convert the response body to a JSON object
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.body());

        // Verify that the "categories" field is a list
        JsonNode categoriesNode = jsonResponse.get("categories");
        assertTrue(categoriesNode.isArray());
    }
}
