#Author: Raghwen@QATechTesting.com
Feature: Amazon Page Verifications

  @amazon_app
  Scenario: Verify shopping in Amazon Application
  	Given I Launch the Amazon Shopping Application
  	When I Login to the Amazon Shopping Application
  	And I Search for 65 inch TV
  	And I purchase a randon TV from the search result
  	And I collect product information from search page
  	And I add the product to cart
  	And I verify product information from checkout page
  	And I delete the product from cart
  	And I logout from the application