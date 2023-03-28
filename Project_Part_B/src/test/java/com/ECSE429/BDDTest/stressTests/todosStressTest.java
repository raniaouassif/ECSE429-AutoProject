package com.ECSE429.BDDTest.stressTests;

import com.ECSE429.API.RestApiCall;
import okhttp3.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class todosStressTest {
    RestApiCall call = new RestApiCall();
    Response response;

    @Test
    @BeforeAll
    public void setupEnvironment() {
        System.out.println("Starting environment");
        Runtime runtime = Runtime.getRuntime();
        try {
            // Lauch the rest api todos list manager with the command
            runtime.exec("java -jar runTodoManagerRestAPI-1.5.5.jar");
            System.out.println("Setting up environment");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @AfterAll
    public void shutdownEnvironment() {
        RestApiCall call = new RestApiCall();
        System.out.println("Shutdown system");
        call.getRequest("shutdown", "json");
    }

    @Test
    public void testPost() throws IOException {
        int j = 1;
        List<List<String>> data = new ArrayList<>();
        List<Integer> valuesToCheck = Arrays.asList(10, 50, 100, 500, 1000, 1500, 2000, 5000, 10000);

        // Initial Free Memory
        long freeMemory = Runtime.getRuntime().freeMemory();

        // Start timer
        long startTime = System.currentTimeMillis();
        while (j <= 100) {
            for (int i = j; i <= 10 * j; i++) {
                // Create a JSON object with a "title" field
                JSONObject requestBody = new JSONObject();
                requestBody.put("title", "Todo");
                response = call.postRequest("todos", "json", requestBody);
            }
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            // Measure CPU usage
            double cpuUsage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();

            // Measure memory usage
            long totalMemory = Runtime.getRuntime().totalMemory();
            long usedMemory = totalMemory - freeMemory;
            // Add data to list
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(j));
            row.add(String.valueOf(elapsedTime));
            row.add(String.format("%.2f", cpuUsage));
            row.add(String.valueOf(usedMemory));
            data.add(row);
            j*=10;
        }

        writeReport("POST_Report.csv", data);

    }

    @Test
    public void testPut() {
        long startTime;
        for (int i = 1; i <= 13; i++) {
            startTime = System.currentTimeMillis();

            // Create a JSON object with a "title" field
            JSONObject requestBody = new JSONObject();
            requestBody.put("title", "Modified Todo #" + i);
            requestBody.put("description", "Modified Using Put Request");
            response = call.putRequest("todos/" + i, "json", requestBody);

            if (response.code() != 200 && response.code() != 201) {
                throw new RuntimeException("Todo POST failed");
            }

            // Measure CPU
            // Measure Memory available

            if (i == 10 || i == 100 || i == 1000 || i == 10000 || i == 100000 || i == 1000000) {
                // Measure Time
                // Measure CPU %
                // Measure Memory available
            }
        }
    }

    // Helper Functions
    public void writeReport(String fileName, List<List<String>> rows) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        // Write header
        List<String> header = Arrays.asList("Objects Number", "Time (ms)", "CPU (%)", "Available Free Memory (B)");
        csvPrinter.printRecord(header);

        for (List<String> row : rows) {
            csvPrinter.printRecord(row);
        }

        csvPrinter.flush();
        csvPrinter.close();
        fileWriter.close();
    }
}
