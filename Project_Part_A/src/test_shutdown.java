/* ECSE429 Software Validation - Automation Project Part A 
 * Unit test suite of "rest api todo list" 
 * Rania Ouassif 260861621
 */

/* This class includes unit test demonstrating the failing test of /shutdown GET request. */

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class test_shutdown {
    // /shutdown GET
    /* Expects a status code for succesful request (200)
     *  Failure, this test is expected to fail if the server has been stopped, the server should crash and the test should not pass 
     */
    @Test
    public void systemShutdown() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/shutdown"))
                .GET()
                .build();
        HttpResponse<Void> response = client.send(request,
                HttpResponse.BodyHandlers.discarding());
        assertEquals(200,response.statusCode());
    }
}
