package com.ECSE429.API;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class RestApiCall {
    String baseUrl = "http://localhost:4567/";

    /**
     * Verify that the server is running
     */
    public Response checkService() {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        Request request = new Request.Builder()
                .url(baseUrl)
                .get()
                .build();
        try {
            // Send the HTTP request and store the server response in the 'response' variable
            response = client.newCall(request).execute();
        } catch (IOException e) {
            // In case of an exception, return null to indicate failure
            System.out.println("ERROR: The server is not running");
            return null;
        }
        return response;
    }

    /**
     * Sends a GET request to the specified URL with the given JSON body
     *
     * @param url         the endpoint URL to send the request to
     * @param contentType the content type of the request body (json or XML)
     * @return a Response object containing the response from the server, or null if there was an error
     */
    public Response getRequest(String url, String contentType){
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        // Construct the full URL by appending the base URL to the endpoint URL
        url = baseUrl + url;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/" + contentType + "; charset=utf-8")
                .get()
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return null;
        }
        return response;
    }

    /**
     * Sends a POST request to the specified URL with the given JSON body
     *
     * @param url         the endpoint URL to send the request to
     * @param contentType the content type of the request body (json or XML)
     * @param jsonBody    the JSON body of the request
     * @return a Response object containing the response from the server, or null if there was an error
     */
    public Response postRequest(String url, String contentType, JSONObject jsonBody){
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        // Construct the full URL by appending the base URL to the endpoint URL
        url = baseUrl + url;

        // Create the request body using the JSON body and content type
        MediaType JSON = MediaType.parse("application/" + contentType + "; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, jsonBody.toString());

        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/" + contentType + "; charset=utf-8")
                .post(body)
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return null;
        }
        return response;
    }

    /**
     * Sends a POST request to the specified URL with the given STRING body
     *
     * @param url         the endpoint URL to send the request to
     * @param contentType the content type of the request body (json or XML)
     * @param requestBody the String body of the request
     * @return a Response object containing the response from the server, or null if there was an error
     */
    public Response postRequestString(String url, String contentType, String requestBody) {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        // Construct the full URL by appending the base URL to the endpoint URL
        url = baseUrl + url;

        // Create the request body using the JSON body and content type
        MediaType mediaType = MediaType.parse("application/" + contentType + "; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/" + contentType + "; charset=utf-8")
                .post(body)
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return null;
        }
        return response;
    }

    /**
     * Sends a PUT request to the specified URL with the given JSON body
     *
     * @param url         the endpoint URL to send the request to
     * @param contentType the content type of the request body (json or XML)
     * @param jsonBody    the JSON body of the request
     * @return a Response object containing the response from the server, or null if there was an error
     */
    public Response putRequest(String url, String contentType, JSONObject jsonBody){
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        // Construct the full URL by appending the base URL to the endpoint URL
        url = baseUrl + url;

        // Create the request body using the JSON body and content type
        MediaType JSON = MediaType.parse("application/" + contentType + "; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody.toString());

        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/" + contentType + "; charset=utf-8")
                .put(body)
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return null;
        }
        return response;
    }

    /**
     * Sends a DELETE request to the specified URL
     *
     * @param url         the endpoint URL to send the request to
     * @param contentType the content type of the request body (json or XML)
     * @return a Response object containing the response from the server, or null if there was an error
     */
    public Response deleteRequest(String url, String contentType){
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        // Construct the full URL by appending the base URL to the endpoint URL
        url = baseUrl + url;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/" + contentType + "; charset=utf-8")
                .delete()
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return null;
        }
        return response;
    }
}
