package com.ECSE429.BDDTest.stressTests;

import com.ECSE429.API.RestApiCall;
import com.sun.management.OperatingSystemMXBean;
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

    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
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
    public void testsPost()  {
        // Create a JSON object with a "title" field
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "Todo");

        //Values to check
        List<List<String>> data = new ArrayList<>();
        List<Integer> valuesToCheck = Arrays.asList(1, 10, 50, 100, 500, 1000, 1500, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 10100);

        // Memory
        long totalMemory = osBean.getTotalPhysicalMemorySize() / (1024 * 1024);
        long freeMemory ;
        long usedMemory;

        //CPU
        double systemCpuUsage;
        double processCpuUsage;

        // Start timer
        long endTime;
        long elapsedTime;
        long startTime = System.currentTimeMillis();
            for (int i = 0; i <= 10100; i++) {
                requestBody.put("title", "Post Todo #" + i);
                response = call.postRequest("todos", "json", requestBody);
                if (response == null || response.code() != 201) {
                    System.out.println("Stopped at POST TODO #" + i);
                    throw new RuntimeException("Project POST failed");
                }
                if(valuesToCheck.contains(i)) {
                    endTime = System.currentTimeMillis();
                    elapsedTime = endTime - startTime;

                    // Measure CPU usage
                    systemCpuUsage = osBean.getSystemCpuLoad();
                    //Process CPU Usage
                    processCpuUsage = osBean.getProcessCpuLoad();

                    // Measure memory usage
                    freeMemory = osBean.getFreePhysicalMemorySize() / (1024 * 1024);
                    usedMemory = totalMemory - freeMemory;

                    // Add data to list
                    List<String> row = new ArrayList<>();
                    row.add(String.valueOf(i));
                    row.add(String.valueOf(elapsedTime));
                    row.add(String.format("%.2f", systemCpuUsage));
                    row.add(String.format("%.2f", processCpuUsage));
                    row.add(String.valueOf(freeMemory));
                    row.add(String.valueOf(usedMemory));
                    data.add(row);
                }
            }
        try {
            writeReport("post.csv", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPut() {
        // Create a JSON object with a "title" field and "description" field
        JSONObject requestBody = new JSONObject();
        requestBody.put("description", "Modified Using Put Request");

        //Values to check
        List<List<String>> data = new ArrayList<>();
        List<Integer> valuesToCheck = Arrays.asList(1, 10, 50, 100, 500, 1000, 1500, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 10100);

        // Memory
        long totalMemory = osBean.getTotalPhysicalMemorySize() / (1024 * 1024);
        long freeMemory ;
        long usedMemory;

        //CPU
        double systemCpuUsage;
        double processCpuUsage;

        // Start timer
        long endTime;
        long elapsedTime;
        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 10100; i++) {
            requestBody.put("title", "Modified Todo #" + i);
            response = call.putRequest("todos/" + i, "json", requestBody);
            if (response == null || response.code() != 200) {
                System.out.println("Stopped at POST TODO #" + i);
                throw new RuntimeException("Project POST failed");
            }

            if(valuesToCheck.contains(i)) {
                endTime = System.currentTimeMillis();
                elapsedTime = endTime - startTime;

                // Measure CPU usage
                systemCpuUsage = osBean.getSystemCpuLoad();
                //Process CPU Usage
                processCpuUsage = osBean.getProcessCpuLoad();

                // Measure memory usage
                freeMemory = osBean.getFreePhysicalMemorySize() / (1024 * 1024);
                usedMemory = totalMemory - freeMemory;

                // Add data to list
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i));
                row.add(String.valueOf(elapsedTime));
                row.add(String.format("%.2f", systemCpuUsage));
                row.add(String.format("%.2f", processCpuUsage));
                row.add(String.valueOf(freeMemory));
                row.add(String.valueOf(usedMemory));
                data.add(row);
            }
        }
        try {
            writeReport("put.csv", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDelete() {
        //Values to check
        List<List<String>> data = new ArrayList<>();
        List<Integer> valuesToCheck = Arrays.asList(1, 10, 50, 100, 500, 1000, 1500, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 10100);

        // Memory
//        long totalMemory = Runtime.getRuntime().totalMemory();
        long totalMemory = osBean.getTotalPhysicalMemorySize() / (1024 * 1024);
        long freeMemory ;
        long usedMemory;

        //CPU
        double systemCpuUsage;
        double processCpuUsage;

        // Start timer
        long endTime;
        long elapsedTime;
        long startTime = System.currentTimeMillis();
        for (int i = 1; i <= 10100; i++) {
            response = call.deleteRequest("todos/" + i);

            if (response == null || response.code() != 200 ) {
                System.out.println("Stopped at POST TODO #" + i);
                throw new RuntimeException("Project POST failed");
            }

            if(valuesToCheck.contains(i)) {
                endTime = System.currentTimeMillis();
                elapsedTime = endTime - startTime;

                // Measure CPU usage
//                    cpuUsage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
                systemCpuUsage = osBean.getSystemCpuLoad();
                //Process CPU Usage
                processCpuUsage = osBean.getProcessCpuLoad();

                // Measure memory usage
//                    freeMemory = Runtime.getRuntime().freeMemory();
                freeMemory = osBean.getFreePhysicalMemorySize() / (1024 * 1024);
                usedMemory = totalMemory - freeMemory;

                // Add data to list
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i));
                row.add(String.valueOf(elapsedTime));
                row.add(String.format("%.2f", systemCpuUsage));
                row.add(String.format("%.2f", processCpuUsage));
                row.add(String.valueOf(freeMemory));
                row.add(String.valueOf(usedMemory));
                data.add(row);
            }
        }
        try {
            writeReport("delete.csv", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper Functions
    public void writeReport(String fileName, List<List<String>> rows) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)) {

            // Write header
            List<String> header = Arrays.asList("Objects Number", "Time (ms)", "System CPU Usage (%)","Process CPU Usage (%)", "Free Memory (%)","Used Memory (B)");
            csvPrinter.printRecord(header);

            for (List<String> row : rows) {
                csvPrinter.printRecord(row);
            }
            csvPrinter.flush();
        }
    }
}
