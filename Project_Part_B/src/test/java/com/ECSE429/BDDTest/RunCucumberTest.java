package com.ECSE429.BDDTest;

import com.ECSE429.API.RestApiCall;
import io.cucumber.java.*;

import okhttp3.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;


@TestMethodOrder(MethodOrderer.Random.class)
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class RunCucumberTest {
    @Before
    public static void setupEnvironment() {
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("java -jar runTodoManagerRestAPI-1.5.5.jar"); // Ensures that the API is ready to be tested
            System.out.println("Setting up environment");
//            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @After
    public static void shutdownEnvironment() {
        RestApiCall call = new RestApiCall();
        System.out.println("Shutdown system");
        call.todosGet("shutdown", "json");
    }

}





