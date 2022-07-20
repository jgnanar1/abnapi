package lib.rest;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.utils.DataInputProvider;
import lib.utils.HTMLReporter;

public class PreAndTest extends HTMLReporter{
	
	public String dataFileName, dataFileType;
	protected static String accessToken;
	protected static String projectID;
	
	//Reporting
	@BeforeSuite
	public void beforeSuite() throws FileNotFoundException, IOException {
		startReport();		
		getGitLabAccessToken();
	}

	@BeforeClass
	public void beforeClass() {
		startTestCase(testCaseName, testDescription);		
	}
	
	
	@BeforeMethod
	public void beforeMethod() throws FileNotFoundException, IOException {
		//for reports		
		svcTest = startTestModule(nodes);
		svcTest.assignAuthor(authors);
		svcTest.assignCategory(category);
		
		Properties prop = new Properties();
		prop.load(new FileInputStream(new File("./src/test/resources/config.properties")));
		
		RestAssured.authentication = RestAssured.oauth2(accessToken);
		RestAssured.baseURI = "https://"+prop.getProperty("server")+"/"+prop.getProperty("resources")+"/";
	

	}

	@AfterMethod
	public void afterMethod() {
	}

	@AfterSuite
	public void afterSuite() {
		// report output
		endResult();
	}

	@DataProvider(name="fetchData")
	public  Object[][] getData(){
		if(dataFileType.equalsIgnoreCase("Excel"))
			return DataInputProvider.getSheet(dataFileName);	
		else if(dataFileType.equalsIgnoreCase("JSON")){
			Object[][] data = new Object[1][1];
			data[0][0] = new File("./data/"+dataFileName+"."+dataFileType);
			System.out.println(data[0][0]);
			return data;
		}else {
			return null;
		}
			
	}

	public long takeSnap() {
		return 0;
	}	

	public void getGitLabAccessToken() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
			prop.load(new FileInputStream(new File("./src/test/resources/config.properties")));
		
		Response response =	RestAssured.given()
				.auth().preemptive().basic(prop.getProperty("CLIENT_ID"), prop.getProperty("CLENT_SECRET"))
				.contentType(ContentType.URLENC).log().all()
				.formParam("grant_type", "password")
				.formParam("username", prop.getProperty("username"))
				.formParam("password", prop.getProperty("password"))
				.when()
				.post("https://gitlab.com/oauth/token");
		response.prettyPrint();
		
		JsonPath jsonPath = response.jsonPath();
		accessToken = jsonPath.get("access_token").toString();
		String tokenType = jsonPath.get("token_type").toString();
		System.out.println("Oauth Token with type " + tokenType + "   " + accessToken);
	}
	
}
