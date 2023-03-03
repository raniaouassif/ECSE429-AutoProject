package com.ECSE429.BDDTest;

import com.ECSE429.API.RestApiCall;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodosStepDefinitions {

    Response response;
    JSONObject responseBody;
    int previousTodosCount;
    int currentTodosCount;


    //BACKGROUND
    @Given("the service is running")
    public void the_service_is_running() {
        RestApiCall call = new RestApiCall();
        response = call.checkService();
        assertEquals(200, response.code());
    }

    //POST TODOS
    @When("I add a new todo with title {string}, description {string} and doneStatus {string}")
    public void i_add_a_new_todo_with_title_description_and_done_status(String todoTitle, String todoDescription, String todoDoneStatus) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create JSONObject to store response body of the two todosGET() API Calls
        JSONObject getTodosResponseBody = null;

        // First API call to retrieve the current list of todos
        Response getPreviousTodos = call.todosGet("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getPreviousTodos.body().string());
            previousTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JSON object with a "title" field containing the specified value
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", todoTitle);
        requestBody.put("description", todoDescription);
        requestBody.put("doneStatus", Boolean.parseBoolean(todoDoneStatus));

        // Second API call to add a new todos using a POST request
        response = call.todosPost("todos", "json", requestBody);
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Third API call to retrieve the updated list of todos after adding the new todos
        Response getCurrentTodos = call.todosGet("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getCurrentTodos.body().string());
            currentTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("I add a new todo with title {string}")
    public void i_add_a_new_todo_with_title(String todoTitle) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create JSONObject to store response body of the two todosGET() API Calls
        JSONObject getTodosResponseBody = null;

        // First API call to retrieve the current list of todos
        Response getPreviousTodos = call.todosGet("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getPreviousTodos.body().string());
            previousTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JSON object with a "title" field containing the specified value
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", todoTitle);

        // Second API call to add a new todos using a POST request
        response = call.todosPost("todos", "json", requestBody);
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Third API call to retrieve the updated list of todos after adding the new todos
        Response getCurrentTodos = call.todosGet("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getCurrentTodos.body().string());
            currentTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Then("I should see one new todo with title {string}, description {string} and doneStatus {string} within the application")
    public void i_should_see_one_new_todo_with_title_description_and_done_status_within_the_application(String todoTitle, String todoDescription, String todoDoneStatus) {
        // Assert that the title, description, and doneStatus fields match the expected values from the response
        assertEquals(1, currentTodosCount - previousTodosCount);
        assertEquals(todoTitle, responseBody.getString("title"));
        assertEquals(todoDescription, responseBody.getString("description"));
        assertEquals(todoDoneStatus, responseBody.getString("doneStatus"));
    }

    @Then("no todo is created")
    public void no_todo_is_created() {
        assertEquals(0 , currentTodosCount- previousTodosCount, "ERROR: No todos should be added. "  );
    }
//
//    @When("I add one new todo with title {string} and invalid field {string}")
//    public void i_add_one_new_todo_with_title_and_invalid_field(String todoTitle, String invalidField) {
//        // Write code here that turns the phrase above into concrete actions
//        throw new io.cucumber.java.PendingException();
//    }

    // GET TODOS BY ID
    @Given("the todo with id {string} exists")
    public void the_todo_with_id_exists(String todoId) {
        // Create a RestApiCall object to make API requests
        RestApiCall call = new RestApiCall();

        // Set a flag to keep track of whether the specified todos exists
        boolean todoExists = false;

        // Send a GET request to retrieve all todos
        Response getTodos = call.todosGet("todos", "json");

        try {
            // Get the response body as a JSON object
            JSONObject getTodosResponseBody = new JSONObject(getTodos.body().string());
            // Get the "todos" array from the response body
            JSONArray todosArray = getTodosResponseBody.getJSONArray("todos");
            // Loop through the todos in the array and check if the specified todo exists
            for (Object obj : todosArray) {
                JSONObject todo = (JSONObject) obj;
                String todoIdStr = todo.getString("id");
                if (todoIdStr.equals(todoId)) {
                    // If the todos with the specified id is found, set the flag to true and exit the loop
                    todoExists = true;
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert that the specified todos exists
        assertEquals(true, todoExists);
    }

    @Given("the todo with id {string} does not exist")
    public void the_todo_with_id_does_not_exist(String todoId) {
        // Create a RestApiCall object to make API requests
        RestApiCall call = new RestApiCall();

        // Set a flag to keep track of whether the specified todos exists
        boolean todoExists = false;

        // Send a GET request to retrieve all todos
        Response getTodos = call.todosGet("todos", "json");

        try {
            // Get the response body as a JSON object
            JSONObject getTodosResponseBody = new JSONObject(getTodos.body().string());
            // Get the "todos" array from the response body
            JSONArray todosArray = getTodosResponseBody.getJSONArray("todos");
            // Loop through the todos in the array and check if the specified todos exists
            for (Object obj : todosArray) {
                JSONObject todo = (JSONObject) obj;
                String todoIdStr = todo.getString("id");
                if (todoIdStr.equals(todoId)) {
                    // If the todos with the specified id is found, set the flag to true and exit the loop
                    todoExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert that the specified todos does not exist
        assertEquals(false, todoExists);
    }

    @When("I get the todo with id {string}")
    public void i_get_the_todo_with_id(String todoId) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Get a todos with id todoId in endpoint
        response = call.todosGet("todos/" + todoId, "json");
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("I filter the endpoint to get the todo with id {string}")
    public void i_filter_the_endpoint_to_get_the_todo_with_id(String todoId) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Get a todos using filter query with id equal to todoId in endpoint
        response = call.todosGet("todos?id=" + todoId, "json");
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Then("I should see a response of one todo with id {string}")
    public void i_should_see_a_response_of_one_todo_with_id(String todoId) {
        //Assert that the length of the responseBody is 1 (only 1 todos is returned)
        assertEquals(1, responseBody.getJSONArray("todos").length());

        JSONArray todosArray = responseBody.getJSONArray("todos");
        JSONObject returnedTodo = todosArray.getJSONObject(0);
        String returnedId = returnedTodo.getString("id");

        //Assert that the returnedId is the same as todoId
        assertEquals(todoId, returnedId);
    }

    @Then("no todo is returned")
    public void no_todo_is_returned() {
        // Assert that the "todos" field does not exist in the response body
        assertTrue(responseBody.isNull("todos"));
    }


    // POST TODOS CATEGORY
    @Given("the category with id {string} exists")
    public void the_category_with_id_exists(String categoryId) {
        // Create a RestApiCall object to make API requests
        RestApiCall call = new RestApiCall();

        // Set a flag to keep track of whether the specified todos exists
        boolean categoryExists = false;

        // Send a GET request to retrieve all todos
        Response getCategories = call.todosGet("categories", "json");

        try {
            // Get the response body as a JSON object
            JSONObject getCategoriesResponseBody = new JSONObject(getCategories.body().string());
            // Get the "category" array from the response body
            JSONArray categoriesArray = getCategoriesResponseBody.getJSONArray("categories");
            // Loop through the todos in the array and check if the specified category exists
            for (Object obj : categoriesArray) {
                JSONObject category = (JSONObject) obj;
                String categoryIdStr = category.getString("id");
                if (categoryIdStr.equals(categoryId)) {
                    // If the todos with the specified id is found, set the flag to true and exit the loop
                    categoryExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert that the specified category exists
        assertEquals(true, categoryExists);
    }

    @Given("there are no relationship between todo with id {string} and category with id {string}")
    public void there_are_no_relationship_between_todo_with_id_and_category_with_id(String todoId, String categoryId) {
        RestApiCall call = new RestApiCall();

        // Send a GET request to retrieve all todos
        Response getTodo = call.todosGet("todos/" + todoId + "/categories", "json");
        boolean categoryExists = false;

        try {
            // Get the response body as a JSON object
            JSONObject getTodoResponseBody = new JSONObject(getTodo.body().string());
            // Get the "Categories" array from the response body
            JSONArray categoriesArray = getTodoResponseBody.getJSONArray("categories");
            // Loop through the categories in the array and check if the specified category exists
            for (Object obj : categoriesArray) {
                JSONObject category = (JSONObject) obj;
                String categoryIdStr = category.getString("id");
                if (categoryIdStr.equals(categoryId)) {
                    // If the todos with the specified id is found, set the flag to true and exit the loop
                    categoryExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert that the specified category does not exist for given todoId
        assertEquals(false, categoryExists);
    }

    @When("I add a relationship between todo with id {string} and category with id {string} using id in endpoint")
    public void i_add_a_relationship_between_todo_with_id_and_category_with_id_using_id_in_endpoint(String todoId, String categoryId) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create a JSON object with an "id" field containing the category id
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", categoryId);
        System.out.println(requestBody);

        // Create relationship categories between todos and catagories using a POST request
        response = call.todosPost("todos/" + todoId + "/categories", "json", requestBody);

    }

    @When("I add a relationship between todo with id {string} and category with id {string} using put method")
    public void i_add_a_relationship_between_todo_with_id_and_category_with_id_using_put_method(String todoId, String categoryId) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create a JSON object with an "id" field containing the category id
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "updated todo");
        JSONArray categoriesArray = new JSONArray();
        JSONObject categoryObject = new JSONObject();
        categoryObject.put("id", categoryId);
        categoriesArray.put(categoryObject);
        requestBody.put("categories", categoriesArray);
        // Create relationship categories between todos and catagories using a PUT request
        response = call.todosPut("todos/" + todoId, "json", requestBody);
    }

    @When("I add a relationship between todo with id {string} and nonexistent category with id {string} using id in endpoint")
    public void i_add_a_relationship_between_todo_with_id_and_nonexistent_category_with_id_using_id_in_endpoint(String todoId, String categoryId) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create a JSON object with an "id" field containing the category id
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", categoryId);
        System.out.println(requestBody);

        // Create relationship categories between todos and catagories using a POST request
        response = call.todosPost("todos/" + todoId + "/categories", "json", requestBody);
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Then("a new categories relationship is created between todo with id {string} and category with id {string}")
    public void a_new_categories_relationship_is_created_between_todo_with_id_and_category_with_id(String todoId, String categoryId) {
        RestApiCall call = new RestApiCall();

        // Send a GET request to retrieve all todos
        Response getTodo = call.todosGet("todos/" + todoId + "/categories", "json");
        boolean categoryExists = false;

        try {
            // Get the response body as a JSON object
            JSONObject getTodoResponseBody = new JSONObject(getTodo.body().string());
            // Get the "Categories" array from the response body
            JSONArray categoriesArray = getTodoResponseBody.getJSONArray("categories");
            // Loop through the categories in the array and check if the specified category exists
            for (Object obj : categoriesArray) {
                JSONObject category = (JSONObject) obj;
                String categoryIdStr = category.getString("id");
                if (categoryIdStr.equals(categoryId)) {
                    // If the category with the specified id is found, set the flag to true and exit the loop
                    categoryExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Assert that the specified category now exists for given todoId
        assertEquals(true, categoryExists);
    }


    // CODES AND MESSAGES
    @Then("a success message {string} with status code {string} is returned")
    public void a_success_message_with_status_code_is_returned(String successMessage, String successCode) {
         assertEquals(successMessage, response.message() );
         assertEquals(Integer.parseInt(successCode), response.code() );
    }


    @Then("an error message {string} with status code {string} is returned")
    public void an_error_message_with_status_code_is_returned(String errorMessage, String errorCode) {
        JSONArray errorMessages = responseBody.getJSONArray("errorMessages");
        String responseErrorMessage = errorMessages.getString(0);

        assertEquals(errorMessage, responseErrorMessage);
        assertEquals(Integer.parseInt(errorCode), response.code());
    }


}
