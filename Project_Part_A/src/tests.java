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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.Random.class)
public class tests {
    
    // Verify that the server has started
    @Test
    public void testServerStart() {
        // Create a new HttpClient
        HttpClient client = HttpClient.newHttpClient();
        // Build a new HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:4567"))
            .GET().build();
        try {
            // Send the HttpRequest and get the response
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            // Verify the response status code is 302
            assertEquals(302, response.statusCode());
        } catch (IOException | InterruptedException e) {
            // Show an error message if the server has not been started
            final JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Error: Start Server on Terminal.");
            // Exit with status code -1
            System.exit(-1);
        }
    }

    ////////////////////  1. /todos  ///////////////////////////////

    // /todos GET 
    /* Notes: This test sees if the GET API succeeds*/
    @Test
    public void testTodosGet() throws IOException, InterruptedException {
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos")).GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode());
        System.out.println("\nget_todos: \n" + response.body());
    }

    // /todos GET  - json
    /* This test sees if the default returned data is in json*/
    @Test
    public void testTodosGet_json() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:4567/todos")).GET()
            .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals("application/json", response.headers().firstValue("Content-Type").get());
    }

    // /todos GET - xml
    /* This test sees confirms GET todos can generate payloads in XML.*/
    @Test
    public void testTodosGet_xml() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:4567/todos"))
            //Accept header to application/xml, 
            .header("Accept", "application/xml") 
            .GET()
            .build();
        HttpResponse<Void> response = client.send(request,
            HttpResponse.BodyHandlers.discarding());
        //Assert that the response payload is in XML format
        assertEquals("application/xml", response.headers().firstValue("Content-Type").get());
    }

    // /todos OPTIONS 
    /* Test to see if using OPTIONS requests to get all todos returns a successful status code (200). 
     * This is an undocumented API discovered during exploratory testing. 
    */ 
    @Test
    public void testTodosOptions() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:4567/todos"))
            //Accept header to application/xml, 
            .header("Accept", "application/xml") 
            .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<Void> response = client.send(request,
            HttpResponse.BodyHandlers.discarding());
            assertEquals(200,response.statusCode());
    }

    // /todos GET - filter
    /*Checks if the request is successful (200) and returns all the todos with specific condition doneStatus of true or false */
    @Test
    public void testTodosGet_Filter() throws IOException, InterruptedException {
        Random random = new Random();
        boolean filterValue = random.nextBoolean(); // randomly choose if the doneStatus is true or false
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos?doneStatus=" + filterValue))
                .GET().build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode());

        // Assert that all the todos have doneStatus set to filter value
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode todos = root.path("todos");
        for(JsonNode todo: todos) {
            System.out.println(todo.path("doneStatus").asText());
            assertEquals(String.valueOf(filterValue), todo.path("doneStatus").asText());
        }   
    }

    // /todos HEAD 
    /* This test sees if the HEAD API succeeds*/
    @Test
    public void testTodosHead() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        HttpHeaders headers = response.headers();
        System.out.println(headers);
        assertEquals(200,response.statusCode());
    }

    // /todos POST - json
    /**This test checks if the POST todo works without an ID using the field values in the body of the message, as specified in the API doc
     * by checking each field in the response body against the values in the request body map*/
    @Test
    public void testTodosPost_json() throws IOException, InterruptedException {
        var val = new HashMap<String, Object>() {{
            put("title", "Meeting at 2 pm");
            put("doneStatus", false);
            put("description", "The meeting purpose is to explain ... ");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Assert the status code is 201 created
        assertEquals(201,response.statusCode());

        // Parse the response body as a JSON object
        JsonNode responseBody = om.readTree(response.body());

        // Check each field in the response body against the values in the request body map
        assertEquals(val.get("title"), responseBody.get("title").asText());
        assertEquals(val.get("doneStatus"), responseBody.get("doneStatus").asBoolean());
        assertEquals(val.get("description"), responseBody.get("description").asText());
    }

     // /todos POST - xml
    /**This test checks if the POST todo works without an ID using the field values in the body of the message, as specified in the API doc
     * by checking each field in the response body against the values in the request body map*/
    @Test
    public void testTodosPost_xml() throws IOException, InterruptedException {
        // Create the XML string to be sent in the request body
        var xmlString = "<todo>" +
            "<doneStatus>false</doneStatus>" +
            "<description>The meeting purpose is to explain ... </description>" +
            "<title>Meeting at 2 pm</title>" +
            "</todo>";
        
        // Create a new HttpClient object
        HttpClient client = HttpClient.newHttpClient();

        // Create the HttpRequest object, specifying the POST method and the URL endpoint
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .header("Content-Type", "application/xml") // Set the Content-Type header to indicate the format of the request body
                .POST(HttpRequest.BodyPublishers.ofString(xmlString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Assert the status code is 201 (created)
        assertEquals(201,response.statusCode());
    }

    // /todos POST  - id in the body
    /**This test confirms that the POST todo does not works with an ID in the body of the message, as specified in API doc. 
     * Note : This is an undocumented API which was tested in the exploratory testing */
    @Test
    public void testTodosPost_idBody() throws IOException, InterruptedException {
        var val = new HashMap<String, Object>() {{
            put("id", "100");
            put("title", "Meeting at 2 pm");
            put("doneStatus", false);
            put("description", "The meeting purpose is to explain ... ");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400,response.statusCode());
    }

    // /todos POST  -  only field is title in body 
    /* Notes:  This test checks that it is possible to create only with title (only mandatory field)
     * This test checks that the default value for doneStatus and description are respectively false and empty string */
    @Test
    public void testTodosPost_TitleOnly() throws IOException, InterruptedException {
        var val = new HashMap<String, Object>() {{
            put("title", "Todo with just the title");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201,response.statusCode());

        // Parse the response body as a JSON object
        JsonNode responseBody = om.readTree(response.body());
        // Check each field in the response body
        assertEquals(val.get("title"), responseBody.get("title").asText());
        // Default doneStatus should be false
        assertEquals(false, responseBody.get("doneStatus").asBoolean());
        // Default description should be ""
        assertEquals("", responseBody.get("description").asText());
    }

    // /todos POST  - title as empty string
    /*Notes:  This test checks if the POST todo with an empty title returns status code 400.*/
    @Test
    public void testTodosPost_EmptyTitle() throws IOException, InterruptedException {
        var val = new HashMap<String, Object>() {{
            put("title", "");
            put("doneStatus", false);
            put("description", "My new todo description is a string.");
        }};

        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);

        System.out.println(requestBody);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(400,response.statusCode());
    }

    // /todos POST - no title in body
    /*Notes:  This test checks if the POST todo without title returns status code 400. */
    @Test
    public void testTodosPost_NoTitle() throws IOException, InterruptedException {
        var val = new HashMap<String, Object>() {{
            put("doneStatus", false);
            put("description", "My new todo description is a string.");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(400,response.statusCode());
    }

    ////////////////////  2. /todos/:id  ///////////////////////////////

    // /todos/:id GET
    /*This test checks if the GET API call to retrieve a todo with a specific id returns a successful status code (200). */
    @Test
    public void testTodoGet() throws IOException, InterruptedException {
        int id = 1;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        //Assert that request is successful
        assertEquals(200,response.statusCode());

        // Assert that the response todo has the same id as the requested id
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        int responseId = root.path("todos").get(0).path("id").asInt();
        assertEquals(id, responseId);
    }

    ///todos/:id GET - nonexisting todo 
    /* Trying to get a nonexisting todo should result in 404 status code, as seen in exploratory testing */
    @Test
    public void testTodoGet_nonexisting() throws IOException, InterruptedException {
        int id = 0; // no todo exists with id = 0, since they start with 1
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(404,response.statusCode());
    }

    // /todos/:id HEAD 
    /*This test checks if the HEAD API call to retrieve a todo with a specific id returns a successful status code (200). */
    @Test
    public void testTodoHead() throws IOException, InterruptedException {
        int id = 1;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode());
    }

    // /todos/:id HEAD - nonexisting
    /*This test checks if the HEAD API call to retrieve a todo with a specific id returns a successful status code (200). */
    @Test
    public void testTodoHead_nonexisting() throws IOException, InterruptedException {
        int id = 0;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(404,response.statusCode());
    }

    // /todos/:id POST 
    /* Notes: This test checks if the POST API doesn't work if the id is in endpoint, as seen in exploratory testing*/
    @Test
    public void testTodoPost() throws IOException, InterruptedException {
        int id = 100; // trying to create todo with id 100
        var val = new HashMap<String, Object>() {{
            put("title", "New Todo");
            put("doneStatus", false);
            put("description", "My new todo description is a string.");
        }};

        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id)) // adding id in endpoint as per API doc
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(404,response.statusCode()); 
    }

    // /todos/:id POST - existing todo (expected) 
    /** This test shows the expected behavior of trying to post with an id of existing todo. Should return 409 conflict status code.
     * This instability was discovered in exploratory testing
    */
    @Test
    public void testTodosPost_existing_expected() throws IOException, InterruptedException {
        int id = 1;
        var val = new HashMap<String, Object>() {{
            put("title", "Meeting at 2 pm");
            put("doneStatus", false);
            put("description", "The meeting purpose is to explain ... ");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id)) 
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(409,response.statusCode());
    }

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

    //  /todos/:id PATCH
    /* Test to see if the PATCH API request sends returns method not allowed with status code 405. 
     * Note : This is an undocumented API tested during exploratory testing
    */
    @Test
    public void testTodoPatch() throws IOException, InterruptedException {
        int id = 1; 
        var val = new HashMap<String, Object>() {{
            put("title", "Some Todo");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id)) // adding id in endpoint as per API doc
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(405,response.statusCode()); 
    }

    //  /todos/:id DELETE
    /*This test checks if the DELETE API call to delete a todo with a specific id returns a successful status code (200). */
    @Test
    public void testTodoDelete() throws IOException, InterruptedException {
        int id = 2;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .DELETE()
                .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(200,response.statusCode());
    }

    // /todos/:id DELETE - nonexisting 
    /*This test checks if the DELETE API call to delete a nonexisting todo returns 404 code. */
    @Test
    public void testTodoDelete_nonexisting() throws IOException, InterruptedException {
        int id = 0; //nonexisting id
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id))
                .DELETE()
                .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(404,response.statusCode());
    }

    ////////////////////  3. /todos/:id/tasksof  ///////////////////////////////

    // /todos/:id/tasksof GET
    /*Notes : Checks status code of GET todo category */
    @Test
    public void testTodosTasksofGet() throws IOException, InterruptedException {
        int id = 2;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/tasksof"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        // System.out.println(response.body());
        assertEquals(200,response.statusCode());
    }

    // /todos/:id/tasksof GET - nonexisting todo (expected)
    /*This is an instability discovered in exploratory testing, should return status code 404 not found*/
    @Test
    public void testTodosTasksofGet_nonexisting_expected() throws IOException, InterruptedException {
        int id = 0;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/tasksof"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(404,response.statusCode());
    }
     
    // /todos/:id/tasksof GET - nonexisting todo (actual)
    /*Notes: This test shows the actual behavior : returns successful 200, with a list of all the categories categories */
    @Test
    public void testTodosTasksofGet_nonexisting_actual() throws IOException, InterruptedException {
        int id = 0;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/tasksof"))
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

    //  /todos/:id/tasksof HEAD
    /*This test checks if the HEAD API call to retrieve a todo with a specific id returns a successful status code (200). */
    @Test
    public void testTodosTasksofHead() throws IOException, InterruptedException {
        int id = 1;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/tasksof"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200,response.statusCode());
    }

    //  /todos/:id/tasksof POST 
    /* Notes: This test checks if the POST API indeed works without ids, as seen in the exploratory testing. */
    @Test
    public void testTodosTasksofPost() throws IOException, InterruptedException {
        int id = 1;
        var val = new HashMap<String, String>() {{
            put("id", "1");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/tasksof"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201,response.statusCode());
    }

    ////////////////////  4. /todos/:id/tasksof/:id  ///////////////////////////////

    // /todos/:id/tasksof/:id DELETE
    /* Notes: This test checks if the DELETE API indeed works without ids, as seen in the exploratory testing. */
    @Test
    public void testTodosTasksofDelete() throws IOException, InterruptedException {
        int todoId = 1;
        int tasksofId = 1;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + todoId + "/tasksof/" + tasksofId))
                .DELETE()
                .build();
        
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(200,response.statusCode());
    }


    ////////////////////  5. /todos/:id/categories  ///////////////////////////////

    // /todos/:id/categories GET
    /*Notes : Checks status code of GET todo category */
    @Test
    public void testTodosCategoriesGet() throws IOException, InterruptedException {
        int id = 1;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/categories"))
                .GET()
                .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(200,response.statusCode());
    }

    // /todos/:id/categories GET - nonexisting todo (expected)
    /*Notes: This is an instability discovered in exploratory testing, should return status code 404 not found
     */
    @Test
    public void testTodosCategoriesGet_nonexisting_expected() throws IOException, InterruptedException {
        int id = 0;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/categories"))
                .GET()
                .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(404,response.statusCode());
    }

    // /todos/:id/categories GET - nonexisting todo (actual)
    /*Notes: This test shows the actual behavior. 
     *It returns successful 200, with a list of all the categories categories 
     */
    @Test
    public void testTodosCategoriesGet_nonexisting_actual() throws IOException, InterruptedException {
        int id = 0;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/categories"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(200,response.statusCode());

        // Convert the response body to a JSON object
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response.body());

        // Verify that the "categories" field is a list
        JsonNode categoriesNode = jsonResponse.get("categories");
        assertTrue(categoriesNode.isArray());
    }

    // /todos/:id/categories HEAD
    /*Notes : Checks status code of HEAD todo category */
    @Test
    public void testTodosCategoriesHead() throws IOException, InterruptedException {
        int id = 1;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/" + id + "/categories"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(200,response.statusCode());
    }

    // /todos/:id/categories POST
    /* Notes: This test checks if the POST API indeed works without ids, as seen in the exploratory testing. */
    @Test
    public void testTodosCategoriesPost() throws IOException, InterruptedException {
        var val = new HashMap<String, String>() {{
            put("title", "New Category Title");
            put("description", "New category description");
        }};
        var om = new ObjectMapper();
        String requestBody = om.writeValueAsString(val);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201,response.statusCode());
    }


    ////////////////////  5. /todos/:id/categories/:id  ///////////////////////////////

    // /todos/:id/categories DELETE
    @Test
    public void testTodosCategoriesDelete() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response Body: " + response.body());
        System.out.println(response.statusCode());
        assertEquals(200,response.statusCode());
    }

    // //Shutdown API 
    // @AfterAll
    // public void system_shutdown() throws IOException, InterruptedException {
    //     HttpClient client = HttpClient.newHttpClient();
    //     HttpRequest request = HttpRequest.newBuilder()
    //             .uri(URI.create("http://localhost:4567/shutdown"))
    //             .GET()
    //             .build();
    //     HttpResponse<Void> response = client.send(request,
    //             HttpResponse.BodyHandlers.discarding());
    //     assertEquals(200,response.statusCode());
    //     System.out.println("\nShutdown: \n" + response.body());       
    // }
}