Feature: Get Todo By Id
  As a user,
  I want to get a todo by id, so I can edit it, or assign it to projects and categories

  Background:
    Given the service is running

  Scenario Outline: Get a todo by id in endpoint (Normal Flow)
    Given the todo with id "<todoId>" exists
    When I get the todo with id "<todoId>"
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And I should see a response of one todo with id "<todoId>"
    Examples:
      | todoId  | statusCode   | responsePhrase   |
      | 1       | 200          | OK               |
      | 2       | 200          | OK               |

  Scenario Outline: Get a todo by filtering endpoint with id (Alternate Flow)
    Given the todo with id "<todoId>" exists
    When I filter the endpoint to get the todo with id "<todoId>"
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And I should see a response of one todo with id "<todoId>"
    Examples:
      | todoId  | statusCode   | responsePhrase   |
      | 1       | 200          | OK               |
      | 2       | 200          | OK               |

  Scenario Outline: Get a nonexistent todo (Error Flow)
    Given the todo with id "<todoId>" does not exist
    When I get the todo with id "<todoId>"
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And the response body has the error message "<errorMessage>"
    And no todo is returned
    Examples:
      | todoId | statusCode | responsePhrase | errorMessage                              |
      | 0      | 404        | Not Found      | Could not find an instance with todos/0   |
      | 100    | 404        | Not Found      | Could not find an instance with todos/100 |

