package de.biniasonline.junit.rules;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShotOnFailure extends TestWatcher {
	
	private WebDriver _browser;
	private File _targetDir;
	
	public ScreenShotOnFailure(WebDriver browser) {
		_browser = browser;
		if (System.getProperty("de.biniasonline.junit.rules.ScreenShotOnFailure.TARGETDIR") != null) {
			_targetDir = new File(System.getProperty("de.biniasonline.junit.rules.ScreenShotOnFailure.TARGETDIR"));
			if (!_targetDir.exists()) {
				_targetDir.mkdir();
			}
		}
	}

	@Override
	protected void failed(Throwable e, Description description) {
		if (_targetDir != null && _targetDir.isDirectory() && _targetDir.canWrite()) {
			TakesScreenshot takesScreenshot = (TakesScreenshot) _browser;			
			File scrFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
	        try {
	        	File resultDir = new File(_targetDir, description.getTestClass().getName());
	        	if (!resultDir.exists()) {
	        		resultDir.mkdir();
	        	}	    
	        	String filename = "classLevel";
	        	if (description.getMethodName() != null) {
	        		filename = description.getMethodName();
	        	}
	        	File screenShot = new File(resultDir, filename + ".png");
	            FileUtils.copyFile(scrFile, screenShot);
	            // log attachment for jenkins junit attachment plugin
	            System.out.println();
	            if (description.getMethodName() != null) {
	            	System.out.println("[[ATTACHMENT|" + screenShot.getAbsolutePath() + "|" + description.getMethodName() + "]]");
	            } else {
	            	System.out.println("[[ATTACHMENT|" + screenShot.getAbsolutePath() + "]]");
	            }
	            System.out.println();
	        } catch (IOException ioe) {
	            throw new RuntimeException(ioe);
	        }
		}
	}		
}
