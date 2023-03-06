package com.ECSE429.BDDTest;

import com.ECSE429.API.RestApiCall;
import io.cucumber.java.*;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class RunCucumberTest {
    @Before
    public static void setupEnvironment() {
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
    @After
    public static void shutdownEnvironment() {
        RestApiCall call = new RestApiCall();
        System.out.println("Shutdown system");
        call.getRequest("shutdown", "json");
    }
}