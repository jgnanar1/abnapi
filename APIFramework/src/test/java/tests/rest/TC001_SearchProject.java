package tests.rest;

import org.testng.annotations.Test;
import java.util.HashMap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import lib.rest.RESTAssuredBase;


public class TC001_SearchProject extends RESTAssuredBase{

	@BeforeTest
	public void setValues() {
		testCaseName = "SearchProject (REST)";
		testDescription = "Search a an existing Project";
		nodes = "Issue";
		authors = "author Name";
		category = "REST";
	}

	@Test
	public void searchProject() {		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("search", "Gitlabissues");
		
		Response response = getWithqueryParams(map, "projects");
		response.prettyPrint();
		
		verifyContentType(response, "application/json");
		verifyResponseCode(response, 200);
		verifyContentsWithKey(response, "name", "Gitlabissues");
		
		projectID = getFirstContentWithKey(response, "id").toString();
		System.out.println(projectID);
	}


}




















