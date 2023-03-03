Feature: Get Todo By Id
  As a user,
  I want to get a todo by id, so I can edit it, or assign it to projects and categories

  Background:
    Given the service is running

  Scenario Outline: Get a todo by id in endpoint (Normal Flow)
    Given the todo with id "<todoId>" exists
    When I get the todo with id "<todoId>"
    Then a success message "<successMessage>" with status code "<successCode>" is returned
    And I should see a response of one todo with id "<todoId>"
    Examples:
      | todoId  | successCode | successMessage |
      | 1       | 200         | OK             |
      | 2       | 200         | OK             |

  Scenario Outline: Get a todo by filtering endpoint with id (Alternate Flow)
    Given the todo with id "<todoId>" exists
    When I filter the endpoint to get the todo with id "<todoId>"
    Then a success message "<successMessage>" with status code "<successCode>" is returned
    And I should see a response of one todo with id "<todoId>"
    Examples:
      | todoId  | successCode | successMessage |
      | 1       | 200         | OK             |
      | 2       | 200         | OK             |

  Scenario Outline: Get a nonexistent todo (Error Flow)
    Given the todo with id "<todoId>" does not exist
    When I get the todo with id "<todoId>"
    Then an error message "<errorMessage>" with status code "<errorCode>" is returned
    And no todo is returned
    Examples:
      | todoId  | errorCode   | errorMessage                                |
      | 0       | 404         | Could not find an instance with todos/0     |
      | 100     | 404         | Could not find an instance with todos/100   |

