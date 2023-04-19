Feature:
  As a user,
  I want to delete a tasksof relationship between a todo and project
  So that I can manage my projects task list.

  Background:
    Given the service is running

  Scenario Outline: Delete a tasksof relationship between todo and project by id in endpoint (Normal Flow)
    Given the todo with id "<todoId>" exists
    And the project with id "<projectId>" exists
    And there is a tasksof relationship between todo with id "<todoId>" and project with id "<projectId>"
    When I delete the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using id in endpoint
    Then the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" is deleted
    And a status code "200" with response phrase "OK" is returned
    Examples:
      | todoId | projectId |
      | 2      | 1         |
      | 1      | 1         |

  Scenario Outline: Delete a tasksof relationship between todo and project using put method (Alternate Flow)
    Given the todo with id "<todoId>" exists
    And the project with id "<projectId>" exists
    And there is a tasksof relationship between todo with id "<todoId>" and project with id "<projectId>"
    When I delete the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using put method
    Then the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" is deleted
    And a status code "200" with response phrase "OK" is returned
    Examples:
      | todoId | projectId |
      | 2      | 1         |
      | 1      | 1         |

  Scenario Outline: Delete a tasksof relationship between todo and project which has already been deleted (Error Flow)
    Given the project with id "<projectId>" exists
    And the todo with id "<todoId>" exists
    And there is a tasksof relationship between todo with id "<todoId>" and project with id "<projectId>"
    When I delete the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using put method
    Then a status code "200" with response phrase "OK" is returned
    And the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" is deleted
    When I delete the nonexistent tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using id in endpoint
    Then the response body has the error message "<errorMessage>"
    And a status code "404" with response phrase "Not Found" is returned
    Examples:
      | todoId | projectId | errorMessage                                        |
      | 1      | 1         | Could not find any instances with todos/1/tasksof/1 |
      | 2      | 1         | Could not find any instances with todos/2/tasksof/1 |

  Scenario Outline: Delete a tasksof relationship between nonexistent todo and project by id in endpoint (Error Flow)
    Given the todo with id "<todoId>" does not exist
    And the project with id "<projectId>" exists
    When I delete the nonexistent tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using id in endpoint
    Then the response body has the error message "Cannot invoke \"uk.co.compendiumdev.thingifier.core.domain.instances.ThingInstance.getRelationships()\" because \"parent\" is null"
    And a status code "400" with response phrase "Bad Request" is returned
    Examples:
        | todoId | projectId |
        | 0      | 1         |
        | 100    | 1         |

  Scenario Outline: Delete a tasksof relationship between todo and nonexistent project by id in endpoint (Error Flow)
    Given the todo with id "<todoId>" exists
    And the project with id "<projectId>" does not exist
    When I delete the nonexistent tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using id in endpoint
    Then the response body has the error message "<errorMessage>"
    And a status code "404" with response phrase "Not Found" is returned
    Examples:
      | todoId | projectId | errorMessage                                          |
      | 1      | 100       | Could not find any instances with todos/1/tasksof/100 |
      | 2      | 0         | Could not find any instances with todos/2/tasksof/0   |