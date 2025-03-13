Feature: Addition of two integers

  Scenario Outline: Addition of two integer values
    Given the payload request for the add service "<scenarioname>",updated with the data "<requestfilename>"
    When user invoke the add service with "<soapaction>"
#    Then status description should be "<statuscode>"
    Then the reponse recieved has the value "<Expectedvalue>"

    Examples:
      | scenarioname   | requestfilename | soapaction             | statuscode |Expectedvalue|
      | AddTwoIntegers | calculator/Add  | http://tempuri.org/Add | 200        |  8           |