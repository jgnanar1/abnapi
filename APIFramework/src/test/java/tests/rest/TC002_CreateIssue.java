package tests.rest;

import org.testng.annotations.Test;
import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import lib.rest.RESTAssuredBase;


public class TC002_CreateIssue extends RESTAssuredBase{
	protected static String IID;

	@BeforeTest
	public void setValues() {
		testCaseName = "CreateIssue (REST)";
		testDescription = "Createa a new Issue";
		nodes = "Issue";
		authors = "author Name";
		category = "REST";
	}

	@Test(dependsOnMethods= {"tests.rest.TC001_SearchProject.searchProject"})
	public void createIssue() {		
		HashMap<String,String> queryParams = new HashMap<String,String>();
		queryParams.put("title", "New Issue added using RestAssured");




		HashMap<String,String> pathParms = new HashMap<String,String>();
		pathParms.put("ID", projectID);

		Response response = postWithPathParamAnd(queryParams, pathParms, "projects/{ID}/issues");
		response.prettyPrint();

		verifyContentType(response, "application/json");
		verifyResponseCode(response, 201);
		verifyContentWithKey(response, "title", "New Issue added using RestAssured");
		


	}
}




















