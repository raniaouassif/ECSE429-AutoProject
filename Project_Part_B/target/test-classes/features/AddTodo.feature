Feature: Post Todo
  As a user,
  I want to post a todo, so I can manage my work progress.

  Background:
    Given the service is running

  Scenario Outline: Create a new todo with title, description and doneStatus(Normal Flow)
    When I add a new todo with title "<todoTitle>", description "<todoDescription>" and doneStatus "<todoDoneStatus>"
    Then a success message "<successMessage>" with status code "<successCode>" is returned
    And I should see one new todo with title "<todoTitle>", description "<todoDescription>" and doneStatus "<todoDoneStatus>" within the application
    Examples:
      | todoTitle                    | todoDescription      | todoDoneStatus | successCode | successMessage |
      | Record Unit Test Suite Video | For ECSE429 project  | false          | 201         | Created        |
      | Complete quiz 3              | For FACC400          | true           | 201         | Created        |
      | with empty Desc              |                      | true           | 201         | Created        |

  Scenario Outline: Create a new todo with title only (Alternate Flow)
    When I add a new todo with title "<todoTitle>"
    Then a success message "<successMessage>" with status code "<successCode>" is returned
    And I should see one new todo with title "<todoTitle>", description "<todoDescription>" and doneStatus "<todoDoneStatus>" within the application
    Examples:
      | todoTitle                    | todoDescription      | todoDoneStatus | successCode | successMessage |
      | Record Unit Test Suite Video |                      | false          | 201         | Created        |
      | Complete quiz 3              |                      | false          | 201         | Created        |
      | with empty Desc              |                      | false          | 201         | Created        |

  Scenario Outline: Create a new todo with empty title (Error Flow)
    When I add a new todo with title "<todoTitle>"
    Then an error message "<errorMessage>" with status code "<errorCode>" is returned
    And no todo is created
    Examples:
      | todoTitle                    | errorCode | errorMessage                                 |
      |                              | 400       | Failed Validation: title : can not be empty  |

#  Scenario Outline: Create a new todo with invalid field  (Error Flow)
#    When I add a new todo with title "<todoTitle>" and invalid field "<invalidField>"
#    Then an error message "<errorMessage>" is returned
#    And no todo is created
#
#    Examples:
#      | todoTitle                    | invalidField        | errorMessage                         |
#      | Record Unit Test Suite Video | field invalid       | Could not find field: field invalid  |
#      | Complete quiz 3              | descriptio\n        |
#      | with empty invalid field     | "title"             |