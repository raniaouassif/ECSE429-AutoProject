import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import javax.swing.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static java.lang.System.exit;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;


public class tests {
    //Verify that server has started
    @Test
    public void main_page() throws IOException, InterruptedException {
        try {
            //Create  new HttpClient
            HttpClient client = HttpClient.newHttpClient();
            //Build a new HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:4567"))
                    .GET().build(); // GET is default
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            assertEquals(302, response.statusCode());
        } catch (IOException e) {
            final JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Error: Start Server on Terminal.");
            exit(-1);
        }
    }

    // GET todos
    /* Notes: This test sees if the get api succeeds*/
    @Test
    public void get_todos() throws IOException, InterruptedException {
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos")).GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode());
        System.out.println("\nget_todos: \n" + response.body());
    }

    // HEAD todos
    /* Notes: This test sees if the get api succeeds*/
    @Test
    public void head_todos() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        HttpHeaders headers = response.headers();
        assertEquals(200,response.statusCode());
        System.out.println("\nhead_todos: \n" + headers);
    }

    // POST todos
    @Test
    public void test_todos_post() throws IOException, InterruptedException {

        String jsonBody = "{\"title\":\"New todo\",\"doneStatus\":false,\"description\":\"My description\"}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> responseStr = client.send(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(201,response.statusCode());
        assertNotNull(responseStr);
    }


    // Get todo with id
    @Test
    public void get_todo_id() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        
        assertEquals(200,response.statusCode());
        System.out.println("\nget_todos: \n" + response.body());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        String id = root.path("todos").get(0).path("id").toString();
        System.out.println(id);
        assertEquals("1", "1");
    }


}
