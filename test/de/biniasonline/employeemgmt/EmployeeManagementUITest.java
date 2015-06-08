package de.biniasonline.employeemgmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import com.safaribooks.junitattachments.CaptureFile;
import com.safaribooks.junitattachments.RecordAttachmentRule;

import de.biniasonline.employeemgmt.ui.EmployeesView;
import de.biniasonline.employeemgmt.ui.SkillsView;
import de.biniasonline.employeemgmt.ui.WelcomeView;
import de.biniasonline.junit.rules.ScreenShotOnFailure;

public class EmployeeManagementUITest {

	private static String APPURL = "http://";
	static {
		APPURL += System.getProperty("de.biniasonline.employeemgmt.TEST_HOST", "localhost");
		APPURL += ":" + System.getProperty("de.biniasonline.employeemgmt.TEST_PORT", "8080");
		APPURL += "/EmployeeManagement/";
	}
	
	private static FirefoxDriver _driver;
	
	@Rule
	public ScreenShotOnFailure screenShotRule = new ScreenShotOnFailure(_driver);
	
//	@Rule
//	public RecordAttachmentRule rule = new RecordAttachmentRule(this);
//
//	@CaptureFile(extension="png")
//	public byte[] capturePage = null;

	@BeforeClass
	public static void setUp() throws Exception {
		_driver = new FirefoxDriver();
		_driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		_driver.get(APPURL);
	}
	
	@Test
	public void shouldDisplayWelcomePage() {
		_driver.get(APPURL);
		WebElement element = _driver.findElementById(WelcomeView.ID_LABEL_WELCOME);
		assertEquals("Welcome-Page displays 'Welcome'", "Welcome", element.getText());
	}
	
	@Test
	public void canNavigateToEmployees() {
		//_driver.get(APPURL);
		boolean clicked = false;
		for (WebElement element : _driver.findElementsByCssSelector(".nav .v-button-caption")) {
			if (element.getText().equals("Employees")) {
				element.click();
				clicked = true;
				break;
			}
		}
		assertTrue("Clicked on button 'Employees'", clicked);
		waitForView(EmployeesView.VIEW_NAME);
		
		WebElement headline = _driver.findElementByCssSelector(".content .v-label");
		assertNotNull("View headline found", headline);
		assertEquals("View 'Employees' is shown.", "Employees", headline.getText());
	}
	
	@Test
	public void canNavigateToSkills() {
		//_driver.get(APPURL);
		boolean clicked = false;
		for (WebElement element : _driver.findElementsByCssSelector(".nav .v-button-caption")) {
			if (element.getText().equals("Skills")) {
				element.click();
				clicked = true;
				break;
			}
		}
		assertTrue("Clicked on button 'Skills'", clicked);
		waitForView(SkillsView.VIEW_NAME);
		
		WebElement headline = _driver.findElementByCssSelector(".content .v-label");
		assertNotNull("View headline found", headline);
		assertEquals("View 'Skills' is shown.", "Skills", headline.getText());
	}
	
//	@After
//    public void cleanupThread() {
//        if (_driver != null) { 
//            // capture a screenshot
//            if (_driver instanceof TakesScreenshot) {
//                capturePage = ((TakesScreenshot) _driver)
//                        .getScreenshotAs(OutputType.BYTES);
//            }
//        }
//    }
	
	@AfterClass
	public static void shutDown() throws Exception {
		_driver.quit();
	}
	
	private void waitForView(final String viewname) {
		WebDriverWait w = new WebDriverWait(_driver, 10);
		w.pollingEvery(100, TimeUnit.MILLISECONDS);
		w.until(new Predicate<WebDriver>() {			
			@Override
			public boolean apply(WebDriver input) {
				return _driver.findElementsById("C_VIEW." + viewname).size() > 0;
			}
		});

	}

}
