Feature: View Todo Categories Relationship
  As a user,
  I want to view the categories relationships between a todo and categories
  so that I can see which categories are associated with a specific todo

  Background:
    Given the service is running
    And the todo with id "2" exists
    And the category with id "1" exists
    And the category with id "2" exists
    And I add a relationship between todo with id "2" and category with id "1" using id in endpoint
    And I add a relationship between todo with id "2" and category with id "2" using id in endpoint

  Scenario Outline: View todo categories relationships between todo and categories using endpoint todos/:id/categories (Normal Flow)
    Given the todo with id "<todoId>" exists
    When I get the todo's categories with id "<todoId>" using categories endpoint
    Then a status code "200" with response phrase "OK" is returned
    And I should see all the todo related categories with ids "<categoriyIds>"
    Examples:
      | todoId | categoriyIds |
      | 1      | 1            |
      | 2      | 1,2          |

  Scenario Outline: View todo categories relationships between todo and categories using todo endpoint todos/:id (Alternate Flow)
    Given the todo with id "<todoId>" exists
    When I get the todo's categories with id "<todoId>" using todo endpoint
    Then a status code "200" with response phrase "OK" is returned
    And I should see all the todo related categories with ids "<categoriyIds>"
    Examples:
      | todoId | categoriyIds |
      | 1      | 1            |
      | 2      | 1,2          |

  # Fails - Actually returns 200 OK with all categories instead
  Scenario Outline: View the todo categories relationship between nonexistent todo and categories using endpoint todos/:id/categories (Error Flow)
    Given the todo with id "<todoId>" does not exist
    When I get the todo's categories with id "<todoId>" using categories endpoint
    Then a status code "400" with response phrase "Bad Request" is returned
    And the response body has the error message "Cannot invoke \"uk.co.compendiumdev.thingifier.core.domain.instances.ThingInstance.getRelationships()\" because \"parent\" is null"
    Examples:
      | todoId |
      | 0      |
