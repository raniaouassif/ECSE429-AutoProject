Feature: Post Todo
  As a user,
  I want to post a todo, so I can manage my work progress.

  Background:
    Given the service is running

  Scenario Outline: Create a new todo with title, description and doneStatus (Normal Flow)
    When I add a new todo with title "<todoTitle>", description "<todoDescription>" and doneStatus "<todoDoneStatus>"
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And I should see one new todo with title "<todoTitle>", description "<todoDescription>" and doneStatus "<todoDoneStatus>" within the application
    Examples:
      | todoTitle                    | todoDescription      | todoDoneStatus | statusCode   | responsePhrase  |
      | Record Unit Test Suite Video | For ECSE429 project  | false          | 201          | Created         |
      | Complete quiz 3              | For FACC400          | true           | 201          | Created         |
      | with empty Desc              |                      | true           | 201          | Created         |

  Scenario Outline: Create a new todo with title only (Alternate Flow)
    When I add a new todo with title "<todoTitle>"
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And I should see one new todo with title "<todoTitle>", description "<todoDescription>" and doneStatus "<todoDoneStatus>" within the application
    Examples:
      | todoTitle                    | todoDescription      | todoDoneStatus | statusCode  | responsePhrase |
      | Record Unit Test Suite Video |                      | false          | 201         | Created        |
      | Complete quiz 3              |                      | false          | 201         | Created        |
      | with empty Desc              |                      | false          | 201         | Created        |

  Scenario Outline: Create a new todo with empty title (Error Flow)
    When I add a new todo with title "<todoTitle>"
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And the response body has the error message "<errorMessage>"
    And no todo is created
    Examples:
      | todoTitle | statusCode | responsePhrase | errorMessage                                |
      |           | 400        | Bad Request    | Failed Validation: title : can not be empty |

  Scenario Outline: Create a new todos with invalid field  (Error Flow)
    When I add a new todos with title "<todoTitle>" and invalid field "<invalidField>"
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And the response body has the error message "<errorMessage>"
    And no todo is created
    Examples:
      | todoTitle                    | invalidField  | statusCode | responsePhrase | errorMessage                         |
      | Record Unit Test Suite Video | field invalid | 400        | Bad Request    | Could not find field: field invalid  |
      | Complete quiz 3              | descriptio\n  | 400        | Bad Request    | Could not find field: descriptio\n   |

  Scenario Outline: Create a new todos with duplicate fields  (Error Flow)
    When I add a new todos with title "<todoTitle>" and duplicate fields "<duplicateField>"
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And the response body has the error message "<errorMessage>"
    And no todo is created
    Examples:
      | todoTitle                    | duplicateField  | statusCode | responsePhrase | errorMessage                 |
      | Record Unit Test Suite Video | title           | 400        | Bad Request    | duplicate key: title         |
      | Complete quiz 3              | doneStatus      | 400        | Bad Request    | duplicate key: doneStatus    |
      | Implement StepDefinitions    | description     | 400        | Bad Request    | duplicate key: description   |

