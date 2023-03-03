Feature: Post Todo Category
  As a user,
  I want to add a category to a todo so I can classify it

  Background:
    Given the service is running

  Scenario Outline: Create an instance of a relationship between todo and category instance by id in endpoint (Normal Flow)
    Given the todo with id "<todoId>" exists
    And the category with id "<categoryId>" exists
    And there are no relationship between todo with id "<todoId>" and category with id "<categoryId>"
    When I add a relationship between todo with id "<todoId>" and category with id "<categoryId>" using id in endpoint
    Then a success message "<successMessage>" with status code "<successCode>" is returned
    And a new categories relationship is created between todo with id "<todoId>" and category with id "<categoryId>"
    Examples:
      | todoId  | categoryId | successCode | successMessage |
      | 2       | 1          | 201         | Created        |

  Scenario Outline: Create an instance of a relationship between todo and category instance using put method (Normal Flow)
    Given the todo with id "<todoId>" exists
    And the category with id "<categoryId>" exists
    And there are no relationship between todo with id "<todoId>" and category with id "<categoryId>"
    When I add a relationship between todo with id "<todoId>" and category with id "<categoryId>" using put method
    Then a success message "<successMessage>" with status code "<successCode>" is returned
    And a new categories relationship is created between todo with id "<todoId>" and category with id "<categoryId>"
    Examples:
      | todoId  | categoryId | successCode | successMessage |
      | 2       | 1          | 200         | OK             |

  Scenario Outline: Create a relationship between nonexistent todo and category (Error Flow)
    Given the todo with id "<todoId>" does not exist
    And the category with id "<categoryId>" exists
    When I add a relationship between todo with id "<todoId>" and nonexistent category with id "<categoryId>" using id in endpoint
    Then an error message "<errorMessage>" with status code "<errorCode>" is returned
    Examples:
      | todoId  | categoryId | errorCode   | errorMessage                                                       |
      | 0       | 1          | 404         | Could not find parent thing for relationship todos/0/categories    |
      | 100     | 2          | 404         | Could not find parent thing for relationship todos/100/categories  |