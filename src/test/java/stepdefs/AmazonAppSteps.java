package stepdefs;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import allocator.ReusableMethods;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import listeners.ExtentReportListener;
import static allocator.ReusableMethods.productDetails;

import java.io.IOException;

public class AmazonAppSteps extends ExtentReportListener {
	ReusableMethods RM= new ReusableMethods();
	String productName_SearchPage;
	String productCost_SearchPage;
	public static String ObjectToken;
	ExtentTest logInfo=null;
	String ProductName_SearchResultsPage;
	
	@Given("^I Launch the Amazon Shopping Application$")
	public void aUserNavigatesToHomePage() throws ClassNotFoundException {
		test = extent.createTest(Feature.class, "Amazon App Verifications");                         
		test=test.createNode(Scenario.class, "Verify Amazon App to buy a product");                       
		logInfo=test.createNode(new GherkinKeyword("Given"), "Verify Amazon Application for purchasing a Television");
		try {
			if(RM.waitForCondition("Presence", "SingInBtn_AppLaunchPage", 10)){
				logInfo.pass("Launched Amazon Shopping Application Successfully");
			}
			else{		
				logInfo.fail("Unable to launch Amazon Shopping Application");
				logInfo.addScreenCaptureFromPath(captureScreenShot(driver));   	
			}
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL",driver,logInfo,e);            
		}
	}

	@When("^I Login to the Amazon Shopping Application$")
	public void userLoginToAmazonApp() {
		try {
			logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
			RM.PerformActionOnElement("SingInBtn_AppLaunchPage","Click","");
			RM.waitForCondition("NotPresence", "ProgressSpinner", 5);
			Thread.sleep(5000);
			String Email=RM.getSpecificColumnData("./src/test/testdata/data.xlsx","sheet1", "Email");
			RM.PerformActionOnElement("EnterEmail_SignInPage","Type",Email);
//			if(RM.waitForCondition("Presence", "ContinueBtn_SignInPage", 5)){
//				RM.PerformActionOnElement("ContinueBtn_SignInPage","Click","");
//			}
			RM.PerformActionOnElement("ShowPasswordChkBox_SignInPage","Click","");
			RM.waitForCondition("NotPresence", "ProgressSpinner", 5);
			String Password=RM.getSpecificColumnData("./src/test/testdata/data.xlsx","sheet1", "Password");
			RM.PerformActionOnElement("EnterPassword_SignInPage","Type",Password);
			RM.PerformActionOnElement("SignInSubmit_SignInPage","Click","");
			RM.waitForCondition("NotPresence", "ProgressSpinner", 5);
			Thread.sleep(5000);
			logInfo.pass("Logged In Successfully");
			logInfo.addScreenCaptureFromPath(captureScreenShot(driver));   
		} catch (AssertionError | Exception e) {
			testStepHandle("FAIL",driver,logInfo,e);            
		}
	}
	@And("^I Search for 65 inch TV$")
	public void searchForTV() throws Exception{
		String SearchItem=RM.getSpecificColumnData("./src/test/testdata/data.xlsx","sheet1", "SearchItem");
		RM.PerformActionOnElement("SearchBox_SearchPage","Type",SearchItem);
		RM.PerformActionOnElement("FirstItemInResult_SearchPage","Click","");
		logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
	}

	@And("^I purchase a randon TV from the search result$")
	public void purchaseRandomTV()
	{
		try {
			RM.PerformActionOnElement("SearchResults_SearchPage","ScrollAndClick","");
			logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("^I collect product information from search page$")	
	public void storeProductInfoFromSearchPage() throws IOException{
		ProductName_SearchResultsPage =productDetails;
		logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
	}

	@And("^I add the product to cart$")	
	public void addToCart(){
		try {
			Thread.sleep(5000);
			if(RM.numberOfElements("BuyingOptionsBtn_ProductDetailsPage")!=0){
				RM.PerformActionOnElement("BuyingOptionsBtn_ProductDetailsPage","ScrollAndClick","");
				RM.PerformActionOnElement("AddToCartBtn_ProductDetailsPage","Click","");
			}
			else{
				RM.PerformActionOnElement("AddToCartBtn_ProductDetailsPage","ScrollAndClick","");
			}
			RM.PerformActionOnElement("CartImg","Click","");
			logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("^I verify product information from checkout page$")	
	public void storeProductInfoFromCheckoutPage(){
		try {
//			test = extent.createTest(Feature.class, "Amazon App Verifications");                         
//			test=test.createNode(Scenario.class, "Verify Amazon App to buy a product");                       
//			logInfo=test.createNode(new GherkinKeyword("And"), "Compare Product Details");
			String ProductName_CheckOutPage=RM.getTextFromElement("SearchResults_SearchPage");
			if(ProductName_CheckOutPage.contains("PowerBridge Solutions")){
				logInfo.pass("Product details Successfully verified");
				logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
			}
			else{
				logInfo.fail("Product details does not match");
				logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("^I delete the product from cart$")	
	public void navigateToCheckout() throws Exception{ 
		RM.PerformActionOnElement("Delete_CheckoutPage","Click","");
		RM.waitForCondition("NotPresence", "ProgressSpinner", 5);
		RM.PerformActionOnElement("ContinueShopping_CheckOutPage","Click","");
		logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
	}

	@And("^I logout from the application$")	
	public void logout() throws Exception
	{
		RM.PerformActionOnElement("AmazonLogo","Click","");
		RM.PerformActionOnElement("Settings_Logout","Click","");
		Thread.sleep(3000);
		RM.PerformActionOnElement("Logout_Popup","Click","");
		RM.waitForCondition("NotPresence", "ProgressSpinner", 5);
		RM.PerformActionOnElement("Sign_out","Click","");
		logInfo.addScreenCaptureFromPath(captureScreenShot(driver));  
	}



}
