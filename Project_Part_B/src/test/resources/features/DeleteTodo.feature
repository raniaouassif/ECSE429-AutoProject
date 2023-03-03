#Feature: Delete Todo
#  As a user,
#  I want to delete a todo, so I can keep my list up to date.
#
#  Background:
#    Given the service is running
#
#    Scenario Outline: Delete a todo using id (Normal Flow)
#      Given the todo with id "<todoId>" exists in the application
#      When I delete the todo with id "<todoId>"
#      Then a success message "<successMessage>" with status code "<successCode>" is returned
#      And the todo with id "<todoId>" should be deleted
#      Examples:
#        | todoId    | successMessage   | successCode    |
#        | 1         | OK               | 200            |
#        | 2         | OK               | 200            |
#
#    Scenario Outline: Delete a nonexistent todo (Error Flow)
#      Given the todo with id "<todoId>" does not exist
#      When I delete the todo with id "<todoId>"
#      Then an error message "<errorMessage>" with status code "<errorCode>" is returned
#      And no todo is deleted
#      Examples:
#        | todoId    | errorCode      | errorMessage                                |
#        | 0         | 404            | Could not find any instances with todos/0   |
#        | 200       | 404            | Could not find any instances with todos/200 |