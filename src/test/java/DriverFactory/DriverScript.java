package DriverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.Test;

import CommonFunLibrary.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {

	static WebDriver driver;
	ExtentReports report;
	ExtentTest test;
	
	public void ERP_Account() throws Throwable  {
		
		//creating reference object for excel util methods
		
		ExcelFileUtil xl = new ExcelFileUtil();
		
		//iterating all row in master testcases sheet
		for (int i = 1; i <= xl.rowCount("MasterTestCases"); i++) {
			
			
			if (xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y")) {
				
				//store module name into TCModule
				String TCModule = xl.getCellData("MasterTestCases", i, 1);
				//generate user defined html report
				report = new ExtentReports("./Reports//"+TCModule+FunctionLibrary.generateDate()+".html");
				//iterate all rows in TCModule
				for (int j = 1; j <= xl.rowCount(TCModule); j++) {
					
					//to start test case here
					test=report.startTest(TCModule);
					
					String Description = xl.getCellData(TCModule, j, 0);
					
					String Function_Name = xl.getCellData(TCModule, j, 1);
					
					String Locator_Type = xl.getCellData(TCModule, j, 2);
					
					String Locator_Value = xl.getCellData(TCModule, j, 3);
					
					String Test_Data = xl.getCellData(TCModule, j, 4);
					
					try {
						if (Function_Name.equalsIgnoreCase("startBrowser")) {
							
							driver= FunctionLibrary.startBrowser();
							test.log(LogStatus.INFO, Description);
							
						} else if (Function_Name.equalsIgnoreCase("openApplication")) {
							
							FunctionLibrary.openApplication(driver);
							test.log(LogStatus.INFO, Description);
							
						} else if (Function_Name.equalsIgnoreCase("waitForElement")) {
						FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);	
						test.log(LogStatus.INFO, Description);
							
						}else if (Function_Name.equalsIgnoreCase("typeAction")) {
							FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
							test.log(LogStatus.INFO, Description);
							
						} else if (Function_Name.equalsIgnoreCase("clickAction")) {
							FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
							test.log(LogStatus.INFO, Description);
							
						} else if (Function_Name.equalsIgnoreCase("closeBrowser")) {
							
							FunctionLibrary.closeBrowser(driver);
							test.log(LogStatus.INFO, Description);
							
						}
						else if (Function_Name.equalsIgnoreCase("captureData")) {
							
							FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
							test.log(LogStatus.INFO, Description);
							
						}else if (Function_Name.equalsIgnoreCase("tableValidation")) {
							
							FunctionLibrary.tableValidation(driver, Test_Data);
							test.log(LogStatus.INFO, Description);
							
						}
						
						//write as pass into column
						xl.setCellData(TCModule, j, 5, "PASS");
						xl.setCellData("MasterTestCases", i, 3, "PASS");
						test.log(LogStatus.PASS, Description);
						
					} catch (Throwable t) {
						
						System.out.println("exception handled");
						xl.setCellData(TCModule, j, 5, "FAIL");
						xl.setCellData("MasterTestCases", i, 3, "FAIL");
						test.log(LogStatus.FAIL, Description);
					}
					report.endTest(test);
					report.flush();
					
				}
				
				
			} else {
				
				//write as not executed into status column in master testcases
				
				xl.setCellData("MasterTestCases", i, 3, "Not Executed");

			}
			
			
		}
		
	}
	
	
	
}
