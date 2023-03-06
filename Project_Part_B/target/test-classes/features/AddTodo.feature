Feature: Add Todo
  As a user,
  I want to post a todo, so I can manage my work progress.

  Background:
    Given the service is running

  Scenario Outline: Create a new todo with title, description and doneStatus (Normal Flow)
    When I add a new todo with title "<todoTitle>", description "<todoDescription>" and doneStatus "<todoDoneStatus>"
    Then I should see one new todo with title "<todoTitle>", description "<todoDescription>" and doneStatus "<todoDoneStatus>" within the application
    And a status code "201" with response phrase "Created" is returned
    Examples:
      | todoTitle                    | todoDescription      | todoDoneStatus |
      | Record Unit Test Suite Video | For ECSE429 project  | false          |
      | Complete quiz 3              | For FACC400          | true           |
      | with empty Desc              |                      | true           |
      | !()                          | no AlphaChar         | false          |

  Scenario Outline: Create a new todo with title only (Alternate Flow)
    When I add a new todo with title "<todoTitle>"
    Then I should see one new todo with title "<todoTitle>", description "" and doneStatus "false" within the application
    And a status code "201" with response phrase "Created" is returned
    Examples:
      | todoTitle                    |
      | Record Unit Test Suite Video |
      | Complete quiz 3              |
      | with empty Desc              |

  Scenario: Create a new todo with empty title (Error Flow)
    When I add a new todo with title ""
    Then no todo is created
    And the response body has the error message "Failed Validation: title : can not be empty"
    And a status code "400" with response phrase "Bad Request" is returned

  Scenario Outline: Create a new todos with invalid field  (Error Flow)
    When I add a new todos with title "MyTodo" and invalid field "<invalidField>"
    Then no todo is created
    And the response body has the error message "<errorMessage>"
    And a status code "400" with response phrase "Bad Request" is returned
    Examples:
      | invalidField  | errorMessage                                                        |
      | field invalid | Could not find field: field invalid                                 |
      | descriptio\n  | Could not find field: descriptio\n                                  |
      | nonexistent   | Could not find field: nonexistent                                   |
      | doneeStatus   | Could not find field: doneeStatus                                   |
      | titl          | Could not find field: titl                                          |
      | id            | Invalid Creation: Failed Validation: Not allowed to create with id  |

  Scenario Outline: Create a new todos with id in body  (Error Flow)
    When I add a new todos with title "MyTodo" and field "<idField>" set to value "<fieldValue>" of type "<idType>"
    Then no todo is created
    And the response body has the error message "Invalid Creation: Failed Validation: Not allowed to create with id"
    And a status code "400" with response phrase "Bad Request" is returned
    Examples:
      | idField | fieldValue  | idType  |
      | id      | 100         | String  |
      | ID      | 10          | Int     |
      | Id      | 2           | Int     |
      | iD      | 3           | Int     |

  Scenario Outline: Create a new todos with invalid field type (Error Flow)
    When I add a new todos with title "MyTodo" and field "<todoField>" set to value "<fieldValue>" of type "<fieldType>"
    Then no todo is created
    And the response body has the error message "<errorMessage>"
    And a status code "400" with response phrase "Bad Request" is returned
    Examples:
      | todoField      | fieldValue  | fieldType  | errorMessage                                    |
      | doneStatus     | false       | String     | Failed Validation: doneStatus should be BOOLEAN |
      | doneStatus     | 2           | Int        | Failed Validation: doneStatus should be BOOLEAN |
      | description    | 2           | Int        | Failed Validation: description should be STRING |
      | description    | false       | BOOLEAN    | Failed Validation: description should be STRING |
      | description    | 900.12      | FLOAT      | Failed Validation: description should be STRING |
      | doneStatus     | 2.0         | FLOAT      | Failed Validation: doneStatus should be BOOLEAN |

  Scenario Outline: Create a new todos with duplicate fields  (Error Flow)
    When I add a new todos with title "MyTodo" and duplicate fields "<duplicateField>"
    Then no todo is created
    And the response body has the error message "<errorMessage>"
    And a status code "400" with response phrase "Bad Request" is returned
    Examples:
      | duplicateField  | errorMessage                 |
      | title           | duplicate key: title         |
      | doneStatus      | duplicate key: doneStatus    |
      | description     | duplicate key: description   |


  # Fails with example =
  Scenario Outline: Create a new todos with malformed JSON due to invalid key-value separator (Error Flow)
    When I add a new todos with title "MyTodo" with invalid key-value pairs separator "<keyPairSeparator>"
    Then no todo is created
    And the response body has the error message "com.google.gson.stream.MalformedJsonException: Expected ':' at line 2 column 10 path $."
    And a status code "400" with response phrase "Bad Request" is returned
    Examples:
       | keyPairSeparator |
       | 0                |
       | !                |
       | -                |
       |                  |
       | =                |
       | ,                |
       | title            |
       | ;                |
       | b                |



