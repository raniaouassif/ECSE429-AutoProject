Feature:
  As a user,
  I want to delete a tasksof relationship between a todo and project
  So that I can manage the project's task list.

  Background:
    Given the service is running

  Scenario Outline: Delete a tasksof relationship between todo and project by id in endpoint (Normal Flow)
    Given the todo with id "<todoId>" exists
    And the project with id "<projectId>" exists
    And there is a tasksof relationship between todo with id "<todoId>" and project with id "<projectId>"
    When I delete the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using id in endpoint
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" is deleted
    Examples:
      | todoId  | projectId  | statusCode  | responsePhrase |
      | 2       | 1          | 200         | OK             |
      | 1       | 1          | 200         | OK             |

  Scenario Outline: Delete a tasksof relationship between todo and project using put method (Alternate Flow)
    Given the todo with id "<todoId>" exists
    And the project with id "<projectId>" exists
    And there is a tasksof relationship between todo with id "<todoId>" and project with id "<projectId>"
    When I delete the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using put method
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And the tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" is deleted
    Examples:
      | todoId  | projectId  | statusCode  | responsePhrase |
      | 2       | 1          | 200         | OK             |
      | 1       | 1          | 200         | OK             |

    Scenario Outline: Delete a tasksof relationship between nonexistent todo and project by id in endpoint (Error Flow)
      Given the todo with id "<todoId>" does not exist
      And the project with id "<projectId>" exists
      When I delete the nonexistent tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using id in endpoint
      Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
      And the response body has the error message "<errorMessage>"
      Examples:
        | todoId | projectId | statusCode | responsePhrase | errorMessage                                                                                                                       |
        | 0      | 1         | 400        | Bad Request    | Cannot invoke \"uk.co.compendiumdev.thingifier.core.domain.instances.ThingInstance.getRelationships()\" because \"parent\" is null |
        | 100    | 1         | 400        | Bad Request    | Cannot invoke \"uk.co.compendiumdev.thingifier.core.domain.instances.ThingInstance.getRelationships()\" because \"parent\" is null |

  Scenario Outline: Delete a tasksof relationship between todo and nonexistent project by id in endpoint (Error Flow)
    Given the todo with id "<todoId>" exists
    And the project with id "<projectId>" does not exist
    When I delete the nonexistent tasksof relationship between todo with id "<todoId>" and project with id "<projectId>" using id in endpoint
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And the response body has the error message "<errorMessage>"
    Examples:
      | todoId  | projectId  | statusCode  | responsePhrase | errorMessage                                           |
      | 1       | 100        | 404         | Not Found      | Could not find any instances with todos/1/tasksof/100  |
      | 2       | 0          | 404         | Not Found      | Could not find any instances with todos/2/tasksof/0    |