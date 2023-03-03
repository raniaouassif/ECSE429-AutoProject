package com.ECSE429.API;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class TodoApiCall {
    String baseUrl = "http://localhost:4567/";

    public static void main(String[] args){
        TodoApiCall todoCall = new TodoApiCall();
        //Response x = todoCall.head("todos", "json");
//        try {
//            System.out.println(x.headers().toString());
//        } catch (Exception e ){
//            e.printStackTrace();
//        }
    }

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

    public Response todosGet(String url, String contentType){
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
     * @param url         the URL to send the request to
     * @param contentType the content type of the request body (json or XML)
     * @param jsonBody    the JSON body of the request
     * @return a Response object containing the response from the server, or null if there was an error
     */
    public Response todosPost(String url, String contentType, JSONObject jsonBody){
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
}
