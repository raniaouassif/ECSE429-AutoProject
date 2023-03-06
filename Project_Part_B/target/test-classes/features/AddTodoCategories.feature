Feature: Add Todo Category
  As a user,
  I want to add a categories relationship between a todo and category
  So that I can easily organize and categorize my todos

  Background:
    Given the service is running

  Scenario Outline: Create a categories relationship between todo and category instance by id in endpoint (Normal Flow)
    Given the todo with id "<todoId>" exists
    And the category with id "<categoryId>" exists
    And there are no relationship between todo with id "<todoId>" and category with id "<categoryId>"
    When I add a relationship between todo with id "<todoId>" and category with id "<categoryId>" using id in endpoint
    Then a status code "201" with response phrase "Created" is returned
    And a new categories relationship is created between todo with id "<todoId>" and category with id "<categoryId>"
    Examples:
      | todoId | categoryId |
      | 2      | 1          |
      | 2      | 2          |
      | 1      | 2          |


  Scenario Outline: Create a categories relationship between todo and category instance using put method (Normal Flow)
    Given the todo with id "<todoId>" exists
    And the category with id "<categoryId>" exists
    And there are no relationship between todo with id "<todoId>" and category with id "<categoryId>"
    When I add a relationship between todo with id "<todoId>" and category with id "<categoryId>" using put method
    Then a status code "200" with response phrase "OK" is returned
    And a new categories relationship is created between todo with id "<todoId>" and category with id "<categoryId>"
    Examples:
      | todoId | categoryId |
      | 2      | 1          |
      | 2      | 2          |
      | 1      | 2          |

  Scenario Outline: Create a relationship between nonexistent todo and category (Error Flow)
    Given the todo with id "<todoId>" does not exist
    And the category with id "<categoryId>" exists
    When I add a relationship between todo with id "<todoId>" and nonexistent category with id "<categoryId>" using id in endpoint
    Then a status code "404" with response phrase "Not Found" is returned
    And the response body has the error message "<errorMessage>"
    Examples:
      | todoId | categoryId | errorMessage                                                      |
      | 0      | 1          | Could not find parent thing for relationship todos/0/categories   |
      | 100    | 2          | Could not find parent thing for relationship todos/100/categories |