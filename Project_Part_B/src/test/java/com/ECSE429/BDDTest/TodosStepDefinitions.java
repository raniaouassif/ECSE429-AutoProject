package com.ECSE429.BDDTest;

import com.ECSE429.API.RestApiCall;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodosStepDefinitions {

    Response response;
    JSONObject responseBody;
    int previousTodosCount;
    int currentTodosCount;

    List<String> todoCategoriesIds;

    //BACKGROUND
    @Given("the service is running")
    public void the_service_is_running() {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Verify that the server is running
        response = call.checkService();
        assertEquals(200, response.code());
    }

    //POST TODOS
    @When("I add a new todo with title {string}, description {string} and doneStatus {string}")
    public void i_add_a_new_todo_with_title_description_and_done_status(String todoTitle, String todoDescription, String todoDoneStatus) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create JSONObject to store response body of the two getRequest() API Calls
        JSONObject getTodosResponseBody = null;

        // First API call to retrieve the current list of todos
        Response getPreviousTodos = call.getRequest("todos", "json");
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
        response = call.postRequest("todos", "json", requestBody);
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Third API call to retrieve the updated list of todos after adding the new todos
        Response getCurrentTodos = call.getRequest("todos", "json");
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

        // Create JSONObject to store response body of the two getRequest() API Calls
        JSONObject getTodosResponseBody = null;

        // First API call to retrieve the current list of todos
        Response getPreviousTodos = call.getRequest("todos", "json");
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
        response = call.postRequest("todos", "json", requestBody);
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Third API call to retrieve the updated list of todos after adding the new todos
        Response getCurrentTodos = call.getRequest("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getCurrentTodos.body().string());
            currentTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("I add a new todos with title {string} and invalid field {string}")
    public void i_add_a_new_todos_with_title_and_invalid_field(String todoTitle, String invalidField) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create JSONObject to store response body of the two getRequest() API Calls
        JSONObject getTodosResponseBody = null;

        // First API call to retrieve the current list of todos
        Response getPreviousTodos = call.getRequest("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getPreviousTodos.body().string());
            previousTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JSON object with a "title" field containing the specified value
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", todoTitle);
        //Add invalid field to the JSON requestBody
        requestBody.put(invalidField, "value");

        // Second API call to add a new todos using a POST request
        response = call.postRequest("todos", "json", requestBody);
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Third API call to retrieve the updated list of todos after adding the new todos
        Response getCurrentTodos = call.getRequest("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getCurrentTodos.body().string());
            currentTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("I add a new todos with title {string} and duplicate fields {string}")
    public void i_add_a_new_todos_with_title_and_duplicate_fields(String todoTitle, String duplicateField) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create JSONObject to store response body of the two getRequest() API Calls
        JSONObject getTodosResponseBody = null;

        // First API call to retrieve the current list of todos
        Response getPreviousTodos = call.getRequest("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getPreviousTodos.body().string());
            previousTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JSON object with a "title" field containing the specified value
        // And the duplicate fields
        String requestBody = "{\n" +
                "    \"title\":\"The title\",\n" +
                "    \"" + duplicateField + "\": \"some Value\",\n" +
                "    \"" + duplicateField + "\": \"Other value for field\"\n" +
                "}";

        System.out.println(requestBody);
        // Second API call to add a new todos using a POST request
        response = call.postRequestString("todos", "json", requestBody);

        try {
            responseBody = new JSONObject(response.body().string());
            System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Third API call to retrieve the updated list of todos after adding the new todos
        Response getCurrentTodos = call.getRequest("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getCurrentTodos.body().string());
            currentTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("I add a new todos with title {string} with invalid key-value pairs separator {string}")
    public void i_add_a_new_todos_with_title_with_invalid_key_value_pairs_separator(String todoTitle, String keyPairSeparator) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create JSONObject to store response body of the two getRequest() API Calls
        JSONObject getTodosResponseBody = null;

        // First API call to retrieve the current list of todos
        Response getPreviousTodos = call.getRequest("todos", "json");
        try {
            getTodosResponseBody = new JSONObject(getPreviousTodos.body().string());
            previousTodosCount = getTodosResponseBody.getJSONArray("todos").length();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JSON object with a "title" field containing the specified value
        // And using the specified key pair separator
        String requestBody = "{\n" +
                " \"title\"" + keyPairSeparator + "\"" + todoTitle + "\"\n}";

        System.out.println(requestBody);
        // Second API call to add a new todos using a POST request
        response = call.postRequestString("todos", "json", requestBody);

        try {
            responseBody = new JSONObject(response.body().string());
            System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Third API call to retrieve the updated list of todos after adding the new todos
        Response getCurrentTodos = call.getRequest("todos", "json");
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

    // GET TODOS BY ID
    @Given("the todo with id {string} exists")
    public void the_todo_with_id_exists(String todoId) {
        // Create a RestApiCall object to make API requests
        RestApiCall call = new RestApiCall();

        // Set a flag to keep track of whether the specified todos exists
        boolean todoExists = false;

        // Send a GET request to retrieve all todos
        Response getTodos = call.getRequest("todos", "json");

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
        Response getTodos = call.getRequest("todos", "json");

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
        response = call.getRequest("todos/" + todoId, "json");
        try {
            responseBody = new JSONObject(response.body().string());
            System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("I filter the endpoint to get the todo with id {string}")
    public void i_filter_the_endpoint_to_get_the_todo_with_id(String todoId) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Get a todos using filter query with id equal to todoId in endpoint
        response = call.getRequest("todos?id=" + todoId, "json");
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

        // Send a GET request to retrieve all categories
        Response getCategories = call.getRequest("categories", "json");

        try {
            // Get the response body as a JSON object
            JSONObject getCategoriesResponseBody = new JSONObject(getCategories.body().string());
            // Get the "category" array from the response body
            JSONArray categoriesArray = getCategoriesResponseBody.getJSONArray("categories");
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
        // Assert that the specified category exists
        assertEquals(true, categoryExists);
    }

    @Given("there are no relationship between todo with id {string} and category with id {string}")
    public void there_are_no_relationship_between_todo_with_id_and_category_with_id(String todoId, String categoryId) {
        RestApiCall call = new RestApiCall();

        // Send a GET request to retrieve all todos
        Response getTodo = call.getRequest("todos/" + todoId + "/categories", "json");
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

        // Create relationship categories between todos and catagories using a POST request
        response = call.postRequest("todos/" + todoId + "/categories", "json", requestBody);
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
        response = call.putRequest("todos/" + todoId, "json", requestBody);
    }

    @When("I add a relationship between todo with id {string} and nonexistent category with id {string} using id in endpoint")
    public void i_add_a_relationship_between_todo_with_id_and_nonexistent_category_with_id_using_id_in_endpoint(String todoId, String categoryId) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create a JSON object with an "id" field containing the category id
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", categoryId);

        // Create relationship categories between todos and catagories using a POST request
        response = call.postRequest("todos/" + todoId + "/categories", "json", requestBody);
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
        Response getTodo = call.getRequest("todos/" + todoId + "/categories", "json");
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

    // DELETE TASKSOF TODOS
    @Given("the project with id {string} exists")
    public void the_project_with_id_exists(String projectId) {
        // Create a RestApiCall object to make API requests
        RestApiCall call = new RestApiCall();

        // Set a flag to keep track of whether the specified todos exists
        boolean projectExists = false;

        // Send a GET request to retrieve all todos
        Response getProjects = call.getRequest("projects", "json");

        try {
            // Get the response body as a JSON object
            JSONObject getProjectsResponseBody = new JSONObject(getProjects.body().string());
            // Get the "projects" array from the response body
            JSONArray projectsArray = getProjectsResponseBody.getJSONArray("projects");
            // Loop through the projects in the array and check if the specified project exists
            for (Object obj : projectsArray) {
                JSONObject project = (JSONObject) obj;
                String projectIdStr = project.getString("id");
                if (projectIdStr.equals(projectId)) {
                    // If the project with the specified id is found, set the flag to true and exit the loop
                    projectExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert that the specified project exists
        assertEquals(true, projectExists);
    }

    @Given("there is a tasksof relationship between todo with id {string} and project with id {string}")
    public void there_is_a_tasksof_relationship_between_todo_with_id_and_project_with_id(String todoId, String projectId) {
        RestApiCall call = new RestApiCall();

        // Send a GET request to retrieve todos with todoId
        Response getTodo = call.getRequest("todos/" + todoId, "json");
        boolean tasksofExists = false;
        try {
            // Get the response body as a JSON object
            JSONObject getTodoResponseBody = new JSONObject(getTodo.body().string());
            // Get the "todos" array from the response body
            JSONArray todosArray = getTodoResponseBody.getJSONArray("todos");
            // Get the first element of the "todos" array
            JSONObject todoObject = todosArray.getJSONObject(0);
            // Get the "tasksof" array from the todos object
            JSONArray tasksofArray = todoObject.getJSONArray("tasksof");

            // Loop through the tasksof in the array and check if the specified project exists
            for (Object obj : tasksofArray) {
                JSONObject tasksof = (JSONObject) obj;
                String tasksofIdStr = tasksof.getString("id");
                if (tasksofIdStr.equals(projectId)) {
                    // If the todos with the specified id is found, set the flag to true and exit the loop
                    tasksofExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert that the specified category does not exist for given todoId
        assertEquals(true, tasksofExists);
    }

    @Given("the project with id {string} does not exist")
    public void the_project_with_id_does_not_exist(String projectId) {
        // Create a RestApiCall object to make API requests
        RestApiCall call = new RestApiCall();

        // Set a flag to keep track of whether the specified todos exists
        boolean projectExists = false;

        // Send a GET request to retrieve all todos
        Response getProjects = call.getRequest("projects", "json");

        try {
            // Get the response body as a JSON object
            JSONObject getProjectsResponseBody = new JSONObject(getProjects.body().string());
            // Get the "projects" array from the response body
            JSONArray projectsArray = getProjectsResponseBody.getJSONArray("projects");
            // Loop through the projects in the array and check if the specified project exists
            for (Object obj : projectsArray) {
                JSONObject project = (JSONObject) obj;
                String projectIdStr = project.getString("id");
                if (projectIdStr.equals(projectId)) {
                    // If the project with the specified id is found, set the flag to true and exit the loop
                    projectExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert that the specified project exists
        assertEquals(false, projectExists);
    }

    @When("I delete the tasksof relationship between todo with id {string} and project with id {string} using id in endpoint")
    public void i_delete_the_tasksof_relationship_between_todo_with_id_and_project_with_id_using_id_in_endpoint(String todoId, String projectId) {
        RestApiCall call = new RestApiCall();
        response = call.deleteRequest("todos/" + todoId + "/tasksof/" + projectId, "json");
    }

    @When("I delete the tasksof relationship between todo with id {string} and project with id {string} using put method")
    public void i_delete_the_tasksof_relationship_between_todo_with_id_and_project_with_id_using_put_method(String todoId, String projectId) {
        // Create a RestApiCall object to handle the API request
        RestApiCall call = new RestApiCall();

        // Create a JSON object with an "id" field containing the category id
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "updated todo");

        // Create relationship categories between todos and catagories using a PUT request
        response = call.putRequest("todos/" + todoId, "json", requestBody);
    }

    @When("I delete the nonexistent tasksof relationship between todo with id {string} and project with id {string} using id in endpoint")
    public void i_delete_the_nonexistent_tasksof_relationship_between_todo_with_id_and_project_with_id_using_id_in_endpoint(String todoId, String projectId) {
        RestApiCall call = new RestApiCall();
        response = call.deleteRequest("todos/" + todoId + "/tasksof/" + projectId, "json");
        try {
            responseBody = new JSONObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Then("the tasksof relationship between todo with id {string} and project with id {string} is deleted")
    public void the_tasksof_relationship_between_todo_with_id_and_project_with_id_is_deleted(String todoId, String projectId) {
        RestApiCall call = new RestApiCall();
        // Send a GET request to retrieve todos with todoId
        Response getTodo = call.getRequest("todos/" + todoId, "json");
        boolean tasksofExists = false;
        try {
            // Get the response body as a JSON object
            JSONObject getTodoResponseBody = new JSONObject(getTodo.body().string());
            if(!getTodoResponseBody.isNull("tasksof")) {
                // Get the "tasksof" array from the response body
                JSONArray tasksofArray = getTodoResponseBody.getJSONArray("tasksof");
                // Loop through the projects in the array and check if the specified project exists
                for (Object obj : tasksofArray) {
                    JSONObject tasksof = (JSONObject) obj;
                    String tasksofIdStr = tasksof.getString("id");
                    if (tasksofIdStr.equals(projectId)) {
                        // If the todos with the specified id is found, set the flag to true and exit the loop
                        tasksofExists = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert that the specified category does not exist for given todoId
        assertEquals(false, tasksofExists);
    }

    // VIEW TODOS CATEGORIES RELATIONSHIP
    @When("I get the todo's categories with id {string} using categories endpoint")
    public void i_get_the_todo_s_categories_with_id_using_categories_endpoint(String todoId) {
        // Initialize the todoCategoriesIds list
        todoCategoriesIds = new ArrayList<>();

        // Send a GET request to the endpoint to get the todos categories
        RestApiCall call = new RestApiCall();
        response = call.getRequest("todos/" + todoId + "/categories", "json");

        // Parse the response body and extract the category ids
        try {
            responseBody = new JSONObject(response.body().string());

            // Should return object with key categories
            JSONArray categoriesArray = responseBody.getJSONArray("categories");
            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject category = categoriesArray.getJSONObject(i);
                String categoryId = category.getString("id");
                todoCategoriesIds.add(categoryId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("I get the todo's categories with id {string} using todo endpoint")
    public void i_get_the_todo_s_categories_with_id_using_todo_endpoint(String todoId) {
        // Initialize the todoCategoriesIds list
        todoCategoriesIds = new ArrayList<>();

        // Send a GET request to the endpoint to get the todos
        RestApiCall call = new RestApiCall();
        response = call.getRequest("todos/" + todoId, "json");

        try {
            responseBody = new JSONObject(response.body().string());
            JSONArray todosArray = responseBody.getJSONArray("todos");
            JSONObject todo = todosArray.getJSONObject(0);

            // If the todos has at least one categories
            // It should have the key categories
            if(todo.has("categories")){
                JSONArray categoriesArray = todo.getJSONArray("categories");
                // Populate todoCategoriesIds with the category ids
                for (int i = 0; i < categoriesArray.length(); i++) {
                    JSONObject category = categoriesArray.getJSONObject(i);
                    String categoryId = category.getString("id");
                    todoCategoriesIds.add(categoryId);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Then("I should see all the todo related categories with ids {string}")
    public void i_should_see_all_the_todo_related_categories_with_ids(String categoryIds) {
        // Split the expected category ids string into a list of ids if the categoryIds is not empty
        List<String> expectedCategoriesIds;
        expectedCategoriesIds = categoryIds.isEmpty() ? new ArrayList<>() : Arrays.asList(categoryIds.split(",")) ;

        // Sort the categories array in case the order of the elements in the expected and actual lists is different
        Collections.sort(expectedCategoriesIds);
        Collections.sort(todoCategoriesIds);

        assertEquals(expectedCategoriesIds,todoCategoriesIds);
    }

    // CODES AND MESSAGES
    @Then("a status code {string} with response phrase {string} is returned")
    public void a_status_code_with_response_phrase_is_returned(String statusCode, String responsePhrase) {
        assertEquals(Integer.parseInt(statusCode), response.code(),
                "ERROR: The response phrase is: " + response.message() +
                        "\n instead of : " + responsePhrase );
        assertEquals(responsePhrase, response.message() );
    }

    @Then("the response body has the error message {string}")
    public void the_response_body_has_the_error_message(String errorMessage) {
        //Check that the first key of the response body is "errorMessages"
        assertEquals("errorMessages", responseBody.keySet().iterator().next(), "\n ERROR: The response body does not have a key \"errorMessage\". \n The response body is : " + responseBody + "\n And should return error message: \n" + errorMessage);

        JSONArray errorMessages = responseBody.getJSONArray("errorMessages");
        String responseErrorMessage = errorMessages.getString(0);
        assertEquals(errorMessage, responseErrorMessage);
    }
}
