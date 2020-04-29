package allocator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.core.api.Scenario;

public class AppConnector<V> {

	public static AndroidDriver driver=null;
	public static Properties prop = new Properties();
	ReusableMethods RM= new ReusableMethods();

	public AppConnector(){
		try {
			prop.load( new FileInputStream("./src/test/config/application.properties") );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUpDriver() throws Exception{
		String deviceType = prop.getProperty("deviceType");
		if (deviceType == null) {
			deviceType = "android-tablet";
		}
		switch (deviceType) {
		case "android-tablet":
			DesiredCapabilities androidCapabilities= new DesiredCapabilities();
			androidCapabilities.setCapability("deviceName", prop.getProperty("DeviceName"));
			androidCapabilities.setCapability("udid", prop.getProperty("UDID"));
			androidCapabilities.setCapability("platformVersion", prop.getProperty("PlatformVersion"));
			androidCapabilities.setCapability("platformName", prop.getProperty("PlatformName"));
			androidCapabilities.setCapability("appPackage", prop.getProperty("AppPackage"));
			androidCapabilities.setCapability("appActivity", prop.getProperty("AppActivity"));
			androidCapabilities.setCapability("noReset", "true");
			androidCapabilities.setCapability("fullReset", "false");
			androidCapabilities.setCapability("unicodeKeyboard", "true");
			androidCapabilities.setCapability("resetKeyboard", "true");
			URL url= new URL(prop.getProperty("DeviceURL"));
			driver= new AndroidDriver<MobileElement>(url,androidCapabilities);
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS); 
			System.out.println("###........CONNECTION SUCCESSFULLY ESTABLISHED WITH NODE DEVICE.......###");
		//	captureLog(driver,"Amazon test");
			break;
		default:
			throw new IllegalArgumentException("Device: \"" + deviceType + "\" isn't supported.");
		}
	}
	public static void captureLog(AppiumDriver driver, String testName)
		    throws Exception {
		    DateFormat df = new SimpleDateFormat("dd_MM_yyyy_HH-mm-ss");
		    Date today = Calendar.getInstance().getTime();
		    String reportDate = df.format(today);
		    String logPath = "C:\\automation_capture\\";
		    List<LogEntry> logEntries = driver.manage().logs().get("logcat").filter(Level.ALL);
		    File logFile = new File(logPath + reportDate + "_" + testName + ".txt");
		    PrintWriter log_file_writer = new PrintWriter(logFile);
		    log_file_writer.println(logEntries );
		    log_file_writer.flush();
		    }
		

	public void closeDriver(Scenario scenario){
		if(scenario.isFailed()){
			RM.saveScreenshotsForScenario(scenario);
		}
		if(driver!=null) {
			driver.closeApp();
		}
	}
}
