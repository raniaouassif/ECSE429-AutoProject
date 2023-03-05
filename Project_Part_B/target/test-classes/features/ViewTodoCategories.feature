Feature: View Todo Categories Relationship
  As a user,
  I want to view the categories relationships between a todo and categories
  so that I can see which categories are associated with a specific todo

  Background:
    Given the service is running

  Scenario Outline: View todo categories relationships between todo and categories using endpoint todos/:id/categories (Normal Flow)
    Given the todo with id "<todoId>" exists
    When I get the todo's categories with id "<todoId>" using categories endpoint
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And I should see all the todo related categories with ids "<categoryIds>"
    Examples:
      | todoId  | categoryIds | statusCode  | responsePhrase |
      | 1       | 1           | 200         | OK             |

  Scenario Outline: View todo categories relationships between todo and categories using todo endpoint todos/:id (Alternate Flow)
    Given the todo with id "<todoId>" exists
    When I get the todo's categories with id "<todoId>" using todo endpoint
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And I should see all the todo related categories with ids "<categoryIds>"
    Examples:
      | todoId  | categoryIds | statusCode  | responsePhrase |
      | 1       | 1           | 200         | OK             |

  # Fails - Actually returns 200 OK with all categories instead
  Scenario Outline: View the todo categories relationship between nonexistent todo and categories using endpoint todos/:id/categories (Error Flow)
    Given the todo with id "<todoId>" does not exist
    When I get the todo's categories with id "<todoId>" using categories endpoint
    Then a status code "<statusCode>" with response phrase "<responsePhrase>" is returned
    And the response body has the error message "<errorMessage>"
    Examples:
      | todoId | statusCode | responsePhrase | errorMessage                                                                                                                       |
      | 0      | 400        | Bad Request    | Cannot invoke \"uk.co.compendiumdev.thingifier.core.domain.instances.ThingInstance.getRelationships()\" because \"parent\" is null |