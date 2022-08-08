@user-copy-list
Feature: User copy list

  Background:
    Given the following users exist:
      | username              | password  | authorities    |
      | admin@example.com     | password1 | ROLE_ADMIN     |
      | committee@example.com | password2 | ROLE_COMMITTEE |
    And we are authenticated as "admin@example.com"
    And we have created a title with name "Some Title"


  Scenario: The user copy list appears as part of the create library item dialog
    Given we are logged in as "admin@example.com"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    Then we will see an element with id "user_copy_list"


  Scenario Outline:
    Given we are logged in as "<owner>"
    And we have created a copy of "Some Title" belonging to "<owner>"
    When we click the element with id "user_dropdown"
    And we click the element with id "create_copy"
    Then the element with xpath "//table[@id='user_copy_list']/tbody/tr[1]/td[1]" will contain "<owner>"
    And the element with xpath "//table[@id='user_copy_list']/tbody/tr[1]/td[2]" will contain "Some Title"

    Examples:
      | owner                 |
      | admin@example.com     |
      | committee@example.com |
