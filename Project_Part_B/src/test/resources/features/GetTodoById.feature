Feature: Get Todo By Id
  As a user,
  I want to get a todo by id, so I can edit it, or assign it to projects and categories

  Background:
    Given the service is running
    And I add a new todo with title "New Todo"

  Scenario Outline: Get a todo by id in endpoint (Normal Flow)
    Given the todo with id "<todoId>" exists
    When I get the todo with id "<todoId>"
    Then a status code "200" with response phrase "OK" is returned
    And I should see a response of one todo with id "<todoId>"
    Examples:
      | todoId  |
      | 1       |
      | 2       |
      | 3       |

  Scenario Outline: Get a todo by filtering endpoint with id (Alternate Flow)
    Given the todo with id "<todoId>" exists
    When I filter the endpoint to get the todo with id "<todoId>"
    Then a status code "200" with response phrase "OK" is returned
    And I should see a response of one todo with id "<todoId>"
    Examples:
      | todoId  |
      | 1       |
      | 2       |
      | 3       |

  Scenario Outline: Get a nonexistent todo (Error Flow)
    Given the todo with id "<todoId>" does not exist
    When I get the todo with id "<todoId>"
    Then a status code "404" with response phrase "Not Found" is returned
    And the response body has the error message "<errorMessage>"
    And no todo is returned
    Examples:
      | todoId | errorMessage                              |
      | 0      | Could not find an instance with todos/0   |
      | 100    | Could not find an instance with todos/100 |

  Scenario Outline: Get a todo with invalid id (Error Flow)
    Given the todo with id "<invalidId>" does not exist
    When I get the todo with id "<invalidId>"
    Then a status code "404" with response phrase "Not Found" is returned
    And the response body has the error message "<errorMessage>"
    And no todo is returned
    Examples:
      | invalidId | errorMessage                                    |
      | a         | Could not find an instance with todos/a         |
      | -         | Could not find an instance with todos/-         |
      | *         | Could not find an instance with todos/*         |
      | ;         | Could not find an instance with todos/;         |
      | ;;;;;;    | Could not find an instance with todos/;;;;;;    |
      | hello     | Could not find an instance with todos/hello     |



